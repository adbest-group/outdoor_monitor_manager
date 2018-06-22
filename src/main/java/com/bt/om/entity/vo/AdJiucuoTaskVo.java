package com.bt.om.entity.vo;

import java.util.Date;

import com.bt.om.entity.AdJiucuoTask;

/**
 * Created by caiting on 2018/1/21.
 */
public class AdJiucuoTaskVo extends AdJiucuoTask {
    private String picUrl1;
    private String activityName;
    private Long province;
    private Long city;
    private Long region;
    private Long street;
    private String road;
    private String location;
    private Integer activityAdseatId;
    private Integer mediaId;
    private Integer mediaTypeParentId;//媒体大类
    private Integer mediaTypeId;//媒体小类
    private String mediaName;
    private String adSeatName;
    private String realname;
    private String problem;
    private String problemOther;
    private Date feedbackTime;
    private Date startTime;
    private Date endTime;
    private String assessorName;
    private Integer feedbackId;
    
    public Integer getFeedbackId() {
		return feedbackId;
	}

	public void setFeedbackId(Integer feedbackId) {
		this.feedbackId = feedbackId;
	}

	public Integer getMediaTypeParentId() {
		return mediaTypeParentId;
	}

	public void setMediaTypeParentId(Integer mediaTypeParentId) {
		this.mediaTypeParentId = mediaTypeParentId;
	}

	public Integer getMediaTypeId() {
		return mediaTypeId;
	}

	public void setMediaTypeId(Integer mediaTypeId) {
		this.mediaTypeId = mediaTypeId;
	}

	public String getProblem() {
        return problem;
    }

    public void setProblem(String problem) {
        this.problem = problem;
    }

    public String getProblemOther() {
        return problemOther;
    }

    public void setProblemOther(String problemOther) {
        this.problemOther = problemOther;
    }

    public Date getFeedbackTime() {
        return feedbackTime;
    }

    public void setFeedbackTime(Date feedbackTime) {
        this.feedbackTime = feedbackTime;
    }

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

    public Long getProvince() {
        return province;
    }

    public void setProvince(Long province) {
        this.province = province;
    }

    public Long getCity() {
        return city;
    }

    public void setCity(Long city) {
        this.city = city;
    }

    public Long getRegion() {
        return region;
    }

    public void setRegion(Long region) {
        this.region = region;
    }

    public Long getStreet() {
        return street;
    }

    public void setStreet(Long street) {
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
	
	public String getAssessorName() {
		return assessorName;
	}
	
	public void setAssessorName(String assessorName) {
		this.assessorName = assessorName;
	}

	public String getRoad() {
		return road;
	}

	public void setRoad(String road) {
		this.road = road;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}
}
