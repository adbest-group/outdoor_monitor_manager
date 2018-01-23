package com.bt.om.entity.vo;

import com.bt.om.entity.AdMonitorTask;

import java.util.Date;

/**
 * Created by caiting on 2018/1/20.
 */
public class AdMonitorTaskVo extends AdMonitorTask {
    private String activityName;
    private String samplePicUrl;
    private Date startTime;
    private Date endTime;
    private Integer province;
    private Integer city;
    private Integer region;
    private Integer street;
    private String mediaName;
    private String adSeatName;
    private String realname;

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
}
