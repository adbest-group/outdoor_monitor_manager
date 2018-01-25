package com.bt.om.service.impl;

import com.bt.om.entity.AdMonitorTask;
import com.bt.om.entity.AdMonitorTaskFeedback;
import com.bt.om.entity.vo.AdMonitorTaskMobileVo;
import com.bt.om.enums.MonitorTaskStatus;
import com.bt.om.mapper.AdMonitorTaskFeedbackMapper;
import com.bt.om.mapper.AdMonitorTaskMapper;
import com.bt.om.service.IAdMonitorTaskService;
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

    @Override
    public void getPageData(SearchDataVo vo) {
        int count = adMonitorTaskMapper.getPageCount(vo.getSearchMap());
        vo.setCount(count);
        if(count>0){
            vo.setList(adMonitorTaskMapper.getPageData(vo.getSearchMap(), new RowBounds(vo.getStart(), vo.getSize())));
        }else{
            vo.setList(new ArrayList<AdMonitorTask>());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void assign(String[] taskIds,Integer userId) {
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
    public List<AdMonitorTaskMobileVo> getByUserIdForMobile(Integer userId) {
        return adMonitorTaskMapper.selectByUserId(userId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void feedback(Integer taskId, AdMonitorTaskFeedback feedback) {
        Date now = new Date();
        List<AdMonitorTaskFeedback> old_feed = adMonitorTaskFeedbackMapper.selectByTaskId(taskId,1);
        for(AdMonitorTaskFeedback old:old_feed){
            old.setStatus(2);
            old.setUpdateTime(now);
            adMonitorTaskFeedbackMapper.updateByPrimaryKeySelective(old);
        }
        feedback.setCreateTime(now);
        feedback.setUpdateTime(now);
        feedback.setMonitorTaskId(taskId);
        adMonitorTaskFeedbackMapper.insertSelective(feedback);
        AdMonitorTask task = new AdMonitorTask();
        task.setId(taskId);
        task.setStatus(MonitorTaskStatus.UNVERIFY.getId());
        adMonitorTaskMapper.updateByPrimaryKeySelective(task);
    }
}
