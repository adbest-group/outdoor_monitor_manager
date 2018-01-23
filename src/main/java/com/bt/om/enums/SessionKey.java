package com.bt.om.enums;

/**
 * 
 * 
 * @author hl-tanyong
 * @version $Id: ResultCode.java, v 0.1 2015年9月29日 下午2:48:48 hl-tanyong Exp $
 */
public enum SessionKey {
    
    /**
     * 验证码
     */
    SESSION_CODE,

	/**
	 * 用户登录信息
	 */
	SESSION_LOGIN_USER,

    /**
     * 用户菜单信息
     */
    SESSION_USER_MENU;

	public String toString() {
		return this.name();
	}
}