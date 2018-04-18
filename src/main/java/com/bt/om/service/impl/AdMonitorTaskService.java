package com.bt.om.service.impl;

import com.bt.om.entity.*;
import com.bt.om.entity.vo.AdMonitorTaskMobileVo;
import com.bt.om.entity.vo.AdMonitorTaskVo;
import com.bt.om.enums.*;
import com.bt.om.mapper.*;
import com.bt.om.service.IAdMonitorTaskService;
import com.bt.om.util.GeoUtil;
import com.bt.om.util.StringUtil;
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
    @Autowired
    private AdJiucuoTaskMapper jiucuoTaskMapper;
    @Autowired
    private AdSeatInfoMapper adSeatInfoMapper;

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
            Integer id = Integer.valueOf(taskId);
//            AdMonitorTask task = new AdMonitorTask();
//            task.setId(Integer.valueOf(taskId));
//            task.setUserId(userId);
//            task.setStatus(MonitorTaskStatus.TO_CARRY_OUT.getId());
//            task.setUpdateTime(new Date());
//            adMonitorTaskMapper.updateByPrimaryKeySelective(task);
            //先查询该任务
            AdMonitorTask task = adMonitorTaskMapper.selectByPrimaryKey(id);
            //任务未查到或任务执行人已指派，抢任务失败
            if(task==null||task.getUserId()!=null){
                continue;
            }
            int count = adMonitorTaskMapper.grabTask(userId,id,task.getUpdateTime());
        }
    }

    @Override
    public void update(AdMonitorTask task) {
        task.setUpdateTime(new Date());
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
        task.setVerifyTime(now);
        task.setUpdateTime(now);
        adMonitorTaskMapper.updateByPrimaryKeySelective(task);
        task = adMonitorTaskMapper.selectByPrimaryKey(task.getId());
        //如果当前任务是子任务，如果有问题，父任务的状态恢复到有问题，如果没有问题，则关闭父任务，这里分父任务是监测或纠错
        if(task.getParentId()!=null){
            if(task.getParentType()==RewardTaskType.MONITOR.getId()){
                AdMonitorTask monitor = new AdMonitorTask();
                monitor.setId(task.getParentId());
                if(task.getProblemStatus()==TaskProblemStatus.PROBLEM.getId()) {
                    monitor.setProblemStatus(TaskProblemStatus.PROBLEM.getId());
                }else{
                    monitor.setProblemStatus(TaskProblemStatus.CLOSED.getId());
                }
                monitor.setUpdateTime(now);
                adMonitorTaskMapper.updateByPrimaryKeySelective(monitor);
            }else if(task.getParentType()==RewardTaskType.JIUCUO.getId()){
                AdJiucuoTask jiucuo  = new AdJiucuoTask();
                jiucuo.setId(task.getParentId());
                if(task.getProblemStatus()==TaskProblemStatus.PROBLEM.getId()) {
                    jiucuo.setProblemStatus(TaskProblemStatus.PROBLEM.getId());
                }else{
                    jiucuo.setProblemStatus(TaskProblemStatus.CLOSED.getId());
                }
                jiucuo.setUpdateTime(now);
                jiucuoTaskMapper.updateByPrimaryKeySelective(jiucuo);
            }
        }
        //如果是上刊安装，激活其他任务
        if(task.getTaskType().equals(MonitorTaskType.SET_UP_MONITOR.getId())){
            adMonitorTaskMapper.activeTask(task.getActivityAdseatId());
        }
        //如果是上刊安装任务，并且广告位上并未记录经纬度,就把安装人员完成任务时的经纬度记录给广告位
        if(task.getTaskType().equals(MonitorTaskType.SET_UP_MONITOR.getId())){
            AdSeatInfo seatInfo = adSeatInfoMapper.getAdSeatInfoByAdActivitySeatId(task.getActivityAdseatId());
            if(seatInfo.getLon()==null||seatInfo.getLat()==null){
                seatInfo.setLon(feedback.getLon());
                seatInfo.setLat(feedback.getLat());
                adSeatInfoMapper.updateByPrimaryKeySelective(seatInfo);
            }
        }

        //奖励相关
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
        Date now = new Date();
        task.setVerifyTime(now);
        task.setUpdateTime(now);
        adMonitorTaskMapper.updateByPrimaryKeySelective(task);
        List<AdMonitorTaskFeedback> feedbacks = adMonitorTaskFeedbackMapper.selectByTaskId(task.getId(), 1);
        for (AdMonitorTaskFeedback feedback : feedbacks) {
            feedback.setReason(reason);
            feedback.setUpdateTime(now);
            adMonitorTaskFeedbackMapper.updateByPrimaryKeySelective(feedback);
        }
    }

    @Override
    public List<AdMonitorTaskMobileVo> getByUserIdForMobile(Integer userId) {
        return adMonitorTaskMapper.selectByUserId(userId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void feedback(Integer taskId, AdMonitorTaskFeedback feedback,String adSeatCode) {
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
            if(task.getTaskType()==MonitorTaskType.SET_UP_MONITOR.getId()){
                //上刊安装任务不提供未完成状态
                task.setStatus(MonitorTaskStatus.UNVERIFY.getId());
            }else {
                //如果本次提交的照片不足，任务状态设置为"未完成"，否则进入待审核
                if (feedback.getPicUrl1() == null || feedback.getPicUrl2() == null || feedback.getPicUrl3() == null || feedback.getPicUrl4() == null) {
                    task.setStatus(MonitorTaskStatus.UN_FINISHED.getId());
                } else {
                    task.setStatus(MonitorTaskStatus.UNVERIFY.getId());
                }
            }
            task.setUpdateTime(now);
            adMonitorTaskMapper.updateByPrimaryKeySelective(task);

            //上刊安装任务，判断是否二维码已绑定广告位
            if (task.getTaskType()== MonitorTaskType.SET_UP_MONITOR.getId()) {
                AdSeatInfo seatInfo = adSeatInfoMapper.getAdSeatInfoByAdActivitySeatId(task.getActivityAdseatId());
                //如果广告位没绑定二维码，本次绑定激活
                if(StringUtil.isEmpty(seatInfo.getAdCode())){
                    if(StringUtil.isEmpty(adSeatCode)){
                        throw new RuntimeException("广告位未激活，需提供广告位二维码");
                    }
                    seatInfo.setAdCode(adSeatCode);
                    seatInfo.setUpdateTime(now);
                    adSeatInfoMapper.updateByPrimaryKeySelective(seatInfo);
                }
            }

        } else if (task.getStatus() == MonitorTaskStatus.UN_FINISHED.getId()) {
            //如果检测任务当前处于"未完成"
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
            task.setUpdateTime(now);
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
        task.setUpdateTime(now);
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

    @Override
    public AdMonitorTaskVo getTaskVoById(Integer id) {
        return adMonitorTaskMapper.selectVoByPrimaryKey(id);
    }

    @Override
    public void getByPointAroundPageData(SearchDataVo vo) {
        int count = adMonitorTaskMapper.getByPointAroundPageCount(vo.getSearchMap());
        vo.setCount(count);
        if (count > 0) {
            vo.setList(adMonitorTaskMapper.getByPointAroundPageData(vo.getSearchMap(), new RowBounds(vo.getStart(), vo.getSize())));
        } else {
            vo.setList(new ArrayList<AdMonitorTaskVo>());
        }
    }

    @Override
    public void getByCurCityPageData(SearchDataVo vo) {
        int count = adMonitorTaskMapper.getByCurCityPageCount(vo.getSearchMap());
        vo.setCount(count);
        if (count > 0) {
            vo.setList(adMonitorTaskMapper.getByCurCityPageData(vo.getSearchMap(), new RowBounds(vo.getStart(), vo.getSize())));
        } else {
            vo.setList(new ArrayList<AdMonitorTaskVo>());
        }
    }

    @Override
    public boolean grabTask(Integer userId, Integer id) {
        boolean ret = false;
        //先查询该任务
        AdMonitorTask task = adMonitorTaskMapper.selectByPrimaryKey(id);
        //任务未查到或任务执行人已指派，抢任务失败
        if(task==null||task.getUserId()!=null){
            return ret;
        }
        int count = adMonitorTaskMapper.grabTask(userId,id,task.getUpdateTime());
        if(count>0){
            ret = true;
        }

        return ret;
    }

}
