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
 * 客户类型/行业类型 相关事务层
 */
@Service
public class AdCustomerTypeService implements IAdCustomerTypeService {

	@Autowired
	private AdCustomerTypeMapper adCustomerTypeMapper;
	
	/**
	 * 分页查询行业类型
	 */
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

	/**
	 * 通过id查询行业类型
	 */
	@Override
	public AdCustomerType getById(Integer id) {
		return adCustomerTypeMapper.selectByPrimaryKey(id);
	}

	/**
	 * 插入一条行业类型
	 */
	@Override
	@Transactional
	public void save(AdCustomerType adCustomerType) {
		adCustomerTypeMapper.insert(adCustomerType);
	}

	/**
	 * 修改行业类型
	 */
	@Override
	@Transactional
	public void modify(AdCustomerType adCustomerType) {
		Date now = new Date();
		adCustomerType.setUpdateTime(now);
		adCustomerTypeMapper.updateByPrimaryKeySelective(adCustomerType);
	}

	/**
	 * 查询全部的行业类型
	 */
	@Override
	public List<AdCustomerType> getAll() {
		return adCustomerTypeMapper.getAll();
	}

}
