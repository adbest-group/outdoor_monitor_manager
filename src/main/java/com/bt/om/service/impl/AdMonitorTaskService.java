package com.bt.om.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.bt.om.enums.*;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bt.om.entity.AdMonitorReward;
import com.bt.om.entity.AdMonitorTask;
import com.bt.om.entity.AdMonitorTaskFeedback;
import com.bt.om.entity.vo.AdMonitorTaskMobileVo;
import com.bt.om.entity.vo.AdMonitorTaskVo;
import com.bt.om.mapper.AdMonitorRewardMapper;
import com.bt.om.mapper.AdMonitorTaskFeedbackMapper;
import com.bt.om.mapper.AdMonitorTaskMapper;
import com.bt.om.service.IAdMonitorTaskService;
import com.bt.om.vo.web.SearchDataVo;
import sun.jvmstat.perfdata.monitor.MonitorStatus;

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
        if (count > 0) {
            vo.setList(adMonitorTaskMapper.getPageData(vo.getSearchMap(), new RowBounds(vo.getStart(), vo.getSize())));
        } else {
            vo.setList(new ArrayList<AdMonitorTask>());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void assign(String[] taskIds, Integer userId) {
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
        //如果监测反馈有问题，问题状态置为有问题，否则无问题
        AdMonitorTaskFeedback feedback = adMonitorTaskFeedbackMapper.selectByTaskId(task.getId(), 1).get(0);
        if (feedback.getProblem() != null || feedback.getProblemOther() != null) {
            task.setProblemStatus(TaskProblemStatus.PROBLEM.getId());
        } else {
            task.setProblemStatus(TaskProblemStatus.NO_PROBLEM.getId());
        }
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
        List<AdMonitorTaskFeedback> feedbacks = adMonitorTaskFeedbackMapper.selectByTaskId(task.getId(), 1);
        for (AdMonitorTaskFeedback feedback : feedbacks) {
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
        //获取监测任务
        AdMonitorTask task = adMonitorTaskMapper.selectByPrimaryKey(taskId);
        //如果检测任务当前处于"待执行"或"审核未通过"
        if (task.getStatus() == MonitorTaskStatus.TO_CARRY_OUT.getId() || task.getStatus() == MonitorTaskStatus.VERIFY_FAILURE.getId()) {
            //获取之前提交的feedback，设置为无效，流程正常情况下只有一条
            List<AdMonitorTaskFeedback> old_feed = adMonitorTaskFeedbackMapper.selectByTaskId(taskId, 1);
            for (AdMonitorTaskFeedback old : old_feed) {
                old.setStatus(2);
                old.setUpdateTime(now);
                adMonitorTaskFeedbackMapper.updateByPrimaryKeySelective(old);
            }
            feedback.setCreateTime(now);
            feedback.setUpdateTime(now);
            feedback.setMonitorTaskId(taskId);
            adMonitorTaskFeedbackMapper.insertSelective(feedback);
            //如果本次提交的照片不足，任务状态设置为"未完成"，否则进入待审核
            if (feedback.getPicUrl1() == null || feedback.getPicUrl2() == null || feedback.getPicUrl3() == null || feedback.getPicUrl4() == null) {
                task.setStatus(MonitorTaskStatus.UN_FINISHED.getId());
            } else {
                task.setStatus(MonitorTaskStatus.UNVERIFY.getId());
            }
            adMonitorTaskMapper.updateByPrimaryKeySelective(task);
            //如果检测任务当前处于"未完成"
        } else if (task.getStatus() == MonitorTaskStatus.UN_FINISHED.getId()) {
            //获取未完成的feedback，流程正常情况下只有一条
            List<AdMonitorTaskFeedback> old_feed = adMonitorTaskFeedbackMapper.selectByTaskId(taskId, 1);
            AdMonitorTaskFeedback old = old_feed.get(old_feed.size() - 1);

            //用本地feedback根据提供字段按需更新
            feedback.setId(old.getId());
            feedback.setUpdateTime(now);
            feedback.setMonitorTaskId(taskId);
            adMonitorTaskFeedbackMapper.updateByPrimaryKeySelective(feedback);
            //如果本次提交的照片+以前提交的照片仍不足，任务状态还是设置为"未完成"，否则进入待审核
            if (feedback.getPicUrl1() == null && old.getPicUrl1() == null || feedback.getPicUrl2() == null && old.getPicUrl2() == null || feedback.getPicUrl3() == null && old.getPicUrl3() == null || feedback.getPicUrl4() == null && old.getPicUrl4() == null) {
                task.setStatus(MonitorTaskStatus.UN_FINISHED.getId());
            } else {
                task.setStatus(MonitorTaskStatus.UNVERIFY.getId());
            }
            adMonitorTaskMapper.updateByPrimaryKeySelective(task);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createSubTask(Integer taskId) {
        Date now = new Date();
        AdMonitorTask task = adMonitorTaskMapper.selectByPrimaryKey(taskId);
        AdMonitorTask sub = new AdMonitorTask();
        sub.setTaskType(MonitorTaskType.FIX_CONFIRM.getId());
        sub.setActivityAdseatId(task.getActivityAdseatId());
        sub.setStatus(MonitorTaskStatus.UNASSIGN.getId());
        sub.setProblemStatus(TaskProblemStatus.UNMONITOR.getId());
        sub.setActivityId(task.getActivityId());
        sub.setParentId(task.getId());
        sub.setParentType(RewardTaskType.MONITOR.getId());
        sub.setSubCreated(2);
        sub.setCreateTime(now);
        sub.setUpdateTime(now);
        adMonitorTaskMapper.insertSelective(sub);
        task.setSubCreated(1);
        adMonitorTaskMapper.updateByPrimaryKeySelective(task);
    }

    @Override
    public AdMonitorTaskVo getTaskDetails(String taskId) {
        int taskIds = Integer.valueOf(taskId);
        return adMonitorTaskMapper.getTaskDetails(taskIds);
    }

    @Override
    public List<AdMonitorTaskVo> getSubmitDetails(String taskId) {
        int taskIds = Integer.valueOf(taskId);
        List<AdMonitorTaskVo> list = adMonitorTaskMapper.getSubmitDetails(taskIds);
        return list;
    }

}
