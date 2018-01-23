package com.bt.om.util.mybatis;

/**
 * 
 * 
 * @author hl-tanyong
 * @version $Id: MySQLDialect.java, v 0.1 2015年9月18日 上午9:35:40 hl-tanyong Exp $
 */
public class MySQLDialect extends Dialect {
	public boolean supportsLimitOffset() {
		return true;
	}

	public boolean supportsLimit() {
		return true;
	}

	public String getLimitString(String sql, int offset,
			String offsetPlaceholder, int limit, String limitPlaceholder) {
		if (offset >= 0){
			return sql + " limit " + offsetPlaceholder + "," + limitPlaceholder;
		}	

		return sql + " limit " + limitPlaceholder;
	}
}