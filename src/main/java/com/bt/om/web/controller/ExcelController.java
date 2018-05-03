package com.bt.om.web.controller;

import java.io.File;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

import com.alibaba.druid.util.StringUtils;
import com.bt.om.cache.CityCache;
import com.bt.om.common.DateUtil;
import com.bt.om.common.SysConst;
import com.bt.om.entity.AdActivity;
import com.bt.om.entity.AdMedia;
import com.bt.om.entity.AdMediaType;
import com.bt.om.entity.AdSeatInfo;
import com.bt.om.entity.City;
import com.bt.om.entity.SysUser;
import com.bt.om.entity.vo.AdActivityAdseatTaskVo;
import com.bt.om.entity.vo.AdMediaTypeVo;
import com.bt.om.enums.AdMediaInfoStatus;
import com.bt.om.enums.ExcelImportFailEnum;
import com.bt.om.enums.MapStandardEnum;
import com.bt.om.enums.ResultCode;
import com.bt.om.enums.SessionKey;
import com.bt.om.exception.web.ExcelException;
import com.bt.om.mapper.AdMediaMapper;
import com.bt.om.security.ShiroUtils;
import com.bt.om.service.IAdActivityService;
import com.bt.om.service.IAdMediaTypeService;
import com.bt.om.service.IAdSeatService;
import com.bt.om.service.ISysUserService;
import com.bt.om.util.ExcelTool;
import com.bt.om.util.ImportExcelUtil;
import com.bt.om.vo.web.ResultVo;
import com.bt.om.web.BasicController;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

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
	
	/**
	 * 具体活动的广告位excel导出报表
	 * @param model
	 * @param request
	 * @param response
	 * @param activityId
	 * @return
	 */
	@RequiresRoles(value = {"customer"})
    @RequestMapping(value = "/exportAdMediaInfo")
	@ResponseBody
	public Model exportAdMediaInfo(Model model, HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "activityId", required = false) Integer activityId) {
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
 		final String fileName = adActivity.getActivityName() + "-广告位导出结果"+ ".xls"; //导出文件名
        List<List<String>> listString = new ArrayList<>();
        
        try {
        	List<AdActivityAdseatTaskVo> vos = adActivityService.selectAdActivityAdseatTask(activityId);
        	for (AdActivityAdseatTaskVo vo : vos) {
				List<String> list = new ArrayList<>();
				list.add(adActivity.getActivityName()); //活动名称
				list.add(vo.getInfo_name()); //广告位名称
				list.add(mediaTypeMap.get(vo.getInfo_mediaTypeParentId())); //媒体大类
				list.add(mediaTypeMap.get(vo.getInfo_mediaTypeId())); //媒体小类
				list.add(cityCache.getCityName(vo.getInfo_province())); //省
				list.add(cityCache.getCityName(vo.getInfo_city())); //市
				list.add(cityCache.getCityName(vo.getInfo_region())); //区（县）
				list.add(cityCache.getCityName(vo.getInfo_street())); //街道（镇，乡）
				list.add(vo.getInfo_location()); //详细位置
				list.add(vo.getInfo_uniqueKey()); //唯一标识
				list.add(DateUtil.dateFormate(vo.getMonitorStart(), "yyyy-MM-dd")); //开始监测时间
				list.add(DateUtil.dateFormate(vo.getMonitorEnd(), "yyyy-MM-dd")); //结束监测时间
				String status = AdMediaInfoStatus.WATCHING.getText(); //当前状态
				if(vo.getMonitorStart().getTime() > now.getTime()) {
					status = AdMediaInfoStatus.NOT_BEGIN.getText();
				}
				if(vo.getMonitorEnd().getTime() < now.getTime()) {
					status = AdMediaInfoStatus.FINISHED.getText();
	        	}
				if(vo.getProblem_count() > 0) {
					status = AdMediaInfoStatus.HAS_PROBLEM.getText();
				}
				list.add(status);
				list.add(vo.getInfo_adSize());
				list.add(vo.getInfo_adArea());
				list.add(vo.getInfo_lon() + "");
				list.add(vo.getInfo_lat() + "");
				list.add(MapStandardEnum.getText(vo.getInfo_mapStandard()));
				list.add(vo.getInfo_contactName());
				list.add(vo.getInfo_contactCell());
				list.add(vo.getInfo_memo());
				
				listString.add(list);
			}
        	
            String[] titleArray = {"活动名称", "广告位名称", "媒体大类", "媒体小类", "省", "市", "区（县）", "街道（镇，乡）", "详细位置", "唯一标识", 
            		"开始监测时间", "结束监测时间", "当前状态",
            		"广告位尺寸", "面积", "经度", "纬度", "地图标准（如百度，谷歌，高德）", "联系人姓名", "联系人电话", "备注"};
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
	 * @param model
	 * @param request
	 * @param excelFile
	 */
	@RequiresRoles(value = {"admin" , "media"}, logical = Logical.OR)
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
		Integer usertype = user.getUsertype();
		if(usertype == 1) {
			//后台管理人员帮助媒体导入
			media = adMediaMapper.selectByPrimaryKey(mediaId);
		} else if (usertype == 3) {
			//媒体人员自行导入
			media = adMediaMapper.selectByUserId(user.getId());
		}
		
		//获取需要插入广告位信息的媒体已经存在的广告位信息
		List<AdSeatInfo> adSeatsByMediaId = adSeatService.getAdSeatByMediaId(media.getId());
		List<String> databaseAdSeats = new ArrayList<>();
		for (AdSeatInfo adSeatInfo : adSeatsByMediaId) {
			StringBuffer buffer = new StringBuffer();
			buffer.append(cityCache.getCityName(adSeatInfo.getProvince())); //省
			buffer.append(cityCache.getCityName(adSeatInfo.getCity())); //市
			buffer.append(cityCache.getCityName(adSeatInfo.getRegion())); //区
			buffer.append(cityCache.getCityName(adSeatInfo.getStreet())); //街道
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
                //广告位名称, 媒体大类, 媒体小类, 省, 市, 区（县）, 街道（镇）, 详细位置, 唯一标识, 广告位长度, 广告位宽度, 面积, 经度, 纬度, 地图标准（如百度，谷歌，高德）, 联系人姓名, 联系人电话, 备注, 导入结果, 导入错误信息
                if(lo.size() <= 20){
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
                		lo.set(18, importFail);
                		lo.set(19, ExcelImportFailEnum.ADNAME_NULL.getText());
                		hasProblem = true;
                	} else {
                		info.setName(String.valueOf(lo.get(0)).trim()); //广告位名称
                	}
                	
                	//设置媒体大类
                	if(hasProblem == false) {
                		if(lo.get(1) == null) {
                    		lo.set(18, importFail);
                    		lo.set(19, ExcelImportFailEnum.PARENT_NULL.getText());
                    		hasProblem = true;
                    	}
                	}
                	
                	//设置媒体小类
                	if(hasProblem == false) {
                		if(lo.get(2) == null) {
                    		lo.set(18, importFail);
                    		lo.set(19, ExcelImportFailEnum.SECOND_NULL.getText());
                    		hasProblem = true;
                    	} else {
                    		String parentName = String.valueOf(lo.get(1)).trim();
                    		String secondName = String.valueOf(lo.get(2)).trim();
                    		adMediaTypeVo = table.get(parentName, secondName);
                    		if(adMediaTypeVo != null) {
                    			info.setMediaTypeParentId(adMediaTypeVo.getParentId());
                    			info.setMediaTypeId(adMediaTypeVo.getId());
                    		} else {
                    			lo.set(18, importFail);
                        		lo.set(19, ExcelImportFailEnum.MEDIA_TYPE_INVALID.getText());
                        		hasProblem = true;
                    		}
    					}
                	}
                	
                	//设置广告位所在省份
                	if(hasProblem == false) {
                		if(lo.get(3) == null) {
                    		/*logger.error(MessageFormat.format("批量导入文件省份不能为空, 导入失败", new Object[] {}));
                    		throw new ExcelException("批量导入文件省份不能为空, 导入失败");*/
                    		lo.set(18, importFail);
                    		lo.set(19, ExcelImportFailEnum.PROVINCE_NULL.getText());
                    		hasProblem = true;
                    	} else {
                    		String provinceName = String.valueOf(lo.get(3)).trim(); //省
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
                    		info.setProvince(provinceId);
                    		buffer.append(provinceName);
    					}
                	}
                	
                	//设置广告位所在市
                	if(hasProblem == false) {
                		if(lo.get(4) == null) {
                    		if(zhiXiaShiFlag == false) {
                    			/*logger.error(MessageFormat.format("批量导入文件市不能为空, 导入失败", new Object[] {}));
                        		throw new ExcelException("批量导入文件市不能为空, 导入失败");*/
                    			lo.set(18, importFail);
                        		lo.set(19, ExcelImportFailEnum.CITY_NULL.getText());
                        		hasProblem = true;
                    		}
                    	} else {
                    		String cityName = String.valueOf(lo.get(4)).trim(); //市
                    		/*if(!cityName.endsWith("市")) {
                    			cityName = cityName + "市";
                    		}*/
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
                    		info.setCity(cityId);
                    		buffer.append(cityName);
    					}
                	}
                	
                	//设置广告位所在区(县)
                	if(hasProblem == false) {
                		if(lo.get(5) == null) {
                    		/*logger.error(MessageFormat.format("批量导入文件区不能为空, 导入失败", new Object[] {}));
                    		throw new ExcelException("批量导入文件区不能为空, 导入失败");*/
                    		lo.set(18, importFail);
                    		lo.set(19, ExcelImportFailEnum.REGION_NULL.getText());
                    		hasProblem = true;
                    	} else {
                    		String regionName = String.valueOf(lo.get(5)).trim(); //区
                    		/*if(!regionName.endsWith("区")) {
                    			regionName = regionName + "区";
                    		}*/
                    		List<City> cities = cityCache.getRegion(cityId);
                    		Map<String, Long> regionMap = citiesToMap(cities);
                    		Set<String> regionNames = regionMap.keySet();
                    		for (String name : regionNames) {
								if(name.contains(regionName)) {
									regionName = name;
									break;
								}
							}
                    		regionId = regionMap.get(regionName);
                    		info.setRegion(regionId);
                    		buffer.append(regionName);
    					}
                	}
                	
                	//设置广告位所在街道(镇，乡)
                	if(hasProblem == false) {
                		if(lo.get(6) == null) {
                    		/*logger.error(MessageFormat.format("批量导入文件街道不能为空, 导入失败", new Object[] {}));
                    		throw new ExcelException("批量导入文件街道不能为空, 导入失败");*/
                    		lo.set(18, importFail);
                    		lo.set(19, ExcelImportFailEnum.STREET_NULL.getText());
                    		hasProblem = true;
                    	} else {
                    		String streetName = String.valueOf(lo.get(6)).trim(); //街道
                    		/*if(!streetName.endsWith("街道")) {
                    			streetName = streetName + "街道";
                    		}*/
                    		List<City> cities = cityCache.getStreet(regionId);
                    		Map<String, Long> streetMap = citiesToMap(cities);
                    		Set<String> streetNames = streetMap.keySet();
                    		for (String name : streetNames) {
								if(name.contains(streetName)) {
									streetName = name;
									break;
								}
							}
                    		Long streetId = streetMap.get(streetName);
                    		info.setStreet(streetId);
                    		buffer.append(streetName);
    					}
                	}
                	
                	//设置广告位详细地址
                	if(hasProblem == false) {
                		if(lo.get(7) == null) {
                    		/*logger.error(MessageFormat.format("批量导入文件详细地址不能为空, 导入失败", new Object[] {}));
                    		throw new ExcelException("批量导入文件详细地址不能为空, 导入失败");*/
                    		lo.set(18, importFail);
                    		lo.set(19, ExcelImportFailEnum.LOCATION_NULL.getText());
                    		hasProblem = true;
                    	} else {
                    		info.setLocation(String.valueOf(lo.get(7)).trim()); //详细位置
                    		buffer.append(String.valueOf(lo.get(7)).trim());
                    	}
                	}
                	
                	//设置唯一标识
                	if(hasProblem == false) {
                		Integer uniqueKeyNeed = adMediaTypeVo.getUniqueKeyNeed();
                		if(lo.get(8) == null) {
                			if(uniqueKeyNeed == 1) {
                				//需要唯一标识
                    			lo.set(18, importFail);
                        		lo.set(19, ExcelImportFailEnum.UNIQUE_KEY_NULL.getText());
                        		hasProblem = true;
                			}
                		} else {
                			info.setUniqueKey(String.valueOf(lo.get(8)).trim());
                		}
                	}
                	
                	//设置广告位尺寸
                	if(hasProblem == false) {
                		if(lo.get(9) != null && lo.get(10) != null) {
                    		DecimalFormat df = new DecimalFormat("0"); // 格式化number String字符
                    		String length = df.format(lo.get(9)).trim();
                    		String width = df.format(lo.get(10)).trim();
                    		info.setAdSize(length + "*" + width); //广告位长度*广告位宽度
                    		lo.set(9, length);
                    		lo.set(10, width);
                    	} else if (lo.get(9) != null && lo.get(10) == null) {
                    		/*logger.error(MessageFormat.format("批量导入文件尺寸有误, 导入失败", new Object[] {}));
                    		throw new ExcelException("批量导入文件尺寸有误, 导入失败");*/
                    		lo.set(18, importFail);
                    		lo.set(19, ExcelImportFailEnum.SIZE_ONLYONE.getText());
                    		hasProblem = true;
    					} else if (lo.get(9) == null && lo.get(10) != null) {
    						/*logger.error(MessageFormat.format("批量导入文件尺寸有误, 导入失败", new Object[] {}));
                    		throw new ExcelException("批量导入文件尺寸有误, 导入失败");*/
                    		lo.set(18, importFail);
                    		lo.set(19, ExcelImportFailEnum.SIZE_ONLYONE.getText());
                    		hasProblem = true;
    					} else {
    					}
                	}
                	
                	//设置面积
                	if(hasProblem == false) {
                		if(lo.get(11) != null) {
                    		info.setAdArea(String.valueOf(lo.get(11)).trim());
                    	}
                	}
                	
                	//设置经纬度
                	if(hasProblem == false) {
                		if(lo.get(12) != null && lo.get(13) == null) {
                    		/*logger.error(MessageFormat.format("批量导入文件经纬度有误, 导入失败", new Object[] {}));
                    		throw new ExcelException("批量导入文件经纬度有误, 导入失败");*/
                    		lo.set(18, importFail);
                    		lo.set(19, ExcelImportFailEnum.LOC_ONLYONE.getText());
                    		hasProblem = true;
                    	} else if (lo.get(12) == null && lo.get(13) != null) {
                    		/*logger.error(MessageFormat.format("批量导入文件经纬度有误, 导入失败", new Object[] {}));
                    		throw new ExcelException("批量导入文件经纬度有误, 导入失败");*/
                    		lo.set(18, importFail);
                    		lo.set(19, ExcelImportFailEnum.LOC_ONLYONE.getText());
                    		hasProblem = true;
    					} else if (lo.get(12) == null && lo.get(13) == null) {
    					} else {
    						//判断经度在-180-180之间
    						double lon = Double.parseDouble(String.valueOf(lo.get(12)).trim());
    						if(lon < -180 || lon > 180) {
    							/*logger.error(MessageFormat.format("批量导入文件有经度不在-180到180之间, 导入失败", new Object[] {}));
    	                		throw new ExcelException("批量导入文件有经度不在-180到180之间, 导入失败");*/
    							lo.set(18, importFail);
    	                		lo.set(19, ExcelImportFailEnum.LON_OVERFLOW.getText());
    	                		hasProblem = true;
    						}
    						//判断纬度在-90-90之间
    						double lat = Double.parseDouble(String.valueOf(lo.get(13)).trim());
    						if(lat < -90 || lat > 90) {
    							/*logger.error(MessageFormat.format("批量导入文件有纬度不在-90到90之间, 导入失败", new Object[] {}));
    	                		throw new ExcelException("批量导入文件有纬度不在-90到90之间, 导入失败");*/
    	                		lo.set(18, importFail);
    	                		lo.set(19, ExcelImportFailEnum.LAT_OVERFLOW.getText());
    	                		hasProblem = true;
    						}
    						info.setLon(lon); //经度
    	                	info.setLat(lat); //纬度
    	                	
    	                	if(hasProblem == false) {
    	                		if(lo.get(14) != null) {
        	                		info.setMapStandard(MapStandardEnum.getId(String.valueOf(lo.get(14)).trim())); //地图标准
        	                	} else {
        	                		/*logger.error(MessageFormat.format("批量导入文件有经纬度没有地图标准, 导入失败", new Object[] {}));
        	                		throw new ExcelException("批量导入文件有经纬度没有地图标准, 导入失败");*/
        	                		lo.set(18, importFail);
        	                		lo.set(19, ExcelImportFailEnum.NONE_MAP.getText());
        	                		hasProblem = true;
        						}
    	                	}
    					}
                	}
                	
                	//设置联系人信息
                	if(hasProblem == false) {
                		if(lo.get(15) != null) {
                    		info.setContactName(String.valueOf(lo.get(15)).trim());
                    	}
                	}
                	if(hasProblem == false) {
                		if(lo.get(16) != null) {
                    		info.setContactCell(String.valueOf(lo.get(16)).trim());
                    	}
                	}
                	
                	//设置备注信息
                	if(hasProblem == false) {
                		if(lo.get(17) != null) {
                			info.setMemo(String.valueOf(lo.get(17)).trim());
                		}
                	}
                	
                	//设置媒体信息
                	info.setMediaId(media.getId());
                	info.setCreateTime(now);
                	info.setUpdateTime(now);
                	
                	//检查是否重复
                	if(hasProblem == false) {
                		if(keySet.contains(buffer.toString())) {
                    		lo.set(18, importFail);
                    		lo.set(19, ExcelImportFailEnum.LOC_DUP.getText());
                    		hasProblem = true;
                    	} else {
                    		keySet.add(buffer.toString());
                    	}
                	}
                	
                	if(!StringUtils.equals(String.valueOf(lo.get(18)), importFail)) {
                		//导入成功
                		lo.set(18, importSucc);
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
            
            //导出到excel
            List<List<String>> listString = objToString(listob);
            String[] titleArray = { "广告位名称", "媒体大类", "媒体小类", "省", "市", "区（县）", "街道（镇，乡）", "详细位置", "唯一标识", "广告位长度", "广告位宽度", "面积", "经度", "纬度",
            		"地图标准（如百度，谷歌，高德）", "联系人姓名", "联系人电话", "备注", "导入结果", "导入错误信息"};
            ExcelTool<List<String>> excelTool = new ExcelTool<List<String>>("importResult");
//            excelTool.exportExcel(listString, titleArray, response);
            String path = request.getSession().getServletContext().getRealPath("/");
    		path = path + (path.endsWith(File.separator)?"":File.separatorChar)+"static"+File.separatorChar+"excel"+File.separatorChar+fileName;
    		excelTool.generateExcel(listString, titleArray, path);
//    		InputStream is = new FileInputStream(excelFile);
//            String filepath = saveFile(path, fileName, is);
            
            result.setCode(ResultCode.RESULT_SUCCESS.getCode());
            result.setResult("/static/excel/" + fileName);
        } catch (Exception e) {
        	logger.error(MessageFormat.format("批量导入文件有误, 导入失败", new Object[] {}));
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
}
