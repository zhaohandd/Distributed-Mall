package cn.e3mall.order.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.e3mall.common.jedis.JedisClient;
import cn.e3mall.common.utils.E3mallResult;
import cn.e3mall.mapper.TbOrderItemMapper;
import cn.e3mall.mapper.TbOrderMapper;
import cn.e3mall.mapper.TbOrderShippingMapper;
import cn.e3mall.order.pojo.OrderInfo;
import cn.e3mall.order.service.OrderService;
import cn.e3mall.pojo.TbOrderItem;
import cn.e3mall.pojo.TbOrderShipping;

@Service
public class OrderServiceImpl implements OrderService{

	@Autowired
	private TbOrderMapper orderMapper;
	@Autowired
	private TbOrderItemMapper orderItemMapper;
	@Autowired
	private TbOrderShippingMapper orderShippingMapper;
	
	@Autowired
	private JedisClient jedisClient;
	
	@Override
	public E3mallResult createOrder(OrderInfo orderInfo) {
		//生成订单号,使用redis的incr生成
		if (!jedisClient.exists("ORDER_ID_GEN")) {
			jedisClient.set("ORDER_ID_GEN", "100544");
		}
		String orderId = jedisClient.incr("ORDER_ID_GEN").toString();
		//补全orderInfo的属性
		orderInfo.setOrderId(orderId);
		//1未付款 2已付款 3未发货 4已发货 5 交易成功 6交易关闭
		orderInfo.setStatus(1);
		orderInfo.setCreateTime(new Date());
		orderInfo.setUpdateTime(new Date());
		//插入订单号
		orderMapper.insert(orderInfo);
		//向订单明细表插入数据
		List<TbOrderItem> orderItems = orderInfo.getOrderItems();
		for (TbOrderItem tbOrderItem : orderItems) {
			//生成明细id
			String  odId = jedisClient.incr("ORDER_DETAIL_ID_GEN").toString();
			//补全pojo的属性
			tbOrderItem.setId(odId);
			tbOrderItem.setOrderId(orderId);
			//向明细表插入数据
			orderItemMapper.insert(tbOrderItem);
		}
		//向订单物流表插入数据
		TbOrderShipping orderShipping = orderInfo.getOrderShipping();
		orderShipping.setOrderId(orderId);
		orderShipping.setCreated(new Date());
		orderShipping.setUpdated(new Date());
		orderShippingMapper.insert(orderShipping);
		//返回e3mallResult，包含订单号
		return E3mallResult.ok(orderId);
	}

}
