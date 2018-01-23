package com.bt.om.web.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * 登录拦截器
 * 
 * @author hl-tanyong
 * @version $Id: LoginInterceptor.java, v 0.1 2015年9月29日 下午2:19:27 hl-tanyong Exp $
 */
public class LoginInterceptor extends HandlerInterceptorAdapter {

    /**
     * 
     * @see org.springframework.web.servlet.handler.HandlerInterceptorAdapter#preHandle(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, Object)
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) throws Exception {
        //#这里判断用户是否登录
        return super.preHandle(request, response, handler);
    }

    /**
     * 
     * @see org.springframework.web.servlet.handler.HandlerInterceptorAdapter#postHandle(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, Object, org.springframework.web.servlet.ModelAndView)
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
        //#这里存放用户全局信息
        super.postHandle(request, response, handler, modelAndView);
    }

}
