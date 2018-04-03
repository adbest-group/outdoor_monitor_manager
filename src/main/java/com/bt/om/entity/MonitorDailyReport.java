package com.bt.om.entity;

import java.io.Serializable;
import java.util.Date;

public class MonitorDailyReport implements Serializable {

	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column monitor_daily_report.id
	 * @mbg.generated  Mon Apr 02 14:18:42 CST 2018
	 */
	private Long id;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column monitor_daily_report.activity_id
	 * @mbg.generated  Mon Apr 02 14:18:42 CST 2018
	 */
	private Integer activityId;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column monitor_daily_report.media_id
	 * @mbg.generated  Mon Apr 02 14:18:42 CST 2018
	 */
	private Integer mediaId;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column monitor_daily_report.province
	 * @mbg.generated  Mon Apr 02 14:18:42 CST 2018
	 */
	private Long province;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column monitor_daily_report.city
	 * @mbg.generated  Mon Apr 02 14:18:42 CST 2018
	 */
	private Long city;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column monitor_daily_report.region
	 * @mbg.generated  Mon Apr 02 14:18:42 CST 2018
	 */
	private Long region;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column monitor_daily_report.street
	 * @mbg.generated  Mon Apr 02 14:18:42 CST 2018
	 */
	private Long street;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column monitor_daily_report.task_count
	 * @mbg.generated  Mon Apr 02 14:18:42 CST 2018
	 */
	private Long taskCount;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column monitor_daily_report.problem_count
	 * @mbg.generated  Mon Apr 02 14:18:42 CST 2018
	 */
	private Long problemCount;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column monitor_daily_report.complete_count
	 * @mbg.generated  Mon Apr 02 14:18:42 CST 2018
	 */
	private Long completeCount;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column monitor_daily_report.report_date
	 * @mbg.generated  Mon Apr 02 14:18:42 CST 2018
	 */
	private Date reportDate;

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column monitor_daily_report.id
	 * @return  the value of monitor_daily_report.id
	 * @mbg.generated  Mon Apr 02 14:18:42 CST 2018
	 */
	public Long getId() {
		return id;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column monitor_daily_report.id
	 * @param id  the value for monitor_daily_report.id
	 * @mbg.generated  Mon Apr 02 14:18:42 CST 2018
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column monitor_daily_report.activity_id
	 * @return  the value of monitor_daily_report.activity_id
	 * @mbg.generated  Mon Apr 02 14:18:42 CST 2018
	 */
	public Integer getActivityId() {
		return activityId;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column monitor_daily_report.activity_id
	 * @param activityId  the value for monitor_daily_report.activity_id
	 * @mbg.generated  Mon Apr 02 14:18:42 CST 2018
	 */
	public void setActivityId(Integer activityId) {
		this.activityId = activityId;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column monitor_daily_report.media_id
	 * @return  the value of monitor_daily_report.media_id
	 * @mbg.generated  Mon Apr 02 14:18:42 CST 2018
	 */
	public Integer getMediaId() {
		return mediaId;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column monitor_daily_report.media_id
	 * @param mediaId  the value for monitor_daily_report.media_id
	 * @mbg.generated  Mon Apr 02 14:18:42 CST 2018
	 */
	public void setMediaId(Integer mediaId) {
		this.mediaId = mediaId;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column monitor_daily_report.province
	 * @return  the value of monitor_daily_report.province
	 * @mbg.generated  Mon Apr 02 14:18:42 CST 2018
	 */
	public Long getProvince() {
		return province;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column monitor_daily_report.province
	 * @param province  the value for monitor_daily_report.province
	 * @mbg.generated  Mon Apr 02 14:18:42 CST 2018
	 */
	public void setProvince(Long province) {
		this.province = province;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column monitor_daily_report.city
	 * @return  the value of monitor_daily_report.city
	 * @mbg.generated  Mon Apr 02 14:18:42 CST 2018
	 */
	public Long getCity() {
		return city;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column monitor_daily_report.city
	 * @param city  the value for monitor_daily_report.city
	 * @mbg.generated  Mon Apr 02 14:18:42 CST 2018
	 */
	public void setCity(Long city) {
		this.city = city;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column monitor_daily_report.region
	 * @return  the value of monitor_daily_report.region
	 * @mbg.generated  Mon Apr 02 14:18:42 CST 2018
	 */
	public Long getRegion() {
		return region;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column monitor_daily_report.region
	 * @param region  the value for monitor_daily_report.region
	 * @mbg.generated  Mon Apr 02 14:18:42 CST 2018
	 */
	public void setRegion(Long region) {
		this.region = region;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column monitor_daily_report.street
	 * @return  the value of monitor_daily_report.street
	 * @mbg.generated  Mon Apr 02 14:18:42 CST 2018
	 */
	public Long getStreet() {
		return street;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column monitor_daily_report.street
	 * @param street  the value for monitor_daily_report.street
	 * @mbg.generated  Mon Apr 02 14:18:42 CST 2018
	 */
	public void setStreet(Long street) {
		this.street = street;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column monitor_daily_report.task_count
	 * @return  the value of monitor_daily_report.task_count
	 * @mbg.generated  Mon Apr 02 14:18:42 CST 2018
	 */
	public Long getTaskCount() {
		return taskCount;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column monitor_daily_report.task_count
	 * @param taskCount  the value for monitor_daily_report.task_count
	 * @mbg.generated  Mon Apr 02 14:18:42 CST 2018
	 */
	public void setTaskCount(Long taskCount) {
		this.taskCount = taskCount;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column monitor_daily_report.problem_count
	 * @return  the value of monitor_daily_report.problem_count
	 * @mbg.generated  Mon Apr 02 14:18:42 CST 2018
	 */
	public Long getProblemCount() {
		return problemCount;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column monitor_daily_report.problem_count
	 * @param problemCount  the value for monitor_daily_report.problem_count
	 * @mbg.generated  Mon Apr 02 14:18:42 CST 2018
	 */
	public void setProblemCount(Long problemCount) {
		this.problemCount = problemCount;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column monitor_daily_report.complete_count
	 * @return  the value of monitor_daily_report.complete_count
	 * @mbg.generated  Mon Apr 02 14:18:42 CST 2018
	 */
	public Long getCompleteCount() {
		return completeCount;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column monitor_daily_report.complete_count
	 * @param completeCount  the value for monitor_daily_report.complete_count
	 * @mbg.generated  Mon Apr 02 14:18:42 CST 2018
	 */
	public void setCompleteCount(Long completeCount) {
		this.completeCount = completeCount;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column monitor_daily_report.report_date
	 * @return  the value of monitor_daily_report.report_date
	 * @mbg.generated  Mon Apr 02 14:18:42 CST 2018
	 */
	public Date getReportDate() {
		return reportDate;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column monitor_daily_report.report_date
	 * @param reportDate  the value for monitor_daily_report.report_date
	 * @mbg.generated  Mon Apr 02 14:18:42 CST 2018
	 */
	public void setReportDate(Date reportDate) {
		this.reportDate = reportDate;
	}
}