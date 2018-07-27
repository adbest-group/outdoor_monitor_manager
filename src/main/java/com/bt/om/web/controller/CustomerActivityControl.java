package com.bt.om.web.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
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

import com.bt.om.cache.CityCache;
import com.bt.om.common.DateUtil;
import com.bt.om.common.SysConst;
import com.bt.om.common.web.PageConst;
import com.bt.om.entity.AdActivity;
import com.bt.om.entity.AdActivityAdseat;
import com.bt.om.entity.AdJiucuoTaskFeedback;
import com.bt.om.entity.SysUser;
import com.bt.om.entity.vo.AdActivityVo;
import com.bt.om.entity.vo.AdJiucuoTaskVo;
import com.bt.om.entity.vo.AdMonitorTaskVo;
import com.bt.om.entity.vo.AdSeatCount;
import com.bt.om.entity.vo.AdSeatInfoVo;
import com.bt.om.enums.ActivityStatus;
import com.bt.om.enums.AllowMultiEnum;
import com.bt.om.enums.JiucuoTaskStatus;
import com.bt.om.enums.ResultCode;
import com.bt.om.enums.SessionKey;
import com.bt.om.enums.UserTypeEnum;
import com.bt.om.mapper.SysUserResMapper;
import com.bt.om.security.ShiroUtils;
import com.bt.om.service.IAdActivityService;
import com.bt.om.service.IAdJiucuoTaskService;
import com.bt.om.service.IAdSeatService;
import com.bt.om.service.IAdUserMessageService;
import com.bt.om.service.ISysResourcesService;
import com.bt.om.service.ISysUserService;
import com.bt.om.util.ConfigUtil;
import com.bt.om.util.StringUtil;
import com.bt.om.vo.web.ResultVo;
import com.bt.om.vo.web.SearchDataVo;
import com.bt.om.web.BasicController;
import com.bt.om.web.util.SearchUtil;

/**
 * Created by caiting on 2018/1/17.
 */
@Controller
@RequestMapping(value = "/customer")
public class CustomerActivityControl extends BasicController {

    @Autowired
    private IAdActivityService adActivityService;
    @Autowired
    private IAdJiucuoTaskService adJiucuoTaskService;
    @Autowired
    private IAdSeatService adSeatService;
    @Autowired
	private IAdUserMessageService adUserMessageService;
    @Autowired 
    private ISysResourcesService sysResourcesService;
    @Autowired 
    private ISysUserService sysUserService;
	@Autowired
	private SysUserResMapper sysUserResMapper;
	@Autowired
	private CityCache cityCache;
	
	/**
     * 查询活动列表
     */
    @RequiresRoles("customer")
    @RequestMapping(value = "/activity/list")
    public String customerList(Model model, HttpServletRequest request,
                               @RequestParam(value = "activityId", required = false) Integer activityId,
                               @RequestParam(value = "status", required = false) Integer status,
                               @RequestParam(value = "startDate", required = false) String startDate,
                               @RequestParam(value = "endDate", required = false) String endDate,
                               @RequestParam(value = "name", required = false) String name,
                               @RequestParam(value = "mediaTypeId", required = false) Integer mediaTypeId,
                               @RequestParam(value = "mediaTypeParentId", required = false) Integer mediaTypeParentId,
                               @RequestParam(value = "province", required = false) String province,
                               @RequestParam(value = "city", required = false) String city) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        SysUser user = (SysUser) ShiroUtils.getSessionAttribute(SessionKey.SESSION_LOGIN_USER.toString());

        SearchDataVo vo = SearchUtil.getVo();
        vo.putSearchParam("userId", user.getId().toString(), user.getId().toString());

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

        return PageConst.CUSTOMER_ACTIVITY_LIST;
    }

	/**
     * 编辑活动页面跳转
     */
    @RequiresRoles(value = {"activityadmin", "depactivityadmin", "superadmin", "customer"}, logical = Logical.OR)
    @RequestMapping(value = "/activity/edit")
    public String customerEdit(Model model, HttpServletRequest request,
                               @RequestParam(value = "id", required = false) Integer id) {
        AdActivityVo activity = adActivityService.getVoById(id);
        
        //获取登录用户信息
        SysUser user = (SysUser) ShiroUtils.getSessionAttribute(SessionKey.SESSION_LOGIN_USER.toString());
        
        if(user != null) {
        	model.addAttribute("user", user);
        	model.addAttribute("usertype", user.getUsertype());
        	if (user.getUsertype()==UserTypeEnum.CUSTOMER.getId() &&user.getId().intValue()!=activity.getUserId().intValue()) {
        		return PageConst.NO_AUTHORITY;
			}
        }
        
        if (activity != null) {
            model.addAttribute("activity", activity);
        }
        
        Integer monitorTime = ConfigUtil.getInt("monitor_time"); //允许任务执行天数
        Integer auditTime = ConfigUtil.getInt("audit_time"); //允许任务审核天数
        
        model.addAttribute("monitorTime", monitorTime);
        model.addAttribute("auditTime", auditTime);

        return PageConst.CUSTOMER_ACTIVITY_EDIT;
    }

	/**
     * 编辑活动广告位页面跳转(已废弃)
     */
    @RequiresRoles(value = {"superadmin", "customer","depactivityadmin","activityadmin"}, logical = Logical.OR)
    @RequestMapping(value = "/activity/adseat/edit")
    public String adSeatEdit(Model model, HttpServletRequest request) {
        return PageConst.CUSTOMER_ACTIVITY_ADSEAT_EDIT;
    }
    
	/**
     * 选择活动的广告位二级页面跳转
     */
    @RequestMapping(value = "/activity/adseat/toSelect")
    public String selectAdSeatPage(Model model, HttpServletRequest request) {
        return PageConst.CUSTOMER_ACTIVITY_ADSEAT_SELE;
    }
    
	/**
     * 选择活动的广告位的接口
     */
    @RequiresRoles(value = {"superadmin", "customer","depactivityadmin","activityadmin"}, logical = Logical.OR)
    @RequestMapping(value = "/activity/adseat/select")
    @ResponseBody
    public Model adSeatSelect(Model model, HttpServletRequest request,
            @RequestParam(value = "province", required = false) Long province,
            @RequestParam(value = "city", required = false) Long city,
            @RequestParam(value = "region", required = false) Long region,
            @RequestParam(value = "street", required = false) Long street,
            @RequestParam(value = "mediaTypeParentId", required = false) Integer mediaTypeParentId,
            @RequestParam(value = "mediaTypeId", required = false) Integer mediaTypeId,
            @RequestParam(value = "mediaId", required = false) Integer mediaId,
            @RequestParam(value = "startDate", required = false) String startDate,
            @RequestParam(value = "endDate", required = false) String endDate,
            @RequestParam(value = "seatIds", required = false) String seatIds) {
    	ResultVo<String> result = new ResultVo<String>();
        result.setCode(ResultCode.RESULT_SUCCESS.getCode());
        result.setResult("查询成功");
        model = new ExtendedModelMap();
    	
    	SearchDataVo vo = SearchUtil.getVo();
        if (street != null) {
            vo.putSearchParam("street", street.toString(), street);
        } else if (region != null) {
            vo.putSearchParam("region", region.toString(), region);
        } else if (city != null) {
            vo.putSearchParam("city", city.toString(), city);
        } else if (province != null) {
            vo.putSearchParam("province", province.toString(), province);
        }
        if (mediaTypeParentId != null) {
            vo.putSearchParam("mediaTypeParentId", mediaTypeParentId.toString(), mediaTypeParentId);
        }
        if (mediaTypeId != null) {
            vo.putSearchParam("mediaTypeId", mediaTypeId.toString(), mediaTypeId);
        }
        if (mediaId != null) {
            vo.putSearchParam("mediaId", mediaId.toString(), mediaId);
        }

        //[1] 查询传递的时间段内正在参与活动的广告位id及参与活动数量
        Map<String, Object> searchMap = new HashMap<>();
        searchMap.put("startDate", DateUtil.parseStrDate(startDate, "yyyy-MM-dd"));
        searchMap.put("endDate", DateUtil.parseStrDate(endDate, "yyyy-MM-dd"));
        List<AdSeatCount> adSeatCounts = adActivityService.selectActiveActivityCount(searchMap);
        
        List<Integer> adseatInfoIds = new ArrayList<Integer>(); //待移除的广告位id集合
        for (AdSeatCount adSeatCount : adSeatCounts) {
			if(adSeatCount != null) {
				//判断是否要移除
				if(adSeatCount.getAllowMulti() == AllowMultiEnum.NOT_ALLOW.getId() && adSeatCount.getCount() >= 1) {
					//否：不允许同时有多个活动; 当前广告位正在参与活动的数量 大于等于 1
					adseatInfoIds.add(adSeatCount.getAdseatId());
				}
				if(adSeatCount.getAllowMulti() == AllowMultiEnum.ALLOW.getId() && adSeatCount.getCount() >= adSeatCount.getMultiNum()) {
					//是: 允许同时有多个活动; 当前广告位正在参与活动的数量 大于等于 最大允许数量
					adseatInfoIds.add(adSeatCount.getAdseatId());
				}
			}
		}
        
        if(adseatInfoIds != null && adseatInfoIds.size() > 0) {
        	List<Integer> existSeatIds = new ArrayList<>();
        	if(StringUtil.isNotEmpty(seatIds)) {
        		String[] splitIds = seatIds.split(","); //活动已有的广告位id集合
        		for (String string : splitIds) {
        			existSeatIds.add(Integer.parseInt(string));
				}
        	}
        	if(existSeatIds.size() > 0) {
        		adseatInfoIds.removeAll(existSeatIds);
        	}
        	vo.putSearchParam("adseatInfoIds", null, adseatInfoIds);
        }
        
        //装换结果集, 将province和city直接转换
        List<AdSeatInfoVo> adSeatInfoVos = new ArrayList<>();
        adSeatService.getPageData(vo);
        List<?> list = vo.getList();
        for (Object object : list) {
        	AdSeatInfoVo adSeatInfoVo = (AdSeatInfoVo) object;
        	adSeatInfoVo.setProvinceName(cityCache.getCityName(adSeatInfoVo.getProvince()));
        	adSeatInfoVo.setCityName(cityCache.getCityName(adSeatInfoVo.getCity()));
        	adSeatInfoVos.add(adSeatInfoVo);
		}
        vo.setList(adSeatInfoVos);
        
        SearchUtil.putToModel(model, vo);
    	
        model.addAttribute("count", vo.getCount()); //总条数
        int totalPageCount = (int) ((vo.getCount() + 20 - 1) / 20);
        model.addAttribute("totalPageCount", totalPageCount); //总页数
        model.addAttribute("start", vo.getStart()); //当前页
        
        model.addAttribute(SysConst.RESULT_KEY, result);
        return model;
    }
    
    /**
     * 选择导入临时表中的广告位
     */
    @RequestMapping(value = "/activity/adseat/selectTmp")
    @ResponseBody
    public Model getAdSeatTmps(Model model, HttpServletRequest request,
            @RequestParam(value = "startDate", required = false) String startDate,
            @RequestParam(value = "endDate", required = false) String endDate,
            @RequestParam(value = "activityId", required = false) Integer activityId) {
    	ResultVo<String> result = new ResultVo<String>();
        result.setCode(ResultCode.RESULT_SUCCESS.getCode());
        result.setResult("查询成功");
        model = new ExtendedModelMap();
    	
        if(StringUtil.isEmpty(startDate) || StringUtil.isEmpty(endDate)) {
        	result.setCode(ResultCode.RESULT_FAILURE.getCode());
            result.setResultDes("请先选择活动时间！");
            model.addAttribute(SysConst.RESULT_KEY, result);
            return model;
        }
        
        //[1] 查询临时表中的id集合
        List<Integer> tmpSeatIds = adSeatService.selectSeatIds();
        
        List<AdSeatInfoVo> problemAdSeatInfos = new ArrayList<>();
        List<AdSeatInfoVo> adSeatInfoVos = new ArrayList<>();
        
        if(tmpSeatIds != null && tmpSeatIds.size() > 0) {
        	//[2] 查询上述id集合在传递的时间段内正在参与活动的广告位id及参与活动数量
            Map<String, Object> searchMap = new HashMap<>();
            searchMap.put("startDate", DateUtil.parseStrDate(startDate, "yyyy-MM-dd"));
            searchMap.put("endDate", DateUtil.parseStrDate(endDate, "yyyy-MM-dd"));
            if(tmpSeatIds != null && tmpSeatIds.size() > 0) {
            	searchMap.put("seatIds", tmpSeatIds);
            }
            // 编辑活动页面点击的"最近一次导入", 排除掉这个活动
            if(activityId != null) {
            	searchMap.put("removeActivityId", activityId);
            }
            List<AdSeatCount> adSeatCounts = adActivityService.selectActiveActivityCount(searchMap);
            
            List<Integer> problemInfoIds = new ArrayList<Integer>(); //已有足够的活动占用的广告位id集合
            for (AdSeatCount adSeatCount : adSeatCounts) {
    			if(adSeatCount != null) {
    				//判断是否要移除
    				if(adSeatCount.getAllowMulti() == AllowMultiEnum.NOT_ALLOW.getId() && adSeatCount.getCount() >= 1) {
    					//否：不允许同时有多个活动; 当前广告位正在参与活动的数量 大于等于 1
    					problemInfoIds.add(adSeatCount.getAdseatId());
    				}
    				if(adSeatCount.getAllowMulti() == AllowMultiEnum.ALLOW.getId() && adSeatCount.getCount() >= adSeatCount.getMultiNum()) {
    					//是: 允许同时有多个活动; 当前广告位正在参与活动的数量 大于等于 最大允许数量
    					problemInfoIds.add(adSeatCount.getAdseatId());
    				}
    			}
    		}
            
            //[3] 查询出有问题广告位
            searchMap.clear();
            searchMap.put("adseatInfoIds", problemInfoIds);
            if(problemInfoIds.size() > 0) {
            	 problemAdSeatInfos = adSeatService.selectSeatByIds(searchMap);
            	 for (AdSeatInfoVo adSeatInfoVo : problemAdSeatInfos) {
                 	adSeatInfoVo.setProvinceName(cityCache.getCityName(adSeatInfoVo.getProvince()));
                 	adSeatInfoVo.setCityName(cityCache.getCityName(adSeatInfoVo.getCity()));
         		}
            }
            
            //[4] 查询出正常的广告位
            searchMap.clear();
            tmpSeatIds.removeAll(problemInfoIds);
            searchMap.put("adseatInfoIds", tmpSeatIds);
            if(tmpSeatIds.size() > 0) {
            	adSeatInfoVos = adSeatService.selectSeatByIds(searchMap);
                for (AdSeatInfoVo adSeatInfoVo : adSeatInfoVos) {
                	adSeatInfoVo.setProvinceName(cityCache.getCityName(adSeatInfoVo.getProvince()));
                	adSeatInfoVo.setCityName(cityCache.getCityName(adSeatInfoVo.getCity()));
        		}
            }
            problemInfoIds.clear();
        }
        
        model.addAttribute("problemAdSeatInfos", problemAdSeatInfos);
        model.addAttribute("adSeatInfoVos", adSeatInfoVos);
        model.addAttribute(SysConst.RESULT_KEY, result);
        
        tmpSeatIds.clear();
        return model;
    }
    
    /**
     * 新增/编辑活动
     */
    @RequiresRoles(value = {"activityadmin", "depactivityadmin", "superadmin", "customer"}, logical = Logical.OR)
    @ResponseBody
    @RequestMapping("/activity/save")
    public Model save(Model model, HttpServletRequest request, HttpServletResponse response,
                      @RequestParam(value = "id", required = false) String id,
                      @RequestParam(value = "activityName", required = false) String activityName,
                      @RequestParam(value = "startDate", required = false) String startDate,
                      @RequestParam(value = "endDate", required = false) String endDate,
                      @RequestParam(value = "area", required = false) String area,
                      @RequestParam(value = "media", required = false) String media,
                      @RequestParam(value = "dels", required = false) String dels,
                      @RequestParam(value = "samplePicUrl", required = false) String samplePicUrl,
                      @RequestParam(value = "customerId", required = false) Integer customerId,
                      @RequestParam(value = "activeSeat", required = false) String activeSeat,
                      @RequestParam(value = "upMonitorLastDays", required = false) String upMonitorLastDays,
                      @RequestParam(value = "durationMonitorLastDays", required = false) String durationMonitorLastDays,
                      @RequestParam(value = "downMonitorLastDays", required = false) String downMonitorLastDays,
                      @RequestParam(value = "upTaskTime", required = false) String upTaskTime,
                      @RequestParam(value = "upMonitorTaskTime", required = false) String upMonitorTaskTime,
                      @RequestParam(value = "durationMonitorTaskTime", required = false) String durationMonitorTaskTime,
                      @RequestParam(value = "downMonitorTaskTime", required = false) String downMonitorTaskTime,
                      @RequestParam(value = "upTaskPoint", required = false) Integer upTaskPoint,
                      @RequestParam(value = "upMonitorTaskPoint", required = false) Integer upMonitorTaskPoint,
                      @RequestParam(value = "durationMonitorTaskPoint", required = false) Integer durationMonitorTaskPoint,
                      @RequestParam(value = "downMonitorTaskPoint", required = false) Integer downMonitorTaskPoint,
                      @RequestParam(value = "upTaskMoney", required = false) double upTaskMoney,
                      @RequestParam(value = "upMonitorTaskMoney", required = false) double upMonitorTaskMoney,
                      @RequestParam(value = "durationMonitorTaskMoney", required = false) double durationMonitorTaskMoney,
                      @RequestParam(value = "downMonitorTaskMoney", required = false) double downMonitorTaskMoney,
                      @RequestParam(value = "qualifiedPicUrl", required = false) String qualifiedPicUrl,
                      @RequestParam(value = "noQualifiedPicUrl1", required = false) String noQualifiedPicUrl1,
                      @RequestParam(value = "noQualifiedPicUrl2", required = false) String noQualifiedPicUrl2,
                      @RequestParam(value = "noQualifiedPicUrl3", required = false) String noQualifiedPicUrl3,
                      @RequestParam(value = "noQualifiedText1", required = false) String noQualifiedText1,
                      @RequestParam(value = "noQualifiedText2", required = false) String noQualifiedText2,
                      @RequestParam(value = "noQualifiedText3", required = false) String noQualifiedText3,
                      @RequestParam(value = "notification", required = false) String notification) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date now = new Date();
        ResultVo<String> result = new ResultVo<String>();
        result.setCode(ResultCode.RESULT_SUCCESS.getCode());
        result.setResult("保存成功");
        model = new ExtendedModelMap();

        SysUser user = (SysUser) ShiroUtils.getSessionAttribute(SessionKey.SESSION_LOGIN_USER.toString());
        if (StringUtil.isNotEmpty(id)) {
        	AdActivityVo activity = adActivityService.getVoById(Integer.valueOf(id));
        	if (user.getUsertype()==2&&user.getId().intValue()!=activity.getUserId().intValue()) {
        		result.setCode(ResultCode.RESULT_NOAUTH.getCode());
                result.setResult("没有权限！");
                model.addAttribute(SysConst.RESULT_KEY, result);
        		return model;
    		}
		}

        AdActivityVo adActivityVo = new AdActivityVo();
        adActivityVo.setActivityName(activityName);
        adActivityVo.setSamplePicUrl(samplePicUrl);    
        adActivityVo.setQualifiedPicUrl(qualifiedPicUrl);
        adActivityVo.setNoQualifiedPicUrl1(noQualifiedPicUrl1);
        adActivityVo.setNoQualifiedPicUrl2(noQualifiedPicUrl2);
        adActivityVo.setNoQualifiedPicUrl3(noQualifiedPicUrl3);
        adActivityVo.setNoQualifiedText1(noQualifiedText1);
        adActivityVo.setNoQualifiedText2(noQualifiedText2);
        adActivityVo.setNoQualifiedText3(noQualifiedText3);
        adActivityVo.setNotification(notification);
//        String[] str = media.split(",");
//        for(String i : str) {
//        	AdActivityMedia aam = new AdActivityMedia();
//        	aam.setMediaId(Integer.parseInt(i));
//        	adActivityVo.getActivityMedias().add(aam);
//        }
        
//		adActivityVo.setCustomerTypeId(customerTypeId); //客户类型
        try {
            adActivityVo.setStartTime(sdf.parse(startDate));
        } catch (ParseException e) {
            result.setCode(ResultCode.RESULT_FAILURE.getCode());
            result.setResultDes("日期格式有误！");
            model.addAttribute(SysConst.RESULT_KEY, result);
            return model;
        }
        try {
            adActivityVo.setEndTime(sdf.parse(endDate));
        } catch (ParseException e) {
            result.setCode(ResultCode.RESULT_FAILURE.getCode());
            result.setResultDes("日期格式有误！");
            model.addAttribute(SysConst.RESULT_KEY, result);
            return model;
        }

        //构建活动的广告位
        if(StringUtil.isEmpty(activeSeat)) {
        	result.setCode(ResultCode.RESULT_FAILURE.getCode());
            result.setResultDes("请先选择活动的广告位！");
            model.addAttribute(SysConst.RESULT_KEY, result);
            return model;
        }
        
        //构造样例图
//        AdActivityAdseat adActivityAdseat = new AdActivityAdseat();
//        adActivityAdseat.setSamplePicUrl(samplePicUrl);
//        adActivityVo.getActivitySeats().add(adActivityAdseat);
        
//        //构造活动地区
//        List<JsonObject> areas = GsonUtil.getObjectList(area, JsonObject.class);
//        if (areas.size() > 0) {
//            for (JsonObject obj : areas) {
//            	boolean zhixiashiFlag = false;
//            	if(obj.get("city") == null || StringUtils.equals(String.valueOf(obj.get("city")), "null")) {
//            		//直辖市"市为空"
//            		zhixiashiFlag = true;
//            	}
//                AdActivityArea aa = new AdActivityArea();
//                aa.setProvince(obj.get("province").getAsLong());
//                if(zhixiashiFlag == true) {
//                	aa.setCity(obj.get("province").getAsLong());
//                } else {
//                	aa.setCity(obj.get("city").getAsLong());
//                }
//                aa.setRegion(obj.get("region").getAsLong());
//                aa.setStreet(obj.get("street").getAsLong());
//                aa.setCreateTime(now);
//                aa.setUpdateTime(now);
//                adActivityVo.getActivityAreas().add(aa);
//            }
//        }
        
//        //选中的广告位所属媒体（不重复）集合
//        Set<Integer> mediaSet = new HashSet<>();
      
        //构造活动广告位
//        List<JsonObject> seats = GsonUtil.getObjectList(activeSeat, JsonObject.class);
//        if (seats.size() > 0) {
//            for (JsonObject obj : seats) {
//                AdActivityAdseat as = new AdActivityAdseat();
//                as.setAdSeatId(obj.get("seatId").getAsInt());
//                as.setBrand(obj.get("brand").getAsString());
//                as.setUpMonitor(obj.get("upMonitor").getAsInt());
//                as.setSamplePicUrl(samplePicUrl);
//                if(as.getUpMonitor()==1){
//                    as.setUpMonitorLastDays(obj.get("upMonitorLastDays").getAsInt());
//                }
//                as.setDurationMonitor(obj.get("durationMonitor").getAsInt());
//                if(as.getDurationMonitor()==1){
//                    as.setDurationMonitorLastDays(obj.get("durationMonitorLastDays").getAsInt());
//                }
//                as.setDownMonitor(obj.get("downMonitor").getAsInt());
//                if(as.getDownMonitor()==1){
//                    as.setDownMonitorLastDays(obj.get("downMonitorLastDays").getAsInt());
//                }
//                as.setMediaId(obj.get("mediaId").getAsInt());
//                mediaSet.add(obj.get("mediaId").getAsInt());
//                
//                as.setMonitorCount(obj.get("monitorCount").getAsInt());
//                try {
//                    as.setMonitorStart(sdf.parse(obj.get("startDate").getAsString()));
//                    as.setMonitorEnd(sdf.parse(obj.get("endDate").getAsString()));
//                } catch (ParseException e) {
//                    result.setCode(ResultCode.RESULT_FAILURE.getCode());
//                    result.setResultDes("日期格式有误！");
//                    model.addAttribute(SysConst.RESULT_KEY, result);
//                    return model;
//                }
//                as.setTaskCreate(1);
////                as.setSamplePicUrl(obj.get("samplePicUrl").getAsString());
//                as.setCreateTime(now);
//                as.setUpdateTime(now);
//                adActivityVo.getActivitySeats().add(as);
//            }
//        }

//        //构造活动媒体
//        for (Integer mediaId : mediaSet) {
//            AdActivityMedia am = new AdActivityMedia();
//            am.setMediaId(mediaId);
//            am.setCreateTime(now);
//            am.setUpdateTime(now);
//            adActivityVo.getActivityMedias().add(am);
//        }

        adActivityVo.setUpTaskTime(upTaskTime); //上刊任务出报告时间
        adActivityVo.setUpMonitorTaskTime(upMonitorTaskTime); //上刊监测任务出报告时间
        adActivityVo.setDurationMonitorTaskTime(durationMonitorTaskTime); //投放期间监测任务出报告时间
        adActivityVo.setDownMonitorTaskTime(downMonitorTaskTime); //下刊监测任务出报告时间
        
        adActivityVo.setUpTaskPoint(upTaskPoint);//上刊任务积分
        adActivityVo.setUpMonitorTaskPoint(upMonitorTaskPoint);//上刊监测任务积分
        adActivityVo.setDurationMonitorTaskPoint(durationMonitorTaskPoint);//投放期间监测积分
        adActivityVo.setDownMonitorTaskPoint(downMonitorTaskPoint);//下刊监测积分
        
        adActivityVo.setUpTaskMoney(upTaskMoney);//上刊任务金额
        adActivityVo.setUpMonitorTaskMoney(upMonitorTaskMoney);//上刊监测任务金额
        adActivityVo.setDurationMonitorTaskMoney(durationMonitorTaskMoney);//投放期间监测任务金额
        adActivityVo.setDownMonitorTaskMoney(downMonitorTaskMoney);//下刊监测任务金额
        
        //新增
        if (StringUtil.isEmpty(id)) {
            adActivityVo.setStatus(ActivityStatus.UNCONFIRM.getId());
            if(customerId != null) {
            	//超级管理员/部门领导/活动审核部员工 帮助广告商创建的活动
            	adActivityVo.setUserId(customerId);
            } else {
            	//广告商自行创建的活动
            	adActivityVo.setUserId(user.getId());
            }
            adActivityVo.setCreateTime(now);
            adActivityVo.setUpdateTime(now);
            adActivityService.add(adActivityVo, activeSeat);
        } else {//更新
            adActivityVo.setId(new Integer(id));
            adActivityVo.setUpdateTime(now);
//            if(customerId != null) {
//            	//超级管理员/部门领导/活动审核部员工 帮助广告商创建的活动
//            	adActivityVo.setUserId(customerId);
//            } else {
//            	//广告商自行创建的活动
//            	adActivityVo.setUserId(user.getId());
//            }
            adActivityService.modify(adActivityVo, activeSeat);
        }

        model.addAttribute(SysConst.RESULT_KEY, result);
        return model;
    }

    @RequiresRoles("customer")
    @RequestMapping(value = "/jiucuo/list")
    public String jiucuoList(Model model, HttpServletRequest request,
                             @RequestParam(value = "activityId", required = false) Integer activityId,
                             @RequestParam(value = "problemStatus", required = false) Integer problemStatus,
                             @RequestParam(value = "startDate", required = false) String startDate,
                             @RequestParam(value = "endDate", required = false) String endDate,
                             @RequestParam(value = "mediaId", required = false) Integer mediaId,
                             @RequestParam(value = "name", required = false) String name,
                             @RequestParam(value = "mediaTypeId", required = false) Integer mediaTypeId,
                             @RequestParam(value = "mediaTypeParentId", required = false) Integer mediaTypeParentId,
                             @RequestParam(value = "province", required = false) String province,
                             @RequestParam(value = "city", required = false) String city) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SearchDataVo vo = SearchUtil.getVo();

        if (activityId != null) {
            vo.putSearchParam("activityId", activityId.toString(), activityId);
        }
        //只查询处于审核通过，有问题，已解决或闭环的纠错
        if (problemStatus != null) {
            vo.putSearchParam("problemStatus", problemStatus.toString(), problemStatus);
        }

        vo.putSearchParam("status", null, JiucuoTaskStatus.VERIFIED.getId());
        //只查询本客户有关的纠错
        SysUser user = (SysUser) ShiroUtils.getSessionAttribute(SessionKey.SESSION_LOGIN_USER.toString());
        vo.putSearchParam("customerUserId", null, user.getId());

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
        //查询活动名称
        if (name != null) {
        	name = "%" + name + "%";
            vo.putSearchParam("activityName", name, name);
        }
        //查询媒体主
        if (mediaId != null) {
            vo.putSearchParam("mediaId", mediaId.toString(), mediaId);
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

        return PageConst.CUSTOMER_JIUCUO_LIST;
    }

    @RequiresRoles("customer")
    @RequestMapping(value = "/jiucuo/detail")
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
        return PageConst.CUSTOMER_JIUCUO_DETAIL;
    }

    @RequestMapping(value = "/resource", method = { RequestMethod.GET, RequestMethod.POST })
    public String index(Model model, @RequestParam(value = "tag", required = false) String tag,
                        @RequestParam(value = "startDate", required = false) String startDate,
                        @RequestParam(value = "endDate", required = false) String endDate,
                        @RequestParam(value = "cpcUserId", required = false) String cpcUserId,
                        @RequestParam(value = "mediaFlag", required = false) String mediaFlag,
                        @RequestParam(value = "agentName", required = false) Integer agentName,
                        @RequestParam(value = "customerId", required = false) Integer customerId) {

        String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        if (StringUtils.isBlank(startDate) && StringUtils.isBlank(endDate)) {
            startDate = today;
            endDate = today;
        }

        return PageConst.CUSTOMER_RESOURCE;
    }

    @RequestMapping(value = "/report", method = { RequestMethod.GET, RequestMethod.POST })
    public String toReport(Model model, @RequestParam(value = "tag", required = false) String tag,
                        @RequestParam(value = "startDate", required = false) String startDate,
                        @RequestParam(value = "endDate", required = false) String endDate,
                        @RequestParam(value = "cpcUserId", required = false) String cpcUserId,
                        @RequestParam(value = "mediaFlag", required = false) String mediaFlag,
                        @RequestParam(value = "agentName", required = false) Integer agentName,
                        @RequestParam(value = "customerId", required = false) Integer customerId) {

        String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        if (StringUtils.isBlank(startDate) && StringUtils.isBlank(endDate)) {
            startDate = today;
            endDate = today;
        }

        return PageConst.CUSTOMER_REPORT;
    }

//    public static void main(String[] args) {
//        System.out.println(GsonUtil.getObjectList("[{'province':1}]", JsonObject.class));
//    }
}