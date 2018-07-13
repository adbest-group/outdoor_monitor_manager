package com.bt.om.vo.api;

/**
 * Created by jiayong.mao on 2018/4/20.
 */
public class CustomerActivityReport extends BasicVo {
	private static final long serialVersionUID = 1L;
	private Integer activityId; //活动Id
	private String activityName; //活动名称
	private String startTime; //广告投放开始时间
	private String endTime; //广告投放结束时间
	private String status; //广告活动状态(1：未确认 2：已确认 3：已完成)
	private String createTime; //活动创建时间
	private Integer unstartNum; //广告位"未开始"数量
	private Integer watchingNum; //广告位"监测中"数量
	private Integer hasProblemNum; //广告位"有问题"数量
	
	public Integer getActivityId() {
		return activityId;
	}
	public void setActivityId(Integer activityId) {
		this.activityId = activityId;
	}
	public String getActivityName() {
		return activityName;
	}
	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public Integer getUnstartNum() {
		return unstartNum;
	}
	public void setUnstartNum(Integer unstartNum) {
		this.unstartNum = unstartNum;
	}
	public Integer getWatchingNum() {
		return watchingNum;
	}
	public void setWatchingNum(Integer watchingNum) {
		this.watchingNum = watchingNum;
	}
	public Integer getHasProblemNum() {
		return hasProblemNum;
	}
	public void setHasProblemNum(Integer hasProblemNum) {
		this.hasProblemNum = hasProblemNum;
	}
	
}
