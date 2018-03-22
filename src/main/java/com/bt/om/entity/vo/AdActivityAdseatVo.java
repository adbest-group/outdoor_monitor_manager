package com.bt.om.entity.vo;

import com.bt.om.entity.AdActivityAdseat;

/**
 * Created by caiting on 2018/1/19.
 */
public class AdActivityAdseatVo extends AdActivityAdseat {
    private String adSeatName;
    private String mediaName;
    private String activityName;
    private Double lon;
    private Double lat;

    public String getAdSeatName() {
        return adSeatName;
    }

    public void setAdSeatName(String adSeatName) {
        this.adSeatName = adSeatName;
    }

    public String getMediaName() {
        return mediaName;
    }

    public void setMediaName(String mediaName) {
        this.mediaName = mediaName;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }
}
