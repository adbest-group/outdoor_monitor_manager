package com.bt.om.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.bt.om.common.web.PageConst;
import com.bt.om.service.IOperateLogService;
import com.bt.om.vo.web.SearchDataVo;
import com.bt.om.web.util.SearchUtil;

/**
 * 运营后台操作日志 相关控制类
 * @author jiayong.mao
 */
@Controller
@RequestMapping(value = "/operateLog")
public class OperateLogController {

	@Autowired
	private IOperateLogService operateLogService;
	
	/**
     * 操作日志展示
     */
    @RequiresRoles("superadmin")
    @RequestMapping(value = "/list")
    public String resourceDetailPage(Model model, HttpServletRequest request,
    		@RequestParam(value = "userId", required = false) Integer userId,
    		@RequestParam(value = "usertype", required = false) Integer usertype) {
        SearchDataVo vo = SearchUtil.getVo();

        if (userId != null) {
            vo.putSearchParam("userId", userId.toString(), userId);
        }
        if (usertype != null) {
            vo.putSearchParam("usertype", usertype.toString(), usertype);
        }
        
        operateLogService.getPageData(vo);
        SearchUtil.putToModel(model, vo);

        return PageConst.OPERATE_lOG_LIST;
    }
}
