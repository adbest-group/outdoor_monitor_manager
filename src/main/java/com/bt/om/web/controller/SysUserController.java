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
import com.bt.om.entity.AdMediaType;
import com.bt.om.entity.SysUser;
import com.bt.om.entity.SysUserDetail;
import com.bt.om.entity.SysUserRole;
import com.bt.om.enums.ResultCode;
import com.bt.om.service.ISysUserService;
import com.bt.om.service.impl.SysUserService;
import com.bt.om.vo.web.ResultVo;
import com.bt.om.vo.web.SearchDataVo;
import com.bt.om.web.BasicController;
import com.bt.om.web.util.SearchUtil;

@Controller
@RequestMapping("/sysUser")
public class SysUserController extends BasicController{

	@Autowired
	private ISysUserService sysUserService;
	
	/**
	 * 查询部门领导账号列表
	 */
	@RequiresRoles("superadmin")
    @RequestMapping(value = "/leaderList")
    public String departmentLeaderList(Model model, HttpServletRequest request,
                               @RequestParam(value = "name", required = false) String name) {
        SearchDataVo vo = SearchUtil.getVo();
        //查询领导名称
        if (name != null) {
        	name = "%" + name + "%";
            vo.putSearchParam("name", name, name);
        }
        
        Integer usertype = 5;
        vo.putSearchParam("usertype", usertype.toString(), usertype);
        sysUserService.getPageData(vo);
        
        SearchUtil.putToModel(model, vo);
        return PageConst.SUPER_ADMIN_LEAD_LIST;
    }
	
	/**
     * 新增领导页面跳转
     */
    @RequestMapping(value = "/addLeader")
    public String gotoAddPage(Model model) {
        return PageConst.SUPER_ADMIN_LEAD_EDIT;
    }
    
	/**
     * 编辑部门领导账号 页面跳转
     **/
    @RequiresRoles("superadmin")
    @RequestMapping(value = "/leaderEdit")
    public String gotoEditPage(Model model, HttpServletRequest request,
                         @RequestParam(value = "id", required = false) Integer id) {
    	
        if (id != null) {
            SysUser sysUser = sysUserService.findUserinfoById(id);
            model.addAttribute("obj", sysUser);
        }
        return PageConst.SUPER_ADMIN_LEAD_EDIT;
    }
    
    /**
     * 保存账号
     **/
    @RequiresRoles("superadmin")
    @RequestMapping(value = {"/saveLeader"}, method = {RequestMethod.POST})
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
            	sysUser.setUsertype(5); //5：部门领导
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
            	sysUserRole.setRoleId(104); //104: 部门领导role
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
     * 修改领导账号状态： 可用, 不可用
     **/
    @RequiresRoles("superadmin")
    @RequestMapping(value = "/leaderStatus")
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
    /*public Model save(Model model,
                      @RequestParam(value = "id", required = true) Integer id,
                      @RequestParam(value = "username", required = true) String username,
                      @RequestParam(value = "relName", required = true) String name,
                      @RequestParam(value = "telephone", required = true) String telephone,
    				  @RequestParam(value = "status", required = true) String status){
    	 Date now = new Date();
        ResultVo<List<SysUser>> resultVo = new ResultVo<List<SysUser>>();
        try {
            if (id == null) {//新增
                SysUserVo user = new SysUserVo();
                user.setUsername(username);
                user.setRealname(name);
                user.setTelephone(telephone);
                user.setCreateTime(now);
                user.setStatus(1);
                sysUserService.createDepartmentLeader(user);
            } else {//修改
                SysUserVo user = new SysUserVo();
                user.setId(id);
                user.setUsername(username);
              
                user.setRealname(name);
                user.setTelephone(telephone);
                sysUserService.updatePasswordAndName(user);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            resultVo.setCode(ResultCode.RESULT_FAILURE.getCode());
            resultVo.setResultDes("服务忙，请稍后再试");
        }

        model.addAttribute(SysConst.RESULT_KEY, resultVo);
        return model;
    }
}*/
