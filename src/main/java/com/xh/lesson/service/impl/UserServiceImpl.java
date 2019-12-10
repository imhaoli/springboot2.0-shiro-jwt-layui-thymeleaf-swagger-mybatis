package com.xh.lesson.service.impl;

import com.github.pagehelper.PageHelper;
import com.xh.lesson.constants.Constant;
import com.xh.lesson.entity.SysDept;
import com.xh.lesson.entity.SysRole;
import com.xh.lesson.entity.SysUser;
import com.xh.lesson.exception.BusinessException;
import com.xh.lesson.exception.code.BaseResponseCode;
import com.xh.lesson.mapper.SysDeptMapper;
import com.xh.lesson.mapper.SysUserMapper;
import com.xh.lesson.service.*;
import com.xh.lesson.utils.JwtTokenUtil;
import com.xh.lesson.utils.PageUtils;
import com.xh.lesson.utils.PasswordUtils;
import com.xh.lesson.utils.TokenSettings;
import com.xh.lesson.vo.req.*;
import com.xh.lesson.vo.resp.LoginRespVO;
import com.xh.lesson.vo.resp.PageVO;
import com.xh.lesson.vo.resp.UserOwnRoleRespVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName: UserServiceImpl
 * TODO:类文件简单描述
 * @Author: 小霍
 * @CreateDate: 2019/9/7 22:56
 * @UpdateUser: 小霍
 * @UpdateDate: 2019/9/7 22:56
 * @Version: 0.0.1
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private RedisService redisService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private PermissionService permissionService;
    @Autowired
    private UserRoleService userRoleService;
    @Autowired
    private TokenSettings tokenSettings;
    @Autowired
    private SysDeptMapper sysDeptMapper;
    @Override
    public String register(RegisterReqVO vo) {
        SysUser sysUser=new SysUser();
        BeanUtils.copyProperties(vo,sysUser);
        sysUser.setSalt(PasswordUtils.getSalt());
        String encode = PasswordUtils.encode(vo.getPassword(), sysUser.getSalt());
        sysUser.setPassword(encode);
        sysUser.setId(UUID.randomUUID().toString());
        sysUser.setCreateTime(new Date());
        int i = sysUserMapper.insertSelective(sysUser);
        if (i!=1){
            throw new BusinessException(BaseResponseCode.OPERATION_ERRO);
        }
        return sysUser.getId();
    }

    @Override
    public LoginRespVO login(LoginReqVO vo) {
        SysUser sysUser=sysUserMapper.getUserInfoByName(vo.getUsername());
        if (null==sysUser){
            throw new BusinessException(BaseResponseCode.NOT_ACCOUNT);
        }
        if (sysUser.getStatus()==2){
            throw new BusinessException(BaseResponseCode.USER_LOCK);
        }
        if(!PasswordUtils.matches(sysUser.getSalt(),vo.getPassword(),sysUser.getPassword())){
            throw new BusinessException(BaseResponseCode.PASSWORD_ERROR);
        }
        LoginRespVO respVO=new LoginRespVO();
        BeanUtils.copyProperties(sysUser,respVO);
        Map<String,Object> claims=new HashMap<>();
        claims.put(Constant.JWT_PERMISSIONS_KEY,getPermissionsByUserId(sysUser.getId()));
        claims.put(Constant.JWT_ROLES_KEY,getRolesByUserId(sysUser.getId()));
        claims.put(Constant.JWT_USER_NAME,sysUser.getUsername());
        String access_token=JwtTokenUtil.getAccessToken(sysUser.getId(),claims);
        String refresh_token;
        if(vo.getType().equals("1")){
            refresh_token=JwtTokenUtil.getRefreshToken(sysUser.getId(),claims);
        }else {
            refresh_token=JwtTokenUtil.getRefreshAppToken(sysUser.getId(),claims);
        }
        respVO.setAccessToken(access_token);
        respVO.setRefreshToken(refresh_token);
        return respVO;
    }

    private List<String> getRolesByUserId(String userId){
       return  roleService.getRoleNames(userId);
    }

    private Set<String>getPermissionsByUserId(String userId){
        return  permissionService.getPermissionsByUserId(userId);
    }

    @Override
    public String refreshToken(String refreshToken,String accessToken) {

        if(redisService.hasKey(Constant.JWT_ACCESS_TOKEN_BLACKLIST+refreshToken)||!JwtTokenUtil.validateToken(refreshToken)){
            throw new BusinessException(BaseResponseCode.TOKEN_ERROR);
        }
        String userId=JwtTokenUtil.getUserId(refreshToken);
        log.info("userId={}",userId);
        SysUser sysUser=sysUserMapper.selectByPrimaryKey(userId);
        if (null==sysUser){
            throw new BusinessException(BaseResponseCode.TOKEN_PARSE_ERROR);
        }
        Map<String,Object> claims=null;

        if(redisService.hasKey(Constant.JWT_REFRESH_KEY+userId)){
            claims=new HashMap<>();
            claims.put(Constant.JWT_ROLES_KEY,getRolesByUserId(userId));
            claims.put(Constant.JWT_PERMISSIONS_KEY,getPermissionsByUserId(userId));
        }
        String newAccessToken=JwtTokenUtil.refreshToken(refreshToken,claims);

        redisService.setifAbsen(Constant.JWT_REFRESH_STATUS+accessToken,userId,1,TimeUnit.MINUTES);


        if(redisService.hasKey(Constant.JWT_REFRESH_KEY+userId)){
            redisService.set(Constant.JWT_REFRESH_IDENTIFICATION+newAccessToken,userId,redisService.getExpire(Constant.JWT_REFRESH_KEY+userId,TimeUnit.MILLISECONDS),TimeUnit.MILLISECONDS);
        }
        return newAccessToken;
    }

    @Override
    public void updateUserInfo(UserUpdateReqVO vo,String operationId) {

        SysUser sysUser=sysUserMapper.selectByPrimaryKey(vo.getId());
        if (null==sysUser){
            log.error("传入 的 id:{}不合法",vo.getId());
            throw new BusinessException(BaseResponseCode.DATA_ERROR);
        }
        BeanUtils.copyProperties(vo,sysUser);
        sysUser.setUpdateTime(new Date());
        if(!StringUtils.isEmpty(vo.getPassword())){
            String newPassword=PasswordUtils.encode(vo.getPassword(),sysUser.getSalt());
            sysUser.setPassword(newPassword);
        }else {
            sysUser.setPassword(null);
        }
        sysUser.setUpdateId(operationId);
        int count= sysUserMapper.updateByPrimaryKeySelective(sysUser);
        if (count!=1){
            throw new BusinessException(BaseResponseCode.OPERATION_ERRO);
        }
       setUserOwnRole(sysUser.getId(),vo.getRoleIds());
        if(vo.getStatus()==2){
            redisService.set(Constant.ACCOUNT_LOCK_KEY+sysUser.getId(),sysUser.getId());
        }else {
            redisService.delete(Constant.ACCOUNT_LOCK_KEY+sysUser.getId());
        }

    }

    @Override
    public PageVO<SysUser> pageInfo(UserPageReqVO vo) {
        PageHelper.startPage(vo.getPageNum(),vo.getPageSize());
        List<SysUser> sysUsers = sysUserMapper.selectAll(vo);
        if(!sysUsers.isEmpty()){
            for (SysUser sysUser:sysUsers){
                SysDept sysDept = sysDeptMapper.selectByPrimaryKey(sysUser.getDeptId());
                if (sysDept!=null){
                    sysUser.setDeptName(sysDept.getName());
                }
            }
        }
        return PageUtils.getPageVO(sysUsers);
    }

    @Override
    public SysUser detailInfo(String userId) {

        return sysUserMapper.selectByPrimaryKey(userId);
    }

    @Override
    public PageVO<SysUser> selectUserInfoByDeptIds(int pageNum, int pageSize,List<String> deptIds) {

        PageHelper.startPage(pageNum,pageSize);
        List<SysUser> list=sysUserMapper.selectUserInfoByDeptIds(deptIds);
        return PageUtils.getPageVO(list);
    }

    @Override
    public void addUser(UserAddReqVO vo) {
        SysUser sysUser=new SysUser();
        BeanUtils.copyProperties(vo,sysUser);
        sysUser.setSalt(PasswordUtils.getSalt());
        String encode = PasswordUtils.encode(vo.getPassword(), sysUser.getSalt());
        sysUser.setPassword(encode);
        sysUser.setId(UUID.randomUUID().toString());
        sysUser.setCreateTime(new Date());
        int i = sysUserMapper.insertSelective(sysUser);
        if (i!=1){
            throw new BusinessException(BaseResponseCode.OPERATION_ERRO);
        }
        if(null!=vo.getRoleIds()&&!vo.getRoleIds().isEmpty()){
            UserRoleOperationReqVO reqVO=new UserRoleOperationReqVO();
            reqVO.setUserId(sysUser.getId());
            reqVO.setRoleIds(vo.getRoleIds());
            userRoleService.addUserRoleInfo(reqVO);
        }
    }

    @Override
    public void logout(String accessToken, String refreshToken) {
        if(StringUtils.isEmpty(accessToken)||StringUtils.isEmpty(refreshToken)){
            throw new BusinessException(BaseResponseCode.DATA_ERROR);
        }
        Subject subject = SecurityUtils.getSubject();
        log.info("subject.getPrincipals()={}",subject.getPrincipals());
        if (subject.isAuthenticated()) {
            subject.logout();
        }
        String userId=JwtTokenUtil.getUserId(accessToken);

        redisService.set(Constant.JWT_REFRESH_TOKEN_BLACKLIST+accessToken,userId,JwtTokenUtil.getRemainingTime(accessToken),TimeUnit.MILLISECONDS);

        redisService.set(Constant.JWT_REFRESH_TOKEN_BLACKLIST+refreshToken,userId,JwtTokenUtil.getRemainingTime(refreshToken),TimeUnit.MILLISECONDS);


        redisService.delete(Constant.IDENTIFY_CACHE_KEY+userId);
    }

    @Override
    public void updatePwd(UpdatePasswordReqVO vo,String userId,String accessToken, String refreshToken) {

        SysUser sysUser=sysUserMapper.selectByPrimaryKey(userId);
        if(sysUser==null){
            throw new BusinessException(BaseResponseCode.DATA_ERROR);
        }
        if(!PasswordUtils.matches(sysUser.getSalt(),vo.getOldPwd(),sysUser.getPassword())){
            throw new BusinessException(BaseResponseCode.OLD_PASSWORD_ERROR);
        }
        sysUser.setUpdateTime(new Date());
        sysUser.setPassword(PasswordUtils.encode(vo.getNewPwd(),sysUser.getSalt()));
        int i = sysUserMapper.updateByPrimaryKeySelective(sysUser);
        if(i!=1){
            throw new BusinessException(BaseResponseCode.OPERATION_ERRO);
        }

        redisService.set(Constant.JWT_REFRESH_TOKEN_BLACKLIST+accessToken,userId,JwtTokenUtil.getRemainingTime(accessToken),TimeUnit.MILLISECONDS);

        redisService.set(Constant.JWT_REFRESH_TOKEN_BLACKLIST+refreshToken,userId,JwtTokenUtil.getRemainingTime(refreshToken),TimeUnit.MILLISECONDS);


        redisService.delete(Constant.IDENTIFY_CACHE_KEY+userId);

    }

    @Override
    public List<SysUser> getUserListByDeptId(String deptId) {
        return sysUserMapper.getUserListByDeptId(deptId);
    }

    @Override
    public List<SysUser> getUserListByDeptIds(List<String> deptIds) {
        return sysUserMapper.selectUserInfoByDeptIds(deptIds);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deletedUsers(List<String> userIds,String operationId) {
        SysUser sysUser=new SysUser();
        sysUser.setUpdateId(operationId);
        sysUser.setUpdateTime(new Date());
        sysUser.setDeleted(0);
        int i = sysUserMapper.deletedUsers(sysUser,userIds);
        if (i==0){
            throw new BusinessException(BaseResponseCode.OPERATION_ERRO);
        }

        for (String userId:userIds){
            redisService.set(Constant.DELETED_USER_KEY+userId,userId,tokenSettings.getRefreshTokenExpireAppTime().toMillis(),TimeUnit.MILLISECONDS);
            //清空权鉴缓存
            redisService.delete(Constant.IDENTIFY_CACHE_KEY+userId);
        }

    }

    @Override
    public UserOwnRoleRespVO getUserOwnRole(String userId) {
        List<String> roleIdsByUserId = userRoleService.getRoleIdsByUserId(userId);
        List<SysRole> list = roleService.selectAllRoles();
        UserOwnRoleRespVO vo=new UserOwnRoleRespVO();
        vo.setAllRole(list);
        vo.setOwnRoles(roleIdsByUserId);
        return vo;
    }

    @Override
    public void setUserOwnRole(String userId, List<String> roleIds) {

        UserRoleOperationReqVO reqVO=new UserRoleOperationReqVO();
        reqVO.setUserId(userId);
        reqVO.setRoleIds(roleIds);
        userRoleService.addUserRoleInfo(reqVO);
        redisService.set(Constant.JWT_REFRESH_KEY+userId,userId,tokenSettings.getAccessTokenExpireTime().toMillis(), TimeUnit.MILLISECONDS);
        //清空权鉴缓存
        redisService.delete(Constant.IDENTIFY_CACHE_KEY+userId);
    }
}
