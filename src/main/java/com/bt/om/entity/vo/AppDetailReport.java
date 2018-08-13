package com.bt.om.entity.vo;

import java.util.ArrayList;
import java.util.List;

public class AppDetailReport {
	private String seatInfoName; //广告位名称
	private String mediaName; //所属媒体名称
	private String mediaTypeParentName; //媒体大类名称
	private String mediaTypeName; //媒体小类名称
	private String province; //省(直辖市)
	private String city; //市
	private String region; //区(县)
	private String road; //主要路段
	private String street; //街道(镇，乡)
	private String location; //详细位置
	private String uniqueKey; //唯一标识
	private String monitorStart; //开始监测时间
	private String monitorEnd; //结束监测时间
	private String currentStatus; //当前状态: 未开始, 监测中, 有问题, 已结束
	private String size; //广告位尺寸
	private String area; //面积
	private String lon; //经度
	private String lat; //纬度
	private String contactName; //联系人姓名
	private String contactCell; //联系人电话
	private String memo; //备注
	private String demoPic; //活动预览图
	private List<PictureVo> upPics; //上刊监测图片集合
	private List<PictureVo> durationPics = new ArrayList<>(); //投放期间监测图片集合
	private List<PictureVo> downPics = new ArrayList<>();; //下刊监测图片集合
	private List<PictureVo> upTaskPics = new ArrayList<>();; //上刊任务图片集合
	private List<PictureVo> zhuijiaPics = new ArrayList<>();; //追加监测图片集合
    
	public String getSeatInfoName() {
		return seatInfoName;
	}
	public void setSeatInfoName(String seatInfoName) {
		this.seatInfoName = seatInfoName;
	}
	public String getMediaName() {
		return mediaName;
	}
	public void setMediaName(String mediaName) {
		this.mediaName = mediaName;
	}
	public String getMediaTypeParentName() {
		return mediaTypeParentName;
	}
	public void setMediaTypeParentName(String mediaTypeParentName) {
		this.mediaTypeParentName = mediaTypeParentName;
	}
	public String getMediaTypeName() {
		return mediaTypeName;
	}
	public void setMediaTypeName(String mediaTypeName) {
		this.mediaTypeName = mediaTypeName;
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
	public String getRegion() {
		return region;
	}
	public void setRegion(String region) {
		this.region = region;
	}
	public String getRoad() {
		return road;
	}
	public void setRoad(String road) {
		this.road = road;
	}
	public String getStreet() {
		return street;
	}
	public void setStreet(String street) {
		this.street = street;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getUniqueKey() {
		return uniqueKey;
	}
	public void setUniqueKey(String uniqueKey) {
		this.uniqueKey = uniqueKey;
	}
	public String getMonitorStart() {
		return monitorStart;
	}
	public void setMonitorStart(String monitorStart) {
		this.monitorStart = monitorStart;
	}
	public String getMonitorEnd() {
		return monitorEnd;
	}
	public void setMonitorEnd(String monitorEnd) {
		this.monitorEnd = monitorEnd;
	}
	public String getCurrentStatus() {
		return currentStatus;
	}
	public void setCurrentStatus(String currentStatus) {
		this.currentStatus = currentStatus;
	}
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public String getLon() {
		return lon;
	}
	public void setLon(String lon) {
		this.lon = lon;
	}
	public String getLat() {
		return lat;
	}
	public void setLat(String lat) {
		this.lat = lat;
	}
	public String getContactName() {
		return contactName;
	}
	public void setContactName(String contactName) {
		this.contactName = contactName;
	}
	public String getContactCell() {
		return contactCell;
	}
	public void setContactCell(String contactCell) {
		this.contactCell = contactCell;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public String getDemoPic() {
		return demoPic;
	}
	public void setDemoPic(String demoPic) {
		this.demoPic = demoPic;
	}
	public List<PictureVo> getUpPics() {
		return upPics;
	}
	public void setUpPics(List<PictureVo> upPics) {
		this.upPics = upPics;
	}
	public List<PictureVo> getDurationPics() {
		return durationPics;
	}
	public void setDurationPics(List<PictureVo> durationPics) {
		this.durationPics = durationPics;
	}
	public List<PictureVo> getDownPics() {
		return downPics;
	}
	public void setDownPics(List<PictureVo> downPics) {
		this.downPics = downPics;
	}
	public List<PictureVo> getUpTaskPics() {
		return upTaskPics;
	}
	public void setUpTaskPics(List<PictureVo> upTaskPics) {
		this.upTaskPics = upTaskPics;
	}
	public List<PictureVo> getZhuijiaPics() {
		return zhuijiaPics;
	}
	public void setZhuijiaPics(List<PictureVo> zhuijiaPics) {
		this.zhuijiaPics = zhuijiaPics;
	}
}
