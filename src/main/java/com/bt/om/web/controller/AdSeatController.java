package com.bt.om.web.controller;

import java.io.File;
import java.text.MessageFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

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
import org.springframework.web.servlet.ModelAndView;

import com.bt.om.cache.CityCache;
import com.bt.om.common.DateUtil;
import com.bt.om.common.SysConst;
import com.bt.om.common.web.PageConst;
import com.bt.om.entity.AdMedia;
import com.bt.om.entity.AdSeatInfo;
import com.bt.om.entity.AdSeatType;
import com.bt.om.entity.OperateLog;
import com.bt.om.entity.SysUser;
import com.bt.om.entity.vo.AdSeatCount;
import com.bt.om.entity.vo.AdSeatInfoVo;
import com.bt.om.entity.vo.CountGroupByCityVo;
import com.bt.om.entity.vo.HeatMapVo;
import com.bt.om.entity.vo.ResourceVo;
import com.bt.om.entity.vo.SysUserVo;
import com.bt.om.enums.AllowMultiEnum;
import com.bt.om.enums.ResultCode;
import com.bt.om.enums.SessionKey;
import com.bt.om.security.ShiroUtils;
import com.bt.om.service.IAdActivityService;
import com.bt.om.service.IAdSeatService;
import com.bt.om.service.IMediaService;
import com.bt.om.service.IOperateLogService;
import com.bt.om.service.IResourceService;
import com.bt.om.service.ISysUserService;
import com.bt.om.util.ConfigUtil;
import com.bt.om.util.QRcodeUtil;
import com.bt.om.vo.web.ResultVo;
import com.bt.om.vo.web.SearchDataVo;
import com.bt.om.web.BasicController;
import com.bt.om.web.util.SearchUtil;

@Controller
@RequestMapping(value = "/adseat")
public class AdSeatController extends BasicController {
	@Autowired
	private CityCache cityCache;
    @Autowired
    private IResourceService resourceService;
    @Autowired
    private IAdSeatService adSeatService;
    @Autowired
    private IAdActivityService adActivityService;
    @Autowired
	private IOperateLogService operateLogService;
    @Autowired
    private IMediaService mediaService;
    @Autowired
    private ISysUserService sysUserService;
    
	private String file_upload_path = ConfigUtil.getString("file.upload.path");
	
	private String file_upload_ip = ConfigUtil.getString("file.upload.ip");
	
	private static final Logger logger = Logger.getLogger(AdSeatController.class);

    /**
     * 新增广告位跳转
     *
     * @param model
     * @return 新增广告位页面
     */
    @RequestMapping(value = "/add")
    public String gotoAddPage(Model model) {
        List<AdMedia> mediaList = resourceService.getAll();
        List<AdSeatType> adSeatTypesList = resourceService.getSeatTypeAll();
        model.addAttribute("mediaList", mediaList);
        model.addAttribute("adSeatTypesList", adSeatTypesList);
        return PageConst.RESOURCE_ADD;
    }

    /**
     * 广告位列表展示
     */
    @RequiresRoles(value = {"superadmin" , "phoneoperator"}, logical = Logical.OR)
    @RequestMapping(value = "/list")
    public String resourceDetailPage(Model model, HttpServletRequest request,
                                     @RequestParam(value = "province", required = false) Long province,
                                     @RequestParam(value = "city", required = false) Long city,
                                     @RequestParam(value = "region", required = false) Long region,
                                     @RequestParam(value = "street", required = false) Long street,
                                     @RequestParam(value = "name", required = false) String name,
                                     @RequestParam(value = "mediaTypeParentId", required = false) Integer mediaTypeParentId,
                                     @RequestParam(value = "mediaTypeId", required = false) Integer mediaTypeId,
                                     @RequestParam(value = "mediaId", required = false) Integer mediaId) {
        SearchDataVo vo = SearchUtil.getVo();
        //获取登录用户信息
        SysUser userObj = (SysUser) ShiroUtils.getSessionAttribute(SessionKey.SESSION_LOGIN_USER.toString());
        
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
        //查询活动名称
        if (name != null) {
        	name = "%" + name + "%";
            vo.putSearchParam("activityName", name, name);
        }
        adSeatService.getPageData(vo);
        SearchUtil.putToModel(model, vo);
        
        model.addAttribute("user",userObj);
        return PageConst.ADSEAT_LIST;
    }

    /**
     * 新增广告位(暂时不用)
     *
     * @param adSeatInfoVo 封装类
     * @param request
     * @param province     省
     * @param city         市
     * @param region       区
     * @param street       街道
     * @return 提示信息
     */
    @RequestMapping(value = "/addinfo")
    @ResponseBody
    public Map<String, Object> addInfo(ResourceVo adSeatInfoVo, HttpServletRequest request,
                                       @RequestParam(value = "province", required = false) Long province,
                                       @RequestParam(value = "city", required = false) Long city,
                                       @RequestParam(value = "region", required = false) Long region,
                                       @RequestParam(value = "street", required = false) Long street) {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        try {
            if (province != null) {
                adSeatInfoVo.getAdSeatInfo().setProvince(province);
            }
            if (city != null) {
                adSeatInfoVo.getAdSeatInfo().setCity(city);
            }
            if (region != null) {
                adSeatInfoVo.getAdSeatInfo().setRegion(region);
            }
            if (street != null) {
                adSeatInfoVo.getAdSeatInfo().setStreet(street);
            }

            resourceService.insertAdSeatInfo(adSeatInfoVo);
            modelMap.put("success", true);
        } catch (Exception e) {
        	logger.error(e);
            modelMap.put("errMsg", "请重新输入!");
            e.printStackTrace();
        }
        return modelMap;
    }

    /**
     * 广告位详情展示
     *
     * @param request
     * @param model
     * @param id
     * @return 详情页面
     */
    @RequestMapping(value = "/showDetails")
    public String showDetails(HttpServletRequest request, Model model, @RequestParam(value = "id") String id) {
        AdSeatInfoVo vo = resourceService.getAdSeatInfoById(id);
        if (vo != null) {
            model.addAttribute("vo", vo);
        }
        return PageConst.RESOURCE_DETAILS;
    }

    /**
     * 广告位删除, 只能删除没有活动的广告位
     *
     * @param request
     * @param id
     * @return 提示信息
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Model deleteAdSeat(Model model, HttpServletRequest request, @RequestParam(value = "id", required = false) Integer id) {
        ResultVo<String> result = new ResultVo<String>();
        result.setCode(ResultCode.RESULT_SUCCESS.getCode());
        result.setResultDes("保存成功");
        model = new ExtendedModelMap();
        Date now = new Date();
        
        try {
        	AdSeatInfoVo adSeatInfoVo = resourceService.getAdSeatInfoById(id + "");
            int count = resourceService.deleteAdSeatById(id);
            if(count == 0) {
            	result.setCode(ResultCode.RESULT_FAILURE.getCode());
                result.setResultDes("该广告位已经参与活动，不能删除！");
                model.addAttribute(SysConst.RESULT_KEY, result);
                return model;
            }
            //添加操作日志
            SysUser user = (SysUser) ShiroUtils.getSessionAttribute(SessionKey.SESSION_LOGIN_USER.toString());
            OperateLog operateLog = new OperateLog();
        	operateLog.setContent("删除" + adSeatInfoVo.getMediaName() + "媒体下的广告位：" + cityCache.getCityName(adSeatInfoVo.getProvince())
        			+ cityCache.getCityName(adSeatInfoVo.getCity()) + adSeatInfoVo.getLocation());
            operateLog.setCreateTime(now);
            operateLog.setUpdateTime(now);
            operateLog.setUserId(user.getId());
            operateLogService.save(operateLog);
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

//	@RequiresRoles("admin")
//	@RequestMapping(value = "/edit")
//	public String toEdit(Model model, HttpServletRequest request,
//						 @RequestParam(value = "id", required = false) Integer id) {
//		if (id != null) {
//			AdSeatInfo adSeatInfo = adSeatService.getById(id);
//			List<AdCrowd> crowds = adSeatService.getCrowdsBySeatId(id);
//			Table<Integer,Integer,Integer> crowdTable= HashBasedTable.create();
//			for(AdCrowd crowd:crowds){
//				crowdTable.put(crowd.getSex(),crowd.getAgePart(),crowd.getNums());
//			}
////			model.addAttribute("crowdTable",crowdTable);
//			request.setAttribute("crowdTable",crowdTable);
//
//			model.addAttribute("adSeatInfo", adSeatInfo);
//		}
//
//		model.addAttribute("agePartValues", AgePart.values());
//
//		return PageConst.ADSEAT_EDIT;
//	}

    /**
     * 前往编辑广告位页面
     */
    @RequiresRoles(value = {"superadmin", "media" , "phoneoperator"}, logical = Logical.OR)
    @RequestMapping(value = "/edit")
    public ModelAndView toEdit(Model model, HttpServletRequest request,
                               @RequestParam(value = "id", required = false) Integer id) {

    	//获取登录用户信息
        SysUser userObj = (SysUser) ShiroUtils.getSessionAttribute(SessionKey.SESSION_LOGIN_USER.toString());
        model.addAttribute("user" , userObj);
        ModelAndView mv = new ModelAndView(PageConst.ADSEAT_EDIT);
        if (id != null) {
//            AdSeatInfo adSeatInfo = adSeatService.getById(id);
        	AdSeatInfoVo adSeatInfoVo = resourceService.getAdSeatInfoById(id + "");
        	String adSize = adSeatInfoVo.getAdSize();
        	String[] strings = adSize.split("\\*");
        	adSeatInfoVo.setWidth(strings[0]);
        	adSeatInfoVo.setHeight(strings[1]);
//            List<AdCrowd> crowds = adSeatService.getCrowdsBySeatId(id);
//            Table<Integer, Integer, String> crowdTable = HashBasedTable.create();
//            if (crowds != null && crowds.size() > 0) {
//                for (AdCrowd crowd : crowds) {
//                    crowdTable.put(crowd.getSex(), crowd.getAgePart(), "[" + crowd.getId() + "," + crowd.getNums() + "]");
//                }
//            } else {
//                for (AgePart agePart : AgePart.values()) {
//                    crowdTable.put(1, agePart.getId(), "[0,0]");
//                    crowdTable.put(2, agePart.getId(), "[0,0]");
//                }
//            }
//            mv.addObject("crowdTable", new Gson().fromJson(crowdTable.toString(), JsonObject.class));
//			request.setAttribute("crowdTable",crowdTable);

            mv.getModel().put("adSeatInfo", adSeatInfoVo);
        }
        
        //获取登录用户信息
        SysUser user = (SysUser) ShiroUtils.getSessionAttribute(SessionKey.SESSION_LOGIN_USER.toString());
        
        if(user != null) {
        	model.addAttribute("usertype", user.getUsertype());
        }

//        mv.getModel().put("agePartValues", AgePart.values());

        return mv;
    }

    /**
     * 保存广告位（暂时不用）
     **/
    @RequiresRoles(value = {"superadmin", "media"}, logical = Logical.OR)
    @RequestMapping(value = "/save")
    @ResponseBody
    public Model addInfo(Model model, AdSeatInfo adSeatInfo, HttpServletRequest request) {
        ResultVo<String> result = new ResultVo<String>();
        result.setCode(ResultCode.RESULT_SUCCESS.getCode());
        result.setResultDes("保存成功");
        model = new ExtendedModelMap();
        if(null!=adSeatInfo.getWidth()&&null!=adSeatInfo.getHeight()) {
            adSeatInfo.setAdSize(adSeatInfo.getWidth() + "*" + adSeatInfo.getHeight());
        }

        try {
        	/**
        	 * List<AdCrowd> crowds = Lists.newArrayList();
            for (AgePart agePart : AgePart.values()) {
                String preMale = "crowd_1_" + agePart.getId();
                String preFemale = "crowd_2_" + agePart.getId();
                String pMaleId = request.getParameter(preMale + "_name");
                String pFemaleId = request.getParameter(preFemale + "_name");
                AdCrowd male = new AdCrowd();
                AdCrowd female = new AdCrowd();
                if (null != pMaleId) {
                    male.setId(Integer.valueOf(pMaleId));
                }
                male.setSex(1);
                male.setAdSeatId(adSeatInfo.getId());
                male.setAgePart(agePart.getId());
                male.setNums(Integer.valueOf(request.getParameter(preMale + "_value")));
                if (null != pFemaleId) {
                    female.setId(Integer.valueOf(pFemaleId));
                }
                female.setSex(2);
                female.setAdSeatId(adSeatInfo.getId());
                female.setAgePart(agePart.getId());
                female.setNums(Integer.valueOf(request.getParameter(preFemale + "_value")));
                crowds.add(male);
                crowds.add(female);
            }
        	 */
        	if(adSeatInfo.getMultiNum() == 0) {
        		adSeatInfo.setMultiNum(1);
        	}
        	if(adSeatInfo.getAllowMulti() == AllowMultiEnum.NOT_ALLOW.getId()) {
        		adSeatInfo.setMultiNum(1); //0代表不允许同时有多个活动, 设置活动数量为1
        	}
            if (adSeatInfo.getId() != null) {
                //adSeatService.modify(adSeatInfo, crowds);
            	adSeatService.modifyInfo(adSeatInfo);
            } else {
                SysUser user = (SysUser) ShiroUtils.getSessionAttribute(SessionKey.SESSION_LOGIN_USER.toString());
                adSeatService.save(adSeatInfo, user.getId());
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
     * 广告主创建活动时选择具体的广告位信息
     * 注意：广告位信息有活动数量限制
     */
    @RequestMapping(value = "/selectSeat")
    @ResponseBody
    public Model selectSeat(Model model, HttpServletRequest request,
                                     @RequestParam(value = "province", required = false) Long province,
                                     @RequestParam(value = "city", required = false) Long city,
                                     @RequestParam(value = "region", required = false) Long region,
                                     @RequestParam(value = "street", required = false) Long street,
                                     @RequestParam(value = "mediaId", required = false) Integer mediaId,
                                     @RequestParam(value = "startDate", required = false) String startDate,
                                     @RequestParam(value = "endDate", required = false) String endDate) {

        ResultVo result = new ResultVo();
        result.setCode(ResultCode.RESULT_SUCCESS.getCode());
        result.setResultDes("查询成功");
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

        if (mediaId != null) {
            vo.putSearchParam("mediaId", mediaId.toString(), mediaId);
        }
        vo.setSize(Integer.MAX_VALUE);

        //[1] 查询传递的时间段内正在参与活动的广告位id及参与活动数量
        Map<String, Object> searchMap = new HashMap<>();
        searchMap.put("startDate", DateUtil.parseStrDate(startDate, "yyyy-MM-dd"));
        searchMap.put("endDate", DateUtil.parseStrDate(endDate, "yyyy-MM-dd"));
        List<AdSeatCount> adSeatCounts = adActivityService.selectActiveActivityCount(searchMap);
        
        //[2] 查询请求参数对应的广告位信息
        adSeatService.getPageData(vo);
        
        //[3] 筛选去除
        List<?> list = vo.getList();
        Iterator<?> iterator = list.iterator();
        while (iterator.hasNext()) {
        	AdSeatInfoVo infoVo = (AdSeatInfoVo) iterator.next();
        	for (AdSeatCount adSeatCount : adSeatCounts) {
    			if(adSeatCount!= null && adSeatCount.getAdseatId() == infoVo.getId()) {
    				//判断是否要移除
    				if(infoVo.getAllowMulti() == AllowMultiEnum.NOT_ALLOW.getId() && adSeatCount.getCount() >= 1) {
    					//否：不允许同时有多个活动; 当前广告位正在参与活动的数量 大于等于 1
    					iterator.remove();
        				break;
    				}
    				if(infoVo.getAllowMulti() == AllowMultiEnum.ALLOW.getId() && adSeatCount.getCount() >= infoVo.getMultiNum()) {
    					//是: 允许同时有多个活动; 当前广告位正在参与活动的数量 大于等于 最大允许数量
    					iterator.remove();
        				break;
    				}
    			}
    		}
		}
        
        result.setResult(vo.getList());

        model.addAttribute(SysConst.RESULT_KEY, result);
        return model;
    }
    
    /**
     * 超级管理员 查看广告位热力图 页面跳转
     */
    @RequestMapping("/superadmin/thermalMap")
	public String superAdminToThermalMap(Model model) {
		return PageConst.SUPER_ADMIN_THERMAL_MAP;
	}
    
    /**
     * 媒体主 查看广告位热力图 页面跳转
     */
    @RequestMapping("/media/thermalMap")
	public String mediaToThermalMap(Model model) {
		return PageConst.MEDIA_THERMAL_MAP;
	}
    
    /**
     * 查询热力图报表
     */
    @RequiresRoles(value = {"superadmin", "media", "customer"}, logical = Logical.OR)
    @RequestMapping(value = "/getCountGroupByCity")
    @ResponseBody
    public Model getCountGroupByCity(Model model, HttpServletRequest request, Integer activityId, Integer mediaId, Long province, 
    		Long city, Long region, Integer mediaTypeParentId, Integer mediaTypeId) {
    	ResultVo result = new ResultVo();
        result.setCode(ResultCode.RESULT_SUCCESS.getCode());
        result.setResultDes("查询成功");
        model = new ExtendedModelMap();
        
        try {
        	SysUser user = (SysUser) ShiroUtils.getSessionAttribute(SessionKey.SESSION_LOGIN_USER.toString());
        	
        	HeatMapVo heatMapVo = new HeatMapVo();
        	heatMapVo.setActivityId(activityId);
        	heatMapVo.setCity(city);
        	heatMapVo.setMediaId(mediaId);
        	heatMapVo.setProvince(province);
        	heatMapVo.setRegion(region);
        	heatMapVo.setMediaTypeId(mediaTypeId);
        	heatMapVo.setMediaTypeParentId(mediaTypeParentId);
        	List<CountGroupByCityVo> groupByCity = adSeatService.getCountGroupByCity(heatMapVo, user);
        	for (CountGroupByCityVo countGroupByCityVo : groupByCity) {
        		//北京市：110000  天津市：120000  上海市：310000  重庆市：500000  香港: 810000  澳门: 820000
        		if(countGroupByCityVo.getProvince().longValue() == 110000
        				|| countGroupByCityVo.getProvince().longValue() == 120000
        				|| countGroupByCityVo.getProvince().longValue() == 310000
        				|| countGroupByCityVo.getProvince().longValue() == 500000
        				|| countGroupByCityVo.getProvince().longValue() == 810000
        				|| countGroupByCityVo.getProvince().longValue() == 820000) {
        			countGroupByCityVo.setCityName(cityCache.getCityName(countGroupByCityVo.getProvince()));
        		} else {
        			countGroupByCityVo.setCityName(cityCache.getCityName(countGroupByCityVo.getCity()));
        		}
			}
        	result.setCode(ResultCode.RESULT_SUCCESS.getCode());
            result.setResult(groupByCity);
		} catch (Exception e) {
			logger.error(MessageFormat.format("查询热力图报表失败", new Object[] {e}));
        	result.setCode(ResultCode.RESULT_FAILURE.getCode());
        	result.setResultDes("查询热力图报表失败");
		}
        
        model.addAttribute(SysConst.RESULT_KEY, result);
        return model;
    }
    
    /**
     * 超级管理员 查看广告位百度地图 页面跳转
     */
    @RequestMapping("/superadmin/baiduMap")
	public String superAdminToBaiduMap(Model model) {
		return PageConst.SUPER_ADMIN_BAIDU_MAP;
	}
    
    /**
     * 媒体主 查看广告位百度地图 页面跳转
     */
    @RequestMapping("/media/baiduMap")
	public String mediaToBaiduMap(Model model) {
		return PageConst.MEDIA_BAIDU_MAP;
	}
    
    /**
     * 查询百度地图报表
     */
    @RequiresRoles(value = {"superadmin", "media", "customer"}, logical = Logical.OR)
    @RequestMapping(value = "/getAllLonLat")
    @ResponseBody
    public Model getAllLonLat(Model model, HttpServletRequest request, Integer activityId, Integer mediaId, Long province, 
    		Long city, Long region, Integer mediaTypeParentId, Integer mediaTypeId) {
    	ResultVo result = new ResultVo();
        result.setCode(ResultCode.RESULT_SUCCESS.getCode());
        result.setResultDes("查询成功");
        model = new ExtendedModelMap();
        
        try {
        	SysUser user = (SysUser) ShiroUtils.getSessionAttribute(SessionKey.SESSION_LOGIN_USER.toString());
        	
        	HeatMapVo heatMapVo = new HeatMapVo();
        	heatMapVo.setActivityId(activityId);
        	heatMapVo.setCity(city);
        	heatMapVo.setMediaId(mediaId);
        	heatMapVo.setProvince(province);
        	heatMapVo.setRegion(region);
        	heatMapVo.setMediaTypeId(mediaTypeId);
        	heatMapVo.setMediaTypeParentId(mediaTypeParentId);
        	List<AdSeatInfo> adSeatInfos = adSeatService.getAllLonLat(heatMapVo, user);
        	result.setCode(ResultCode.RESULT_SUCCESS.getCode());
            result.setResult(adSeatInfos);
		} catch (Exception e) {
			logger.error(MessageFormat.format("查询百度地图报表失败", new Object[] {}));
        	result.setCode(ResultCode.RESULT_FAILURE.getCode());
        	result.setResultDes("查询百度地图报表失败");
            e.printStackTrace();
		}
        
        model.addAttribute(SysConst.RESULT_KEY, result);
        return model;
    }
    
    /**
     * 生成广告位的二维码
     */
    @RequestMapping(value = "/generateAdCode")
    @ResponseBody
    public Model generateAdCode(Model model, HttpServletRequest request, Integer adSeatId) {
    	ResultVo result = new ResultVo();
        result.setCode(ResultCode.RESULT_SUCCESS.getCode());
        result.setResultDes("操作成功");
        model = new ExtendedModelMap();
        
        try {
        	AdSeatInfo adSeatInfo = adSeatService.getById(adSeatId);
        	AdMedia adMedia = mediaService.getById(adSeatInfo.getMediaId());
        	SysUserVo mediaUser = sysUserService.findUserinfoById(adMedia.getUserId());
        	//生成广告位对应的二维码
    		String adCodeInfo = mediaUser.getPrefix() + UUID.randomUUID(); //二维码存的值（媒体前缀比如media3- 加上UUID随机数）
    		String path = file_upload_path;
    		Calendar date = Calendar.getInstance();
            String pathDir = date.get(Calendar.YEAR)
            + File.separator + (date.get(Calendar.MONTH)+1) + File.separator
            + date.get(Calendar.DAY_OF_MONTH) + File.separator;
    		path = path + (path.endsWith(File.separator)?"":File.separatorChar)+"media"+File.separatorChar+mediaUser.getPrefix().replaceAll("-", "")+File.separatorChar+"qrcode"+File.separatorChar+pathDir+adCodeInfo + ".jpg";
    		QRcodeUtil.encode(adCodeInfo, path);
    		path = path.substring(path.indexOf(":")+1, path.length()).replaceAll("\\\\", "/");
    		path = path.replaceFirst("/opt/", "/");
    		adSeatInfo.setAdCode(adCodeInfo);
    		adSeatInfo.setAdCodeUrl(file_upload_ip+path);
    		
    		//更新
    		adSeatService.modify(adSeatInfo);
		} catch (Exception e) {
			logger.error(MessageFormat.format("生成广告位二维码失败", new Object[] {}));
        	result.setCode(ResultCode.RESULT_FAILURE.getCode());
        	result.setResultDes("生成广告位二维码失败");
        	return model;
		}
        
        model.addAttribute(SysConst.RESULT_KEY, result);
        return model;
    }
    
}
