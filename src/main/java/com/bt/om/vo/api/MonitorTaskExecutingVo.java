package com.bt.om.vo.api;

import java.util.ArrayList;
import java.util.List;

import com.bt.om.common.DateUtil;
import com.bt.om.entity.vo.AdMonitorTaskMobileVo;
import com.bt.om.enums.MonitorTaskType;

/**
 * Created by caiting on 2018/1/24.
 */
public class MonitorTaskExecutingVo extends BasicVo {
	private static final long serialVersionUID = -2193883337064835653L;
	private Integer task_id;
    private Integer task_type;
    private String task_type_text;
    private String ad_activity_name;
    private String ad_name;
    private String monitor_time;
    private Integer ad_seat_id;
    private String ad_location;
    private Integer ad_status;
    private List<String> img_url_list;
    private String problem;
    private String problem_other;
    private Double lat;
    private Double lon;
    private Double feedback_lat;
    private Double feedback_lon;
    private String region;
    private String street;
    private String startTime;
    private String endTime;
    private Integer assignType;
    private String province;
    private String city;
    private Integer adCodeFlag;
    private String adCode;
    private String road;
    private Integer task_point;
    private Double task_money;
    private String qualifiedPicUrl;
    private String noQualifiedPicUrl1;
    private String noQualifiedPicUrl2;
    private String noQualifiedPicUrl3;
    private String noQualifiedText1;
    private String noQualifiedText2;
    private String noQualifiedText3;
    private String notification;
    
    public MonitorTaskExecutingVo(AdMonitorTaskMobileVo task){
        this.task_id = task.getId();
        this.task_type = task.getTaskType();
        this.task_type_text = MonitorTaskType.getText(task.getTaskType());
        this.ad_activity_name = task.getActivityName();
        this.ad_name = task.getAdSeatName();
        if(task.getFeedbackTime() != null) {
        	this.monitor_time = sdf.format(task.getFeedbackTime());
        }
        this.ad_seat_id = task.getAdSeatId();
        this.ad_location = task.getAdSeatLocation();
        this.ad_status = task.getStatus();
        this.img_url_list = new ArrayList<>();
        this.img_url_list.add(task.getPicUrl1());
        this.img_url_list.add(task.getPicUrl2());
        this.img_url_list.add(task.getPicUrl3());
        this.img_url_list.add(task.getPicUrl4());
        this.problem = task.getProblem();
        this.problem_other = task.getProblemOther();
        this.lon = task.getLon();
        this.lat = task.getLat();
        this.feedback_lat = task.getFeedbackLat();
        this.feedback_lon = task.getFeedbackLon();
        this.road = task.getRoad();
        if(task.getStartTime() != null) {
        	this.startTime = DateUtil.dateFormate(task.getStartTime(), "yyyy-MM-dd HH:mm:ss");
        }
        if(task.getEndTime() != null) {
        	this.endTime = DateUtil.dateFormate(task.getEndTime(), "yyyy-MM-dd HH:mm:ss");
        }
        this.assignType = task.getAssignType();
        this.adCodeFlag = task.getAdCodeFlag();
        this.adCode = task.getAdCode();
        this.task_point = task.getTaskPoint();
        this.task_money = task.getTaskMoney();
        this.qualifiedPicUrl = task.getQualifiedPicUrl();
		this.noQualifiedPicUrl1 = task.getNoQualifiedPicUrl1();
		this.noQualifiedPicUrl2 = task.getNoQualifiedPicUrl2();
		this.noQualifiedPicUrl3 = task.getNoQualifiedPicUrl3();
		this.noQualifiedText1 = task.getNoQualifiedText1();
		this.noQualifiedText2 = task.getNoQualifiedText2();
		this.noQualifiedText3 = task.getNoQualifiedText3();
		this.notification = task.getNotification();
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

    public String getAdCode() {
		return adCode;
	}


	public void setAdCode(String adCode) {
		this.adCode = adCode;
	}


	public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public Double getFeedback_lat() {
        return feedback_lat;
    }

    public void setFeedback_lat(Double feedback_lat) {
        this.feedback_lat = feedback_lat;
    }

    public Double getFeedback_lon() {
        return feedback_lon;
    }

    public void setFeedback_lon(Double feedback_lon) {
        this.feedback_lon = feedback_lon;
    }

    public Integer getAd_seat_id() {
        return ad_seat_id;
    }

    public void setAd_seat_id(Integer ad_seat_id) {
        this.ad_seat_id = ad_seat_id;
    }

    public String getProblem() {
        return problem;
    }

    public void setProblem(String problem) {
        this.problem = problem;
    }

    public String getProblem_other() {
        return problem_other;
    }

    public void setProblem_other(String problem_other) {
        this.problem_other = problem_other;
    }

    public Integer getTask_id() {
        return task_id;
    }

    public void setTask_id(Integer task_id) {
        this.task_id = task_id;
    }

    public Integer getTask_type() {
        return task_type;
    }

    public void setTask_type(Integer task_type) {
        this.task_type = task_type;
    }

    public String getTask_type_text() {
        return task_type_text;
    }

    public void setTask_type_text(String task_type_text) {
        this.task_type_text = task_type_text;
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
    
    public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public Integer getAssignType() {
		return assignType;
	}

	public void setAssignType(Integer assignType) {
		this.assignType = assignType;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
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

	public Integer getTask_point() {
		return task_point;
	}

	public void setTask_point(Integer task_point) {
		this.task_point = task_point;
	}

	public Double getTask_money() {
		return task_money;
	}

	public void setTask_money(Double task_money) {
		this.task_money = task_money;
	}


	
}
