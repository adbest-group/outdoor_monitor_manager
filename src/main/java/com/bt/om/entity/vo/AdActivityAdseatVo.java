package com.bt.om.entity.vo;

import com.bt.om.entity.AdActivityAdseat;

/**
 * Created by caiting on 2018/1/19.
 */
public class AdActivityAdseatVo extends AdActivityAdseat {
    private String adSeatName;
    private String mediaName;

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
}
