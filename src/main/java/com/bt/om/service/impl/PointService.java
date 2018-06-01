package com.bt.om.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bt.om.entity.AdPoint;
import com.bt.om.mapper.AdPointMapper;
import com.bt.om.service.IPointService;

@Service
public class PointService implements IPointService {

	@Autowired
	private AdPointMapper adPointMapper;
	/**
	 * 将积分添加进用户积分表
	 * */
	@Override
	public void addUserPoint() {
		// TODO Auto-generated method stub
		
	}

	/**
	 * 查找当前积分类型下的积分设置数据
	 * */
	@Override
	public AdPoint findPointValue(int type) {
		return adPointMapper.selectByPointType(type);
	}

}
