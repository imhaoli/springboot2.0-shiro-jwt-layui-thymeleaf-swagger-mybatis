package com.xh.lesson.utils;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * http上下文
 *
 */
public class HttpContextUtils {

	public static HttpServletRequest getHttpServletRequest() {
		return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
	}
	/**
	 * 判断是否是 ajax/app请求
	 * @Author:      小霍
	 * @CreateDate:  2019/11/6 14:42
	 * @UpdateUser:
	 * @UpdateDate:  2019/11/6 14:42
	 * @Version:     0.0.1
	 * @param request
	 * @return       boolean
	 * @throws
	 */
	public  static boolean isAjaxRequest(HttpServletRequest request){

		String accept = request.getHeader("accept");
		String xRequestedWith = request.getHeader("X-Requested-With");

		// 如果是异步请求或是手机端，则直接返回信息
		return ((accept != null && accept.indexOf("application/json") != -1
				|| (xRequestedWith != null && xRequestedWith.indexOf("XMLHttpRequest") != -1)
		));
	}
}
