package com.bt.om.service;

import com.bt.om.entity.AdJiucuoTask;
import com.bt.om.entity.AdJiucuoTaskFeedback;
import com.bt.om.entity.vo.AdJiucuoTaskVo;
import com.bt.om.vo.web.SearchDataVo;

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
}
