package com.bt.om.entity.vo;

import com.bt.om.entity.AdMonitorTask;

public class AllAdMonitorTaskVo extends AdMonitorTask {
	private String activityName; //活动名称
	private String seatInfoName; //广告位名称
	private String userRealName; //任务执行人姓名
	private String assessorName; //审核人姓名
	private String assignorName; //指派人姓名
	
	public String getActivityName() {
		return activityName;
	}
	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}
	public String getSeatInfoName() {
		return seatInfoName;
	}
	public void setSeatInfoName(String seatInfoName) {
		this.seatInfoName = seatInfoName;
	}
	public String getUserRealName() {
		return userRealName;
	}
	public void setUserRealName(String userRealName) {
		this.userRealName = userRealName;
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
