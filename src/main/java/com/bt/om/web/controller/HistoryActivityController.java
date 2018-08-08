package com.bt.om.web.controller;

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
import com.bt.om.entity.AdMonitorTask;
import com.bt.om.entity.CreateHistoryTableRecord;
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
import com.bt.om.service.IAdUserMessageService;
import com.bt.om.service.ICreateHistoryTableRecordService;
import com.bt.om.service.IHistoryAdActivityService;
import com.bt.om.service.IHistoryAdMonitorTaskService;
import com.bt.om.service.IOperateLogService;
import com.bt.om.service.ISysGroupService;
import com.bt.om.service.ISysResourcesService;
import com.bt.om.service.ISysUserRoleService;
import com.bt.om.service.ISysUserService;
import com.bt.om.util.ConfigUtil;
import com.bt.om.util.GsonUtil;
import com.bt.om.util.HistoryDataMoveUtil;
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
@RequestMapping(value = "/history")
public class HistoryActivityController extends BasicController {

	@Autowired
	private IHistoryAdActivityService historyAdActivityService;
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
	protected RedisTemplate redisTemplate;
	@Autowired
	private IHistoryAdMonitorTaskService historyAdMonitorTaskService;
	@Autowired
	private ICreateHistoryTableRecordService createHistoryTableRecordService;
	private static final Logger logger = Logger.getLogger(HistoryActivityController.class);

	/**
     * 【超级管理员】查看 历史活动页面
     */
    @RequiresRoles(value = {"superadmin", "phoneoperator"}, logical = Logical.OR)
    @RequestMapping(value = "/activity")
    public String customerList(Model model, HttpServletRequest request,
                               @RequestParam(value = "activityId", required = false) Integer activityId,
                               @RequestParam(value = "status", required = false) Integer status,
                               @RequestParam(value = "recordName", required = false) String recordName,
                               @RequestParam(value = "name", required = false) String name,
                               @RequestParam(value = "mediaTypeId", required = false) Integer mediaTypeId,
                               @RequestParam(value = "mediaTypeParentId", required = false) Integer mediaTypeParentId,
                               @RequestParam(value = "province", required = false) String province,
                               @RequestParam(value = "city", required = false) String city,
                               @RequestParam(value = "userId", required = false) Integer userId) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        
        SearchDataVo vo = SearchUtil.getVo();
        //获取登录用户信息
        SysUser userObj = (SysUser) ShiroUtils.getSessionAttribute(SessionKey.SESSION_LOGIN_USER.toString());
        if(userId != null) {
        	vo.putSearchParam("userId", userId.toString(), userId);
        }
        
        if (activityId != null) {
            vo.putSearchParam("activityId", activityId.toString(), activityId);
        }
        
        if (status != null) {
            vo.putSearchParam("status", status.toString(), status);
        }
        String tableName = HistoryDataMoveUtil.getTableName(new Date());
        if (recordName != null) {
        	tableName = recordName;
		}
        vo.putSearchParam("tableName", recordName, tableName);
        if (ShiroUtils.getSessionAttribute("tableName")!=null) {
        	ShiroUtils.removeAttribute("tableName");
		}
        ShiroUtils.setSessionAttribute("tableName", tableName);
        model.addAttribute("tableName",tableName);
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
        historyAdActivityService.getPageData(vo);
        List<CreateHistoryTableRecord> records = createHistoryTableRecordService.selectRecord();
        SearchUtil.putToModel(model, vo);
        model.addAttribute("user",userObj);
        model.addAttribute("records",records);
        return PageConst.HISTORY_ACTIVITY;
    }
   
	// 前往编辑活动
    @RequiresRoles(value = {"superadmin","phoneoperator"}, logical = Logical.OR)
    @RequestMapping(value = "/activity/edit")
    public String customerEdit(Model model, HttpServletRequest request,
                               @RequestParam(value = "id", required = false) Integer id) {
    	SearchDataVo vo = SearchUtil.getVo();
    	String tableName = (String) ShiroUtils.getSessionAttribute("tableName");
		vo.putSearchParam("tableName", null, tableName);
        vo.putSearchParam("id", null, id);
    	AdActivityVo activity = historyAdActivityService.getVoById(vo);

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
        return PageConst.HISTORY_ACTIVITY_EDIT;
    }
	//通过ActivityId查出所有任务列表
	@RequestMapping(value="/activity/selectTaskToExcel")
	public String getActivity(Model model,HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "activityId", required = false) Integer activityId) {
		SysUser userObj = (SysUser) ShiroUtils.getSessionAttribute(SessionKey.SESSION_LOGIN_USER.toString());
		SearchDataVo vo = SearchUtil.getVo();
    	String tableName = (String) ShiroUtils.getSessionAttribute("tableName");
		vo.putSearchParam("tableName", null, tableName);
		vo.putSearchParam("activityId", null, activityId);
		model.addAttribute("tableName",tableName);
		Date now = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		//查询所有任务列表
		List<AdMonitorTask> list = historyAdMonitorTaskService.getAllTasksByActivityId(vo);
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
		return PageConst.HISTORY_SELECT_ALL_TASKS;
	}
	
	//Pdf导出任务列表报告
	@RequestMapping(value="/activity/selectTasksToPdf")
	public String selectTasksToPdf(Model model,HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "activityId", required = false) Integer activityId) {
		SysUser userObj = (SysUser) ShiroUtils.getSessionAttribute(SessionKey.SESSION_LOGIN_USER.toString());
		Date now = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		SearchDataVo vo = SearchUtil.getVo();
    	String tableName = (String) ShiroUtils.getSessionAttribute("tableName");
		vo.putSearchParam("tableName", null, tableName);
		vo.putSearchParam("activityId", null, activityId);
		
		//查询所有任务列表
		List<AdMonitorTask> list = historyAdMonitorTaskService.getAllTasksByActivityId(vo);
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
		return PageConst.HISTORY_SELECT_TASKPDF;
	}
}
