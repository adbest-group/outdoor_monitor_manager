package com.bt.om.service.impl;

import com.bt.om.entity.AdMonitorReward;
import com.bt.om.entity.AdMonitorTask;
import com.bt.om.entity.AdMonitorTaskFeedback;
import com.bt.om.entity.vo.AdMonitorTaskMobileVo;
import com.bt.om.enums.MonitorTaskStatus;
import com.bt.om.enums.RewardTaskType;
import com.bt.om.enums.RewardType;
import com.bt.om.mapper.AdMonitorRewardMapper;
import com.bt.om.mapper.AdMonitorTaskFeedbackMapper;
import com.bt.om.mapper.AdMonitorTaskMapper;
import com.bt.om.service.IAdMonitorTaskService;
import com.bt.om.vo.web.SearchDataVo;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by caiting on 2018/1/20.
 */
@Service
public class AdMonitorTaskService implements IAdMonitorTaskService {
    @Autowired
    private AdMonitorTaskMapper adMonitorTaskMapper;
    @Autowired
    private AdMonitorTaskFeedbackMapper adMonitorTaskFeedbackMapper;
    @Autowired
    private AdMonitorRewardMapper adMonitorRewardMapper;

    @Override
    public void getPageData(SearchDataVo vo) {
        int count = adMonitorTaskMapper.getPageCount(vo.getSearchMap());
        vo.setCount(count);
        if(count>0){
            vo.setList(adMonitorTaskMapper.getPageData(vo.getSearchMap(), new RowBounds(vo.getStart(), vo.getSize())));
        }else{
            vo.setList(new ArrayList<AdMonitorTask>());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void assign(String[] taskIds,Integer userId) {
        for (String taskId : taskIds) {
            AdMonitorTask task = new AdMonitorTask();
            task.setId(Integer.valueOf(taskId));
            task.setUserId(userId);
            task.setStatus(MonitorTaskStatus.TO_CARRY_OUT.getId());
            adMonitorTaskMapper.updateByPrimaryKeySelective(task);
        }
    }

    @Override
    public void update(AdMonitorTask task) {
        adMonitorTaskMapper.updateByPrimaryKeySelective(task);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void pass(AdMonitorTask task) {
        Date now = new Date();
        adMonitorTaskMapper.updateByPrimaryKeySelective(task);
        task = adMonitorTaskMapper.selectByPrimaryKey(task.getId());
        AdMonitorReward reward = new AdMonitorReward();
        reward.setMonitorTaskId(task.getId());
        reward.setType(RewardType.ADD.getId());
        reward.setTaskType(RewardTaskType.MONITOR.getId());
        reward.setUserId(task.getUserId());
        //? 奖励点数设计，这里随便写死10点
        reward.setRewardPoints(10);
        reward.setCreateTime(now);
        reward.setUpdateTime(now);
        adMonitorRewardMapper.insert(reward);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void reject(AdMonitorTask task, String reason) {
        adMonitorTaskMapper.updateByPrimaryKeySelective(task);
        List<AdMonitorTaskFeedback> feedbacks = adMonitorTaskFeedbackMapper.selectByTaskId(task.getId(),1);
        for(AdMonitorTaskFeedback feedback : feedbacks){
            feedback.setReason(reason);
            adMonitorTaskFeedbackMapper.updateByPrimaryKeySelective(feedback);
        }
    }

    @Override
    public List<AdMonitorTaskMobileVo> getByUserIdForMobile(Integer userId) {
        return adMonitorTaskMapper.selectByUserId(userId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void feedback(Integer taskId, AdMonitorTaskFeedback feedback) {
        Date now = new Date();
        List<AdMonitorTaskFeedback> old_feed = adMonitorTaskFeedbackMapper.selectByTaskId(taskId,1);
        for(AdMonitorTaskFeedback old:old_feed){
            old.setStatus(2);
            old.setUpdateTime(now);
            adMonitorTaskFeedbackMapper.updateByPrimaryKeySelective(old);
        }
        feedback.setCreateTime(now);
        feedback.setUpdateTime(now);
        feedback.setMonitorTaskId(taskId);
        adMonitorTaskFeedbackMapper.insertSelective(feedback);
        AdMonitorTask task = new AdMonitorTask();
        task.setId(taskId);
        task.setStatus(MonitorTaskStatus.UNVERIFY.getId());
        adMonitorTaskMapper.updateByPrimaryKeySelective(task);
    }
}
