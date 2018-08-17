package com.bt.om.web.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

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
import org.springframework.web.multipart.MultipartFile;

import com.adtime.common.lang.StringUtil;
import com.alibaba.druid.util.StringUtils;
import com.bt.om.cache.CityCache;
import com.bt.om.common.DateUtil;
import com.bt.om.common.SysConst;
import com.bt.om.entity.AdActivity;
import com.bt.om.entity.AdActivityAdseat;
import com.bt.om.entity.AdApp;
import com.bt.om.entity.AdMedia;
import com.bt.om.entity.AdMediaType;
import com.bt.om.entity.AdMonitorTask;
import com.bt.om.entity.AdMonitorTaskFeedback;
import com.bt.om.entity.AdSeatInfo;
import com.bt.om.entity.City;
import com.bt.om.entity.SysUser;
import com.bt.om.entity.SysUserExecute;
import com.bt.om.entity.vo.AdActivityAdseatTaskVo;
import com.bt.om.entity.vo.AdActivityAdseatVo;
import com.bt.om.entity.vo.AdActivityVo;
import com.bt.om.entity.vo.AdMediaTypeVo;
import com.bt.om.entity.vo.AdMonitorTaskVo;
import com.bt.om.entity.vo.FileInfoVo;
import com.bt.om.enums.AdCodeFlagEnum;
import com.bt.om.enums.AdminImportAdSeatEnum;
import com.bt.om.enums.AdminImportMonitorEnum;
import com.bt.om.enums.AppUserTypeEnum;
import com.bt.om.enums.ExcelImportFailEnum;
import com.bt.om.enums.MapStandardEnum;
import com.bt.om.enums.MediaImportAdSeatEnum;
import com.bt.om.enums.MonitorTaskFeedBackStatus;
import com.bt.om.enums.MonitorTaskStatus;
import com.bt.om.enums.MonitorTaskType;
import com.bt.om.enums.ResultCode;
import com.bt.om.enums.SessionKey;
import com.bt.om.enums.TaskProblemStatus;
import com.bt.om.enums.UserTypeEnum;
import com.bt.om.enums.VerifyType;
import com.bt.om.exception.web.ExcelException;
import com.bt.om.filter.LogFilter;
import com.bt.om.mapper.AdMediaMapper;
import com.bt.om.security.ShiroUtils;
import com.bt.om.service.IAdActivityService;
import com.bt.om.service.IAdMediaTypeService;
import com.bt.om.service.IAdMonitorTaskService;
import com.bt.om.service.IAdSeatService;
import com.bt.om.service.IAdUserMessageService;
import com.bt.om.service.IAppService;
import com.bt.om.service.IMediaService;
import com.bt.om.service.ISysUserExecuteService;
import com.bt.om.service.ISysUserService;
import com.bt.om.util.ConfigUtil;
import com.bt.om.util.ExcelTool;
import com.bt.om.util.ImportExcelUtil;
import com.bt.om.util.NumberUtil;
import com.bt.om.util.pdf.AlternatingBackground;
import com.bt.om.vo.web.ResultVo;
import com.bt.om.web.BasicController;
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
@RequestMapping(value = "/excel")
public class ExcelController extends BasicController {
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
	private ISysUserExecuteService sysUserExecuteService;
	
	private String fileUploadPath = ConfigUtil.getString("file.upload.path");
	
	private String fileUploadIp = ConfigUtil.getString("file.upload.ip");
	
	private static final Logger logger = Logger.getLogger(ExcelController.class);
	/**
	 * 批量导入媒体监测人员
	 */
	@RequiresRoles(value = {"superadmin","thirdcompany"}, logical = Logical.OR)
    @RequestMapping(value = "/insertMediaAppUserByExcel")
	@ResponseBody
	public Model insertMediaAppUserByExcel(Model model, HttpServletRequest request, HttpServletResponse response,
            @RequestParam(value = "excelFile", required = false) MultipartFile file,
            @RequestParam(value = "mediaId", required = false) Integer mediaId,
            @RequestParam(value = "companyId", required = false) Integer companyId,
            @RequestParam(value = "usertype", required = false) Integer usertype,
            @RequestParam(value = "password", required = false) String password) {
		//相关返回结果
		ResultVo result = new ResultVo();
        result.setCode(ResultCode.RESULT_SUCCESS.getCode());
        result.setResultDes("操作成功");
        model = new ExtendedModelMap();
        
        SysUser userObj = (SysUser) ShiroUtils.getSessionAttribute(SessionKey.SESSION_LOGIN_USER.toString());
  		try {
  			if (file.isEmpty()) {
				logger.error(MessageFormat.format("批量导入文件不能为空, 导入失败", new Object[] {}));
        		throw new ExcelException("批量导入文件不能为空, 导入失败");
			}
  			
  			if(usertype == AppUserTypeEnum.MEDIA.getId() && mediaId == null) {
  				result.setCode(ResultCode.RESULT_FAILURE.getCode());
                result.setResultDes("媒体不能为空！");
                model.addAttribute(SysConst.RESULT_KEY, result);
                return model;
  			}
  			
  			if(usertype == AppUserTypeEnum.THIRD_COMPANY.getId() && companyId == null) {
  				result.setCode(ResultCode.RESULT_FAILURE.getCode());
                result.setResultDes("第三方监测公司不能为空！");
                model.addAttribute(SysConst.RESULT_KEY, result);
                return model;
  			}
  			if(StringUtil.isBlank(password)) {
  				result.setCode(ResultCode.RESULT_FAILURE.getCode());
                result.setResultDes("密码不能为空！");
                model.addAttribute(SysConst.RESULT_KEY, result);
                return model;
  			}
  			
  			if(Pattern.matches(":\"^\\\\*{6}|(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,16}$\"", password)) {
  				result.setCode(ResultCode.RESULT_FAILURE.getCode());
                result.setResultDes("密码格式得是字母和数字组合！");
                model.addAttribute(SysConst.RESULT_KEY, result);
                return model;
  			}
  			
  			InputStream in = file.getInputStream();
	        List<List<Object>> listob = new ArrayList<List<Object>>();
	       
	        //导出文件相关
	 		final String fileName = "导入结果-" + System.currentTimeMillis() + ".xls"; //导出文件名
	        
            //excel上传支持
            listob = new ImportExcelUtil().getBankListByExcel(in, file.getOriginalFilename());

            //业务层操作
            Integer operateId = null;
            if(usertype == AppUserTypeEnum.MEDIA.getId()) {
            	operateId = mediaId;
            }else if(usertype == AppUserTypeEnum.THIRD_COMPANY.getId()) {
            	operateId = companyId;
            }else if(usertype == null) {	//第三方监测公司账号批量导入
            	operateId = userObj.getId();
            	usertype = AppUserTypeEnum.THIRD_COMPANY.getId();
            }
            adUserMessageService.insertBatchByExcel(listob, operateId, usertype, password);
            
            //导出到excel, 返回导入媒体监测人员信息结果
            List<List<String>> listString = objToString(listob);
            String[] titleArray = { "手机号", "真实姓名", "导入结果", "导入错误信息"};
            ExcelTool<List<String>> excelTool = new ExcelTool<List<String>>("importResult");
            String path = request.getSession().getServletContext().getRealPath("/");
    		path = path + (path.endsWith(File.separator)?"":File.separatorChar)+"static"+File.separatorChar+"excel"+File.separatorChar+fileName;
    		excelTool.generateExcel(listString, titleArray, path);
            result.setCode(ResultCode.RESULT_SUCCESS.getCode());
            result.setResult("/static/excel/" + fileName);
  		} catch (Exception e) {
        	logger.error(MessageFormat.format("批量导入文件有误, 导入失败", new Object[] {}));
        	result.setCode(ResultCode.RESULT_FAILURE.getCode());
        	result.setResultDes("导入失败");
            e.printStackTrace();
        }
		
        model.addAttribute(SysConst.RESULT_KEY, result);
        return model;
	}
	
	/**
	 * 批量导入媒体类型(包括媒体大类媒体小类)
	 */
	@RequiresRoles(value = {"superadmin"}, logical = Logical.OR)
    @RequestMapping(value = "/insertMediaTypeByExcel")
	@ResponseBody
	public Model insertMediaTypeByExcel(Model model, HttpServletRequest request, HttpServletResponse response,
            @RequestParam(value = "excelFile", required = false) MultipartFile file) {
		//相关返回结果
		ResultVo result = new ResultVo();
        result.setCode(ResultCode.RESULT_SUCCESS.getCode());
        result.setResultDes("操作成功");
        model = new ExtendedModelMap();
        
        //获取数据库里已有的媒体类型
  		List<AdMediaTypeVo> adMediaTypeVos = adMediaTypeService.selectParentAndSecond();
  		//(大类名-小类名-信息)
  		Table<String, String, AdMediaTypeVo> table = getTable(adMediaTypeVos);
  		
  		try {
  			if (file.isEmpty()) {
				logger.error(MessageFormat.format("批量导入文件不能为空, 导入失败", new Object[] {}));
        		throw new ExcelException("批量导入文件不能为空, 导入失败");
			}
  			
  			InputStream in = file.getInputStream();
	        List<List<Object>> listob = new ArrayList<List<Object>>();
	       
            //excel上传支持
            listob = new ImportExcelUtil().getBankListByExcel(in, file.getOriginalFilename());

            //业务层操作
            adMediaTypeService.insertBatchByExcel(listob, table);
  		} catch (Exception e) {
        	logger.error(MessageFormat.format("批量导入文件有误, 导入失败", new Object[] {}));
        	result.setCode(ResultCode.RESULT_FAILURE.getCode());
        	result.setResultDes("导入失败");
            e.printStackTrace();
        }
		
        model.addAttribute(SysConst.RESULT_KEY, result);
        return model;
	}
	
	/**
	 * 具体活动的pdf导出
	 * @throws ParseException 
	 */
	@RequiresRoles(value = {"superadmin", "activityadmin", "depactivityadmin", "admin" , "customer","phoneoperator"}, logical = Logical.OR)
    @RequestMapping(value = "/exportAdMediaPdf")
	@ResponseBody
	public Model exportPdf(Model model, HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "activityId", required = false) Integer activityId,
			@RequestParam(value = "taskreport", required = false) String taskreport) throws ParseException {
		System.setProperty("sun.jnu.encoding", "utf-8");
		SysUser userObj = (SysUser) ShiroUtils.getSessionAttribute(SessionKey.SESSION_LOGIN_USER.toString());
		
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
			
			searchMap.put("activityId", activityId);
			searchMap.put("reportTime", reportTime);
			searchMap.put("taskType", taskType);
		}
		//导出文件相关
		AdActivity adActivity = adActivityService.getById(activityId);
//		AdCustomerType customerType = adCustomerTypeService.getById(adActivity.getCustomerTypeId()); //客户类型
// 		final String fileName = adActivity.getActivityName() + "-广告位导出结果"+ ".pdf"; //导出文件名
		final String fileName = adActivity.getId() + "-" + System.currentTimeMillis() + ".pdf"; //导出文件名
 		List<List<String>> listString = new ArrayList<>();
        Map<Integer, List<String>> map = new HashMap<>();
        Rectangle pageSize = new Rectangle(1920, 1080);
//        Document document = new Document(PageSize.LEDGER);
        Document document = new Document(pageSize);
        
        Integer userId = adActivity.getUserId();//广告主id
        SysUser sysUser = sysUserService.getUserAppType(userId);
        Integer appId = sysUser.getAppTypeId();
        AdApp adapp = adappService.selectById(appId); 
        
        try {
        	//指定文件保存位置
	        String path = request.getSession().getServletContext().getRealPath("/");
			path = path + (path.endsWith(File.separator)?"":File.separatorChar)+"static"+File.separatorChar+"pdf"+File.separatorChar+fileName;
	        result.setCode(ResultCode.RESULT_SUCCESS.getCode());
	        result.setResult("/static/pdf/" + fileName); //提供下载
	        
	        //生成pdf文件
			PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(path));
			document.open();
			PdfContentByte cb = writer.getDirectContent();
			
			//[1] 生成pdf首页
		    BaseFont bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
		    BaseFont secfont = BaseFont.createFont(request.getSession().getServletContext().getRealPath("/") + "/static/font/SIMKAI.TTF", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
		    BaseFont thifont = BaseFont.createFont(request.getSession().getServletContext().getRealPath("/") + "/static/font/SIMFANG.TTF", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
		    Font font = new Font(bfChinese,12, Font.BOLD);
		    
		    //拼接title
		    StringBuffer stringBuffer = new StringBuffer();
		    stringBuffer.append(adActivity.getActivityName());
		    stringBuffer.append(taskreport.substring(10));
		    
		    Image image1 = Image.getInstance(request.getSession().getServletContext().getRealPath("/")+"/static/images/cover.jpg");
			image1.setAlignment(Image.ALIGN_CENTER);
			image1.scaleAbsolute(1920,1080);//控制图片大小
			image1.setAbsolutePosition(0,0);//控制图片位置
			document.add(image1);
			
		    //Header  
	        float y = document.top(380); 
			cb.beginText();  
			cb.setFontAndSize(secfont, 53);  
//			cb.showTextAligned(PdfContentByte.ALIGN_CENTER, stringBuffer.toString(), (document.right() + document.left())/2, y, 0);
			cb.showTextAligned(PdfContentByte.ALIGN_LEFT, stringBuffer.toString(), 180, y, 0);
			cb.endText();
			
			cb = writer.getDirectContent();
			cb.beginText();  
			cb.setFontAndSize(secfont, 26);  
//			cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "报告时间 "+taskreport.substring(0, 10), 1500, 200, 0);
			cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "报告时间  "+sdf.format(now) , 1500, 200, 0);
			cb.endText();
			Image image = Image.getInstance(request.getSession().getServletContext().getRealPath("/")+"/static/images/grouplogo.png");
			image.setAlignment(Image.ALIGN_CENTER);
			image.scaleAbsolute(140,50);//控制图片大小
			image.setAbsolutePosition(1620,80);//控制图片位置
			document.add(image);
			
			Image image2 = Image.getInstance(request.getSession().getServletContext().getRealPath("/")+"/static/images/jflogo.png");
			image2.setAlignment(Image.ALIGN_CENTER);
			image2.scaleAbsolute(200,50);//控制图片大小
			image2.setAbsolutePosition(200,80);//控制图片位置
			document.add(image2);
			
			Image image3 = Image.getInstance(request.getSession().getServletContext().getRealPath("/")+adapp.getAppPictureUrl());
			image3.setAlignment(Image.ALIGN_CENTER);
			image3.scaleAbsolute(65,60);//控制图片大小
			image3.setAbsolutePosition(1650,950);//控制图片位置
			document.add(image3);
			
//        	List<AdActivityAdseatTaskVo> vos = adActivityService.selectAdActivityAdseatTaskReport(activityId);
			List<AdActivityAdseatTaskVo> vos = adActivityService.newSelectAdActivityAdseatTaskReport(searchMap);
        	for (AdActivityAdseatTaskVo vo : vos) {
				List<String> list = new ArrayList<>();
				list.add(adActivity.getActivityName()); //活动名称 0
				list.add(vo.getInfo_name()); //广告位名称 1
				list.add(cityCache.getCityName(vo.getInfo_province())); //省 2
				list.add(cityCache.getCityName(vo.getInfo_city())); //市 3
//				list.add(cityCache.getCityName(vo.getInfo_region())); //区（县） 4
//				list.add(cityCache.getCityName(vo.getInfo_street())); //街道 5  
				list.add(vo.getInfo_road());//主要路段 4
				list.add(vo.getInfo_location()); //详细位置 5
				list.add(vo.getInfo_memo()); //媒体方广告位编号 6
//				list.add(vo.getInfo_uniqueKey()); //唯一标识 7
				list.add(DateUtil.dateFormate(vo.getMonitorStart(), "yyyy-MM-dd")); //开始监测时间 7
				list.add(DateUtil.dateFormate(vo.getMonitorEnd(), "yyyy-MM-dd")); //结束监测时间 8
//				String status = AdMediaInfoStatus.WATCHING.getText(); //当前状态 9
//				if(vo.getProblem_count() > 0) {
//					status = AdMediaInfoStatus.HAS_PROBLEM.getText();
//				}
//				if(vo.getMonitorStart().getTime() > now.getTime()) {
//					status = AdMediaInfoStatus.NOT_BEGIN.getText();
//				}
//				if(vo.getMonitorEnd().getTime() < now.getTime()) {
//					status = AdMediaInfoStatus.FINISHED.getText();
//	        	}
				Integer prostatus = vo.getProblemStatus();
				String problemStatus = null;
				if(prostatus==TaskProblemStatus.CLOSED.getId()) {
					problemStatus = TaskProblemStatus.CLOSED.getText();
				}else if(prostatus==TaskProblemStatus.FIXED.getId()) {
					problemStatus = TaskProblemStatus.FIXED.getText();
				}else if(prostatus==TaskProblemStatus.NO_PROBLEM.getId()) {
					problemStatus = TaskProblemStatus.NO_PROBLEM.getText();
				}
				else if(prostatus==TaskProblemStatus.UNMONITOR.getId()) {
					problemStatus = TaskProblemStatus.UNMONITOR.getText();
				}
				else if(prostatus==TaskProblemStatus.PROBLEM.getId()) {
					problemStatus = TaskProblemStatus.PROBLEM.getText();
				}
				list.add(problemStatus);
//				list.add(status); //当前状态
				list.add(vo.getInfo_adSize()); //尺寸 10
				list.add(vo.getInfo_adArea()); //面积 11
				list.add(vo.getInfo_adNum()+"");//面数12
				list.add(vo.getInfo_lon() + ""); //经度 13
				list.add(vo.getInfo_lat() + ""); //纬度 14
				if(vo.getInfo_mapStandard() != null) {
					list.add(MapStandardEnum.getText(vo.getInfo_mapStandard())); //地图标准 15
				} else {
					list.add(null);
				}
				list.add(vo.getInfo_contactName()); //联系人姓名 16
				list.add(vo.getInfo_contactCell()); //联系人电话 17
				list.add(mediaTypeMap.get(vo.getInfo_mediaTypeParentId())); //媒体大类 18
				list.add(mediaTypeMap.get(vo.getInfo_mediaTypeId())); //媒体小类 19
				list.add(vo.getMediaName()); //媒体主20
				list.add(type);//任务类型21
//				if(customerType != null) {
//					list.add(customerType.getName()); //客户类型22
//				} else {
//					list.add(null); //客户类型22
//				}
				list.add(vo.getRealname());	//22 审核人员
				Integer taskStatus = vo.getStatus();
				String  status= null;
				if(taskStatus== MonitorTaskStatus.UNASSIGN.getId()) {
					status = MonitorTaskStatus.UNASSIGN.getText();
				}else if(taskStatus == MonitorTaskStatus.TO_CARRY_OUT.getId()) {
					status = MonitorTaskStatus.TO_CARRY_OUT.getText();
				}else if(taskStatus == MonitorTaskStatus.UNVERIFY.getId()) {
					status = MonitorTaskStatus.UNVERIFY.getText();
				}else if(taskStatus == MonitorTaskStatus.VERIFIED.getId()) {
					status = MonitorTaskStatus.VERIFIED.getText();
				}else if(taskStatus == MonitorTaskStatus.VERIFY_FAILURE.getId()) {
					status = MonitorTaskStatus.VERIFY_FAILURE.getText();
				}else if(taskStatus == MonitorTaskStatus.UN_FINISHED.getId()) {
					status = MonitorTaskStatus.UN_FINISHED.getText();
				}else if(taskStatus == MonitorTaskStatus.UN_ACTIVE.getId()) {
					status = MonitorTaskStatus.UN_ACTIVE.getText();
				}else if(taskStatus == MonitorTaskStatus.CAN_GRAB.getId()) {
					status = MonitorTaskStatus.CAN_GRAB.getText();
				}else if(taskStatus == MonitorTaskStatus.VERIFY.getId()) {
					status = MonitorTaskStatus.VERIFY.getText();
				}
				list.add(status);//26 任务状态
				list.add(vo.getExe_realname());//27 任务执行人
				map.put(vo.getId(), list); //ad_activity_adseat的id
				listString.add(list);
			}
        	
        	document.setMargins(-70f, 100f, 250f, 50f);
        	document.newPage();
        	//[2] 点位信息生成
        	cb = writer.getDirectContent();
			cb.beginText();  
			cb.setFontAndSize(secfont, 53);  
			cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "广告位信息 ", 120, 860, 0);
			cb.endText();
			
			PdfPTable table1 = createTable2(listString);
			document.add(table1);
			image = Image.getInstance(request.getSession().getServletContext().getRealPath("/")+"/static/images/grouplogo.png");
			image.setAlignment(Image.ALIGN_CENTER);
			image.scaleAbsolute(140,50);//控制图片大小
			image.setAbsolutePosition(1620,80);//控制图片位置
			document.add(image);
			
			image2 = Image.getInstance(request.getSession().getServletContext().getRealPath("/")+"/static/images/jflogo.png");
			image2.setAlignment(Image.ALIGN_CENTER);
			image2.scaleAbsolute(200,50);//控制图片大小
			image2.setAbsolutePosition(200,80);//控制图片位置
			document.add(image2);
			
			image3 = Image.getInstance(request.getSession().getServletContext().getRealPath("/")+adapp.getAppPictureUrl());
			image3.setAlignment(Image.ALIGN_CENTER);
			image3.scaleAbsolute(65,60);//控制图片大小
			image3.setAbsolutePosition(1650,950);//控制图片位置
			document.add(image3);
        	            
			//生成pdf图片页
			List<Integer> ids = new ArrayList<>();
			List<Integer> activityAdseatIds = new ArrayList<>();
			//查询每个广告位最新的一条监测任务
//            List<AdMonitorTask> tasks = adMonitorTaskService.selectLatestMonitorTaskIds(activityId);
 			List<AdMonitorTask> tasks = adMonitorTaskService.newSelectLatestMonitorTaskIds(searchMap);
            for (AdMonitorTask task : tasks) {
            	if((userObj.getUsertype()==UserTypeEnum.CUSTOMER.getId() && task.getStatus()==MonitorTaskStatus.VERIFIED.getId()) || (userObj.getUsertype()!=UserTypeEnum.CUSTOMER.getId())) {
            		//登录方是广告主且任务当前状态是已审核  或者是群邑
            		ids.add(task.getId()); //ad_monitor_task的id
            		activityAdseatIds.add(task.getActivityAdseatId()); //ad_activity_adseat的id
            	}
			}
            //查询上述监测任务有效的一条反馈
            if(ids.size() > 0) {
            	List<AdMonitorTaskFeedback> taskFeedbacks = adMonitorTaskService.selectByActivity(ids);
            	for (Integer monitorTaskId : ids) {
            		//广告位信息
            		List<String> list = map.get(activityAdseatIds.get(ids.indexOf(monitorTaskId)));
            		
            		//生成广告位图片信息页, 每个广告位一页
            		document.setMargins(-1020f, 100f, 300f, 50f);
                	document.newPage();
                	PdfPTable table = createTable1(list);
        			document.add(table);
                	
                	//[3] 广告位信息生成
                	cb = writer.getDirectContent();
        			cb.beginText();  
        			cb.setFontAndSize(secfont, 53);  
        			cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "广告位信息 ", 120, 860, 0);
        			cb.endText(); 
        			
        			for (AdMonitorTaskFeedback feedback : taskFeedbacks) {
    					if(feedback.getMonitorTaskId().equals(monitorTaskId)) {
    						createPage(document, list, feedback, request);
    						break;
    					}
    				}
        			
        			image = Image.getInstance(request.getSession().getServletContext().getRealPath("/")+"/static/images/grouplogo.png");
        			image.setAlignment(Image.ALIGN_CENTER);
        			image.scaleAbsolute(140,50);//控制图片大小
        			image.setAbsolutePosition(1620,80);//控制图片位置
        			document.add(image);
        			
        			image2 = Image.getInstance(request.getSession().getServletContext().getRealPath("/")+"/static/images/jflogo.png");
        			image2.setAlignment(Image.ALIGN_CENTER);
        			image2.scaleAbsolute(200,50);//控制图片大小
        			image2.setAbsolutePosition(200,80);//控制图片位置
        			document.add(image2);
        			
        			image3 = Image.getInstance(request.getSession().getServletContext().getRealPath("/")+adapp.getAppPictureUrl());
        			image3.setAlignment(Image.ALIGN_CENTER);
        			image3.scaleAbsolute(65,60);//控制图片大小
        			image3.setAbsolutePosition(1650,950);//控制图片位置
        			document.add(image3);
				}
            }
            
            document.newPage();
            cb = writer.getDirectContent();
			cb.beginText();  
			cb.setFontAndSize(secfont, 53);  
			cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "甲方： ",320, 560, 0);
//			cb.endText(); 
//			
//			cb = writer.getDirectContent();
//			cb.beginText();  
			cb.setFontAndSize(secfont, 53);  
			cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "乙方： ", 1100, 560, 0);
			cb.endText(); 
            
			image = Image.getInstance(request.getSession().getServletContext().getRealPath("/")+"/static/images/grouplogo.png");
			image.setAlignment(Image.ALIGN_CENTER);
			image.scaleAbsolute(140,50);//控制图片大小
			image.setAbsolutePosition(1620,80);//控制图片位置
			document.add(image);
			
			image2 = Image.getInstance(request.getSession().getServletContext().getRealPath("/")+"/static/images/jflogo.png");
			image2.setAlignment(Image.ALIGN_CENTER);
			image2.scaleAbsolute(200,50);//控制图片大小
			image2.setAbsolutePosition(200,80);//控制图片位置
			document.add(image2);
			
			image3 = Image.getInstance(request.getSession().getServletContext().getRealPath("/")+adapp.getAppPictureUrl());
			image3.setAlignment(Image.ALIGN_CENTER);
			image3.scaleAbsolute(65,60);//控制图片大小
			image3.setAbsolutePosition(1650,950);//控制图片位置
			document.add(image3);
//			cb = writer.getDirectContent();
//			cb.beginText();  
//			cb.setFontAndSize(secfont, 30);  
//			cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "群邑上海广告有限公司 ", 1300, 410, 0);
//			cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "玖凤监测广告有限公司 ", 1300, 360, 0);
			cb.showTextAligned(PdfContentByte.ALIGN_LEFT, sysUser.getRealname(), 450, 420, 0);
//			cb.endText();
			
//			Image image4 = Image.getInstance(request.getSession().getServletContext().getRealPath("/")+"/static/images/gongsi.png");
//			image4.setAlignment(Image.ALIGN_CENTER);
//			image4.scaleAbsolute(360,150);//控制图片大小
//			image4.setAbsolutePosition(1260,300);//控制图片位置
//			document.add(image4);
			
			Image image5 = Image.getInstance(request.getSession().getServletContext().getRealPath("/")+"/static/images/gongzhang.png");
			image5.setAlignment(Image.ALIGN_CENTER);
			image5.scaleAbsolute(250,180);//控制图片大小
			image5.setAbsolutePosition(1300,350);//控制图片位置
			document.add(image5);
		} catch (Exception e) {
			logger.error(MessageFormat.format("批量导出pdf失败", new Object[] {}));
        	result.setCode(ResultCode.RESULT_FAILURE.getCode());
        	result.setResultDes(e.getMessage());
            e.printStackTrace();
		} finally {
			document.close();
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
			
			searchMap.put("activityId", activityId);
			searchMap.put("reportTime", reportTime);
			searchMap.put("taskType", taskType);
		}
		//导出文件相关
		AdActivity adActivity = adActivityService.getById(activityId); //活动
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
			List<AdMonitorTask> tasks = adMonitorTaskService.newSelectLatestMonitorTaskIds(searchMap);
	        for (AdMonitorTask task : tasks) {
	        	if((userObj.getUsertype()==UserTypeEnum.CUSTOMER.getId() && task.getStatus()==MonitorTaskStatus.VERIFIED.getId()) || (userObj.getUsertype()!=UserTypeEnum.CUSTOMER.getId())) {
	        		//登录方是广告主且任务当前状态是已审核  或者是群邑
	        		ids.add(task.getId()); //ad_monitor_task的id
	        	}
			}
			//图片地址
	        if(ids.size() > 0) {
	        	taskFeedbacks = adMonitorTaskService.selectByActivity(ids);
	        }
        	
        	List<AdActivityAdseatTaskVo> vos = adActivityService.newSelectAdActivityAdseatTaskReport(searchMap);
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
	 * 【群邑方】 批量插入广告位 (增加 广告位所属媒体主)
	 * 需要插入临时表
	 */
	@RequiresRoles(value = {"superadmin" , "media"}, logical = Logical.OR)
    @RequestMapping(value = "/insertBatch")
	@ResponseBody
	public Model insertBatchByExcel(Model model, HttpServletRequest request, HttpServletResponse response,
                                     @RequestParam(value = "excelFile", required = false) MultipartFile file) {
		//相关返回结果
		ResultVo result = new ResultVo();
        result.setCode(ResultCode.RESULT_SUCCESS.getCode());
        result.setResultDes("查询成功");
        model = new ExtendedModelMap();
        
        //导出文件相关
 		final String fileName = "导入广告位结果-" + System.currentTimeMillis() + ".xls"; //导出文件名
		List<City> provinces = cityCache.getAllProvince(); //获取全部省份
		Map<String, Long> provinceMap = citiesToMap(provinces);
		
		//获取登录用户信息
		Date nowDate = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	String formatDateStr = format.format(nowDate);
    	Date now = DateUtil.parseStrDate(formatDateStr, "yyyy-MM-dd HH:mm:ss");

    	//获取全部的媒体主信息
    	List<AdMedia> allMedias = mediaService.selectAllMedia();
		
//		//获取需要插入广告位信息的媒体已经存在的广告位信息
//		List<AdSeatInfo> adSeatsByMediaId = adSeatService.getAdSeatByMediaId(media.getId());
		//获取全部广告位(检验唯一性)
		List<AdSeatInfo> adSeats = adSeatService.selectAllSeats();
		Map<String, Integer> databaseAdseatMap = new HashMap<>();
		Set<String> memoSet = new HashSet<>();
		for (AdSeatInfo adSeatInfo : adSeats) {
			// 拼接详细描述 保证唯一性
//			StringBuffer buffer = new StringBuffer();
//			if(adSeatInfo.getProvince() != null) {
//				buffer.append(cityCache.getCityName(adSeatInfo.getProvince())); //省
//			}
//			if(adSeatInfo.getCity() != null) {
//				buffer.append(cityCache.getCityName(adSeatInfo.getCity())); //市
//			}
//			if(adSeatInfo.getRoad() != null) {
//				buffer.append(adSeatInfo.getRoad());//主要路段
//			}
//			buffer.append(adSeatInfo.getLocation()); //详细位置
//			databaseAdseatMap.put(buffer.toString(), adSeatInfo.getId());
			
			// 记录广告位编号 保证唯一性
			if(StringUtil.isNotBlank(adSeatInfo.getMemo())) {
				memoSet.add(adSeatInfo.getMemo());
			}
		}
		Set<String> keySet = new HashSet<>(databaseAdseatMap.keySet());
		adSeats.clear();
		
		//获取媒体类型
		List<AdMediaTypeVo> adMediaTypeVos = adMediaTypeService.selectParentAndSecond();
		Table<String, String, AdMediaTypeVo> table = getTable(adMediaTypeVos);
		adMediaTypeVos.clear();
		
		//需要插入临时表中的广告位id集合
		List<Integer> tmpSeatIds = new ArrayList<>();
		
		try {
			if (file.isEmpty()) {
				logger.error(MessageFormat.format("批量导入文件不能为空, 导入失败", new Object[] {}));
        		throw new ExcelException("批量导入文件不能为空, 导入失败");
			}
			
	        InputStream in = file.getInputStream();
	        List<List<Object>> listob = new ArrayList<List<Object>>();
	       
            //excel上传支持
            listob = new ImportExcelUtil().getBankListByExcel(in, file.getOriginalFilename());
            List<AdSeatInfo> insertAdSeatInfos = new ArrayList<AdSeatInfo>(); //需要导入到数据库中的广告位信息
            
            //excel列表
            for (int i = 1; i < listob.size(); i++) {
                List<Object> lo = listob.get(i);
                //广告位名称, 所属媒体主, 媒体大类, 媒体小类, 省（直辖市）, 市,  主要路段, 
                //详细位置, 媒体方广告位编号, 广告位长度, 广告位宽度, 经度, 纬度,面数, 地图标准（如百度，谷歌，高德）, 联系人姓名, 联系人电话, 导入结果, 导入错误信息
                if(lo.size() <= 19){
                	AdSeatInfo info = new AdSeatInfo();
                	Long provinceId = 0L;
                	Long cityId = 0L;
                	Boolean zhiXiaShiFlag = false;
                	Boolean hasProblem = false;
                	StringBuffer buffer = new StringBuffer();
                	AdMediaTypeVo adMediaTypeVo = null;
                	
                	//设置广告位名称
                	if(lo.get(AdminImportAdSeatEnum.ADSEAT_NAME.getId()) == null) {
                		lo.set(AdminImportAdSeatEnum.IMPORT_RESULT.getId(), IMPORT_FAIL);
                		lo.set(AdminImportAdSeatEnum.IMPORT_DES.getId(), ExcelImportFailEnum.ADNAME_NULL.getText());
                		hasProblem = true;
                	} else {
                		info.setName(String.valueOf(lo.get(AdminImportAdSeatEnum.ADSEAT_NAME.getId())).trim().replaceAll("\n", "")); //广告位名称
                		lo.set(AdminImportAdSeatEnum.ADSEAT_NAME.getId(),
                				String.valueOf(lo.get(AdminImportAdSeatEnum.ADSEAT_NAME.getId())).trim().replaceAll("\n", ""));
                	}
                	
                	//设置媒体主
                	if(hasProblem == false) {
                		if(lo.get(AdminImportAdSeatEnum.MEDIA_NAME.getId()) == null) {
                    		lo.set(AdminImportAdSeatEnum.IMPORT_RESULT.getId(), IMPORT_FAIL);
                    		lo.set(AdminImportAdSeatEnum.IMPORT_DES.getId(), ExcelImportFailEnum.MEDIA_NULL.getText());
                    		hasProblem = true;
                    	} else {
                    		for (AdMedia adMedia : allMedias) {
                    			if(StringUtil.equals(adMedia.getMediaName(), String.valueOf(lo.get(AdminImportAdSeatEnum.MEDIA_NAME.getId())).trim())) {
                    				info.setMediaId(adMedia.getId());
                    				break;
                    			}
                    		}
                    		if(info.getMediaId() == null) {
                    			lo.set(AdminImportAdSeatEnum.IMPORT_RESULT.getId(), IMPORT_FAIL);
                        		lo.set(AdminImportAdSeatEnum.IMPORT_DES.getId(), ExcelImportFailEnum.MEDIA_INVAILD.getText());
                        		hasProblem = true;
                    		}
                    	}
                	}
                	
                	//设置媒体大类
                	if(hasProblem == false) {
                		if(lo.get(AdminImportAdSeatEnum.PARENT_NAME.getId()) == null) {
                    		lo.set(AdminImportAdSeatEnum.IMPORT_RESULT.getId(), IMPORT_FAIL);
                    		lo.set(AdminImportAdSeatEnum.IMPORT_DES.getId(), ExcelImportFailEnum.PARENT_NULL.getText());
                    		hasProblem = true;
                    	}
                	}
                	
                	//设置媒体小类
                	if(hasProblem == false) {
                		if(lo.get(AdminImportAdSeatEnum.SECOND_NAME.getId()) == null) {
                    		lo.set(AdminImportAdSeatEnum.IMPORT_RESULT.getId(), IMPORT_FAIL);
                    		lo.set(AdminImportAdSeatEnum.IMPORT_DES.getId(), ExcelImportFailEnum.SECOND_NULL.getText());
                    		hasProblem = true;
                    	} else {
                    		String parentName = String.valueOf(lo.get(AdminImportAdSeatEnum.PARENT_NAME.getId())).trim();
                    		String secondName = String.valueOf(lo.get(AdminImportAdSeatEnum.SECOND_NAME.getId())).trim();
                    		adMediaTypeVo = table.get(parentName, secondName);
                    		if(adMediaTypeVo != null) {
                    			info.setMediaTypeParentId(adMediaTypeVo.getParentId());
                    			info.setMediaTypeId(adMediaTypeVo.getId());
                    			//通过媒体类型来确认广告位是否支持多个活动
                    			info.setAllowMulti(adMediaTypeVo.getAllowMulti());
                    			info.setMultiNum(adMediaTypeVo.getMultiNum());
                    		} else {
                    			lo.set(AdminImportAdSeatEnum.IMPORT_RESULT.getId(), IMPORT_FAIL);
                        		lo.set(AdminImportAdSeatEnum.IMPORT_DES.getId(), ExcelImportFailEnum.MEDIA_TYPE_INVALID.getText());
                        		hasProblem = true;
                    		}
    					}
                	}
                	
                	//设置广告位所在省份(包括直辖市)
                	if(hasProblem == false) {
                		if(lo.get(AdminImportAdSeatEnum.PROVINCE.getId()) == null) {
                    		lo.set(AdminImportAdSeatEnum.IMPORT_RESULT.getId(), IMPORT_FAIL);
                    		lo.set(AdminImportAdSeatEnum.IMPORT_DES.getId(), ExcelImportFailEnum.PROVINCE_NULL.getText());
                    		hasProblem = true;
                    	} else {
                    		String provinceName = String.valueOf(lo.get(AdminImportAdSeatEnum.PROVINCE.getId())).trim(); //省
                    		//判断是否是直辖市
                    		if(provinceName.contains("北京") || provinceName.contains("上海") || provinceName.contains("天津")
                    				|| provinceName.contains("重庆")) {
                    			zhiXiaShiFlag = true;
                    			if(!provinceName.endsWith("市")) {
                        			provinceName = provinceName + "市";
                        		}
                    		} else {
                    			if(provinceName.contains("内蒙")) {
                    				provinceName = "内蒙古自治区";
                    			} else if (provinceName.contains("广西")) {
                    				provinceName = "广西壮族自治区";
    							} else if (provinceName.contains("西藏")) {
    								provinceName = "西藏自治区";
    							} else if (provinceName.contains("宁夏")) {
    								provinceName = "宁夏回族自治区";
    							} else if (provinceName.contains("新疆")) {
    								provinceName = "新疆维吾尔自治区";
    							} else if (provinceName.contains("香港")) {
    								provinceName = "香港特别行政区";
    							} else if (provinceName.contains("澳门")) {
    								provinceName = "澳门特别行政区";
    							} else {
    								if(!provinceName.endsWith("省")) {
    	                    			provinceName = provinceName + "省";
    	                    		}
    							}
                    		}
                    		provinceId = provinceMap.get(provinceName);
                    		if(provinceId == null) {
                    			lo.set(AdminImportAdSeatEnum.IMPORT_RESULT.getId(), IMPORT_FAIL);
                        		lo.set(AdminImportAdSeatEnum.IMPORT_DES.getId(), ExcelImportFailEnum.PROVINCE_INVALID.getText());
                        		hasProblem = true;
                    		}
                    		info.setProvince(provinceId);
                    		buffer.append(provinceName);
    					}
                	}
                	
                	//设置广告位所在市
                	if(hasProblem == false) {
                		if(lo.get(AdminImportAdSeatEnum.CITY.getId()) == null) {
                    		if(zhiXiaShiFlag == false) {
                    			lo.set(AdminImportAdSeatEnum.IMPORT_RESULT.getId(), IMPORT_FAIL);
                        		lo.set(AdminImportAdSeatEnum.IMPORT_DES.getId(), ExcelImportFailEnum.CITY_NULL.getText());
                        		hasProblem = true;
                    		}
                    	} else {
                    		if(zhiXiaShiFlag == true) {
                    			//直辖市的city字段不存库
                    		} else {
                    			String cityName = String.valueOf(lo.get(AdminImportAdSeatEnum.CITY.getId())).trim(); //市
                        		if(zhiXiaShiFlag == true) {
                        			info.setCity(info.getProvince());
                        			cityId = provinceId;
                        		} else {
                        			List<City> cities = cityCache.getCity(provinceId);
                            		Map<String, Long> cityMap = citiesToMap(cities);
                            		Set<String> cityNames = cityMap.keySet();
                            		for (String name : cityNames) {
        								if(name.contains(cityName)) {
        									cityName = name;
        									break;
        								}
        							}
                            		cityId = cityMap.get(cityName);
                            		if(cityId == null) {
                            			lo.set(AdminImportAdSeatEnum.IMPORT_RESULT.getId(), IMPORT_FAIL);
                                		lo.set(AdminImportAdSeatEnum.IMPORT_DES.getId(), ExcelImportFailEnum.CITY_INVALID.getText());
                                		hasProblem = true;
                            		}
                            		info.setCity(cityId);
                            		buffer.append(cityName);
                        		}
                    		}
    					}
                	}
                	
                	//设置广告位所在主要路段
                	if(hasProblem == false) {
                		if(lo.get(AdminImportAdSeatEnum.ROAD.getId()) == null) {
                    		lo.set(AdminImportAdSeatEnum.IMPORT_RESULT.getId(), IMPORT_FAIL);
                    		lo.set(AdminImportAdSeatEnum.IMPORT_DES.getId(), ExcelImportFailEnum.STREET_NULL.getText());
                    		hasProblem = true;
                    	} else {
                    		String road = String.valueOf(lo.get(AdminImportAdSeatEnum.ROAD.getId())).trim(); //主要路段
                    		info.setRoad(road);
                    		buffer.append(road);
                    	}
                	}
                	
                	//设置广告位详细地址
                	if(hasProblem == false) {
                		if(lo.get(AdminImportAdSeatEnum.LOCATION.getId()) == null) {
                    		lo.set(AdminImportAdSeatEnum.IMPORT_RESULT.getId(), IMPORT_FAIL);
                    		lo.set(AdminImportAdSeatEnum.IMPORT_DES.getId(), ExcelImportFailEnum.LOCATION_NULL.getText());
                    		hasProblem = true;
                    	} else {
                    		info.setLocation(String.valueOf(lo.get(AdminImportAdSeatEnum.LOCATION.getId())).trim().replaceAll("\n", "")); //详细位置
                    		buffer.append(String.valueOf(lo.get(AdminImportAdSeatEnum.LOCATION.getId())).trim().replaceAll("\n", ""));
                    		lo.set(AdminImportAdSeatEnum.LOCATION.getId(), 
                    				String.valueOf(lo.get(AdminImportAdSeatEnum.LOCATION.getId())).trim().replaceAll("\n", ""));
                    	}
                	}
                	
                	//设置媒体方广告位编号信息
                	if(hasProblem == false) {
                		if(lo.get(AdminImportAdSeatEnum.MEMO.getId()) != null) {
                			String memo = String.valueOf(lo.get(AdminImportAdSeatEnum.MEMO.getId())).trim();
                			if(memoSet.contains(memo) == true) {
                				lo.set(AdminImportAdSeatEnum.IMPORT_RESULT.getId(), IMPORT_FAIL);
                        		lo.set(AdminImportAdSeatEnum.IMPORT_DES.getId(), ExcelImportFailEnum.MEMO_DUP.getText());
                        		hasProblem = true;
                			} else {
                				info.setMemo(memo);
                				memoSet.add(memo);
                			}
                		}
                	}
                	
                	//设置广告位尺寸
                	if(hasProblem == false) {
                		if(lo.get(AdminImportAdSeatEnum.LENGTH.getId()) != null && lo.get(AdminImportAdSeatEnum.WIDTH.getId()) != null) {
//                    		DecimalFormat df = new DecimalFormat("0"); // 格式化number String字符
                    		String length = String.valueOf(lo.get(AdminImportAdSeatEnum.LENGTH.getId())).trim();
                    		String width = String.valueOf(lo.get(AdminImportAdSeatEnum.WIDTH.getId())).trim();
                    		info.setAdSize(length + "*" + width); //广告位长度*广告位宽度
                    		lo.set(AdminImportAdSeatEnum.LENGTH.getId(), length);
                    		lo.set(AdminImportAdSeatEnum.WIDTH.getId(), width);
                    		double area = NumberUtil.divideInHalfUp(NumberUtil.multiply(width, length).toString(),"10000" , 3).doubleValue();
                    		info.setAdArea(area + "");
                    	} else if (lo.get(AdminImportAdSeatEnum.LENGTH.getId()) != null && lo.get(AdminImportAdSeatEnum.WIDTH.getId()) == null) {
                    		lo.set(AdminImportAdSeatEnum.IMPORT_RESULT.getId(), IMPORT_FAIL);
                    		lo.set(AdminImportAdSeatEnum.IMPORT_DES.getId(), ExcelImportFailEnum.SIZE_ONLYONE.getText());
                    		hasProblem = true;
    					} else if (lo.get(AdminImportAdSeatEnum.LENGTH.getId()) == null && lo.get(AdminImportAdSeatEnum.WIDTH.getId()) != null) {
                    		lo.set(AdminImportAdSeatEnum.IMPORT_RESULT.getId(), IMPORT_FAIL);
                    		lo.set(AdminImportAdSeatEnum.IMPORT_DES.getId(), ExcelImportFailEnum.SIZE_ONLYONE.getText());
                    		hasProblem = true;
    					} else {
    					}
                	}
                	
                	//设置面积
//                	if(hasProblem == false) {
//                		if(lo.get(11) != null) {
//                    		info.setAdArea(String.valueOf(lo.get(11)).trim());
//                    	}
//                	}
                	
                	//设置面数
                	if(hasProblem == false) {
                		if(lo.get(AdminImportAdSeatEnum.AD_NUM.getId()) != null) {
                			Double b = Double.parseDouble(String.valueOf(lo.get(AdminImportAdSeatEnum.AD_NUM.getId())).trim());
                    		info.setAdNum((new Double(b)).intValue());
                    	}
                	}
                	
                	//设置经纬度
                	if(hasProblem == false) {
                		if(lo.get(AdminImportAdSeatEnum.LON.getId()) != null && lo.get(AdminImportAdSeatEnum.LAT.getId()) == null) {
                    		lo.set(AdminImportAdSeatEnum.IMPORT_RESULT.getId(), IMPORT_FAIL);
                    		lo.set(AdminImportAdSeatEnum.IMPORT_DES.getId(), ExcelImportFailEnum.LOC_ONLYONE.getText());
                    		hasProblem = true;
                    	} else if (lo.get(AdminImportAdSeatEnum.LON.getId()) == null && lo.get(AdminImportAdSeatEnum.LAT.getId()) != null) {
                    		lo.set(AdminImportAdSeatEnum.IMPORT_RESULT.getId(), IMPORT_FAIL);
                    		lo.set(AdminImportAdSeatEnum.IMPORT_DES.getId(), ExcelImportFailEnum.LOC_ONLYONE.getText());
                    		hasProblem = true;
    					} else if (lo.get(AdminImportAdSeatEnum.LON.getId()) == null && lo.get(AdminImportAdSeatEnum.LAT.getId()) == null) {
    					} else {
    						//判断经度在-180-180之间
    						double lon = Double.parseDouble(String.valueOf(lo.get(AdminImportAdSeatEnum.LON.getId())).trim());
    						if(lon < -180 || lon > 180) {
    							lo.set(AdminImportAdSeatEnum.IMPORT_RESULT.getId(), IMPORT_FAIL);
    	                		lo.set(AdminImportAdSeatEnum.IMPORT_DES.getId(), ExcelImportFailEnum.LON_OVERFLOW.getText());
    	                		hasProblem = true;
    						}
    						//判断纬度在-90-90之间
    						double lat = Double.parseDouble(String.valueOf(lo.get(AdminImportAdSeatEnum.LAT.getId())).trim());
    						if(lat < -90 || lat > 90) {
    	                		lo.set(AdminImportAdSeatEnum.IMPORT_RESULT.getId(), IMPORT_FAIL);
    	                		lo.set(AdminImportAdSeatEnum.IMPORT_DES.getId(), ExcelImportFailEnum.LAT_OVERFLOW.getText());
    	                		hasProblem = true;
    						}
    						info.setLon(lon); //经度
    	                	info.setLat(lat); //纬度
    	                	
    	                	//地图标准
    	                	if(hasProblem == false) {
    	                		if(lo.get(AdminImportAdSeatEnum.MAP_STANDARD.getId()) != null) {
    	                			if(String.valueOf(lo.get(AdminImportAdSeatEnum.MAP_STANDARD.getId())).trim().contains("百度")) {
    	                				info.setMapStandard(MapStandardEnum.getId("百度"));
    	                			} else if(String.valueOf(lo.get(AdminImportAdSeatEnum.MAP_STANDARD.getId())).trim().contains("高德")) {
    	                				info.setMapStandard(MapStandardEnum.getId("高德"));
    	                			} else if(String.valueOf(lo.get(AdminImportAdSeatEnum.MAP_STANDARD.getId())).trim().contains("谷歌")) {
    	                				info.setMapStandard(MapStandardEnum.getId("谷歌"));
    	                			}
        	                	} else {
        	                		lo.set(AdminImportAdSeatEnum.IMPORT_RESULT.getId(), IMPORT_FAIL);
        	                		lo.set(AdminImportAdSeatEnum.IMPORT_DES.getId(), ExcelImportFailEnum.NONE_MAP.getText());
        	                		hasProblem = true;
        						}
    	                	}
    					}
                	}
                	
                	//设置联系人信息
                	if(hasProblem == false) {
                		if(lo.get(AdminImportAdSeatEnum.CONTACT_NAME.getId()) != null) {
                    		info.setContactName(String.valueOf(lo.get(AdminImportAdSeatEnum.CONTACT_NAME.getId())).trim());
                    	}
                	}
                	
                	//联系人电话
                	if(hasProblem == false) {
                		if(lo.get(AdminImportAdSeatEnum.CONTANT_CELL.getId()) != null) {
                    		info.setContactCell(String.valueOf(lo.get(AdminImportAdSeatEnum.CONTANT_CELL.getId())).trim());
                    	}
                	}
                	
                	info.setCreateTime(now);
                	info.setUpdateTime(now);
                	
                	//检查是否重复(去除hasProblem校验, 之前的导入失败原因会被这个覆盖, 这是为了适配最近一次导入的问题)
            		if(keySet.contains(buffer.toString())) {
                		lo.set(AdminImportAdSeatEnum.IMPORT_RESULT.getId(), IMPORT_FAIL);
                		lo.set(AdminImportAdSeatEnum.IMPORT_DES.getId(), ExcelImportFailEnum.LOC_DUP.getText());
                		hasProblem = true;
                		//重复的广告位放入临时表id集合中
                		if (databaseAdseatMap.get(buffer.toString())!=null) {
                			tmpSeatIds.add(databaseAdseatMap.get(buffer.toString()));
						}
                	} else {
                		keySet.add(buffer.toString());
                	}
                	
                	if(!(StringUtils.equals(String.valueOf(lo.get(AdminImportAdSeatEnum.IMPORT_RESULT.getId())), IMPORT_FAIL))) {
                		//导入成功
                		lo.set(AdminImportAdSeatEnum.IMPORT_RESULT.getId(), IMPORT_SUCC);
                		//默认没有贴上二维码
                		info.setCodeFlag(AdCodeFlagEnum.NO.getId());
                		
//                		//生成广告位对应的二维码
//                		String adCodeInfo = mediaUser.getPrefix() + UUID.randomUUID(); //二维码存的值（媒体前缀比如media3- 加上UUID随机数）
//                		String path = request.getSession().getServletContext().getRealPath("/");
//                		path = path + (path.endsWith(File.separator)?"":File.separatorChar)+"static"+File.separatorChar+"qrcode"+File.separatorChar+adCodeInfo + ".jpg";
//                		QRcodeUtil.encode(adCodeInfo, path);
//                		info.setAdCode(adCodeInfo);
//                		info.setAdCodeUrl("/static/qrcode/" + adCodeInfo + ".jpg");
                		
                		insertAdSeatInfos.add(info);
                	}
                } else {
                	logger.error(MessageFormat.format("批量导入文件有误, 导入失败", new Object[] {}));
                	result.setCode(ResultCode.RESULT_FAILURE.getCode());
                	result.setResultDes("批量导入文件有误, 导入失败");
                    throw new ExcelException("批量导入文件有误, 导入失败");
                }
            }
            
            //正常数据插入到数据库中
        	adSeatService.insertBatchByExcel(insertAdSeatInfos, tmpSeatIds, now);
        	insertAdSeatInfos.clear();
        	table.clear();
        	keySet.clear();
        	databaseAdseatMap.clear();
        	tmpSeatIds.clear();
        	memoSet.clear();
            
            //导出到excel, 返回导入广告位信息结果
            List<List<String>> listString = objToString(listob);
            listob.clear();
            String[] titleArray = { "广告位名称", "所属媒体主", "媒体大类", "媒体小类", "省(直辖市)", "市", "主要路段", "详细位置", 
            		"广告位编号", "广告位长度", "广告位宽度", "面数","经度", "纬度",
            		"地图标准（如百度，谷歌，高德）", "联系人姓名", "联系人电话", "导入结果", "导入错误信息"};
            ExcelTool<List<String>> excelTool = new ExcelTool<List<String>>("importResult");
            String path = request.getSession().getServletContext().getRealPath("/");
    		path = path + (path.endsWith(File.separator)?"":File.separatorChar)+"static"+File.separatorChar+"excel"+File.separatorChar+fileName;
    		excelTool.generateExcel(listString, titleArray, path);
            
            result.setCode(ResultCode.RESULT_SUCCESS.getCode());
            result.setResult("/static/excel/" + fileName);
            listString.clear();
        } catch (Exception e) {
        	logger.error(MessageFormat.format("批量导入文件有误, 导入失败", new Object[] {}));
        	result.setCode(ResultCode.RESULT_FAILURE.getCode());
        	result.setResultDes("导入失败");
            e.printStackTrace();
        }
		
        model.addAttribute(SysConst.RESULT_KEY, result);
        return model;
	}
	
	/**
	 * 批量导入广告位编号和广告位图片
	 * */
    @RequestMapping(value = "/insertMemoByExcel")
	@ResponseBody
	public Model insertMemoByExcel(Model model, HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "filepath", required = false) String filepath,
			@RequestParam(value = "excelFile", required = false) MultipartFile file) {
    	//相关返回结果
		ResultVo result = new ResultVo();
        result.setCode(ResultCode.RESULT_SUCCESS.getCode());
        result.setResultDes("查询成功");
        model = new ExtendedModelMap();
        try {
        	if (file.isEmpty()) {
				logger.error(MessageFormat.format("批量导入文件不能为空, 导入失败", new Object[] {}));
        		throw new ExcelException("批量导入文件不能为空, 导入失败");
			}
			
	        InputStream in = file.getInputStream();
	        List<List<Object>> listob = new ArrayList<List<Object>>();
	       
            //excel上传支持
            listob = new ImportExcelUtil().getBankListByExcel(in, file.getOriginalFilename());
            //导出文件相关
	 		final String fileName = "导入结果-" + System.currentTimeMillis() + ".xls"; //导出文件名
	 		
            //excel列表
            Map<String,String> memoMap = new HashMap<String,String>();
            List<String> memoList = new ArrayList<>();
            for (int i = 1; i < listob.size(); i++) {
            	List<Object> lo = listob.get(i);
            	memoMap.put((String) lo.get(0),(String) lo.get(1));
            }
            for(Map.Entry<String, String> entry : memoMap.entrySet()){
            	memoList.add(entry.getKey()); //获取所有广告位编号
            }
            //业务层操作
            //【1】查出所有指定广告位编号的广告位信息
            List<AdActivityAdseatVo> adSeat = adActivityService.findAllMemo(memoList);
            //【2】删除所有指定广告位-活动的信息
            Map<String, Object> searchMap = new HashMap<>();
            //获取所有的活动id
            for(AdActivityAdseatVo vo : adSeat) {
            	searchMap.put("activityIds", vo.getActivityId());
            }
//            if(!filepath.startsWith("/")) {
//            	filepath = "/" + filepath;
//            }
//            if(!filepath.endsWith("/")) {
//            	filepath = filepath + "/";
//            }
            adActivityService.deleteSeats(searchMap,adSeat,memoMap,filepath,listob);
            
            //导出到excel, 返回导入媒体监测人员信息结果
            List<List<String>> listString = objToString(listob);
            String[] titleArray = { "广告位编号", "图片名称", "导入结果", "导入错误信息"};
            ExcelTool<List<String>> excelTool = new ExcelTool<List<String>>("importResult");
            String path = request.getSession().getServletContext().getRealPath("/");
    		path = path + (path.endsWith(File.separator)?"":File.separatorChar)+"static"+File.separatorChar+"excel"+File.separatorChar+fileName;
    		excelTool.generateExcel(listString, titleArray, path);
            result.setCode(ResultCode.RESULT_SUCCESS.getCode());
            result.setResult("/static/excel/" + fileName);
        }  catch (Exception e) {
        	logger.error(MessageFormat.format("批量导入文件有误, 导入失败", new Object[] {}));
        	result.setCode(ResultCode.RESULT_FAILURE.getCode());
        	result.setResultDes("导入失败");
            e.printStackTrace();
        }
		
        model.addAttribute(SysConst.RESULT_KEY, result);
        return model;
	}
	
	/**
	 * 【媒体方】 批量插入广告位
	 */
	@RequiresRoles(value = {"superadmin" , "media"}, logical = Logical.OR)
    @RequestMapping(value = "/insertBatchMedia")
	@ResponseBody
	public Model insertBatchMediaByExcel(Model model, HttpServletRequest request, HttpServletResponse response,
                                     @RequestParam(value = "excelFile", required = false) MultipartFile file,
                                     @RequestParam(value = "mediaId", required = false) Integer mediaId) {
		//相关返回结果
		ResultVo result = new ResultVo();
        result.setCode(ResultCode.RESULT_SUCCESS.getCode());
        result.setResultDes("查询成功");
        model = new ExtendedModelMap();
        
        //导出文件相关
 		final String fileName = "导入结果-" + System.currentTimeMillis() + ".xls"; //导出文件名
		List<City> provinces = cityCache.getAllProvince(); //获取全部省份
		Map<String, Long> provinceMap = citiesToMap(provinces);
		
		//获取登录用户信息
		Date now = new Date();
    	SysUser user = (SysUser) ShiroUtils.getSessionAttribute(SessionKey.SESSION_LOGIN_USER.toString());
    	user = sysUserService.findUserinfoById(user.getId());
    	AdMedia media = new AdMedia();
//    	SysUserVo mediaUser = new SysUserVo();
		Integer usertype = user.getUsertype();
		if(usertype == UserTypeEnum.SUPER_ADMIN.getId() || usertype == UserTypeEnum.DEPARTMENT_LEADER.getId() || UserTypeEnum.ADMIN.getId() == 1) {
			//后台管理人员帮助媒体导入
			media = adMediaMapper.selectByPrimaryKey(mediaId);
//			mediaUser = sysUserService.findUserinfoById(media.getUserId());
		} else if (usertype == UserTypeEnum.MEDIA.getId()) {
			//媒体人员自行导入
			media = adMediaMapper.selectByUserId(user.getId());
//			mediaUser = sysUserService.findUserinfoById(media.getUserId());
		}
		
//		//获取需要插入广告位信息的媒体已经存在的广告位信息
//		List<AdSeatInfo> adSeatsByMediaId = adSeatService.getAdSeatByMediaId(media.getId());
		//获取全部广告位(检验唯一性)
		List<AdSeatInfo> adSeats = adSeatService.selectAllSeats();
		List<String> databaseAdSeats = new ArrayList<>();
		Set<String> memoSet = new HashSet<>();
		for (AdSeatInfo adSeatInfo : adSeats) {
//			StringBuffer buffer = new StringBuffer();
//			if(adSeatInfo.getProvince() != null) {
//				buffer.append(cityCache.getCityName(adSeatInfo.getProvince())); //省
//			}
//			if(adSeatInfo.getCity() != null) {
//				buffer.append(cityCache.getCityName(adSeatInfo.getCity())); //市
//			}
//			buffer.append(adSeatInfo.getRoad());//主要路段
//			buffer.append(adSeatInfo.getLocation()); //详细位置
//			databaseAdSeats.add(buffer.toString());
			
			// 记录广告位编号 保证唯一性
			if(StringUtil.isNotBlank(adSeatInfo.getMemo())) {
				memoSet.add(adSeatInfo.getMemo());
			}
		}
		Set<String> keySet = new HashSet<>(databaseAdSeats);
		adSeats.clear();
		
		//获取媒体类型
		List<AdMediaTypeVo> adMediaTypeVos = adMediaTypeService.selectParentAndSecond();
		Table<String, String, AdMediaTypeVo> table = getTable(adMediaTypeVos);
		adMediaTypeVos.clear();
		
		try {
			if (file.isEmpty()) {
				logger.error(MessageFormat.format("批量导入文件不能为空, 导入失败", new Object[] {}));
        		throw new ExcelException("批量导入文件不能为空, 导入失败");
			}
			
	        InputStream in = file.getInputStream();
	        List<List<Object>> listob = new ArrayList<List<Object>>();
	       
            //excel上传支持
            listob = new ImportExcelUtil().getBankListByExcel(in, file.getOriginalFilename());
            List<AdSeatInfo> insertAdSeatInfos = new ArrayList<AdSeatInfo>(); //需要导入到数据库中的广告位信息
            
            //excel列表
            for (int i = 1; i < listob.size(); i++) {
                List<Object> lo = listob.get(i);
                //广告位名称, 媒体大类, 媒体小类, 省（直辖市）, 市, 主要路段, 
                //详细位置, 广告位编号， 广告位长度, 广告位宽度, 经度, 纬度, 地图标准（如百度，谷歌，高德）, 联系人姓名, 联系人电话, 导入结果, 导入错误信息
                if(lo.size() <= 18){
                	AdSeatInfo info = new AdSeatInfo();
                	Long provinceId = 0L;
                	Long cityId = 0L;
                	Boolean zhiXiaShiFlag = false;
                	Boolean hasProblem = false;
                	StringBuffer buffer = new StringBuffer();
                	AdMediaTypeVo adMediaTypeVo = null;
                	
                	//设置广告位名称
                	if(lo.get(MediaImportAdSeatEnum.ADSEAT_NAME.getId()) == null) {
                		lo.set(MediaImportAdSeatEnum.IMPORT_RESULT.getId(), IMPORT_FAIL);
                		lo.set(MediaImportAdSeatEnum.IMPORT_DES.getId(), ExcelImportFailEnum.ADNAME_NULL.getText());
                		hasProblem = true;
                	} else {
                		info.setName(String.valueOf(lo.get(MediaImportAdSeatEnum.ADSEAT_NAME.getId())).trim().replaceAll("\n", "")); //广告位名称
                		lo.set(MediaImportAdSeatEnum.ADSEAT_NAME.getId(), 
                				String.valueOf(lo.get(MediaImportAdSeatEnum.ADSEAT_NAME.getId())).trim().replaceAll("\n", ""));
                	}
                	
                	//设置媒体主
    				info.setMediaId(media.getId());
                	
                	//设置媒体大类
                	if(hasProblem == false) {
                		if(lo.get(MediaImportAdSeatEnum.PARENT_NAME.getId()) == null) {
                    		lo.set(MediaImportAdSeatEnum.IMPORT_RESULT.getId(), IMPORT_FAIL);
                    		lo.set(MediaImportAdSeatEnum.IMPORT_DES.getId(), ExcelImportFailEnum.PARENT_NULL.getText());
                    		hasProblem = true;
                    	}
                	}
                	
                	//设置媒体小类
                	if(hasProblem == false) {
                		if(lo.get(MediaImportAdSeatEnum.SECOND_NAME.getId()) == null) {
                    		lo.set(MediaImportAdSeatEnum.IMPORT_RESULT.getId(), IMPORT_FAIL);
                    		lo.set(MediaImportAdSeatEnum.IMPORT_DES.getId(), ExcelImportFailEnum.SECOND_NULL.getText());
                    		hasProblem = true;
                    	} else {
                    		String parentName = String.valueOf(lo.get(MediaImportAdSeatEnum.PARENT_NAME.getId())).trim();
                    		String secondName = String.valueOf(lo.get(MediaImportAdSeatEnum.SECOND_NAME.getId())).trim();
                    		adMediaTypeVo = table.get(parentName, secondName);
                    		if(adMediaTypeVo != null) {
                    			info.setMediaTypeParentId(adMediaTypeVo.getParentId());
                    			info.setMediaTypeId(adMediaTypeVo.getId());
                    			//通过媒体类型来确认广告位是否支持多个活动
                    			info.setAllowMulti(adMediaTypeVo.getAllowMulti());
                    			info.setMultiNum(adMediaTypeVo.getMultiNum());
                    		} else {
                    			lo.set(MediaImportAdSeatEnum.IMPORT_RESULT.getId(), IMPORT_FAIL);
                        		lo.set(MediaImportAdSeatEnum.IMPORT_DES.getId(), ExcelImportFailEnum.MEDIA_TYPE_INVALID.getText());
                        		hasProblem = true;
                    		}
    					}
                	}
                	
//                	//设置是否允许多个活动（是或否）不填写则默认为否
//                	if(hasProblem == false) {
//                		if(lo.get(3) == null) {
//                			info.setAllowMulti(0); //不填写则默认为否
//                		} else {
//                			String allowMulti = String.valueOf(lo.get(3)).trim();
//                			if(StringUtil.equals(allowMulti, "是")) {
//                				info.setAllowMulti(1); //1：是
//                			} else if(StringUtil.equals(allowMulti, "否")) {
//                				info.setAllowMulti(0); //0：否
//                			} else {
//                				info.setAllowMulti(0); //0：否
//                			}
//                		}
//                	}
//                	
//                	//设置允许活动数量, 不填写默认为1
//                	if(hasProblem == false) {
//                		if(lo.get(4) == null) {
//                			info.setMultiNum(1); //不填写默认为1
//                		} else {
//                			if(info.getAllowMulti() == 0) {
//                				info.setMultiNum(1); //allowMulti为0代表不允许多个活动, 设置活动数量为1
//                			} else {
//                				String multiNumStr = String.valueOf(lo.get(4)).trim();
//                				try {
//                					Double multiNumDou = new Double(multiNumStr);
//                					Integer multiNum = multiNumDou.intValue();
//                					info.setMultiNum(multiNum);
//								} catch (Exception e) {
//									lo.set(17, IMPORT_FAIL);
//	                        		lo.set(18, ExcelImportFailEnum.NUM_INVALID.getText());
//	                        		hasProblem = true;
//								}
//                			}
//                		}
//                	}
                	
                	//设置广告位所在省份(包括直辖市)
                	if(hasProblem == false) {
                		if(lo.get(MediaImportAdSeatEnum.PROVINCE.getId()) == null) {
                    		lo.set(MediaImportAdSeatEnum.IMPORT_RESULT.getId(), IMPORT_FAIL);
                    		lo.set(MediaImportAdSeatEnum.IMPORT_DES.getId(), ExcelImportFailEnum.PROVINCE_NULL.getText());
                    		hasProblem = true;
                    	} else {
                    		String provinceName = String.valueOf(lo.get(MediaImportAdSeatEnum.PROVINCE.getId())).trim(); //省
                    		//判断是否是直辖市
                    		if(provinceName.contains("北京") || provinceName.contains("上海") || provinceName.contains("天津")
                    				|| provinceName.contains("重庆")) {
                    			zhiXiaShiFlag = true;
                    			if(!provinceName.endsWith("市")) {
                        			provinceName = provinceName + "市";
                        		}
                    		} else {
                    			if(provinceName.contains("内蒙")) {
                    				provinceName = "内蒙古自治区";
                    			} else if (provinceName.contains("广西")) {
                    				provinceName = "广西壮族自治区";
    							} else if (provinceName.contains("西藏")) {
    								provinceName = "西藏自治区";
    							} else if (provinceName.contains("宁夏")) {
    								provinceName = "宁夏回族自治区";
    							} else if (provinceName.contains("新疆")) {
    								provinceName = "新疆维吾尔自治区";
    							} else if (provinceName.contains("香港")) {
    								provinceName = "香港特别行政区";
    							} else if (provinceName.contains("澳门")) {
    								provinceName = "澳门特别行政区";
    							} else {
    								if(!provinceName.endsWith("省")) {
    	                    			provinceName = provinceName + "省";
    	                    		}
    							}
                    		}
                    		provinceId = provinceMap.get(provinceName);
                    		if(provinceId == null) {
                    			lo.set(MediaImportAdSeatEnum.IMPORT_RESULT.getId(), IMPORT_FAIL);
                        		lo.set(MediaImportAdSeatEnum.IMPORT_DES.getId(), ExcelImportFailEnum.PROVINCE_INVALID.getText());
                        		hasProblem = true;
                    		}
                    		info.setProvince(provinceId);
                    		buffer.append(provinceName);
    					}
                	}
                	
                	//设置广告位所在市
                	if(hasProblem == false) {
                		if(lo.get(MediaImportAdSeatEnum.CITY.getId()) == null) {
                    		if(zhiXiaShiFlag == false) {
                    			lo.set(MediaImportAdSeatEnum.IMPORT_RESULT.getId(), IMPORT_FAIL);
                        		lo.set(MediaImportAdSeatEnum.IMPORT_DES.getId(), ExcelImportFailEnum.CITY_NULL.getText());
                        		hasProblem = true;
                    		}
                    	} else {
                    		if(zhiXiaShiFlag == true) {
                    			//直辖市的city字段不存库
                    		} else {
                    			String cityName = String.valueOf(lo.get(MediaImportAdSeatEnum.CITY.getId())).trim(); //市
                        		/*if(!cityName.endsWith("市")) {
                        			cityName = cityName + "市";
                        		}*/
                        		if(zhiXiaShiFlag == true) {
                        			info.setCity(info.getProvince());
                        			cityId = provinceId;
                        		} else {
                        			List<City> cities = cityCache.getCity(provinceId);
                            		Map<String, Long> cityMap = citiesToMap(cities);
                            		Set<String> cityNames = cityMap.keySet();
                            		for (String name : cityNames) {
        								if(name.contains(cityName)) {
        									cityName = name;
        									break;
        								}
        							}
                            		cityId = cityMap.get(cityName);
                            		if(cityId == null) {
                            			lo.set(MediaImportAdSeatEnum.IMPORT_RESULT.getId(), IMPORT_FAIL);
                                		lo.set(MediaImportAdSeatEnum.IMPORT_DES.getId(), ExcelImportFailEnum.CITY_INVALID.getText());
                                		hasProblem = true;
                            		}
                            		info.setCity(cityId);
                            		buffer.append(cityName);
                        		}
                    		}
    					}
                	}
                	
                	//设置广告位所在区(县)
//                	if(hasProblem == false) {
//                		if(lo.get(5) == null) {
//                    		lo.set(17, IMPORT_FAIL);
//                    		lo.set(18, ExcelImportFailEnum.REGION_NULL.getText());
//                    		hasProblem = true;
//                    	} else {
//                    		String regionName = String.valueOf(lo.get(5)).trim(); //区
//                    		/*if(!regionName.endsWith("区")) {
//                    			regionName = regionName + "区";
//                    		}*/
//                    		if(zhiXiaShiFlag == true) {
//                    			List<City> cities = cityCache.getRegionByProvince(provinceId);
//                        		Map<String, Long> regionMap = citiesToMap(cities);
//                        		Set<String> regionNames = regionMap.keySet();
//                        		for (String name : regionNames) {
//    								if(name.contains(regionName)) {
//    									regionName = name;
//    									break;
//    								}
//    							}
//                        		regionId = regionMap.get(regionName);
//                        		if(regionId == null) {
//                        			lo.set(17, IMPORT_FAIL);
//                            		lo.set(18, ExcelImportFailEnum.REGION_INVALID.getText());
//                            		hasProblem = true;
//                        		}
//                        		info.setRegion(regionId);
//                        		buffer.append(regionName);
//                    		} else {
//                    			List<City> cities = cityCache.getRegion(cityId);
//                        		Map<String, Long> regionMap = citiesToMap(cities);
//                        		Set<String> regionNames = regionMap.keySet();
//                        		for (String name : regionNames) {
//    								if(name.contains(regionName)) {
//    									regionName = name;
//    									break;
//    								}
//    							}
//                        		regionId = regionMap.get(regionName);
//                        		if(regionId == null) {
//                        			lo.set(17, IMPORT_FAIL);
//                            		lo.set(18, ExcelImportFailEnum.REGION_INVALID.getText());
//                            		hasProblem = true;
//                        		}
//                        		info.setRegion(regionId);
//                        		buffer.append(regionName);
//                    		}
//    					}
//                	}
//                	
                	//设置广告位所在主要路段
                	if(hasProblem == false) {
                		if(lo.get(MediaImportAdSeatEnum.ROAD.getId()) == null) {
                    		lo.set(MediaImportAdSeatEnum.IMPORT_RESULT.getId(), IMPORT_FAIL);
                    		lo.set(MediaImportAdSeatEnum.IMPORT_DES.getId(), ExcelImportFailEnum.STREET_NULL.getText());
                    		hasProblem = true;
                    	} else {
                    		String road = String.valueOf(lo.get(MediaImportAdSeatEnum.ROAD.getId())).trim(); //主要路段
                    		info.setRoad(road);
                    		buffer.append(road);
                    	}
                	}
                	
                	//设置广告位详细地址
                	if(hasProblem == false) {
                		if(lo.get(MediaImportAdSeatEnum.LOCATION.getId()) == null) {
                    		lo.set(MediaImportAdSeatEnum.IMPORT_RESULT.getId(), IMPORT_FAIL);
                    		lo.set(MediaImportAdSeatEnum.IMPORT_DES.getId(), ExcelImportFailEnum.LOCATION_NULL.getText());
                    		hasProblem = true;
                    	} else {
                    		info.setLocation(String.valueOf(lo.get(MediaImportAdSeatEnum.LOCATION.getId())).trim().replaceAll("\n", "")); //详细位置
                    		buffer.append(String.valueOf(lo.get(MediaImportAdSeatEnum.LOCATION.getId())).trim().replaceAll("\n", ""));
                    		lo.set(MediaImportAdSeatEnum.LOCATION.getId(), 
                    				String.valueOf(lo.get(MediaImportAdSeatEnum.LOCATION.getId())).trim().replaceAll("\n", ""));
                    	}
                	}
                	
                	//设置媒体方广告位编号信息
                	if(hasProblem == false) {
                		if(lo.get(MediaImportAdSeatEnum.MEMO.getId()) != null) {
                			String memo = String.valueOf(lo.get(MediaImportAdSeatEnum.MEMO.getId())).trim();
                			if(memoSet.contains(memo) == true) {
                				lo.set(MediaImportAdSeatEnum.IMPORT_RESULT.getId(), IMPORT_FAIL);
                        		lo.set(MediaImportAdSeatEnum.IMPORT_DES.getId(), ExcelImportFailEnum.MEMO_DUP.getText());
                        		hasProblem = true;
                			} else {
                				info.setMemo(memo);
                				memoSet.add(memo);
                			}
                		}
                	}
                	
                	//设置唯一标识
//                	if(hasProblem == false) {
//                		Integer uniqueKeyNeed = adMediaTypeVo.getUniqueKeyNeed();
//                		
//                		if(lo.get(10) == null) {
//                			if(uniqueKeyNeed == 1) {
//                				//需要唯一标识
//                    			lo.set(17, IMPORT_FAIL);
//                        		lo.set(18, ExcelImportFailEnum.UNIQUE_KEY_NULL.getText());
//                        		hasProblem = true;
//                			}
//                		} else {
//                			info.setUniqueKey(String.valueOf(lo.get(10)).trim());
//                		}
//                	}
                	
                	//设置广告位尺寸
                	if(hasProblem == false) {
                		if(lo.get(MediaImportAdSeatEnum.LENGTH.getId()) != null && lo.get(MediaImportAdSeatEnum.WIDTH.getId()) != null) {
//                    		DecimalFormat df = new DecimalFormat("0"); // 格式化number String字符
                    		String length = String.valueOf(lo.get(MediaImportAdSeatEnum.LENGTH.getId())).trim();
                    		String width = String.valueOf(lo.get(MediaImportAdSeatEnum.WIDTH.getId())).trim();
                    		info.setAdSize(length + "*" + width); //广告位长度*广告位宽度
                    		lo.set(MediaImportAdSeatEnum.LENGTH.getId(), length);
                    		lo.set(MediaImportAdSeatEnum.WIDTH.getId(), width);
                    		double area = NumberUtil.divideInHalfUp(NumberUtil.multiply(width, length).toString(),"10000" , 3).doubleValue();
                    		info.setAdArea(area + "");
                    	} else if (lo.get(MediaImportAdSeatEnum.LENGTH.getId()) != null && lo.get(MediaImportAdSeatEnum.WIDTH.getId()) == null) {
                    		lo.set(MediaImportAdSeatEnum.IMPORT_RESULT.getId(), IMPORT_FAIL);
                    		lo.set(MediaImportAdSeatEnum.IMPORT_DES.getId(), ExcelImportFailEnum.SIZE_ONLYONE.getText());
                    		hasProblem = true;
    					} else if (lo.get(MediaImportAdSeatEnum.LENGTH.getId()) == null && lo.get(MediaImportAdSeatEnum.WIDTH.getId()) != null) {
                    		lo.set(MediaImportAdSeatEnum.IMPORT_RESULT.getId(), IMPORT_FAIL);
                    		lo.set(MediaImportAdSeatEnum.IMPORT_DES.getId(), ExcelImportFailEnum.SIZE_ONLYONE.getText());
                    		hasProblem = true;
    					} else {
    					}
                	}
                	
                	//设置面积
//                	if(hasProblem == false) {
//                		if(lo.get(10) != null) {
//                    		info.setAdArea(String.valueOf(lo.get(10)).trim());
//                    	}
//                	}
                	
                	//设置面数
                	if(hasProblem == false) {
                		if(lo.get(MediaImportAdSeatEnum.AD_NUM.getId()) != null) {
                			Double b = Double.parseDouble(String.valueOf(lo.get(MediaImportAdSeatEnum.AD_NUM.getId())).trim());
                    		info.setAdNum((new Double(b)).intValue());
                    	}
                	}
                	
                	//设置经纬度
                	if(hasProblem == false) {
                		if(lo.get(MediaImportAdSeatEnum.LON.getId()) != null && lo.get(MediaImportAdSeatEnum.LAT.getId()) == null) {
                    		lo.set(MediaImportAdSeatEnum.IMPORT_RESULT.getId(), IMPORT_FAIL);
                    		lo.set(MediaImportAdSeatEnum.IMPORT_DES.getId(), ExcelImportFailEnum.LOC_ONLYONE.getText());
                    		hasProblem = true;
                    	} else if (lo.get(MediaImportAdSeatEnum.LON.getId()) == null && lo.get(MediaImportAdSeatEnum.LAT.getId()) != null) {
                    		lo.set(MediaImportAdSeatEnum.IMPORT_RESULT.getId(), IMPORT_FAIL);
                    		lo.set(MediaImportAdSeatEnum.IMPORT_DES.getId(), ExcelImportFailEnum.LOC_ONLYONE.getText());
                    		hasProblem = true;
    					} else if (lo.get(MediaImportAdSeatEnum.LON.getId()) == null && lo.get(MediaImportAdSeatEnum.LAT.getId()) == null) {
    					} else {
    						//判断经度在-180-180之间
    						double lon = Double.parseDouble(String.valueOf(lo.get(MediaImportAdSeatEnum.LON.getId())).trim());
    						if(lon < -180 || lon > 180) {
    							lo.set(MediaImportAdSeatEnum.IMPORT_RESULT.getId(), IMPORT_FAIL);
    	                		lo.set(MediaImportAdSeatEnum.IMPORT_DES.getId(), ExcelImportFailEnum.LON_OVERFLOW.getText());
    	                		hasProblem = true;
    						}
    						//判断纬度在-90-90之间
    						double lat = Double.parseDouble(String.valueOf(lo.get(MediaImportAdSeatEnum.LAT.getId())).trim());
    						if(lat < -90 || lat > 90) {
    	                		lo.set(MediaImportAdSeatEnum.IMPORT_RESULT.getId(), IMPORT_FAIL);
    	                		lo.set(MediaImportAdSeatEnum.IMPORT_DES.getId(), ExcelImportFailEnum.LAT_OVERFLOW.getText());
    	                		hasProblem = true;
    						}
    						info.setLon(lon); //经度
    	                	info.setLat(lat); //纬度
    	                	
    	                	//地图标准
    	                	if(hasProblem == false) {
    	                		if(lo.get(MediaImportAdSeatEnum.MAP_STANDARD.getId()) != null) {
    	                			if(String.valueOf(lo.get(MediaImportAdSeatEnum.MAP_STANDARD.getId())).trim().contains("百度")) {
    	                				info.setMapStandard(MapStandardEnum.getId("百度"));
    	                			} else if(String.valueOf(lo.get(MediaImportAdSeatEnum.MAP_STANDARD.getId())).trim().contains("高德")) {
    	                				info.setMapStandard(MapStandardEnum.getId("高德"));
    	                			} else if(String.valueOf(lo.get(MediaImportAdSeatEnum.MAP_STANDARD.getId())).trim().contains("谷歌")) {
    	                				info.setMapStandard(MapStandardEnum.getId("谷歌"));
    	                			}
        	                	} else {
        	                		lo.set(MediaImportAdSeatEnum.IMPORT_RESULT.getId(), IMPORT_FAIL);
        	                		lo.set(MediaImportAdSeatEnum.IMPORT_DES.getId(), ExcelImportFailEnum.NONE_MAP.getText());
        	                		hasProblem = true;
        						}
    	                	}
    					}
                	}
                	
                	//设置联系人信息
                	if(hasProblem == false) {
                		if(lo.get(MediaImportAdSeatEnum.CONTACT_NAME.getId()) != null) {
                    		info.setContactName(String.valueOf(lo.get(MediaImportAdSeatEnum.CONTACT_NAME.getId())).trim());
                    	}
                	}
                	
                	//联系人电话
                	if(hasProblem == false) {
                		if(lo.get(MediaImportAdSeatEnum.CONTANT_CELL.getId()) != null) {
                    		info.setContactCell(String.valueOf(lo.get(MediaImportAdSeatEnum.CONTANT_CELL.getId())).trim());
                    	}
                	}
                	
                	info.setCreateTime(now);
                	info.setUpdateTime(now);
                	
                	//检查是否重复 广告位地址重复
//                	if(hasProblem == false) {
//                		if(keySet.contains(buffer.toString())) {
//                    		lo.set(MediaImportAdSeatEnum.IMPORT_RESULT.getId(), IMPORT_FAIL);
//                    		lo.set(MediaImportAdSeatEnum.IMPORT_DES.getId(), ExcelImportFailEnum.LOC_DUP.getText());
//                    		hasProblem = true;
//                    	} else {
//                    		keySet.add(buffer.toString());
//                    	}
//                	}
                	
                	if(!(StringUtils.equals(String.valueOf(lo.get(MediaImportAdSeatEnum.IMPORT_RESULT.getId())), IMPORT_FAIL))) {
                		//导入成功
                		lo.set(MediaImportAdSeatEnum.IMPORT_RESULT.getId(), IMPORT_SUCC);
                		//默认没有贴上二维码
                		info.setCodeFlag(AdCodeFlagEnum.NO.getId());
                		
//                		//生成广告位对应的二维码
//                		String adCodeInfo = mediaUser.getPrefix() + UUID.randomUUID(); //二维码存的值（媒体前缀比如media3- 加上UUID随机数）
//                		String path = request.getSession().getServletContext().getRealPath("/");
//                		path = path + (path.endsWith(File.separator)?"":File.separatorChar)+"static"+File.separatorChar+"qrcode"+File.separatorChar+adCodeInfo + ".jpg";
//                		QRcodeUtil.encode(adCodeInfo, path);
//                		info.setAdCode(adCodeInfo);
//                		info.setAdCodeUrl("/static/qrcode/" + adCodeInfo + ".jpg");
                		
                		insertAdSeatInfos.add(info);
                	}
                } else {
                	logger.error(MessageFormat.format("批量导入文件有误, 导入失败", new Object[] {}));
                	result.setCode(ResultCode.RESULT_FAILURE.getCode());
                	result.setResultDes("批量导入文件有误, 导入失败");
                    throw new ExcelException("批量导入文件有误, 导入失败");
                }
            }
            
            //正常数据插入到数据库中
            if(insertAdSeatInfos != null && insertAdSeatInfos.size() > 0){
            	adSeatService.insertBatchByExcel(insertAdSeatInfos, null, null);
            	insertAdSeatInfos.clear();
            	table.clear();
            	keySet.clear();
            }
            
            //导出到excel, 返回导入广告位信息结果
            List<List<String>> listString = objToString(listob);
            listob.clear();
            String[] titleArray = { "广告位名称", "媒体大类", "媒体小类", "省（直辖市）", "市",  "主要路段", "详细位置", 
            		"广告位编号", "广告位长度", "广告位宽度", "面数","经度", "纬度",
            		"地图标准（如百度，谷歌，高德）", "联系人姓名", "联系人电话", "导入结果", "导入错误信息"};
            ExcelTool<List<String>> excelTool = new ExcelTool<List<String>>("importResult");
//          excelTool.exportExcel(listString, titleArray, response);
            String path = request.getSession().getServletContext().getRealPath("/");
    		path = path + (path.endsWith(File.separator)?"":File.separatorChar)+"static"+File.separatorChar+"excel"+File.separatorChar+fileName;
    		excelTool.generateExcel(listString, titleArray, path);
//    		InputStream is = new FileInputStream(excelFile);
//          String filepath = saveFile(path, fileName, is);
            
            result.setCode(ResultCode.RESULT_SUCCESS.getCode());
            result.setResult("/static/excel/" + fileName);
            listString.clear();
        } catch (Exception e) {
        	logger.error(MessageFormat.format("批量导入文件有误, 导入失败", new Object[] {}));
        	result.setCode(ResultCode.RESULT_FAILURE.getCode());
        	result.setResultDes("导入失败");
            e.printStackTrace();
        }
		
        model.addAttribute(SysConst.RESULT_KEY, result);
        return model;
	}
	
	/**
	 * 【群邑方】 导入广告位模板下载
	 */
	@RequiresRoles(value = {"superadmin" , "media"}, logical = Logical.OR)
    @RequestMapping(value = "/downloadBatch")
	@ResponseBody
	public Model downloadBatch(Model model, HttpServletRequest request, HttpServletResponse response) {
		//相关返回结果
		ResultVo result = new ResultVo();
        result.setCode(ResultCode.RESULT_SUCCESS.getCode());
        result.setResultDes("查询成功");
        model = new ExtendedModelMap();
        
        result.setCode(ResultCode.RESULT_SUCCESS.getCode());
        result.setResult("/static/excel/" + "template.zip");
        
        model.addAttribute(SysConst.RESULT_KEY, result);
        return model;
	}
	
	/**
	 * 【媒体方】 导入广告位模板下载
	 */
	@RequiresRoles(value = {"superadmin" , "media"}, logical = Logical.OR)
    @RequestMapping(value = "/downloadBatchMedia")
	@ResponseBody
	public Model downloadBatchMedia(Model model, HttpServletRequest request, HttpServletResponse response) {
		//相关返回结果
		ResultVo result = new ResultVo();
        result.setCode(ResultCode.RESULT_SUCCESS.getCode());
        result.setResultDes("查询成功");
        model = new ExtendedModelMap();
        
        result.setCode(ResultCode.RESULT_SUCCESS.getCode());
        result.setResult("/static/excel/" + "template4.zip");
        
        model.addAttribute(SysConst.RESULT_KEY, result);
        return model;
	}
	
	/**
	 * 导入媒体类型模板下载
	 */
    @RequestMapping(value = "/downloadMediaTypeBatch")
	@ResponseBody
	public Model downloadMediaTypeBatch(Model model, HttpServletRequest request, HttpServletResponse response) {
		//相关返回结果
		ResultVo result = new ResultVo();
        result.setCode(ResultCode.RESULT_SUCCESS.getCode());
        result.setResultDes("查询成功");
        model = new ExtendedModelMap();
        
        result.setCode(ResultCode.RESULT_SUCCESS.getCode());
        result.setResult("/static/excel/" + "template1.zip");
        
        model.addAttribute(SysConst.RESULT_KEY, result);
        return model;
	}
    
    /**
	 * 导入媒体监测人员模板下载
	 */
    @RequestMapping(value = "/downloadMediaAppUserBatch")
	@ResponseBody
	public Model downloadMediaAppUserBatch(Model model, HttpServletRequest request, HttpServletResponse response) {
		//相关返回结果
		ResultVo result = new ResultVo();
        result.setCode(ResultCode.RESULT_SUCCESS.getCode());
        result.setResultDes("查询成功");
        model = new ExtendedModelMap();
        
        result.setCode(ResultCode.RESULT_SUCCESS.getCode());
        result.setResult("/static/excel/" + "template2.zip");
        
        model.addAttribute(SysConst.RESULT_KEY, result);
        return model;
	}
	
    /**
	 * 导入第三方监测人员模板下载
	 */
    @RequestMapping(value = "/downloadThirdCompanyUserBatch")
	@ResponseBody
	public Model downloadThirdCompanyUserBatch(Model model, HttpServletRequest request, HttpServletResponse response) {
		//相关返回结果
		ResultVo result = new ResultVo();
        result.setCode(ResultCode.RESULT_SUCCESS.getCode());
        result.setResultDes("查询成功");
        model = new ExtendedModelMap();
        
        result.setCode(ResultCode.RESULT_SUCCESS.getCode());
        result.setResult("/static/excel/" + "template3.zip");
        
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
			float width = image1.getWidth();
			float height = image1.getHeight();
 	    	//合理压缩，height>width，按width压缩，否则按width压缩
 	    	float percent = getPercent(height, width);
			image1.setAlignment(Image.ALIGN_CENTER);
			image1.scalePercent(percent);//依照比例缩放
			image1.scaleAbsolute(width*percent,percent*height);//控制图片大小
			image1.setAbsolutePosition(760,583);//控制图片位置
			document.add(image1);
		}
		
		if(!StringUtils.isEmpty(feedback.getPicUrl2())) {
			Image image2 = Image.getInstance(feedback.getPicUrl2());
			image2.setAlignment(Image.ALIGN_CENTER);
			float width = image2.getWidth();
			float height = image2.getHeight();
			//合理压缩，height>width，按width压缩，否则按width压缩
 	    	float percent = getPercent(height, width);
			image2.scalePercent(percent);//依照比例缩放
			image2.scaleAbsolute(width*percent,percent*height);//控制图片大小
			image2.setAbsolutePosition(1280,583);//控制图片位置
			document.add(image2);
		}
		
		if(!StringUtils.isEmpty(feedback.getPicUrl3())) {
			Image image3 = Image.getInstance(feedback.getPicUrl3());
			image3.setAlignment(Image.ALIGN_CENTER);
			float width = image3.getWidth();
			float height = image3.getHeight();
			//合理压缩，height>width，按width压缩，否则按width压缩
 	    	float percent = getPercent(height, width);
			image3.scalePercent(percent);//依照比例缩放
			image3.scaleAbsolute(width*percent,percent*height);//控制图片大小
			image3.setAbsolutePosition(760,180);//控制图片位置
			document.add(image3);
		}
		
		if(!StringUtils.isEmpty(feedback.getPicUrl4())) {
			Image image4 = Image.getInstance(feedback.getPicUrl4());
			image4.setAlignment(Image.ALIGN_CENTER);
			float width = image4.getWidth();
			float height = image4.getHeight();
			//合理压缩，height>width，按width压缩，否则按width压缩
 	    	float percent = getPercent(height, width);
			image4.scalePercent(percent);//依照比例缩放
			image4.scaleAbsolute(width*percent,percent*height);//控制图片大小
			image4.setAbsolutePosition(1280,180);//控制图片位置
			document.add(image4);
		}
	}
	
	public static float getPercent(float h,float w)
	{
		float p=0.0f;
		if(h>w)
		{
			p=330/h;
		}
		else
		{
			p=430/w;
		}
		return p;
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
	/**
	 * 【群邑方】 导入监测任务模板下载
	 */
	@RequiresRoles(value = {"superadmin"}, logical = Logical.OR)
    @RequestMapping(value = "/downMonitorBatch")
	@ResponseBody
	public Model downMonitorBatch(Model model, HttpServletRequest request, HttpServletResponse response) {
		//相关返回结果
		ResultVo result = new ResultVo();
        result.setCode(ResultCode.RESULT_SUCCESS.getCode());
        result.setResultDes("查询成功");
        model = new ExtendedModelMap();
        
        result.setCode(ResultCode.RESULT_SUCCESS.getCode());
        result.setResult("/static/excel/" + "template5.zip");
        
        model.addAttribute(SysConst.RESULT_KEY, result);
        return model;
	}
	
	/**
	 * 批量导入监测任务
	 * @param activityId 活动id
	 */
	@RequiresRoles(value = {"superadmin"})
    @RequestMapping(value = "/insertTaskBatchByExcel")
	@ResponseBody
	public Model insertTaskBatchByExcel(Model model, HttpServletRequest request, HttpServletResponse response,
                                     @RequestParam(value = "excelFile", required = false) MultipartFile file,
                                     @RequestParam(value = "activityId", required = false) Integer activityId) throws Exception {
    	ResultVo result = new ResultVo();
        result.setCode(ResultCode.RESULT_SUCCESS.getCode());
        result.setResultDes("查询成功");
        model = new ExtendedModelMap();
        AdActivityVo activity = adActivityService.getVoById(activityId);
        if (activity==null) {
        	result.setCode(ResultCode.RESULT_FAILURE.getCode());
            result.setResultDes("活动不存在");
            model.addAttribute(SysConst.RESULT_KEY, result);
			return model;
		}
        //获取登录用户信息
        SysUser user = (SysUser) ShiroUtils.getSessionAttribute(SessionKey.SESSION_LOGIN_USER.toString());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    	InputStream in = file.getInputStream();
    	try {
			if (file.isEmpty()) {
        		throw new ExcelException("批量导入文件不能为空, 导入失败");
			}
	        List<List<Object>> listob = new ArrayList<List<Object>>();
	        //excel上传支持
	        listob = new ImportExcelUtil().getBankListByExcel(in, file.getOriginalFilename());
	        //文件夹图片
	        Map<String, Map<String, List<FileInfoVo>>> pics = getPics(activityId);
	        List<AdMonitorTaskVo> tasks = new ArrayList<>();
	        Map<String, List<FileInfoVo>> imgs = null;
	        Table<String, String, Integer> tableData = HashBasedTable.create();
	        //excel表格任务对应文件夹图片   
	        for (int i = 1; i < listob.size(); i++) {
	            List<Object> lo = listob.get(i);
	            String seatName = (String) lo.get(AdminImportMonitorEnum.ADSEAT_MEMO.getId());
	            String mobile = (String) lo.get(AdminImportMonitorEnum.TASK_USER.getId());
	            String task_type = (String) lo.get(AdminImportMonitorEnum.TASK_TYPE.getId());
	            Integer id = MonitorTaskType.getId(task_type);
	            if (seatName==null) {
	            	lo.set(AdminImportMonitorEnum.IMPORT_RESULT.getId(), IMPORT_FAIL);
	            	lo.set(AdminImportMonitorEnum.IMPORT_DES.getId(), ExcelImportFailEnum.ADNAME_MEMO_NULL.getText());
	            	continue;
				}
	            if (mobile==null) {
	            	lo.set(AdminImportMonitorEnum.IMPORT_RESULT.getId(), IMPORT_FAIL);
	            	lo.set(AdminImportMonitorEnum.IMPORT_DES.getId(), ExcelImportFailEnum.TASK_USER_NULL.getText());
	            	continue;
				}
	            if (task_type==null) {
	            	lo.set(AdminImportMonitorEnum.IMPORT_RESULT.getId(), IMPORT_FAIL);
	            	lo.set(AdminImportMonitorEnum.IMPORT_DES.getId(), ExcelImportFailEnum.TASK_TYPE_NULL.getText());
	            	continue;
				}
	            if (id==null) {
	            	lo.set(AdminImportMonitorEnum.IMPORT_RESULT.getId(), IMPORT_FAIL);
	            	lo.set(AdminImportMonitorEnum.IMPORT_DES.getId(), ExcelImportFailEnum.TASK_TYPE_INVALID.getText());
					continue;
				}
	            if (tableData.get(seatName, task_type)!=null) {
	            	lo.set(AdminImportMonitorEnum.IMPORT_RESULT.getId(), IMPORT_FAIL);
	            	lo.set(AdminImportMonitorEnum.IMPORT_DES.getId(), ExcelImportFailEnum.TASK_DUP.getText());
					continue;
				}
	            tableData.put(seatName, task_type, i);
	            imgs = pics.get(id.toString());
	            if (imgs!=null&&imgs.size()>0) {
					List<FileInfoVo> list = imgs.get(seatName);
					if (list!=null&&list.size()>0) {
						AdMonitorTaskVo adMonitorTaskVo = new AdMonitorTaskVo();
						adMonitorTaskVo.setMemo(seatName);
						adMonitorTaskVo.setTaskType(id);
						adMonitorTaskVo.setMobile(mobile);
						adMonitorTaskVo.setVerifyTime(list.get(0).getTime());
						for (int j = 0; j < list.size(); j++) {
							String path = list.get(j).getPath().substring(list.get(j).getPath().indexOf(":")+1, list.get(j).getPath().length()).replaceAll("\\\\", "/");
							path = path.replaceFirst("/opt/", "/");
							if (j==0) {
								adMonitorTaskVo.setPicUrl1(fileUploadIp + path);
							}else if (j==1) {
								adMonitorTaskVo.setPicUrl2(fileUploadIp + path);
							}else if (j==2) {
								adMonitorTaskVo.setPicUrl3(fileUploadIp + path);
							}else if (j==3) {
								adMonitorTaskVo.setPicUrl4(fileUploadIp + path);
							}
						}
						tasks.add(adMonitorTaskVo);
					}else {
						lo.set(AdminImportMonitorEnum.IMPORT_RESULT.getId(), IMPORT_FAIL);
		            	lo.set(AdminImportMonitorEnum.IMPORT_DES.getId(), ExcelImportFailEnum.PIC_INVALID.getText());
					}
				}else {
					lo.set(AdminImportMonitorEnum.IMPORT_RESULT.getId(), IMPORT_FAIL);
	            	lo.set(AdminImportMonitorEnum.IMPORT_DES.getId(), ExcelImportFailEnum.PIC_INVALID.getText());
				}
	        }
	        Table<String, Integer, AdMonitorTaskVo> table = getTaskTable(tasks);
	        List<AdMonitorTaskVo> TaskVos = new ArrayList<>();
	        if (tasks.size()>0) {
	        	TaskVos = adMonitorTaskService.findAllMemo(activity.getId(),tasks);
			}
	        List<AdMonitorTaskVo> Tasks = new ArrayList<>();
	        Map<Integer, String> excelInfo = new HashMap<>();
	        for (int j = 0; j < TaskVos.size(); j++) {
	        		AdMonitorTaskVo taskVo = TaskVos.get(j);
	        	if (taskVo.getStatus()==MonitorTaskStatus.VERIFIED.getId()) {
	        		Integer index = tableData.get(taskVo.getMemo(),MonitorTaskType.getText(taskVo.getTaskType()));
	        		excelInfo.put(index, ExcelImportFailEnum.TASK_VERIFY.getText());
					continue;
				}
				AdMonitorTaskVo adMonitorTaskVo = table.get(taskVo.getMemo(), taskVo.getTaskType());
				if (adMonitorTaskVo!=null) {
					taskVo = replaceTaskAttr(taskVo,adMonitorTaskVo);
					taskVo.setAssignorId(user.getId());
					taskVo.setFeedbackStatus(MonitorTaskFeedBackStatus.VALID_FEEDBACK.getId());
					SysUserExecute userExecute = sysUserExecuteService.getByUsername(adMonitorTaskVo.getMobile());
					if (userExecute==null) {
						Integer index = tableData.get(taskVo.getMemo(),MonitorTaskType.getText(taskVo.getTaskType()));
		        		excelInfo.put(index, ExcelImportFailEnum.TASK_VERIFY.getText());
						continue;
					}
					taskVo.setUserId(userExecute.getId());
					Tasks.add(taskVo);
				}
			}
	        if (Tasks.size()>0) {
	        	adMonitorTaskService.updateBatch(Tasks);
			}
	        
	        for (Integer index : excelInfo.keySet()) {
	        	List<Object> lo = listob.get(index);
	        	lo.set(AdminImportMonitorEnum.IMPORT_RESULT.getId(), IMPORT_FAIL);
	        	lo.set(AdminImportMonitorEnum.IMPORT_DES.getId(), excelInfo.get(index));
			}
	        
	        for (int i = 1; i < listob.size(); i++) {
	        	List<Object> lo = listob.get(i);
	        	if (excelInfo.get(i)!=null) {
	        		lo.set(AdminImportMonitorEnum.IMPORT_RESULT.getId(), IMPORT_FAIL);
	            	lo.set(AdminImportMonitorEnum.IMPORT_DES.getId(), excelInfo.get(i));
				}
	        	if (!(StringUtils.equals(String.valueOf(lo.get(AdminImportMonitorEnum.IMPORT_RESULT.getId())), IMPORT_FAIL))) {
	        		lo.set(AdminImportMonitorEnum.IMPORT_RESULT.getId(), IMPORT_SUCC);
				}
	        }
	        
	        
	        final String fileName = "导入监测任务结果-" + System.currentTimeMillis() + ".xls"; //导出文件名
	        //导出到excel, 返回导入广告位信息结果
	        List<List<String>> listString = objToString(listob);
	        listob.clear();
	        String[] titleArray = { "广告位编号", "任务执行人", "任务类型", "导入结果", "导入错误信息"};
	        ExcelTool<List<String>> excelTool = new ExcelTool<List<String>>("importResult");
	        String path = request.getSession().getServletContext().getRealPath("/");
			path = path + (path.endsWith(File.separator)?"":File.separatorChar)+"static"+File.separatorChar+"excel"+File.separatorChar+fileName;
			excelTool.generateExcel(listString, titleArray, path);
	        
	        result.setCode(ResultCode.RESULT_SUCCESS.getCode());
	        result.setResult("/static/excel/" + fileName);
	        listString.clear();
	        model.addAttribute(SysConst.RESULT_KEY, result);
    	}catch (Exception e) {
    		result.setCode(ResultCode.RESULT_FAILURE.getCode());
        	result.setResultDes("导入失败");
            e.printStackTrace();
		}
        return model;
	}
	/**
	 * 读取文件夹图片
	 */
    private Map<String, Map<String, List<FileInfoVo>>> getPics(Integer activityId){
    	File file=new File(fileUploadPath + "\\activity\\" + activityId + "\\temporary");
		File[] Files = file.listFiles();
		Map<String, Map<String, List<FileInfoVo>>> monitors = new HashMap<>();
		String regex = ".+(.JPEG|.jpeg|.JPG|.jpg|.png|.PNG)$";
		for (int i = 0; i < Files.length; i++) {
			File file2 = Files[i];
			if (file2.isDirectory()) {
				File[] fileList = file2.listFiles();
				Map<String, List<FileInfoVo>> fileNames = new HashMap<>();
				for (int k = 0; k < fileList.length; k++) {
					File file3 = fileList[k];
					if (file3.isDirectory()) {
						File[] listFiles = file3.listFiles();
						List<FileInfoVo> names = new ArrayList<>();
						for (int j = 0; j < listFiles.length; j++) {
							if (!listFiles[j].isDirectory()) {
								if (Pattern.compile(regex).matcher(listFiles[j].getName().toString()).matches()) {
									try {
										FileInfoVo fileInfoVo = new FileInfoVo();
										fileInfoVo.setPath(listFiles[j].getCanonicalPath());
										long lastModified = file.lastModified();
										fileInfoVo.setTime(new Date(lastModified));
										names.add(fileInfoVo);
									} catch (IOException e) {
										e.printStackTrace();
									}
								}
							}
						}
						if (names.size()>0) {
							fileNames.put(file3.getName(), names);
						}
					}
				}
				if (fileNames.size()>0) {
					monitors.put(file2.getName(), fileNames);
				}
			}
		}
		return monitors;
    }
    /**
     * 替换监测任务属性
     */
    private AdMonitorTaskVo replaceTaskAttr(AdMonitorTaskVo taskVo,AdMonitorTaskVo newTaskVo) {
    	taskVo.setPicUrl1(newTaskVo.getPicUrl1());
    	taskVo.setPicUrl2(newTaskVo.getPicUrl2());
    	taskVo.setPicUrl3(newTaskVo.getPicUrl3());
    	taskVo.setPicUrl4(newTaskVo.getPicUrl4());
    	taskVo.setPicUrl1Status(VerifyType.PASS_TYPE.getId());
    	taskVo.setPicUrl2Status(VerifyType.PASS_TYPE.getId());
    	taskVo.setPicUrl3Status(VerifyType.PASS_TYPE.getId());
    	taskVo.setPicUrl4Status(VerifyType.PASS_TYPE.getId());
    	taskVo.setStatus(MonitorTaskStatus.VERIFIED.getId());
    	taskVo.setProblemStatus(TaskProblemStatus.NO_PROBLEM.getId());
    	taskVo.setVerifyTime(newTaskVo.getVerifyTime());
    	taskVo.setCreateTime(newTaskVo.getVerifyTime());
    	taskVo.setUpdateTime(newTaskVo.getVerifyTime());
    	taskVo.setFeedbackStatus(MonitorTaskFeedBackStatus.VALID_FEEDBACK.getId());
    	return taskVo;
    }
	/**
	 * 设置以广告位编号, 任务类型为Key, AdMonitorTaskVo为Value的集合
	 * @param taskVos
	 * @return
	 */
	private Table<String, Integer, AdMonitorTaskVo> getTaskTable(List<AdMonitorTaskVo> taskVos) {
		Table<String, Integer, AdMonitorTaskVo> table = HashBasedTable.create();
		for (AdMonitorTaskVo adMonitorTaskVo : taskVos) {
			table.put(adMonitorTaskVo.getMemo(), adMonitorTaskVo.getTaskType(), adMonitorTaskVo);
		}
		return table;
	}
}
