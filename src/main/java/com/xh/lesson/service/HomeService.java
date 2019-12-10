package com.xh.lesson.service;

import com.xh.lesson.vo.resp.HomeRespVO;

/**
 * @ClassName: HomeService
 * TODO:类文件简单描述
 * @Author: 小霍
 * @CreateDate: 2019/10/25 21:27
 * @UpdateUser: 小霍
 * @UpdateDate: 2019/10/25 21:27
 * @Version: 0.0.1
 */
public interface HomeService {

    HomeRespVO getHomeInfo(String userId);
}
