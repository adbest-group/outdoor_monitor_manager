package com.bt.om.web.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

import com.adtime.common.lang.StringUtil;
import com.alibaba.druid.util.StringUtils;
import com.bt.om.cache.CityCache;
import com.bt.om.common.DateUtil;
import com.bt.om.common.SysConst;
import com.bt.om.entity.AdActivity;
import com.bt.om.entity.AdApp;
import com.bt.om.entity.AdMediaType;
import com.bt.om.entity.AdMonitorTask;
import com.bt.om.entity.AdMonitorTaskFeedback;
import com.bt.om.entity.City;
import com.bt.om.entity.SysUser;
import com.bt.om.entity.vo.AdActivityAdseatTaskVo;
import com.bt.om.entity.vo.AdMediaTypeVo;
import com.bt.om.entity.vo.AdMonitorTaskVo;
import com.bt.om.enums.MapStandardEnum;
import com.bt.om.enums.MonitorTaskStatus;
import com.bt.om.enums.MonitorTaskType;
import com.bt.om.enums.ResultCode;
import com.bt.om.enums.SessionKey;
import com.bt.om.enums.TaskProblemStatus;
import com.bt.om.enums.UserTypeEnum;
import com.bt.om.mapper.AdMediaMapper;
import com.bt.om.security.ShiroUtils;
import com.bt.om.service.IAdActivityService;
import com.bt.om.service.IAdMediaTypeService;
import com.bt.om.service.IAdMonitorTaskService;
import com.bt.om.service.IAdSeatService;
import com.bt.om.service.IAdUserMessageService;
import com.bt.om.service.IAppService;
import com.bt.om.service.IHistoryAdActivityService;
import com.bt.om.service.IHistoryAdMonitorTaskService;
import com.bt.om.service.IMediaService;
import com.bt.om.service.ISysUserService;
import com.bt.om.util.ConfigUtil;
import com.bt.om.util.ExcelTool;
import com.bt.om.util.pdf.AlternatingBackground;
import com.bt.om.vo.web.ResultVo;
import com.bt.om.web.BasicController;
import com.bt.om.web.util.HistoryPDFHelper;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPTableEvent;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * Created by jiayong.mao on 2018/4/4.
 */
@Controller
@RequestMapping(value = "/history/excel")
public class HistoryExcelController extends BasicController {
	private static final String IMPORT_SUCC = "导入成功";
	private static final String IMPORT_FAIL = "导入失败";
	
	@Autowired
    private IAdSeatService adSeatService;
	
	@Autowired
	private ISysUserService sysUserService;
	
	@Autowired
	private CityCache cityCache;
	
	@Autowired
	private AdMediaMapper adMediaMapper;
	
	@Autowired
	private IAdMediaTypeService adMediaTypeService;
	
	@Autowired
	private IAdActivityService adActivityService;
	
	@Autowired
	private IAdMonitorTaskService adMonitorTaskService;
	
	@Autowired
	private IAppService adappService;
	
	@Autowired
	private IMediaService mediaService;
	
	@Autowired
	private IAdUserMessageService adUserMessageService;
	
	@Autowired
	private IHistoryAdActivityService historyAdActivityService;
	
	@Autowired
	private IHistoryAdMonitorTaskService historyAdMonitorTaskService;
	
	private static final Logger logger = Logger.getLogger(HistoryExcelController.class);
    private String file_upload_path = ConfigUtil.getString("file.upload.path");
    private String file_upload_ip = ConfigUtil.getString("file.upload.ip");
	
	/**
	 * 具体活动的pdf导出
	 * @throws ParseException 
	 */
	@RequiresRoles(value = {"superadmin", "activityadmin", "depactivityadmin", "admin" , "customer"}, logical = Logical.OR)
    @RequestMapping(value = "/exportAdMediaPdf")
	@ResponseBody
	public Model exportPdf(Model model, HttpServletRequest request, HttpServletResponse response,
            @RequestParam(value = "activityId", required = false) Integer activityId,
            @RequestParam(value = "taskreport", required = false) String taskreport,
            @RequestParam(value = "brandName", required = false) String brandName,
            @RequestParam(value = "titleName", required = false) String titleName) throws ParseException {
		System.setProperty("sun.jnu.encoding", "utf-8");
		SysUser userObj = (SysUser) ShiroUtils.getSessionAttribute(SessionKey.SESSION_LOGIN_USER.toString());
    	String tableName = (String) ShiroUtils.getSessionAttribute("tableName");

    	//相关返回结果
		ResultVo result = new ResultVo();
        result.setCode(ResultCode.RESULT_SUCCESS.getCode());
        result.setResultDes("查询成功");
        model = new ExtendedModelMap();
		Date now = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Map<String,Object> searchMap = new HashMap<>();
		String type = null;
		
		//查询媒体类型
		List<AdMediaType> allAdMediaType = adMediaTypeService.getAll();
		Map<Integer, String> mediaTypeMap = new HashMap<>();
		for (AdMediaType adMediaType : allAdMediaType) {
			mediaTypeMap.put(adMediaType.getId(), adMediaType.getName());
		}
		
		Date reportTime = null;
		Integer taskType = null;
		if(taskreport!=null) {
			String reportTimeStr = taskreport.substring(0, 10); //报告时间
			reportTime = sdf.parse(reportTimeStr);
			type = taskreport.substring(10,taskreport.length()-2);
			if (type.contains("上刊监测")) {
                taskType = MonitorTaskType.UP_MONITOR.getId();
            } else if (type.contains("上刊")) {
                taskType = MonitorTaskType.UP_TASK.getId();
            }else if (type.contains("投放期间监测")) {
                taskType = MonitorTaskType.DURATION_MONITOR.getId();
            } else if (type.contains("下刊监测")) {
                taskType = MonitorTaskType.DOWNMONITOR.getId();
            } else if (type.contains("追加监测")) {
                taskType = MonitorTaskType.ZHUIJIA_MONITOR.getId();
            }
			
			searchMap.put("activityId", activityId);
			searchMap.put("reportTime", reportTime);
			searchMap.put("taskType", taskType);
			searchMap.put("tableName", tableName);
		}
		//导出文件相关
		AdActivity adActivity = historyAdActivityService.getById(searchMap);

		Map<Integer, List<String>> map = new HashMap<>();
 		List<AdMonitorTaskVo> taskVos = historyAdMonitorTaskService.selectMonitorTaskIdsByActicityId(searchMap);
        Map<Integer, Integer> taskIds = new HashMap<>();
        for (AdMonitorTaskVo adMonitorTaskVo : taskVos) {
            taskIds.put(adMonitorTaskVo.getId(), adMonitorTaskVo.getActivityAdseatId());
        }
        Rectangle pageSize = new Rectangle(1920, 1080);
        //广告主id
        Integer userId = adActivity.getUserId();//广告主id
        SysUser sysUser = sysUserService.getUserAppType(userId);
        Integer appId = sysUser.getAppTypeId();
        AdApp adapp = adappService.selectById(appId); 
        
        try {
        	//指定文件保存位置
            String path = file_upload_path;//request.getSession().getServletContext().getRealPath("/");
            //pdf存在路径，已活动ID为文件夹
            String target = (path.endsWith(File.separator) ? "" : File.separatorChar) + activityId.toString() + File.separatorChar + "pdf" + File.separatorChar;
            path = path + target;
            result.setCode(ResultCode.RESULT_SUCCESS.getCode());
            //result.setResult("/static/pdf/" + fileName);
            //返回pdf存储路径
            String _path = path.substring(path.indexOf(":") + 1, path.length()).replaceAll("\\\\", "/");
            _path = _path.replaceFirst("/opt/", "/");
            result.setResult(file_upload_ip + _path);
            //拼接title
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append(taskreport.substring(10));

            List<AdActivityAdseatTaskVo> vos = historyAdActivityService.newSelectAdActivityAdseatTaskReport(searchMap);
            Map<String, Map<Integer, List<String>>> cityMap = new HashMap<>();
            Map<String, String> cityFileNameMap = new HashMap<>();
            for (AdActivityAdseatTaskVo vo : vos) {
                Map<Integer, List<String>> _tempMap = new HashMap<>();
                Long provinceCode = vo.getInfo_province();
                Long cityCode = vo.getInfo_city();
                String provinceName = cityCache.getCityName(provinceCode);
                String cityName = cityCache.getCityName(cityCode);
                List<String> list = new ArrayList<>();
                //活动名称 0
                list.add(adActivity.getActivityName());
                //广告位名称 1
                list.add(vo.getInfo_name());
                //省 2
                list.add(provinceName);
                //市 3
                list.add(cityName==null?provinceName:cityName);
                //主要路段 4
                list.add(vo.getInfo_road());
                //详细位置 5
                list.add(vo.getInfo_location());
                //媒体方广告位编号 6
                list.add(vo.getInfo_memo());
                /**开始监测时间 7*/
                list.add(DateUtil.dateFormate(vo.getMonitorStart(), "yyyy-MM-dd"));
                /**结束监测时间 8*/
                list.add(DateUtil.dateFormate(vo.getMonitorEnd(), "yyyy-MM-dd"));
                Integer proStatus = vo.getProblemStatus();
                String problemStatus = null;
                if (proStatus.equals(TaskProblemStatus.CLOSED.getId())) {
                    problemStatus = TaskProblemStatus.CLOSED.getText();
                } else if (proStatus.equals(TaskProblemStatus.FIXED.getId())) {
                    problemStatus = TaskProblemStatus.FIXED.getText();
                } else if (proStatus.equals(TaskProblemStatus.NO_PROBLEM.getId())) {
                    problemStatus = TaskProblemStatus.NO_PROBLEM.getText();
                } else if (proStatus.equals(TaskProblemStatus.UNMONITOR.getId())) {
                    problemStatus = TaskProblemStatus.UNMONITOR.getText();
                } else if (proStatus.equals(TaskProblemStatus.PROBLEM.getId())) {
                    problemStatus = TaskProblemStatus.PROBLEM.getText();
                }
                list.add(problemStatus);
                //尺寸 10
                list.add(vo.getInfo_adSize());
                //面积 11
                list.add(vo.getInfo_adArea());
                //面数12
                list.add(vo.getInfo_adNum() + "");
                //经度 13
                list.add(vo.getInfo_lon() + "");
                //纬度 14
                list.add(vo.getInfo_lat() + "");
                if (vo.getInfo_mapStandard() != null) {
                    //地图标准 15
                    list.add(MapStandardEnum.getText(vo.getInfo_mapStandard()));
                } else {
                    list.add(null);
                }
                //联系人姓名 16
                list.add(vo.getInfo_contactName());
                //联系人电话 17
                list.add(vo.getInfo_contactCell());
                //媒体大类 18
                list.add(mediaTypeMap.get(vo.getInfo_mediaTypeParentId()));
                //媒体小类 19
                list.add(mediaTypeMap.get(vo.getInfo_mediaTypeId()));
                //媒体主20
                list.add(vo.getMediaName());
                //任务类型21
                list.add(type);
                //22 审核人员
                list.add(vo.getRealname());
                Integer taskStatus = vo.getStatus();
                String status = null;
                if (taskStatus.equals(MonitorTaskStatus.UNASSIGN.getId())) {
                    status = MonitorTaskStatus.UNASSIGN.getText();
                } else if (taskStatus.equals(MonitorTaskStatus.TO_CARRY_OUT.getId())) {
                    status = MonitorTaskStatus.TO_CARRY_OUT.getText();
                } else if (taskStatus.equals(MonitorTaskStatus.UNVERIFY.getId())) {
                    status = MonitorTaskStatus.UNVERIFY.getText();
                } else if (taskStatus.equals(MonitorTaskStatus.VERIFIED.getId())) {
                    status = MonitorTaskStatus.VERIFIED.getText();
                } else if (taskStatus.equals(MonitorTaskStatus.VERIFY_FAILURE.getId())) {
                    status = MonitorTaskStatus.VERIFY_FAILURE.getText();
                } else if (taskStatus.equals(MonitorTaskStatus.UN_FINISHED.getId())) {
                    status = MonitorTaskStatus.UN_FINISHED.getText();
                } else if (taskStatus.equals(MonitorTaskStatus.UN_ACTIVE.getId())) {
                    status = MonitorTaskStatus.UN_ACTIVE.getText();
                } else if (taskStatus.equals(MonitorTaskStatus.CAN_GRAB.getId())) {
                    status = MonitorTaskStatus.CAN_GRAB.getText();
                } else if (taskStatus.equals(MonitorTaskStatus.VERIFY.getId())) {
                    status = MonitorTaskStatus.VERIFY.getText();
                }
                //23 任务状态
                list.add(status);
                //24 任务执行人
                list.add(vo.getExe_realname());
                //25 活动示例图
                list.add(vo.getSamplePicUrl());
                if (taskreport != null) {
                    String reportTimeStr = taskreport.substring(0, 10);
                    reportTime = sdf.parse(reportTimeStr);
                    //26 报告时间
                    list.add(reportTimeStr);
                }
                //27 品牌名
                list.add(brandName);
                //28 广告位点位图
                list.add(vo.getMapPic());
                //ad_activity_adseat的id
                map.put(vo.getId(), list);

                //将数据根据城市进行区分
                String key = provinceCode.toString() + (cityCode == null ? "" : cityCode.toString());
                if (cityMap.containsKey(key)) {
                    cityMap.get(key).put(vo.getId(), list);
                } else {
                    _tempMap.put(vo.getId(), list);
                    cityMap.put(key, _tempMap);
                }
                //如果当前城市key不存在，则添加当前城市的pdf文件名记录
                if (!cityFileNameMap.containsKey(key)) {
                    String reportTimeStr = taskreport.substring(0, 10);
                    reportTimeStr = reportTimeStr.replaceAll("-", "");
                    cityFileNameMap.put(key, MessageFormat.format("{0}{1}{2}_{3}报告.pdf", cityName==null?provinceName:cityName, reportTimeStr, activityId,type));
                }
            }

            if (cityFileNameMap.size() >0) {
                Map<String,Object> files=new HashMap<>();
                List<String> listFiles=new ArrayList<>();
                for (Map.Entry<String,String> entry:cityFileNameMap.entrySet()){
                    listFiles.add(entry.getValue());
                }
                files.put("files",listFiles);
                files.put("domainPath",file_upload_ip + _path);
                result.setResult(files);
            }

            //【3】生成pdf图片页
            List<Integer> ids = new ArrayList<>();
            List<Integer> activityAdseatIds = new ArrayList<>();
            //查询每个广告位最新的一条监测任务
            List<AdMonitorTask> tasks = historyAdMonitorTaskService.newSelectLatestMonitorTaskIds(searchMap);
            for (AdMonitorTask task : tasks) {
                //登录方是广告主且任务当前状态是已审核  或者是群邑
                if ((userObj.getUsertype().equals(UserTypeEnum.CUSTOMER.getId()) && task.getStatus().equals(MonitorTaskStatus.VERIFIED.getId())) || (userObj.getUsertype() != UserTypeEnum.CUSTOMER.getId())) {
                    //ad_monitor_task的id
                    ids.add(task.getId());
                    //ad_activity_adseat的id
                    activityAdseatIds.add(task.getActivityAdseatId());
                }
            }
            if (ids.size() > 0) {
                List<AdMonitorTaskFeedback> taskFeedbacks = historyAdMonitorTaskService.selectByActivity(tableName,ids);
                HistoryPDFHelper pdfHelper = new HistoryPDFHelper();

                if (!pdfHelper.buildCityReport(request, taskFeedbacks, path, ids, activityAdseatIds, taskIds, cityFileNameMap, cityMap, adapp, titleName + stringBuffer.toString(),taskType)) {
                    result.setCode(ResultCode.RESULT_FAILURE.getCode());
                    result.setResultDes("批量导出pdf失败");
                } else {
                    result.setCode(ResultCode.RESULT_SUCCESS.getCode());
                }
//                if (!pdfHelper.buildReport(request, taskFeedbacks, path, ids, activityAdseatIds, taskIds, map, adapp, titleName + stringBuffer.toString())) {
//                    result.setCode(ResultCode.RESULT_FAILURE.getCode());
//                    result.setResultDes("批量导出pdf失败");
//                } else {
//                    result.setCode(ResultCode.RESULT_SUCCESS.getCode());
//                }
            }
            else {
                result.setCode(ResultCode.RESULT_FAILURE.getCode());
                result.setResultDes(MessageFormat.format("未找到{0}数据",stringBuffer.toString()));
            }
        } catch (Exception e) {
            logger.error(MessageFormat.format("批量导出pdf失败", new Object[]{}));
            result.setCode(ResultCode.RESULT_FAILURE.getCode());
            result.setResultDes(e.getMessage());
            e.printStackTrace();
        }

        model.addAttribute(SysConst.RESULT_KEY, result);
        return model;
    }
	
	/**
	 * 具体活动的广告位excel导出报表
	 * @throws ParseException 
	 */
	@RequiresRoles(value = {"superadmin", "activityadmin", "depactivityadmin", "admin" , "customer"}, logical = Logical.OR)
    @RequestMapping(value = "/exportAdMediaInfo")
	@ResponseBody
	public Model exportAdMediaInfo(Model model, HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "activityId", required = false) Integer activityId,
			@RequestParam(value = "taskreport", required = false) String taskreport) throws ParseException {
		System.setProperty("sun.jnu.encoding", "utf-8");
		SysUser userObj = (SysUser) ShiroUtils.getSessionAttribute(SessionKey.SESSION_LOGIN_USER.toString());
		String tableName = (String) ShiroUtils.getSessionAttribute("tableName");
		//相关返回结果
		ResultVo result = new ResultVo();
        result.setCode(ResultCode.RESULT_SUCCESS.getCode());
        result.setResultDes("查询成功");
        model = new ExtendedModelMap();
		Date now = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Map<String,Object> searchMap = new HashMap<>();
		String type = null;
		
		//查询媒体类型
		List<AdMediaType> allAdMediaType = adMediaTypeService.getAll();
		Map<Integer, String> mediaTypeMap = new HashMap<>();
		for (AdMediaType adMediaType : allAdMediaType) {
			mediaTypeMap.put(adMediaType.getId(), adMediaType.getName());
		}
		
		if(taskreport!=null) {
			String reportTimeStr = taskreport.substring(0, 10); //报告时间
			Date reportTime = sdf.parse(reportTimeStr);
			Integer taskType = null;
			type = taskreport.substring(10,taskreport.length()-2);
			if(type.contains("上刊任务")) {
				taskType = MonitorTaskType.UP_TASK.getId();
			} else if(type.contains("上刊监测")) {
				taskType = MonitorTaskType.UP_MONITOR.getId();
			} else if(type.contains("投放期间监测")) {
				taskType = MonitorTaskType.DURATION_MONITOR.getId();
			} else if(type.contains("下刊监测")) {
				taskType = MonitorTaskType.DOWNMONITOR.getId();
			} else if(type.contains("追加监测")) {
				taskType = MonitorTaskType.ZHUIJIA_MONITOR.getId();
			}
			
			searchMap.put("tableName", tableName);
			searchMap.put("activityId", activityId);
			searchMap.put("reportTime", reportTime);
			searchMap.put("taskType", taskType);
		}
		//导出文件相关
		AdActivity adActivity = historyAdActivityService.getById(searchMap); //活动
//		AdCustomerType customerType = adCustomerTypeService.getById(adActivity.getCustomerTypeId()); //客户类型
// 		final String fileName = adActivity.getActivityName() + "-广告位导出结果"+ ".xls"; //导出文件名
 		final String fileName = adActivity.getId() + "-" + System.currentTimeMillis() + ".xls"; //导出文件名
        List<List<String>> listString = new ArrayList<>();
        
		Integer userId = adActivity.getUserId();//广告主id
        SysUser sysUser = sysUserService.getUserAppType(userId);
        
        StringBuffer url = request.getRequestURL();  
        String prefix = url.delete(url.length() - request.getRequestURI().length(), url.length()).append("/").toString();  
        prefix = prefix.substring(0, prefix.length() - 1);
        
        try {
//        	List<AdActivityAdseatTaskVo> vos = adActivityService.selectAdActivityAdseatTaskReport(activityId);
        	
			List<Integer> ids = new ArrayList<>(); //任务id集合
			List<AdMonitorTaskFeedback> taskFeedbacks = new ArrayList<>(); //任务反馈信息集合
			//查询每个广告位最新的一条监测任务
			List<AdMonitorTask> tasks = historyAdMonitorTaskService.newSelectLatestMonitorTaskIds(searchMap);
	        for (AdMonitorTask task : tasks) {
	        	if((userObj.getUsertype()==UserTypeEnum.CUSTOMER.getId() && task.getStatus()==MonitorTaskStatus.VERIFIED.getId()) || (userObj.getUsertype()!=UserTypeEnum.CUSTOMER.getId())) {
	        		//登录方是广告主且任务当前状态是已审核  或者是群邑
	        		ids.add(task.getId()); //ad_monitor_task的id
	        	}
			}
			//图片地址
	        if(ids.size() > 0) {
	        	taskFeedbacks = historyAdMonitorTaskService.selectByActivity(tableName,ids);
	        }
        	
        	List<AdActivityAdseatTaskVo> vos = historyAdActivityService.newSelectAdActivityAdseatTaskReport(searchMap);
        	for (AdActivityAdseatTaskVo vo : vos) {
        		if((userObj.getUsertype()==UserTypeEnum.CUSTOMER.getId() && vo.getStatus()==MonitorTaskStatus.VERIFIED.getId()) || (userObj.getUsertype()!=UserTypeEnum.CUSTOMER.getId())) {
        			//登录方是广告主且任务当前状态是已审核  或者是群邑
					List<String> list = new ArrayList<>();
					list.add(adActivity.getActivityName()); //活动名称 
					list.add(sysUser.getRealname());//广告主名称
					list.add(type);//任务类型
					list.add(taskreport.substring(0, 10));//报告时间
	//				list.add(customerType.getName()); //客户类型
					list.add(vo.getInfo_name()); //广告位名称
					list.add(vo.getMediaName()); //媒体主
					list.add(vo.getInfo_memo()); //广告位编号
					String startDate = DateUtil.dateFormate(vo.getMonitorStart(), "yyyy-MM-dd");
					String endDate = DateUtil.dateFormate(vo.getMonitorEnd(), "yyyy-MM-dd");
					list.add(startDate+" - "+endDate);//投放日期
					list.add(vo.getInfo_adSize() != null ? vo.getInfo_adSize() : "暂无"); //广告位尺寸
					String bigMedia = mediaTypeMap.get(vo.getInfo_mediaTypeParentId());
					String smallMedia = mediaTypeMap.get(vo.getInfo_mediaTypeId());
					list.add(bigMedia+"-"+smallMedia);//媒体类型
					String province = cityCache.getCityName(vo.getInfo_province());
					String city = cityCache.getCityName(vo.getInfo_city());
					String road = vo.getInfo_road();
					String location = vo.getInfo_location();
					list.add(province+city+road+location);//地理位置
					
					list.add(vo.getInfo_lon() + ""); //经度
					list.add(vo.getInfo_lat() + ""); //纬度
					
					String picUrl1 = null;
					String picUrl2 = null;
					String picUrl3 = null;
					String picUrl4 = null;
		        	for (AdMonitorTaskFeedback feedback : taskFeedbacks) {
		        		if(feedback.getMonitorTaskId().equals(vo.getMonitorTaskId())) {
				        	if(!StringUtils.isEmpty(feedback.getPicUrl1())){
				        		picUrl1 = prefix + feedback.getPicUrl1();
				        	}
				        	if(!StringUtils.isEmpty(feedback.getPicUrl2())) {
				        		picUrl2 = prefix + feedback.getPicUrl2();
				        	}
				        	if(!StringUtils.isEmpty(feedback.getPicUrl3())) {
				        		picUrl3 = prefix + feedback.getPicUrl3();
				        	}
				        	if(!StringUtils.isEmpty(feedback.getPicUrl4())) {
				        		picUrl4 = prefix + feedback.getPicUrl4();
				        	}
				        	break;
		        		}
		        	}
		        	list.add(picUrl1);
		        	list.add(picUrl2);
		        	list.add(picUrl3);
		        	list.add(picUrl4);
					
//					list.add(cityCache.getCityName(vo.getInfo_province())); //省
//					list.add(cityCache.getCityName(vo.getInfo_city())); //市
//	//				list.add(cityCache.getCityName(vo.getInfo_region())); //区（县）
////					list.add(cityCache.getCityName(vo.getInfo_street())); // 主要路段
//					list.add(vo.getInfo_road());//主要路段
//					list.add(vo.getInfo_location()); //详细位置
//					list.add(mediaTypeMap.get(vo.getInfo_mediaTypeParentId())); //媒体大类
//					list.add(mediaTypeMap.get(vo.getInfo_mediaTypeId())); //媒体小类
//					
//					list.add((vo.getInfo_adNum() + "") != null ? (vo.getInfo_adNum() + "") : "暂无");//面数
//					list.add(vo.getInfo_adArea() != null ? vo.getInfo_adArea() : "暂无"); //面积
//					list.add(vo.getInfo_lon() != null ? (vo.getInfo_lon() + "") : "暂无"); //经度
//					list.add(vo.getInfo_lat() != null ? (vo.getInfo_lat() + "") : "暂无"); //纬度
//					list.add(vo.getInfo_mapStandard() != null ? MapStandardEnum.getText(vo.getInfo_mapStandard()) : "暂无"); //地图标准（如百度，谷歌，高德）
//					list.add(vo.getInfo_contactName() != null ? vo.getInfo_contactName() : "暂无"); //联系人姓名
//					list.add(vo.getInfo_contactCell() != null ? vo.getInfo_contactCell() : "暂无"); //联系人电话
//					if(vo.getInfo_adcode_flag() != null) {	//二维码状态
//						if(vo.getInfo_adcode_flag()==1) {
//							list.add("二维码已贴");
//						}else {
//							list.add("二维码未贴");
//						}
//					}
//					list.add(vo.getInfo_uniqueKey()); //唯一标识
//					list.add(DateUtil.dateFormate(vo.getMonitorStart(), "yyyy-MM-dd")); //开始监测时间
//					list.add(DateUtil.dateFormate(vo.getMonitorEnd(), "yyyy-MM-dd")); //结束监测时间
//					String status = AdMediaInfoStatus.WATCHING.getText(); //当前状态
//					if(vo.getProblem_count() > 0) {
//						status = AdMediaInfoStatus.HAS_PROBLEM.getText();
//					}
//					if(vo.getMonitorStart().getTime() > now.getTime()) {
//						status = AdMediaInfoStatus.NOT_BEGIN.getText();
//					}
//					if(vo.getMonitorEnd().getTime() < now.getTime()) {
//						status = AdMediaInfoStatus.FINISHED.getText();
//		        	}
					
					listString.add(list);
				}
    		}
        	
        	String[] titleArray = { "活动名称" , "广告主" , "任务类型" , "报告时间" , "点位名称" , "媒体主" , "广告位编号" , "投放日期" , "广告位尺寸", "媒体类型", "地理位置",
        			"经度", "纬度", "图片1" ,"图片2","图片3","图片4"};
            ExcelTool<List<String>> excelTool = new ExcelTool<List<String>>("importResult");
            String path = request.getSession().getServletContext().getRealPath("/");
    		path = path + (path.endsWith(File.separator)?"":File.separatorChar)+"static"+File.separatorChar+"excel"+File.separatorChar+fileName;
    		
    		excelTool.generateExcel(listString, titleArray, path);
    		
    		
            result.setCode(ResultCode.RESULT_SUCCESS.getCode());
            result.setResult("/static/excel/" + fileName);
		} catch (Exception e) {
			logger.error(MessageFormat.format("批量导出失败", new Object[] {}));
        	result.setCode(ResultCode.RESULT_FAILURE.getCode());
        	result.setResultDes(e.getMessage());
            e.printStackTrace();
		}
        
         model.addAttribute(SysConst.RESULT_KEY, result);
        return model;
	}
	
	/**
	 * 设置以媒体大类名称, 媒体小类名称为Key, AdMediaTypeVo为Value的集合
	 * @param adMediaTypeVos
	 * @return
	 */
	private Table<String, String, AdMediaTypeVo> getTable(List<AdMediaTypeVo> adMediaTypeVos) {
		Table<String, String, AdMediaTypeVo> table = HashBasedTable.create();
		for (AdMediaTypeVo adMediaTypeVo : adMediaTypeVos) {
			table.put(adMediaTypeVo.getParentName(), adMediaTypeVo.getName(), adMediaTypeVo);
		}
		return table;
	}
	
	/**
	 * 将adMediaTypes集合转成以名称为Key, AdMediaType为Value的Map
	 * @param adMediaTypes
	 * @return
	 */
	private Map<String, AdMediaType> typesToMap(List<AdMediaType> adMediaTypes){
		Map<String, AdMediaType> map = new HashMap<>();
		for (AdMediaType adMediaType : adMediaTypes) {
			map.put(adMediaType.getName(), adMediaType);
		}
		return map;
	}
	
	/**
	 * 将City集合转成以名称为Key, Id为Value的Map
	 * @param cities
	 * @return
	 */
	private Map<String, Long> citiesToMap(List<City> cities){
		Map<String, Long> map = new HashMap<String, Long>();
		for (City province : cities) {
			map.put(province.getName(), province.getId());
		}
		return map;
	}
	
	/**
	 * 对象转String
	 * @param listobj
	 * @return
	 */
	private List<List<String>> objToString(List<List<Object>> listobj){
		List<List<String>> listString = new ArrayList<>();
		for (List<Object> list : listobj) {
			if(listobj.indexOf(list) != 0) {
				List<String> line = new ArrayList<>();
				for (Object object : list) {
					line.add(String.valueOf(object));
				}
				listString.add(line);
			}
		}
		return listString;
	}
	
	/**
	 * 每个广告位对应的图片
	 * @throws IOException
	 * @throws DocumentException 
	 */
	private void createPage(Document document, List<String> list, AdMonitorTaskFeedback feedback, HttpServletRequest request) 
			throws IOException, DocumentException {
		//设置字体  
//	    BaseFont bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
//	    Font fontChinese = new Font(bfChinese, 20, Font.NORMAL);// 创建字体，设置family，size，style,还可以设置color 
//	    Font titleChinese = new Font(bfChinese, 20, Font.BOLD);  
//	    Font BoldChinese = new Font(bfChinese, 20, Font.BOLD);  
//	    Font subBoldFontChinese = new Font(bfChinese, 20, Font.BOLD); 
//	    
//		Paragraph pt = new Paragraph(list.get(1), fontChinese);//设置字体样式pt.setAlignment(1);//设置文字居中 0靠左   1，居中     2，靠右
//		pt.setAlignment(1);
//		document.add(pt);
		
		String path = request.getSession().getServletContext().getRealPath("/");
		if(!StringUtils.isEmpty(feedback.getPicUrl1())) {
			Image image1 = Image.getInstance(feedback.getPicUrl1());
			image1.setAlignment(Image.ALIGN_CENTER);
//			image1.scalePercent(40);//依照比例缩放
			image1.scaleAbsolute(330,262);//控制图片大小
			image1.setAbsolutePosition(950,530);//控制图片位置
			document.add(image1);
		}
		
		if(!StringUtils.isEmpty(feedback.getPicUrl2())) {
			Image image2 = Image.getInstance(feedback.getPicUrl2());
			image2.setAlignment(Image.ALIGN_CENTER);
//			image2.scalePercent(40);//依照比例缩放
			image2.scaleAbsolute(330,262);//控制图片大小
			image2.setAbsolutePosition(1350,530);//控制图片位置
			document.add(image2);
		}
		
		if(!StringUtils.isEmpty(feedback.getPicUrl3())) {
			Image image3 = Image.getInstance(feedback.getPicUrl3());
			image3.setAlignment(Image.ALIGN_CENTER);
//			image3.scalePercent(40);//依照比例缩放
			image3.scaleAbsolute(330,262);//控制图片大小
			image3.setAbsolutePosition(950,230);//控制图片位置
			document.add(image3);
		}
		
		if(!StringUtils.isEmpty(feedback.getPicUrl4())) {
			Image image4 = Image.getInstance(feedback.getPicUrl4());
			image4.setAlignment(Image.ALIGN_CENTER);
//			image4.scalePercent(40);//依照比例缩放
			image4.scaleAbsolute(330,262);//控制图片大小
			image4.setAbsolutePosition(1350,230);//控制图片位置
			document.add(image4);
		}
	}
	
	/**
	 * 生成表格
	 * @return
	 * @throws DocumentException
	 * @throws IOException
	 */
	private PdfPTable createTable1(List<String> list) throws DocumentException, IOException {
		//设置字体  
	    BaseFont bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
	    Font fontChinese = new Font(bfChinese, 20, Font.NORMAL);// 创建字体，设置family，size，style,还可以设置color 
	    Font subBoldFontChinese = new Font(bfChinese, 15, Font.BOLD); 

		PdfPTable table = new PdfPTable(2);
		table.getDefaultCell().setBorder(PdfPCell.NO_BORDER);  
		table.setTotalWidth(550f);
		table.setLockedWidth(true);
		table.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.setSpacingAfter(20.0f);
		table.setSpacingBefore(20.0f);
		table.setWidths(new int[] { 1, 1});
		table.getDefaultCell().setMinimumHeight(60f);
		
//        table.addCell(new Paragraph("广告位名称", fontChinese));
//        table.addCell(new Paragraph("客户类型", fontChinese));
//        table.addCell(new Paragraph("活动名",subBoldFontChinese));
//        table.addCell(new Paragraph("媒体主", fontChinese));
//        table.addCell(new Paragraph("地理位置", fontChinese));
//        table.addCell(new Paragraph("详细位置", fontChinese));
//        table.addCell(new Paragraph("媒体大类", fontChinese));
//        table.addCell(new Paragraph("媒体小类", fontChinese));
//        table.addCell(new Paragraph("媒体类型",subBoldFontChinese));//媒体类型
//        table.addCell(new Paragraph("开始监测时间", fontChinese));
//        table.addCell(new Paragraph("结束监测时间", fontChinese));
//        table.addCell(new Paragraph("当前状态", subBoldFontChinese));
//        table.addCell(new Paragraph("任务类型", subBoldFontChinese));
//        table.addCell(new Paragraph("广告位编号", fontChinese));

        	table.addCell(new Paragraph("媒体主", fontChinese));
        	table.addCell(new Paragraph(list.get(20), fontChinese));//媒体主
        	table.addCell(new Paragraph("广告位编号", fontChinese));
        	table.addCell(new Paragraph(list.get(6), fontChinese));//广告位编号
        	table.addCell(new Paragraph("广告位名称", fontChinese));
        	table.addCell(new Paragraph(list.get(1), fontChinese)); //广告位名称
        	table.addCell(new Paragraph("广告位尺寸", fontChinese));
        	table.addCell(new Paragraph(list.get(10)+"(cm²)",fontChinese));//广告位尺寸
        	table.addCell(new Paragraph("媒体类型",fontChinese));//媒体类型
        	table.addCell(new Paragraph(list.get(18)+"-"+list.get(19),fontChinese)); //媒体类型
        	table.addCell(new Paragraph("地理位置", fontChinese));
        	
        	StringBuffer location = new StringBuffer();
        	if(StringUtil.isNotBlank(list.get(2))) {
        		location.append(list.get(2));  //省
        	}
        	if(StringUtil.isNotBlank(list.get(3))) {
        		location.append(list.get(3));  //市
        	}
        	if(StringUtil.isNotBlank(list.get(4))) {
        		location.append(list.get(4));  //主要路段
        	}
        	if(StringUtil.isNotBlank(list.get(5))) { //详细位置
        		location.append(list.get(5));
        	}
            table.addCell(new Paragraph(location.toString(), fontChinese));//具体位置
            
//            table.addCell(new Paragraph(list.get(7), fontChinese));//开始监测时间
//            table.addCell(new Paragraph(list.get(8), fontChinese));//结束监测时间
		
        
        //加入隔行换色事件
        PdfPTableEvent event = new AlternatingBackground();
        table.setTableEvent(event);
        table.addCell(table.getDefaultCell());
		return table;
	}
	
	private PdfPTable createTable2(List<List<String>> listString) throws DocumentException, IOException {
		//设置字体  
	    BaseFont bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
	    Font fontChinese = new Font(bfChinese, 20, Font.NORMAL);// 创建字体，设置family，size，style,还可以设置color 
	    Font subBoldFontChinese = new Font(bfChinese, 15, Font.BOLD); 

		PdfPTable table = new PdfPTable(4);
		table.getDefaultCell().setBorder(PdfPCell.NO_BORDER);  
		table.setTotalWidth(1500f);
		table.setLockedWidth(true);
		
		table.setSpacingAfter(20.0f);
		table.setSpacingBefore(20.0f);
		table.setWidths(new int[] { 1, 1 ,1 ,1});
//		table.getDefaultCell().setMinimumHeight(500f);
		PdfPCell cell = new PdfPCell(new Paragraph("点位名称", fontChinese)); 
		cell.setBackgroundColor(new BaseColor(211,211,211));
		cell.setBorder(0);
		cell.setHorizontalAlignment(1);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE); 
		cell.setMinimumHeight(50f);
		table.addCell(cell);
		cell = new PdfPCell(new Paragraph("媒体主", fontChinese));
		cell.setBackgroundColor(new BaseColor(211,211,211));
		cell.setBorder(0);
		cell.setHorizontalAlignment(1);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		table.addCell(cell);
		cell = new PdfPCell(new Paragraph("广告位编号", fontChinese));
		cell.setBackgroundColor(new BaseColor(211,211,211));
		cell.setBorder(0);
		cell.setHorizontalAlignment(1);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		table.addCell(cell);
		cell = new PdfPCell(new Paragraph("投放日期", fontChinese));
		cell.setBackgroundColor(new BaseColor(211,211,211));
		cell.setBorder(0);
		cell.setHorizontalAlignment(1);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		table.addCell(cell);
		
		for (List<String> list : listString) {
			cell = new PdfPCell(new Paragraph(list.get(1), fontChinese));//点位名称
			cell.setHorizontalAlignment(1);
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setMinimumHeight(30f);
			cell.setBorder(0);
			table.addCell(cell);
			cell = new PdfPCell(new Paragraph(list.get(20), fontChinese));//媒体主
			cell.setHorizontalAlignment(1);
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setBorder(0);
			table.addCell(cell);
			cell = new PdfPCell(new Paragraph(list.get(6), fontChinese));//广告位编号
			cell.setHorizontalAlignment(1);
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setBorder(0);
			table.addCell(cell);
			cell = new PdfPCell(new Paragraph(list.get(7)+" - "+list.get(8), fontChinese));//投放日期
			cell.setHorizontalAlignment(1);
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setBorder(0);
			table.addCell(cell);
		}
		return table;
	}
	
}
