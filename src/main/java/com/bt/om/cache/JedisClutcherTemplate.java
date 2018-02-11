package com.bt.om.cache;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;

import java.util.HashSet;
import java.util.Set;

import com.bt.om.util.NumberUtil;

/**
 * Created by Administrator on 2015/10/28 0028.
 */
public class JedisClutcherTemplate {

    private static JedisCluster jedisCluster;

    public JedisClutcherTemplate(JedisPoolConfig jedisPoolConfig, String ipPort) {
        if (ipPort == null){
            return;
        }
        String[] ipPorts = ipPort.split(",");
        if (ipPorts == null){
            return;
        }
        Set<HostAndPort> nodes = new HashSet<HostAndPort>();
        for (String temp : ipPorts){
            String[] ips = temp.split(":");
            if (ips.length == 2){
                String ip = ips[0];
                int port = NumberUtil.parseInt(ips[1]);
                HostAndPort hostAndPort = new HostAndPort(ip, port);
                nodes.add(hostAndPort);
            }
        }
        jedisCluster = new JedisCluster(nodes, jedisPoolConfig);
    }

    public JedisCluster getJedisClutcher(){
        return jedisCluster;
    }
}
