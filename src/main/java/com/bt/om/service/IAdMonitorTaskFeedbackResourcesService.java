package com.bt.om.service;

import java.util.List;

import com.bt.om.entity.AdMonitorTaskFeedbackResources;

public interface IAdMonitorTaskFeedbackResourcesService {
	public List<AdMonitorTaskFeedbackResources> selectByMonitorTaskFeedbackId(Integer monitorTaskFeedbackId);

	public int updatePicUrl(AdMonitorTaskFeedbackResources resources);
	
	public AdMonitorTaskFeedbackResources selectById(Integer id);
	
	public int insertSelective(AdMonitorTaskFeedbackResources resources);
	
	public int selectCountByMonitorTaskFeedbackId(Integer monitorTaskFeedbackId);
}
