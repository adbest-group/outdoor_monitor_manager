package com.bt.om.web.controller.api;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.adtime.common.lang.StringUtil;
import com.bt.om.cache.AdVersionCache;
import com.bt.om.cache.CityCache;
import com.bt.om.common.DateUtil;
import com.bt.om.common.SysConst;
import com.bt.om.entity.AdActivity;
import com.bt.om.entity.AdActivityAdseat;
import com.bt.om.entity.AdApp;
import com.bt.om.entity.AdJiucuoTask;
import com.bt.om.entity.AdJiucuoTaskFeedback;
import com.bt.om.entity.AdMedia;
import com.bt.om.entity.AdMediaType;
import com.bt.om.entity.AdMonitorReward;
import com.bt.om.entity.AdMonitorTask;
import com.bt.om.entity.AdMonitorTaskFeedback;
import com.bt.om.entity.AdPoint;
import com.bt.om.entity.AdSeatInfo;
import com.bt.om.entity.AdSystemPush;
import com.bt.om.entity.AdUserMoney;
import com.bt.om.entity.AdUserPoint;
import com.bt.om.entity.AdVersion;
import com.bt.om.entity.City;
import com.bt.om.entity.LoginLog;
import com.bt.om.entity.SysUser;
import com.bt.om.entity.SysUserExecute;
import com.bt.om.entity.vo.AbandonTaskVo;
import com.bt.om.entity.vo.ActivityMobileReportVo;
import com.bt.om.entity.vo.AdActivityAdseatTaskVo;
import com.bt.om.entity.vo.AdActivityAdseatVo;
import com.bt.om.entity.vo.AdJiucuoTaskMobileVo;
import com.bt.om.entity.vo.AdMonitorTaskMobileVo;
import com.bt.om.entity.vo.AppDetailReport;
import com.bt.om.entity.vo.AppDetailReports;
import com.bt.om.entity.vo.ParentMediaType;
import com.bt.om.entity.vo.PictureVo;
import com.bt.om.entity.vo.ProvinceAndCity;
import com.bt.om.entity.vo.SysUserVo;
import com.bt.om.enums.AdCodeFlagEnum;
import com.bt.om.enums.AdMediaInfoStatus;
import com.bt.om.enums.AdPointEnum;
import com.bt.om.enums.AppTaskEnum;
import com.bt.om.enums.AppUpdateTypeEnum;
import com.bt.om.enums.AppUserTaskEnum;
import com.bt.om.enums.AppUserTypeEnum;
import com.bt.om.enums.JiucuoTaskStatus;
import com.bt.om.enums.LoginLogTypeEnum;
import com.bt.om.enums.MonitorTaskStatus;
import com.bt.om.enums.MonitorTaskType;
import com.bt.om.enums.ResultCode;
import com.bt.om.enums.SessionKey;
import com.bt.om.enums.TaskProblemStatus;
import com.bt.om.enums.VerifyType;
import com.bt.om.filter.LogFilter;
import com.bt.om.mapper.SysUserResMapper;
import com.bt.om.service.IAdActivityService;
import com.bt.om.service.IAdJiucuoTaskService;
import com.bt.om.service.IAdMediaTypeService;
import com.bt.om.service.IAdMonitorRewardService;
import com.bt.om.service.IAdMonitorTaskService;
import com.bt.om.service.IAdSeatService;
import com.bt.om.service.IAdSystemPushService;
import com.bt.om.service.IAdUserMessageService;
import com.bt.om.service.IAppService;
import com.bt.om.service.ILoginLogService;
import com.bt.om.service.IMediaService;
import com.bt.om.service.IPointService;
import com.bt.om.service.ISendSmsService;
import com.bt.om.service.ISysResourcesService;
import com.bt.om.service.ISysUserExecuteService;
import com.bt.om.service.ISysUserService;
import com.bt.om.service.IUserMoneyService;
import com.bt.om.service.IUserPointService;
import com.bt.om.util.AddressUtils;
import com.bt.om.util.CityUtil;
import com.bt.om.util.ConfigUtil;
import com.bt.om.util.GeoUtil;
import com.bt.om.util.MarkLogoUtil;
import com.bt.om.util.QRcodeUtil;
import com.bt.om.vo.api.ActivityReportVo;
import com.bt.om.vo.api.AdActivitySeatInfoInQRVO;
import com.bt.om.vo.api.AdSeatCodeCheckInfo;
import com.bt.om.vo.api.CustomerActivityReport;
import com.bt.om.vo.api.ImageCodeResultVo;
import com.bt.om.vo.api.JiucuoTaskListResultVo;
import com.bt.om.vo.api.JiucuoTaskVo;
import com.bt.om.vo.api.MonitorTaskArroundVo;
import com.bt.om.vo.api.MonitorTaskCheckedVo;
import com.bt.om.vo.api.MonitorTaskExecutingVo;
import com.bt.om.vo.api.MonitorTaskListResultVo;
import com.bt.om.vo.api.MonitorTaskUnFinishedVo;
import com.bt.om.vo.api.MonitorTaskWaitToExecutedVo;
import com.bt.om.vo.api.QRCodeInfoVo;
import com.bt.om.vo.api.RewardResultVo;
import com.bt.om.vo.api.RewardVo;
import com.bt.om.vo.api.SMSCheckCodeResultVo;
import com.bt.om.vo.api.SysUserExecuteVo;
import com.bt.om.vo.web.ResultVo;
import com.bt.om.vo.web.SearchDataVo;
import com.bt.om.web.BasicController;
import com.bt.om.web.session.SessionByRedis;
import com.bt.om.web.util.UploadFileUtil;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * Created by caiting on 2018/1/22.
 */
@RestController
@RequestMapping(value = "/api")
public class ApiController extends BasicController {

    @Autowired
    private IAdActivityService adActivityService;
    @Autowired
    private ISysUserExecuteService sysUserExecuteService;
    @Autowired
    private IAdMonitorTaskService adMonitorTaskService;
    @Autowired
    private IAdJiucuoTaskService adJiucuoTaskService;
    @Autowired
    private IAdMonitorRewardService adMonitorRewardService;
    @Autowired
    private ISysUserService sysUserService;
    @Autowired
    private IAdSeatService adSeatService;
    @Autowired
    private SessionByRedis sessionByRedis;
    @Autowired
    private CityCache cityCache;
    @Autowired
    private AdVersionCache adVersionCache;
    @Autowired
    private ISendSmsService sendSmsService;
    @Autowired
	private IAdMediaTypeService adMediaTypeService;
    @Autowired
	private IPointService pointService;
    @Autowired
	private IUserPointService userPointService;
    @Autowired
	private IAppService appService;
	@Autowired
	private ISysResourcesService sysResourcesService;
	@Autowired
	private SysUserResMapper sysUserResMapper;
	@Autowired
	private IAdUserMessageService adUserMessageService;
	@Autowired
	private IMediaService mediaService;
	@Autowired
	private ILoginLogService loginLogService;
    @Autowired
    private IUserMoneyService userMoneyService;
    @Autowired
	private IAdSystemPushService adSystemPushService;
    
    @Value("${sms.checkcode.content.template}")
    private String SMS_CHECKCODE_CONTENT_TEMPLATE;
    @Value("${mobile.number.regex}")
    private String MOBILE_NUMBER_REGEX;
    
    private String file_upload_path = ConfigUtil.getString("file.upload.path");
	
	private String file_upload_ip = ConfigUtil.getString("file.upload.ip");

    private static ThreadLocal<Boolean> useSession = new ThreadLocal<>();
    
    private static final Logger logger = Logger.getLogger(ApiController.class);

    static {
        useSession.set(true);
    }

    //测试用
    @RequestMapping(value = "/aaa/bbb/aaa")
    @ResponseBody
    public Model confirm(Model model, HttpServletRequest request,
                         @RequestParam(value = "id", required = false) Integer id) {
        ResultVo<AdActivity> result = new ResultVo<AdActivity>();
        result.setCode(ResultCode.RESULT_SUCCESS.getCode());
        result.setResultDes("确认成功");
        result.setResult(adActivityService.getVoById(2));
        model = new ExtendedModelMap();

        model.addAttribute(SysConst.RESULT_KEY, result);
        return model;
    }


//    //解析上传的二维码照片
//    @RequestMapping(value="/qrcodeanalysis")
//    @ResponseBody
//    public Model decodeQR(Model model, HttpServletRequest request,
//                          @RequestParam(value = "pic", required = false) MultipartFile file) {
//        ResultVo result = new ResultVo();
//        result.setCode(ResultCode.RESULT_SUCCESS.getCode());
//        result.setResultDes("解析成功");
//        result.setResult("");
//        model = new ExtendedModelMap();
//
//        InputStream is = null;
//
//        try {
//            is = file.getInputStream();
//
//
////            String path = request.getRealPath("/");
////            path = path + (path.endsWith(File.separator)?"":File.separatorChar)+"static"+File.separatorChar+"upload"+File.separatorChar;
////            String imageName = file.getOriginalFilename();
////            saveFile(path,imageName,is);
//
////            Result res = QRcodeUtil.readQRCodeResult(is);
////            result.setResult(res.getText());
//            result.setResult(new QRCodeInfoVo((AdActivityAdseatVo) adActivityService.getActivitySeatById(6)));
//
//        } catch (IOException e) {
//            e.printStackTrace();
//            result.setCode(ResultCode.RESULT_FAILURE.getCode());
//            result.setResultDes("二维码解析失败失败！");
//            model.addAttribute(SysConst.RESULT_KEY, result);
//            return model;
////        } catch (ReaderException e) {
////            e.printStackTrace();
////            result.setCode(ResultCode.RESULT_FAILURE.getCode());
////            result.setResultDes("二维码解析失败失败！");
////            model.addAttribute(SysConst.RESULT_KEY, result);
////            return model;
//        }finally {
//            if (is!=null){
//                try {
//                    is.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//
//        model.addAttribute(SysConst.RESULT_KEY, result);
//        return model;
//    }

    //解析上传的二维码照片
    @RequestMapping(value = "/qrcodeanalysis")
    @ResponseBody
    public Model decodeQR(Model model, HttpServletRequest request,
                          @RequestParam(value = "pic", required = false) String file) {
        ResultVo result = new ResultVo();
        result.setCode(ResultCode.RESULT_SUCCESS.getCode());
        result.setResultDes("解析成功");
        result.setResult("");
        model = new ExtendedModelMap();

        InputStream is = null;
        saveLog(request, "");
        try {
            file = file.replaceAll("data:image/jpeg;base64,", "");
            is = new ByteArrayInputStream(Base64.getDecoder().decode(file));

//            String path = request.getRealPath("/");
//            path = path + (path.endsWith(File.separator) ? "" : File.separatorChar) + "static" + File.separatorChar + "upload" + File.separatorChar;
//            String imageName = "qrcode.jpg";
//            imageName = UploadFileUtil.saveFile(path, imageName, is);
//            System.out.println(path+imageName);


            String code = QRcodeUtil.decode(is);
            List<AdActivityAdseatVo> list = adActivityService.getActivitySeatBySeatId(Integer.valueOf(code));
            QRCodeInfoVo qr = new QRCodeInfoVo();
            qr.setAd_seat_id(Integer.valueOf(code));
            for (AdActivityAdseatVo vo : list) {
                qr.getAd_activity_seats().add(new AdActivitySeatInfoInQRVO(vo));
            }
            result.setResult(qr);

        } catch (IOException e) {
            e.printStackTrace();
            logger.error(e);
            result.setCode(ResultCode.RESULT_FAILURE.getCode());
            result.setResultDes("二维码解析失败！");
            model.addAttribute(SysConst.RESULT_KEY, result);
            return model;
        } catch (Exception e) {
        	logger.error(e);
            e.printStackTrace();
            result.setCode(ResultCode.RESULT_FAILURE.getCode());
            result.setResultDes("二维码解析失败！");
            model.addAttribute(SysConst.RESULT_KEY, result);
            return model;
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                	logger.error(e);
                    e.printStackTrace();
                }
            }
        }

        model.addAttribute(SysConst.RESULT_KEY, result);
        return model;
    }

    //获取给定的广告位编号对应的广告位id和相关有效的广告活动（仅【纠错任务】调用, 扫二维码获取对应广告位的接口）
    @RequestMapping(value = "/seatActivities")
    @ResponseBody
    public Model getSeatActivity(Model model, HttpServletRequest request) {
        ResultVo result = new ResultVo();
        result.setCode(ResultCode.RESULT_SUCCESS.getCode());
        result.setResultDes("获取成功");
        result.setResult("");
        model = new ExtendedModelMap();
        saveLog(request, "");

        InputStream is = null;
        String seatCode = null;
        Integer adSeatId = null;
        Double lon = null;
        Double lat = null;
        String title = null;
        String memo = null;
        
        try {
            is = request.getInputStream();
            Gson gson = new Gson();
            JsonObject obj = gson.fromJson(new InputStreamReader(is), JsonObject.class);
            seatCode = obj==null||obj.get("seatCode") == null ? null : obj.get("seatCode").getAsString(); //[1] 扫描二维码调取接口
            adSeatId = obj==null||obj.get("adSeatId") == null ? null : obj.get("adSeatId").getAsInt();
            lon = obj==null||obj.get("lon") == null ? null : obj.get("lon").getAsDouble(); //[2] 通过经纬度调取接口
            lat = obj==null||obj.get("lat") == null ? null : obj.get("lat").getAsDouble(); //[2] 通过经纬度调取接口
            title = obj==null||obj.get("title") == null ? null : obj.get("title").getAsString(); //[2] 通过经纬度调取接口
            memo = obj==null||obj.get("memo") == null ? null : obj.get("memo").getAsString();//[3] 通过memo调取接口
        } catch (IOException e) {
        	logger.error(e);
            result.setCode(ResultCode.RESULT_FAILURE.getCode());
            result.setResultDes("系统繁忙，请稍后再试！");
            model.addAttribute(SysConst.RESULT_KEY, result);
            return model;
        }finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                	logger.error(e);
                    e.printStackTrace();
                }
            }
        }
        
        try {
        	List<AdJiucuoTask> jiucuoTasks = new ArrayList<>();
            List<AdActivityAdseatVo> list = null;
            List<AdActivityAdseatVo> list1 = new ArrayList<>();//当前时间广告位的活动已开始
            List<AdActivityAdseatVo> list2 = new ArrayList<>();//当前时间广告位的活动暂未开始
            List<AdActivityAdseatVo> list3 = new ArrayList<>();//当前时间广告位的活动已结束
            if(StringUtil.isNotEmpty(seatCode)){
            	//[1] 扫描二维码调取接口
                list = adActivityService.getActivitySeatBySeatCode(seatCode);
                Date now = new Date();
                
                for(AdActivityAdseatVo vo : list) {
                	if((now.compareTo(vo.getMonitorStart())>=0) && (now.compareTo(vo.getMonitorEnd())<=0)) {
                		list1.add(vo);
                	}else if(now.compareTo(vo.getMonitorStart())<=0) {
                		list2.add(vo);
                	}else if(now.compareTo(vo.getMonitorEnd())>=0) {
                		list3.add(vo);
                	}
                }
                if(list1.size()>0) {
            		Map<String, Object> searchMap = new HashMap<>();
                	searchMap.put("status", 1); //待审核
                	searchMap.put("adSeatCode", seatCode); //二维码信息
                	jiucuoTasks = adJiucuoTaskService.selectInfoByQrCode(searchMap);
                    
                	//移除
                	Iterator<AdActivityAdseatVo> iterator = list1.iterator();
                	while (iterator.hasNext()) {
    					AdActivityAdseatVo adActivityAdseatVo = (AdActivityAdseatVo) iterator.next();
    					for (AdJiucuoTask task : jiucuoTasks) {
    						if(task.getActivityId() == adActivityAdseatVo.getActivityId()) {
    							iterator.remove();
    							break;
    						}
    					}
    				}
                	
            	}else if(list1.size() == 0 && list2.size()>0){
            		result.setCode(ResultCode.RESULT_FAILURE.getCode());
                    result.setResultDes("广告位的活动暂未开始！");
                    model.addAttribute(SysConst.RESULT_KEY, result);
                    return model;
            	}else if(list1.size()==0 && list2.size()==0 && list3.size()>0) {
            		result.setCode(ResultCode.RESULT_FAILURE.getCode());
                    result.setResultDes("广告位的活动已结束！");
                    model.addAttribute(SysConst.RESULT_KEY, result);
                    return model;
            	}else {
            		result.setCode(ResultCode.RESULT_FAILURE.getCode());
                    result.setResultDes("广告位暂无活动！");
                    model.addAttribute(SysConst.RESULT_KEY, result);
                    return model;
            	}
                
//                if(list == null || list.size() == 0) {
//                	result.setCode(ResultCode.RESULT_FAILURE.getCode());
//                    result.setResultDes("广告位暂无活动！");
//                    model.addAttribute(SysConst.RESULT_KEY, result);
//                    return model;
//                }
                
                
            } else if(lon != null && lat != null && StringUtil.isNotEmpty(title)) {
            	//[2] 通过经纬度调取接口
                list1 = adActivityService.selectVoByLonLatTitle(lon, lat, title);
                if(list1 == null || list1.size() == 0) {
                	result.setCode(ResultCode.RESULT_FAILURE.getCode());
                    result.setResultDes("没有查询到经纬度信息！");
                    model.addAttribute(SysConst.RESULT_KEY, result);
                    return model;
                }
                
                Map<String, Object> searchMap = new HashMap<>();
            	searchMap.put("status", 1); //待审核
            	searchMap.put("lon", lon); //经度
            	searchMap.put("lat", lat); //纬度
            	searchMap.put("title", title); //广告位名称
            	jiucuoTasks = adJiucuoTaskService.selectInfoByLonLatTitle(searchMap);
            	
            	//移除
            	Iterator<AdActivityAdseatVo> iterator = list1.iterator();
            	while (iterator.hasNext()) {
					AdActivityAdseatVo adActivityAdseatVo = (AdActivityAdseatVo) iterator.next();
					for (AdJiucuoTask task : jiucuoTasks) {
						if(task.getActivityId() == adActivityAdseatVo.getActivityId()) {
							iterator.remove();
							break;
						}
					}
				}
            } else if(StringUtil.isNotEmpty(memo)) {
            	//[3] 媒体方编号memo调取接口
                list1 = adActivityService.getActivitySeatByMemo(memo);
                if(list1 == null || list1.size() == 0) {
                	result.setCode(ResultCode.RESULT_FAILURE.getCode());
                    result.setResultDes("没有查询到媒体方编号信息！");
                    model.addAttribute(SysConst.RESULT_KEY, result);
                    return model;
                }
                
                Map<String, Object> searchMap = new HashMap<>();
            	searchMap.put("status", 1); //待审核
            	searchMap.put("memo", memo); //媒体方编号
            	jiucuoTasks = adJiucuoTaskService.selectInfoByMemo(searchMap);
                
            	//移除
            	Iterator<AdActivityAdseatVo> iterator = list1.iterator();
            	while (iterator.hasNext()) {
					AdActivityAdseatVo adActivityAdseatVo = (AdActivityAdseatVo) iterator.next();
					for (AdJiucuoTask task : jiucuoTasks) {
						if(task.getActivityId() == adActivityAdseatVo.getActivityId()) {
							iterator.remove();
							break;
						}
					}
				}
            } else {
            	//参数有误
            	result.setCode(ResultCode.RESULT_FAILURE.getCode());
                result.setResultDes("系统繁忙，请稍后再试！");
                model.addAttribute(SysConst.RESULT_KEY, result);
                return model;
            }
            
            QRCodeInfoVo qr = new QRCodeInfoVo();
//            qr.setAd_seat_id(Integer.valueOf(seatCode));
            if(list1 != null && list1.size() > 0) {
                for (AdActivityAdseatVo vo : list1) {
                    qr.getAd_activity_seats().add(new AdActivitySeatInfoInQRVO(vo));
                }
            } else {
            	result.setCode(ResultCode.RESULT_FAILURE.getCode());
                result.setResultDes("已有人正在执行该纠错任务！");
                model.addAttribute(SysConst.RESULT_KEY, result);
                return model;
            }
            result.setResult(qr);
        } catch (Exception e) {
        	logger.error(e);
            e.printStackTrace();
            result.setCode(ResultCode.RESULT_FAILURE.getCode());
            result.setResultDes("获取失败！");
            model.addAttribute(SysConst.RESULT_KEY, result);
            return model;
        }

        model.addAttribute(SysConst.RESULT_KEY, result);
        return model;
    }

    //登录
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public Model login(Model model, HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
        ResultVo<SysUserExecuteVo> result = new ResultVo<>();
        result.setCode(ResultCode.RESULT_SUCCESS.getCode());
        result.setResultDes("登录成功");
        model = new ExtendedModelMap();
        String username = null;
        String password = null;
        String vcode = null;
        String token = null;
        String appSid = null;
        String deviceId=null;
        String systemVersion=null;
        try {
            InputStream is = request.getInputStream();
            Gson gson = new Gson();
            JsonObject obj = gson.fromJson(new InputStreamReader(is), JsonObject.class);
            username = obj.get("username") == null ? null : obj.get("username").getAsString();
            password = obj.get("password") == null ? null : obj.get("password").getAsString();
            vcode = obj.get("vcode") == null ? null : obj.get("vcode").getAsString();
            token = obj.get("token") == null ? null : obj.get("token").getAsString();
            appSid = obj.get("appSid") == null ? null : obj.get("appSid").getAsString();
            deviceId = obj.get("deviceId") == null ? null : obj.get("deviceId").getAsString();
            systemVersion = obj.get("systemVersion") == null ? null : obj.get("systemVersion").getAsString();
            if (token != null) {
                useSession.set(Boolean.FALSE);
                this.sessionByRedis.setToken(token);
            } else {
                useSession.set(Boolean.TRUE);
            }
        } catch (IOException e) {
        	logger.error(e);
            result.setCode(ResultCode.RESULT_FAILURE.getCode());
            result.setResultDes("系统繁忙，请稍后再试！");
            model.addAttribute(SysConst.RESULT_KEY, result);
            return model;
        }

        // 账户必须验证
        if (StringUtils.isEmpty(username)) {
            result.setCode(ResultCode.RESULT_FAILURE.getCode());
            result.setResultDes("用户名为必填！");
            model.addAttribute(SysConst.RESULT_KEY, result);
            return model;
        }

        // 密码必须验证
        if (StringUtils.isEmpty(password)) {
            result.setCode(ResultCode.RESULT_FAILURE.getCode());
            result.setResultDes("密码为必填！");
            model.addAttribute(SysConst.RESULT_KEY, result);
            return model;
        }

        // 验证码必须验证
        if (StringUtils.isEmpty(vcode)) {
            result.setCode(ResultCode.RESULT_FAILURE.getCode());
            result.setResultDes("验证码为必填！");
            model.addAttribute(SysConst.RESULT_KEY, result);
            return model;
        }

        String sessionCode = null;
        HttpSession session = request.getSession();
        if (useSession.get()) {
            sessionCode = session.getAttribute(SessionKey.SESSION_CODE.toString()) == null ? ""
                    : session.getAttribute(SessionKey.SESSION_CODE.toString()).toString();
        } else {
            sessionCode = sessionByRedis.getImageCode();
        }

        // 验证码有效验证
        if (!vcode.equalsIgnoreCase(sessionCode)) {
            result.setCode(ResultCode.RESULT_FAILURE.getCode());
            result.setResultDes("验证码错误！");
            model.addAttribute(SysConst.RESULT_KEY, result);
            return model;
        }

        Subject subject = SecurityUtils.getSubject();
        String md5Pwd = new Md5Hash(password, username).toString();
//        UsernamePasswordToken token = new UsernamePasswordToken(username, md5Pwd);
//        try {
//            // 账号密码有效性验证
//            subject.login(token);
//
//            // 获取用户信息
//            SysUser findUser = getLoginUser();
//
//            // 账户停用的时候
//            if (findUser.getStatus()!=null&&findUser.getStatus() == 2) {
//                // 清除session中的用户信息
//                ShiroUtils.removeAttribute(SessionKey.SESSION_LOGIN_USER.toString());
////                model.addAttribute(SysConst.RESULT_KEY, "该账户已被停用，请联系管理员");
////                model.addAttribute("username", user.getUsername());
////                return PageConst.LOGIN_PAGE;
//            }
//
//        } catch (AuthenticationException ae) {
//            model.addAttribute(SysConst.RESULT_KEY, "用户名或密码错误");
////            model.addAttribute("username", user.getUsername());
////            return PageConst.LOGIN_PAGE;
//        }
       
        SysUserExecute userExecute = sysUserExecuteService.getByUsername(username);
        if (userExecute == null || !md5Pwd.equals(userExecute.getPassword())) {
            result.setCode(ResultCode.RESULT_FAILURE.getCode());
            result.setResultDes("用户名或密码有误！");
            model.addAttribute(SysConst.RESULT_KEY, result);
            return model;
        }
        if (!userExecute.getStatus().equals(Integer.valueOf(1))) {
            result.setCode(ResultCode.RESULT_FAILURE.getCode());
            result.setResultDes("账号已被停用！");
            model.addAttribute(SysConst.RESULT_KEY, result);
            return model;
        }
        
        if(StringUtil.isNotBlank(deviceId) && StringUtil.isNotBlank(systemVersion)) {
        	SysUserExecute sysUserExecute = new SysUserExecute();
        	sysUserExecute.setId(userExecute.getId());
        	sysUserExecute.setDeviceId(deviceId);
        	sysUserExecute.setSystemVersion(systemVersion);
        	sysUserExecuteService.updatePhoneModel(sysUserExecute);
        }
        saveLog(request, username);
        //登录日志
        AddressUtils addressUtils = new AddressUtils();
        String  address = addressUtils.getAddressesByBaidu( getIp(), "utf-8");
        Date now = new Date();	           
        LoginLog loginlog=new LoginLog();   
        loginlog.setUserId(userExecute.getId());
        loginlog.setType(LoginLogTypeEnum.APP.getId());
        loginlog.setIp(getIp());
        loginlog.setLocation(address);
        loginlog.setCreateTime(now);
		loginLogService.save(loginlog);   
        
//        //客户登录APP, 校验appSid
//        if(userExecute.getUsertype() == 2) {
//        	System.out.println(userExecute.getAppSid());
//        	System.out.println(appSid);
//        	if(!StringUtil.equals(userExecute.getAppSid(), appSid)) {
//        		result.setCode(ResultCode.RESULT_FAILURE.getCode());
//                result.setResultDes("用户名或密码有误！");
//                model.addAttribute(SysConst.RESULT_KEY, result);
//                return model;
//        	}
//        } 

        if (useSession.get()) {
            session.setAttribute(SessionKey.SESSION_LOGIN_USER.toString(), userExecute);
        } else {
            sessionByRedis.setAttribute(SessionKey.SESSION_LOGIN_USER.toString(), userExecute);
        }

        result.setResult(new SysUserExecuteVo(userExecute));
        model.addAttribute(SysConst.RESULT_KEY, result);
//        response.getHeaders().add("Access-Control-Allow-Credentials","true");
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("origin"));
        response.setHeader("Access-Control-Allow-Credentials", "true");
        return model;
    }

    //退出
    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    @ResponseBody
    public Model logOut(Model model, HttpServletRequest request, HttpServletResponse response) {
        ResultVo<SysUserExecuteVo> result = new ResultVo<>();
        result.setCode(ResultCode.RESULT_SUCCESS.getCode());
        result.setResultDes("登出成功");
        model = new ExtendedModelMap();

        String token = null;

        try {
            InputStream is = request.getInputStream();
            Gson gson = new Gson();
            JsonObject obj = gson.fromJson(new InputStreamReader(is), JsonObject.class);
            token = obj.get("token") == null ? null : obj.get("token").getAsString();
            if (token != null) {
                useSession.set(Boolean.FALSE);
                this.sessionByRedis.setToken(token);
            } else {
                useSession.set(Boolean.TRUE);
            }
        } catch (IOException e) {
        	logger.error(e);
            result.setCode(ResultCode.RESULT_FAILURE.getCode());
            result.setResultDes("系统繁忙，请稍后再试！");
            model.addAttribute(SysConst.RESULT_KEY, result);
            return model;
        }
        SysUserExecute user = getLoginUser(request, token);
        saveLog(request, user==null?"":user.getUsername());
        if (useSession.get()) {
            HttpSession session = request.getSession();
            
            session.removeAttribute(SessionKey.SESSION_LOGIN_USER.toString());
        } else {
            this.sessionByRedis.remove();
        }

        model.addAttribute(SysConst.RESULT_KEY, result);
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("origin"));
        response.setHeader("Access-Control-Allow-Credentials", "true");
        return model;
    }

    //获取图片验证码
    @RequestMapping(value = "/getCodeBase64")
    @ResponseBody
    public Model getCode(Model model, HttpServletRequest request, HttpServletResponse response) {
    	saveLog(request, "");
        ResultVo result = new ResultVo();
        result.setCode(ResultCode.RESULT_SUCCESS.getCode());
        result.setResultDes("获取验证码成功");
        model = new ExtendedModelMap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            // 创建一张空白的图片
            BufferedImage image = new BufferedImage(100, 30, BufferedImage.TYPE_INT_RGB);
            // 获取该图片的笔画
            Graphics g = image.getGraphics();

            // 绘制背景
            // 设置画笔的颜色
            Random r = new Random();
            g.setColor(new Color(249, 249, 250));
            // 绘制一个实心额矩形区域
            g.fillRect(0, 0, 100, 30);
            // 绘制内容
            g.setColor(new Color(r.nextInt(255), r.nextInt(255), r.nextInt(255)));
            g.setFont(new Font(null, Font.BOLD, 25));

            // 生成验证码
            String num = getNumber(5);
            g.drawString(num, 5, 25);

            // 验证码的内容保存到session中
//        HttpSession session = request.getSession();
//        System.out.println(session.getId());
//        session.setAttribute(SessionKey.SESSION_CODE.toString(), num);
            String token = sessionByRedis.initToken();
            sessionByRedis.setImageCode(num);
            ImageCodeResultVo res = new ImageCodeResultVo();
            res.setToken(token);

            ImageIO.write(image, "jpeg", baos);
            res.setImage_code_base64("data:image/jpeg;base64," + Base64.getEncoder().encodeToString(baos.toByteArray()));
            result.setResult(res);
            model.addAttribute(SysConst.RESULT_KEY, result);
        } catch (IOException e) {
        	logger.error(e);
            result.setCode(ResultCode.RESULT_FAILURE.getCode());
            result.setResultDes("系统繁忙，请稍后再试！");
            model.addAttribute(SysConst.RESULT_KEY, result);
            return model;
        } finally {
            try {
                baos.flush();
                baos.close();
            } catch (IOException e) {
            	logger.error(e);
            }
        }

        response.setHeader("Access-Control-Allow-Origin", request.getHeader("origin"));
        response.setHeader("Access-Control-Allow-Credentials", "true");
        return model;
    }

    //请求积分明细列表
    @RequestMapping(value = "/getpointlist")
    @ResponseBody
    public Model pointList(Model model, HttpServletRequest request, HttpServletResponse response) throws IOException {
    	ResultVo result = new ResultVo();
        result.setCode(ResultCode.RESULT_SUCCESS.getCode());
        result.setResultDes("获取成功");
        model = new ExtendedModelMap();
    	
        Integer userId = null;
        String token = null;
        Integer page = 1;
        Integer pageSize = 10;
        String updateTime = null;
        
        try {
            InputStream is = request.getInputStream();
            Gson gson = new Gson();
            JsonObject obj = gson.fromJson(new InputStreamReader(is), JsonObject.class);
            token = obj.get("token") == null ? null : obj.get("token").getAsString();
            userId = obj.get("userId").getAsInt();
            updateTime = obj.get("updateTime") == null ? null : obj.get("updateTime").getAsString();
            if (token != null) {
                useSession.set(Boolean.FALSE);
                this.sessionByRedis.setToken(token);
            } else {
                useSession.set(Boolean.TRUE);
            }
            if(obj.get("page") != null){
                page = obj.get("page").getAsInt();
            }
            if(obj.get("page_size") != null){
                pageSize = obj.get("page_size").getAsInt();
            }
        } catch (IOException e) {
        	logger.error(e);
            result.setCode(ResultCode.RESULT_FAILURE.getCode());
            result.setResultDes("系统繁忙，请稍后再试！");
            model.addAttribute(SysConst.RESULT_KEY, result);
            return model;
        }
        
        saveLog(request, userId+"");
        SearchDataVo vo = new SearchDataVo(null,null,(page-1)*pageSize,pageSize);
        vo.putSearchParam("userId",null,userId);
        vo.putSearchParam("updateTime", null, updateTime);
        //通过用户id查找该用户的所有积分数据
        userPointService.getPageData(vo);
        List<?> list = vo.getList();
        Integer totalPoint = userPointService.getPointCountById(userId);
        if(totalPoint == null) {
        	totalPoint = 0;
        }
        List<AdUserPoint> userpointlist = new ArrayList<>();
        
        for(Object obj : list) {
        	AdUserPoint userPoint = (AdUserPoint) obj;
        	userPoint.setCreateTimeStr(DateUtil.dateFormate(userPoint.getCreateTime(), "MM-dd HH:mm"));
        	userPoint.setUpdateTimeStr(DateUtil.dateFormate(userPoint.getUpdateTime(), "MM-dd HH:mm"));
        	userpointlist.add(userPoint);
        }
        //设置总页数
        int totalCount = (int) ((vo.getCount() + pageSize - 1) / pageSize);
        result.setResult(userpointlist);
        model.addAttribute(SysConst.RESULT_KEY, result);
        model.addAttribute("totalPoint", totalPoint);
        model.addAttribute("totalCount", totalCount);
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("origin"));
        response.setHeader("Access-Control-Allow-Credentials", "true");
        return model;
    }
    
    //请求金额明细列表
    @RequestMapping(value = "/getmoneylist")
    @ResponseBody
    public Model moneyList(Model model, HttpServletRequest request, HttpServletResponse response) throws IOException {
    	ResultVo result = new ResultVo();
        result.setCode(ResultCode.RESULT_SUCCESS.getCode());
        result.setResultDes("获取成功");
        model = new ExtendedModelMap();
    	
        Integer userId = null;
        String token = null;
        Integer page = 1;
        Integer pageSize = 10;
        String updateTime = null;
        
        try {
            InputStream is = request.getInputStream();
            Gson gson = new Gson();
            JsonObject obj = gson.fromJson(new InputStreamReader(is), JsonObject.class);
            token = obj.get("token") == null ? null : obj.get("token").getAsString();
            userId = obj.get("userId").getAsInt();
            updateTime = obj.get("updateTime") == null ? null : obj.get("updateTime").getAsString();
            if (token != null) {
                useSession.set(Boolean.FALSE);
                this.sessionByRedis.setToken(token);
            } else {
                useSession.set(Boolean.TRUE);
            }
            if(obj.get("page") != null){
                page = obj.get("page").getAsInt();
            }
            if(obj.get("page_size") != null){
                pageSize = obj.get("page_size").getAsInt();
            }
        } catch (IOException e) {
        	logger.error(e);
            result.setCode(ResultCode.RESULT_FAILURE.getCode());
            result.setResultDes("系统繁忙，请稍后再试！");
            model.addAttribute(SysConst.RESULT_KEY, result);
            return model;
        }
        
        saveLog(request, userId+"");
        SearchDataVo vo = new SearchDataVo(null,null,(page-1)*pageSize,pageSize);
        vo.putSearchParam("userId",null,userId);
        vo.putSearchParam("updateTime", null, updateTime);
        //通过用户id查找该用户的所有金额数据
        userMoneyService.getPageData(vo);
        List<?> list = vo.getList();
        Double totalMoney = userMoneyService.getMoneyCountById(userId);
        if(totalMoney == null) {
        	totalMoney = 0.00;
        }
        List<AdUserMoney> usermoneylist = new ArrayList<>();
        
        for(Object obj : list) {
        	AdUserMoney userMoney = (AdUserMoney) obj;
        	userMoney.setCreateTimeStr(DateUtil.dateFormate(userMoney.getCreateTime(), "MM-dd HH:mm"));
        	userMoney.setUpdateTimeStr(DateUtil.dateFormate(userMoney.getUpdateTime(), "MM-dd HH:mm"));
        	usermoneylist.add(userMoney);
        }
        //设置总页数
        int totalCount = (int) ((vo.getCount() + pageSize - 1) / pageSize);
        result.setResult(usermoneylist);
        model.addAttribute(SysConst.RESULT_KEY, result);
        model.addAttribute("totalMoney", totalMoney);
        model.addAttribute("totalCount", totalCount);
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("origin"));
        response.setHeader("Access-Control-Allow-Credentials", "true");
        return model;
    }
    
    //请求推送消息列表
    @RequestMapping(value = "/getpushlist")
    @ResponseBody
    public Model pushList(Model model, HttpServletRequest request, HttpServletResponse response) throws IOException {
    	ResultVo result = new ResultVo();
        result.setCode(ResultCode.RESULT_SUCCESS.getCode());
        result.setResultDes("获取成功");
        model = new ExtendedModelMap();
    	
        Integer userId = null;
        String token = null;
        Integer page = 1;
        Integer pageSize = 10;
        String createTime = null;
        
        try {
            InputStream is = request.getInputStream();
            Gson gson = new Gson();
            JsonObject obj = gson.fromJson(new InputStreamReader(is), JsonObject.class);
            token = obj.get("token") == null ? null : obj.get("token").getAsString();
            userId = obj.get("userId").getAsInt();
            createTime = obj.get("createTime") == null ? null : obj.get("createTime").getAsString();
            if (token != null) {
                useSession.set(Boolean.FALSE);
                this.sessionByRedis.setToken(token);
            } else {
                useSession.set(Boolean.TRUE);
            }
            if(obj.get("page") != null){
                page = obj.get("page").getAsInt();
            }
            if(obj.get("page_size") != null){
                pageSize = obj.get("page_size").getAsInt();
            }
        } catch (IOException e) {
        	logger.error(e);
            result.setCode(ResultCode.RESULT_FAILURE.getCode());
            result.setResultDes("系统繁忙，请稍后再试！");
            model.addAttribute(SysConst.RESULT_KEY, result);
            return model;
        }
        
        saveLog(request, userId+"");
        SearchDataVo vo = new SearchDataVo(null,null,(page-1)*pageSize,pageSize);
        vo.putSearchParam("userId",null,userId);
        vo.putSearchParam("createTime", null, createTime);
        //通过用户id查找该用户的所有推送数据
        adSystemPushService.getPageData(vo);
        List<?> list = vo.getList();
     
        List<AdSystemPush> pushlist = new ArrayList<>();
        for(Object obj : list) {
        	AdSystemPush userPush = (AdSystemPush) obj;
        	userPush.setCreateTimeStr(DateUtil.dateFormate(userPush.getCreateTime(), "MM-dd HH:mm"));
        	pushlist.add(userPush);
        }
        //设置总页数
        int totalCount = (int) ((vo.getCount() + pageSize - 1) / pageSize);
        result.setResult(pushlist);
        model.addAttribute(SysConst.RESULT_KEY, result);
        model.addAttribute("totalCount", totalCount);
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("origin"));
        response.setHeader("Access-Control-Allow-Credentials", "true");
        return model;
    }
    
    //请求监测任务 或 纠错任务列表
    @RequestMapping(value = "/gettasklist")
    @ResponseBody
    public Model taskList(Model model, HttpServletRequest request, HttpServletResponse response) {
        ResultVo result = new ResultVo();
        result.setCode(ResultCode.RESULT_SUCCESS.getCode());
        result.setResultDes("获取成功");
        model = new ExtendedModelMap();

        Integer type = null;
        String token = null;
        String memo = null;
        Integer page = 1;
        Integer pageSize = 10;
        String updateTime = null;
        Integer status = null;
        try {
            InputStream is = request.getInputStream();
            Gson gson = new Gson();
            JsonObject obj = gson.fromJson(new InputStreamReader(is), JsonObject.class);
            type = obj.get("type").getAsInt();
            token = obj.get("token") == null ? null : obj.get("token").getAsString();
            memo = obj.get("memo") == null ? null : obj.get("memo").getAsString();
            status = obj.get("status") == null ? null : obj.get("status").getAsInt();
            updateTime = obj.get("updateTime") == null ? null : obj.get("updateTime").getAsString();
            if (token != null) {
                useSession.set(Boolean.FALSE);
                this.sessionByRedis.setToken(token);
            } else {
                useSession.set(Boolean.TRUE);
            }
            if(obj.get("page") != null){
                page = obj.get("page").getAsInt();
            }
            if(obj.get("page_size") != null){
                pageSize = obj.get("page_size").getAsInt();
            }
        } catch (IOException e) {
        	logger.error(e);
            result.setCode(ResultCode.RESULT_FAILURE.getCode());
            result.setResultDes("系统繁忙，请稍后再试！");
            model.addAttribute(SysConst.RESULT_KEY, result);
            return model;
        }

        //验证登录
        if (useSession.get()) {
            if (!checkLogin(model, result, request)) {
                return model;
            }
        } else {
            if (!checkLogin(model, result, token)) {
                return model;
            }
        }

        SysUserExecute user = getLoginUser(request, token);
        saveLog(request, user.getUsername());
        SearchDataVo datavo = new SearchDataVo(null,null,(page-1)*pageSize,pageSize);
        datavo.putSearchParam("userId",null,user.getId());
        
        //监测任务列表(已修改：添加了province, city, region, street, startTime, endTime, assignType)
        if (type == AppTaskEnum.MONITOR_TASK.getId()) {
        	if(status == MonitorTaskStatus.VERIFY.getId()) {
            	//9：已审核 包括 审核通过 和 审核未通过
            	List<Integer> statuses = new ArrayList<>();
            	statuses.add(MonitorTaskStatus.VERIFIED.getId());
            	statuses.add(MonitorTaskStatus.VERIFY_FAILURE.getId());
            	datavo.putSearchParam("statuses", null, statuses);
            } else {
            	datavo.putSearchParam("status", null, status);
            }
            datavo.putSearchParam("updateTime", null, updateTime);
            //List<AdMonitorTaskMobileVo> tasks = adMonitorTaskService.getByUserIdForMobile(user.getId());
            Integer userTaskStatus = AppUserTaskEnum.NORMAL.getId(); //状态：1.正常 2.主动放弃 3.超时回收 4.修改指派
            datavo.putSearchParam("userTaskStatus", null, userTaskStatus);
            adMonitorTaskService.getTaskPageData(datavo);
            List<AdMonitorTaskMobileVo> list = (List<AdMonitorTaskMobileVo>) datavo.getList();
            MonitorTaskListResultVo resultVo = new MonitorTaskListResultVo();
            
            for (AdMonitorTaskMobileVo task : list) {
                if (task.getStatus() == MonitorTaskStatus.TO_CARRY_OUT.getId()) {
                    MonitorTaskWaitToExecutedVo vo = new MonitorTaskWaitToExecutedVo(task);
                    vo.setProvince(cityCache.getCityName(task.getProvince()));
                    vo.setCity(cityCache.getCityName(task.getCity()));
                    vo.setRegion(cityCache.getCityName(task.getRegion()));
                    vo.setStreet(cityCache.getCityName(task.getStreet()));
                    //若没有贴上二维码, 清空二维码信息
                    if(vo.getAdCodeFlag() == AdCodeFlagEnum.NO.getId()) {
                    	vo.setAd_seat_code(null);
                    	vo.setAdCode(null);
                    }
                    resultVo.getWait_to_executed().add(vo);
                } else if (task.getStatus() == MonitorTaskStatus.UNVERIFY.getId()) {
                    MonitorTaskExecutingVo vo = new MonitorTaskExecutingVo(task);
                    vo.setProvince(cityCache.getCityName(task.getProvince()));
                    vo.setCity(cityCache.getCityName(task.getCity()));
                    vo.setRegion(cityCache.getCityName(task.getRegion()));
                    vo.setStreet(cityCache.getCityName(task.getStreet()));
                    
                    //若没有贴上二维码, 清空二维码信息
                    if(vo.getAdCodeFlag() == AdCodeFlagEnum.NO.getId()) {
                    	vo.setAdCode(null);
                    }
                    resultVo.getExecuting().add(vo);
                } else if (task.getStatus() == MonitorTaskStatus.VERIFIED.getId() || task.getStatus() == MonitorTaskStatus.VERIFY_FAILURE.getId()) {
                    MonitorTaskCheckedVo vo = new MonitorTaskCheckedVo(task);
                    vo.setProvince(cityCache.getCityName(task.getProvince()));
                    vo.setCity(cityCache.getCityName(task.getCity()));
                    vo.setRegion(cityCache.getCityName(task.getRegion()));
                    vo.setStreet(cityCache.getCityName(task.getStreet()));
                    //若没有贴上二维码, 清空二维码信息
                    if(vo.getAdCodeFlag() == AdCodeFlagEnum.NO.getId()) {
                    	vo.setAd_seat_code(null);
                    	vo.setAdCode(null);
                    }
                    
                    if(task.getStatus() == MonitorTaskStatus.VERIFY_FAILURE.getId()) {
                    	vo.setPicUrl1Status(task.getPicUrl1Status());
                    	vo.setPicUrl2Status(task.getPicUrl2Status());
                    	vo.setPicUrl3Status(task.getPicUrl3Status());
                    	vo.setPicUrl4Status(task.getPicUrl4Status());
                    }
                    resultVo.getChecked().add(vo);
                } else if (task.getStatus() == MonitorTaskStatus.UN_FINISHED.getId()) {
                	MonitorTaskUnFinishedVo vo = new MonitorTaskUnFinishedVo(task);
                	vo.setProvince(cityCache.getCityName(task.getProvince()));
                    vo.setCity(cityCache.getCityName(task.getCity()));
                	vo.setRegion(cityCache.getCityName(task.getRegion()));
                    vo.setStreet(cityCache.getCityName(task.getStreet()));
                    //若没有贴上二维码, 清空二维码信息
                    if(vo.getAdCodeFlag() == AdCodeFlagEnum.NO.getId()) {
                    	vo.setAd_seat_code(null);
                    	vo.setAdCode(null);
                    }
                    resultVo.getUn_finished().add(vo);
                }
            }
            
            result.setResult(resultVo);
        } else if (type == AppTaskEnum.JIUCUO_TASK.getId()) {
        	if(status == JiucuoTaskStatus.VERIFY.getId()) {
            	//4：纠错审核已结束 包括 审核通过 和 审核未通过
            	List<Integer> statuses = new ArrayList<>();
            	statuses.add(JiucuoTaskStatus.VERIFIED.getId());
            	statuses.add(JiucuoTaskStatus.VERIFY_FAILURE.getId());
            	datavo.putSearchParam("statuses", null, statuses);
            } else {
            	datavo.putSearchParam("status", null, status);
            } 
        	//纠错任务列表
//            List<AdJiucuoTaskMobileVo> tasks = adJiucuoTaskService.getByUserIdForMobile(user.getId());
            adJiucuoTaskService.getJiucuoPageData(datavo);
            List<AdJiucuoTaskMobileVo> tasks = (List<AdJiucuoTaskMobileVo>) datavo.getList();
            JiucuoTaskListResultVo resultVo = new JiucuoTaskListResultVo();
            for (AdJiucuoTaskMobileVo task : tasks) {
                if (task.getStatus() == JiucuoTaskStatus.UNVERIFY.getId()) {
                    resultVo.getJiucuo_submit().add(new JiucuoTaskVo(task));
                } else if (task.getStatus() == JiucuoTaskStatus.VERIFIED.getId() || task.getStatus() == JiucuoTaskStatus.VERIFY_FAILURE.getId()) {
                    resultVo.getJiucuo_success().add(new JiucuoTaskVo(task));
                } else if (task.getStatus() == JiucuoTaskStatus.VERIFY.getId()) {
                    resultVo.getJiucuo_success().add(new JiucuoTaskVo(task));
                } 
            }
            result.setResult(resultVo);
        }
        //设置总页数
        int totalCount = (int) ((datavo.getCount() + pageSize - 1) / pageSize);
        model.addAttribute("totalCount", totalCount);
        model.addAttribute(SysConst.RESULT_KEY, result);
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("origin"));
        response.setHeader("Access-Control-Allow-Credentials", "true");
        return model;
    }

    //提交监测任务反馈
//    @RequestMapping(value="/tasksubmit")
    @ResponseBody
    public Model feedback(Model model, HttpServletRequest request, HttpServletResponse response,
                          @RequestParam(value = "token", required = false) String token,
                          @RequestParam(value = "type", required = false) Integer type,
                          @RequestParam(value = "task_id", required = false) Integer taskId,
                          @RequestParam(value = "lon", required = false) Double lon,
                          @RequestParam(value = "lat", required = false) Double lat,
                          @RequestParam(value = "ad_activity_seat_id", required = false) Integer adActivitySeatId,
                          @RequestParam(value = "ad_seat_code", required = false) String adSeatCode,
                          @RequestParam(value = "problem", required = false) String problem,
                          @RequestParam(value = "other", required = false) String other,
//                              @RequestParam(value = "pic", required = false) MultipartFile[] files) {
                          @RequestParam(value = "pic1", required = false) MultipartFile file1,
                          @RequestParam(value = "pic2", required = false) MultipartFile file2,
                          @RequestParam(value = "pic3", required = false) MultipartFile file3,
                          @RequestParam(value = "pic4", required = false) MultipartFile file4) {
        ResultVo result = new ResultVo();
        result.setCode(ResultCode.RESULT_SUCCESS.getCode());
        result.setResultDes("提交成功");
        model = new ExtendedModelMap();

        SysUserExecute user = getLoginUser(request, token);
        
        if (token != null) {
            useSession.set(Boolean.FALSE);
            this.sessionByRedis.setToken(token);
        } else {
            useSession.set(Boolean.TRUE);
        }

        if (useSession.get()) {
            if (!checkLogin(model, result, request)) {
                return model;
            }
        } else {
            if (!checkLogin(model, result, token)) {
                return model;
            }
        }

        //参数不对
        if (type == null) {
            result.setCode(ResultCode.RESULT_PARAM_ERROR.getCode());
            result.setResultDes("参数有误！");
            model.addAttribute(SysConst.RESULT_KEY, result);
            return model;
        }
        String path = request.getRealPath("/");
        path = path + (path.endsWith(File.separator) ? "" : File.separatorChar) + "static" + File.separatorChar + "upload" + File.separatorChar;
        if (type == AppTaskEnum.MONITOR_TASK.getId()) {
            //参数不对
            if (type == null || taskId == null || file1 == null || file2 == null || file3 == null || file4 == null) {
                result.setCode(ResultCode.RESULT_PARAM_ERROR.getCode());
                result.setResultDes("参数有误！");
                model.addAttribute(SysConst.RESULT_KEY, result);
                return model;
            }

            InputStream is1 = null;
            InputStream is2 = null;
            InputStream is3 = null;
            InputStream is4 = null;
            String filename1 = null;
            String filename2 = null;
            String filename3 = null;
            String filename4 = null;

            try {
                is1 = file1.getInputStream();
                filename1 = UploadFileUtil.saveFile(path, file1.getOriginalFilename(), is1);
                is2 = file2.getInputStream();
                filename2 = UploadFileUtil.saveFile(path, file2.getOriginalFilename(), is2);
                is3 = file3.getInputStream();
                filename3 = UploadFileUtil.saveFile(path, file3.getOriginalFilename(), is3);
                is4 = file4.getInputStream();
                filename4 = UploadFileUtil.saveFile(path, file4.getOriginalFilename(), is4);
                if (filename1 == null || filename2 == null || filename3 == null | filename4 == null) {
                    result.setCode(ResultCode.RESULT_PARAM_ERROR.getCode());
                    result.setResultDes("上传出错！");
                    model.addAttribute(SysConst.RESULT_KEY, result);
                    return model;
                }
                AdMonitorTaskFeedback feedback = new AdMonitorTaskFeedback();
                feedback.setLat(lat);
                feedback.setLon(lon);
                feedback.setPicUrl1("/static/upload/" + filename1);
                feedback.setPicUrl2("/static/upload/" + filename2);
                feedback.setPicUrl3("/static/upload/" + filename3);
                feedback.setPicUrl4("/static/upload/" + filename4);
                feedback.setProblem(problem);
                feedback.setProblemOther(other);
                feedback.setStatus(1);
                try {
                    adMonitorTaskService.feedback(taskId, feedback,adSeatCode, user);
                } catch (Exception e) {
                	logger.error(e);
                    result.setCode(ResultCode.RESULT_PARAM_ERROR.getCode());
                    result.setResultDes("保存出错！");
                    model.addAttribute(SysConst.RESULT_KEY, result);
                    return model;
                }
            } catch (IOException e) {
            	logger.error(e);
                result.setCode(ResultCode.RESULT_PARAM_ERROR.getCode());
                result.setResultDes("上传出错！");
                model.addAttribute(SysConst.RESULT_KEY, result);
                return model;
            }
        } else if (type == AppTaskEnum.JIUCUO_TASK.getId()) {
            //参数不对
            if (type == null || adActivitySeatId == null || file1 == null) {
                result.setCode(ResultCode.RESULT_PARAM_ERROR.getCode());
                result.setResultDes("参数有误！");
                model.addAttribute(SysConst.RESULT_KEY, result);
                return model;
            }
            AdActivityAdseat seat = adActivityService.getActivitySeatById(adActivitySeatId);
            if (seat == null) {
                result.setCode(ResultCode.RESULT_PARAM_ERROR.getCode());
                result.setResultDes("无效广告位信息！");
                model.addAttribute(SysConst.RESULT_KEY, result);
                return model;
            }
            InputStream is1 = null;
            String filename1 = null;

            try {
                is1 = file1.getInputStream();
                filename1 = UploadFileUtil.saveFile(path, file1.getOriginalFilename(), is1);
                if (filename1 == null) {
                    result.setCode(ResultCode.RESULT_PARAM_ERROR.getCode());
                    result.setResultDes("上传出错！");
                    model.addAttribute(SysConst.RESULT_KEY, result);
                    return model;
                }

                AdJiucuoTask task = new AdJiucuoTask();
                AdJiucuoTaskFeedback feedback = new AdJiucuoTaskFeedback();
                task.setStatus(JiucuoTaskStatus.UNVERIFY.getId());
                task.setActivityId(seat.getActivityId());
                task.setAdSeatId(seat.getAdSeatId());
                task.setUserId(user.getId());
                task.setProblemStatus(TaskProblemStatus.PROBLEM.getId());
                feedback.setLat(lat);
                feedback.setLon(lon);
                feedback.setPicUrl1("/static/upload/" + filename1);
                feedback.setProblem(problem);
                feedback.setProblemOther(other);

                try {
                    adJiucuoTaskService.feedback(task, feedback);
                } catch (Exception e) {
                	logger.error(e);
                    result.setCode(ResultCode.RESULT_PARAM_ERROR.getCode());
                    result.setResultDes("保存出错！");
                    model.addAttribute(SysConst.RESULT_KEY, result);
                    return model;
                }
            } catch (IOException e) {
            	logger.error(e);
                result.setCode(ResultCode.RESULT_PARAM_ERROR.getCode());
                result.setResultDes("上传出错！");
                model.addAttribute(SysConst.RESULT_KEY, result);
                return model;
            }
        } else {
            result.setCode(ResultCode.RESULT_PARAM_ERROR.getCode());
            result.setResultDes("参数错误！");
            model.addAttribute(SysConst.RESULT_KEY, result);
            return model;
        }

        model.addAttribute(SysConst.RESULT_KEY, result);
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("origin"));
        response.setHeader("Access-Control-Allow-Credentials", "true");
        return model;
    }

    //提交监测任务反馈,图片为base64
    @RequestMapping(value = "/tasksubmit")
    @ResponseBody
    public Model feedbackBase64(Model model, HttpServletRequest request, HttpServletResponse response,
                                @RequestParam(value = "token", required = false) String token,
                                @RequestParam(value = "type", required = false) Integer type,
                                @RequestParam(value = "task_id", required = false) Integer taskId,
                                @RequestParam(value = "seat_lon", required = false) Double seatLon,
                                @RequestParam(value = "seat_lat", required = false) Double seatLat,
                                @RequestParam(value = "lon", required = false) Double lon,
                                @RequestParam(value = "lat", required = false) Double lat,
                                @RequestParam(value = "ad_activity_seat_id", required = false) Integer adActivitySeatId,
                                @RequestParam(value = "ad_seat_code", required = false) String adSeatCode,
                                @RequestParam(value = "problem", required = false) String problem,
                                @RequestParam(value = "other", required = false) String other,
                                @RequestParam(value = "pic1", required = false) String file1,
                                @RequestParam(value = "pic2", required = false) String file2,
                                @RequestParam(value = "pic3", required = false) String file3,
                                @RequestParam(value = "pic4", required = false) String file4) {
        ResultVo result = new ResultVo();
        result.setCode(ResultCode.RESULT_SUCCESS.getCode());
        result.setResultDes("提交成功");
        model = new ExtendedModelMap();
        Date now = new Date();
        SysUserExecute user = getLoginUser(request, token);
        if (token != null) {
            useSession.set(Boolean.FALSE);
            this.sessionByRedis.setToken(token);
        } else {
            useSession.set(Boolean.TRUE);
        }

        if (useSession.get()) {
            if (!checkLogin(model, result, request)) {
                return model;
            }
        } else {
            if (!checkLogin(model, result, token)) {
                return model;
            }
        }
        saveLog(request, user.getUsername());
        //参数不对
        if (type == null) {
            result.setCode(ResultCode.RESULT_PARAM_ERROR.getCode());
            result.setResultDes("参数有误！");
            model.addAttribute(SysConst.RESULT_KEY, result);
            return model;
        }
        
        String path = file_upload_path;
        Calendar date = Calendar.getInstance();
        String timePath = date.get(Calendar.YEAR)+ File.separator + (date.get(Calendar.MONTH)+1) + File.separator+ date.get(Calendar.DAY_OF_MONTH) + File.separator;
        path = path + (path.endsWith(File.separator) ? "" : File.separatorChar) + "activity" + File.separatorChar;
        
        if (type == AppTaskEnum.MONITOR_TASK.getId()) {
        	AdMonitorTask adMonitorTask = adMonitorTaskService.selectByPrimaryKey(taskId);
        	path = path + adMonitorTask.getActivityId() + File.separatorChar + "monitor" + File.separator + timePath;
        	String servicePath = path.substring(path.indexOf(":")+1, path.length()).replaceAll("\\\\", "/");
        	servicePath = servicePath.replaceFirst("/opt/", "/");
            //参数不对
            if (type == null || taskId == null || (file1 == null && file2 == null && file3 == null && file4 == null)) {
                result.setCode(ResultCode.RESULT_PARAM_ERROR.getCode());
                result.setResultDes("参数有误！");                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                           
                model.addAttribute(SysConst.RESULT_KEY, result);
                return model;
            }
            
            //不是以data:image/jpeg;base64,开头的说明并没有重新拍照
            /**
             * 审核不通过重新执行的任务反馈中: 
             * 1、base64串 → 代表审核不通过的图片重新拍照执行
             * 2、null → 代表审核不通过但是照片没替换  或者  审核通过的图片
             */
            if (StringUtil.isNotBlank(file1) && !file1.startsWith("data:image/jpeg;base64,")) {
                file1 = null;
            }
            if (StringUtil.isNotBlank(file2) && !file2.startsWith("data:image/jpeg;base64,")) {
                file2 = null;
            }
            if (StringUtil.isNotBlank(file3) && !file3.startsWith("data:image/jpeg;base64,")) {
                file3 = null;
            }
            if (StringUtil.isNotBlank(file4) && !file4.startsWith("data:image/jpeg;base64,")) {
                file4 = null;
            }

            InputStream is1 = null;
            InputStream is2 = null;
            InputStream is3 = null;
            InputStream is4 = null;
            String filename1 = null;
            String filename2 = null;
            String filename3 = null;
            String filename4 = null;

            try {
                if (file1 != null) {
                    file1 = file1.replaceAll("data:image/jpeg;base64,", "");
//                    is1 = new ByteArrayInputStream(Base64.getDecoder().decode(file1));
                    is1 = new ByteArrayInputStream(org.apache.commons.codec.binary.Base64.decodeBase64(file1));
                    
                    filename1 = UploadFileUtil.saveFile(path, "image.jpg", is1);
                }
                if (file2 != null) {
                    file2 = file2.replaceAll("data:image/jpeg;base64,", "");
//                    is2 = new ByteArrayInputStream(Base64.getDecoder().decode(file2));
                    is2 = new ByteArrayInputStream(org.apache.commons.codec.binary.Base64.decodeBase64(file2));

                    filename2 = UploadFileUtil.saveFile(path, "image.jpg", is2);
                }
                if (file3 != null) {
                    file3 = file3.replaceAll("data:image/jpeg;base64,", "");
//                    is3 = new ByteArrayInputStream(Base64.getDecoder().decode(file3));
                    is3 = new ByteArrayInputStream(org.apache.commons.codec.binary.Base64.decodeBase64(file3));
                    filename3 = UploadFileUtil.saveFile(path, "image.jpg", is3);
                }
                if (file4 != null) {
                    file4 = file4.replaceAll("data:image/jpeg;base64,", "");
//                    is4 = new ByteArrayInputStream(Base64.getDecoder().decode(file4));
                    is4 = new ByteArrayInputStream(org.apache.commons.codec.binary.Base64.decodeBase64(file4));
                    filename4 = UploadFileUtil.saveFile(path, "image.jpg", is4);
                }
                if (filename1 == null && filename2 == null && filename3 == null && filename4 == null) {
                    result.setCode(ResultCode.RESULT_PARAM_ERROR.getCode());
                    result.setResultDes("上传图片失败，请稍后再试！");
                    model.addAttribute(SysConst.RESULT_KEY, result);
                    return model;
                }
                AdMonitorTaskFeedback feedback = new AdMonitorTaskFeedback();
                feedback.setSeatLat(seatLat);
                feedback.setSeatLon(seatLon);
                feedback.setLat(lat);
                feedback.setLon(lon);
                //path = path.substring(path.indexOf(":")+1, path.length()).replaceAll("\\\\", "/");
                if (filename1 != null) {
                	int index = filename1.indexOf('.');
                    MarkLogoUtil.markImageBySingleIcon(request.getSession().getServletContext().getRealPath("/")+"/static/images/jflogomin.png", path+filename1, path, filename1.substring(0, index), "jpg", null);
                    feedback.setPicUrl1(file_upload_ip + servicePath + filename1);    
                }
                if (filename2 != null) {
                	int index = filename2.indexOf('.');
                    MarkLogoUtil.markImageBySingleIcon(request.getSession().getServletContext().getRealPath("/")+"/static/images/jflogomin.png", path+filename2, path, filename2.substring(0, index), "jpg", null);
                    feedback.setPicUrl2(file_upload_ip + servicePath + filename2);
                }
                if (file3 != null) {
                	int index = filename3.indexOf('.');
                    MarkLogoUtil.markImageBySingleIcon(request.getSession().getServletContext().getRealPath("/")+"/static/images/jflogomin.png", path+filename3, path, filename3.substring(0, index), "jpg", null);
                    feedback.setPicUrl3(file_upload_ip + servicePath + filename3);
                }
                if (file4 != null) {
                	int index = filename4.indexOf('.');
                    MarkLogoUtil.markImageBySingleIcon(request.getSession().getServletContext().getRealPath("/")+"/static/images/jflogomin.png", path+filename4, path, filename4.substring(0, index), "jpg", null);
                    feedback.setPicUrl4(file_upload_ip + servicePath + filename4);
                }
                feedback.setProblem(problem);
                feedback.setProblemOther(other);
                feedback.setStatus(1);
                
                try {
                    adMonitorTaskService.feedback(taskId, feedback,adSeatCode, user);
                } catch (Exception e) {
                	logger.error(e);
                    result.setCode(ResultCode.RESULT_PARAM_ERROR.getCode());
                    result.setResultDes("保存出错！");
                    model.addAttribute(SysConst.RESULT_KEY, result);
                    return model;
                }
            } catch (Exception e) {
            	logger.error(e);
                e.printStackTrace();
                result.setCode(ResultCode.RESULT_PARAM_ERROR.getCode());
                result.setResultDes("上传出错！");
                model.addAttribute(SysConst.RESULT_KEY, result);
                return model;
            }
        } else if (type == AppTaskEnum.JIUCUO_TASK.getId()) {
            //参数不对
            if (type == null || adActivitySeatId == null || file1 == null) {
                result.setCode(ResultCode.RESULT_PARAM_ERROR.getCode());
                result.setResultDes("参数有误！");
                model.addAttribute(SysConst.RESULT_KEY, result);
                return model;
            }
            AdActivityAdseat seat = adActivityService.getActivitySeatById(adActivitySeatId);
            if (seat == null) {
                result.setCode(ResultCode.RESULT_PARAM_ERROR.getCode());
                result.setResultDes("无效广告位信息！");
                model.addAttribute(SysConst.RESULT_KEY, result);
                return model;
            }
            path = path + seat.getActivityId() + File.separator + "jiucuo" + File.separator + timePath;
        	String servicePath = path.substring(path.indexOf(":")+1, path.length()).replaceAll("\\\\", "/");
        	servicePath = servicePath.replaceFirst("/opt/", "/");
        	
            //查询是否有人正在做这个广告位这个活动的纠错任务(即待审核的纠错任务)
            Map<String, Object> searchMap = new HashMap<>();
            searchMap.put("activityId", seat.getActivityId()); //活动id
            searchMap.put("adSeatId", seat.getAdSeatId()); //广告位id
            searchMap.put("status", JiucuoTaskStatus.UNVERIFY.getId()); //纠错任务待审核
            int count = adJiucuoTaskService.selectCountByActivityAndSeat(searchMap);
            if(count > 0) {
            	result.setCode(ResultCode.RESULT_FAILURE.getCode());
                result.setResultDes("已有人正在执行该纠错任务！");
                model.addAttribute(SysConst.RESULT_KEY, result);
                return model;
            }
            
            InputStream is1 = null;
            String filename1 = null;

            try {
                file1 = file1.replaceAll("data:image/jpeg;base64,", "");
                is1 = new ByteArrayInputStream(org.apache.commons.codec.binary.Base64.decodeBase64(file1));
                filename1 = UploadFileUtil.saveFile(path, "image.jpg", is1);
                if (filename1 == null) {
                    result.setCode(ResultCode.RESULT_PARAM_ERROR.getCode());
                    result.setResultDes("上传出错！");
                    model.addAttribute(SysConst.RESULT_KEY, result);
                    return model;
                }

                AdJiucuoTask task = new AdJiucuoTask();
                AdJiucuoTaskFeedback feedback = new AdJiucuoTaskFeedback();
                task.setStatus(JiucuoTaskStatus.UNVERIFY.getId());
                task.setActivityId(seat.getActivityId());
                task.setAdSeatId(seat.getAdSeatId());
                task.setUserId(user.getId());
                task.setProblemStatus(TaskProblemStatus.PROBLEM.getId());
                feedback.setSeatLat(seatLat);
                feedback.setSeatLon(seatLon);
                feedback.setLat(lat);
                feedback.setLon(lon);
                int index = filename1.indexOf('.');
                MarkLogoUtil.markImageBySingleIcon(request.getSession().getServletContext().getRealPath("/")+"/static/images/jflogomin.png", path+filename1, path, filename1.substring(0, index), "jpg", null);      
                //feedback.setPicUrl1("/static/upload/" + filename1);
                feedback.setPicUrl1(file_upload_ip + servicePath + filename1);
                feedback.setProblem(problem);
                feedback.setProblemOther(other);
                
                try {
                    adJiucuoTaskService.feedback(task, feedback);
                } catch (Exception e) {
                	logger.error(e);
                    result.setCode(ResultCode.RESULT_PARAM_ERROR.getCode());
                    result.setResultDes("保存出错！");
                    model.addAttribute(SysConst.RESULT_KEY, result);
                    return model;
                }
            } catch (Exception e) {
            	logger.error(e);
                result.setCode(ResultCode.RESULT_PARAM_ERROR.getCode());
                result.setResultDes("上传出错！");
                model.addAttribute(SysConst.RESULT_KEY, result);
                return model;
            }
        } else {
            result.setCode(ResultCode.RESULT_PARAM_ERROR.getCode());
            result.setResultDes("参数错误！");
            model.addAttribute(SysConst.RESULT_KEY, result);
            return model;
        }

        model.addAttribute(SysConst.RESULT_KEY, result);
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("origin"));
        response.setHeader("Access-Control-Allow-Credentials", "true");
        return model;
    }

    //请求资讯列表
    @RequestMapping(value = "/activity")
    @ResponseBody
    public Model activiList(Model model, HttpServletRequest request, HttpServletResponse response) {
        ResultVo result = new ResultVo();
        result.setCode(ResultCode.RESULT_SUCCESS.getCode());
        result.setResultDes("获取成功");
        model = new ExtendedModelMap();

        String token = null;
        
        try {
            InputStream is = request.getInputStream();
            Gson gson = new Gson();
            JsonObject obj = gson.fromJson(new InputStreamReader(is), JsonObject.class);
            token = obj.get("token") == null ? null : obj.get("token").getAsString();
            if (token != null) {
                useSession.set(Boolean.FALSE);
                this.sessionByRedis.setToken(token);
            } else {
                useSession.set(Boolean.TRUE);
            }
        } catch (IOException e) {
        	logger.error(e);
            result.setCode(ResultCode.RESULT_FAILURE.getCode());
            result.setResultDes("系统繁忙，请稍后再试！");
            model.addAttribute(SysConst.RESULT_KEY, result);
            return model;
        }

        //验证登录
        if (useSession.get()) {
            if (!checkLogin(model, result, request)) {
                return model;
            }
        } else {
            if (!checkLogin(model, result, token)) {
                return model;
            }
        }

        SysUserExecute user = getLoginUser(request, token);
        saveLog(request, user.getUsername());
        List<ActivityMobileReportVo> list = adActivityService.getMobileReport(user);
        List<ActivityReportVo> apiVoList = Lists.newArrayList();
        for (ActivityMobileReportVo vo : list) {
            apiVoList.add(new ActivityReportVo(vo));
        }
        result.setResult(apiVoList);

        model.addAttribute(SysConst.RESULT_KEY, result);
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("origin"));
        response.setHeader("Access-Control-Allow-Credentials", "true");
        return model;
    }

    //请求奖励列表
    @RequestMapping(value = "/getreward")
    @ResponseBody
    public Model getreward(Model model, HttpServletRequest request, HttpServletResponse response) {
        ResultVo result = new ResultVo();
        result.setCode(ResultCode.RESULT_SUCCESS.getCode());
        result.setResultDes("获取成功");
        model = new ExtendedModelMap();

        String token = null;
        Integer page = 1;
        Integer pageSize = 10;
        
        try {
            InputStream is = request.getInputStream();
            Gson gson = new Gson();
            JsonObject obj = gson.fromJson(new InputStreamReader(is), JsonObject.class);
            token = obj.get("token") == null ? null : obj.get("token").getAsString();
            if (token != null) {
                useSession.set(Boolean.FALSE);
                this.sessionByRedis.setToken(token);
            } else {
                useSession.set(Boolean.TRUE);
            }
            if(obj.get("page") != null){
                page = obj.get("page").getAsInt();
            }
            if(obj.get("page_size") != null){
                pageSize = obj.get("page_size").getAsInt();
            }
        } catch (IOException e) {
        	logger.error(e);
            result.setCode(ResultCode.RESULT_FAILURE.getCode());
            result.setResultDes("系统繁忙，请稍后再试！");
            model.addAttribute(SysConst.RESULT_KEY, result);
            return model;
        }

        //验证登录
        if (useSession.get()) {
            if (!checkLogin(model, result, request)) {
                return model;
            }
        } else {
            if (!checkLogin(model, result, token)) {
                return model;
            }
        }

        SysUserExecute user = getLoginUser(request, token);
        saveLog(request, user.getUsername());
        List<AdMonitorReward> rewards = adMonitorRewardService.getByUserId(user.getId());
        RewardResultVo resultVo = new RewardResultVo();
        resultVo.setTotal_reward(adMonitorRewardService.getTotalRewardByUserId(user.getId()));
        for (AdMonitorReward reward : rewards) {
            resultVo.getDetail().add(new RewardVo(reward));
        }
        result.setResult(resultVo);

        model.addAttribute(SysConst.RESULT_KEY, result);
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("origin"));
        response.setHeader("Access-Control-Allow-Credentials", "true");
        return model;
    }

    //请求省列表
    @RequestMapping(value = "/getProvince")
    @ResponseBody
    public Model getProvince(Model model, HttpServletRequest request, HttpServletResponse response) {
    	saveLog(request, "");
        ResultVo result = new ResultVo();
        result.setCode(ResultCode.RESULT_SUCCESS.getCode());
        result.setResultDes("获取成功");
        model = new ExtendedModelMap();

        result.setResult(cityCache.getAllProvince());

        model.addAttribute(SysConst.RESULT_KEY, result);
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("origin"));
        response.setHeader("Access-Control-Allow-Credentials", "true");
        return model;
    }

    //请求城市列表，参数为省id
    @RequestMapping(value = "/getCity")
    @ResponseBody
    public Model getCity(Model model, HttpServletRequest request, HttpServletResponse response) {
    	saveLog(request, "");
        ResultVo result = new ResultVo();
        result.setCode(ResultCode.RESULT_SUCCESS.getCode());
        result.setResultDes("获取成功");
        model = new ExtendedModelMap();

        Long provinceId = null;

        try {
            InputStream is = request.getInputStream();
            Gson gson = new Gson();
            JsonObject obj = gson.fromJson(new InputStreamReader(is), JsonObject.class);
            provinceId = obj.get("provinceId") == null ? null : obj.get("provinceId").getAsLong();
        } catch (IOException e) {
        	logger.error(e);
            result.setCode(ResultCode.RESULT_FAILURE.getCode());
            result.setResultDes("系统繁忙，请稍后再试！");
            model.addAttribute(SysConst.RESULT_KEY, result);
            return model;
        }

        if(provinceId!=null){
            List<City> citys = cityCache.getCity(provinceId);
            if(citys.size()<1){
                City city = new City();
                city.setId(provinceId);
                city.setName(cityCache.getCityName(provinceId));
                citys.add(city);
            }
            result.setResult(citys);
        }else{
            result.setResult(Lists.newArrayList());
        }

        model.addAttribute(SysConst.RESULT_KEY, result);
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("origin"));
        response.setHeader("Access-Control-Allow-Credentials", "true");
        return model;
    }

    //请求省的区县列表，参数为省id，适用于直辖市
    @RequestMapping(value = "/getProvinceRegion")
    @ResponseBody
    public Model getProvinceRegion(Model model, HttpServletRequest request, HttpServletResponse response) {
    	saveLog(request, "");
        ResultVo result = new ResultVo();
        result.setCode(ResultCode.RESULT_SUCCESS.getCode());
        result.setResultDes("获取成功");
        model = new ExtendedModelMap();

        Long provinceId = null;

        try {
            InputStream is = request.getInputStream();
            Gson gson = new Gson();
            JsonObject obj = gson.fromJson(new InputStreamReader(is), JsonObject.class);
            provinceId = obj.get("provinceId") == null ? null : obj.get("provinceId").getAsLong();
        } catch (IOException e) {
        	logger.error(e);
            result.setCode(ResultCode.RESULT_FAILURE.getCode());
            result.setResultDes("系统繁忙，请稍后再试！");
            model.addAttribute(SysConst.RESULT_KEY, result);
            return model;
        }

        result.setResult(provinceId==null?Lists.newArrayList():cityCache.getRegionByProvince(provinceId));

        model.addAttribute(SysConst.RESULT_KEY, result);
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("origin"));
        response.setHeader("Access-Control-Allow-Credentials", "true");
        return model;
    }

    //请求市的区县列表，参数为市id
    @RequestMapping(value = "/getRegion")
    @ResponseBody
    public Model getRegion(Model model, HttpServletRequest request, HttpServletResponse response) {
    	saveLog(request, "");
        ResultVo result = new ResultVo();
        result.setCode(ResultCode.RESULT_SUCCESS.getCode());
        result.setResultDes("获取成功");
        model = new ExtendedModelMap();

        Long cityId = null;

        try {
            InputStream is = request.getInputStream();
            Gson gson = new Gson();
            JsonObject obj = gson.fromJson(new InputStreamReader(is), JsonObject.class);
            cityId = obj.get("cityId") == null ? null : obj.get("cityId").getAsLong();
        } catch (IOException e) {
        	logger.error(e);
            result.setCode(ResultCode.RESULT_FAILURE.getCode());
            result.setResultDes("系统繁忙，请稍后再试！");
            model.addAttribute(SysConst.RESULT_KEY, result);
            return model;
        }

        if(CityUtil.isProvince(cityId)) {
            result.setResult(cityCache.getRegionByProvince(cityId));
        }else{
            result.setResult(cityId == null ? Lists.newArrayList() : cityCache.getRegion(cityId));
        }

        model.addAttribute(SysConst.RESULT_KEY, result);
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("origin"));
        response.setHeader("Access-Control-Allow-Credentials", "true");
        return model;
    }

    //请求区县的街道列表，参数为区县id
    @RequestMapping(value = "/getStreet")
    @ResponseBody
    public Model getStreet(Model model, HttpServletRequest request, HttpServletResponse response) {
    	saveLog(request, "");
        ResultVo result = new ResultVo();
        result.setCode(ResultCode.RESULT_SUCCESS.getCode());
        result.setResultDes("获取成功");
        model = new ExtendedModelMap();

        Long regionId = null;

        try {
            InputStream is = request.getInputStream();
            Gson gson = new Gson();
            JsonObject obj = gson.fromJson(new InputStreamReader(is), JsonObject.class);
            regionId = obj.get("regionId") == null ? null : obj.get("regionId").getAsLong();
        } catch (IOException e) {
        	logger.error(e);
            result.setCode(ResultCode.RESULT_FAILURE.getCode());
            result.setResultDes("系统繁忙，请稍后再试！");
            model.addAttribute(SysConst.RESULT_KEY, result);
            return model;
        }

        result.setResult(regionId==null?Lists.newArrayList():cityCache.getStreet(regionId));

        model.addAttribute(SysConst.RESULT_KEY, result);
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("origin"));
        response.setHeader("Access-Control-Allow-Credentials", "true");
        return model;
    }

    //请求当前媒体工作人员的街道的广告位列表
    @RequestMapping(value = "/getAdSeatList")
    @ResponseBody
    public Model getAdSeatList(Model model, HttpServletRequest request, HttpServletResponse response) {
        ResultVo result = new ResultVo();
        result.setCode(ResultCode.RESULT_SUCCESS.getCode());
        result.setResultDes("获取成功");
        model = new ExtendedModelMap();

        String token = null;
        Long street=null;
        Integer page = 1;
        Integer pageSize = 10;
        
        try {
            InputStream is = request.getInputStream();
            Gson gson = new Gson();
            JsonObject obj = gson.fromJson(new InputStreamReader(is), JsonObject.class);
            token = obj.get("token") == null ? null : obj.get("token").getAsString();
            street = obj.get("street") == null ? null : obj.get("street").getAsLong();
            if (token != null) {
                useSession.set(Boolean.FALSE);
                this.sessionByRedis.setToken(token);
            } else {
                useSession.set(Boolean.TRUE);
            }
            if(obj.get("page") != null){
                page = obj.get("page").getAsInt();
            }
            if(obj.get("page_size") != null){
                pageSize = obj.get("page_size").getAsInt();
            }
        } catch (IOException e) {
        	logger.error(e);
            result.setCode(ResultCode.RESULT_FAILURE.getCode());
            result.setResultDes("系统繁忙，请稍后再试！");
            model.addAttribute(SysConst.RESULT_KEY, result);
            return model;
        }

        //验证登录
        if (useSession.get()) {
            if (!checkLogin(model, result, request)) {
                return model;
            }
        } else {
            if (!checkLogin(model, result, token)) {
                return model;
            }
        }

        SysUserExecute user = getLoginUser(request, token);
        saveLog(request, user.getUsername());
        if(user.getUsertype()==AppUserTypeEnum.MEDIA.getId()){
            result.setResult(adSeatService.getByStreetAndMediaUserId(street,user.getOperateId()));
        }else{
            result.setResult(Lists.newArrayList());
        }

        model.addAttribute(SysConst.RESULT_KEY, result);
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("origin"));
        response.setHeader("Access-Control-Allow-Credentials", "true");
        return model;
    }

    //绑定广告位二维码时检验
    @RequestMapping(value = "/checkAdSeatCode")
    @ResponseBody
    public Model checkAdSeatCode(Model model, HttpServletRequest request, HttpServletResponse response) {
        ResultVo result = new ResultVo();
        result.setCode(ResultCode.RESULT_SUCCESS.getCode());
        result.setResultDes("检查成功");
        model = new ExtendedModelMap();

        String token = null;
        String adSeatCode=null;

        try {
            InputStream is = request.getInputStream();
            Gson gson = new Gson();
            JsonObject obj = gson.fromJson(new InputStreamReader(is), JsonObject.class);
            token = obj.get("token") == null ? null : obj.get("token").getAsString();
            adSeatCode = obj.get("adSeatCode") == null ? null : obj.get("adSeatCode").getAsString();
            if (token != null) {
                useSession.set(Boolean.FALSE);
                this.sessionByRedis.setToken(token);
            } else {
                useSession.set(Boolean.TRUE);
            }
        } catch (IOException e) {
        	logger.error(e);
            result.setCode(ResultCode.RESULT_FAILURE.getCode());
            result.setResultDes("系统繁忙，请稍后再试！");
            model.addAttribute(SysConst.RESULT_KEY, result);
            return model;
        }

        if(adSeatCode==null){
            result.setCode(ResultCode.RESULT_FAILURE.getCode());
            result.setResultDes("参数有误！");
            model.addAttribute(SysConst.RESULT_KEY, result);
            return model;
        }

        //验证登录
        if (useSession.get()) {
            if (!checkLogin(model, result, request)) {
                return model;
            }
        } else {
            if (!checkLogin(model, result, token)) {
                return model;
            }
        }

        SysUserExecute user = getLoginUser(request, token);
        saveLog(request, user.getUsername());
        SysUserVo operate = sysUserService.findUserinfoById(user.getOperateId());
        AdSeatCodeCheckInfo checkInfo = new AdSeatCodeCheckInfo();

        if(!adSeatCode.startsWith(operate.getPrefix())){
            checkInfo.setValid(false);
            checkInfo.setErr_msg("该广告位二维码有误或媒体不匹配！");
        }else{
            if(adSeatService.getCountByAdCode(adSeatCode)>0){
                checkInfo.setValid(false);
                checkInfo.setErr_msg("该广告位已激活！");
            }
        }

        result.setResult(checkInfo);

        model.addAttribute(SysConst.RESULT_KEY, result);
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("origin"));
        response.setHeader("Access-Control-Allow-Credentials", "true");
        return model;
    }

    /**
     * APP一打开调用 检查版本号是否要更新
     */
    @RequestMapping(value = "/checkVersion")
    @ResponseBody
    public Model getLatestForceUpdateVersion(Model model, HttpServletRequest request, HttpServletResponse response) {
    	saveLog(request, "");
    	ResultVo result = new ResultVo();
        result.setCode(ResultCode.RESULT_SUCCESS.getCode());
        result.setResultDes("操作成功");
        model = new ExtendedModelMap();
        
        Integer needForceUpdate = AppUpdateTypeEnum.VERSION_ERROR.getId(); // 0: 有新版本,需要强制更新; 1: 有新版本,可去更新; 2: 最新版本; 3: 版本号有误
        String appVersion;
        
        // 查询最新的需要强制更新的版本号
        AdVersion version = adVersionCache.getVersion();
        // 查询最新的版本号
        AdVersion nowVersion = adVersionCache.getNowVersion();
        try {
            InputStream is = request.getInputStream();
            Gson gson = new Gson();
            JsonObject obj = gson.fromJson(new InputStreamReader(is), JsonObject.class);
            appVersion = obj.get("appVersion") == null ? null : obj.get("appVersion").getAsString();
            
            //【1】判断版本号比较, 提示是否需要强制更新
            if(version != null) {
            	String[] versionSplit = version.getAppVersion().split("\\.");
            	String[] appVersionSplit = appVersion.split("\\.");
            	for (int i = 0; i < appVersionSplit.length; i++) {
        			Integer versionInt = Integer.parseInt(versionSplit[i]);
        			Integer appVersionInt = Integer.parseInt(appVersionSplit[i]);
        			if(appVersionInt < versionInt) {
        				//需要强制更新
        				needForceUpdate = AppUpdateTypeEnum.FORCE_UPDATE.getId();
        				model.addAttribute("apkUrl", version.getApkUrl());
                    	model.addAttribute("iosUrl", version.getIosUrl());
        				break;
        			} else if(appVersionInt > versionInt) {
        				//不需要强制更新
        				needForceUpdate = AppUpdateTypeEnum.CAN_UPDATE.getId();
        				break;
        			}
        		}
            	result.setCode(ResultCode.RESULT_SUCCESS.getCode());
            }
            
            //【2】判断版本号比较, 提示是否更新
            if(nowVersion == null) {
            	result.setCode(ResultCode.RESULT_SUCCESS.getCode());
                result.setResultDes("版本号有误！");
                needForceUpdate = AppUpdateTypeEnum.VERSION_ERROR.getId();
                result.setResult(needForceUpdate);
                model.addAttribute(SysConst.RESULT_KEY, result);
                return model;
            } else {
            	if(needForceUpdate != AppUpdateTypeEnum.FORCE_UPDATE.getId()) {
            		//不需要强制更新
            		String[] versionSplit = nowVersion.getAppVersion().split("\\.");
                	String[] appVersionSplit = appVersion.split("\\.");
                	for (int i = 0; i < appVersionSplit.length; i++) {
            			Integer versionInt = Integer.parseInt(versionSplit[i]);
            			Integer appVersionInt = Integer.parseInt(appVersionSplit[i]);
            			if(appVersionInt < versionInt) {
            				//有新版本,可去更新
            				needForceUpdate = AppUpdateTypeEnum.CAN_UPDATE.getId();
            				model.addAttribute("apkUrl", nowVersion.getApkUrl());
                        	model.addAttribute("iosUrl", nowVersion.getIosUrl());
            				break;
            			} else if(appVersionInt == versionInt) {
            				needForceUpdate = AppUpdateTypeEnum.LATEEST_VERSION.getId();
            			}
            		}
            	}
            	result.setCode(ResultCode.RESULT_SUCCESS.getCode());
			}
            
            if(needForceUpdate == AppUpdateTypeEnum.LATEEST_VERSION.getId()) {
        		result.setResultDes("最新版本");
        	} else if(needForceUpdate == AppUpdateTypeEnum.FORCE_UPDATE.getId()) {
        		result.setResultDes("有新版本，需要强制更新！新版本号：" + version.getAppVersion());
        	} else if(needForceUpdate == AppUpdateTypeEnum.CAN_UPDATE.getId()) {
        		result.setResultDes("有新版本，可以更新！新版本号：" + version.getAppVersion());
        	} else {
        		result.setResultDes("最新版本！");
        	}
            result.setResult(needForceUpdate);
        } catch (IOException e) {
        	logger.error(e);
            result.setCode(ResultCode.RESULT_FAILURE.getCode());
            result.setResultDes("系统繁忙，请稍后再试！");
            model.addAttribute(SysConst.RESULT_KEY, result);
            return model;
        }
        
        model.addAttribute(SysConst.RESULT_KEY, result);
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("origin"));
        response.setHeader("Access-Control-Allow-Credentials", "true");
        return model;
    }

    //获取附近有活动的广告位
    @RequestMapping(value = "/getAdSeatAround")
    @ResponseBody
    public Model getAdSeatAround(Model model, HttpServletRequest request, HttpServletResponse response) {
        ResultVo result = new ResultVo();
        result.setCode(ResultCode.RESULT_SUCCESS.getCode());
        result.setResultDes("检查成功");
        model = new ExtendedModelMap();

        String token = null;
        Double lon = null;
        Double lat = null;
        Double metre = null;
        Integer page = 1;
        Integer pageSize = 10;
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                   
        try {
            InputStream is = request.getInputStream();
            Gson gson = new Gson();
            JsonObject obj = gson.fromJson(new InputStreamReader(is), JsonObject.class);
            token = obj.get("token") == null ? null : obj.get("token").getAsString();
            lon = obj.get("lon") == null ? null : obj.get("lon").getAsDouble();
            lat = obj.get("lat") == null ? null : obj.get("lat").getAsDouble();
            metre = obj.get("metre") == null ? null : obj.get("metre").getAsDouble();
            if (token != null) {
                useSession.set(Boolean.FALSE);
                this.sessionByRedis.setToken(token);
            } else {
                useSession.set(Boolean.TRUE);
            }
            if(obj.get("page") != null){
                page = obj.get("page").getAsInt();
            }
            if(obj.get("page_size") != null){
                pageSize = obj.get("page_size").getAsInt();
            }
        } catch (IOException e) {
        	logger.error(e);
            result.setCode(ResultCode.RESULT_FAILURE.getCode());
            result.setResultDes("系统繁忙，请稍后再试！");
            model.addAttribute(SysConst.RESULT_KEY, result);
            return model;
        }

        if(lon==null||lat==null||metre==null){
            result.setCode(ResultCode.RESULT_FAILURE.getCode());
            result.setResultDes("参数有误！");
            model.addAttribute(SysConst.RESULT_KEY, result);
            return model;
        }

        //验证登录
        if (useSession.get()) {
            if (!checkLogin(model, result, request)) {
                return model;
            }
        } else {
            if (!checkLogin(model, result, token)) {
                return model;
            }
        }
        SysUserExecute user = getLoginUser(request, token);
        saveLog(request, user.getUsername());
        SearchDataVo vo = new SearchDataVo(null,null,(page-1)*pageSize,pageSize);
        vo.putSearchParam("lat",null,lat);
        vo.putSearchParam("lon",null,lon);
        vo.putSearchParam("metre",null,metre);
        adSeatService.getPageData(vo);
//        List<AdSeatInfo> seats = adSeatService.getAdseatAround(lat,lon,metre);
        List<?> list = vo.getList();
        List<AdSeatInfo> seats = Lists.newArrayList();
        
        for(Object obj : list) {
        	AdSeatInfo adSeatInfo = (AdSeatInfo) obj;
        	adSeatInfo.setCreateTimeStr(DateUtil.dateFormate(adSeatInfo.getCreateTime(), "MM-dd HH:mm"));
        	adSeatInfo.setUpdateTimeStr(DateUtil.dateFormate(adSeatInfo.getUpdateTime(), "MM-dd HH:mm"));
        	seats.add(adSeatInfo);
        }
        result.setResult(seats);

        model.addAttribute(SysConst.RESULT_KEY, result);
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("origin"));
        response.setHeader("Access-Control-Allow-Credentials", "true");
        return model;
    }

    //获取附近任务
    @RequestMapping(value = "/getAdMonitorTaskAround")
    @ResponseBody
    public Model getAdMonitorTaskAround(Model model, HttpServletRequest request, HttpServletResponse response) {
        ResultVo result = new ResultVo();
        result.setCode(ResultCode.RESULT_SUCCESS.getCode());
        result.setResultDes("查询成功");
        model = new ExtendedModelMap();

        String token = null;
        Double lon = null;
        Double lat = null;
        Double metre = null;
        Integer page = 1;
        Integer pageSize = 10;
        String updateTime = null;
        
        try {
            InputStream is = request.getInputStream();
            Gson gson = new Gson();
            JsonObject obj = gson.fromJson(new InputStreamReader(is), JsonObject.class);
            token = obj.get("token") == null ? null : obj.get("token").getAsString();
            lon = obj.get("lon") == null ? null : obj.get("lon").getAsDouble();
            lat = obj.get("lat") == null ? null : obj.get("lat").getAsDouble();
            metre = obj.get("metre") == null ? null : obj.get("metre").getAsDouble();
            updateTime = obj.get("updateTime") == null ? null : obj.get("updateTime").getAsString();
            if(obj.get("page") != null){
                page = obj.get("page").getAsInt();
            }
            if(obj.get("page_size") != null){
                pageSize = obj.get("page_size").getAsInt();
            }
            if (token != null) {
                useSession.set(Boolean.FALSE);
                this.sessionByRedis.setToken(token);
            } else {
                useSession.set(Boolean.TRUE);
            }
        } catch (IOException e) {
        	logger.error(e);
            result.setCode(ResultCode.RESULT_FAILURE.getCode());
            result.setResultDes("系统繁忙，请稍后再试！");
            model.addAttribute(SysConst.RESULT_KEY, result);
            return model;
        }

        if(lon==null||lat==null||metre==null){
            result.setCode(ResultCode.RESULT_FAILURE.getCode());
            result.setResultDes("参数有误！");
            model.addAttribute(SysConst.RESULT_KEY, result);
            return model;
        }

        //验证登录
        if (useSession.get()) {
            if (!checkLogin(model, result, request)) {
                return model;
            }
        } else {
            if (!checkLogin(model, result, token)) {
                return model;
            }
        }
        List<MonitorTaskArroundVo> list = Lists.newArrayList();
        SearchDataVo vo = new SearchDataVo(null,null,(page-1)*pageSize,pageSize);
        vo.putSearchParam("lon",null,lon);
        vo.putSearchParam("lat",null,lat);
        vo.putSearchParam("metre",null,metre);
        vo.putSearchParam("metreDegree",null, GeoUtil.getDegreeFromDistance(metre));
        vo.putSearchParam("updateTime", null, updateTime);
        vo.putSearchParam("nowDate", null, new Date());
        
        //获取登录用户, 判断用户类型
        SysUserExecute user = getLoginUser(request, token);
        if(user == null) {
        	result.setCode(ResultCode.RESULT_FAILURE.getCode());
            result.setResultDes("系统繁忙，请稍后再试！");
            model.addAttribute(SysConst.RESULT_KEY, result);
            return model;
        }
        saveLog(request, user.getUsername());
        Integer status = MonitorTaskStatus.CAN_GRAB.getId();
        if(user.getUsertype().equals(AppUserTypeEnum.SOCIAL.getId())) {
        	//[1] 社会人员抢单
        	status = MonitorTaskStatus.CAN_GRAB.getId(); //8：可抢单
        	vo.putSearchParam("status", null, status);
        }  else {
        	if(user.getUsertype().equals(AppUserTypeEnum.MEDIA.getId())) {
            	//[2] 媒体公司员工抢单  
        		AdMedia media = mediaService.getMediaByUserId(user.getOperateId());
        		vo.putSearchParam("mediaId", null, media.getId());
            }

        	//[3] 媒体公司/第三方监测公司人员抢单（只抢属于自己公司下的任务）
        	vo.putSearchParam("companyId", null, user.getOperateId());
        	
        	status = MonitorTaskStatus.UNASSIGN.getId(); //1：待指派
        	vo.putSearchParam("status", null, status);
        }
        adMonitorTaskService.getByPointAroundPageData(vo);
        
        for(Object obj : vo.getList()){
        	AdMonitorTaskMobileVo task = (AdMonitorTaskMobileVo) obj;
        	MonitorTaskArroundVo arroundVo = new MonitorTaskArroundVo(task);
        	arroundVo.setProvince(cityCache.getCityName(task.getProvince()));
        	arroundVo.setCity(cityCache.getCityName(task.getCity()));
//        	arroundVo.setRegion(cityCache.getCityName(task.getRegion()));
//        	arroundVo.setStreet(cityCache.getCityName(task.getStreet()));
        	arroundVo.setStartTime(DateUtil.dateFormate(task.getMonitorDate(), "yyyy-MM-dd"));
        	Long timestamp = task.getMonitorDate().getTime() + (task.getMonitorLastDays() - 1)*24*60*60*1000;
        	arroundVo.setEndTime(DateUtil.dateFormate(new Date(timestamp), "yyyy-MM-dd"));
        	arroundVo.setTask_point(task.getTaskPoint());
        	arroundVo.setTask_money(task.getTaskMoney());
        	arroundVo.setQualifiedPicUrl(task.getQualifiedPicUrl());
        	arroundVo.setNoQualifiedPicUrl1(task.getNoQualifiedPicUrl1());
        	arroundVo.setNoQualifiedPicUrl2(task.getNoQualifiedPicUrl2());
        	arroundVo.setNoQualifiedPicUrl3(task.getNoQualifiedPicUrl2());
        	arroundVo.setNoQualifiedText1(task.getNoQualifiedText1());
        	arroundVo.setNoQualifiedText2(task.getNoQualifiedText2());
        	arroundVo.setNoQualifiedText3(task.getNoQualifiedText3());
        	arroundVo.setNotification(task.getNotification());
        	list.add(arroundVo);
        }

        result.setResult(list);

        //设置总页数
        int totalCount = (int) ((vo.getCount() + pageSize - 1) / pageSize);

        model.addAttribute("totalCount", totalCount);
        model.addAttribute(SysConst.RESULT_KEY, result);
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("origin"));
        response.setHeader("Access-Control-Allow-Credentials", "true");
        return model;
    }

    //获取本市任务
    @RequestMapping(value = "/getAdMonitorTaskCurCity")
    @ResponseBody
    public Model getAdMonitorTaskCurCity(Model model, HttpServletRequest request, HttpServletResponse response) {
        ResultVo result = new ResultVo();
        result.setCode(ResultCode.RESULT_SUCCESS.getCode());
        result.setResultDes("查询成功");
        model = new ExtendedModelMap();

        String token = null;
        Double lon = null;
        Double lat = null;
        Double metre = null;
        Integer page = 1;
        Integer pageSize = 20;
        String province = null;
        String city = null;
        String updateTime = null;
        try {
            InputStream is = request.getInputStream();
            Gson gson = new Gson();
            JsonObject obj = gson.fromJson(new InputStreamReader(is), JsonObject.class);
            token = obj.get("token") == null ? null : obj.get("token").getAsString();
            lon = obj.get("lon") == null ? null : obj.get("lon").getAsDouble();
            lat = obj.get("lat") == null ? null : obj.get("lat").getAsDouble();
            metre = obj.get("metre") == null ? null : obj.get("metre").getAsDouble();
            province = obj.get("province") == null ? null : obj.get("province").getAsString();
            city = obj.get("city") == null ? null : obj.get("city").getAsString();
            updateTime = obj.get("updateTime") == null ? null : obj.get("updateTime").getAsString();
            if(obj.get("page") != null){
                page = obj.get("page").getAsInt();
            }
            if(obj.get("page_size") != null){
                pageSize = obj.get("page_size").getAsInt();
            }
            if (token != null) {
                useSession.set(Boolean.FALSE);
                this.sessionByRedis.setToken(token);
            } else {
                useSession.set(Boolean.TRUE);
            }
        } catch (IOException e) {
        	logger.error(e);
            result.setCode(ResultCode.RESULT_FAILURE.getCode());
            result.setResultDes("系统繁忙，请稍后再试！");
            model.addAttribute(SysConst.RESULT_KEY, result);
            return model;
        }

        Long provinceId = null;
        Long cityId = null;


        for(City prov : cityCache.getAllProvince()){
            if(prov.getName().contains(province)){
                provinceId = prov.getId();
            }
        }
        if(provinceId!=null) {
            for (City vcity : cityCache.getCity(provinceId)) {
                if (vcity.getName().contains(city)) {
                    cityId = vcity.getId();
                }
            }
        }

        if(lon==null||lat==null||province==null){
            result.setCode(ResultCode.RESULT_FAILURE.getCode());
            result.setResultDes("参数有误！");
            model.addAttribute(SysConst.RESULT_KEY, result);
            return model;
        }

        //验证登录
        if (useSession.get()) {
            if (!checkLogin(model, result, request)) {
                return model;
            }
        } else {
            if (!checkLogin(model, result, token)) {
                return model;
            }
        }
        
        SearchDataVo vo = new SearchDataVo(null,null,(page-1)*pageSize,pageSize);
        vo.putSearchParam("lon",null,lon);
        vo.putSearchParam("lat",null,lat);
        vo.putSearchParam("province",null,provinceId);
        vo.putSearchParam("city",null,cityId);
        vo.putSearchParam("updateTime", null, updateTime);
        vo.putSearchParam("nowDate", null, new Date());
        
        //获取登录用户, 判断用户类型
        SysUserExecute user = getLoginUser(request, token);
        if(user == null) {
        	result.setCode(ResultCode.RESULT_FAILURE.getCode());
            result.setResultDes("系统繁忙，请稍后再试！");
            model.addAttribute(SysConst.RESULT_KEY, result);
            return model;
        }
        saveLog(request, user.getUsername());
        
        Integer status = null;
        if(user.getUsertype().equals(AppUserTypeEnum.SOCIAL.getId())) {
        	//[1] 社会人员抢单
        	status = MonitorTaskStatus.CAN_GRAB.getId(); //8：可抢单
        	vo.putSearchParam("status", null, status);
        } else if(user.getUsertype().equals(AppUserTypeEnum.MEDIA.getId())) {
        	//[2] 媒体监测人员抢单(自己媒体公司下的任务)
        	Integer mediaSysUserId = user.getOperateId();
        	AdMedia adMedia = mediaService.getMediaByUserId(mediaSysUserId);
        	vo.putSearchParam("mediaId", null, adMedia.getId());
        	
        	status = MonitorTaskStatus.UNASSIGN.getId(); //1：待指派
        	vo.putSearchParam("status", null, status);
        } else if(user.getUsertype().equals(AppUserTypeEnum.THIRD_COMPANY.getId())) {
        	//[3] 第三方监测公司人员抢单（只抢属于自己公司下的任务）
        	Integer companyId = user.getOperateId();
        	vo.putSearchParam("companyId", null, companyId);
        	
        	status = MonitorTaskStatus.UNASSIGN.getId(); //1：待指派
        	vo.putSearchParam("status", null, status);
        }
        
        adMonitorTaskService.getByCurCityPageData(vo);
        List<MonitorTaskArroundVo> list = Lists.newArrayList();

        for(Object obj : vo.getList()){
        	AdMonitorTaskMobileVo task = (AdMonitorTaskMobileVo) obj;
        	MonitorTaskArroundVo arroundVo = new MonitorTaskArroundVo(task);
        	arroundVo.setProvince(cityCache.getCityName(task.getProvince()));
        	arroundVo.setCity(cityCache.getCityName(task.getCity()));
        	arroundVo.setRegion(cityCache.getCityName(task.getRegion()));
        	arroundVo.setStreet(cityCache.getCityName(task.getStreet()));
        	arroundVo.setStartTime(DateUtil.dateFormate(task.getMonitorDate(), "yyyy-MM-dd"));
        	Long timestamp = task.getMonitorDate().getTime() + (task.getMonitorLastDays() - 1)*24*60*60*1000;
        	arroundVo.setEndTime(DateUtil.dateFormate(new Date(timestamp), "yyyy-MM-dd"));
        	arroundVo.setTask_point(task.getTaskPoint());
        	arroundVo.setTask_money(task.getTaskMoney());
         	arroundVo.setQualifiedPicUrl(task.getQualifiedPicUrl());
        	arroundVo.setNoQualifiedPicUrl1(task.getNoQualifiedPicUrl1());
        	arroundVo.setNoQualifiedPicUrl2(task.getNoQualifiedPicUrl2());
        	arroundVo.setNoQualifiedPicUrl3(task.getNoQualifiedPicUrl2());
        	arroundVo.setNoQualifiedText1(task.getNoQualifiedText1());
        	arroundVo.setNoQualifiedText2(task.getNoQualifiedText2());
        	arroundVo.setNoQualifiedText3(task.getNoQualifiedText3());
        	arroundVo.setNotification(task.getNotification());
        	list.add(arroundVo);
        }

        result.setResult(list);
        //设置总页数
        int totalCount = (int) ((vo.getCount() + pageSize - 1) / pageSize);

        model.addAttribute("totalCount", totalCount);
        model.addAttribute(SysConst.RESULT_KEY, result);
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("origin"));
        response.setHeader("Access-Control-Allow-Credentials", "true");
        return model;
    }

    //发送短信验证码, 需要先校验图片验证码
    @RequestMapping(value = "/getSMSCode")
    @ResponseBody
    public Model getSMSCode(Model model, HttpServletRequest request, HttpServletResponse response) {
    	saveLog(request, "");
        ResultVo result = new ResultVo();
        result.setCode(ResultCode.RESULT_SUCCESS.getCode());
        result.setResultDes("获取成功");
        model = new ExtendedModelMap();

        String mobile = null;
        String vcode = null;
        String picToken = null;
        
        try {
            InputStream is = request.getInputStream();
            Gson gson = new Gson();
            JsonObject obj = gson.fromJson(new InputStreamReader(is), JsonObject.class);
            mobile = obj.get("mobile") == null ? null : obj.get("mobile").getAsString();
            vcode = obj.get("vcode") == null ? null : obj.get("vcode").getAsString();
            picToken = obj.get("token") == null ? null : obj.get("token").getAsString();
            if (picToken != null) {
                useSession.set(Boolean.FALSE);
                this.sessionByRedis.setToken(picToken);
            } else {
                useSession.set(Boolean.TRUE);
            }
        } catch (IOException e) {
        	logger.error(e);
            result.setCode(ResultCode.RESULT_FAILURE.getCode());
            result.setResultDes("系统繁忙，请稍后再试！");
            model.addAttribute(SysConst.RESULT_KEY, result);
            return model;
        }
        
        // 图片验证码必须填写
        if (StringUtils.isEmpty(vcode)) {
            result.setCode(ResultCode.RESULT_FAILURE.getCode());
            result.setResultDes("请填写验证码！");
            model.addAttribute(SysConst.RESULT_KEY, result);
            return model;
        }
        
        String sessionCode = null;
        HttpSession session = request.getSession();
        if (useSession.get()) {
            sessionCode = session.getAttribute(SessionKey.SESSION_CODE.toString()) == null ? ""
                    : session.getAttribute(SessionKey.SESSION_CODE.toString()).toString();
        } else {
            sessionCode = sessionByRedis.getImageCode();
        }

        // 图片验证码有效验证
        if (!vcode.equalsIgnoreCase(sessionCode)) {
            result.setCode(ResultCode.RESULT_FAILURE.getCode());
            result.setResultDes("验证码错误！");
            model.addAttribute(SysConst.RESULT_KEY, result);
            return model;
        }

        // 验证手机号
        if(mobile==null||!Pattern.matches(MOBILE_NUMBER_REGEX,mobile)){
            result.setCode(ResultCode.RESULT_PARAM_ERROR.getCode());
            result.setResultDes("手机号码有误！");
            model.addAttribute(SysConst.RESULT_KEY, result);
            return model;
        }
        
        //生成随机数验证码和token
        String num = getNumber(6);
        String token = sessionByRedis.initToken();
        sessionByRedis.setImageCode(num);

        try {
        	//发送短信
            sendSmsService.sendSms(mobile, SMS_CHECKCODE_CONTENT_TEMPLATE.replaceAll("\\{\\{code\\}\\}",num));
        }catch (Exception e){
        	logger.error(e);
            result.setCode(ResultCode.RESULT_FAILURE.getCode());
            result.setResultDes("系统繁忙，请稍后再试！");
            model.addAttribute(SysConst.RESULT_KEY, result);
            return model;
        }
        SMSCheckCodeResultVo smsCheckCodeResultVo = new SMSCheckCodeResultVo();
        smsCheckCodeResultVo.setToken(token);
        result.setResult(smsCheckCodeResultVo);

        model.addAttribute(SysConst.RESULT_KEY, result);
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("origin"));
        response.setHeader("Access-Control-Allow-Credentials", "true");
        return model;
    }

    //验证短信验证码
    @RequestMapping(value = "/checkSMSCode", method = RequestMethod.POST)
    @ResponseBody
    public Model checkSMSCode(Model model, HttpServletRequest request, HttpServletResponse response) {
        ResultVo<SysUserExecuteVo> result = new ResultVo<>();
        result.setCode(ResultCode.RESULT_SUCCESS.getCode());
        result.setResultDes("检查成功");
        model = new ExtendedModelMap();
        String username = null;
        String vcode = null;
        String token = null;

        try {
            InputStream is = request.getInputStream();
            Gson gson = new Gson();
            JsonObject obj = gson.fromJson(new InputStreamReader(is), JsonObject.class);
            username = obj.get("username") == null ? null : obj.get("username").getAsString();
            vcode = obj.get("vcode") == null ? null : obj.get("vcode").getAsString();
            token = obj.get("token") == null ? null : obj.get("token").getAsString();
            if (token != null) {
                useSession.set(Boolean.FALSE);
                this.sessionByRedis.setToken(token);
            } else {
                useSession.set(Boolean.TRUE);
            }
        } catch (IOException e) {
        	logger.error(e);
            result.setCode(ResultCode.RESULT_FAILURE.getCode());
            result.setResultDes("系统繁忙，请稍后再试！");
            model.addAttribute(SysConst.RESULT_KEY, result);
            return model;
        }

        // 账户必须验证
        if (StringUtils.isEmpty(username)||!Pattern.matches(MOBILE_NUMBER_REGEX,username)) {
            result.setCode(ResultCode.RESULT_FAILURE.getCode());
            result.setResultDes("手机号有误！");
            model.addAttribute(SysConst.RESULT_KEY, result);
            return model;
        }
        SysUserExecute userExecute = sysUserExecuteService.getByUsername(username);
        if(userExecute==null){
            result.setCode(ResultCode.RESULT_FAILURE.getCode());
            result.setResultDes("该手机号未注册！");
            model.addAttribute(SysConst.RESULT_KEY, result);
            return model;
        }

        // 验证码必须验证
        if (StringUtils.isEmpty(vcode)) {
            result.setCode(ResultCode.RESULT_FAILURE.getCode());
            result.setResultDes("验证码为必填！");
            model.addAttribute(SysConst.RESULT_KEY, result);
            return model;
        }

        String sessionCode = null;
        HttpSession session = request.getSession();
        if (useSession.get()) {
            sessionCode = session.getAttribute(SessionKey.SESSION_CODE.toString()) == null ? ""
                    : session.getAttribute(SessionKey.SESSION_CODE.toString()).toString();
        } else {
            sessionCode = sessionByRedis.getImageCode();
        }

        // 验证码有效验证
        if (!vcode.equalsIgnoreCase(sessionCode)) {
            result.setCode(ResultCode.RESULT_FAILURE.getCode());
            result.setResultDes("验证码错误！");
            model.addAttribute(SysConst.RESULT_KEY, result);
            return model;
        }
        saveLog(request, userExecute.getUsername());
        model.addAttribute(SysConst.RESULT_KEY, result);
//        response.getHeaders().add("Access-Control-Allow-Credentials","true");
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("origin"));
        response.setHeader("Access-Control-Allow-Credentials", "true");
        return model;
    }

    //app端手机号注册
    @RequestMapping(value = "/registMobile", method = RequestMethod.POST)
    @ResponseBody
    public Model regist(Model model, HttpServletRequest request, HttpServletResponse response) {
        ResultVo<SysUserExecuteVo> result = new ResultVo<>();
        result.setCode(ResultCode.RESULT_SUCCESS.getCode());
        result.setResultDes("注册成功");
        model = new ExtendedModelMap();
        String username = null;
        String password = null;
        String vcode = null;
        String token = null;
        String mac = null;
        String inviteAcc = null;
        String deviceId = null;
        String systemVersion = null;
        String isJoin = null;
        Date now = new Date();
        try {
            InputStream is = request.getInputStream();
            Gson gson = new Gson();
            JsonObject obj = gson.fromJson(new InputStreamReader(is), JsonObject.class);
            username = obj.get("username") == null ? null : obj.get("username").getAsString();
            password = obj.get("password") == null ? null : obj.get("password").getAsString();
            vcode = obj.get("vcode") == null ? null : obj.get("vcode").getAsString();
            token = obj.get("token") == null ? null : obj.get("token").getAsString();
            mac = obj.get("mac") == null ? null : obj.get("mac").getAsString();
            inviteAcc = obj.get("inviteAcc") == null ? null : obj.get("inviteAcc").getAsString();
            deviceId = obj.get("deviceId") == null ? null : obj.get("deviceId").getAsString();
            systemVersion = obj.get("systemVersion") == null ? null : obj.get("systemVersion").getAsString();
            isJoin = obj.get("isJoin") == null ? null : obj.get("isJoin").getAsString();
            if (token != null) {
                useSession.set(Boolean.FALSE);
                this.sessionByRedis.setToken(token);
            } else {
                useSession.set(Boolean.TRUE);
            }
        } catch (IOException e) {
        	logger.error(e);
            result.setCode(ResultCode.RESULT_FAILURE.getCode());
            result.setResultDes("系统繁忙，请稍后再试！");
            model.addAttribute(SysConst.RESULT_KEY, result);
            return model;
        }

        // 账户必须验证
        if (StringUtils.isEmpty(username)||!Pattern.matches(MOBILE_NUMBER_REGEX,username)) {
            result.setCode(ResultCode.RESULT_FAILURE.getCode());
            result.setResultDes("手机号有误！");
            model.addAttribute(SysConst.RESULT_KEY, result);
            return model;
        }
        SysUserExecute userExecute = sysUserExecuteService.getByUsername(username);
        if(userExecute!=null){
            result.setCode(ResultCode.RESULT_FAILURE.getCode());
            result.setResultDes("该手机号已注册！");
            model.addAttribute(SysConst.RESULT_KEY, result);
            return model;
        }

        // 密码必须验证
        if (StringUtils.isEmpty(password)) {
            result.setCode(ResultCode.RESULT_FAILURE.getCode());
            result.setResultDes("密码为必填！");
            model.addAttribute(SysConst.RESULT_KEY, result);
            return model;
        }

        // 短信验证码必须填写
        if (StringUtils.isEmpty(vcode)) {
            result.setCode(ResultCode.RESULT_FAILURE.getCode());
            result.setResultDes("验证码为必填！");
            model.addAttribute(SysConst.RESULT_KEY, result);
            return model;
        }

        String sessionCode = null;
        HttpSession session = request.getSession();
        if (useSession.get()) {
            sessionCode = session.getAttribute(SessionKey.SESSION_CODE.toString()) == null ? ""
                    : session.getAttribute(SessionKey.SESSION_CODE.toString()).toString();
        } else {
            sessionCode = sessionByRedis.getImageCode();
        }

        // 短信验证码有效验证
        if (!vcode.equalsIgnoreCase(sessionCode)) {
            result.setCode(ResultCode.RESULT_FAILURE.getCode());
            result.setResultDes("验证码错误！");
            model.addAttribute(SysConst.RESULT_KEY, result);
            return model;
        }
        
        //邀请码验证(注册的人添加的积分是"正常注册"+"邀请码注册", 邀请方添加的积分是"邀请码注册")
        if(!StringUtils.isEmpty(inviteAcc)) {
        	//有邀请码的情况 
        	//[1] 输入的是手机号
         	SysUserExecute sysUserExecute = sysUserExecuteService.getMobile(inviteAcc);
         	//[2] 输入的是公司邮箱
         	SysUser sysUser = sysUserService.getUserName(inviteAcc);
        	if(sysUserExecute!=null) {
        	    //邀请码输入正确   ,获得邀请注册默认积分
        		AdPoint	adpoint = pointService.findPointValue(AdPointEnum.INVITE_REGIST.getId());
        		//正常注册，获得默认注册积分
        		AdPoint adpointreg = pointService.findPointValue(AdPointEnum.NORMAL_REGIST.getId());
        		//正常注册
            	String md5Pwd = new Md5Hash(password, username).toString();
                userExecute = new SysUserExecute();
                userExecute.setUsername(username);
                userExecute.setRealname(username);
                userExecute.setPassword(md5Pwd);
                userExecute.setUsertype(AppUserTypeEnum.SOCIAL.getId());
                userExecute.setStatus(1);
                userExecute.setMobile(username);
                userExecute.setMac(mac);
                userExecute.setCreateTime(now);
                userExecute.setUpdateTime(now);
                userExecute.setDeviceId(deviceId);
                userExecute.setSystemVersion(systemVersion);
                try{
                	sysUserExecuteService.add(userExecute);
            		SysUserExecute sysUserExe = sysUserExecuteService.getByUsername(username);
            		//注册人积分增加（正常注册+邀请码积分） 邀请方积分增加
            		userPointService.addByInvite(sysUserExe,adpoint,adpointreg,username,sysUserExecute);
                }catch (Exception e){
                	logger.error(e);
                    result.setCode(ResultCode.RESULT_FAILURE.getCode());
                    result.setResultDes("注册失败！");
                    model.addAttribute(SysConst.RESULT_KEY, result);
                    return model;
                }
        	} else if(isJoin.equals("true")) {
        		//正常注册，获得默认注册积分
        		AdPoint adpointreg = pointService.findPointValue(AdPointEnum.NORMAL_REGIST.getId());
        		//正常注册
            	String md5Pwd = new Md5Hash(password, username).toString();
                userExecute = new SysUserExecute();
                userExecute.setUsername(username);
                userExecute.setRealname(username);
                userExecute.setPassword(md5Pwd);
                userExecute.setUsertype(AppUserTypeEnum.THIRD_COMPANY.getId());
                userExecute.setStatus(1);
                userExecute.setMobile(username);
                userExecute.setMac(mac);
                userExecute.setCreateTime(now);
                userExecute.setUpdateTime(now);
                userExecute.setDeviceId(deviceId);
                userExecute.setSystemVersion(systemVersion);
                userExecute.setOperateId(sysUser.getId());
                try{
                	sysUserExecuteService.add(userExecute);
            		SysUserExecute sysUserExe = sysUserExecuteService.getByUsername(username);
            		//注册人积分增加（正常注册） 
            		userPointService.addByInvite(sysUserExe,null,adpointreg,username,sysUserExecute);
                }catch (Exception e){
                	logger.error(e);
                    result.setCode(ResultCode.RESULT_FAILURE.getCode());
                    result.setResultDes("注册失败！");
                    model.addAttribute(SysConst.RESULT_KEY, result);
                    return model;
                }
        	}else {
        		//邀请码输入错误
        		result.setCode(ResultCode.RESULT_FAILURE.getCode());
                result.setResultDes("注册失败！,邀请人不存在，请重新输入！");
                model.addAttribute(SysConst.RESULT_KEY, result);
                return model;
        	}
        } else {
	        //正常注册(注册的人添加的积分是"正常注册")
        	AdPoint adpointreg = pointService.findPointValue(AdPointEnum.NORMAL_REGIST.getId()); //正常注册
	    	String md5Pwd = new Md5Hash(password, username).toString();
	
	        userExecute = new SysUserExecute();
	        userExecute.setUsername(username);
	        userExecute.setRealname(username);
	        userExecute.setPassword(md5Pwd);
	        userExecute.setUsertype(AppUserTypeEnum.SOCIAL.getId());
	        userExecute.setStatus(1);
	        userExecute.setMobile(username);
	        userExecute.setMac(mac);
	        userExecute.setCreateTime(now);
            userExecute.setUpdateTime(now);
            userExecute.setDeviceId(deviceId);
            userExecute.setSystemVersion(systemVersion);
	        try{
	        	sysUserExecuteService.add(userExecute);
	        	SysUserExecute sysUser = sysUserExecuteService.getByUsername(username);
	        	//正常注册积分增加
	        	userPointService.addByReg(sysUser,adpointreg);
	        }catch (Exception e){
	        	logger.error(e);
	            result.setCode(ResultCode.RESULT_FAILURE.getCode());
	            result.setResultDes("注册失败！");
	            model.addAttribute(SysConst.RESULT_KEY, result);
	            return model;
	        }
        }
//        SysUserExecute userExecute = sysUserExecuteService.getByUsername(username);
//        if (userExecute == null || !md5Pwd.equals(userExecute.getPassword())) {
//            result.setCode(ResultCode.RESULT_FAILURE.getCode());
//            result.setResultDes("用户名或密码有误！");
//            model.addAttribute(SysConst.RESULT_KEY, result);
//            return model;
//        }

        if (useSession.get()) {
            session.setAttribute(SessionKey.SESSION_LOGIN_USER.toString(), userExecute);
        } else {
            sessionByRedis.setAttribute(SessionKey.SESSION_LOGIN_USER.toString(), userExecute);
        }
        saveLog(request, username);
        result.setResult(new SysUserExecuteVo(userExecute));
        model.addAttribute(SysConst.RESULT_KEY, result);
//        response.getHeaders().add("Access-Control-Allow-Credentials","true");
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("origin"));
        response.setHeader("Access-Control-Allow-Credentials", "true");
        return model;
    }

    //邀请码为公司邮箱时 返回公司名
    @RequestMapping(value = "/getCompanyName", method = RequestMethod.POST)
    @ResponseBody
    public Model getCompanyName(Model model, HttpServletRequest request, HttpServletResponse response) {
    	ResultVo<SysUser> result = new ResultVo<>();
        result.setCode(ResultCode.RESULT_SUCCESS.getCode());
        model = new ExtendedModelMap();
        String inviteAcc = null;
        try {
            InputStream is = request.getInputStream();
            Gson gson = new Gson();
            JsonObject obj = gson.fromJson(new InputStreamReader(is), JsonObject.class);
            inviteAcc = obj.get("inviteAcc") == null ? null : obj.get("inviteAcc").getAsString();
        } catch (IOException e) {
        	logger.error(e);
            result.setCode(ResultCode.RESULT_FAILURE.getCode());
            result.setResultDes("系统繁忙，请稍后再试！");
            model.addAttribute(SysConst.RESULT_KEY, result);
            return model;
        }
        SysUser sysUser = null;
        if(!StringUtils.isEmpty(inviteAcc)) {
        	//输入的是公司邮箱
         	sysUser = sysUserService.getUserName(inviteAcc);
        }
        //未查询到该公司
        if(sysUser == null) {
        	result.setCode(ResultCode.RESULT_FAILURE.getCode());
        	result.setResultDes("未查询到该公司,请输入正确的公司邮箱！");
        }else {
        	model.addAttribute("companyName" , sysUser.getRealname());
        }
        
        model.addAttribute(SysConst.RESULT_KEY, result);
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("origin"));
        response.setHeader("Access-Control-Allow-Credentials", "true");
        return model;
    }
    //app端手机号验证码登录
    @RequestMapping(value = "/smsLogin", method = RequestMethod.POST)
    @ResponseBody
    public Model smsLogin(Model model, HttpServletRequest request, HttpServletResponse response) {
        ResultVo<SysUserExecuteVo> result = new ResultVo<>();
        result.setCode(ResultCode.RESULT_SUCCESS.getCode());
        result.setResultDes("登录成功");
        model = new ExtendedModelMap();
        String username = null;
        String vcode = null;
        String token = null;
        String deviceId=null;
        String systemVersion=null;
        
        try {
            InputStream is = request.getInputStream();
            Gson gson = new Gson();
            JsonObject obj = gson.fromJson(new InputStreamReader(is), JsonObject.class);
            username = obj.get("username") == null ? null : obj.get("username").getAsString();
            vcode = obj.get("vcode") == null ? null : obj.get("vcode").getAsString();
            token = obj.get("token") == null ? null : obj.get("token").getAsString();
            deviceId = obj.get("deviceId") == null ? null : obj.get("deviceId").getAsString();
            systemVersion = obj.get("systemVersion") == null ? null : obj.get("systemVersion").getAsString();
            if (token != null) {
                useSession.set(Boolean.FALSE);
                this.sessionByRedis.setToken(token);
            } else {
                useSession.set(Boolean.TRUE);
            }
        } catch (IOException e) {
        	logger.error(e);
            result.setCode(ResultCode.RESULT_FAILURE.getCode());
            result.setResultDes("系统繁忙，请稍后再试！");
            model.addAttribute(SysConst.RESULT_KEY, result);
            return model;
        }

        // 账户必须验证
        if (StringUtils.isEmpty(username)||!Pattern.matches(MOBILE_NUMBER_REGEX,username)) {
            result.setCode(ResultCode.RESULT_FAILURE.getCode());
            result.setResultDes("手机号有误！");
            model.addAttribute(SysConst.RESULT_KEY, result);
            return model;
        }

        // 验证码必须验证
        if (StringUtils.isEmpty(vcode)) {
            result.setCode(ResultCode.RESULT_FAILURE.getCode());
            result.setResultDes("验证码为必填！");
            model.addAttribute(SysConst.RESULT_KEY, result);
            return model;
        }

        String sessionCode = null;
        HttpSession session = request.getSession();
        if (useSession.get()) {
            sessionCode = session.getAttribute(SessionKey.SESSION_CODE.toString()) == null ? ""
                    : session.getAttribute(SessionKey.SESSION_CODE.toString()).toString();
        } else {
            sessionCode = sessionByRedis.getImageCode();
        }

        // 验证码有效验证
        if (!vcode.equalsIgnoreCase(sessionCode)) {
            result.setCode(ResultCode.RESULT_FAILURE.getCode());
            result.setResultDes("验证码错误！");
            model.addAttribute(SysConst.RESULT_KEY, result);
            return model;
        }


        SysUserExecute userExecute = sysUserExecuteService.getByUsername(username);
        if(userExecute==null){
            result.setCode(ResultCode.RESULT_FAILURE.getCode());
            result.setResultDes("账户不存在！");
            model.addAttribute(SysConst.RESULT_KEY, result);
            return model;
        }
        if(userExecute.getStatus()==2){
            result.setCode(ResultCode.RESULT_FAILURE.getCode());
            result.setResultDes("账户已停用！");
            model.addAttribute(SysConst.RESULT_KEY, result);
            return model;
        }
//        SysUserExecute userExecute = sysUserExecuteService.getByUsername(username);
//        if (userExecute == null || !md5Pwd.equals(userExecute.getPassword())) {
//            result.setCode(ResultCode.RESULT_FAILURE.getCode());
//            result.setResultDes("用户名或密码有误！");
//            model.addAttribute(SysConst.RESULT_KEY, result);
//            return model;
//        }

        if (!userExecute.getStatus().equals(Integer.valueOf(1))) {
            result.setCode(ResultCode.RESULT_FAILURE.getCode());
            result.setResultDes("账号已被停用！");
            model.addAttribute(SysConst.RESULT_KEY, result);
            return model;
        }
        
        if(StringUtil.isNotBlank(deviceId) && StringUtil.isNotBlank(systemVersion)) {
        	SysUserExecute sysUserExecute = new SysUserExecute();
        	sysUserExecute.setId(userExecute.getId());
        	sysUserExecute.setDeviceId(deviceId);
        	sysUserExecute.setSystemVersion(systemVersion);
        	sysUserExecuteService.updatePhoneModel(sysUserExecute);
        }

        if (useSession.get()) {
            session.setAttribute(SessionKey.SESSION_LOGIN_USER.toString(), userExecute);
        } else {
            sessionByRedis.setAttribute(SessionKey.SESSION_LOGIN_USER.toString(), userExecute);
        }

        saveLog(request, username);
        result.setResult(new SysUserExecuteVo(userExecute));
        model.addAttribute(SysConst.RESULT_KEY, result);
//        response.getHeaders().add("Access-Control-Allow-Credentials","true");
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("origin"));
        response.setHeader("Access-Control-Allow-Credentials", "true");
        return model;
    }

    //app端手机号验证码验证，修改密码
    @RequestMapping(value = "/smsResetPassword", method = RequestMethod.POST)
    @ResponseBody
    public Model smsResetPassword(Model model, HttpServletRequest request, HttpServletResponse response) {
        ResultVo<SysUserExecuteVo> result = new ResultVo<>();
        result.setCode(ResultCode.RESULT_SUCCESS.getCode());
        result.setResultDes("重置成功");
        model = new ExtendedModelMap();
        String username = null;
        String password = null;
        String vcode = null;
        String token = null;

        try {
            InputStream is = request.getInputStream();
            Gson gson = new Gson();
            JsonObject obj = gson.fromJson(new InputStreamReader(is), JsonObject.class);
            username = obj.get("username") == null ? null : obj.get("username").getAsString();
            password = obj.get("password") == null ? null : obj.get("password").getAsString();
            vcode = obj.get("vcode") == null ? null : obj.get("vcode").getAsString();
            token = obj.get("token") == null ? null : obj.get("token").getAsString();
            if (token != null) {
                useSession.set(Boolean.FALSE);
                this.sessionByRedis.setToken(token);
            } else {
                useSession.set(Boolean.TRUE);
            }
        } catch (IOException e) {
        	logger.error(e);
            result.setCode(ResultCode.RESULT_FAILURE.getCode());
            result.setResultDes("系统繁忙，请稍后再试！");
            model.addAttribute(SysConst.RESULT_KEY, result);
            return model;
        }

        // 账户必须验证
        if (StringUtils.isEmpty(username)||!Pattern.matches(MOBILE_NUMBER_REGEX,username)) {
            result.setCode(ResultCode.RESULT_FAILURE.getCode());
            result.setResultDes("手机号有误！");
            model.addAttribute(SysConst.RESULT_KEY, result);
            return model;
        }

        // 验证码必须验证
        if (StringUtils.isEmpty(vcode)) {
            result.setCode(ResultCode.RESULT_FAILURE.getCode());
            result.setResultDes("验证码为必填！");
            model.addAttribute(SysConst.RESULT_KEY, result);
            return model;
        }

        String sessionCode = null;
        HttpSession session = request.getSession();
        if (useSession.get()) {
            sessionCode = session.getAttribute(SessionKey.SESSION_CODE.toString()) == null ? ""
                    : session.getAttribute(SessionKey.SESSION_CODE.toString()).toString();
        } else {
            sessionCode = sessionByRedis.getImageCode();
        }

        // 验证码有效验证
        if (!vcode.equalsIgnoreCase(sessionCode)) {
            result.setCode(ResultCode.RESULT_FAILURE.getCode());
            result.setResultDes("验证码错误！");
            model.addAttribute(SysConst.RESULT_KEY, result);
            return model;
        }


        SysUserExecute userExecute = sysUserExecuteService.getByUsername(username);
        if(userExecute==null){
            result.setCode(ResultCode.RESULT_FAILURE.getCode());
            result.setResultDes("账户不存在！");
            model.addAttribute(SysConst.RESULT_KEY, result);
            return model;
        }
        if(userExecute.getStatus()==2){
            result.setCode(ResultCode.RESULT_FAILURE.getCode());
            result.setResultDes("账户已停用！");
            model.addAttribute(SysConst.RESULT_KEY, result);
            return model;
        }

        try{
            String md5Pwd = new Md5Hash(password, username).toString();
            userExecute.setPassword(md5Pwd);
            userExecute.setUpdateTime(new Date());
            sysUserExecuteService.modify(userExecute);
        }catch (Exception e){
        	logger.error(e);
            result.setCode(ResultCode.RESULT_FAILURE.getCode());
            result.setResultDes("账户已停用！");
            model.addAttribute(SysConst.RESULT_KEY, result);
            return model;
        }

        if (useSession.get()) {
            session.setAttribute(SessionKey.SESSION_LOGIN_USER.toString(), userExecute);
        } else {
            sessionByRedis.setAttribute(SessionKey.SESSION_LOGIN_USER.toString(), userExecute);
        }
        saveLog(request, username);
        result.setResult(new SysUserExecuteVo(userExecute));
        model.addAttribute(SysConst.RESULT_KEY, result);
//        response.getHeaders().add("Access-Control-Allow-Credentials","true");
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("origin"));
        response.setHeader("Access-Control-Allow-Credentials", "true");
        return model;
    }

    //社会人员/媒体监测人员 抢单
    @RequestMapping(value = "/grabTask")
    @ResponseBody
    public Model grabTask(Model model, HttpServletRequest request, HttpServletResponse response) {
        ResultVo result = new ResultVo();
        result.setCode(ResultCode.RESULT_SUCCESS.getCode());
        result.setResultDes("调用成功");
        model = new ExtendedModelMap();

        String token = null;
        Integer taskId=null;

        try {
            InputStream is = request.getInputStream();
            Gson gson = new Gson();
            JsonObject obj = gson.fromJson(new InputStreamReader(is), JsonObject.class);
            token = obj.get("token") == null ? null : obj.get("token").getAsString();
            taskId = obj.get("task_id") == null ? null : obj.get("task_id").getAsInt();
            if (token != null) {
                useSession.set(Boolean.FALSE);
                this.sessionByRedis.setToken(token);
            } else {
                useSession.set(Boolean.TRUE);
            }
        } catch (IOException e) {
        	logger.error(e);
            result.setCode(ResultCode.RESULT_FAILURE.getCode());
            result.setResultDes("系统繁忙，请稍后再试！");
            model.addAttribute(SysConst.RESULT_KEY, result);
            return model;
        }

        //验证登录
        if (useSession.get()) {
            if (!checkLogin(model, result, request)) {
                return model;
            }
        } else {
            if (!checkLogin(model, result, token)) {
                return model;
            }
        }

        SysUserExecute user = getLoginUser(request, token);
        saveLog(request, user.getUsername());
        try{
            boolean flag = adMonitorTaskService.grabTask(user.getId(),taskId);
            //flag 即表示任务接取成功或失败，直接赋予 result
            result.setResult(flag);
        }catch (Exception e){
        	logger.error(e);
            result.setCode(ResultCode.RESULT_FAILURE.getCode());
            result.setResultDes("系统繁忙，请稍后再试！");
            model.addAttribute(SysConst.RESULT_KEY, result);
            return model;
        }

        model.addAttribute(SysConst.RESULT_KEY, result);
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("origin"));
        response.setHeader("Access-Control-Allow-Credentials", "true");
        return model;
    }

    //测试短信
    @RequestMapping(value = "/sendSms")
    @ResponseBody
    public void sendSms() {
    	String cell = "18657478455";
    	String randomNum = "1234";
    	String signature = "浙江百泰";
    	String context = "您的验证码为";
    	StringBuffer buffer = new StringBuffer();
    	buffer.append("【");
    	buffer.append(signature);
    	buffer.append("】");
    	buffer.append(context);
    	buffer.append(randomNum);
    	sendSmsService.sendSms(cell, buffer.toString());
    }

    //【1】广告主首页简单报表(即查询广告主下面的所有活动列表)
    @RequestMapping(value = "/getActivityReport")
    @ResponseBody
    public Model getCustomerActivityReport(Model model, HttpServletRequest request, HttpServletResponse response) {
    	ResultVo result = new ResultVo();
        result.setCode(ResultCode.RESULT_SUCCESS.getCode());
        result.setResultDes("调用成功");
        model = new ExtendedModelMap();

        String token = null;
        Integer taskId = null;
        Integer page = 1;
        Integer pageSize = 5;
        Date now = new Date();
        String startDate = null; //搜索的开始时间
        String endDate = null; //搜索的结束时间
        String activityName = null; //活动名称
        Integer status = null; //广告活动状态(1：未确认 2：已确认 3：已结束)
        String updateTime = null;
        try {
            InputStream is = request.getInputStream();
            Gson gson = new Gson();
            JsonObject obj = gson.fromJson(new InputStreamReader(is), JsonObject.class);
            token = obj.get("token") == null ? null : obj.get("token").getAsString();
            startDate = obj.get("startDate") == null ? null : obj.get("startDate").getAsString();
            endDate = obj.get("endDate") == null ? null : obj.get("endDate").getAsString();
            activityName = obj.get("activityName") == null ? null : obj.get("activityName").getAsString();
            status = obj.get("status") == null ? null : obj.get("status").getAsInt();
            updateTime = obj.get("updateTime") == null ? null : obj.get("updateTime").getAsString();
            if(obj.get("page") != null){
                page = obj.get("page").getAsInt();
            }
            if(obj.get("page_size") != null){
                pageSize = obj.get("page_size").getAsInt();
            }
            if (token != null) {
                useSession.set(Boolean.FALSE);
                this.sessionByRedis.setToken(token);
            } else {
                useSession.set(Boolean.TRUE);
            }
        } catch (IOException e) {
        	logger.error(e);
            result.setCode(ResultCode.RESULT_FAILURE.getCode());
            result.setResultDes("系统繁忙，请稍后再试！");
            model.addAttribute(SysConst.RESULT_KEY, result);
            return model;
        }

        //验证登录
        if (useSession.get()) {
            if (!checkLogin(model, result, request)) {
                return model;
            }
        } else {
            if (!checkLogin(model, result, token)) {
                return model;
            }
        }

        SysUserExecute user = getLoginUser(request, token); //APP登录的广告商
        SysUserVo sysUserVo = sysUserService.findByUsername(user.getUsername()); //通过APP和后台用户同一个用户名的关系去查后台customer用户的信息
        saveLog(request, user.getUsername());
        List<Integer> activityIds = new ArrayList<>();
        //首先查询时间段以外的活动信息
        if(startDate != null && endDate != null) {
        	Map<String, Object> searchMap = new HashMap<>();
            searchMap.put("userId", sysUserVo.getId());
        	searchMap.put("startDate", DateUtil.parseStrDate(startDate, "yyyy-MM-dd"));
            searchMap.put("endDate", DateUtil.parseStrDate(endDate, "yyyy-MM-dd"));
            activityIds = adActivityService.selectReportPageDataTime(searchMap);
            if(activityIds.size() == 0) {
            	activityIds = null;
            }
        } else {
        	activityIds = null;
        }
        
        //分页查询广告商的活动信息
        SearchDataVo vo = new SearchDataVo(null, null, (page-1)*pageSize, pageSize);
        vo.putSearchParam("userId", null, sysUserVo.getId());
        if(StringUtil.isNotBlank(activityName)) {
        	activityName = "%" + activityName + "%";
        }
        vo.putSearchParam("activityName", null, activityName);
        vo.putSearchParam("activityIds", null, activityIds);
        vo.putSearchParam("status", null, status);
        adActivityService.selectReportPageData(vo);
        //设置总页数
        int totalCount = (int) ((vo.getCount() + pageSize - 1) / pageSize);
        
        //设置结果集
        List<CustomerActivityReport> reports = new ArrayList<>();
        for(Object obj : vo.getList()){
        	AdActivity activity = (AdActivity) obj;
        	CustomerActivityReport report = new CustomerActivityReport();
        	report.setActivityId(activity.getId());
        	report.setActivityName(activity.getActivityName());
        	report.setCreateTime(DateUtil.dateFormate(activity.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
        	report.setStartTime(DateUtil.dateFormate(activity.getStartTime(), "yyyy-MM-dd"));
        	report.setEndTime(DateUtil.dateFormate(activity.getEndTime(), "yyyy-MM-dd"));
        	if(activity.getStatus() == 1) {
        		report.setStatus("未确认");
        	} else if(activity.getStatus() == 2) {
        		report.setStatus("已确认");
        	} else {
        		report.setStatus("已结束");
        	}
        	Integer unstartNum = 0;
        	Integer watchingNum = 0;
        	Integer hasProblemNum = 0;
        	if(!StringUtil.equals(report.getStatus(), "已结束")) {
        		List<AdActivityAdseatTaskVo> adseatTaskVos = adActivityService.selectAdSeatTaskReport(activity.getId());
            	for (AdActivityAdseatTaskVo adseatTaskVo : adseatTaskVos) {
            		if(adseatTaskVo.getMonitorStart().getTime() > now.getTime()) {
            			unstartNum++;
    				}
    				if(adseatTaskVo.getProblem_count() > 0) {
    					hasProblemNum++;
    				}
            	}
            	watchingNum = adseatTaskVos.size() - unstartNum - hasProblemNum;
        	}
        	report.setUnstartNum(unstartNum);
        	report.setWatchingNum(watchingNum);
        	report.setHasProblemNum(hasProblemNum);
        	
        	reports.add(report);
        }
        
        result.setResult(reports);

        model.addAttribute("totalCount", totalCount);
        model.addAttribute(SysConst.RESULT_KEY, result);
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("origin"));
        response.setHeader("Access-Control-Allow-Credentials", "true");
        return model;
    }

    //个人中心信息获取
    @RequestMapping(value = "/getAppUserInfo")
    @ResponseBody
    public Model getAppUserInfo(Model model, HttpServletRequest request, HttpServletResponse response) {
    	ResultVo result = new ResultVo();
        result.setCode(ResultCode.RESULT_SUCCESS.getCode());
        result.setResultDes("调用成功");
        model = new ExtendedModelMap();
        
        String token = null;

        try {
            InputStream is = request.getInputStream();
            Gson gson = new Gson();
            JsonObject obj = gson.fromJson(new InputStreamReader(is), JsonObject.class);
            token = obj.get("token") == null ? null : obj.get("token").getAsString();
            if (token != null) {
                useSession.set(Boolean.FALSE);
                this.sessionByRedis.setToken(token);
            } else {
                useSession.set(Boolean.TRUE);
            }
        } catch (IOException e) {
        	logger.error(e);
            result.setCode(ResultCode.RESULT_FAILURE.getCode());
            result.setResultDes("系统繁忙，请稍后再试！");
            model.addAttribute(SysConst.RESULT_KEY, result);
            return model;
        }

        //验证登录
        if (useSession.get()) {
            if (!checkLogin(model, result, request)) {
                return model;
            }
        } else {
            if (!checkLogin(model, result, token)) {
                return model;
            }
        }

        SysUserExecute user = getLoginUser(request, token);
        user = sysUserExecuteService.getById(user.getId());
        result.setResult(user);
        saveLog(request, user.getUsername());
        model.addAttribute(SysConst.RESULT_KEY, result);
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("origin"));
        response.setHeader("Access-Control-Allow-Credentials", "true");
        return model;
    }
    
    //社会人员/媒体监测人员 抢单放弃任务
    @RequestMapping(value = "/abandonMonitorTask")
    @ResponseBody
    public Model abandonMonitorTask(Model model, HttpServletRequest request, HttpServletResponse response) {
    	ResultVo result = new ResultVo();
        result.setCode(ResultCode.RESULT_SUCCESS.getCode());
        result.setResultDes("调用成功");
        model = new ExtendedModelMap();
        
        String token = null;
        Integer monitorTaskId = null;
        Date now = new Date();

        try {
            InputStream is = request.getInputStream();
            Gson gson = new Gson();
            JsonObject obj = gson.fromJson(new InputStreamReader(is), JsonObject.class);
            token = obj.get("token") == null ? null : obj.get("token").getAsString();
            monitorTaskId = obj.get("monitorTaskId") == null ? null : obj.get("monitorTaskId").getAsInt();
            if (token != null) {
                useSession.set(Boolean.FALSE);
                this.sessionByRedis.setToken(token);
            } else {
                useSession.set(Boolean.TRUE);
            }
        } catch (IOException e) {
        	logger.error(e);
            result.setCode(ResultCode.RESULT_FAILURE.getCode());
            result.setResultDes("系统繁忙，请稍后再试！");
            model.addAttribute(SysConst.RESULT_KEY, result);
            return model;
        }

        //验证登录
//        if (useSession.get()) {
//            if (!checkLogin(model, result, request)) {
//                return model;
//            }
//        } else {
//            if (!checkLogin(model, result, token)) {
//                return model;
//            }
//        }
        
        if(monitorTaskId == null){
            result.setCode(ResultCode.RESULT_FAILURE.getCode());
            result.setResultDes("参数有误！");
            model.addAttribute(SysConst.RESULT_KEY, result);
            return model;
        }
        
        //获取登录用户, 判断用户类型
        SysUserExecute user = getLoginUser(request, token);
        if(user == null) {
        	result.setCode(ResultCode.RESULT_FAILURE.getCode());
            result.setResultDes("系统繁忙，请稍后再试！");
            model.addAttribute(SysConst.RESULT_KEY, result);
            return model;
        }
        saveLog(request, user.getUsername());
        //[1] 获取该条任务信息
        AdMonitorTask adMonitorTask = adMonitorTaskService.selectByPrimaryKey(monitorTaskId);
        
        AbandonTaskVo vo = new AbandonTaskVo();
        vo.setId(monitorTaskId);
        vo.setAbandonTime(now);
        vo.setUpdateTime(now);
        vo.setUserTaskStatus(AppUserTaskEnum.ABANDON.getId()); //1.正常 2.主动放弃 3.超时回收
        
        if(user.getUsertype().equals(AppUserTypeEnum.SOCIAL.getId())) {
        	//[1] 社会人员抢单 放弃任务
        	if(adMonitorTask.getStatus() == MonitorTaskStatus.TO_CARRY_OUT.getId() || adMonitorTask.getStatus() == MonitorTaskStatus.UN_FINISHED.getId()) {
            	//[2] 判断24+12小时的逻辑, 将状态改成 1：待指派 或 8：可抢单
                /**
                 * monitor_date + monitor_last_days - 2天 + 12小时 < now
                 * 可推出当 monitor_date < now - monitor_last_days + 2天 - 12小时 时改为1：可指派，否则为8：可抢单
                 */
                Date monitorDate = adMonitorTask.getMonitorDate();
                Integer monitorLastDays = adMonitorTask.getMonitorLastDays();
                if(monitorDate.getTime() <= now.getTime() - monitorLastDays*24*60*60*1000 + 2*24*60*60*1000 - 12*60*60*1000) {
                	vo.setTaskStatus(MonitorTaskStatus.UNASSIGN.getId()); //1：待指派
                } else {
                	vo.setTaskStatus(MonitorTaskStatus.CAN_GRAB.getId()); //8：可抢单
                }
            }
        } else if(user.getUsertype().equals(AppUserTypeEnum.MEDIA.getId())||user.getUsertype().equals(AppUserTypeEnum.THIRD_COMPANY.getId())) {
        	//[1] 媒体监测人员抢单(自己媒体公司下的任务) 放弃任务直接改回待指派
        	vo.setTaskStatus(MonitorTaskStatus.UNASSIGN.getId()); //1：待指派
        }
        
        //[3] 联表更新
        adMonitorTaskService.abandonUserTask(vo);
        
        model.addAttribute(SysConst.RESULT_KEY, result);
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("origin"));
        response.setHeader("Access-Control-Allow-Credentials", "true");
        return model;
    }
    
    //【2】广告主首页简单报表 详细报表信息(即查看某个活动下所有广告位列表以及监测任务列表, 有城市、媒体大类的筛选)
    @RequestMapping(value = "/activityDetailReport")
    @ResponseBody
    public Model activityDetailReport(Model model, HttpServletRequest request, HttpServletResponse response) {
        ResultVo result = new ResultVo();
        result.setCode(ResultCode.RESULT_SUCCESS.getCode());
        result.setResultDes("获取成功");
        model = new ExtendedModelMap();

        Integer page = 1;
        Integer pageSize = 5;
        String token = null;
        Date now = new Date();
        Integer activityId = null; //活动id
        Integer provinceId = null; //省份id
        Integer cityId = null; //城市id
        Integer mediaTypeParentId = null; //媒体大类id
        String updateTime = null; //app提交的时间, 防止分页数据可能造成的重复问题
        try {
            InputStream is = request.getInputStream();
            Gson gson = new Gson();
            JsonObject obj = gson.fromJson(new InputStreamReader(is), JsonObject.class);
            activityId = obj.get("activityId").getAsInt();
            token = obj.get("token") == null ? null : obj.get("token").getAsString();
            cityId = obj.get("cityId") == null ? null : obj.get("cityId").getAsInt();
            provinceId = obj.get("provinceId") == null ? null : obj.get("provinceId").getAsInt();
            mediaTypeParentId = obj.get("mediaTypeParentId") == null ? null : obj.get("mediaTypeParentId").getAsInt();
            updateTime = obj.get("updateTime") == null ? null : obj.get("updateTime").getAsString();
            if(obj.get("page") != null){
                page = obj.get("page").getAsInt();
            }
            if(obj.get("page_size") != null){
                pageSize = obj.get("page_size").getAsInt();
            }
            if (token != null) {
                useSession.set(Boolean.FALSE);
                this.sessionByRedis.setToken(token);
            } else {
                useSession.set(Boolean.TRUE);
            }
            
            //验证登录
            if (useSession.get()) {
                if (!checkLogin(model, result, request)) {
                    return model;
                }
            } else {
                if (!checkLogin(model, result, token)) {
                    return model;
                }
            }

            //获取登录的当前用户
            SysUserExecute user = getLoginUser(request, token);
            saveLog(request, user==null?"":user.getUsername());
            //查询媒体类型包括媒体大类媒体小类
    		List<AdMediaType> allAdMediaType = adMediaTypeService.getAll();
    		Map<Integer, String> mediaTypeMap = new HashMap<>();
    		for (AdMediaType adMediaType : allAdMediaType) {
    			mediaTypeMap.put(adMediaType.getId(), adMediaType.getName());
    		}
    		
    		//查询活动, 获取样例图
    		AdActivity adActivity = adActivityService.getById(activityId);
    		model.addAttribute("demoPic", adActivity.getSamplePicUrl()); //活动样例图
    		
    		//分页查询活动的广告位信息, 添加筛选条件
            SearchDataVo searchDataVo = new SearchDataVo(null, null, (page-1)*pageSize, pageSize);
            searchDataVo.putSearchParam("activityId", null, activityId);
            searchDataVo.putSearchParam("cityId", null, cityId);
            searchDataVo.putSearchParam("provinceId", null, provinceId);
            searchDataVo.putSearchParam("mediaTypeParentId", null, mediaTypeParentId);
    		searchDataVo.putSearchParam("updateTime", null, updateTime);
        	
        	adActivityService.selectAdActivityAdseatTask(searchDataVo); //查询该活动的所有广告位以及相关监测任务信息
            int totalCount = (int) ((searchDataVo.getCount() + pageSize - 1) / pageSize); //设置总页数
            
            //设置结果集
    		AppDetailReports appDetailReports = new AppDetailReports(); //总结果集
            List<AppDetailReport> notStartReport = new ArrayList<>(); //未开始的广告位结果集
        	List<AppDetailReport> monitorReport = new ArrayList<>(); //监测中的广告位结果集
        	List<AppDetailReport> problemReport = new ArrayList<>(); //有问题的广告位结果集
            
            List<?> list = searchDataVo.getList();
        	for (Object object : list) {
        		AdActivityAdseatTaskVo vo = (AdActivityAdseatTaskVo) object;
        		
				AppDetailReport report = new AppDetailReport();
				report.setSeatInfoName(vo.getInfo_name()); //广告位名称
				report.setMediaName(vo.getMediaName()); //媒体名称
				report.setDemoPic(vo.getDemoPic()); //活动预览图
				report.setMediaTypeParentName(mediaTypeMap.get(vo.getInfo_mediaTypeParentId())); //媒体大类
				report.setMediaTypeName(mediaTypeMap.get(vo.getInfo_mediaTypeId())); //媒体小类
				report.setProvince(cityCache.getCityName(vo.getInfo_province())); //省
				report.setCity(cityCache.getCityName(vo.getInfo_city())); //市
				report.setRegion(null); //区（县）
				report.setStreet(null); //街道（镇，乡）
				report.setRoad(vo.getInfo_road()); //主要路段
				report.setLocation(vo.getInfo_location()); //详细位置
				report.setUniqueKey(vo.getInfo_uniqueKey()); //唯一标识
				report.setMonitorStart(DateUtil.dateFormate(vo.getMonitorStart(), "yyyy-MM-dd")); //开始监测时间
				report.setMonitorEnd(DateUtil.dateFormate(vo.getMonitorEnd(), "yyyy-MM-dd")); //结束监测时间
				String currentStatus = AdMediaInfoStatus.WATCHING.getText(); //当前状态: 监测中
				if(vo.getProblem_count() > 0) {
					currentStatus = AdMediaInfoStatus.HAS_PROBLEM.getText(); //当前状态: 有问题
				}
				if(vo.getMonitorStart().getTime() > now.getTime()) {
					currentStatus = AdMediaInfoStatus.NOT_BEGIN.getText(); //当前状态: 未开始
				}
				if(vo.getMonitorEnd().getTime() < now.getTime()) {
					currentStatus = AdMediaInfoStatus.FINISHED.getText(); //当前状态: 已结束
	        	}
				report.setCurrentStatus(currentStatus); //当前状态
				report.setSize(vo.getInfo_adSize()); //广告位尺寸
				report.setArea(vo.getInfo_adArea()); //面积
				report.setLon(vo.getInfo_lon() + ""); //经度
				report.setLat(vo.getInfo_lat() + ""); //纬度
				report.setContactName(vo.getInfo_contactName()); //联系人姓名
				report.setContactCell(vo.getInfo_contactCell()); //联系人电话
				report.setMemo(vo.getInfo_memo()); //备注
				
				//查询图片
				Map<String, Object> searchMap = new HashMap<>();
				searchMap.put("activityId", activityId);
				searchMap.put("activityAdseatId", vo.getId());
				List<PictureVo> pictureVos = adMonitorTaskService.selectFeedBackByActivityIdAndSeatId(searchMap);
				List<PictureVo> upPics = new ArrayList<>(); //上刊监测图片集合
				List<PictureVo> durationPics = new ArrayList<>(); //投放期间监测图片集合
				List<PictureVo> downPics = new ArrayList<>(); //下刊监测图片集合
				List<PictureVo> upTaskPics = new ArrayList<>(); //上刊任务图片集合
				List<PictureVo> zhuijiaPics = new ArrayList<>(); //追加监测图片集合
				
				for (PictureVo pictureVo : pictureVos) {
					if(pictureVo.getDate() != null) {
						//代表有人做过该任务
						pictureVo.setTime(DateUtil.dateFormate(pictureVo.getDate(), "yyyy-MM-dd HH:mm:ss"));
						if(StringUtil.equals(pictureVo.getTaskType(), MonitorTaskType.UP_MONITOR.getId() + "")) {
							//1：上刊监测
							pictureVo.setTaskType("上刊监测");
							upPics.add(pictureVo);
						} else if(StringUtil.equals(pictureVo.getTaskType(), MonitorTaskType.DURATION_MONITOR.getId() + "")) {
							//2：投放期间监测
							pictureVo.setTaskType("投放期间监测");
							durationPics.add(pictureVo);
						} else if(StringUtil.equals(pictureVo.getTaskType(), MonitorTaskType.DOWNMONITOR.getId() + "")) {
							//3：下刊监测
							pictureVo.setTaskType("下刊监测");
							downPics.add(pictureVo);
						} else if(StringUtil.equals(pictureVo.getTaskType(), MonitorTaskType.UP_TASK.getId() + "")) {
							//5：上刊任务
							pictureVo.setTaskType("上刊任务");
							upTaskPics.add(pictureVo);
						} else if(StringUtil.equals(pictureVo.getTaskType(), MonitorTaskType.ZHUIJIA_MONITOR.getId() + "")) {
							//6：追加监测任务
							pictureVo.setTaskType("追加监测");
							zhuijiaPics.add(pictureVo);
						}
					}
				}
				if(upPics.size() > 0) {
					report.setUpPics(upPics);
				} else {
					report.setUpPics(null); //方便APP展示
				}
				if(durationPics.size() > 0) {
					report.setDurationPics(durationPics);
				} else {
					report.setDurationPics(null); //方便APP展示
				}
				if(downPics.size() > 0) {
					report.setDownPics(downPics);
				} else {
					report.setDownPics(null); //方便APP展示
				}
				if(upTaskPics.size() > 0) {
					report.setUpTaskPics(upTaskPics);
				} else {
					report.setUpTaskPics(null); //方便APP展示
				}
				if(zhuijiaPics.size() > 0) {
					report.setZhuijiaPics(zhuijiaPics);
				} else {
					report.setZhuijiaPics(null); //方便APP展示
				}
				
				if(StringUtil.equals(report.getCurrentStatus(), "监测中")) {
					monitorReport.add(report);
				} else if(StringUtil.equals(report.getCurrentStatus(), "有问题")) {
					problemReport.add(report);
				} else if(StringUtil.equals(report.getCurrentStatus(), "未开始")) {
					notStartReport.add(report);
				}
			}
        	appDetailReports.setNotStartReport(notStartReport);
        	appDetailReports.setMonitorReport(monitorReport);
        	appDetailReports.setProblemReport(problemReport);
        	result.setResult(appDetailReports);
        	
        	model.addAttribute("totalCount", totalCount); //当次查询总页数
        } catch (IOException e) {
        	logger.error(e);
            result.setCode(ResultCode.RESULT_FAILURE.getCode());
            result.setResultDes("系统繁忙，请稍后再试！");
            model.addAttribute(SysConst.RESULT_KEY, result);
            return model;
        }

        model.addAttribute(SysConst.RESULT_KEY, result);
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("origin"));
        response.setHeader("Access-Control-Allow-Credentials", "true");
        return model;
    }
    
    //【2】广告主首页简单报表详细报表信息 - 查询省市以及媒体大类（获取筛选条件用）
    @RequestMapping(value = "/getActivityCityAndMediaType")
    @ResponseBody
    public Model getActivityCityAndMediaType(Model model, HttpServletRequest request, HttpServletResponse response) {
    	ResultVo result = new ResultVo();
        result.setCode(ResultCode.RESULT_SUCCESS.getCode());
        result.setResultDes("获取成功");
        model = new ExtendedModelMap();

        Integer activityId = null;
        String token = null;
        try {
            InputStream is = request.getInputStream();
            Gson gson = new Gson();
            JsonObject obj = gson.fromJson(new InputStreamReader(is), JsonObject.class);
            activityId = obj.get("activityId").getAsInt();
            token = obj.get("token") == null ? null : obj.get("token").getAsString();
            if (token != null) {
                useSession.set(Boolean.FALSE);
                this.sessionByRedis.setToken(token);
            } else {
                useSession.set(Boolean.TRUE);
            }
            
            //验证登录
            if (useSession.get()) {
                if (!checkLogin(model, result, request)) {
                    return model;
                }
            } else {
                if (!checkLogin(model, result, token)) {
                    return model;
                }
            }
            SysUserExecute user = getLoginUser(request, token);
            saveLog(request, user.getUsername());
            //查询媒体类型包括媒体大类媒体小类
    		List<AdMediaType> allAdMediaType = adMediaTypeService.getAll();
    		Map<Integer, String> mediaTypeMap = new HashMap<>();
    		for (AdMediaType adMediaType : allAdMediaType) {
    			mediaTypeMap.put(adMediaType.getId(), adMediaType.getName());
    		}
    		
    		//查询活动的广告位信息 - 省市以及媒体大类
    		List<AdSeatInfo> adSeatInfos = adActivityService.selectSeatInfoByActivityId(activityId);
    		
    		//设置结果集
        	List<ProvinceAndCity> provinceAndCities = new ArrayList<>(); //该活动下所有广告位所在的省市关联
        	List<ParentMediaType> parentMediaTypes = new ArrayList<>(); //该活动下所有广告位的媒体大类
        	Map<Long, Set<Long>> cityMap = new HashMap<>();
        	Map<Integer, String> typeMap = new HashMap<>();
        	
        	for (AdSeatInfo adSeatInfo : adSeatInfos) {
        		typeMap.put(adSeatInfo.getMediaTypeParentId(), mediaTypeMap.get(adSeatInfo.getMediaTypeParentId()));
				
				if(cityMap.containsKey(adSeatInfo.getProvince())) {
					Set<Long> cities = cityMap.get(adSeatInfo.getProvince());
					cities.add(adSeatInfo.getCity());
					cityMap.put(adSeatInfo.getProvince(), cities);
				} else {
					Set<Long> cities = new HashSet<>();
					cities.add(adSeatInfo.getCity());
					cityMap.put(adSeatInfo.getProvince(), cities);
				}
			}

        	//设置其余结果集
        	//[1] 设置省-市关联集合
        	Set<Entry<Long,Set<Long>>> cityEntrySet = cityMap.entrySet();
        	Iterator<Entry<Long, Set<Long>>> cityIterator = cityEntrySet.iterator();
        	while (cityIterator.hasNext()) {
        		//遍历map
        		Entry<Long, Set<Long>> next = cityIterator.next();
        		ProvinceAndCity provinceAndCity = new ProvinceAndCity();
        		//设置省信息
        		City provinceInfo = new City();
        		provinceInfo.setId(next.getKey());
        		provinceInfo.setName(cityCache.getCityName(next.getKey()));
        		provinceAndCity.setProvinceInfo(provinceInfo);
        		//设置市信息
        		Set<Long> cities = next.getValue();
        		List<City> cityInfos = new ArrayList<>();
        		for (Long id : cities) {
        			City cityInfo = new City();
        			cityInfo.setId(id);
        			cityInfo.setName(cityCache.getCityName(id));
        			cityInfos.add(cityInfo);
				}
        		provinceAndCity.setCityInfo(cityInfos);
        		//放入结果集
        		provinceAndCities.add(provinceAndCity);
			}
        	//[2] 设置媒体大类集合
        	Set<Entry<Integer,String>> mediaTypeEntrySet = typeMap.entrySet();
        	Iterator<Entry<Integer, String>> mediaTypeIterator = mediaTypeEntrySet.iterator();
        	while (mediaTypeIterator.hasNext()) {
        		Entry<Integer, String> next = mediaTypeIterator.next();
        		ParentMediaType parentMediaType = new ParentMediaType();
        		//设置媒体大类信息
        		parentMediaType.setId(next.getKey());
        		parentMediaType.setTypeName(next.getValue());
        		parentMediaTypes.add(parentMediaType);
			}
        	
        	model.addAttribute("provinceAndCities", provinceAndCities); //该活动下所有广告位的省市关联集合
        	model.addAttribute("parentMediaTypes", parentMediaTypes); //该活动下所有广告位的媒体大类集合
        } catch (Exception e) {
        	logger.error(e);
            result.setCode(ResultCode.RESULT_FAILURE.getCode());
            result.setResultDes("系统繁忙，请稍后再试！");
            model.addAttribute(SysConst.RESULT_KEY, result);
            return model;
        }

        model.addAttribute(SysConst.RESULT_KEY, result);
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("origin"));
        response.setHeader("Access-Control-Allow-Credentials", "true");
        return model;
    }
    
    //获取app logo和app标题
    @RequestMapping(value = "/getBranch")
    @ResponseBody
    public Model getBranch(Model model, HttpServletRequest request, HttpServletResponse response) {
    	saveLog(request, "");
    	ResultVo result = new ResultVo();
        result.setCode(ResultCode.RESULT_SUCCESS.getCode());
        result.setResultDes("调用成功");
        model = new ExtendedModelMap();
        
        String sid = null;
        
        try {
        	InputStream is = request.getInputStream();
            Gson gson = new Gson();
            JsonObject obj = gson.fromJson(new InputStreamReader(is), JsonObject.class);
            sid = obj.get("sid").getAsString();
            
           if(StringUtil.isBlank(sid)) {
            	result.setCode(ResultCode.RESULT_FAILURE.getCode());
                result.setResultDes("系统繁忙，请稍后再试！");
                model.addAttribute(SysConst.RESULT_KEY, result);
                return model;
            }
            AdApp adApp = appService.selectAppPicUrlAndTitleBySid(sid);
            result.setResult(adApp);
        } catch (Exception e) {
        	logger.error(e);
            result.setCode(ResultCode.RESULT_FAILURE.getCode());
            result.setResultDes("系统繁忙，请稍后再试！");
            model.addAttribute(SysConst.RESULT_KEY, result);
            return model;
        }
        
        model.addAttribute(SysConst.RESULT_KEY, result);
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("origin"));
        response.setHeader("Access-Control-Allow-Credentials", "true");
        return model;
    }
    
    private Boolean checkLogin(Model model, ResultVo result, HttpServletRequest request) {
        boolean isLogin = true;
        HttpSession session = request.getSession();
        SysUserExecute user = (SysUserExecute) session.getAttribute(SessionKey.SESSION_LOGIN_USER.toString());
        if (user == null) {
            isLogin = false;
            result.setCode(ResultCode.RESULT_NOLOGIN.getCode());
            result.setResultDes("未登录！");
            model.addAttribute(SysConst.RESULT_KEY, result);
        }
        return isLogin;
    }

    private Boolean checkLogin(Model model, ResultVo result, String token) {
        boolean isLogin = true;
        this.sessionByRedis.setToken(token);
        SysUserExecute user = (SysUserExecute) this.sessionByRedis.getAttribute(SessionKey.SESSION_LOGIN_USER.toString());
        if (user == null) {
            isLogin = false;
            result.setCode(ResultCode.RESULT_NOLOGIN.getCode());
            result.setResultDes("未登录！");
            model.addAttribute(SysConst.RESULT_KEY, result);
        }
        return isLogin;
    }

    private SysUserExecute getLoginUser(HttpServletRequest request, String token) {
        if (useSession.get()) {
            HttpSession session = request.getSession();
            return (SysUserExecute) session.getAttribute(SessionKey.SESSION_LOGIN_USER.toString());
        } else {
            return (SysUserExecute) this.sessionByRedis.getAttribute(SessionKey.SESSION_LOGIN_USER.toString());
        }
    }
    
    /**
     * 根据位数生成验证码
     *
     * @param size 位数
     * @return
     */
    private String getNumber(int size) {

        String retNum = "";

        // 定义验证码的范围
//		String codeStr = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        String codeStr = "1234567890";

        Random r = new Random();
        for (int i = 0; i < size; i++) {
            retNum += codeStr.charAt(r.nextInt(codeStr.length()));
        }

        return retNum;
    }

    public static void main(String[] args) {
        System.out.println(new Md5Hash("admin123", "superadmin").toString());
//        System.out.println("【浙江百泰】您的验证码为${code}".replaceAll("\\$\\{code\\}","122321"));
    }
    /**
     * 保存日志信息
     * 
     * @param request
     * @param userName 当前登录用户
     */
    private void saveLog(HttpServletRequest request,String userName) {
    	String url = request.getRequestURI();
    	String method = request.getMethod();
    	if (!StringUtils.isEmpty(userName)) {
			userName = "  userName:" + userName;
		}
    	logger.info(method + "  " + url + "  " + userName);
    }
}
