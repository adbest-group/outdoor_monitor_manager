package com.bt.om.entity;

import java.util.Date;

public class AdApp {

	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column ad_app.id
	 * @mbg.generated  Wed May 30 17:27:30 CST 2018
	 */
	private Integer id;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column ad_app.app_name
	 * @mbg.generated  Wed May 30 17:27:30 CST 2018
	 */
	private String appName;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column ad_app.app_sid
	 * @mbg.generated  Wed May 30 17:27:30 CST 2018
	 */
	private String appSid;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column ad_app.status
	 * @mbg.generated  Wed May 30 17:27:30 CST 2018
	 */
	private Integer status;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column ad_app.create_time
	 * @mbg.generated  Wed May 30 17:27:30 CST 2018
	 */
	private Date createTime;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column ad_app.update_time
	 * @mbg.generated  Wed May 30 17:27:30 CST 2018
	 */
	private Date updateTime;

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column ad_app.id
	 * @return  the value of ad_app.id
	 * @mbg.generated  Wed May 30 17:27:30 CST 2018
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column ad_app.id
	 * @param id  the value for ad_app.id
	 * @mbg.generated  Wed May 30 17:27:30 CST 2018
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column ad_app.app_name
	 * @return  the value of ad_app.app_name
	 * @mbg.generated  Wed May 30 17:27:30 CST 2018
	 */
	public String getAppName() {
		return appName;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column ad_app.app_name
	 * @param appName  the value for ad_app.app_name
	 * @mbg.generated  Wed May 30 17:27:30 CST 2018
	 */
	public void setAppName(String appName) {
		this.appName = appName == null ? null : appName.trim();
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column ad_app.app_sid
	 * @return  the value of ad_app.app_sid
	 * @mbg.generated  Wed May 30 17:27:30 CST 2018
	 */
	public String getAppSid() {
		return appSid;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column ad_app.app_sid
	 * @param appSid  the value for ad_app.app_sid
	 * @mbg.generated  Wed May 30 17:27:30 CST 2018
	 */
	public void setAppSid(String appSid) {
		this.appSid = appSid == null ? null : appSid.trim();
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column ad_app.status
	 * @return  the value of ad_app.status
	 * @mbg.generated  Wed May 30 17:27:30 CST 2018
	 */
	public Integer getStatus() {
		return status;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column ad_app.status
	 * @param status  the value for ad_app.status
	 * @mbg.generated  Wed May 30 17:27:30 CST 2018
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column ad_app.create_time
	 * @return  the value of ad_app.create_time
	 * @mbg.generated  Wed May 30 17:27:30 CST 2018
	 */
	public Date getCreateTime() {
		return createTime;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column ad_app.create_time
	 * @param createTime  the value for ad_app.create_time
	 * @mbg.generated  Wed May 30 17:27:30 CST 2018
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column ad_app.update_time
	 * @return  the value of ad_app.update_time
	 * @mbg.generated  Wed May 30 17:27:30 CST 2018
	 */
	public Date getUpdateTime() {
		return updateTime;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column ad_app.update_time
	 * @param updateTime  the value for ad_app.update_time
	 * @mbg.generated  Wed May 30 17:27:30 CST 2018
	 */
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
}