package com.bt.om.entity.vo;

public class CountGroupByCityVo {
	private Integer count;
	private Long province;
	private Long city;
	private String cityName;
	
	public Integer getCount() {
		return count;
	}
	
	public void setCount(Integer count) {
		this.count = count;
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
	
	public String getCityName() {
		return cityName;
	}
	
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
}
