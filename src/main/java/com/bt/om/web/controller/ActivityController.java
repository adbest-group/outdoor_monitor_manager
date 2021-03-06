package com.bt.om.web.controller;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.adtime.common.lang.StringUtil;
import com.bt.om.common.SysConst;
import com.bt.om.common.web.PageConst;
import com.bt.om.entity.AdActivity;
import com.bt.om.entity.AdActivityAdseat;
import com.bt.om.entity.AdMonitorTask;
import com.bt.om.entity.AdSystemPush;
import com.bt.om.entity.SysUser;
import com.bt.om.entity.TaskDownload;
import com.bt.om.entity.vo.AdActivityAdseatVo;
import com.bt.om.entity.vo.AdActivityVo;
import com.bt.om.enums.MonitorTaskStatus;
import com.bt.om.enums.MonitorTaskType;
import com.bt.om.enums.ResultCode;
import com.bt.om.enums.SessionKey;
import com.bt.om.enums.UserTypeEnum;
import com.bt.om.filter.LogFilter;
import com.bt.om.mapper.SysUserResMapper;
import com.bt.om.security.ShiroUtils;
import com.bt.om.service.IAdActivityService;
import com.bt.om.service.IAdMonitorTaskService;
import com.bt.om.service.IAdSystemPushService;
import com.bt.om.service.IAdUserMessageService;
import com.bt.om.service.IOperateLogService;
import com.bt.om.service.ISysGroupService;
import com.bt.om.service.ISysResourcesService;
import com.bt.om.service.ISysUserRoleService;
import com.bt.om.service.ISysUserService;
import com.bt.om.util.ConfigUtil;
import com.bt.om.util.GsonUtil;
import com.bt.om.util.QRcodeUtil;
import com.bt.om.vo.api.AdActivitySeatInfoInQRVO;
import com.bt.om.vo.web.ResultVo;
import com.bt.om.vo.web.SearchDataVo;
import com.bt.om.web.BasicController;
import com.bt.om.web.util.JPushUtils;
import com.bt.om.web.util.SearchUtil;

/**
 * Created by caiting on 2018/1/20.
 */
@Controller
@RequestMapping(value = "/activity")
public class ActivityController extends BasicController {

	@Autowired
	private IAdActivityService adActivityService;
	@Autowired
	private ISysGroupService sysGroupService;
	@Autowired
	private ISysUserService sysUserService;
	@Autowired
	private ISysResourcesService sysResourcesService;
	@Autowired
	private ISysUserRoleService sysUserRoleService;
	@Autowired
	private IOperateLogService operateLogService;
	@Autowired
	private SysUserResMapper sysUserResMapper;
	@Autowired
	private IAdUserMessageService adUserMessageService;
	@Autowired
	private IAdMonitorTaskService adMonitorTaskService;
    @Autowired
    private IAdSystemPushService systemPushService;
	@Autowired
	protected RedisTemplate redisTemplate;
	private static final Logger logger = Logger.getLogger(ActivityController.class);
	private String file_upload_path = ConfigUtil.getString("file.upload.path");
	// 活动审核人员查看活动列表
	@RequiresRoles("activityadmin")
	@RequestMapping(value = "/list")
	public String customerList(Model model, HttpServletRequest request,
			@RequestParam(value = "activityId", required = false) Integer activityId,
			@RequestParam(value = "status", required = false) Integer status,
			@RequestParam(value = "startDate", required = false) String startDate,
			@RequestParam(value = "endDate", required = false) String endDate,
			@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "mediaTypeId", required = false) Integer mediaTypeId,
            @RequestParam(value = "mediaTypeParentId", required = false) Integer mediaTypeParentId,
            @RequestParam(value = "province", required = false) String province,
            @RequestParam(value = "city", required = false) String city,
            @RequestParam(value = "userId", required = false) Integer userId) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		SearchDataVo vo = SearchUtil.getVo();
		// Integer shenheCount = 0;

		// 获取登录的审核员工activityadmin
		SysUser userObj = (SysUser) ShiroUtils.getSessionAttribute(SessionKey.SESSION_LOGIN_USER.toString());

		if(userId != null) {
        	vo.putSearchParam("userId", userId.toString(), userId);
        }
		
		if (activityId != null) {
			vo.putSearchParam("activityId", activityId.toString(), activityId);
			//hhjskjskjsk
		}

		if (status != null) {
			// status = 1;
			vo.putSearchParam("status", status.toString(), status);
			model.addAttribute("status", status);
		} // 如果没有传参status, 默认取1：未确认

		if (startDate != null) {
			try {
				vo.putSearchParam("startDate", startDate, sdf.parse(startDate));
			} catch (ParseException e) {
				logger.error(e);
			}
		}
		if (endDate != null) {
			try {
				vo.putSearchParam("endDate", endDate, sdf.parse(endDate));
			} catch (ParseException e) {
				logger.error(e);
			}
		}
		//查询活动名称
        if (name != null) {
        	name = "%" + name + "%";
            vo.putSearchParam("activityName", name, name);
        }
        //媒体大类
        if (mediaTypeParentId != null) {
            vo.putSearchParam("mediaTypeParentId", mediaTypeParentId.toString(), mediaTypeParentId);
        }
        //媒体小类
        if (mediaTypeId != null) {
        	vo.putSearchParam("mediaTypeId", mediaTypeId.toString(), mediaTypeId);
        }
        //省
        if (province != null) {
        	vo.putSearchParam("province", province.toString(), province);
        }
        //城市
        if (city != null) {
            vo.putSearchParam("city", city.toString(), city);
        }
		// //只能查询自己参与的活动审核
		// if(userObj != null) {
		// Integer assessorId = userObj.getId();
		// vo.putSearchParam("assessorId", assessorId.toString(), assessorId);
		// }
		List<Integer> customerIds = sysUserService.getCustomerIdsByAdminId(userObj.getId()); // 根据员工id查询所属组对应的所有广告商id集合
		if (customerIds != null && customerIds.size() == 0) {
			// 员工对应的广告商id集合为空, 不需要再去查询活动列表
			vo.setCount(0);
			vo.setSize(20);
			vo.setStart(0);
			vo.setList(null);
		} else {
			vo.putSearchParam("customerIds", null, customerIds);
			adActivityService.getPageData(vo);
		}

		// //所有未确认的活动
		// Map<String, Object> searchMap1 = new HashMap<>();
		// searchMap1.put("customerIds", customerIds);
		// List<AdActivity> allActivityUncertain =
		// adActivityService.getAllByStatusUncertain(searchMap1);
		//
		// shenheCount = allActivityUncertain.size() - vo.getList().size();
		// if(shenheCount < 0) {
		// shenheCount = 0;
		// }
		// model.addAttribute("shenheCount", shenheCount);

		// if(status == 2 || status == 3) {
		// //查询已确认 或 已结束 的活动
		// adActivityService.getPageData(vo);
		// } else {
		// //查询未确认的活动
		// //[1] 先查询审核id的所有未确认活动
		// Map<String, Object> searchMap = new HashMap<>();
		// searchMap.put("status", 1);
		// searchMap.put("assessorId", userObj.getId());
		//// searchMap.put("activityId", activityId);
		//// searchMap.put("startDate", startDate);
		//// searchMap.put("endDate", endDate);
		// List<AdActivity> activities =
		// adActivityService.selectAllByAssessorId(searchMap);//通过所有审核员id获取的所有活动
		//
		// if(activities != null && activities.size() > 0) {
		// //条数大于0, 返回给页面
		// Iterator<AdActivity> iterator = activities.iterator();
		// while(iterator.hasNext()) {
		// boolean remove = false; //不用抹去
		// AdActivity adActivity = iterator.next();
		// //通过页面上的activityId做筛选
		// if(activityId != null) {
		// if(adActivity.getId() != activityId) {
		// remove = true;
		// }
		// }
		// //通过页面上的startDate做筛选
		// if(StringUtil.isNotBlank(startDate)) {
		// if(adActivity.getStartTime().getTime() < sdf.parse(startDate).getTime()) {
		// remove = true;
		// }
		// }
		// //通过页面上的endDate做筛选
		// if(StringUtil.isNotBlank(endDate)) {
		// if(adActivity.getEndTime().getTime() > sdf.parse(endDate).getTime()) {
		// remove = true;
		// }
		// }
		// if(remove == true) {
		// iterator.remove();
		// }
		// }
		// vo.setCount(activities.size());
		// vo.setSize(20);
		// vo.setStart(0);
		// vo.setList(activities);
		// shenheCount = allActivityUncertain.size() - activities.size();
		// if(shenheCount < 0) {
		// shenheCount = 0;
		// }
		// model.addAttribute("shenheCount", shenheCount);
		// } else {
		// //条数等于0, 新查询1条或者0条没人认领的未确认活动(需要匹配 员工 - 组 - 广告商 之间的关系)
		// if(customerIds != null && customerIds.size() > 0) {
		// searchMap.clear();
		// searchMap.put("status", 1);
		// searchMap.put("customerIds", customerIds);
		// searchMap.put("assessorId", userObj.getId());
		// List<AdActivity> atimeActivity =
		// adActivityService.getAtimeActivity(searchMap);//获取一次未确认活动
		// vo.setCount(atimeActivity.size());
		// vo.setSize(20);
		// vo.setStart(0);
		// vo.setList(atimeActivity);
		// shenheCount = allActivityUncertain.size() - atimeActivity.size();
		// if(shenheCount < 0) {
		// shenheCount = 0;
		// }
		// model.addAttribute("shenheCount", shenheCount);
		// }
		// }
		// }

		SearchUtil.putToModel(model, vo);

		return PageConst.ACTIVITY_LIST;
	}

	// 前往编辑活动
    @RequiresRoles(value = {"activityadmin", "depactivityadmin", "superadmin", "customer" ,"phoneoperator"}, logical = Logical.OR)
    @RequestMapping(value = "/edit")
    public String customerEdit(Model model, HttpServletRequest request,
                               @RequestParam(value = "id", required = false) Integer id) {
    	AdActivityVo activity = adActivityService.getVoById(id);

        //获取登录用户信息
        SysUser user = (SysUser) ShiroUtils.getSessionAttribute(SessionKey.SESSION_LOGIN_USER.toString());
        
        if(user != null) {
        	model.addAttribute("usertype", user.getUsertype());
        	if (user.getUsertype() == UserTypeEnum.CUSTOMER.getId() && user.getId().intValue() != activity.getUserId().intValue()) {
        		return PageConst.NO_AUTHORITY;
			}
        }
        
        if (activity != null) {
            model.addAttribute("activity", activity);
        }
        
        Integer monitorTime = ConfigUtil.getInt("monitor_time"); //允许任务执行天数
        Integer auditTime = ConfigUtil.getInt("audit_time"); //允许任务审核天数
        
        model.addAttribute("monitorTime", monitorTime);
        model.addAttribute("auditTime", auditTime);
        model.addAttribute("user" , user);
        return PageConst.ACTIVITY_EDIT;
    }

    // 追加监测任务页面跳转
    @RequestMapping(value = "/addTask")
    public String gotoAddPage(Model model, Integer seatId, String startTime, String endTime, String activityId) {
        model.addAttribute("monitorTime", ConfigUtil.getInt("monitor_time")); //允许任务执行天数
        model.addAttribute("auditTime", ConfigUtil.getInt("audit_time")); //允许任务审核天数
    	model.addAttribute("seatId", seatId);
    	model.addAttribute("startTime", startTime);
    	model.addAttribute("endTime", endTime);
    	model.addAttribute("activityId", activityId);
        return PageConst.ADD_ZHUIJIA;
    }
    
    // 【已确认但未结束的活动】追加监测任务
 	@RequestMapping(value = "/zhuijiaTask")
 	@ResponseBody
 	public Model zhuijiaTask(Model model, HttpServletRequest request,
 			@RequestParam(value = "activityId", required = false) Integer activityId,
 			@RequestParam(value = "seatIds", required = false) String seatIds,
 			@RequestParam(value = "reportTime", required = false) String reportTime,
 			@RequestParam(value = "zhuijiaMonitorTaskPoint", required = false) Integer zhuijiaMonitorTaskPoint,
 			@RequestParam(value = "zhuijiaMonitorTaskMoney", required = false) double zhuijiaMonitorTaskMoney) {
 		ResultVo<String> result = new ResultVo<String>();
 		result.setCode(ResultCode.RESULT_SUCCESS.getCode());
 		result.setResultDes("确认成功");
 		model = new ExtendedModelMap();
 		
 		if(activityId == null || StringUtil.isBlank(reportTime) || StringUtil.isBlank(seatIds)) {
 			result.setCode(ResultCode.RESULT_FAILURE.getCode());
 			result.setResultDes("添加失败！");
 			model.addAttribute(SysConst.RESULT_KEY, result);
 			return model;
 		}
 		
 		try {
 			String[] splitSeatIds = seatIds.split(",");
 			//批量插入
			adMonitorTaskService.insertMonitorTask(activityId, Arrays.asList(splitSeatIds), reportTime ,zhuijiaMonitorTaskPoint,zhuijiaMonitorTaskMoney);
 		} catch (Exception e) {
 			logger.error(e);
 			result.setCode(ResultCode.RESULT_FAILURE.getCode());
 			result.setResultDes("确认失败！");
 			model.addAttribute(SysConst.RESULT_KEY, result);
 			return model;
 		}

 		model.addAttribute(SysConst.RESULT_KEY, result);
 		return model;
 	}
    
	// 确认活动
	@RequiresRoles(value = { "activityadmin", "depactivityadmin", "superadmin" }, logical = Logical.OR)
	@RequestMapping(value = "/confirm")
	@ResponseBody
	public Model confirm(Model model, HttpServletRequest request,
			@RequestParam(value = "ids", required = false) String ids,
			@RequestParam(value = "userId", required = false) Integer userId) {
		ResultVo<String> result = new ResultVo<String>();
		Date now = new Date();
		// [1] ids拆分成id集合
		String[] activityIds = ids.split(",");
		// [2] 循环判断每一个id是否已经在redis中. 存在一个即返回错误信息
		for (String actId : activityIds) {
			String beginRedisStr = "activity_" + actId + "_begin";
			String finishRedisStr = "activity_" + actId + "_finish";
			if (redisTemplate.opsForValue().get(finishRedisStr) != null
					&& StringUtil.equals(redisTemplate.opsForValue().get(finishRedisStr) + "", "true")) {
				result.setCode(ResultCode.RESULT_FAILURE.getCode());
				result.setResultDes("活动已被确认，请刷新再试！");
				model.addAttribute(SysConst.RESULT_KEY, result);
				return model;
			}
			if (redisTemplate.opsForValue().get(beginRedisStr) != null
					&& StringUtil.equals(redisTemplate.opsForValue().get(beginRedisStr) + "", "true")) {
				result.setCode(ResultCode.RESULT_FAILURE.getCode());
				result.setResultDes("活动正被确认中，请刷新再试！");
				model.addAttribute(SysConst.RESULT_KEY, result);
				return model;
			}
		}

		// [3] 循环放入redis中
		for (String actId : activityIds) {
			String beginRedisStr = "activity_" + actId + "_begin";
			// 放入Redis缓存处理并发
			redisTemplate.opsForValue().set(beginRedisStr, "true", 60 * 30, TimeUnit.SECONDS); //设置半小时超时时间
		}

		result.setCode(ResultCode.RESULT_SUCCESS.getCode());
		result.setResultDes("确认成功");
		model = new ExtendedModelMap();
		
		try {
			// 获取登录的审核人(员工/部门领导/超级管理员)
			SysUser userObj = (SysUser) ShiroUtils.getSessionAttribute(SessionKey.SESSION_LOGIN_USER.toString());

			// 确认活动
			adActivityService.confirm(activityIds, userObj.getId());

			// 循环推送消息
			for (String actId : activityIds) {
				AdActivity adActivity = adActivityService.getById(Integer.parseInt(actId));
				// ==========web端活动审核成功之后根据活动创建者id进行app消息推送==============
				Map<String, Object> param = new HashMap<>();
				Map<String, String> extras = new HashMap<>();
				List<String> alias = new ArrayList<>(); // 别名用户List
				alias.add(String.valueOf(adActivity.getUserId())); // 活动创建者
				extras.put("type", "activity_confirm_push");
				param.put("msg", "您创建的活动有一条新的通知！");
				param.put("title", "玖凤平台");
				param.put("alias", alias); // 根据别名选择推送用户（这里userId用作推送时的用户别名）
				param.put("extras", extras);
				String pushResult = JPushUtils.pushAllByAlias(param);
				System.out.println("pushResult:: " + pushResult);
				
				AdSystemPush push = new AdSystemPush();
				push.setUserId(adActivity.getUserId());
				push.setActivityName(adActivity.getActivityName());
				push.setTitle("活动确认");
				push.setContent("您创建的【"+adActivity.getActivityName()+"】活动已被确认");
				push.setCreateTime(now);
				systemPushService.add(push);
			}
		} catch (Exception e) {
			logger.error(e);
			// [5] 异常情况, 循环删除redis
			for (String actId : activityIds) {
				String beginRedisStr = "activity_" + actId + "_begin";
				// 异常情况, 移除Redis缓存处理并发
				redisTemplate.delete(beginRedisStr);
			}

			result.setCode(ResultCode.RESULT_FAILURE.getCode());
			result.setResultDes("确认失败！");
			model.addAttribute(SysConst.RESULT_KEY, result);
			return model;
		}

		// [6] 处理成功, 循环放入redis
		for (String actId : activityIds) {
			// 放入Redis缓存处理并发
			String finishRedisStr = "activity_" + actId + "_finish";
			redisTemplate.opsForValue().set(finishRedisStr, "true", 60*30, TimeUnit.SECONDS); //设置半小时超时时间
		}
		model.addAttribute(SysConst.RESULT_KEY, result);
		return model;
	}

	// 撤销活动
	@RequiresRoles("activityadmin")
	@RequestMapping(value = "/cancel")
	@ResponseBody
	public Model cancel(Model model, HttpServletRequest request,
			@RequestParam(value = "id", required = false) Integer id,
			@RequestParam(value = "userId", required = false) Integer userId) {
		ResultVo<String> result = new ResultVo<String>();
		result.setCode(ResultCode.RESULT_SUCCESS.getCode());
		result.setResultDes("撤销成功");
		model = new ExtendedModelMap();

		try {
			// 获取当前登录的后台用户信息
			SysUser sysUser = (SysUser) ShiroUtils.getSessionAttribute(SessionKey.SESSION_LOGIN_USER.toString());
			Map<String, Object> searchMap = new HashMap<>();

			searchMap.put("userId", sysUser.getId());
			Integer groupId = sysUserRoleService.selectGroupIdByUserId(searchMap);
			/*
			 * //获取当前登录的后台用户的所属组信息 SysResources group = sysGroupService.getById(groupId);
			 */
			// 获取该组所有员工
			List<SysUser> sysUsers = sysGroupService.selectUserName(groupId);

			if (sysUsers.size() > 1) {
				adActivityService.offActivityByAssessorId(id);
			} else if (sysUsers.size() <= 1) {
				result.setCode(ResultCode.RESULT_FAILURE.getCode());
				result.setResultDes("只剩一人不能撤销！");
				model.addAttribute(SysConst.RESULT_KEY, result);
				return model;
			}
		} catch (Exception e) {
			logger.error(e);
			result.setCode(ResultCode.RESULT_FAILURE.getCode());
			result.setResultDes("撤销失败！");
			model.addAttribute(SysConst.RESULT_KEY, result);
			return model;
		}
		model.addAttribute(SysConst.RESULT_KEY, result);
		return model;
	}

	// 删除活动
	@RequiresRoles(value = { "admin", "customer", "activityadmin", "depactivityadmin",
			"superadmin" }, logical = Logical.OR)
	@RequestMapping(value = "/delete")
	@ResponseBody
	public Model delete(Model model, HttpServletRequest request,
			@RequestParam(value = "id", required = false) Integer id) {
		ResultVo<String> result = new ResultVo<String>();
		result.setCode(ResultCode.RESULT_SUCCESS.getCode());
		result.setResultDes("删除成功");
		model = new ExtendedModelMap();
		Date now = new Date();

		// 获取登录的审核人(员工/部门领导/超级管理员)
		SysUser userObj = (SysUser) ShiroUtils.getSessionAttribute(SessionKey.SESSION_LOGIN_USER.toString());
		
		try {
			// 删除活动
			adActivityService.delete(id, userObj.getId());
		} catch (Exception e) {
			logger.error(e);
			result.setCode(ResultCode.RESULT_FAILURE.getCode());
			result.setResultDes("删除失败！");
			model.addAttribute(SysConst.RESULT_KEY, result);
			return model;
		}
		model.addAttribute(SysConst.RESULT_KEY, result);
		return model;
	}

	/**
	 * 根据活动广告位关联id，获取二维码
	 **/
	@RequestMapping(value = { "/getQrcode" }, method = RequestMethod.GET)
	public void getCode(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "id", required = false) Integer adActivityAdseatId) throws Exception {

		AdActivitySeatInfoInQRVO vo = new AdActivitySeatInfoInQRVO(
				(AdActivityAdseatVo) adActivityService.getActivitySeatById(adActivityAdseatId));

		// 将内存中的图片发送到客户端
		response.setContentType("image/jpg");
		QRcodeUtil.encode(GsonUtil.GsonString(vo), response.getOutputStream());
	}
	
	//通过ActivityId查出所有任务列表
	@RequestMapping(value="/selectTaskToExcel")
	public String getActivity(Model model,HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "activityId", required = false) Integer activityId) {
		SysUser userObj = (SysUser) ShiroUtils.getSessionAttribute(SessionKey.SESSION_LOGIN_USER.toString());
		Date now = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		//查询所有任务列表
		List<AdMonitorTask> list = adMonitorTaskService.getAllTasksByActivityId(activityId);
		//所有投放期间监测任务列表
		List<AdMonitorTask> durationList = new ArrayList<>();
		//所有追加监测列表
		List<AdMonitorTask> zhuijiaList = new ArrayList<>();
		for(AdMonitorTask task : list) {
			StringBuffer stringBuffer = new StringBuffer();
			if(now.compareTo(task.getReportTime())>0) {	//当前时间大于出报告时间
				String nowDate = sdf.format(task.getReportTime());
				//if((userObj.getUsertype()==2 && task.getStatus()==4) || (userObj.getUsertype()!=2)) {	//登录用户是广告主且通过审核  或者登录用户是群邑
					if(task.getTaskType()==MonitorTaskType.UP_TASK.getId()) {	//5 上刊任务
						TaskDownload upTask = new TaskDownload();
						stringBuffer.append(nowDate);
						stringBuffer.append("	");
						stringBuffer.append(MonitorTaskType.UP_TASK.getText());
						stringBuffer.append("报告");
						upTask.setKey(stringBuffer.toString());
						upTask.setValue(task.getActivityId());
						model.addAttribute("upTask_show",upTask);
					}else if(task.getTaskType()==MonitorTaskType.UP_MONITOR.getId()) {	//1 上刊监测
						TaskDownload upMonitor = new TaskDownload();
						stringBuffer.append(nowDate);
						stringBuffer.append("	");
						stringBuffer.append(MonitorTaskType.UP_MONITOR.getText());
						stringBuffer.append("报告");
						upMonitor.setKey(stringBuffer.toString());
						upMonitor.setValue(task.getActivityId());
						model.addAttribute("upMonitor_show",upMonitor);
					}else if(task.getTaskType()==MonitorTaskType.DOWNMONITOR.getId()) {	//3 下刊监测
						TaskDownload downMonitor = new TaskDownload();
						stringBuffer.append(nowDate);
						stringBuffer.append("	");
						stringBuffer.append(MonitorTaskType.DOWNMONITOR.getText());
						stringBuffer.append("报告");
						downMonitor.setKey(stringBuffer.toString());
						downMonitor.setValue(task.getActivityId());
						model.addAttribute("downMonitor_show",downMonitor);
					}else if(task.getTaskType()==MonitorTaskType.DURATION_MONITOR.getId()) {  //2 投放期间监测
						durationList.add(task);
					}else if(task.getTaskType()==MonitorTaskType.ZHUIJIA_MONITOR.getId()) {  //6 追加监测
						zhuijiaList.add(task);
					}
				//}
			}
		}
		
		//遍历投放期间监测
		if(durationList != null && durationList.size()>0) {
			List<TaskDownload> taskDownloadsExcel = new ArrayList<>();
			for(AdMonitorTask task : durationList) {
				StringBuffer stringBuffer = new StringBuffer();
				if(now.compareTo(task.getReportTime())>0) {	//当前时间大于出报告时间
					String nowDate = sdf.format(task.getReportTime());
					if((userObj.getUsertype()==UserTypeEnum.CUSTOMER.getId() && task.getStatus()==MonitorTaskStatus.VERIFIED.getId()) || (userObj.getUsertype()!=UserTypeEnum.CUSTOMER.getId())) {
						//登录用户是广告主且通过审核  或者登录用户是群邑
						TaskDownload durationMonitor = new TaskDownload();
						stringBuffer.append(nowDate);
						stringBuffer.append("	");
						stringBuffer.append(MonitorTaskType.DURATION_MONITOR.getText());
						stringBuffer.append("报告");
						durationMonitor.setKey(stringBuffer.toString());
						durationMonitor.setValue(task.getActivityId());
						taskDownloadsExcel.add(durationMonitor);
					}
				}
			}
			model.addAttribute("durationMonitor_show",taskDownloadsExcel);
		}
		
		//遍历追加监测
		if(zhuijiaList!= null && zhuijiaList.size()>0) {
			List<TaskDownload> zhuijiaExcel = new ArrayList<>();
			for(AdMonitorTask task : zhuijiaList) {
				StringBuffer stringBuffer = new StringBuffer();
				if(now.compareTo(task.getReportTime())>0) {	//当前时间大于出报告时间
					String nowDate = sdf.format(task.getReportTime());
					if((userObj.getUsertype()==UserTypeEnum.CUSTOMER.getId() && task.getStatus()==MonitorTaskStatus.VERIFIED.getId()) || (userObj.getUsertype()!=UserTypeEnum.CUSTOMER.getId())) {
						//登录用户是广告主且通过审核  或者登录用户是群邑
						TaskDownload zhuijiaMonitor = new TaskDownload();
						stringBuffer.append(nowDate);
						stringBuffer.append("	");
						stringBuffer.append(MonitorTaskType.ZHUIJIA_MONITOR.getText());
						stringBuffer.append("报告");
						zhuijiaMonitor.setKey(stringBuffer.toString());
						zhuijiaMonitor.setValue(task.getActivityId());
						zhuijiaExcel.add(zhuijiaMonitor);
					}
				}
			}
			model.addAttribute("zhuijiaMonitor_show",zhuijiaExcel);
		}
		return PageConst.SELECT_ALL_TASKS;
	}
	
	//输入品牌和PDF导出标题
	@RequestMapping(value="/writeBrand")
	public String writeBrand(Model model,HttpServletRequest request,
			@RequestParam(value = "activityId", required = false) Integer activityId,
			@RequestParam(value = "taskreport", required = false) String taskreport) {
		model.addAttribute("activityId" ,activityId);
		model.addAttribute("taskreport" , taskreport);
		return PageConst.WRITE_BRAND_TITLE_PDF;
	}
	
	//Pdf导出任务列表报告,淘汰接口
	@RequestMapping(value="/selectTasksToPdf")
	public String selectTasksToPdf(Model model,HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "activityId", required = false) Integer activityId) {
		SysUser userObj = (SysUser) ShiroUtils.getSessionAttribute(SessionKey.SESSION_LOGIN_USER.toString());
		Date now = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		//查询所有任务列表
		List<AdMonitorTask> list = adMonitorTaskService.getAllTasksByActivityId(activityId);
		//所有投放期间监测任务列表
		List<AdMonitorTask> durationList = new ArrayList<>();
		//所有追加监测列表
		List<AdMonitorTask> zhuijiaList = new ArrayList<>();
		for(AdMonitorTask task : list) {
			StringBuffer stringBuffer = new StringBuffer();
			if(now.compareTo(task.getReportTime())>0) {	//当前时间大于出报告时间
				String nowDate = sdf.format(task.getReportTime());
				if((userObj.getUsertype()==UserTypeEnum.CUSTOMER.getId() && task.getStatus()==MonitorTaskStatus.VERIFIED.getId()) || (userObj.getUsertype()!=UserTypeEnum.CUSTOMER.getId())) {
					//登录用户是广告主且通过审核  或者登录用户是群邑
					if(task.getTaskType()==MonitorTaskType.UP_TASK.getId()) {	//5 上刊任务
						TaskDownload upTask = new TaskDownload();
						stringBuffer.append(nowDate);
						stringBuffer.append("	");
						stringBuffer.append(MonitorTaskType.UP_TASK.getText());
						stringBuffer.append("报告");
						upTask.setKey(stringBuffer.toString());
						upTask.setValue(task.getActivityId());
						model.addAttribute("upTask_show",upTask);
					}else if(task.getTaskType()==MonitorTaskType.UP_MONITOR.getId()) {	//1 上刊监测
						TaskDownload upMonitor = new TaskDownload();
						stringBuffer.append(nowDate);
						stringBuffer.append("	");
						stringBuffer.append(MonitorTaskType.UP_MONITOR.getText());
						stringBuffer.append("报告");
						upMonitor.setKey(stringBuffer.toString());
						upMonitor.setValue(task.getActivityId());
						model.addAttribute("upMonitor_show",upMonitor);
					}else if(task.getTaskType()==MonitorTaskType.DOWNMONITOR.getId()) {	//3 下刊监测
						TaskDownload downMonitor = new TaskDownload();
						stringBuffer.append(nowDate);
						stringBuffer.append("	");
						stringBuffer.append(MonitorTaskType.DOWNMONITOR.getText());
						stringBuffer.append("报告");
						downMonitor.setKey(stringBuffer.toString());
						downMonitor.setValue(task.getActivityId());
						model.addAttribute("downMonitor_show",downMonitor);
					}else if(task.getTaskType()==MonitorTaskType.DURATION_MONITOR.getId()) {  //2 投放期间监测
						durationList.add(task);
					}else if(task.getTaskType()==MonitorTaskType.ZHUIJIA_MONITOR.getId()) {  //6 追加监测
						zhuijiaList.add(task);
					}
				}
			}
		}
		
		//遍历投放期间监测
		if(durationList != null && durationList.size()>0) {
			List<TaskDownload> taskDownloadsPdf = new ArrayList<>();
			for(AdMonitorTask task : durationList) {
				StringBuffer stringBuffer = new StringBuffer();
				if(now.compareTo(task.getReportTime())>0) {	//当前时间大于出报告时间
					String nowDate = sdf.format(task.getReportTime());
					if((userObj.getUsertype()==UserTypeEnum.CUSTOMER.getId() && task.getStatus()==MonitorTaskStatus.VERIFIED.getId()) || (userObj.getUsertype()!=UserTypeEnum.CUSTOMER.getId())) {
						//登录用户是广告主且通过审核  或者登录用户是群邑
						TaskDownload durationMonitor = new TaskDownload();
						stringBuffer.append(nowDate);
						stringBuffer.append("	");
						stringBuffer.append(MonitorTaskType.DURATION_MONITOR.getText());
						stringBuffer.append("报告");
						durationMonitor.setKey(stringBuffer.toString());
						durationMonitor.setValue(task.getActivityId());
						taskDownloadsPdf.add(durationMonitor);
					}
				}
			}
			model.addAttribute("durationMonitor_show",taskDownloadsPdf);
		}
		
		//遍历追加监测
		if(zhuijiaList!= null && zhuijiaList.size()>0) {
			List<TaskDownload> zhuijiaPdf = new ArrayList<>();
			for(AdMonitorTask task : zhuijiaList) {
				StringBuffer stringBuffer = new StringBuffer();
				if(now.compareTo(task.getReportTime())>0) {	//当前时间大于出报告时间
					String nowDate = sdf.format(task.getReportTime());
					if((userObj.getUsertype()==UserTypeEnum.CUSTOMER.getId() && task.getStatus()==MonitorTaskStatus.VERIFIED.getId()) || (userObj.getUsertype()!=UserTypeEnum.CUSTOMER.getId())) {
						//登录用户是广告主且通过审核  或者登录用户是群邑
						TaskDownload zhuijiaMonitor = new TaskDownload();
						stringBuffer.append(nowDate);
						stringBuffer.append("	");
						stringBuffer.append(MonitorTaskType.ZHUIJIA_MONITOR.getText());
						stringBuffer.append("报告");
						zhuijiaMonitor.setKey(stringBuffer.toString());
						zhuijiaMonitor.setValue(task.getActivityId());
						zhuijiaPdf.add(zhuijiaMonitor);
					}
				}
			}
			model.addAttribute("zhuijiaMonitor_show",zhuijiaPdf);
		}
		return PageConst.SELECT_TASKPDF;
	}
	
	@RequestMapping(value = "/changePic")
	public String changePic(Model model,HttpServletRequest request, HttpServletResponse response) {
		
		return PageConst.CHANGE_ADSEAT_PIC;
	}
	/**
	 * pdf导出报告列表
	 * @param activityId 活动id
	 * @return
	 */
	@RequestMapping(value="/selectTasksToPdfs")
	public String selectTasksToPdfs(Model model,HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "activityId", required = false) Integer activityId) {
		SysUser userObj = (SysUser) ShiroUtils.getSessionAttribute(SessionKey.SESSION_LOGIN_USER.toString());
		Date now = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Map<String, Object> searchMap = new HashMap<>();;
		searchMap.put("activityId", activityId);
		searchMap.put("reportTime", now);
		//查询所有任务列表
		List<AdMonitorTask> list = null;
		if (userObj.getUsertype()==UserTypeEnum.CUSTOMER.getId()) {
			searchMap.put("status", MonitorTaskStatus.VERIFIED.getId());
			list = adMonitorTaskService.getAllTaskTypesByActivityIdReportTime(searchMap);
		}else if (userObj.getUsertype()==UserTypeEnum.SUPER_ADMIN.getId()) {
			list = adMonitorTaskService.getAllTaskTypesByActivityIdReportTime(searchMap);
		}
		List<String> reportList = new ArrayList<>();
		for(AdMonitorTask task : list) {
			StringBuffer stringBuffer = new StringBuffer();
			String nowDate = sdf.format(task.getReportTime());
			stringBuffer.append(nowDate).append("	");
			stringBuffer.append(MonitorTaskType.getText(task.getTaskType())).append("报告");
			reportList.add(stringBuffer.toString());
		}
		model.addAttribute("activityId",activityId);
		model.addAttribute("reportList",reportList);
		return PageConst.SELECT_TASKPDF;
	}
}
