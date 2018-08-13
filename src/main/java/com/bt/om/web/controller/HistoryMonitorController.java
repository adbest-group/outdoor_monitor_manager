package com.bt.om.web.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.adtime.common.lang.StringUtil;
import com.bt.om.common.SysConst;
import com.bt.om.common.web.PageConst;
import com.bt.om.entity.CreateHistoryTableRecord;
import com.bt.om.entity.SysResources;
import com.bt.om.entity.SysUser;
import com.bt.om.entity.SysUserRes;
import com.bt.om.entity.vo.AdMonitorTaskVo;
import com.bt.om.entity.vo.UserRoleVo;
import com.bt.om.enums.DepartmentTypeEnum;
import com.bt.om.enums.MonitorTaskStatus;
import com.bt.om.enums.MonitorTaskType;
import com.bt.om.enums.ResultCode;
import com.bt.om.enums.RewardTaskType;
import com.bt.om.enums.SessionKey;
import com.bt.om.enums.UserRoleEnum;
import com.bt.om.enums.UserTypeEnum;
import com.bt.om.filter.LogFilter;
import com.bt.om.security.ShiroUtils;
import com.bt.om.service.IAdActivityService;
import com.bt.om.service.IAdJiucuoTaskService;
import com.bt.om.service.IAdMonitorTaskService;
import com.bt.om.service.IAdUserMessageService;
import com.bt.om.service.ICreateHistoryTableRecordService;
import com.bt.om.service.IHistoryAdJiucuoTaskService;
import com.bt.om.service.IHistoryAdMonitorTaskService;
import com.bt.om.service.ISysGroupService;
import com.bt.om.service.ISysResourcesService;
import com.bt.om.service.ISysUserService;
import com.bt.om.util.HistoryDataMoveUtil;
import com.bt.om.vo.web.ResultVo;
import com.bt.om.vo.web.SearchDataVo;
import com.bt.om.web.BasicController;
import com.bt.om.web.util.SearchUtil;

@Controller
@RequestMapping("/history/monitor")
public class HistoryMonitorController extends BasicController{
	
	@Autowired
    IAdMonitorTaskService adMonitorTaskService;
	@Autowired
	private ISysGroupService sysGroupService;
	@Autowired
	private ISysUserService sysUserService;
	@Autowired
	private ISysResourcesService sysResourcesService;
	@Autowired
	private IAdActivityService adActivityService;
	@Autowired
    private IAdJiucuoTaskService adJiucuoTaskService;
	@Autowired
	private IAdUserMessageService adUserMessageService;
	@Autowired
	private IHistoryAdMonitorTaskService historyAdMonitorTaskService;
	@Autowired
	private ICreateHistoryTableRecordService createHistoryTableRecordService;
	@Autowired
	private IHistoryAdJiucuoTaskService historyAdJiucuoTaskService;
	private static final Logger logger = Logger.getLogger(LogFilter.class);
	
    /**
     * 【任务审核部门】领导/【超级管理员】查看 监测任务审核页面
     */
    @RequiresRoles(value = {"superadmin","phoneoperator"}, logical = Logical.OR)
    @RequestMapping(value = "/taskList")
    public String getTaskList(Model model, HttpServletRequest request,
                              @RequestParam(value = "activityId", required = false) Integer activityId,
                              @RequestParam(value = "taskType", required = false) Integer taskType,
                              @RequestParam(value = "status", required = false) Integer status,
                              @RequestParam(value = "problemStatus", required = false) Integer problemStatus,
                              @RequestParam(value = "startDate", required = false) String startDate,
                              @RequestParam(value = "endDate", required = false) String endDate,
                              @RequestParam(value = "pid", required = false) Integer pid,
                              @RequestParam(value = "ptype", required = false) Integer ptype,
                              @RequestParam(value = "mediaId", required = false) Integer mediaId,
                              @RequestParam(value = "name", required = false) String name,
                              @RequestParam(value = "mediaTypeId", required = false) Integer mediaTypeId,
                              @RequestParam(value = "mediaTypeParentId", required = false) Integer mediaTypeParentId,
                              @RequestParam(value = "province", required = false) Long province,
                              @RequestParam(value = "recordName", required = false) String recordName,
                              @RequestParam(value = "city", required = false) Long city) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SearchDataVo vo = SearchUtil.getVo();
        String tableName = HistoryDataMoveUtil.getTableName(new Date());
        if (recordName != null) {
        	tableName = recordName;
		}
        vo.putSearchParam("tableName", recordName, tableName);
        ShiroUtils.setSessionAttribute("tableName", tableName);
        model.addAttribute("tableName",tableName);
     	//获取登录用户信息
        SysUser userObj = (SysUser) ShiroUtils.getSessionAttribute(SessionKey.SESSION_LOGIN_USER.toString());
        if (activityId != null) {
            vo.putSearchParam("activityId", activityId.toString(), activityId);
        }
        if (taskType != null) {
            vo.putSearchParam("taskType", taskType.toString(), taskType);
        }
        
//        //限制不查询【上刊任务】
//  		List<Integer> notTaskTypes = new ArrayList<>();
//  		notTaskTypes.add(MonitorTaskType.UP_TASK.getId());
//  		vo.putSearchParam("notTaskTypes", null, notTaskTypes);
  		
        if (status != null) {
            vo.putSearchParam("status", status.toString(), status);
            model.addAttribute("status", status);
        }
        if (problemStatus != null) {
            vo.putSearchParam("problemStatus", problemStatus.toString(), problemStatus);
        }
        //pid和ptype配合用于从纠纠错复查监测任务和监测任务查复查监测任务用，好像有点绕
        //如果查纠错的复查监测任务
        if (pid != null && ptype != null) {
            if (ptype == RewardTaskType.JIUCUO.getId()) {
                vo.putSearchParam("parentId", pid.toString(), pid);
                vo.putSearchParam("parentType", ptype.toString(), ptype);
            } else {
                //任务和对应的复查任务都是监测任务，这里一起查出来方便查看，mapper里注意一下写法
                vo.putSearchParam("idpid", pid.toString(), pid);
            }
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
            }
        }
        //查询媒体主
        if (mediaId != null) {
            vo.putSearchParam("mediaId", mediaId.toString(), mediaId);
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
        historyAdMonitorTaskService.getPageData(vo);
        // vo.putSearchParam("hasUserId","1","1");
        List<CreateHistoryTableRecord> records = createHistoryTableRecordService.selectRecord();
        model.addAttribute("records",records);
        SearchUtil.putToModel(model, vo);
        model.addAttribute("user",userObj);
        return PageConst.HISTORY_MONITOR_TASK_LIST;
    }

    /**
	 * 详情页面
	 *
	 * @param taskId
	 * @param model
	 * @param request
	 * @return 详情页面
	 */
	@RequiresRoles(value = { "superadmin","phoneoperator"}, logical = Logical.OR)
	@RequestMapping(value = "/details")
	public String gotoDetailsPage(@RequestParam("task_Id") String taskId, Model model, HttpServletRequest request) {
		String tableName = (String) ShiroUtils.getSessionAttribute("tableName");
		SearchDataVo svo = SearchUtil.getVo();
		svo.putSearchParam("tableName", null, tableName);
		svo.putSearchParam("taskId", null, taskId);
		
		AdMonitorTaskVo vo = historyAdMonitorTaskService.getTaskDetails(svo);
		List<AdMonitorTaskVo> list = historyAdMonitorTaskService.getSubmitDetails(svo);

		// 获取当前登录的后台用户信息
		SysUser sysUser = (SysUser) ShiroUtils.getSessionAttribute(SessionKey.SESSION_LOGIN_USER.toString());
		model.addAttribute("usertype", sysUser.getUsertype());
		
		// 获取父任务信息，分监测和纠错
		if (vo.getParentId() != null) {
				svo.putSearchParam("id", null, vo.getParentId());
			if (vo.getParentType() == RewardTaskType.MONITOR.getId()) {
				// 父任务是监测
				model.addAttribute("pmTask", historyAdMonitorTaskService.getTaskVoById(svo));
			} else if (vo.getParentType() == RewardTaskType.JIUCUO.getId()) {
				// 父任务是纠错
				model.addAttribute("pjTask", historyAdJiucuoTaskService.getVoById(svo));
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
		return PageConst.HISTORY_MONITOR_DETAILS_PAGE;
	}
}
