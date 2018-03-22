package com.bt.om.service;

import com.bt.om.entity.AdJiucuoTask;
import com.bt.om.entity.AdJiucuoTaskFeedback;
import com.bt.om.entity.vo.AdJiucuoTaskMobileVo;
import com.bt.om.entity.vo.AdJiucuoTaskVo;
import com.bt.om.entity.vo.AdMonitorTaskMobileVo;
import com.bt.om.entity.vo.AdMonitorTaskVo;
import com.bt.om.vo.web.SearchDataVo;

import java.util.List;

/**
 * Created by caiting on 2018/1/21.
 */
public interface IAdJiucuoTaskService {
    public void getPageData(SearchDataVo vo);
    public AdJiucuoTask getById(Integer id);
    public AdJiucuoTaskVo getVoById(Integer id);
    public AdJiucuoTaskFeedback getFeadBackById(Integer id);
    public AdJiucuoTaskFeedback getFeadBackByTaskId(Integer id);
    public void update(AdJiucuoTask task);
    /**
     *  审核通过
     **/
    public void pass(AdJiucuoTask task);
    /**
     *  审核不通过
     **/
    public void reject(AdJiucuoTask task,String reason);
    public void feedback(AdJiucuoTask task,AdJiucuoTaskFeedback feedback);
    public List<AdJiucuoTaskMobileVo> getByUserIdForMobile(Integer userId);
    /**
     * 根据当前纠错编号生成子任务
     **/
    public void createSubTask(Integer taskId);

    /**
     *  查询纠错子任务执行情况，用于详情
     **/
    public List<AdMonitorTaskVo> getSubTask(Integer id);
}
