package com.bt.om.web.controller;

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
import com.bt.om.common.SysConst;
import com.bt.om.entity.AdMedia;
import com.bt.om.entity.AdSeatInfo;
import com.bt.om.entity.City;
import com.bt.om.entity.SysUser;
import com.bt.om.enums.ExcelImportFailEnum;
import com.bt.om.enums.MapStandardEnum;
import com.bt.om.enums.ResultCode;
import com.bt.om.enums.SessionKey;
import com.bt.om.exception.web.ExcelException;
import com.bt.om.mapper.AdMediaMapper;
import com.bt.om.security.ShiroUtils;
import com.bt.om.service.IAdSeatService;
import com.bt.om.util.ImportExcelUtil;
import com.bt.om.vo.web.ResultVo;
import com.bt.om.web.BasicController;

/**
 * Created by jiayong.mao on 2018/4/2.
 */
@Controller
@RequestMapping(value = "/excel")
public class ExcelController extends BasicController {
	private static final String importSucc = "导入成功";
	private static final String importFail = "导入失败";
	
	@Autowired
    private IAdSeatService adSeatService;
	
	@Autowired
	private CityCache cityCache;
	
	@Autowired
	private AdMediaMapper adMediaMapper;
	
	/**
	 * 批量插入广告位
	 * @param model
	 * @param request
	 * @param excelFile
	 */
	@RequiresRoles(value = {"admin" , "media"}, logical = Logical.OR)
    @RequestMapping(value = "/insertBatch")
	@ResponseBody
	public Model insertBatchByExcel(Model model, HttpServletRequest request,
                                     @RequestParam(value = "excelFile", required = false) MultipartFile file,
                                     @RequestParam(value = "mediaId", required = false) Integer mediaId,
                                     @RequestParam(value = "city", required = false) String city) {
		ResultVo result = new ResultVo();
        result.setCode(ResultCode.RESULT_SUCCESS.getCode());
        result.setResultDes("查询成功");
        model = new ExtendedModelMap();
		
		List<City> provinces = cityCache.getAllProvince(); //获取全部省份
		Map<String, Long> provinceMap = citiesToMap(provinces);
		
		//获取登录用户信息
		Date now = new Date();
    	SysUser user = (SysUser) ShiroUtils.getSessionAttribute(SessionKey.SESSION_LOGIN_USER.toString());
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
                //广告位名称, 广告位类型, 省, 市, 区, 街道, 详细位置, 广告位长度, 广告位宽度, 面积, 经度, 纬度, 地图标准（如百度，谷歌，高德）, 联系人姓名, 联系人电话, 导入结果, 导入错误信息
                if(lo.size() <= 17){
                	AdSeatInfo info = new AdSeatInfo();
                	Long provinceId = 0L;
                	Long cityId = 0L;
                	Long regionId = 0L;
                	Boolean zhiXiaShiFlag = false;
                	StringBuffer buffer = new StringBuffer();
                	
                	//设置广告位名称
                	if(lo.get(0) == null) {
                		/*logger.error(MessageFormat.format("批量导入文件广告位名称不能为空, 导入失败", new Object[] {}));
                		throw new ExcelException("批量导入文件广告位名称不能为空, 导入失败");*/
                		lo.set(15, importFail);
                		lo.set(16, ExcelImportFailEnum.ADNAME_NULL.getText());
                	} else {
                		info.setName(String.valueOf(lo.get(0)).trim()); //广告位名称
                	}
                	
                	//设置广告位类型
                	if(lo.get(1) != null) {
                		info.setAdSeatTypeText(String.valueOf(lo.get(1)).trim());
                	}
                	
                	//设置广告位所在省份
                	if(lo.get(2) == null) {
                		/*logger.error(MessageFormat.format("批量导入文件省份不能为空, 导入失败", new Object[] {}));
                		throw new ExcelException("批量导入文件省份不能为空, 导入失败");*/
                		lo.set(15, importFail);
                		lo.set(16, ExcelImportFailEnum.PROVINCE_NULL.getText());
                	} else {
                		String provinceName = String.valueOf(lo.get(2)).trim(); //省
                		//判断是否是直辖市
                		if(provinceName.contains("北京") || provinceName.contains("上海") || provinceName.contains("天津")
                				|| provinceName.contains("重庆")) {
                			zhiXiaShiFlag = true;
                			if(!provinceName.endsWith("市")) {
                    			provinceName = provinceName + "市";
                    		}
                		} else {
                			if(!provinceName.endsWith("省")) {
                    			provinceName = provinceName + "省";
                    		}
                		}
                		provinceId = provinceMap.get(provinceName);
                		info.setProvince(provinceId);
                		buffer.append(provinceName);
					}
                	
                	//设置广告位所在市
                	if(lo.get(3) == null) {
                		if(zhiXiaShiFlag == false) {
                			/*logger.error(MessageFormat.format("批量导入文件市不能为空, 导入失败", new Object[] {}));
                    		throw new ExcelException("批量导入文件市不能为空, 导入失败");*/
                			lo.set(15, importFail);
                    		lo.set(16, ExcelImportFailEnum.CITY_NULL.getText());
                		}
                	} else {
                		String cityName = String.valueOf(lo.get(3)).trim(); //市
                		if(!cityName.endsWith("市")) {
                			cityName = cityName + "市";
                		}
                		List<City> cities = cityCache.getCity(provinceId);
                		Map<String, Long> cityMap = citiesToMap(cities);
                		cityId = cityMap.get(cityName);
                		info.setCity(cityId);
                		buffer.append(cityName);
					}
                	
                	//设置广告位所在区(县)
                	if(lo.get(4) == null) {
                		/*logger.error(MessageFormat.format("批量导入文件区不能为空, 导入失败", new Object[] {}));
                		throw new ExcelException("批量导入文件区不能为空, 导入失败");*/
                		lo.set(15, importFail);
                		lo.set(16, ExcelImportFailEnum.REGION_NULL.getText());
                	} else {
                		String regionName = String.valueOf(lo.get(4)).trim(); //区
                		/*if(!regionName.endsWith("区")) {
                			regionName = regionName + "区";
                		}*/
                		List<City> cities = cityCache.getRegion(cityId);
                		Map<String, Long> regionMap = citiesToMap(cities);
                		regionId = regionMap.get(regionName);
                		info.setRegion(regionId);
                		buffer.append(regionName);
					}
                	
                	//设置广告位所在街道(镇)
                	if(lo.get(5) == null) {
                		/*logger.error(MessageFormat.format("批量导入文件街道不能为空, 导入失败", new Object[] {}));
                		throw new ExcelException("批量导入文件街道不能为空, 导入失败");*/
                		lo.set(15, importFail);
                		lo.set(16, ExcelImportFailEnum.STREET_NULL.getText());
                	} else {
                		String streetName = String.valueOf(lo.get(5)).trim(); //街道
                		/*if(!streetName.endsWith("街道")) {
                			streetName = streetName + "街道";
                		}*/
                		List<City> cities = cityCache.getStreet(regionId);
                		Map<String, Long> streetMap = citiesToMap(cities);
                		Long streetId = streetMap.get(streetName);
                		info.setStreet(streetId);
                		buffer.append(streetName);
					}
                	
                	//设置广告位详细地址
                	if(lo.get(6) == null) {
                		/*logger.error(MessageFormat.format("批量导入文件详细地址不能为空, 导入失败", new Object[] {}));
                		throw new ExcelException("批量导入文件详细地址不能为空, 导入失败");*/
                		lo.set(15, importFail);
                		lo.set(16, ExcelImportFailEnum.LOCATION_NULL.getText());
                	} else {
                		info.setLocation(String.valueOf(lo.get(6)).trim()); //详细位置
                		buffer.append(String.valueOf(lo.get(6)).trim());
                	}
                	
                	//设置广告位尺寸
                	if(lo.get(7) != null && lo.get(8) != null) {
                		DecimalFormat df = new DecimalFormat("0"); // 格式化number String字符
                		String length = df.format(lo.get(7)).trim();
                		String width = df.format(lo.get(8)).trim();
                		info.setAdSize(length + "*" + width); //广告位长度*广告位宽度
                	} else if (lo.get(7) != null && lo.get(8) == null) {
                		/*logger.error(MessageFormat.format("批量导入文件尺寸有误, 导入失败", new Object[] {}));
                		throw new ExcelException("批量导入文件尺寸有误, 导入失败");*/
                		lo.set(15, importFail);
                		lo.set(16, ExcelImportFailEnum.SIZE_ONLYONE.getText());
					} else if (lo.get(7) == null && lo.get(8) != null) {
						/*logger.error(MessageFormat.format("批量导入文件尺寸有误, 导入失败", new Object[] {}));
                		throw new ExcelException("批量导入文件尺寸有误, 导入失败");*/
                		lo.set(15, importFail);
                		lo.set(16, ExcelImportFailEnum.SIZE_ONLYONE.getText());
					} else {
					}
                	
                	//设置面积
                	if(lo.get(9) != null) {
                		info.setAdArea(String.valueOf(lo.get(9)).trim());
                	}
                	
                	//设置经纬度
                	if(lo.get(10) != null && lo.get(11) == null) {
                		/*logger.error(MessageFormat.format("批量导入文件经纬度有误, 导入失败", new Object[] {}));
                		throw new ExcelException("批量导入文件经纬度有误, 导入失败");*/
                		lo.set(15, importFail);
                		lo.set(16, ExcelImportFailEnum.LOC_ONLYONE.getText());
                	} else if (lo.get(10) == null && lo.get(11) != null) {
                		/*logger.error(MessageFormat.format("批量导入文件经纬度有误, 导入失败", new Object[] {}));
                		throw new ExcelException("批量导入文件经纬度有误, 导入失败");*/
                		lo.set(15, importFail);
                		lo.set(16, ExcelImportFailEnum.LOC_ONLYONE.getText());
					} else if (lo.get(10) == null && lo.get(11) == null) {
					} else {
						//判断经度在-180-180之间
						double lon = Double.parseDouble(String.valueOf(lo.get(10)).trim());
						if(lon < -180 || lon > 180) {
							/*logger.error(MessageFormat.format("批量导入文件有经度不在-180到180之间, 导入失败", new Object[] {}));
	                		throw new ExcelException("批量导入文件有经度不在-180到180之间, 导入失败");*/
							lo.set(15, importFail);
	                		lo.set(16, ExcelImportFailEnum.LON_OVERFLOW.getText());
						}
						//判断纬度在-90-90之间
						double lat = Double.parseDouble(String.valueOf(lo.get(11)).trim());
						if(lat < -90 || lat > 90) {
							/*logger.error(MessageFormat.format("批量导入文件有纬度不在-90到90之间, 导入失败", new Object[] {}));
	                		throw new ExcelException("批量导入文件有纬度不在-90到90之间, 导入失败");*/
	                		lo.set(15, importFail);
	                		lo.set(16, ExcelImportFailEnum.LAT_OVERFLOW.getText());
						}
						info.setLon(lon); //经度
	                	info.setLat(lat); //纬度
	                	if(lo.get(12) != null) {
	                		info.setMapStandard(MapStandardEnum.getId(String.valueOf(lo.get(12)).trim())); //地图标准
	                	} else {
	                		/*logger.error(MessageFormat.format("批量导入文件有经纬度没有地图标准, 导入失败", new Object[] {}));
	                		throw new ExcelException("批量导入文件有经纬度没有地图标准, 导入失败");*/
	                		lo.set(15, importFail);
	                		lo.set(16, ExcelImportFailEnum.NONE_MAP.getText());
						}
					}
                	
                	//设置媒体信息
                	info.setMediaId(media.getId());
                	info.setCreateTime(now);
                	info.setUpdateTime(now);
                	
                	//检查是否重复
                	if(keySet.contains(buffer.toString())) {
                		lo.set(15, importFail);
                		lo.set(16, ExcelImportFailEnum.LOC_DUP.getText());
                	} else {
                		keySet.add(buffer.toString());
                	}
                	
                	if(!StringUtils.equals(String.valueOf(lo.get(15)), importFail)) {
                		//导入成功
                		lo.set(15, importSucc);
                		insertAdSeatInfos.add(info);
                	}
                } else {
                	logger.error(MessageFormat.format("批量导入文件有误, 导入失败", new Object[] {}));
                	result.setCode(ResultCode.RESULT_FAILURE.getCode());
                	result.setResultDes("批量导入文件有误, 导入失败");
                    throw new ExcelException("批量导入文件有误, 导入失败");
                }
            }
            if(insertAdSeatInfos != null && insertAdSeatInfos.size() > 0){
            	adSeatService.insertBatchByExcel(insertAdSeatInfos);
            }
			
            result.setCode(ResultCode.RESULT_SUCCESS.getCode());
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
	
}
