package com.bt.om.web.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.Logical;
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
import com.bt.om.entity.SysResources;
import com.bt.om.entity.SysUser;
import com.bt.om.entity.SysUserDetail;
import com.bt.om.entity.SysUserRole;
import com.bt.om.entity.vo.SysUserVo;
import com.bt.om.enums.ResultCode;
import com.bt.om.enums.SessionKey;
import com.bt.om.security.ShiroUtils;
import com.bt.om.service.ISysGroupService;
import com.bt.om.service.ISysResourcesService;
import com.bt.om.service.ISysUserRoleService;
import com.bt.om.service.ISysUserService;
import com.bt.om.vo.web.ResultVo;
import com.bt.om.vo.web.SearchDataVo;
import com.bt.om.web.util.SearchUtil;

@Controller
@RequestMapping("/sysUser")
public class SysAdminController {

	@Autowired
	private ISysUserService sysUserService;
	@Autowired
	private ISysGroupService sysGroupService;
	@Autowired
	private ISysResourcesService sysResourcesService;
	@Autowired
	private ISysUserRoleService sysUserRoleService;
	
	/**
	 * 查询admin账号列表
	 */
	@RequiresRoles(value = {"departmentadmin", "depactivityadmin", "deptaskadmin", "depjiucuoadmin", "superadmin"}, logical = Logical.OR)
    @RequestMapping(value = "/list")
    public String departmentLeaderList(Model model, HttpServletRequest request,
                               @RequestParam(value = "name", required = false) String name) {
        SearchDataVo vo = SearchUtil.getVo();
        List<Integer> userIds = new ArrayList<>();

        
        //获取登录用户信息
        SysUser user = (SysUser) ShiroUtils.getSessionAttribute(SessionKey.SESSION_LOGIN_USER.toString());
        
        //查询admin名称
        if (name != null) {
        	name = "%" + name + "%";
            vo.putSearchParam("nameOrUsername", name, name);
        }
        Integer usertype = 1;
        vo.putSearchParam("usertype", usertype.toString(), usertype);
        
        if(user.getUsertype() == 4) {
        	//超级管理员登录, 不做任何限制
        } else if(user.getUsertype() == 5) {
        	//部门领导登录, 只能修改本部门下的所有员工信息
        	SysResources department = sysResourcesService.getByUserId(user.getId()); //通过部门领导账号的id查询出他管理的部门信息
        	List<Integer> groupIds = sysGroupService.selectGroupIdsByDepartmentId(department.getId()); //通过部门id查询出下面所有的组的id
      
        	Map<String, Object> searchMap = new HashMap<>();
        	searchMap.put("type", 1);
        	searchMap.put("resIds", groupIds);
        	userIds = sysUserService.selectUserIdsByResIds(searchMap); //管理的所有员工id集合
//        	vo.putSearchParam("ids", "ids", userIds);
        }
        
    	sysUserService.getPageData(vo);
    	List<?> list = vo.getList(); //查询到的所有员工
    	for (Object object : list) {
			SysUserVo sysUserVo = (SysUserVo) object;
			if(user.getUsertype() == 5) {
				//[1] 设置是否是部门领导的专属员工
				for (Integer userId : userIds) {
					if(userId == sysUserVo.getId()) {
						sysUserVo.setIsOwn("1"); //是自己管理的员工id
						break;
					}
				}
			} else if(user.getUsertype() == 4) {
				//[2] 超级管理员查询每个员工的组名称和部门名称
				Map<String, Object> searchMap = new HashMap<>();
				searchMap.put("type", 1);
				searchMap.put("userId", sysUserVo.getId());
				Integer groupId = sysUserRoleService.selectGroupIdByUserId(searchMap);
				if(groupId != null) {
					SysResources group = sysGroupService.getById(groupId);
					SysResources department = sysResourcesService.getById(group.getParentid());//通过组的parentid查部门
					
					sysUserVo.setGroupName(group.getName());
					sysUserVo.setDepartmentName(department.getName());
				}
			}
		}
    
        SearchUtil.putToModel(model, vo);
        if(user.getUsertype() == 4) {
        	return PageConst.SUPERADMIN_ADMIN_USER_LIST;
        } else {
        	return PageConst.DEPARMENT_ADMIN_USER_LIST;
        }
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
    @RequiresRoles(value = {"departmentadmin", "depactivityadmin", "deptaskadmin", "depjiucuoadmin", "superadmin"}, logical = Logical.OR)
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
    @RequiresRoles(value = {"departmentadmin", "depactivityadmin", "deptaskadmin", "depjiucuoadmin", "superadmin"}, logical = Logical.OR)
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
            	sysUserRole.setRoleId(100); //100: admin的role, 刚创建的admin员工角色为admin
            	
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
    @RequiresRoles(value = {"departmentadmin", "depactivityadmin", "deptaskadmin", "depjiucuoadmin", "superadmin"}, logical = Logical.OR)
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
