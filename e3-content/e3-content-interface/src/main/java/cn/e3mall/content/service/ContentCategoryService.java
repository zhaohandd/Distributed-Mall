package cn.e3mall.content.service;

import java.util.List;

import cn.e3mall.common.pojo.EasyUIDataGridResult;
import cn.e3mall.common.pojo.EasyUITreeNode;
import cn.e3mall.common.utils.E3mallResult;

public interface ContentCategoryService {
	
	List<EasyUITreeNode> getContentCatList(Long parentId); 
	
	E3mallResult addContentCategory(long parentId, String name);
	
	EasyUIDataGridResult getContentList(int page, int rows);
}
