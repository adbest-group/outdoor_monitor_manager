package com.bt.om.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bt.om.entity.AdUserPoint;
import com.bt.om.mapper.AdUserPointMapper;
import com.bt.om.service.IUserPointService;
@Service
public class UserPointService implements IUserPointService {

	@Autowired
	private AdUserPointMapper adUserPointMapper;

	@Override
	public void addUserPoint(AdUserPoint adUserPoint) {
		adUserPointMapper.insert(adUserPoint);
	}
	
	

}
