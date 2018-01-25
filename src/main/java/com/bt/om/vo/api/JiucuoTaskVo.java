package com.bt.om.vo.api;

import com.bt.om.entity.vo.AdJiucuoTaskMobileVo;
import com.fasterxml.jackson.databind.ser.Serializers;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * Created by caiting on 2018/1/25.
 */
public class JiucuoTaskVo extends BasicVo{
    private Integer task_id;
    private String ad_activity_name;
    private String ad_name;
    private String monitor_time;
    private String ad_location;
    private Integer ad_status;
    private List<String> img_url_list;
    private String reason;

    public JiucuoTaskVo(AdJiucuoTaskMobileVo task){
        this.task_id = task.getId();
        this.ad_activity_name = task.getActivityName();
        this.ad_name = task.getAdSeatName();
        this.monitor_time = sdf.format(task.getFeedbackTime());
        this.ad_location = task.getAdSeatName();
        this.ad_status = task.getStatus();
        this.reason = task.getReason();
        this.img_url_list = Lists.newArrayList();
        this.img_url_list.add(task.getPicUrl1());
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Integer getTask_id() {
        return task_id;
    }

    public void setTask_id(Integer task_id) {
        this.task_id = task_id;
    }

    public String getAd_activity_name() {
        return ad_activity_name;
    }

    public void setAd_activity_name(String ad_activity_name) {
        this.ad_activity_name = ad_activity_name;
    }

    public String getAd_name() {
        return ad_name;
    }

    public void setAd_name(String ad_name) {
        this.ad_name = ad_name;
    }

    public String getMonitor_time() {
        return monitor_time;
    }

    public void setMonitor_time(String monitor_time) {
        this.monitor_time = monitor_time;
    }

    public String getAd_location() {
        return ad_location;
    }

    public void setAd_location(String ad_location) {
        this.ad_location = ad_location;
    }

    public Integer getAd_status() {
        return ad_status;
    }

    public void setAd_status(Integer ad_status) {
        this.ad_status = ad_status;
    }

    public List<String> getImg_url_list() {
        return img_url_list;
    }

    public void setImg_url_list(List<String> img_url_list) {
        this.img_url_list = img_url_list;
    }
}
