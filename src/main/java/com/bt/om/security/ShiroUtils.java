package com.bt.om.security;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;

public class ShiroUtils extends SecurityUtils {

	public static Object getSessionAttribute(String name){
		return getSubject().getSession().getAttribute(name);
	}
	
	public static void setSessionAttribute(String key,Object value){
		getSubject().getSession().setAttribute(key, value);
	}
    
    public static void removeAttribute(String key){
        getSubject().getSession().removeAttribute(key);
    }
	
	public static Session getSession(){
		return getSubject().getSession();
	}
}
