package com.bt.om.handler;

import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

/**
 * 
 * 
 * @author hl-tanyong
 * @version $Id: Date2LongTypeHandler.java, v 0.1 2015年10月19日 上午11:21:04
 *          hl-tanyong Exp $
 */
public class Blob2StringTypeHandler implements TypeHandler<String> {

	public void setParameter(PreparedStatement ps, int i, String parameter,
			JdbcType jdbcType) throws SQLException {
		// TODO Auto-generated method stub
		ps.setObject(i, parameter);
	}

	public String getResult(ResultSet rs, String columnName) throws SQLException {
		// TODO Auto-generated method stub
		Blob blob = rs.getBlob(columnName);
		if (null != blob) {
			byte[] bs = blob.getBytes(1l, (int) blob.length());
			return new String(bs);

		}
		return null;
	}

	public String getResult(ResultSet rs, int columnIndex) throws SQLException {
		// TODO Auto-generated method stub
		Blob blob = rs.getBlob(columnIndex);
		if (null != blob) {
			byte[] bs = blob.getBytes(1l, (int) blob.length());
			return new String(bs);

		}
		return null;
	}

	public String getResult(CallableStatement cs, int columnIndex)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

}
