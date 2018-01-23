package com.bt.om.service.impl;

import com.bt.om.entity.AdJiucuoTask;
import com.bt.om.entity.AdJiucuoTaskFeedback;
import com.bt.om.entity.AdMonitorTask;
import com.bt.om.entity.vo.AdJiucuoTaskVo;
import com.bt.om.mapper.AdJiucuoTaskFeedbackMapper;
import com.bt.om.mapper.AdJiucuoTaskMapper;
import com.bt.om.service.IAdJiucuoTaskService;
import com.bt.om.vo.web.SearchDataVo;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

/**
 * Created by caiting on 2018/1/21.
 */
@Service
public class AdJiucuoTaskService implements IAdJiucuoTaskService {
    @Autowired
    private AdJiucuoTaskMapper adJiucuoTaskMapper;
    @Autowired
    private AdJiucuoTaskFeedbackMapper adJiucuoTaskFeedbackMapper;

    @Override
    public void getPageData(SearchDataVo vo) {
        int count = adJiucuoTaskMapper.getPageCount(vo.getSearchMap());
        vo.setCount(count);
        if(count>0){
            vo.setList(adJiucuoTaskMapper.getPageData(vo.getSearchMap(), new RowBounds(vo.getStart(), vo.getSize())));
        }else{
            vo.setList(new ArrayList<AdJiucuoTask>());
        }
    }

    @Override
    public AdJiucuoTask getById(Integer id) {
        return adJiucuoTaskMapper.selectByPrimaryKey(id);
    }

    @Override
    public AdJiucuoTaskVo getVoById(Integer id) {
        return adJiucuoTaskMapper.selectVoByPrimaryKey(id);
    }

    @Override
    public AdJiucuoTaskFeedback getFeadBackById(Integer id) {
        return adJiucuoTaskFeedbackMapper.selectByPrimaryKey(id);
    }

    @Override
    public AdJiucuoTaskFeedback getFeadBackByTaskId(Integer id) {
        return adJiucuoTaskFeedbackMapper.selectByTaskId(id);
    }

    @Override
    public void update(AdJiucuoTask task) {
        adJiucuoTaskMapper.updateByPrimaryKeySelective(task);
    }
}
