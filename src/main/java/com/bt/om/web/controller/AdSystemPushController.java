package com.bt.om.web.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

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

import com.alibaba.druid.util.StringUtils;
import com.bt.om.common.SysConst;
import com.bt.om.common.web.PageConst;
import com.bt.om.entity.AdSystemPush;
import com.bt.om.enums.ResultCode;
import com.bt.om.filter.LogFilter;
import com.bt.om.service.IAdSystemPushService;
import com.bt.om.vo.web.ResultVo;
import com.bt.om.vo.web.SearchDataVo;
import com.bt.om.web.util.JPushUtils;
import com.bt.om.web.util.SearchUtil;

@Controller
@RequestMapping(value = "/systempush")
public class AdSystemPushController {

	@Autowired
	private IAdSystemPushService adSystemPushService;
	
	private static final Logger logger = Logger.getLogger(AdSystemPushController.class);
	
//    @RequiresRoles("superadmin")
//    @RequestMapping(value = "/list")
//    public String resourceDetailPage(Model model, HttpServletRequest request,
//                                     @RequestParam(value = "content", required = false) String content,
//                                     @RequestParam(value = "type", required = false) String type) {
//        SearchDataVo vo = SearchUtil.getVo();
//
//        if (!StringUtils.isEmpty(content)) {
//        	model.addAttribute("searchContent", content);
//        	content = "%" + content + "%";
//            vo.putSearchParam("content", content, content);
//        }
////        if (type == null) {
////        	type = 1;
////        }
//        vo.putSearchParam("type", type, type);
//        model.addAttribute("searchType", type);
//        
//        iAdSystemPushService.getPageData(vo);
//        SearchUtil.putToModel(model, vo);
//
//        return PageConst.SYSTRM_PUSH_LIST;
//    }
    
     @RequiresRoles(value = {"superadmin" ,"phoneoperator"}, logical = Logical.OR)
	 @RequestMapping(value = "/list")
	 public String userPushList(Model model, HttpServletRequest request,
	         @RequestParam(value = "push", required = false) String push,
	         @RequestParam(value = "username", required = false) String username) {
	    SearchDataVo vo = SearchUtil.getVo();
	    //查询指定推送消息
	    if (push != null) {
	    	push = "%" + push + "%";
	        vo.putSearchParam("content", push, push);
	    }
	    //查询指定用户
	    if (username != null) {
	    	username = "%" + username + "%";
	        vo.putSearchParam("username", username, username);
	    }
	    //查询用户积分列表
	    adSystemPushService.getPageData(vo);
	    SearchUtil.putToModel(model, vo);
	    return PageConst.SYSTRM_PUSH_LIST;
	}
//    @RequestMapping(value = "/add")
//    public String gotoAddPage(Model model) {
//        return PageConst.SYSTRM_PUSH_EDIT;
//    }
//    
//    @RequiresRoles("superadmin")
//    @RequestMapping(value = "/addSystemPush")
//    @ResponseBody
//	public Model insert(HttpServletRequest request,Model model, 
//			@RequestParam(value = "content", required = false) String content,
//            @RequestParam(value = "type", required = false) String type) {
//    	ResultVo<String> result = new ResultVo<String>();
//        result.setCode(ResultCode.RESULT_SUCCESS.getCode());
//        result.setResultDes("保存成功");
//        model = new ExtendedModelMap();
//        try {
//    		Date now = new Date();
//    		AdSystemPush adSystemPush = new AdSystemPush();
//        	adSystemPush.setContent(content);
//        	adSystemPush.setCreateTime(now);
//        	adSystemPush.setType(type);
//    		
//    		iAdSystemPushService.save(adSystemPush);
//    		
//    		//==========web端创建系统消息推送之后根据类型进行app消息推送==============
//    		String pushType = "";
//    		/** 所需url后续确认**/
//    		String url = "";
//    		if(type != null && type.equals("1")) {  //版本更新
//    			pushType = "version_update_push";
//    			url = "";
//    		} else if (type != null && type.equals("2")) {  //免责声明更新
//    			pushType = "disclaimer_update_push";
//    			url = "";
//			}
//            Map<String, Object> param = new HashMap<>();
//            Map<String, String> extras = new HashMap<>();
//            extras.put("type", pushType);
//            extras.put("url", url);
//            param.put("msg", content);
//            param.put("title", "玖凤平台");
//            param.put("extras", extras);
//            String pushResult = JPushUtils.pushAll(param);
//            System.out.println("pushResult:: " + pushResult);
//        } catch (Exception e) {
//        	logger.error(e);
//        	result.setCode(ResultCode.RESULT_FAILURE.getCode());
//            result.setResultDes("保存失败！");
//            model.addAttribute(SysConst.RESULT_KEY, result);
//        }
//        model.addAttribute(SysConst.RESULT_KEY, result);
//        return model;
//	}
	
}
