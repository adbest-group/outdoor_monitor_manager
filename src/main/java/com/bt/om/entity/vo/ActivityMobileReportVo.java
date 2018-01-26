package com.bt.om.entity.vo;

import com.bt.om.entity.AdActivity;
import com.bt.om.vo.api.BasicVo;

/**
 * Created by caiting on 2018/1/26.
 */
public class ActivityMobileReportVo extends AdActivity {
    private Integer activityId;    //活动ID
    private String activityName;   //活动名称
    private Integer deliveryNum;   //投放中
    private Integer monitorNum; //监测中
    private Integer errorNum;  //报错

    public Integer getActivityId() {
        return activityId;
    }

    public void setActivityId(Integer activityId) {
        this.activityId = activityId;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public Integer getDeliveryNum() {
        return deliveryNum;
    }

    public void setDeliveryNum(Integer deliveryNum) {
        this.deliveryNum = deliveryNum;
    }

    public Integer getMonitorNum() {
        return monitorNum;
    }

    public void setMonitorNum(Integer monitorNum) {
        this.monitorNum = monitorNum;
    }

    public Integer getErrorNum() {
        return errorNum;
    }

    public void setErrorNum(Integer errorNum) {
        this.errorNum = errorNum;
    }
}
