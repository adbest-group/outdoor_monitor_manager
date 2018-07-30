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

/**
 * 广告位媒体资源 相关事务层
 */
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

	/**
	 * 插入广告位及广告位人群信息, 暂时不用
	 */
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

	/**
	 * 分页查询广告位信息
	 */
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

	/**
	 * 通过id查询广告位信息
	 */
	@Override
	public AdSeatInfoVo getAdSeatInfoById(String id) {
		int ids = Integer.valueOf(id);
		return adSeatInfoMapper.getAdSeatInfoById(ids);
	}

	/**
	 * 查询所有的媒体主
	 */
	@Override
	public List<AdMedia> getAll() {
		return adMediaMapper.getAll();
	}
	
	/**
	 * 查询所有可用的媒体主
	 */
	@Override
	public List<AdMedia> getAvailableAll() {
		return adMediaMapper.getAvailableAll();
	}

	/**
	 * 删除没有参与活动的广告位
	 */
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

	/**
	 * 获取全部的广告位类型, 功能已废弃
	 */
	@Override
	public List<AdSeatType> getSeatTypeAll() {
		return adSeatTypeMapper.getSeatTypeAll();
	}

	/**
	 * 广告位人群暂时不用
	 */
	public List<AdCrowd> getAgePartListByAdSeatId(Integer seatId) {
		Integer ids = Integer.valueOf(seatId);
		return adCrowdMapper.getAgePartListByAdSeatId(ids);
	}
}
