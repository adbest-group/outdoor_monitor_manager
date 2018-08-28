package com.bt.om.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.adtime.common.lang.StringUtil;
import com.bt.om.entity.AdActivity;
import com.bt.om.entity.AdJiucuoTask;
import com.bt.om.entity.AdJiucuoTaskFeedback;
import com.bt.om.entity.AdMonitorTask;
import com.bt.om.entity.AdSeatInfo;
import com.bt.om.entity.AdUserMessage;
import com.bt.om.entity.SysUser;
import com.bt.om.entity.SysUserExecute;
import com.bt.om.entity.vo.AdJiucuoTaskMobileVo;
import com.bt.om.entity.vo.AdJiucuoTaskVo;
import com.bt.om.entity.vo.AdMonitorTaskVo;
import com.bt.om.enums.DepartmentTypeEnum;
import com.bt.om.enums.JiucuoTaskStatus;
import com.bt.om.enums.MessageIsFinish;
import com.bt.om.enums.MessageType;
import com.bt.om.enums.MonitorTaskStatus;
import com.bt.om.enums.MonitorTaskType;
import com.bt.om.enums.RewardTaskType;
import com.bt.om.enums.TaskProblemStatus;
import com.bt.om.enums.UserTypeEnum;
import com.bt.om.mapper.AdActivityAdseatMapper;
import com.bt.om.mapper.AdActivityMapper;
import com.bt.om.mapper.AdJiucuoTaskFeedbackMapper;
import com.bt.om.mapper.AdJiucuoTaskMapper;
import com.bt.om.mapper.AdMonitorRewardMapper;
import com.bt.om.mapper.AdMonitorTaskMapper;
import com.bt.om.mapper.AdSeatInfoMapper;
import com.bt.om.mapper.AdUserMessageMapper;
import com.bt.om.mapper.HistoryAdJiucuoTaskFeedbackMapper;
import com.bt.om.mapper.HistoryAdJiucuoTaskMapper;
import com.bt.om.mapper.HistoryAdMonitorTaskMapper;
import com.bt.om.mapper.SysResourcesMapper;
import com.bt.om.mapper.SysUserExecuteMapper;
import com.bt.om.mapper.SysUserMapper;
import com.bt.om.mapper.SysUserResMapper;
import com.bt.om.service.IAdJiucuoTaskService;
import com.bt.om.service.IHistoryAdJiucuoTaskService;
import com.bt.om.util.ConfigUtil;
import com.bt.om.vo.web.SearchDataVo;

/**
 * 纠错任务 相关事务层
 */
@Service
public class HistoryAdJiucuoTaskService implements IHistoryAdJiucuoTaskService {
    @Autowired
    private AdJiucuoTaskMapper adJiucuoTaskMapper;
    @Autowired
    private AdJiucuoTaskFeedbackMapper adJiucuoTaskFeedbackMapper;
    @Autowired
    private SysUserExecuteMapper sysUserExecuteMapper;
    @Autowired
    private AdMonitorRewardMapper adMonitorRewardMapper;
    @Autowired
    private AdActivityAdseatMapper adActivityAdseatMapper;
    @Autowired
    private AdMonitorTaskMapper adMonitorTaskMapper;
    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private SysResourcesMapper sysResourcesMapper;
    @Autowired
    private SysUserResMapper sysUserResMapper;
    @Autowired
    private AdActivityMapper adActivityMapper;
    @Autowired
    private AdUserMessageMapper adUserMessageMapper;
    @Autowired
    private AdSeatInfoMapper adSeatInfoMapper;
    @Autowired
    private HistoryAdJiucuoTaskMapper historyAdJiucuoTaskMapper;
    @Autowired
    private HistoryAdJiucuoTaskFeedbackMapper historyAdJiucuoTaskFeedbackMapper;
    @Autowired
    private HistoryAdMonitorTaskMapper historyAdMonitorTaskMapper;

    /**
     * 分页查询纠错信息
     */
    @Override
    public void getPageData(SearchDataVo vo) {
        int count = historyAdJiucuoTaskMapper.getPageCount(vo.getSearchMap());
        vo.setCount(count);
        if(count>0){
            vo.setList(historyAdJiucuoTaskMapper.getPageData(vo.getSearchMap(), new RowBounds(vo.getStart(), vo.getSize())));
        }else{
            vo.setList(new ArrayList<AdJiucuoTask>());
        }
    }

    /**
     * 通过纠错id查询纠错详细信息
     */
    @Override
    public AdJiucuoTaskVo getVoById(SearchDataVo vo) {
        return historyAdJiucuoTaskMapper.selectVoByPrimaryKey(vo.getSearchMap());
    }

    /**
     * 通过纠错任务id查询反馈信息
     */
    @Override
    public AdJiucuoTaskFeedback getFeadBackByTaskId(SearchDataVo vo) {
        return historyAdJiucuoTaskFeedbackMapper.selectByTaskId(vo.getSearchMap());
    }

    /**
     * 查询纠错任务的子监测任务
     */
    @Override
    public List<AdMonitorTaskVo> getSubTask(SearchDataVo vo) {
    	vo.putSearchParam("parentType", null, 2);
        return historyAdMonitorTaskMapper.selectVoByParent(vo.getSearchMap());
    }	
}
