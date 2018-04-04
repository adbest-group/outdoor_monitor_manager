package com.bt.om.web.controller.api;

import com.bt.om.cache.AdVersionCache;
import com.bt.om.cache.CityCache;
import com.bt.om.common.SysConst;
import com.bt.om.entity.*;
import com.bt.om.entity.vo.*;
import com.bt.om.enums.*;
import com.bt.om.mapper.SysUserDetailMapper;
import com.bt.om.service.*;
import com.bt.om.util.CityUtil;
import com.bt.om.util.QRcodeUtil;
import com.bt.om.vo.api.*;
import com.bt.om.vo.web.ResultVo;
import com.bt.om.web.BasicController;
import com.bt.om.web.session.SessionByRedis;
import com.bt.om.web.util.UploadFileUtil;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Base64;
import java.util.List;
import java.util.Random;

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


    //获取给定的广告位编号对应的广告位id和相关有效的广告活动
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

        try {
            is = request.getInputStream();
            Gson gson = new Gson();
            JsonObject obj = gson.fromJson(new InputStreamReader(is), JsonObject.class);
            seatCode = obj==null||obj.get("seatCode") == null ? null : obj.get("seatCode").getAsString();
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
            List<AdActivityAdseatVo> list = adActivityService.getActivitySeatBySeatCode(seatCode);
            QRCodeInfoVo qr = new QRCodeInfoVo();
//            qr.setAd_seat_id(Integer.valueOf(seatCode));
            for (AdActivityAdseatVo vo : list) {
                qr.getAd_activity_seats().add(new AdActivitySeatInfoInQRVO(vo));
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

    //登录
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

    //验证码
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
        //任务列表
        if (type == 1) {
            List<AdMonitorTaskMobileVo> tasks = adMonitorTaskService.getByUserIdForMobile(user.getId());
            MonitorTaskListResultVo resultVo = new MonitorTaskListResultVo();
            for (AdMonitorTaskMobileVo task : tasks) {
                if (task.getStatus() == MonitorTaskStatus.TO_CARRY_OUT.getId()) {
                    MonitorTaskWaitToExecutedVo vo = new MonitorTaskWaitToExecutedVo(task);
                    resultVo.getWait_to_executed().add(vo);
                } else if (task.getStatus() == MonitorTaskStatus.UNVERIFY.getId()) {
                    MonitorTaskExecutingVo vo = new MonitorTaskExecutingVo(task);
                    resultVo.getExecuting().add(vo);
                } else if (task.getStatus() == MonitorTaskStatus.VERIFIED.getId() || task.getStatus() == MonitorTaskStatus.VERIFY_FAILURE.getId()) {
                    MonitorTaskCheckedVo vo = new MonitorTaskCheckedVo(task);
                    resultVo.getChecked().add(vo);
                } else if (task.getStatus() == MonitorTaskStatus.UN_FINISHED.getId()) {
                    resultVo.getUn_finished().add(new MonitorTaskUnFinishedVo(task));
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
        
        Integer needForceUpdate = 0; // 0: 不需要更新; 1: 需要强制更新; 2: 存在新版本
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
            if(version == null) {
            	result.setCode(ResultCode.RESULT_SUCCESS.getCode());
                result.setResultDes("不需要强制更新！");
                needForceUpdate = 0;
                result.setResult(needForceUpdate);
                model.addAttribute(SysConst.RESULT_KEY, result);
                return model;
            } else {
            	String[] versionSplit = version.getAppVersion().split("\\.");
            	String[] appVersionSplit = appVersion.split("\\.");
            	for (int i = 0; i < appVersionSplit.length; i++) {
        			Integer versionInt = Integer.parseInt(versionSplit[i]);
        			Integer appVersionInt = Integer.parseInt(appVersionSplit[i]);
        			if(appVersionInt < versionInt) {
        				//需要强制更新
        				needForceUpdate = 1;
        				break;
        			} else if(appVersionInt > versionInt) {
        				//不需要强制更新
        				needForceUpdate = 0;
        				break;
        			}
        		}
            	result.setCode(ResultCode.RESULT_SUCCESS.getCode());
			}
            
            //判断版本号比较, 提示是否需要强制更新
            if(nowVersion == null) {
            	result.setCode(ResultCode.RESULT_SUCCESS.getCode());
                result.setResultDes("不需要强制更新！");
                needForceUpdate = 0;
                result.setResult(needForceUpdate);
                model.addAttribute(SysConst.RESULT_KEY, result);
                return model;
            } else {
            	if(needForceUpdate != 1) {
            		String[] versionSplit = nowVersion.getAppVersion().split("\\.");
                	String[] appVersionSplit = appVersion.split("\\.");
                	for (int i = 0; i < appVersionSplit.length; i++) {
            			Integer versionInt = Integer.parseInt(versionSplit[i]);
            			Integer appVersionInt = Integer.parseInt(appVersionSplit[i]);
            			if(appVersionInt < versionInt) {
            				needForceUpdate = 2;
            				break;
            			}
            		}
            	}
            	result.setCode(ResultCode.RESULT_SUCCESS.getCode());
			}
            
            if(needForceUpdate == 1) {
        		result.setResultDes("需要强制更新！");
        	} else if(needForceUpdate == 0) {
        		result.setResultDes("不需要强制更新！");
        	} else {
        		result.setResultDes("存在新版本！");
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
        System.out.println(new Md5Hash("123456", "media@adbest.com").toString());
    }
}
