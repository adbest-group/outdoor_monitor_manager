package com.bt.om.web.controller;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

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
import com.bt.om.entity.AdCustomerType;
import com.bt.om.enums.ResultCode;
import com.bt.om.service.IAdCustomerTypeService;
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
	
	/**
     * 客户类型展示
     */
    @RequiresRoles("admin")
    @RequestMapping(value = "/list")
    public String resourceDetailPage(Model model, HttpServletRequest request,
                                     @RequestParam(value = "name", required = false) String name) {
        SearchDataVo vo = SearchUtil.getVo();

        if (!StringUtils.isEmpty(name)) {
        	model.addAttribute("searchName", name);
        	name = "%" + name + "%";
            vo.putSearchParam("name", name, name);
        }
        
        adCustomerTypeService.getPageData(vo);
        SearchUtil.putToModel(model, vo);

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
    @RequiresRoles("admin")
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
    @RequiresRoles("admin")
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
            result.setCode(ResultCode.RESULT_FAILURE.getCode());
            result.setResultDes("保存失败！");
            model.addAttribute(SysConst.RESULT_KEY, result);
            return model;
        }

        model.addAttribute(SysConst.RESULT_KEY, result);
        return model;
    }
    
}
