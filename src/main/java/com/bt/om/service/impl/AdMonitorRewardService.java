package com.bt.om.service.impl;

import com.bt.om.entity.AdMonitorReward;
import com.bt.om.entity.vo.AdActivityVo2;
import com.bt.om.mapper.AdMonitorRewardMapper;
import com.bt.om.service.IAdMonitorRewardService;
import com.bt.om.vo.web.SearchDataVo;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by caiting on 2018/1/26.
 */
@Service
public class AdMonitorRewardService implements IAdMonitorRewardService {
    @Autowired
    private AdMonitorRewardMapper adMonitorRewardMapper;

    @Override
    public Integer getTotalRewardByUserId(Integer userId) {
        return adMonitorRewardMapper.selectTotalRewardByUserId(userId);
    }

    @Override
    public List<AdMonitorReward> getByUserId(Integer userId) {
        return adMonitorRewardMapper.selectByUserId(userId);
    }

	@Override
	public void getPageData(SearchDataVo vo) {
		int count = adMonitorRewardMapper.getPageCount(vo.getSearchMap());
        vo.setCount(count);
        if (count > 0) {
            vo.setList(adMonitorRewardMapper.getPageData(vo.getSearchMap(), new RowBounds(vo.getStart(), vo.getSize())));
        } else {
            vo.setList(new ArrayList<AdActivityVo2>());
        }
	}
}
