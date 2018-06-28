package com.bt.om.web.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import org.springframework.web.multipart.MultipartFile;

import com.adtime.common.lang.StringUtil;
import com.bt.om.common.SysConst;
import com.bt.om.common.web.PageConst;
import com.bt.om.entity.AdMedia;
import com.bt.om.entity.AdMediaType;
import com.bt.om.entity.AdMonitorTask;

import com.bt.om.entity.AdMonitorTaskFeedback;
import com.bt.om.entity.AdSeatInfo;
import com.bt.om.entity.AdUserMessage;

import com.bt.om.entity.SysUser;
import com.bt.om.entity.SysUserExecute;
import com.bt.om.entity.vo.AdMonitorTaskVo;
import com.bt.om.enums.MonitorTaskStatus;
import com.bt.om.enums.MonitorTaskType;
import com.bt.om.enums.ResultCode;
import com.bt.om.enums.RewardTaskType;
import com.bt.om.enums.SessionKey;
import com.bt.om.enums.TaskProblemStatus;
import com.bt.om.mapper.SysUserResMapper;
import com.bt.om.security.ShiroUtils;
import com.bt.om.service.IAdActivityService;
import com.bt.om.service.IAdJiucuoTaskService;
import com.bt.om.service.IAdMonitorTaskService;
import com.bt.om.service.IAdUserMessageService;
import com.bt.om.service.IMediaService;
import com.bt.om.service.ISysGroupService;
import com.bt.om.service.ISysResourcesService;
import com.bt.om.service.ISysUserExecuteService;
import com.bt.om.service.ISysUserRoleService;
import com.bt.om.service.ISysUserService;
import com.bt.om.vo.web.ResultVo;
import com.bt.om.vo.web.SearchDataVo;
import com.bt.om.web.BasicController;
import com.bt.om.web.util.JPushUtils;
import com.bt.om.web.util.SearchUtil;
import com.google.common.collect.Maps;

/**
 * Created by caiting on 2018/1/20.
 */
@Controller
@RequestMapping("/task")
public class MonitorTaskController extends BasicController {
	@Autowired
	IAdMonitorTaskService adMonitorTaskService;
	@Autowired
	ISysUserExecuteService sysUserExecuteService;
	@Autowired
	IAdJiucuoTaskService adJiucuoTaskService;
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
	private IAdActivityService adActivityService;
	@Autowired
	private IAdUserMessageService adUserMessageService;

	/**
	 * 监测任务管理（任务审核指派部员工登录）
	 **/
	@RequiresRoles(value = { "taskadmin", "deptaskadmin", "depjiucuoadmin", "jiucuoadmin", "superadmin" }, logical = Logical.OR)
	@RequestMapping(value = "/list")
	public String getTaskList(Model model, HttpServletRequest request,
			@RequestParam(value = "activityId", required = false) Integer activityId,
			@RequestParam(value = "taskType", required = false) Integer taskType,
			@RequestParam(value = "status", required = false) Integer status,
			@RequestParam(value = "problemStatus", required = false) Integer problemStatus,
			@RequestParam(value = "startDate", required = false) String startDate,
			@RequestParam(value = "endDate", required = false) String endDate,
			@RequestParam(value = "pid", required = false) Integer pid,
			@RequestParam(value = "ptype", required = false) Integer ptype,
			@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "mediaTypeId", required = false) Integer mediaTypeId,
            @RequestParam(value = "mediaTypeParentId", required = false) Integer mediaTypeParentId,
            @RequestParam(value = "province", required = false) String province,
            @RequestParam(value = "city", required = false) String city) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		SearchDataVo vo = SearchUtil.getVo();

		// 获取登录的审核员工taskadmin
		SysUser userObj = (SysUser) ShiroUtils.getSessionAttribute(SessionKey.SESSION_LOGIN_USER.toString());

		// 所有未审核的任务
		// Map<String, Object> searchMap1 = new HashMap<>();
		// searchMap1.put("customerIds", customerIds);
		// List<AdMonitorTask> allMonitorTaskUncertain =
		// adMonitorTaskService.getAllByStatusUnCheck(searchMap1);
		// Integer shenheCount = 0;

		if (activityId != null) {
			vo.putSearchParam("activityId", activityId.toString(), activityId);
		}
		if (taskType != null) {
			vo.putSearchParam("taskType", taskType.toString(), taskType);
		}
		
		//限制不查询【上刊任务】
		List<Integer> notTaskTypes = new ArrayList<>();
		notTaskTypes.add(MonitorTaskType.UP_TASK.getId());
		vo.putSearchParam("notTaskTypes", null, notTaskTypes);
		
		List<Integer> statuses = new ArrayList<>();
		if (status == null) {
			status = 3; // 如果没有传参status, 默认取3：待审核
		}
		statuses.add(status);
		vo.putSearchParam("statuses", null, statuses);
		model.addAttribute("status", status);

		if (problemStatus != null) {
			vo.putSearchParam("problemStatus", problemStatus.toString(), problemStatus);
		}
		// pid和ptype配合用于从纠纠错复查监测任务和监测任务查复查监测任务用，好像有点绕
		// 如果查纠错的复查监测任务
		if (pid != null && ptype != null) {
			if (ptype == RewardTaskType.JIUCUO.getId()) {
				vo.putSearchParam("parentId", pid.toString(), pid);
				vo.putSearchParam("parentType", ptype.toString(), ptype);
			} else {
				// 任务和对应的复查任务都是监测任务，这里一起查出来方便查看，mapper里注意一下写法
				vo.putSearchParam("idpid", pid.toString(), pid);
			}
		}
		if (startDate != null) {
			try {
				vo.putSearchParam("startDate", startDate, sdf.parse(startDate));
			} catch (ParseException e) {
			}
		}
		if (endDate != null) {
			try {
				vo.putSearchParam("endDate", endDate, sdf.parse(endDate));
			} catch (ParseException e) {
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
		List<Integer> customerIds = sysUserService.getCustomerIdsByAdminId(userObj.getId()); // 根据员工id查询所属组对应的所有广告商id集合
		if (customerIds != null && customerIds.size() == 0) {
			// 员工对应的广告商id集合为空, 不需要再去查询任务审核列表
			vo.setCount(0);
			vo.setSize(20);
			vo.setStart(0);
			vo.setList(null);
		} else {
			vo.putSearchParam("customerIds", null, customerIds);
			adMonitorTaskService.getPageData(vo);
		}

		// //只能查询自己参与的任务审核
		// if(userObj != null) {
		// Integer assessorId = userObj.getId();
		// vo.putSearchParam("assessorId", assessorId.toString(), assessorId);
		// }

		// if(status != 3) {
		// //查询非待审核的监测任务
		// adMonitorTaskService.getPageData(vo);
		// } else {
		// //查询待审核的监测任务
		// //[1] 先查询审核id的所有待审核的监测任务
		// Map<String, Object> searchMap = new HashMap<>();
		// searchMap.put("status", 3);
		// searchMap.put("assessorId", userObj.getId());
		// List<AdMonitorTaskVo> taskVos =
		// adMonitorTaskService.selectAllByAssessorId(searchMap);
		// if(taskVos != null && taskVos.size() > 0) {
		// //条数大于0, 返回给页面
		// Iterator<AdMonitorTaskVo> iterator = taskVos.iterator();
		// while(iterator.hasNext()) {
		// boolean remove = false; //不用抹去
		// AdMonitorTaskVo taskVo = iterator.next();
		// //通过页面上的activityId做筛选
		// if(activityId != null) {
		// if(taskVo.getActivityId() != activityId) {
		// remove = true;
		// }
		// }
		// //通过页面上的taskType做筛选
		// if(taskType != null) {
		// if(taskVo.getTaskType() != taskType) {
		// remove = true;
		// }
		// }
		// //通过页面上的problemStatus做筛选
		// if(problemStatus != null) {
		// if(taskVo.getProblemStatus() != problemStatus) {
		// remove = true;
		// }
		// }
		// //通过页面上的pid和ptype做筛选
		// if (pid != null && ptype != null) {
		// if (ptype == RewardTaskType.JIUCUO.getId()) {
		// if(taskVo.getParentId() != pid) {
		// remove = true;
		// }
		// if(taskVo.getParentType() != ptype) {
		// remove = true;
		// }
		// } else {
		// if(!((taskVo.getId() == pid || taskVo.getParentId() == pid) &&
		// taskVo.getParentType() == 1)) {
		// remove = true;
		// }
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
		// shenheCount = allMonitorTaskUncertain.size() - taskVos.size();
		// if(shenheCount < 0) {
		// shenheCount = 0;
		// }
		// model.addAttribute("shenheCount", shenheCount);
		// } else {
		// //条数等于0, 新查询10条或者小于10条没人认领的待审核的监测任务(需要匹配 员工 - 组 - 广告商 之间的关系)
		// //List<Integer> customerIds =
		// sysUserService.getCustomerIdsByAdminId(userObj.getId());
		// if(customerIds != null && customerIds.size() > 0) {
		// searchMap.clear();
		// searchMap.put("status", 3);
		// searchMap.put("customerIds", customerIds);
		// searchMap.put("assessorId", userObj.getId());
		// List<AdMonitorTaskVo> monitorTaskVos =
		// adMonitorTaskService.getTenAdMonitorTaskVo(searchMap);
		// vo.setCount(monitorTaskVos.size());
		// vo.setSize(20);
		// vo.setStart(0);
		// vo.setList(monitorTaskVos);
		// shenheCount = allMonitorTaskUncertain.size() - monitorTaskVos.size();
		// if(shenheCount < 0) {
		// shenheCount = 0;
		// }
		// model.addAttribute("shenheCount", shenheCount);
		// }
		// }
		// }

		// vo.putSearchParam("hasUserId","1","1");
		SearchUtil.putToModel(model, vo);

		return PageConst.TASK_LIST;
	}

	/**
	 * 监测任务指派（任务审核指派部员工登录）
	 **/
	@RequiresRoles(value = { "taskadmin", "deptaskadmin", "superadmin" }, logical = Logical.OR)
	@RequestMapping(value = "/unassign")
	public String getUnAssignList(Model model, HttpServletRequest request,
			@RequestParam(value = "id", required = false) Integer id,
			@RequestParam(value = "activityId", required = false) Integer activityId,
			@RequestParam(value = "startDate", required = false) String startDate,
			@RequestParam(value = "mediaId", required = false) Integer mediaId,
			@RequestParam(value = "status", required = false) Integer status,
			@RequestParam(value = "endDate", required = false) String endDate,
			@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "mediaTypeId", required = false) Integer mediaTypeId,
            @RequestParam(value = "mediaTypeParentId", required = false) Integer mediaTypeParentId,
            @RequestParam(value = "province", required = false) String province,
            @RequestParam(value = "city", required = false) String city) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		SearchDataVo vo = SearchUtil.getVo();
		AdMonitorTask task = new AdMonitorTask();
		task.setId(id);
		task.setStatus(status);
		// Integer shenheCount = 0;

		// 获取登录的审核员工taskadmin
		SysUser userObj = (SysUser) ShiroUtils.getSessionAttribute(SessionKey.SESSION_LOGIN_USER.toString());

		// 指派员登录显示属于自己组的所有未指派的任务
		// Map<String, Object> searchMap2 = new HashMap<>();
		// List<Integer> customerIds =
		// sysUserService.getCustomerIdsByAdminId(userObj.getId());
		// //根据员工id查询所属组对应的所有广告商id集合
		// searchMap2.put("customerIds", customerIds);
		// List<AdMonitorTask> allMonitorTaskUnZhipai=
		// adMonitorTaskService.getAllByStatusUnZhipai(searchMap2);

		if (status == null) {
            vo.putSearchParam("statuses", null,
                    new Integer[]{MonitorTaskStatus.UNASSIGN.getId(), MonitorTaskStatus.TO_CARRY_OUT.getId(), MonitorTaskStatus.CAN_GRAB.getId()});
        } else {
            vo.putSearchParam("status", String.valueOf(status), String.valueOf(status));
        }

		// 运营平台指派任务只指派监测期间的任务
		// vo.putSearchParam("taskTypes", null, new
		// Integer[]{MonitorTaskType.UP_MONITOR.getId(),MonitorTaskType.DURATION_MONITOR.getId(),MonitorTaskType.DOWNMONITOR.getId(),
		// MonitorTaskType.FIX_CONFIRM.getId()});

		//限制不查询【上刊任务】
  		List<Integer> notTaskTypes = new ArrayList<>();
  		notTaskTypes.add(MonitorTaskType.UP_TASK.getId());
  		vo.putSearchParam("notTaskTypes", null, notTaskTypes);
		
		if (activityId != null) {
			vo.putSearchParam("activityId", activityId.toString(), activityId);
		}
		if (startDate != null) {
			try {
				vo.putSearchParam("startDate", startDate, sdf.parse(startDate));
			} catch (ParseException e) {
			}
		}
		if (endDate != null) {
			try {
				vo.putSearchParam("endDate", endDate, sdf.parse(endDate));
			} catch (ParseException e) {
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
		List<Integer> customerIds = sysUserService.getCustomerIdsByAdminId(userObj.getId()); // 根据员工id查询所属组对应的所有广告商id集合
		if (customerIds != null && customerIds.size() == 0) {
			// 员工对应的广告商id集合为空, 不需要再去查询任务指派列表
			vo.setCount(0);
			vo.setSize(20);
			vo.setStart(0);
			vo.setList(null);
		} else {
			vo.putSearchParam("customerIds", null, customerIds);
			adMonitorTaskService.getPageData(vo);
		}

		// //只能查询自己参与的任务指派
		// if(userObj != null) {
		// Integer assignorId = userObj.getId();
		// vo.putSearchParam("assignorId", assignorId.toString(), assignorId);
		// }

		// if(status != 1) {
		// //查询非待审核的监测指派
		// adMonitorTaskService.getPageData(vo);
		// } else {
		// //查询待审核的监测指派
		// //[1] 先查询审核id的所有待审核的监测任务
		// Map<String, Object> searchMap = new HashMap<>();
		// searchMap.put("status", status); //1: 待指派
		// searchMap.put("assignorId", userObj.getId()); //指派员id
		// List<AdMonitorTaskVo> taskVos =
		// adMonitorTaskService.selectAllByAssessorId(searchMap);
		//
		// if(taskVos != null && taskVos.size() > 0) {
		// //条数大于0, 返回给页面
		// Iterator<AdMonitorTaskVo> iterator = taskVos.iterator();
		// while(iterator.hasNext()) {
		// boolean remove = false; //不用抹去
		// AdMonitorTaskVo taskVo = iterator.next();
		// //通过页面上的activityId做筛选
		// if(activityId != null) {
		// if(taskVo.getActivityId() != activityId) {
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
		//
		// shenheCount = allMonitorTaskUnZhipai.size() - taskVos.size();
		// if(shenheCount < 0) {
		// shenheCount = 0;
		// }
		// model.addAttribute("shenheCount", shenheCount);
		// } else {
		// //条数等于0, 新查询10条或者小于10条没人认领的待审核的监测指派任务(需要匹配 员工 - 组 - 广告商 之间的关系)
		// //List<Integer> customerIds =
		// sysUserService.getCustomerIdsByAdminId(userObj.getId());
		// if(customerIds != null && customerIds.size() > 0) {
		// searchMap.clear();
		// searchMap.put("status", 1);
		// searchMap.put("customerIds", customerIds);
		// searchMap.put("assignorId", userObj.getId());
		// List<AdMonitorTaskVo> monitorTaskVos =
		// adMonitorTaskService.getTenAdMonitorTaskAssignVo(searchMap);
		// vo.setCount(monitorTaskVos.size());
		// vo.setSize(20);
		// vo.setStart(0);
		// vo.setList(monitorTaskVos);
		// shenheCount = allMonitorTaskUnZhipai.size() - monitorTaskVos.size();
		// if(shenheCount < 0) {
		// shenheCount = 0;
		// }
		// model.addAttribute("shenheCount", shenheCount);
		// }
		// }
		// }

		SearchUtil.putToModel(model, vo);

		return PageConst.UNASSIGN_TASK_LIST;
	}

	/**
	 * 上刊任务指派（任务审核指派部员工登录）
	 **/
	@RequiresRoles(value = { "taskadmin", "deptaskadmin", "superadmin" }, logical = Logical.OR)
	@RequestMapping(value = "/upTaskList")
	public String getUpTaskList(Model model, HttpServletRequest request,
			@RequestParam(value = "id", required = false) Integer id,
			@RequestParam(value = "activityId", required = false) Integer activityId,
			@RequestParam(value = "startDate", required = false) String startDate,
			@RequestParam(value = "mediaId", required = false) Integer mediaId,
			@RequestParam(value = "status", required = false) Integer status,
			@RequestParam(value = "endDate", required = false) String endDate,
			@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "mediaTypeId", required = false) Integer mediaTypeId,
            @RequestParam(value = "mediaTypeParentId", required = false) Integer mediaTypeParentId,
            @RequestParam(value = "province", required = false) String province,
            @RequestParam(value = "city", required = false) String city) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		SearchDataVo vo = SearchUtil.getVo();
		AdMonitorTask task = new AdMonitorTask();
		task.setId(id);
		task.setStatus(status);
		// Integer shenheCount = 0;

		// 获取登录的审核员工taskadmin
		SysUser userObj = (SysUser) ShiroUtils.getSessionAttribute(SessionKey.SESSION_LOGIN_USER.toString());

		// 指派员登录显示属于自己组的所有未指派的任务
		// Map<String, Object> searchMap2 = new HashMap<>();
		// List<Integer> customerIds =
		// sysUserService.getCustomerIdsByAdminId(userObj.getId());
		// //根据员工id查询所属组对应的所有广告商id集合
		// searchMap2.put("customerIds", customerIds);
		// List<AdMonitorTask> allMonitorTaskUnZhipai=
		// adMonitorTaskService.getAllByStatusUnZhipai(searchMap2);

		if (status == null) {
            vo.putSearchParam("statuses", null,
                    new Integer[]{MonitorTaskStatus.UNASSIGN.getId(), MonitorTaskStatus.TO_CARRY_OUT.getId(), MonitorTaskStatus.CAN_GRAB.getId()});
        } else {
            vo.putSearchParam("status", String.valueOf(status), String.valueOf(status));
        }

		// 运营平台指派任务只指派监测期间的任务
		// vo.putSearchParam("taskTypes", null, new
		// Integer[]{MonitorTaskType.UP_MONITOR.getId(),MonitorTaskType.DURATION_MONITOR.getId(),MonitorTaskType.DOWNMONITOR.getId(),
		// MonitorTaskType.FIX_CONFIRM.getId()});

		//限制只查询【上刊任务】
		Integer taskType = MonitorTaskType.UP_TASK.getId();
  		vo.putSearchParam("taskType", taskType.toString(), taskType);
		
		if (activityId != null) {
			vo.putSearchParam("activityId", activityId.toString(), activityId);
		}
		if (startDate != null) {
			try {
				vo.putSearchParam("startDate", startDate, sdf.parse(startDate));
			} catch (ParseException e) {
			}
		}
		if (endDate != null) {
			try {
				vo.putSearchParam("endDate", endDate, sdf.parse(endDate));
			} catch (ParseException e) {
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
		List<Integer> customerIds = sysUserService.getCustomerIdsByAdminId(userObj.getId()); // 根据员工id查询所属组对应的所有广告商id集合
		if (customerIds != null && customerIds.size() == 0) {
			// 员工对应的广告商id集合为空, 不需要再去查询任务指派列表
			vo.setCount(0);
			vo.setSize(20);
			vo.setStart(0);
			vo.setList(null);
		} else {
			vo.putSearchParam("customerIds", null, customerIds);
			adMonitorTaskService.getPageData(vo);
		}

		// //只能查询自己参与的任务指派
		// if(userObj != null) {
		// Integer assignorId = userObj.getId();
		// vo.putSearchParam("assignorId", assignorId.toString(), assignorId);
		// }

		// if(status != 1) {
		// //查询非待审核的监测指派
		// adMonitorTaskService.getPageData(vo);
		// } else {
		// //查询待审核的监测指派
		// //[1] 先查询审核id的所有待审核的监测任务
		// Map<String, Object> searchMap = new HashMap<>();
		// searchMap.put("status", status); //1: 待指派
		// searchMap.put("assignorId", userObj.getId()); //指派员id
		// List<AdMonitorTaskVo> taskVos =
		// adMonitorTaskService.selectAllByAssessorId(searchMap);
		//
		// if(taskVos != null && taskVos.size() > 0) {
		// //条数大于0, 返回给页面
		// Iterator<AdMonitorTaskVo> iterator = taskVos.iterator();
		// while(iterator.hasNext()) {
		// boolean remove = false; //不用抹去
		// AdMonitorTaskVo taskVo = iterator.next();
		// //通过页面上的activityId做筛选
		// if(activityId != null) {
		// if(taskVo.getActivityId() != activityId) {
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
		//
		// shenheCount = allMonitorTaskUnZhipai.size() - taskVos.size();
		// if(shenheCount < 0) {
		// shenheCount = 0;
		// }
		// model.addAttribute("shenheCount", shenheCount);
		// } else {
		// //条数等于0, 新查询10条或者小于10条没人认领的待审核的监测指派任务(需要匹配 员工 - 组 - 广告商 之间的关系)
		// //List<Integer> customerIds =
		// sysUserService.getCustomerIdsByAdminId(userObj.getId());
		// if(customerIds != null && customerIds.size() > 0) {
		// searchMap.clear();
		// searchMap.put("status", 1);
		// searchMap.put("customerIds", customerIds);
		// searchMap.put("assignorId", userObj.getId());
		// List<AdMonitorTaskVo> monitorTaskVos =
		// adMonitorTaskService.getTenAdMonitorTaskAssignVo(searchMap);
		// vo.setCount(monitorTaskVos.size());
		// vo.setSize(20);
		// vo.setStart(0);
		// vo.setList(monitorTaskVos);
		// shenheCount = allMonitorTaskUnZhipai.size() - monitorTaskVos.size();
		// if(shenheCount < 0) {
		// shenheCount = 0;
		// }
		// model.addAttribute("shenheCount", shenheCount);
		// }
		// }
		// }

		SearchUtil.putToModel(model, vo);

		return PageConst.UP_TASK_LIST;
	}
	
	/**
	 * 选择监测人员页面
	 **/
	@RequiresRoles(value = { "taskadmin", "media", "deptaskadmin", "superadmin" }, logical = Logical.OR)
	@RequestMapping(value = "/selectUserExecute")
	public String toSelectUserExecute(Model model, HttpServletRequest request,
			@RequestParam(value = "mediaId", required = false) Integer mediaId) {
		Map condition = Maps.newHashMap();
		// 指派人员改成指派给媒体人员
		condition.put("usertype", 3); // 3: 媒体监测人员
		if (mediaId != null) {
			AdMedia media = mediaService.getById(mediaId);
			condition.put("operateId", media.getUserId());
		}
		List<SysUserExecute> ues = sysUserExecuteService.getByConditionMap(condition);
		model.addAttribute("userList", ues);

		return PageConst.SELECT_USER_EXECUTE;
	}
	
	  
    /**
     * 通过媒体id查询下属的所有媒体成员
     */
	@RequiresRoles(value ="superadmin")
	@RequestMapping(value = "/selectUserExecuteTask")
	@ResponseBody
    public Model searchMediaName(Model model,HttpServletRequest request,@RequestParam(value = "mediaId", required = false) Integer mediaId) {
    	ResultVo resultVo = new ResultVo();
        try {
        	if (mediaId != null) {
        	
			//AdMedia media = mediaService.getById(mediaId);
			List<SysUserExecute> mediaExecuteName=sysUserExecuteService.selectMediaNameByUserId(mediaId);
			
        	resultVo.setResult(mediaExecuteName);
        	resultVo.setCode(ResultCode.RESULT_SUCCESS.getCode());
        	}
        } catch (Exception ex) {
            ex.printStackTrace();
            resultVo.setCode(ResultCode.RESULT_FAILURE.getCode());
            resultVo.setResultDes("服务忙，请稍后再试");
        }
        model.addAttribute(SysConst.RESULT_KEY, resultVo);
        return model;
    }

	/**
	 * 指派任务
	 **/
	@RequiresRoles(value = { "taskadmin", "media", "deptaskadmin", "superadmin" }, logical = Logical.OR)
	@RequestMapping(value = "/assign")
	@ResponseBody
	public Model assign(Model model, HttpServletRequest request,
			@RequestParam(value = "ids", required = false) String ids,
			@RequestParam(value = "userId", required = false) Integer userId) {
		ResultVo<String> result = new ResultVo<String>();
		Date now = new Date();
		// [1] ids拆分成id集合
		String[] taskIds = ids.split(",");
		// [2] 循环判断每一个id是否已经在redis中. 存在一个即返回错误信息
		for (String taskId : taskIds) {
			// 注意：这里没有考虑批量指派的问题. 如果批量指派, 需要循环放入Redis
			String beginRedisStr = "zhipai_" + taskId + "_begin";
			String finishRedisStr = "zhipai_" + taskId + "_finish";
			if (redisTemplate.opsForValue().get(finishRedisStr) != null
					&& StringUtil.equals(redisTemplate.opsForValue().get(finishRedisStr) + "", "true")) {
				result.setCode(ResultCode.RESULT_FAILURE.getCode());
				result.setResultDes("任务已被指派，请刷新再试！");
				model.addAttribute(SysConst.RESULT_KEY, result);
				return model;
			}
			if (redisTemplate.opsForValue().get(beginRedisStr) != null
					&& StringUtil.equals(redisTemplate.opsForValue().get(beginRedisStr) + "", "true")) {
				result.setCode(ResultCode.RESULT_FAILURE.getCode());
				result.setResultDes("任务正被指派中，请刷新再试！");
				model.addAttribute(SysConst.RESULT_KEY, result);
				return model;
			}
		}
		// [3] 循环放入redis中
		for (String taskId : taskIds) {
			String beginRedisStr = "zhipai_" + taskId + "_begin";
			// 放入Redis缓存处理并发
			redisTemplate.opsForValue().set(beginRedisStr, "true", 60 * 1, TimeUnit.SECONDS); // 设置1分钟超时时间（为了待执行的也可以修改指派）
		}
		result.setCode(ResultCode.RESULT_SUCCESS.getCode());
		result.setResultDes("指派成功");
		model = new ExtendedModelMap();

		try {
			// 获取登录的审核人(员工/部门领导/超级管理员)
			SysUser userObj = (SysUser) ShiroUtils.getSessionAttribute(SessionKey.SESSION_LOGIN_USER.toString());

			adMonitorTaskService.assign(taskIds, userId, userObj.getId());
			// ==========web端指派成功之后根据userId进行app消息推送==============
			Map<String, Object> param = new HashMap<>();
			Map<String, String> extras = new HashMap<>();
			List<String> alias = new ArrayList<>(); // 别名用户List
			alias.add(String.valueOf(userId));
			extras.put("type", "new_assign_push");
			param.put("msg", "您被指派一条新的任务！");
			param.put("title", "玖凤平台");
			param.put("alias", alias); // 根据别名选择推送用户（这里userId用作推送时的用户别名）
			param.put("extras", extras);
			String pushResult = JPushUtils.pushAllByAlias(param);
			System.out.println("pushResult:: " + pushResult);
		} catch (Exception e) {
			// [5] 异常情况, 循环删除redis
			for (String taskId : taskIds) {
				String beginRedisStr = "zhipai_" + taskId + "_begin";
				// 异常情况, 移除Redis缓存处理并发
				redisTemplate.delete(beginRedisStr);
			}
			result.setCode(ResultCode.RESULT_FAILURE.getCode());
			result.setResultDes("指派失败！");
			model.addAttribute(SysConst.RESULT_KEY, result);
			return model;
		}
		// [6] 处理成功, 循环放入redis
		for (String taskId : taskIds) {
			// 放入Redis缓存处理并发
			String finishRedisStr = "zhipai_" + taskId + "_finish";
			redisTemplate.opsForValue().set(finishRedisStr, "true", 60 * 1, TimeUnit.SECONDS); // 设置1分钟超时时间（为了待执行的也可以修改指派）
		}
		model.addAttribute(SysConst.RESULT_KEY, result);
		return model;
	}

	/**
	 * 审核任务
	 **/
	@RequiresRoles(value = { "taskadmin", "deptaskadmin", "superadmin" }, logical = Logical.OR)
	@RequestMapping(value = "/verify")
	@ResponseBody
	public Model verify(Model model, HttpServletRequest request,
			@RequestParam(value = "ids", required = false) String ids,
			@RequestParam(value = "status", required = false) Integer status,
			@RequestParam(value = "reason", required = false) String reason) {
		ResultVo<String> result = new ResultVo<String>();
		Date now = new Date();
		// [1] ids拆分成id集合
		String[] taskIds = ids.split(",");
		// [2] 循环判断每一个id是否已经在redis中. 存在一个即返回错误信息
		for (String taskId : taskIds) {
			String beginRedisStr = "monitorTask_" + taskId + "_begin";
			String finishRedisStr = "monitorTask_" + taskId + "_finish";
			if (redisTemplate.opsForValue().get(finishRedisStr) != null
					&& StringUtil.equals(redisTemplate.opsForValue().get(finishRedisStr) + "", "true")) {
				result.setCode(ResultCode.RESULT_FAILURE.getCode());
				result.setResultDes("任务已被审核，请刷新再试！");
				model.addAttribute(SysConst.RESULT_KEY, result);
				return model;
			}
			if (redisTemplate.opsForValue().get(beginRedisStr) != null
					&& StringUtil.equals(redisTemplate.opsForValue().get(beginRedisStr) + "", "true")) {
				result.setCode(ResultCode.RESULT_FAILURE.getCode());
				result.setResultDes("任务正被审核中，请刷新再试！");
				model.addAttribute(SysConst.RESULT_KEY, result);
				return model;
			}
		}
		// [3] 循环放入redis中
		for (String taskId : taskIds) { 
			String beginRedisStr = "monitorTask_" + taskId + "_begin";
			// 放入Redis缓存处理并发
			redisTemplate.opsForValue().set(beginRedisStr, "true", 60 * 30, TimeUnit.SECONDS); // 设置半小时超时时间
		}
		result.setCode(ResultCode.RESULT_SUCCESS.getCode());
		result.setResultDes("审核成功");
		model = new ExtendedModelMap();
		AdMonitorTask task = new AdMonitorTask();
//		 task.setId(taskId);
		task.setStatus(status);
		try {
			// 获取登录的审核人(员工/部门领导/超级管理员)
			SysUser userObj = (SysUser) ShiroUtils.getSessionAttribute(SessionKey.SESSION_LOGIN_USER.toString());

			if (task.getStatus() == MonitorTaskStatus.VERIFIED.getId()) {
				// adMonitorTaskService.update(task);
				adMonitorTaskService.pass(taskIds, userObj.getId(), status);
			} else {
				adMonitorTaskService.reject(taskIds, reason, userObj.getId(),status);
			}
			
			// [3] 循环推送
			for (String taskId : taskIds) {
				task = adMonitorTaskService.selectByPrimaryKey(Integer.parseInt(taskId));
				// ==========web端任务审核之后根据userId进行app消息推送==============
				Map<String, Object> param = new HashMap<>();
				Map<String, String> extras = new HashMap<>();
				List<String> alias = new ArrayList<>(); // 别名用户List
				alias.add(String.valueOf(task.getUserId())); // 任务执行者
				extras.put("type", "task_audit_push");
				param.put("msg", "您的任务有一条新的后台审核通知！");
				param.put("title", "玖凤平台");
				param.put("alias", alias); // 根据别名选择推送用户（这里userId用作推送时的用户别名）
				param.put("extras", extras);
				String pushResult = JPushUtils.pushAllByAlias(param);
				System.out.println("pushResult:: " + pushResult);
			}
		} catch (Exception e) {
			// [5] 异常情况, 循环删除redis
			for (String taskId : taskIds) {
				String beginRedisStr = "monitorTask_" + taskId + "_begin";
				// 异常情况, 移除Redis缓存处理并发
				redisTemplate.delete(beginRedisStr);
			}
			result.setCode(ResultCode.RESULT_FAILURE.getCode());
			result.setResultDes("审核失败！");
			model.addAttribute(SysConst.RESULT_KEY, result);
			return model;
		}
		// [6] 处理成功, 循环放入redis
		for (String taskId : taskIds) {
			// 放入Redis缓存处理并发
			String finishRedisStr = "monitorTask_" + taskId + "_finish";
			redisTemplate.opsForValue().set(finishRedisStr, "true", 60 * 30, TimeUnit.SECONDS); // 设置半小时超时时间
		}
		model.addAttribute(SysConst.RESULT_KEY, result);
		return model;
	}

	// 撤消审核任务
	@RequiresRoles("taskadmin")
	@RequestMapping(value = "/cancel")
	@ResponseBody
	public Model cancel(Model model, HttpServletRequest request,
			@RequestParam(value = "id", required = false) Integer id,
			@RequestParam(value = "userId", required = false) Integer userId,
			@RequestParam(value = "reason", required = false) String reason) {
		ResultVo<String> result = new ResultVo<String>();
		result.setCode(ResultCode.RESULT_SUCCESS.getCode());
		result.setResultDes("撤消成功");
		model = new ExtendedModelMap();
		AdMonitorTask task = new AdMonitorTask();

		try {
			// 获取当前登录的后台用户信息
			SysUser sysUser = (SysUser) ShiroUtils.getSessionAttribute(SessionKey.SESSION_LOGIN_USER.toString());
			Map<String, Object> searchMap = new HashMap<>();
			searchMap.put("userId", sysUser.getId());
			Integer groupId = sysUserRoleService.selectGroupIdByUserId(searchMap);
			// 获取该组所有员工
			List<SysUser> sysUsers = sysGroupService.selectUserName(groupId);

			if (sysUsers.size() > 1) {// 待审核
				adMonitorTaskService.offAdMonitorTaskByAssessorId(id);
			} else if (sysUsers.size() <= 1) {
				result.setCode(ResultCode.RESULT_FAILURE.getCode());
				result.setResultDes("只剩一人不能撤销！");
				model.addAttribute(SysConst.RESULT_KEY, result);
				return model;
			}
		} catch (Exception e) {
			result.setCode(ResultCode.RESULT_FAILURE.getCode());
			result.setResultDes("撤消失败！");
			model.addAttribute(SysConst.RESULT_KEY, result);
			return model;
		}
		model.addAttribute(SysConst.RESULT_KEY, result);
		return model;
	}

	// 撤消指派任务
	@RequiresRoles("taskadmin")
	@RequestMapping(value = "/cancelZhipai")
	@ResponseBody
	public Model cancelZhipai(Model model, HttpServletRequest request,
			@RequestParam(value = "id", required = false) Integer id,
			@RequestParam(value = "userId", required = false) Integer userId,
			@RequestParam(value = "reason", required = false) String reason) {
		ResultVo<String> result = new ResultVo<String>();
		result.setCode(ResultCode.RESULT_SUCCESS.getCode());
		result.setResultDes("撤消成功");
		model = new ExtendedModelMap();
		AdMonitorTask task = new AdMonitorTask();

		try {
			// 获取当前登录的后台用户信息
			SysUser sysUser = (SysUser) ShiroUtils.getSessionAttribute(SessionKey.SESSION_LOGIN_USER.toString());
			Map<String, Object> searchMap = new HashMap<>();
			searchMap.put("userId", sysUser.getId());
			Integer groupId = sysUserRoleService.selectGroupIdByUserId(searchMap);
			// 获取该组所有员工
			List<SysUser> sysUsers = sysGroupService.selectUserName(groupId);

			if (sysUsers.size() > 1) {// 待指派
				adMonitorTaskService.offAdMonitorTaskByAssignorId(id);
			} else if (sysUsers.size() <= 1) {
				result.setCode(ResultCode.RESULT_FAILURE.getCode());
				result.setResultDes("只剩一人不能撤销！");
				model.addAttribute(SysConst.RESULT_KEY, result);
				return model;
			}
		} catch (Exception e) {
			result.setCode(ResultCode.RESULT_FAILURE.getCode());
			result.setResultDes("撤消失败！");
			model.addAttribute(SysConst.RESULT_KEY, result);
			return model;
		}
		model.addAttribute(SysConst.RESULT_KEY, result);
		return model;
	}

	/**
	 * 关闭问题任务
	 **/
	@RequiresRoles(value = { "taskadmin", "deptaskadmin", "superadmin" }, logical = Logical.OR)
	@RequestMapping(value = "/close")
	@ResponseBody
	public Model close(Model model, HttpServletRequest request,
			@RequestParam(value = "id", required = false) Integer id) {
		ResultVo<String> result = new ResultVo<String>();
		result.setCode(ResultCode.RESULT_SUCCESS.getCode());
		result.setResultDes("关闭成功");
		model = new ExtendedModelMap();

		AdMonitorTask task = new AdMonitorTask();
		task.setProblemStatus(TaskProblemStatus.CLOSED.getId());
		task.setId(id);
		try {
			adMonitorTaskService.update(task);
		} catch (Exception e) {
			result.setCode(ResultCode.RESULT_FAILURE.getCode());
			result.setResultDes("关闭失败！");
			model.addAttribute(SysConst.RESULT_KEY, result);
			return model;
		}

		model.addAttribute(SysConst.RESULT_KEY, result);
		return model;
	}

	/**
	 * 创建子任务
	 **/
	@RequiresRoles(value = { "taskadmin", "deptaskadmin", "superadmin" }, logical = Logical.OR)
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
			adMonitorTaskService.createSubTask(id);
		} catch (Exception e) {
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
	 * 详情页面
	 *
	 * @param taskId
	 * @param model
	 * @param request
	 * @return 详情页面
	 */
	@RequiresRoles(value = { "superadmin", "taskadmin", "customer", "media", "deptaskadmin", "activityadmin",
			"depactivityadmin" }, logical = Logical.OR)
	@RequestMapping(value = "/details")
	public String gotoDetailsPage(@RequestParam("task_Id") String taskId, Model model, HttpServletRequest request) {
		AdMonitorTaskVo vo = adMonitorTaskService.getTaskDetails(taskId);
		List<AdMonitorTaskVo> list = adMonitorTaskService.getSubmitDetails(taskId);

		// 获取当前登录的后台用户信息
		SysUser sysUser = (SysUser) ShiroUtils.getSessionAttribute(SessionKey.SESSION_LOGIN_USER.toString());
		model.addAttribute("usertype", sysUser.getUsertype());
		
		// 获取父任务信息，分监测和纠错
		if (vo.getParentId() != null) {
			if (vo.getParentType() == RewardTaskType.MONITOR.getId()) {
				// 父任务是监测
				model.addAttribute("pmTask", adMonitorTaskService.getTaskVoById(vo.getParentId()));
			} else if (vo.getParentType() == RewardTaskType.JIUCUO.getId()) {
				// 父任务是纠错
				model.addAttribute("pjTask", adJiucuoTaskService.getVoById(vo.getParentId()));
			}
		}

		// 重新设置监测时间段
		vo.setMonitorsStart(vo.getMonitorDate());
		long timestamp = vo.getMonitorDate().getTime() + (24 * 60 * 60 * 1000) * (vo.getMonitorLastDays() - 1);
		vo.setMonitorsEnd(new Date(timestamp));

		if (vo != null && list != null) {
			model.addAttribute("vo", vo);
			model.addAttribute("list", list);
			model.addAttribute("taskId", taskId);
		}
		return PageConst.DETAILS_PAGE;
	}

	/**
	 * 所有任务页面
	 */
	@RequiresRoles("superadmin")
	@RequestMapping(value = "/allList")
	public String gotoAllTaskPage(Model model, HttpServletRequest request,
			@RequestParam(value = "activityId", required = false) Integer activityId,
			@RequestParam(value = "taskType", required = false) Integer taskType,
			@RequestParam(value = "status", required = false) Integer status,
			@RequestParam(value = "problemStatus", required = false) Integer problemStatus) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		SearchDataVo vo = SearchUtil.getVo();

		if (problemStatus != null) {
			vo.putSearchParam("problemStatus", problemStatus.toString(), problemStatus);
		}
		if (activityId != null) {
			vo.putSearchParam("activityId", activityId.toString(), activityId);
		}
		if (taskType != null) {
			vo.putSearchParam("taskType", taskType.toString(), taskType);
		}
		if (status != null) {
			vo.putSearchParam("status", status.toString(), status);
		}

		adMonitorTaskService.getPageDataAllTask(vo);

		SearchUtil.putToModel(model, vo);
		return PageConst.ALLTASK_LIST;
	}
	
	/**
	 * 任务图片更替
	 */
	@RequiresRoles("superadmin")
	@RequestMapping(value = "/changePic")
	@ResponseBody
	public Model changeDetailsPage(Model model, @RequestParam(value ="taskFeedBackId", required = false) Integer id, HttpServletRequest request, HttpServletResponse response,
									@RequestParam(value = "picFile", required = false) MultipartFile file,
									@RequestParam(value = "index", required = false) Integer index,
									@RequestParam(value = "monitorTaskId", required = false) Integer monitorTaskId,
									@RequestParam(value = "lon", required = false) Double lon,
									@RequestParam(value = "lat", required = false) Double lat,
									@RequestParam(value = "userId", required = false) Integer userId) {
		ResultVo<String> result = new ResultVo<String>();
		result.setCode(ResultCode.RESULT_SUCCESS.getCode());
		result.setResultDes("替换成功");
		model = new ExtendedModelMap();
		Date now = new Date();
		
		// 获取当前登录的后台用户信息
		SysUser sysUser = (SysUser) ShiroUtils.getSessionAttribute(SessionKey.SESSION_LOGIN_USER.toString());
		
		/*if(id == null) {
			result.setCode(ResultCode.RESULT_FAILURE.getCode());
			result.setResultDes("替换失败！");
			model.addAttribute(SysConst.RESULT_KEY, result);
			return model;
		}*/
		
		String path = request.getRealPath("/");
		path = path + (path.endsWith(File.separator)?"":File.separatorChar)+"static"+File.separatorChar+"upload"+File.separatorChar;
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
			if(monitorTaskId == null) {
				//[2] 已有feedback的时候替换的图片
				adMonitorTaskService.updatePicUrl(id, filepath, index);
			} else {
				//[3] 没有feedback的时候新增feedback
				AdMonitorTaskFeedback feedback = new AdMonitorTaskFeedback();
				feedback.setCreateTime(now);
				feedback.setUpdateTime(now);
				feedback.setMonitorTaskId(monitorTaskId);
				feedback.setProblem(null);
				feedback.setProblemOther(null);
				feedback.setStatus(1); //1：反馈信息有效
				feedback.setLon(lon); //做任务时的经度
				feedback.setLat(lat); //做任务时的纬度
				if(index == 1) {
					feedback.setPicUrl1(filepath);
				} else if (index == 2) {
					feedback.setPicUrl2(filepath);
				} else if (index == 3) {
					feedback.setPicUrl3(filepath);
				} else if (index == 4) {
					feedback.setPicUrl4(filepath);
				}
				AdSeatInfo seatInfo = adMonitorTaskService.selectLonLatByMonitorTaskId(monitorTaskId);
				if(seatInfo != null) {
					feedback.setSeatLon(seatInfo.getLon()); //做任务时的广告位经度
					feedback.setSeatLat(seatInfo.getLat()); //做任务时的广告位纬度
				}
				//插入新的feedback
				adMonitorTaskService.insertMonitorTaskFeedback(feedback, userId, sysUser.getId());
			}
		} catch (IOException e) {
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
			 fos = new FileOutputStream(path+filename);
			int len = 0;
			byte[] buff = new byte[1024];
			while((len=is.read(buff))>0){
				fos.write(buff);
			}
			return "/static/upload/"+filename;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			if(fos!=null){
				try {
					fos.flush();
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(is!=null){
				try {
					is.close();
				} catch (IOException e) {
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
