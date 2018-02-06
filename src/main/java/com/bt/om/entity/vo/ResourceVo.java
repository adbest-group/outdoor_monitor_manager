package com.bt.om.entity.vo;

import java.util.List;

import com.bt.om.entity.AdMedia;
import com.bt.om.entity.AdSeatInfo;
import com.bt.om.entity.AdSeatType;

public class ResourceVo {
	private AdSeatInfo adSeatInfo;

	private AdSeatType adSeatType;

	private AdMedia adMedia;

	private List<AdCrowdVo> list;

	private AdCrowdVo adCrowdVo;

	public AdSeatInfo getAdSeatInfo() {
		return adSeatInfo;
	}

	public void setAdSeatInfo(AdSeatInfo adSeatInfo) {
		this.adSeatInfo = adSeatInfo;
	}

	public AdSeatType getAdSeatType() {
		return adSeatType;
	}

	public void setAdSeatType(AdSeatType adSeatType) {
		this.adSeatType = adSeatType;
	}

	public AdMedia getAdMedia() {
		return adMedia;
	}

	public void setAdMedia(AdMedia adMedia) {
		this.adMedia = adMedia;
	}

	public AdCrowdVo getAdCrowdVo() {
		return adCrowdVo;
	}

	public void setAdCrowdVo(AdCrowdVo adCrowdVo) {
		this.adCrowdVo = adCrowdVo;
	}

	public List<AdCrowdVo> getList() {
		return list;
	}

	public void setList(List<AdCrowdVo> list) {
		this.list = list;
	}
}
