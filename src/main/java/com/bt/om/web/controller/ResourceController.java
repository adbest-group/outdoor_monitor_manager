package com.bt.om.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bt.om.common.web.PageConst;
import com.bt.om.entity.AdMedia;
import com.bt.om.entity.AdSeatType;
import com.bt.om.entity.vo.AdCrowdVo;
import com.bt.om.entity.vo.AdSeatInfoVo;
import com.bt.om.entity.vo.ResourceVo;
import com.bt.om.enums.ResultCode;
import com.bt.om.service.IResourceService;
import com.bt.om.vo.web.ResultVo;
import com.bt.om.vo.web.SearchDataVo;
import com.bt.om.web.Pagination.Page;
import com.bt.om.web.util.SearchUtil;

@Controller
@RequestMapping(value = "/resource")
public class ResourceController {
	@Autowired
	private IResourceService resourceService;

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
	 * 
	 * @param model
	 * @param request
	 * @param province
	 *            省
	 * @param city
	 *            市
	 * @param region
	 *            区
	 * @param street
	 *            街道
	 * @param mediaId
	 *            媒体id
	 * @param sex
	 *            性别
	 * @param agePart
	 *            年龄段
	 * @return 广告位列表
	 */
	@RequestMapping(value = "/list")
	public String resourceDetailPage(Model model, HttpServletRequest request,
			@RequestParam(value = "province", required = false) Integer province,
			@RequestParam(value = "city", required = false) Integer city,
			@RequestParam(value = "region", required = false) Integer region,
			@RequestParam(value = "street", required = false) Integer street,
			@RequestParam(value = "mediaId", required = false) Integer mediaId) {
		SearchDataVo vo = SearchUtil.getVo();
		if (province != null) {
			vo.putSearchParam("province", province.toString(), province);
		}
		if (city != null) {
			vo.putSearchParam("city", city.toString(), city);
		}
		if (region != null) {
			vo.putSearchParam("region", region.toString(), region);
		}
		if (street != null) {
			vo.putSearchParam("street", street.toString(), street);
		}
		if (mediaId != null) {
			vo.putSearchParam("mediaId", mediaId.toString(), mediaId);
		}
		resourceService.getDetailsInfo(vo);
		SearchUtil.putToModel(model, vo);

		return PageConst.RESOURCE_LIST;
	}

	/**
	 * 新增广告位
	 * 
	 * @param adSeatInfoVo
	 *            封装类
	 * @param request
	 * @param province
	 *            省
	 * @param city
	 *            市
	 * @param region
	 *            区
	 * @param street
	 *            街道
	 * @return 提示信息
	 */
	@RequestMapping(value = "/addinfo")
	@ResponseBody
	public Map<String, Object> addInfo(ResourceVo adSeatInfoVo, HttpServletRequest request,
			@RequestParam(value = "province", required = false) Integer province,
			@RequestParam(value = "city", required = false) Integer city,
			@RequestParam(value = "region", required = false) Integer region,
			@RequestParam(value = "street", required = false) Integer street) {
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

	@RequestMapping(value = "/edit")
	public String adSeatInfoEdit(HttpServletRequest request, Model model, @RequestParam("id") String seatId) {
		AdSeatInfoVo vo = resourceService.getAdSeatInfoById(seatId);
		List<AdMedia> mediaList = resourceService.getAll();
		List<AdSeatType> adSeatTypesList = resourceService.getSeatTypeAll();
		List<AdCrowdVo> adCrowdVoList = resourceService.getAgePartListByAdSeatId(seatId);
		model.addAttribute("mediaList", mediaList);
		model.addAttribute("adSeatTypesList", adSeatTypesList);
		model.addAttribute("vo", vo);
		model.addAttribute("adCrowdVoList", adCrowdVoList);
		return PageConst.RESOURCE_EDIT;
	}
	
	@RequestMapping(value = "/updateAdSeatInfo")
	public Map<String, Object> updateAdseatInfo(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		return modelMap;
	}
}
