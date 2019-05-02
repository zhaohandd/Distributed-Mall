package cn.e3mall.content.service;

import java.util.List;

import cn.e3mall.common.utils.E3mallResult;
import cn.e3mall.pojo.TbContent;

public interface ContentService {
	
	E3mallResult addContent(TbContent content);
	
	List<TbContent> getContentListByCid(long cid);

}
