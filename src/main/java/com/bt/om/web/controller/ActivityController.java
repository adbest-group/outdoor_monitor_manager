package com.bt.om.web.controller;

import com.bt.om.common.SysConst;
import com.bt.om.common.web.PageConst;
import com.bt.om.entity.SysUser;
import com.bt.om.entity.vo.AdActivityAdseatVo;
import com.bt.om.entity.vo.AdActivityVo;
import com.bt.om.entity.vo.SysUserVo;
import com.bt.om.enums.ResultCode;
import com.bt.om.enums.SessionKey;
import com.bt.om.security.ShiroUtils;
import com.bt.om.service.IAdActivityService;
import com.bt.om.util.GsonUtil;
import com.bt.om.util.QRcodeUtil;
import com.bt.om.vo.api.QRCodeInfoVo;
import com.bt.om.vo.web.ResultVo;
import com.bt.om.vo.web.SearchDataVo;
import com.bt.om.web.BasicController;
import com.bt.om.web.util.SearchUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Random;

/**
 * Created by caiting on 2018/1/20.
 */
@Controller
@RequestMapping(value = "/activity")
public class ActivityController  extends BasicController {

    @Autowired
    private IAdActivityService adActivityService;

    @RequestMapping(value="/list")
    public String customerList(Model model, HttpServletRequest request,
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

        adActivityService.getPageData(vo);

        SearchUtil.putToModel(model,vo);

        return PageConst.ACTIVITY_LIST;
    }

    //前往编辑活动
    @RequestMapping(value="/edit")
    public String customerEdit(Model model, HttpServletRequest request,
                               @RequestParam(value = "id", required = false) Integer id) {
        AdActivityVo activity = adActivityService.getVoById(id);

        if(activity!=null){
            model.addAttribute("activity",activity);
        }

        return PageConst.ACTIVITY_EDIT;
    }

    //确认活动
    @RequestMapping(value="/confirm")
    @ResponseBody
    public Model confirm(Model model, HttpServletRequest request,
                          @RequestParam(value = "id", required = false) Integer id) {
        ResultVo<String> result = new ResultVo<String>();
        result.setCode(ResultCode.RESULT_SUCCESS.getCode());
        result.setResultDes("确认成功");
        model = new ExtendedModelMap();
        try{
            adActivityService.confirm(id);
        }catch (Exception e){
            result.setCode(ResultCode.RESULT_FAILURE.getCode());
            result.setResultDes("确认失败！");
            model.addAttribute(SysConst.RESULT_KEY, result);
            return model;
        }


        model.addAttribute(SysConst.RESULT_KEY, result);
        return model;
    }

    //删除活动
    @RequestMapping(value="/delete")
    @ResponseBody
    public Model delete(Model model, HttpServletRequest request,
                         @RequestParam(value = "id", required = false) Integer id) {
        ResultVo<String> result = new ResultVo<String>();
        result.setCode(ResultCode.RESULT_SUCCESS.getCode());
        result.setResultDes("删除成功");
        model = new ExtendedModelMap();
        try{
            adActivityService.delete(id);
        }catch (Exception e){
            result.setCode(ResultCode.RESULT_FAILURE.getCode());
            result.setResultDes("删除失败！");
            model.addAttribute(SysConst.RESULT_KEY, result);
            return model;
        }


        model.addAttribute(SysConst.RESULT_KEY, result);
        return model;
    }

    /**
     *  根据活动广告位关联id，获取二维码
     **/
    @RequestMapping(value = {"/getQrcode"}, method = RequestMethod.GET)
    public void getCode(HttpServletRequest request, HttpServletResponse response,
                        @RequestParam(value = "id", required = false) Integer adActivityAdseatId) throws Exception {

        QRCodeInfoVo vo = new QRCodeInfoVo((AdActivityAdseatVo) adActivityService.getActivitySeatById(adActivityAdseatId));

        // 将内存中的图片发送到客户端
        response.setContentType("image/jpg");
        QRcodeUtil.encode(GsonUtil.GsonString(vo),response.getOutputStream());
    }
}
