package com.bt.om.vo.api;

import com.bt.om.entity.vo.AdActivityAdseatVo;
import com.google.common.collect.Lists;

import java.io.Serializable;
import java.sql.JDBCType;
import java.util.List;

/**
 * Created by caiting on 2018/1/25.
 */
public class QRCodeInfoVo implements Serializable {
    private Integer ad_seat_id;
    private List<AdActivitySeatInfoInQRVO> ad_activity_seats;

    public QRCodeInfoVo(){
        ad_activity_seats = Lists.newArrayList();
    }

    public Integer getAd_seat_id() {
        return ad_seat_id;
    }

    public void setAd_seat_id(Integer ad_seat_id) {
        this.ad_seat_id = ad_seat_id;
    }

    public List<AdActivitySeatInfoInQRVO> getAd_activity_seats() {
        return ad_activity_seats;
    }

    public void setAd_activity_seats(List<AdActivitySeatInfoInQRVO> ad_activity_seats) {
        this.ad_activity_seats = ad_activity_seats;
    }
}
