package com.bt.om.vo.api;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * Created by caiting on 2018/1/26.
 */
public class RewardResultVo {
    private Integer total_reward;
    private List<RewardVo> detail;

    public RewardResultVo(){
        detail = Lists.newArrayList();
    }

    public Integer getTotal_reward() {
        return total_reward;
    }

    public void setTotal_reward(Integer total_reward) {
        this.total_reward = total_reward;
    }

    public List<RewardVo> getDetail() {
        return detail;
    }

    public void setDetail(List<RewardVo> detail) {
        this.detail = detail;
    }
}
