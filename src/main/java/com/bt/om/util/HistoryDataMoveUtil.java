package com.bt.om.util;

import java.util.Calendar;
import java.util.Date;

public class HistoryDataMoveUtil {
	
	/**
	 * 根据时间获取历史数据的表名
	 * 
	 * @param date 
	 * @return String 表名
	 */
	 public static String getTableName(Date date) {
	    	Calendar calendar = Calendar.getInstance();
	    	calendar.setTime(date);
			calendar.add(Calendar.MONTH, -6);
			String year = String.valueOf(calendar.get(Calendar.YEAR));
		    int type = calendar.get(Calendar.MONTH)>5?1:0;
	    	return year + "_" + type;
	    }
}
