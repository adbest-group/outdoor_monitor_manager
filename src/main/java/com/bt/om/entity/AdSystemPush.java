package com.bt.om.entity;

import java.util.Date;

/**
 * @Description: "系统消息推送"实体类
 * @author sjh
 * @date: 2018年5月30日 下午1:59:22
 */
public class AdSystemPush extends ID {
	
	private Integer id;   //主键
	private String type; //消息类型 1.版本更新 2.免责声明更新
	private Integer userId;//推送的用户id
	private String title;//推送标题
	private String content; //内容，显示在通知栏里
	private String activityName;//活动名称
	private Date createTime; //创建日期
	private String createTimeStr;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getActivityName() {
		return activityName;
	}
	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getCreateTimeStr() {
		return createTimeStr;
	}
	public void setCreateTimeStr(String createTimeStr) {
		this.createTimeStr = createTimeStr;
	}
	
}
