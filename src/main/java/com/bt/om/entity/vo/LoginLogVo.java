package com.bt.om.entity.vo;

import java.util.Date;

import com.bt.om.entity.LoginLog;



public class LoginLogVo extends LoginLog{
	private Integer id;
	private Integer userId;
	private Date begin;
	private Date end;
	
	public Date getBegin() {
		return begin;
	}
	public void setBegin(Date begin) {
		this.begin = begin;
	}
	public Date getEnd() {
		return end;
	}
	public void setEnd(Date end) {
		this.end = end;
	}
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
