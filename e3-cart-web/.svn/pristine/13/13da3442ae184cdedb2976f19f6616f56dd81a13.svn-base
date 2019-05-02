package cn.e3mall.cart.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.omg.CORBA.StringHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mall.cart.service.CartService;
import cn.e3mall.common.utils.CookieUtils;
import cn.e3mall.common.utils.E3mallResult;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.service.ItemService;

@Controller
public class CartController {
	/*
	 ** 购物车处理
	 */
	
	@Autowired
	private ItemService itemService;
	@Value("${COOKIE_CART_EXPIRE}")
	private Integer COOKIE_CART_EXPIRE;
	@Autowired
	private CartService cartService;
	
	@RequestMapping("/cart/add/{itemId}")
	public String addCart(@PathVariable Long itemId, @RequestParam(defaultValue="1")Integer num,
			HttpServletRequest request, HttpServletResponse response) {
		//判断用户是否登录
		TbUser user = (TbUser)request.getAttribute("user");
		if (user != null) {
			//如果是登录状态，把购物车写入redis
			//保存到服务端，返回逻辑视图
			cartService.addCart(user.getId(), itemId, num);
			return "cartSuccess";
		}
		
		//如果未登录，使用cookie
		//从cookie中取购物车商品列表
		List<TbItem> cartList = getCartListFromCookie(request);
		//判断商品在商品列表中是否存在
		boolean flag = false;
		for (TbItem tbItem : cartList) {
			//包装数据类型不能直接相等
			if (tbItem.getId() == itemId.longValue()) {
				//如果存在，数量相加
				flag = true;
				tbItem.setNum(tbItem.getNum() + num);
				//跳出循环
				break;
			}
		}
		//如果不存在
		if (!flag) {
			//根据商品id查询商品信息，得到TBItem对象
			TbItem tbItem = itemService.getItemById(itemId);
			//设置商品数量
			tbItem.setNum(num);
			//取一张图片
			String image = tbItem.getImage();
			if (StringUtils.isNotBlank(image)) {
				tbItem.setImage(image.split(",")[0]);
			}
			//把商品添加到商品列表
			cartList.add(tbItem);
		}
		
		//写入cookie
		CookieUtils.setCookie(request, response, "cart", JsonUtils.objectToJson(cartList), COOKIE_CART_EXPIRE, true);
		//返回添加成功页面
		return "cartSuccess";
	}
	
	
	//从cookie中取购物车列表的处理
	private List<TbItem> getCartListFromCookie(HttpServletRequest request) {
		String json = CookieUtils.getCookieValue(request, "cart", true);
		//判断json是否为空
		if (StringUtils.isBlank(json)) {
			return new ArrayList<>();
		}
		//把json转换成一个商品列表
		List<TbItem> list = JsonUtils.jsonToList(json, TbItem.class);
		return list;
	}
	/*
	 * *展示购物车列表
	 */
	@RequestMapping("/cart/cart")
	public String showCartList(HttpServletRequest request, HttpServletResponse response) {
		//判断用户是否为登录状态
		TbUser user = (TbUser)request.getAttribute("user");
		//从cookie中取购物车列表
		List<TbItem> cartList = getCartListFromCookie(request);
		//登录状态
		if (user != null) {
			//如果不为空，把cookie中的购物车商品和服务端（redis）的购物车商品合并
			cartService.mergeCart(user.getId(), cartList);
			//把cookie中的购物车列表删除
			CookieUtils.deleteCookie(request, response, "cart");
			//从服务端取购物车列表
			cartList = cartService.getCartList(user.getId());
		}
		
		//未登录状态
		//把列表传递给页面
		request.setAttribute("cartList", cartList);
		//返回逻辑视图
		return "cart";
	}
	
	/*
	 * *更新购物车商品数量
	 */
	@RequestMapping("/cart/update/num/{itemId}/{num}")
	@ResponseBody
	public E3mallResult updateCartNum(@PathVariable Long itemId, @PathVariable Integer num,
			HttpServletRequest request, HttpServletResponse response) {
		//判断用户是否为登录状态
		TbUser user = (TbUser)request.getAttribute("user");
		if (user != null) {
			cartService.updateCartNum(user.getId(), itemId, num);
			return E3mallResult.ok();
		}
		//从cookie中取购物车列表
		List<TbItem> cartList = getCartListFromCookie(request);
		//遍历商品列表 找到对应商品
		for (TbItem tbItem : cartList) {
			if (tbItem.getId().longValue() == itemId) {
				//更新数量
				tbItem.setNum(num);
				break;
			}
		}
		//把购物车列表写回cookie
		CookieUtils.setCookie(request, response, "cart", JsonUtils.objectToJson(cartList), COOKIE_CART_EXPIRE, true);
		//返回成功
		return E3mallResult.ok();
	}
	
	/*
	 * *删除购物车商品
	 */
	 @RequestMapping("/cart/delete/{itemId}")
	 public String deleteCartItem(@PathVariable Long itemId, HttpServletRequest request, HttpServletResponse response) {
		//判断用户是否为登录状态
		TbUser user = (TbUser)request.getAttribute("user");
		if (user != null) {
			cartService.deleteCartItem(user.getId(), itemId);
			return "redirect:/cart/cart.html";
		}
		 //从cookie中取购物车列表
		 List<TbItem> cartList = getCartListFromCookie(request);
		 //遍历列表，找到要删除的商品
		 for (TbItem tbItem : cartList) {
			if (tbItem.getId().longValue() == itemId) {
				 //删除商品
				cartList.remove(tbItem);
				//跳出循环
				break;
			}
		 }
		 //把购物车列表写回cookie
		 CookieUtils.setCookie(request, response, "cart", JsonUtils.objectToJson(cartList), COOKIE_CART_EXPIRE, true);
		 //返回逻辑视图
		 return "redirect:/cart/cart.html";
	 }

}
