package com.bt.om.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.bt.om.common.web.PageConst;
import com.bt.om.entity.SysUser;
import com.bt.om.enums.SessionKey;
import com.bt.om.security.ShiroUtils;
import com.bt.om.service.ILoginLogService;
import com.bt.om.vo.web.SearchDataVo;
import com.bt.om.web.util.SearchUtil;

import cn.jiguang.common.connection.IHttpClient.RequestMethod;
@Controller
@RequestMapping("/root")
public class LoginLogController {

	@Autowired
	private ILoginLogService loginLogService;
    /**
     * 登录日志展示
     */
    @RequiresRoles("superadmin")
    @RequestMapping(value = "/loginLog" )
    public String resourceDetailPage(Model model, HttpServletRequest request,
    		@RequestParam(value = "createTime", required = false) String createTime,
    		@RequestParam(value = "type", required = false) String type,
    		@RequestParam(value = "realname", required = false) String realname,
    		@RequestParam(value = "username", required = false) String username){
	   //	SysUser user = (SysUser) ShiroUtils.getSessionAttribute(SessionKey.SESSION_LOGIN_USER.toString());
        SearchDataVo vo = SearchUtil.getVo();
        if (createTime != null) {
            vo.putSearchParam("createTime", createTime.toString(), createTime);
        }
        if (type != null) {
            vo.putSearchParam("type", type.toString(), type);
        } else {
        	vo.putSearchParam("type", "0", "0");
        }
        if (realname != null) {
            vo.putSearchParam("realname", realname.toString(), realname);
        }
        if (username != null) {
            vo.putSearchParam("username", username.toString(), username);
        }
    
		loginLogService.getPageData(vo);
        SearchUtil.putToModel(model, vo);
        return PageConst.LOGIN_LOG_PAGE;
    }
}
