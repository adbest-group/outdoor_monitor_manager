package com.bt.om.web.session;

import com.bt.om.enums.SessionKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Created by caiting on 2018/2/28.
 */
@Service
public class SessionByRedis {
    //session键缓存时间
    private final int expire = 7;
    private final TimeUnit expireUnit = TimeUnit.DAYS;
    //session新生成时缓存时间
    private final int expireCode = 5;
    private final TimeUnit expireCodeUnit = TimeUnit.MINUTES;

    private final String sessionKeyPrifx = "session-key:";

    private ThreadLocal<String> token = new ThreadLocal<>();
    @Autowired
    protected RedisTemplate redisTemplate;

    /**
     * 将数据以键值对的方式储存到当前token对应的缓存
     **/
    public void setAttribute(String mapKey, Serializable mapValue){
        redisTemplate.opsForHash().put(this.getSessionKey(),mapKey,mapValue);
        redisTemplate.expire(this.getSessionKey(),expire, expireUnit);
    }

    /**
     * 获取当前token对应缓存中键mapKey对应的值
     **/
    public Object getAttribute( String mapKey){
        redisTemplate.expire(this.getSessionKey(),expire, expireUnit);
        return redisTemplate.opsForHash().get(this.getSessionKey(),mapKey);
    }

    /**
     * 删除当前token的缓存
     **/
    public void remove(){
        redisTemplate.delete(this.getSessionKey());
    }

    public void setImageCode(String code){
        redisTemplate.opsForHash().put(this.getSessionKey(), SessionKey.SESSION_CODE.toString(),code);
        redisTemplate.expire(this.getSessionKey(),expireCode,expireCodeUnit);
    }

    public String getImageCode(){
        return (String)redisTemplate.opsForHash().get(this.getSessionKey(),SessionKey.SESSION_CODE.toString());
    }

    public void setToken(String token){
        this.token.set(token);
    }

    public String getToken(){
        return this.token.get();
    }

    public String initToken(){
        this.token.set(UUID.randomUUID().toString());
        return token.get();
    }

    private String getSessionKey(){
        return this.sessionKeyPrifx+this.token.get();
    }
}
