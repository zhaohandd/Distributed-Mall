package cn.e3mall.cart.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import cn.e3mall.cart.service.CartService;
import cn.e3mall.common.jedis.JedisClient;
import cn.e3mall.common.utils.E3mallResult;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.mapper.TbItemMapper;
import cn.e3mall.pojo.TbItem;

public class CartServiceImpl implements CartService{

	@Autowired
	private JedisClient jedisClient;
	@Autowired
	private TbItemMapper itemMapper;
	
	@Override
	public E3mallResult addCart(long userId, long itemId, int num) {
		// TODO Auto-generated method stub
		//向redis中添加购物车
		//数据类型是hash key：用户id userId； field：商品id itemId； value：商品信息
		//判断商品是否存在
		Boolean hexists = jedisClient.hexists("CART:" + userId, itemId + "");
		//如果存在,数量相加
		if (hexists) {
			String json = jedisClient.hget("CART:" + userId, itemId + "");
			//把json转换成TbItem
			TbItem item = JsonUtils.jsonToPojo(json, TbItem.class);
			item.setNum(item.getNum() + num);
			//写入redis
			jedisClient.hset("CART:" + userId, itemId + "", JsonUtils.objectToJson(item));
			return E3mallResult.ok();
		}
		//如果不存在，根据商品id取商品信息
		TbItem item = itemMapper.selectByPrimaryKey(itemId);
		//设置购物车数量
		item.setNum(num);
		//取一张图片
		String image = item.getImage();
		if (StringUtils.isNotBlank(image)) {
			item.setImage(image.split(",")[0]);
		}
		//添加到购物车列表
		jedisClient.hset("CART:" + userId, itemId + "", JsonUtils.objectToJson(item));
		//返回成功
		return E3mallResult.ok();
	}

	@Override
	public E3mallResult mergeCart(long userId, List<TbItem> itemList) {
		//遍历商品列表
		//把列表添加到购物车
		//判断购物车中是否有此商品
		//如果有，数量相加
		//如果没有，添加新的商品
		for (TbItem tbItem : itemList) {
			addCart(userId, tbItem.getId(), tbItem.getNum());
		}
		//返回成功 
		return E3mallResult.ok();
	}

	@Override
	public List<TbItem> getCartList(long userId) {
		//根据用户id查询购物车列表
		List<String> jsonList = jedisClient.hvals("CART:" + userId);
		List<TbItem> itemList = new ArrayList<>();
		for (String string : jsonList) {
			//创建一个TbItem对象
			TbItem item = JsonUtils.jsonToPojo(string, TbItem.class);
			//添加到列表
			itemList.add(item);
		}
		return itemList;
	}

	@Override
	public E3mallResult updateCartNum(long userId, long itemId, int num) {
		//从redis中取商品信息
		String json = jedisClient.hget("CART:" + userId, itemId + "");
		//更新商品数量
		TbItem tbItem = JsonUtils.jsonToPojo(json, TbItem.class);
		tbItem.setNum(num);
		//写入redis
		jedisClient.hset("CART:" + userId, itemId + "", JsonUtils.objectToJson(tbItem));
		return E3mallResult.ok();
	}

	@Override
	public E3mallResult deleteCartItem(long userId, long itemId) {
		//删除购物车商品
		jedisClient.hdel("CART:" + userId, itemId + "");
		return E3mallResult.ok();
	}

	@Override
	public E3mallResult clearCartItem(long userId) {
		//删除购物车信息
		jedisClient.del("CART:" + userId);
		return E3mallResult.ok();
	}

}
