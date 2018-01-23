package com.bt.om.web.controller.api;

import com.bt.om.common.SysConst;
import com.bt.om.entity.AdActivity;
import com.bt.om.enums.ResultCode;
import com.bt.om.service.IAdActivityService;
import com.bt.om.vo.web.ResultVo;
import com.bt.om.web.BasicController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by caiting on 2018/1/22.
 */
@RestController
@RequestMapping(value = "/api")
public class ApiController extends BasicController {

    @Autowired
    private IAdActivityService adActivityService;

    //测试用
    @RequestMapping(value="/aaa/bbb/aaa")
    @ResponseBody
    public Model confirm(Model model, HttpServletRequest request,
                         @RequestParam(value = "id", required = false) Integer id) {
        ResultVo<AdActivity> result = new ResultVo<AdActivity>();
        result.setCode(ResultCode.RESULT_SUCCESS.getCode());
        result.setResultDes("确认成功");
        result.setResult(adActivityService.getVoById(2));
        model = new ExtendedModelMap();

        model.addAttribute(SysConst.RESULT_KEY, result);
        return model;
    }


    //解析上传的二维码照片
    @RequestMapping(value="/qrcode")
    @ResponseBody
    public Model decodeQR(Model model, HttpServletRequest request,
                         @RequestParam(value = "id", required = false) Integer id) {
        ResultVo<String> result = new ResultVo<>();
        result.setCode(ResultCode.RESULT_SUCCESS.getCode());
        result.setResultDes("解析成功");
        result.setResult("");
        model = new ExtendedModelMap();



        model.addAttribute(SysConst.RESULT_KEY, result);
        return model;
    }
}
