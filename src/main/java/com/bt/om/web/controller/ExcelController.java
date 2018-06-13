package com.bt.om.web.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.bt.om.entity.AdMedia;
import com.bt.om.entity.AdMediaType;
import com.bt.om.entity.AdMonitorTask;
import com.bt.om.entity.AdMonitorTaskFeedback;
import com.bt.om.entity.AdSeatInfo;
import com.bt.om.entity.City;
import com.bt.om.entity.SysUser;
import com.bt.om.entity.vo.AdActivityAdseatTaskVo;
import com.bt.om.entity.vo.AdMediaTypeVo;
import com.bt.om.entity.vo.SysUserVo;
import com.bt.om.enums.AdMediaInfoStatus;
import com.bt.om.enums.ExcelImportFailEnum;
import com.bt.om.enums.MapStandardEnum;
import com.bt.om.enums.ResultCode;
import com.bt.om.enums.SessionKey;
import com.bt.om.exception.web.ExcelException;
import com.bt.om.mapper.AdMediaMapper;
import com.bt.om.security.ShiroUtils;
import com.bt.om.service.IAdActivityService;
import com.bt.om.service.IAdCustomerTypeService;
import com.bt.om.service.IAdMediaTypeService;
import com.bt.om.service.IAdMonitorTaskService;
import com.bt.om.service.IAdSeatService;
import com.bt.om.service.ISysUserService;
import com.bt.om.util.ExcelTool;
import com.bt.om.util.ImportExcelUtil;
import com.bt.om.util.QRcodeUtil;
import com.bt.om.vo.web.ResultVo;
import com.bt.om.web.BasicController;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * Created by jiayong.mao on 2018/4/4.
 */
@Controller
@RequestMapping(value = "/excel")
public class ExcelController extends BasicController {
	private static final String importSucc = "导入成功";
	private static final String importFail = "导入失败";
	
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
	private IAdCustomerTypeService adCustomerTypeService;
	
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
	 */
	@RequiresRoles(value = {"superadmin", "activityadmin", "depactivityadmin", "admin" , "customer"}, logical = Logical.OR)
    @RequestMapping(value = "/exportAdMediaPdf")
	@ResponseBody
	public Model exportPdf(Model model, HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "activityId", required = false) Integer activityId) {
		System.setProperty("sun.jnu.encoding", "utf-8");
		//相关返回结果
		ResultVo result = new ResultVo();
        result.setCode(ResultCode.RESULT_SUCCESS.getCode());
        result.setResultDes("查询成功");
        model = new ExtendedModelMap();
		Date now = new Date();
		
		//查询媒体类型
		List<AdMediaType> allAdMediaType = adMediaTypeService.getAll();
		Map<Integer, String> mediaTypeMap = new HashMap<>();
		for (AdMediaType adMediaType : allAdMediaType) {
			mediaTypeMap.put(adMediaType.getId(), adMediaType.getName());
		}
		
		//导出文件相关
		AdActivity adActivity = adActivityService.getById(activityId);
//		AdCustomerType customerType = adCustomerTypeService.getById(adActivity.getCustomerTypeId()); //客户类型
// 		final String fileName = adActivity.getActivityName() + "-广告位导出结果"+ ".pdf"; //导出文件名
		final String fileName = adActivity.getId() + "-" + System.currentTimeMillis() + ".pdf"; //导出文件名
 		List<List<String>> listString = new ArrayList<>();
        Map<Integer, List<String>> map = new HashMap<>();
        Document document = new Document(PageSize.LEDGER);
        
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
		    //Header  
	        float y = document.top(368); 
			cb.beginText();  
			cb.setFontAndSize(bfChinese, 54);  
			cb.showTextAligned(PdfContentByte.ALIGN_CENTER, adActivity.getActivityName(), (document.right() + document.left())/2, y, 0);
			cb.endText();
			
			cb = writer.getDirectContent();
			cb.beginText();  
			cb.setFontAndSize(bfChinese, 26);  
			cb.showTextAligned(PdfContentByte.ALIGN_RIGHT, DateUtil.dateFormate(now, "yyyy-MM-dd"), 1000, 150, 0);
			cb.endText();
			
			//查询活动对应的 广告位信息+监测时间+是否有问题
			document.newPage();
        	List<AdActivityAdseatTaskVo> vos = adActivityService.selectAdActivityAdseatTaskReport(activityId);
        	for (AdActivityAdseatTaskVo vo : vos) {
				List<String> list = new ArrayList<>();
				list.add(adActivity.getActivityName()); //活动名称 0
				list.add(vo.getInfo_name()); //广告位名称 1
				list.add(cityCache.getCityName(vo.getInfo_province())); //省 2
				list.add(cityCache.getCityName(vo.getInfo_city())); //市 3
//				list.add(cityCache.getCityName(vo.getInfo_region())); //区（县） 4
				list.add(cityCache.getCityName(vo.getInfo_street())); //主要路段 5  
				list.add(vo.getInfo_location()); //详细位置 6
				list.add(vo.getInfo_memo()); //媒体方广告位编号 7
//				list.add(vo.getInfo_uniqueKey()); //唯一标识 7
				list.add(DateUtil.dateFormate(vo.getMonitorStart(), "yyyy-MM-dd")); //开始监测时间 8
				list.add(DateUtil.dateFormate(vo.getMonitorEnd(), "yyyy-MM-dd")); //结束监测时间 9
				String status = AdMediaInfoStatus.WATCHING.getText(); //当前状态 10
				if(vo.getProblem_count() > 0) {
					status = AdMediaInfoStatus.HAS_PROBLEM.getText();
				}
				if(vo.getMonitorStart().getTime() > now.getTime()) {
					status = AdMediaInfoStatus.NOT_BEGIN.getText();
				}
				if(vo.getMonitorEnd().getTime() < now.getTime()) {
					status = AdMediaInfoStatus.FINISHED.getText();
	        	}
				list.add(status);
				list.add(vo.getInfo_adSize()); //尺寸 11
				list.add(vo.getInfo_adArea()); //面积 12
				list.add(vo.getInfo_adNum()+"");//面数13
				list.add(vo.getInfo_lon() + ""); //经度 14
				list.add(vo.getInfo_lat() + ""); //纬度 15
				if(vo.getInfo_mapStandard() != null) {
					list.add(MapStandardEnum.getText(vo.getInfo_mapStandard())); //地图标准 16
				} else {
					list.add(null);
				}
				list.add(vo.getInfo_contactName()); //联系人姓名 17
				list.add(vo.getInfo_contactCell()); //联系人电话 18
				list.add(mediaTypeMap.get(vo.getInfo_mediaTypeParentId())); //媒体大类 20
				list.add(mediaTypeMap.get(vo.getInfo_mediaTypeId())); //媒体小类 21
				list.add(vo.getMediaName()); //媒体名称22
//				if(customerType != null) {
//					list.add(customerType.getName()); //客户类型22
//				} else {
//					list.add(null); //客户类型22
//				}
				
				map.put(vo.getId(), list); //ad_activity_adseat的id
				listString.add(list);
			}
        	
        	//[2] 生成pdf表格页
			PdfPTable table = createTable1(listString);
			document.add(table);
        	
			//[3] 生成pdf图片页
			List<Integer> ids = new ArrayList<>();
			List<Integer> activityAdseatIds = new ArrayList<>();
			//查询每个广告位最新的一条监测任务
            List<AdMonitorTask> tasks = adMonitorTaskService.selectLatestMonitorTaskIds(activityId);
            for (AdMonitorTask task : tasks) {
            	ids.add(task.getId()); //ad_monitor_task的id
            	activityAdseatIds.add(task.getActivityAdseatId()); //ad_activity_adseat的id
			}
            //查询上述监测任务有效的一条反馈
            if(ids.size() > 0) {
            	List<AdMonitorTaskFeedback> taskFeedbacks = adMonitorTaskService.selectByActivity(ids);
                for (AdMonitorTaskFeedback feedback : taskFeedbacks) {
                	//生成广告位图片信息页, 每个广告位一页
        			document.newPage();
        			List<String> list = map.get(activityAdseatIds.get(ids.indexOf(feedback.getMonitorTaskId())));
        			createPage(document, list, feedback, request);
    			}
            }
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
	 */
	@RequiresRoles(value = {"superadmin", "activityadmin", "depactivityadmin", "admin" , "customer"}, logical = Logical.OR)
    @RequestMapping(value = "/exportAdMediaInfo")
	@ResponseBody
	public Model exportAdMediaInfo(Model model, HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "activityId", required = false) Integer activityId) {
		System.setProperty("sun.jnu.encoding", "utf-8");
		
		//相关返回结果
		ResultVo result = new ResultVo();
        result.setCode(ResultCode.RESULT_SUCCESS.getCode());
        result.setResultDes("查询成功");
        model = new ExtendedModelMap();
		Date now = new Date();
		
		//查询媒体类型
		List<AdMediaType> allAdMediaType = adMediaTypeService.getAll();
		Map<Integer, String> mediaTypeMap = new HashMap<>();
		for (AdMediaType adMediaType : allAdMediaType) {
			mediaTypeMap.put(adMediaType.getId(), adMediaType.getName());
		}
		
		//导出文件相关
		AdActivity adActivity = adActivityService.getById(activityId); //活动
//		AdCustomerType customerType = adCustomerTypeService.getById(adActivity.getCustomerTypeId()); //客户类型
// 		final String fileName = adActivity.getActivityName() + "-广告位导出结果"+ ".xls"; //导出文件名
 		final String fileName = adActivity.getId() + "-" + System.currentTimeMillis() + ".xls"; //导出文件名
        List<List<String>> listString = new ArrayList<>();
        
        try {
        	List<AdActivityAdseatTaskVo> vos = adActivityService.selectAdActivityAdseatTaskReport(activityId);
        	for (AdActivityAdseatTaskVo vo : vos) {
				List<String> list = new ArrayList<>();
				list.add(adActivity.getActivityName()); //活动名称
//				list.add(customerType.getName()); //客户类型
				list.add(vo.getInfo_name()); //广告位名称
				list.add(vo.getMediaName()); //媒体名称
				list.add(mediaTypeMap.get(vo.getInfo_mediaTypeParentId())); //媒体大类
				list.add(mediaTypeMap.get(vo.getInfo_mediaTypeId())); //媒体小类
				list.add(cityCache.getCityName(vo.getInfo_province())); //省
				list.add(cityCache.getCityName(vo.getInfo_city())); //市
//				list.add(cityCache.getCityName(vo.getInfo_region())); //区（县）
				list.add(cityCache.getCityName(vo.getInfo_street())); // 主要路段
				list.add(vo.getInfo_location()); //详细位置
				list.add(vo.getInfo_memo()); //媒体方广告位编号
//				list.add(vo.getInfo_uniqueKey()); //唯一标识
				list.add(DateUtil.dateFormate(vo.getMonitorStart(), "yyyy-MM-dd")); //开始监测时间
				list.add(DateUtil.dateFormate(vo.getMonitorEnd(), "yyyy-MM-dd")); //结束监测时间
				String status = AdMediaInfoStatus.WATCHING.getText(); //当前状态
				if(vo.getProblem_count() > 0) {
					status = AdMediaInfoStatus.HAS_PROBLEM.getText();
				}
				if(vo.getMonitorStart().getTime() > now.getTime()) {
					status = AdMediaInfoStatus.NOT_BEGIN.getText();
				}
				if(vo.getMonitorEnd().getTime() < now.getTime()) {
					status = AdMediaInfoStatus.FINISHED.getText();
	        	}
				list.add(status); //当前状态
				list.add(vo.getInfo_adSize()); //广告位尺寸
				list.add(vo.getInfo_adArea()); //面积
				list.add(vo.getInfo_lon() + ""); //经度
				list.add(vo.getInfo_lat() + ""); //纬度
				list.add(vo.getInfo_adNum() + "");//面数
				list.add(MapStandardEnum.getText(vo.getInfo_mapStandard())); //地图标准（如百度，谷歌，高德）
				list.add(vo.getInfo_contactName()); //联系人姓名
				list.add(vo.getInfo_contactCell()); //联系人电话
				
				listString.add(list);
			}
        	
            String[] titleArray = {"活动名称", "广告位名称", "供应商（媒体）", "媒体大类", "媒体小类", "省", "市",  "主要路段", "详细位置", "媒体方广告位编号", 
            		"开始监测时间", "结束监测时间", "当前状态",
            		"广告位尺寸", "面积", "经度", "纬度", "面数","地图标准（如百度，谷歌，高德）", "联系人姓名", "联系人电话"};
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
	 * 批量插入广告位
	 */
	@RequiresRoles(value = {"superadmin" , "media"}, logical = Logical.OR)
    @RequestMapping(value = "/insertBatch")
	@ResponseBody
	public Model insertBatchByExcel(Model model, HttpServletRequest request, HttpServletResponse response,
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
    	SysUserVo mediaUser = new SysUserVo();
		Integer usertype = user.getUsertype();
		if(usertype == 4 || usertype == 5 || usertype == 1) {
			//后台管理人员帮助媒体导入
			media = adMediaMapper.selectByPrimaryKey(mediaId);
			mediaUser = sysUserService.findUserinfoById(media.getUserId());
		} else if (usertype == 3) {
			//媒体人员自行导入
			media = adMediaMapper.selectByUserId(user.getId());
			mediaUser = sysUserService.findUserinfoById(media.getUserId());
		}
		
		//获取需要插入广告位信息的媒体已经存在的广告位信息
		List<AdSeatInfo> adSeatsByMediaId = adSeatService.getAdSeatByMediaId(media.getId());
		List<String> databaseAdSeats = new ArrayList<>();
		for (AdSeatInfo adSeatInfo : adSeatsByMediaId) {
			StringBuffer buffer = new StringBuffer();
			if(adSeatInfo.getProvince() != null) {
				buffer.append(cityCache.getCityName(adSeatInfo.getProvince())); //省
			}
			if(adSeatInfo.getCity() != null) {
				buffer.append(cityCache.getCityName(adSeatInfo.getCity())); //市
			}
//			if(adSeatInfo.getRegion() != null) {
//				buffer.append(cityCache.getCityName(adSeatInfo.getRegion())); //区
//			}
//			if(adSeatInfo.getStreet() != null) {
//				buffer.append(cityCache.getCityName(adSeatInfo.getStreet())); //街道 ->主要路段
//			}
			buffer.append(adSeatInfo.getLocation()); //详细位置
			databaseAdSeats.add(buffer.toString());
		}
		Set<String> keySet = new HashSet<>(databaseAdSeats);
		
		//获取媒体类型
		List<AdMediaTypeVo> adMediaTypeVos = adMediaTypeService.selectParentAndSecond();
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
            List<AdSeatInfo> insertAdSeatInfos = new ArrayList<AdSeatInfo>(); //需要导入到数据库中的广告位信息
            
            //excel列表
            for (int i = 1; i < listob.size(); i++) {
                List<Object> lo = listob.get(i);
                //广告位名称, 媒体大类, 媒体小类, 是否允许多个活动（是或否）, 允许活动数量, 省（直辖市）, 市,  主要路段, 
                //详细位置, , 媒体方广告位编号， 广告位长度, 广告位宽度, 面积, 经度, 纬度,面数， 地图标准（如百度，谷歌，高德）, 联系人姓名, 联系人电话, 导入结果, 导入错误信息
                if(lo.size() <= 21){
                	AdSeatInfo info = new AdSeatInfo();
                	Long provinceId = 0L;
                	Long cityId = 0L;
                	Long regionId = 0L;
                	Boolean zhiXiaShiFlag = false;
                	Boolean hasProblem = false;
                	StringBuffer buffer = new StringBuffer();
                	AdMediaTypeVo adMediaTypeVo = null;
                	
                	//设置广告位名称
                	if(lo.get(0) == null) {
                		/*logger.error(MessageFormat.format("批量导入文件广告位名称不能为空, 导入失败", new Object[] {}));
                		throw new ExcelException("批量导入文件广告位名称不能为空, 导入失败");*/
                		lo.set(19, importFail);
                		lo.set(20, ExcelImportFailEnum.ADNAME_NULL.getText());
                		hasProblem = true;
                	} else {
                		info.setName(String.valueOf(lo.get(0)).trim()); //广告位名称
                	}
                	
                	//设置媒体大类
                	if(hasProblem == false) {
                		if(lo.get(1) == null) {
                    		lo.set(19, importFail);
                    		lo.set(20, ExcelImportFailEnum.PARENT_NULL.getText());
                    		hasProblem = true;
                    	}
                	}
                	
                	//设置媒体小类
                	if(hasProblem == false) {
                		if(lo.get(2) == null) {
                    		lo.set(19, importFail);
                    		lo.set(20, ExcelImportFailEnum.SECOND_NULL.getText());
                    		hasProblem = true;
                    	} else {
                    		String parentName = String.valueOf(lo.get(1)).trim();
                    		String secondName = String.valueOf(lo.get(2)).trim();
                    		adMediaTypeVo = table.get(parentName, secondName);
                    		if(adMediaTypeVo != null) {
                    			info.setMediaTypeParentId(adMediaTypeVo.getParentId());
                    			info.setMediaTypeId(adMediaTypeVo.getId());
                    		} else {
                    			lo.set(19, importFail);
                        		lo.set(20, ExcelImportFailEnum.MEDIA_TYPE_INVALID.getText());
                        		hasProblem = true;
                    		}
    					}
                	}
                	
                	//设置是否允许多个活动（是或否）不填写则默认为否
                	if(hasProblem == false) {
                		if(lo.get(3) == null) {
                			info.setAllowMulti(0); //不填写则默认为否
                		} else {
                			String allowMulti = String.valueOf(lo.get(3)).trim();
                			if(StringUtil.equals(allowMulti, "是")) {
                				info.setAllowMulti(1); //1：是
                			} else if(StringUtil.equals(allowMulti, "否")) {
                				info.setAllowMulti(0); //0：否
                			} else {
                				info.setAllowMulti(0); //0：否
                			}
                		}
                	}
                	
                	//设置允许活动数量, 不填写默认为1
                	if(hasProblem == false) {
                		if(lo.get(4) == null) {
                			info.setMultiNum(1); //不填写默认为1
                		} else {
                			if(info.getAllowMulti() == 0) {
                				info.setMultiNum(1); //allowMulti为0代表不允许多个活动, 设置活动数量为1
                			} else {
                				String multiNumStr = String.valueOf(lo.get(4)).trim();
                				try {
                					Double multiNumDou = new Double(multiNumStr);
                					Integer multiNum = multiNumDou.intValue();
                					info.setMultiNum(multiNum);
								} catch (Exception e) {
									lo.set(19, importFail);
	                        		lo.set(20, ExcelImportFailEnum.NUM_INVALID.getText());
	                        		hasProblem = true;
								}
                			}
                		}
                	}
                	
                	//设置广告位所在省份(包括直辖市)
                	if(hasProblem == false) {
                		if(lo.get(5) == null) {
                    		/*logger.error(MessageFormat.format("批量导入文件省份不能为空, 导入失败", new Object[] {}));
                    		throw new ExcelException("批量导入文件省份不能为空, 导入失败");*/
                    		lo.set(19, importFail);
                    		lo.set(20, ExcelImportFailEnum.PROVINCE_NULL.getText());
                    		hasProblem = true;
                    	} else {
                    		String provinceName = String.valueOf(lo.get(5)).trim(); //省
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
                    			lo.set(19, importFail);
                        		lo.set(20, ExcelImportFailEnum.PROVINCE_INVALID.getText());
                        		hasProblem = true;
                    		}
                    		info.setProvince(provinceId);
                    		buffer.append(provinceName);
    					}
                	}
                	
                	//设置广告位所在市
                	if(hasProblem == false) {
                		if(lo.get(6) == null) {
                    		if(zhiXiaShiFlag == false) {
                    			/*logger.error(MessageFormat.format("批量导入文件市不能为空, 导入失败", new Object[] {}));
                        		throw new ExcelException("批量导入文件市不能为空, 导入失败");*/
                    			lo.set(19, importFail);
                        		lo.set(20, ExcelImportFailEnum.CITY_NULL.getText());
                        		hasProblem = true;
                    		}
                    	} else {
                    		if(zhiXiaShiFlag == true) {
                    			//直辖市的city字段不存库
                    		} else {
                    			String cityName = String.valueOf(lo.get(6)).trim(); //市
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
                            			lo.set(19, importFail);
                                		lo.set(20, ExcelImportFailEnum.CITY_INVALID.getText());
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
//                		if(lo.get(7) == null) {
//                    		/*logger.error(MessageFormat.format("批量导入文件区不能为空, 导入失败", new Object[] {}));
//                    		throw new ExcelException("批量导入文件区不能为空, 导入失败");*/
//                    		lo.set(20, importFail);
//                    		lo.set(21, ExcelImportFailEnum.REGION_NULL.getText());
//                    		hasProblem = true;
//                    	} else {
//                    		String regionName = String.valueOf(lo.get(7)).trim(); //区
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
//                        			lo.set(20, importFail);
//                            		lo.set(21, ExcelImportFailEnum.REGION_INVALID.getText());
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
//                        			lo.set(20, importFail);
//                            		lo.set(21, ExcelImportFailEnum.REGION_INVALID.getText());
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
                		if(lo.get(7) == null) {
                    		/*logger.error(MessageFormat.format("批量导入文件主要路段不能为空, 导入失败", new Object[] {}));
                    		throw new ExcelException("批量导入文件主要路段不能为空, 导入失败");*/
                    		lo.set(19, importFail);
                    		lo.set(20, ExcelImportFailEnum.STREET_NULL.getText());
                    		hasProblem = true;
                    	} else {
                    		String road = String.valueOf(lo.get(8)).trim(); //主要路段
                    		info.setRoad(road);
                	}
                	
                	//设置广告位详细地址
                	if(hasProblem == false) {
                		if(lo.get(8) == null) {
                    		/*logger.error(MessageFormat.format("批量导入文件详细地址不能为空, 导入失败", new Object[] {}));
                    		throw new ExcelException("批量导入文件详细地址不能为空, 导入失败");*/
                    		lo.set(19, importFail);
                    		lo.set(20, ExcelImportFailEnum.LOCATION_NULL.getText());
                    		hasProblem = true;
                    	} else {
                    		info.setLocation(String.valueOf(lo.get(8)).trim()); //详细位置
                    		buffer.append(String.valueOf(lo.get(8)).trim());
                    	}
                	}
                	
                	//设置媒体方广告位编号信息
                	if(hasProblem == false) {
                		if(lo.get(9) != null) {
                			info.setMemo(String.valueOf(lo.get(9)).trim());
                		}else {
                			lo.set(19, importFail);
                    		lo.set(20, ExcelImportFailEnum.MEDIA_NUM_INVAILD.getText());
                    		hasProblem = true;
                		}
                	}
                	//设置唯一标识
//                	if(hasProblem == false) {
//                		Integer uniqueKeyNeed = adMediaTypeVo.getUniqueKeyNeed();
//                		
//                		if(lo.get(10) == null) {
//                			if(uniqueKeyNeed == 1) {
//                				//需要唯一标识
//                    			lo.set(20, importFail);
//                        		lo.set(21, ExcelImportFailEnum.UNIQUE_KEY_NULL.getText());
//                        		hasProblem = true;
//                			}
//                		} else {
//                			info.setUniqueKey(String.valueOf(lo.get(10)).trim());
//                		}
//                	}
                	
                	//设置广告位尺寸
                	if(hasProblem == false) {
                		if(lo.get(10) != null && lo.get(11) != null) {
//                    		DecimalFormat df = new DecimalFormat("0"); // 格式化number String字符
                    		String length = String.valueOf(lo.get(10)).trim();
                    		String width = String.valueOf(lo.get(11)).trim();
                    		info.setAdSize(length + "*" + width); //广告位长度*广告位宽度
                    		lo.set(10, length);
                    		lo.set(11, width);
                    	} else if (lo.get(10) != null && lo.get(11) == null) {
                    		/*logger.error(MessageFormat.format("批量导入文件尺寸有误, 导入失败", new Object[] {}));
                    		throw new ExcelException("批量导入文件尺寸有误, 导入失败");*/
                    		lo.set(19, importFail);
                    		lo.set(20, ExcelImportFailEnum.SIZE_ONLYONE.getText());
                    		hasProblem = true;
    					} else if (lo.get(10) == null && lo.get(11) != null) {
    						/*logger.error(MessageFormat.format("批量导入文件尺寸有误, 导入失败", new Object[] {}));
                    		throw new ExcelException("批量导入文件尺寸有误, 导入失败");*/
                    		lo.set(19, importFail);
                    		lo.set(20, ExcelImportFailEnum.SIZE_ONLYONE.getText());
                    		hasProblem = true;
    					} else {
    					}
                	}
                	
                	//设置面积
                	if(hasProblem == false) {
                		if(lo.get(12) != null) {
                    		info.setAdArea(String.valueOf(lo.get(12)).trim());
                    	}
                	}
                	
                	//设置面数
                	if(hasProblem == false) {
                		if(lo.get(13) != null) {
                			Double b = Double.parseDouble(String.valueOf(lo.get(13)).trim());
                    		info.setAdNum((new Double(b)).intValue());
                    	}
                	}
                	
                	//设置经纬度
                	if(hasProblem == false) {
                		if(lo.get(14) != null && lo.get(15) == null) {
                    		/*logger.error(MessageFormat.format("批量导入文件经纬度有误, 导入失败", new Object[] {}));
                    		throw new ExcelException("批量导入文件经纬度有误, 导入失败");*/
                    		lo.set(19, importFail);
                    		lo.set(20, ExcelImportFailEnum.LOC_ONLYONE.getText());
                    		hasProblem = true;
                    	} else if (lo.get(14) == null && lo.get(15) != null) {
                    		/*logger.error(MessageFormat.format("批量导入文件经纬度有误, 导入失败", new Object[] {}));
                    		throw new ExcelException("批量导入文件经纬度有误, 导入失败");*/
                    		lo.set(19, importFail);
                    		lo.set(20, ExcelImportFailEnum.LOC_ONLYONE.getText());
                    		hasProblem = true;
    					} else if (lo.get(14) == null && lo.get(15) == null) {
    					} else {
    						//判断经度在-180-180之间
    						double lon = Double.parseDouble(String.valueOf(lo.get(14)).trim());
    						if(lon < -180 || lon > 180) {
    							/*logger.error(MessageFormat.format("批量导入文件有经度不在-180到180之间, 导入失败", new Object[] {}));
    	                		throw new ExcelException("批量导入文件有经度不在-180到180之间, 导入失败");*/
    							lo.set(19, importFail);
    	                		lo.set(20, ExcelImportFailEnum.LON_OVERFLOW.getText());
    	                		hasProblem = true;
    						}
    						//判断纬度在-90-90之间
    						double lat = Double.parseDouble(String.valueOf(lo.get(15)).trim());
    						if(lat < -90 || lat > 90) {
    							/*logger.error(MessageFormat.format("批量导入文件有纬度不在-90到90之间, 导入失败", new Object[] {}));
    	                		throw new ExcelException("批量导入文件有纬度不在-90到90之间, 导入失败");*/
    	                		lo.set(19, importFail);
    	                		lo.set(20, ExcelImportFailEnum.LAT_OVERFLOW.getText());
    	                		hasProblem = true;
    						}
    						info.setLon(lon); //经度
    	                	info.setLat(lat); //纬度
    	                	
    	                	//地图标准
    	                	if(hasProblem == false) {
    	                		if(lo.get(16) != null) {
    	                			if(String.valueOf(lo.get(16)).trim().contains("百度")) {
    	                				info.setMapStandard(MapStandardEnum.getId("百度"));
    	                			} else if(String.valueOf(lo.get(16)).trim().contains("高德")) {
    	                				info.setMapStandard(MapStandardEnum.getId("高德"));
    	                			} else if(String.valueOf(lo.get(16)).trim().contains("谷歌")) {
    	                				info.setMapStandard(MapStandardEnum.getId("谷歌"));
    	                			}
        	                	} else {
        	                		/*logger.error(MessageFormat.format("批量导入文件有经纬度没有地图标准, 导入失败", new Object[] {}));
        	                		throw new ExcelException("批量导入文件有经纬度没有地图标准, 导入失败");*/
        	                		lo.set(19, importFail);
        	                		lo.set(20, ExcelImportFailEnum.NONE_MAP.getText());
        	                		hasProblem = true;
        						}
    	                	}
    					}
                	}
                	
                	
                	
                	//设置联系人信息
                	if(hasProblem == false) {
                		if(lo.get(17) != null) {
                    		info.setContactName(String.valueOf(lo.get(17)).trim());
                    	}
                	}
                	
                	//联系人电话
                	if(hasProblem == false) {
                		if(lo.get(18) != null) {
                    		info.setContactCell(String.valueOf(lo.get(18)).trim());
                    	}
                	}
                	
                	
                	//设置媒体信息
                	info.setMediaId(media.getId());
                	info.setCreateTime(now);
                	info.setUpdateTime(now);
                	info.setCodeFlag(1);
                	
                	//检查是否重复
                	if(hasProblem == false) {
                		if(keySet.contains(buffer.toString())) {
                    		lo.set(19, importFail);
                    		lo.set(20, ExcelImportFailEnum.LOC_DUP.getText());
                    		hasProblem = true;
                    	} else {
                    		keySet.add(buffer.toString());
                    	}
                	}
                	
                	if(!(StringUtils.equals(String.valueOf(lo.get(19)), importFail))) {
                		//导入成功
                		lo.set(19, importSucc);
                		//生成广告位对应的二维码
                		String adCodeInfo = mediaUser.getPrefix() + UUID.randomUUID(); //二维码存的值（媒体前缀比如media3- 加上UUID随机数）
                		String path = request.getSession().getServletContext().getRealPath("/");
                		path = path + (path.endsWith(File.separator)?"":File.separatorChar)+"static"+File.separatorChar+"qrcode"+File.separatorChar+adCodeInfo + ".jpg";
                		QRcodeUtil.encode(adCodeInfo, path);
                		info.setAdCode(adCodeInfo);
                		info.setAdCodeUrl("/static/qrcode/" + adCodeInfo + ".jpg");
                		//默认贴上二维码
                		info.setCodeFlag(1);
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
            	adSeatService.insertBatchByExcel(insertAdSeatInfos);
            }
            
            //导出到excel, 返回导入广告位信息结果
            List<List<String>> listString = objToString(listob);
            String[] titleArray = { "广告位名称", "媒体大类", "媒体小类", "是否允许多个活动", "允许活动数量", "省", "市",  "主要路段", "详细位置", 
            		"媒体方广告位编号", "广告位长度", "广告位宽度", "面积", "面数","经度", "纬度",
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
            }
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
	 * 导入广告位模板下载
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
	    BaseFont bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
	    Font fontChinese = new Font(bfChinese, 20, Font.NORMAL);// 创建字体，设置family，size，style,还可以设置color 
//	    Font titleChinese = new Font(bfChinese, 20, Font.BOLD);  
//	    Font BoldChinese = new Font(bfChinese, 20, Font.BOLD);  
//	    Font subBoldFontChinese = new Font(bfChinese, 20, Font.BOLD); 
	    
		Paragraph pt = new Paragraph(list.get(1), fontChinese);//设置字体样式pt.setAlignment(1);//设置文字居中 0靠左   1，居中     2，靠右
		pt.setAlignment(1);
		document.add(pt);
		
		String path = request.getSession().getServletContext().getRealPath("/");
		if(!StringUtils.isEmpty(feedback.getPicUrl1())) {
			Image image1 = Image.getInstance(path + feedback.getPicUrl1());
			image1.setAlignment(Image.ALIGN_CENTER);
//			image1.scalePercent(40);//依照比例缩放
			image1.scaleAbsolute(360,262);//控制图片大小
			image1.setAbsolutePosition(222,350);//控制图片位置
			document.add(image1);
		}
		
		if(!StringUtils.isEmpty(feedback.getPicUrl2())) {
			Image image2 = Image.getInstance(path + feedback.getPicUrl2());
			image2.setAlignment(Image.ALIGN_CENTER);
//			image2.scalePercent(40);//依照比例缩放
			image2.scaleAbsolute(360,262);//控制图片大小
			image2.setAbsolutePosition(642,350);//控制图片位置
			document.add(image2);
		}
		
		if(!StringUtils.isEmpty(feedback.getPicUrl3())) {
			Image image3 = Image.getInstance(path + feedback.getPicUrl3());
			image3.setAlignment(Image.ALIGN_CENTER);
//			image3.scalePercent(40);//依照比例缩放
			image3.scaleAbsolute(360,262);//控制图片大小
			image3.setAbsolutePosition(222,48);//控制图片位置
			document.add(image3);
		}
		
		if(!StringUtils.isEmpty(feedback.getPicUrl4())) {
			Image image4 = Image.getInstance(path + feedback.getPicUrl4());
			image4.setAlignment(Image.ALIGN_CENTER);
//			image4.scalePercent(40);//依照比例缩放
			image4.scaleAbsolute(360,262);//控制图片大小
			image4.setAbsolutePosition(642,48);//控制图片位置
			document.add(image4);
		}
	}
	
	/**
	 * 生成表格
	 * @return
	 * @throws DocumentException
	 * @throws IOException
	 */
	private PdfPTable createTable1(List<List<String>> listString) throws DocumentException, IOException {
		//设置字体  
	    BaseFont bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
	    Font fontChinese = new Font(bfChinese, 15, Font.NORMAL);// 创建字体，设置family，size，style,还可以设置color 
	    Font subBoldFontChinese = new Font(bfChinese, 15, Font.BOLD); 
//	    Font titleChinese = new Font(bfChinese, 20, Font.BOLD);  
//	    Font BoldChinese = new Font(bfChinese, 20, Font.BOLD);  
		
		PdfPTable table = new PdfPTable(10);
		table.setWidthPercentage(100);
		table.setWidths(new int[] { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1});

        table.addCell(new Paragraph("广告位名称", fontChinese));
//        table.addCell(new Paragraph("客户类型", fontChinese));
        table.addCell(new Paragraph("供应商", fontChinese));
        table.addCell(new Paragraph("地理位置", fontChinese));
        table.addCell(new Paragraph("详细位置", fontChinese));
        table.addCell(new Paragraph("媒体大类", fontChinese));
        table.addCell(new Paragraph("媒体小类", fontChinese));
        table.addCell(new Paragraph("开始监测时间", fontChinese));
        table.addCell(new Paragraph("结束监测时间", fontChinese));
        table.addCell(new Paragraph("当前状态", subBoldFontChinese));
        table.addCell(new Paragraph("媒体方编号", fontChinese));
        
        for (List<String> list : listString) {
        	table.addCell(new Paragraph(list.get(1), fontChinese));
//        	table.addCell(new Paragraph(list.get(22), fontChinese));
        	table.addCell(new Paragraph(list.get(21), fontChinese));
        	StringBuffer location = new StringBuffer();
        	if(StringUtil.isNotBlank(list.get(2))) {
        		location.append(list.get(2));
        	}
        	if(StringUtil.isNotBlank(list.get(3))) {
        		location.append(list.get(3));
        	}
        	if(StringUtil.isNotBlank(list.get(4))) {
        		location.append(list.get(4));
        	}
        	if(StringUtil.isNotBlank(list.get(5))) {
        		location.append(list.get(5));
        	}
            table.addCell(new Paragraph(location.toString(), fontChinese));
            table.addCell(new Paragraph(list.get(6), fontChinese));
            table.addCell(new Paragraph(list.get(19), fontChinese));
            table.addCell(new Paragraph(list.get(20), fontChinese));
            table.addCell(new Paragraph(list.get(8), fontChinese));
            table.addCell(new Paragraph(list.get(9), fontChinese));
            table.addCell(new Paragraph(list.get(10), subBoldFontChinese));
            table.addCell(new Paragraph(list.get(18), fontChinese));
		}
		return table;
	}
}
