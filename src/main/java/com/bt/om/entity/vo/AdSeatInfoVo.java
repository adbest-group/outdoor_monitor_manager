package com.bt.om.entity.vo;

import com.bt.om.entity.AdSeatInfo;

public class AdSeatInfoVo extends AdSeatInfo {
	private String name;

	private String mediaName;

	private String typeName;

	private Integer sex;

	private Integer agePart;

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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	public Integer getAgePart() {
		return agePart;
	}

	public void setAgePart(Integer agePart) {
		this.agePart = agePart;
	}
}
