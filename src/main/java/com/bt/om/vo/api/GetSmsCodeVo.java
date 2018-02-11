package com.bt.om.vo.api;

import java.io.Serializable;

public class GetSmsCodeVo implements Serializable {

	private static final long serialVersionUID = 5774873558751018950L;
	private Integer status;
	private String msg;
	private String vcode;

	public GetSmsCodeVo(Integer status, String msg, String vcode) {
		this.status = status;
		this.msg = msg;
		this.vcode = vcode;
	}

	public String getVcode() {
		return vcode;
	}

	public void setVcode(String vcode) {
		this.vcode = vcode;
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
