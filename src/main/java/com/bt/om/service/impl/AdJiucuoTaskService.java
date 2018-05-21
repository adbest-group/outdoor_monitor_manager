package com.bt.om.service.impl;

import com.bt.om.entity.*;
import com.bt.om.entity.vo.AdJiucuoTaskMobileVo;
import com.bt.om.entity.vo.AdJiucuoTaskVo;
import com.bt.om.entity.vo.AdMonitorTaskVo;
import com.bt.om.enums.*;
import com.bt.om.mapper.*;
import com.bt.om.service.IAdJiucuoTaskService;
import com.bt.om.vo.web.SearchDataVo;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
    @Autowired
    private AdActivityAdseatMapper adActivityAdseatMapper;
    @Autowired
    private AdMonitorTaskMapper adMonitorTaskMapper;

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
        Date now = new Date();
        task.setStatus(JiucuoTaskStatus.VERIFIED.getId());
        task.setVerifyTime(now);
        adJiucuoTaskMapper.updateByPrimaryKeySelective(task);
        task = adJiucuoTaskMapper.selectByPrimaryKey(task.getId());
        SysUserExecute user = sysUserExecuteMapper.selectByPrimaryKey(task.getUserId());
        if(user.getId() == SysUserExecuteType.WORKER.getId()) {
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
        task.setVerifyTime(new Date());
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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createSubTask(Integer taskId) {
        Date now = new Date();
        AdJiucuoTask task = adJiucuoTaskMapper.selectByPrimaryKey(taskId);
        AdMonitorTask sub = new AdMonitorTask();
        sub.setTaskType(MonitorTaskType.FIX_CONFIRM.getId());
        //通过活动编号和广告位编号获取 活动和广告位的关联编号
        sub.setActivityAdseatId(adActivityAdseatMapper.selectByActivityAndSeatId(task.getActivityId(),task.getAdSeatId()).getId());
        sub.setStatus(MonitorTaskStatus.UNASSIGN.getId());
        sub.setProblemStatus(TaskProblemStatus.UNMONITOR.getId());
        sub.setActivityId(task.getActivityId());
        sub.setParentId(task.getId());
        sub.setParentType(RewardTaskType.JIUCUO.getId());
        sub.setSubCreated(2);
        sub.setCreateTime(now);
        sub.setUpdateTime(now);
        adMonitorTaskMapper.insertSelective(sub);
        task.setSubCreated(1);
        adJiucuoTaskMapper.updateByPrimaryKeySelective(task);
    }

    @Override
    public List<AdMonitorTaskVo> getSubTask(Integer id) {
        return adMonitorTaskMapper.selectVoByParent(id,2);
    }

	@Override
	public List<AdJiucuoTaskVo> selectAllByAssessorId(Map<String, Object> searchMap) {
		return adJiucuoTaskMapper.selectAllByAssessorId(searchMap);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public List<AdJiucuoTaskVo> getTenAdMonitorTaskVo(Map<String, Object> searchMap) {
		//[1] 查询 for update
		Integer assessorId = (Integer) searchMap.get("assessorId");
		List<AdJiucuoTaskVo> taskVos = adJiucuoTaskMapper.getTenAdJiucuoTaskVo(searchMap);
		List<Integer> ids = new ArrayList<>();
		for (AdJiucuoTaskVo adJiucuoTaskVo : taskVos) {
			ids.add(adJiucuoTaskVo.getId());
			adJiucuoTaskVo.setAssessorId(assessorId);
		}
		//[2] 更新 update
		searchMap.clear();
		searchMap.put("assessorId", assessorId);
		searchMap.put("ids", ids);
		if(ids.size() > 0) {
			adJiucuoTaskMapper.updateAssessorId(searchMap);
		}
		return taskVos;
	}
	
	@Override
	public int selectCountByActivityAndSeat(Map<String, Object> searchMap) {
		return adJiucuoTaskMapper.selectCountByActivityAndSeat(searchMap);
	}
	
	@Override
	public List<AdJiucuoTask> selectInfoByQrCode(Map<String, Object> searchMap) {
		return adJiucuoTaskMapper.selectInfoByQrCode(searchMap);
	}
	
	@Override
	public List<AdJiucuoTask> selectInfoByLonLatTitle(Map<String, Object> searchMap) {
		return adJiucuoTaskMapper.selectInfoByLonLatTitle(searchMap);
	}
}
