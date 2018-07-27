package com.bt.om.web.controller;

import com.bt.om.cache.CityCache;
import com.bt.om.common.SysConst;
import com.bt.om.common.web.PageConst;
import com.bt.om.entity.AdSeatInfo;
import com.bt.om.entity.City;
import com.bt.om.entity.MonitorDailyReport;
import com.bt.om.entity.SysUser;
import com.bt.om.entity.vo.SysUserVo;
import com.bt.om.enums.ResultCode;
import com.bt.om.service.IMonitorDailyReportService;
import com.bt.om.service.ISysUserExecuteService;
import com.bt.om.service.ISysUserService;
import com.bt.om.vo.report.ManageIndexReportVo;
import com.bt.om.vo.web.ResultVo;
import com.bt.om.web.BasicController;
import com.google.common.collect.Maps;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 首页Controller
 */
@Controller
public class IndexController extends BasicController {

	@Autowired
	private IMonitorDailyReportService monitorDailyReportService;

	@Autowired
	private ISysUserService userService;
	
	@Autowired
	private CityCache cityCache;
	/**
	 * 首页
	 * 
	 * @param model
	 * @param tag
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	@RequestMapping(value = "/index", method = { RequestMethod.GET, RequestMethod.POST })
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

		return PageConst.INDEX_PAGE;
	}

	@RequestMapping("/index/topwd")
	public String topwd(Model model) {
		return PageConst.INDEX_PASSWD;
	}

//	@RequestMapping("/area")
//	public String area(Model model) {
//		return PageConst.AREA;
//	}
//
//	@RequestMapping("/plan/condition")
//	public String condition(Model model) {
//		// OttvUser user = getLoginUser();
//		// model.addAttribute("plan",
//		// ottvPlanService.selectPlanConditions(user.getId()));
//		// model.addAttribute("thirdPlan",
//		// ottvPlanService.selectThirdPlanConditions(user.getId()));
//		return PageConst.PLAN_CONDITION;
//	}
//
//	@RequestMapping("/media/condition")
//	public String channel(Model model) {
//		// OttvUser user = getLoginUser();
//		// model.addAttribute("media",
//		// ottvPlanService.selectMediaConditions(user.getId()));
//		return PageConst.MEDIA_CONDITION;
//	}

	/**
	 * 修改密码
	 * 
	 * @param model
	 * @param req
	 * @return
	 */
	@RequestMapping(value = "/index/updatepwd", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody Model updatepwd(Model model, HttpServletRequest req) {

		 ResultVo<SysUser> resultVo = new ResultVo<SysUser>();
		 SysUser user = getLoginUser();
		 if (user.getPassword().equals((new	 Md5Hash(req.getParameter("password"),
		 user.getUsername())).toString())) {
		 if
		 (req.getParameter("password1").equals(req.getParameter("password2")))
		 {
		 SysUser u = new SysUser();
		 u.setId(user.getId());
		 u.setUsername(user.getUsername());
		 u.setPassword(new Md5Hash(req.getParameter("password1"), user.getUsername()).toString());
		 u.setUpdateTime(new Date());
		 try {
		 // 更新密码
		 if (userService.updateByPrimaryKeySelective(u) == 1) {
		 resultVo.setResultDes("修改成功");
		 SecurityUtils.getSubject().logout();
		 
		 } else {
		 resultVo.setCode(ResultCode.RESULT_FAILURE.getCode());
		 resultVo.setResultDes("修改失败");
		 }
		 } catch (Exception ex) {
		 ex.printStackTrace();
		 resultVo.setCode(ResultCode.RESULT_FAILURE.getCode());
		 resultVo.setResultDes("服务忙，请稍后再试");
		 }
		 } else {
		 resultVo.setCode(ResultCode.RESULT_FAILURE.getCode());
		 resultVo.setResultDes("新密码和确认密码不一致");
		 }
		 } else {
		 resultVo.setCode(ResultCode.RESULT_FAILURE.getCode());
		 resultVo.setResultDes("原密码错误");
		 }
		
		 model.addAttribute(SysConst.RESULT_KEY, resultVo);

		return model;
	}

//	@RequestMapping(value = "/index/report", method = { RequestMethod.GET, RequestMethod.POST })
//	public @ResponseBody Model getIndexReportAjaxSearch(Model model,
//			@RequestParam(value = "startDate", required = false) String startDate,
//			@RequestParam(value = "endDate", required = false) String endDate,
//			@RequestParam(value = "mediaFlag", required = false) String mediaFlag,
//			@RequestParam(value = "planId", required = false) Integer planId) {
//
//		// ReportVo[] report = new ReportVo[1];
//		// ResultVo<String> resultVo = new ResultVo<String>();
//		// IndexVo vo = new IndexVo();
//		// String[] arr = new String[12];
//		// List<Map<String, Object>> hourDisplayNum = null;
//		// List<Map<String, Object>> dysDisplayNum = null;
//		// OttvUser user = getLoginUser();
//		//
//		// String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
//		// if (StringUtils.isBlank(startDate) && StringUtils.isBlank(endDate)) {
//		// startDate = today;
//		// endDate = today;
//		// }
//		// for (int i = 0; i < 12; i++) {
//		// arr[i] = "0";
//		// }
//		//
//		// //获取首页折线图报表数据
//		// if (StringUtils.isNotBlank(startDate) &&
//		// StringUtils.isNotBlank(endDate) && startDate.equals(endDate)) {
//		// hourDisplayNum = ottvPlanService.selectHourDisplayNum(user.getId(),
//		// mediaFlag, planId, startDate, endDate);
//		// } else {
//		// dysDisplayNum =
//		// ottvPlanService.selectDaysDisplayNum(user.getId(),mediaFlag, planId,
//		// startDate, endDate);
//		// getAllDayData(dysDisplayNum, startDate, endDate, vo);
//		// }
//		//
//		// if ((hourDisplayNum != null && hourDisplayNum.size() > 0) ||
//		// (StringUtils.isNotBlank(startDate)
//		// && StringUtils.isNotBlank(endDate) && startDate.equals(endDate))) {
//		// ReportVo r = new ReportVo("展示次数", 12);
//		// for (Map<String, Object> map : hourDisplayNum) {
//		// int index = Integer.parseInt(map.get("hour").toString()) / 2;
//		// arr[index] = String
//		// .valueOf((Integer.parseInt(arr[index]) + ((BigDecimal)
//		// (map.get("display_num"))).intValue()));
//		// }
//		// String[] childName = { "0-2点", "2-4点", "4-6点", "6-8点", "8-10点",
//		// "10-12点", "12-14点", "14-16点", "16-18点",
//		// "18-20点", "20-22点", "22-24" };
//		// r.setChildName(childName);
//		// r.setChildValue(arr);
//		// report[0] = r;
//		//
//		// int displaySum = 0;
//		// for (Object perDisplayNum : arr) {
//		// displaySum += Integer.parseInt(perDisplayNum + "");
//		// }
//		// model.addAttribute("displaySum", displaySum);
//		// }
//		//
//		// if (StringUtils.isNotBlank(startDate)
//		// && StringUtils.isNotBlank(endDate) && !startDate.equals(endDate)) {
//		// report[0] = vo.getDisplayReport()[0];
//		// }
//		//
//		// if (dysDisplayNum != null && dysDisplayNum.size() > 0) {
//		// int displaySum = 0;
//		// for (Map<String, Object> displayMap : dysDisplayNum) {
//		// displaySum += ((BigDecimal)
//		// displayMap.get("display_num")).intValue();
//		// }
//		// model.addAttribute("displaySum", displaySum);
//		// }
//		// if (CollectionUtil.isEmpty(dysDisplayNum) &&
//		// CollectionUtil.isEmpty(hourDisplayNum))
//		// model.addAttribute("displaySum", 0);
//		//
//		// //获取首页计划表格信息数据
//		// List<Map<String, Object>> indexPlanList =
//		// ottvPlanService.getIndexPlanList(user.getId(), mediaFlag, planId,
//		// startDate, endDate);
//		//
//		// String disNumJson = GsonUtil.GsonString(report);
//		// model.addAttribute("indexPlanList",
//		// GsonUtil.GsonString(indexPlanList));
//		// model.addAttribute("disNum",disNumJson);
//		// resultVo.setCode(ResultCode.RESULT_SUCCESS.getCode());
//		// model.addAttribute(SysConst.RESULT_KEY, resultVo);
//		return model;
//	}

	@RequestMapping(value = "/index/report")
	@ResponseBody
	public Model getReportData(Model model, AdSeatInfo adSeatInfo, HttpServletRequest request,
						 @RequestParam(value = "activityId", required = false) Integer activityId,
						 @RequestParam(value = "mediaId", required = false) Integer mediaId,
						 @RequestParam(value = "province", required = false) Long province,
						 @RequestParam(value = "city", required = false) Long city,
						 @RequestParam(value = "region", required = false) Long region,
						 @RequestParam(value = "street", required = false) Long street) {
		ResultVo result = new ResultVo();
		result.setCode(ResultCode.RESULT_SUCCESS.getCode());
		result.setResultDes("获取成功");
		model = new ExtendedModelMap();

		ManageIndexReportVo report = new ManageIndexReportVo();
		try {
			LocalDate today = LocalDate.now();
			Map condition = Maps.newHashMap();
			condition.put("activityId",activityId);
			condition.put("mediaId",mediaId);
			condition.put("province",province);
			condition.put("city",city);
			condition.put("region",region);
			condition.put("street",street);
			condition.put("reportDate",today.minusDays(1).toString());
			report.setYesterday(monitorDailyReportService.getSumReport(condition));
			MonitorDailyReport todayReport = new MonitorDailyReport();
			todayReport.setActivityId(activityId);
			todayReport.setMediaId(mediaId);
			todayReport.setProvince(province==null?null:(long)province);
			todayReport.setCity(city==null?null:(long)city);
			todayReport.setRegion(region==null?null:(long)region);
			todayReport.setStreet(street==null?null:(long)street);
			monitorDailyReportService.getTodaySumReport(todayReport);
			report.setToday(todayReport);
			condition.put("reportDate",today.plusDays(1).toString());
			report.setTomorrow(monitorDailyReportService.getSumReport(condition));
			condition.put("reportDate",today.toString());
			condition.put("weekendDate",today.plusDays(7-today.getDayOfWeek().getValue()).toString());
			report.setLeftWeek(monitorDailyReportService.getSumReport(condition));

			result.setResult(report);

		} catch (Exception e) {
			e.printStackTrace();
			result.setCode(ResultCode.RESULT_FAILURE.getCode());
			result.setResultDes("获取失败！");
			model.addAttribute(SysConst.RESULT_KEY, result);
			return model;
		}

		model.addAttribute(SysConst.RESULT_KEY, result);
		return model;

	}
	/**
     * 获取地区二级联动数据
     **/
    @RequestMapping(value = "/api/city")
    @ResponseBody
    public Map getCityData(HttpServletRequest request, HttpServletResponse response, @RequestParam(value = "provinceId", required = false)Long provinceId) {
        Map<Long,String> map = null;
        if (provinceId==null) {
        	List<City> cities = cityCache.getAllProvince(); //获取全部省份
        	map = citiesToMap(cities);
		}else {
			List<City> cities = cityCache.getCity(provinceId);
			map = citiesToMap(cities);
		}
        return map;
    }
    /**
	 * 将City集合转成以名称为Key, Id为Value的Map
	 * @param cities
	 * @return
	 */
	private Map<Long,String> citiesToMap(List<City> cities){
		Map<Long,String> map = new LinkedHashMap<Long,String>();
		for (City province : cities) {
			map.put(province.getId(),province.getName());
		}
		return map;
	}
}
