package com.bt.om.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bt.om.entity.AdVersion;
import com.bt.om.mapper.AdVersionMapper;

/**
 * Created by jiayong.mao on 2018/4/3.
 */
@Service
public class AdVersionCache {
	private AdVersion version; // 强制更新的版本
	private AdVersion nowVersion; // 最新的一个版本
	
	@Autowired
	private AdVersionMapper adVersionMapper;
	
	public void init() {
		AdVersion adVersion = adVersionMapper.selectLatestForceUpdateRecord();
		if(adVersion != null) {
			this.version = adVersion;
		}
		
		adVersion = adVersionMapper.selectLatestRecord();
		if(adVersion != null) {
			this.nowVersion = adVersion;
		}
	}
	
	public AdVersion getVersion() {
		return version;
	}
	
	public AdVersion getNowVersion() {
		return nowVersion;
	}
}
