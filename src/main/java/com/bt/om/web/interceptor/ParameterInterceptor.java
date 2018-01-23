package com.bt.om.web.interceptor;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.bt.om.util.ConfigUtil;
import com.bt.om.util.RequestUtil;
import com.bt.om.web.util.ServletAttributes;

/**
 * <ol>保存当前线程中的request与response <b>用与之后获取</b></ol>
 * <ol>将URI中的参数放至model中 <b>用与freemarker中直接获取</b></ol>
 * 
 * @author hl-tanyong
 * @version $Id: ParameterInterceptor.java, v 0.1 2015年9月29日 下午1:36:46 hl-tanyong Exp $
 */
public class ParameterInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) throws Exception {
        ServletAttributes.setRequest(request);
        ServletAttributes.setResponse(response);
        return super.preHandle(request, response, handler);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
        if (modelAndView != null) {
            // vm path replace
            String viewName = modelAndView.getViewName();
            // set parameters to model
            if (!RequestUtil.isAjax(request)) {
                if (null != viewName && !viewName.startsWith("redirect")
                    && !viewName.startsWith("forward")) {
                    modelAndView.addObject("domain", ConfigUtil.getProjectDomain());
                    modelAndView.addObject("projectUrl", ConfigUtil.getProjectUrl());
                    if (!modelAndView.getModel().containsKey("thisUrl")) {
                        modelAndView.addObject("thisUrl", RequestUtil.getURI(request));
                    }

                    Enumeration<String> parameterNames = request.getParameterNames();
                    while (parameterNames.hasMoreElements()) {
                        String paramName = parameterNames.nextElement();
                        if (!modelAndView.getModel().containsKey(paramName)) {
                            modelAndView.addObject(paramName, request.getParameter(paramName));
                        }
                    }
                }
            }
        }
        super.postHandle(request, response, handler, modelAndView);
    }

}
