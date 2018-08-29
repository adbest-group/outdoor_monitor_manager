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
import com.bt.om.entity.AdActivity;
import com.bt.om.entity.AdActivityAdseat;
import com.bt.om.entity.AdJiucuoTaskFeedback;
import com.bt.om.entity.CreateHistoryTableRecord;
import com.bt.om.entity.SysResources;
import com.bt.om.entity.SysUser;
import com.bt.om.entity.SysUserRes;
import com.bt.om.entity.vo.AdJiucuoTaskVo;
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
import com.bt.om.service.IHistoryAdActivityService;
import com.bt.om.service.IHistoryAdJiucuoTaskService;
import com.bt.om.service.ISysGroupService;
import com.bt.om.service.ISysResourcesService;
import com.bt.om.service.ISysUserService;
import com.bt.om.util.HistoryDataMoveUtil;
import com.bt.om.vo.web.ResultVo;
import com.bt.om.vo.web.SearchDataVo;
import com.bt.om.web.BasicController;
import com.bt.om.web.util.SearchUtil;

@Controller
@RequestMapping("/history/jiucuo")
public class HistoryJiucuoController extends BasicController{
	
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
	private IHistoryAdJiucuoTaskService historyAdJiucuoTaskService;
	@Autowired
	private ICreateHistoryTableRecordService createHistoryTableRecordService;
	@Autowired
	private IHistoryAdActivityService historyAdActivityService;
	private static final Logger logger = Logger.getLogger(HistoryJiucuoController.class);
    
    /**
     * 【纠错审核部门】领导/【超级管理员】查看 纠错任务页面
     */
    @RequiresRoles(value = {"superadmin", "phoneoperator"}, logical = Logical.OR)
    @RequestMapping(value = "/jiucuoList")
    public String joucuoList(Model model, HttpServletRequest request,
                             @RequestParam(value = "id", required = false) Integer id,
                             @RequestParam(value = "activityId", required = false) Integer activityId,
                             @RequestParam(value = "status", required = false) Integer status,
                             @RequestParam(value = "problemStatus", required = false) Integer problemStatus,
                             @RequestParam(value = "mediaId", required = false) Integer mediaId,
                             @RequestParam(value = "name", required = false) String name,
                             @RequestParam(value = "recordName", required = false) String recordName,
                             @RequestParam(value = "mediaTypeId", required = false) Integer mediaTypeId,
                             @RequestParam(value = "mediaTypeParentId", required = false) Integer mediaTypeParentId,
                             @RequestParam(value = "province", required = false) Long province,
                             @RequestParam(value = "city", required = false) Long city) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SearchDataVo vo = SearchUtil.getVo();
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
        //获取登录用户信息
        SysUser userObj = (SysUser) ShiroUtils.getSessionAttribute(SessionKey.SESSION_LOGIN_USER.toString());
        if (id != null) {
            vo.putSearchParam("id", id.toString(), id);
        }
        if (activityId != null) {
            vo.putSearchParam("activityId", activityId.toString(), activityId);
        }
        if (status != null) {
            vo.putSearchParam("status", status.toString(), status);
        }
        
        if (problemStatus != null) {
            vo.putSearchParam("problemStatus", problemStatus.toString(), problemStatus);
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
        historyAdJiucuoTaskService.getPageData(vo);
        List<CreateHistoryTableRecord> records = createHistoryTableRecordService.selectRecord();
        SearchUtil.putToModel(model, vo);
        model.addAttribute("user",userObj);
        model.addAttribute("records",records);
        return PageConst.HISTORY_JIUCUO_LIST;
    }
    /**
	 * 查看纠错详情
	 */
	@RequestMapping(value = "/detail")
	public String showDetail(Model model, HttpServletRequest request,
			@RequestParam(value = "id", required = false) Integer id) {
		String tableName = (String) ShiroUtils.getSessionAttribute("tableName");
		// 获取当前登录的后台用户信息
		SysUser sysUser = (SysUser) ShiroUtils.getSessionAttribute(SessionKey.SESSION_LOGIN_USER.toString());
		model.addAttribute("usertype", sysUser.getUsertype());
		SearchDataVo vo = SearchUtil.getVo();
		vo.putSearchParam("tableName", null, tableName);
		vo.putSearchParam("id", null, id);
		// 因当前纠错发起的子监测任务
		List<AdMonitorTaskVo> subs = historyAdJiucuoTaskService.getSubTask(vo);
		// 纠错任务
		AdJiucuoTaskVo task = historyAdJiucuoTaskService.getVoById(vo);
		// 上传内容
		AdJiucuoTaskFeedback feedback = historyAdJiucuoTaskService.getFeadBackByTaskId(vo);
		// 广告活动
		vo.putSearchParam("id", null, task.getActivityId());
		AdActivity activity = historyAdActivityService.getVoById(vo);
		// 广告活动广告位
		vo.putSearchParam("id", null, task.getActivityAdseatId());
		AdActivityAdseat seat = historyAdActivityService.getActivitySeatById(vo);

		model.addAttribute("task", task);
		model.addAttribute("activity", activity);
		model.addAttribute("seat", seat);
		model.addAttribute("feedback", feedback);
		model.addAttribute("subs", subs);
		return PageConst.HISTORY_JIUCUO_DETAIL;
	}
}
