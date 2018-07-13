package com.bt.om.entity.vo;

import com.bt.om.entity.AdMonitorTask;

import java.util.Date;

/**
 * Created by caiting on 2018/1/20.
 */
public class AdMonitorTaskVo extends AdMonitorTask {
	private String activityName;// 活动名称
	private String samplePicUrl;// 样例图片
	private Date startTime;// 投放开始时间
	private Date endTime;// 投放结束时间
	private Long province;// 省
	private Long city;// 市
	private Long region;// 区
	private Long street;// 街道
	private String location; //详细地址
	private Integer mediaId;//媒体id
	private String mediaName;// 媒体名
    private Integer mediaTypeParentId;//媒体大类
    private Integer mediaTypeId;//媒体小类
	private String adSeatName;//
	private String realname;
	private Date monitorsStart;// 监测开始时间
	private Date monitorsEnd;// 监测结束时间
	private String brand;// 品牌名
	private Integer status;// 状态
	private String problem;// 反馈问题
	private String problemOther;
	private String picUrl1;// 上传图片1
	private String picUrl2;// 上传图片2
	private String picUrl3;// 上传图片3
	private String picUrl4;// 上传图片4
	private String name;
	private int feedbackStatus;
	private Date feedbackCreateTime;
	private String reason;
	private Double lon;
	private Double lat;
	private Double feedbackLon;
	private Double feedbackLat;
	private Integer feedbackId;
	private String assessorName;
	private String assignorName;
	private String parentName;
	private String secondName;
	
	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	public String getSecondName() {
		return secondName;
	}

	public void setSecondName(String secondName) {
		this.secondName = secondName;
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

	public Integer getMediaId() {
		return mediaId;
	}

	public void setMediaId(Integer mediaId) {
		this.mediaId = mediaId;
	}

	public Double getLon() {
		return lon;
	}

	public void setLon(Double lon) {
		this.lon = lon;
	}

	public Double getLat() {
		return lat;
	}

	public void setLat(Double lat) {
		this.lat = lat;
	}

	public Double getFeedbackLon() {
		return feedbackLon;
	}

	public void setFeedbackLon(Double feedbackLon) {
		this.feedbackLon = feedbackLon;
	}

	public Double getFeedbackLat() {
		return feedbackLat;
	}

	public void setFeedbackLat(Double feedbackLat) {
		this.feedbackLat = feedbackLat;
	}
	
	public Integer getFeedbackId() {
		return feedbackId;
	}
	
	public void setFeedbackId(Integer feedbackId) {
		this.feedbackId = feedbackId;
	}

	public int getDownMonitor() {
		return downMonitor;
	}

	public void setDownMonitor(int downMonitor) {
		this.downMonitor = downMonitor;
	}

	public int getDurationMonitor() {
		return durationMonitor;
	}

	public void setDurationMonitor(int durationMonitor) {
		this.durationMonitor = durationMonitor;
	}

	private int downMonitor;
	private int durationMonitor;

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getPicUrl1() {
		return picUrl1;
	}

	public void setPicUrl1(String picUrl1) {
		this.picUrl1 = picUrl1;
	}

	public String getPicUrl2() {
		return picUrl2;
	}

	public void setPicUrl2(String picUrl2) {
		this.picUrl2 = picUrl2;
	}

	public String getPicUrl3() {
		return picUrl3;
	}

	public void setPicUrl3(String picUrl3) {
		this.picUrl3 = picUrl3;
	}

	public String getPicUrl4() {
		return picUrl4;
	}

	public void setPicUrl4(String picUrl4) {
		this.picUrl4 = picUrl4;
	}

	public String getActivityName() {
		return activityName;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

	public String getSamplePicUrl() {
		return samplePicUrl;
	}

	public void setSamplePicUrl(String samplePicUrl) {
		this.samplePicUrl = samplePicUrl;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
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

	public Long getStreet() {
		return street;
	}

	public void setStreet(Long street) {
		this.street = street;
	}
	
	public String getLocation() {
		return location;
	}
	
	public void setLocation(String location) {
		this.location = location;
	}

	public String getMediaName() {
		return mediaName;
	}

	public void setMediaName(String mediaName) {
		this.mediaName = mediaName;
	}

	public String getAdSeatName() {
		return adSeatName;
	}

	public void setAdSeatName(String adSeatName) {
		this.adSeatName = adSeatName;
	}

	public String getRealname() {
		return realname;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}

	public Date getMonitorsEnd() {
		return monitorsEnd;
	}

	public void setMonitorsEnd(Date monitorsEnd) {
		this.monitorsEnd = monitorsEnd;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public Date getMonitorsStart() {
		return monitorsStart;
	}

	public void setMonitorsStart(Date monitorsStart) {
		this.monitorsStart = monitorsStart;
	}

	public String getProblem() {
		return problem;
	}

	public void setProblem(String problem) {
		this.problem = problem;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getProblemOther() {
		return problemOther;
	}

	public void setProblemOther(String problemOther) {
		this.problemOther = problemOther;
	}

	public Date getFeedbackCreateTime() {
		return feedbackCreateTime;
	}

	public void setFeedbackCreateTime(Date feedbackCreateTime) {
		this.feedbackCreateTime = feedbackCreateTime;
	}

	public int getFeedbackStatus() {
		return feedbackStatus;
	}

	public void setFeedbackStatus(int feedbackStatus) {
		this.feedbackStatus = feedbackStatus;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getAssessorName() {
		return assessorName;
	}

	public void setAssessorName(String assessorName) {
		this.assessorName = assessorName;
	}

	public String getAssignorName() {
		return assignorName;
	}

	public void setAssignorName(String assignorName) {
		this.assignorName = assignorName;
	}
}
