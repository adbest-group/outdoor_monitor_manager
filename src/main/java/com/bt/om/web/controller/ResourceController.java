package com.bt.om.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bt.om.common.web.PageConst;

@Controller
@RequestMapping(value = "/resource")
public class ResourceController {

	@RequestMapping(value = "/add")
	public String gotoAddPage() {
		return PageConst.RESOURCE_ADD;
	}
}
