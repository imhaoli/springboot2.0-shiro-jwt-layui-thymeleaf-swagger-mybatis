package com.xh.lesson.service;

import com.xh.lesson.entity.SysLog;
import com.xh.lesson.vo.req.SysLogPageReqVO;
import com.xh.lesson.vo.resp.PageVO;

import java.util.List;

/**
 * @ClassName: LogService
 * TODO:类文件简单描述
 * @Author: 小霍
 * @CreateDate: 2019/9/23 16:17
 * @UpdateUser: 小霍
 * @UpdateDate: 2019/9/23 16:17
 * @Version: 0.0.1
 */
public interface LogService {

    PageVO<SysLog> pageInfo(SysLogPageReqVO vo);

    void deleted(List<String> logIds);
}
