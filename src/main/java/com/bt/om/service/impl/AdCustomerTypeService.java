package com.bt.om.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bt.om.entity.AdCustomerType;
import com.bt.om.mapper.AdCustomerTypeMapper;
import com.bt.om.service.IAdCustomerTypeService;
import com.bt.om.vo.web.SearchDataVo;

/**
 * Created by jiayong.mao on 2018/4/25.
 */
@Service
public class AdCustomerTypeService implements IAdCustomerTypeService {

	@Autowired
	private AdCustomerTypeMapper adCustomerTypeMapper;
	
	@Override
	public void getPageData(SearchDataVo vo) {
		int count = adCustomerTypeMapper.getPageCount(vo.getSearchMap());
        vo.setCount(count);
        if (count > 0) {
            vo.setList(adCustomerTypeMapper.getPageData(vo.getSearchMap(), new RowBounds(vo.getStart(), vo.getSize())));
        } else {
            vo.setList(new ArrayList<AdCustomerType>());
        }
	}

	@Override
	public AdCustomerType getById(Integer id) {
		return adCustomerTypeMapper.selectByPrimaryKey(id);
	}

	@Override
	@Transactional
	public void save(AdCustomerType adCustomerType) {
		adCustomerTypeMapper.insert(adCustomerType);
	}

	@Override
	@Transactional
	public void modify(AdCustomerType adCustomerType) {
		Date now = new Date();
		adCustomerType.setUpdateTime(now);
		adCustomerTypeMapper.updateByPrimaryKeySelective(adCustomerType);
	}

	@Override
	public List<AdCustomerType> getAll() {
		return adCustomerTypeMapper.getAll();
	}

}
