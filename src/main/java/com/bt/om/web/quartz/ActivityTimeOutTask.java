package com.bt.om.web.quartz;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import com.bt.om.service.IAdActivityService;
import com.bt.om.util.ConfigUtil;

/**
 * 任务激活定时
 * Created by jiayong.mao on 2018/4/26.
 */
public class ActivityTimeOutTask extends AbstractTask {
	@Autowired
	private IAdActivityService adActivityService;
	
	@Override
	protected boolean canProcess() {
		return true;
	}

	@Override
	protected void process() {
        Date date = new Date();
        Integer upTime = ConfigUtil.getInt("up_time"); //允许活动提前多少天确认, 比如提前5天还未确认的设置活动为"超时未确认"
        Long deadLine = date.getTime() + upTime*24*60*60*1000;
		Date deadLineTime = new Date(deadLine);
		
		//更新活动开始时间晚于deadLineTime的活动状态为"超时未确认"
		adActivityService.deadLineAuditActivity(deadLineTime);
	}
}
