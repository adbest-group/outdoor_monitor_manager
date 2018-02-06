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

	@RequestMapping(value = "/add")
	public String gotoAddPage() {
		return PageConst.RESOURCE_ADD;
	}

	@RequestMapping(value = "/list")
	public String resourceDetailPage(Model model, HttpServletRequest request,
			@RequestParam(value = "province", required = false) Integer province,
			@RequestParam(value = "city", required = false) Integer city,
			@RequestParam(value = "region", required = false) Integer region,
			@RequestParam(value = "street", required = false) Integer street,
			@RequestParam(value = "mediaId", required = false) Integer mediaId,
			@RequestParam(value = "sex", required = false) Integer sex,
			@RequestParam(value = "agePart", required = false) Integer agePart) {
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
		if (sex != null) {
			vo.putSearchParam("sex", sex.toString(), sex);
		}
		if (agePart != null) {
			vo.putSearchParam("agePart", agePart.toString(), agePart);
		}
		resourceService.getDetailsInfo(vo);
		SearchUtil.putToModel(model, vo);

		return PageConst.RESOURCE_LIST;
	}

	@RequestMapping(value = "/addinfo")
	@ResponseBody
	public Map<String, Object> addInfo(ResourceVo adSeatInfoVo, HttpServletRequest request,
			@RequestParam("province") Integer province, @RequestParam("city") Integer city,
			@RequestParam("region") Integer region, @RequestParam("street") Integer street) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		try {
			adSeatInfoVo.getAdSeatInfo().setProvince(province);
			adSeatInfoVo.getAdSeatInfo().setCity(city);
			adSeatInfoVo.getAdSeatInfo().setRegion(region);
			adSeatInfoVo.getAdSeatInfo().setStreet(street);

			resourceService.insertAdSeatInfo(adSeatInfoVo);
			modelMap.put("success", true);
		} catch (Exception e) {
			modelMap.put("errMsg", "请重新输入!");
			e.printStackTrace();
		}
		return modelMap;
	}

	@RequestMapping(value = "/showDetails")
	public String showDetails(HttpServletRequest request, Model model, @RequestParam(value = "id") String id) {
		AdSeatInfoVo vo = resourceService.getAdSeatInfoById(id);
		if (vo != null) {
			model.addAttribute("vo", vo);
		}
		return PageConst.RESOURCE_DETAILS;
	}

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
}
