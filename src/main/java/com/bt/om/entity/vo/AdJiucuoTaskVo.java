package com.bt.om.entity.vo;

import com.bt.om.entity.AdJiucuoTask;

import java.util.Date;

/**
 * Created by caiting on 2018/1/21.
 */
public class AdJiucuoTaskVo extends AdJiucuoTask {
    private String picUrl1;
    private String activityName;
    private Long province;
    private Long city;
    private Long region;
    private Long street;
    private Integer activityAdseatId;
    private Integer mediaId;
    private String mediaName;
    private String adSeatName;
    private String realname;
    private String problem;
    private String problemOther;
    private Date feedbackTime;

    public String getProblem() {
        return problem;
    }

    public void setProblem(String problem) {
        this.problem = problem;
    }

    public String getProblemOther() {
        return problemOther;
    }

    public void setProblemOther(String problemOther) {
        this.problemOther = problemOther;
    }

    public Date getFeedbackTime() {
        return feedbackTime;
    }

    public void setFeedbackTime(Date feedbackTime) {
        this.feedbackTime = feedbackTime;
    }

    public String getPicUrl1() {
        return picUrl1;
    }

    public void setPicUrl1(String picUrl1) {
        this.picUrl1 = picUrl1;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public Long getProvince() {
        return province;
    }

    public void setProvince(Long province) {
        this.province = province;
    }

    public Long getCity() {
        return city;
    }

    public void setCity(Long city) {
        this.city = city;
    }

    public Long getRegion() {
        return region;
    }

    public void setRegion(Long region) {
        this.region = region;
    }

    public Long getStreet() {
        return street;
    }

    public void setStreet(Long street) {
        this.street = street;
    }

    public Integer getActivityAdseatId() {
        return activityAdseatId;
    }

    public void setActivityAdseatId(Integer activityAdseatId) {
        this.activityAdseatId = activityAdseatId;
    }

    public Integer getMediaId() {
        return mediaId;
    }

    public void setMediaId(Integer mediaId) {
        this.mediaId = mediaId;
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
}
