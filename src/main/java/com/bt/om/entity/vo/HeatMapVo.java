package com.bt.om.entity.vo;

import java.util.List;

/**
 * 热力图报表查询参数
 */
public class HeatMapVo {
	private Integer activityId; // 活动id
	private Integer mediaId; // 媒体主id
	private Long province; // 省
	private Long city; // 市
	private Long region; // 区
	private List<Integer> infoIds; // 广告位id集合
	private Integer userId; // 广告主id
	private Integer mediaTypeId; // 媒体小类id
	private Integer mediaTypeParentId; // 媒体大类id
	
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
	public Integer getMediaTypeId() {
		return mediaTypeId;
	}
	public void setMediaTypeId(Integer mediaTypeId) {
		this.mediaTypeId = mediaTypeId;
	}
	public Integer getMediaTypeParentId() {
		return mediaTypeParentId;
	}
	public void setMediaTypeParentId(Integer mediaTypeParentId) {
		this.mediaTypeParentId = mediaTypeParentId;
	}
}
