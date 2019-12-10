package com.xh.lesson.mapper;

import com.xh.lesson.entity.SysUser;
import com.xh.lesson.vo.req.UserPageReqVO;
import io.swagger.annotations.ApiParam;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysUserMapper {
    int deleteByPrimaryKey(String id);

    int insert(SysUser record);

    int insertSelective(SysUser record);

    SysUser selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(SysUser record);

    int updateByPrimaryKey(SysUser record);

    SysUser getUserInfoByName(String username);

    List<SysUser> selectAll(UserPageReqVO vo);

    List<SysUser> selectUserInfoByDeptIds (List<String> deptIds);

    List<SysUser> getUserListByDeptId(String deptId);

    int deletedUsers(@Param("sysUser") SysUser sysUser,@Param("list") List<String> list);
}