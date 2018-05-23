package com.bt.om.entity.vo;

import java.util.Date;

public class AbandonTaskVo {
	private Integer id;
	private Integer userTaskStatus;
	private Integer taskStatus;
	private Date abandonTime;
	private Date updateTime;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getUserTaskStatus() {
		return userTaskStatus;
	}
	public void setUserTaskStatus(Integer userTaskStatus) {
		this.userTaskStatus = userTaskStatus;
	}
	public Integer getTaskStatus() {
		return taskStatus;
	}
	public void setTaskStatus(Integer taskStatus) {
		this.taskStatus = taskStatus;
	}
	public Date getAbandonTime() {
		return abandonTime;
	}
	public void setAbandonTime(Date abandonTime) {
		this.abandonTime = abandonTime;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	
}
