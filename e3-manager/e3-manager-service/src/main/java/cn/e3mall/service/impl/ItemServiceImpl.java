package cn.e3mall.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cn.e3mall.common.jedis.JedisClient;
import cn.e3mall.common.pojo.EasyUIDataGridResult;
import cn.e3mall.common.utils.E3mallResult;
import cn.e3mall.common.utils.IDUtils;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.mapper.TbItemDescMapper;
import cn.e3mall.mapper.TbItemMapper;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbItemDesc;
import cn.e3mall.pojo.TbItemExample;
import cn.e3mall.service.ItemService;

@Service
public class ItemServiceImpl implements ItemService{
	
	@Autowired
	private TbItemMapper itemMapper;
	@Autowired
	private TbItemDescMapper itemDescMapper;
	@Autowired
	private JmsTemplate jmsTemplate;
	@Resource
	private Destination topicDestination;
	@Autowired
	private JedisClient jedisClient;
	
	@Override
	public TbItem getItemById(long itemId) {
		// TODO Auto-generated method stub
		//查询缓存
		try {
			String json = jedisClient.get("ITEM_INFO:" + itemId + ":BASE");
			if (StringUtils.isNotBlank(json)) {
				
				TbItem tbItem = JsonUtils.jsonToPojo(json, TbItem.class);
				return tbItem;
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		//缓存中没有，查询数据库
		TbItem tbItem = itemMapper.selectByPrimaryKey(itemId);
		try {
			//可以将其写入属性文件
			jedisClient.set("ITEM_INFO:" + itemId + ":BASE", JsonUtils.objectToJson(tbItem));
			//设置过期时间
			jedisClient.expire("ITEM_INFO:" + itemId + ":BASE", 3600);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return tbItem;
	}
	
	@Override
	public EasyUIDataGridResult getItemList(int page, int rows) {
		// TODO Auto-generated method stub
		//设置分页信息
		PageHelper.startPage(page, rows);
		//执行查询
		TbItemExample example = new TbItemExample();
		List<TbItem> list = itemMapper.selectByExample(example);
		//创建一个返回值对象
		EasyUIDataGridResult result = new EasyUIDataGridResult();
		result.setRows(list);
		//取分页结果
		PageInfo<TbItem> pageInfo = new PageInfo<>(list);
		//取总记录数
		long total = pageInfo.getTotal();
		result.setTotal(total);
		return result;
	}

	@Override
	public E3mallResult addItem(TbItem item, String desc) {
		//生成商品id
		final long itemId = IDUtils.genItemId();
		//补全item的属性
		item.setId(itemId);
		//1-正常 2-下架 3-删除
		item.setStatus((byte)1);
		item.setCreated(new Date());
		item.setUpdated(new Date());
		//向商品表插入数据
		itemMapper.insert(item);
		//创建一个商品描述表对应的pojo对象
		TbItemDesc itemDesc = new TbItemDesc();
		//补全属性
		itemDesc.setItemId(itemId);
		itemDesc.setItemDesc(desc);
		itemDesc.setCreated(new Date());
		itemDesc.setUpdated(new Date());
		//向商品描述表插入数据
		itemDescMapper.insert(itemDesc);
		//发送一个商品添加消息
		jmsTemplate.send(topicDestination, new MessageCreator() {
			
			@Override
			public Message createMessage(Session session) throws JMSException {
				// TODO Auto-generated method stub
				return session.createTextMessage(itemId + "");
			}
		});
		//返回成功
		return E3mallResult.ok();
	}

	@Override
	public TbItemDesc getItemDescById(long itemId) {
		// TODO Auto-generated method stub
		//查询缓存
		try {
			String json = jedisClient.get("ITEM_INFO:" + itemId + ":BASE");
			if (StringUtils.isNotBlank(json)) {
				
				TbItemDesc itemDesc = JsonUtils.jsonToPojo(json, TbItemDesc.class);
				return itemDesc;
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		TbItemDesc itemDesc = itemDescMapper.selectByPrimaryKey(itemId);
		try {
			//可以将其写入属性文件
			jedisClient.set("ITEM_INFO:" + itemId + ":BASE", JsonUtils.objectToJson(itemDesc));
			//设置过期时间
			jedisClient.expire("ITEM_INFO:" + itemId + ":BASE", 3600);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return itemDesc;
	}

}
