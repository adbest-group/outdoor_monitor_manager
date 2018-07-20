package com.bt.om.vo.api;

import com.bt.om.entity.vo.AdMonitorTaskMobileVo;
import com.bt.om.enums.MonitorTaskType;

/**
 * Created by caiting on 2018/1/24.
 */
public class MonitorTaskArroundVo extends BasicVo {
    private Integer task_id;
    private Integer task_type;
    private String task_type_text;
    private String ad_activity_name;
    private String ad_name;
    private String monitor_time;
    private String monitor_start;
    private String monitor_end;
    private Integer ad_seat_id;
    private String ad_seat_code;
    private String ad_location;
    private Integer ad_status;
    private String sample_url;
    private Double lat;
    private Double lon;
    private Double distance;
    private String region;
    private String street;
    private String province;
    private String city;
    private String startTime;
    private String endTime;
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
    
    public MonitorTaskArroundVo(AdMonitorTaskMobileVo task){
        this.task_id = task.getId();
        this.task_type = task.getTaskType();
        this.task_type_text = MonitorTaskType.getText(task.getTaskType());
        this.ad_activity_name = task.getActivityName();
        this.ad_name = task.getAdSeatName();
        this.monitor_start = sdf.format(task.getMonitorStart());
        this.monitor_end = sdf.format(task.getMonitorEnd());
        this.ad_seat_id = task.getAdSeatId();
        this.ad_seat_code = task.getAdSeatCode();
        this.ad_location = task.getAdSeatLocation();
        this.ad_status = task.getStatus();
        this.sample_url = task.getSamplePicUrl();
        this.lon = task.getLon();
        this.lat = task.getLat();
        this.distance = task.getDistance();
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



	public String getAd_seat_code() {
        return ad_seat_code;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
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

    public void setAd_seat_code(String ad_seat_code) {
        this.ad_seat_code = ad_seat_code;
    }

    public Integer getAd_seat_id() {
        return ad_seat_id;
    }

    public void setAd_seat_id(Integer ad_seat_id) {
        this.ad_seat_id = ad_seat_id;
    }

    public String getSample_url() {
        return sample_url;
    }

    public void setSample_url(String sample_url) {
        this.sample_url = sample_url;
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

    public String getMonitor_start() {
        return monitor_start;
    }

    public void setMonitor_start(String monitor_start) {
        this.monitor_start = monitor_start;
    }

    public String getMonitor_end() {
        return monitor_end;
    }

    public void setMonitor_end(String monitor_end) {
        this.monitor_end = monitor_end;
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
}
