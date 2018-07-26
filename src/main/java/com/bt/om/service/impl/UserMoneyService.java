package com.bt.om.service.impl;

import java.util.ArrayList;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bt.om.entity.SysResources;
import com.bt.om.mapper.AdUserMoneyMapper;
import com.bt.om.service.IUserMoneyService;
import com.bt.om.vo.web.SearchDataVo;
@Service
public class UserMoneyService implements IUserMoneyService {

	@Autowired
	private AdUserMoneyMapper adUserMoneyMapper;
	@Override
	public void getPageData(SearchDataVo vo) {
		int count = adUserMoneyMapper.getPageCount(vo.getSearchMap());
		vo.setCount(count);
		if (count > 0) {
			vo.setList(adUserMoneyMapper.getPageData(vo.getSearchMap(), new RowBounds(vo.getStart(), vo.getSize())));
		} else {
			vo.setList(new ArrayList<SysResources>());
		}
	}
	
	@Override
	public Double getMoneyCountById(Integer userId) {
		return adUserMoneyMapper.getMoneyCountById(userId);
	}

}
