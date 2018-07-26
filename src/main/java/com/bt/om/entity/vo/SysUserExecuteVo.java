package com.bt.om.entity.vo;

import com.bt.om.entity.SysUserExecute;

public class SysUserExecuteVo extends SysUserExecute {

	private String mediaName; //媒体名称
	
	private String companyName; //sys_user表的realname
	
	public String getMediaName() {
		return mediaName;
	}
	
	public void setMediaName(String mediaName) {
		this.mediaName = mediaName;
	}
	
	public String getCompanyName() {
		return companyName;
	}
	
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
}
