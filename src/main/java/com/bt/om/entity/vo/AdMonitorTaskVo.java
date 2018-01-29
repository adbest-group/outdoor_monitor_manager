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
	private Integer monitorsCount;// 监测次数
	private String brand;// 品牌名
	private Integer status;// 状态
	private String problem;// 反馈问题
	private String pic_url1;// 上传图片1
	private String pic_url2;// 上传图片2
	private String pic_url3;// 上传图片3
	private String pic_url4;// 上传图片4
	private String name;

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	

	public String getPic_url1() {
		return pic_url1;
	}

	public void setPic_url1(String pic_url1) {
		this.pic_url1 = pic_url1;
	}

	public String getPic_url2() {
		return pic_url2;
	}

	public void setPic_url2(String pic_url2) {
		this.pic_url2 = pic_url2;
	}

	public String getPic_url3() {
		return pic_url3;
	}

	public void setPic_url3(String pic_url3) {
		this.pic_url3 = pic_url3;
	}

	public String getPic_url4() {
		return pic_url4;
	}

	public void setPic_url4(String pic_url4) {
		this.pic_url4 = pic_url4;
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

	public Integer getMonitorsCount() {
		return monitorsCount;
	}

	public void setMonitorsCount(Integer monitorsCount) {
		this.monitorsCount = monitorsCount;
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

}
