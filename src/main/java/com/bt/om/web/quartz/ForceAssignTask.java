package com.bt.om.web.quartz;

import org.springframework.beans.factory.annotation.Autowired;

import com.bt.om.service.IAdMonitorTaskService;

/**
 * 强制指派定时
 * Created by jiayong.mao on 2018/4/27.
 */
public class ForceAssignTask extends AbstractTask {
	@Autowired
	private IAdMonitorTaskService adMonitorTaskService;
	
	@Override
	protected boolean canProcess() {
		return true;
	}

	@Override
	protected void process() {
		/**
		 * 抢单的任务进行强制指派操作, 因为一直没有人接单
		 * 判断任务结束时间, 由8：可抢单 → 1：待指派（12小时+24小时的限制条件）
		 */
		adMonitorTaskService.forceAssignTask();
	}
}