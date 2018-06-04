package com.bt.om.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bt.om.entity.AdUserPoint;
import com.bt.om.entity.SysResources;
import com.bt.om.mapper.AdUserPointMapper;
import com.bt.om.service.IUserPointService;
import com.bt.om.vo.web.SearchDataVo;
@Service
public class UserPointService implements IUserPointService {

	@Autowired
	private AdUserPointMapper adUserPointMapper;

	@Override
	public void addUserPoint(AdUserPoint adUserPoint) {
		adUserPointMapper.insert(adUserPoint);
	}

	@Override
	public void getPageData(SearchDataVo vo) {
		int count = adUserPointMapper.getPageCount(vo.getSearchMap());
		vo.setCount(count);
		if (count > 0) {
			vo.setList(adUserPointMapper.getPageData(vo.getSearchMap(), new RowBounds(vo.getStart(), vo.getSize())));
		} else {
			vo.setList(new ArrayList<SysResources>());
		}
	}

	//通过用户id查找该用户的所有积分数据
	@Override
	public List<AdUserPoint> getListById(Integer userId) {
		return adUserPointMapper.getListById(userId);
	}
	
	

}
