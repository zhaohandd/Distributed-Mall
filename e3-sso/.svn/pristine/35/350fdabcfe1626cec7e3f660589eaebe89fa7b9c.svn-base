package cn.e3mall.sso.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.e3mall.common.jedis.JedisClient;
import cn.e3mall.common.utils.E3mallResult;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.sso.service.TokenService;

@Service
public class TokenServiceImpl implements TokenService{

	@Autowired 
	private JedisClient jedisClient;
	@Override
	public E3mallResult getUserByToken(String token) {
		// TODO Auto-generated method stub
		//根据token到redis中取用户信息
		String json = jedisClient.get("SESSION:" + token);
		//取不到用户信息，登录已经过期，返回登录过期
		if (StringUtils.isBlank(json)) {
			return E3mallResult.build(201, "用户登录已经过期");
		}
		//取到用户信息，更新token的过期时间
		jedisClient.expire("SESSION:" + token, 1800);
		//返回结果，E3mallResult其中包含TbUser对象
		TbUser user = JsonUtils.jsonToPojo(json, TbUser.class);
		return E3mallResult.ok(user);
	}
}
