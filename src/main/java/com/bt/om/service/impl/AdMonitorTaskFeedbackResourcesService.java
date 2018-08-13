package com.bt.om.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bt.om.entity.AdMonitorTaskFeedbackResources;
import com.bt.om.mapper.AdMonitorTaskFeedbackResourcesMapper;
import com.bt.om.service.IAdMonitorTaskFeedbackResourcesService;
/**
 * 
 * @author gaohao
 * 监测任务图片事物
 *
 */
@Service
public class AdMonitorTaskFeedbackResourcesService implements IAdMonitorTaskFeedbackResourcesService{
	@Autowired
	private AdMonitorTaskFeedbackResourcesMapper adMonitorTaskFeedbackResourcesMapper;
	
	/**
	 *根据监测任务Id获取监测图片
	 */
	public List<AdMonitorTaskFeedbackResources> selectByMonitorTaskFeedbackId(Integer monitorTaskFeedbackId){
		return adMonitorTaskFeedbackResourcesMapper.selectByMonitorTaskFeedbackId(monitorTaskFeedbackId);
	}
	
	/**
	 * 更改监测任务图片
	 */
	@Override
	public int updatePicUrl(AdMonitorTaskFeedbackResources resources) {
		return adMonitorTaskFeedbackResourcesMapper.updateByPrimaryKeySelective(resources);
	}

	@Override
	public AdMonitorTaskFeedbackResources selectById(Integer id) {
		return adMonitorTaskFeedbackResourcesMapper.selectByPrimaryKey(id);
	}

	@Override
	public int insertSelective(AdMonitorTaskFeedbackResources resources) {
		return adMonitorTaskFeedbackResourcesMapper.insertSelective(resources);
	}

	@Override
	public int selectCountByMonitorTaskFeedbackId(Integer monitorTaskFeedbackId) {
		return adMonitorTaskFeedbackResourcesMapper.selectCountByMonitorTaskFeedbackId(monitorTaskFeedbackId);
	}
}
