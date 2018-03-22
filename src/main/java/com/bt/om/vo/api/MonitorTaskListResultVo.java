package com.bt.om.vo.api;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by caiting on 2018/1/24.
 */
public class MonitorTaskListResultVo implements Serializable {
    List<MonitorTaskWaitToExecutedVo> wait_to_executed;
    List<MonitorTaskUnFinishedVo> un_finished;
    List<MonitorTaskExecutingVo> executing;
    List<MonitorTaskCheckedVo> checked;

    public MonitorTaskListResultVo(){
        this.wait_to_executed = new ArrayList<>();
        this.executing = new ArrayList<>();
        this.checked = new ArrayList<>();
        this.un_finished = new ArrayList<>();
    }

    public List<MonitorTaskUnFinishedVo> getUn_finished() {
        return un_finished;
    }

    public void setUn_finished(List<MonitorTaskUnFinishedVo> un_finished) {
        this.un_finished = un_finished;
    }

    public List<MonitorTaskWaitToExecutedVo> getWait_to_executed() {
        return wait_to_executed;
    }

    public void setWait_to_executed(List<MonitorTaskWaitToExecutedVo> wait_to_executed) {
        this.wait_to_executed = wait_to_executed;
    }

    public List<MonitorTaskExecutingVo> getExecuting() {
        return executing;
    }

    public void setExecuting(List<MonitorTaskExecutingVo> executing) {
        this.executing = executing;
    }

    public List<MonitorTaskCheckedVo> getChecked() {
        return checked;
    }

    public void setChecked(List<MonitorTaskCheckedVo> checked) {
        this.checked = checked;
    }
}
