package com.bt.om.log;

public class SystemLog {

//	SysRoleMapper sysRoleMapper = ServiceManager.getService(SysRoleMapper.class);
//	SysMenuMapper sysMenuMapper = ServiceManager.getService(SysMenuMapper.class);

	public void info(String moduleName, String optType, String objectName, String ip, Object beforeObj, Object afterObj,
			Integer location_flag) {
	}

	// /**
	// * 获取登录用户信息
	// *
	// * @return
	// */
	// protected SysUser getLoginUser() {
	// return (SysUser)
	// ShiroUtils.getSessionAttribute(SessionKey.SESSION_LOGIN_USER.toString());
	// }
}
