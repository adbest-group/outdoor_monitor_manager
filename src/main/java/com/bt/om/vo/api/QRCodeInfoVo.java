package com.bt.om.vo.api;

import com.bt.om.entity.vo.AdActivityAdseatVo;

import java.io.Serializable;
import java.sql.JDBCType;

/**
 * Created by caiting on 2018/1/25.
 */
public class QRCodeInfoVo implements Serializable {
    private Integer ad_activity_seat_id;
    private String activity_name;
    private String ad_seat_name;
    private String pic_url;
    private Double lon;
    private Double lat;

    public QRCodeInfoVo(AdActivityAdseatVo vo){
        this.ad_activity_seat_id = vo.getId();
        this.activity_name = vo.getActivityName();
        this.ad_seat_name = vo.getAdSeatName();
        this.pic_url = vo.getSamplePicUrl();
        this.lon = vo.getLon();
        this.lat = vo.getLat();
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

    public Integer getAd_activity_seat_id() {
        return ad_activity_seat_id;
    }

    public void setAd_activity_seat_id(Integer ad_activity_seat_id) {
        this.ad_activity_seat_id = ad_activity_seat_id;
    }

    public String getActivity_name() {
        return activity_name;
    }

    public void setActivity_name(String activity_name) {
        this.activity_name = activity_name;
    }

    public String getAd_seat_name() {
        return ad_seat_name;
    }

    public void setAd_seat_name(String ad_seat_name) {
        this.ad_seat_name = ad_seat_name;
    }

    public String getPic_url() {
        return pic_url;
    }

    public void setPic_url(String pic_url) {
        this.pic_url = pic_url;
    }
}
