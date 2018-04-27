package com.bt.om.web.quartz;

import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import com.bt.om.service.IAdActivityService;

/**
 * 结束活动定时
 * Created by jiayong.mao on 2018/4/26.
 */
public class ActivityEndTask extends AbstractTask {
	@Autowired
	private IAdActivityService adActivityService;
	
	@Override
	protected boolean canProcess() {
		return true;
	}

	@Override
	protected void process() {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, -1);  
        Date date = calendar.getTime();
        
        adActivityService.updateStatusByEndTime(date);
	}
}
