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
		//操作不放在同一个事务中是因为本身就是独立的操作
		
		/**
		 * 指派的任务不进行回收操作
		 * 社会人员抢单的任务进行回收操作(由于暂时没用社会人员抢单, 所以功能注释掉)
		 * 判断任务结束时间, 由2：待执行 → 8：可抢单或1：待指派（12小时+24小时的限制条件）
		 */
		//adMonitorTaskService.recycleMonitorTask();
		
		/**
		 * 媒体监测人员抢单的任务进行回收操作
		 */
		adMonitorTaskService.recycleMediaMonitorTask();
		
		/**
		 * 任务主表超时 将状态改成"已超时"
		 */
		
		
		/**
		 * 即将结束的任务推送通知
		 */
		adMonitorTaskService.getTaskWillEnd(2);
	}
}
