package com.bt.om.web.interceptor;

import com.bt.om.web.component.VMComponent;
import freemarker.ext.beans.BeansWrapper;
import freemarker.ext.beans.BeansWrapperBuilder;
import freemarker.template.TemplateModelException;
import freemarker.template.Version;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 
 * @author hl-tanyong
 * @version $Id: FreemarkerInterceptor.java, v 0.1 2015年9月29日 下午12:09:02 hl-tanyong Exp $
 */
public class FreemarkerInterceptor extends HandlerInterceptorAdapter {

    private Map<String, Class<?>> staticClassMap;

    @Resource
    private VMComponent vmComponent;

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
        if (null != modelAndView) {
            // 将class的引用注入给freemarker，作用域为request
            for (Entry<String, Class<?>> entry : staticClassMap.entrySet()) {
                String name = entry.getKey();
                modelAndView.addObject(name, getStaticModel(entry.getValue()));
            }
            modelAndView.addObject("vm", vmComponent);
            //modelAndView.addObject("now", new Date());
        }
    }

    /**
     * 获取freemarker可使用的bean
     * 
     * @param clz
     *            类型
     * @return
     */
    private static Object getStaticModel(Class<?> clz) {
        BeansWrapperBuilder builder = new BeansWrapperBuilder(new Version("2.3.20"));
        BeansWrapper wrapper = builder.build();
        try {
            return wrapper.getStaticModels().get(clz.getName());
        } catch (TemplateModelException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setStaticClassMap(Map<String, String> staticClasses) {
        staticClassMap = new HashMap<String, Class<?>>();
        for (Entry<String, String> entry : staticClasses.entrySet()) {
            try {
                Class<?> clazz = Class.forName(entry.getValue());
                staticClassMap.put(entry.getKey(), clazz);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

}
