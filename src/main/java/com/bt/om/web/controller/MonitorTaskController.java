package com.bt.om.web.controller;

import com.bt.om.common.SysConst;
import com.bt.om.common.web.PageConst;
import com.bt.om.entity.AdJiucuoTask;
import com.bt.om.entity.AdMonitorTask;
import com.bt.om.entity.vo.AdMonitorTaskVo;
import com.bt.om.enums.MonitorTaskStatus;
import com.bt.om.enums.ResultCode;
import com.bt.om.service.IAdMonitorTaskService;
import com.bt.om.vo.web.ResultVo;
import com.bt.om.vo.web.SearchDataVo;
import com.bt.om.web.BasicController;
import com.bt.om.web.Pagination.Page;
import com.bt.om.web.util.SearchUtil;
import com.sun.javafx.collections.MappingChange.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;

/**
 * Created by caiting on 2018/1/20.
 */
@Controller
@RequestMapping("/task")
public class MonitorTaskController extends BasicController {
    @Autowired
    IAdMonitorTaskService adMonitorTaskService;

    /**
     * 监测管理，已分配任务
     **/
    @RequestMapping(value="/list")
    public String getTaskList(Model model, HttpServletRequest request,
                              @RequestParam(value = "activityId", required = false) Integer activityId,
                              @RequestParam(value = "status", required = false) Integer status,
                              @RequestParam(value = "startDate", required = false) String startDate,
                              @RequestParam(value = "endDate", required = false) String endDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SearchDataVo vo = SearchUtil.getVo();

        if(activityId != null){
            vo.putSearchParam("activityId",activityId.toString(),activityId);
        }
        if(status!=null){
            vo.putSearchParam("status",status.toString(),status);
        }
        if(startDate!=null){
            try {
                vo.putSearchParam("startDate",startDate,sdf.parse(startDate));
            } catch (ParseException e) {}
        }
        if(endDate!=null){
            try {
                vo.putSearchParam("endDate",endDate,sdf.parse(endDate));
            } catch (ParseException e) {}
        }

//        vo.putSearchParam("hasUserId","1","1");
        adMonitorTaskService.getPageData(vo);

        SearchUtil.putToModel(model,vo);

        return PageConst.TASK_LIST;
    }

    /**
     * 监测管理，未分配任务
     **/
    @RequestMapping(value="/unassign")
    public String getUnAssignList(Model model, HttpServletRequest request,
                                  @RequestParam(value = "activityId", required = false) Integer activityId,
                                  @RequestParam(value = "startDate", required = false) String startDate,
                                  @RequestParam(value = "endDate", required = false) String endDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SearchDataVo vo = SearchUtil.getVo();
        vo.putSearchParam("status", String.valueOf(MonitorTaskStatus.UNASSIGN.getId()),String.valueOf(MonitorTaskStatus.UNASSIGN.getId()));



        if(activityId != null){
            vo.putSearchParam("activityId",activityId.toString(),activityId);
        }
        if(startDate!=null){
            try {
                vo.putSearchParam("startDate",startDate,sdf.parse(startDate));
            } catch (ParseException e) {}
        }
        if(endDate!=null){
            try {
                vo.putSearchParam("endDate",endDate,sdf.parse(endDate));
            } catch (ParseException e) {}
        }

        adMonitorTaskService.getPageData(vo);
        SearchUtil.putToModel(model,vo);

        return PageConst.UNASSIGN_TASK_LIST;
    }


    /**
     * 选择监测人员页面
     **/
    @RequestMapping(value="/selectUserExecute")
    public String toSelectUserExecute(Model model, HttpServletRequest request) {

        return PageConst.SELECT_USER_EXECUTE;
    }

    //删除活动
    @RequestMapping(value="/assign")
    @ResponseBody
    public Model delete(Model model, HttpServletRequest request,
                        @RequestParam(value = "ids", required = false) String ids,
                        @RequestParam(value = "userId", required = false) Integer userId) {
        ResultVo<String> result = new ResultVo<String>();
        result.setCode(ResultCode.RESULT_SUCCESS.getCode());
        result.setResultDes("指派成功");
        model = new ExtendedModelMap();

        String[] taskIds = ids.split(",");
        try{
            adMonitorTaskService.assign(taskIds,userId);
        }catch (Exception e){
            result.setCode(ResultCode.RESULT_FAILURE.getCode());
            result.setResultDes("指派失败！");
            model.addAttribute(SysConst.RESULT_KEY, result);
            return model;
        }


        model.addAttribute(SysConst.RESULT_KEY, result);
        return model;
    }

    //审核纠错
    @RequestMapping(value="/verify")
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
        try{
            if(task.getStatus()==MonitorTaskStatus.VERIFIED.getId()){
//                adMonitorTaskService.update(task);
                adMonitorTaskService.pass(task);
            }else{
                adMonitorTaskService.reject(task,reason);
            }
        }catch (Exception e){
            result.setCode(ResultCode.RESULT_FAILURE.getCode());
            result.setResultDes("审核失败！");
            model.addAttribute(SysConst.RESULT_KEY, result);
            return model;
        }


        model.addAttribute(SysConst.RESULT_KEY, result);
        return model;
    }

	/**
	 * 查看任务详情
	 * 
	 * @param taskId
	 * @param model
	 * @param request
	 * @return 详情页面
	 */

	/*
	 * @RequestMapping(value = "/details") public String
	 * gotoDetailsPage(@RequestParam("task_Id") String
	 * taskId, @RequestParam("media_Name") String mediaName, Model model,
	 * HttpServletRequest request) { AdMonitorTaskVo vo =
	 * adMonitorTaskService.getTaskDetails(taskId, mediaName); if (vo != null) {
	 * model.addAttribute("vo", vo); } return PageConst.DETAILS_PAGE; }
	 */

	@RequestMapping(value = "/details", method = RequestMethod.GET)
	@ResponseBody
	private HashMap<String, Object> gotoDetailsPage(@RequestParam("task_Id") String taskId,
			@RequestParam("media_Name") String mediaName, HttpServletRequest request) {
		HashMap<String, Object> modelMap = new HashMap<String, Object>();
		AdMonitorTaskVo vo = new AdMonitorTaskVo();
		try {
			vo = adMonitorTaskService.getTaskDetails(taskId, mediaName);
			List<AdMonitorTaskVo> list = adMonitorTaskService.getSubmitDetails(taskId);
			modelMap.put("list", list);
			modelMap.put("vo", vo);
			modelMap.put("success", true);
			return modelMap;
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
		}
		return modelMap;
	}

	/**
	 * 路由
	 * 
	 * @param request
	 * @param taskId
	 * @param mediaName
	 * @param model
	 * @return DETAILS_PAGE
	 */
	@RequestMapping(value = "/gotoDetailsPage", method = RequestMethod.GET)
	private String gotoDetailPage(HttpServletRequest request, @RequestParam("task_Id") String taskId,
			@RequestParam("media_Name") String mediaName, Model model) {
		model.addAttribute("taskId", taskId);
		model.addAttribute("mediaName", mediaName);
		return PageConst.DETAILS_PAGE;
	}

}
