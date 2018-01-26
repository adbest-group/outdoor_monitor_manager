package com.bt.om.service;

import com.bt.om.entity.AdMonitorReward;
import com.bt.om.entity.AdMonitorTask;

import java.util.List;

/**
 * Created by caiting on 2018/1/26.
 */
public interface IAdMonitorRewardService {
    public Integer getTotalRewardByUserId(Integer userId);
    public List<AdMonitorReward> getByUserId(Integer userId);
}
