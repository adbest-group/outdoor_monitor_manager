package com.bt.om.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
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
import com.bt.om.entity.SysUser;
import com.bt.om.entity.SysUserExecute;
import com.bt.om.entity.vo.ActivityMobileReportVo;
import com.bt.om.entity.vo.AdActivityAdseatTaskVo;
import com.bt.om.entity.vo.AdActivityAdseatVo;
import com.bt.om.entity.vo.AdActivityVo;
import com.bt.om.entity.vo.AdActivityVo2;
import com.bt.om.entity.vo.AdMonitorTaskVo;
import com.bt.om.entity.vo.AdSeatCount;
import com.bt.om.enums.ActivityStatus;
import com.bt.om.enums.MessageIsFinish;
import com.bt.om.enums.MessageType;
import com.bt.om.enums.MonitorTaskStatus;
import com.bt.om.enums.MonitorTaskType;
import com.bt.om.enums.TaskProblemStatus;
import com.bt.om.mapper.AdActivityAdseatMapper;
import com.bt.om.mapper.AdActivityAreaMapper;
import com.bt.om.mapper.AdActivityMapper;
import com.bt.om.mapper.AdActivityMediaMapper;
import com.bt.om.mapper.AdMonitorTaskMapper;
import com.bt.om.mapper.AdSeatInfoMapper;
import com.bt.om.mapper.AdUserMessageMapper;
import com.bt.om.mapper.SysResourcesMapper;
import com.bt.om.mapper.SysUserMapper;
import com.bt.om.mapper.SysUserResMapper;
import com.bt.om.service.IAdActivityService;
import com.bt.om.util.ConfigUtil;
import com.bt.om.vo.web.SearchDataVo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * Created by caiting on 2018/1/18.
 */
@Service
public class AdActivityService implements IAdActivityService {
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
    SysUserMapper sysUserMapper;
    @Autowired
    AdSeatInfoMapper adSeatInfoMapper;
    @Autowired
    SysUserResMapper sysUserResMapper;
    @Autowired
    SysResourcesMapper sysResourcesMapper;
    @Autowired
    AdUserMessageMapper adUserMessageMapper;

    @Override
    public void save(AdActivity adActivity) {
        adActivityMapper.insert(adActivity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(AdActivityVo adActivityVo, String activeSeat) {
    	Date now = new Date();
    	
        //[1] 保存活动
        adActivityMapper.insert((AdActivity) adActivityVo);
        
//        if(n<1){
//            throw new RuntimeException("保存广告活动失败！");
//        }

//        //保存广告活动区域
//        for (AdActivityArea area : adActivityVo.getActivityAreas()) {
//            area.setActivityId(adActivityVo.getId());
//            adActivityAreaMapper.insert(area);
//        }

//        //保存广告活动媒体
//        for (AdActivityMedia media : adActivityVo.getActivityMedias()) {
//            media.setActivityId(adActivityVo.getId());
//            adActivityMediaMapper.insert(media);
//        }

        //[2] 保存广告活动广告位
        List<AdActivityAdseat> adActivityAdseats = new ArrayList<>();
        
        String[] seatIds = activeSeat.split(",");
        List<String> seatIdList = Arrays.asList(seatIds);
        //查询广告位的相关信息
        Map<String, Object> searchMap = new HashMap<>();
        searchMap.put("ids", seatIdList);
        List<AdSeatInfo> adSeatInfos = adSeatInfoMapper.selectInfoByIds(searchMap);
        
        for (String seatId : seatIds) {
        	Integer id = Integer.parseInt(seatId);
        	
        	AdActivityAdseat seat = new AdActivityAdseat();
        	seat.setActivityId(adActivityVo.getId());
        	seat.setAdSeatId(id); //广告位id
        	seat.setMonitorStart(adActivityVo.getStartTime());
        	seat.setMonitorEnd(adActivityVo.getEndTime());
        	seat.setUpMonitor(1);
        	seat.setUpMonitorLastDays(3); //默认3天
        	seat.setDownMonitor(1);
        	seat.setDownMonitorLastDays(3); //默认3天
        	seat.setDurationMonitor(1);
        	seat.setDurationMonitorLastDays(3); //默认3天
        	seat.setSamplePicUrl(adActivityVo.getSamplePicUrl());
        	seat.setTaskCreate(2); //默认已创建
        	seat.setCreateTime(now);
        	seat.setUpdateTime(now);
        	
        	for (AdSeatInfo adSeatInfo : adSeatInfos) {
				if(adSeatInfo.getId().equals(id)) {
					seat.setMediaId(adSeatInfo.getMediaId());
					break;
				}
			}
        	
        	adActivityAdseats.add(seat);
		}
        
        if(adActivityAdseats != null && adActivityAdseats.size() > 0) {
        	//批量插入
        	adActivityAdseatMapper.insertBatch(adActivityAdseats);
        }
        
        //[3] 插入站内信
        SysUser sysUser = sysUserMapper.selectByPrimaryKey(adActivityVo.getUserId());
        List<Integer> list = new ArrayList<>();
        list = sysUserMapper.getUserId(4);//4：超级管理员
        Integer dep_id = sysResourcesMapper.getUserId(1);//1：活动审核部门
        
        List<Integer> reslist = sysUserResMapper.getUserId(adActivityVo.getUserId(), 2);//获取广告商下面的组id集合
        Integer resId = null;
        for(Integer i:reslist) {
        	resId = sysResourcesMapper.getResId(i, 1);//1：活动审核部门
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
        
        List<AdUserMessage> message = new ArrayList<>();
        for(Integer i: userIdList) {
        	AdUserMessage mess = new AdUserMessage();
        	mess.setContent("【" + sysUser.getRealname()+"】广告主的"+ "【" + adActivityVo.getActivityName() +"】活动待确认！");
        	mess.setTargetId(adActivityVo.getId()); //活动表的id
        	mess.setTargetUserId(i);
        	mess.setIsFinish(0);
        	mess.setType(1); //1：活动确认
        	mess.setCreateTime(now);
        	mess.setUpdateTime(now);
        	message.add(mess);
        }
        if(message != null && message.size() > 0) {
        	adUserMessageMapper.insertMessage(message);
        }
    }

    @Override
    public void modify(AdActivityVo adActivityVo, String activeSeat) {
    	Date now = new Date();
    	
    	SysUser sysUser = sysUserMapper.selectByPrimaryKey(adActivityVo.getUserId()); //获取活动所属广告主的信息
    	
        //保存活动
        adActivityMapper.updateByPrimaryKeySelective((AdActivity) adActivityVo);

//        //保存广告活动区域
//        adActivityAreaMapper.deleteByActivityId(adActivityVo.getId());
//        for (AdActivityArea area : adActivityVo.getActivityAreas()) {
//            area.setActivityId(adActivityVo.getId());
//            adActivityAreaMapper.insert(area);
//        }
//
//        //保存广告活动媒体
//        adActivityMediaMapper.deleteByActivityId(adActivityVo.getId());
//        for (AdActivityMedia media : adActivityVo.getActivityMedias()) {
//            media.setActivityId(adActivityVo.getId());
//            adActivityMediaMapper.insert(media);
//        }

//        //保存广告活动广告位
//        adActivityAdseatMapper.deleteByActivityId(adActivityVo.getId());
//        for (AdActivityAdseat seat : adActivityVo.getActivitySeats()) {
//            seat.setActivityId(adActivityVo.getId());
//            adActivityAdseatMapper.insert(seat);
//        }
        
        //保存广告活动广告位
        List<AdActivityAdseat> adActivityAdseats = new ArrayList<>();
        
        String[] seatIds = activeSeat.split(",");
        List<String> seatIdList = Arrays.asList(seatIds);
        //查询广告位的相关信息
        Map<String, Object> searchMap = new HashMap<>();
        searchMap.put("ids", seatIdList);
        List<AdSeatInfo> adSeatInfos = adSeatInfoMapper.selectInfoByIds(searchMap);
        
        for (String seatId : seatIds) {
        	Integer id = Integer.parseInt(seatId);
        	
        	AdActivityAdseat seat = new AdActivityAdseat();
        	seat.setActivityId(adActivityVo.getId());
        	seat.setAdSeatId(id);
        	seat.setMonitorStart(adActivityVo.getStartTime());
        	seat.setMonitorEnd(adActivityVo.getEndTime());
        	seat.setUpMonitor(1);
        	seat.setUpMonitorLastDays(3); //默认3天
        	seat.setDownMonitor(1);
        	seat.setDownMonitorLastDays(3); //默认3天
        	seat.setDurationMonitor(1);
        	seat.setDurationMonitorLastDays(3); //默认3天
        	seat.setSamplePicUrl(adActivityVo.getSamplePicUrl());
        	seat.setTaskCreate(2); //默认已创建
        	seat.setCreateTime(now);
        	seat.setUpdateTime(now);
        	
        	for (AdSeatInfo adSeatInfo : adSeatInfos) {
        		if(adSeatInfo.getId().equals(id)) {
					seat.setMediaId(adSeatInfo.getMediaId());
					break;
				}
			}
        	
        	adActivityAdseats.add(seat);
		}
        
        //先删除
        adActivityAdseatMapper.deleteByActivityId(adActivityVo.getId());
        
        if(adActivityAdseats != null && adActivityAdseats.size() > 0) {
        	//批量插入
        	adActivityAdseatMapper.insertBatch(adActivityAdseats);
        }
        
        //修改站内信
        Map<String, Object> map = new HashMap<>();
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("【");
        stringBuffer.append(sysUser.getRealname());
        stringBuffer.append("】广告主的【");
        stringBuffer.append(adActivityVo.getActivityName());
        stringBuffer.append("】活动待确认！");
        searchMap.put("content", stringBuffer.toString());
        searchMap.put("isFinish", MessageIsFinish.CONFIRMED.getId()); //0：未处理
        searchMap.put("targetId", adActivityVo.getId()); //活动表的id
        searchMap.put("type", MessageType.ACTIVITY_AUDIT.getId()); //1：活动确认
        adUserMessageMapper.updateUserMessage(map);
    }

    @Override
    public void modify(AdActivityVo adActivityVo, Integer[] activitySeatDels) {

    }

    @Override
    public void getPageData(SearchDataVo vo) {
        int count = adActivityMapper.getPageCount(vo.getSearchMap());
        vo.setCount(count);
        if (count > 0) {
            vo.setList(adActivityMapper.getPageData(vo.getSearchMap(), new RowBounds(vo.getStart(), vo.getSize())));
        } else {
            vo.setList(new ArrayList<AdActivityVo2>());
        }
    }

    @Override
    public AdActivityVo getVoById(Integer id) {
    	if(id != null) {
    		AdActivityVo adActivityVo = adActivityMapper.selectVoByPrimaryKey(id);
        	List<AdActivityAdseatVo> adseatVos = adActivityAdseatMapper.selectByActivityId(id);
        	adActivityVo.setAdActivityAdseatVos(adseatVos);
        	return adActivityVo;
    	} else {
    		return null;
    	}
    }

    /**
     * 活动确认, 生成4种任务
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void confirm(String[] activityIds, Integer assessorId) {
    	Date now = new Date();
    	Integer monitorTime = ConfigUtil.getInt("monitor_time"); //允许任务执行天数
        Integer auditTime = ConfigUtil.getInt("audit_time"); //允许任务审核天数
        
        SysUser auditPerson = sysUserMapper.selectByPrimaryKey(assessorId); //获取审核人的相关信息
    	
        //【2】添加上刊任务的站内信
        List<AdUserMessage> message = new ArrayList<>();
        List<Integer> userIds = new ArrayList<>();
        //查询添加站内信的用户id集合
		userIds = sysUserMapper.getUserId(4);//超级管理员id
		Integer dep_id = sysResourcesMapper.getUserId(2);//2：任务审核、指派部门
		userIds.add(dep_id);
        
    	//[4] 确认操作在业务层方法里进行循环
    	for (String actId : activityIds) {
    		Integer id = Integer.parseInt(actId);
    		AdActivity adActivity = adActivityMapper.selectByPrimaryKey(id); //活动信息
    		List<AdActivityAdseatVo> seats = adActivityAdseatMapper.selectByActivityId(id); //每个活动对应的广告位信息
    		
    		SysUser sysUser = sysUserMapper.selectByPrimaryKey(adActivity.getUserId()); //获取活动所属广告主的信息
    		
            List<AdMonitorTask> tasks = new ArrayList<>();
            for (AdActivityAdseatVo seat : seats) {
                //安装人员上刊安装
                //不需要安装类任务
//                AdMonitorTask taskSetUp = generateMonitorTask(id, seat);
//                taskSetUp.setTaskType(MonitorTaskType.SET_UP_MONITOR.getId());
//                taskSetUp.setStatus(MonitorTaskStatus.UNASSIGN.getId());
//                taskSetUp.setMonitorDate(seat.getMonitorStart());
//                tasks.add(taskSetUp);

            	//【1】上刊任务
            	AdMonitorTask upTask = generateMonitorTask(id, seat);
            	upTask.setStatus(MonitorTaskStatus.UNASSIGN.getId()); //直接就是待指派
            	upTask.setTaskType(MonitorTaskType.UP_TASK.getId());
            	upTask.setMonitorDate(DateUtil.getDayBegin(new Date()));
            	if(StringUtil.isNotBlank(adActivity.getUpTaskTime())) {
            		Date upTaskTime = DateUtil.parseStrDate(adActivity.getUpTaskTime(), "yyyy-MM-dd"); //页面上的上刊任务出报告时间
            		Date beforeDays = DateUtil.addDays(upTaskTime, -auditTime); //提前两天
            		int betweenDays = DateUtil.getBetweenDays(upTask.getMonitorDate(), beforeDays);
            		upTask.setMonitorLastDays(betweenDays);
            		upTask.setReportTime(upTaskTime);
                    tasks.add(upTask);
            	}
            	
                //【2】上刊监测任务
                if (seat.getUpMonitor() == 1) {
                	if(StringUtil.isNotBlank(adActivity.getUpMonitorTaskTime())) {
                		AdMonitorTask task = generateMonitorTask(id, seat);
                        task.setTaskType(MonitorTaskType.UP_MONITOR.getId());
//                        task.setMonitorDate(Date.from(seat.getMonitorStart().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant()));
//                        //任务日期为监测第一天，可持续upMonitorLastDays天
//                        task.setMonitorDate(seat.getMonitorStart());
//                        task.setMonitorLastDays(seat.getUpMonitorLastDays());
                        Date upMonitorTask = DateUtil.parseStrDate(adActivity.getUpMonitorTaskTime(), "yyyy-MM-dd"); //页面上的上刊监测任务出报告时间
                        Date auditDate = DateUtil.addDays(upMonitorTask, -auditTime); //提前两天审核+替换图片
                        Date monitorDate = DateUtil.addDays(auditDate, -monitorTime); //提前三天执行任务
                        int betweenDays = DateUtil.getBetweenDays(monitorDate, auditDate);
                        task.setMonitorDate(monitorDate);
                		task.setMonitorLastDays(betweenDays);
                		task.setReportTime(upMonitorTask);
                        tasks.add(task);
                	}
                }
                
                //【3】下刊监测任务
                if (seat.getDownMonitor() == 1) {
                	if(StringUtil.isNotBlank(adActivity.getDownMonitorTaskTime())) {
                		AdMonitorTask task = generateMonitorTask(id, seat);
                        task.setTaskType(MonitorTaskType.DOWNMONITOR.getId());
//                        task.setMonitorDate(Date.from(seat.getMonitorEnd().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().minusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant()));
//                        task.setMonitorDate(Date.from(seat.getMonitorEnd().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().minusDays(seat.getDownMonitorLastDays()-1).atStartOfDay(ZoneId.systemDefault()).toInstant()));
//                        task.setMonitorLastDays(seat.getDownMonitorLastDays());
                        Date downMonitorTask = DateUtil.parseStrDate(adActivity.getDownMonitorTaskTime(), "yyyy-MM-dd"); //页面上的下刊监测任务出报告时间
                        Date auditDate = DateUtil.addDays(downMonitorTask, -auditTime); //提前两天审核+替换图片
                        Date monitorDate = DateUtil.addDays(auditDate, -monitorTime); //提前三天执行任务
                        int betweenDays = DateUtil.getBetweenDays(monitorDate, auditDate);
                        task.setMonitorDate(monitorDate);
                		task.setMonitorLastDays(betweenDays);
                		task.setReportTime(downMonitorTask);
                        tasks.add(task);
                	}
                }
                
                //【4】投放期间监测任务
                if (seat.getDurationMonitor() == 1) {
//                    LocalDate next = seat.getMonitorStart().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().plusMonths(1);
//                    LocalDate end = seat.getMonitorEnd().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
//                    /**
//                     * 投放期间监测大致一个月一次，此处规则从监测开始时间开始算，每次month+1，
//                     * 如果计算结果和监测结束在一个月，就不生成该次监测任务
//                     **/
//                    while(YearMonth.from(next).compareTo(YearMonth.from(end))<0){
//                        AdMonitorTask task = generateMonitorTask(id, seat);
//                        task.setTaskType(MonitorTaskType.DURATION_MONITOR.getId());
////                        long betweenDays = ChronoUnit.DAYS.between(seat.getMonitorStart().toInstant(),seat.getMonitorEnd().toInstant());
//                        task.setMonitorDate(Date.from(next.atStartOfDay(ZoneId.systemDefault()).toInstant()));
//                        task.setMonitorLastDays(seat.getDurationMonitorLastDays());
//                        tasks.add(task);
//
//                        next = next.plusMonths(1);
//                    }
                	if(StringUtil.isNotBlank(adActivity.getDurationMonitorTaskTime())) {
                		String[] splitDates = adActivity.getDurationMonitorTaskTime().split(",");
                		for (String splitDate : splitDates) {
                			AdMonitorTask task = generateMonitorTask(id, seat);
                            task.setTaskType(MonitorTaskType.DURATION_MONITOR.getId());
                            Date durationMonitorTask = DateUtil.parseStrDate(splitDate, "yyyy-MM-dd"); //页面上的投放期间监测任务出报告时间
                            Date auditDate = DateUtil.addDays(durationMonitorTask, -auditTime); //提前两天审核+替换图片
                            Date monitorDate = DateUtil.addDays(auditDate, -monitorTime); //提前三天执行任务
                            int betweenDays = DateUtil.getBetweenDays(monitorDate, auditDate);
                            task.setMonitorDate(monitorDate);
                    		task.setMonitorLastDays(betweenDays);
                    		task.setReportTime(durationMonitorTask);
                            tasks.add(task);
						}
                	}
                }
            }
            
            //批量保存监测任务
            if(tasks != null && tasks.size() > 0) {
            	adMonitorTaskMapper.insertList(tasks);
            }
            
//            //修改广告活动广告位的创建任务状态【暂时没用】
//            for (AdActivityAdseatVo seat : seats) {
//                seat.setTaskCreate(2);
//                adActivityAdseatMapper.updateByPrimaryKeySelective(seat);
//            }
            
            //修改活动状态
            AdActivity activity = new AdActivity();
            activity.setId(id);
            activity.setStatus(ActivityStatus.CONFIRMED.getId());
            activity.setAssessorId(assessorId); //设置审核人
            adActivityMapper.updateByPrimaryKeySelective(activity); 
            
            //【1】修改活动相关的站内信
            Map<String, Object> searchMap = new HashMap<>();
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("【");
            stringBuffer.append(sysUser.getRealname());
            stringBuffer.append("】广告主的【");
            stringBuffer.append(adActivity.getActivityName());
            stringBuffer.append("】活动已被【");
            stringBuffer.append(auditPerson.getRealname());
            stringBuffer.append("】确认");
            searchMap.put("content", stringBuffer.toString());
            searchMap.put("isFinish", MessageIsFinish.UNCONFIRM.getId()); //1：已处理
            searchMap.put("targetId", id); //活动表的id
            searchMap.put("type", MessageType.ACTIVITY_AUDIT.getId()); //1：活动确认
            adUserMessageMapper.updateUserMessage(searchMap);
            
            //【2】添加上刊任务的站内信
            List<Integer> taskIds = adMonitorTaskMapper.selectUpTaskIds(id); //通过活动id查询出对应的上刊任务id集合
            for (Integer monitorId : taskIds) {
				    List<Integer> reslist = sysUserResMapper.getUserId(sysUser.getId(), 2);//获取广告商下面的组id集合
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
		        
		        StringBuffer stringBufferTask = new StringBuffer();
		        stringBufferTask.append("【");
		        stringBufferTask.append(sysUser.getRealname());
		        stringBufferTask.append("】广告主的【");
		        stringBufferTask.append(adActivity.getActivityName());
		        stringBufferTask.append("】活动的【上刊任务】待指派");
	            
		        for(Integer i: userIdList) {
	            	AdUserMessage mess = new AdUserMessage();
	            	mess.setContent(stringBufferTask.toString());
	            	mess.setTargetId(monitorId); //任务表的id
	            	mess.setTargetUserId(i);
	            	mess.setIsFinish(MessageIsFinish.CONFIRMED.getId()); //0：未处理
	            	mess.setType(MessageType.TASK_ASSIGN.getId()); //3：任务指派
	            	mess.setCreateTime(now);
	            	mess.setUpdateTime(now);
	            	message.add(mess);
	            }
            }
		}
    	
    	if(message != null && message.size() > 0) {
        	adUserMessageMapper.insertMessage(message);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Integer id) {
        adMonitorTaskMapper.deleteByActivityId(id);
        adActivityAreaMapper.deleteByActivityId(id);
        adActivityMediaMapper.deleteByActivityId(id);
        adActivityAdseatMapper.deleteByActivityId(id);
        adActivityMapper.deleteByPrimaryKey(id);
    }

    @Override
    public List<AdActivity> getAll() {
        return adActivityMapper.selectByMap(MapUtils.EMPTY_MAP);
    }

    @Override
    public List<AdActivity> getByUerId(Integer userId) {
        Map<String, Object> map = Maps.newHashMap();
        map.put("userId", userId);
        return adActivityMapper.selectByMap(map);
    }

    @Override
    public AdActivityAdseat getActivitySeatById(Integer id) {
        return adActivityAdseatMapper.selectVoById(id);
    }

    @Override
    public List<AdActivityAdseatVo> getActivitySeatBySeatId(Integer id) {
        return adActivityAdseatMapper.selectVoBySeatId(id);
    }
	
	@Override
    public List<AdActivityAdseatVo> selectVoByLonLatTitle(Double lon, Double lat, String title) {
        return adActivityAdseatMapper.selectVoByLonLatTitle(lon, lat, title);
    }

    @Override
    public List<AdActivityAdseatVo> getActivitySeatBySeatCode(String adSeatCode) {
        return adActivityAdseatMapper.selectVoBySeatCode(adSeatCode);
    }

    @Override
    public List<ActivityMobileReportVo> getMobileReport(SysUserExecute user) {
        SysUser sysUser = sysUserMapper.findByUsername(user.getUsername());
        if (sysUser != null) {
            return adActivityMapper.selectActivityReportForMobile(sysUser.getId());
        } else {
            return Lists.newArrayList();
        }
    }

    private AdMonitorTask generateMonitorTask(Integer activityId, AdActivityAdseat seat) {
        Date now = new Date();
        AdMonitorTask task = new AdMonitorTask();
        task.setActivityId(activityId);
        task.setActivityAdseatId(seat.getId());
        task.setStatus(MonitorTaskStatus.UN_ACTIVE.getId());
        task.setProblemStatus(TaskProblemStatus.UNMONITOR.getId());
        task.setCreateTime(now);
        task.setUpdateTime(now);
        return task;
    }

	@Override
	public AdActivity getById(Integer id) {
		return adActivityMapper.selectByPrimaryKey(id);
	}

	/**
	 * 后台excel导出和pdf导出查询活动的广告位信息
	 */
	@Override
	public List<AdActivityAdseatTaskVo> selectAdActivityAdseatTaskReport(Integer activityId){
		return adActivityAdseatMapper.selectAdActivityAdseatTaskReport(activityId);
	}
	
	/**
	 * APP端广告主查询活动的广告位信息, 需分页
	 */
	@Override
	public void selectAdActivityAdseatTask(SearchDataVo vo) {
		int count = adActivityAdseatMapper.selectAdActivityAdseatTaskCount(vo.getSearchMap());
        vo.setCount(count);
        if (count > 0) {
            vo.setList(adActivityAdseatMapper.selectAdActivityAdseatTask(vo.getSearchMap(), new RowBounds(vo.getStart(), vo.getSize())));
        } else {
            vo.setList(new ArrayList<AdActivityAdseatTaskVo>());
        }
	}
	
//    public static void main(String[] args) {
//        System.out.println(LocalDate.now());
//        System.out.println(YearMonth.from(LocalDate.now()));
//        System.out.println(LocalDate.now().plusYears(1).getMonthValue());
//    }

	@Override
	public void selectReportPageData(SearchDataVo vo) {
		int count = adActivityMapper.selectActivityReportByUserIdCount(vo.getSearchMap());
        vo.setCount(count);
        if (count > 0) {
            vo.setList(adActivityMapper.selectActivityReportByUserId(vo.getSearchMap(), new RowBounds(vo.getStart(), vo.getSize())));
        } else {
            vo.setList(new ArrayList<AdMonitorTaskVo>());
        }
	}
	
	/**
	 * 查询时间段以外的活动信息
	 */
	@Override
	public List<Integer> selectReportPageDataTime(Map<String, Object> searchMap) {
		List<Integer> activityReportByTime1 = adActivityMapper.selectActivityReportByTime1(searchMap);
		List<Integer> activityReportByTime2 = adActivityMapper.selectActivityReportByTime2(searchMap);
		activityReportByTime1.addAll(activityReportByTime2);
		return activityReportByTime1;
	}

	@Override
	public List<AdActivityAdseatTaskVo> selectAdSeatTaskReport(Integer activityId) {
		return adActivityAdseatMapper.selectAdSeatTaskReport(activityId);
	}
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updateStatusByEndTime(Date nowDate) {
		adActivityMapper.updateStatusByEndTime(nowDate);
	}
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void deadLineAuditActivity(Date endDate) {
		//[1] 获取超时未确认的活动id集合
		List<Integer> activityIds = adActivityMapper.getDeadLineAuditActivity(endDate);
		if(activityIds != null && activityIds.size() > 0) {
			//[2] 更新超时未确认的活动
			adActivityMapper.deadLineAuditActivity(endDate);
			//[3] 删除超时未确认的活动的广告位占用信息
			Map<String, Object> searchMap = new HashMap<>();
			searchMap.put("activityIds", activityIds);
			adActivityAdseatMapper.deleteByActivityIds(searchMap);
		}
	}
	
	/**
	 * 获取所有当前日期已经结束状态的活动创建者列表
	 */
	@Override
	public List<Integer> getEndActivityList(Date nowDate) {
		return adActivityMapper.getEndActivityList(nowDate);
	}

	/**
	 * 获取当前审核员待审核的活动列表
	 */
	@Override
	public List<AdActivity> selectAllByAssessorId(Map<String, Object> searchMap) {
		return adActivityMapper.selectAllByAssessorId(searchMap);
	}

	/**
	 * 当前审核员已经全部审核完毕所有的活动, 获取新的一条待审核活动, 打上自己的id标签
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public List<AdActivity> getAtimeActivity(Map<String, Object> searchMap) {
		//[1] 查询 for update
		Integer assessorId = (Integer) searchMap.get("assessorId");
		List<AdActivity> atimeActivity = adActivityMapper.getAtimeActivity(searchMap);
		List<Integer> ids = new ArrayList<>();
		for (AdActivity adActivity : atimeActivity) {
			ids.add(adActivity.getId());
			adActivity.setAssessorId(assessorId);
		}
		//[2] 更新 update
		searchMap.clear();
		searchMap.put("assessorId", assessorId);
		searchMap.put("ids", ids);
		if(ids.size() > 0) {
			adActivityMapper.updateAssessorId(searchMap);
		}
		return atimeActivity;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void offActivityByAssessorId(Integer id) {
		adActivityMapper.cancelActivityByAssessorId(id);
		
	}

	@Override
	public List<AdActivity> getAllByStatusUncertain(Map<String, Object> searchMap) {
		return adActivityMapper.getAllByStatusUncertain(searchMap);
	}
	
	/**
	 * 按照广告主在添加活动时选择的活动投放时间段  查询该时间段内各个广告位是否参与的活动以及参与的活动数量
	 */
	@Override
	public List<AdSeatCount> selectActiveActivityCount(Map<String, Object> searchMap) {
		List<AdSeatCount> adSeatCounts = new ArrayList<>();
		List<AdSeatCount> adSeatCounts1 = adActivityAdseatMapper.selectActiveActivityCount1(searchMap);
		List<AdSeatCount> adSeatCounts2 = adActivityAdseatMapper.selectActiveActivityCount2(searchMap);
		List<AdSeatCount> adSeatCounts3 = adActivityAdseatMapper.selectActiveActivityCount3(searchMap);
		List<AdSeatCount> adSeatCounts4 = adActivityAdseatMapper.selectActiveActivityCount4(searchMap);
		for (AdSeatCount adSeatCount : adSeatCounts1) {
			boolean isExist = false; //是否在返回结果中存在
			for (AdSeatCount returnCount : adSeatCounts) { //遍历返回结果
				if(returnCount.getAdseatId() == adSeatCount.getAdseatId()) {
					returnCount.setCount(returnCount.getCount() + adSeatCount.getCount());
					isExist = true;
					break;
				}
			}
			
			if(isExist == false) {
				adSeatCounts.add(adSeatCount);
			}
		}
		for (AdSeatCount adSeatCount : adSeatCounts2) {
			boolean isExist = false; //是否在返回结果中存在
			for (AdSeatCount returnCount : adSeatCounts) { //遍历返回结果
				if(returnCount.getAdseatId() == adSeatCount.getAdseatId()) {
					returnCount.setCount(returnCount.getCount() + adSeatCount.getCount());
					isExist = true;
					break;
				}
			}
			
			if(isExist == false) {
				adSeatCounts.add(adSeatCount);
			}
		}
		for (AdSeatCount adSeatCount : adSeatCounts3) {
			boolean isExist = false; //是否在返回结果中存在
			for (AdSeatCount returnCount : adSeatCounts) { //遍历返回结果
				if(returnCount.getAdseatId() == adSeatCount.getAdseatId()) {
					returnCount.setCount(returnCount.getCount() + adSeatCount.getCount());
					isExist = true;
					break;
				}
			}
			
			if(isExist == false) {
				adSeatCounts.add(adSeatCount);
			}
		}
		for (AdSeatCount adSeatCount : adSeatCounts4) {
			boolean isExist = false; //是否在返回结果中存在
			for (AdSeatCount returnCount : adSeatCounts) { //遍历返回结果
				if(returnCount.getAdseatId() == adSeatCount.getAdseatId()) {
					returnCount.setCount(returnCount.getCount() + adSeatCount.getCount());
					isExist = true;
					break;
				}
			}
			
			if(isExist == false) {
				adSeatCounts.add(adSeatCount);
			}
		}
		return adSeatCounts;
	}
	
	/**
	 * 根据活动查询下属广告位的信息
	 */
	@Override
	public List<AdSeatInfo> selectSeatInfoByActivityId(Integer activityId) {
		return adActivityAdseatMapper.selectSeatInfoByActivityId(activityId);
	}

	/**
	 * 根据memo确定广告位信息
	 * */
	@Override
	public List<AdActivityAdseatVo> getActivitySeatByMemo(String memo) {
		return adActivityAdseatMapper.selectVoByMemo(memo);
	}

	@Override
	public AdActivity getActivityName(int id) {
		return adActivityMapper.selectByPrimaryKey(id);
	}

	@Override
	public AdActivity getUserId(Integer activityId) {
		return adActivityMapper.selectByPrimaryKey(activityId);
	}


}