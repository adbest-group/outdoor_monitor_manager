package com.bt.om.vo.api;

import java.io.Serializable;

import com.bt.om.entity.User;

public class UserVo implements Serializable {
	private static final long serialVersionUID = 1644252664911316576L;
	private Integer userId;
	private Integer status;
	private String msg;

	public UserVo(User user) {
		this.userId = user.getId();
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

}
