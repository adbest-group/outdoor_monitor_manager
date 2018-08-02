package com.bt.om.web.quartz;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.audience.Audience;

import com.bt.om.entity.AdActivity;
import com.bt.om.entity.AdSystemPush;
import com.bt.om.service.IAdActivityService;
import com.bt.om.service.IAdSystemPushService;
import com.bt.om.web.util.JPushUtils;

/**
 * 结束活动定时
 * Created by jiayong.mao on 2018/4/26.
 */
public class ActivityEndTask extends AbstractTask {
	@Autowired
	private IAdActivityService adActivityService;
    @Autowired
    private IAdSystemPushService systemPushService;
    
	@Override
	protected boolean canProcess() {
		return true;
	}

	@Override
	protected void process() {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, -1);  
        Date date = calendar.getTime();
        Date now = new Date();
        adActivityService.updateStatusByEndTime(date);
        
        //==========活动结束之后根据活动创建者id进行app消息推送==============
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String single_schedule_time = formatter.format(new Date()) + " 09:00:00";  //早上9点进行推送
        List<AdActivity> activities = adActivityService.getEndActivityList(date);
        //所有当前日期下活动已经结束的活动创建者别名列表
        //用set存储避免一个用户创建多个活动，进行多次推送 
        Set<String> aliases = new HashSet<String>();
        for(AdActivity activity : activities) {
        	aliases.add(String.valueOf(activity.getUserId())); //把userId列表转成String类型，极光推送api需要
        	AdSystemPush push = new AdSystemPush();
        	push.setActivityName(activity.getActivityName());
        	push.setTitle("活动结束");
        	push.setUserId(activity.getUserId());
        	push.setContent("【"+activity.getActivityName()+"】活动已结束");
        	push.setCreateTime(now);
        	systemPushService.add(push);
        }
        Map<String, Object> param = new HashMap<>();
        Map<String, String> extras = new HashMap<>();
        extras.put("type", "end_activity_push");
        param.put("msg", "您创建的活动有新的结束通知！");
        param.put("title", "玖凤平台");
        param.put("alias", aliases);  //根据别名选择推送用户（这里userId用作推送时的用户别名）
        param.put("extras", extras);
        String pushResult = JPushUtils.singleSchedulePush(
        		single_schedule_time,   //定时时间
        		Platform.all(),        //推送平台
        		Audience.alias(aliases),              //推送目标
        		param);
        System.out.println("pushResult:: " + pushResult);
	}
}
