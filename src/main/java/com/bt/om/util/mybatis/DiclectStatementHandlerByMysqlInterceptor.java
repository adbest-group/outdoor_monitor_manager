package com.bt.om.util.mybatis;

import java.sql.Connection;
import java.util.Properties;

import org.apache.ibatis.executor.statement.PreparedStatementHandler;
import org.apache.ibatis.executor.statement.RoutingStatementHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.RowBounds;

/**
 * MyBatis插件，捕捉StatementHandler，让Mysql语句支持翻页
 * 
 * @author hl-tanyong
 * @version $Id: DiclectStatementHandlerByMysqlInterceptor.java, v 0.1 2015年9月18日 上午9:37:32 hl-tanyong Exp $
 */
@Intercepts({ @Signature(type = StatementHandler.class, method = "prepare", args = { Connection.class }) })
public class DiclectStatementHandlerByMysqlInterceptor implements Interceptor {
    private static final String DIALECT = "com.bt.om.util.mybatis.MySQLDialect";

    public Object intercept(Invocation invocation) throws Throwable {
        RoutingStatementHandler statement = (RoutingStatementHandler) invocation.getTarget();
        PreparedStatementHandler handler = (PreparedStatementHandler) ReflectUtil.getFieldValue(
            statement, "delegate");
        RowBounds rowBounds = (RowBounds) ReflectUtil.getFieldValue(handler, "rowBounds");

        if (rowBounds.getLimit() > 0 && rowBounds.getLimit() < RowBounds.NO_ROW_LIMIT) {
            BoundSql boundSql = statement.getBoundSql();
            String sql = boundSql.getSql();

            MySQLDialect dialect = (MySQLDialect) Class.forName(DIALECT).newInstance();
            sql = dialect.getLimitString(sql, rowBounds.getOffset(), rowBounds.getLimit());

            ReflectUtil.setFieldValue(boundSql, "sql", sql);
        }
        return invocation.proceed();
    }

    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    public void setProperties(Properties properties) {

    }
}
