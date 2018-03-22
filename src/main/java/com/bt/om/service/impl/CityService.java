package com.bt.om.service.impl;

import com.bt.om.entity.City;
import com.bt.om.mapper.CityMapper;
import com.bt.om.service.ICityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by caiting on 2018/2/11.
 */
@Service
public class CityService implements ICityService {
    @Autowired
    CityMapper cityMapper;

    @Override
    public List<City> getAll() {
        return cityMapper.selectAll();
    }
}
