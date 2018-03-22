package com.bt.om.web.controller;

import com.bt.om.common.SysConst;
import com.bt.om.common.web.PageConst;
import com.bt.om.entity.*;
import com.bt.om.entity.vo.AdActivityVo;
import com.bt.om.entity.vo.AdJiucuoTaskVo;
import com.bt.om.enums.ActivityStatus;
import com.bt.om.enums.ResultCode;
import com.bt.om.enums.SessionKey;
import com.bt.om.security.ShiroUtils;
import com.bt.om.service.IAdActivityService;
import com.bt.om.service.IAdJiucuoTaskService;
import com.bt.om.util.GsonUtil;
import com.bt.om.util.StringUtil;
import com.bt.om.vo.web.ResultVo;
import com.bt.om.vo.web.SearchDataVo;
import com.bt.om.web.BasicController;
import com.bt.om.web.util.SearchUtil;
import com.google.gson.JsonObject;
import freemarker.template.SimpleDate;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by caiting on 2018/1/17.
 */
@Controller
@RequestMapping(value = "/customer")
public class CustomerActivityControl extends BasicController {

    @Autowired
    private IAdActivityService adActivityService;
    @Autowired
    private IAdJiucuoTaskService adJiucuoTaskService;

    @RequestMapping(value="/activity/list")
    public String customerList(Model model, HttpServletRequest request,
                               @RequestParam(value = "activityId", required = false) Integer activityId,
                               @RequestParam(value = "status", required = false) Integer status,
                               @RequestParam(value = "startDate", required = false) String startDate,
                               @RequestParam(value = "endDate", required = false) String endDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        SysUser user = (SysUser)ShiroUtils.getSessionAttribute(SessionKey.SESSION_LOGIN_USER.toString());

        SearchDataVo vo = SearchUtil.getVo();
        vo.putSearchParam("userId",user.getId().toString(),user.getId().toString());

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

        adActivityService.getPageData(vo);

        SearchUtil.putToModel(model,vo);

        return PageConst.CUSTOMER_ACTIVITY_LIST;
    }


    @RequestMapping(value="/activity/edit")
    public String customerEdit(Model model, HttpServletRequest request,
                               @RequestParam(value = "id", required = false) Integer id) {
        AdActivityVo activity = adActivityService.getVoById(id);

        if(activity!=null){
            model.addAttribute("activity",activity);
        }

        return PageConst.CUSTOMER_ACTIVITY_EDIT;
    }


    @RequestMapping(value="/activity/adseat/edit")
    public String adSeatEdit(Model model, HttpServletRequest request) {


        return PageConst.CUSTOMER_ACTIVITY_ADSEAT_EDIT;
    }

    /**
     * 新增/编辑代理商
     */
    @ResponseBody
    @RequestMapping("/activity/save")
    public Model save(Model model, HttpServletRequest request, HttpServletResponse response,
                      @RequestParam(value = "id", required = false) String id,
                      @RequestParam(value = "activityName", required = false) String activityName,
                      @RequestParam(value = "startDate", required = false) String startDate,
                      @RequestParam(value = "endDate", required = false) String endDate,
                      @RequestParam(value = "area", required = false) String area,
                      @RequestParam(value = "media", required = false) String media,
                      @RequestParam(value = "activeSeat", required = false) String activeSeat) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date now = new Date();
        ResultVo<String> result = new ResultVo<String>();
        result.setCode(ResultCode.RESULT_SUCCESS.getCode());
        result.setResult("保存成功");
        model = new ExtendedModelMap();

        SysUser user = (SysUser)ShiroUtils.getSessionAttribute(SessionKey.SESSION_LOGIN_USER.toString());

        AdActivityVo adActivityVo = new AdActivityVo();
        adActivityVo.setActivityName(activityName);
        try {
            adActivityVo.setStartTime(sdf.parse(startDate));
        } catch (ParseException e) {
            result.setCode(ResultCode.RESULT_FAILURE.getCode());
            result.setResultDes("日期格式有误！");
            model.addAttribute(SysConst.RESULT_KEY, result);
            return model;

        }try {
            adActivityVo.setEndTime(sdf.parse(endDate));
        } catch (ParseException e) {
            result.setCode(ResultCode.RESULT_FAILURE.getCode());
            result.setResultDes("日期格式有误！");
            model.addAttribute(SysConst.RESULT_KEY, result);
            return model;
        }
        //构造活动媒体
        String[] mediaArr = media.split(",");
        for(String mediaId:mediaArr){
            AdActivityMedia am = new AdActivityMedia();
            am.setMediaId(new Integer(mediaId));
            am.setCreateTime(now);
            am.setUpdateTime(now);
            adActivityVo.getActivityMedias().add(am);
        }

        //构造活动地区
        List<JsonObject> areas = GsonUtil.getObjectList(area,JsonObject.class);
        if(areas.size()>0){
            for(JsonObject obj : areas){
                AdActivityArea aa = new AdActivityArea();
                aa.setProvince(obj.get("province").getAsInt());
                aa.setCity(obj.get("city").getAsInt());
                aa.setRegion(obj.get("region").getAsInt());
                aa.setStreet(obj.get("street").getAsInt());
                aa.setCreateTime(now);
                aa.setUpdateTime(now);
                adActivityVo.getActivityAreas().add(aa);
            }
        }

        //构造活动广告位
        List<JsonObject> seats = GsonUtil.getObjectList(activeSeat,JsonObject.class);
        if(seats.size()>0){
            for(JsonObject obj : seats){
                AdActivityAdseat as = new AdActivityAdseat();
                as.setAdSeatId(obj.get("seatId").getAsInt());
                as.setBrand(obj.get("brand").getAsString());
                as.setUpMonitor(obj.get("upMonitor").getAsInt());
                as.setDurationMonitor(obj.get("durationMonitor").getAsInt());
                as.setDownMonitor(obj.get("downMonitor").getAsInt());
                as.setMediaId(obj.get("mediaId").getAsInt());
                as.setMonitorCount(obj.get("monitorCount").getAsInt());
                try {
                    as.setMonitorStart(sdf.parse(obj.get("startDate").getAsString()));
                    as.setMonitorEnd(sdf.parse(obj.get("endDate").getAsString()));
                } catch (ParseException e) {
                    result.setCode(ResultCode.RESULT_FAILURE.getCode());
                    result.setResultDes("日期格式有误！");
                    model.addAttribute(SysConst.RESULT_KEY, result);
                    return model;
                }
                as.setTaskCreate(1);
                as.setSamplePicUrl(obj.get("samplePicUrl").getAsString());
                as.setCreateTime(now);
                as.setUpdateTime(now);
                adActivityVo.getActivitySeats().add(as);
            }
        }

        //新增
        if(StringUtil.isEmpty(id)){
            adActivityVo.setStatus(ActivityStatus.UNCONFIRM.getId());
            adActivityVo.setUserId(user.getId());
            adActivityVo.setCreateTime(now);
            adActivityVo.setUpdateTime(now);
            adActivityService.add(adActivityVo);
        }else{//更新
            adActivityVo.setId(new Integer(id));
            adActivityVo.setUpdateTime(now);
            adActivityService.modify(adActivityVo);
        }

        model.addAttribute(SysConst.RESULT_KEY, result);
        return model;
    }

    @RequestMapping(value="/jiucuo/list")
    public String jiucuoList(Model model, HttpServletRequest request) {

        SearchDataVo vo = SearchUtil.getVo();
        adJiucuoTaskService.getPageData(vo);

        SearchUtil.putToModel(model,vo);

        return PageConst.CUSTOMER_JIUCUO_LIST;
    }

    @RequestMapping(value="/jiucuo/detail")
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
        return PageConst.CUSTOMER_JIUCUO_DETAIL;
    }

//    public static void main(String[] args) {
//        System.out.println(GsonUtil.getObjectList("[{'province':1}]", JsonObject.class));
//    }
}
