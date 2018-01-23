package com.bt.om.common;

/**
 * 
 * 
 * @author hl-tanyong
 * @version $Id: ServiceManager.java, v 0.1 2015年9月18日 下午3:43:13 hl-tanyong Exp $
 */
public class ServiceManager {

    public static <T> T getService(Class<T> clazz) {
        return ServiceLocator.getService(clazz);
    }

    public static <T> T getService(String name, Class<T> clazz) {
        return ServiceLocator.getService(name, clazz);
    }

}
