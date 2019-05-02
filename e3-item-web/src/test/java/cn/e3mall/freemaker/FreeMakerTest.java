package cn.e3mall.freemaker;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import freemarker.template.Configuration;
import freemarker.template.Template;

public class FreeMakerTest {
	
	@Test
	public void testFreeMaker() throws Exception {
		//创建一个模板文件
		//创建一个Configuration对象
		Configuration configuration = new Configuration(Configuration.getVersion());
		//设置模板文件保存的目录
		configuration.setDirectoryForTemplateLoading(new File("E:\\eclipse-workspace\\e3-item-web\\src\\main\\webapp\\WEB-INF\\ftl"));
		//模板文件的编码格式，一般就是utf-8
		configuration.setDefaultEncoding("utf-8");
		//加载一个模板文件，创建一个模板对象
		Template template = configuration.getTemplate("student.ftl");
		//创建一个数据集。可以使pojo，也可以是map，推荐用map
		Map data = new HashMap<>();
		data.put("hello", "hello freemaker!");
		//创建一个pojo对象
		Student student = new Student(1, "zhangsan", 18, "Ali");
		data.put("student", student);
		//添加一个List
		List<Student> stuList = new ArrayList<>();
		stuList.add(new Student(2, "lisi", 21, "AliPay"));
		stuList.add(new Student(3, "lisi1", 12, "AliPay1"));
		stuList.add(new Student(4, "lisi2", 32, "AliPay3"));
		stuList.add(new Student(5, "lisi3", 52, "AliPay2"));
		stuList.add(new Student(6, "lisi4", 26, "AliPay4"));
		stuList.add(new Student(7, "lisi5", 22, "AliPay5"));
		stuList.add(new Student(8, "lisi6", 29, "AliPay6"));
		stuList.add(new Student(9, "lisi7", 31, "AliPay7"));
		data.put("stuList", stuList);
		//添加日期类型
		data.put("date", new Date());
		//null值的测试
		data.put("val", null);
		//创建一个writer对象，指定输出文件的路径和文件名
		Writer out = new FileWriter(new File("E:\\eclipse-workspace\\freemaker\\student.html"));
		//生成静态页面
		template.process(data, out);
		//关闭流
		out.close();
	}

}
