package com.bt.om.web.controller;

import java.util.ArrayList;
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

import com.adtime.common.lang.StringUtil;
import com.bt.om.common.SysConst;
import com.bt.om.common.web.PageConst;
import com.bt.om.entity.SysResources;
import com.bt.om.entity.SysUser;
import com.bt.om.entity.SysUserRes;
import com.bt.om.entity.vo.UserRoleVo;
import com.bt.om.enums.ResultCode;
import com.bt.om.enums.SessionKey;
import com.bt.om.security.ShiroUtils;
import com.bt.om.service.ISysGroupService;
import com.bt.om.service.ISysResourcesService;
import com.bt.om.service.ISysUserRoleService;
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
	@Autowired
	private ISysUserRoleService sysUserRoleService;
	@Autowired
	private ISysResourcesService sysResourcesService;
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
                         @RequestParam(value = "id", required = false) Integer groupId,
    					 @RequestParam(value = "parentId", required = false) Integer parentId) {
        if (groupId != null) {
        	//[1] 获取与该组有关系的员工
            List<SysUser> sysUsers = sysGroupService.selectUserName(groupId);
            model.addAttribute("sysUsers", sysUsers);
            
            //[2] 获取不属于任何组的员工
            List<SysUser> sysUserss= sysGroupService.selectNoUserName(groupId);
            model.addAttribute("sysUserss", sysUserss);
            
            model.addAttribute("groupId", groupId);
            model.addAttribute("parentId", parentId);
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
     * 保存员工, 将员工的角色改为部门相关的角色(admin -> jiucuoadmin, taskadmin, activityadmin)
     **/
    @RequiresRoles("departmentadmin")
    @RequestMapping(value = "/saveUser")
    @ResponseBody
    public Model saveUsers(Model model, HttpServletRequest request, Integer groupId, String userIds, Integer parentId) {
        ResultVo<String> result = new ResultVo<String>();
        result.setCode(ResultCode.RESULT_SUCCESS.getCode());
        result.setResultDes("保存成功");
        model = new ExtendedModelMap();
        Date now = new Date();
        
        try {
            if (StringUtil.isNotBlank(userIds)) {
            	//获取与该组有关系的员工
                List<SysUser> sysUsers = sysGroupService.selectUserName(groupId); //获取与该组有关系的员工
                List<Integer> insideUserIds = new ArrayList<>(); //存所有与该组有关系的员工的id集合
                for (SysUser sysUser : sysUsers) {
                	insideUserIds.add(sysUser.getId());
				}
                
            	List<SysUserRes> sysUserRess = new ArrayList<>();
            	String[] split = userIds.split(",");
            	List<Integer> splitUserIds = new ArrayList<>(); //仍然在这个组内的员工的id集合
            	for (String userId : split) {
            		Integer intUserId = Integer.parseInt(userId);
            		SysUserRes res = new SysUserRes();
            		res.setResId(groupId);
            		res.setUserId(intUserId);
            		splitUserIds.add(intUserId);
            		res.setType(1);
            		res.setCreateTime(now);
            		res.setUpdateTime(now);
            		sysUserRess.add(res);
        		}
            	
            	insideUserIds.removeAll(splitUserIds); //留下将要被踢出组的员工的id集合
            	
            	SysUserRes sysUserRes = new SysUserRes();
            	sysUserRes.setResId(groupId);
            	sysUserRes.setType(1);
            	//[1] 插入sys_user_res表
            	sysUserService.insertUserRess(sysUserRess, sysUserRes);
            	//[2] 将员工的角色改为部门相关的角色
            	SysResources department = sysResourcesService.getById(parentId);//部门
            	Integer departmentType = department.getDepartmentType(); //部门类型
            	
            	Integer roleId = 100;
            	if(departmentType == 1) {
            		//活动审核部门
            		roleId = 105;
            	} else if(departmentType == 2) {
            		//任务审核、指派部门
            		roleId = 106;
            	} else if(departmentType == 3) {
            		//纠错审核部门
            		roleId = 107;
            	}
            	
            	//[3] 更新还在组内的员工角色信息
            	UserRoleVo userRoleVo = new UserRoleVo();
            	userRoleVo.setUpdateTime(now);
            	userRoleVo.setRoleId(roleId);
            	userRoleVo.setUserIds(splitUserIds);
            	if(splitUserIds.size() > 0) {
            		sysUserRoleService.updateUserRole(userRoleVo);
            	}
            	
            	//[4] 更新不在组内的员工角色信息
            	userRoleVo.setRoleId(100); //100:admin
            	userRoleVo.setUserIds(insideUserIds);
            	if(insideUserIds.size() > 0) {
            		sysUserRoleService.updateUserRole(userRoleVo);
            	}
            } else {
            	//直接做删除, 并修改角色回admin：100
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
     * 保存客户
     **/
    @RequiresRoles("departmentadmin")
    @RequestMapping(value = "/saveCustomer")
    @ResponseBody
    public Model saveCustomers(Model model, HttpServletRequest request, Integer groupId, String userIds) {
        ResultVo<String> result = new ResultVo<String>();
        result.setCode(ResultCode.RESULT_SUCCESS.getCode());
        result.setResultDes("保存成功");
        model = new ExtendedModelMap();
        Date now = new Date();
        
        try {
            if (StringUtil.isNotBlank(userIds)) {
            	List<SysUserRes> sysUserRess = new ArrayList<>();
            	String[] split = userIds.split(",");
            	for (String userId : split) {
            		SysUserRes res = new SysUserRes();
            		res.setResId(groupId);
            		res.setUserId(Integer.parseInt(userId));
            		res.setType(2);
            		res.setCreateTime(now);
            		res.setUpdateTime(now);
            		sysUserRess.add(res);
        		}
            	
            	SysUserRes sysUserRes = new SysUserRes();
            	sysUserRes.setResId(groupId);
            	sysUserRes.setType(2);
            	sysUserService.insertUserRess(sysUserRess, sysUserRes);
            } else {
            	//直接删除
            	
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
