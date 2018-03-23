package com.bt.om.web.controller;

import com.bt.om.common.SysConst;
import com.bt.om.common.web.PageConst;
import com.bt.om.entity.AdMonitorTask;
import com.bt.om.entity.SysUserExecute;
import com.bt.om.entity.vo.AdMonitorTaskVo;
import com.bt.om.enums.*;
import com.bt.om.service.IAdJiucuoTaskService;
import com.bt.om.service.IAdMonitorTaskService;
import com.bt.om.service.ISysUserExecuteService;
import com.bt.om.vo.web.ResultVo;
import com.bt.om.vo.web.SearchDataVo;
import com.bt.om.web.BasicController;
import com.bt.om.web.util.SearchUtil;
import com.google.common.collect.Maps;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    /**
     * 监测管理，已分配任务
     **/
    @RequiresRoles("admin")
    @RequestMapping(value = "/list")
    public String getTaskList(Model model, HttpServletRequest request,
                              @RequestParam(value = "activityId", required = false) Integer activityId,
                              @RequestParam(value = "taskType", required = false) Integer taskType,
                              @RequestParam(value = "status", required = false) Integer status,
                              @RequestParam(value = "problemStatus", required = false) Integer problemStatus,
                              @RequestParam(value = "startDate", required = false) String startDate,
                              @RequestParam(value = "endDate", required = false) String endDate,
                              @RequestParam(value = "pid", required = false) Integer pid,
                              @RequestParam(value = "ptype", required = false) Integer ptype) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SearchDataVo vo = SearchUtil.getVo();

        if (activityId != null) {
            vo.putSearchParam("activityId", activityId.toString(), activityId);
        }
        if (taskType != null) {
            vo.putSearchParam("taskType", taskType.toString(), taskType);
        }
        if (status != null) {
            vo.putSearchParam("status", status.toString(), status);
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

        return PageConst.TASK_LIST;
    }

    /**
     * 监测管理，未分配任务
     **/
    @RequiresRoles("admin")
    @RequestMapping(value = "/unassign")
    public String getUnAssignList(Model model, HttpServletRequest request,
                                  @RequestParam(value = "activityId", required = false) Integer activityId,
                                  @RequestParam(value = "startDate", required = false) String startDate,
                                  @RequestParam(value = "endDate", required = false) String endDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SearchDataVo vo = SearchUtil.getVo();
        vo.putSearchParam("status", String.valueOf(MonitorTaskStatus.UNASSIGN.getId()),
                String.valueOf(MonitorTaskStatus.UNASSIGN.getId()));
        //运营平台指派任务只指派监测期间的任务
        vo.putSearchParam("taskTypes", null, new Integer[]{MonitorTaskType.UP_MONITOR.getId(),MonitorTaskType.DURATION_MONITOR.getId(),MonitorTaskType.DOWNMONITOR.getId(), MonitorTaskType.FIX_CONFIRM.getId()});

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

        adMonitorTaskService.getPageData(vo);
        SearchUtil.putToModel(model, vo);

        return PageConst.UNASSIGN_TASK_LIST;
    }

    /**
     * 选择监测人员页面
     **/
    @RequiresRoles(value = {"admin", "media"}, logical = Logical.OR)
    @RequestMapping(value = "/selectUserExecute")
    public String toSelectUserExecute(Model model, HttpServletRequest request) {

        Map condition = Maps.newHashMap();
        condition.put("usertype", 1);

        List<SysUserExecute> ues = sysUserExecuteService.getByConditionMap(condition);
        model.addAttribute("userList", ues);

        return PageConst.SELECT_USER_EXECUTE;
    }

    // 分配任务
    @RequiresRoles(value = {"admin","media"},logical = Logical.OR)
    @RequestMapping(value = "/assign")
    @ResponseBody
    public Model assign(Model model, HttpServletRequest request,
                        @RequestParam(value = "ids", required = false) String ids,
                        @RequestParam(value = "userId", required = false) Integer userId) {
        ResultVo<String> result = new ResultVo<String>();
        result.setCode(ResultCode.RESULT_SUCCESS.getCode());
        result.setResultDes("指派成功");
        model = new ExtendedModelMap();

        String[] taskIds = ids.split(",");
        try {
            adMonitorTaskService.assign(taskIds, userId);
        } catch (Exception e) {
            result.setCode(ResultCode.RESULT_FAILURE.getCode());
            result.setResultDes("指派失败！");
            model.addAttribute(SysConst.RESULT_KEY, result);
            return model;
        }

        model.addAttribute(SysConst.RESULT_KEY, result);
        return model;
    }

    // 审核纠错
    @RequiresRoles("admin")
    @RequestMapping(value = "/verify")
    @ResponseBody
    public Model verify(Model model, HttpServletRequest request,
                        @RequestParam(value = "id", required = false) Integer id,
                        @RequestParam(value = "status", required = false) Integer status,
                        @RequestParam(value = "reason", required = false) String reason) {
        ResultVo<String> result = new ResultVo<String>();
        result.setCode(ResultCode.RESULT_SUCCESS.getCode());
        result.setResultDes("审核成功");
        model = new ExtendedModelMap();

        AdMonitorTask task = new AdMonitorTask();
        task.setId(id);
        task.setStatus(status);
        try {
            if (task.getStatus() == MonitorTaskStatus.VERIFIED.getId()) {
                // adMonitorTaskService.update(task);
                adMonitorTaskService.pass(task);
            } else {
                adMonitorTaskService.reject(task, reason);
            }
        } catch (Exception e) {
            result.setCode(ResultCode.RESULT_FAILURE.getCode());
            result.setResultDes("审核失败！");
            model.addAttribute(SysConst.RESULT_KEY, result);
            return model;
        }

        model.addAttribute(SysConst.RESULT_KEY, result);
        return model;
    }

    //关闭问题任务
    @RequiresRoles("admin")
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

    //创建子任务
    @RequiresRoles("admin")
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
    @RequiresRoles(value = {"admin", "customer", "media"}, logical = Logical.OR)
    @RequestMapping(value = "/details")
    public String gotoDetailsPage(@RequestParam("task_Id") String taskId, Model model, HttpServletRequest request) {
        AdMonitorTaskVo vo = adMonitorTaskService.getTaskDetails(taskId);
        List<AdMonitorTaskVo> list = adMonitorTaskService.getSubmitDetails(taskId);

        //获取父任务信息，分监测和纠错
        if(vo.getParentId()!=null){
            if(vo.getParentType() == RewardTaskType.MONITOR.getId()){
                //父任务是监测
                model.addAttribute("pmTask", adMonitorTaskService.getTaskVoById(vo.getParentId()));
            }else if(vo.getParentType() == RewardTaskType.JIUCUO.getId()){
                //父任务是纠错
                model.addAttribute("pjTask",adJiucuoTaskService.getVoById(vo.getParentId()));
            }
        }

        if (vo != null && list != null) {
            model.addAttribute("vo", vo);
            model.addAttribute("list", list);
            model.addAttribute("taskId", taskId);
        }
        return PageConst.DETAILS_PAGE;
    }

}
