package com.bt.om.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * Created by caiting on 2018/2/11.
 */
@Component("LocalApplicationListener")
public class LocalApplicationListener implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    CityCache cityCache;
    @Autowired
    AdSeatTypeCache adSeatTypeCache;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if(event.getApplicationContext().getParent()==null){
            cityCache.init();
            adSeatTypeCache.init();
        }
    }
}
