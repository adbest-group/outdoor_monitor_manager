package com.bt.om.entity;

import java.util.Date;

public class AdSeatType {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ad_seat_type.id
     *
     * @mbggenerated Thu Feb 01 14:08:19 CST 2018
     */
    private Integer id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ad_seat_type.name
     *
     * @mbggenerated Thu Feb 01 14:08:19 CST 2018
     */
    private String name;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ad_seat_type.create_time
     *
     * @mbggenerated Thu Feb 01 14:08:19 CST 2018
     */
    private Date createTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ad_seat_type.update_time
     *
     * @mbggenerated Thu Feb 01 14:08:19 CST 2018
     */
    private Date updateTime;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ad_seat_type.id
     *
     * @return the value of ad_seat_type.id
     *
     * @mbggenerated Thu Feb 01 14:08:19 CST 2018
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ad_seat_type.id
     *
     * @param id the value for ad_seat_type.id
     *
     * @mbggenerated Thu Feb 01 14:08:19 CST 2018
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ad_seat_type.name
     *
     * @return the value of ad_seat_type.name
     *
     * @mbggenerated Thu Feb 01 14:08:19 CST 2018
     */
    public String getName() {
        return name;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ad_seat_type.name
     *
     * @param name the value for ad_seat_type.name
     *
     * @mbggenerated Thu Feb 01 14:08:19 CST 2018
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ad_seat_type.create_time
     *
     * @return the value of ad_seat_type.create_time
     *
     * @mbggenerated Thu Feb 01 14:08:19 CST 2018
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ad_seat_type.create_time
     *
     * @param createTime the value for ad_seat_type.create_time
     *
     * @mbggenerated Thu Feb 01 14:08:19 CST 2018
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ad_seat_type.update_time
     *
     * @return the value of ad_seat_type.update_time
     *
     * @mbggenerated Thu Feb 01 14:08:19 CST 2018
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ad_seat_type.update_time
     *
     * @param updateTime the value for ad_seat_type.update_time
     *
     * @mbggenerated Thu Feb 01 14:08:19 CST 2018
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}