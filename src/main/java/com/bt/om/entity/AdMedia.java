package com.bt.om.entity;

import java.util.Date;

public class AdMedia {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ad_media.id
     *
     * @mbggenerated Thu Feb 01 14:08:19 CST 2018
     */
    private Integer id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ad_media.media_name
     *
     * @mbggenerated Thu Feb 01 14:08:19 CST 2018
     */
    private String mediaName;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ad_media.create_time
     *
     * @mbggenerated Thu Feb 01 14:08:19 CST 2018
     */
    private Date createTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ad_media.update_time
     *
     * @mbggenerated Thu Feb 01 14:08:19 CST 2018
     */
    private Date updateTime;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ad_media.id
     *
     * @return the value of ad_media.id
     *
     * @mbggenerated Thu Feb 01 14:08:19 CST 2018
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ad_media.id
     *
     * @param id the value for ad_media.id
     *
     * @mbggenerated Thu Feb 01 14:08:19 CST 2018
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ad_media.media_name
     *
     * @return the value of ad_media.media_name
     *
     * @mbggenerated Thu Feb 01 14:08:19 CST 2018
     */
    public String getMediaName() {
        return mediaName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ad_media.media_name
     *
     * @param mediaName the value for ad_media.media_name
     *
     * @mbggenerated Thu Feb 01 14:08:19 CST 2018
     */
    public void setMediaName(String mediaName) {
        this.mediaName = mediaName == null ? null : mediaName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ad_media.create_time
     *
     * @return the value of ad_media.create_time
     *
     * @mbggenerated Thu Feb 01 14:08:19 CST 2018
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ad_media.create_time
     *
     * @param createTime the value for ad_media.create_time
     *
     * @mbggenerated Thu Feb 01 14:08:19 CST 2018
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ad_media.update_time
     *
     * @return the value of ad_media.update_time
     *
     * @mbggenerated Thu Feb 01 14:08:19 CST 2018
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ad_media.update_time
     *
     * @param updateTime the value for ad_media.update_time
     *
     * @mbggenerated Thu Feb 01 14:08:19 CST 2018
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}