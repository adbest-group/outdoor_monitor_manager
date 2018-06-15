package com.bt.om.service.impl;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
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

import com.bt.om.entity.AdActivity;
import com.bt.om.entity.AdActivityAdseat;
import com.bt.om.entity.AdActivityArea;
import com.bt.om.entity.AdActivityMedia;
import com.bt.om.entity.AdMonitorTask;
import com.bt.om.entity.AdSeatInfo;
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
import com.bt.om.enums.MonitorTaskStatus;
import com.bt.om.enums.MonitorTaskType;
import com.bt.om.enums.TaskProblemStatus;
import com.bt.om.mapper.AdActivityAdseatMapper;
import com.bt.om.mapper.AdActivityAreaMapper;
import com.bt.om.mapper.AdActivityMapper;
import com.bt.om.mapper.AdActivityMediaMapper;
import com.bt.om.mapper.AdMonitorTaskMapper;
import com.bt.om.mapper.AdSeatInfoMapper;
import com.bt.om.mapper.SysUserMapper;
import com.bt.om.service.IAdActivityService;
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

    @Override
    public void save(AdActivity adActivity) {
        adActivityMapper.insert(adActivity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(AdActivityVo adActivityVo, String activeSeat) {
    	Date now = new Date();
    	
        //保存活动
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
    }

    @Override
    public void modify(AdActivityVo adActivityVo, String activeSeat) {
    	Date now = new Date();
    	
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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void confirm(String[] activityIds, Integer assessorId) {
    	//[4] 确认操作在业务层方法里进行循环
    	for (String actId : activityIds) {
    		Integer id = Integer.parseInt(actId);
    		List<AdActivityAdseatVo> seats = adActivityAdseatMapper.selectByActivityId(id);
            List<AdMonitorTask> tasks = new ArrayList<>();
            for (AdActivityAdseatVo seat : seats) {
                //安装人员上刊安装
                //不需要安装类任务
//                AdMonitorTask taskSetUp = generateMonitorTask(id, seat);
//                taskSetUp.setTaskType(MonitorTaskType.SET_UP_MONITOR.getId());
//                taskSetUp.setStatus(MonitorTaskStatus.UNASSIGN.getId());
//                taskSetUp.setMonitorDate(seat.getMonitorStart());
//                tasks.add(taskSetUp);

                //如果需要上刊监测
                if (seat.getUpMonitor() == 1) {
                    AdMonitorTask task = generateMonitorTask(id, seat);
                    task.setTaskType(MonitorTaskType.UP_MONITOR.getId());
//                    task.setMonitorDate(Date.from(seat.getMonitorStart().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant()));
                    //任务日期为监测第一天，可持续upMonitorLastDays天
                    task.setMonitorDate(seat.getMonitorStart());
                    task.setMonitorLastDays(seat.getUpMonitorLastDays());
                    tasks.add(task);
                }
                //如果需要下刊监测
                if (seat.getDownMonitor() == 1) {
                    AdMonitorTask task = generateMonitorTask(id, seat);
                    task.setTaskType(MonitorTaskType.DOWNMONITOR.getId());
//                    task.setMonitorDate(Date.from(seat.getMonitorEnd().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().minusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant()));
                    task.setMonitorDate(Date.from(seat.getMonitorEnd().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().minusDays(seat.getDownMonitorLastDays()-1).atStartOfDay(ZoneId.systemDefault()).toInstant()));
                    task.setMonitorLastDays(seat.getDownMonitorLastDays());
                    tasks.add(task);
                }
                //如果需要投放期间监测
                if (seat.getDurationMonitor() == 1) {
                    LocalDate next = seat.getMonitorStart().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().plusMonths(1);
                    LocalDate end = seat.getMonitorEnd().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    /**
                     * 投放期间监测大致一个月一次，此处规则从监测开始时间开始算，每次month+1，
                     * 如果计算结果和监测结束在一个月，就不生成该次监测任务
                     **/
                    while(YearMonth.from(next).compareTo(YearMonth.from(end))<0){
                        AdMonitorTask task = generateMonitorTask(id, seat);
                        task.setTaskType(MonitorTaskType.DURATION_MONITOR.getId());
//                        long betweenDays = ChronoUnit.DAYS.between(seat.getMonitorStart().toInstant(),seat.getMonitorEnd().toInstant());
                        task.setMonitorDate(Date.from(next.atStartOfDay(ZoneId.systemDefault()).toInstant()));
                        task.setMonitorLastDays(seat.getDurationMonitorLastDays());
                        tasks.add(task);

                        next = next.plusMonths(1);
                    }
                }
            }
            //保存监测任务
//            adMonitorTaskMapper.insertList(tasks);
            for (AdMonitorTask task : tasks) {
                adMonitorTaskMapper.insert(task);
            }
            //修改广告活动广告位的创建任务状态
            for (AdActivityAdseatVo seat : seats) {
                seat.setTaskCreate(2);
                adActivityAdseatMapper.updateByPrimaryKeySelective(seat);
            }
            //修改活动状态
            AdActivity activity = new AdActivity();
            activity.setId(id);
            activity.setStatus(ActivityStatus.CONFIRMED.getId());
            activity.setAssessorId(assessorId); //设置审核人
            adActivityMapper.updateByPrimaryKeySelective(activity); 
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
}