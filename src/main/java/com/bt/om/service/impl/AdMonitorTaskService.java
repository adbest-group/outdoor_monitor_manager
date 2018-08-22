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
import com.bt.om.entity.AdActivityAdseat;
import com.bt.om.entity.AdJiucuoTask;
import com.bt.om.entity.AdMedia;
import com.bt.om.entity.AdMonitorTask;
import com.bt.om.entity.AdMonitorTaskFeedback;
import com.bt.om.entity.AdMonitorUserTask;
import com.bt.om.entity.AdSeatInfo;
import com.bt.om.entity.AdSystemPush;
import com.bt.om.entity.AdUserMessage;
import com.bt.om.entity.AdUserMoney;
import com.bt.om.entity.AdUserPoint;
import com.bt.om.entity.SysUser;
import com.bt.om.entity.SysUserExecute;
import com.bt.om.entity.vo.AbandonTaskVo;
import com.bt.om.entity.vo.AdActivityAdseatVo;
import com.bt.om.entity.vo.AdMonitorTaskMobileVo;
import com.bt.om.entity.vo.AdMonitorTaskVo;
import com.bt.om.entity.vo.AllAdMonitorTaskVo;
import com.bt.om.entity.vo.PictureVo;
import com.bt.om.entity.vo.PushInfoVo;
import com.bt.om.entity.vo.TaskAdSeat;
import com.bt.om.enums.AssignTypeEnum;
import com.bt.om.enums.DepartmentTypeEnum;
import com.bt.om.enums.MessageIsFinish;
import com.bt.om.enums.MessageType;
import com.bt.om.enums.MonitorTaskStatus;
import com.bt.om.enums.MonitorTaskType;
import com.bt.om.enums.RewardTaskType;
import com.bt.om.enums.SessionKey;
import com.bt.om.enums.SysUserExecuteType;
import com.bt.om.enums.TaskProblemStatus;
import com.bt.om.enums.UserTypeEnum;
import com.bt.om.enums.VerifyType;
import com.bt.om.mapper.AdActivityAdseatMapper;
import com.bt.om.mapper.AdActivityMapper;
import com.bt.om.mapper.AdJiucuoTaskMapper;
import com.bt.om.mapper.AdMediaMapper;
import com.bt.om.mapper.AdMonitorTaskFeedbackMapper;
import com.bt.om.mapper.AdMonitorTaskMapper;
import com.bt.om.mapper.AdMonitorUserTaskMapper;
import com.bt.om.mapper.AdPointMapper;
import com.bt.om.mapper.AdSeatInfoMapper;
import com.bt.om.mapper.AdSystemPushMapper;
import com.bt.om.mapper.AdUserMessageMapper;
import com.bt.om.mapper.AdUserMoneyMapper;
import com.bt.om.mapper.AdUserPointMapper;
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
 * 监测任务相关事务层
 */
@Service
public class AdMonitorTaskService implements IAdMonitorTaskService {
    @Autowired
    private AdMonitorTaskMapper adMonitorTaskMapper;
    @Autowired
    private AdMonitorTaskFeedbackMapper adMonitorTaskFeedbackMapper;
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
	@Autowired
	private AdUserPointMapper adUserPointMapper;
	@Autowired
	private AdMediaMapper adMediaMapper;
	@Autowired
	private AdPointMapper adPointMapper;
	@Autowired
	private AdUserMoneyMapper adUserMoneyMapper;
    @Autowired
    private AdSystemPushMapper adSystemPushMapper;
    
	/**
	 * 分页查询监测任务信息
	 */
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

	/**
	 * 任务指派给APP人员
	 */
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
            } else {
            	//任务状态不是待指派，不是可抢单，不是待执行
            	if(task.getStatus() != MonitorTaskStatus.UNASSIGN.getId() 
            			&& task.getStatus() != MonitorTaskStatus.TO_CARRY_OUT.getId()
            			&& task.getStatus() != MonitorTaskStatus.CAN_GRAB.getId()) {
            		continue;
            	}
            }
            
            //查询当前任务对应的广告位信息
            AdActivityAdseat adActivityAdseat = adActivityAdseatMapper.selectByPrimaryKey(task.getActivityAdseatId());
            AdSeatInfo adSeatInfo = null;
            if(adActivityAdseat != null) {
            	adSeatInfo = adSeatInfoMapper.selectByPrimaryKey(adActivityAdseat.getAdSeatId());
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
            userTask.setAssignType(AssignTypeEnum.ASSIGN.getId());
            userTask.setStatus(1);
            userTask.setCreateTime(now);
            userTask.setUpdateTime(now);
            task.setAssignorId(loginUser.getId());
            task.setAssignorTime(now);
            task.setStatus(MonitorTaskStatus.TO_CARRY_OUT.getId());//变为待执行
            task.setUserId(userId);//执行人员id
            adMonitorTaskMapper.updateByPrimaryKey(task);
            adMonitorUserTaskMapper.insertSelective(userTask);
            
            //指派成功修改站内信
	        String taskType = null;
	        if(task.getTaskType()==MonitorTaskType.UP_MONITOR.getId()) {
	        	taskType = "上刊监测";
	        }else if(task.getTaskType()==MonitorTaskType.DURATION_MONITOR.getId()) {
	        	taskType = "投放期间监测";
	        }else if(task.getTaskType()==MonitorTaskType.DOWNMONITOR.getId()) {
	        	taskType = "下刊监测";
	        }else if(task.getTaskType()==MonitorTaskType.UP_TASK.getId()) {
	        	taskType = "上刊";
	        }else if(task.getTaskType()==MonitorTaskType.ZHUIJIA_MONITOR.getId()) {
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
            if(adSeatInfo != null) {
            	stringBuffer.append(adSeatInfo.getName());
                stringBuffer.append("】广告位的【");
            }
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
            searchMap.put("updateTime", now); //修改时间
            adUserMessageMapper.updateUserMessage(searchMap);
        }
    }

    /**
     * 更新监测任务
     */
    @Override
    public void update(AdMonitorTask task) {
        task.setUpdateTime(new Date());
        adMonitorTaskMapper.updateByPrimaryKeySelective(task);
    }

    /**
     * 任务审核通过
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void pass(String[] taskIds, Integer assessorId, Integer status) {
    	Date now = new Date();
    	SysUser auditPerson = sysUserMapper.selectByPrimaryKey(assessorId); //获取审核人的相关信息
    	
//    	AdPoint point5= adPointMapper.selectByPointType(5);//上刊监测
//    	AdPoint point6= adPointMapper.selectByPointType(6);//期间监测
//    	AdPoint point7= adPointMapper.selectByPointType(7);//下刊监测
//    	AdPoint point8= adPointMapper.selectByPointType(8);//追加监测
    	
    	//[4] 确认操作在业务层方法里进行循环
    	for (String taskId : taskIds) {
    		Integer id = Integer.parseInt(taskId);
	        AdMonitorTask task = new AdMonitorTask();
	        task.setId(id);
	        task.setFirstVerify(VerifyType.PASS_TYPE.getId());//1：初审通过
	        //当登录用户不是第三方监测公司时 初步审核和审核状态都改为通过
	        if(auditPerson.getUsertype()!=UserTypeEnum.THIRD_COMPANY.getId()) {
	        	task.setStatus(status);
	        }
	        task.setAssessorId(assessorId);
	        
	        AdMonitorTaskFeedback feedback = null;
	        //如果监测反馈有问题，问题状态置为有问题，否则无问题
	        List<AdMonitorTaskFeedback> feedbacks = adMonitorTaskFeedbackMapper.selectByTaskId(task.getId(), 1);
	        //任务审核通过 feedback的四张图片的状态改为通过 status=1 
	        for(AdMonitorTaskFeedback feedbackTask : feedbacks) {
	        	feedbackTask.setPicUrl1Status(VerifyType.PASS_TYPE.getId());
	        	feedbackTask.setPicUrl2Status(VerifyType.PASS_TYPE.getId());
	        	feedbackTask.setPicUrl3Status(VerifyType.PASS_TYPE.getId());
	        	feedbackTask.setPicUrl4Status(VerifyType.PASS_TYPE.getId());
	        	adMonitorTaskFeedbackMapper.updateByPrimaryKey(feedbackTask);
	        }
	        
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
	        
	        //完成任务且审核通过给用户添加积分
	        AdUserPoint adUserPoint = new AdUserPoint();
	        adUserPoint.setUserId(task.getUserId());
	        adUserPoint.setCreateTime(now);
	        adUserPoint.setUpdateTime(now);
	        
	        //完成任务且审核通过给用户添加金额
	        AdUserMoney adUserMoney  = new AdUserMoney();
	        adUserMoney.setUserId(task.getUserId());
	        adUserMoney.setCreateTime(now);
	        adUserMoney.setUpdateTime(now);
	        if(task.getTaskType()==1){
	        	adUserPoint.setPoint(task.getTaskPoint()); 
	        	adUserPoint.setResult("恭喜您完成上刊监测任务！");
	        	adUserMoney.setMoney(task.getTaskMoney());
	        	adUserMoney.setResult("完成上刊监测任务！");
	        }else if(task.getTaskType()==2){
	        	adUserPoint.setPoint(task.getTaskPoint()); 
	        	adUserPoint.setResult("恭喜您完成投放期间监测任务！");
	        	adUserMoney.setMoney(task.getTaskMoney());
	        	adUserMoney.setResult("完成投放期间监测任务！");
	        }else if(task.getTaskType()==3){
	        	adUserPoint.setPoint(task.getTaskPoint()); 
	        	adUserPoint.setResult("恭喜您完成下刊监测任务！");
	        	adUserMoney.setMoney(task.getTaskMoney());
	        	adUserMoney.setResult("完成下刊监测任务！");
	        }else if(task.getTaskType()==6){
	        	adUserPoint.setPoint(task.getTaskPoint());  
	        	adUserPoint.setResult("恭喜您完成追加监测任务！");
	        	adUserMoney.setMoney(task.getTaskMoney());
	        	adUserMoney.setResult("完成追加监测任务！");
	        }
	        adUserPointMapper.insertSelective(adUserPoint);
	        adUserMoneyMapper.insertSelective(adUserMoney);
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
        	//查询当前任务对应的广告位信息
            AdActivityAdseat adActivityAdseat = adActivityAdseatMapper.selectByPrimaryKey(task.getActivityAdseatId());
            AdSeatInfo adSeatInfo = null;
            if(adActivityAdseat != null) {
            	adSeatInfo = adSeatInfoMapper.selectByPrimaryKey(adActivityAdseat.getAdSeatId());
            }
            
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
            stringBuffer.append(taskType);
            stringBuffer.append("任务】已被【");
            stringBuffer.append(auditPerson.getRealname());
            //登录方是第三方监测公司时 初审通过
            if(auditPerson.getUsertype()==UserTypeEnum.THIRD_COMPANY.getId()) {
            	stringBuffer.append("】初步审核通过");
            }else {
            	//群邑方审核通过
            	stringBuffer.append("】审核通过");
            }
            
            searchMap.put("content", stringBuffer.toString());
            searchMap.put("isFinish", MessageIsFinish.UNCONFIRM.getId()); //1：已处理
            searchMap.put("targetId", id); //任务表的id
            searchMap.put("type", MessageType.TASK_AUDIT.getId()); //2,"任务审核"
            searchMap.put("updateTime", now); //修改时间
            adUserMessageMapper.updateUserMessage(searchMap);
	    }
    }
    
    /**
     * 任务审核不通过
     */
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
	        task.setFirstVerify(VerifyType.REJECT_TYPE.getId());//2：初审驳回
	        task.setStatus(status);
	        task.setAssessorId(assessorId);
	        adMonitorTaskMapper.updateByPrimaryKeySelective(task);
	        List<AdMonitorTaskFeedback> feedbacks = adMonitorTaskFeedbackMapper.selectByTaskId(task.getId(), 1);
	        for (AdMonitorTaskFeedback feedback : feedbacks) {
	            feedback.setReason(reason);
	            feedback.setUpdateTime(now);
	            feedback.setPicUrl1Status(VerifyType.REJECT_TYPE.getId());//status=2 图片被驳回
	            feedback.setPicUrl2Status(VerifyType.REJECT_TYPE.getId());
	            feedback.setPicUrl3Status(VerifyType.REJECT_TYPE.getId());
	            feedback.setPicUrl4Status(VerifyType.REJECT_TYPE.getId());
	            adMonitorTaskFeedbackMapper.updateByPrimaryKey(feedback);
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
        	//查询当前任务对应的广告位信息
            AdActivityAdseat adActivityAdseat = adActivityAdseatMapper.selectByPrimaryKey(task.getActivityAdseatId());
            AdSeatInfo adSeatInfo = null;
            if(adActivityAdseat != null) {
            	adSeatInfo = adSeatInfoMapper.selectByPrimaryKey(adActivityAdseat.getAdSeatId());
            }
            
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
            stringBuffer.append(taskType);
            stringBuffer.append("任务】已被【");
            stringBuffer.append(auditPerson.getRealname());
	        //登录用户是第三方监测公司时 初步审核不通过
	        if(auditPerson.getUsertype() == UserTypeEnum.THIRD_COMPANY.getId()) {
	        	stringBuffer.append("】初步审核不通过");
	        }else {
	        	//群邑方审核不通过
	        	stringBuffer.append("】审核不通过");
	        }
            searchMap.put("content", stringBuffer.toString());
            searchMap.put("isFinish", MessageIsFinish.UNCONFIRM.getId()); //1：已处理
            searchMap.put("targetId", id); //任务表的id
            searchMap.put("type", MessageType.TASK_AUDIT.getId()); //2,"任务审核"
            searchMap.put("updateTime", now); //修改时间
            adUserMessageMapper.updateUserMessage(searchMap);
    	}
    }

    /**
     * 通过任务执行人id查询监测任务
     */
    @Override
    public List<AdMonitorTaskMobileVo> getByUserIdForMobile(Integer userId) {
        return adMonitorTaskMapper.selectByUserId(userId);
    }

    /**
     * APP人员执行了任务
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void feedback(Integer taskId, AdMonitorTaskFeedback feedback,String adSeatCode,SysUserExecute user) {
        Date now = new Date();
        Integer auditTime = ConfigUtil.getInt("audit_time"); //允许任务审核天数
        
        //获取监测任务
        AdMonitorTask task = adMonitorTaskMapper.selectByPrimaryKey(taskId);

        //每次更新广告位经纬度
        if (feedback.getLat()!=null&feedback.getLon()!=null) {
        	AdActivityAdseatVo adseatVo = adActivityAdseatMapper.selectVoById(task.getActivityAdseatId());
            AdSeatInfo adSeatInfo = new AdSeatInfo();
            adSeatInfo.setId(adseatVo.getAdSeatId());
            adSeatInfo.setLat(feedback.getLat());
            adSeatInfo.setLon(feedback.getLon());
            adSeatInfoMapper.updateByPrimaryKeySelective(adSeatInfo);
		}
        
        //第一次做任务时上刊任务添加用户积分
        if(task.getStatus() == MonitorTaskStatus.TO_CARRY_OUT.getId() && task.getTaskType() == 5) {
//        	AdPoint adPoint = adPointMapper.selectByPointType(4);//上刊任务
        	AdUserPoint userPoint = new AdUserPoint();
        	AdUserMoney userMoney = new AdUserMoney();
        	userPoint.setPoint(task.getTaskPoint()); 
        	userPoint.setResult("恭喜您完成上刊任务！");
        	userPoint.setUserId(task.getUserId());
        	userPoint.setUpdateTime(now);
        	userPoint.setCreateTime(now);
        	userMoney.setMoney(task.getTaskMoney());
        	userMoney.setResult("完成上刊任务！");
        	userMoney.setUserId(task.getUserId());
        	userMoney.setCreateTime(now);
        	userMoney.setUpdateTime(now);
        	adUserPointMapper.insert(userPoint);
        	adUserMoneyMapper.insert(userMoney);
        }
        
        if(StringUtil.isNotBlank(feedback.getProblem()) || StringUtil.isNotBlank(feedback.getProblemOther())) {
        	task.setProblemStatus(TaskProblemStatus.PROBLEM.getId());
        } else {
        	task.setProblemStatus(TaskProblemStatus.NO_PROBLEM.getId());
        }
        
        //获取任务对应的广告位
        AdSeatInfo seatInfo = adSeatInfoMapper.getAdSeatInfoByAdActivitySeatId(task.getActivityAdseatId());
//        if(seatInfo.getLon() == null || seatInfo.getLat() == null) {
//        	seatInfo.setLon(feedback.getLon());
//        	seatInfo.setLat(feedback.getLat());
//        	adSeatInfoMapper.updateByPrimaryKeySelective(seatInfo);
//        }
        
        if (task.getStatus() == MonitorTaskStatus.TO_CARRY_OUT.getId()) {
        	//如果监测任务当前处于"待执行", 插入feedback
            feedback.setCreateTime(now);
            feedback.setUpdateTime(now);
            feedback.setMonitorTaskId(taskId);
           
            if(task.getTaskType() == MonitorTaskType.UP_TASK.getId()) {
            	//上刊任务, 不校验, 直接审核通过
            	task.setStatus(MonitorTaskStatus.VERIFIED.getId());
            	//将反馈的四张图片的状态设为审核通过
            	feedback.setPicUrl1Status(VerifyType.PASS_TYPE.getId());
            	feedback.setPicUrl2Status(VerifyType.PASS_TYPE.getId());
            	feedback.setPicUrl3Status(VerifyType.PASS_TYPE.getId());
            	feedback.setPicUrl4Status(VerifyType.PASS_TYPE.getId());
            } else {
            	//非上刊任务做校验
            	//如果本次提交的照片不足，任务状态设置为"未完成"，否则进入待审核
            	if (feedback.getPicUrl1() == null || feedback.getPicUrl2() == null || feedback.getPicUrl3() == null || feedback.getPicUrl4() == null) {
                    task.setStatus(MonitorTaskStatus.UN_FINISHED.getId());
                } else {
                    task.setStatus(MonitorTaskStatus.UNVERIFY.getId());
                }
            }
            adMonitorTaskFeedbackMapper.insertSelective(feedback);
        } else if (task.getStatus() == MonitorTaskStatus.VERIFY_FAILURE.getId()) {
        	//如果监测任务当前处于"审核不通过", 更新feedback
        	//获取之前提交的feedback，更新feedback,流程正常情况下只有一条
            List<AdMonitorTaskFeedback> old_feed = adMonitorTaskFeedbackMapper.selectByTaskId(taskId, 1);
            for (AdMonitorTaskFeedback old : old_feed) {
                if(feedback.getPicUrl1() != null) {
                	old.setPicUrl1(feedback.getPicUrl1());
                }
                if(feedback.getPicUrl2() != null) {
                	old.setPicUrl2(feedback.getPicUrl2());
                }
                if(feedback.getPicUrl3() != null) {
                	old.setPicUrl3(feedback.getPicUrl3());
                }
                if(feedback.getPicUrl4() != null) {
                	old.setPicUrl4(feedback.getPicUrl4());
                }
                old.setProblem(feedback.getProblem());
                old.setProblemOther(feedback.getProblemOther());
                old.setUpdateTime(now); 
                adMonitorTaskFeedbackMapper.updateByPrimaryKeySelective(old);
            }
            
            task.setStatus(MonitorTaskStatus.UNVERIFY.getId());
        } else if (task.getStatus() == MonitorTaskStatus.UN_FINISHED.getId()) {
            //如果监测任务当前处于"未完成", 更新feedback
            List<AdMonitorTaskFeedback> old_feed = adMonitorTaskFeedbackMapper.selectByTaskId(taskId, 1);
            AdMonitorTaskFeedback old = old_feed.get(old_feed.size() - 1);

            //用本地feedback根据提供字段按需更新
//            feedback.setId(old.getId());
//            feedback.setUpdateTime(now);
//            feedback.setMonitorTaskId(taskId);
            
            old.setUpdateTime(now);
            if(StringUtil.isNotBlank(feedback.getPicUrl1())) {
            	old.setPicUrl1(feedback.getPicUrl1());
            }
            if(StringUtil.isNotBlank(feedback.getPicUrl2())) {
            	old.setPicUrl2(feedback.getPicUrl2());
            }
            if(StringUtil.isNotBlank(feedback.getPicUrl3())) {
            	old.setPicUrl3(feedback.getPicUrl3());
            }
            if(StringUtil.isNotBlank(feedback.getPicUrl4())) {
            	old.setPicUrl4(feedback.getPicUrl4());
            }
            adMonitorTaskFeedbackMapper.updateByPrimaryKeySelective(old);
            //如果本次提交的照片+以前提交的照片仍不足，任务状态还是设置为"未完成"，否则进入待审核
            if (old.getPicUrl1() == null|| old.getPicUrl2() == null || old.getPicUrl3() == null || old.getPicUrl4() == null) {
                task.setStatus(MonitorTaskStatus.UN_FINISHED.getId());
            } else {
                task.setStatus(MonitorTaskStatus.UNVERIFY.getId());
            }
        }
        
        //更新任务主表
        task.setUpdateTime(now);
        adMonitorTaskMapper.updateByPrimaryKeySelective(task);
        
        //如果任务到了待审核的状态【普通监测任务】/ 审核通过的状态【上刊任务】, 则需要插入站内信
        if(task.getStatus() == MonitorTaskStatus.UNVERIFY.getId() || task.getStatus() == MonitorTaskStatus.VERIFIED.getId()) {
        	List<Integer> list = new ArrayList<>();
	        list = sysUserMapper.getUserId(UserTypeEnum.SUPER_ADMIN.getId());//4：超级管理员
	        Integer dep_id = sysResourcesMapper.getUserId(DepartmentTypeEnum.MONITOR_TASK.getId());//2：任务审核、指派部门
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
	        userIdList.addAll(list);
	        userIdList.addAll(cuslist);
	        list.removeAll(list);
	        list = sysUserMapper.getUserId(UserTypeEnum.PHONE_OPERATOR.getId());//6:呼叫中心人员
	        userIdList.addAll(list);
	        userIdList.add(dep_id);
	        if(user.getUsertype().equals(SysUserExecuteType.THIRDCOMPANY.getId())) {
	        	//执行人员是第三方监测公司的员工  第三方监测公司 收到站内信
	        	userIdList.add(user.getOperateId());
	        }
	        
	        String taskType = null;
	        if(task.getTaskType().equals(MonitorTaskType.UP_MONITOR.getId())) {
	        	taskType = "上刊监测";
	        }else if(task.getTaskType().equals(MonitorTaskType.DURATION_MONITOR.getId())) {
	        	taskType = "投放期间监测";
	        }else if(task.getTaskType().equals(MonitorTaskType.DOWNMONITOR.getId())) {
	        	taskType = "下刊监测";
	        }else if(task.getTaskType().equals(MonitorTaskType.UP_TASK.getId())) {
	        	taskType = "上刊";
	        }else if(task.getTaskType().equals(MonitorTaskType.ZHUIJIA_MONITOR.getId())) {
	        	taskType = "追加监测";
	        }
	        
	        StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("【");
            stringBuffer.append(sysUser.getRealname());
            stringBuffer.append("】广告主的【");
            stringBuffer.append(adActivity.getActivityName());
            stringBuffer.append("】活动的【");
            if(seatInfo != null) {
            	stringBuffer.append(seatInfo.getName());
                stringBuffer.append("】广告位的【");
            }
            stringBuffer.append(taskType);
            stringBuffer.append("任务】已被【");
            stringBuffer.append(user.getRealname());
            stringBuffer.append("】执行");
            if(task.getStatus() == MonitorTaskStatus.UNVERIFY.getId()) {
            	stringBuffer.append("，待审核");
            } else {
            	stringBuffer.append("，有");
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

    /**
     * 创建有问题的监测任务的子任务
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createSubTask(Integer taskId) {
    	Integer monitorTime = ConfigUtil.getInt("monitor_time"); //允许任务执行天数
        Date now = new Date();
        //查询父监测任务信息
        AdMonitorTask task = adMonitorTaskMapper.selectByPrimaryKey(taskId);
        //创建子任务
        AdMonitorTask sub = new AdMonitorTask();
        sub.setTaskType(MonitorTaskType.FIX_CONFIRM.getId());
        sub.setActivityAdseatId(task.getActivityAdseatId());
        sub.setStatus(MonitorTaskStatus.UNASSIGN.getId()); //待指派的任务
        sub.setProblemStatus(TaskProblemStatus.UNMONITOR.getId());
        sub.setActivityId(task.getActivityId());
        sub.setParentId(task.getId());
        sub.setParentType(RewardTaskType.MONITOR.getId());
        sub.setSubCreated(2);
        sub.setCreateTime(now);
        sub.setUpdateTime(now);
        sub.setMonitorDate(now);
        sub.setMonitorLastDays(monitorTime);
        adMonitorTaskMapper.insertSelective(sub);
        //更新父任务
        task.setSubCreated(1);
        task.setUpdateTime(now);
        adMonitorTaskMapper.updateByPrimaryKeySelective(task);
    }

    /**
     * 获取监测任务的详细信息
     */
    @Override
    public AdMonitorTaskVo getTaskDetails(String taskId) {
        int taskIds = Integer.valueOf(taskId);
        return adMonitorTaskMapper.getTaskDetails(taskIds);
    }

    /**
     * 获取一组监测任务的详细信息
     */
    @Override
    public List<AdMonitorTaskVo> getSubmitDetails(String taskId) {
        int taskIds = Integer.valueOf(taskId);
        List<AdMonitorTaskVo> list = adMonitorTaskMapper.getSubmitDetails(taskIds);
        return list;
    }

    /**
     * 通过任务id查询任务信息
     */
    @Override
    public AdMonitorTaskVo getTaskVoById(Integer id) {
        return adMonitorTaskMapper.selectVoByPrimaryKey(id);
    }

    /**
     * 通过经纬度查询附近的活动-广告位-监测任务信息
     */
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

    /**
     * 通过本市的活动-广告位-监测任务信息
     */
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

    /**
     * 社会人员/媒体监测人员 抢单
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean grabTask(Integer userId, Integer id) {
        Date now = new Date();
        boolean ret = false;
        //先查询该任务
        AdMonitorTask task = adMonitorTaskMapper.selectByPrimaryKey(id);
        //任务未查到 或 任务执行人已指派，抢任务失败
        if(task==null||task.getUserId()!=null&&task.getStatus()!=1){
            return ret;
        }
        int count = adMonitorTaskMapper.grabTask(userId,id,task.getUpdateTime());
        if(count>0){
        	//查询用户类型
        	SysUserExecute sysUserExecute = sysUserExecuteMapper.selectByPrimaryKey(userId);
        	
            //添加用户和任务关联关系
            AdMonitorUserTask userTask = new AdMonitorUserTask();
            userTask.setUserId(userId); //任务执行人id
            userTask.setMonitorTaskId(id); //任务主表id
            userTask.setStartTime(now); //任务开始时间
            userTask.setEndTime(Date.from(now.toInstant().atZone(ZoneId.systemDefault()).plusHours(12).toInstant())); //任务结束时间 = 任务开始时间 + 12小时
            if(sysUserExecute.getUsertype().equals(4)) {
            	//[1] 社会人员抢单
            	userTask.setAssignType(2); //2.自助抢单（社会人员）
            } else if(sysUserExecute.getUsertype().equals(3)||sysUserExecute.getUsertype().equals(5)) {
            	//[2] 媒体监测人员抢单
            	userTask.setAssignType(3); //3.自助接取（媒体监测人员）
            }
            userTask.setStatus(1);
            userTask.setCreateTime(now);
            userTask.setUpdateTime(now);
            adMonitorUserTaskMapper.insertSelective(userTask);
            
            ret = true;
        }
        
        return ret;
    }

    /**
     * 获取全部的监测任务
     */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public List<AdMonitorTask> selectAllTask() {
		List<AdMonitorTask> taskList = adMonitorTaskMapper.findAllTask();		
		return taskList;
	}

	/**
	 * 分页查询监测任务
	 */
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

	/**
	 * 获取后台审核admin捞取过的任务信息, 功能已废弃
	 */
	@Override
	public List<AdMonitorTaskVo> selectAllByAssessorId(Map<String, Object> searchMap) {
		return adMonitorTaskMapper.selectAllByAssessorId(searchMap);
	}
	
	/**
	 * 后台审核admin捞取任务, 功能已废弃
	 */
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
	
	/**
	 * 后台指派admin捞取任务, 功能已废弃
	 */
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

	/**
	 * 查询某一活动最新的监测任务
	 */
	@Override
	public List<AdMonitorTask> selectLatestMonitorTaskIds(Integer activityId) {
		return adMonitorTaskMapper.selectLatestMonitorTaskIds(activityId);
	}
    
	/**
	 * 通过某些任务主表id查询对应的反馈信息有效的反馈信息
	 */
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
		List<TaskAdSeat> taskAdSeats = adMonitorTaskMapper.getWaitToActivateIds(nowDate);
		adMonitorTaskMapper.newActivateMonitorTask(nowDate);
		
		//[2] 添加站内信
		List<Integer> userIds = new ArrayList<>();
		Date now = new Date();
		
		if(taskAdSeats != null && taskAdSeats.size() > 0) {
			List<AdUserMessage> message = new ArrayList<>();
			
			//查询添加站内信的用户id集合
			userIds = sysUserMapper.getUserId(4);//超级管理员id
			Integer dep_id = sysResourcesMapper.getUserId(2);//2：任务审核、指派部门
			userIds.add(dep_id);
			
			for (TaskAdSeat taskAdSeat : taskAdSeats) {
				//员工
				AdMonitorTask adMonitorTask = adMonitorTaskMapper.selectByPrimaryKey(taskAdSeat.getId());
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
		        userIds.removeAll(userIds);
		        userIds = sysUserMapper.getUserId(UserTypeEnum.PHONE_OPERATOR.getId());//6:呼叫中心人员
		        userIdList.addAll(userIds);
		        userIds.removeAll(userIds);
		        userIds = sysUserMapper.getUserId(UserTypeEnum.THIRD_COMPANY.getId());//7:第三方监测公司
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
	            stringBuffer.append(taskAdSeat.getAdSeatName() + "】广告位的【");
	            stringBuffer.append(taskType);
	            stringBuffer.append("任务】待指派");
	            
		        for(Integer i: userIdList) {
	            	AdUserMessage mess = new AdUserMessage();
	            	mess.setContent(stringBuffer.toString());
	            	mess.setTargetId(taskAdSeat.getId()); //任务表的id
	            	mess.setTargetUserId(i);
	            	mess.setIsFinish(MessageIsFinish.CONFIRMED.getId()); //0：未处理
	            	mess.setType(MessageType.TASK_ASSIGN.getId()); //3：任务指派
	            	mess.setCreateTime(now);
	            	mess.setUpdateTime(now);
	            	message.add(mess);
	            }
			}
			
			if(message != null && message.size() > 0) {
	        	adUserMessageMapper.insertMessage(message);
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

	/**
	 * 社会人员抢单的任务进行回收操作
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void recycleMonitorTask() {
		Date now = new Date();
		
		//[1] 查询ad_monitor_user_task待回收的任务id集合
		Map<String, Object> searchMap = new HashMap<>();
		searchMap.put("nowDate", now);
		searchMap.put("assignType", 2); //2.自主抢单（社会人员）
		List<Integer> monitorTaskIds = adMonitorUserTaskMapper.selectRecycleTaskIds(searchMap);
		
		//[2] 修改ad_monitor_user_task表中待回收的状态及回收时间
		if(CollectionUtil.isNotEmpty(monitorTaskIds)) {
			AdMonitorUserTask task = new AdMonitorUserTask();
			task.setAbandonTime(now);
			task.setUpdateTime(now);
			task.setStatus(3); //3.超时回收
			task.setAssignType(2); //2.自主抢单（社会人员）
			adMonitorUserTaskMapper.recycleUserTask(task);
			
			//[3] 修改ad_monitor_task表中回收的任务的状态为 1：待指派 或 8：可抢单
			adMonitorTaskMapper.recycleTask(monitorTaskIds, 12);
		}
	}
	
	/**
	 * 媒体监测人员抢单的任务进行回收操作
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void recycleMediaMonitorTask() {
		Date now = new Date();
		
		//[1] 查询ad_monitor_user_task待回收的任务id集合
		Map<String, Object> searchMap = new HashMap<>();
		searchMap.put("nowDate", now);
		searchMap.put("assignType", 3); //3.自主接取（媒体监测人员）
		List<Integer> monitorTaskIds = adMonitorUserTaskMapper.selectRecycleTaskIds(searchMap);
		
		//[2] 修改ad_monitor_user_task表中待回收的状态及回收时间
		if(CollectionUtil.isNotEmpty(monitorTaskIds)) {
			AdMonitorUserTask task = new AdMonitorUserTask();
			task.setAbandonTime(now);
			task.setUpdateTime(now);
			task.setStatus(3); //3.超时回收
			task.setAssignType(3); //3.自主接取（媒体监测人员）
			adMonitorUserTaskMapper.recycleUserTask(task);
			
			//[3] 修改ad_monitor_task表中回收的任务的状态为 1：待指派
			searchMap.clear();
			searchMap.put("updateTime", now);
			searchMap.put("status", 1); //1：待指派
			searchMap.put("monitorTaskIds", monitorTaskIds);
			adMonitorTaskMapper.recycleMediaTask(searchMap);
		}
	}
	
	/**
	 * 获取即将结束的任务(2小时之前)进行极光消息推送
	 */
	@Override
	public String getTaskWillEnd(Integer duration) {
		List<AdMonitorUserTask> adMonitorUserTasks = adMonitorUserTaskMapper.getTaskWillEnd(duration);
		Date now = new Date();
		List<Integer> ids = new ArrayList<>();
		List<Integer> userIds = new ArrayList<>();
		//==========任务即将结束之后根据接取任务的用户id进行app消息推送==============
		//用set存储避免一个用户创建多个活动，进行多次推送 
        Set<String> aliases = new HashSet<String>();
        for(AdMonitorUserTask adMonitorUserTask : adMonitorUserTasks) {
        	aliases.add(String.valueOf(adMonitorUserTask.getUserId())); //把userId列表转成String类型，极光推送api需要
        	ids.add(adMonitorUserTask.getId());
        	userIds.add(adMonitorUserTask.getUserId());//获取所有推送的用户Id
        }
        Map<String, Object> param = new HashMap<>();
        Map<String, String> extras = new HashMap<>();
        extras.put("type", "task_will_end_push");
        param.put("msg", "您有任务即将结束，请尽快执行！");
        param.put("title", "玖凤平台");
        param.put("alias", aliases);  //根据别名选择推送用户（这里userId用作推送时的用户别名）
        param.put("extras", extras);
        String pushResult = JPushUtils.pushAllByAlias(param);
        System.out.println("pushResult:: " + pushResult);
        //消息推送之后将任务is_push状态更新为1（已推送）
        if(ids != null && ids.size() > 0) {
        	adMonitorUserTaskMapper.updateIsPush(ids);
        	
        	for(Integer id : userIds) {
        		PushInfoVo info = adMonitorUserTaskMapper.getInfoById(id);
        		AdSystemPush push = new AdSystemPush();
        		push.setUserId(id);
        		push.setActivityName(info.getActivityName());
        		push.setTitle("任务即将结束");
        		push.setContent("【"+info.getProvince()+info.getCity()+info.getRoad()+info.getLocation()+"】广告位的【"+info.getActivityName()+"】活动的任务即将结束");
        		push.setCreateTime(now);
        		adSystemPushMapper.insertSelective(push);
        	}
        }
		return pushResult;
	}

	/**
	 * 强制指派
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void forceAssignTask() {
		adMonitorTaskMapper.forceAssignTask(12);
	}

	/**
	 * 通过任务id查询任务信息
	 */
	@Override
	public AdMonitorTask selectByPrimaryKey(Integer id) {
		return adMonitorTaskMapper.selectByPrimaryKey(id);
	}

	/**
	 * APP用户主动放弃任务
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void abandonUserTask(AbandonTaskVo vo) {
		adMonitorUserTaskMapper.abandonUserTask(vo);
	}

	/**
	 * 后台审核admin撤销任务, 功能已废弃
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void offAdMonitorTaskByAssessorId(Integer id) {
		adMonitorTaskMapper.cancelAdMonitorTaskByAssessorId(id);	
	}

	/**
	 * 后台指派admin撤销任务, 功能已废弃
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void offAdMonitorTaskByAssignorId(Integer id) {
		adMonitorTaskMapper.cancelAdMonitorTaskByAssignorId(id);	
	}

	/**
	 * 获取某一任务执行人所有待审核的任务
	 */
	@Override
	public List<AdMonitorTask> getAllByStatusUnCheck(Map<String, Object> searchMap) {
		return adMonitorTaskMapper.getAllByStatusUnCheck(searchMap);
	}

	/**
	 * 获取某一任务执行人所有待指派的任务
	 */
	@Override
	public List<AdMonitorTask> getAllByStatusUnZhipai(Map<String, Object> searchMap) {
		return adMonitorTaskMapper.getAllByStatusUnZhipai(searchMap);
	}
	
	/**
	 * 查询每条监测任务中最新的一条反馈信息
	 */
	@Override
	public List<PictureVo> selectFeedBackByActivityIdAndSeatId(Map<String, Object> searchMap) {
		return adMonitorTaskMapper.selectFeedBackByActivityIdAndSeatId(searchMap);
	}

	/**
	 * 分页查询任务信息
	 */
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
	public void insertMonitorTask(Integer activityId, List<String> seatIds, String reportTime, Integer zhuijiaMonitorTaskPoint, double zhuijiaMonitorTaskMoney) {
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
 			adMonitorTask.setTaskPoint(zhuijiaMonitorTaskPoint);
 			adMonitorTask.setTaskMoney(zhuijiaMonitorTaskMoney);
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

	/**
	 * 通过任务id查询任务信息
	 */
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
	
	/**
	 * 查询主任务下存在的反馈数量
	 */
	@Override
	public Integer selectCountByMonitorTaskId(int monitorTaskId) {
		return adMonitorTaskFeedbackMapper.selectCountByMonitorTaskId(monitorTaskId);
	}
	
	/**
	 * 查询该任务对应的广告位的经纬度信息
	 */
	@Override
	public AdSeatInfo selectLonLatByMonitorTaskId(int monitorTaskId) {
		return adMonitorTaskMapper.selectLonLatByMonitorTaskId(monitorTaskId);
	}

	/**
	 * 通过活动id查询出所有的任务信息
	 */
	@Override
	public List<AdMonitorTask> getAllTasksByActivityId(Integer activityId) {
		return adMonitorTaskMapper.getAllTasksByActivityId(activityId);
	}

	/**
	 * 查询某一活动某种任务类型某一报告时间的最新的监测任务
	 */
	@Override
	public List<AdMonitorTask> newSelectLatestMonitorTaskIds(Map<String, Object> searchMap) {
		return adMonitorTaskMapper.newSelectLatestMonitorTaskIds(searchMap);
	}
	
	/**
	 * 通过任务id集合去查询所有的APP执行人员手机号
	 */
	@Override
	public List<String> selectUserNameByTaskIdBatch(Map<String, Object> searchMap) {
		return adMonitorTaskMapper.selectUserNameByTaskIdBatch(searchMap);
	}

	/**
	 * 任务主表超时 将状态改成"已超时"
	 * */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void changeStatus() {
		Date now = new Date();
		
		//查询所有超时任务 10
		List<AdMonitorTask> list = adMonitorTaskMapper.selectOverTimeTasks(now);
		// [1] 将所有ad_monitor_task超时的任务状态设为10 "已超时
		boolean result = adMonitorTaskMapper.updateTaskStatus(now);
		
		if(result!=false) {
			// [2] 修改站内信
			for(AdMonitorTask task : list) {
				//获取任务对应的广告位
		        AdSeatInfo seatInfo = adSeatInfoMapper.getAdSeatInfoByAdActivitySeatId(task.getActivityAdseatId());
	        	AdActivity adActivity = adActivityMapper.selectByPrimaryKey(task.getActivityId());//通过id找到广告商id
	        	SysUser sysUser = sysUserMapper.selectByPrimaryKey(adActivity.getUserId());//获得广告商名
		
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
		        
	            //任务审核超时发送站内信
	            //修改站内信
	            Map<String, Object> map = new HashMap<>();
	            StringBuffer stringBuffer = new StringBuffer();
	            stringBuffer.append("【");
	            stringBuffer.append(sysUser.getRealname());
	            stringBuffer.append("】广告主的【");
	            stringBuffer.append(adActivity.getActivityName());
	            stringBuffer.append("】活动的【");
	            if(seatInfo != null) {
	            	stringBuffer.append(seatInfo.getName());
	                stringBuffer.append("】广告位的【");
	            }
	            stringBuffer.append(taskType);
	            stringBuffer.append("任务】已超时");
	            map.put("content", stringBuffer.toString());
	            map.put("isFinish", MessageIsFinish.CONFIRMED.getId()); //0：未处理
	            map.put("targetId", task.getId()); //任务表的id
	            map.put("type", MessageType.TASK_AUDIT.getId()); //2 任务审核 
	            map.put("updateTime", now); //修改时间
	            adUserMessageMapper.updateUserMessage(map);
	            map.put("type", MessageType.TASK_ASSIGN.getId());//3 任务指派
	            adUserMessageMapper.updateUserMessage(map);
			}
		}
	}

	/**
	 * 通过反馈id查询对应的任务主表信息
	 */
	@Override
	public AdMonitorTask geAdMonitorTaskByFeedbackId(Integer adMonitorTaskFeedbackId) {
		AdMonitorTaskFeedback taskFeedback = adMonitorTaskFeedbackMapper.selectByPrimaryKey(adMonitorTaskFeedbackId);
		AdMonitorTask adMonitorTask = adMonitorTaskMapper.selectByPrimaryKey(taskFeedback.getMonitorTaskId());
		return adMonitorTask;
	}

	@Override
	public Integer updatePicStatus(AdMonitorTaskFeedback feedback,Integer status) {
		adMonitorTaskFeedbackMapper.updateByPrimaryKeySelective(feedback);
		AdMonitorTaskFeedback feedback1 = adMonitorTaskFeedbackMapper.selectByPrimaryKey(feedback.getId());
		AdMonitorTask task = adMonitorTaskMapper.selectByPrimaryKey(feedback1.getMonitorTaskId());//查询出当前任务的所有信息
		
		AdMonitorTask adMonitorTask = new AdMonitorTask();
		//如果status=1   查看是否存在图片为空
		if(status == 1) {
			if(feedback1.getPicUrl1Status()==null || feedback1.getPicUrl2Status()==null || feedback1.getPicUrl3Status()==null || feedback1.getPicUrl4Status()==null) {
				//存在图片为空  任务主表设为审核中 
				adMonitorTask.setId(feedback1.getMonitorTaskId());
				adMonitorTask.setStatus(3);//3 审核中
				adMonitorTaskMapper.updateByPrimaryKeySelective(adMonitorTask);
			}else if(feedback1.getPicUrl1Status()==VerifyType.PASS_TYPE.getId()  && feedback1.getPicUrl2Status()==VerifyType.PASS_TYPE.getId() && feedback1.getPicUrl3Status()==VerifyType.PASS_TYPE.getId() && feedback1.getPicUrl4Status()==VerifyType.PASS_TYPE.getId()) {
				//都审核通过  任务主表设为通过
				adMonitorTask.setId(feedback1.getMonitorTaskId());
				adMonitorTask.setStatus(4);//4 审核通过
				adMonitorTaskMapper.updateByPrimaryKeySelective(adMonitorTask);
			}else {
				//存在审核不通过  任务主表设为未通过
				adMonitorTask.setId(feedback1.getMonitorTaskId());
				adMonitorTask.setStatus(5);//5 审核未通过
				adMonitorTaskMapper.updateByPrimaryKeySelective(adMonitorTask);
			}
		}else {
			//status=2 查看是否存在图片为空
			if(feedback1.getPicUrl1Status()==null || feedback1.getPicUrl2Status()==null || feedback1.getPicUrl3Status()==null || feedback1.getPicUrl4Status()==null ) {
				//存在图片为空  任务主表设为审核中 
				adMonitorTask.setId(feedback1.getMonitorTaskId());
				adMonitorTask.setStatus(3);//3 审核中
				adMonitorTaskMapper.updateByPrimaryKeySelective(adMonitorTask);
			}else if(feedback1.getPicUrl1Status()==VerifyType.REJECT_TYPE.getId() || feedback1.getPicUrl2Status()==VerifyType.REJECT_TYPE.getId() || feedback1.getPicUrl3Status()==VerifyType.REJECT_TYPE.getId() || feedback1.getPicUrl4Status()==VerifyType.REJECT_TYPE.getId() ) {
				//有一张图片审核不通过  任务主表设为未通过
				adMonitorTask.setId(feedback1.getMonitorTaskId());
				adMonitorTask.setStatus(5);//5 审核未通过
				adMonitorTaskMapper.updateByPrimaryKeySelective(adMonitorTask);
			}
		}
		
		adMonitorTask = adMonitorTaskMapper.selectByPrimaryKey(feedback1.getMonitorTaskId());//获得更新后的任务信息
		if(feedback1.getPicUrl1Status()!=null && feedback1.getPicUrl2Status()!=null && feedback1.getPicUrl3Status()!=null && feedback1.getPicUrl4Status()!=null ) {
			if(task.getStatus()!=MonitorTaskStatus.VERIFY_FAILURE.getId() && adMonitorTask.getStatus()==MonitorTaskStatus.VERIFY_FAILURE.getId()) {
				//上次不是审核不通过 这次是审核不通过 ，返回任务主表id 
				return feedback1.getMonitorTaskId();
			}
		}
		return null;
	}

	@Override
	public String selectUserNameByTaskId(Integer monitorTaskId) {
		return adMonitorTaskMapper.selectUserNameByTaskId(monitorTaskId);
	}

	@Override
	public void assign(String[] taskIds, Integer mediaId, Integer companyId, Integer mediaUser, Integer companyUser,
			Integer assignorId) {
		SysUser auditPerson = sysUserMapper.selectByPrimaryKey(assignorId); //获取指派人的相关信息
		SysUser loginUser = (SysUser)ShiroUtils.getSessionAttribute(SessionKey.SESSION_LOGIN_USER.toString());//当前用户
//    	SysUserExecute sysUserExecute = sysUserExecuteMapper.selectByPrimaryKey(userId); //app执行人员的信息
		Integer company_id = null;
		
		if(mediaId!=null && loginUser.getUsertype().equals(UserTypeEnum.MEDIA.getId())) {
			//媒体方登录指派给本公司下的员工
			company_id = mediaId;
		}else if(mediaId!=null && loginUser.getUsertype()!=UserTypeEnum.MEDIA.getId()) {
			//群邑方指派给媒体公司
			AdMedia media = adMediaMapper.selectByPrimaryKey(mediaId);
			company_id = media.getUserId();
		}
		else if(companyId != null) {
			//群邑方指派给第三方公司
			company_id = companyId;
		}else {
			//第三方监测公司指派本公司下的员工
			company_id = assignorId;
		}
		SysUser sysCompany = sysUserMapper.selectByPrimaryKey(company_id); //媒体公司/第三方监测公司信息
        Date now = new Date();
        //[4] 确认操作在业务层方法里进行循环
        for (String taskId : taskIds) {
            Integer id = Integer.valueOf(taskId);
            //先查询该任务
            AdMonitorTask task = adMonitorTaskMapper.selectByPrimaryKey(id);
            //任务未查到
            if(task == null){
                continue;
            } else {
            	//任务状态不是待指派，不是可抢单，不是待执行
            	if(task.getStatus() != MonitorTaskStatus.UNASSIGN.getId() 
            			&& task.getStatus() != MonitorTaskStatus.TO_CARRY_OUT.getId()
            			&& task.getStatus() != MonitorTaskStatus.CAN_GRAB.getId()) {
            		continue;
            	}
            }
            
            //查询当前任务对应的广告位信息
            AdActivityAdseat adActivityAdseat = adActivityAdseatMapper.selectByPrimaryKey(task.getActivityAdseatId());
            AdSeatInfo adSeatInfo = null;
            if(adActivityAdseat != null) {
            	adSeatInfo = adSeatInfoMapper.selectByPrimaryKey(adActivityAdseat.getAdSeatId());
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
            
            AdMonitorUserTask userTask = new AdMonitorUserTask();
            SysUserExecute sysUserExecute = null;
            //媒体公司人员获取任务
            if(mediaUser != null) {
            	adMonitorTaskMapper.grabTask(mediaUser,id,task.getUpdateTime()); //用户获取任务, 修改任务主表
            	userTask.setUserId(mediaUser);
            	task.setUserId(mediaUser);//执行人员id
            	sysUserExecute = sysUserExecuteMapper.selectByPrimaryKey(mediaUser); //app执行人员的信息
            }
            //第三方监测公司人员获取任务
            if(companyUser != null) {
            	adMonitorTaskMapper.grabTask(companyUser, id, task.getUpdateTime());
            	userTask.setUserId(companyUser);
            	task.setUserId(companyUser);//执行人员id
            	sysUserExecute = sysUserExecuteMapper.selectByPrimaryKey(companyUser); //app执行人员的信息
            }
            //指派给公司下的人员
            if(mediaUser !=null || companyUser != null) {
	            //添加用户和任务关联关系
	            userTask.setAssignUserId(loginUser.getId());
	            userTask.setMonitorTaskId(id);
	            userTask.setStartTime(now);
	            userTask.setEndTime(Date.from(task.getMonitorDate().toInstant().atZone(ZoneId.systemDefault()).plusDays(task.getMonitorLastDays()).minusSeconds(1).toInstant()));
	            userTask.setAssignType(AssignTypeEnum.ASSIGN.getId());
	            userTask.setStatus(1);
	            userTask.setCreateTime(now);
	            userTask.setUpdateTime(now);
	            task.setAssignorId(loginUser.getId());
	            task.setAssignorTime(now);
	            task.setCompanyId(company_id);
	            if( (!loginUser.getUsertype().equals(UserTypeEnum.MEDIA.getId())) && (!loginUser.getUsertype().equals(UserTypeEnum.THIRD_COMPANY.getId())) ) {
	            	task.setIsAssign(1);//登录方不是媒体主和第三方监测公司
	            }
	            task.setStatus(MonitorTaskStatus.TO_CARRY_OUT.getId());//变为待执行
	            adMonitorUserTaskMapper.insertSelective(userTask);
            }else {
            	//指派给公司
            	task.setCompanyId(company_id);
            	task.setStatus(MonitorTaskStatus.UNASSIGN.getId());//状态还是待指派
//            	task.setAssignorId(loginUser.getId());
//	            task.setAssignorTime(now);
            }
            adMonitorTaskMapper.updateByPrimaryKey(task);
            
            //指派成功修改站内信
	        String taskType = null;
	        if(task.getTaskType()==MonitorTaskType.UP_MONITOR.getId()) {
	        	taskType = "上刊监测";
	        }else if(task.getTaskType()==MonitorTaskType.DURATION_MONITOR.getId()) {
	        	taskType = "投放期间监测";
	        }else if(task.getTaskType()==MonitorTaskType.DOWNMONITOR.getId()) {
	        	taskType = "下刊监测";
	        }else if(task.getTaskType()==MonitorTaskType.UP_TASK.getId()) {
	        	taskType = "上刊";
	        }else if(task.getTaskType()==MonitorTaskType.ZHUIJIA_MONITOR.getId()) {
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
            if(adSeatInfo != null) {
            	stringBuffer.append(adSeatInfo.getName());
                stringBuffer.append("】广告位的【");
            }
            stringBuffer.append(taskType);
            stringBuffer.append("任务】已被【");
            stringBuffer.append(auditPerson.getRealname());
            stringBuffer.append("】指派给【");
            stringBuffer.append(sysCompany.getRealname());
            stringBuffer.append("】公司");
            //指派给公司 站内信中设置未处理
            searchMap.put("isFinish", MessageIsFinish.CONFIRMED.getId()); //0：未处理
            //指派给公司员工 若app人员没有填写真实姓名, 取其登录的用户名
            if(mediaUser != null || companyUser != null) {
                String realName = null;
            	stringBuffer.append("下的【");
            	if(StringUtil.isNotBlank(sysUserExecute.getRealname())) {
            		realName = sysUserExecute.getRealname();
            	} else {
                	realName = sysUserExecute.getUsername();
                }
            	stringBuffer.append(realName+"】员工");
            	//指派给员工 站内信设置已处理
            	searchMap.put("isFinish", MessageIsFinish.UNCONFIRM.getId()); //1：已处理
            }
            
            searchMap.put("content", stringBuffer.toString());
            searchMap.put("targetId", id); //任务表的id
            searchMap.put("type", MessageType.TASK_ASSIGN.getId()); //3,"任务指派"
            searchMap.put("updateTime", now); //修改时间
            adUserMessageMapper.updateUserMessage(searchMap);
        }
		
	}
	/**
	 * 获取某个活动的监测任务
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public List<AdMonitorTaskVo> findAllMemo(Integer activityId,List<AdMonitorTaskVo> memos) {
		return adMonitorTaskMapper.findAllMemo(activityId,memos);
	}
	/**
	 * 批量修改监测任务并创建监测任务反馈表
	 */
	@Override
	public void updateBatch(List<AdMonitorTaskVo> tasks) {
		//批量删除
		adMonitorTaskMapper.deleteBatch(tasks);
		//批量修改监测任务
		adMonitorTaskMapper.insertBatch(tasks);
		//批量插入监测反馈
		adMonitorTaskFeedbackMapper.insertBatch(tasks);
	}

	@Override
	public List<AdMonitorTaskVo> selectMonitorTaskIdsByActicityId(Integer acticityId) {
		return adMonitorTaskMapper.selectMonitorTaskIdsByActicityId(acticityId);
	}

}
