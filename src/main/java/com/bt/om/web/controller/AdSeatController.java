package com.bt.om.web.controller;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

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
import com.bt.om.common.SysConst;
import com.bt.om.common.web.PageConst;
import com.bt.om.entity.AdCrowd;
import com.bt.om.entity.AdMedia;
import com.bt.om.entity.AdSeatInfo;
import com.bt.om.entity.AdSeatType;
import com.bt.om.entity.SysUser;
import com.bt.om.entity.vo.AdSeatInfoVo;
import com.bt.om.entity.vo.CountGroupByCityVo;
import com.bt.om.entity.vo.HeatMapVo;
import com.bt.om.entity.vo.ResourceVo;
import com.bt.om.enums.AgePart;
import com.bt.om.enums.ResultCode;
import com.bt.om.enums.SessionKey;
import com.bt.om.security.ShiroUtils;
import com.bt.om.service.IAdSeatService;
import com.bt.om.service.IResourceService;
import com.bt.om.vo.web.ResultVo;
import com.bt.om.vo.web.SearchDataVo;
import com.bt.om.web.BasicController;
import com.bt.om.web.util.SearchUtil;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Lists;
import com.google.common.collect.Table;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

@Controller
@RequestMapping(value = "/adseat")
public class AdSeatController extends BasicController {
	@Autowired
	private CityCache cityCache;
	
    @Autowired
    private IResourceService resourceService;

    @Autowired
    private IAdSeatService adSeatService;

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
    @RequiresRoles("superadmin")
    @RequestMapping(value = "/list")
    public String resourceDetailPage(Model model, HttpServletRequest request,
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

        adSeatService.getPageData(vo);
        SearchUtil.putToModel(model, vo);

        return PageConst.ADSEAT_LIST;
    }

    /**
     * 新增广告位
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
     * 广告位删除
     *
     * @param request
     * @param id
     * @return 提示信息
     */
    @RequestMapping(value = "/verify")
    @ResponseBody
    public Map<String, Object> deleteAdSeat(HttpServletRequest request,
                                            @RequestParam(value = "id", required = false) Integer id) {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        try {
            resourceService.deleteAdSeatById(id);
            modelMap.put("success", true);
        } catch (Exception e) {
            modelMap.put("success", false);
        }
        return modelMap;
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

    @RequiresRoles("superadmin")
    @RequestMapping(value = "/edit")
    public ModelAndView toEdit(Model model, HttpServletRequest request,
                               @RequestParam(value = "id", required = false) Integer id) {

        ModelAndView mv = new ModelAndView(PageConst.ADSEAT_EDIT);
        if (id != null) {
//            AdSeatInfo adSeatInfo = adSeatService.getById(id);
        	AdSeatInfoVo adSeatInfoVo = resourceService.getAdSeatInfoById(id + "");
            List<AdCrowd> crowds = adSeatService.getCrowdsBySeatId(id);
            Table<Integer, Integer, String> crowdTable = HashBasedTable.create();
            if (crowds != null && crowds.size() > 0) {
                for (AdCrowd crowd : crowds) {
                    crowdTable.put(crowd.getSex(), crowd.getAgePart(), "[" + crowd.getId() + "," + crowd.getNums() + "]");
                }
            } else {
                for (AgePart agePart : AgePart.values()) {
                    crowdTable.put(1, agePart.getId(), "[0,0]");
                    crowdTable.put(2, agePart.getId(), "[0,0]");
                }
            }
            mv.addObject("crowdTable", new Gson().fromJson(crowdTable.toString(), JsonObject.class));
//			request.setAttribute("crowdTable",crowdTable);

            mv.getModel().put("adSeatInfo", adSeatInfoVo);
        }

        mv.getModel().put("agePartValues", AgePart.values());

        return mv;
    }

    /**
     * 保存广告位
     **/
    @RequiresRoles("superadmin")
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
            if (adSeatInfo.getId() != null) {
                //adSeatService.modify(adSeatInfo, crowds);
            	adSeatService.modifyInfo(adSeatInfo);
            } else {
                SysUser user = (SysUser) ShiroUtils.getSessionAttribute(SessionKey.SESSION_LOGIN_USER.toString());
                adSeatService.save(adSeatInfo, user.getId());
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

    @RequestMapping(value = "/selectSeat")
    @ResponseBody
    public Model selectSeat(Model model, HttpServletRequest request,
                                     @RequestParam(value = "province", required = false) Long province,
                                     @RequestParam(value = "city", required = false) Long city,
                                     @RequestParam(value = "region", required = false) Long region,
                                     @RequestParam(value = "street", required = false) Long street,
                                     @RequestParam(value = "mediaId", required = false) Integer mediaId) {

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

        adSeatService.getPageData(vo);
        result.setResult(vo.getList());

        model.addAttribute(SysConst.RESULT_KEY, result);
        return model;
    }
    
    /**
     * 查询热力图报表
     */
    @RequiresRoles("customer")
    @RequestMapping(value = "/getCountGroupByCity")
    @ResponseBody
    public Model getCountGroupByCity(Model model, HttpServletRequest request, Integer activityId, Integer mediaId, Long province, Long city, Long region) {
    	ResultVo result = new ResultVo();
        result.setCode(ResultCode.RESULT_SUCCESS.getCode());
        result.setResultDes("查询成功");
        model = new ExtendedModelMap();
        
        try {
        	HeatMapVo heatMapVo = new HeatMapVo();
        	heatMapVo.setActivityId(activityId);
        	heatMapVo.setCity(city);
        	heatMapVo.setMediaId(mediaId);
        	heatMapVo.setProvince(province);
        	heatMapVo.setRegion(region);
        	List<CountGroupByCityVo> groupByCity = adSeatService.getCountGroupByCity(heatMapVo);
        	for (CountGroupByCityVo countGroupByCityVo : groupByCity) {
        		countGroupByCityVo.setCityName(cityCache.getCityName(countGroupByCityVo.getCity()));
			}
        	result.setCode(ResultCode.RESULT_SUCCESS.getCode());
            result.setResult(groupByCity);
		} catch (Exception e) {
			logger.error(MessageFormat.format("查询热力图报表失败", new Object[] {}));
        	result.setCode(ResultCode.RESULT_FAILURE.getCode());
        	result.setResultDes("查询热力图报表失败");
            e.printStackTrace();
		}
        
        model.addAttribute(SysConst.RESULT_KEY, result);
        return model;
    }
    
    /**
     * 查询百度地图报表
     */
    @RequiresRoles("customer")
    @RequestMapping(value = "/getAllLonLat")
    @ResponseBody
    public Model getAllLonLat(Model model, HttpServletRequest request, Integer activityId, Integer mediaId, Long province, Long city, Long region) {
    	ResultVo result = new ResultVo();
        result.setCode(ResultCode.RESULT_SUCCESS.getCode());
        result.setResultDes("查询成功");
        model = new ExtendedModelMap();
        
        try {
        	HeatMapVo heatMapVo = new HeatMapVo();
        	heatMapVo.setActivityId(activityId);
        	heatMapVo.setCity(city);
        	heatMapVo.setMediaId(mediaId);
        	heatMapVo.setProvince(province);
        	heatMapVo.setRegion(region);
        	List<AdSeatInfo> adSeatInfos = adSeatService.getAllLonLat(heatMapVo);
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
}
