package cn.e3mall.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mall.common.pojo.EasyUIDataGridResult;
import cn.e3mall.common.pojo.EasyUITreeNode;
import cn.e3mall.common.utils.E3mallResult;
import cn.e3mall.content.service.ContentCategoryService;

@Controller
public class ContentCatController {
	
	@Autowired
	private ContentCategoryService contentCategoryService;

	@RequestMapping("/content/category/list")
	@ResponseBody
	public List<EasyUITreeNode> getContentCatList(@RequestParam(value="id", defaultValue="0")Long parentId){
		List<EasyUITreeNode> list = contentCategoryService.getContentCatList(parentId);
		
		return list;
	}
	
	@RequestMapping(value="/content/category/create", method=RequestMethod.POST)
	@ResponseBody
	public E3mallResult createContent(Long parentId, String name) {
		//调用服务添加节点
		E3mallResult e3mallResult = contentCategoryService.addContentCategory(parentId, name);
		return e3mallResult;
		
	}
	
	@RequestMapping("/content/query/list")
	@ResponseBody
	public EasyUIDataGridResult getContentList(Integer page, Integer rows) {
		
		EasyUIDataGridResult result = contentCategoryService.getContentList(page, rows);
		return result;
	}
	
}
