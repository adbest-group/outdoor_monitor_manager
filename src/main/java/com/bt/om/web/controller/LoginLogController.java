package com.bt.om.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.bt.om.common.web.PageConst;
import com.bt.om.service.ILoginLogService;
import com.bt.om.vo.web.SearchDataVo;
import com.bt.om.web.util.SearchUtil;

/**
 * 登录日志 控制类
 */
@Controller
@RequestMapping("/root")
public class LoginLogController {

	@Autowired
	private ILoginLogService loginLogService;
    /**
     * 登录日志展示
     */
    @RequiresRoles(value = {"superadmin" , "phoneoperator"}, logical = Logical.OR)
    @RequestMapping(value = "/loginLog" )
    public String resourceDetailPage(Model model, HttpServletRequest request,
    		@RequestParam(value = "begin", required = false) String begin,
    		@RequestParam(value = "end", required = false) String end,
    		@RequestParam(value = "type", required = false) String type,
    		@RequestParam(value = "realname", required = false) String realname,
    		@RequestParam(value = "username", required = false) String username){
	   //	SysUser user = (SysUser) ShiroUtils.getSessionAttribute(SessionKey.SESSION_LOGIN_USER.toString());
        SearchDataVo vo = SearchUtil.getVo();
        if (begin != null) {
        	String beginTime = begin + " 00:00:00";
        	model.addAttribute("beginTime", begin);
            vo.putSearchParam("begin", beginTime, beginTime);
        }
        if (end != null) {
        	String endTime = end + " 23:59:59";
        	model.addAttribute("endTime", end);
            vo.putSearchParam("end", endTime, endTime);
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
