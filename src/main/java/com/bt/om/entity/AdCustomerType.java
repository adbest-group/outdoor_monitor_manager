package com.bt.om.entity;

import java.util.Date;

public class AdCustomerType {

	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column ad_customer_type.id
	 * @mbg.generated  Wed Apr 25 09:23:29 CST 2018
	 */
	private Integer id;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column ad_customer_type.name
	 * @mbg.generated  Wed Apr 25 09:23:29 CST 2018
	 */
	private String name;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column ad_customer_type.parent_id
	 * @mbg.generated  Wed Apr 25 09:23:29 CST 2018
	 */
	private Integer parentId;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column ad_customer_type.create_time
	 * @mbg.generated  Wed Apr 25 09:23:29 CST 2018
	 */
	private Date createTime;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column ad_customer_type.update_time
	 * @mbg.generated  Wed Apr 25 09:23:29 CST 2018
	 */
	private Date updateTime;

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column ad_customer_type.id
	 * @return  the value of ad_customer_type.id
	 * @mbg.generated  Wed Apr 25 09:23:29 CST 2018
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column ad_customer_type.id
	 * @param id  the value for ad_customer_type.id
	 * @mbg.generated  Wed Apr 25 09:23:29 CST 2018
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column ad_customer_type.name
	 * @return  the value of ad_customer_type.name
	 * @mbg.generated  Wed Apr 25 09:23:29 CST 2018
	 */
	public String getName() {
		return name;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column ad_customer_type.name
	 * @param name  the value for ad_customer_type.name
	 * @mbg.generated  Wed Apr 25 09:23:29 CST 2018
	 */
	public void setName(String name) {
		this.name = name == null ? null : name.trim();
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column ad_customer_type.parent_id
	 * @return  the value of ad_customer_type.parent_id
	 * @mbg.generated  Wed Apr 25 09:23:29 CST 2018
	 */
	public Integer getParentId() {
		return parentId;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column ad_customer_type.parent_id
	 * @param parentId  the value for ad_customer_type.parent_id
	 * @mbg.generated  Wed Apr 25 09:23:29 CST 2018
	 */
	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column ad_customer_type.create_time
	 * @return  the value of ad_customer_type.create_time
	 * @mbg.generated  Wed Apr 25 09:23:29 CST 2018
	 */
	public Date getCreateTime() {
		return createTime;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column ad_customer_type.create_time
	 * @param createTime  the value for ad_customer_type.create_time
	 * @mbg.generated  Wed Apr 25 09:23:29 CST 2018
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column ad_customer_type.update_time
	 * @return  the value of ad_customer_type.update_time
	 * @mbg.generated  Wed Apr 25 09:23:29 CST 2018
	 */
	public Date getUpdateTime() {
		return updateTime;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column ad_customer_type.update_time
	 * @param updateTime  the value for ad_customer_type.update_time
	 * @mbg.generated  Wed Apr 25 09:23:29 CST 2018
	 */
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
}