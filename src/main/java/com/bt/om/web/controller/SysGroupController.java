package com.bt.om.web.controller;

import java.util.Date;
import java.util.List;

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
import com.bt.om.entity.SysUser;
import com.bt.om.enums.ResultCode;
import com.bt.om.enums.SessionKey;
import com.bt.om.security.ShiroUtils;
import com.bt.om.service.ISysGroupService;
import com.bt.om.service.ISysUserService;
import com.bt.om.vo.web.ResultVo;
import com.bt.om.vo.web.SearchDataVo;
import com.bt.om.web.BasicController;
import com.bt.om.web.util.SearchUtil;

@Controller
@RequestMapping("/sysResources")
public class SysGroupController extends BasicController{

	@Autowired
	private ISysGroupService sysGroupService;
	@Autowired
	private ISysUserService sysUserService;
	
	/**
	 * 部门管理员查询部门列表
	 */
	@RequiresRoles("departmentadmin")
    @RequestMapping(value = "/groupList")
    public String customerList(Model model, HttpServletRequest request,
                               @RequestParam(value = "name", required = false) String name) {
        SearchDataVo vo = SearchUtil.getVo();
        //查询小组名称
        if (name != null) {
        	name = "%" + name + "%";
            vo.putSearchParam("name", name, name);
        }
        //查询类型为2：组
        String type = "2";
        vo.putSearchParam("type", type, type);
        
        //获取当前登录的后台用户id
        SysUser sysUser = (SysUser) ShiroUtils.getSessionAttribute(SessionKey.SESSION_LOGIN_USER.toString());
        if(sysUser.getUsertype() == 5) {
        	//部门领导登录, 查询部门领导账号一对一管理的部门信息
        	SysResources department = sysGroupService.getByUserId(sysUser.getId());
        	vo.putSearchParam("parentid", null, department.getId());
        }
        
        sysGroupService.getPageData(vo);
        SearchUtil.putToModel(model, vo);
        return PageConst.DEPARMENT_ADMIN_GROUP_LIST;
    }
	
	/**
     * 新增组页面跳转
     */
    @RequestMapping(value = "/addGroup")
    public String gotoAddPage(Model model) {
    	
        return PageConst.DEPARMENT_ADMIN_GROUP_EDIT;
    }
    
    /**
     * 保存组
     **/
    @RequiresRoles("departmentadmin")
    @RequestMapping(value = "/saveGroup")
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
            	sysGroupService.modify(sysResources);
            } else {
            	sysResources.setCreateTime(now);
            	sysResources.setUpdateTime(now);
            	sysResources.setType("2"); //设置类型为组
            	//获取当前登录的后台用户id
                SysUser sysUser = (SysUser) ShiroUtils.getSessionAttribute(SessionKey.SESSION_LOGIN_USER.toString());
                if(sysUser.getUsertype() == 5) {
                	//部门领导登录, 查询部门领导账号一对一管理的部门信息
                	SysResources department = sysGroupService.getByUserId(sysUser.getId());
                	sysResources.setParentid(department.getId());
                }
            	sysGroupService.insert(sysResources);
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
     * 编辑组页面跳转
     **/
    @RequiresRoles("departmentadmin")
    @RequestMapping(value = "/editGroup")
    public String gotoEditPage(Model model, HttpServletRequest request,
                         @RequestParam(value = "id", required = false) Integer id) {
        if (id != null) {
            SysResources sysResources = sysGroupService.getById(id);
            model.addAttribute("obj", sysResources);
        }
        return PageConst.DEPARMENT_ADMIN_GROUP_EDIT ;
    }
    
    /**
     * 编辑组-员工页面跳转
     **/
    @RequiresRoles("departmentadmin")
    @RequestMapping(value = "/resUser")
    public String gotoUserName(Model model, HttpServletRequest request,
                         @RequestParam(value = "id", required = false) Integer groupId) {
        if (groupId != null) {
        	//[1] 获取与该组有关系的员工
            List<SysUser> sysUsers = sysGroupService.selectUserName(groupId);
            model.addAttribute("sysUsers", sysUsers);
            
            //[2] 获取不属于任何组的员工
            List<SysUser> sysUserss= sysGroupService.selectNoUserName(groupId);
            model.addAttribute("sysUserss", sysUserss);
            
            model.addAttribute("groupId", groupId);
        }
        return PageConst.DEPARMENT_ADMIN_GROUP_USER;
    }
    
    /**
     * 编辑组-客户页面跳转
     **/
    @RequiresRoles("departmentadmin")
    @RequestMapping(value = "/resCustomer")
    public String gotoCustomerName(Model model, HttpServletRequest request,
                         @RequestParam(value = "id", required = false) Integer groupId) {
    	if (groupId != null) {
    		//[1] 获取与该组有关系的客户
            List<SysUser> sysUsers = sysGroupService.selectCustomerName(groupId);
            model.addAttribute("sysUsers", sysUsers);
            //[2] 获取不属于任何组的客户
            List<SysUser> sysUserss= sysGroupService.selectNoCustomerName(groupId);
            model.addAttribute("sysUserss", sysUserss);
            
            model.addAttribute("groupId", groupId);
        }
        return PageConst.DEPARMENT_ADMIN_GROUP_CUSTOMER;
    }
    /**
     * 保存员工
     **/
    @RequiresRoles("departmentadmin")
    @RequestMapping(value = "/saveUser")
    @ResponseBody
    public Model saveUsers(Model model, SysUser sysUser, SysResources sysResources,HttpServletRequest request,Integer id) {
        ResultVo<String> result = new ResultVo<String>();
        result.setCode(ResultCode.RESULT_SUCCESS.getCode());
        result.setResultDes("保存成功");
        model = new ExtendedModelMap();
        Date now = new Date();
        
        try {
            if (sysUser.getId() != null) {
            	sysUserService.deleteUserById(id);
            	
            } else {
            	sysResources.setType("1"); 
            	sysUserService.addUsers(sysUser);
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
