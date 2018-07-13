package com.bt.om.entity.vo;

import java.util.Date;

import com.bt.om.entity.SysUserHistory;

public class SysUserHistoryVo extends SysUserHistory{

	private String loginName;
	private String mediaNameOld;
	private String mediaNameNew;
	public String getLoginName() {
		return loginName;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	public String getMediaNameOld() {
		return mediaNameOld;
	}
	public void setMediaNameOld(String mediaNameOld) {
		this.mediaNameOld = mediaNameOld;
	}
	public String getMediaNameNew() {
		return mediaNameNew;
	}
	public void setMediaNameNew(String mediaNameNew) {
		this.mediaNameNew = mediaNameNew;
	}
	
	
}
