package com.bt.om.entity.vo;

import com.bt.om.entity.AdActivity;

public class AdActivityVo2 extends AdActivity {

	private String realName; //审核人真实姓名
	
	private String customerName; //广告商姓名
	
    private Integer mediaTypeParentId;//媒体大类
    private Integer mediaTypeId;//媒体小类
	private Long province;// 省
	private Long city;// 市
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

	public Integer getMediaTypeParentId() {
		return mediaTypeParentId;
	}

	public void setMediaTypeParentId(Integer mediaTypeParentId) {
		this.mediaTypeParentId = mediaTypeParentId;
	}

	public Integer getMediaTypeId() {
		return mediaTypeId;
	}

	public void setMediaTypeId(Integer mediaTypeId) {
		this.mediaTypeId = mediaTypeId;
	}

	public Long getProvince() {
		return province;
	}

	public void setProvince(Long province) {
		this.province = province;
	}

	public Long getCity() {
		return city;
	}

	public void setCity(Long city) {
		this.city = city;
	}
	
}
