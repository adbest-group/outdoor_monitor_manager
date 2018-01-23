package com.bt.om.web.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * 获取request/response工具
 * 
 * @author hl-tanyong
 * @version $Id: ServletAttributes.java, v 0.1 2015年9月29日 上午11:30:15 hl-tanyong Exp $
 */
public class ServletAttributes {

	private static ThreadLocal<HttpServletRequest> requestContext = new ThreadLocal<HttpServletRequest>();

	private static ThreadLocal<HttpServletResponse> responseContext = new ThreadLocal<HttpServletResponse>();

	public static void setResponse(HttpServletResponse response) {
		responseContext.set(response);
	}

	public static void setRequest(HttpServletRequest request) {
		requestContext.set(request);
	}

	public static HttpServletRequest getRequest() {
		HttpServletRequest request = requestContext.get();
		if (null == request) { // 如果拦截器没有成功保存request，则通过另一种方式取request
			request = ((ServletRequestAttributes) RequestContextHolder
					.getRequestAttributes()).getRequest();
		}
		return request;
	}

	public static HttpServletResponse getResponse() {
		return responseContext.get();
	}

}
