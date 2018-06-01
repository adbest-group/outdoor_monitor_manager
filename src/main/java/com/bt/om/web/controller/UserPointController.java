package com.bt.om.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bt.om.service.IUserPointService;

@Controller
@RequestMapping("/userPoint")
public class UserPointController {

	@Autowired
	private IUserPointService userPointService;
}
