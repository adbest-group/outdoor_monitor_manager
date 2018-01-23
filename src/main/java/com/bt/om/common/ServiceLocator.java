package com.bt.om.common;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.bt.om.util.ConfigUtil;

/**
 * 
 * 
 * @author hl-tanyong
 * @version $Id: ServiceLocator.java, v 0.1 2015年9月18日 下午3:38:07 hl-tanyong Exp $
 */
public class ServiceLocator {

    private static ApplicationContext context;

    private static final String[]     DEFAULT_SPRING_CONFIG_PATH = { "/spring/applicationContext.xml" };

    private static Log                log                        = LogFactory
                                                                     .getLog(ServiceLocator.class);

    static {
        long startTime = System.currentTimeMillis();
        String[] configs;
        String configStr = System.getProperty("spring.configfile");
        if (null != configStr) {
            configs = StringUtils.split(configStr, ",");
        } else {
            configs = ConfigUtil.getStringArray("sys.spring.configfile");
        }

        if (null == configs || configs.length == 0) {
            configs = DEFAULT_SPRING_CONFIG_PATH;
        }
        context = new ClassPathXmlApplicationContext(configs);
        long elapsedTime = System.currentTimeMillis() - startTime;
        log.info("Spring initialization completed in " + elapsedTime + " ms");
    }

    public static void init() {
        // 已经在static中初始化了
    }

    public static ApplicationContext getContext() {
        return context;
    }

    public static void setContext(ApplicationContext context) {
        ServiceLocator.context = context;
    }

    public static void destroy() {
        if (context instanceof ConfigurableApplicationContext) {
            ((ConfigurableApplicationContext) context).close();
        }
    }

    public static <T> T getService(String beanName, Class<T> clazz) {
        return context.getBean(beanName, clazz);
    }

    public static <T> T getService(Class<T> clazz) {
        return context.getBean(clazz);
    }
}