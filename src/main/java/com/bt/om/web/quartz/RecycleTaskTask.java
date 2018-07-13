package com.bt.om.web.quartz;

import org.springframework.beans.factory.annotation.Autowired;

import com.bt.om.service.IAdMonitorTaskService;

/**
 * 任务回收定时
 * Created by jiayong.mao on 2018/4/27.
 */
public class RecycleTaskTask extends AbstractTask {
	@Autowired
	private IAdMonitorTaskService adMonitorTaskService;
	
	@Override
	protected boolean canProcess() {
		return true;
	}

	@Override
	protected void process() {
		/**
		 * 指派的任务不进行回收操作
		 * 抢单的任务进行回收操作
		 * 判断任务结束时间, 由2：待执行 → 8：可抢单或1：待指派（12小时+24小时的限制条件）
		 */
		adMonitorTaskService.recycleMonitorTask();
		
		adMonitorTaskService.getTaskWillEnd(2);
	}
}
