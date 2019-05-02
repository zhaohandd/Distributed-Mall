package cn.e3mall.solrj;

import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

public class TestSolrj {
	
	@Test
	public void addDocument() throws Exception {
		//创建一个solrserver对象，创建一个连接。参数solr服务的url
		SolrServer solrServer = new HttpSolrServer("http://192.168.25.128:8081/solr/collection1");
		//创建一个文档对象，solrinputdocument
		SolrInputDocument document = new SolrInputDocument();
		//向文档对象中添加域。文档中必须包含一个id域，所有的域的名称必须在scheme.xml中定义
		document.addField("id", "doc01");
		document.addField("item_title", "测试商品01");
		document.addField("item_price", 999);
		//把文档写入索引库
		solrServer.add(document);
		//提交
		solrServer.commit();
	}
	
	@Test
	public void delDocument() throws Exception {
		//创建一个solrserver对象，创建一个连接。参数solr服务的url
		SolrServer solrServer = new HttpSolrServer("http://192.168.25.128:8081/solr/collection1");
		//删除文档
		solrServer.deleteById("doc01");
		//提交
		solrServer.commit();
	}
	
	@Test
	public void queryIndex() throws Exception{
		//创建一个solrserver对象
		SolrServer solrServer = new HttpSolrServer("http://192.168.25.128:8081/solr/collection1");
		//创建一个solrquery对象
		SolrQuery solrQuery = new SolrQuery();
		//设置查询条件
		solrQuery.setQuery("*:*");
		//执行查询，queryresponse对象
		QueryResponse queryResponse = solrServer.query(solrQuery);
		//取文档列表。取查询结果的总记录数
		SolrDocumentList results = queryResponse.getResults();
		long num = results.getNumFound();
		System.out.println("总数" + num);
		//遍历文档列表，从取域的内容
		for (SolrDocument solrDocument : results) {
			System.out.println(solrDocument.get("id"));
			System.out.println(solrDocument.get("item_title"));
			System.out.println(solrDocument.get("item_sell_point"));
			System.out.println(solrDocument.get("item_price"));
			System.out.println(solrDocument.get("item_image"));
			System.out.println(solrDocument.get("item_category_name"));
			System.out.println("__________________________________");
		}
	}
	
	@Test
	public void queryIndexComplicate() throws Exception{
		//创建一个solrserver对象
		SolrServer solrServer = new HttpSolrServer("http://192.168.25.128:8081/solr/collection1");
		//创建一个solrquery对象
		SolrQuery solrQuery = new SolrQuery();
		//设置查询条件
		solrQuery.setQuery("手机");
		solrQuery.setStart(0);
		solrQuery.setRows(20);
		solrQuery.set("df", "item_title");
		solrQuery.setHighlight(true);
		solrQuery.addHighlightField("item_title");
		solrQuery.setHighlightSimplePre("<em>");
		solrQuery.setHighlightSimplePost("</em>");
		//执行查询，queryresponse对象
		QueryResponse queryResponse = solrServer.query(solrQuery);
		//取文档列表。取查询结果的总记录数
		SolrDocumentList results = queryResponse.getResults();
		long num = results.getNumFound();
		System.out.println("总数" + num);
		//遍历文档列表，从取域的内容
		for (SolrDocument solrDocument : results) {
			System.out.println(solrDocument.get("id"));
			//取高亮显示
			Map<String, Map<String, List<String>>> highlighting = queryResponse.getHighlighting();
			List<String> list = highlighting.get(solrDocument.get("id")).get("item_title");
			String title = "";
			if (list != null && list.size() > 0) {
				title = list.get(0);
			}else {
				title = (String)solrDocument.get("item_title");
			}
			System.out.println(title);
			System.out.println(solrDocument.get("item_sell_point"));
			System.out.println(solrDocument.get("item_price"));
			System.out.println(solrDocument.get("item_image"));
			System.out.println(solrDocument.get("item_category_name"));
			System.out.println("__________________________________");
		}
	}

}
