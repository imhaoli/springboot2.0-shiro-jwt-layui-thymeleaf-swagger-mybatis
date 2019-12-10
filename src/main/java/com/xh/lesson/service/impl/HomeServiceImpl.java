package com.xh.lesson.service.impl;

import com.xh.lesson.entity.SysDept;
import com.xh.lesson.entity.SysUser;
import com.xh.lesson.service.DeptService;
import com.xh.lesson.service.HomeService;
import com.xh.lesson.service.PermissionService;
import com.xh.lesson.service.UserService;
import com.xh.lesson.vo.resp.HomeRespVO;
import com.xh.lesson.vo.resp.PermissionRespNode;
import com.xh.lesson.vo.resp.UserInfoRespVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * @ClassName: HomeServiceImpl
 * TODO:类文件简单描述
 * @Author: 小霍
 * @CreateDate: 2019/10/25 21:27
 * @UpdateUser: 小霍
 * @UpdateDate: 2019/10/25 21:27
 * @Version: 0.0.1
 */
@Service
public class HomeServiceImpl implements HomeService {
    @Autowired
    private UserService userService;
    @Autowired
    private DeptService deptService;
    @Autowired
    private PermissionService permissionService;

    @Override
    public HomeRespVO getHomeInfo(String userId) {


        SysUser sysUser=userService.detailInfo(userId);
        UserInfoRespVO vo=new UserInfoRespVO();

        if(sysUser!=null){
            BeanUtils.copyProperties(sysUser, vo);
            SysDept sysDept = deptService.detailInfo(sysUser.getDeptId());
            if(sysDept!=null){
                vo.setDeptId(sysDept.getId());
                vo.setDeptName(sysDept.getName());
            }

        }

        List<PermissionRespNode> menus = permissionService.permissionTreeList(userId);

        HomeRespVO respVO=new HomeRespVO();
        respVO.setMenus(menus);
        respVO.setUserInfo(vo);

        return respVO;
    }
}
