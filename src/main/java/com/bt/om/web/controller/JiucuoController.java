package com.bt.om.web.controller;

import com.bt.om.common.SysConst;
import com.bt.om.common.web.PageConst;
import com.bt.om.entity.AdActivity;
import com.bt.om.entity.AdActivityAdseat;
import com.bt.om.entity.AdJiucuoTask;
import com.bt.om.entity.AdJiucuoTaskFeedback;
import com.bt.om.entity.vo.AdActivityVo;
import com.bt.om.entity.vo.AdJiucuoTaskVo;
import com.bt.om.entity.vo.SysUserVo;
import com.bt.om.enums.ResultCode;
import com.bt.om.enums.SessionKey;
import com.bt.om.security.ShiroUtils;
import com.bt.om.service.IAdActivityService;
import com.bt.om.service.IAdJiucuoTaskService;
import com.bt.om.vo.web.ResultVo;
import com.bt.om.vo.web.SearchDataVo;
import com.bt.om.web.BasicController;
import com.bt.om.web.util.SearchUtil;
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

    @RequestMapping(value="/list")
    public String joucuoList(Model model, HttpServletRequest request,
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

        adJiucuoTaskService.getPageData(vo);
        SearchUtil.putToModel(model,vo);

        return PageConst.JIUCUO_LIST;
    }

    @RequestMapping(value="/detail")
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


        model.addAttribute("task",task);
        model.addAttribute("activity",activity);
        model.addAttribute("seat",seat);
        model.addAttribute("feedback",feedback);
        return PageConst.JIUCUO_DETAIL;
    }

    //审核纠错
    @RequestMapping(value="/verify")
    @ResponseBody
    public Model confirm(Model model, HttpServletRequest request,
                          @RequestParam(value = "id", required = false) Integer id,
                          @RequestParam(value = "status", required = false) Integer status) {
        ResultVo<String> result = new ResultVo<String>();
        result.setCode(ResultCode.RESULT_SUCCESS.getCode());
        result.setResultDes("审核成功");
        model = new ExtendedModelMap();

        AdJiucuoTask task = new AdJiucuoTask();
        task.setId(id);
        task.setStatus(status);
        try{
            adJiucuoTaskService.update(task);
        }catch (Exception e){
            result.setCode(ResultCode.RESULT_FAILURE.getCode());
            result.setResultDes("审核失败！");
            model.addAttribute(SysConst.RESULT_KEY, result);
            return model;
        }


        model.addAttribute(SysConst.RESULT_KEY, result);
        return model;
    }

//    //删除活动
//    @RequestMapping(value="/delete")
//    @ResponseBody
//    public Model delete(Model model, HttpServletRequest request,
//                         @RequestParam(value = "id", required = false) Integer id) {
//        ResultVo<String> result = new ResultVo<String>();
//        result.setCode(ResultCode.RESULT_SUCCESS.getCode());
//        result.setResult("删除成功");
//        model = new ExtendedModelMap();
//        try{
//            adActivityService.delete(id);
//        }catch (Exception e){
//            result.setCode(ResultCode.RESULT_FAILURE.getCode());
//            result.setResultDes("删除失败！");
//            model.addAttribute(SysConst.RESULT_KEY, result);
//            return model;
//        }
//
//
//        model.addAttribute(SysConst.RESULT_KEY, result);
//        return model;
//    }
}
