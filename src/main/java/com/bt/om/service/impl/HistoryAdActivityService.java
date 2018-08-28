package com.bt.om.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.adtime.common.lang.StringUtil;
import com.bt.om.common.DateUtil;
import com.bt.om.entity.AdActivity;
import com.bt.om.entity.AdActivityAdseat;
import com.bt.om.entity.AdMonitorTask;
import com.bt.om.entity.AdSeatInfo;
import com.bt.om.entity.AdUserMessage;
import com.bt.om.entity.HistoryAdActivity;
import com.bt.om.entity.HistoryAdActivityAdseat;
import com.bt.om.entity.HistoryAdJiucuoTask;
import com.bt.om.entity.HistoryAdJiucuoTaskFeedback;
import com.bt.om.entity.HistoryAdMonitorTask;
import com.bt.om.entity.HistoryAdMonitorTaskFeedback;
import com.bt.om.entity.HistoryAdMonitorUserTask;
import com.bt.om.entity.SysUser;
import com.bt.om.entity.SysUserExecute;
import com.bt.om.entity.vo.ActivityMobileReportVo;
import com.bt.om.entity.vo.AdActivityAdseatTaskVo;
import com.bt.om.entity.vo.AdActivityAdseatVo;
import com.bt.om.entity.vo.AdActivityVo;
import com.bt.om.entity.vo.AdActivityVo2;
import com.bt.om.entity.vo.AdMonitorTaskVo;
import com.bt.om.entity.vo.AdSeatCount;
import com.bt.om.entity.vo.TaskAdSeat;
import com.bt.om.enums.ActivityStatus;
import com.bt.om.enums.DepartmentTypeEnum;
import com.bt.om.enums.MessageIsFinish;
import com.bt.om.enums.MessageType;
import com.bt.om.enums.MonitorTaskStatus;
import com.bt.om.enums.MonitorTaskType;
import com.bt.om.enums.TaskProblemStatus;
import com.bt.om.enums.UserTypeEnum;
import com.bt.om.mapper.AdActivityAdseatMapper;
import com.bt.om.mapper.AdActivityAreaMapper;
import com.bt.om.mapper.AdActivityMapper;
import com.bt.om.mapper.AdActivityMediaMapper;
import com.bt.om.mapper.AdJiucuoTaskFeedbackMapper;
import com.bt.om.mapper.AdJiucuoTaskMapper;
import com.bt.om.mapper.AdMonitorTaskFeedbackMapper;
import com.bt.om.mapper.AdMonitorTaskMapper;
import com.bt.om.mapper.AdMonitorUserTaskMapper;
import com.bt.om.mapper.AdSeatInfoMapper;
import com.bt.om.mapper.AdUserMessageMapper;
import com.bt.om.mapper.HistoryAdActivityAdseatMapper;
import com.bt.om.mapper.HistoryAdActivityMapper;
import com.bt.om.mapper.HistoryAdJiucuoTaskFeedbackMapper;
import com.bt.om.mapper.HistoryAdJiucuoTaskMapper;
import com.bt.om.mapper.HistoryAdMonitorTaskFeedbackMapper;
import com.bt.om.mapper.HistoryAdMonitorTaskMapper;
import com.bt.om.mapper.HistoryAdMonitorUserTaskMapper;
import com.bt.om.mapper.SysResourcesMapper;
import com.bt.om.mapper.SysUserMapper;
import com.bt.om.mapper.SysUserResMapper;
import com.bt.om.service.IHistoryAdActivityService;
import com.bt.om.util.ConfigUtil;
import com.bt.om.vo.web.SearchDataVo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * 活动相关事务层
 */
@Service
public class HistoryAdActivityService implements IHistoryAdActivityService {
    @Autowired
    AdActivityMapper adActivityMapper;
    @Autowired
    AdActivityAdseatMapper adActivityAdseatMapper;
    @Autowired
    AdActivityMediaMapper adActivityMediaMapper;
    @Autowired
    AdActivityAreaMapper adActivityAreaMapper;
    @Autowired
    AdMonitorTaskMapper adMonitorTaskMapper;
    @Autowired
    AdMonitorTaskFeedbackMapper adMonitorTaskFeedbackMapper;
    @Autowired
    AdMonitorUserTaskMapper adMonitorUserTaskMapper;
    @Autowired
    SysUserMapper sysUserMapper;
    @Autowired
    AdSeatInfoMapper adSeatInfoMapper;
    @Autowired
    SysUserResMapper sysUserResMapper;
    @Autowired
    SysResourcesMapper sysResourcesMapper;
    @Autowired
    AdUserMessageMapper adUserMessageMapper;
    @Autowired
    HistoryAdActivityMapper historyAdActivityMapper;
    @Autowired
    HistoryAdActivityAdseatMapper historyAdActivityAdseatMapper;
    @Autowired
    AdJiucuoTaskMapper adJiucuoTaskMapper;
    @Autowired
    AdJiucuoTaskFeedbackMapper adJiucuoTaskFeedbackMapper;
    @Autowired
    HistoryAdJiucuoTaskMapper historyAdJiucuoTaskMapper;
    @Autowired
    HistoryAdJiucuoTaskFeedbackMapper historyAdJiucuoTaskFeedbackMapper;
    @Autowired
    HistoryAdMonitorTaskMapper historyAdMonitorTaskMapper;
    @Autowired
    HistoryAdMonitorTaskFeedbackMapper historyAdMonitorTaskFeedbackMapper;
    @Autowired
    HistoryAdMonitorUserTaskMapper historyAdMonitorUserTaskMapper;

    /**
     * 分页查询活动信息
     */
    @Override
    public void getPageData(SearchDataVo vo) {
        int count = historyAdActivityMapper.getPageCount(vo.getSearchMap());
        vo.setCount(count);
        if (count > 0) {
            vo.setList(historyAdActivityMapper.getPageData(vo.getSearchMap(), new RowBounds(vo.getStart(), vo.getSize())));
        } else {
            vo.setList(new ArrayList<AdActivityVo2>());
        }
    }

    /**
     * 通过活动id查询活动、活动的广告位信息
     */
    @Override
    public AdActivityVo getVoById(SearchDataVo vo) {
    	if(vo.getSearchMap().get("id") != null) {
    		AdActivityVo adActivityVo = historyAdActivityMapper.selectVoByPrimaryKey(vo.getSearchMap());
        	List<AdActivityAdseatVo> adseatVos = historyAdActivityAdseatMapper.selectByActivityId(vo.getSearchMap());
        	adActivityVo.setAdActivityAdseatVos(adseatVos);
        	return adActivityVo;
    	} else {
    		return null;
    	}
    }

    /**
     * 通过活动-广告位关联表的id查询关联信息
     */
    @Override
    public AdActivityAdseat getActivitySeatById(SearchDataVo vo) {
        return historyAdActivityAdseatMapper.selectVoById(vo.getSearchMap());
    }

    /**
     * 通过活动id查询出活动信息
     */
	@Override
	public AdActivity getById(Map<String, Object> searchMap) {
		return historyAdActivityMapper.selectByPrimaryKey(searchMap);
	}

	@Override
	public List<AdActivityAdseatTaskVo> newSelectAdActivityAdseatTaskReport(Map<String, Object> searchMap) {
		return historyAdActivityAdseatMapper.newSelectAdActivityAdseatTaskReport(searchMap);
	}
	
	/**
	 * 迁移活动结束时间大于半年的数据
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void batchDataTransfer(HistoryAdActivity adActivity) {
		List<HistoryAdActivityAdseat> adActivityAdseats = new ArrayList<>();
		List<HistoryAdJiucuoTask> adJiucuoTasks = new ArrayList<>();
		List<HistoryAdJiucuoTaskFeedback> adJiucuoTaskFeedback  = new ArrayList<>();
		List<HistoryAdMonitorTask> adMonitorTasks = new ArrayList<>();
		List<HistoryAdMonitorUserTask> adMonitorUserTasks = new ArrayList<>();
		List<HistoryAdMonitorTaskFeedback> adMonitorTaskFeedbacks = new ArrayList<>();
		Date startTime = adActivity.getStartTime();
		Calendar time = Calendar.getInstance();
		time.setTime(startTime);
	    String year = String.valueOf(time.get(Calendar.YEAR));
	    int type = time.get(Calendar.MONTH)>5?1:0;
	    String tableName = year + "_" + type;
		adActivity.setTableName(tableName);
		historyAdActivityMapper.insertSelective(adActivity);//活动
		
		adActivityAdseats = adActivityAdseatMapper.selectActivityAdseatByActivityId(adActivity.getId());//广告活动-广告位
		if (adActivityAdseats.size()>0) {
			historyAdActivityAdseatMapper.insertBatch(tableName, adActivityAdseats);
		}
		adActivityAdseats.clear();
		
		//纠错任务
		adJiucuoTasks = adJiucuoTaskMapper.selectByActivityId(adActivity.getId());
		if (adJiucuoTasks.size()>0) {
			historyAdJiucuoTaskMapper.insertBatch(tableName,adJiucuoTasks);
			adJiucuoTaskFeedback = adJiucuoTaskFeedbackMapper.selectAllByTaskIds(adJiucuoTasks);
		}
		if (adJiucuoTaskFeedback.size()>0) {
			historyAdJiucuoTaskFeedbackMapper.insertBatch(tableName,adJiucuoTaskFeedback);
			adJiucuoTaskFeedbackMapper.deleteByIds(adJiucuoTaskFeedback);
		}
		adJiucuoTasks.clear();
		adJiucuoTaskFeedback.clear();
		
		//监测任务
		adMonitorTasks = adMonitorTaskMapper.selectByActivityIds(adActivity.getId());
 		if (adMonitorTasks.size()>0) {
			historyAdMonitorTaskMapper.insertBatch(tableName,adMonitorTasks);
			adMonitorTaskFeedbacks = adMonitorTaskFeedbackMapper.selectAllByMonitorTaskIds(adMonitorTasks);
			adMonitorUserTasks = adMonitorUserTaskMapper.selectAllByMonitorTaskIds(adMonitorTasks);
		}
		if (adMonitorTaskFeedbacks.size()>0) {
			historyAdMonitorTaskFeedbackMapper.insertBatch(tableName,adMonitorTaskFeedbacks);
			adMonitorTaskFeedbackMapper.deleteByIds(adMonitorTaskFeedbacks);
		}
		if (adMonitorUserTasks.size()>0) {
			historyAdMonitorUserTaskMapper.insertBatch(tableName,adMonitorUserTasks);
			adMonitorUserTaskMapper.deleteByIds(adMonitorUserTasks);
		}
		adMonitorTasks.clear();
		adMonitorUserTasks.clear();
		adMonitorTaskFeedbacks.clear();
		
		adActivityMapper.deleteByPrimaryKey(adActivity.getId());
		adActivityAdseatMapper.deleteByActivityId(adActivity.getId());
		adJiucuoTaskMapper.deleteByActivityId(adActivity.getId());
		adMonitorTaskMapper.deleteByActivityId(adActivity.getId());
	}
}