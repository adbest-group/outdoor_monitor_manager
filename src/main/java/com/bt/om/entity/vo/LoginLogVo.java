package com.bt.om.entity.vo;

import com.bt.om.entity.LoginLog;

public class LoginLogVo extends LoginLog{
	private Integer id;
	private Integer userId;
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	private String username;
	private String realname;
	public String getUsername() {
		return username;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getRealname() {
		return realname;
	}
	public void setRealname(String realname) {
		this.realname = realname;
	}

	
}
