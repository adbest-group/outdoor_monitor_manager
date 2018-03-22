package com.bt.om.service.impl;

import com.bt.om.entity.*;
import com.bt.om.entity.vo.ActivityMobileReportVo;
import com.bt.om.entity.vo.AdActivityAdseatVo;
import com.bt.om.entity.vo.AdActivityVo;
import com.bt.om.enums.ActivityStatus;
import com.bt.om.enums.MonitorTaskStatus;
import com.bt.om.enums.MonitorTaskType;
import com.bt.om.enums.TaskProblemStatus;
import com.bt.om.mapper.*;
import com.bt.om.service.IAdActivityService;
import com.bt.om.vo.web.SearchDataVo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.collections.MapUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;

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

    @Override
    public void save(AdActivity adActivity) {
        adActivityMapper.insert(adActivity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(AdActivityVo adActivityVo) {
        //保存活动
        int n = adActivityMapper.insert((AdActivity) adActivityVo);
//        if(n<1){
//            throw new RuntimeException("保存广告活动失败！");
//        }

        //保存广告活动区域
        for (AdActivityArea area : adActivityVo.getActivityAreas()) {
            area.setActivityId(adActivityVo.getId());
            adActivityAreaMapper.insert(area);
        }

        //保存广告活动媒体
        for (AdActivityMedia media : adActivityVo.getActivityMedias()) {
            media.setActivityId(adActivityVo.getId());
            adActivityMediaMapper.insert(media);
        }

        //保存广告活动广告位
        for (AdActivityAdseat seat : adActivityVo.getActivitySeats()) {
            seat.setActivityId(adActivityVo.getId());
            adActivityAdseatMapper.insert(seat);
        }

    }

    @Override
    public void modify(AdActivityVo adActivityVo) {
        //保存活动
        int n = adActivityMapper.updateByPrimaryKeySelective((AdActivity) adActivityVo);

        //保存广告活动区域
        adActivityAreaMapper.deleteByActivityId(adActivityVo.getId());
        for (AdActivityArea area : adActivityVo.getActivityAreas()) {
            area.setActivityId(adActivityVo.getId());
            adActivityAreaMapper.insert(area);
        }

        //保存广告活动媒体
        adActivityMediaMapper.deleteByActivityId(adActivityVo.getId());
        for (AdActivityMedia media : adActivityVo.getActivityMedias()) {
            media.setActivityId(adActivityVo.getId());
            adActivityMediaMapper.insert(media);

        }

        //保存广告活动广告位
        adActivityAdseatMapper.deleteByActivityId(adActivityVo.getId());
        for (AdActivityAdseat seat : adActivityVo.getActivitySeats()) {
            seat.setActivityId(adActivityVo.getId());
            adActivityAdseatMapper.insert(seat);
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
            vo.setList(new ArrayList<AdActivityVo>());
        }

    }

    @Override
    public AdActivityVo getVoById(Integer id) {
        return adActivityMapper.selectVoByPrimaryKey(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void confirm(Integer id) {

        List<AdActivityAdseatVo> seats = adActivityAdseatMapper.selectByActivityId(id);
        List<AdMonitorTask> tasks = new ArrayList<>();
        for (AdActivityAdseatVo seat : seats) {
            //安装人员上刊安装
            AdMonitorTask taskSetUp = generateMonitorTask(id, seat);
            taskSetUp.setTaskType(MonitorTaskType.SET_UP_MONITOR.getId());
            taskSetUp.setStatus(MonitorTaskStatus.UNASSIGN.getId());
            taskSetUp.setMonitorDate(seat.getMonitorStart());

            tasks.add(taskSetUp);
            //如果需要上刊监测
            if (seat.getUpMonitor() == 1) {
                AdMonitorTask task = generateMonitorTask(id, seat);
                task.setTaskType(MonitorTaskType.UP_MONITOR.getId());
                task.setMonitorDate(Date.from(seat.getMonitorStart().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant()));
                tasks.add(task);
            }
            //如果需要投放期间监测
            if (seat.getDurationMonitor() == 1) {
                AdMonitorTask task = generateMonitorTask(id, seat);
                task.setTaskType(MonitorTaskType.DURATION_MONITOR.getId());
                long betweenDays = ChronoUnit.DAYS.between(seat.getMonitorStart().toInstant(),seat.getMonitorEnd().toInstant());
                task.setMonitorDate(Date.from(seat.getMonitorStart().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().plusDays(betweenDays/2).atStartOfDay(ZoneId.systemDefault()).toInstant()));
                tasks.add(task);
            }
            //如果需要下刊监测
            if (seat.getDownMonitor() == 1) {
                AdMonitorTask task = generateMonitorTask(id, seat);
                task.setTaskType(MonitorTaskType.DOWNMONITOR.getId());
                task.setMonitorDate(Date.from(seat.getMonitorEnd().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().minusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant()));
                tasks.add(task);
            }
        }
        //保存监测任务
//        adMonitorTaskMapper.insertList(tasks);
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
        adActivityMapper.updateByPrimaryKeySelective(activity);
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
}
