package com.xh.lesson.service.impl;

import com.xh.lesson.entity.SysUserRole;
import com.xh.lesson.exception.BusinessException;
import com.xh.lesson.exception.code.BaseResponseCode;
import com.xh.lesson.mapper.SysUserRoleMapper;
import com.xh.lesson.service.UserRoleService;
import com.xh.lesson.vo.req.UserRoleOperationReqVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @ClassName: UserRoleServiceImpl
 * TODO:类文件简单描述
 * @Author: 小霍
 * @CreateDate: 2019/9/19 11:42
 * @UpdateUser: 小霍
 * @UpdateDate: 2019/9/19 11:42
 * @Version: 0.0.1
 */
@Service
public class UserRoleServiceImpl implements UserRoleService {
    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;

    @Override
    public int removeByRoleId(String roleId) {
        return sysUserRoleMapper.removeByRoleId(roleId);
    }

    @Override
    public List<String> getRoleIdsByUserId(String userId) {
        return sysUserRoleMapper.getRoleIdsByUserId(userId);
    }



    @Transactional(rollbackFor = Exception.class)
    @Override
    public void addUserRoleInfo(UserRoleOperationReqVO vo) {
        if(vo.getRoleIds()==null||vo.getRoleIds().isEmpty()){
            return;
        }
        Date createTime=new Date();
        List<SysUserRole> list=new ArrayList<>();
        for (String roleId:vo.getRoleIds()){
            SysUserRole sysUserRole=new SysUserRole();
            sysUserRole.setId(UUID.randomUUID().toString());
            sysUserRole.setCreateTime(createTime);
            sysUserRole.setUserId(vo.getUserId());
            sysUserRole.setRoleId(roleId);
            list.add(sysUserRole);
        }
        sysUserRoleMapper.removeByUserId(vo.getUserId());
        int count=sysUserRoleMapper.batchUserRole(list);
        if (count==0){
            throw new BusinessException(BaseResponseCode.OPERATION_ERRO);
        }
    }

    @Override
    public int removeByUserId(String userId) {

        return sysUserRoleMapper.removeByUserId(userId);
    }

    @Override
    public List<String> getUserIdsByRoleIds(List<String> roleIds) {

        return sysUserRoleMapper.getUserIdsByRoleIds(roleIds);
    }
}
