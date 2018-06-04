package com.bt.om.web.util;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Message;
import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.AndroidNotification;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;
import cn.jpush.api.schedule.ScheduleResult;

import com.bt.om.common.JPushConst;

/**
 * @Description: “极光推送”工具类
 * @author sjh
 * @date: 2018年5月28日 下午6:03:12
 */
public class JPushUtils {
	
	protected static final Logger logger = Logger.getLogger(JPushUtils.class);
	
	private static final String appKey = JPushConst.APP_KEY;
	private static final String masterSecret = JPushConst.MASTER_SECRET;
	
	/**
	 * 推送给指定平台的指定用户
	 * @param platform：指定平台
	 * @param audience：指定用户
	 * @param param
	 * ---------------
	 * 	key   |  value
	 * ---------------
	 * 	msg	  |  手机通知栏显示内容
	 * title  |  安卓手机通知栏显示标题
	 * extras |  前端业务使用数据(Java中数据格式为Map)
	 * 
	 *  例：
	 *  Map<String, Object> param = new HashMap();
	 *	Map<String, String> extras = new HashMap();
	 *	extras.put("id", "123456");
	 *	extras.put("name", "张三");
	 *	param.put("msg", "推送测试！");
	 *	param.put("title", "玖凤平台");
	 *	param.put("extras", extras);
	 */
	public static String push(Platform platform, Audience audience, Map<String, Object> param) {
		String result = "0"; //推送结果：0：失败，1：成功
		//创建JPushClient实例
		JPushClient jPushClient = new JPushClient(masterSecret, appKey);
		//创建PushPayload实例，生成推送的内容
		PushPayload payload = PushPayload.newBuilder()
	            .setPlatform(platform)  //指定推送平台
	            .setAudience(audience) //指定用户
	            .setNotification(Notification.newBuilder()   //设置推送通知
	                    .addPlatformNotification(IosNotification.newBuilder()  //指定苹果平台的通知
	                            .setAlert(param.get("msg"))  //手机通知栏显示内容
	                            .setBadge(+1)           //应用角标
	                            .setSound("happy")      //通知声音
	                            .addExtras((Map)param.get("extras"))       //此字段为透传字段，不会显示在通知栏。用户可以通过此字段来做一些定制需求，如特定的key传要指定跳转的页面（value）
	                            .build())
	                    .addPlatformNotification(AndroidNotification.newBuilder()//指定安卓平台的通知
	                    		.setTitle((String)param.get("title"))  //设置标题
	                            .addExtras((Map)param.get("extras"))
	                            .setAlert(param.get("msg"))
	                            .build())
	                    .build())
	            .setOptions(Options.newBuilder()
	            		//以秒为单位,此处设置为1周
	            		.setTimeToLive(604800)//推送当前用户不在线时，为该用户保留多长时间的离线消息，以便其上线时再次推送。默认 86400 （1 天），最长 10 天。设置为 0 表示不保留离线消息，只有推送当前在线的用户可以收到。
	            		.setApnsProduction(true)//指定生产环境(True 表示推送生产环境，False 表示要推送开发环境)
	            		.build())
	            .setMessage(Message.newBuilder().setMsgContent((String)param.get("msg")).addExtras((Map)param.get("extras")).build())//自定义信息
	            .build();
		
		 try {
			PushResult pu = jPushClient.sendPush(payload);
			result = pu.getResponseCode() == 200 ? "1" : "0"; //响应码200表示推送成功
			return result;
		} catch (Exception e) {
			logger.error("app消息推送出现异常: " + e.getMessage());
//			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 极光推送定时任务
	 * @param single_schedule_time : 定时时间
	 * @param platform
	 * @param audience
	 * @param param
	 * @return
	 */
	public static String singleSchedulePush(String single_schedule_time, Platform platform, Audience audience, Map<String, Object> param) {
        JPushClient jpushClient = new JPushClient(masterSecret, appKey);
        String name = "single_schedule_push"; 
        String time = single_schedule_time; //定时时间
        String result = "0"; //推送结果：0：失败，1：成功
		//创建JPushClient实例
		JPushClient jPushClient = new JPushClient(masterSecret, appKey);
		//创建PushPayload实例，生成推送的内容
		PushPayload payload = PushPayload.newBuilder()
	            .setPlatform(platform)  //指定推送平台
	            .setAudience(audience) //指定用户
	            .setNotification(Notification.newBuilder()   //设置推送通知
	                    .addPlatformNotification(IosNotification.newBuilder()  //指定苹果平台的通知
	                            .setAlert(param.get("msg"))  //手机通知栏显示内容
	                            .setBadge(+1)           //应用角标
	                            .setSound("happy")      //通知声音
	                            .addExtras((Map)param.get("extras"))       //此字段为透传字段，不会显示在通知栏。用户可以通过此字段来做一些定制需求，如特定的key传要指定跳转的页面（value）
	                            .build())
	                    .addPlatformNotification(AndroidNotification.newBuilder()//指定安卓平台的通知
	                    		.setTitle((String)param.get("title"))  //设置标题
	                            .addExtras((Map)param.get("extras"))
	                            .setAlert(param.get("msg"))
	                            .build())
	                    .build())
	            .setOptions(Options.newBuilder()
	            		//以秒为单位,此处设置为1周
	            		.setTimeToLive(604800)//推送当前用户不在线时，为该用户保留多长时间的离线消息，以便其上线时再次推送。默认 86400 （1 天），最长 10 天。设置为 0 表示不保留离线消息，只有推送当前在线的用户可以收到。
	            		.setApnsProduction(true)//指定生产环境(True 表示推送生产环境，False 表示要推送开发环境)
	            		.build())
	            .setMessage(Message.newBuilder().setMsgContent((String)param.get("msg")).addExtras((Map)param.get("extras")).build())//自定义信息
	            .build();
        try {
            ScheduleResult sr = jpushClient.createSingleSchedule(name, time, payload);
            result = sr.getResponseCode() == 200 ? "1" : "0"; //响应码200表示推送成功
			return result;
        } catch (Exception e) {
        	logger.error("app定时消息推送出现异常: " + e.getMessage());
        }
        return result;
    }
	/**
	 * 推送给所有平台的所有用户
	 */
	public static String pushAll(Map<String, Object> param) {
		return push(Platform.all(), Audience.all(), param);
	}
	/**
	 * 推送给所有平台的标签用户(只要在任何一个标签范围内都满足)
	 * ---------------
	 * 	key   |  value
	 * ---------------
	 * tags   |  标签,用于指定用户（Java中数据格式为String数组）
	 * 
	 * 例：
	 * "tag" : [ "深圳", "广州", "北京" ]   --》    在深圳、广州、或者北京
	 */
	public static String pushAllByTag(Map<String, Object> param) {
		return push(Platform.all(), Audience.tag((Collection)param.get("tags")), param);
	}
	/**
	 * 推送给所有平台的标签用户(需要同时在多个标签范围内)
	 * ---------------
	 * 	key   |  value
	 * ---------------
	 *tag_and |  标签,用于指定用户（Java中数据格式为String数组）
	 * 
	 * 例：
	 * "tag_and" : [ "深圳", "女" ]   --》   在深圳并且是“女”
	 */
	public static String pushAllByTagAnd(Map<String, Object> param) {
		return push(Platform.all(), Audience.tag_and((Collection)param.get("tag_and")), param);
	}
	/**
	 * 推送给所有平台的别名用户
	 * ---------------
	 * 	key   |  value
	 * ---------------
	 * alias  |  别名,用于指定用户（Java中数据格式为String数组）
	 */
	public static String pushAllByAlias(Map<String, Object> param) {
		return push(Platform.all(), Audience.alias((Collection)param.get("alias")), param);
	}
	/**
	 * 推送给所有平台的registrationId用户
	 * ---------------
	 * 	key   |  value
	 * ---------------
	 * registration_id  |  registrationId,用于指定用户（Java中数据格式为String数组）
	 */
	public static String pushAllByRegistrationId(Map<String, Object> param) {
		return push(Platform.all(), Audience.registrationId((Collection)param.get("registration_id")), param);
	}
	
	
	/**
	 * 推送给安卓平台的所有用户
	 */
	public static String pushAndroid(Map<String, Object> param) {
		return push(Platform.android(), Audience.all(), param);
	}
	/**
	 * 推送给安卓平台的标签用户(只要在任何一个标签范围内都满足)
	 */
	public static String pushAndroidByTag(Map<String, Object> param) {
		return push(Platform.android(), Audience.tag((Collection)param.get("tags")), param);
	}
	/**
	 * 推送给安卓平台的标签用户(需要同时在多个标签范围内)
	 */
	public static String pushAndroidByTagAnd(Map<String, Object> param) {
		return push(Platform.android(), Audience.tag_and((Collection)param.get("tag_and")), param);
	}
	/**
	 * 推送给安卓平台的别名用户
	 */
	public static String pushAndroidByAlias(Map<String, Object> param) {
		return push(Platform.android(), Audience.alias((Collection)param.get("alias")), param);
	}
	/**
	 * 推送给安卓平台的registrationId用户
	 */
	public static String pushAndroidByRegistrationId(Map<String, Object> param) {
		return push(Platform.android(), Audience.registrationId((Collection)param.get("registration_id")), param);
	}
	
	
	/**
	 * 推送给IOS平台的所有用户
	 */
	public static String pushIOS(Map<String, Object> param) {
		return push(Platform.ios(), Audience.all(), param);
	}
	/**
	 * 推送给IOS平台的标签用户(只要在任何一个标签范围内都满足)
	 */
	public static String pushIOSByTags(Map<String, Object> param) {
		return push(Platform.ios(), Audience.tag((Collection)param.get("tags")), param);
	}
	/**
	 * 推送给IOS平台的标签用户(需要同时在多个标签范围内)
	 */
	public static String pushIOSByTagAnd(Map<String, Object> param) {
		return push(Platform.ios(), Audience.tag_and((Collection)param.get("tag_and")), param);
	}
	/**
	 * 推送给IOS平台的别名用户
	 */
	public static String pushIOSByAlias(Map<String, Object> param) {
		return push(Platform.ios(), Audience.alias((Collection)param.get("alias")), param);
	}
	/**
	 * 推送给IOS平台的registrationId用户
	 */
	public static String pushIOSByRegistrationId(Map<String, Object> param) {
		return push(Platform.ios(), Audience.registrationId((Collection)param.get("registration_id")), param);
	}
}
