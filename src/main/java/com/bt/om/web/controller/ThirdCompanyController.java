package com.bt.om.web.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
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
import com.bt.om.entity.SysUser;
import com.bt.om.entity.SysUserExecute;
import com.bt.om.enums.MonitorTaskStatus;
import com.bt.om.enums.MonitorTaskType;
import com.bt.om.enums.ResultCode;
import com.bt.om.enums.RewardTaskType;
import com.bt.om.enums.SessionKey;
import com.bt.om.enums.SysUserExecuteType;
import com.bt.om.enums.UserRoleEnum;
import com.bt.om.enums.UserTypeEnum;
import com.bt.om.filter.LogFilter;
import com.bt.om.security.ShiroUtils;
import com.bt.om.service.IAdMonitorTaskService;
import com.bt.om.service.ISysUserExecuteService;
import com.bt.om.service.ISysUserService;
import com.bt.om.vo.web.ResultVo;
import com.bt.om.vo.web.SearchDataVo;
import com.bt.om.web.util.SearchUtil;

/**
 * 第三方监测公司 相关控制类
 */
@Controller
@RequestMapping("/thirdCompany")
public class ThirdCompanyController {
	@Autowired
	private ISysUserService sysUserService;
	@Autowired
	private ISysUserExecuteService sysUserExecuteService;
	@Autowired
    IAdMonitorTaskService adMonitorTaskService;
	private static final Logger logger = Logger.getLogger(ThirdCompanyController.class);
	
	/**
	 * 第三方监测公司 查询
	 */
    @RequiresRoles(value={"superadmin"}, logical = Logical.OR)
    @RequestMapping(value="/list")
    public String phoneoperatorList(Model model ,HttpServletRequest request,
    		@RequestParam(value = "nameOrUsername", required = false) String nameOrUsername) {
    	 SearchDataVo vo = SearchUtil.getVo();
         // 名称或登录账号
         if (StringUtils.isNotBlank(nameOrUsername)) {
             vo.putSearchParam("nameOrUsername", nameOrUsername, "%" + nameOrUsername + "%");
         }
         vo.putSearchParam("usertype", "usertype", UserTypeEnum.THIRD_COMPANY.getId());
         sysUserService.getPageData(vo);
         SearchUtil.putToModel(model, vo);

    	return PageConst.THIRD_COMPANY_LIST;
    }
    
    /**
     * 前往 编辑第三方监测公司
     */
    @RequestMapping(value = "/toEdit")
    public String AddPhoneoperator(Model model,HttpServletRequest request,
    		@RequestParam(value = "id", required = false) Integer id) {
    	if (id != null) {
    		SysUser sysUser = sysUserService.findUserinfoById(id);
    		model.addAttribute("obj" ,sysUser);
    	}
        return PageConst.THIRD_COMPANY_EDIT;
    }
    
    /**
     * 新增/修改第三方监测公司
     */
    @RequestMapping(value="/save")
    @RequiresRoles("superadmin")
    @ResponseBody
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
        	sysUser.setUsertype(UserTypeEnum.THIRD_COMPANY.getId()); //7：第三方监测公司
        }
        sysUser.setMobile(telephone);
        try {
        	if(sysUser.getId() != null) {
        		sysUserService.modify(sysUser);
        	} else {
        		sysUserService.addUser(sysUser, UserRoleEnum.THIRD_COMPANY.getId());
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
     * 第三方监测公司用户列表
     * */
    @RequiresRoles("thirdcompany")
    @RequestMapping(value = "/userlist")
    public String userList(Model model ,HttpServletRequest request,
    		@RequestParam(value = "nameOrUsername", required = false) String nameOrUsername) {
    	 SearchDataVo vo = SearchUtil.getVo();
    	 SysUser userObj = (SysUser) ShiroUtils.getSessionAttribute(SessionKey.SESSION_LOGIN_USER.toString());
         // 名称或登录账号
         if (StringUtils.isNotBlank(nameOrUsername)) {
             vo.putSearchParam("nameOrUsername", nameOrUsername, "%" + nameOrUsername + "%");
         }
         vo.putSearchParam("usertype", "usertype", SysUserExecuteType.THIRDCOMPANY.getId());
         vo.putSearchParam("operateId", null, userObj.getId());
         sysUserExecuteService.getPageData(vo);
         SearchUtil.putToModel(model, vo);

    	return PageConst.THIRD_COMPANY_USER_LIST;
    }
    
    /**
     * 前往 编辑第三方监测公司
     */
    @RequestMapping(value = "/userEdit")
    public String AddUserMember(Model model,HttpServletRequest request,
    		@RequestParam(value = "id", required = false) Integer id) {
    	if (id != null) {
    		SysUserExecute sysUserExecute = sysUserExecuteService.getById(id);
    		model.addAttribute("obj" ,sysUserExecute);
    	}
        return PageConst.THIRD_COMPANY_USER_EDIT;
    }
    
    /**
     * 新增 第三方监测公司成员
     * */
    @RequestMapping(value = "/saveUser")
    @ResponseBody
    public Model saveMember(Model model, SysUserExecute sysUserExecute, HttpServletRequest request,
    		@RequestParam(value = "username" , required = false) String username,
    		@RequestParam(value = "password" , required = false) String password,
    		@RequestParam(value = "name" , required = false) String name,
    		@RequestParam(value = "companyId" , required = false) Integer companyId) {
    	ResultVo<String> result = new ResultVo<String>();
        result.setCode(ResultCode.RESULT_SUCCESS.getCode());
        result.setResultDes("保存成功");
        model = new ExtendedModelMap();
        
        Date now = new Date();
        if(sysUserExecute.getStatus() == null) {
        	sysUserExecute.setStatus(1);	//可用
        }
        if(sysUserExecute.getUsertype() == null) {
        	sysUserExecute.setUsertype(SysUserExecuteType.THIRDCOMPANY.getId());//5 第三方监测公司
        }
        sysUserExecute.setPassword(new Md5Hash(password, username).toString());
        sysUserExecute.setRealname(name);
        sysUserExecute.setOperateId(companyId);
        sysUserExecute.setMobile(username);
        
        try {
        	if(sysUserExecute.getId() != null) {
        		if (!"******".equals(password)) {
        			sysUserExecute.setPassword(new Md5Hash(password, username).toString());
                }else {
                	sysUserExecute.setPassword(null);
                }
        		sysUserExecute.setUpdateTime(now);
        		sysUserExecuteService.modify(sysUserExecute); 
        	} else {
        		sysUserExecute.setCreateTime(now);
        		sysUserExecute.setUpdateTime(now);
        		sysUserExecuteService.add(sysUserExecute);
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
    
    @RequiresRoles("thirdcompany")
    @RequestMapping(value = {"/updateStatus"}, method = {RequestMethod.POST})
    @ResponseBody
    public Model updateStatus(Model model, @RequestParam(value = "id", required = true) Integer id,
                                     @RequestParam(value = "status", required = true) Integer status) {
        ResultVo resultVo = new ResultVo();
        try {
            SysUserExecute user = new SysUserExecute();
            user.setId(id);
            user.setStatus(status);
            sysUserExecuteService.modify(user);
        } catch (Exception ex) {
        	logger.error(ex);
            ex.printStackTrace();
            resultVo.setCode(ResultCode.RESULT_FAILURE.getCode());
            resultVo.setResultDes("服务忙，请稍后再试");
        }

        model.addAttribute(SysConst.RESULT_KEY, resultVo);
        return model;
    }
    
    /**
     * 查看 上刊任务指派页面
     */
    @RequiresRoles(value = {"thirdcompany" ,"taskadmin"}, logical = Logical.OR)
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
        vo.putSearchParam("companyId", userObj.getId().toString(), userObj.getId());
    	adMonitorTaskService.getPageData(vo);
        SearchUtil.putToModel(model, vo);
        model.addAttribute("user",userObj);
        return PageConst.THIRDCOMPANY_UP_TASK_UNASSIGN;
    }
    
    /**
     * 查看 监测任务指派页面
     */
    @RequiresRoles(value = {"thirdcompany","taskadmin"}, logical = Logical.OR)
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
        vo.putSearchParam("companyId", userObj.getId().toString(), userObj.getId());
    	adMonitorTaskService.getPageData(vo);
        SearchUtil.putToModel(model, vo);
        model.addAttribute("user",userObj);
        return PageConst.THIRDCOMPANY_TASK_UNASSIGN;
    }
    
    /**
     * 查看 监测任务审核页面
     */
    @RequiresRoles(value = {"thirdcompany","taskadmin"}, logical = Logical.OR)
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
        vo.putSearchParam("companyId", userObj.getId().toString(), userObj.getId());
    	adMonitorTaskService.getPageData(vo);
        SearchUtil.putToModel(model, vo);
        model.addAttribute("user",userObj);
        return PageConst.THIRDCOMPANY_TASK_LIST;
    }

}
