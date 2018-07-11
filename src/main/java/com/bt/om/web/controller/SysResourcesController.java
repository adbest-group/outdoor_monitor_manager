package com.bt.om.web.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresRoles;
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
import com.bt.om.entity.AdApp;
import com.bt.om.entity.AdPoint;
import com.bt.om.entity.SysResources;
import com.bt.om.entity.vo.UserRoleVo;
import com.bt.om.enums.ResultCode;
import com.bt.om.service.IPointService;
import com.bt.om.service.ISysResourcesService;
import com.bt.om.service.ISysUserService;
import com.bt.om.service.IUserPointService;
import com.bt.om.vo.web.ResultVo;
import com.bt.om.vo.web.SearchDataVo;
import com.bt.om.web.BasicController;
import com.bt.om.web.util.SearchUtil;

@Controller
@RequestMapping("/sysResources")
public class SysResourcesController extends BasicController {

	@Autowired
	private ISysResourcesService sysResourcesService;
	
	@Autowired
	private ISysUserService sysUserService;
	
	@Autowired
	private IPointService pointService;
	
	@Autowired
	private IUserPointService userpointService;
	
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
            	//修改
            	SysResources department = sysResourcesService.getById(sysResources.getId()); //数据库里当前部门的信息
            	Integer userId = sysResources.getUserId(); //获取页面选择的部门领导id
            	if(userId != null) {
            		//选择了部门领导
            		if(department.getUserId() != userId) {
                		//查询该领导是否已经有管理部门
                    	int count = sysResourcesService.selectCountByUserId(userId);
                    	if(count > 0) {
                    		result.setCode(ResultCode.RESULT_FAILURE.getCode());
                            result.setResultDes("该部门领导已有部门！");
                            model.addAttribute(SysConst.RESULT_KEY, result);
                            return model;
                    	} else {
                    		//修改部门
                    		sysResources.setUserId(userId);
                    		sysResources.setUpdateTime(now);
                        	sysResourcesService.modify(sysResources);
                        	
                        	//修改领导的role从104：departmentadmin到
                        	Integer roleId = 104;
                        	if(department.getDepartmentType() == 1) {
                        		//活动审核部门 depactivityadmin
                        		roleId = 108;
                        	} else if(department.getDepartmentType() == 2) {
                        		//任务审核、指派部门 deptaskadmin 
                        		roleId = 109;
                        	} else if(department.getDepartmentType() == 3) {
                        		//纠错审核部门 depjiucuoadmin
                        		roleId = 110;
                        	}
                        	
                        	UserRoleVo userRoleVo = new UserRoleVo();
                        	List<Integer> userIds = new ArrayList<Integer>();
                        	userIds.add(userId);
                        	userRoleVo.setRoleId(roleId);
                        	userRoleVo.setUserIds(userIds);
                        	userRoleVo.setUpdateTime(now);
                        	sysUserService.updateListUserRes(userRoleVo);
                    	}
                	} else {
                		//修改部门
                		sysResources.setUserId(department.getUserId());
                		sysResources.setUpdateTime(now);
                    	sysResourcesService.modify(sysResources);
                	}
            	} else {
            		//清空部门领导
            		sysResources.setUserId(null);
            		sysResources.setUpdateTime(now);
                	sysResourcesService.updateByPrimaryKeyUserIdNull(sysResources);

                	//修改领导的role改回104：departmentadmin
                	UserRoleVo userRoleVo = new UserRoleVo();
                	List<Integer> userIds = new ArrayList<Integer>();
                	userIds.add(department.getUserId());
                	userRoleVo.setRoleId(104);
                	userRoleVo.setUserIds(userIds);
                	userRoleVo.setUpdateTime(now);
                	sysUserService.updateListUserRes(userRoleVo);
            	}
            } else {
            	//插入
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
    
    @RequiresRoles("superadmin")
    @RequestMapping(value = "/pointList")
    public String pointList(Model model, HttpServletRequest request,
            @RequestParam(value = "name", required = false) String name) {
    	SearchDataVo vo = SearchUtil.getVo();
        //查询积分明细名称
        if (name != null) {
        	name = "%" + name + "%";
            vo.putSearchParam("name", name, name);
        }
        //查询积分列表
        pointService.getPageData(vo);
        SearchUtil.putToModel(model, vo);
        return PageConst.SUPER_ADMIN_POINT_LIST;
    }
    
    @RequiresRoles("superadmin")
	@RequestMapping(value = "/pointEdit")
	public String appEdit(Model model, HttpServletRequest request,
            @RequestParam(value = "id", required = false) Integer id) {
		AdPoint adpoint = pointService.getVoById(id);
		if (adpoint != null) {
            model.addAttribute("adpoint", adpoint);
		}	
        return PageConst.SUPER_ADMIN_POINT_EDIT;
	}
	
	@RequiresRoles("superadmin")
	@ResponseBody
	@RequestMapping(value = "/save", method=RequestMethod.POST)
	public Model appSave(Model model, AdPoint adpoint, HttpServletRequest request) {
		ResultVo<String> result = new ResultVo<String>();
        result.setCode(ResultCode.RESULT_SUCCESS.getCode());
        result.setResultDes("保存成功");
        model = new ExtendedModelMap();
        Date now = new Date();
     
        try {
            if (adpoint.getId() != null) {
            	if(adpoint.getName()==null || adpoint.getName().equals("")) {
            		result.setCode(ResultCode.RESULT_FAILURE.getCode());
                    result.setResultDes("积分名称不能为空！");
                    model.addAttribute(SysConst.RESULT_KEY, result);
                    return model;
            	}else if(adpoint.getPoint()==null || adpoint.getPoint()==0){
            		result.setCode(ResultCode.RESULT_FAILURE.getCode());
                    result.setResultDes("积分值不能为空！");
                    model.addAttribute(SysConst.RESULT_KEY, result);
                    return model;
            	}else {
            		adpoint.setUpdateTime(now);
                	pointService.modify(adpoint);
            	}
            } else {
            	adpoint.setCreateTime(now);
            	adpoint.setUpdateTime(now);
            	pointService.save(adpoint);
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
	
	 @RequiresRoles("superadmin")
	 @RequestMapping(value = "/user/pointList")
	 public String userPointList(Model model, HttpServletRequest request,
	         @RequestParam(value = "username", required = false) String username) {
	    SearchDataVo vo = SearchUtil.getVo();
	    //查询指定用户积分明细
	    if (username != null) {
	    	username = "%" + username + "%";
	        vo.putSearchParam("username", username, username);
	    }
	    //查询用户积分列表
	    userpointService.getPageData(vo);
	    SearchUtil.putToModel(model, vo);
	    return PageConst.SUPER_ADMIN_USERPOINT_LIST;
	}
}
