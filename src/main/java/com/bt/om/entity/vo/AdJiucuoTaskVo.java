package com.bt.om.entity.vo;

import com.bt.om.entity.AdJiucuoTask;

/**
 * Created by caiting on 2018/1/21.
 */
public class AdJiucuoTaskVo extends AdJiucuoTask {
    private String picUrl1;
    private String activityName;
    private Integer province;
    private Integer city;
    private Integer region;
    private Integer street;
    private Integer activityAdseatId;
    private Integer mediaId;
    private String mediaName;
    private String adSeatName;
    private String realname;

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
