package com.bt.om.web.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
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

import com.adtime.common.lang.StringUtil;
import com.bt.om.common.SysConst;
import com.bt.om.common.web.PageConst;
import com.bt.om.entity.SysResources;
import com.bt.om.entity.SysUser;
import com.bt.om.entity.SysUserRes;
import com.bt.om.entity.vo.UserRoleVo;
import com.bt.om.enums.DepartmentTypeEnum;
import com.bt.om.enums.MonitorTaskStatus;
import com.bt.om.enums.MonitorTaskType;
import com.bt.om.enums.ResultCode;
import com.bt.om.enums.RewardTaskType;
import com.bt.om.enums.SessionKey;
import com.bt.om.enums.UserRoleEnum;
import com.bt.om.enums.UserTypeEnum;
import com.bt.om.filter.LogFilter;
import com.bt.om.security.ShiroUtils;
import com.bt.om.service.IAdActivityService;
import com.bt.om.service.IAdJiucuoTaskService;
import com.bt.om.service.IAdMonitorTaskService;
import com.bt.om.service.IAdUserMessageService;
import com.bt.om.service.ISysGroupService;
import com.bt.om.service.ISysResourcesService;
import com.bt.om.service.ISysUserService;
import com.bt.om.vo.web.ResultVo;
import com.bt.om.vo.web.SearchDataVo;
import com.bt.om.web.BasicController;
import com.bt.om.web.util.SearchUtil;

@Controller
@RequestMapping("/sysResources")
public class SysGroupController extends BasicController{
	
	@Autowired
    IAdMonitorTaskService adMonitorTaskService;
	@Autowired
	private ISysGroupService sysGroupService;
	@Autowired
	private ISysUserService sysUserService;
	@Autowired
	private ISysResourcesService sysResourcesService;
	@Autowired
	private IAdActivityService adActivityService;
	@Autowired
    private IAdJiucuoTaskService adJiucuoTaskService;
	@Autowired
	private IAdUserMessageService adUserMessageService;
	private static final Logger logger = Logger.getLogger(LogFilter.class);
	
	/**
	 * 部门管理员查询组列表
	 */
	@RequiresRoles(value = {"departmentadmin", "depactivityadmin", "deptaskadmin", "depjiucuoadmin", "superadmin" ,"phoneoperator"}, logical = Logical.OR)
    @RequestMapping(value = "/groupList")
    public String customerList(Model model, HttpServletRequest request,
                               @RequestParam(value = "name", required = false) String name) {
        SearchDataVo vo = SearchUtil.getVo();
        //获取登录用户信息
        SysUser userObj = (SysUser) ShiroUtils.getSessionAttribute(SessionKey.SESSION_LOGIN_USER.toString());
        //查询小组名称
        if (name != null) {
        	name = "%" + name + "%";
            vo.putSearchParam("name", name, name);
        }
        //查询类型为2：组
        String type = "2";
        vo.putSearchParam("type", type, type);
        
        //获取当前登录的后台用户信息
        SysUser sysUser = (SysUser) ShiroUtils.getSessionAttribute(SessionKey.SESSION_LOGIN_USER.toString());
        if(sysUser.getUsertype() == UserTypeEnum.DEPARTMENT_LEADER.getId()) {
        	//部门领导登录, 查询部门领导账号一对一管理的部门信息
        	SysResources department = sysGroupService.getByUserId(sysUser.getId());
        	vo.putSearchParam("parentid", null, department.getId());
        }
        
        sysGroupService.getPageData(vo);
        SearchUtil.putToModel(model, vo);
        
        model.addAttribute("user" , userObj);
        return PageConst.DEPARMENT_ADMIN_GROUP_LIST;
    }
	
	/**
     * 新增组页面跳转
     */
    @RequestMapping(value = "/addGroup")
    public String gotoAddPage(Model model) {
    	SysUser sysUser = (SysUser) ShiroUtils.getSessionAttribute(SessionKey.SESSION_LOGIN_USER.toString());
    	model.addAttribute("usertype", sysUser.getUsertype());
    	model.addAttribute("obj", new SysResources());
//        SearchDataVo vo = SearchUtil.getVo();
//    	if(sysUser.getUsertype() == 4) {
//    		//超级管理员登录，展示所有部门信息
//    		Integer type = 1;	//type=1的所有部门信息
//    		vo.putSearchParam("type", type.toString(), type);
//    		sysGroupService.getPageData(vo);
//        	
//    	}
//    	SearchUtil.putToModel(model, vo);
        return PageConst.DEPARMENT_ADMIN_GROUP_EDIT;
    }
    
    /**
     * 保存组
     **/
    @RequiresRoles(value = {"departmentadmin", "depactivityadmin", "deptaskadmin", "depjiucuoadmin", "superadmin"}, logical = Logical.OR)
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
                if(sysUser.getUsertype() == UserTypeEnum.DEPARTMENT_LEADER.getId()) {
                	if(sysResources.getParentid()==null) {
	                	//部门领导登录, 查询部门领导账号一对一管理的部门信息
	                	SysResources department = sysGroupService.getByUserId(sysUser.getId());
	                	sysResources.setParentid(department.getId());
	                	sysGroupService.insert(sysResources);
                	}
                }else if(sysUser.getUsertype() == UserTypeEnum.SUPER_ADMIN.getId() && sysResources.getParentid() != null) {
                	//超级管理员登录
                	sysGroupService.insert(sysResources);
                }
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
     * 编辑组页面跳转
     **/
    @RequiresRoles(value = {"departmentadmin", "depactivityadmin", "deptaskadmin", "depjiucuoadmin", "superadmin"}, logical = Logical.OR)
    @RequestMapping(value = "/editGroup")
    public String gotoEditPage(Model model, HttpServletRequest request,
                         @RequestParam(value = "id", required = false) Integer id,
                         @RequestParam(value = "parentid" ,required = false) Integer parentid) {
     	SysUser sysUser = (SysUser) ShiroUtils.getSessionAttribute(SessionKey.SESSION_LOGIN_USER.toString());
     	model.addAttribute("usertype", sysUser.getUsertype());
//    	SearchDataVo vo = SearchUtil.getVo();
        if (id != null) {
            SysResources sysResources = sysGroupService.getById(id);
            model.addAttribute("obj", sysResources);
//            if(sysUser.getUsertype() == 4) {
//        		//超级管理员登录，展示当前部门信息
//        		vo.putSearchParam("id", parentid.toString(), parentid);
//        	}
        }
        
//    	sysGroupService.getPageData(vo);
//    	SearchUtil.putToModel(model, vo);
        return PageConst.DEPARMENT_ADMIN_GROUP_EDIT;
    }
    
    /**
     * 删除组
     * */
    @RequiresRoles(value = {"departmentadmin", "depactivityadmin", "deptaskadmin", "depjiucuoadmin", "superadmin"}, logical = Logical.OR)
	@RequestMapping(value = "/delete")
    @ResponseBody
	public Model appDelete(Model model, HttpServletRequest request, @RequestParam(value = "id", required = false) Integer id) {
		ResultVo<String> result = new ResultVo<String>();
        result.setCode(ResultCode.RESULT_SUCCESS.getCode());
        result.setResultDes("保存成功");
        model = new ExtendedModelMap();
        Date now = new Date();
        
        try {
            int count = sysGroupService.deleteGroupById(id);
            if(count == 0) {
            	result.setCode(ResultCode.RESULT_FAILURE.getCode());
                result.setResultDes("该小组不能删除！");
                model.addAttribute(SysConst.RESULT_KEY, result);
                return model;
            }
        } catch (Exception e) {
        	logger.error(e);
        	result.setCode(ResultCode.RESULT_FAILURE.getCode());
            result.setResultDes("删除失败！");
            model.addAttribute(SysConst.RESULT_KEY, result);
            return model;
        }
        model.addAttribute(SysConst.RESULT_KEY, result);
        return model;
	}
    
    /**
     * 编辑组-员工页面跳转
     **/
    @RequiresRoles(value = {"departmentadmin", "depactivityadmin", "deptaskadmin", "depjiucuoadmin", "superadmin"}, logical = Logical.OR)
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
    @RequiresRoles(value = {"departmentadmin", "depactivityadmin", "deptaskadmin", "depjiucuoadmin", "superadmin"}, logical = Logical.OR)
    @RequestMapping(value = "/resCustomer")
    public String gotoCustomerName(Model model, HttpServletRequest request,
                         @RequestParam(value = "id", required = false) Integer groupId) {
    	if (groupId != null) {
    		//[1] 获取与该组有关系的客户
            List<SysUser> sysUsers = sysGroupService.selectCustomerName(groupId);
            model.addAttribute("sysUsers", sysUsers);
            //[2] 获取【本部门中】不属于任何组的客户
            Map<String, Object> searchMap = new HashMap<>();
            // 查询本部门中所有组的id集合
            SysResources group = sysGroupService.getById(groupId); //传参的该组信息
            List<Integer> groupIds = sysGroupService.selectGroupIdsByDepartmentId(group.getParentid()); //本部门下所有组的id集合
            searchMap.put("groupIds", groupIds);
            
            List<SysUser> sysUserss = sysGroupService.selectNoCustomerName(searchMap); //查询出本部门所有组下的 广告商信息
            List<SysUser> allCustomers = sysUserService.getIdNameByUserType(UserTypeEnum.CUSTOMER.getId()); //查询出所有的广告商信息
            allCustomers.removeAll(sysUserss);
            model.addAttribute("sysUserss", allCustomers);
            
            model.addAttribute("groupId", groupId);
        }
        return PageConst.DEPARMENT_ADMIN_GROUP_CUSTOMER;
    }
    
    /**
     * 保存员工与组之间的关系, 还要将员工的角色改为部门相关的角色(admin -> jiucuoadmin, taskadmin, activityadmin)
     **/
    @RequiresRoles(value = {"departmentadmin", "depactivityadmin", "deptaskadmin", "depjiucuoadmin", "superadmin"}, logical = Logical.OR)
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
            	
            	//[1] 将员工的角色改为部门相关的角色
            	SysResources department = sysResourcesService.getById(parentId);//部门
            	Integer departmentType = department.getDepartmentType(
            			); //部门类型
            	
            	Integer roleId = UserRoleEnum.ADMIN.getId();
            	if(departmentType == DepartmentTypeEnum.ACTIVITY.getId()) {
            		//活动审核部门
            		roleId = UserRoleEnum.ACTIVITY_ADMIN.getId();
            	} else if(departmentType == DepartmentTypeEnum.MONITOR_TASK.getId()) {
            		//任务审核、指派部门
            		roleId = UserRoleEnum.TASK_ADMIN.getId();
            	} else if(departmentType == DepartmentTypeEnum.JIUCUO_TASK.getId()) {
            		//纠错审核部门
            		roleId = UserRoleEnum.JIUCUO_ADMIN.getId();
            	}
            	
            	//[2] 更新还在组内的员工角色信息
            	UserRoleVo userRoleVo = new UserRoleVo();
            	userRoleVo.setUpdateTime(now);
            	userRoleVo.setRoleId(roleId);
            	userRoleVo.setUserIds(splitUserIds);
            	
            	//[3] 更新不在组内的员工角色信息
            	UserRoleVo userRoleVo2 = new UserRoleVo();
            	userRoleVo2.setUpdateTime(now);
            	userRoleVo2.setRoleId(UserRoleEnum.ADMIN.getId()); //100:admin
            	userRoleVo2.setUserIds(insideUserIds);
            	
            	//[4] 插入sys_user_res表
            	sysUserService.updateUserRess(sysUserRess, sysUserRes, userRoleVo, userRoleVo2);
            } else {
            	//[1] 直接做删除
            	SysUserRes sysUserRes = new SysUserRes();
            	sysUserRes.setResId(groupId);
            	sysUserRes.setType(1);
            	
            	//[2] 更新不在组内的员工角色信息, 修改角色回admin：100
            	//获取与该组有关系的员工
                List<SysUser> sysUsers = sysGroupService.selectUserName(groupId); //获取与该组有关系的员工
                List<Integer> insideUserIds = new ArrayList<>(); //存所有与该组有关系的员工的id集合
                for (SysUser sysUser : sysUsers) {
                	insideUserIds.add(sysUser.getId());
				}
            	
            	UserRoleVo userRoleVo = new UserRoleVo();
            	userRoleVo.setUpdateTime(now);
            	userRoleVo.setRoleId(UserRoleEnum.ADMIN.getId()); //100:admin
            	userRoleVo.setUserIds(insideUserIds);
            	
            	sysUserService.deleteUserRess(sysUserRes, userRoleVo);
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
     * 保存客户(广告商)与组之间的关系
     **/
    @RequiresRoles(value = {"departmentadmin", "depactivityadmin", "deptaskadmin", "depjiucuoadmin", "superadmin"}, logical = Logical.OR)
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
            	SysUserRes sysUserRes = new SysUserRes();
            	sysUserRes.setResId(groupId);
            	sysUserRes.setType(2);
            	sysUserService.deleteCustomerRess(sysUserRes);
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
     * 【活动审核部门】领导/【超级管理员】查看 所有活动页面
     */
    @RequiresRoles(value = {"departmentadmin", "depactivityadmin", "superadmin", "phoneoperator"}, logical = Logical.OR)
    @RequestMapping(value = "/activity")
    public String customerList(Model model, HttpServletRequest request,
                               @RequestParam(value = "activityId", required = false) Integer activityId,
                               @RequestParam(value = "status", required = false) Integer status,
                               @RequestParam(value = "startDate", required = false) String startDate,
                               @RequestParam(value = "endDate", required = false) String endDate,
                               @RequestParam(value = "name", required = false) String name,
                               @RequestParam(value = "mediaTypeId", required = false) Integer mediaTypeId,
                               @RequestParam(value = "mediaTypeParentId", required = false) Integer mediaTypeParentId,
                               @RequestParam(value = "province", required = false) String province,
                               @RequestParam(value = "city", required = false) String city,
                               @RequestParam(value = "userId", required = false) Integer userId) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        SearchDataVo vo = SearchUtil.getVo();
        //获取登录用户信息
        SysUser userObj = (SysUser) ShiroUtils.getSessionAttribute(SessionKey.SESSION_LOGIN_USER.toString());
        if(userId != null) {
        	vo.putSearchParam("userId", userId.toString(), userId);
        }
        
        if (activityId != null) {
            vo.putSearchParam("activityId", activityId.toString(), activityId);
        }
        
        if (status != null) {
            vo.putSearchParam("status", status.toString(), status);
        }
        
        if (startDate != null) {
            try {
                vo.putSearchParam("startDate", startDate, sdf.parse(startDate));
            } catch (ParseException e) {
            }
        }
        if (endDate != null) {
            try {
                vo.putSearchParam("endDate", endDate, sdf.parse(endDate));
            } catch (ParseException e) {
            	logger.error(e);
            }
        }
        //查询活动名称
        if (name != null) {
        	name = "%" + name + "%";
            vo.putSearchParam("activityName", name, name);
        }
        //媒体大类
        if (mediaTypeParentId != null) {
            vo.putSearchParam("mediaTypeParentId", mediaTypeParentId.toString(), mediaTypeParentId);
        }
        //媒体小类
        if (mediaTypeId != null) {
        	vo.putSearchParam("mediaTypeId", mediaTypeId.toString(), mediaTypeId);
        }
        //省
        if (province != null) {
        	vo.putSearchParam("province", province.toString(), province);
        }
        //城市
        if (city != null) {
            vo.putSearchParam("city", city.toString(), city);
        }
    	adActivityService.getPageData(vo);
        SearchUtil.putToModel(model, vo);
        model.addAttribute("user",userObj);
        return PageConst.RESOURCES_ACTIVITY;
    }
    
    /**
     * 【任务审核部门】领导/【超级管理员】查看 监测任务审核页面
     */
    @RequiresRoles(value = {"departmentadmin", "deptaskadmin", "superadmin","jiucuoadmin", "phoneoperator"}, logical = Logical.OR)
    @RequestMapping(value = "/taskList")
    public String getTaskList(Model model, HttpServletRequest request,
                              @RequestParam(value = "activityId", required = false) Integer activityId,
                              @RequestParam(value = "taskType", required = false) Integer taskType,
                              @RequestParam(value = "status", required = false) Integer status,
                              @RequestParam(value = "problemStatus", required = false) Integer problemStatus,
                              @RequestParam(value = "startDate", required = false) String startDate,
                              @RequestParam(value = "endDate", required = false) String endDate,
                              @RequestParam(value = "pid", required = false) Integer pid,
                              @RequestParam(value = "ptype", required = false) Integer ptype,
                              @RequestParam(value = "mediaId", required = false) Integer mediaId,
                              @RequestParam(value = "name", required = false) String name,
                              @RequestParam(value = "mediaTypeId", required = false) Integer mediaTypeId,
                              @RequestParam(value = "mediaTypeParentId", required = false) Integer mediaTypeParentId,
                              @RequestParam(value = "province", required = false) Long province,
                              @RequestParam(value = "city", required = false) Long city) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SearchDataVo vo = SearchUtil.getVo();
        
     	//获取登录用户信息
        SysUser userObj = (SysUser) ShiroUtils.getSessionAttribute(SessionKey.SESSION_LOGIN_USER.toString());
        if (activityId != null) {
            vo.putSearchParam("activityId", activityId.toString(), activityId);
        }
        if (taskType != null) {
            vo.putSearchParam("taskType", taskType.toString(), taskType);
        }
        
//        //限制不查询【上刊任务】
//  		List<Integer> notTaskTypes = new ArrayList<>();
//  		notTaskTypes.add(MonitorTaskType.UP_TASK.getId());
//  		vo.putSearchParam("notTaskTypes", null, notTaskTypes);
  		
        if (status != null) {
            vo.putSearchParam("status", status.toString(), status);
            model.addAttribute("status", status);
        }
        if (problemStatus != null) {
            vo.putSearchParam("problemStatus", problemStatus.toString(), problemStatus);
        }
        //pid和ptype配合用于从纠纠错复查监测任务和监测任务查复查监测任务用，好像有点绕
        //如果查纠错的复查监测任务
        if (pid != null && ptype != null) {
            if (ptype == RewardTaskType.JIUCUO.getId()) {
                vo.putSearchParam("parentId", pid.toString(), pid);
                vo.putSearchParam("parentType", ptype.toString(), ptype);
            } else {
                //任务和对应的复查任务都是监测任务，这里一起查出来方便查看，mapper里注意一下写法
                vo.putSearchParam("idpid", pid.toString(), pid);
            }
        }
        if (startDate != null) {
            try {
                vo.putSearchParam("startDate", startDate, sdf.parse(startDate));
            } catch (ParseException e) {
            	logger.error(e);
            }
        }
        if (endDate != null) {
            try {
                vo.putSearchParam("endDate", endDate, sdf.parse(endDate));
            } catch (ParseException e) {
            }
        }
        //查询媒体主
        if (mediaId != null) {
            vo.putSearchParam("mediaId", mediaId.toString(), mediaId);
        }
        //查询活动名称
        if (name != null) {
        	name = "%" + name + "%";
            vo.putSearchParam("activityName", name, name);
        }
        //媒体大类
        if (mediaTypeParentId != null) {
            vo.putSearchParam("mediaTypeParentId", mediaTypeParentId.toString(), mediaTypeParentId);
        }
        //媒体小类
        if (mediaTypeId != null) {
        	vo.putSearchParam("mediaTypeId", mediaTypeId.toString(), mediaTypeId);
        }
        //省
        if (province != null) {
        	vo.putSearchParam("province", province.toString(), province);
        }
        //城市
        if (city != null) {
            vo.putSearchParam("city", city.toString(), city);
        }
    	adMonitorTaskService.getPageData(vo);
        // vo.putSearchParam("hasUserId","1","1");
        SearchUtil.putToModel(model, vo);
        model.addAttribute("user",userObj);
        return PageConst.RESOURCES_TASK_LIST;
    }

    /**
     * 【任务审核部门】领导/【超级管理员】查看 监测任务指派页面
     */
    @RequiresRoles(value = {"departmentadmin", "deptaskadmin", "superadmin", "phoneoperator"}, logical = Logical.OR)
    @RequestMapping(value = "/taskUnassign")
    public String getUnAssignList(Model model, HttpServletRequest request,
                                  @RequestParam(value = "activityId", required = false) Integer activityId,
                                  @RequestParam(value = "startDate", required = false) String startDate,
                                  @RequestParam(value = "status", required = false) Integer status,
                                  @RequestParam(value = "endDate", required = false) String endDate,
                                  @RequestParam(value = "mediaId", required = false) Integer mediaId,
                                  @RequestParam(value = "name", required = false) String name,
                                  @RequestParam(value = "mediaTypeId", required = false) Integer mediaTypeId,
                                  @RequestParam(value = "mediaTypeParentId", required = false) Integer mediaTypeParentId,
                                  @RequestParam(value = "province", required = false) Long province,
                                  @RequestParam(value = "city", required = false) Long city) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SearchDataVo vo = SearchUtil.getVo();

        //获取登录用户信息
        SysUser userObj = (SysUser) ShiroUtils.getSessionAttribute(SessionKey.SESSION_LOGIN_USER.toString());
        if (status == null) {
            vo.putSearchParam("statuses", null,
                    new Integer[]{MonitorTaskStatus.UNASSIGN.getId(), MonitorTaskStatus.CAN_GRAB.getId(), MonitorTaskStatus.TO_CARRY_OUT.getId()});
        } else {
            vo.putSearchParam("status", String.valueOf(status), String.valueOf(status));
        }
        //运营平台指派任务只指派监测期间的任务
//        vo.putSearchParam("taskTypes", null, new Integer[]{MonitorTaskType.UP_MONITOR.getId(),MonitorTaskType.DURATION_MONITOR.getId(),MonitorTaskType.DOWNMONITOR.getId(), MonitorTaskType.FIX_CONFIRM.getId()});

        //限制不查询【上刊任务】
  		List<Integer> notTaskTypes = new ArrayList<>();
  		notTaskTypes.add(MonitorTaskType.UP_TASK.getId());
  		vo.putSearchParam("notTaskTypes", null, notTaskTypes);
        
        if (activityId != null) {
            vo.putSearchParam("activityId", activityId.toString(), activityId);
        }
        if (startDate != null) {
            try {
                vo.putSearchParam("startDate", startDate, sdf.parse(startDate));
            } catch (ParseException e) {
            	logger.error(e);
            }
        }
        if (endDate != null) {
            try {
                vo.putSearchParam("endDate", endDate, sdf.parse(endDate));
            } catch (ParseException e) {
            	logger.error(e);
            }
        }
       //查询媒体主
        if (mediaId != null) {
            vo.putSearchParam("mediaId", mediaId.toString(), mediaId);
        }
        //查询活动名称
        if (name != null) {
        	name = "%" + name + "%";
            vo.putSearchParam("activityName", name, name);
        }
        //媒体大类
        if (mediaTypeParentId != null) {
            vo.putSearchParam("mediaTypeParentId", mediaTypeParentId.toString(), mediaTypeParentId);
        }
        //媒体小类
        if (mediaTypeId != null) {
        	vo.putSearchParam("mediaTypeId", mediaTypeId.toString(), mediaTypeId);
        }
        //省
        if (province != null) {
        	vo.putSearchParam("province", province.toString(), province);
        }
        //城市
        if (city != null) {
            vo.putSearchParam("city", city.toString(), city);
        }
    	adMonitorTaskService.getPageData(vo);
        SearchUtil.putToModel(model, vo);
        model.addAttribute("user",userObj);
        return PageConst.RESOURCES_TASK_UNASSIGN;
    }
    
    /**
     * 【任务审核部门】领导/【超级管理员】查看 上刊任务指派页面
     */
    @RequiresRoles(value = {"departmentadmin", "deptaskadmin", "superadmin" , "phoneoperator"}, logical = Logical.OR)
    @RequestMapping(value = "/upTaskList")
    public String getUpTaskList(Model model, HttpServletRequest request,
                                  @RequestParam(value = "activityId", required = false) Integer activityId,
                                  @RequestParam(value = "startDate", required = false) String startDate,
                                  @RequestParam(value = "status", required = false) Integer status,
                                  @RequestParam(value = "endDate", required = false) String endDate,
                                  @RequestParam(value = "mediaId", required = false) Integer mediaId,
                                  @RequestParam(value = "name", required = false) String name,
                                  @RequestParam(value = "mediaTypeId", required = false) Integer mediaTypeId,
                                  @RequestParam(value = "mediaTypeParentId", required = false) Integer mediaTypeParentId,
                                  @RequestParam(value = "province", required = false) Long province,
                                  @RequestParam(value = "city", required = false) Long city) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SearchDataVo vo = SearchUtil.getVo();

        //获取登录用户信息
        SysUser userObj = (SysUser) ShiroUtils.getSessionAttribute(SessionKey.SESSION_LOGIN_USER.toString());
        if (status == null) {
            vo.putSearchParam("statuses", null,
                    new Integer[]{MonitorTaskStatus.UNASSIGN.getId(), MonitorTaskStatus.TO_CARRY_OUT.getId(), MonitorTaskStatus.CAN_GRAB.getId()});
        } else {
            vo.putSearchParam("status", String.valueOf(status), String.valueOf(status));
        }
        //运营平台指派任务只指派监测期间的任务
//        vo.putSearchParam("taskTypes", null, new Integer[]{MonitorTaskType.UP_MONITOR.getId(),MonitorTaskType.DURATION_MONITOR.getId(),MonitorTaskType.DOWNMONITOR.getId(), MonitorTaskType.FIX_CONFIRM.getId()});

        //限制只查询【上刊任务】
		Integer taskType = MonitorTaskType.UP_TASK.getId();
		vo.putSearchParam("taskType", taskType.toString(), taskType);
        
        if (activityId != null) {
            vo.putSearchParam("activityId", activityId.toString(), activityId);
        }
        if (startDate != null) {
            try {
                vo.putSearchParam("startDate", startDate, sdf.parse(startDate));
            } catch (ParseException e) {
            }
        }
        if (endDate != null) {
            try {
                vo.putSearchParam("endDate", endDate, sdf.parse(endDate));
            } catch (ParseException e) {
            	logger.error(e);
            }
        }
       //查询媒体主
        if (mediaId != null) {
            vo.putSearchParam("mediaId", mediaId.toString(), mediaId);
        }
        //查询活动名称
        if (name != null) {
        	name = "%" + name + "%";
            vo.putSearchParam("activityName", name, name);
        }
        //媒体大类
        if (mediaTypeParentId != null) {
            vo.putSearchParam("mediaTypeParentId", mediaTypeParentId.toString(), mediaTypeParentId);
        }
        //媒体小类
        if (mediaTypeId != null) {
        	vo.putSearchParam("mediaTypeId", mediaTypeId.toString(), mediaTypeId);
        }
        //省
        if (province != null) {
        	vo.putSearchParam("province", province.toString(), province);
        }
        //城市
        if (city != null) {
            vo.putSearchParam("city", city.toString(), city);
        }
    	adMonitorTaskService.getPageData(vo);
        SearchUtil.putToModel(model, vo);
        model.addAttribute("user",userObj);
        return PageConst.UP_TASK_UNASSIGN;
    }
    
    /**
     * 【纠错审核部门】领导/【超级管理员】查看 纠错任务页面
     */
    @RequiresRoles(value = {"departmentadmin", "depjiucuoadmin", "superadmin", "phoneoperator"}, logical = Logical.OR)
    @RequestMapping(value = "/jiucuoList")
    public String joucuoList(Model model, HttpServletRequest request,
                             @RequestParam(value = "id", required = false) Integer id,
                             @RequestParam(value = "activityId", required = false) Integer activityId,
                             @RequestParam(value = "status", required = false) Integer status,
                             @RequestParam(value = "problemStatus", required = false) Integer problemStatus,
                             @RequestParam(value = "startDate", required = false) String startDate,
                             @RequestParam(value = "endDate", required = false) String endDate,
                             @RequestParam(value = "mediaId", required = false) Integer mediaId,
                             @RequestParam(value = "name", required = false) String name,
                             @RequestParam(value = "mediaTypeId", required = false) Integer mediaTypeId,
                             @RequestParam(value = "mediaTypeParentId", required = false) Integer mediaTypeParentId,
                             @RequestParam(value = "province", required = false) Long province,
                             @RequestParam(value = "city", required = false) Long city) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SearchDataVo vo = SearchUtil.getVo();
        
        //获取登录用户信息
        SysUser userObj = (SysUser) ShiroUtils.getSessionAttribute(SessionKey.SESSION_LOGIN_USER.toString());
        if (id != null) {
            vo.putSearchParam("id", id.toString(), id);
        }
        if (activityId != null) {
            vo.putSearchParam("activityId", activityId.toString(), activityId);
        }
        if (status != null) {
            vo.putSearchParam("status", status.toString(), status);
        }
        
        if (problemStatus != null) {
            vo.putSearchParam("problemStatus", problemStatus.toString(), problemStatus);
        }
        if (startDate != null) {
            try {
                vo.putSearchParam("startDate", startDate, sdf.parse(startDate));
            } catch (ParseException e) {
            	logger.error(e);
            }
        }
        if (endDate != null) {
            try {
                vo.putSearchParam("endDate", endDate, sdf.parse(endDate));
            } catch (ParseException e) {
            	logger.error(e);
            }
        }
      //查询媒体主
        if (mediaId != null) {
            vo.putSearchParam("mediaId", mediaId.toString(), mediaId);
        }
        //查询活动名称
        if (name != null) {
        	name = "%" + name + "%";
            vo.putSearchParam("activityName", name, name);
        }
        //媒体大类
        if (mediaTypeParentId != null) {
            vo.putSearchParam("mediaTypeParentId", mediaTypeParentId.toString(), mediaTypeParentId);
        }
        //媒体小类
        if (mediaTypeId != null) {
        	vo.putSearchParam("mediaTypeId", mediaTypeId.toString(), mediaTypeId);
        }
        //省
        if (province != null) {
        	vo.putSearchParam("province", province.toString(), province);
        }
        //城市
        if (city != null) {
            vo.putSearchParam("city", city.toString(), city);
        }
    	adJiucuoTaskService.getPageData(vo);
        SearchUtil.putToModel(model, vo);
        model.addAttribute("user",userObj);
        return PageConst.RESOURCES_JIUCUO_LIST;
    }
    
    /**
     * 【活动审核部门】【任务审核部门】【纠错审核部门】领导 删除组
     */
    @RequiresRoles(value = {"departmentadmin", "depactivityadmin", "deptaskadmin", "depjiucuoadmin","superadmin"}, logical = Logical.OR)
    @ResponseBody
    @RequestMapping(value = "/deleteGroup")
    public Model deleteGroup(Model model ,HttpServletRequest request,
            @RequestParam(value = "id", required = false) Integer id) {
    	 ResultVo<String> result = new ResultVo<String>();
         result.setCode(ResultCode.RESULT_SUCCESS.getCode());
         result.setResultDes("删除成功");
         model = new ExtendedModelMap();
    	 try {
	         sysGroupService.deleteGroup(id);
    	}catch (Exception e) {
    		logger.error(e);
            result.setCode(ResultCode.RESULT_FAILURE.getCode());
            result.setResultDes("删除失败！");
            model.addAttribute(SysConst.RESULT_KEY, result);
            return model;
        }
        model.addAttribute(SysConst.RESULT_KEY, result);
        return model;
    }
    
    //站内信列表
    @RequestMapping(value = "/messageList")
    public String messageList(Model model ,HttpServletRequest request,
            @RequestParam(value = "id", required = false) Integer id,
            @RequestParam(value = "type", required = false) Integer type,
            @RequestParam(value = "isFinish", required = false) Integer isFinish,
            @RequestParam(value = "searchContent", required = false) String content
            )throws ParseException {
        SearchDataVo vo = SearchUtil.getVo();
        SysUser user = (SysUser) ShiroUtils.getSessionAttribute(SessionKey.SESSION_LOGIN_USER.toString());
        vo.putSearchParam("targetUserId", null, user.getId());
        if (type != null) {
            vo.putSearchParam("type", type.toString(), type);
        }
        if (isFinish != null) {
            vo.putSearchParam("isFinish", isFinish.toString(), isFinish);
        }
        if(StringUtil.isNotBlank(content)) {
        	model.addAttribute("searchContent", content);
        	content = "%" + content + "%";
        	vo.putSearchParam("content", content, content);
        }
        
        adUserMessageService.getPageData(vo);
        SearchUtil.putToModel(model, vo);

        return PageConst.RESOURCES_MESSAGE_LIST;
    }
    
    //呼叫中心人员管理phoneoperatorList
    @RequiresRoles(value={"superadmin"}, logical = Logical.OR)
    @RequestMapping(value="/phoneoperatorList")
    public String phoneoperatorList(Model model ,HttpServletRequest request,
    		@RequestParam(value = "nameOrUsername", required = false) String nameOrUsername) {
    	 SearchDataVo vo = SearchUtil.getVo();

//         SysUser user = (SysUser) ShiroUtils.getSessionAttribute(SessionKey.SESSION_LOGIN_USER.toString());
         // 名称或登录账号
         if (StringUtils.isNotBlank(nameOrUsername)) {
             vo.putSearchParam("nameOrUsername", nameOrUsername, "%" + nameOrUsername + "%");
         }
         vo.putSearchParam("usertype", "usertype", UserTypeEnum.PHONE_OPERATOR.getId());
         sysUserService.getPageData(vo);
         SearchUtil.putToModel(model, vo);

    	return PageConst.RESOURCES_PHONEOPERATOR_LIST;
    }
    
    /**
     * 编辑话务员
     */
    @RequestMapping(value = "/phoneoperatorEdit")
    public String AddPhoneoperator(Model model,HttpServletRequest request,
    		@RequestParam(value = "id", required = false) Integer id) {
    	if (id != null) {
    		SysUser sysUser = sysUserService.findUserinfoById(id);
    		model.addAttribute("obj" ,sysUser);
    	}
        return PageConst.PHONEOPERATOR_EDIT;
    }
    
    /***
     * 保存话务员信息
     * **/
    @ResponseBody
    @RequestMapping(value="/phoneoperatorSave")
    @RequiresRoles("superadmin")
    public Model savePhoneoperator(Model model, SysUser sysUser, HttpServletRequest request,
    		@RequestParam(value = "telephone") String telephone) {
    	ResultVo<String> result = new ResultVo<String>();
        result.setCode(ResultCode.RESULT_SUCCESS.getCode());
        result.setResultDes("保存成功");
        model = new ExtendedModelMap();
        
        if(sysUser.getStatus() == null) {
        	sysUser.setStatus(1);	//可用
        }
        if(sysUser.getPlatform() == null) {
        	sysUser.setPlatform(1); 
        }
        if(sysUser.getUsertype() == null) {
        	sysUser.setUsertype(UserTypeEnum.PHONE_OPERATOR.getId()); //6：话务员 
        }
        sysUser.setMobile(telephone);
        try {
        	if(sysUser.getId() != null) {
        		sysUserService.modify(sysUser);
        	}else {
        		sysUserService.addUser(sysUser, UserRoleEnum.PHONE_OPERATOR.getId());
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
}
