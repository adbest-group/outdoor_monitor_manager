package com.bt.om.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import com.bt.om.entity.vo.SysUserVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import com.bt.om.entity.vo.SysUserVo;
import com.bt.om.enums.SessionKey;
import com.bt.om.security.ShiroUtils;
import com.bt.om.util.RequestUtil;
import com.bt.om.web.support.IntegerPropertiesSupport;
import com.bt.om.web.support.LongPropertiesSupport;
import com.bt.om.web.support.StringPropertiesSupport;
import com.bt.om.web.util.ServletAttributes;

/**
 * 基础Controller
 * 
 * @author hl-tanyong
 * @version $Id: BasicController.java, v 0.1 2015年9月30日 上午9:07:56 hl-tanyong Exp
 *          $
 */
public abstract class BasicController {

	protected Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * 文本直接输出
	 * 
	 * @param s
	 * @param response
	 */
	protected void out(String s) {
		try {
			PrintWriter out = ServletAttributes.getResponse().getWriter();
			out.print(s);
			out.close();
		} catch (Exception e) {
			logger.info(e.getMessage());
		}
	}

	/**
	 * 判断请求是否为ajax
	 * 
	 * @param request
	 * @return
	 */
	protected boolean isAjax() {
		return RequestUtil.isAjax(ServletAttributes.getRequest());
	}

	/**
	 * 判断是否为爬虫
	 * 
	 * @param request
	 * @return
	 */
	public boolean isRobot() {
		return RequestUtil.isRobot(ServletAttributes.getRequest());
	}

	/**
	 * 判断是否为手机访问
	 * 
	 * @param request
	 * @return
	 */
	protected boolean isMobile() {
		return RequestUtil.isMobile(ServletAttributes.getRequest());
	}

	/**
	 * 判断是否为ipad访问
	 * 
	 * @param request
	 * @return
	 */
	protected boolean isIpad() {
		return RequestUtil.isIpad(ServletAttributes.getRequest());
	}

	/**
	 * 是否为UC浏览器
	 * 
	 * @param request
	 * @return
	 */
	public boolean checkUCJisu() {
		return RequestUtil.checkUCJisu(ServletAttributes.getRequest());
	}

	/**
	 * 302跳转
	 * 
	 * @param response
	 * @param redirectUrl
	 */
	protected void send302(HttpServletResponse response, String redirectUrl) {
		RequestUtil.send302(response, redirectUrl);
	}

	/**
	 * 301跳转
	 * 
	 * @param response
	 * @param redirectUrl
	 */
	protected void send301(HttpServletResponse response, String redirectUrl) {
		RequestUtil.send301(response, redirectUrl);
	}

	/**
	 * 修改response的状态码
	 * 
	 * @param response
	 * @param code
	 */
	protected void sendError(HttpServletResponse response, int code) {
		try {
			response.sendError(code);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 得到来源
	 * 
	 * @param request
	 * @return
	 */
	protected String getReferer() {
		return ServletAttributes.getRequest().getHeader("Referer");
	}

	/**
	 * 得到来源
	 * 
	 * @param request
	 * @return
	 */
	protected String getIp() {
		return RequestUtil.getRealIp(ServletAttributes.getRequest());
	}

	@InitBinder
	// 必须有一个参数WebDataBinder
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd"), true));
		binder.registerCustomEditor(int.class, new IntegerPropertiesSupport());
		binder.registerCustomEditor(Integer.class, new IntegerPropertiesSupport());
		binder.registerCustomEditor(Long.class, new LongPropertiesSupport());
		binder.registerCustomEditor(String.class, new StringPropertiesSupport());
	}

    protected SysUserVo getLoginUser() {
        return (SysUserVo) ShiroUtils.getSessionAttribute(SessionKey.SESSION_LOGIN_USER.toString());
    }

}
