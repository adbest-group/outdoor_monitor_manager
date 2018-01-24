package com.bt.om.web.controller.api;

import com.bt.om.common.SysConst;
import com.bt.om.entity.AdActivity;
import com.bt.om.entity.SysUser;
import com.bt.om.entity.SysUserExecute;
import com.bt.om.enums.ResultCode;
import com.bt.om.enums.SessionKey;
import com.bt.om.security.ShiroUtils;
import com.bt.om.service.IAdActivityService;
import com.bt.om.service.ISysUserExecuteService;
import com.bt.om.util.QRcodeUtil;
import com.bt.om.vo.api.SysUserExecuteVo;
import com.bt.om.vo.web.ResultVo;
import com.bt.om.web.BasicController;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import com.google.zxing.ReaderException;
import com.google.zxing.Result;
import org.apache.commons.lang.StringUtils;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;

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

    //测试用
    @RequestMapping(value="/aaa/bbb/aaa")
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


    //解析上传的二维码照片
    @RequestMapping(value="/qrcodeanalysis")
    @ResponseBody
    public Model decodeQR(Model model, HttpServletRequest request,
                          @RequestParam(value = "pic", required = false) MultipartFile file) {
        ResultVo<String> result = new ResultVo<>();
        result.setCode(ResultCode.RESULT_SUCCESS.getCode());
        result.setResultDes("解析成功");
        result.setResult("");
        model = new ExtendedModelMap();

        InputStream is = null;

        try {
            is = file.getInputStream();


//            String path = request.getRealPath("/");
//            path = path + (path.endsWith(File.separator)?"":File.separatorChar)+"static"+File.separatorChar+"upload"+File.separatorChar;
//            String imageName = file.getOriginalFilename();
//            saveFile(path,imageName,is);

            Result res = QRcodeUtil.readQRCodeResult(is);
            result.setResult(res.getText());
        } catch (IOException e) {
            e.printStackTrace();
            result.setCode(ResultCode.RESULT_FAILURE.getCode());
            result.setResultDes("二维码解析失败失败！");
            model.addAttribute(SysConst.RESULT_KEY, result);
            return model;
        } catch (ReaderException e) {
            e.printStackTrace();
            result.setCode(ResultCode.RESULT_FAILURE.getCode());
            result.setResultDes("二维码解析失败失败！");
            model.addAttribute(SysConst.RESULT_KEY, result);
            return model;
        }finally {
            if (is!=null){
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
    @RequestMapping(value="/login",method = RequestMethod.POST)
    @ResponseBody
    public Model login(Model model, HttpServletRequest request, HttpServletResponse response,
                         @RequestParam(value = "username", required = false) String username,
                       @RequestParam(value = "password", required = false) String password,
                       @RequestParam(value = "vcode", required = false) String vcode) {
        ResultVo<SysUserExecuteVo> result = new ResultVo<>();
        result.setCode(ResultCode.RESULT_SUCCESS.getCode());
        result.setResultDes("登录成功");
        model = new ExtendedModelMap();

        try {
            InputStream is = request.getInputStream();
            Gson gson = new Gson();
            JsonObject obj = gson.fromJson(new InputStreamReader(is), JsonObject.class);
            username = obj.get("username").getAsString();
            password = obj.get("password").getAsString();
            vcode = obj.get("vcode").getAsString();
        } catch (IOException e) {
            e.printStackTrace();
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
System.out.println(session.getId()+"|"+vcode+"|"+sessionCode);
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
        if(userExecute == null || !md5Pwd.equals(userExecute.getPassword())){
            result.setCode(ResultCode.RESULT_FAILURE.getCode());
            result.setResultDes("用户名或密码有误！");
            model.addAttribute(SysConst.RESULT_KEY, result);
            return model;
        }

        session.setAttribute(SessionKey.SESSION_LOGIN_USER.toString(),userExecute);

        result.setResult(new SysUserExecuteVo(userExecute));
        model.addAttribute(SysConst.RESULT_KEY, result);
//        response.getHeaders().add("Access-Control-Allow-Credentials","true");
        response.setHeader("Access-Control-Allow-Origin",request.getHeader("origin"));
        response.setHeader("Access-Control-Allow-Credentials","true");
        return model;
    }

//    //保存在本服务器
//    private String saveFile(String path,String filename,InputStream is){
//        String ext = filename.substring(filename.lastIndexOf("."));
//        filename = UUID.randomUUID().toString().toLowerCase()+"."+ext.toLowerCase();
//        FileOutputStream fos = null;
//        try {
//            fos = new FileOutputStream(path+filename);
//            int len = 0;
//            byte[] buff = new byte[1024];
//            while((len=is.read(buff))>0){
//                fos.write(buff);
//            }
//            return "/static/upload/"+filename;
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }finally {
//            if(fos!=null){
//                try {
//                    fos.flush();
//                    fos.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//            if(is!=null){
//                try {
//                    is.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//        return "error";
//    }
}
