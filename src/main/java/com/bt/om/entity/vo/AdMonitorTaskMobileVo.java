package com.bt.om.entity.vo;

import com.bt.om.entity.AdMonitorTask;

import java.util.Date;

/**
 * Created by caiting on 2018/1/24.
 */
public class AdMonitorTaskMobileVo extends AdMonitorTask {
	private static final long serialVersionUID = 21928227421439115L;
	private String activityName;
    private Integer adSeatId;
    private String adSeatCode;
    private String adSeatName;
    private String adSeatLocation;
    private String samplePicUrl;
    private String picUrl1;
    private String picUrl2;
    private String picUrl3;
    private String picUrl4;
    private String reason;
    private Date monitorStart;
    private Date monitorEnd;
    private Date feedbackTime;
    private String problem;
    private String problemOther;
    private Double lon;
    private Double lat;
    private Double feedbackLon;
    private Double feedbackLat;
    private Double distance;
    private Long region;
    private Long street;
    private Date startTime;
    private Date endTime;
    private Integer assignType;
    private Long province;
    private Long city;
    private Integer adCodeFlag;
    private String adCode;
    private String memo;
    private String road;
    
    private String qualifiedPicUrl;
    private String noQualifiedPicUrl1;
    private String noQualifiedPicUrl2;
    private String noQualifiedPicUrl3;
    private String noQualifiedText1;
    private String noQualifiedText2;
    private String noQualifiedText3;
    private String notification;
    
    private Integer picUrl1Status;
    private Integer picUrl2Status;
    private Integer picUrl3Status;
    private Integer picUrl4Status;
    
    public Integer getPicUrl1Status() {
		return picUrl1Status;
	}

	public void setPicUrl1Status(Integer picUrl1Status) {
		this.picUrl1Status = picUrl1Status;
	}

	public Integer getPicUrl2Status() {
		return picUrl2Status;
	}

	public void setPicUrl2Status(Integer picUrl2Status) {
		this.picUrl2Status = picUrl2Status;
	}

	public Integer getPicUrl3Status() {
		return picUrl3Status;
	}

	public void setPicUrl3Status(Integer picUrl3Status) {
		this.picUrl3Status = picUrl3Status;
	}

	public Integer getPicUrl4Status() {
		return picUrl4Status;
	}

	public void setPicUrl4Status(Integer picUrl4Status) {
		this.picUrl4Status = picUrl4Status;
	}

	public String getQualifiedPicUrl() {
		return qualifiedPicUrl;
	}

	public void setQualifiedPicUrl(String qualifiedPicUrl) {
		this.qualifiedPicUrl = qualifiedPicUrl;
	}

	public String getNoQualifiedPicUrl1() {
		return noQualifiedPicUrl1;
	}

	public void setNoQualifiedPicUrl1(String noQualifiedPicUrl1) {
		this.noQualifiedPicUrl1 = noQualifiedPicUrl1;
	}

	public String getNoQualifiedPicUrl2() {
		return noQualifiedPicUrl2;
	}

	public void setNoQualifiedPicUrl2(String noQualifiedPicUrl2) {
		this.noQualifiedPicUrl2 = noQualifiedPicUrl2;
	}

	public String getNoQualifiedPicUrl3() {
		return noQualifiedPicUrl3;
	}

	public void setNoQualifiedPicUrl3(String noQualifiedPicUrl3) {
		this.noQualifiedPicUrl3 = noQualifiedPicUrl3;
	}

	public String getNoQualifiedText1() {
		return noQualifiedText1;
	}

	public void setNoQualifiedText1(String noQualifiedText1) {
		this.noQualifiedText1 = noQualifiedText1;
	}

	public String getNoQualifiedText2() {
		return noQualifiedText2;
	}

	public void setNoQualifiedText2(String noQualifiedText2) {
		this.noQualifiedText2 = noQualifiedText2;
	}

	public String getNoQualifiedText3() {
		return noQualifiedText3;
	}

	public void setNoQualifiedText3(String noQualifiedText3) {
		this.noQualifiedText3 = noQualifiedText3;
	}

	public String getNotification() {
		return notification;
	}

	public void setNotification(String notification) {
		this.notification = notification;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getAdCode() {
		return adCode;
	}

	public void setAdCode(String adCode) {
		this.adCode = adCode;
	}

	public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public String getAdSeatLocation() {
        return adSeatLocation;
    }

    public void setAdSeatLocation(String adSeatLocation) {
        this.adSeatLocation = adSeatLocation;
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

    public Double getFeedbackLon() {
        return feedbackLon;
    }

    public void setFeedbackLon(Double feedbackLon) {
        this.feedbackLon = feedbackLon;
    }

    public Double getFeedbackLat() {
        return feedbackLat;
    }

    public void setFeedbackLat(Double feedbackLat) {
        this.feedbackLat = feedbackLat;
    }

    public String getAdSeatCode() {
        return adSeatCode;
    }

    public void setAdSeatCode(String adSeatCode) {
        this.adSeatCode = adSeatCode;
    }

    public Integer getAdSeatId() {
        return adSeatId;
    }

    public void setAdSeatId(Integer adSeatId) {
        this.adSeatId = adSeatId;
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

	public Integer getAssignType() {
		return assignType;
	}

	public void setAssignType(Integer assignType) {
		this.assignType = assignType;
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
	
	public Integer getAdCodeFlag() {
		return adCodeFlag;
	}
	
	public void setAdCodeFlag(Integer adCodeFlag) {
		this.adCodeFlag = adCodeFlag;
	}
	
	public String getRoad() {
		return road;
	}
	
	public void setRoad(String road) {
		this.road = road;
	}
}
