package com.bt.om.web.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.adtime.common.lang.StringUtil;
import com.bt.om.common.SysConst;
import com.bt.om.common.web.PageConst;
import com.bt.om.entity.AdActivity;
import com.bt.om.entity.AdActivityAdseat;
import com.bt.om.entity.AdJiucuoTask;
import com.bt.om.entity.AdJiucuoTaskFeedback;
import com.bt.om.entity.AdMonitorTask;
import com.bt.om.entity.SysUser;
import com.bt.om.entity.vo.AdJiucuoTaskVo;
import com.bt.om.entity.vo.AdMonitorTaskVo;
import com.bt.om.enums.JiucuoTaskStatus;
import com.bt.om.enums.ResultCode;
import com.bt.om.enums.SessionKey;
import com.bt.om.enums.TaskProblemStatus;
import com.bt.om.security.ShiroUtils;
import com.bt.om.service.IAdActivityService;
import com.bt.om.service.IAdJiucuoTaskService;
import com.bt.om.service.IAdMonitorTaskService;
import com.bt.om.service.IMediaService;
import com.bt.om.service.ISysGroupService;
import com.bt.om.service.ISysResourcesService;
import com.bt.om.service.ISysUserRoleService;
import com.bt.om.service.ISysUserService;
import com.bt.om.vo.web.ResultVo;
import com.bt.om.vo.web.SearchDataVo;
import com.bt.om.web.BasicController;
import com.bt.om.web.util.JPushUtils;
import com.bt.om.web.util.SearchUtil;

/**
 * Created by caiting on 2018/1/20.
 */
@Controller
@RequestMapping(value = "/jiucuo")
public class JiucuoController extends BasicController {
    @Autowired
    private IAdJiucuoTaskService adJiucuoTaskService;
    @Autowired
    private IAdActivityService adActivityService;
    @Autowired
    IAdMonitorTaskService adMonitorTaskService;
    @Autowired
    private ISysUserService sysUserService;
    @Autowired
    IMediaService mediaService;
    @Autowired
	private ISysGroupService sysGroupService;
	@Autowired
	private ISysResourcesService sysResourcesService;
	@Autowired
	private ISysUserRoleService sysUserRoleService;
	@Autowired
    protected RedisTemplate redisTemplate;

	/**
     * 查看纠错审核列表
     */
    @RequestMapping(value = "/list")
    @RequiresRoles(value = {"jiucuoadmin","superadmin","depjiucuoadmin"},logical = Logical.OR)
    public String joucuoList(Model model, HttpServletRequest request,
                             @RequestParam(value = "id", required = false) Integer id,
                             @RequestParam(value = "activityId", required = false) Integer activityId,
                             @RequestParam(value = "status", required = false) Integer status,
                             @RequestParam(value = "problemStatus", required = false) Integer problemStatus,
                             @RequestParam(value = "startDate", required = false) String startDate,
                             @RequestParam(value = "endDate", required = false) String endDate) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SearchDataVo vo = SearchUtil.getVo();
        
        //获取登录的审核员工jiucuoadmin
        SysUser userObj = (SysUser) ShiroUtils.getSessionAttribute(SessionKey.SESSION_LOGIN_USER.toString());
        
        //所有未审核的任务
//        Map<String, Object> searchMap1 = new HashMap<>();
//        List<Integer> customerIds = sysUserService.getCustomerIdsByAdminId(userObj.getId()); //根据员工id查询所属组对应的所有广告商id集合
//        searchMap1.put("customerIds", customerIds);
//    	List<AdJiucuoTaskVo> allJiucuoTaskUncertain = adJiucuoTaskService.getAllByStatusUnCheck(searchMap1);
//        Integer shenheCount = 0;

        if (id != null) {
            vo.putSearchParam("id", id.toString(), id);
        }
        if (activityId != null) {
            vo.putSearchParam("activityId", activityId.toString(), activityId);
        }
        if (status != null) {
//        	status = 1; //如果没有传参status, 默认取1：待审核
        	vo.putSearchParam("status", status.toString(), status);
            model.addAttribute("status", status);
        }
        
        if (problemStatus != null) {
            vo.putSearchParam("problemStatus", problemStatus.toString(), problemStatus);
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
            }
        }
        
        List<Integer> customerIds = sysUserService.getCustomerIdsByAdminId(userObj.getId()); //根据员工id查询所属组对应的所有广告商id集合
        if(customerIds != null && customerIds.size() == 0) {
        	//员工对应的广告商id集合为空, 不需要再去查询纠错审核列表
        	vo.setCount(0);
        	vo.setSize(20);
        	vo.setStart(0);
        	vo.setList(null);
        } else {
        	vo.putSearchParam("customerIds", null, customerIds);
        	adJiucuoTaskService.getPageData(vo);
        }
        
//        //只能查询自己参与的纠错任务审核
//        if(userObj != null) {
//        	Integer assessorId = userObj.getId();
//        	vo.putSearchParam("assessorId", assessorId.toString(), assessorId);
//        }
        
//        if(status == 2 || status == 3) {
//        	//查询通过审核 或 未通过审核 的纠错任务
//        	adJiucuoTaskService.getPageData(vo);
//        } else {
//        	//查询待审核的纠错任务
//        	//[1] 先查询审核id的所有待审核的纠错任务
//        	Map<String, Object> searchMap = new HashMap<>();
//        	searchMap.put("status", 1);
//            searchMap.put("assessorId", userObj.getId());
////            searchMap.put("activityId", activityId);
////            searchMap.put("id", id);
////            searchMap.put("problemStatus", problemStatus);
////            searchMap.put("startDate", startDate);
////            searchMap.put("endDate", endDate);
//            List<AdJiucuoTaskVo> taskVos = adJiucuoTaskService.selectAllByAssessorId(searchMap);
//            
//            if(taskVos != null && taskVos.size() > 0) {
//            	//条数大于0, 返回给页面
//            	Iterator<AdJiucuoTaskVo> iterator = taskVos.iterator();
//            	while(iterator.hasNext()) {
//            		boolean remove = false; //不用抹去
//            		AdJiucuoTaskVo taskVo = iterator.next();
//            		//通过页面上的activityId做筛选
//            		if(activityId != null) {
//            			if(taskVo.getActivityId() != activityId) {
//            				remove = true;
//            			}
//            		}
//            		if(id != null) {
//            			if(taskVo.getId() != id) {
//            				remove = true;
//            			}
//            		}
//            		//通过页面上的problemStatus做筛选
//            		if(problemStatus != null) {
//            			if(taskVo.getProblemStatus() != problemStatus) {
//            				remove = true;
//            			}
//            		}
//            		//通过页面上的startDate做筛选
//            		if(StringUtil.isNotBlank(startDate)) {
//            			if(taskVo.getStartTime().getTime() < sdf.parse(startDate).getTime()) {
//            				remove = true;
//            			}
//            		}
//            		//通过页面上的endDate做筛选
//            		if(StringUtil.isNotBlank(endDate)) {
//            			if(taskVo.getEndTime().getTime() > sdf.parse(endDate).getTime()) {
//            				remove = true;
//            			}
//            		}
//            		if(remove == true) {
//            			iterator.remove();
//            		}
//            	}
//            	vo.setCount(taskVos.size());
//            	vo.setSize(20);
//            	vo.setStart(0);
//            	vo.setList(taskVos);
//            	shenheCount = allJiucuoTaskUncertain.size() - taskVos.size();
//            	if(shenheCount < 0) {
//            		shenheCount = 0;
//            	}
//            	model.addAttribute("shenheCount", shenheCount);
//            } else {
//            	//条数等于0, 新查询10条或者小于10条没人认领的待审核的纠错任务(需要匹配 员工 - 组 - 广告商 之间的关系)
//            	//List<Integer> customerIds = sysUserService.getCustomerIdsByAdminId(userObj.getId());
//            	if(customerIds != null && customerIds.size() > 0) {
//            		searchMap.clear();
//            		searchMap.put("status", 1);
//            		searchMap.put("customerIds", customerIds);
//            		searchMap.put("assessorId", userObj.getId());
//            		List<AdJiucuoTaskVo> adJiucuoTaskVos = adJiucuoTaskService.getTenAdMonitorTaskVo(searchMap);
//                	vo.setCount(adJiucuoTaskVos.size());
//                	vo.setSize(20);
//                	vo.setStart(0);
//                	vo.setList(adJiucuoTaskVos);
//                	shenheCount = allJiucuoTaskUncertain.size() - adJiucuoTaskVos.size();
//                	if(shenheCount < 0) {
//                		shenheCount = 0;
//                	}
//                	model.addAttribute("shenheCount", shenheCount);
//            	}
//            }
//        }
        
        SearchUtil.putToModel(model, vo);

        return PageConst.JIUCUO_LIST;
    }

	/**
     * 查看纠错详情
     */
    @RequiresRoles(value = {"jiucuoadmin", "media", "customer", "depjiucuoadmin", "superadmin"}, logical = Logical.OR)
    @RequestMapping(value = "/detail")
    public String showDetail(Model model, HttpServletRequest request,
                             @RequestParam(value = "id", required = false) Integer id) {
        //纠错任务
        AdJiucuoTaskVo task = adJiucuoTaskService.getVoById(id);
        //上传内容
        AdJiucuoTaskFeedback feedback = adJiucuoTaskService.getFeadBackByTaskId(id);
        //广告活动
        AdActivity activity = adActivityService.getVoById(task.getActivityId());
        //广告活动广告位
        AdActivityAdseat seat = adActivityService.getActivitySeatById(task.getActivityAdseatId());
        //因当前纠错发起的子监测任务
        List<AdMonitorTaskVo> subs = adJiucuoTaskService.getSubTask(id);

        model.addAttribute("task", task);
        model.addAttribute("activity", activity);
        model.addAttribute("seat", seat);
        model.addAttribute("feedback", feedback);
        model.addAttribute("subs", subs);
        return PageConst.JIUCUO_DETAIL;
    }

    /**
     * 审核纠错
     */
    @RequiresRoles(value = {"jiucuoadmin", "depjiucuoadmin", "superadmin"}, logical = Logical.OR)
    @RequestMapping(value = "/verify")
    @ResponseBody
    public Model confirm(Model model, HttpServletRequest request,
                         @RequestParam(value = "id", required = false) Integer id,
                         @RequestParam(value = "status", required = false) Integer status,
                         @RequestParam(value = "reason", required = false) String reason) {
        ResultVo<String> result = new ResultVo<String>();
        
    	String beginRedisStr = "jiucuo_" + id + "_begin";
    	String finishRedisStr = "jiucuo_" + id + "_finish";
    	if (redisTemplate.opsForValue().get(finishRedisStr) != null && StringUtil.equals(redisTemplate.opsForValue().get(finishRedisStr) + "", "true")) {
    		result.setCode(ResultCode.RESULT_FAILURE.getCode());
            result.setResultDes("纠错已被审核，请刷新再试！");
            model.addAttribute(SysConst.RESULT_KEY, result);
            return model;
    	}
    	if (redisTemplate.opsForValue().get(beginRedisStr) != null && StringUtil.equals(redisTemplate.opsForValue().get(beginRedisStr) + "", "true")) {
    		result.setCode(ResultCode.RESULT_FAILURE.getCode());
    		 result.setResultDes("纠错正被审核中，请刷新再试！");
            model.addAttribute(SysConst.RESULT_KEY, result);
            return model;
    	}
    	
    	//放入Redis缓存处理并发
    	redisTemplate.opsForValue().set(beginRedisStr, "true");
        
        result.setCode(ResultCode.RESULT_SUCCESS.getCode());
        result.setResultDes("审核成功");
        model = new ExtendedModelMap();

        AdJiucuoTask task = new AdJiucuoTask();
        task.setId(id);
        try {
            //获取登录的审核人(员工/部门领导/超级管理员)
            SysUser userObj = (SysUser) ShiroUtils.getSessionAttribute(SessionKey.SESSION_LOGIN_USER.toString());
            
            if (status == JiucuoTaskStatus.VERIFIED.getId()) {//审核通过
                adJiucuoTaskService.pass(task, userObj.getId());
            } else if (status == JiucuoTaskStatus.VERIFY_FAILURE.getId()) {//审核不通过
                adJiucuoTaskService.reject(task, reason, userObj.getId());
            }
			
            task = adJiucuoTaskService.getById(id);
            //==========web端任务纠错之后根据userId进行app消息推送==============
            Map<String, Object> param = new HashMap<>();
            Map<String, String> extras = new HashMap<>();
            List<String> alias = new ArrayList<>(); //别名用户List
            alias.add(String.valueOf(task.getUserId()));  //纠错任务提交者
            extras.put("type", "task_jiucuo_push");
            param.put("msg", "您的任务一条新的纠错审核通知！");
            param.put("title", "玖凤平台");
            param.put("alias", alias);  //根据别名选择推送用户（这里userId用作推送时的用户别名）
            param.put("extras", extras);
            String pushResult = JPushUtils.pushAllByAlias(param);
            System.out.println("pushResult:: " + pushResult);
        } catch (Exception e) {
        	//异常情况, 移除Redis缓存处理并发
        	redisTemplate.delete(beginRedisStr);
        	
            result.setCode(ResultCode.RESULT_FAILURE.getCode());
            result.setResultDes("审核失败！");
            model.addAttribute(SysConst.RESULT_KEY, result);
            return model;
        }

        //放入Redis缓存处理并发
    	redisTemplate.opsForValue().set(finishRedisStr, "true");
        
        model.addAttribute(SysConst.RESULT_KEY, result);
        return model;
    }
	
    /**
     * 撤消纠错
     */
    @RequiresRoles("jiucuoadmin")
    @RequestMapping(value = "/cancel")
    @ResponseBody
    public Model cancel(Model model, HttpServletRequest request,
                        @RequestParam(value = "id", required = false) Integer id,
                        @RequestParam(value = "userId", required = false) Integer userId,
                        @RequestParam(value = "mediaId", required = false) Integer mediaId,
                        @RequestParam(value = "reason", required = false) String reason) {
        ResultVo<String> result = new ResultVo<String>();
        result.setCode(ResultCode.RESULT_SUCCESS.getCode());
        result.setResultDes("撤消成功");
        model = new ExtendedModelMap();
        AdJiucuoTask jiucuoTask = new AdJiucuoTask();
        try {
        	//获取当前登录的后台用户信息
        	SysUser sysUser = (SysUser) ShiroUtils.getSessionAttribute(SessionKey.SESSION_LOGIN_USER.toString());
        	Map<String, Object> searchMap = new HashMap<>();
			searchMap.put("userId", sysUser.getId());
			Integer groupId = sysUserRoleService.selectGroupIdByUserId(searchMap);
        	//获取该组所有员工
        	List<SysUser> sysUsers = sysGroupService.selectUserName(groupId);
        	
        	if(sysUsers.size() > 1) {//待审核
        		adJiucuoTaskService.offJiucuoTaskByAssessorId(id); 
        	}else if((sysUsers.size() <= 1)){
        		result.setCode(ResultCode.RESULT_FAILURE.getCode());
                result.setResultDes("只剩一人不能撤销！");
                model.addAttribute(SysConst.RESULT_KEY, result);
                return model;
           	}
        } catch (Exception e) {
            result.setCode(ResultCode.RESULT_FAILURE.getCode());
            result.setResultDes("撤消失败！");
            model.addAttribute(SysConst.RESULT_KEY, result);
            return model;
        } 
        model.addAttribute(SysConst.RESULT_KEY, result);
        return model;
    }
	
    /**
     * 关闭纠错问题任务
     */
    @RequiresRoles(value = {"jiucuoadmin", "depjiucuoadmin", "superadmin"}, logical = Logical.OR)
    @RequestMapping(value = "/close")
    @ResponseBody
    public Model close(Model model, HttpServletRequest request,
                       @RequestParam(value = "id", required = false) Integer id) {
        ResultVo<String> result = new ResultVo<String>();
        result.setCode(ResultCode.RESULT_SUCCESS.getCode());
        result.setResultDes("关闭成功");
        model = new ExtendedModelMap();

        AdJiucuoTask task = new AdJiucuoTask();
        task.setProblemStatus(TaskProblemStatus.CLOSED.getId());
        task.setId(id);
        try {
            adJiucuoTaskService.update(task);
        } catch (Exception e) {
            result.setCode(ResultCode.RESULT_FAILURE.getCode());
            result.setResultDes("关闭失败！");
            model.addAttribute(SysConst.RESULT_KEY, result);
            return model;
        }

        model.addAttribute(SysConst.RESULT_KEY, result);
        return model;
    }

    /**
     * 创建复查子任务
     */
    @RequiresRoles(value = {"jiucuoadmin", "depjiucuoadmin", "superadmin"}, logical = Logical.OR)
    @RequestMapping(value = "/createTask")
    @ResponseBody
    public Model newSub(Model model, HttpServletRequest request,
                        @RequestParam(value = "id", required = false) Integer id) {
        ResultVo<String> result = new ResultVo<String>();
        result.setCode(ResultCode.RESULT_SUCCESS.getCode());
        result.setResultDes("创建成功");
        model = new ExtendedModelMap();

        AdMonitorTask task = new AdMonitorTask();
        task.setProblemStatus(TaskProblemStatus.CLOSED.getId());
        task.setId(id);
        try {
            adJiucuoTaskService.createSubTask(id);
        } catch (Exception e) {
            e.printStackTrace();
            result.setCode(ResultCode.RESULT_FAILURE.getCode());
            result.setResultDes("创建失败！");
            model.addAttribute(SysConst.RESULT_KEY, result);
            return model;
        }

        model.addAttribute(SysConst.RESULT_KEY, result);
        return model;
    }

}