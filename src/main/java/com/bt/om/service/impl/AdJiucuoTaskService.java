package com.bt.om.service.impl;

import com.bt.om.entity.*;
import com.bt.om.entity.vo.AdJiucuoTaskMobileVo;
import com.bt.om.entity.vo.AdJiucuoTaskVo;
import com.bt.om.enums.JiucuoTaskStatus;
import com.bt.om.enums.RewardTaskType;
import com.bt.om.enums.RewardType;
import com.bt.om.enums.SysUserExecuteType;
import com.bt.om.mapper.AdJiucuoTaskFeedbackMapper;
import com.bt.om.mapper.AdJiucuoTaskMapper;
import com.bt.om.mapper.AdMonitorRewardMapper;
import com.bt.om.mapper.SysUserExecuteMapper;
import com.bt.om.service.IAdJiucuoTaskService;
import com.bt.om.vo.web.SearchDataVo;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by caiting on 2018/1/21.
 */
@Service
public class AdJiucuoTaskService implements IAdJiucuoTaskService {
    @Autowired
    private AdJiucuoTaskMapper adJiucuoTaskMapper;
    @Autowired
    private AdJiucuoTaskFeedbackMapper adJiucuoTaskFeedbackMapper;
    @Autowired
    private SysUserExecuteMapper sysUserExecuteMapper;
    @Autowired
    private AdMonitorRewardMapper adMonitorRewardMapper;

    @Override
    public void getPageData(SearchDataVo vo) {
        int count = adJiucuoTaskMapper.getPageCount(vo.getSearchMap());
        vo.setCount(count);
        if(count>0){
            vo.setList(adJiucuoTaskMapper.getPageData(vo.getSearchMap(), new RowBounds(vo.getStart(), vo.getSize())));
        }else{
            vo.setList(new ArrayList<AdJiucuoTask>());
        }
    }

    @Override
    public AdJiucuoTask getById(Integer id) {
        return adJiucuoTaskMapper.selectByPrimaryKey(id);
    }

    @Override
    public AdJiucuoTaskVo getVoById(Integer id) {
        return adJiucuoTaskMapper.selectVoByPrimaryKey(id);
    }

    @Override
    public AdJiucuoTaskFeedback getFeadBackById(Integer id) {
        return adJiucuoTaskFeedbackMapper.selectByPrimaryKey(id);
    }

    @Override
    public AdJiucuoTaskFeedback getFeadBackByTaskId(Integer id) {
        return adJiucuoTaskFeedbackMapper.selectByTaskId(id);
    }

    @Override
    public void update(AdJiucuoTask task) {
        adJiucuoTaskMapper.updateByPrimaryKeySelective(task);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void pass(AdJiucuoTask task) {
        task.setStatus(JiucuoTaskStatus.VERIFIED.getId());
        adJiucuoTaskMapper.updateByPrimaryKeySelective(task);
        task = adJiucuoTaskMapper.selectByPrimaryKey(task.getId());
        SysUserExecute user = sysUserExecuteMapper.selectByPrimaryKey(task.getUserId());
        if(user.getId() == SysUserExecuteType.WORKER.getId()) {
            Date now = new Date();
            AdMonitorReward reward = new AdMonitorReward();
            reward.setMonitorTaskId(task.getId());
            reward.setType(RewardType.ADD.getId());
            reward.setTaskType(RewardTaskType.JIUCUO.getId());
            reward.setUserId(task.getUserId());
            //? 奖励点数设计，这里随便写死15点
            reward.setRewardPoints(15);
            reward.setCreateTime(now);
            reward.setUpdateTime(now);
            adMonitorRewardMapper.insert(reward);
        }

    }

    @Override
    public void reject(AdJiucuoTask task, String reason) {
        task.setReason(reason);
        task.setStatus(JiucuoTaskStatus.VERIFY_FAILURE.getId());
        adJiucuoTaskMapper.updateByPrimaryKeySelective(task);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void feedback(AdJiucuoTask task, AdJiucuoTaskFeedback feedback) {
        Date now = new Date();
        task.setCreateTime(now);
        task.setSubmitTime(now);
        task.setUpdateTime(now);
        adJiucuoTaskMapper.insert(task);
        feedback.setJiucuoTaskId(task.getId());
        feedback.setCreateTime(now);
        feedback.setUpdateTime(now);
        adJiucuoTaskFeedbackMapper.insert(feedback);
    }

    @Override
    public List<AdJiucuoTaskMobileVo> getByUserIdForMobile(Integer userId) {
        return adJiucuoTaskMapper.selectByUserId(userId);
    }
}
