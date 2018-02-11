package com.bt.om.web.controller;

import com.bt.om.common.SysConst;
import com.bt.om.common.web.PageConst;
import com.bt.om.entity.*;
import com.bt.om.entity.vo.AdJiucuoTaskVo;
import com.bt.om.entity.vo.AdMonitorTaskVo;
import com.bt.om.enums.*;
import com.bt.om.security.ShiroUtils;
import com.bt.om.service.IAdActivityService;
import com.bt.om.service.IAdJiucuoTaskService;
import com.bt.om.service.IAdMonitorTaskService;
import com.bt.om.service.ISysUserExecuteService;
import com.bt.om.vo.web.ResultVo;
import com.bt.om.vo.web.SearchDataVo;
import com.bt.om.web.util.SearchUtil;
import com.google.common.collect.Maps;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

/**
 * Created by caiting on 2018/2/5.
 */
@Controller
@RequestMapping(value = "/platmedia")
public class MediaManagerController {
    @Autowired
    private IAdMonitorTaskService adMonitorTaskService;
    @Autowired
    ISysUserExecuteService sysUserExecuteService;
    @Autowired
    IAdJiucuoTaskService adJiucuoTaskService;
    @Autowired
    IAdActivityService adActivityService;

    /**
     * 媒体端任务管理，主要分配任务
     **/
    @RequiresRoles("media")
    @RequestMapping(value = "/task/list")
    public String getTaskList(Model model, HttpServletRequest request,
                              @RequestParam(value = "activityId", required = false) Integer activityId,
//                              @RequestParam(value = "status", required = false) Integer status,
                              @RequestParam(value = "startDate", required = false) String startDate,
                              @RequestParam(value = "endDate", required = false) String endDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SearchDataVo vo = SearchUtil.getVo();

        if (activityId != null) {
            vo.putSearchParam("activityId", activityId.toString(), activityId);
        }

        //只显示待指派的
        vo.putSearchParam("status", null, MonitorTaskStatus.UNASSIGN.getId());
        //只显示上刊和下刊任务
        vo.putSearchParam("taskTypes", null, new Integer[]{MonitorTaskType.UP_MONITOR.getId(), MonitorTaskType.DOWNMONITOR.getId()});
        //只显示本媒体广告位的任务
        SysUser user = (SysUser) ShiroUtils.getSessionAttribute(SessionKey.SESSION_LOGIN_USER.toString());
        vo.putSearchParam("mediaUserId", null, user.getId());

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

        // vo.putSearchParam("hasUserId","1","1");
        adMonitorTaskService.getPageData(vo);

        SearchUtil.putToModel(model, vo);

        return PageConst.MEDIA_TASK_LIST;
    }

    /**
     * 媒体端报错任务列表
     **/
    @RequiresRoles("media")
    @RequestMapping(value = "/task/problemList")
    public String getTaskHasProblemList(Model model, HttpServletRequest request,
                                        @RequestParam(value = "activityId", required = false) Integer activityId,
                                        @RequestParam(value = "problemStatus", required = false) Integer problemStatus,
                                        @RequestParam(value = "startDate", required = false) String startDate,
                                        @RequestParam(value = "endDate", required = false) String endDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SearchDataVo vo = SearchUtil.getVo();

        if (activityId != null) {
            vo.putSearchParam("activityId", activityId.toString(), activityId);
        }

        //只显示已审核通过带有问题的任务
        if (problemStatus == null) {
            vo.putSearchParam("problemStatuses", null, new Integer[]{TaskProblemStatus.PROBLEM.getId(), TaskProblemStatus.FIXED.getId(), TaskProblemStatus.CLOSED.getId()});
        } else {
            vo.putSearchParam("problemStatus", problemStatus.toString(), problemStatus);
        }
        vo.putSearchParam("status", null, MonitorTaskStatus.VERIFIED.getId());
        //只显示本媒体广告位的任务
        SysUser user = (SysUser) ShiroUtils.getSessionAttribute(SessionKey.SESSION_LOGIN_USER.toString());
        vo.putSearchParam("mediaUserId", null, user.getId());

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

        // vo.putSearchParam("hasUserId","1","1");
        adMonitorTaskService.getPageData(vo);

        SearchUtil.putToModel(model, vo);

        return PageConst.MEDIA_PROBLEM_TASK_LIST;
    }

    /**
     * 选择监测人员页面
     **/
    @RequiresRoles("media")
    @RequestMapping(value = "/selectUserExecute")
    public String toSelectUserExecute(Model model, HttpServletRequest request) {

        SysUser user = (SysUser) ShiroUtils.getSessionAttribute(SessionKey.SESSION_LOGIN_USER.toString());

        Map condition = Maps.newHashMap();
        condition.put("operateId", user.getId());

        List<SysUserExecute> ues = sysUserExecuteService.getByConditionMap(condition);
        model.addAttribute("userList", ues);

        return PageConst.SELECT_USER_EXECUTE;
    }


    //处理问题，只是标示该任务的问题已被处理
    @RequiresRoles("media")
    @RequestMapping(value = "/task/fix")
    @ResponseBody
    public Model fix(Model model, HttpServletRequest request,
                     @RequestParam(value = "id", required = false) Integer id) {
        ResultVo<String> result = new ResultVo<String>();
        result.setCode(ResultCode.RESULT_SUCCESS.getCode());
        result.setResultDes("处理成功");
        model = new ExtendedModelMap();

        AdMonitorTask task = new AdMonitorTask();
        task.setProblemStatus(TaskProblemStatus.FIXED.getId());
        task.setId(id);
        try {
            adMonitorTaskService.update(task);
        } catch (Exception e) {
            result.setCode(ResultCode.RESULT_FAILURE.getCode());
            result.setResultDes("处理失败！");
            model.addAttribute(SysConst.RESULT_KEY, result);
            return model;
        }

        model.addAttribute(SysConst.RESULT_KEY, result);
        return model;
    }

    @RequestMapping(value = "/jiucuo/list")
    @RequiresRoles("media")
    public String joucuoList(Model model, HttpServletRequest request,
                             @RequestParam(value = "activityId", required = false) Integer activityId,
                             @RequestParam(value = "problemStatus", required = false) Integer problemStatus,
                             @RequestParam(value = "startDate", required = false) String startDate,
                             @RequestParam(value = "endDate", required = false) String endDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SearchDataVo vo = SearchUtil.getVo();

        if (activityId != null) {
            vo.putSearchParam("activityId", activityId.toString(), activityId);
        }
        //只查询处于审核通过，有问题，已解决或闭环的纠错
        if (problemStatus != null) {
            vo.putSearchParam("problemStatus", problemStatus.toString(), problemStatus);
        } else {
            vo.putSearchParam("problemStatuses", null, new Integer[]{TaskProblemStatus.PROBLEM.getId(), TaskProblemStatus.FIXED.getId(), TaskProblemStatus.CLOSED.getId()});
        }
        vo.putSearchParam("status", null, JiucuoTaskStatus.VERIFIED.getId());
        //只查询本媒体广告位有关的纠错
        SysUser user = (SysUser) ShiroUtils.getSessionAttribute(SessionKey.SESSION_LOGIN_USER.toString());
        vo.putSearchParam("mediaUserId", null, user.getId());

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

        adJiucuoTaskService.getPageData(vo);
        SearchUtil.putToModel(model, vo);

        return PageConst.MEDIA_JIUCUO_LIST;
    }

    @RequiresRoles("media")
    @RequestMapping(value = "/jiucuo/detail")
    public String showDetail(Model model, HttpServletRequest request,
                             @RequestParam(value = "id", required = false) Integer id) {
        //纠错任务
        AdJiucuoTaskVo task = adJiucuoTaskService.getVoById(id);
        //上传内容
        AdJiucuoTaskFeedback feedback = adJiucuoTaskService.getFeadBackByTaskId(id);
        //广告活动
        AdActivity activity = adActivityService.getVoById(task.getActivityId());
        //广告活动广告位
        AdActivityAdseat seat = adActivityService.getActivitySeatById(task.getActivityAdseatId());
        //因当前纠错发起的子监测任务
        List<AdMonitorTaskVo> subs = adJiucuoTaskService.getSubTask(id);

        model.addAttribute("task", task);
        model.addAttribute("activity", activity);
        model.addAttribute("seat", seat);
        model.addAttribute("feedback", feedback);
        model.addAttribute("subs", subs);
        return PageConst.CUSTOMER_JIUCUO_DETAIL;
    }

    //处理纠错问题，只是标示该纠错任务的问题已被处理
    @RequiresRoles("media")
    @RequestMapping(value = "/jiucuo/fix")
    @ResponseBody
    public Model jiucuoFix(Model model, HttpServletRequest request,
                           @RequestParam(value = "id", required = false) Integer id) {
        ResultVo<String> result = new ResultVo<String>();
        result.setCode(ResultCode.RESULT_SUCCESS.getCode());
        result.setResultDes("处理成功");
        model = new ExtendedModelMap();

        AdJiucuoTask task = new AdJiucuoTask();
        task.setProblemStatus(TaskProblemStatus.FIXED.getId());
        task.setId(id);
        try {
            adJiucuoTaskService.update(task);
        } catch (Exception e) {
            result.setCode(ResultCode.RESULT_FAILURE.getCode());
            result.setResultDes("处理失败！");
            model.addAttribute(SysConst.RESULT_KEY, result);
            return model;
        }

        model.addAttribute(SysConst.RESULT_KEY, result);
        return model;
    }
}
