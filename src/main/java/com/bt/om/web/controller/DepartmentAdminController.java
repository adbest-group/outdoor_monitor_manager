package com.bt.om.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bt.om.service.ISysUserService;
import com.bt.om.web.BasicController;

@Controller
@RequestMapping("/dpartmentAdmin")
public class DepartmentAdminController  extends BasicController{
	@Autowired
	private ISysUserService sysUserService;
	
}
