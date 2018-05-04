package com.bt.om.web.controller;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bt.om.common.SysConst;
import com.bt.om.common.web.PageConst;
import com.bt.om.entity.SysUser;
import com.bt.om.entity.SysUserDetail;
import com.bt.om.entity.SysUserRole;
import com.bt.om.enums.ResultCode;
import com.bt.om.service.ISysUserService;
import com.bt.om.vo.web.ResultVo;
import com.bt.om.vo.web.SearchDataVo;
import com.bt.om.web.util.SearchUtil;

@Controller
@RequestMapping("/sysUser")
public class SysAdminController {

	@Autowired
	private ISysUserService sysUserService;
	
	/**
	 * 查询admin账号列表
	 */
	@RequiresRoles("departmentadmin")
    @RequestMapping(value = "/list")
    public String departmentLeaderList(Model model, HttpServletRequest request,
                               @RequestParam(value = "name", required = false) String name) {
        SearchDataVo vo = SearchUtil.getVo();
        //查询admin名称
        if (name != null) {
        	name = "%" + name + "%";
            vo.putSearchParam("nameOrUsername", name, name);
        }
        Integer usertype = 1;
        vo.putSearchParam("usertype", usertype.toString(), usertype);
        sysUserService.getPageData(vo);
        SearchUtil.putToModel(model, vo);
        return PageConst.DEPARMENT_ADMIN_USER_LIST;
    }
	/**
     * 新增admin页面跳转
     */
    @RequestMapping(value = "/adminAdd")
    public String gotoAddPage(Model model) {
        return PageConst.DEPARMENT_ADMIN_USER_EDIT;
    }
    
	/**
     * 编辑admin账号 页面跳转
     **/
    @RequiresRoles("departmentadmin")
    @RequestMapping(value = "/adminEdit")
    public String gotoEditPage(Model model, HttpServletRequest request,
                         @RequestParam(value = "id", required = false) Integer id) {
    	
        if (id != null) {
            SysUser sysUser = sysUserService.findUserinfoById(id);
            model.addAttribute("obj", sysUser);
        }
        return PageConst.DEPARMENT_ADMIN_USER_EDIT;
    }
    /**
     * 保存admin账号
     **/
    @RequiresRoles("departmentadmin")
    @RequestMapping(value = {"/adminSave"}, method = {RequestMethod.POST})
    @ResponseBody
    public Model addInfo(Model model, SysUser sysUser, String oldPassword, HttpServletRequest request) {
        ResultVo<String> result = new ResultVo<String>();
        result.setCode(ResultCode.RESULT_SUCCESS.getCode());
        result.setResultDes("保存成功");
        model = new ExtendedModelMap();
        Date now = new Date();
        
        try {
            if (sysUser.getId() != null) {
            	sysUser.setUpdateTime(now);
            	if(!StringUtils.equals(sysUser.getPassword(), "********")) {
            		//填写的密码是新密码, 先进行md5加密后存储
            		sysUser.setPassword(new Md5Hash(sysUser.getPassword(), sysUser.getUsername()).toString());
            	} else {
            		//老密码
            		sysUser.setPassword(oldPassword);
            	}
            	sysUserService.updatePasswordAndName(sysUser);
            } else {
            	//[1] 插入sys_user
            	sysUser.setCreateTime(now);
            	sysUser.setUpdateTime(now);
            	sysUser.setPlatform(1);
            	sysUser.setUsertype(1); //1：admin账户
            	sysUser.setStatus(1); //1：可用（默认）
            	sysUser.setPassword(new Md5Hash(sysUser.getPassword(), sysUser.getUsername()).toString());
            	//[2] 插入sys_user_detail
            	SysUserDetail sysUserDetail = new SysUserDetail();
            	sysUserDetail.setUserId(sysUser.getId());
            	sysUserDetail.setTelephone(sysUser.getMobile());
            	sysUserDetail.setCreateTime(now);
            	sysUserDetail.setUpdateTime(now);
            	//[3] 插入sys_user_role
            	SysUserRole sysUserRole = new SysUserRole();
            	sysUserRole.setPlatform(1);
            	sysUserRole.setUserId(sysUser.getId());
            	sysUserRole.setRoleId(100); //100: admin的role
            	sysUserRole.setCreateTime(now);
            	sysUserRole.setUpdateTime(now);
            	sysUserService.createDepartmentLeader(sysUser, sysUserDetail, sysUserRole);
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
     * 修改admin账号状态： 可用, 不可用
     **/
    @RequiresRoles("departmentadmin")
    @RequestMapping(value = "/adminStatus")
    @ResponseBody
    public Model updateStatus(Model model, Integer id, SysUser status) {
    	ResultVo<String> result = new ResultVo<String>();
        result.setCode(ResultCode.RESULT_SUCCESS.getCode());
        result.setResultDes("保存成功");
        model = new ExtendedModelMap();
        
        try {
        	SysUser userType = new SysUser();
        	userType.setId(id);
        	userType.setStatus(1);
        	sysUserService.updateStatus(status);
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
