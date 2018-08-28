package com.bt.om.web.quartz;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.bt.om.entity.HistoryAdActivity;
import com.bt.om.service.IAdActivityService;
import com.bt.om.service.IHistoryAdActivityService;
import com.bt.om.vo.web.SearchDataVo;

/**
 * 活动结束时间大于半年的数据迁移
 * Created by gaohao on 2018/8/1.
 */
public class ActivateDataTransferTask extends AbstractTask{
	
	@Autowired
	private IHistoryAdActivityService historyAdActivityService;
	@Autowired
	private IAdActivityService adActivityService;
	
	@Override
	protected boolean canProcess() {
		return true;
	}

	@Override
	protected void process() {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MONTH, -6);
		Date date = calendar.getTime();
		SearchDataVo vo = new SearchDataVo(null, null, 0, 0);
		vo.putSearchParam("endTime", null, date);
		List<HistoryAdActivity> adActivities = adActivityService.selectActivityAllByEndTime(vo.getSearchMap());
    	for (HistoryAdActivity adActivity : adActivities) {
    		try {
    			historyAdActivityService.batchDataTransfer(adActivity);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
