package com.bt.om.service.impl;

import com.bt.om.entity.AdMonitorReward;
import com.bt.om.mapper.AdMonitorRewardMapper;
import com.bt.om.service.IAdMonitorRewardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
