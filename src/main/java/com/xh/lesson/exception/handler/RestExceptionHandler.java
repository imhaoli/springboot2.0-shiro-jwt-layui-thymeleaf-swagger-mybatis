package com.xh.lesson.exception.handler;

import com.xh.lesson.exception.BusinessException;
import com.xh.lesson.exception.code.BaseResponseCode;
import com.xh.lesson.utils.DataResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * @ClassName: RestExceptionHandler
 * controller 层全局异常统一处理类
 * @Author: 小霍
 * @CreateDate: 2019/9/4 15:55
 * @UpdateUser: 小霍
 * @UpdateDate: 2019/9/4 15:55
 * @Version: 0.0.1
 */
@RestControllerAdvice
@Slf4j
public class RestExceptionHandler {

    /**
     * 系统繁忙，请稍候再试"
     * @Author:      小霍
     * @CreateDate:  2019/9/4 23:25
     * @UpdateUser:
     * @UpdateDate:  2019/9/4 23:25
     * @Version:     0.0.1
     * @param e
     * @return       com.xh.lesson.utils.DataResult<T>
     * @throws
     */
    @ExceptionHandler(Exception.class)
    public <T>DataResult<T> handleException(Exception e){
        log.error("Exception,exception:{}", e);
        return DataResult.getResult(BaseResponseCode.SYSTEM_BUSY);
    }

    /**
     * 自定义全局异常处理
     * @Author:      小霍
     * @CreateDate:  2019/9/4 16:39
     * @UpdateUser:
     * @UpdateDate:  2019/9/4 16:39
     * @Version:     0.0.1
     * @param e
     * @return       com.xh.lesson.utils.DataResult<T>
     * @throws
     */
    @ExceptionHandler(value = BusinessException.class)
    <T> DataResult<T> businessExceptionHandler(BusinessException e) {
        log.error("BusinessException,exception:{}", e);
        return new DataResult<>(e.getMessageCode(),e.getDetailMessage());
    }
    /**
     * 没有权限 返回403视图
     * @Author:      小霍
     * @CreateDate:  2019/10/29 15:14
     * @UpdateUser:
     * @UpdateDate:  2019/10/29 15:14
     * @Version:     0.0.1
     * @param
     * @return       org.springframework.web.servlet.ModelAndView
     * @throws
     */
    @ExceptionHandler(value = AuthorizationException.class)
    public <T> DataResult<T>  erroPermission(AuthorizationException e){
        log.error("BusinessException,exception:{}", e);
        return new DataResult<>(BaseResponseCode.UNAUTHORIZED_ERROR);

    }
    /**
     * 处理validation 框架异常
     * @Author:      huoth
     * @CreateDate:  2019/1/5 16:01
     * @UpdateUser:
     * @UpdateDate:  2019/1/5 16:01
     * @Version:     0.0.1
     * @param e
     * @return       com.hth.cloud.common.base.HgResult<T>
     * @throws
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    <T> DataResult<T> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        log.error("methodArgumentNotValidExceptionHandler bindingResult.allErrors():{},exception:{}", e.getBindingResult().getAllErrors(), e);
        List<ObjectError> errors = e.getBindingResult().getAllErrors();
        return createValidExceptionResp(errors);
    }
    private <T> DataResult<T> createValidExceptionResp(List<ObjectError> errors) {
        String[] msgs = new String[errors.size()];
        int i = 0;
        for (ObjectError error : errors) {
            msgs[i] = error.getDefaultMessage();
            log.info("msg={}",msgs[i]);
            i++;
        }
        return DataResult.getResult(BaseResponseCode.METHODARGUMENTNOTVALIDEXCEPTION.getCode(), msgs[0]);
    }



}
