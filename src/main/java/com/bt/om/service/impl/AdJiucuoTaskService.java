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
import com.bt.om.mapper.SysResourcesMapper;
import com.bt.om.mapper.SysUserExecuteMapper;
import com.bt.om.mapper.SysUserMapper;
import com.bt.om.mapper.SysUserResMapper;
import com.bt.om.service.IAdJiucuoTaskService;
import com.bt.om.util.ConfigUtil;
import com.bt.om.vo.web.SearchDataVo;

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

    /**
     * 纠错任务被审核通过
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void pass(String[] jiucuoIds, Integer assessorId, Integer status) {
    	SysUser auditPerson = sysUserMapper.selectByPrimaryKey(assessorId); //获取审核人的相关信息
    	
    	//[4] 确认操作在业务层方法里进行循环
    	for (String jcId : jiucuoIds) {
    		//[1] 审核通过纠错任务
    		Integer id = Integer.parseInt(jcId);
	        Date now = new Date();
	        AdJiucuoTask task= new AdJiucuoTask();
	        task.setStatus(JiucuoTaskStatus.VERIFIED.getId());
	        task.setVerifyTime(now);
	        task.setId(id);
	        task.setAssessorId(assessorId); //审核人id
	        task.setStatus(status);
	        adJiucuoTaskMapper.updateByPrimaryKeySelective(task);
	        
	        //[2] 修改站内信
	        Map<String, Object> searchMap = new HashMap<>();
	        task = adJiucuoTaskMapper.selectByPrimaryKey(id);
	        AdActivity adActivity = adActivityMapper.selectByPrimaryKey(task.getActivityId());//通过id找到广告商id
        	SysUser sysUser = sysUserMapper.selectByPrimaryKey(adActivity.getUserId());//获得广告商名
        	AdSeatInfo adSeatInfo = adSeatInfoMapper.selectByPrimaryKey(task.getAdSeatId()); //获得对应广告位信息
        	
	        StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("【");
            stringBuffer.append(sysUser.getRealname());
            stringBuffer.append("】广告主的【");
            stringBuffer.append(adActivity.getActivityName());
            stringBuffer.append("】活动的【");
            if(adSeatInfo != null) {
            	stringBuffer.append(adSeatInfo.getName());
                stringBuffer.append("】广告位的【");
            }
            stringBuffer.append("纠错任务】已被【");
            stringBuffer.append(auditPerson.getRealname());
            stringBuffer.append("】审核通过");
            
            searchMap.put("content", stringBuffer.toString());
            searchMap.put("isFinish", MessageIsFinish.UNCONFIRM.getId()); //1：已处理
            searchMap.put("targetId", id); //纠错表的id
            searchMap.put("type", MessageType.JIUCUO_AUDIT.getId()); //4,"纠错审核"
            searchMap.put("updateTime", now); //修改时间
            adUserMessageMapper.updateUserMessage(searchMap);
        
//        task = adJiucuoTaskMapper.selectByPrimaryKey(task.getId());
//        SysUserExecute user = sysUserExecuteMapper.selectByPrimaryKey(task.getUserId());
//        if(user.getId() == SysUserExecuteType.WORKER.getId()) {
//            AdMonitorReward reward = new AdMonitorReward();
//            reward.setMonitorTaskId(task.getId());
//            reward.setType(RewardType.ADD.getId());
//            reward.setTaskType(RewardTaskType.JIUCUO.getId());
//            reward.setUserId(task.getUserId());
//            //? 奖励点数设计，这里随便写死15点
//            reward.setRewardPoints(15);
//            reward.setCreateTime(now);
//            reward.setUpdateTime(now);
//            adMonitorRewardMapper.insert(reward);
//        }
    	}
    }
    
    /**
     * 纠错任务被审核不通过
     */
    @Override
    public void reject(String[] jiucuoIds, String reason, Integer assessorId, Integer status) {
    	SysUser auditPerson = sysUserMapper.selectByPrimaryKey(assessorId); //获取审核人的相关信息
    	
    	//[4] 确认操作在业务层方法里进行循环
    	for (String taskId : jiucuoIds) {
	    	Integer id = Integer.parseInt(taskId);
	        Date now = new Date();
	        AdJiucuoTask task= new AdJiucuoTask();
		    task.setId(id);
	        task.setReason(reason);
	        task.setStatus(JiucuoTaskStatus.VERIFY_FAILURE.getId());
	        task.setVerifyTime(now);
	        task.setAssessorId(assessorId); //审核人id
	        adJiucuoTaskMapper.updateByPrimaryKeySelective(task);
	        
	        //[2] 修改站内信
	        Map<String, Object> searchMap = new HashMap<>();
	        task = adJiucuoTaskMapper.selectByPrimaryKey(id);
	        AdActivity adActivity = adActivityMapper.selectByPrimaryKey(task.getActivityId());//通过id找到广告商id
        	SysUser sysUser = sysUserMapper.selectByPrimaryKey(adActivity.getUserId());//获得广告商名
        	AdSeatInfo adSeatInfo = adSeatInfoMapper.selectByPrimaryKey(task.getAdSeatId()); //获得对应广告位信息
        	
	        StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("【");
            stringBuffer.append(sysUser.getRealname());
            stringBuffer.append("】广告主的【");
            stringBuffer.append(adActivity.getActivityName());
            stringBuffer.append("】活动的【");
            if(adSeatInfo != null) {
            	stringBuffer.append(adSeatInfo.getName());
                stringBuffer.append("】广告位的【");
            }
            stringBuffer.append("纠错任务】已被【");
            stringBuffer.append(auditPerson.getRealname());
            stringBuffer.append("】审核不通过");
            
            searchMap.put("content", stringBuffer.toString());
            searchMap.put("isFinish", MessageIsFinish.UNCONFIRM.getId()); //1：已处理
            searchMap.put("targetId", id); //纠错表的id
            searchMap.put("type", MessageType.JIUCUO_AUDIT.getId()); //4,"纠错审核"
            searchMap.put("updateTime", now); //修改时间
            adUserMessageMapper.updateUserMessage(searchMap);
	    }
    }
    
    /**
     * APP人员提交纠错任务
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void feedback(AdJiucuoTask task, AdJiucuoTaskFeedback feedback) {
    	//[1] 插入纠错主表
        Date now = new Date();
        task.setCreateTime(now);
        task.setSubmitTime(now);
        task.setUpdateTime(now);
        adJiucuoTaskMapper.insert(task);
        
        //[2] 插入纠错反馈表
        feedback.setJiucuoTaskId(task.getId());
        feedback.setCreateTime(now);
        feedback.setUpdateTime(now);
        adJiucuoTaskFeedbackMapper.insert(feedback);
        
        List<Integer> list = new ArrayList<>();
		List<Integer> cuslist = new ArrayList<>();
        list = sysUserMapper.getUserId(UserTypeEnum.SUPER_ADMIN.getId());//4：超级管理员
        Integer dep_id = sysResourcesMapper.getUserId(DepartmentTypeEnum.JIUCUO_TASK.getId());//3：纠错审核部门
        List<AdUserMessage> message = new ArrayList<>();
		
		AdActivity adActivity = null;
        AdJiucuoTask adJiucuoTask = adJiucuoTaskMapper.selectByPrimaryKey(task.getId());//通过纠错id找到activityId
    	adActivity = adActivityMapper.selectByPrimaryKey(adJiucuoTask.getActivityId());//通过id找到广告商id
    	SysUser sysUser = sysUserMapper.selectByPrimaryKey(adActivity.getUserId());//获得广告商名
    	AdSeatInfo adSeatInfo = adSeatInfoMapper.selectByPrimaryKey(task.getAdSeatId()); //获得对应广告位信息
    	
    	List<Integer> reslist = sysUserResMapper.getUserId(adActivity.getUserId(),2);//获取广告商下面的组id集合
    	Integer resId = null;
        for(Integer i:reslist) {
        	resId = sysResourcesMapper.getResId(i,3);//找到审核纠错的组id
        	if(resId != null) {
        		break;
        	}
        }
        cuslist = sysUserResMapper.getAnotherUserId(resId, 1);//获取组下面的员工id集合
        List<Integer> userIdList = new ArrayList<>();
        userIdList.addAll(list);
        userIdList.addAll(cuslist);
        list.removeAll(list);
        list = sysUserMapper.getUserId(UserTypeEnum.PHONE_OPERATOR.getId());//6:呼叫中心人员
        userIdList.addAll(list);
        userIdList.add(dep_id);
        
        SysUserExecute sysUserExecute = sysUserExecuteMapper.selectByPrimaryKey(task.getUserId()); //获取app提交人员的信息
        
        for(Integer i: userIdList) {
        	AdUserMessage mess = new AdUserMessage();
        	StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("【");
            stringBuffer.append(sysUser.getRealname());
            stringBuffer.append("】广告主的【");
            stringBuffer.append(adActivity.getActivityName());
            stringBuffer.append("】活动的【");
            if(adSeatInfo != null) {
            	stringBuffer.append(adSeatInfo.getName());
                stringBuffer.append("】广告位的【");
            }
            stringBuffer.append("纠错任务】已被【");
            String realName = null;
            //若app人员没有填写真实姓名, 取其登录的用户名
            if(StringUtil.isNotBlank(sysUserExecute.getRealname())) {
            	realName = sysUserExecute.getRealname();
            } else {
            	realName = sysUserExecute.getUsername();
            }
            stringBuffer.append(realName);
            stringBuffer.append("】提交，待审核");
        	
        	mess.setContent(stringBuffer.toString());
        	mess.setTargetId(task.getId()); //纠错任务的id
        	mess.setTargetUserId(i);
        	mess.setIsFinish(MessageIsFinish.CONFIRMED.getId()); //1: 未处理
        	mess.setType(MessageType.JIUCUO_AUDIT.getId()); //4: 纠错审核
        	mess.setCreateTime(now);
        	mess.setUpdateTime(now);
        	message.add(mess);
        }
        if(message != null && message.size() > 0) {
        	adUserMessageMapper.insertMessage(message);
        }
    }

    @Override
    public List<AdJiucuoTaskMobileVo> getByUserIdForMobile(Integer userId) {
        return adJiucuoTaskMapper.selectByUserId(userId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createSubTask(Integer taskId) {
    	Integer monitorTime = ConfigUtil.getInt("monitor_time"); //允许任务执行天数
        Date now = new Date();
        //查询父纠错任务信息
        AdJiucuoTask task = adJiucuoTaskMapper.selectByPrimaryKey(taskId);
        //创建子任务
        AdMonitorTask sub = new AdMonitorTask();
        sub.setTaskType(MonitorTaskType.FIX_CONFIRM.getId());
        //通过活动编号和广告位编号获取 活动和广告位的关联编号
        sub.setActivityAdseatId(adActivityAdseatMapper.selectByActivityAndSeatId(task.getActivityId(),task.getAdSeatId()).getId());
        sub.setStatus(MonitorTaskStatus.UNASSIGN.getId()); //待指派的任务
        sub.setProblemStatus(TaskProblemStatus.UNMONITOR.getId());
        sub.setActivityId(task.getActivityId());
        sub.setParentId(task.getId());
        sub.setParentType(RewardTaskType.JIUCUO.getId());
        sub.setSubCreated(2);
        sub.setCreateTime(now);
        sub.setUpdateTime(now);
        sub.setMonitorDate(now);
        sub.setMonitorLastDays(monitorTime);
        adMonitorTaskMapper.insertSelective(sub);
        //更新父任务
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
	@Transactional(rollbackFor = Exception.class)
	public void offJiucuoTaskByAssessorId(Integer id) {
		 adJiucuoTaskMapper.cancelJiucuoTaskByAssessorId(id);		
	}

	@Override
	public List<AdJiucuoTaskVo> getAllByStatusUnCheck(Map<String, Object> searchMap) {
		return adJiucuoTaskMapper.getAllByStatusUnCheck(searchMap);
	}
	
	@Override
	public List<AdJiucuoTask> selectInfoByQrCode(Map<String, Object> searchMap) {
		return adJiucuoTaskMapper.selectInfoByQrCode(searchMap);
	}
	
	@Override
	public List<AdJiucuoTask> selectInfoByLonLatTitle(Map<String, Object> searchMap) {
		return adJiucuoTaskMapper.selectInfoByLonLatTitle(searchMap);
	}
	
	@Override
	public List<AdJiucuoTask> selectInfoByMemo(Map<String, Object> searchMap) {
		return adJiucuoTaskMapper.selectInfoByMemo(searchMap);
	}

	@Override
	public void getJiucuoPageData(SearchDataVo datavo) {
		int count = adJiucuoTaskMapper.getJiucuoPageCount(datavo.getSearchMap());
        datavo.setCount(count);
        if(count>0){
            datavo.setList(adJiucuoTaskMapper.getJiucuoPageData(datavo.getSearchMap(), new RowBounds(datavo.getStart(), datavo.getSize())));
        }else{
        	datavo.setList(new ArrayList<AdJiucuoTask>());
        }
		
	}


	/**
	 * 替换任务反馈中的图片
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updatePicUrl(Integer id, String picUrl, Integer index) {
		Map<String, Object> searchMap = new HashMap<>();
		searchMap.put("id", id);
		if(index == 1) {
			searchMap.put("picUrl1", picUrl);
			adJiucuoTaskFeedbackMapper.updatePicUrl1(searchMap);
		} else if (index == 2) {
			searchMap.put("picUrl2", picUrl);
			adJiucuoTaskFeedbackMapper.updatePicUrl2(searchMap);
		} else if (index == 3) {
			searchMap.put("picUrl3", picUrl);
			adJiucuoTaskFeedbackMapper.updatePicUrl3(searchMap);
		} else if (index == 4) {
			searchMap.put("picUrl4", picUrl);
			adJiucuoTaskFeedbackMapper.updatePicUrl4(searchMap);
		}
	}

	@Override
	public AdJiucuoTask getActivityId(int id) {
		return adJiucuoTaskMapper.selectByPrimaryKey(id);
	}	
}
