package com.bt.om.web.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.adtime.common.lang.StringUtil;
import com.bt.om.common.SysConst;
import com.bt.om.common.web.PageConst;
import com.bt.om.entity.AdActivity;
import com.bt.om.entity.SysUser;
import com.bt.om.entity.vo.AdActivityAdseatVo;
import com.bt.om.entity.vo.AdActivityVo;
import com.bt.om.enums.ResultCode;
import com.bt.om.enums.SessionKey;
import com.bt.om.security.ShiroUtils;
import com.bt.om.service.IAdActivityService;
import com.bt.om.service.IOperateLogService;
import com.bt.om.service.ISysGroupService;
import com.bt.om.service.ISysResourcesService;
import com.bt.om.service.ISysUserRoleService;
import com.bt.om.service.ISysUserService;
import com.bt.om.util.GsonUtil;
import com.bt.om.util.QRcodeUtil;
import com.bt.om.vo.api.AdActivitySeatInfoInQRVO;
import com.bt.om.vo.web.ResultVo;
import com.bt.om.vo.web.SearchDataVo;
import com.bt.om.web.BasicController;
import com.bt.om.web.util.JPushUtils;
import com.bt.om.web.util.SearchUtil;

/**
 * Created by caiting on 2018/1/20.
 */
@Controller
@RequestMapping(value = "/activity")
public class ActivityController extends BasicController {

    @Autowired
    private IAdActivityService adActivityService;
    @Autowired
	private ISysGroupService sysGroupService;
    @Autowired
    private ISysUserService sysUserService;
	@Autowired
	private ISysResourcesService sysResourcesService;
	@Autowired
	private ISysUserRoleService sysUserRoleService;
   
    @Autowired
	private IOperateLogService operateLogService;
    
    //活动审核人员查看活动列表
    @RequiresRoles("activityadmin")
    @RequestMapping(value = "/list")
    public String customerList(Model model, HttpServletRequest request,
                               @RequestParam(value = "activityId", required = false) Integer activityId,
                               @RequestParam(value = "status", required = false) Integer status,
                               @RequestParam(value = "startDate", required = false) String startDate,
                               @RequestParam(value = "endDate", required = false) String endDate) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        SearchDataVo vo = SearchUtil.getVo();
        Integer shenheCount = 0;
        
        //获取登录的审核员工activityadmin
        SysUser userObj = (SysUser) ShiroUtils.getSessionAttribute(SessionKey.SESSION_LOGIN_USER.toString());
        
        if (activityId != null) {
            vo.putSearchParam("activityId", activityId.toString(), activityId);
        }
        
        if (status != null) {
            vo.putSearchParam("status", status.toString(), status);
            model.addAttribute("status", status);
        } else {
        	status = 1; //如果没有传参status, 默认取1：未确认
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
        //只能查询自己参与的活动审核
        if(userObj != null) {
        	Integer assessorId = userObj.getId();
        	vo.putSearchParam("assessorId", assessorId.toString(), assessorId);
        }
        
        //所有未确认的活动
        Map<String, Object> searchMap1 = new HashMap<>();
        List<Integer> customerIds = sysUserService.getCustomerIdsByAdminId(userObj.getId()); //根据员工id查询所属组对应的所有广告商id集合
        searchMap1.put("customerIds", customerIds);
    	List<AdActivity> allActivityUncertain = adActivityService.getAllByStatusUncertain(searchMap1);
    	
        if(status == 2 || status == 3) {
        	//查询已确认 或 已结束 的活动
        	adActivityService.getPageData(vo);
        } else {
        	//查询未确认的活动
        	//[1] 先查询审核id的所有未确认活动
            Map<String, Object> searchMap = new HashMap<>();
            searchMap.put("status", 1);
            searchMap.put("assessorId", userObj.getId());
//            searchMap.put("activityId", activityId);
//            searchMap.put("startDate", startDate);
//            searchMap.put("endDate", endDate);
            List<AdActivity> activities = adActivityService.selectAllByAssessorId(searchMap);//通过所有审核员id获取的所有活动
            
            if(activities != null && activities.size() > 0) {
            	//条数大于0, 返回给页面
            	Iterator<AdActivity> iterator = activities.iterator();
            	while(iterator.hasNext()) {
            		boolean remove = false; //不用抹去
            		AdActivity adActivity = iterator.next();
            		//通过页面上的activityId做筛选
            		if(activityId != null) {
            			if(adActivity.getId() != activityId) {
            				remove = true;
            			}
            		}
            		//通过页面上的startDate做筛选
            		if(StringUtil.isNotBlank(startDate)) {
            			if(adActivity.getStartTime().getTime() < sdf.parse(startDate).getTime()) {
            				remove = true;
            			}
            		}
            		//通过页面上的endDate做筛选
            		if(StringUtil.isNotBlank(endDate)) {
            			if(adActivity.getEndTime().getTime() > sdf.parse(endDate).getTime()) {
            				remove = true;
            			}
            		}
            		if(remove == true) {
            			iterator.remove();
            		}
            	}      	
            	vo.setCount(activities.size());
            	vo.setSize(20);
            	vo.setStart(0);
            	vo.setList(activities);
            	shenheCount = allActivityUncertain.size() - activities.size();
            	if(shenheCount < 0) {
            		shenheCount = 0;
            	}
            	model.addAttribute("shenheCount", shenheCount);
            } else {
            	//条数等于0, 新查询1条或者0条没人认领的未确认活动(需要匹配 员工 - 组 - 广告商 之间的关系)
            	if(customerIds != null && customerIds.size() > 0) {
            		searchMap.clear();
            		searchMap.put("status", 1);
            		searchMap.put("customerIds", customerIds);
            		searchMap.put("assessorId", userObj.getId());
            		List<AdActivity> atimeActivity = adActivityService.getAtimeActivity(searchMap);//获取一次未确认活动
            		vo.setCount(atimeActivity.size());
                	vo.setSize(20);
                	vo.setStart(0);
                	vo.setList(atimeActivity);
                	shenheCount = allActivityUncertain.size() - atimeActivity.size();
                	if(shenheCount < 0) {
                		shenheCount = 0;
                	}
                	model.addAttribute("shenheCount", shenheCount);
            	}
            }
        }
        
        SearchUtil.putToModel(model, vo);

        return PageConst.ACTIVITY_LIST;
    }

    //前往编辑活动
    @RequiresRoles(value = {"activityadmin", "depactivityadmin", "superadmin"}, logical = Logical.OR)
    @RequestMapping(value = "/edit")
    public String customerEdit(Model model, HttpServletRequest request,
                               @RequestParam(value = "id", required = false) Integer id) {
        AdActivityVo activity = adActivityService.getVoById(id);

        if (activity != null) {
            model.addAttribute("activity", activity);
        }

        return PageConst.ACTIVITY_EDIT;
    }

    //确认活动
    @RequiresRoles("activityadmin")
    @RequestMapping(value = "/confirm")
    @ResponseBody
    public Model confirm(Model model, HttpServletRequest request,
                         @RequestParam(value = "id", required = false) Integer id) {
        ResultVo<String> result = new ResultVo<String>();
        result.setCode(ResultCode.RESULT_SUCCESS.getCode());
        result.setResultDes("确认成功");
        model = new ExtendedModelMap();
        Date now = new Date();
        
        try {
        	//确认活动
            adActivityService.confirm(id);
            
            AdActivity adActivity = adActivityService.getById(id);
            
            //==========web端活动审核成功之后根据活动创建者id进行app消息推送==============
            Map<String, Object> param = new HashMap<>();
            Map<String, String> extras = new HashMap<>();
            List<String> alias = new ArrayList<>(); //别名用户List
            alias.add(String.valueOf(adActivity.getUserId()));  //活动创建者
            extras.put("type", "activity_confirm_push");
            param.put("msg", "您创建的活动有一条新的通知！");
            param.put("title", "玖凤平台");
            param.put("alias", alias);  //根据别名选择推送用户（这里userId用作推送时的用户别名）
            param.put("extras", extras);
            String pushResult = JPushUtils.pushAllByAlias(param);
            System.out.println("pushResult:: " + pushResult);
        } catch (Exception e) {
            result.setCode(ResultCode.RESULT_FAILURE.getCode());
            result.setResultDes("确认失败！");
            model.addAttribute(SysConst.RESULT_KEY, result);
            return model;
        }
        
        model.addAttribute(SysConst.RESULT_KEY, result);
        return model;
    }
    
    //撤销活动
    @RequiresRoles("activityadmin")
    @RequestMapping(value = "/cancel")
    @ResponseBody
    public Model cancel(Model model, HttpServletRequest request,
                        @RequestParam(value = "id", required = false) Integer id, 
    					@RequestParam(value = "userId", required = false) Integer userId){
        ResultVo<String> result = new ResultVo<String>();
        result.setCode(ResultCode.RESULT_SUCCESS.getCode());
        result.setResultDes("撤销成功");
        model = new ExtendedModelMap();
        
        try {
        	//获取当前登录的后台用户信息
        	SysUser sysUser = (SysUser) ShiroUtils.getSessionAttribute(SessionKey.SESSION_LOGIN_USER.toString());
        	Map<String, Object> searchMap = new HashMap<>();
			searchMap.put("userId", sysUser.getId());
			Integer groupId = sysUserRoleService.selectGroupIdByUserId(searchMap);
        	/*//获取当前登录的后台用户的所属组信息
			SysResources group = sysGroupService.getById(groupId);*/
        	//获取该组所有员工
        	List<SysUser> sysUsers = sysGroupService.selectUserName(groupId);
        	
        	if(sysUsers.size() > 1) { 
        		adActivityService.offActivityByAssessorId(id);
        	} else if(sysUsers.size() <= 1){
          		result.setCode(ResultCode.RESULT_FAILURE.getCode());
                result.setResultDes("只剩一人不能撤销！");
                model.addAttribute(SysConst.RESULT_KEY, result);
                return model;
           	}
        }catch (Exception e) {
            result.setCode(ResultCode.RESULT_FAILURE.getCode());
            result.setResultDes("撤销失败！");
            model.addAttribute(SysConst.RESULT_KEY, result);
            return model;
        }
        model.addAttribute(SysConst.RESULT_KEY, result);
        return model;
    }

    //删除活动
    @RequiresRoles(value= {"admin", "customer", "activityadmin"}, logical = Logical.OR)
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Model delete(Model model, HttpServletRequest request,
                        @RequestParam(value = "id", required = false) Integer id) {
        ResultVo<String> result = new ResultVo<String>();
        result.setCode(ResultCode.RESULT_SUCCESS.getCode());
        result.setResultDes("删除成功");
        model = new ExtendedModelMap();
        Date now = new Date();
        
        try {
        	//删除活动
            adActivityService.delete(id);
        } catch (Exception e) {
            result.setCode(ResultCode.RESULT_FAILURE.getCode());
            result.setResultDes("删除失败！");
            model.addAttribute(SysConst.RESULT_KEY, result);
            return model;
        }
        model.addAttribute(SysConst.RESULT_KEY, result);
        return model;
    }

    /**
     * 根据活动广告位关联id，获取二维码
     **/
    @RequestMapping(value = {"/getQrcode"}, method = RequestMethod.GET)
    public void getCode(HttpServletRequest request, HttpServletResponse response,
                        @RequestParam(value = "id", required = false) Integer adActivityAdseatId) throws Exception {

        AdActivitySeatInfoInQRVO vo = new AdActivitySeatInfoInQRVO((AdActivityAdseatVo) adActivityService.getActivitySeatById(adActivityAdseatId));

        // 将内存中的图片发送到客户端
        response.setContentType("image/jpg");
        QRcodeUtil.encode(GsonUtil.GsonString(vo), response.getOutputStream());
    }
}
