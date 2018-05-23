package com.bt.om.entity.vo;

import com.bt.om.entity.AdSeatInfo;

public class AdSeatInfoVo extends AdSeatInfo {
	private String mediaName;

	private String typeName;

	private Integer agePart;
	
	private Integer mediaId;

	private Integer typeId;
	
	private String parentName;
	
	private String secondName;
	
	

	public Integer getMediaId() {
		return mediaId;
	}

	public void setMediaId(Integer mediaId) {
		this.mediaId = mediaId;
	}

	public Integer getTypeId() {
		return typeId;
	}

	public void setTypeId(Integer typeId) {
		this.typeId = typeId;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getMediaName() {
		return mediaName;
	}

	public void setMediaName(String mediaName) {
		this.mediaName = mediaName;
	}

	public Integer getAgePart() {
		return agePart;
	}

	public void setAgePart(Integer agePart) {
		this.agePart = agePart;
	}
	
	public String getParentName() {
		return parentName;
	}
	
	public void setParentName(String parentName) {
		this.parentName = parentName;
	}
	
	public String getSecondName() {
		return secondName;
	}
	
	public void setSecondName(String secondName) {
		this.secondName = secondName;
	}
}
