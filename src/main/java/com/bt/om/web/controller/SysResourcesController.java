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

import com.bt.om.common.SysConst;
import com.bt.om.common.web.PageConst;
import com.bt.om.entity.SysResources;
import com.bt.om.enums.ResultCode;
import com.bt.om.service.ISysResourcesService;
import com.bt.om.vo.web.ResultVo;
import com.bt.om.vo.web.SearchDataVo;
import com.bt.om.web.BasicController;
import com.bt.om.web.util.SearchUtil;

@Controller
@RequestMapping("/sysResources")
public class SysResourcesController extends BasicController {

	@Autowired
	private ISysResourcesService sysResourcesService;
	
	/**
	 * 超级管理员查询部门列表
	 */
	@RequiresRoles("superadmin")
    @RequestMapping(value = "/departmentList")
    public String customerList(Model model, HttpServletRequest request,
                               @RequestParam(value = "name", required = false) String name) {
        SearchDataVo vo = SearchUtil.getVo();
        //查询部门名称
        if (name != null) {
        	name = "%" + name + "%";
            vo.putSearchParam("name", name, name);
        }
        //查询类型为1：部门
        String type = "1";
        vo.putSearchParam("type", type, type);
        sysResourcesService.getPageData(vo);
        SearchUtil.putToModel(model, vo);
        return PageConst.SUPER_ADMIN_DEPT_LIST;
    }

	/**
     * 新增部门页面跳转
     */
    @RequestMapping(value = "/addDepartment")
    public String gotoAddPage(Model model) {
        return PageConst.SUPER_ADMIN_DEPT_EDIT;
    }
    
    /**
     * 保存部门
     **/
    @RequiresRoles("superadmin")
    @RequestMapping(value = "/saveDepartment")
    @ResponseBody
    public Model addInfo(Model model, SysResources sysResources, HttpServletRequest request) {
        ResultVo<String> result = new ResultVo<String>();
        result.setCode(ResultCode.RESULT_SUCCESS.getCode());
        result.setResultDes("保存成功");
        model = new ExtendedModelMap();
        Date now = new Date();
        
        try {
            if (sysResources.getId() != null) {
            	sysResources.setUpdateTime(now);
            	sysResourcesService.modify(sysResources);
            } else {
            	sysResources.setCreateTime(now);
            	sysResources.setUpdateTime(now);
            	sysResources.setType("1"); //设置类型为部门
            	sysResourcesService.save(sysResources);
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
    
    /**
     * 编辑部门 页面跳转
     **/
    @RequiresRoles("superadmin")
    @RequestMapping(value = "/editDepartment")
    public String gotoEditPage(Model model, HttpServletRequest request,
                         @RequestParam(value = "id", required = false) Integer id) {
        if (id != null) {
            SysResources sysResources = sysResourcesService.getById(id);
            model.addAttribute("obj", sysResources);
        }
        return PageConst.SUPER_ADMIN_DEPT_EDIT;
    }
    
    
}
