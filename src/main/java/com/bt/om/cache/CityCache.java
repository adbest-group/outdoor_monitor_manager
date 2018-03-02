package com.bt.om.cache;

import com.bt.om.entity.City;
import com.bt.om.service.ICityService;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by caiting on 2018/2/11.
 */
@Service
public class CityCache {
    private Map<Long,String> citys = Maps.newHashMap();

    @Autowired
    private ICityService cityService;

    public String getCityName(Long code){
        return citys.get(code);
    }

    public void init(){
        Map<Long,String> newMap = Maps.newHashMap();
        List<City> list = cityService.getAll();
        if(list!=null&&list.size()>0){
            for(City city:list) {
                newMap.put(city.getId(),city.getName());
            }
        }
        this.citys = newMap;
    }
}
