package com.bt.om.entity.vo;

import com.bt.om.entity.AdActivity;

public class AdActivityVo2 extends AdActivity {

	private String realName; //审核人真实姓名
	
	private String customerName; //广告商姓名
	
	public String getRealName() {
		return realName;
	}
	
	public void setRealName(String realName) {
		this.realName = realName;
	}
	
	public String getCustomerName() {
		return customerName;
	}
	
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
}
