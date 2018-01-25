package com.bt.om.entity.vo;

import com.bt.om.entity.AdMonitorTask;

import java.util.Date;

/**
 * Created by caiting on 2018/1/24.
 */
public class AdMonitorTaskMobileVo extends AdMonitorTask {
    private String activityName;
    private String adSeatName;
    private String samplePicUrl;
    private String picUrl1;
    private String picUrl2;
    private String picUrl3;
    private String picUrl4;
    private String reason;
    private Date monitorStart;
    private Date monitorEnd;
    private Date feedbackTime;

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public String getAdSeatName() {
        return adSeatName;
    }

    public void setAdSeatName(String adSeatName) {
        this.adSeatName = adSeatName;
    }

    public String getSamplePicUrl() {
        return samplePicUrl;
    }

    public void setSamplePicUrl(String samplePicUrl) {
        this.samplePicUrl = samplePicUrl;
    }

    public String getPicUrl1() {
        return picUrl1;
    }

    public void setPicUrl1(String picUrl1) {
        this.picUrl1 = picUrl1;
    }

    public String getPicUrl2() {
        return picUrl2;
    }

    public void setPicUrl2(String picUrl2) {
        this.picUrl2 = picUrl2;
    }

    public String getPicUrl3() {
        return picUrl3;
    }

    public void setPicUrl3(String picUrl3) {
        this.picUrl3 = picUrl3;
    }

    public String getPicUrl4() {
        return picUrl4;
    }

    public void setPicUrl4(String picUrl4) {
        this.picUrl4 = picUrl4;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Date getMonitorStart() {
        return monitorStart;
    }

    public void setMonitorStart(Date monitorStart) {
        this.monitorStart = monitorStart;
    }

    public Date getMonitorEnd() {
        return monitorEnd;
    }

    public void setMonitorEnd(Date monitorEnd) {
        this.monitorEnd = monitorEnd;
    }

    public Date getFeedbackTime() {
        return feedbackTime;
    }

    public void setFeedbackTime(Date feedbackTime) {
        this.feedbackTime = feedbackTime;
    }
}
