package com.bt.om.service;

import java.util.List;

import com.bt.om.entity.AdCrowd;
import com.bt.om.entity.AdMedia;
import com.bt.om.entity.AdSeatType;
import com.bt.om.entity.vo.AdCrowdVo;
import com.bt.om.entity.vo.AdSeatInfoVo;
import com.bt.om.entity.vo.ResourceVo;
import com.bt.om.vo.web.SearchDataVo;

public interface IResourceService {
	/**
	 * 新增广告位信息
	 * 
	 * @param adSeatInfoVo
	 */
	void insertAdSeatInfo(ResourceVo adSeatInfoVo);

	void getDetailsInfo(SearchDataVo vo);

	AdSeatInfoVo getAdSeatInfoById(String id);

	/**
	 * 获取所有媒体名称
	 * 
	 * @return
	 */
	List<AdMedia> getAll();

	int deleteAdSeatById(Integer id);

	List<AdSeatType> getSeatTypeAll();

	public List<AdCrowd> getAgePartListByAdSeatId(Integer seatId);
}
