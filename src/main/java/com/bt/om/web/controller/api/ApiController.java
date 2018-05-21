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
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
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
import com.bt.om.entity.AdJiucuoTask;
import com.bt.om.entity.AdJiucuoTaskFeedback;
import com.bt.om.entity.AdMediaType;
import com.bt.om.entity.AdMonitorReward;
import com.bt.om.entity.AdMonitorTask;
import com.bt.om.entity.AdMonitorTaskFeedback;
import com.bt.om.entity.AdSeatInfo;
import com.bt.om.entity.AdVersion;
import com.bt.om.entity.City;
import com.bt.om.entity.SysUserExecute;
import com.bt.om.entity.vo.AbandonTaskVo;
import com.bt.om.entity.vo.ActivityMobileReportVo;
import com.bt.om.entity.vo.AdActivityAdseatTaskVo;
import com.bt.om.entity.vo.AdActivityAdseatVo;
import com.bt.om.entity.vo.AdJiucuoTaskMobileVo;
import com.bt.om.entity.vo.AdMonitorTaskMobileVo;
import com.bt.om.entity.vo.AppDetailReport;
import com.bt.om.entity.vo.AppDetailReports;
import com.bt.om.entity.vo.PictureVo;
import com.bt.om.entity.vo.SysUserVo;
import com.bt.om.enums.AdMediaInfoStatus;
import com.bt.om.enums.JiucuoTaskStatus;
import com.bt.om.enums.MonitorTaskStatus;
import com.bt.om.enums.ResultCode;
import com.bt.om.enums.SessionKey;
import com.bt.om.enums.TaskProblemStatus;
import com.bt.om.enums.UserExecuteType;
import com.bt.om.service.IAdActivityService;
import com.bt.om.service.IAdJiucuoTaskService;
import com.bt.om.service.IAdMediaTypeService;
import com.bt.om.service.IAdMonitorRewardService;
import com.bt.om.service.IAdMonitorTaskService;
import com.bt.om.service.IAdSeatService;
import com.bt.om.service.ISendSmsService;
import com.bt.om.service.ISysUserExecuteService;
import com.bt.om.service.ISysUserService;
import com.bt.om.util.CityUtil;
import com.bt.om.util.GeoUtil;
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

    @Value("${sms.checkcode.content.template}")
    private String SMS_CHECKCODE_CONTENT_TEMPLATE;
    @Value("${mobile.number.regex}")
    private String MOBILE_NUMBER_REGEX;

    private static ThreadLocal<Boolean> useSession = new ThreadLocal<>();

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
            result.setCode(ResultCode.RESULT_FAILURE.getCode());
            result.setResultDes("二维码解析失败失败！");
            model.addAttribute(SysConst.RESULT_KEY, result);
            return model;
        } catch (Exception e) {
            e.printStackTrace();
            result.setCode(ResultCode.RESULT_FAILURE.getCode());
            result.setResultDes("二维码解析失败失败！");
            model.addAttribute(SysConst.RESULT_KEY, result);
            return model;
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
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

        InputStream is = null;
        String seatCode = null;
        Integer adSeatId = null;
        Double lon = null;
        Double lat = null;
        String title = null;
        
        try {
            is = request.getInputStream();
            Gson gson = new Gson();
            JsonObject obj = gson.fromJson(new InputStreamReader(is), JsonObject.class);
            seatCode = obj==null||obj.get("seatCode") == null ? null : obj.get("seatCode").getAsString(); //扫描二维码调取接口
            adSeatId = obj==null||obj.get("adSeatId") == null ? null : obj.get("adSeatId").getAsInt();
            lon = obj==null||obj.get("lon") == null ? null : obj.get("lon").getAsDouble(); //通过经纬度调取接口
            lat = obj==null||obj.get("lat") == null ? null : obj.get("lat").getAsDouble(); //通过经纬度调取接口
            title = obj==null||obj.get("title") == null ? null : obj.get("title").getAsString(); //通过经纬度调取接口
        } catch (IOException e) {
            result.setCode(ResultCode.RESULT_FAILURE.getCode());
            result.setResultDes("系统繁忙，请稍后再试！");
            model.addAttribute(SysConst.RESULT_KEY, result);
            return model;
        }finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        try {
        	List<AdJiucuoTask> jiucuoTasks = new ArrayList<>();
            List<AdActivityAdseatVo> list = null;
            if(seatCode!=null){
            	//扫描二维码调取接口
                list = adActivityService.getActivitySeatBySeatCode(seatCode);
                
                Map<String, Object> searchMap = new HashMap<>();
            	searchMap.put("status", 1); //待审核
            	searchMap.put("adSeatCode", seatCode); //二维码信息
            	jiucuoTasks = adJiucuoTaskService.selectInfoByQrCode(searchMap);
                
            	//移除
            	Iterator<AdActivityAdseatVo> iterator = list.iterator();
            	while (iterator.hasNext()) {
					AdActivityAdseatVo adActivityAdseatVo = (AdActivityAdseatVo) iterator.next();
					for (AdJiucuoTask task : jiucuoTasks) {
						if(task.getActivityId() == adActivityAdseatVo.getActivityId()) {
							iterator.remove();
							break;
						}
					}
				}
            }else if(lon!=null && lat!=null && title!=null) {
            	//通过经纬度调取接口
                list = adActivityService.selectVoByLonLatTitle(lon, lat, title);
                
                Map<String, Object> searchMap = new HashMap<>();
            	searchMap.put("status", 1); //待审核
            	searchMap.put("lon", lon); //经度
            	searchMap.put("lat", lat); //纬度
            	searchMap.put("title", title); //广告位名称
            	jiucuoTasks = adJiucuoTaskService.selectInfoByLonLatTitle(searchMap);
            	
            	//移除
            	Iterator<AdActivityAdseatVo> iterator = list.iterator();
            	while (iterator.hasNext()) {
					AdActivityAdseatVo adActivityAdseatVo = (AdActivityAdseatVo) iterator.next();
					for (AdJiucuoTask task : jiucuoTasks) {
						if(task.getActivityId() == adActivityAdseatVo.getActivityId()) {
							iterator.remove();
							break;
						}
					}
				}
            }
            
            QRCodeInfoVo qr = new QRCodeInfoVo();
//            qr.setAd_seat_id(Integer.valueOf(seatCode));
            if(list != null && list.size() > 0) {
                for (AdActivityAdseatVo vo : list) {
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
    public Model login(Model model, HttpServletRequest request, HttpServletResponse response) {
        ResultVo<SysUserExecuteVo> result = new ResultVo<>();
        result.setCode(ResultCode.RESULT_SUCCESS.getCode());
        result.setResultDes("登录成功");
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
            result.setCode(ResultCode.RESULT_FAILURE.getCode());
            result.setResultDes("系统繁忙，请稍后再试！");
            model.addAttribute(SysConst.RESULT_KEY, result);
            return model;
        }
        if (useSession.get()) {
            HttpSession session = request.getSession();
            session.removeAttribute(SessionKey.SESSION_LOGIN_USER.toString());
        } else {
            this.sessionByRedis.remove();
        }

        response.setHeader("Access-Control-Allow-Origin", request.getHeader("origin"));
        response.setHeader("Access-Control-Allow-Credentials", "true");
        return model;
    }

    //获取图片验证码
    @RequestMapping(value = "/getCodeBase64")
    @ResponseBody
    public Model getCode(Model model, HttpServletRequest request, HttpServletResponse response) {
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
            result.setCode(ResultCode.RESULT_FAILURE.getCode());
            result.setResultDes("系统繁忙，请稍后再试！");
            model.addAttribute(SysConst.RESULT_KEY, result);
            return model;
        } finally {
            try {
                baos.flush();
                baos.close();
            } catch (IOException e) {
            }
        }

        response.setHeader("Access-Control-Allow-Origin", request.getHeader("origin"));
        response.setHeader("Access-Control-Allow-Credentials", "true");
        return model;
    }

    //请求列表任务或纠错列表
    @RequestMapping(value = "/gettasklist")
    @ResponseBody
    public Model taskList(Model model, HttpServletRequest request, HttpServletResponse response) {
        ResultVo result = new ResultVo();
        result.setCode(ResultCode.RESULT_SUCCESS.getCode());
        result.setResultDes("获取成功");
        model = new ExtendedModelMap();

        Integer type = null;
        String token = null;

        try {
            InputStream is = request.getInputStream();
            Gson gson = new Gson();
            JsonObject obj = gson.fromJson(new InputStreamReader(is), JsonObject.class);
            type = obj.get("type").getAsInt();
            token = obj.get("token") == null ? null : obj.get("token").getAsString();
            if (token != null) {
                useSession.set(Boolean.FALSE);
                this.sessionByRedis.setToken(token);
            } else {
                useSession.set(Boolean.TRUE);
            }
        } catch (IOException e) {
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
        //任务列表(已修改：添加了province, city, region, street, startTime, endTime, assignType)
        if (type == 1) {
            List<AdMonitorTaskMobileVo> tasks = adMonitorTaskService.getByUserIdForMobile(user.getId());
            MonitorTaskListResultVo resultVo = new MonitorTaskListResultVo();
            for (AdMonitorTaskMobileVo task : tasks) {
                if (task.getStatus() == MonitorTaskStatus.TO_CARRY_OUT.getId()) {
                    MonitorTaskWaitToExecutedVo vo = new MonitorTaskWaitToExecutedVo(task);
                    vo.setProvince(cityCache.getCityName(task.getProvince()));
                    vo.setCity(cityCache.getCityName(task.getCity()));
                    vo.setRegion(cityCache.getCityName(task.getRegion()));
                    vo.setStreet(cityCache.getCityName(task.getStreet()));
                    resultVo.getWait_to_executed().add(vo);
                } else if (task.getStatus() == MonitorTaskStatus.UNVERIFY.getId()) {
                    MonitorTaskExecutingVo vo = new MonitorTaskExecutingVo(task);
                    vo.setProvince(cityCache.getCityName(task.getProvince()));
                    vo.setCity(cityCache.getCityName(task.getCity()));
                    vo.setRegion(cityCache.getCityName(task.getRegion()));
                    vo.setStreet(cityCache.getCityName(task.getStreet()));
                    resultVo.getExecuting().add(vo);
                } else if (task.getStatus() == MonitorTaskStatus.VERIFIED.getId() || task.getStatus() == MonitorTaskStatus.VERIFY_FAILURE.getId()) {
                    MonitorTaskCheckedVo vo = new MonitorTaskCheckedVo(task);
                    vo.setProvince(cityCache.getCityName(task.getProvince()));
                    vo.setCity(cityCache.getCityName(task.getCity()));
                    vo.setRegion(cityCache.getCityName(task.getRegion()));
                    vo.setStreet(cityCache.getCityName(task.getStreet()));
                    resultVo.getChecked().add(vo);
                } else if (task.getStatus() == MonitorTaskStatus.UN_FINISHED.getId()) {
                	MonitorTaskUnFinishedVo vo = new MonitorTaskUnFinishedVo(task);
                	vo.setProvince(cityCache.getCityName(task.getProvince()));
                    vo.setCity(cityCache.getCityName(task.getCity()));
                	vo.setRegion(cityCache.getCityName(task.getRegion()));
                    vo.setStreet(cityCache.getCityName(task.getStreet()));
                    resultVo.getUn_finished().add(vo);
                }
            }
            result.setResult(resultVo);
            //纠错列表
        } else if (type == 2) {
            List<AdJiucuoTaskMobileVo> tasks = adJiucuoTaskService.getByUserIdForMobile(user.getId());
            JiucuoTaskListResultVo resultVo = new JiucuoTaskListResultVo();
            for (AdJiucuoTaskMobileVo task : tasks) {
                if (task.getStatus() == JiucuoTaskStatus.UNVERIFY.getId()) {
                    resultVo.getJiucuo_submit().add(new JiucuoTaskVo(task));
                } else if (task.getStatus() == JiucuoTaskStatus.VERIFIED.getId() || task.getStatus() == JiucuoTaskStatus.VERIFY_FAILURE.getId()) {
                    resultVo.getJiucuo_success().add(new JiucuoTaskVo(task));
                }
            }
            result.setResult(resultVo);
        }


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
        if (type == 1) {
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
                    adMonitorTaskService.feedback(taskId, feedback,adSeatCode);
                } catch (Exception e) {
                    result.setCode(ResultCode.RESULT_PARAM_ERROR.getCode());
                    result.setResultDes("保存出错！");
                    model.addAttribute(SysConst.RESULT_KEY, result);
                    return model;
                }
            } catch (IOException e) {
                result.setCode(ResultCode.RESULT_PARAM_ERROR.getCode());
                result.setResultDes("上传出错！");
                model.addAttribute(SysConst.RESULT_KEY, result);
                return model;
            }
        } else if (type == 2) {
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
            SysUserExecute user = getLoginUser(request, token);

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
                    result.setCode(ResultCode.RESULT_PARAM_ERROR.getCode());
                    result.setResultDes("保存出错！");
                    model.addAttribute(SysConst.RESULT_KEY, result);
                    return model;
                }
            } catch (IOException e) {
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
        if (type == 1) {
            //参数不对
            if (type == null || taskId == null || (file1 == null && file2 == null && file3 == null && file4 == null)) {
                result.setCode(ResultCode.RESULT_PARAM_ERROR.getCode());
                result.setResultDes("参数有误！");
                model.addAttribute(SysConst.RESULT_KEY, result);
                return model;
            }
            //不是以data:image/jpeg;base64,开头的说明并没有重新拍照
            if (file1!=null&&!file1.startsWith("data:image/jpeg;base64,")) {
                file1 = null;
            }
            if (file2!=null&&!file2.startsWith("data:image/jpeg;base64,")) {
                file2 = null;
            }
            if (file3!=null&&!file3.startsWith("data:image/jpeg;base64,")) {
                file3 = null;
            }
            if (file4!=null&&!file4.startsWith("data:image/jpeg;base64,")) {
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
                    result.setResultDes("上传出错！");
                    model.addAttribute(SysConst.RESULT_KEY, result);
                    return model;
                }
                AdMonitorTaskFeedback feedback = new AdMonitorTaskFeedback();
                feedback.setSeatLat(seatLat);
                feedback.setSeatLon(seatLon);
                feedback.setLat(lat);
                feedback.setLon(lon);
                if (filename1 != null) {
                    feedback.setPicUrl1("/static/upload/" + filename1);
                }
                if (filename2 != null) {
                    feedback.setPicUrl2("/static/upload/" + filename2);
                }
                if (file3 != null) {
                    feedback.setPicUrl3("/static/upload/" + filename3);
                }
                if (file4 != null) {
                    feedback.setPicUrl4("/static/upload/" + filename4);
                }
                feedback.setProblem(problem);
                feedback.setProblemOther(other);
                feedback.setStatus(1);
                try {
                    adMonitorTaskService.feedback(taskId, feedback,adSeatCode);
                } catch (Exception e) {
                    result.setCode(ResultCode.RESULT_PARAM_ERROR.getCode());
                    result.setResultDes("保存出错！");
                    model.addAttribute(SysConst.RESULT_KEY, result);
                    return model;
                }
            } catch (Exception e) {
                e.printStackTrace();
                result.setCode(ResultCode.RESULT_PARAM_ERROR.getCode());
                result.setResultDes("上传出错！");
                model.addAttribute(SysConst.RESULT_KEY, result);
                return model;
            }
        } else if (type == 2) {
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
            
            //查询是否有人正在做这个广告位这个活动的纠错任务(即待审核的纠错任务)
            Map<String, Object> searchMap = new HashMap<>();
            searchMap.put("activityId", seat.getActivityId()); //活动id
            searchMap.put("adSeatId", seat.getAdSeatId()); //广告位id
            searchMap.put("status", 1); //纠错任务待审核
            int count = adJiucuoTaskService.selectCountByActivityAndSeat(searchMap);
            if(count > 0) {
            	result.setCode(ResultCode.RESULT_FAILURE.getCode());
                result.setResultDes("已有人正在执行该纠错任务！");
                model.addAttribute(SysConst.RESULT_KEY, result);
                return model;
            }
            
            InputStream is1 = null;
            String filename1 = null;
            SysUserExecute user = getLoginUser(request, token);

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
                feedback.setPicUrl1("/static/upload/" + filename1);
                feedback.setProblem(problem);
                feedback.setProblemOther(other);

                try {
                    adJiucuoTaskService.feedback(task, feedback);
                } catch (Exception e) {
                    result.setCode(ResultCode.RESULT_PARAM_ERROR.getCode());
                    result.setResultDes("保存出错！");
                    model.addAttribute(SysConst.RESULT_KEY, result);
                    return model;
                }
            } catch (Exception e) {
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
        } catch (IOException e) {
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

        if(user.getUsertype()==3){
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
     * APP一打开调用
     * @param model
     * @param request
     * @param response
     */
    @RequestMapping(value = "/checkVersion")
    @ResponseBody
    public Model getLatestForceUpdateVersion(Model model, HttpServletRequest request, HttpServletResponse response) {
    	ResultVo result = new ResultVo();
        result.setCode(ResultCode.RESULT_SUCCESS.getCode());
        result.setResultDes("操作成功");
        model = new ExtendedModelMap();
        
        Integer needForceUpdate = 3; // 0: 有新版本,需要强制更新; 1: 有新版本,可去更新; 2: 最新版本; 3: 版本号有误
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
            
            //判断版本号比较, 提示是否需要强制更新
            if(version != null) {
            	String[] versionSplit = version.getAppVersion().split("\\.");
            	String[] appVersionSplit = appVersion.split("\\.");
            	for (int i = 0; i < appVersionSplit.length; i++) {
        			Integer versionInt = Integer.parseInt(versionSplit[i]);
        			Integer appVersionInt = Integer.parseInt(appVersionSplit[i]);
        			if(appVersionInt < versionInt) {
        				//需要强制更新
        				needForceUpdate = 0;
        				break;
        			} else if(appVersionInt > versionInt) {
        				//不需要强制更新
        				needForceUpdate = 1;
        				break;
        			}
        		}
            	result.setCode(ResultCode.RESULT_SUCCESS.getCode());
            }
            
            //判断版本号比较, 提示是否更新
            if(nowVersion == null) {
            	result.setCode(ResultCode.RESULT_SUCCESS.getCode());
                result.setResultDes("版本号有误！");
                needForceUpdate = 3;
                result.setResult(needForceUpdate);
                model.addAttribute(SysConst.RESULT_KEY, result);
                return model;
            } else {
            	if(needForceUpdate != 0) {
            		String[] versionSplit = nowVersion.getAppVersion().split("\\.");
                	String[] appVersionSplit = appVersion.split("\\.");
                	for (int i = 0; i < appVersionSplit.length; i++) {
            			Integer versionInt = Integer.parseInt(versionSplit[i]);
            			Integer appVersionInt = Integer.parseInt(appVersionSplit[i]);
            			if(appVersionInt < versionInt) {
            				needForceUpdate = 1;
            				break;
            			} else if(appVersionInt == versionInt) {
            				needForceUpdate = 2;
            			}
            		}
            	}
            	result.setCode(ResultCode.RESULT_SUCCESS.getCode());
			}
            
            if(needForceUpdate == 1) {
        		result.setResultDes("有新版本，可去更新！");
        	} else if(needForceUpdate == 0) {
        		result.setResultDes("有新版本，需要强制更新！");
        	} else {
        		result.setResultDes("最新版本！");
        	}
            result.setResult(needForceUpdate);
        } catch (IOException e) {
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
        } catch (IOException e) {
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

        List<AdSeatInfo> seats = adSeatService.getAdseatAround(lat,lon,metre);

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
        Integer pageSize = 20;

        try {
            InputStream is = request.getInputStream();
            Gson gson = new Gson();
            JsonObject obj = gson.fromJson(new InputStreamReader(is), JsonObject.class);
            token = obj.get("token") == null ? null : obj.get("token").getAsString();
            lon = obj.get("lon") == null ? null : obj.get("lon").getAsDouble();
            lat = obj.get("lat") == null ? null : obj.get("lat").getAsDouble();
            metre = obj.get("metre") == null ? null : obj.get("metre").getAsDouble();
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
        SearchDataVo vo = new SearchDataVo(null,null,(page-1)*pageSize,pageSize);
        vo.putSearchParam("lon",null,lon);
        vo.putSearchParam("lat",null,lat);
        vo.putSearchParam("metre",null,metre);
        vo.putSearchParam("metreDegree",null, GeoUtil.getDegreeFromDistance(metre));

        adMonitorTaskService.getByPointAroundPageData(vo);
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
        	list.add(arroundVo);
        }

        result.setResult(list);

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
        	list.add(arroundVo);
        }

        result.setResult(list);

        model.addAttribute(SysConst.RESULT_KEY, result);
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("origin"));
        response.setHeader("Access-Control-Allow-Credentials", "true");
        return model;
    }

    //发送短信验证码, 需要先校验图片验证码
    @RequestMapping(value = "/getSMSCode")
    @ResponseBody
    public Model getSMSCode(Model model, HttpServletRequest request, HttpServletResponse response) {
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
            result.setCode(ResultCode.RESULT_FAILURE.getCode());
            result.setResultDes("系统繁忙，请稍后再试！");
            model.addAttribute(SysConst.RESULT_KEY, result);
            return model;
        }
        
        // 验证码必须验证
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

        // 验证码有效验证
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


        String md5Pwd = new Md5Hash(password, username).toString();

        userExecute = new SysUserExecute();
        userExecute.setUsername(username);
        userExecute.setRealname(username);
        userExecute.setPassword(md5Pwd);
        userExecute.setUsertype(UserExecuteType.Social.getId());
        userExecute.setStatus(1);
        userExecute.setMobile(username);

        try{
            sysUserExecuteService.add(userExecute);
        }catch (Exception e){
            result.setCode(ResultCode.RESULT_FAILURE.getCode());
            result.setResultDes("注册失败！");
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

        result.setResult(new SysUserExecuteVo(userExecute));
        model.addAttribute(SysConst.RESULT_KEY, result);
//        response.getHeaders().add("Access-Control-Allow-Credentials","true");
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("origin"));
        response.setHeader("Access-Control-Allow-Credentials", "true");
        return model;
    }

    //社会人员抢单
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

        try{
            boolean flag = adMonitorTaskService.grabTask(user.getId(),taskId);
            //flag 即表示任务接取成功或失败，直接赋予 result
            result.setResult(flag);
        }catch (Exception e){
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

    //广告主首页简单报表
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

        try {
            InputStream is = request.getInputStream();
            Gson gson = new Gson();
            JsonObject obj = gson.fromJson(new InputStreamReader(is), JsonObject.class);
            token = obj.get("token") == null ? null : obj.get("token").getAsString();
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
//        Integer operateId = user.getOperateId(); //ad_activity的user_id
        SearchDataVo vo = new SearchDataVo(null, null, (page-1)*pageSize, pageSize);
        vo.putSearchParam("userId", null, sysUserVo.getId());
        
        //分页查询广告商的活动信息
        List<CustomerActivityReport> reports = new ArrayList<>();
        adActivityService.selectReportPageData(vo);
        //设置总页数
        int totalCount = (int) ((vo.getCount() + pageSize - 1) / pageSize);
        
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

        model.addAttribute(SysConst.RESULT_KEY, result);
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("origin"));
        response.setHeader("Access-Control-Allow-Credentials", "true");
        return model;
    }
    
    //社会人员抢单放弃任务
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
        
        if(monitorTaskId == null){
            result.setCode(ResultCode.RESULT_FAILURE.getCode());
            result.setResultDes("参数有误！");
            model.addAttribute(SysConst.RESULT_KEY, result);
            return model;
        }
        
        //[1] 获取该条任务信息
        AdMonitorTask adMonitorTask = adMonitorTaskService.selectByPrimaryKey(monitorTaskId);
        if(adMonitorTask.getStatus() == 2 || adMonitorTask.getStatus() == 6) {
        	//[2] 判断24+12小时的逻辑, 将状态改成 1：待指派 或 8：可抢单
            /**
             * monitor_date + monitor_last_days - 2天 + 12小时 < now
             * 可推出当 monitor_date < now - monitor_last_days + 2天 - 12小时 时改为1：可指派，否则为8：可抢单
             */
            AbandonTaskVo vo = new AbandonTaskVo();
            vo.setId(monitorTaskId);
            vo.setAbandonTime(now);
            vo.setUpdateTime(now);
            vo.setUserTaskStatus(2); //1.正常 2.主动放弃 3.超时回收
            
            Date monitorDate = adMonitorTask.getMonitorDate();
            Integer monitorLastDays = adMonitorTask.getMonitorLastDays();
            if(monitorDate.getTime() <= now.getTime() - monitorLastDays*24*60*60*1000 + 2*24*60*60*1000 - 12*60*60*1000) {
            	vo.setTaskStatus(1); //1：可指派
            } else {
            	vo.setTaskStatus(8); //8：可抢单
            }
            
            //[3] 联表更新
            adMonitorTaskService.abandonUserTask(vo);
        }
        
        model.addAttribute(SysConst.RESULT_KEY, result);
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("origin"));
        response.setHeader("Access-Control-Allow-Credentials", "true");
        return model;
    }
    
    //广告主首页简单报表 点击后查看 详细报表信息
    @RequestMapping(value = "/activityDetailReport")
    @ResponseBody
    public Model activityDetailReport(Model model, HttpServletRequest request, HttpServletResponse response) {
        ResultVo result = new ResultVo();
        result.setCode(ResultCode.RESULT_SUCCESS.getCode());
        result.setResultDes("获取成功");
        model = new ExtendedModelMap();

        Integer activityId = null;
        String token = null;
        Date now = new Date();

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

            //获取登录的当前用户
//            SysUserExecute user = getLoginUser(request, token);
            //查询媒体类型包括媒体大类媒体小类
    		List<AdMediaType> allAdMediaType = adMediaTypeService.getAll();
    		Map<Integer, String> mediaTypeMap = new HashMap<>();
    		for (AdMediaType adMediaType : allAdMediaType) {
    			mediaTypeMap.put(adMediaType.getId(), adMediaType.getName());
    		}
    		
    		//导出相关
    		AppDetailReports appDetailReports = new AppDetailReports();
            List<AppDetailReport> notStartReport = new ArrayList<>();
        	List<AppDetailReport> monitorReport = new ArrayList<>();
        	List<AppDetailReport> problemReport = new ArrayList<>();
            List<AdActivityAdseatTaskVo> vos = adActivityService.selectAdActivityAdseatTask(activityId);
        	for (AdActivityAdseatTaskVo vo : vos) {
				AppDetailReport report = new AppDetailReport();
				report.setSeatInfoName(vo.getInfo_name()); //广告位名称
				report.setMediaName(vo.getMediaName()); //媒体名称
				report.setMediaTypeParentName(mediaTypeMap.get(vo.getInfo_mediaTypeParentId())); //媒体大类
				report.setMediaTypeName(mediaTypeMap.get(vo.getInfo_mediaTypeId())); //媒体小类
				report.setProvince(cityCache.getCityName(vo.getInfo_province())); //省
				report.setCity(cityCache.getCityName(vo.getInfo_city())); //市
				report.setRegion(cityCache.getCityName(vo.getInfo_region())); //区（县）
				report.setStreet(cityCache.getCityName(vo.getInfo_street())); //街道（镇，乡）
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
				for (PictureVo pictureVo : pictureVos) {
					if(pictureVo.getDate() != null) {
						//代表有人做过该任务
						pictureVo.setTime(DateUtil.dateFormate(pictureVo.getDate(), "yyyy-MM-dd HH:mm:ss"));
						if(StringUtil.equals(pictureVo.getTaskType(), "1")) {
							pictureVo.setTaskType("上刊监测");
							upPics.add(pictureVo);
						} else if(StringUtil.equals(pictureVo.getTaskType(), "2")) {
							pictureVo.setTaskType("投放期间监测");
							durationPics.add(pictureVo);
						} else if(StringUtil.equals(pictureVo.getTaskType(), "3")) {
							pictureVo.setTaskType("下刊监测");
							downPics.add(pictureVo);
						}
					}
				}
				if(upPics.size() > 0) {
					report.setUpPics(upPics);
				} else {
					report.setUpPics(null);
				}
				if(durationPics.size() > 0) {
					report.setDurationPics(durationPics);
				} else {
					report.setDurationPics(null);
				}
				if(downPics.size() > 0) {
					report.setDownPics(downPics);
				} else {
					report.setDownPics(null);
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
        } catch (IOException e) {
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
}
