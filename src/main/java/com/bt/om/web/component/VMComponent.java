package com.bt.om.web.component;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bt.om.cache.AdSeatTypeCache;
import com.bt.om.cache.CityCache;
import com.bt.om.entity.AdActivity;
import com.bt.om.entity.AdApp;
import com.bt.om.entity.AdCustomerType;
import com.bt.om.entity.AdMedia;
import com.bt.om.entity.AdMediaType;
import com.bt.om.entity.AdSeatType;
import com.bt.om.entity.SysResources;
import com.bt.om.entity.SysUser;
import com.bt.om.entity.SysUserExecute;
import com.bt.om.entity.vo.SysMenuVo;
import com.bt.om.entity.vo.SysUserVo;
import com.bt.om.enums.ActivityStatus;
import com.bt.om.enums.AppUserTypeEnum;
import com.bt.om.enums.JiucuoTaskStatus;
import com.bt.om.enums.MediaType;
import com.bt.om.enums.MessageIsFinish;
import com.bt.om.enums.MessageType;
import com.bt.om.enums.MonitorTaskStatus;
import com.bt.om.enums.MonitorTaskType;
import com.bt.om.enums.SessionKey;
import com.bt.om.enums.TaskProblemStatus;
import com.bt.om.enums.UserTypeEnum;
import com.bt.om.enums.VerifyType;
//import com.bt.om.mapper.SysDictMapper;
import com.bt.om.security.ShiroUtils;
import com.bt.om.service.IAdActivityService;
import com.bt.om.service.IAdCustomerTypeService;
import com.bt.om.service.IAdMediaTypeService;
import com.bt.om.service.IAppService;
import com.bt.om.service.IResourceService;
import com.bt.om.service.ISysGroupService;
import com.bt.om.service.ISysUserExecuteService;
import com.bt.om.service.ISysUserService;
import com.bt.om.util.CityUtil;
import com.bt.om.util.ConfigUtil;
import com.bt.om.util.StringUtil;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;

/**
 * @author hl-tanyong
 * @version $Id: VMComponent.java, v 0.1 2015年9月29日 上午11:33:12 hl-tanyong Exp $
 */
@Component
public class VMComponent {

    protected final Logger logger = LoggerFactory.getLogger(VMComponent.class);
    
    @Autowired
    ISysUserService sysUserService;
    
    @Autowired
    IAdActivityService adActivityService;

    @Autowired
    IResourceService resourceService;

    @Autowired
    CityCache cityCache;

    @Autowired
    AdSeatTypeCache adSeatTypeCache;
    
    @Autowired
    IAdMediaTypeService adMediaTypeService;
	
	@Autowired
    IAdCustomerTypeService adCustomerTypeService;

	@Autowired
	IAppService adAppService;
	
	@Autowired
	ISysUserExecuteService sysUserExecuteService;
	
	@Autowired
	private ISysGroupService sysGroupService;
    public String getCityName(Long code) {
        return code == null ? "" : cityCache.getCityName(code);
    }

    public String getCityName(Integer code) {
        return code == null ? "" : cityCache.getCityName((long) code);
    }

    public String getCityName(String code) {
        try {
            return code == null ? "" : cityCache.getCityName(Long.valueOf(code));
        } catch (Exception e) {
        }
        return "";
    }

    public String getCityNameFull(Long code, String split) {
        if(code==null||split==null){
            return "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append(cityCache.getCityName(CityUtil.getProvinceCode(code)));
        Long sub = CityUtil.getCityCode(code);
        if (sub != null) {
            String subName = cityCache.getCityName(sub);
            if (StringUtil.isNotEmpty(subName)) {
                sb.append(split).append(subName);
            }
//            sub = CityUtil.getRegionCode(code);
//            if (sub != null) {
//                subName = cityCache.getCityName(sub);
//                if (StringUtil.isNotEmpty(subName)) {
//                    sb.append(split).append(subName);
//                }
//                if (CityUtil.isStreet(code)) {
//                    sb.append(split).append(cityCache.getCityName(code));
//                }
//            }
        }
        return sb.toString();
    }

    public List<AdSeatType> getAllAdSeatTypes() {
        return adSeatTypeCache.getTypes();
    }

    public String getEvnVariable() {
        return ConfigUtil.getString("is_test_evn", "off");
    }

    @SuppressWarnings("unchecked")
    public List<SysMenuVo> getMenuList() {
        Object menuList = ShiroUtils.getSessionAttribute(SessionKey.SESSION_USER_MENU.toString());
        if (menuList == null) {
            return new ArrayList<SysMenuVo>();
        } else {
            return (List<SysMenuVo>) menuList;
        }
    }

    public String getLoginUserName() {
        Object userObj = ShiroUtils.getSessionAttribute(SessionKey.SESSION_LOGIN_USER.toString());
        if (userObj != null) {
            SysUserVo userInfo = (SysUserVo) userObj;
            return userInfo.getRealname();
        }
        return "";
    }

    /**
     * 得到一个删除其中几个参数的URL
     *
     * @param url
     * @param keys
     * @return
     */
    public String getUrlByRemoveKey(String url, String[] keys) {
        String qs = StringUtil.findStr(url, "?", null);
        HashMap<String, String> map = getParams(qs);
        if (map.size() > 0) {
            for (String key : keys) {
                map.remove(key);
            }
            StringBuilder sb = new StringBuilder();
            sb.append(StringUtil.findStr(url, null, "?"));
            if (map.size() > 0) {
                sb.append("?");
                int i = 0;
                for (Entry<String, String> kv : map.entrySet()) {
                    if (i > 0) {
                        sb.append("&");
                    }
                    sb.append(kv.getKey());
                    sb.append("=");
                    sb.append(StringUtil.urlencoder(kv.getValue()));
                    i++;
                }
            }
            return sb.toString();
        }
        return url;
    }

    /**
     * 获取广告活动
     */
    public List<AdActivity> getAllActivity() {
        return adActivityService.getAll();
    }

    /**
     * 获取拥有的广告活动
     */
    public List<AdActivity> getOwnActivity() {
        Object userObj = ShiroUtils.getSessionAttribute(SessionKey.SESSION_LOGIN_USER.toString());
        return adActivityService.getByUerId(((SysUser) userObj).getId());
    }

    /**
     * 获取广告活动状态文字（带颜色）
     */
    public String getActivityStatusTextWithColor(int id) {
        String color = "";
        if (id == ActivityStatus.UNCONFIRM.getId()) {
            color = "style='color:red;'";
        } else if (id == ActivityStatus.COMPLETE.getId()) {
            color = "style='color:green;'";
        }
        return "<span " + color + " >" + ActivityStatus.getText(id) + "</span>";
    }

    /**
     * 获取app端用户类型名称
     **/
    public String getUserExecuteTypeText(int id) {	
        return AppUserTypeEnum.getText(id);
    }
    
    /**
     * 获取用户站内信名称
     **/
    public String getUserMessageText(int id) {	
        return MessageIsFinish.getText(id);
    }
    
    /**
     * 获取用户站内信类型的名称
     **/
    public String getUserMessageTypeText(int id) {	
        return MessageType.getText(id);
    }
    
    /**
     * 获取任务状态的名称
     **/
    public String getVerifyTypeText(int id) {	
        return VerifyType.getText(id);
    }
    
    /**
     * 获取用户站内信名称列表
     **/
    public MessageIsFinish[] getAllUserMessageFinishText() {	
        return MessageIsFinish.values();
    }
    
    /**
     * 获取用户站内信类型的名称列表
     **/
    public MessageType[] getAllUserMessageTypeText() {	
        return MessageType.values();
    }

    /**
     * 获取app端用户类型列表
     **/
    public AppUserTypeEnum[] getUserExecuteTypeList() {
        return AppUserTypeEnum.values();
    }
    
    /**
     * 获取广告活动状态文字
     */
    public String getActivityStatusText(int id) {
        return ActivityStatus.getText(id);
    }

    /**
     * 获取广告活动状态列表
     */
    public ActivityStatus[] getActivityStatusList() {
        return ActivityStatus.values();
    }

    /**
     * 获取广告活动监测任务状态文字
     */
    public String getMonitorTaskStatusText(int id) {
        return MonitorTaskStatus.getText(id);
    }

    /**
     * 获取广告活动监测任务状态列表
     */
    public MonitorTaskStatus[] getMonitorTaskStatusList() {
        return MonitorTaskStatus.values();
    }

    /**
     * 获取广告活动监测任务类型列表
     */
    public MonitorTaskType[] getMonitorTaskTypeList() {
        return MonitorTaskType.values();
    }
    
    /**
     * 获取媒体类型列表
     */
    public MediaType[] getMediaTypeList() {
        return MediaType.values();
    }
    
    /**
     * 获取广告活动监测任务类型文字
     */
    public String getMonitorTaskTypeText(int id) {
        return MonitorTaskType.getText(id);
    }

    /**
     * 获取广告活动纠错任务状态文字
     */
    public String getJiucuoTaskStatusText(int id) {
        return JiucuoTaskStatus.getText(id);
    }

    /**
     * 获取广告活动纠错任务状态列表
     */
    public JiucuoTaskStatus[] getJiucuoTaskStatusList() {
        return JiucuoTaskStatus.values();
    }

    /**
     * 获取问题状态状态文字
     */
    public String getProblemStatusText(int id) {
        return TaskProblemStatus.getText(id);
    }

    /**
     * 获取问题状态列表
     */
    public TaskProblemStatus[] getProblemStatusList() {
        return TaskProblemStatus.values();
    }

    /**
     * 获取媒体用户用到的问题状态列表，不包括未监测和没问题
     */
    public Collection<TaskProblemStatus> getMediaProblemStatusList() {
        Collection<TaskProblemStatus> status = Collections2.filter(Lists.newArrayList(TaskProblemStatus.values()), new Predicate<TaskProblemStatus>() {
            @Override
            public boolean apply(TaskProblemStatus input) {
                return input.getId() >= TaskProblemStatus.PROBLEM.getId();
            }
        });
        return status;
    }

    /**
     * 根据用户类型查询该类型下的所有账号
     * 用户类型  1：运营平台账户 2：客户账户 3：媒体账户 4：超级管理员 5：部门领导
     */
    public List<SysUserVo> getAllDepartmentLeader() {
    	return sysUserService.getAllByUserType(5);
    }
    
    /**
     * 获取所有部门名称  type=1
     * */
    public List<SysResources> getAllDepartment() {
    	return sysGroupService.getAllDepartment(1);
    }
    
    /**
     * 获取媒体
     */
    public List<AdMedia> getAllMedia() {
        return resourceService.getAll();
    }
	
	/**
     * 获取全部可用媒体
     */
    public List<AdMedia> getAllAvailableMedia() {
        return resourceService.getAvailableAll();
    }
    
    /**
     * 获取全部媒体大类
     */
    public List<AdMediaType> getAllParentMediaType() {
    	return adMediaTypeService.getParentMedia(1);
    }
    
    /**
     * 获取全部可用媒体大类
     */
    public List<AdMediaType> getAllParentMediaTypeAvailable() {
    	return adMediaTypeService.getParentMediaAvailable(1);
    }
	
	/**
     * 获取全部客户类型
     */
    public List<AdCustomerType> getAllCustomerType() {
    	return adCustomerTypeService.getAll();
    }
    
    /**
     * 获取全部App类型
     * */
    public List<AdApp> getAllAppType() {
    	return adAppService.getAllAppType();
    }
    
    /**
     * 获取带指派任务是否过时
     * */
    public Integer getUnassignTask(Date endTime) {
    	Date date = new Date();
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	String t1 = sdf.format(date);
    	String t2 = sdf.format(endTime);
    	
    	Integer i = t1.compareTo(t2);
    	return i;
    }
    /**
     * 获取全部可用的客户
     */
    public List<SysUser> getAllCustomerAvailable() {
    	return sysUserService.getAvailableByUserType(UserTypeEnum.CUSTOMER.getId()); //2：客户账户
    }
    
    /**
     * 获取全部的客户
     */
    public List<SysUserVo> getAllCustomerByUserType() {
    	return sysUserService.getAllByUserType(UserTypeEnum.CUSTOMER.getId()); //2：客户账户
    }
    
    /**
     * 获取该媒体下的所有APP执行人员
     */
    public List<SysUserExecute> showUserExecuteByMedia(String mediaId) {
    	return sysUserExecuteService.selectMediaNameByUserId(Integer.parseInt(mediaId));
    }
    
    /**
     * 获取全部的第三方监测公司
     */
    public List<SysUserVo> getAllThirdCompanyByUserType() {
    	return sysUserService.getAllByUserType(UserTypeEnum.THIRD_COMPANY.getId()); //7：第三方监测公司
    }
    
    /***************************** 下面是工具类 ****************************************/
    
    private HashMap<String, String> getParams(String qs) {
        HashMap<String, String> params = new HashMap<String, String>();
        if (qs != null) {
            String[] keyValue = qs.split("&");
            for (String kv : keyValue) {
                int offset = kv.indexOf("=");
                if ((offset > 0) && (offset < (kv.length() - 1))) {
                    params.put(kv.substring(0, offset), kv.substring(offset + 1));
                }
            }
        }
        return params;
    }

    // public List<OttvUserinfoAdvertiserVo> getAdvUserList() {
    //
    // OttvUserVo user = (OttvUserVo)
    // ShiroUtils.getSessionAttribute(SessionKey.SESSION_LOGIN_USER.toString());
    // List<OttvUserinfoAdvertiserVo> ottvUserinfoAdvertiserList = null;
    // Map<String, Object> searchMap = new HashMap<String, Object>();
    //
    // if (user.isSale()) {
    // searchMap.put("saleId", user.getId());
    // ottvUserinfoAdvertiserList = ottvUserInfoService.getAdvUserList(searchMap);
    // } else {
    // ottvUserinfoAdvertiserList = ottvUserInfoService.getAdvUserList(searchMap);
    // }
    //
    // return ottvUserinfoAdvertiserList;
    // }
    //
    // public List<OttvOrder> getOttvOrderNameList() {
    // OttvUserVo user = (OttvUserVo)
    // ShiroUtils.getSessionAttribute(SessionKey.SESSION_LOGIN_USER.toString());
    // List<OttvOrder> ottvOrderList = null;
    // if (user.isSale()) {
    // ottvOrderList = ottvOrderService.getOrderNameList(user.getId());
    // } else {
    // ottvOrderList = ottvOrderService.getOrderNameList(null);
    // }
    // return ottvOrderList;
    // }
    //
    // public List<String> getAreaNeedAssigned(Integer orderId) {
    // List<String> areaList = new ArrayList<String>();
    //
    // OttvOrder ottvOrder = ottvOrderService.selectByPrimaryKey(orderId);
    // String area = ottvOrder.getArea();
    // SearchDataVo vo = SearchUtil.getVo();
    //
    // if (orderId != null) {
    // vo.putSearchParam("id", orderId + "", orderId);
    // }
    //
    // if (area != null && area != "") {
    // Map<String, Object> map = GsonUtil.GsonToMaps(area);
    // for (Entry<String, Object> en : map.entrySet()) {
    // try {
    // Map<String, Object> map2 =
    // GsonUtil.GsonToMaps(GsonUtil.GsonString(en.getValue()));
    // Map<String, String> map3 =
    // GsonUtil.GsonToMaps(GsonUtil.GsonString(map2.get("child")));
    // String proName = (String) map2.get("name");
    // for (Entry<String, String> en2 : map3.entrySet()) {
    // if ("北京".equals(proName.trim()) || "上海".equals(proName.trim()) ||
    // "天津".equals(proName.trim()) || "重庆".equals(proName.trim())
    // || "澳门".equals(proName.trim()) || "澳门".equals(proName.trim())) {
    //
    // areaList.add("" +
    // (GsonUtil.GsonToMaps(GsonUtil.GsonString(en2.getValue()))).get("name"));
    // } else {
    // areaList.add(proName + "-"
    // + (GsonUtil.GsonToMaps(GsonUtil.GsonString(en2.getValue()))).get("name"));
    // }
    // }
    // } catch (Exception e) {
    //
    // }
    // }
    // }
    //
    // List<OttvOrderPlanVo> ottvOrderPlanVoList =
    // ottvOrderPlanService.getOrderPlanListByOrderId(vo);
    // List<Integer> planIds = new ArrayList<Integer>();
    // for (OttvOrderPlan ottvOrderPlan : ottvOrderPlanVoList) {
    // planIds.add(ottvOrderPlan.getId());
    // }
    //
    // Set<String> set = new HashSet<String>();
    // if(CollectionUtils.isNotEmpty(planIds)){
    // List<OttvOrderPlanResource> ottvOrderPlanResourceList =
    // ottvOrderPlanResourceService.getOrderPlanListByOrderPlanId(planIds);
    // for (OttvOrderPlanResource ottvOrderPlanResource : ottvOrderPlanResourceList)
    // {
    // set.add(ottvOrderPlanResource.getAreaName());
    // }
    //
    // Iterator<String> iterator = areaList.iterator();
    // while(iterator.hasNext()) {
    // String areaName = iterator.next().trim();
    // for (String areaStr : set) {
    // if (areaName.contains(areaStr.trim()) || areaName.equals(areaStr.trim())) {
    // iterator.remove();
    // }
    // }
    // }
    // }
    //
    // return areaList;
    // }
}