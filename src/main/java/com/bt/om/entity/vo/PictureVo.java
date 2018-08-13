package com.bt.om.entity.vo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.bt.om.entity.AdMonitorTaskFeedbackResources;

public class PictureVo {
	private String taskType; //监测类型（上刊监测, 投放期间监测, 下刊监测）
	private String time; //监测时间
	private Date date; //监测时间
	private String problem; //监测发现的问题(通过选择提交的问题)
	private String problemOther; //监测发现的问题(通过输入提交的问题)
	private String userName; //谁做的监测任务
	private List<AdMonitorTaskFeedbackResources> pics = new ArrayList<>();
	
	public String getTaskType() {
		return taskType;
	}
	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getProblem() {
		return problem;
	}
	public void setProblem(String problem) {
		this.problem = problem;
	}
	public String getProblemOther() {
		return problemOther;
	}
	public void setProblemOther(String problemOther) {
		this.problemOther = problemOther;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public List<AdMonitorTaskFeedbackResources> getPics() {
		return pics;
	}
	public void setPics(List<AdMonitorTaskFeedbackResources> pics) {
		this.pics = pics;
	}
}
