package com.bt.om.entity.vo;

public class AdSeatCount {
	private Integer adseatId; //广告位id
	private Integer count; //搜索的时间段内该广告位同时开展的活动数量
	private Integer allowMulti; //是否允许多个活动
    private Integer multiNum; //允许活动的数量

	public Integer getAdseatId() {
		return adseatId;
	}
	
	public void setAdseatId(Integer adseatId) {
		this.adseatId = adseatId;
	}
	
	public Integer getCount() {
		return count;
	}
	
	public void setCount(Integer count) {
		this.count = count;
	}
	
	public Integer getAllowMulti() {
		return allowMulti;
	}
	
	public void setAllowMulti(Integer allowMulti) {
		this.allowMulti = allowMulti;
	}
	
	public Integer getMultiNum() {
		return multiNum;
	}
	
	public void setMultiNum(Integer multiNum) {
		this.multiNum = multiNum;
	}
}
