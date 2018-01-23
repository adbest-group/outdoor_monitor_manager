package com.bt.om.service.impl;

import com.bt.om.entity.AdMonitorTask;
import com.bt.om.enums.MonitorTaskStatus;
import com.bt.om.mapper.AdMonitorTaskMapper;
import com.bt.om.service.IAdMonitorTaskService;
import com.bt.om.vo.web.SearchDataVo;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

/**
 * Created by caiting on 2018/1/20.
 */
@Service
public class AdMonitorTaskService implements IAdMonitorTaskService {
    @Autowired
    private AdMonitorTaskMapper adMonitorTaskMapper;

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
}
