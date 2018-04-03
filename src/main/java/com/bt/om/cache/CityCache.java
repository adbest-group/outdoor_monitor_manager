package com.bt.om.cache;

import com.bt.om.entity.City;
import com.bt.om.service.ICityService;
import com.bt.om.util.CityUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by caiting on 2018/2/11.
 */
@Service
public class CityCache {
    private Map<Long, City> citys = Maps.newHashMap();
    
    private List<City> province = Lists.newArrayList();

    @Autowired
    private ICityService cityService;

    public String getCityName(Long code) {
        if(citys.get(code)==null)
            return null;
        return citys.get(code).getName();
    }

    public void init() {
        Map<Long, City> newMap = Maps.newHashMap();
        List<City> list = cityService.getAll();
        if (list != null && list.size() > 0) {
            for (City city : list) {
                newMap.put(city.getId(), city);
                if (CityUtil.isProvince(city.getId())) {
                    province.add(city);
                }
            }
        }
        this.citys = newMap;
    }

    public List<City> getAllProvince() {
        return province;
    }

    public List<City> getCity(Long provinceId) {
        Stream<City> s = citys.entrySet().stream().filter(x -> CityUtil.isCity(x.getKey())&&(CityUtil.getProvinceCode(x.getKey()).equals(provinceId))).map(x -> x.getValue());
        return s.collect(Collectors.toList());
    }

    public List<City> getRegionByProvince(Long provinceId) {
        return citys.entrySet().stream().filter(x -> CityUtil.isRegion(x.getKey()) && CityUtil.getProvinceCode(x.getKey()).equals(provinceId)).map(x -> x.getValue()).collect(Collectors.toList());
    }

    public List<City> getRegion(Long cityId) {
        return citys.entrySet().stream().filter(x -> CityUtil.isRegion(x.getKey()) && CityUtil.getCityCode(x.getKey()).equals(cityId)).map(x -> x.getValue()).collect(Collectors.toList());
    }

    public List<City> getStreet(Long regionId) {
        return citys.entrySet().stream().filter(x -> CityUtil.isStreet(x.getKey()) && CityUtil.getRegionCode(x.getKey()).equals(regionId)).map(x -> x.getValue()).collect(Collectors.toList());
    }
}
