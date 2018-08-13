package com.bt.om.entity.vo;

import java.util.ArrayList;
import java.util.List;

import com.bt.om.entity.AdMonitorTaskFeedback;
import com.bt.om.entity.AdMonitorTaskFeedbackResources;

public class AdMonitorTaskFeedbackVo extends AdMonitorTaskFeedback{
	List<AdMonitorTaskFeedbackResources> pics = new ArrayList<>();

	public List<AdMonitorTaskFeedbackResources> getPics() {
		return pics;
	}

	public void setPics(List<AdMonitorTaskFeedbackResources> pics) {
		this.pics = pics;
	}
}
