package com.bt.om.cache;

import com.bt.om.entity.AdSeatType;
import com.bt.om.mapper.AdSeatTypeMapper;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by caiting on 2018/3/5.
 */
@Service
public class AdSeatTypeCache {
    private List<AdSeatType> types = Lists.newArrayList();

    @Autowired
    private AdSeatTypeMapper adSeatTypeMapper;

    public void init(){
        List<AdSeatType> adTypes = adSeatTypeMapper.getSeatTypeAll();
        if(adTypes!=null&&adTypes.size()>0){
            this.types = adTypes;
        }
    }

    public List<AdSeatType> getTypes() {
        return types;
    }
}
