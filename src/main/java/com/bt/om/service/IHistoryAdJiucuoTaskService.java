package com.bt.om.service;

import java.util.List;
import java.util.Map;

import com.bt.om.entity.AdJiucuoTask;
import com.bt.om.entity.AdJiucuoTaskFeedback;
import com.bt.om.entity.AdMonitorTask;
import com.bt.om.entity.vo.AdJiucuoTaskMobileVo;
import com.bt.om.entity.vo.AdJiucuoTaskVo;
import com.bt.om.entity.vo.AdMonitorTaskVo;
import com.bt.om.vo.web.SearchDataVo;

/**
 * Created by caiting on 2018/1/21.
 */
public interface IHistoryAdJiucuoTaskService {
    public void getPageData(SearchDataVo vo);
    public AdJiucuoTaskVo getVoById(SearchDataVo vo);
    public AdJiucuoTaskFeedback getFeadBackByTaskId(SearchDataVo vo);

    /**
     *  查询纠错子任务执行情况，用于详情
     **/
    public List<AdMonitorTaskVo> getSubTask(SearchDataVo vo);
    
}
