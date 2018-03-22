package com.bt.om.web.controller;


import com.bt.om.common.SysConst;
import com.bt.om.common.web.PageConst;
import com.bt.om.entity.SysRole;
import com.bt.om.entity.SysUser;
import com.bt.om.entity.vo.SysRoleVo;
import com.bt.om.entity.vo.SysUserVo;
import com.bt.om.enums.ResultCode;
import com.bt.om.service.ISysRoleService;
import com.bt.om.service.ISysUserService;
import com.bt.om.vo.web.ResultVo;
import com.bt.om.vo.web.SearchDataVo;
import com.bt.om.web.BasicController;
import com.bt.om.web.util.SearchUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 系统管理Controller
 */
@Controller
public class SystemController extends BasicController {

	@Autowired
	private ISysUserService sysUserService;
	@Autowired
	private ISysRoleService sysRoleService;

	@RequestMapping(value = { "/system/account/list" }, method = RequestMethod.GET)
	public String accountList(Model model,
			@RequestParam(value = "nameOrUsername", required = false) String nameOrUsername) {

		SearchDataVo vo = SearchUtil.getVo();
		SysUser sysUser = getLoginUser();
//		vo.putSearchParam("userId", null, sysUser.getParentId());

		// 姓名或登录账号
		if (StringUtils.isNotBlank(nameOrUsername)) {
			vo.putSearchParam("nameOrUsername", nameOrUsername, "%" + nameOrUsername + "%");
		}

		// 如果是yy登录系统，也能查出自己
//		if (sysUser.getParentId().intValue() == 1) {
//			vo.putSearchParam("p", "p", "p");
//		}

		vo.setCount(sysUserService.getPageCount(vo.getSearchMap()));
		if (vo.getCount() > 0) {
			vo.setList(sysUserService.getPageData(vo));
		} else {
			vo.setList(new ArrayList<SysUserVo>());
		}

		SearchUtil.putToModel(model, vo);

		return "/system/account/list";
	}

	// @RequestMapping(value = { "/system/getRoleList" }, method = {
	// RequestMethod.POST })
	// public @ResponseBody Model getRoleList(Model model) {
	//
	// ResultVo<List<SysRoleVo>> resultVo = new ResultVo<List<SysRoleVo>>();
	// try {
	// Integer userId = getLoginUser().getParentId();
	// resultVo.setResult(sysRoleService.findRoleByUserId(userId));
	// } catch (Exception ex) {
	// ex.printStackTrace();
	// resultVo.setCode(ResultCode.RESULT_FAILURE.getCode());
	// resultVo.setResultDes("服务忙，请稍后再试");
	// }
	//
	// model.addAttribute(SysConst.RESULT_KEY, resultVo);
	//
	// return model;
	// }

//	@RequestMapping(value = { "/system/isExistsAccountName" }, method = { RequestMethod.POST })
//	public @ResponseBody Model isExistsAccountName(Model model,
//			@RequestParam(value = "username", required = true) String username) {
//
//		ResultVo<List<SysUser>> resultVo = new ResultVo<List<SysUser>>();
//		try {
//			List<SysUser> userList = sysUserService.isExistsName(username);
//			if (userList != null && userList.size() > 0) {
//				resultVo.setCode(ResultCode.RESULT_FAILURE.getCode());
//				resultVo.setResultDes("已存在该登录账户，请修改");
//			}
//		} catch (Exception ex) {
//			ex.printStackTrace();
//			resultVo.setCode(ResultCode.RESULT_FAILURE.getCode());
//			resultVo.setResultDes("服务忙，请稍后再试");
//		}
//
//		model.addAttribute(SysConst.RESULT_KEY, resultVo);
//		return model;
//	}
//
//	@RequestMapping(value = { "/system/saveAccount" }, method = { RequestMethod.POST })
//	public @ResponseBody Model saveAccount(Model model, @RequestParam(value = "id", required = false) String id,
//			@RequestParam(value = "name", required = true) String name,
//			@RequestParam(value = "username", required = false) String username,
//			@RequestParam(value = "password", required = false) String password,
//			@RequestParam(value = "telephone", required = false) String telephone,
//			@RequestParam(value = "roleIds", required = false) String roleIds,
//			@RequestParam(value = "teamVal", required = false) Integer teamVal,
//			@RequestParam(value = "jlSelect", required = false) Integer jlSelect,
//			@RequestParam(value = "zgSelect", required = false) Integer zgSelect) {
//
//		ResultVo<List<SysUser>> resultVo = new ResultVo<List<SysUser>>();
//		SysUserVo user = getLoginUser();
//		try {
//			SysUserVo ottvUser = new SysUserVo();
//			if (StringUtil.isNotEmpty(id)) {
//				ottvUser.setId(Integer.parseInt(id.trim()));
//			}
//			if (StringUtil.isNotEmpty(username)) {
//				ottvUser.setUsername(username);
//			}
//			if (StringUtil.isNotEmpty(password)) {
//				ottvUser.setPassword(password);
//			}
//
//			SysUserinfoManager advertiser = new SysUserinfoManager();
//			if (StringUtil.isNotEmpty(telephone)) {
//				advertiser.setTelephone(telephone);
//			}
//			if (StringUtil.isNotEmpty(name)) {
//				advertiser.setName(name);
//			}
//			ottvUser.setUserinfo(advertiser);
//			ottvUser.setOperateId(user.getId());
//
//			sysUserService.saveUser(ottvUser, roleIds);
//		} catch (Exception ex) {
//			ex.printStackTrace();
//			resultVo.setCode(ResultCode.RESULT_FAILURE.getCode());
//			resultVo.setResultDes("服务忙，请稍后再试");
//		}
//
//		model.addAttribute(SysConst.RESULT_KEY, resultVo);
//		return model;
//	}
//
//	@RequestMapping(value = { "/system/toAdd" })
//	public String detail(Model model, @RequestParam(value = "id", required = false) Integer id) {
//
//		ResultVo<Map<String, Object>> resultVo = new ResultVo<Map<String, Object>>();
//		try {
//			if (id != null) {
//				SysUserVo user = sysUserService.findUserinfoById(id);
//				model.addAttribute("obj", user);
//				List<SysRoleVo> roleList = sysRoleService.findRoleByUserId(id);
//				model.addAttribute("roleList", roleList);
//			} else {
//				List<SysRoleVo> roleList = sysRoleService.findRoleByUserId(0);
//				model.addAttribute("roleList", roleList);
//			}
//		} catch (Exception ex) {
//			ex.printStackTrace();
//			resultVo.setCode(ResultCode.RESULT_FAILURE.getCode());
//			resultVo.setResultDes("服务忙，请稍后再试");
//		}
//
//		return PageConst.ACCOUNT_ADD_PAGE;
//	}
//
//	@RequestMapping(value = { "/system/deleteAccount" }, method = { RequestMethod.POST })
//	public @ResponseBody Model deleteAccount(Model model, @RequestParam(value = "id", required = true) Integer id) {
//
//		ResultVo<List<SysRole>> resultVo = new ResultVo<List<SysRole>>();
//		try {
//			int delCount = sysUserService.delUserById(id);
//			if (delCount == 1) {
//				resultVo.setCode(ResultCode.RESULT_SUCCESS.getCode());
//			} else {
//				resultVo.setCode(ResultCode.RESULT_FAILURE.getCode());
//				resultVo.setResultDes("删除失败，请稍后再试");
//			}
//		} catch (Exception ex) {
//			ex.printStackTrace();
//			resultVo.setCode(ResultCode.RESULT_FAILURE.getCode());
//			resultVo.setResultDes("服务忙，请稍后再试");
//		}
//
//		model.addAttribute(SysConst.RESULT_KEY, resultVo);
//		return model;
//	}
//
//	@RequestMapping(value = { "/system/updateAccountStatus" }, method = { RequestMethod.POST })
//	public @ResponseBody Model updateAccountStatus(Model model, @RequestParam(value = "id", required = true) Integer id,
//			@RequestParam(value = "status", required = true) Integer status) {
//
//		ResultVo<List<SysRole>> resultVo = new ResultVo<List<SysRole>>();
//		try {
//			int updCount = sysUserService.updateUserStatus(id, status);
//			if (updCount == 1) {
//				resultVo.setCode(ResultCode.RESULT_SUCCESS.getCode());
//			} else {
//				resultVo.setCode(ResultCode.RESULT_FAILURE.getCode());
//				resultVo.setResultDes("停用失败，请稍后再试");
//			}
//		} catch (Exception ex) {
//			ex.printStackTrace();
//			resultVo.setCode(ResultCode.RESULT_FAILURE.getCode());
//			resultVo.setResultDes("服务忙，请稍后再试");
//		}
//
//		model.addAttribute(SysConst.RESULT_KEY, resultVo);
//		return model;
//	}

}
