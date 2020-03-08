package com.zscat.mallplus.admin.test;

import redis.clients.jedis.Jedis;

/**
 * 测试redis
 */
public class RedisTest {

    public static void main(String[] args) {
        Jedis jedis = new Jedis("39.100.250.104",6379);
        String ping = jedis.ping();
        if (ping.equalsIgnoreCase("PONG")) {
            System.out.println("redis connect success");
            jedis.set("wyg","wyg");
            System.out.println("redis set value");
            System.out.println("redis get value is "+ jedis.get("wyg"));
        }else{
            System.out.println("redis connect fail");
        }
    }
}
