package com.bt.om.entity.vo;

import java.util.List;

public class HeatMapVo {
	private Integer activityId;
	private Integer mediaId;
	private Long province;
	private Long city;
	private Long region;
	private List<Integer> infoIds;
	private Integer userId;
	
	public Integer getActivityId() {
		return activityId;
	}
	public void setActivityId(Integer activityId) {
		this.activityId = activityId;
	}
	public Integer getMediaId() {
		return mediaId;
	}
	public void setMediaId(Integer mediaId) {
		this.mediaId = mediaId;
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
	public Long getRegion() {
		return region;
	}
	public void setRegion(Long region) {
		this.region = region;
	}
	public List<Integer> getInfoIds() {
		return infoIds;
	}
	public void setInfoIds(List<Integer> infoIds) {
		this.infoIds = infoIds;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
}
