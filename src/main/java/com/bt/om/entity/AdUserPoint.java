package com.bt.om.entity;

import java.util.Date;

public class AdUserPoint {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ad_user_point.id
     *
     * @mbg.generated Thu May 31 11:25:13 CST 2018
     */
    private Integer id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ad_user_point.user_id
     *
     * @mbg.generated Thu May 31 11:25:13 CST 2018
     */
    private Integer userId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ad_user_point.point
     *
     * @mbg.generated Thu May 31 11:25:13 CST 2018
     */
    private Integer point;
    /**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column ad_point.result
	 * @mbg.generated  Thu May 31 11:11:28 CST 2018
	 */
	private String result;
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ad_user_point.create_time
     *
     * @mbg.generated Thu May 31 11:25:13 CST 2018
     */
    
    private Date createTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ad_user_point.update_time
     *
     * @mbg.generated Thu May 31 11:25:13 CST 2018
     */
    private Date updateTime;

    private String createTimeStr;
    
    private String updateTimeStr;
    
    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ad_user_point.id
     *
     * @return the value of ad_user_point.id
     *
     * @mbg.generated Thu May 31 11:25:13 CST 2018
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ad_user_point.id
     *
     * @param id the value for ad_user_point.id
     *
     * @mbg.generated Thu May 31 11:25:13 CST 2018
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ad_user_point.user_id
     *
     * @return the value of ad_user_point.user_id
     *
     * @mbg.generated Thu May 31 11:25:13 CST 2018
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ad_user_point.user_id
     *
     * @param userId the value for ad_user_point.user_id
     *
     * @mbg.generated Thu May 31 11:25:13 CST 2018
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ad_user_point.point
     *
     * @return the value of ad_user_point.point
     *
     * @mbg.generated Thu May 31 11:25:13 CST 2018
     */
    public Integer getPoint() {
        return point;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ad_user_point.point
     *
     * @param point the value for ad_user_point.point
     *
     * @mbg.generated Thu May 31 11:25:13 CST 2018
     */
    public void setPoint(Integer point) {
        this.point = point;
    }
    /**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column ad_point.result
	 * @return  the value of ad_point.point
	 * @mbg.generated  Thu May 31 11:11:28 CST 2018
	 */
	public String getResult() {
		return result;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column ad_point.result
	 * @param point  the value for ad_point.point
	 * @mbg.generated  Thu May 31 11:11:28 CST 2018
	 */
	public void setResult(String result) {
		this.result = result;
	}
    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ad_user_point.create_time
     *
     * @return the value of ad_user_point.create_time
     *
     * @mbg.generated Thu May 31 11:25:13 CST 2018
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ad_user_point.create_time
     *
     * @param createTime the value for ad_user_point.create_time
     *
     * @mbg.generated Thu May 31 11:25:13 CST 2018
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ad_user_point.update_time
     *
     * @return the value of ad_user_point.update_time
     *
     * @mbg.generated Thu May 31 11:25:13 CST 2018
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ad_user_point.update_time
     *
     * @param updateTime the value for ad_user_point.update_time
     *
     * @mbg.generated Thu May 31 11:25:13 CST 2018
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
    
    public String getCreateTimeStr() {
		return createTimeStr;
	}
    
    public void setCreateTimeStr(String createTimeStr) {
		this.createTimeStr = createTimeStr;
	}

	public String getUpdateTimeStr() {
		return updateTimeStr;
	}

	public void setUpdateTimeStr(String updateTimeStr) {
		this.updateTimeStr = updateTimeStr;
	}
    
    
}