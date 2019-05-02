package cn.e3mall.sso.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mall.common.utils.CookieUtils;
import cn.e3mall.common.utils.E3mallResult;
import cn.e3mall.sso.service.LoginService;

@Controller
public class LoginController {

	@Autowired
	private LoginService loginService;
	
	@RequestMapping("/page/login")
	public String showLogin(String redirect, Model model) {
		model.addAttribute("redirect", redirect);
		return "login";
	}
	
	@RequestMapping(value="/user/login", method=RequestMethod.POST)
	@ResponseBody
	public E3mallResult login(String username, String password, HttpServletRequest request, HttpServletResponse response) {
		E3mallResult e3mallResult = loginService.userLogin(username, password);
		//判断是否登录成功
		if (e3mallResult.getStatus() == 200) {
			String token = e3mallResult.getData().toString();
			//如果成功，token写入cookie
			CookieUtils.setCookie(request, response, "token", token);
		}
		//返回结果
		return e3mallResult;
	}
	
}
