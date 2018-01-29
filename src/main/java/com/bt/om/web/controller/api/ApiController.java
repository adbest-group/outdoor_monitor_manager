package com.bt.om.web.controller.api;

import com.alibaba.fastjson.util.Base64;
import com.bt.om.common.SysConst;
import com.bt.om.entity.*;
import com.bt.om.entity.vo.ActivityMobileReportVo;
import com.bt.om.entity.vo.AdActivityAdseatVo;
import com.bt.om.entity.vo.AdJiucuoTaskMobileVo;
import com.bt.om.entity.vo.AdMonitorTaskMobileVo;
import com.bt.om.enums.JiucuoTaskStatus;
import com.bt.om.enums.MonitorTaskStatus;
import com.bt.om.enums.ResultCode;
import com.bt.om.enums.SessionKey;
import com.bt.om.security.ShiroUtils;
import com.bt.om.service.*;
import com.bt.om.util.QRcodeUtil;
import com.bt.om.vo.api.*;
import com.bt.om.vo.web.ResultVo;
import com.bt.om.web.BasicController;
import com.bt.om.web.util.UploadFileUtil;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import com.google.zxing.ReaderException;
import com.google.zxing.Result;
import org.apache.commons.lang.StringUtils;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpProcessor;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Decoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.List;
import java.util.UUID;

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
            is = new ByteArrayInputStream(java.util.Base64.getDecoder().decode(file));

            String path = request.getRealPath("/");
            path = path + (path.endsWith(File.separator) ? "" : File.separatorChar) + "static" + File.separatorChar + "upload" + File.separatorChar;
            String imageName = "qrcode.jpg";
            UploadFileUtil.saveFile(path, imageName, is);

//            Result res = QRcodeUtil.readQRCodeResult(is);
//            result.setResult(res.getText());
            result.setResult(new QRCodeInfoVo((AdActivityAdseatVo) adActivityService.getActivitySeatById(6)));

//        } catch (IOException e) {
//            e.printStackTrace();
//            result.setCode(ResultCode.RESULT_FAILURE.getCode());
//            result.setResultDes("二维码解析失败失败！");
//            model.addAttribute(SysConst.RESULT_KEY, result);
//            return model;
//        } catch (ReaderException e) {
//            e.printStackTrace();
//            result.setCode(ResultCode.RESULT_FAILURE.getCode());
//            result.setResultDes("二维码解析失败失败！");
//            model.addAttribute(SysConst.RESULT_KEY, result);
//            return model;
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

        try {
            InputStream is = request.getInputStream();
            Gson gson = new Gson();
            JsonObject obj = gson.fromJson(new InputStreamReader(is), JsonObject.class);
            username = obj.get("username").getAsString();
            password = obj.get("password").getAsString();
            vcode = obj.get("vcode").getAsString();
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
        HttpSession session = request.getSession();
        String sessionCode = session.getAttribute(SessionKey.SESSION_CODE.toString()) == null ? ""
                : session.getAttribute(SessionKey.SESSION_CODE.toString()).toString();

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

        session.setAttribute(SessionKey.SESSION_LOGIN_USER.toString(), userExecute);

        result.setResult(new SysUserExecuteVo(userExecute));
        model.addAttribute(SysConst.RESULT_KEY, result);
//        response.getHeaders().add("Access-Control-Allow-Credentials","true");
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

        //验证登录
        if (!checkLogin(model, result, request)) {
            return model;
        }

        Integer type = null;

        try {
            InputStream is = request.getInputStream();
            Gson gson = new Gson();
            JsonObject obj = gson.fromJson(new InputStreamReader(is), JsonObject.class);
            type = obj.get("type").getAsInt();
        } catch (IOException e) {
            result.setCode(ResultCode.RESULT_FAILURE.getCode());
            result.setResultDes("系统繁忙，请稍后再试！");
            model.addAttribute(SysConst.RESULT_KEY, result);
            return model;
        }
        HttpSession session = request.getSession();
        SysUserExecute user = (SysUserExecute) session.getAttribute(SessionKey.SESSION_LOGIN_USER.toString());
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
                          @RequestParam(value = "type", required = false) Integer type,
                          @RequestParam(value = "task_id", required = false) Integer taskId,
                          @RequestParam(value = "lon", required = false) Double lon,
                          @RequestParam(value = "lat", required = false) Double lat,
                          @RequestParam(value = "ad_activity_seat_id", required = false) Integer adActivitySeatId,
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

        //验证登录
        if (!checkLogin(model, result, request)) {
            return model;
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
                    adMonitorTaskService.feedback(taskId, feedback);
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
            HttpSession session = request.getSession();
            SysUserExecute user = (SysUserExecute) session.getAttribute(SessionKey.SESSION_LOGIN_USER.toString());

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
                                @RequestParam(value = "type", required = false) Integer type,
                                @RequestParam(value = "task_id", required = false) Integer taskId,
                                @RequestParam(value = "lon", required = false) Double lon,
                                @RequestParam(value = "lat", required = false) Double lat,
                                @RequestParam(value = "ad_activity_seat_id", required = false) Integer adActivitySeatId,
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

        //验证登录
        if (!checkLogin(model, result, request)) {
            return model;
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
                file1 = file1.replaceAll("data:image/jpeg;base64,", "");
                is1 = new ByteArrayInputStream(java.util.Base64.getDecoder().decode(file1));
                filename1 = UploadFileUtil.saveFile(path, "image.jpg", is1);
                file2 = file2.replaceAll("data:image/jpeg;base64,", "");
                is2 = new ByteArrayInputStream(java.util.Base64.getDecoder().decode(file2));
                filename2 = UploadFileUtil.saveFile(path, "image.jpg", is2);
                file3 = file3.replaceAll("data:image/jpeg;base64,", "");
                is3 = new ByteArrayInputStream(java.util.Base64.getDecoder().decode(file3));
                filename3 = UploadFileUtil.saveFile(path, "image.jpg", is3);
                file4 = file4.replaceAll("data:image/jpeg;base64,", "");
                is4 = new ByteArrayInputStream(java.util.Base64.getDecoder().decode(file4));
                filename4 = UploadFileUtil.saveFile(path, "image.jpg", is4);
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
                    adMonitorTaskService.feedback(taskId, feedback);
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
            HttpSession session = request.getSession();
            SysUserExecute user = (SysUserExecute) session.getAttribute(SessionKey.SESSION_LOGIN_USER.toString());

            try {

                file1 = file1.replaceAll("data:image/jpeg;base64,", "");
                is1 = new ByteArrayInputStream(java.util.Base64.getDecoder().decode(file1));
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

        //验证登录
        if (!checkLogin(model, result, request)) {
            return model;
        }
        HttpSession session = request.getSession();
        SysUserExecute user = (SysUserExecute) session.getAttribute(SessionKey.SESSION_LOGIN_USER.toString());

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

    //请求资讯列表
    @RequestMapping(value = "/getreward")
    @ResponseBody
    public Model getreward(Model model, HttpServletRequest request, HttpServletResponse response) {
        ResultVo result = new ResultVo();
        result.setCode(ResultCode.RESULT_SUCCESS.getCode());
        result.setResultDes("获取成功");
        model = new ExtendedModelMap();

        //验证登录
        if (!checkLogin(model, result, request)) {
            return model;
        }
        HttpSession session = request.getSession();
        SysUserExecute user = (SysUserExecute) session.getAttribute(SessionKey.SESSION_LOGIN_USER.toString());

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

    public static void main(String[] args) {

    }
}
