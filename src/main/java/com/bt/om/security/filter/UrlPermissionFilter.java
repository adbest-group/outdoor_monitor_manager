package com.bt.om.security.filter;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.springframework.stereotype.Component;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by caiting on 2018/2/9.
 */
@Component
public class UrlPermissionFilter extends AccessControlFilter {
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        //先判断带参数的权限判断
        Subject subject = getSubject(request, response);
        if(null != mappedValue){
            String[] arra = (String[])mappedValue;
            for (String permission : arra) {
                if(subject.isPermitted(permission)){
                    return Boolean.TRUE;
                }
            }
        }

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String uri = httpRequest.getRequestURI();//获取URI
        String basePath = httpRequest.getContextPath();//获取basePath
        if(null != uri && uri.startsWith(basePath)){
            uri = uri.replaceFirst(basePath, "");
        }
        if(subject.isPermitted(uri)){
            return Boolean.TRUE;
        }

        return Boolean.FALSE;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        return false;
    }
}
