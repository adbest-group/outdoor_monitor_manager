package com.bt.om.service.impl;

import com.bt.om.entity.AdCrowd;
import com.bt.om.entity.AdMedia;
import com.bt.om.entity.AdMonitorTask;
import com.bt.om.entity.AdSeatType;
import com.bt.om.entity.vo.AdSeatInfoVo;
import com.bt.om.entity.vo.ResourceVo;
import com.bt.om.mapper.AdActivityAdseatMapper;
import com.bt.om.mapper.AdCrowdMapper;
import com.bt.om.mapper.AdMediaMapper;
import com.bt.om.mapper.AdSeatInfoMapper;
import com.bt.om.mapper.AdSeatTypeMapper;
import com.bt.om.service.IResourceService;
import com.bt.om.vo.web.SearchDataVo;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ResourceService implements IResourceService {
	@Autowired
	private AdSeatInfoMapper adSeatInfoMapper;
	
	@Autowired
	private AdActivityAdseatMapper adActivityAdseatMapper;

	@Autowired
	private AdMediaMapper adMediaMapper;

	@Autowired
	private AdSeatTypeMapper adSeatTypeMapper;

	@Autowired
	private AdCrowdMapper adCrowdMapper;

	@Override
	@Transactional
	public void insertAdSeatInfo(ResourceVo adSeatInfoVo) {
		// 插入ad_seat_info
		adSeatInfoVo.getAdSeatInfo().setCreateTime(new Date());
		adSeatInfoVo.getAdSeatInfo().setUpdateTime(new Date());
		adSeatInfoVo.getAdSeatInfo().setMediaId(adSeatInfoVo.getAdMedia().getId());
		adSeatInfoVo.getAdSeatInfo().setAdSeatType(adSeatInfoVo.getAdSeatType().getId());
		adSeatInfoMapper.insertSelective(adSeatInfoVo.getAdSeatInfo());

		// 插入ad_crowd
		adSeatInfoVo.getAdCrowdVo().setAdSeatId(adSeatInfoVo.getAdSeatInfo().getId());
		adSeatInfoVo.getAdCrowdVo().setCreateTime(new Date());
		adSeatInfoVo.getAdCrowdVo().setUpdateTime(new Date());
		adSeatInfoVo.getAdCrowdVo().setMale(1);// 男
		adSeatInfoVo.getAdCrowdVo().setFemale(2);// 女
		adCrowdMapper.insertAdCrowdVoMale(adSeatInfoVo.getAdCrowdVo());
		adCrowdMapper.insertAdCrowdVoFemale(adSeatInfoVo.getAdCrowdVo());
	}

	@Override
	public void getDetailsInfo(SearchDataVo vo) {
		int count = adSeatInfoMapper.getPageCount(vo.getSearchMap());
		vo.setCount(count);
		if (count > 0) {
			vo.setList(adSeatInfoMapper.getPageData(vo.getSearchMap(), new RowBounds(vo.getStart(), vo.getSize())));
		} else {
			vo.setList(new ArrayList<AdMonitorTask>());
		}
	}

	@Override
	public AdSeatInfoVo getAdSeatInfoById(String id) {
		int ids = Integer.valueOf(id);
		return adSeatInfoMapper.getAdSeatInfoById(ids);
	}

	@Override
	public List<AdMedia> getAll() {
		return adMediaMapper.getAll();
	}
	
	@Override
	public List<AdMedia> getAvailableAll() {
		return adMediaMapper.getAvailableAll();
	}

	@Override
	public int deleteAdSeatById(Integer id) {
		//只能删除没有参与活动的广告位
		Integer countByAdSeatId = adActivityAdseatMapper.selectCountByAdSeatId(id);
		if(countByAdSeatId == 0) {
			return adSeatInfoMapper.deleteByPrimaryKey(id);
		} else {
			return 0;
		}
	}

	@Override
	public List<AdSeatType> getSeatTypeAll() {
		return adSeatTypeMapper.getSeatTypeAll();
	}

	public List<AdCrowd> getAgePartListByAdSeatId(Integer seatId) {
		Integer ids = Integer.valueOf(seatId);
		return adCrowdMapper.getAgePartListByAdSeatId(ids);
	}
}
