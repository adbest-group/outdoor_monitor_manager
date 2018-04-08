package com.bt.om.entity;

import java.util.Date;

public class AdVersion {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ad_version.id
     *
     * @mbg.generated Tue Apr 03 15:33:52 CST 2018
     */
    private Integer id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ad_version.app_version
     *
     * @mbg.generated Tue Apr 03 15:33:52 CST 2018
     */
    private String appVersion;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ad_version.api_version
     *
     * @mbg.generated Tue Apr 03 15:33:52 CST 2018
     */
    private String apiVersion;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ad_version.is_force_update
     *
     * @mbg.generated Tue Apr 03 15:33:52 CST 2018
     */
    private Integer isForceUpdate;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ad_version.create_time
     *
     * @mbg.generated Tue Apr 03 15:33:52 CST 2018
     */
    private Date createTime;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ad_version.id
     *
     * @return the value of ad_version.id
     *
     * @mbg.generated Tue Apr 03 15:33:52 CST 2018
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ad_version.id
     *
     * @param id the value for ad_version.id
     *
     * @mbg.generated Tue Apr 03 15:33:52 CST 2018
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ad_version.app_version
     *
     * @return the value of ad_version.app_version
     *
     * @mbg.generated Tue Apr 03 15:33:52 CST 2018
     */
    public String getAppVersion() {
        return appVersion;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ad_version.app_version
     *
     * @param appVersion the value for ad_version.app_version
     *
     * @mbg.generated Tue Apr 03 15:33:52 CST 2018
     */
    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion == null ? null : appVersion.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ad_version.api_version
     *
     * @return the value of ad_version.api_version
     *
     * @mbg.generated Tue Apr 03 15:33:52 CST 2018
     */
    public String getApiVersion() {
        return apiVersion;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ad_version.api_version
     *
     * @param apiVersion the value for ad_version.api_version
     *
     * @mbg.generated Tue Apr 03 15:33:52 CST 2018
     */
    public void setApiVersion(String apiVersion) {
        this.apiVersion = apiVersion == null ? null : apiVersion.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ad_version.is_force_update
     *
     * @return the value of ad_version.is_force_update
     *
     * @mbg.generated Tue Apr 03 15:33:52 CST 2018
     */
    public Integer getIsForceUpdate() {
        return isForceUpdate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ad_version.is_force_update
     *
     * @param isForceUpdate the value for ad_version.is_force_update
     *
     * @mbg.generated Tue Apr 03 15:33:52 CST 2018
     */
    public void setIsForceUpdate(Integer isForceUpdate) {
        this.isForceUpdate = isForceUpdate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ad_version.create_time
     *
     * @return the value of ad_version.create_time
     *
     * @mbg.generated Tue Apr 03 15:33:52 CST 2018
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ad_version.create_time
     *
     * @param createTime the value for ad_version.create_time
     *
     * @mbg.generated Tue Apr 03 15:33:52 CST 2018
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}