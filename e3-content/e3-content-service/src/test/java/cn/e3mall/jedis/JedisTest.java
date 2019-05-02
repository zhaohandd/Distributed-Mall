package cn.e3mall.jedis;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

public class JedisTest {
	
	@Test
	public void testJedis() throws Exception{
		//单机版
		//创建一个jedis连接对象，参数host和port
		Jedis jedis = new Jedis("192.168.25.133", 6379);
		//直接使用jedis操作redis，所有jedis的命令都对应一个方法
		jedis.set("test123", "mytest");
		String string = jedis.get("test123");
		System.out.println(string);
		//关闭连接
		jedis.close();
		
	}
	
	//连接池，节省资源
	@Test
	public void testJedisPool() throws Exception{
		//创建一个连接池对象，host和port
		JedisPool jedisPool = new JedisPool("192.168.25.133", 6379);
		//从连接池获得一个连接，jedis对象
		Jedis jedis = jedisPool.getResource();
		//操作redis
		String string = jedis.get("test123");
		System.out.println(string);
		//关闭连接，每次使用完毕后关闭连接。连接池回收资源
		jedis.close();
		//关闭连接池
		jedisPool.close();
	}
	
	@Test
	public void testJedisCluster() throws Exception{
		//创建一个jedisCluster对象，有一个参数nodes是一个set类型。set中包含若干个HostandPort对象
		Set<HostAndPort> nodes = new HashSet<>();
		nodes.add(new HostAndPort("192.168.25.133", 7001));
		nodes.add(new HostAndPort("192.168.25.133", 7002));
		nodes.add(new HostAndPort("192.168.25.133", 7003));
		nodes.add(new HostAndPort("192.168.25.133", 7004));
		nodes.add(new HostAndPort("192.168.25.133", 7005));
		nodes.add(new HostAndPort("192.168.25.133", 7006));
		
		JedisCluster jedisCluster = new JedisCluster(nodes);
		//直接使用jedisCluster对象操作redis。
		jedisCluster.set("test", "123");
		String string = jedisCluster.get("test");
		System.out.println(string);
		//关闭jedisCluster对象
		jedisCluster.close();
		
	}

}
