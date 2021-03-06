package com.bt.om.web.controller;

import java.util.Date;
import java.util.HashMap;
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
import com.bt.om.entity.AdMediaType;
import com.bt.om.entity.SysUser;
import com.bt.om.enums.AllowMultiEnum;
import com.bt.om.enums.MediaType;
import com.bt.om.enums.ResultCode;
import com.bt.om.enums.SessionKey;
import com.bt.om.security.ShiroUtils;
import com.bt.om.service.IAdMediaTypeService;
import com.bt.om.vo.web.ResultVo;
import com.bt.om.vo.web.SearchDataVo;
import com.bt.om.web.util.SearchUtil;

/**
 * Created by jiayong.mao on 2018/4/13.
 */
@Controller
@RequestMapping(value = "/mediaType")
public class AdMediaTypeController {

	@Autowired
	private IAdMediaTypeService adMediaTypeService;
	
	private static final Logger logger = Logger.getLogger(AdMediaTypeController.class);
	/**
     * 媒体大类, 媒体小类展示
     */
    @RequiresRoles(value = {"superadmin" , "phoneoperator"}, logical = Logical.OR)
    @RequestMapping(value = "/list")
    public String resourceDetailPage(Model model, HttpServletRequest request,
                                     @RequestParam(value = "name", required = false) String name,
                                     @RequestParam(value = "mediaType", required = false) Integer mediaType,
                                     @RequestParam(value = "parentId", required = false) Integer searchParentMediaId) {
        SearchDataVo vo = SearchUtil.getVo();
        
        //获取登录用户信息
        SysUser userObj = (SysUser) ShiroUtils.getSessionAttribute(SessionKey.SESSION_LOGIN_USER.toString());
        if (!StringUtils.isEmpty(name)) {
        	model.addAttribute("searchMediaName", name);
        	name = "%" + name + "%";
            vo.putSearchParam("name", name, name);
        }
        if (mediaType == null&&searchParentMediaId == null) {
        	mediaType =1;
        }
        if (mediaType != null) {
        	vo.putSearchParam("mediaType", mediaType.toString(), mediaType);
        }
        if (searchParentMediaId != null) {
            vo.putSearchParam("parentId", searchParentMediaId.toString(), searchParentMediaId);
        }

        adMediaTypeService.getPageData(vo);
        SearchUtil.putToModel(model, vo);
        model.addAttribute("user" , userObj);
        return PageConst.MEDIA_TYPE_LIST;
    }
	
    /**
     * 新增媒体类型页面跳转
     */
    @RequestMapping(value = "/add")
    public String gotoAddPage(Model model) {
        return PageConst.MEDIA_TYPE_EDIT;
    }
    
    /**
     * 新增媒体大类或媒体小类（暂时没用）
     */
    @RequiresRoles("superadmin")
    @RequestMapping(value = "/addMediaType")
    @ResponseBody
	public Map<String, Object> insert(HttpServletRequest request,
			@RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "parentId", required = false) Integer parentId,
            @RequestParam(value = "mediaType", required = false) Integer mediaType) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
        try {
        	if(mediaType == MediaType.SECOND_TYPE.getId() && parentId == null) {
        		//添加媒体小类, 但是没有选择媒体大类
        		modelMap.put("success", false);
        		modelMap.put("errMsg", "请选择媒体大类!");
        	} else {
        		Date now = new Date();
        		AdMediaType adMediaType = new AdMediaType();
            	adMediaType.setName(name);
            	adMediaType.setCreateTime(now);
            	adMediaType.setMediaType(mediaType);
            	adMediaType.setParentId(parentId);
            	adMediaType.setStatus(1); //可用
            	adMediaType.setUpdateTime(now);
            	
            	adMediaTypeService.save(adMediaType);
                modelMap.put("success", true);
        	}
        } catch (Exception e) {
        	logger.error(e);
        	modelMap.put("success", false);
            modelMap.put("errMsg", "请重新输入!");
            e.printStackTrace();
        }
        return modelMap;
	}
	
    /**
     * 编辑媒体类型 页面跳转
     **/
    @RequiresRoles("superadmin")
    @RequestMapping(value = "/edit")
    public String gotoEditPage(Model model, HttpServletRequest request,
                         @RequestParam(value = "id", required = false) Integer id) {
        if (id != null) {
            AdMediaType mediaType = adMediaTypeService.getById(id);
            model.addAttribute("obj", mediaType);
        }

        return PageConst.MEDIA_TYPE_EDIT;
    }
    
    /**
     * 保存媒体类型
     **/
    @RequiresRoles("superadmin")
    @RequestMapping(value = "/save")
    @ResponseBody
    public Model addInfo(Model model, AdMediaType adMediaType, HttpServletRequest request) {
        ResultVo<String> result = new ResultVo<String>();
        result.setCode(ResultCode.RESULT_SUCCESS.getCode());
        result.setResultDes("保存成功");
        model = new ExtendedModelMap();
        Date now = new Date();
//        adMediaType.setStatus(1); //可用
//        if(adMediaType.getMediaType() == 1) {
//        	adMediaType.setParentId(null);
//        }
        if(adMediaType.getUniqueKeyNeed() == null) {
        	adMediaType.setUniqueKeyNeed(2);
        }
        
        if(adMediaType.getMultiNum() == null) {
        	//默认不支持多个活动, 并且活动数量为1
        	adMediaType.setAllowMulti(AllowMultiEnum.NOT_ALLOW.getId());
        	adMediaType.setMultiNum(1);
        } else {
        	if(adMediaType.getMultiNum().equals(1)) {
        		//活动数量等于1, 设置不允许同时支持多个活动
        		adMediaType.setAllowMulti(AllowMultiEnum.NOT_ALLOW.getId());
        	} else if(adMediaType.getMultiNum() > 1) {
        		//活动数量大于1, 设置允许同时支持多个活动
        		adMediaType.setAllowMulti(AllowMultiEnum.ALLOW.getId());
        	} else {
        		//活动数量小于1, 设置默认不支持多个活动, 并且活动数量为1
            	adMediaType.setAllowMulti(AllowMultiEnum.NOT_ALLOW.getId());
            	adMediaType.setMultiNum(1);
        	}
        }
        
        try {
            if (adMediaType.getId() != null) {
            	adMediaType.setUpdateTime(now);
            	adMediaTypeService.modify(adMediaType);
            } else {
            	adMediaType.setCreateTime(now);
            	adMediaType.setUpdateTime(now);
            	adMediaTypeService.save(adMediaType);
            }
        } catch (Exception e) {
        	logger.error(e);
            result.setCode(ResultCode.RESULT_FAILURE.getCode());
            result.setResultDes("保存失败！");
            model.addAttribute(SysConst.RESULT_KEY, result);
            return model;
        }

        model.addAttribute(SysConst.RESULT_KEY, result);
        return model;
    }
    
    /**
     * 修改媒体类型状态： 可用, 不可用
     **/
    @RequiresRoles("superadmin")
    @RequestMapping(value = "/updateMediaTypeStatus")
    @ResponseBody
    public Model updateStatus(Model model, Integer id, Integer status, Integer mediaType) {
    	ResultVo<String> result = new ResultVo<String>();
        result.setCode(ResultCode.RESULT_SUCCESS.getCode());
        result.setResultDes("保存成功");
        model = new ExtendedModelMap();
        Date now = new Date();
        
        try {
        	AdMediaType adMediaType = new AdMediaType();
        	adMediaType.setId(id);
        	adMediaType.setStatus(status);
        	adMediaType.setMediaType(mediaType);
        	adMediaTypeService.updateStatusById(adMediaType);
        } catch (Exception e) {
        	logger.error(e);
            result.setCode(ResultCode.RESULT_FAILURE.getCode());
            result.setResultDes("保存失败！");
            model.addAttribute(SysConst.RESULT_KEY, result);
            return model;
        }

        model.addAttribute(SysConst.RESULT_KEY, result);
        return model;
    }
    
    /**
     * 修改媒体类型是否需要唯一标识
     **/
    @RequiresRoles("superadmin")
    @RequestMapping(value = "/updateUniqueTypeNeed")
    @ResponseBody
    public Model updateUniqueTypeNeed(Model model, Integer id, Integer uniqueKeyNeed) {
    	ResultVo<String> result = new ResultVo<String>();
        result.setCode(ResultCode.RESULT_SUCCESS.getCode());
        result.setResultDes("保存成功");
        model = new ExtendedModelMap();
        Date now = new Date();
        
        try {
        	AdMediaType adMediaType = new AdMediaType();
        	adMediaType.setId(id);
        	adMediaType.setUniqueKeyNeed(uniqueKeyNeed);
        	adMediaTypeService.updateNeedById(adMediaType);
        	
        } catch (Exception e) {
        	logger.error(e);
            result.setCode(ResultCode.RESULT_FAILURE.getCode());
            result.setResultDes("保存失败！");
            model.addAttribute(SysConst.RESULT_KEY, result);
            return model;
        }

        model.addAttribute(SysConst.RESULT_KEY, result);
        return model;
    }
}
