package com.bt.om.vo.api;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * Created by caiting on 2018/1/25.
 */
public class JiucuoTaskListResultVo {
    private List<JiucuoTaskVo> jiucuo_submit;
    private List<JiucuoTaskVo> jiucuo_success;

    public JiucuoTaskListResultVo(){
        jiucuo_submit = Lists.newArrayList();
        jiucuo_success = Lists.newArrayList();
    }

    public List<JiucuoTaskVo> getJiucuo_submit() {
        return jiucuo_submit;
    }

    public void setJiucuo_submit(List<JiucuoTaskVo> jiucuo_submit) {
        this.jiucuo_submit = jiucuo_submit;
    }

    public List<JiucuoTaskVo> getJiucuo_success() {
        return jiucuo_success;
    }

    public void setJiucuo_success(List<JiucuoTaskVo> jiucuo_success) {
        this.jiucuo_success = jiucuo_success;
    }
}
