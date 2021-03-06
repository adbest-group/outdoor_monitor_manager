package com.bt.om.entity.vo;

import java.util.Date;

public class PictureVo {
	private String taskType; //监测类型（上刊监测, 投放期间监测, 下刊监测）
	private String pic1;
	private String pic2;
	private String pic3;
	private String pic4;
	private String time; //监测时间
	private Date date; //监测时间
	private String problem; //监测发现的问题(通过选择提交的问题)
	private String problemOther; //监测发现的问题(通过输入提交的问题)
	private String userName; //谁做的监测任务
	
	public String getTaskType() {
		return taskType;
	}
	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}
	public String getPic1() {
		return pic1;
	}
	public void setPic1(String pic1) {
		this.pic1 = pic1;
	}
	public String getPic2() {
		return pic2;
	}
	public void setPic2(String pic2) {
		this.pic2 = pic2;
	}
	public String getPic3() {
		return pic3;
	}
	public void setPic3(String pic3) {
		this.pic3 = pic3;
	}
	public String getPic4() {
		return pic4;
	}
	public void setPic4(String pic4) {
		this.pic4 = pic4;
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
}
