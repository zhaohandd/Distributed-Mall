package cn.e3mall.fastDFS;

import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.StorageServer;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.junit.Test;

import cn.e3mall.common.utils.FastDFSClient;

public class FastDFSTest {
	
	@Test
	public void testUpload() throws Exception {
		//创建配置文件,文件名任意,内容为tracker服务器地址
		//使用全局对象加载配置文件,绝对路径
		ClientGlobal.init("E:/eclipse-workspace/e3-manager-web/src/main/resources/conf/client.conf");
		//创建一个TrackClient对象
		TrackerClient trackerClient = new TrackerClient();
		//通过TrackClient获得一个TrackServer对象
		TrackerServer trackerServer = trackerClient.getConnection();
		//创建一个StorageServer的引用，可以是null
		StorageServer storageServer = null;
		//创建一个StorageClient，参数需要TrackServer和StorageServer
		StorageClient storageClient = new StorageClient(trackerServer, storageServer);
		//使用StorageClient上传文件
		String[] strings = storageClient.upload_file("C:\\Users\\Public\\Pictures\\Sample Pictures\\1.jpg", "jpg", null);
		for (String string : strings) {
			System.out.println(string);
		}
	}
	
	@Test
	public void testFastDFSClient() throws Exception{
		FastDFSClient fastDFSClient = new FastDFSClient("E:/eclipse-workspace/e3-manager-web/src/main/resources/conf/client.conf");
		String string = fastDFSClient.uploadFile("C:\\Users\\Public\\Pictures\\Sample Pictures\\2.jpg");
		System.out.println(string);
	}

}
