package com.bt.om.service;

import com.bt.om.entity.AdMonitorTask;
import com.bt.om.entity.AdMonitorTaskFeedback;
import com.bt.om.entity.vo.AdActivityVo;
import com.bt.om.entity.vo.AdMonitorTaskMobileVo;
import com.bt.om.vo.web.SearchDataVo;

import java.util.List;

/**
 * Created by caiting on 2018/1/20.
 */
public interface IAdMonitorTaskService {

    public void getPageData(SearchDataVo vo);

    public void assign(String[] taskIds,Integer userId);

    public void update(AdMonitorTask task);

    /**
     * 审核通过
     **/
    public void pass(AdMonitorTask task);
    /**
     * 审核不通过
     **/
    public void reject(AdMonitorTask task,String reason);

    public List<AdMonitorTaskMobileVo> getByUserIdForMobile(Integer userId);

    public void feedback(Integer taskId, AdMonitorTaskFeedback feedback);
}
