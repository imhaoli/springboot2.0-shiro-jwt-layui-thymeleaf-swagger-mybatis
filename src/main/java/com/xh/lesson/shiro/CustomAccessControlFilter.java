package com.xh.lesson.shiro;

import com.alibaba.fastjson.JSON;
import com.xh.lesson.constants.Constant;
import com.xh.lesson.exception.BusinessException;
import com.xh.lesson.exception.code.BaseResponseCode;
import com.xh.lesson.utils.DataResult;
import com.xh.lesson.utils.HttpContextUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.util.StringUtils;
import org.apache.shiro.mgt.SecurityManager;


import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;

/**
 * @ClassName: CustomAccessControlFilter
 * TODO:类文件简单描述
 * @Author: 小霍
 * @CreateDate: 2019/9/6 23:22
 * @UpdateUser: 小霍
 * @UpdateDate: 2019/9/6 23:22
 * @Version: 0.0.1
 */
@Slf4j
public class CustomAccessControlFilter extends AccessControlFilter {
    @Override
    protected boolean isAccessAllowed(ServletRequest servletRequest, ServletResponse servletResponse, Object o) {
        return false;
    }
    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws IOException, ServletException {
        HttpServletRequest request= (HttpServletRequest) servletRequest;
        try {
            Subject subject=getSubject(servletRequest,servletResponse);
            System.out.println(subject.isAuthenticated()+"");

            System.out.println(HttpContextUtils.isAjaxRequest(request));
            log.info(request.getMethod());
            log.info(request.getRequestURL().toString());
            String token=request.getHeader(Constant.ACCESS_TOKEN);
            if(StringUtils.isEmpty(token)){
                throw new BusinessException(BaseResponseCode.TOKEN_ERROR);
            }
            CustomPasswordToken customPasswordToken=new CustomPasswordToken(token);
            getSubject(servletRequest, servletResponse).login(customPasswordToken);
        }catch (BusinessException exception){
            if(HttpContextUtils.isAjaxRequest(request)){
                customRsponse(exception.getMessageCode(),exception.getDetailMessage(),servletResponse);
            }else if(exception.getMessageCode()==BaseResponseCode.TOKEN_ERROR.getCode()){
                servletRequest.getRequestDispatcher("/index/login").forward(servletRequest,servletResponse);
            }else if(exception.getMessageCode()==BaseResponseCode.UNAUTHORIZED_ERROR.getCode()){
                servletRequest.getRequestDispatcher("/index/403").forward(servletRequest,servletResponse);
            }else {
                servletRequest.getRequestDispatcher("/index/500").forward(servletRequest,servletResponse);
            }
            return false;
        } catch (AuthenticationException e){
            if(HttpContextUtils.isAjaxRequest(request)){
                if(e.getCause() instanceof BusinessException){
                    BusinessException exception= (BusinessException) e.getCause();
                    customRsponse(exception.getMessageCode(),exception.getDetailMessage(),servletResponse);
                }else {
                    customRsponse(BaseResponseCode.SYSTEM_BUSY.getCode(),BaseResponseCode.SYSTEM_BUSY.getMsg(),servletResponse);
                }
            }else {
                servletRequest.getRequestDispatcher("/index/403").forward(servletRequest,servletResponse);
            }
            return false;
        }catch (Exception e) {
            if(HttpContextUtils.isAjaxRequest(request)){
                if(e.getCause() instanceof BusinessException){
                    BusinessException exception= (BusinessException) e.getCause();
                    customRsponse(exception.getMessageCode(),exception.getDetailMessage(),servletResponse);
                }else {
                    customRsponse(BaseResponseCode.SYSTEM_BUSY.getCode(),BaseResponseCode.SYSTEM_BUSY.getMsg(),servletResponse);
                }
            }else {
                servletRequest.getRequestDispatcher("/index/500").forward(servletRequest,servletResponse);
            }
            return false;
        }
        return true;
    }

    private void customRsponse(int code,String msg,ServletResponse response){
        try {
            DataResult result = DataResult.getResult(code,msg);

            response.setContentType("application/json; charset=utf-8");
            response.setCharacterEncoding("UTF-8");

            String userJson = JSON.toJSONString(result);
            OutputStream out = response.getOutputStream();
            out.write(userJson.getBytes("UTF-8"));
            out.flush();
        } catch (IOException e) {
            log.error("eror={}",e);
        }
    }

}
