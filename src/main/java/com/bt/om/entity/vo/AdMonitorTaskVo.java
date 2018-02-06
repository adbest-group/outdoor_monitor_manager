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
	private Integer province;// 省
	private Integer city;// 市
	private Integer region;// 区
	private Integer street;// 街道
	private String mediaName;// 媒体名
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

	public Integer getProvince() {
		return province;
	}

	public void setProvince(Integer province) {
		this.province = province;
	}

	public Integer getCity() {
		return city;
	}

	public void setCity(Integer city) {
		this.city = city;
	}

	public Integer getRegion() {
		return region;
	}

	public void setRegion(Integer region) {
		this.region = region;
	}

	public Integer getStreet() {
		return street;
	}

	public void setStreet(Integer street) {
		this.street = street;
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

}
