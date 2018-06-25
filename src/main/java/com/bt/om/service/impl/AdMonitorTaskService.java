package com.bt.om.service.impl;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.adtime.common.lang.CollectionUtil;
import com.adtime.common.lang.StringUtil;
import com.bt.om.common.DateUtil;
import com.bt.om.entity.AdActivity;
import com.bt.om.entity.AdJiucuoTask;
import com.bt.om.entity.AdMonitorTask;
import com.bt.om.entity.AdMonitorTaskFeedback;
import com.bt.om.entity.AdMonitorUserTask;
import com.bt.om.entity.AdSeatInfo;
import com.bt.om.entity.AdUserMessage;
import com.bt.om.entity.SysUser;
import com.bt.om.entity.SysUserExecute;
import com.bt.om.entity.vo.AbandonTaskVo;
import com.bt.om.entity.vo.AdMonitorTaskMobileVo;
import com.bt.om.entity.vo.AdMonitorTaskVo;
import com.bt.om.entity.vo.AllAdMonitorTaskVo;
import com.bt.om.entity.vo.PictureVo;
import com.bt.om.enums.MessageIsFinish;
import com.bt.om.enums.MessageType;
import com.bt.om.enums.MonitorTaskStatus;
import com.bt.om.enums.MonitorTaskType;
import com.bt.om.enums.RewardTaskType;
import com.bt.om.enums.SessionKey;
import com.bt.om.enums.TaskProblemStatus;
import com.bt.om.mapper.AdActivityAdseatMapper;
import com.bt.om.mapper.AdActivityMapper;
import com.bt.om.mapper.AdJiucuoTaskMapper;
import com.bt.om.mapper.AdMonitorRewardMapper;
import com.bt.om.mapper.AdMonitorTaskFeedbackMapper;
import com.bt.om.mapper.AdMonitorTaskMapper;
import com.bt.om.mapper.AdMonitorUserTaskMapper;
import com.bt.om.mapper.AdSeatInfoMapper;
import com.bt.om.mapper.AdUserMessageMapper;
import com.bt.om.mapper.SysResourcesMapper;
import com.bt.om.mapper.SysUserExecuteMapper;
import com.bt.om.mapper.SysUserMapper;
import com.bt.om.mapper.SysUserResMapper;
import com.bt.om.security.ShiroUtils;
import com.bt.om.service.IAdMonitorTaskService;
import com.bt.om.util.ConfigUtil;
import com.bt.om.vo.web.SearchDataVo;
import com.bt.om.web.util.JPushUtils;

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
    @Autowired
    private AdMonitorUserTaskMapper adMonitorUserTaskMapper;
    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private SysResourcesMapper sysResourcesMapper;
	@Autowired
	private SysUserResMapper sysUserResMapper;
    @Autowired
	private AdUserMessageMapper adUserMessageMapper;
    @Autowired
    private AdActivityAdseatMapper adActivityAdseatMapper;
    @Autowired
    private AdActivityMapper adActivityMapper;
    @Autowired
    private SysUserExecuteMapper sysUserExecuteMapper;

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
    public void assign(String[] taskIds, Integer userId, Integer assignorId) {
    	SysUser auditPerson = sysUserMapper.selectByPrimaryKey(assignorId); //获取指派人的相关信息
    	SysUserExecute sysUserExecute = sysUserExecuteMapper.selectByPrimaryKey(userId); //app执行人员的信息
    	
        Date now = new Date();
        //[4] 确认操作在业务层方法里进行循环
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
            //任务未查到
            if(task == null){
                continue;
            }
            
            //【待执行】状态时 修改被指派人
            if(task.getStatus() == MonitorTaskStatus.TO_CARRY_OUT.getId()) {
            	//[1] 清理主表信息
            	AdMonitorTask adMonitorTask = new AdMonitorTask();
            	adMonitorTask.setId(id);
            	adMonitorTask.setStatus(MonitorTaskStatus.UNASSIGN.getId()); //改回待指派
            	adMonitorTaskMapper.cleanTask(adMonitorTask);
            	//[2] 清理副表信息
            	AdMonitorUserTask userTask = new AdMonitorUserTask();
            	userTask.setMonitorTaskId(id);
            	userTask.setStatus(1);
    			adMonitorUserTaskMapper.cleanTask(userTask);
            }
            
            adMonitorTaskMapper.grabTask(userId,id,task.getUpdateTime()); //用户获取任务, 修改任务主表
            SysUser loginUser = (SysUser)ShiroUtils.getSessionAttribute(SessionKey.SESSION_LOGIN_USER.toString());
            //添加用户和任务关联关系
            AdMonitorUserTask userTask = new AdMonitorUserTask();
            userTask.setUserId(userId);
            userTask.setAssignUserId(loginUser.getId());
            userTask.setMonitorTaskId(id);
            userTask.setStartTime(now);
            userTask.setEndTime(Date.from(task.getMonitorDate().toInstant().atZone(ZoneId.systemDefault()).plusDays(task.getMonitorLastDays()).minusSeconds(1).toInstant()));
            userTask.setAssignType(1);
            userTask.setStatus(1);
            userTask.setCreateTime(now);
            userTask.setUpdateTime(now);
            task.setAssignorId(loginUser.getId());
            task.setAssignorTime(now);
            task.setStatus(2);//变为待执行
            task.setUserId(userId);//执行人员id
            adMonitorTaskMapper.updateByPrimaryKey(task);
            adMonitorUserTaskMapper.insertSelective(userTask);
            
            //指派成功修改站内信
	        String taskType = null;
	        if(task.getTaskType()==1) {
	        	taskType = "上刊监测";
	        }else if(task.getTaskType()==2) {
	        	taskType = "投放期间监测";
	        }else if(task.getTaskType()==3) {
	        	taskType = "下刊监测";
	        }else if(task.getTaskType()==5) {
	        	taskType = "上刊";
	        }else if(task.getTaskType()==6) {
	        	taskType = "追加监测";
	        }
	        
	        Map<String, Object> searchMap = new HashMap<>();
	        AdActivity adActivity = adActivityMapper.selectByPrimaryKey(task.getActivityId());//通过id找到广告商id
        	SysUser sysUser = sysUserMapper.selectByPrimaryKey(adActivity.getUserId());//获得广告商名
	        StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("【");
            stringBuffer.append(sysUser.getRealname());
            stringBuffer.append("】广告主的【");
            stringBuffer.append(adActivity.getActivityName());
            stringBuffer.append("】活动的【");
            stringBuffer.append(taskType);
            stringBuffer.append("任务】已被【");
            stringBuffer.append(auditPerson.getRealname());
            stringBuffer.append("】指派给【");
            String realName = null;
            //若app人员没有填写真实姓名, 取其登录的用户名
            if(StringUtil.isNotBlank(sysUserExecute.getRealname())) {
            	realName = sysUserExecute.getRealname();
            } else {
            	realName = sysUserExecute.getUsername();
            }
            stringBuffer.append(realName);
            stringBuffer.append("】");
            
            searchMap.put("content", stringBuffer.toString());
            searchMap.put("isFinish", MessageIsFinish.UNCONFIRM.getId()); //1：已处理
            searchMap.put("targetId", id); //任务表的id
            searchMap.put("type", MessageType.TASK_ASSIGN.getId()); //3,"任务指派"
            adUserMessageMapper.updateUserMessage(searchMap);
        }
    }

    @Override
    public void update(AdMonitorTask task) {
        task.setUpdateTime(new Date());
        adMonitorTaskMapper.updateByPrimaryKeySelective(task);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void pass(String[] taskIds, Integer assessorId, Integer status) {
    	SysUser auditPerson = sysUserMapper.selectByPrimaryKey(assessorId); //获取审核人的相关信息
    	
    	//[4] 确认操作在业务层方法里进行循环
    	for (String taskId : taskIds) {
    		Integer id = Integer.parseInt(taskId);
	        Date now = new Date();
	        AdMonitorTask task= new AdMonitorTask();
	        task.setId(id);
	        task.setStatus(status);
	        task.setAssessorId(assessorId);
	        AdMonitorTaskFeedback feedback = null;
	        //如果监测反馈有问题，问题状态置为有问题，否则无问题
	        List<AdMonitorTaskFeedback> feedbacks = adMonitorTaskFeedbackMapper.selectByTaskId(task.getId(), 1);
	//        AdMonitorTaskFeedback feedback = adMonitorTaskFeedbackMapper.selectByTaskId(task.getId(), 1).get(0);
	        if(feedbacks == null || feedbacks.size() == 0) {
	        	task.setProblemStatus(TaskProblemStatus.NO_PROBLEM.getId());
	        } else {
	        	feedback = feedbacks.get(0);
	        	if (feedback.getProblem() != null || feedback.getProblemOther() != null) {
	                task.setProblemStatus(TaskProblemStatus.PROBLEM.getId());
	            } else {
	                task.setProblemStatus(TaskProblemStatus.NO_PROBLEM.getId());
	            }
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
	        //现在又没有上刊安装任务了
	//        if(task.getTaskType().equals(MonitorTaskType.SET_UP_MONITOR.getId())){
	//            adMonitorTaskMapper.activeTask(task.getActivityAdseatId());
	//        }
	        //如果是上刊安装任务，并且广告位上并未记录经纬度,就把安装人员完成任务时的经纬度记录给广告位
	        //现在又没有上刊安装任务了
	//        if(task.getTaskType().equals(MonitorTaskType.SET_UP_MONITOR.getId())){
	//            AdSeatInfo seatInfo = adSeatInfoMapper.getAdSeatInfoByAdActivitySeatId(task.getActivityAdseatId());
	//            if(seatInfo.getLon()==null||seatInfo.getLat()==null){
	//                seatInfo.setLon(feedback.getLon());
	//                seatInfo.setLat(feedback.getLat());
	//                adSeatInfoMapper.updateByPrimaryKeySelective(seatInfo);
	//            }
	//        }
	        //现在又没有上刊安装任务了
	        //审核通过时，如果广告位上并未记录经纬度,就把该任务完成时的经纬度记录给广告位
	        AdSeatInfo seatInfo = adSeatInfoMapper.getAdSeatInfoByAdActivitySeatId(task.getActivityAdseatId());
	        if(seatInfo.getLon()==null||seatInfo.getLat()==null){
	        	if(feedback != null) {
	        		seatInfo.setLon(feedback.getLon());
	                seatInfo.setLat(feedback.getLat());
	                adSeatInfoMapper.updateByPrimaryKeySelective(seatInfo);
	        	}
	        }
	
//	        //奖励相关
//	        AdMonitorReward reward = new AdMonitorReward();
//	        reward.setMonitorTaskId(task.getId());
//	        reward.setType(RewardType.ADD.getId());
//	        reward.setTaskType(RewardTaskType.MONITOR.getId());
//	        reward.setUserId(task.getUserId());
//	        //? 奖励点数设计，这里随便写死10点
//	        reward.setRewardPoints(10);
//	        reward.setCreateTime(now);
//	        reward.setUpdateTime(now);
//	        adMonitorRewardMapper.insert(reward);
	        
	        //审核成功修改站内信
	        String taskType = null;
	        if(task.getTaskType()==1) {
	        	taskType = "上刊监测";
	        }else if(task.getTaskType()==2) {
	        	taskType = "投放期间监测";
	        }else if(task.getTaskType()==3) {
	        	taskType = "下刊监测";
	        }else if(task.getTaskType()==5) {
	        	taskType = "上刊";
	        }else if(task.getTaskType()==6) {
	        	taskType = "追加监测";
	        }
	        
	        Map<String, Object> searchMap = new HashMap<>();
	        AdActivity adActivity = adActivityMapper.selectByPrimaryKey(task.getActivityId());//通过id找到广告商id
        	SysUser sysUser = sysUserMapper.selectByPrimaryKey(adActivity.getUserId());//获得广告商名
	        StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("【");
            stringBuffer.append(sysUser.getRealname());
            stringBuffer.append("】广告主的【");
            stringBuffer.append(adActivity.getActivityName());
            stringBuffer.append("】活动的【");
            stringBuffer.append(taskType);
            stringBuffer.append("任务】已被【");
            stringBuffer.append(auditPerson.getRealname());
            stringBuffer.append("】审核通过");
            
            searchMap.put("content", stringBuffer.toString());
            searchMap.put("isFinish", MessageIsFinish.UNCONFIRM.getId()); //1：已处理
            searchMap.put("targetId", id); //任务表的id
            searchMap.put("type", MessageType.TASK_AUDIT.getId()); //2,"任务审核"
            adUserMessageMapper.updateUserMessage(searchMap);
	    }
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void reject(String[] taskIds, String reason, Integer assessorId, Integer status) {
    	SysUser auditPerson = sysUserMapper.selectByPrimaryKey(assessorId); //获取审核人的相关信息
    	
    	//[4] 确认操作在业务层方法里进行循环
    	for (String taskId : taskIds) {
	    	Integer id = Integer.parseInt(taskId);
	        Date now = new Date();
	        AdMonitorTask task= new AdMonitorTask();
	        task.setVerifyTime(now);
	        task.setUpdateTime(now);
	        task.setId(id);
	        task.setStatus(status);
	        task.setAssessorId(assessorId);
	        adMonitorTaskMapper.updateByPrimaryKeySelective(task);
	        List<AdMonitorTaskFeedback> feedbacks = adMonitorTaskFeedbackMapper.selectByTaskId(task.getId(), 1);
	        for (AdMonitorTaskFeedback feedback : feedbacks) {
	            feedback.setReason(reason);
	            feedback.setUpdateTime(now);
	            adMonitorTaskFeedbackMapper.updateByPrimaryKeySelective(feedback);
	        }
	        
	        //审核不通过修改站内信
	        task = adMonitorTaskMapper.selectByPrimaryKey(task.getId());
	        String taskType = null;
	        if(task.getTaskType()==1) {
	        	taskType = "上刊监测";
	        }else if(task.getTaskType()==2) {
	        	taskType = "投放期间监测";
	        }else if(task.getTaskType()==3) {
	        	taskType = "下刊监测";
	        }else if(task.getTaskType()==5) {
	        	taskType = "上刊";
	        }else if(task.getTaskType()==6) {
	        	taskType = "追加监测";
	        }
	        
	        Map<String, Object> searchMap = new HashMap<>();
	        AdActivity adActivity = adActivityMapper.selectByPrimaryKey(task.getActivityId());//通过id找到广告商id
        	SysUser sysUser = sysUserMapper.selectByPrimaryKey(adActivity.getUserId());//获得广告商名
	        StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("【");
            stringBuffer.append(sysUser.getRealname());
            stringBuffer.append("】广告主的【");
            stringBuffer.append(adActivity.getActivityName());
            stringBuffer.append("】活动的【");
            stringBuffer.append(taskType);
            stringBuffer.append("任务】已被【");
            stringBuffer.append(auditPerson.getRealname());
            stringBuffer.append("】审核不通过");
            
            searchMap.put("content", stringBuffer.toString());
            searchMap.put("isFinish", MessageIsFinish.UNCONFIRM.getId()); //1：已处理
            searchMap.put("targetId", id); //任务表的id
            searchMap.put("type", MessageType.TASK_AUDIT.getId()); //2,"任务审核"
            adUserMessageMapper.updateUserMessage(searchMap);
    	}
    }

    @Override
    public List<AdMonitorTaskMobileVo> getByUserIdForMobile(Integer userId) {
        return adMonitorTaskMapper.selectByUserId(userId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void feedback(Integer taskId, AdMonitorTaskFeedback feedback,String adSeatCode,SysUserExecute user) {
        Date now = new Date();
        Integer auditTime = ConfigUtil.getInt("audit_time"); //允许任务审核天数
        
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
            //现在又没有上刊安装任务了
//            if(task.getTaskType()==MonitorTaskType.SET_UP_MONITOR.getId()){
//                //上刊安装任务不提供未完成状态
//                task.setStatus(MonitorTaskStatus.UNVERIFY.getId());
//            }else {
//                //如果本次提交的照片不足，任务状态设置为"未完成"，否则进入待审核
//                if (feedback.getPicUrl1() == null || feedback.getPicUrl2() == null || feedback.getPicUrl3() == null || feedback.getPicUrl4() == null) {
//                    task.setStatus(MonitorTaskStatus.UN_FINISHED.getId());
//                } else {
//                    task.setStatus(MonitorTaskStatus.UNVERIFY.getId());
//                }
//            }
            if(task.getTaskType() == 5) {
            	//上刊任务, 不校验, 直接审核通过
            	task.setStatus(MonitorTaskStatus.VERIFIED.getId());
            } else {
            	//非上刊任务做校验
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
            //现在又没有上刊安装任务了
//            if (task.getTaskType()== MonitorTaskType.SET_UP_MONITOR.getId()) {
//                AdSeatInfo seatInfo = adSeatInfoMapper.getAdSeatInfoByAdActivitySeatId(task.getActivityAdseatId());
//                //如果广告位没绑定二维码，本次绑定激活
//                if(StringUtil.isEmpty(seatInfo.getAdCode())){
//                    if(StringUtil.isEmpty(adSeatCode)){
//                        throw new RuntimeException("广告位未激活，需提供广告位二维码");
//                    }
//                    seatInfo.setAdCode(adSeatCode);
//                    seatInfo.setUpdateTime(now);
//                    adSeatInfoMapper.updateByPrimaryKeySelective(seatInfo);
//                }
//            }

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
        
        //如果任务到了待审核的状态【普通监测任务】/ 审核通过的状态【上刊任务】, 则需要插入站内信
        if(task.getStatus() == MonitorTaskStatus.UNVERIFY.getId() || task.getStatus() == MonitorTaskStatus.VERIFIED.getId()) {
        	List<Integer> list = new ArrayList<>();
	        list = sysUserMapper.getUserId(4);//4：超级管理员
	        Integer dep_id = sysResourcesMapper.getUserId(2);//2：任务审核、指派部门
	        List<AdUserMessage> message = new ArrayList<>();
	        
			AdMonitorTask adMonitorTask = adMonitorTaskMapper.selectByPrimaryKey(taskId);
        	AdActivity adActivity = adActivityMapper.selectByPrimaryKey(adMonitorTask.getActivityId());//通过id找到广告商id
        	SysUser sysUser = sysUserMapper.selectByPrimaryKey(adActivity.getUserId());//获得广告商名
			List<Integer> reslist = sysUserResMapper.getUserId(adActivity.getUserId(),2);//获取广告商下面的组id集合
	        Integer resId = null;
	        for(Integer i:reslist) {
	        	resId = sysResourcesMapper.getResId(i,2);//找到任务审核的组id
	        	if(resId != null) {
	        		break;
	        	}
	        }
	        List<Integer> cuslist = sysUserResMapper.getAnotherUserId(resId, 1);//获取组下面的员工id集合
	        List<Integer> userIdList = new ArrayList<>();
	        for(Integer i : list) {
	        	userIdList.add(i);
	        }
	        for(Integer i: cuslist) {
	        	userIdList.add(i);
	        }
	        userIdList.add(dep_id);
	        String taskType = null;
	        if(adMonitorTask.getTaskType()==1) {
	        	taskType = "上刊监测";
	        }else if(adMonitorTask.getTaskType()==2) {
	        	taskType = "投放期间监测";
	        }else if(adMonitorTask.getTaskType()==3) {
	        	taskType = "下刊监测";
	        }else if(adMonitorTask.getTaskType()==5) {
	        	taskType = "上刊";
	        }else if(adMonitorTask.getTaskType()==6) {
	        	taskType = "追加监测";
	        }
	        
	        StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("【");
            stringBuffer.append(sysUser.getRealname());
            stringBuffer.append("】广告主的【");
            stringBuffer.append(adActivity.getActivityName());
            stringBuffer.append("】活动的【");
            stringBuffer.append(taskType);
            stringBuffer.append("任务】已被【");
            stringBuffer.append(user.getRealname());
            stringBuffer.append("】执行");
            if(task.getStatus() == MonitorTaskStatus.UNVERIFY.getId()) {
            	stringBuffer.append("，待审核");
            } else {
            	stringBuffer.append("有");
            	stringBuffer.append(auditTime);
            	stringBuffer.append("天可替换图片");
            }
            
	        for(Integer i: userIdList) {
            	AdUserMessage mess = new AdUserMessage();
            	mess.setContent(stringBuffer.toString());
            	mess.setTargetId(taskId); //任务表的id
            	mess.setTargetUserId(i);
            	mess.setIsFinish(MessageIsFinish.CONFIRMED.getId()); //0：未处理
            	mess.setType(MessageType.TASK_AUDIT.getId()); //2：任务审核
            	mess.setCreateTime(now);
            	mess.setUpdateTime(now);
            	message.add(mess);
            }
	        if(message != null && message.size() > 0) {
	        	adUserMessageMapper.insertMessage(message);
	        }
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
        Date now = new Date();
        boolean ret = false;
        //先查询该任务
        AdMonitorTask task = adMonitorTaskMapper.selectByPrimaryKey(id);
        //任务未查到或任务执行人已指派，抢任务失败
        if(task==null||task.getUserId()!=null&&task.getStatus()!=1){
            return ret;
        }
        int count = adMonitorTaskMapper.grabTask(userId,id,task.getUpdateTime());
        if(count>0){
            //添加用户和任务关联关系
            AdMonitorUserTask userTask = new AdMonitorUserTask();
            userTask.setUserId(userId);
            userTask.setMonitorTaskId(id);
            userTask.setStartTime(now);
            userTask.setEndTime(Date.from(now.toInstant().atZone(ZoneId.systemDefault()).plusHours(12).toInstant()));
            userTask.setAssignType(2);
            userTask.setStatus(1);
            userTask.setCreateTime(now);
            userTask.setUpdateTime(now);
            adMonitorUserTaskMapper.insertSelective(userTask);
            
            ret = true;
        }
        
        return ret;
    }

	@Override
  @Transactional(rollbackFor = Exception.class)
	public List<AdMonitorTask> selectAllTask() {
		List<AdMonitorTask> taskList = adMonitorTaskMapper.findAllTask();		
		return taskList;
	}

	@Override
	public void getPageDataAllTask(SearchDataVo vo) {
		int count = adMonitorTaskMapper.getPageCountAllTask(vo.getSearchMap());
        vo.setCount(count);
        if (count > 0) {
            vo.setList(adMonitorTaskMapper.getPageDataAllTask(vo.getSearchMap(), new RowBounds(vo.getStart(), vo.getSize())));
        } else {
            vo.setList(new ArrayList<AllAdMonitorTaskVo>());
        }
    }

	@Override
	public List<AdMonitorTaskVo> selectAllByAssessorId(Map<String, Object> searchMap) {
		return adMonitorTaskMapper.selectAllByAssessorId(searchMap);
	}
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public List<AdMonitorTaskVo> getTenAdMonitorTaskVo(Map<String, Object> searchMap) {
		//[1] 查询 for update
		Integer assessorId = (Integer) searchMap.get("assessorId");
		List<AdMonitorTaskVo> taskVos = adMonitorTaskMapper.getTenAdMonitorTaskVo(searchMap);
		List<Integer> ids = new ArrayList<>();
		for (AdMonitorTaskVo adMonitorTaskVo : taskVos) {
			ids.add(adMonitorTaskVo.getId());
			adMonitorTaskVo.setAssessorId(assessorId);
		}
		//[2] 更新 update
		searchMap.clear();
		searchMap.put("assessorId", assessorId);
		searchMap.put("ids", ids);
		if(ids.size() > 0) {
			adMonitorTaskMapper.updateAssessorId(searchMap);
		}
		return taskVos;
	}
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public List<AdMonitorTaskVo> getTenAdMonitorTaskAssignVo(Map<String, Object> searchMap) {
		//[1] 查询 for update
		Integer assignorId = (Integer) searchMap.get("assignorId");
		List<AdMonitorTaskVo> taskVos = adMonitorTaskMapper.getTenAdMonitorTaskVo(searchMap);
		List<Integer> ids = new ArrayList<>();
		for (AdMonitorTaskVo adMonitorTaskVo : taskVos) {
			ids.add(adMonitorTaskVo.getId());
			adMonitorTaskVo.setAssignorId(assignorId);
		}
		//[2] 更新 update
		searchMap.clear();
		searchMap.put("assignorId", assignorId);
		searchMap.put("ids", ids);
		if(ids.size() > 0) {
			adMonitorTaskMapper.updateAssignorId(searchMap);
		}
		return taskVos;
	}

  @Override
	public List<AdMonitorTask> selectLatestMonitorTaskIds(Integer activityId) {
		return adMonitorTaskMapper.selectLatestMonitorTaskIds(activityId);
	}
    
	@Override
	public List<AdMonitorTaskFeedback> selectByActivity(List<Integer> monitorTaskIds) {
		return adMonitorTaskFeedbackMapper.selectByActivity(monitorTaskIds);
	}

	/**
	 * 新版本的激活任务定时: 只有"待指派"
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void newActivateMonitorTask(Date nowDate) {
		//[1] 激活任务
		List<Integer> ids = adMonitorTaskMapper.getWaitToActivateIds(nowDate);
		adMonitorTaskMapper.newActivateMonitorTask(nowDate);
		
		//[2] 添加站内信
		List<Integer> userIds = new ArrayList<>();
		Date now = new Date();
		
		if(ids != null && ids.size() > 0) {
			//查询添加站内信的用户id集合
			userIds = sysUserMapper.getUserId(4);//超级管理员id
			Integer dep_id = sysResourcesMapper.getUserId(2);//2：任务审核、指派部门
			userIds.add(dep_id);
			
			for (Integer monitorId : ids) {
				List<AdUserMessage> message = new ArrayList<>();
				//员工
				AdMonitorTask adMonitorTask = adMonitorTaskMapper.selectByPrimaryKey(monitorId);
	        	AdActivity adActivity = adActivityMapper.selectByPrimaryKey(adMonitorTask.getActivityId());//通过id找到广告商id
	        	SysUser sysUser = sysUserMapper.selectByPrimaryKey(adActivity.getUserId());//获得广告商名
				
				List<Integer> reslist = sysUserResMapper.getUserId(adActivity.getUserId(),2);//获取广告商下面的组id集合
		        Integer resId = null;
		        for(Integer i:reslist) {
		        	resId = sysResourcesMapper.getResId(i,2);//找到任务审核的组id
		        	if(resId != null) {
		        		break;
		        	}
		        }
		        List<Integer> cuslist = sysUserResMapper.getAnotherUserId(resId, 1);//获取组下面的员工id集合
		        
		        List<Integer> userIdList = new ArrayList<>();
		        userIdList.addAll(userIds);
		        userIdList.addAll(cuslist);
		        
		        String taskType = null;
		        if(adMonitorTask.getTaskType()==1) {
		        	taskType = "上刊监测";
		        }else if(adMonitorTask.getTaskType()==2) {
		        	taskType = "投放期间监测";
		        }else if(adMonitorTask.getTaskType()==3) {
		        	taskType = "下刊监测";
		        }else if(adMonitorTask.getTaskType()==5) {
		        	taskType = "上刊";
		        }else if(adMonitorTask.getTaskType()==6) {
		        	taskType = "追加监测";
		        }
		        
		        StringBuffer stringBuffer = new StringBuffer();
	            stringBuffer.append("【");
	            stringBuffer.append(sysUser.getRealname());
	            stringBuffer.append("】广告主的【");
	            stringBuffer.append(adActivity.getActivityName());
	            stringBuffer.append("】活动的【");
	            stringBuffer.append(taskType);
	            stringBuffer.append("任务】待指派");
	            
		        for(Integer i: userIdList) {
	            	AdUserMessage mess = new AdUserMessage();
	            	mess.setContent(stringBuffer.toString());
	            	mess.setTargetId(monitorId); //任务表的id
	            	mess.setTargetUserId(i);
	            	mess.setIsFinish(MessageIsFinish.CONFIRMED.getId()); //0：未处理
	            	mess.setType(MessageType.TASK_ASSIGN.getId()); //3：任务指派
	            	mess.setCreateTime(now);
	            	mess.setUpdateTime(now);
	            	message.add(mess);
	            }
		        if(message != null && message.size() > 0) {
		        	adUserMessageMapper.insertMessage(message);
		        }
			}
		}
	}
	
	/**
	 * 老版本的激活任务定时: 有24小时的逻辑激活任务成"待指派"或"可抢单"
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void activateMonitorTask(Date nowDate) {
		adMonitorTaskMapper.activateMonitorTask(nowDate);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void recycleMonitorTask() {
		Date now = new Date();
		//[1] 查询ad_monitor_user_task待回收的任务id集合
		List<Integer> monitorTaskIds = adMonitorUserTaskMapper.selectRecycleTaskIds(now);
		//[2] 修改ad_monitor_user_task表中待回收的状态及回收时间
		if(CollectionUtil.isNotEmpty(monitorTaskIds)) {
			AdMonitorUserTask task = new AdMonitorUserTask();
			task.setAbandonTime(now);
			task.setUpdateTime(now);
			task.setStatus(3);
			adMonitorUserTaskMapper.recycleUserTask(task);
			//[3] 修改ad_monitor_task表中回收的任务的状态为 1：待指派 或 8：可抢单
			adMonitorTaskMapper.recycleTask(monitorTaskIds, 12);
		}
	}
	
	/**
	 * 获取即将结束的任务(2小时之前)
	 */
	@Override
	public String getTaskWillEnd(Integer duration) {
		List<AdMonitorUserTask> adMonitorUserTasks = adMonitorUserTaskMapper.getTaskWillEnd(duration);
		List<Integer> ids = new ArrayList<>();
		//==========任务即将结束之后根据接取任务的用户id进行app消息推送==============
		//用set存储避免一个用户创建多个活动，进行多次推送 
        Set<String> aliases = new HashSet<String>();
        for(AdMonitorUserTask adMonitorUserTask : adMonitorUserTasks) {
        	aliases.add(String.valueOf(adMonitorUserTask.getUserId())); //把userId列表转成String类型，极光推送api需要
        	ids.add(adMonitorUserTask.getId());
        }
        Map<String, Object> param = new HashMap<>();
        Map<String, String> extras = new HashMap<>();
        extras.put("type", "task_will_end_push");
        param.put("msg", "您的任务有的即将结束，请尽快办理！");
        param.put("title", "玖凤平台");
        param.put("alias", aliases);  //根据别名选择推送用户（这里userId用作推送时的用户别名）
        param.put("extras", extras);
        String pushResult = JPushUtils.pushAllByAlias(param);
        System.out.println("pushResult:: " + pushResult);
        //消息推送之后将任务is_push状态更新为1（已推送）
        if(ids != null && ids.size() > 0) {
        	adMonitorUserTaskMapper.updateIsPush(ids);
        }
		return pushResult;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void forceAssignTask() {
		adMonitorTaskMapper.forceAssignTask(12);
	}

	@Override
	public AdMonitorTask selectByPrimaryKey(Integer id) {
		return adMonitorTaskMapper.selectByPrimaryKey(id);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void abandonUserTask(AbandonTaskVo vo) {
		adMonitorUserTaskMapper.abandonUserTask(vo);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void offAdMonitorTaskByAssessorId(Integer id) {
		adMonitorTaskMapper.cancelAdMonitorTaskByAssessorId(id);	
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void offAdMonitorTaskByAssignorId(Integer id) {
		adMonitorTaskMapper.cancelAdMonitorTaskByAssignorId(id);	
	}

	@Override
	public List<AdMonitorTask> getAllByStatusUnCheck(Map<String, Object> searchMap) {
		return adMonitorTaskMapper.getAllByStatusUnCheck(searchMap);
	}

	@Override
	public List<AdMonitorTask> getAllByStatusUnZhipai(Map<String, Object> searchMap) {
		return adMonitorTaskMapper.getAllByStatusUnZhipai(searchMap);
	}
	
	@Override
	public List<PictureVo> selectFeedBackByActivityIdAndSeatId(Map<String, Object> searchMap) {
		return adMonitorTaskMapper.selectFeedBackByActivityIdAndSeatId(searchMap);
	}

	@Override
	public void getTaskPageData(SearchDataVo datavo) {
		int count = adMonitorTaskMapper.getTaskPageCount(datavo.getSearchMap());
		datavo.setCount(count);
        if (count > 0) {
        	datavo.setList(adMonitorTaskMapper.getTaskPageData(datavo.getSearchMap(), new RowBounds(datavo.getStart(), datavo.getSize())));
        } else {
        	datavo.setList(new ArrayList<AdMonitorTask>());
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
		
		//[1] 替换反馈表中的图片
		if(index == 1) {
			searchMap.put("picUrl1", picUrl);
			adMonitorTaskFeedbackMapper.updatePicUrl1(searchMap);
		} else if (index == 2) {
			searchMap.put("picUrl2", picUrl);
			adMonitorTaskFeedbackMapper.updatePicUrl2(searchMap);
		} else if (index == 3) {
			searchMap.put("picUrl3", picUrl);
			adMonitorTaskFeedbackMapper.updatePicUrl3(searchMap);
		} else if (index == 4) {
			searchMap.put("picUrl4", picUrl);
			adMonitorTaskFeedbackMapper.updatePicUrl4(searchMap);
		}
		
		//[2] 更新主任务表的状态
		AdMonitorTaskFeedback taskFeedback = adMonitorTaskFeedbackMapper.selectByPrimaryKey(id);
		adMonitorTaskMapper.changeStatusAndproblemStatus(taskFeedback.getMonitorTaskId());
	}

	 /**
	  * 批量插入追加监测任务
	  */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void insertMonitorTask(Integer activityId, List<String> seatIds, String reportTime) {
		Integer monitorTime = ConfigUtil.getInt("monitor_time"); //允许任务执行天数
        Integer auditTime = ConfigUtil.getInt("audit_time"); //允许任务审核天数
		
        //查询需要追加监测任务的广告位关联id集合
        Map<String, Object> searchMap = new HashMap<>();
        searchMap.put("activityId", activityId);
        searchMap.put("seatIds", seatIds);
        List<Integer> adActivityAdseatIds = adActivityAdseatMapper.selectByActivityIdAndSeatIds(searchMap);
        
		List<AdMonitorTask> tasks = new ArrayList<>();
		for (Integer seatId : adActivityAdseatIds) {
			// 追加监测任务
 			AdMonitorTask adMonitorTask = new AdMonitorTask();
 			
 			Date now = new Date();
 			adMonitorTask.setActivityId(activityId);
 			adMonitorTask.setActivityAdseatId(seatId);
 			adMonitorTask.setStatus(MonitorTaskStatus.UN_ACTIVE.getId());
 			adMonitorTask.setProblemStatus(TaskProblemStatus.UNMONITOR.getId());
 			adMonitorTask.setCreateTime(now);
 			adMonitorTask.setUpdateTime(now);
 			
 			adMonitorTask.setTaskType(MonitorTaskType.ZHUIJIA_MONITOR.getId());
            Date durationMonitorTask = DateUtil.parseStrDate(reportTime, "yyyy-MM-dd"); //页面上的追加监测任务出报告时间
            Date auditDate = DateUtil.addDays(durationMonitorTask, -auditTime); //提前两天审核+替换图片
            Date monitorDate = DateUtil.addDays(auditDate, -monitorTime); //提前三天执行任务
            int betweenDays = DateUtil.getBetweenDays(monitorDate, auditDate);
            adMonitorTask.setMonitorDate(monitorDate);
            adMonitorTask.setMonitorLastDays(betweenDays);
            adMonitorTask.setReportTime(durationMonitorTask);
            
            tasks.add(adMonitorTask);
		}
		
		//[1] 插入任务表
		if(tasks != null && tasks.size() > 0) {
			adMonitorTaskMapper.insertList(tasks);
		}
		
		//[2] 更新活动表
		AdActivity adActivity = adActivityMapper.selectByPrimaryKey(activityId);
		String zhuijiaMonitorTaskTime = adActivity.getZhuijiaMonitorTaskTime();
		if(StringUtil.isBlank(zhuijiaMonitorTaskTime)) {
			adActivity.setZhuijiaMonitorTaskTime(reportTime);
		} else {
			adActivity.setZhuijiaMonitorTaskTime(zhuijiaMonitorTaskTime + "," + reportTime);
		}
		adActivityMapper.updateByPrimaryKeySelective(adActivity);
	}

	@Override
	public AdMonitorTask getActivityId(int id) {
		return adMonitorTaskMapper.selectByPrimaryKey(id);
	}
	
	/**
	 * 【待执行】/【待指派】这种没有app人员做任务时的替换造假
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public int insertMonitorTaskFeedback(AdMonitorTaskFeedback feedback, Integer userId, Integer assessorId) {
		//[1] 更新任务主表的执行人员
		AdMonitorTask adMonitorTask = adMonitorTaskMapper.selectByPrimaryKey(feedback.getMonitorTaskId());
		adMonitorTask.setUserId(userId); //任务执行人ID
		adMonitorTask.setStatus(4); //4：通过审核
		adMonitorTask.setProblemStatus(2); //2：无问题
		adMonitorTask.setVerifyTime(feedback.getUpdateTime()); //审核时间
		adMonitorTask.setUpdateTime(feedback.getUpdateTime()); //更新时间
		adMonitorTask.setAssessorId(assessorId); //审核员id
		adMonitorTaskMapper.updateByPrimaryKeySelective(adMonitorTask);
		//[2] 插入新的feedback
		return adMonitorTaskFeedbackMapper.insert(feedback);
	}
	
	@Override
	public Integer selectCountByMonitorTaskId(int monitorTaskId) {
		return adMonitorTaskFeedbackMapper.selectCountByMonitorTaskId(monitorTaskId);
	}
	
	@Override
	public AdSeatInfo selectLonLatByMonitorTaskId(int monitorTaskId) {
		return adMonitorTaskMapper.selectLonLatByMonitorTaskId(monitorTaskId);
	}
}
