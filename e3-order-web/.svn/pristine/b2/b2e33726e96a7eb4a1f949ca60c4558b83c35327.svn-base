package cn.e3mall.order.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import cn.e3mall.cart.service.CartService;
import cn.e3mall.common.utils.CookieUtils;
import cn.e3mall.common.utils.E3mallResult;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.sso.service.TokenService;

public class LoginInterceptor implements HandlerInterceptor{
	
	@Autowired 
	private TokenService tokenService;
	@Autowired 
	private CartService cartService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		//从cookie中取token
		String token = CookieUtils.getCookieValue(request, "token");
		//判断token是否存在
		if (StringUtils.isBlank(token)) {
			//如果token不存在，未登录状态，跳转到sso系统的登录首页。用户登录成功后，跳转到当前请求的url
			response.sendRedirect("http://localhost:8088/page/login?redirect=" + request.getRequestURL());
			//拦截
			return false;
		}
		
		//如果token存在，需要调用sso系统的服务，根据token取用户登录信息
		E3mallResult e3mallResult = tokenService.getUserByToken(token);
		//如果取不到，用户登录过期，需要登录
		if (e3mallResult.getStatus() != 200) {
			response.sendRedirect("http://localhost:8088/page/login?redirect=" + request.getRequestURL());
			return false;
		}
		//如果取到，登录状态，需要将用户信息写入request
		TbUser user = (TbUser)e3mallResult.getData();
		request.setAttribute("user", user);
		//判断cookie中是否有购物车数据，如果有就合并到服务端
		String jsonCartList = CookieUtils.getCookieValue(request, "cart", true);
		if (StringUtils.isNoneBlank(jsonCartList)) {
			//合并购物车
			cartService.mergeCart(user.getId(), JsonUtils.jsonToList(jsonCartList, TbItem.class));
		}
		//放行
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

}
