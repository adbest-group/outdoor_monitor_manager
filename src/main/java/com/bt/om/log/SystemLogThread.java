package com.bt.om.log;

public class SystemLogThread implements Runnable {

	private String moduleName;
	private String optType;
	private String objectName;
	private Object beforeObj;
	private Object afterObj;
	private String ip;
	// 各个模块对应的id(参见sys_menu表)
	private Integer location_flag;

	public SystemLogThread(String moduleName, String optType, String objectName,
			String ip, Object beforeObj, Object afterObj,Integer location_flag) {

		this.moduleName = moduleName;
		this.optType = optType;
		this.objectName = objectName;
		this.ip = ip;
		this.beforeObj = beforeObj;
		this.afterObj = afterObj;
		this.location_flag = location_flag;
	}

	@Override
	public void run() {
		try {
			SystemLog log = new SystemLog();
			log.info(moduleName, optType, objectName, ip, beforeObj, afterObj,location_flag);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
