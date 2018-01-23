package com.bt.om.security;

import java.io.IOException;

import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import com.jagregory.shiro.freemarker.ShiroTags;

import freemarker.template.TemplateException;

/**
 * freemarker整合shiro标签 配置类
 * @author jade
 *
 */
public class ShiroTagFreeMarkerConfigurer extends FreeMarkerConfigurer {
	
	public void afterPropertiesSet() throws IOException, TemplateException {
        super.afterPropertiesSet();
        this.getConfiguration().setSharedVariable("shiro", new ShiroTags());
    }
}
