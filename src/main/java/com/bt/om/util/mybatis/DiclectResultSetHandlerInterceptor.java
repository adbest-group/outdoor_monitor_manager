package com.bt.om.util.mybatis;

import java.sql.Statement;
import java.util.Properties;

import org.apache.ibatis.executor.resultset.DefaultResultSetHandler;
import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.RowBounds;

/**
 *  MyBatis插件，捕捉ResultSetHandler
 * 
 * @author hl-tanyong
 * @version $Id: DiclectResultSetHandlerInterceptor.java, v 0.1 2015年9月18日 上午9:34:12 hl-tanyong Exp $
 */
@Intercepts({ @Signature(type = ResultSetHandler.class, method = "handleResultSets", args = { Statement.class }) })
public class DiclectResultSetHandlerInterceptor implements Interceptor {

    public Object intercept(Invocation invocation) throws Throwable {
        DefaultResultSetHandler resultSet = (DefaultResultSetHandler) invocation.getTarget();

        RowBounds rowBounds = (RowBounds) ReflectUtil.getFieldValue(resultSet, "rowBounds");

        if (rowBounds.getLimit() > 0 && rowBounds.getLimit() < RowBounds.NO_ROW_LIMIT) {
            ReflectUtil.setFieldValue(resultSet, "rowBounds", new RowBounds());
        }
        return invocation.proceed();
    }

    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    public void setProperties(Properties properties) {
    }
}