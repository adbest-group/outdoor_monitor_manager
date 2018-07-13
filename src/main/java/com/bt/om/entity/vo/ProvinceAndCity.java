package com.bt.om.entity.vo;

import java.util.List;

import com.bt.om.entity.City;

public class ProvinceAndCity {
	private City provinceInfo;
	private List<City> cityInfo;
	
	public City getProvinceInfo() {
		return provinceInfo;
	}
	public void setProvinceInfo(City provinceInfo) {
		this.provinceInfo = provinceInfo;
	}
	public List<City> getCityInfo() {
		return cityInfo;
	}
	public void setCityInfo(List<City> cityInfo) {
		this.cityInfo = cityInfo;
	}
}
