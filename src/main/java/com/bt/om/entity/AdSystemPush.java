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
	private String content; //内容，显示在通知栏里
	private Date create_time; //创建日期
	
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
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Date getCreate_time() {
		return create_time;
	}
	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}
}
