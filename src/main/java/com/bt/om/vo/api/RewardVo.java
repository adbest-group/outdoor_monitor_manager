package com.bt.om.vo.api;

import com.bt.om.entity.AdMonitorReward;
import com.fasterxml.jackson.databind.ser.Serializers;

/**
 * Created by caiting on 2018/1/26.
 */
public class RewardVo extends BasicVo{
    private String date;
    private String time;
    private Integer reward;

    public RewardVo(AdMonitorReward reward){
        this.date = sdf.format(reward.getCreateTime());
        this.time = timeSdf.format(reward.getCreateTime());
        this.reward = reward.getRewardPoints();
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Integer getReward() {
        return reward;
    }

    public void setReward(Integer reward) {
        this.reward = reward;
    }
}
