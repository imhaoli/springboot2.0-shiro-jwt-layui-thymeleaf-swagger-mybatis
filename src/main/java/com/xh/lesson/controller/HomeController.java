package com.xh.lesson.controller;

import com.xh.lesson.service.HomeService;
import com.xh.lesson.utils.DataResult;
import com.xh.lesson.utils.JwtTokenUtil;
import com.xh.lesson.vo.resp.HomeRespVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @ClassName: HomeController
 * TODO:类文件简单描述
 * @Author: 小霍
 * @CreateDate: 2019/10/25 21:20
 * @UpdateUser: 小霍
 * @UpdateDate: 2019/10/25 21:20
 * @Version: 0.0.1
 */
@RestController
@RequestMapping("/sys")
@Api(tags = "首页数据")
public class HomeController {
    @Autowired
    private HomeService homeService;

    @GetMapping("/home")
    @ApiOperation(value ="获取首页数据接口")
    public DataResult<HomeRespVO> getHomeInfo(HttpServletRequest request){
        String accessToken=request.getHeader("authorization");
        /**
         * 通过access_token拿userId
         */
        String userId= JwtTokenUtil.getUserId(accessToken);
        DataResult<HomeRespVO> result=DataResult.success();
        result.setData(homeService.getHomeInfo(userId));
        return result;
    }
}
