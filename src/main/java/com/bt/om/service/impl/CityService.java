package com.bt.om.service.impl;

import com.bt.om.entity.City;
import com.bt.om.mapper.CityMapper;
import com.bt.om.service.ICityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 省市区街道事务层
 */
@Service
public class CityService implements ICityService {
    
	@Autowired
    CityMapper cityMapper;

	/**
	 * 查询所有的省市区街道
	 */
    @Override
    public List<City> getAll() {
        return cityMapper.selectAll();
    }

	@Override
	public City getName(Long id) {
		return cityMapper.selectByPrimaryKey(id);
	}
}
