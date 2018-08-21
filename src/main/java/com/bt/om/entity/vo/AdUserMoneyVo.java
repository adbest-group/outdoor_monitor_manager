package com.bt.om.entity.vo;

import com.bt.om.entity.AdUserMoney;

public class AdUserMoneyVo extends AdUserMoney {
	private String username;
	private Integer sum;

	public Integer getSum() {
		return sum;
	}

	public void setSum(Integer sum) {
		this.sum = sum;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
}
