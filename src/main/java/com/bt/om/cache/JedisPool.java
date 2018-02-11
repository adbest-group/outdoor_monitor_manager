package com.bt.om.cache;

import java.util.ArrayList;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.apache.log4j.Logger;

import com.bt.om.util.NumberUtil;

import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;


/**
 * 
 * 
 * @author hl-tanyong
 * @version $Id: JedisPool.java, v 0.1 2015年9月18日 上午10:44:50 hl-tanyong Exp $
 */
public class JedisPool {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(JedisPool.class);

	private static ShardedJedisPool pool;

	public JedisPool(JedisPoolConfig jedisPoolConfig, String redisPoolList) {
		if (redisPoolList == null) {
			return;
		}
		String[] redisIPs = redisPoolList.split(",");

		if (redisIPs == null) {
			return;
		}

		ArrayList<JedisShardInfo> ppl = new ArrayList<JedisShardInfo>();
		for (String tmp : redisIPs) {
			String[] ips = tmp.split(":");
			if (ips.length == 2) {
				String ip = ips[0];
				int port = NumberUtil.parseInt(ips[1]);

				JedisShardInfo pool = new JedisShardInfo(ip, port);
				ppl.add(pool);
				logger.info("add Jedis Service:ip:" + ip + ",port:" + port);
			}
		}

		pool = new ShardedJedisPool((GenericObjectPoolConfig) jedisPoolConfig,
				ppl);
	}

	public ShardedJedisPool getPool() {
		return pool;
	}

	public ShardedJedis getResource() {
		return pool.getResource();
	}

	public void returnResource(ShardedJedis jedis) {
		if (jedis != null) {
			pool.close();
//			pool.close();
		}
	}

	public void returnBrokenResource(ShardedJedis jedis) {
		if (jedis != null) {
		    pool.close();
		}
	}
}