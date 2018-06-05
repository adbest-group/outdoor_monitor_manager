package com.bt.om.web.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.adtime.common.lang.StringUtil;
import com.bt.om.common.SysConst;
import com.bt.om.common.web.PageConst;
import com.bt.om.entity.AdApp;
import com.bt.om.entity.OperateLog;
import com.bt.om.entity.SysUser;
import com.bt.om.entity.vo.AdSeatInfoVo;
import com.bt.om.enums.ResultCode;
import com.bt.om.enums.SessionKey;
import com.bt.om.security.ShiroUtils;
import com.bt.om.service.IAppService;
import com.bt.om.vo.web.ResultVo;
import com.bt.om.vo.web.SearchDataVo;
import com.bt.om.web.util.SearchUtil;

@Controller
@RequestMapping(value = "/app")
public class AppController {

	@Autowired
	private IAppService appService;
	
	@RequiresRoles("superadmin")
	@RequestMapping(value = "/list")
	public String getList(Model model, HttpServletRequest request,
			@RequestParam(value = "id", required = false) Integer id,
			@RequestParam(value = "appName", required = false) String appName,
            @RequestParam(value = "appSid", required = false) String appSid,
            @RequestParam(value = "appPictureUrl", required = false) String appPictureUrl,
            @RequestParam(value = "appTitle", required = false) String appTitle,
            @RequestParam(value = "createDate", required = false) String createDate,
            @RequestParam(value = "updateDate", required = false) String updateDate) {
		
		SearchDataVo vo = SearchUtil.getVo();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		if (id != null) {
            vo.putSearchParam("id", id.toString(), id);
		}
		if (appName != null) {
	        vo.putSearchParam("appName", appName.toString(), appName);
	    }
	    if (appSid != null) {
	        vo.putSearchParam("appSid", appSid.toString(), appSid);
	    }
	    if (appPictureUrl != null) {
	        vo.putSearchParam("appPictureUrl", appPictureUrl.toString(), appPictureUrl);
	    }
	    if (appTitle != null) {
	        vo.putSearchParam("appTitle", appTitle.toString(), appTitle);
	    }
	    if (createDate != null) {
	        try {
	            vo.putSearchParam("createDate", createDate, sdf.parse(createDate));
	        } catch (ParseException e) {
	        }
	    }
	    if (updateDate != null) {
	        try {
	           vo.putSearchParam("updateDate", updateDate, sdf.parse(updateDate));
	        } catch (ParseException e) {
	        }
	    }

	    appService.getPageData(vo);
	    SearchUtil.putToModel(model, vo);
	    return PageConst.SUPER_ADMIN_APP_LIST;
	}
	
	@RequiresRoles("superadmin")
	@RequestMapping(value = "/edit")
	public String appEdit(Model model, HttpServletRequest request,
            @RequestParam(value = "id", required = false) Integer id) {
		AdApp adapp = appService.getVoById(id);
		if (adapp != null) {
            model.addAttribute("adapp", adapp);
		}	
        return PageConst.SUPER_ADMIN_APP_EDIT;
	}
	
	@RequiresRoles("superadmin")
	@ResponseBody
	@RequestMapping(value = "/save", method=RequestMethod.POST)
	public Model appSave(Model model, AdApp adapp, HttpServletRequest request) {
		ResultVo<String> result = new ResultVo<String>();
        result.setCode(ResultCode.RESULT_SUCCESS.getCode());
        result.setResultDes("保存成功");
        model = new ExtendedModelMap();
        Date now = new Date();
        
        if(StringUtil.isBlank(adapp.getAppPictureUrl())) {	
        	result.setCode(ResultCode.RESULT_FAILURE.getCode());
            result.setResultDes("App logo不能为空！");
            model.addAttribute(SysConst.RESULT_KEY, result);
            return model;
        }
        
        try {
            if (adapp.getId() != null) {
            	adapp.setUpdateTime(now);
            	appService.modify(adapp);
            } else {
            	String appSid = UUID.randomUUID().toString();
            	adapp.setAppSid(appSid);
            	adapp.setCreateTime(now);
            	adapp.setUpdateTime(now);
            	adapp.setStatus(1);
        		appService.save(adapp);
            }
        } catch (Exception e) {
            result.setCode(ResultCode.RESULT_FAILURE.getCode());
            result.setResultDes("保存失败！");
            model.addAttribute(SysConst.RESULT_KEY, result);
            return model;
        }

        model.addAttribute(SysConst.RESULT_KEY, result);
        return model;
    }
	
	@RequiresRoles("superadmin")
	@ResponseBody
	@RequestMapping(value = "/delete")
	public Model appDelete(Model model, HttpServletRequest request, @RequestParam(value = "id", required = false) Integer id) {
		ResultVo<String> result = new ResultVo<String>();
        result.setCode(ResultCode.RESULT_SUCCESS.getCode());
        result.setResultDes("保存成功");
        model = new ExtendedModelMap();
        Date now = new Date();
        
        try {
            int count = appService.deleteAppById(id);
            if(count == 0) {
            	result.setCode(ResultCode.RESULT_FAILURE.getCode());
                result.setResultDes("该app账号不能删除！");
                model.addAttribute(SysConst.RESULT_KEY, result);
                return model;
            }
        } catch (Exception e) {
        	result.setCode(ResultCode.RESULT_FAILURE.getCode());
            result.setResultDes("删除失败！");
            model.addAttribute(SysConst.RESULT_KEY, result);
            return model;
        }
        model.addAttribute(SysConst.RESULT_KEY, result);
        return model;
	}
	
}
