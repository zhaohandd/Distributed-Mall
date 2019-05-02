package cn.e3mall.sso.service;

import cn.e3mall.common.utils.E3mallResult;

public interface LoginService {

	//参数是用户名和密码
	//业务逻辑
	/*
	 * 1.判断用户名和密码是否正确
	 * 2.如果不正确，返回登录失败
	 * 3.如果正确生成token
	 * 4.用户信息写入redis，key是token value是用户信息
	 * 5.设置session的过期时间
	 * 6.把token返回
	 */
	//返回值是E3mallResult，其中包含token信息
	E3mallResult userLogin(String name, String password);
}
