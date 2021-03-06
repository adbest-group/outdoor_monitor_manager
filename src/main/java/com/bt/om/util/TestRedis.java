package com.bt.om.util;

import java.util.Set;

import redis.clients.jedis.Jedis;

/**
 * 本地操作redis缓存
 */
public class TestRedis {

	public static void main(String[] args) {
		Jedis jedis = new Jedis("10.2.30.54", 6379); 
		
		Set<String> set = jedis.keys("activity_*");  
        for (String key : set) {  
            System.out.println(key);
//            jedis.del(key);
        }
        
        set = jedis.keys("jiucuo_*");  
        for (String key : set) {  
            System.out.println(key);
//            jedis.del(key);
        }
        
        set = jedis.keys("zhipai_*");  
        for (String key : set) {  
            System.out.println(key);
//            jedis.del(key);
        }
        
        set = jedis.keys("monitorTask_*");  
        for (String key : set) {  
            System.out.println(key);
//            jedis.del(key);
        }
        
        System.out.println(jedis.ttl("zhipai_262_finish"));
	}
}
