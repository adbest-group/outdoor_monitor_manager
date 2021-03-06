package com.bt.om.web.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.adtime.common.lang.StringUtil;
import com.bt.om.common.SysConst;
import com.bt.om.common.web.PageConst;
import com.bt.om.entity.AdActivity;
import com.bt.om.entity.AdActivityAdseat;
import com.bt.om.entity.AdJiucuoTask;
import com.bt.om.entity.AdJiucuoTaskFeedback;
import com.bt.om.entity.AdMonitorTask;
import com.bt.om.entity.AdSeatInfo;
import com.bt.om.entity.AdSystemPush;
import com.bt.om.entity.City;
import com.bt.om.entity.SysUser;
import com.bt.om.entity.vo.AdJiucuoTaskVo;
import com.bt.om.entity.vo.AdMonitorTaskVo;
import com.bt.om.enums.JiucuoTaskStatus;
import com.bt.om.enums.ResultCode;
import com.bt.om.enums.SessionKey;
import com.bt.om.enums.TaskProblemStatus;
import com.bt.om.enums.UserTypeEnum;
import com.bt.om.filter.LogFilter;
import com.bt.om.mapper.SysUserResMapper;
import com.bt.om.security.ShiroUtils;
import com.bt.om.service.IAdActivityService;
import com.bt.om.service.IAdJiucuoTaskService;
import com.bt.om.service.IAdMonitorTaskService;
import com.bt.om.service.IAdSeatService;
import com.bt.om.service.IAdSystemPushService;
import com.bt.om.service.IAdUserMessageService;
import com.bt.om.service.ICityService;
import com.bt.om.service.IMediaService;
import com.bt.om.service.ISysGroupService;
import com.bt.om.service.ISysResourcesService;
import com.bt.om.service.ISysUserRoleService;
import com.bt.om.service.ISysUserService;
import com.bt.om.util.ConfigUtil;
import com.bt.om.util.MarkLogoUtil;
import com.bt.om.vo.web.ResultVo;
import com.bt.om.vo.web.SearchDataVo;
import com.bt.om.web.BasicController;
import com.bt.om.web.util.JPushUtils;
import com.bt.om.web.util.SearchUtil;

/**
 * Created by caiting on 2018/1/20.
 */
@Controller
@RequestMapping(value = "/jiucuo")
public class JiucuoController extends BasicController {
	@Autowired
	private IAdJiucuoTaskService adJiucuoTaskService;
	@Autowired
	private IAdActivityService adActivityService;
	@Autowired
	IAdMonitorTaskService adMonitorTaskService;
	@Autowired
	private ISysUserService sysUserService;
	@Autowired
	IMediaService mediaService;
	@Autowired
	private ISysGroupService sysGroupService;
	@Autowired
	private ISysResourcesService sysResourcesService;
	@Autowired
	private ISysUserRoleService sysUserRoleService;
	@Autowired
	protected RedisTemplate redisTemplate;
	@Autowired
	private SysUserResMapper sysUserResMapper;
    @Autowired
	private IAdUserMessageService adUserMessageService;
    @Autowired
    private IAdSeatService adSeatInfoService;
    @Autowired
    private IAdSystemPushService systemPushService;
    @Autowired
    private ICityService cityService;
    
    private String file_upload_path = ConfigUtil.getString("file.upload.path");
	
	private String file_upload_ip = ConfigUtil.getString("file.upload.ip");
	
	private static final Logger logger = Logger.getLogger(JiucuoController.class);

	/**
	 * 查看纠错审核列表
	 */
	@RequestMapping(value = "/list")
	@RequiresRoles(value = { "jiucuoadmin", "superadmin", "depjiucuoadmin", "deptaskadmin",	"taskadmin" }, logical = Logical.OR)
	public String joucuoList(Model model, HttpServletRequest request,
			@RequestParam(value = "id", required = false) Integer id,
			@RequestParam(value = "activityId", required = false) Integer activityId,
			@RequestParam(value = "status", required = false) Integer status,
			@RequestParam(value = "problemStatus", required = false) Integer problemStatus,
			@RequestParam(value = "startDate", required = false) String startDate,
			@RequestParam(value = "endDate", required = false) String endDate,
			@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "mediaTypeId", required = false) Integer mediaTypeId,
            @RequestParam(value = "mediaTypeParentId", required = false) Integer mediaTypeParentId,
            @RequestParam(value = "province", required = false) String province,
            @RequestParam(value = "city", required = false) String city) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		SearchDataVo vo = SearchUtil.getVo();

		// 获取登录的审核员工jiucuoadmin
		SysUser userObj = (SysUser) ShiroUtils.getSessionAttribute(SessionKey.SESSION_LOGIN_USER.toString());

		// 所有未审核的任务
		// Map<String, Object> searchMap1 = new HashMap<>();
		// List<Integer> customerIds =
		// sysUserService.getCustomerIdsByAdminId(userObj.getId());
		// //根据员工id查询所属组对应的所有广告商id集合
		// searchMap1.put("customerIds", customerIds);
		// List<AdJiucuoTaskVo> allJiucuoTaskUncertain =
		// adJiucuoTaskService.getAllByStatusUnCheck(searchMap1);
		// Integer shenheCount = 0;

		if (id != null) {
			vo.putSearchParam("id", id.toString(), id);
		}
		if (activityId != null) {
			vo.putSearchParam("activityId", activityId.toString(), activityId);
		}
		if (status != null) {
			// status = 1; //如果没有传参status, 默认取1：待审核
			vo.putSearchParam("status", status.toString(), status);
			model.addAttribute("status", status);
		}

		if (problemStatus != null) {
			vo.putSearchParam("problemStatus", problemStatus.toString(), problemStatus);
		}
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
		if (userObj.getUsertype() != UserTypeEnum.SUPER_ADMIN.getId() && userObj.getUsertype() != UserTypeEnum.DEPARTMENT_LEADER.getId()) {
			List<Integer> customerIds = sysUserService.getCustomerIdsByAdminId(userObj.getId()); // 根据员工id查询所属组对应的所有广告商id集合
			if (customerIds != null && customerIds.size() == 0) {
				// 员工对应的广告商id集合为空, 不需要再去查询纠错审核列表
				vo.setCount(0);
				vo.setSize(20);
				vo.setStart(0);
				vo.setList(null);
			} else {
				vo.putSearchParam("customerIds", null, customerIds);
			}
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
		adJiucuoTaskService.getPageData(vo);

		// //只能查询自己参与的纠错任务审核
		// if(userObj != null) {
		// Integer assessorId = userObj.getId();
		// vo.putSearchParam("assessorId", assessorId.toString(), assessorId);
		// }

		// if(status == 2 || status == 3) {
		// //查询通过审核 或 未通过审核 的纠错任务
		// adJiucuoTaskService.getPageData(vo);
		// } else {
		// //查询待审核的纠错任务
		// //[1] 先查询审核id的所有待审核的纠错任务
		// Map<String, Object> searchMap = new HashMap<>();
		// searchMap.put("status", 1);
		// searchMap.put("assessorId", userObj.getId());
		//// searchMap.put("activityId", activityId);
		//// searchMap.put("id", id);
		//// searchMap.put("problemStatus", problemStatus);
		//// searchMap.put("startDate", startDate);
		//// searchMap.put("endDate", endDate);
		// List<AdJiucuoTaskVo> taskVos =
		// adJiucuoTaskService.selectAllByAssessorId(searchMap);
		//
		// if(taskVos != null && taskVos.size() > 0) {
		// //条数大于0, 返回给页面
		// Iterator<AdJiucuoTaskVo> iterator = taskVos.iterator();
		// while(iterator.hasNext()) {
		// boolean remove = false; //不用抹去
		// AdJiucuoTaskVo taskVo = iterator.next();
		// //通过页面上的activityId做筛选
		// if(activityId != null) {
		// if(taskVo.getActivityId() != activityId) {
		// remove = true;
		// }
		// }
		// if(id != null) {
		// if(taskVo.getId() != id) {
		// remove = true;
		// }
		// }
		// //通过页面上的problemStatus做筛选
		// if(problemStatus != null) {
		// if(taskVo.getProblemStatus() != problemStatus) {
		// remove = true;
		// }
		// }
		// //通过页面上的startDate做筛选
		// if(StringUtil.isNotBlank(startDate)) {
		// if(taskVo.getStartTime().getTime() < sdf.parse(startDate).getTime()) {
		// remove = true;
		// }
		// }
		// //通过页面上的endDate做筛选
		// if(StringUtil.isNotBlank(endDate)) {
		// if(taskVo.getEndTime().getTime() > sdf.parse(endDate).getTime()) {
		// remove = true;
		// }
		// }
		// if(remove == true) {
		// iterator.remove();
		// }
		// }
		// vo.setCount(taskVos.size());
		// vo.setSize(20);
		// vo.setStart(0);
		// vo.setList(taskVos);
		// shenheCount = allJiucuoTaskUncertain.size() - taskVos.size();
		// if(shenheCount < 0) {
		// shenheCount = 0;
		// }
		// model.addAttribute("shenheCount", shenheCount);
		// } else {
		// //条数等于0, 新查询10条或者小于10条没人认领的待审核的纠错任务(需要匹配 员工 - 组 - 广告商 之间的关系)
		// //List<Integer> customerIds =
		// sysUserService.getCustomerIdsByAdminId(userObj.getId());
		// if(customerIds != null && customerIds.size() > 0) {
		// searchMap.clear();
		// searchMap.put("status", 1);
		// searchMap.put("customerIds", customerIds);
		// searchMap.put("assessorId", userObj.getId());
		// List<AdJiucuoTaskVo> adJiucuoTaskVos =
		// adJiucuoTaskService.getTenAdMonitorTaskVo(searchMap);
		// vo.setCount(adJiucuoTaskVos.size());
		// vo.setSize(20);
		// vo.setStart(0);
		// vo.setList(adJiucuoTaskVos);
		// shenheCount = allJiucuoTaskUncertain.size() - adJiucuoTaskVos.size();
		// if(shenheCount < 0) {
		// shenheCount = 0;
		// }
		// model.addAttribute("shenheCount", shenheCount);
		// }
		// }
		// }

		SearchUtil.putToModel(model, vo);

		return PageConst.JIUCUO_LIST;
	}

	/**
	 * 查看纠错详情
	 */
	@RequestMapping(value = "/detail")
	public String showDetail(Model model, HttpServletRequest request,
			@RequestParam(value = "id", required = false) Integer id) {
		
		// 获取当前登录的后台用户信息
		SysUser sysUser = (SysUser) ShiroUtils.getSessionAttribute(SessionKey.SESSION_LOGIN_USER.toString());
		model.addAttribute("usertype", sysUser.getUsertype());
		
		// 纠错任务
		AdJiucuoTaskVo task = adJiucuoTaskService.getVoById(id);
		// 上传内容
		AdJiucuoTaskFeedback feedback = adJiucuoTaskService.getFeadBackByTaskId(id);
		// 广告活动
		AdActivity activity = adActivityService.getVoById(task.getActivityId());
		// 广告活动广告位
		AdActivityAdseat seat = adActivityService.getActivitySeatById(task.getActivityAdseatId());
		// 因当前纠错发起的子监测任务
		List<AdMonitorTaskVo> subs = adJiucuoTaskService.getSubTask(id);

		model.addAttribute("task", task);
		model.addAttribute("activity", activity);
		model.addAttribute("seat", seat);
		model.addAttribute("feedback", feedback);
		model.addAttribute("subs", subs);
		return PageConst.JIUCUO_DETAIL;
	}

	/**
	 * 批量审核纠错
	 */
	@RequiresRoles(value = { "jiucuoadmin", "depjiucuoadmin", "superadmin" }, logical = Logical.OR)
	@RequestMapping(value = "/verify")
	@ResponseBody
	public Model confirm(Model model, HttpServletRequest request,
			@RequestParam(value = "ids", required = false) String ids,
			@RequestParam(value = "status", required = false) Integer status,
			@RequestParam(value = "reason", required = false) String reason) {
		ResultVo<String> result = new ResultVo<String>();
		Date now = new Date();
		// [1] ids拆分成id集合
		String[] jiucuoIds = ids.split(",");
		// [2] 循环判断每一个id是否已经在redis中. 存在一个即返回错误信息
		for (String jcId : jiucuoIds) {
			String beginRedisStr = "jiucuo_" + jcId + "_begin";
			String finishRedisStr = "jiucuo_" + jcId + "_finish";
			if (redisTemplate.opsForValue().get(finishRedisStr) != null
					&& StringUtil.equals(redisTemplate.opsForValue().get(finishRedisStr) + "", "true")) {
				result.setCode(ResultCode.RESULT_FAILURE.getCode());
				result.setResultDes("纠错已被审核，请刷新再试！");
				model.addAttribute(SysConst.RESULT_KEY, result);
				return model;
			}
			if (redisTemplate.opsForValue().get(beginRedisStr) != null
					&& StringUtil.equals(redisTemplate.opsForValue().get(beginRedisStr) + "", "true")) {
				result.setCode(ResultCode.RESULT_FAILURE.getCode());
				result.setResultDes("纠错正被审核中，请刷新再试！");
				model.addAttribute(SysConst.RESULT_KEY, result);
				return model;
			}
		}
		// [3] 循环放入redis中
		for (String jcId : jiucuoIds) {
			String beginRedisStr = "jiucuo_" + jcId + "_begin";
			// 放入Redis缓存处理并发
			redisTemplate.opsForValue().set(beginRedisStr, "true", 60 * 30, TimeUnit.SECONDS); // 设置半小时超时时间
		}
		result.setCode(ResultCode.RESULT_SUCCESS.getCode());
		result.setResultDes("审核成功");
		model = new ExtendedModelMap();

		AdJiucuoTask task = new AdJiucuoTask();
		// task.setId(jcId);
		
		try {
			// 获取登录的审核人(员工/部门领导/超级管理员)
			SysUser userObj = (SysUser) ShiroUtils.getSessionAttribute(SessionKey.SESSION_LOGIN_USER.toString());

			if (status == JiucuoTaskStatus.VERIFIED.getId()) {// 审核通过
				adJiucuoTaskService.pass(jiucuoIds, userObj.getId(), status);
			} else if (status == JiucuoTaskStatus.VERIFY_FAILURE.getId()) {// 审核不通过
				adJiucuoTaskService.reject(jiucuoIds, reason, userObj.getId(),status);
			}

			AdActivity adactivity = new AdActivity();
			AdSeatInfo info = new AdSeatInfo();
			// 循环推送消息
			for (String jcId : jiucuoIds) {
				task = adJiucuoTaskService.getById(Integer.parseInt(jcId));
				// ==========web端任务纠错之后根据userId进行app消息推送==============
				Map<String, Object> param = new HashMap<>();
				Map<String, String> extras = new HashMap<>();
				List<String> alias = new ArrayList<>(); // 别名用户List
				alias.add(String.valueOf(task.getUserId())); // 纠错任务提交者
				extras.put("type", "task_jiucuo_push");
				param.put("msg", "您的任务一条新的纠错审核通知！");
				param.put("title", "玖凤平台");
				param.put("alias", alias); // 根据别名选择推送用户（这里userId用作推送时的用户别名）
				param.put("extras", extras);
				String pushResult = JPushUtils.pushAllByAlias(param);
				System.out.println("pushResult:: " + pushResult);
				
				info = adSeatInfoService.getById(task.getAdSeatId());
				City province = cityService.getName(info.getProvince());//获取省
				City city = cityService.getName(info.getCity());//获取市
				adactivity = adActivityService.getActivityName(task.getActivityId());//获取活动名
				AdSystemPush push = new AdSystemPush();
				push.setUserId(task.getUserId());
				push.setActivityName(adactivity.getActivityName());
				push.setTitle(JiucuoTaskStatus.getText(task.getStatus()));
				String proName = "";
				String cityName = "";
				String location = "";
				String road = "";
				if(province != null) {
					proName = province.getName();
				}
				if(city != null) {
					cityName = city.getName();
				}
				if(info.getLocation() != null) {
					location = info.getLocation();
				}
				if(info.getRoad() != null) {
					road = info.getRoad();
				}
				push.setContent("【"+proName+cityName+location+road+"】广告位的【"+adactivity.getActivityName()+"】活动");
				push.setCreateTime(now);
				systemPushService.add(push);
			}
		} catch (Exception e) {
			logger.error(e);
			// [5] 异常情况, 循环删除redis
			for (String jcId : jiucuoIds) {
				String beginRedisStr = "jiucuo_" + jcId + "_begin";
				// 异常情况, 移除Redis缓存处理并发
				redisTemplate.delete(beginRedisStr);
			}
			result.setCode(ResultCode.RESULT_FAILURE.getCode());
			result.setResultDes("审核失败！");
			model.addAttribute(SysConst.RESULT_KEY, result);
			return model;
		}
		// [6] 处理成功, 循环放入redis
		for (String jcId : jiucuoIds) {
			String finishRedisStr = "jiucuo_" + jcId + "_finish";
			// 放 入Redis缓存处理并发
			redisTemplate.opsForValue().set(finishRedisStr, "true", 60 * 30, TimeUnit.SECONDS); // 设置半小时超时时间
		}
		model.addAttribute(SysConst.RESULT_KEY, result);
		return model;
	}

	/**
	 * 撤消纠错
	 */
	@RequiresRoles("jiucuoadmin")
	@RequestMapping(value = "/cancel")
	@ResponseBody
	public Model cancel(Model model, HttpServletRequest request,
			@RequestParam(value = "id", required = false) Integer id,
			@RequestParam(value = "userId", required = false) Integer userId,
			@RequestParam(value = "mediaId", required = false) Integer mediaId,
			@RequestParam(value = "reason", required = false) String reason) {
		ResultVo<String> result = new ResultVo<String>();
		result.setCode(ResultCode.RESULT_SUCCESS.getCode());
		result.setResultDes("撤消成功");
		model = new ExtendedModelMap();
		AdJiucuoTask jiucuoTask = new AdJiucuoTask();
		try {
			// 获取当前登录的后台用户信息
			SysUser sysUser = (SysUser) ShiroUtils.getSessionAttribute(SessionKey.SESSION_LOGIN_USER.toString());
			Map<String, Object> searchMap = new HashMap<>();
			searchMap.put("userId", sysUser.getId());
			Integer groupId = sysUserRoleService.selectGroupIdByUserId(searchMap);
			// 获取该组所有员工
			List<SysUser> sysUsers = sysGroupService.selectUserName(groupId);

			if (sysUsers.size() > 1) {// 待审核
				adJiucuoTaskService.offJiucuoTaskByAssessorId(id);
			} else if ((sysUsers.size() <= 1)) {
				result.setCode(ResultCode.RESULT_FAILURE.getCode());
				result.setResultDes("只剩一人不能撤销！");
				model.addAttribute(SysConst.RESULT_KEY, result);
				return model;
			}
		} catch (Exception e) {
			logger.error(e);
			result.setCode(ResultCode.RESULT_FAILURE.getCode());
			result.setResultDes("撤消失败！");
			model.addAttribute(SysConst.RESULT_KEY, result);
			return model;
		}
		model.addAttribute(SysConst.RESULT_KEY, result);
		return model;
	}

	/**
	 * 关闭纠错问题任务
	 */
	@RequestMapping(value = "/close")
	@ResponseBody
	public Model close(Model model, HttpServletRequest request,
			@RequestParam(value = "id", required = false) Integer id) {
		ResultVo<String> result = new ResultVo<String>();
		result.setCode(ResultCode.RESULT_SUCCESS.getCode());
		result.setResultDes("关闭成功");
		model = new ExtendedModelMap();

		AdJiucuoTask task = new AdJiucuoTask();
		task.setProblemStatus(TaskProblemStatus.CLOSED.getId());
		task.setId(id);
		try {
			adJiucuoTaskService.update(task);
		} catch (Exception e) {
			logger.error(e);
			result.setCode(ResultCode.RESULT_FAILURE.getCode());
			result.setResultDes("关闭失败！");
			model.addAttribute(SysConst.RESULT_KEY, result);
			return model;
		}

		model.addAttribute(SysConst.RESULT_KEY, result);
		return model;
	}

	/**
	 * 创建复查子任务
	 */
	@RequestMapping(value = "/createTask")
	@ResponseBody
	public Model newSub(Model model, HttpServletRequest request,
			@RequestParam(value = "id", required = false) Integer id) {
		ResultVo<String> result = new ResultVo<String>();
		result.setCode(ResultCode.RESULT_SUCCESS.getCode());
		result.setResultDes("创建成功");
		model = new ExtendedModelMap();

		AdMonitorTask task = new AdMonitorTask();
		task.setProblemStatus(TaskProblemStatus.CLOSED.getId());
		task.setId(id);
		try {
			adJiucuoTaskService.createSubTask(id);
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
			result.setCode(ResultCode.RESULT_FAILURE.getCode());
			result.setResultDes("创建失败！");
			model.addAttribute(SysConst.RESULT_KEY, result);
			return model;
		}

		model.addAttribute(SysConst.RESULT_KEY, result);
		return model;
	}
	
	/**
	 * 任务图片更替
	 */
	@RequestMapping(value = "/changePic")
	@ResponseBody
	public Model changeDetailsPage(Model model, @RequestParam("taskFeedBackId") Integer id, HttpServletRequest request, HttpServletResponse response,
									@RequestParam(value = "picFile", required = false) MultipartFile file,
									@RequestParam(value = "index", required = false) Integer index) {
		ResultVo<String> result = new ResultVo<String>();
		result.setCode(ResultCode.RESULT_SUCCESS.getCode());
		result.setResultDes("替换成功");
		model = new ExtendedModelMap();
		
		if(id == null) {
			result.setCode(ResultCode.RESULT_FAILURE.getCode());
			result.setResultDes("替换失败！");
			model.addAttribute(SysConst.RESULT_KEY, result);
			return model;
		}
		
		AdJiucuoTask jiucuoTask = adJiucuoTaskService.getById(id);
		String path = file_upload_path;
		Calendar date = Calendar.getInstance();
		String timePath = date.get(Calendar.YEAR)+ File.separator + (date.get(Calendar.MONTH)+1) + File.separator+ date.get(Calendar.DAY_OF_MONTH) + File.separator;
		path = path + (path.endsWith(File.separator) ? "" : File.separatorChar) + "activity" + File.separatorChar + jiucuoTask.getActivityId() + File.separatorChar + "jiucuo" + File.separator + timePath;
        String imageName = file.getOriginalFilename();
		InputStream is;
		String filepath;
		try {
			is = file.getInputStream();
			Long size = file.getSize();
			if (!isImg(imageName.toLowerCase())) {
				result.setCode(ResultCode.RESULT_FAILURE.getCode());
				result.setResultDes("上传的不是图片！");
				model.addAttribute(SysConst.RESULT_KEY, result);
				return model;
			}
			if (size > 1024 * 1024) {
				result.setCode(ResultCode.RESULT_FAILURE.getCode());
				result.setResultDes("图片尺寸太大！");
				model.addAttribute(SysConst.RESULT_KEY, result);
				return model;
			}
			//[1] 上传图片
			filepath = saveFile(path,imageName,is);
			int picindex = filepath.lastIndexOf('/')+1;
			String filename = filepath.substring(picindex);
			int nameindex = filename.indexOf('.');
			MarkLogoUtil.markImageBySingleIcon(request.getSession().getServletContext().getRealPath("/")+"/static/images/jflogomin.png", path+filename, path, filename.substring(0, nameindex), "jpg", null);
            
			//[2] 更改数据库
			adJiucuoTaskService.updatePicUrl(id, file_upload_ip + filepath, index);
		} catch (IOException e) {
			logger.error(e);
			result.setCode(ResultCode.RESULT_FAILURE.getCode());
			result.setResultDes("替换失败！");
			model.addAttribute(SysConst.RESULT_KEY, result);
			return model;
		}
		
		model.addAttribute(SysConst.RESULT_KEY, result);
		return model;
	}
	
	//保存在本服务器
	private String saveFile(String path,String filename,InputStream is){
		String ext = filename.substring(filename.lastIndexOf(".") + 1);
		filename = UUID.randomUUID().toString().toLowerCase()+"."+ext.toLowerCase();
		FileOutputStream fos = null;
		try {
			File file = new File(path);
	        if(!file.exists()){
	            file.mkdirs();
	        }
			fos = new FileOutputStream(path+filename);
			int len = 0;
			byte[] buff = new byte[1024];
			while((len=is.read(buff))>0){
				fos.write(buff);
			}
			path = path.substring(path.indexOf(":")+1, path.length()).replaceAll("\\\\", "/");
			path = path.replaceFirst("/opt/", "/");
			return path+filename;
		} catch (FileNotFoundException e) {
			logger.error(e);
			e.printStackTrace();
		} catch (IOException e) {
			logger.error(e);
			e.printStackTrace();
		}finally {
			if(fos!=null){
				try {
					fos.flush();
					fos.close();
				} catch (IOException e) {
					logger.error(e);
					e.printStackTrace();
				}
			}
			if(is!=null){
				try {
					is.close();
				} catch (IOException e) {
					logger.error(e);
					e.printStackTrace();
				}
			}
		}
		return "error";
	}

	private boolean isImg(String imgName) {
		imgName = imgName.toLowerCase();
		if (imgName.endsWith(".jpg") || imgName.endsWith(".jpeg") || imgName.endsWith(".png") || imgName.endsWith(".gif")) {
			return true;
		}
		return false;
	}

}