package com.bt.om.entity.vo;

import com.bt.om.entity.AdActivityAdseat;

public class AdActivityAdseatTaskVo extends AdActivityAdseat {
	private static final long serialVersionUID = 1L;
	//ad_media 相关属性
	private String mediaName;
	private String prefix;
	
	//ad_monitor_task 相关属性
	private Integer monitorTaskId; //任务主表的id
	private Integer problemStatus; //问题状态 (1：未监测 2：无问题 3：有问题 4：已解决 5：已闭环)
	private Integer status; //任务状态(1：待指派 2：待执行 3：审核中 4：通过审核 5：未通过审核)
	private String statusDesp; //未开始, 监测中, 有问题, 已结束
	private String problem;//存在问题
    private Integer feedback_status;//反馈信息
    
	//ad_seat_info 相关属性
	private Integer problem_count;
	private String info_name;
    private Long info_province;
    private Long info_city;
    private Long info_region;
    private Long info_street;
    private String info_location;
    private String info_adSize;
    private Double info_lon;
    private Double info_lat;
    private Integer info_mapStandard;
    private String info_adArea;
    private String info_contactName;
    private String info_contactCell;
    private Integer info_mediaTypeParentId;
    private Integer info_mediaTypeId;
    private String info_uniqueKey;
    private String info_memo;
    private String demoPic; //活动预览图
	private Integer info_adNum;//面数
	private String info_road;//主要路段
	private Integer info_adcode_flag;//二维码状态
	
	//ad_monitor_user_task 相关属性
    private Integer user_id;//执行用户id
    
    //sys_user_execute 相关属性
    private String username;
    private String exe_realname;
    
    //sys_user 相关属性
    private String realname;
    
    //
    public Integer getUser_id() {
		return user_id;
	}

	public void setUser_id(Integer user_id) {
		this.user_id = user_id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getExe_realname() {
		return exe_realname;
	}

	public void setExe_realname(String exe_realname) {
		this.exe_realname = exe_realname;
	}

	public Integer getInfo_adcode_flag() {
		return info_adcode_flag;
	}

	public void setInfo_adcode_flag(Integer info_adcode_flag) {
		this.info_adcode_flag = info_adcode_flag;
	}

	public String getProblem() {
		return problem;
	}

	public void setProblem(String problem) {
		this.problem = problem;
	}

	public Integer getFeedback_status() {
		return feedback_status;
	}

	public void setFeedback_status(Integer feedback_status) {
		this.feedback_status = feedback_status;
	}

	public String getRealname() {
		return realname;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}

	public String getInfo_road() {
		return info_road;
	}

	public void setInfo_road(String info_road) {
		this.info_road = info_road;
	}

	public Integer getInfo_adNum() {
		return info_adNum;
	}

	public void setInfo_adNum(Integer info_adNum) {
		this.info_adNum = info_adNum;
	}

	public String getMediaName() {
		return mediaName;
	}
    
    public void setMediaName(String mediaName) {
		this.mediaName = mediaName;
	}
    
    public String getPrefix() {
		return prefix;
	}
    
    public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
    
	public Integer getProblemStatus() {
		return problemStatus;
	}
	
	public void setProblemStatus(Integer problemStatus) {
		this.problemStatus = problemStatus;
	}
	
	public Integer getStatus() {
		return status;
	}
	
	public void setStatus(Integer status) {
		this.status = status;
	}
	
	public String getStatusDesp() {
		return statusDesp;
	}
	
	public void setStatusDesp(String statusDesp) {
		this.statusDesp = statusDesp;
	}

	public Integer getProblem_count() {
		return problem_count;
	}
	
	public void setProblem_count(Integer problem_count) {
		this.problem_count = problem_count;
	}
	
	public String getInfo_name() {
		return info_name;
	}

	public void setInfo_name(String info_name) {
		this.info_name = info_name;
	}

	public Long getInfo_province() {
		return info_province;
	}

	public void setInfo_province(Long info_province) {
		this.info_province = info_province;
	}

	public Long getInfo_city() {
		return info_city;
	}

	public void setInfo_city(Long info_city) {
		this.info_city = info_city;
	}

	public Long getInfo_region() {
		return info_region;
	}

	public void setInfo_region(Long info_region) {
		this.info_region = info_region;
	}

	public Long getInfo_street() {
		return info_street;
	}

	public void setInfo_street(Long info_street) {
		this.info_street = info_street;
	}

	public String getInfo_location() {
		return info_location;
	}

	public void setInfo_location(String info_location) {
		this.info_location = info_location;
	}

	public String getInfo_adSize() {
		return info_adSize;
	}

	public void setInfo_adSize(String info_adSize) {
		this.info_adSize = info_adSize;
	}

	public Double getInfo_lon() {
		return info_lon;
	}

	public void setInfo_lon(Double info_lon) {
		this.info_lon = info_lon;
	}

	public Double getInfo_lat() {
		return info_lat;
	}

	public void setInfo_lat(Double info_lat) {
		this.info_lat = info_lat;
	}

	public Integer getInfo_mapStandard() {
		return info_mapStandard;
	}

	public void setInfo_mapStandard(Integer info_mapStandard) {
		this.info_mapStandard = info_mapStandard;
	}

	public String getInfo_adArea() {
		return info_adArea;
	}

	public void setInfo_adArea(String info_adArea) {
		this.info_adArea = info_adArea;
	}

	public String getInfo_contactName() {
		return info_contactName;
	}

	public void setInfo_contactName(String info_contactName) {
		this.info_contactName = info_contactName;
	}

	public String getInfo_contactCell() {
		return info_contactCell;
	}

	public void setInfo_contactCell(String info_contactCell) {
		this.info_contactCell = info_contactCell;
	}

	public Integer getInfo_mediaTypeParentId() {
		return info_mediaTypeParentId;
	}

	public void setInfo_mediaTypeParentId(Integer info_mediaTypeParentId) {
		this.info_mediaTypeParentId = info_mediaTypeParentId;
	}

	public Integer getInfo_mediaTypeId() {
		return info_mediaTypeId;
	}

	public void setInfo_mediaTypeId(Integer info_mediaTypeId) {
		this.info_mediaTypeId = info_mediaTypeId;
	}

	public String getInfo_uniqueKey() {
		return info_uniqueKey;
	}

	public void setInfo_uniqueKey(String info_uniqueKey) {
		this.info_uniqueKey = info_uniqueKey;
	}

	public String getInfo_memo() {
		return info_memo;
	}

	public void setInfo_memo(String info_memo) {
		this.info_memo = info_memo;
	}

	public String getDemoPic() {
		return demoPic;
	}
	
	public void setDemoPic(String demoPic) {
		this.demoPic = demoPic;
	}
	
	public Integer getMonitorTaskId() {
		return monitorTaskId;
	}
	
	public void setMonitorTaskId(Integer monitorTaskId) {
		this.monitorTaskId = monitorTaskId;
	}
}
