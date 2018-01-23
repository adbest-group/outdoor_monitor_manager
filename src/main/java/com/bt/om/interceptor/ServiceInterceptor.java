package com.bt.om.interceptor;

import org.apache.log4j.Logger;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;

import com.bt.om.util.ConfigUtil;

/**
 * 服务栏截器
 * @author hl-tanyong
 *
 */
public class ServiceInterceptor {
    /**
     * Logger for this class
     */
    private static final Logger logger        = Logger.getLogger(ServiceInterceptor.class);
    private static final int    QUERY_INFO_MS = ConfigUtil.getInt("sys.query.info.sec", 3) * 1000;
    private static final int    QUERY_WARN_MS = ConfigUtil.getInt("sys.query.warn.sec", 5) * 1000;

    /**
     * 记录执行时间
     * 
     * @param query
     * @param time
     */
    private void logQueryTime(String query, long time) {
        String content = time + " " + query;
        if (time > QUERY_WARN_MS) {
            logger.warn(content);
        } else if (time > QUERY_INFO_MS) {
            logger.info(content);
        } else {
            logger.debug(content);
        }
    }

    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
        long time = System.currentTimeMillis();
        Object retVal = pjp.proceed();
        time = System.currentTimeMillis() - time;
        this.logQueryTime(
            new StringBuilder().append(pjp.getTarget().getClass().getName()).append(".")
                .append(pjp.getSignature().getName()).toString(), time);
        return retVal;
    }

    public void doThrowing(JoinPoint jp, Throwable ex) {
        logger.error("method " + jp.getTarget().getClass().getName() + "."
                     + jp.getSignature().getName() + " throw exception");
        StringWriter out = new StringWriter();
        ex.printStackTrace(new PrintWriter(out));
        logger.error(out.toString());
    }

}