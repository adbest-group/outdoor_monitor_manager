package com.bt.om.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bt.om.entity.AdMedia;
import com.bt.om.entity.AdMonitorTask;
import com.bt.om.entity.AdSeatInfo;
import com.bt.om.entity.AdSeatType;
import com.bt.om.entity.vo.AdCrowdVo;
import com.bt.om.entity.vo.AdSeatInfoVo;
import com.bt.om.entity.vo.ResourceVo;
import com.bt.om.mapper.AdCrowdMapper;
import com.bt.om.mapper.AdMediaMapper;
import com.bt.om.mapper.AdSeatInfoMapper;
import com.bt.om.mapper.AdSeatTypeMapper;
import com.bt.om.service.IResourceService;
import com.bt.om.vo.web.SearchDataVo;

@Service
public class ResourceService implements IResourceService {
	@Autowired
	private AdSeatInfoMapper adSeatInfoMapper;

	@Autowired
	private AdMediaMapper adMediaMapper;

	@Autowired
	private AdSeatTypeMapper adSeatTypeMapper;

	@Autowired
	private AdCrowdMapper adCrowdMapper;

	@Override
	public void insertAdSeatInfo(ResourceVo adSeatInfoVo) {

		// 插入ad_media
		adSeatInfoVo.getAdMedia().setCreateTime(new Date());
		adSeatInfoVo.getAdMedia().setUpdateTime(new Date());
		adMediaMapper.insertSelective(adSeatInfoVo.getAdMedia());

		// 插入ad_seat_type
		adSeatInfoVo.getAdSeatType().setCreateTime(new Date());
		adSeatInfoVo.getAdSeatType().setUpdateTime(new Date());
		adSeatTypeMapper.insertSelective(adSeatInfoVo.getAdSeatType());

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
	public int deleteAdSeatById(Integer id) {
		return adSeatInfoMapper.deleteByPrimaryKey(id);
	}

}