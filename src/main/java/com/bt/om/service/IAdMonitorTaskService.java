package com.bt.om.service;

import com.bt.om.entity.AdMonitorTask;
import com.bt.om.entity.vo.AdActivityVo;
import com.bt.om.vo.web.SearchDataVo;

/**
 * Created by caiting on 2018/1/20.
 */
public interface IAdMonitorTaskService {

    public void getPageData(SearchDataVo vo);

    public void assign(String[] taskIds,Integer userId);
}
