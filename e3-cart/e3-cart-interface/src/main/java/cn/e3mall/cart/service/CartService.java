package cn.e3mall.cart.service;

import java.util.List;

import cn.e3mall.common.utils.E3mallResult;
import cn.e3mall.pojo.TbItem;

public interface CartService {
	
	E3mallResult addCart(long userId, long itemId, int num);
	
	E3mallResult mergeCart(long userId, List<TbItem> itemList);
	
	List<TbItem> getCartList(long userId);
	
	E3mallResult updateCartNum(long userId, long itemId, int num);
	
	E3mallResult deleteCartItem(long userId, long itemId);
	
	E3mallResult clearCartItem(long userId);

}
