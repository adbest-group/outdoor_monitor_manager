package com.bt.om.handler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;
import com.adtime.common.lang.StringUtil;

/**
 * 
 * 
 * @author hl-tanyong
 * @version $Id: Date2LongTypeHandler.java, v 0.1 2015年10月19日 上午11:21:04 hl-tanyong Exp $
 */
public class String2ListTypeHandler implements TypeHandler<List<String>> {

    @Override
    public void setParameter(PreparedStatement ps, int i, List<String> parameter,
                             JdbcType jdbcType) throws SQLException {
        ps.setObject(i, parameter);
    }

    @Override
    public List<String> getResult(ResultSet rs, String columnName) throws SQLException {
        String value = rs.getString(columnName);
        if (StringUtil.isNotBlank(value)) {
            String[] values = value.split(",");
            return Arrays.asList(values);
        }
        return new ArrayList<String>();
    }

    @Override
    public List<String> getResult(ResultSet rs, int columnIndex) throws SQLException {
        String value = rs.getString(columnIndex);
        if (StringUtil.isNotBlank(value)) {
            String[] values = value.split(",");
            return Arrays.asList(values);
        }
        return new ArrayList<String>();
    }

    @Override
    public List<String> getResult(CallableStatement cs, int columnIndex) throws SQLException {
        return null;
    }

}
