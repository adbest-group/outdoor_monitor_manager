package com.bt.om.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bt.om.common.SysConst;
import com.bt.om.common.web.PageConst;
import com.bt.om.entity.SysUser;
import com.bt.om.enums.ResultCode;
import com.bt.om.enums.UserRoleEnum;
import com.bt.om.enums.UserTypeEnum;
import com.bt.om.service.ISysUserService;
import com.bt.om.vo.web.ResultVo;
import com.bt.om.vo.web.SearchDataVo;
import com.bt.om.web.util.SearchUtil;

/**
 * 第三方监测公司 相关控制类
 */
@Controller
@RequestMapping("/thirdCompany")
public class ThirdCompanyController {
	@Autowired
	private ISysUserService sysUserService;
	
	/**
	 * 第三方监测公司 查询
	 */
    @RequiresRoles(value={"superadmin"}, logical = Logical.OR)
    @RequestMapping(value="/list")
    public String phoneoperatorList(Model model ,HttpServletRequest request,
    		@RequestParam(value = "nameOrUsername", required = false) String nameOrUsername) {
    	 SearchDataVo vo = SearchUtil.getVo();
         // 名称或登录账号
         if (StringUtils.isNotBlank(nameOrUsername)) {
             vo.putSearchParam("nameOrUsername", nameOrUsername, "%" + nameOrUsername + "%");
         }
         vo.putSearchParam("usertype", "usertype", UserTypeEnum.THIRD_COMPANY.getId());
         sysUserService.getPageData(vo);
         SearchUtil.putToModel(model, vo);

    	return PageConst.THIRD_COMPANY_LIST;
    }
    
    /**
     * 前往 编辑第三方监测公司
     */
    @RequestMapping(value = "/toEdit")
    public String AddPhoneoperator(Model model,HttpServletRequest request,
    		@RequestParam(value = "id", required = false) Integer id) {
    	if (id != null) {
    		SysUser sysUser = sysUserService.findUserinfoById(id);
    		model.addAttribute("obj" ,sysUser);
    	}
        return PageConst.THIRD_COMPANY_EDIT;
    }
    
    /**
     * 新增/修改第三方监测公司
     */
    @RequestMapping(value="/save")
    @RequiresRoles("superadmin")
    @ResponseBody
    public Model savePhoneoperator(Model model, SysUser sysUser, HttpServletRequest request,
    		@RequestParam(value = "telephone") String telephone) {
    	ResultVo<String> result = new ResultVo<String>();
        result.setCode(ResultCode.RESULT_SUCCESS.getCode());
        result.setResultDes("保存成功");
        model = new ExtendedModelMap();
        
        if(sysUser.getStatus() == null) {
        	sysUser.setStatus(1);	//可用
        }
        if(sysUser.getPlatform() == null) {
        	sysUser.setPlatform(1); 
        }
        if(sysUser.getUsertype() == null) {
        	sysUser.setUsertype(UserTypeEnum.THIRD_COMPANY.getId()); //7：第三方监测公司
        }
        sysUser.setMobile(telephone);
        try {
        	if(sysUser.getId() != null) {
        		sysUserService.modify(sysUser);
        	} else {
        		sysUserService.addUser(sysUser, UserRoleEnum.THIRD_COMPANY.getId());
        	}
        } catch (Exception e) {
            result.setCode(ResultCode.RESULT_FAILURE.getCode());
            result.setResultDes("保存失败！");
            model.addAttribute(SysConst.RESULT_KEY, result);
            return model;
        }
        model.addAttribute(SysConst.RESULT_KEY, result);
		return model;
    }
    
}
