package com.bt.om.web.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bt.om.common.SysConst;
import com.bt.om.common.web.PageConst;
import com.bt.om.entity.AdMedia;
import com.bt.om.entity.SysUser;
import com.bt.om.entity.SysUserExecute;
import com.bt.om.entity.vo.SysUserExecuteVo;
import com.bt.om.enums.ResultCode;
import com.bt.om.enums.SessionKey;
import com.bt.om.enums.UserExecuteType;
import com.bt.om.security.ShiroUtils;
import com.bt.om.service.IMediaService;
import com.bt.om.service.ISysUserExecuteService;
import com.bt.om.vo.web.ResultVo;
import com.bt.om.vo.web.SearchDataVo;
import com.bt.om.web.BasicController;
import com.bt.om.web.util.SearchUtil;

/**
 * Created by caiting on 2018/4/10.
 */
@Controller
@RequestMapping("/appAccount")
public class AppAccountController extends BasicController {
    @Autowired
    private ISysUserExecuteService sysUserExecuteService;
    @Autowired
    private IMediaService mediaService;

    /**
     * 媒体安装人员管理列表
     **/
    @RequiresRoles({"superadmin"})
    @RequestMapping(value = "/list")
    public String getWorkerList(Model model, HttpServletRequest request,
                                @RequestParam(value = "nameOrUsername", required = false) String name,
                                @RequestParam(value = "searchUserType", required = false) String usertype) {
        SearchDataVo vo = SearchUtil.getVo();

        //vo.putSearchParam("usertypes", null, new Integer[]{UserExecuteType.CUSTOMER.getId(), UserExecuteType.MONITOR.getId(),UserExecuteType.MEDIA_WORKER.getId(),UserExecuteType.Social.getId()});
        SysUser user = (SysUser) ShiroUtils.getSessionAttribute(SessionKey.SESSION_LOGIN_USER.toString());
//        vo.putSearchParam("operateId",null,user.getId());
        // 名称或登录账号
        if (StringUtils.isNotBlank(name)) {
            vo.putSearchParam("nameOrUsername", name, "%" + name + "%");
        }
        if (StringUtils.isNotBlank(usertype)) {
        	vo.putSearchParam("usertype", usertype, usertype);
        }
        
        sysUserExecuteService.getPageData(vo);

        SearchUtil.putToModel(model, vo);

        return PageConst.APP_ACCOUNT_LIST;
    }

    /**
     * 媒体安装工人编辑
     **/
    @RequiresRoles("superadmin")
    @RequestMapping(value = "/edit")
    public String toEditWorker(Model model, HttpServletRequest request,
                               @RequestParam(value = "id", required = false) Integer id) {
        if (id != null) {
        	SysUserExecuteVo user = sysUserExecuteService.selectByIdAndMedia(id);
            if (user != null) {
                model.addAttribute("obj", user);
                //如果是媒体安装人员，传所属媒体id
                if(user.getUsertype()==UserExecuteType.MEDIA_WORKER.getId()){
                    AdMedia media = mediaService.getMediaByUserId(user.getOperateId());
                    if(media!=null) {
                        model.addAttribute("mediaId", media.getId());
                    }
                }
            }
        }

        return PageConst.APP_ACCOUNT_EDIT;
    }

    /**
     * 检查媒体工人账号是否重名
     **/
    @RequestMapping(value = {"/isExistsAccountName"}, method = {RequestMethod.POST})
    @ResponseBody
    public Model isExistsAccountName(Model model,
                                     @RequestParam(value = "username", required = true) String username,
                                     @RequestParam(value = "id", required = true) Integer id) {

        ResultVo<List<SysUser>> resultVo = new ResultVo<List<SysUser>>();
        try {
            List<SysUserExecute> userList = sysUserExecuteService.isExistsName(username,id);
            if (userList != null && userList.size() > 0) {
                resultVo.setCode(ResultCode.RESULT_FAILURE.getCode());
                resultVo.setResultDes("已存在该登录账户，请修改");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            resultVo.setCode(ResultCode.RESULT_FAILURE.getCode());
            resultVo.setResultDes("服务忙，请稍后再试");
        }

        model.addAttribute(SysConst.RESULT_KEY, resultVo);
        return model;
    }

    /**
     * 保存工人
     **/
    @RequiresRoles("superadmin")
    @RequestMapping(value = {"/save"}, method = {RequestMethod.POST})
    @ResponseBody
    public Model save(Model model,
                      @RequestParam(value = "id", required = true) Integer id,
                      @RequestParam(value = "username", required = true) String username,
                      @RequestParam(value = "password", required = true) String password,
                      @RequestParam(value = "name", required = true) String name,
                      @RequestParam(value = "usertype", required = true) Integer usertype,
                      @RequestParam(value = "mediaId", required = true) Integer mediaId) {

        ResultVo resultVo = new ResultVo();
        SysUser loginuser = (SysUser) ShiroUtils.getSessionAttribute(SessionKey.SESSION_LOGIN_USER.toString());
        try {
            if (id == null) {//新增
                SysUserExecute user = new SysUserExecute();
                user.setUsername(username);
                user.setPassword(new Md5Hash(password, username).toString());
                user.setRealname(name);
                user.setMobile(username);
                user.setUsertype(usertype);
                user.setStatus(1);
                user.setCompany(loginuser.getRealname());
                if(usertype==3){
                    AdMedia media = mediaService.getById(mediaId);
                    user.setOperateId(media.getUserId());
                }else if(usertype!=4){
                    user.setOperateId(loginuser.getId());
                }
                sysUserExecuteService.add(user);
            } else {//修改
                SysUserExecute user = sysUserExecuteService.getById(id);
//                user.setId(id);
//                user.setUsername(username);
                if (!"******".equals(password)) {
                    user.setPassword(new Md5Hash(password, username).toString());
                }
                user.setRealname(name);
                user.setMobile(username);
                user.setUsertype(usertype);
                if(usertype==3){
                    AdMedia media = mediaService.getById(mediaId);
                    user.setOperateId(media.getUserId());
                }else if(usertype==4) {
                	user.setOperateId(null);
                }
//                if(usertype==3){
//                    AdMedia media = mediaService.getById(mediaId);
//                    user.setOperateId(media.getUserId());
//                }else if(usertype!=4){
//                    user.setOperateId(loginuser.getId());
//                }
                sysUserExecuteService.modify(user);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            resultVo.setCode(ResultCode.RESULT_FAILURE.getCode());
            resultVo.setResultDes("服务忙，请稍后再试");
        }

        model.addAttribute(SysConst.RESULT_KEY, resultVo);
        return model;
    }


    @RequiresRoles("superadmin")
    @RequestMapping(value = {"/updateAccountStatus"}, method = {RequestMethod.POST})
    @ResponseBody
    public Model updateAccountStatus(Model model, @RequestParam(value = "id", required = true) Integer id,
                                     @RequestParam(value = "status", required = true) Integer status) {

        ResultVo resultVo = new ResultVo();
        try {
            SysUserExecute user = new SysUserExecute();
            user.setId(id);
            user.setStatus(status);
            sysUserExecuteService.modify(user);
        } catch (Exception ex) {
            ex.printStackTrace();
            resultVo.setCode(ResultCode.RESULT_FAILURE.getCode());
            resultVo.setResultDes("服务忙，请稍后再试");
        }

        model.addAttribute(SysConst.RESULT_KEY, resultVo);
        return model;
    }
}
