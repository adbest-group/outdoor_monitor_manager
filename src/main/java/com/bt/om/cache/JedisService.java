package com.bt.om.cache;


import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.bt.om.util.NumberUtil;

import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.ShardedJedisPool;

import javax.annotation.Resource;

import java.io.*;
import java.util.List;

/**
 * 
 * 
 * @author hl-tanyong
 * @version $Id: JedisService.java, v 0.1 2015年9月18日 上午10:55:43 hl-tanyong Exp $
 */
//@Service
public class JedisService {
    /**
     * Logger for this class
     */
    private static final Logger logger      = Logger.getLogger(JedisService.class);

    @Resource
    private JedisPool jedisPool;

    @Resource
    private JedisClutcherTemplate clutcherTemplate;
    
    public void setClutcherTemplate(JedisClutcherTemplate clutcherTemplate) {
        this.clutcherTemplate = clutcherTemplate;
    }
    /**
     * 缓存180秒
     */
    public static final int     REF_SECONDS = 180;

    private byte[] getCacheName(String type, Object key) {
        StringBuilder cacheName = new StringBuilder(type);
        if (key != null && key.toString().length() > 0) {
            cacheName.append("_").append(key);
        }
        return cacheName.toString().getBytes();
    }

    /**
     * 序列化
     *
     * @param value
     * @return
     */
    private byte[] getSerializable(Object value) {
        try {
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(buffer);
            oos.writeObject(value);
            return buffer.toByteArray();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            logger.error("ERROR:", e);
        }
        return null;
    }

    /**
     * 反序列化
     *
     * @param value
     * @return
     */
    private Object getDeserialization(byte[] value) {
        if (value == null) {
            return null;
        }
        try {
            ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(value));
            return ois.readObject();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            logger.error("ERROR:", e);
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            logger.error("ERROR:", e);
        }
        return null;
    }

    /**
     * 添加一个指定的值到缓存中.
     *
     * @param key
     * @param value
     * @return
     */
    public void putInCache(String type, Object key, Object value) {
        putInCache(type, key, value, REF_SECONDS);
    }

    /**
     * 无时限缓存
     *
     * @param type
     * @param key
     * @param value
     */
    public void putNoTimeInCache(String type, Object key, Object value) {
        if (value != null) {
            putInCache(type, key, value, -1);
        }
    }

    /**
     * 写入Cache
     * 
     * @param type
     * @param key
     * @param value
     * @param seconds
     */
    public void putInCache(String type, Object key, Object value, int seconds) {
        if (value != null) {
            byte[] cacheName = getCacheName(type, key);
            byte[] v = this.getSerializable(value);
            if (v != null) {
                JedisCluster jedis = null;
                try {
                    jedis = getJedisClutcher();
                    if (seconds < 1) {
                        jedis.set(cacheName, v);
                    } else {
                        jedis.setex(cacheName, seconds, v);
                    }
                } catch (Exception e) {
                    logger.error("cache " + getCacheName(type, key) + " socket error。");
                }
            }
        }
    }

    /**
     * 删除缓存
     * 
     * @param type
     * @param key
     */
    public void deleteCache(String type, Object key) {
        JedisCluster jedis = null;
        try {
            jedis = getJedisClutcher();
            jedis.del(getCacheName(type, key));
        } catch (Exception e) {
            logger.error("cache " + getCacheName(type, key) + " socket error。");
        }
    }

    /**
     * 根据指定的关键字获取对象.
     * 
     * @param key
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T> T getFromCache(Class<T> clazz, String type, Object key) {
        return (T) getFromCache(type, key);
    }

    /**
     * 读取一个列表
     * 
     * @param <T>
     * @param clazz
     * @param type
     * @param key
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T> List<T> getListFromCache(Class<T> clazz, String type, Object key) {
        return (List<T>) getFromCache(type, key);
    }

    public Object getFromCache(String type, Object key) {
        JedisCluster jedis = null;
        try {
            jedis = getJedisClutcher();
            byte[] v = jedis.get(getCacheName(type, key));
            if (null == v){
                return null;
            }
            return this.getDeserialization(v);
        } catch (Exception e) {
            logger.debug("cache " + getCacheName(type, key) + " error。");
            return null;
        }
    }

    /**
     * 存储计数器
     * 
     * @param type
     * @param key
     * @param value
     */
    public long putCounterInCache(String type, Object key, long value) {
        JedisCluster jedis = null;
        try {
            jedis = getJedisClutcher();            
            long ret = jedis.incrBy(getCacheName(type, key), value);
            return ret;
        } catch (Exception e) {
        	logger.info(value+"++++"+e.getMessage());
        	e.printStackTrace();
            logger.warn("cache " + getCacheName(type, key) + " error。");
            return -1;
        }
    }

    /**
     * 改变存储计数
     * 
     * @param type
     * @param key
     * @param value
     */
    public long incrCounterCache(String type, Object key, long value) {
        long ret = 0;
        JedisCluster jedis = null;
        try {
            jedis = getJedisClutcher();
            if (value >= 0) {
                ret = jedis.incrBy(getCacheName(type, key), Math.abs(value));
            } else {
                ret = jedis.decrBy(getCacheName(type, key), Math.abs(value));
            }
        } catch (Exception e) {
            logger.warn("cache " + getCacheName(type, key) + " error。");
            return -1;
        }
        return ret;
    }

    /**
     * 得到计数器数值
     * 
     * @param type
     * @param key
     * @return
     */
    public long getCounter(String type, Object key) {
        JedisCluster jedis = null;
        try {
            jedis = getJedisClutcher();
            byte[] v = jedis.get(getCacheName(type, key));
            if (v != null) {
                return NumberUtil.parseLong(new String(v));
            } else {
                return 0;
            }
        } catch (Exception e) {
            logger.warn("cache " + getCacheName(type, key) + " error。");
            return -1;
        }
    }

    public boolean exists(String type, Object key) {
        JedisCluster jedis = null;
        try {
            jedis = getJedisClutcher();
            boolean ret = jedis.exists(getCacheName(type, key));
            return ret;
        } catch (Exception e) {
            logger.debug("cache " + getCacheName(type, key) + " error。");
        }
        return false;
    }

    public void close() {
        try {
            logger.info("cache shutdown！");

        } catch (Exception e) {
            logger.error("cache clear socket error。");
        }
    }

    public ShardedJedisPool getJedisPool() {
        return jedisPool.getPool();
    }
    public JedisCluster getJedisClutcher(){
        return clutcherTemplate.getJedisClutcher();
    }

    public String getFromKey(String key) {
        JedisCluster jedis = null;
        try {
            jedis = getJedisClutcher();
            String v = jedis.get(key);
            return v;
        } catch (Exception e) {
            logger.debug("cache " + key + " error。");
            return null;
        }
    }
}
