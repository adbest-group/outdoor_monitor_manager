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
import com.bt.om.entity.SysRole;
import com.bt.om.entity.SysUser;
import com.bt.om.entity.vo.SysUserVo;
import com.bt.om.enums.ResultCode;
import com.bt.om.service.IMediaService;
import com.bt.om.service.ISysUserService;
import com.bt.om.vo.web.ResultVo;
import com.bt.om.vo.web.SearchDataVo;
import com.bt.om.web.util.SearchUtil;

/**
 * Created by caiting on 2018/2/27.
 */
@Controller
@RequestMapping(value = "/media")
public class MediaController {

    @Autowired
    private ISysUserService sysUserService;
    @Autowired
    private IMediaService mediaService;

    /**
     * 媒体管理列表
     **/
    @RequiresRoles("superadmin")
    @RequestMapping(value = "/list")
    public String getList(Model model, HttpServletRequest request,
                          @RequestParam(value = "name", required = false) String name) {
        SearchDataVo vo = SearchUtil.getVo();

        vo.putSearchParam("usertype", null, 3);
        // 名称或登录账号
        if (StringUtils.isNotBlank(name)) {
            vo.putSearchParam("nameOrUsername", name, "%" + name + "%");
        }

        sysUserService.getPageData(vo);

        SearchUtil.putToModel(model, vo);

        return PageConst.MEDIA_LIST;
    }

    /**
     * 媒体编辑
     **/
    @RequiresRoles("superadmin")
    @RequestMapping(value = "/edit")
    public String toEdit(Model model, HttpServletRequest request,
                         @RequestParam(value = "id", required = false) Integer id) {

        if (id != null) {
            SysUserVo user = sysUserService.findUserinfoById(id);
            if (user != null) {
                model.addAttribute("obj", user);
            }
        }

        return PageConst.MEDIA_EDIT;
    }

    /**
     * 检查是否重名
     **/
    @RequestMapping(value = {"isExistsAccountName"}, method = {RequestMethod.POST})
    @ResponseBody
    public Model isExistsAccountName(Model model,
                                     @RequestParam(value = "username", required = true) String username) {

        ResultVo<List<SysUser>> resultVo = new ResultVo<List<SysUser>>();
        try {
            List<SysUserVo> userList = sysUserService.isExistsName(username);
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
     * 检查是否前缀prefix重复
     **/
    @RequestMapping(value = {"isExistsPrefix"}, method = {RequestMethod.POST})
    @ResponseBody
    public Model isExistsPrefix(Model model,
                                     @RequestParam(value = "prefix", required = true) String prefix,
                                @RequestParam(value = "id", required = true) Integer id) {

        ResultVo<List<SysUser>> resultVo = new ResultVo<List<SysUser>>();
        try {
            boolean isExists = sysUserService.isExistsPrefix(prefix, id);
            if (isExists) {
                resultVo.setCode(ResultCode.RESULT_FAILURE.getCode());
                resultVo.setResultDes("已存在该前缀，请修改");
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
     * 保存媒体
     **/
    @RequiresRoles("superadmin")
    @RequestMapping(value = {"/save"}, method = {RequestMethod.POST})
    @ResponseBody
    public Model save(Model model,
                      @RequestParam(value = "id", required = true) Integer id,
                      @RequestParam(value = "username", required = true) String username,
                      @RequestParam(value = "password", required = true) String password,
                      @RequestParam(value = "name", required = true) String name,
                      @RequestParam(value = "telephone", required = true) String telephone,
                      @RequestParam(value = "prefix", required = true) String prefix) {

        ResultVo<List<SysUser>> resultVo = new ResultVo<List<SysUser>>();
        try {
            if (id == null) {//新增
                SysUserVo user = new SysUserVo();
                user.setUsername(username);
                user.setPassword(new Md5Hash(password, username).toString());
                user.setRealname(name);
                user.setTelephone(telephone);
                user.setPlatform(1);
                user.setUsertype(3);
                user.setStatus(1);
                user.setPrefix(prefix);
                mediaService.add(user);
            } else {//修改
                SysUserVo user = new SysUserVo();
                user.setId(id);
                user.setUsername(username);
                if (!"******".equals(password)) {
                    user.setPassword(new Md5Hash(password, username).toString());
                }
                user.setPrefix(prefix);
                user.setRealname(name);
                user.setTelephone(telephone);
                mediaService.modify(user);
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

        ResultVo<List<SysRole>> resultVo = new ResultVo<List<SysRole>>();
        try {
            SysUserVo user = new SysUserVo();
            user.setId(id);
            user.setStatus(status);
            int updCount = sysUserService.update(user);
            if (updCount == 1) {
                resultVo.setCode(ResultCode.RESULT_SUCCESS.getCode());
            } else {
                resultVo.setCode(ResultCode.RESULT_FAILURE.getCode());
                resultVo.setResultDes("操作失败，请稍后再试");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            resultVo.setCode(ResultCode.RESULT_FAILURE.getCode());
            resultVo.setResultDes("服务忙，请稍后再试");
        }

        model.addAttribute(SysConst.RESULT_KEY, resultVo);
        return model;
    }
}
