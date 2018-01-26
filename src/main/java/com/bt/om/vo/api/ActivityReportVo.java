package com.bt.om.vo.api;

import com.bt.om.entity.vo.ActivityMobileReportVo;

/**
 * Created by caiting on 2018/1/26.
 */
public class ActivityReportVo extends BasicVo {
    private Integer activity_id;
    private String activity_name;
    private Integer delivery_num;
    private Integer monitor_num;
    private Integer error_num;

    public ActivityReportVo(ActivityMobileReportVo vo){
        this.activity_id = vo.getId();
        this.activity_name = vo.getActivityName();
        this.delivery_num = vo.getDeliveryNum();
        this.monitor_num = vo.getMonitorNum();
        this.error_num = vo.getErrorNum();
    }

    public Integer getActivity_id() {
        return activity_id;
    }

    public void setActivity_id(Integer activity_id) {
        this.activity_id = activity_id;
    }

    public String getActivity_name() {
        return activity_name;
    }

    public void setActivity_name(String activity_name) {
        this.activity_name = activity_name;
    }

    public Integer getDelivery_num() {
        return delivery_num;
    }

    public void setDelivery_num(Integer delivery_num) {
        this.delivery_num = delivery_num;
    }

    public Integer getMonitor_num() {
        return monitor_num;
    }

    public void setMonitor_num(Integer monitor_num) {
        this.monitor_num = monitor_num;
    }

    public Integer getError_num() {
        return error_num;
    }

    public void setError_num(Integer error_num) {
        this.error_num = error_num;
    }
}
