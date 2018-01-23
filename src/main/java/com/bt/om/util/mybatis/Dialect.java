package com.bt.om.util.mybatis;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * 
 * @author hl-tanyong
 * @version $Id: Dialect.java, v 0.1 2015年9月18日 上午9:36:18 hl-tanyong Exp $
 */
public class Dialect {
    public boolean supportsLimit() {
        return false;
    }

    public boolean supportsLimitOffset() {
        return supportsLimit();
    }

    public String getLimitString(String sql, int offset, int limit) {
        return getLimitString(sql, offset, Integer.toString(offset), limit, Integer.toString(limit));
    }

    public String getLimitString(String sql, int offset, String offsetPlaceholder, int limit,
                                 String limitPlaceholder) {
        throw new UnsupportedOperationException("paged queries not supported");
    }

    public String getCountSQL(String sql) {
        String countQueryString = null;
        if (isWarp(sql))
            countQueryString = "select count(*) from (" + removeOrders(sql) + ") _COUNT_FOR_SQL_";
        else
            countQueryString = " select count(*) " + removeSelect(removeOrders(sql));

        return countQueryString;
    }

    private static String removeOrders(String hql) {
        Pattern p = Pattern.compile("order\\s*by[\\w|\\W|\\s|\\S]*", 2);
        Matcher m = p.matcher(hql);
        StringBuffer sb = new StringBuffer();
        while (m.find())
            m.appendReplacement(sb, "");

        m.appendTail(sb);
        return sb.toString();
    }

    private static boolean isWarp(String hql) {
        Pattern p = Pattern.compile("(group\\s*by[\\w|\\W|\\s|\\S]*|union)+", 2);
        Matcher m = p.matcher(hql);
        return m.find();
    }

    private static String removeSelect(String hql) {
        int beginPos = hql.toLowerCase().indexOf("from");
        return hql.substring(beginPos);
    }
}