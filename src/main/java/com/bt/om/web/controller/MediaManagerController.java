package com.bt.om.web.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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

import com.adtime.common.lang.StringUtil;
import com.bt.om.common.SysConst;
import com.bt.om.common.web.PageConst;
import com.bt.om.entity.AdActivity;
import com.bt.om.entity.AdActivityAdseat;
import com.bt.om.entity.AdJiucuoTask;
import com.bt.om.entity.AdJiucuoTaskFeedback;
import com.bt.om.entity.AdMedia;
import com.bt.om.entity.AdMediaType;
import com.bt.om.entity.AdMonitorTask;
import com.bt.om.entity.AdSeatInfo;
import com.bt.om.entity.SysUser;
import com.bt.om.entity.SysUserExecute;
import com.bt.om.entity.vo.AdJiucuoTaskVo;
import com.bt.om.entity.vo.AdMonitorTaskVo;
import com.bt.om.entity.vo.AdSeatInfoVo;
import com.bt.om.entity.vo.SysUserVo;
import com.bt.om.enums.AllowMultiEnum;
import com.bt.om.enums.JiucuoTaskStatus;
import com.bt.om.enums.MonitorTaskStatus;
import com.bt.om.enums.ResultCode;
import com.bt.om.enums.RewardTaskType;
import com.bt.om.enums.SessionKey;
import com.bt.om.enums.TaskProblemStatus;
import com.bt.om.mapper.AdMediaMapper;
import com.bt.om.security.ShiroUtils;
import com.bt.om.service.IAdActivityService;
import com.bt.om.service.IAdJiucuoTaskService;
import com.bt.om.service.IAdMediaTypeService;
import com.bt.om.service.IAdMonitorTaskService;
import com.bt.om.service.IAdSeatService;
import com.bt.om.service.IResourceService;
import com.bt.om.service.ISysUserExecuteService;
import com.bt.om.service.ISysUserService;
import com.bt.om.vo.web.ResultVo;
import com.bt.om.vo.web.SearchDataVo;
import com.bt.om.web.util.SearchUtil;
import com.google.common.collect.Maps;

/**
 * Created by caiting on 2018/2/5.
 */
@Controller
@RequestMapping(value = "/platmedia")
public class MediaManagerController {
    @Autowired
    private IAdMonitorTaskService adMonitorTaskService;
    @Autowired
    ISysUserExecuteService sysUserExecuteService;
    @Autowired
    IAdJiucuoTaskService adJiucuoTaskService;
    @Autowired
    IAdActivityService adActivityService;
    @Autowired
    IAdSeatService adSeatService;
    @Autowired
    private IResourceService resourceService;
    @Autowired
	private ISysUserService sysUserService;
	@Autowired
	private AdMediaMapper adMediaMapper;
	@Autowired
	private IAdMediaTypeService adMediaTypeService;

    /**
     * 媒体端任务管理，主要分配任务
     **/
    @RequiresRoles("media")
    @RequestMapping(value = "/task/list")
    public String getTaskList(Model model, HttpServletRequest request,
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
        SearchDataVo vo = SearchUtil.getVo();
        
        if (status != null) {
            vo.putSearchParam("status", status.toString(), status);
        }
        
        if (activityId != null) {
            vo.putSearchParam("activityId", activityId.toString(), activityId);
        }

        //只显示待指派的
       // vo.putSearchParam("status", null, MonitorTaskStatus.UNASSIGN.getId());
        //只显示上刊和下刊任务
//        vo.putSearchParam("taskTypes", null, new Integer[]{MonitorTaskType.SET_UP_MONITOR.getId()});
        //只显示本媒体广告位的任务
        SysUser user = (SysUser) ShiroUtils.getSessionAttribute(SessionKey.SESSION_LOGIN_USER.toString());
        vo.putSearchParam("mediaUserId", null, user.getId());

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
        // vo.putSearchParam("hasUserId","1","1");
        adMonitorTaskService.getPageData(vo);

        SearchUtil.putToModel(model, vo);

        return PageConst.MEDIA_TASK_LIST;
    }

    /**
     * 媒体端报错任务列表
     **/
    @RequiresRoles("media")
    @RequestMapping(value = "/task/problemList")
    public String getTaskHasProblemList(Model model, HttpServletRequest request,
                                        @RequestParam(value = "activityId", required = false) Integer activityId,
                                        @RequestParam(value = "problemStatus", required = false) Integer problemStatus,
                                        @RequestParam(value = "startDate", required = false) String startDate,
                                        @RequestParam(value = "endDate", required = false) String endDate,
                                        @RequestParam(value = "name", required = false) String name) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SearchDataVo vo = SearchUtil.getVo();

        if (activityId != null) {
            vo.putSearchParam("activityId", activityId.toString(), activityId);
        }

        //只显示已审核通过带有问题的任务
        if (problemStatus == null) {
            vo.putSearchParam("problemStatuses", null, new Integer[]{TaskProblemStatus.PROBLEM.getId(), TaskProblemStatus.FIXED.getId(), TaskProblemStatus.CLOSED.getId()});
        } else {
            vo.putSearchParam("problemStatus", problemStatus.toString(), problemStatus);
        }
        vo.putSearchParam("status", null, MonitorTaskStatus.VERIFIED.getId());
        //只显示本媒体广告位的任务
        SysUser user = (SysUser) ShiroUtils.getSessionAttribute(SessionKey.SESSION_LOGIN_USER.toString());
        vo.putSearchParam("mediaUserId", null, user.getId());

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
        // vo.putSearchParam("hasUserId","1","1");
        adMonitorTaskService.getPageData(vo);

        SearchUtil.putToModel(model, vo);

        return PageConst.MEDIA_PROBLEM_TASK_LIST;
    }

    /**
     * 选择监测人员页面
     **/
    @RequiresRoles("media")
    @RequestMapping(value = "/selectUserExecute")
    public String toSelectUserExecute(Model model, HttpServletRequest request) {

        SysUser user = (SysUser) ShiroUtils.getSessionAttribute(SessionKey.SESSION_LOGIN_USER.toString());

        Map condition = Maps.newHashMap();
        condition.put("operateId", user.getId());

        List<SysUserExecute> ues = sysUserExecuteService.getByConditionMap(condition);
        model.addAttribute("userList", ues);

        return PageConst.SELECT_USER_EXECUTE;
    }

    //处理问题，只是标示该任务的问题已被处理
    @RequiresRoles("media")
    @RequestMapping(value = "/task/fix")
    @ResponseBody
    public Model fix(Model model, HttpServletRequest request,
                     @RequestParam(value = "id", required = false) Integer id) {
        ResultVo<String> result = new ResultVo<String>();
        result.setCode(ResultCode.RESULT_SUCCESS.getCode());
        result.setResultDes("处理成功");
        model = new ExtendedModelMap();

        AdMonitorTask task = new AdMonitorTask();
        task.setProblemStatus(TaskProblemStatus.FIXED.getId());
        task.setId(id);
        try {
            adMonitorTaskService.update(task);
        } catch (Exception e) {
            result.setCode(ResultCode.RESULT_FAILURE.getCode());
            result.setResultDes("处理失败！");
            model.addAttribute(SysConst.RESULT_KEY, result);
            return model;
        }

        model.addAttribute(SysConst.RESULT_KEY, result);
        return model;
    }
    
    @RequestMapping(value = "/jiucuo/list")
    @RequiresRoles("media")
    public String joucuoList(Model model, HttpServletRequest request,
                             @RequestParam(value = "activityId", required = false) Integer activityId,
                             @RequestParam(value = "problemStatus", required = false) Integer problemStatus,
                             @RequestParam(value = "startDate", required = false) String startDate,
                             @RequestParam(value = "endDate", required = false) String endDate,
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
        } else {
            vo.putSearchParam("problemStatuses", null, new Integer[]{TaskProblemStatus.PROBLEM.getId(), TaskProblemStatus.FIXED.getId(), TaskProblemStatus.CLOSED.getId()});
        }
        vo.putSearchParam("status", null, JiucuoTaskStatus.VERIFIED.getId());
        //只查询本媒体广告位有关的纠错
        SysUser user = (SysUser) ShiroUtils.getSessionAttribute(SessionKey.SESSION_LOGIN_USER.toString());
        vo.putSearchParam("mediaUserId", null, user.getId());

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
        adJiucuoTaskService.getPageData(vo);
        SearchUtil.putToModel(model, vo);

        return PageConst.MEDIA_JIUCUO_LIST;
    }

    @RequiresRoles("media")
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

    //处理纠错问题，只是标示该纠错任务的问题已被处理
    @RequiresRoles("media")
    @RequestMapping(value = "/jiucuo/fix")
    @ResponseBody
    public Model jiucuoFix(Model model, HttpServletRequest request,
                           @RequestParam(value = "id", required = false) Integer id) {
        ResultVo<String> result = new ResultVo<String>();
        result.setCode(ResultCode.RESULT_SUCCESS.getCode());
        result.setResultDes("处理成功");
        model = new ExtendedModelMap();

        AdJiucuoTask task = new AdJiucuoTask();
        task.setProblemStatus(TaskProblemStatus.FIXED.getId());
        task.setId(id);
        try {
            adJiucuoTaskService.update(task);
        } catch (Exception e) {
            result.setCode(ResultCode.RESULT_FAILURE.getCode());
            result.setResultDes("处理失败！");
            model.addAttribute(SysConst.RESULT_KEY, result);
            return model;
        }

        model.addAttribute(SysConst.RESULT_KEY, result);
        return model;
    }

    /**
     * 广告位列表
     **/
    @RequiresRoles(value= "media")
    @RequestMapping(value = "/adseat/list")
    public String adseatList(Model model, HttpServletRequest request,
                             @RequestParam(value = "province", required = false) Long province,
                             @RequestParam(value = "city", required = false) Long city,
                             @RequestParam(value = "region", required = false) Long region,
                             @RequestParam(value = "street", required = false) Long street,
                             @RequestParam(value = "mediaId", required = false) Integer mediaId) {
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
        if (mediaId != null) {
            vo.putSearchParam("mediaId", mediaId.toString(), mediaId);
        }
        //只查询本媒体广告位
        SysUser user = (SysUser) ShiroUtils.getSessionAttribute(SessionKey.SESSION_LOGIN_USER.toString());
        vo.putSearchParam("userId", null, user.getId());

        adSeatService.getPageData(vo);
        SearchUtil.putToModel(model, vo);

        return PageConst.MEDIA_ADSEAT_LIST;
    }

    /**
     * 广告位编辑 前往编辑页面
     **/
    @RequiresRoles(value = {"superadmin", "media"}, logical = Logical.OR)
    @RequestMapping(value = "/adseat/edit")
    public String toEdit(Model model, HttpServletRequest request,
                         @RequestParam(value = "id", required = false) Integer id) {
        if (id != null) {
        	AdSeatInfoVo adSeatInfoVo = resourceService.getAdSeatInfoById(id + "");
        	String adSize = adSeatInfoVo.getAdSize();
        	String[] strings = adSize.split("\\*");
        	adSeatInfoVo.setWidth(strings[0]);
        	adSeatInfoVo.setHeight(strings[1]);
            model.addAttribute("adSeatInfo", adSeatInfoVo);
        }
        
        //获取登录用户信息
        SysUser user = (SysUser) ShiroUtils.getSessionAttribute(SessionKey.SESSION_LOGIN_USER.toString());
        
        if(user != null) {
        	model.addAttribute("usertype", user.getUsertype());
        }
        
        return PageConst.MEDIA_ADSEAT_EDIT;
    }

    /**
     * 保存广告位(媒体主 和 超级管理员创建广告位共用)
     **/
    @RequiresRoles(value = {"superadmin", "media"}, logical = Logical.OR)
    @RequestMapping(value = "/adseat/save")
    @ResponseBody
    public Model addInfo(Model model, AdSeatInfo adSeatInfo, HttpServletRequest request, Integer mediaId) {
        ResultVo<String> result = new ResultVo<String>();
        result.setCode(ResultCode.RESULT_SUCCESS.getCode());
        result.setResultDes("保存成功");
        model = new ExtendedModelMap();

        adSeatInfo.setAdSize(adSeatInfo.getWidth() + "*" + adSeatInfo.getHeight());
		Map<String, Object> modelMap = new HashMap<String, Object>();

        try {
        	//校验地址唯一性
        	Long province = adSeatInfo.getProvince();
        	Long city = adSeatInfo.getCity();
//        	Long region = adSeatInfo.getRegion();
//        	Long street = adSeatInfo.getStreet();
        	String road = adSeatInfo.getRoad();
        	String location = adSeatInfo.getLocation();
        	Map<String, Object> searchMap = new HashMap<>();
        	if(province!=null && province !=0) {
        		searchMap.put("province", province);
        	}
        	if(city!=null && city!=0) {
        		searchMap.put("city", city);
        	}
        	
        	if(adSeatInfo.getLon() != null && adSeatInfo.getLat() == null) {
        		result.setCode(ResultCode.RESULT_FAILURE.getCode());
                result.setResultDes("经纬度填写不完整！");
                model.addAttribute(SysConst.RESULT_KEY, result);
                return model;
        	}
        	
        	if(adSeatInfo.getLat() != null && adSeatInfo.getLon() == null) {
        		result.setCode(ResultCode.RESULT_FAILURE.getCode());
                result.setResultDes("经纬度填写不完整！");
                model.addAttribute(SysConst.RESULT_KEY, result);
                return model;
        	}
        	
//        	searchMap.put("region", region);
//        	searchMap.put("street", street);
        	if(road !=null && !road.equals("")) {
        		searchMap.put("road", road);
        	}else {
        		result.setCode(ResultCode.RESULT_FAILURE.getCode());
                result.setResultDes("广告位主要路段不能为空！");
                model.addAttribute(SysConst.RESULT_KEY, result);
                return model;
        	}
        	if(location!=null && !location.equals("")) {
        		searchMap.put("location", location);
        	}else {
        		result.setCode(ResultCode.RESULT_FAILURE.getCode());
                result.setResultDes("广告位详细位置不能为空！");
                model.addAttribute(SysConst.RESULT_KEY, result);
                return model;
        	}
        	
        	if(adSeatInfo.getMultiNum() == 0) {
        		adSeatInfo.setMultiNum(1);
        		adSeatInfo.setAllowMulti(AllowMultiEnum.NOT_ALLOW.getId());
        	}
        	if(adSeatInfo.getAllowMulti() == 0) {
        		adSeatInfo.setMultiNum(1); //0代表不允许同时有多个活动, 设置活动数量为1
        	}
        	
            if (adSeatInfo.getId() != null) {
            	//修改时检验广告位位置唯一，忽略自身，先查校验后再修改
            	List<AdSeatInfo> adSeatInfos = adSeatService.searchLocation(searchMap);
            	if(adSeatInfos != null && adSeatInfos.size() > 0) {
            		for (AdSeatInfo adSeatInfo2 : adSeatInfos) {
            			if(!adSeatInfo.getId().equals(adSeatInfo2.getId())) {
                			result.setCode(ResultCode.RESULT_FAILURE.getCode());
                            result.setResultDes("广告位位置已存在！");
                            model.addAttribute(SysConst.RESULT_KEY, result);
                            return model;
                		}
					}
            	}
            	
            	//修改时校验广告位编号唯一, 忽略自身
            	if(StringUtil.isNotBlank(adSeatInfo.getMemo())) {
            		Map<String, Object> map = new HashMap<>();
            		map.put("memo", adSeatInfo.getMemo());
            		List<AdSeatInfo> adSeatInfosForMemo = adSeatService.searchLocation(map);
            		if(adSeatInfosForMemo != null && adSeatInfosForMemo.size() > 0) {
            			for (AdSeatInfo adSeatInfo3 : adSeatInfosForMemo) {
                    		if(!adSeatInfo.getId().equals(adSeatInfo3.getId())) {
                    			result.setCode(ResultCode.RESULT_FAILURE.getCode());
                                result.setResultDes("广告位编号已存在！");
                                model.addAttribute(SysConst.RESULT_KEY, result);
                                return model;
                    		}
    					}
            		}
            	}
            	
            	//修改
                adSeatService.modify(adSeatInfo);
            } else {
            	int count = adSeatService.selectByLocation(searchMap);
            	if(count > 0) {
            		result.setCode(ResultCode.RESULT_FAILURE.getCode());
                    result.setResultDes("广告位位置已存在！");
                    model.addAttribute(SysConst.RESULT_KEY, result);
                    return model;
            	}
            	
            	//添加时校验广告位编号唯一
            	if(StringUtil.isNotBlank(adSeatInfo.getMemo())) {
            		Map<String, Object> map = new HashMap<>();
            		map.put("memo", adSeatInfo.getMemo());
            		List<AdSeatInfo> adSeatInfosForMemo = adSeatService.searchLocation(map);
            		if(adSeatInfosForMemo != null && adSeatInfosForMemo.size() > 0) {
            			result.setCode(ResultCode.RESULT_FAILURE.getCode());
                        result.setResultDes("广告位编号已存在！");
                        model.addAttribute(SysConst.RESULT_KEY, result);
                        return model;
            		}
            	}
            	
            	if(adSeatInfo.getMediaTypeParentId() == null || adSeatInfo.getMediaTypeId() == null) {
            		result.setCode(ResultCode.RESULT_FAILURE.getCode());
                    result.setResultDes("媒体类型不能为空！");
                    model.addAttribute(SysConst.RESULT_KEY, result);
                    return model;
            	}
            	
            	//添加
                SysUser user = (SysUser) ShiroUtils.getSessionAttribute(SessionKey.SESSION_LOGIN_USER.toString());
                AdMedia media = new AdMedia();
            	SysUserVo mediaUser = new SysUserVo();
        		Integer usertype = user.getUsertype();
        		if (usertype == 3) {
        			//媒体人员自行添加 3：媒体账户
        			media = adMediaMapper.selectByUserId(user.getId()); //通过登录后台用户id查询AdMedia信息
        			mediaUser = sysUserService.findUserinfoById(media.getUserId()); //通过登录后台用户id查询媒体信息(比如二维码前缀)
        		} else {
        			//超级管理员添加广告位
        			media = adMediaMapper.selectByPrimaryKey(mediaId);
        			mediaUser = sysUserService.findUserinfoById(media.getUserId()); //通过媒体的后台用户id查询媒体信息(比如二维码前缀)
        		}
        		
//        		//生成广告位对应的二维码
//        		String adCodeInfo = mediaUser.getPrefix() + UUID.randomUUID(); //二维码存的值（媒体前缀比如media3- 加上UUID随机数）
//        		String path = request.getSession().getServletContext().getRealPath("/");
//        		path = path + (path.endsWith(File.separator)?"":File.separatorChar)+"static"+File.separatorChar+"qrcode"+File.separatorChar+adCodeInfo + ".jpg";
//        		QRcodeUtil.encode(adCodeInfo, path);
//        		adSeatInfo.setAdCode(adCodeInfo);
//        		adSeatInfo.setAdCodeUrl("/static/qrcode/" + adCodeInfo + ".jpg");
        		
        		//默认没有贴上二维码
        		adSeatInfo.setCodeFlag(0);
        		adSeatService.save(adSeatInfo, mediaUser.getId());
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
     * 判断广告位编号是否已存在
     **/
    @RequiresRoles("media")
    @RequestMapping(value = "/adseat/isExistsCode")
    @ResponseBody
    public Model isExistsCode(Model model, HttpServletRequest request,
                              @RequestParam(value = "id", required = false) Integer id,
                              @RequestParam(value = "adCode", required = false) String adCode) {
        ResultVo<String> result = new ResultVo<String>();
        result.setCode(ResultCode.RESULT_SUCCESS.getCode());
        result.setResultDes("判断成功");
        model = new ExtendedModelMap();

        SearchDataVo vo = SearchUtil.getVo();

        if (adCode != null) {
            vo.putSearchParam("adCode", null, adCode);
        }
        if (id != null) {
            vo.putSearchParam("exceptId", null, id);
        }
        //只查询本媒体广告位
        SysUser user = (SysUser) ShiroUtils.getSessionAttribute(SessionKey.SESSION_LOGIN_USER.toString());
        vo.putSearchParam("userId", null, user.getId());

        try {
            int count = adSeatService.getPageCount(vo);

            if (count > 0) {
                result.setCode(ResultCode.RESULT_FAILURE.getCode());
                result.setResultDes("已存在该编号，请修改！");
            }
        } catch (Exception e) {
            result.setCode(ResultCode.RESULT_FAILURE.getCode());
            result.setResultDes("判断失败！");
            return model;
        }

        model.addAttribute(SysConst.RESULT_KEY, result);
        return model;
    }

    /**
     * 删除广告位
     **/
    @RequiresRoles("media")
    @RequestMapping(value = "/adseat/delete")
    @ResponseBody
    public Model delete(Model model, HttpServletRequest request,
                        @RequestParam(value = "id", required = false) Integer id) {
    	ResultVo<String> result = new ResultVo<String>();
        result.setCode(ResultCode.RESULT_SUCCESS.getCode());
        result.setResultDes("保存成功");
        model = new ExtendedModelMap();
        
        try {
            int count = resourceService.deleteAdSeatById(id);
            if(count == 0) {
            	result.setCode(ResultCode.RESULT_FAILURE.getCode());
                result.setResultDes("该广告位已经参与活动，不能删除！");
                model.addAttribute(SysConst.RESULT_KEY, result);
                return model;
            }
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
     * 详情页面
     *
     * @param taskId
     * @param model
     * @param request
     * @return 详情页面
     */
    @RequiresRoles(value = "media")
    @RequestMapping(value = "/task/details")
    public String gotoDetailsPage(@RequestParam("task_Id") String taskId, Model model, HttpServletRequest request) {
        AdMonitorTaskVo vo = adMonitorTaskService.getTaskDetails(taskId);
        List<AdMonitorTaskVo> list = adMonitorTaskService.getSubmitDetails(taskId);

        //获取父任务信息，分监测和纠错
        if(vo.getParentId()!=null){
            if(vo.getParentType() == RewardTaskType.MONITOR.getId()){
                //父任务是监测
                model.addAttribute("pmTask", adMonitorTaskService.getTaskVoById(vo.getParentId()));
            }else if(vo.getParentType() == RewardTaskType.JIUCUO.getId()){
                //父任务是纠错
                model.addAttribute("pjTask",adJiucuoTaskService.getVoById(vo.getParentId()));
            }
        }

        if (vo != null && list != null) {
            model.addAttribute("vo", vo);
            model.addAttribute("list", list);
            model.addAttribute("taskId", taskId);
        }
        return PageConst.MEDIA_TASK_DETAIL;
    }

    /**
     * 媒体安装人员管理列表
     **/
    @RequiresRoles("media")
    @RequestMapping(value = "/worker/list")
    public String getWorkerList(Model model, HttpServletRequest request,
                          @RequestParam(value = "name", required = false) String name) {
        SearchDataVo vo = SearchUtil.getVo();

        vo.putSearchParam("usertype", null, 3);
        SysUser user = (SysUser) ShiroUtils.getSessionAttribute(SessionKey.SESSION_LOGIN_USER.toString());
        vo.putSearchParam("operateId",null,user.getId());
        // 名称或登录账号
        if (StringUtils.isNotBlank(name)) {
            vo.putSearchParam("nameOrUsername", name, "%" + name + "%");
        }

        sysUserExecuteService.getPageData(vo);

        SearchUtil.putToModel(model, vo);

        return PageConst.MEDIA_WORKER_LIST;
    }

    /**
     * 媒体安装工人编辑
     **/
    @RequiresRoles("media")
    @RequestMapping(value = "/worker/edit")
    public String toEditWorker(Model model, HttpServletRequest request,
                         @RequestParam(value = "id", required = false) Integer id) {

        if (id != null) {
            SysUserExecute user = sysUserExecuteService.getById(id);
            if (user != null) {
                model.addAttribute("obj", user);
            }
        }

        return PageConst.MEDIA_WORKER_EDIT;
    }

    /**
     * 检查媒体工人账号是否重名
     **/
    @RequestMapping(value = {"/worker/isExistsAccountName"}, method = {RequestMethod.POST})
    @ResponseBody
    public Model isExistsAccountName(Model model,
                                     @RequestParam(value = "username", required = true) String username) {

        ResultVo<List<SysUser>> resultVo = new ResultVo<List<SysUser>>();
        try {
            List<SysUserExecute> userList = sysUserExecuteService.isExistsName(username);
            if (userList != null && userList.size() > 0) {
                resultVo.setCode(ResultCode.RESULT_FAILURE.getCode());
                resultVo.setResultDes("已存在该登录账户，请修改");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            resultVo.setCode(ResultCode.RESULT_FAILURE.getCode());
            resultVo.setResultDes("服务忙，请稍后再试");
        }

        model.addAttribute(SysConst.RESULT_KEY, resultVo);
        return model;
    }

    /**
     * 保存工人
     **/
    @RequiresRoles("media")
    @RequestMapping(value = {"/worker/save"}, method = {RequestMethod.POST})
    @ResponseBody
    public Model save(Model model,
                      @RequestParam(value = "id", required = true) Integer id,
                      @RequestParam(value = "username", required = true) String username,
                      @RequestParam(value = "password", required = true) String password,
                      @RequestParam(value = "name", required = true) String name/*,
                      @RequestParam(value = "telephone", required = true) String telephone*/) {

        ResultVo resultVo = new ResultVo();
        SysUser loginuser = (SysUser) ShiroUtils.getSessionAttribute(SessionKey.SESSION_LOGIN_USER.toString());
        try {
            if (id == null) {//新增
                SysUserExecute user = new SysUserExecute();
                user.setUsername(username);
                user.setPassword(new Md5Hash(password, username).toString());
                user.setRealname(name);
                user.setMobile(username);
                user.setUsertype(3);
                user.setStatus(1);
                user.setOperateId(loginuser.getId());
                user.setCompany(loginuser.getRealname());
                sysUserExecuteService.add(user);
            } else {//修改
                SysUserExecute user = new SysUserExecute();
                user.setId(id);
                user.setUsername(username);
                if (!"******".equals(password)) {
                    user.setPassword(new Md5Hash(password, username).toString());
                }
                user.setRealname(name);
                user.setMobile(username);
                sysUserExecuteService.modify(user);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            resultVo.setCode(ResultCode.RESULT_FAILURE.getCode());
            resultVo.setResultDes("服务忙，请稍后再试");
        }

        model.addAttribute(SysConst.RESULT_KEY, resultVo);
        return model;
    }

    @RequiresRoles("media")
    @RequestMapping(value = {"/worker/updateAccountStatus"}, method = {RequestMethod.POST})
    @ResponseBody
    public Model updateAccountStatus(Model model, @RequestParam(value = "id", required = true) Integer id,
                                     @RequestParam(value = "status", required = true) Integer status) {

        ResultVo resultVo = new ResultVo();
        try {
            SysUserExecute user = new SysUserExecute();
            user.setId(id);
            user.setStatus(status);
            sysUserExecuteService.modify(user);
        } catch (Exception ex) {
            ex.printStackTrace();
            resultVo.setCode(ResultCode.RESULT_FAILURE.getCode());
            resultVo.setResultDes("服务忙，请稍后再试");
        }

        model.addAttribute(SysConst.RESULT_KEY, resultVo);
        return model;
    }
    
    /**
     * 修改二维码状态： 已贴, 未贴
     **/
    @RequiresRoles(value= {"media","superadmin"}, logical = Logical.OR)
    @RequestMapping(value = "/codeFlag")
    @ResponseBody
    public Model updateStatus(Model model, Integer id, AdSeatInfo codeFlag) {
   
    	ResultVo<String> result = new ResultVo<String>();
        result.setCode(ResultCode.RESULT_SUCCESS.getCode());
        result.setResultDes("保存成功");
        model = new ExtendedModelMap();
        try {
        	AdSeatInfoVo seatInfo = new AdSeatInfoVo();
        	seatInfo.setId(id);
        	//seatInfo.setCodeFlag(1);
        	adSeatService.updateFlag(codeFlag.getCodeFlag(),id);
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
     * 通过媒体大类的id查询下属的所有媒体小类
     */
    @RequestMapping(value = {"/adseat/searchMediaType"}, method = {RequestMethod.POST})
    @ResponseBody
    public Model searchMediaType(Model model, @RequestParam(value = "parentId", required = true) Integer parentId) {
    	ResultVo resultVo = new ResultVo();
        try {
        	List<AdMediaType> adMediaTypes = adMediaTypeService.selectByParentId(parentId);
        	resultVo.setResult(adMediaTypes);
        	resultVo.setCode(ResultCode.RESULT_SUCCESS.getCode());
        } catch (Exception ex) {
            ex.printStackTrace();
            resultVo.setCode(ResultCode.RESULT_FAILURE.getCode());
            resultVo.setResultDes("服务忙，请稍后再试");
        }
        model.addAttribute(SysConst.RESULT_KEY, resultVo);
        return model;
    }
}
