package com.bt.om.web.controller;

import java.util.Date;
import java.util.List;

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
import com.bt.om.entity.AdApp;
import com.bt.om.entity.AdCustomerType;
import com.bt.om.entity.SysUser;
import com.bt.om.entity.vo.SysUserVo;
import com.bt.om.enums.ResultCode;
import com.bt.om.enums.SessionKey;
import com.bt.om.enums.UserTypeEnum;
import com.bt.om.filter.LogFilter;
import com.bt.om.security.ShiroUtils;
import com.bt.om.service.IAdCustomerTypeService;
import com.bt.om.service.IAppService;
import com.bt.om.service.ISysUserService;
import com.bt.om.vo.web.ResultVo;
import com.bt.om.vo.web.SearchDataVo;
import com.bt.om.web.util.SearchUtil;

/**
 * Created by jiayong.mao on 2018/4/25.
 */
@Controller
@RequestMapping(value = "/customerType")
public class CustomerTypeController {

	@Autowired
	private IAdCustomerTypeService adCustomerTypeService;
	
	@Autowired
    private ISysUserService sysUserService;
	
	@Autowired
	private IAppService appService;
	
	private static final Logger logger = Logger.getLogger(CustomerTypeController.class);
	
	/**
     * 客户类型展示
     */
    @RequiresRoles(value = {"superadmin" , "phoneoperator"}, logical = Logical.OR)
    @RequestMapping(value = "/list")
    public String resourceDetailPage(Model model, HttpServletRequest request,
                                     @RequestParam(value = "name", required = false) String name) {
        SearchDataVo vo = SearchUtil.getVo();
        //获取登录用户信息
        SysUser userObj = (SysUser) ShiroUtils.getSessionAttribute(SessionKey.SESSION_LOGIN_USER.toString());
        if (!StringUtils.isEmpty(name)) {
        	model.addAttribute("searchName", name);
        	name = "%" + name + "%";
            vo.putSearchParam("name", name, name);
        }
        
        adCustomerTypeService.getPageData(vo);
        SearchUtil.putToModel(model, vo);
        model.addAttribute("user" ,userObj);
        return PageConst.CUSTOMER_TYPE_LIST;
    }
    
    /**
     * 新增客户类型页面跳转
     */
    @RequestMapping(value = "/add")
    public String gotoAddPage(Model model) {
        return PageConst.CUSTOMER_TYPE_EDIT;
    }
    
    /**
     * 编辑客户类型 页面跳转
     **/
    @RequiresRoles("superadmin")
    @RequestMapping(value = "/edit")
    public String gotoEditPage(Model model, HttpServletRequest request,
                         @RequestParam(value = "id", required = false) Integer id) {
        if (id != null) {
            AdCustomerType adCustomerType = adCustomerTypeService.getById(id);
            model.addAttribute("obj", adCustomerType);
        }

        return PageConst.CUSTOMER_TYPE_EDIT;
    }
    
    /**
     * 保存广告位
     **/
    @RequiresRoles(value = {"admin", "superadmin"}, logical = Logical.OR)
    @RequestMapping(value = "/save")
    @ResponseBody
    public Model addInfo(Model model, AdCustomerType customerType, HttpServletRequest request) {
        ResultVo<String> result = new ResultVo<String>();
        result.setCode(ResultCode.RESULT_SUCCESS.getCode());
        result.setResultDes("保存成功");
        model = new ExtendedModelMap();
        Date now = new Date();
        
        try {
            if (customerType.getId() != null) {
            	customerType.setUpdateTime(now);
            	adCustomerTypeService.modify(customerType);
            } else {
            	customerType.setCreateTime(now);
            	customerType.setUpdateTime(now);
            	adCustomerTypeService.save(customerType);
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
     * 客户类型对应用户
     */
    @RequiresRoles(value = {"superadmin" , "phoneoperator"}, logical = Logical.OR)
    @RequestMapping(value = "/users")
    public String resourceUsersPage(Model model, HttpServletRequest request,
    		@RequestParam(value = "customerTypeId", required = true) Integer id) {
        SearchDataVo vo = SearchUtil.getVo();
        vo.putSearchParam("customerTypeId", id.toString(), id.toString());
        sysUserService.getPageData(vo);

        List<?> list = vo.getList();
        for (Object object : list) {
        	SysUserVo userVo = (SysUserVo) object;
        	if(userVo.getAppTypeId() != null) {
        		AdApp adapp = appService.selectById(userVo.getAppTypeId());
        		userVo.setAppTypeName(adapp.getAppName());
        	}
		}
        SearchUtil.putToModel(model, vo);
        return PageConst.CUSTOMER_TYPE_DETAILS;
    }
}
