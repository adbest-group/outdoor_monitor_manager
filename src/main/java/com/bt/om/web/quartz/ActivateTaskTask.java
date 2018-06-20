package com.bt.om.web.quartz;

import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;

import com.bt.om.service.IAdMonitorTaskService;

/**
 * 任务激活定时
 * Created by jiayong.mao on 2018/4/26.
 */
public class ActivateTaskTask extends AbstractTask {
	@Autowired
	private IAdMonitorTaskService adMonitorTaskService;
	
	@Override
	protected boolean canProcess() {
		return true;
	}

	@Override
	protected void process() {
        Date date = new Date();
        //老版本的激活任务定时: 有24小时的逻辑激活任务成"待指派"或"可抢单"
        //adMonitorTaskService.activateMonitorTask(date);
        
        //新版本的激活任务定时: 只有"待指派"
        adMonitorTaskService.newActivateMonitorTask(date);
	}
}
