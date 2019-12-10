package com.xh.lesson.service;

import com.xh.lesson.entity.SysDept;
import com.xh.lesson.entity.SysUser;
import com.xh.lesson.vo.req.DeptAddReqVO;
import com.xh.lesson.vo.req.DeptPageReqVO;
import com.xh.lesson.vo.req.DeptUpdateReqVO;
import com.xh.lesson.vo.req.UserPageUserByDeptReqVO;
import com.xh.lesson.vo.resp.DeptRespNodeVO;
import com.xh.lesson.vo.resp.PageVO;

import java.util.List;

/**
 * @ClassName: DeptService
 * TODO:类文件简单描述
 * @Author: 小霍
 * @CreateDate: 2019/9/19 11:38
 * @UpdateUser: 小霍
 * @UpdateDate: 2019/9/19 11:38
 * @Version: 0.0.1
 */
public interface DeptService {

    SysDept addDept(DeptAddReqVO vo);

    void updateDept(DeptUpdateReqVO vo);

    SysDept detailInfo(String id);

    void deleted(String id);

    PageVO<SysDept> pageInfo(DeptPageReqVO vo);


    List<DeptRespNodeVO> deptTreeList(String deptId);

    PageVO<SysUser> pageDeptUserInfo(UserPageUserByDeptReqVO vo);

    List<SysDept> selectAll();
}
