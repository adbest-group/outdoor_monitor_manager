package com.bt.om.entity.vo;

import com.bt.om.entity.AdActivityAdseat;

public class AdActivityAdseatTaskVo extends AdActivityAdseat {
	private static final long serialVersionUID = 1L;
	
	//ad_monitor_task 相关属性
	private Integer problemStatus; //问题状态 (1：未监测 2：无问题 3：有问题 4：已解决 5：已闭环)
	private Integer status; //任务状态(1：待指派 2：待执行 3：审核中 4：通过审核 5：未通过审核)
	private String statusDesp; //未开始, 监测中, 有问题, 已结束
	
	//ad_seat_info 相关属性
	private String info_name;
    private Long info_province;
    private Long info_city;
    private Long info_region;
    private Long info_street;
    private String info_location;
    private String info_adSize;
    private Double info_lon;
    private Double info_lat;
    private Integer info_mapStandard;
    private String info_adArea;
    private String info_contactName;
    private String info_contactCell;
    private Integer info_mediaTypeParentId;
    private Integer info_mediaTypeId;
    private String info_uniqueKey;
    private String info_memo;
	
	public Integer getProblemStatus() {
		return problemStatus;
	}
	
	public void setProblemStatus(Integer problemStatus) {
		this.problemStatus = problemStatus;
	}
	
	public Integer getStatus() {
		return status;
	}
	
	public void setStatus(Integer status) {
		this.status = status;
	}
	
	public String getStatusDesp() {
		return statusDesp;
	}
	
	public void setStatusDesp(String statusDesp) {
		this.statusDesp = statusDesp;
	}

	public String getInfo_name() {
		return info_name;
	}

	public void setInfo_name(String info_name) {
		this.info_name = info_name;
	}

	public Long getInfo_province() {
		return info_province;
	}

	public void setInfo_province(Long info_province) {
		this.info_province = info_province;
	}

	public Long getInfo_city() {
		return info_city;
	}

	public void setInfo_city(Long info_city) {
		this.info_city = info_city;
	}

	public Long getInfo_region() {
		return info_region;
	}

	public void setInfo_region(Long info_region) {
		this.info_region = info_region;
	}

	public Long getInfo_street() {
		return info_street;
	}

	public void setInfo_street(Long info_street) {
		this.info_street = info_street;
	}

	public String getInfo_location() {
		return info_location;
	}

	public void setInfo_location(String info_location) {
		this.info_location = info_location;
	}

	public String getInfo_adSize() {
		return info_adSize;
	}

	public void setInfo_adSize(String info_adSize) {
		this.info_adSize = info_adSize;
	}

	public Double getInfo_lon() {
		return info_lon;
	}

	public void setInfo_lon(Double info_lon) {
		this.info_lon = info_lon;
	}

	public Double getInfo_lat() {
		return info_lat;
	}

	public void setInfo_lat(Double info_lat) {
		this.info_lat = info_lat;
	}

	public Integer getInfo_mapStandard() {
		return info_mapStandard;
	}

	public void setInfo_mapStandard(Integer info_mapStandard) {
		this.info_mapStandard = info_mapStandard;
	}

	public String getInfo_adArea() {
		return info_adArea;
	}

	public void setInfo_adArea(String info_adArea) {
		this.info_adArea = info_adArea;
	}

	public String getInfo_contactName() {
		return info_contactName;
	}

	public void setInfo_contactName(String info_contactName) {
		this.info_contactName = info_contactName;
	}

	public String getInfo_contactCell() {
		return info_contactCell;
	}

	public void setInfo_contactCell(String info_contactCell) {
		this.info_contactCell = info_contactCell;
	}

	public Integer getInfo_mediaTypeParentId() {
		return info_mediaTypeParentId;
	}

	public void setInfo_mediaTypeParentId(Integer info_mediaTypeParentId) {
		this.info_mediaTypeParentId = info_mediaTypeParentId;
	}

	public Integer getInfo_mediaTypeId() {
		return info_mediaTypeId;
	}

	public void setInfo_mediaTypeId(Integer info_mediaTypeId) {
		this.info_mediaTypeId = info_mediaTypeId;
	}

	public String getInfo_uniqueKey() {
		return info_uniqueKey;
	}

	public void setInfo_uniqueKey(String info_uniqueKey) {
		this.info_uniqueKey = info_uniqueKey;
	}

	public String getInfo_memo() {
		return info_memo;
	}

	public void setInfo_memo(String info_memo) {
		this.info_memo = info_memo;
	}
	
}
