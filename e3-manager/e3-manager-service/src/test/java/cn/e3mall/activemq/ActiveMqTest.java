package cn.e3mall.activemq;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.junit.Test;

public class ActiveMqTest {
	
	/*
	 * *点到点形式发送消息
	 */
	@Test
	public void testQueueProducer() throws Exception{
		//1、创建一个连接工厂对象，需要制定服务的ip及端口
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.25.128:61616");
		//2、使用工厂对象创建connection对象
		Connection connection = connectionFactory.createConnection();
		//3、开启连接，调用start方法
		connection.start();
		//4、创建一个session对象
		//第一个参数：是否开启事务，如果true开启事务；第二个参数无意义。一般不开启事务false
		//第二个参数：应答模式。自动应答或者手动应答，一般自动应答
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		//5、使用session对象创建一个Destination对象。两种形式queue、topic，当前应该使用queue
		Queue queue = session.createQueue("test-queue");
		//6、使用session对象创建一个producer对象
		MessageProducer producer = session.createProducer(queue);
		//7、创建一个message对象，可以使用TextMessage
		TextMessage textMessage = new ActiveMQTextMessage();
		textMessage.setText("hello activemq!");
		/*session.createTextMessage("hello activemq!");*/
		//8、发送消息
		producer.send(textMessage);
		//9、关闭资源
		producer.close();
		session.close();
		connection.close();
		
	}
	
	@Test
	public void testQueueConsumer() throws Exception {
		//1.创建一个connectionFactory对象连接mq服务器
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.25.128:61616");
		//2.创建一个连接对象
		Connection connection = connectionFactory.createConnection();
		//3.开启连接
		connection.start();
		//4.使用Connection对象创建一个session对象
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		//5.创建一个Destination对象queue
		Queue queue = session.createQueue("spring-queue");
		//6.使用session对象创建消费者对象
		MessageConsumer consumer = session.createConsumer(queue);
		//7.接收消息
		consumer.setMessageListener(new MessageListener() {
			
			@Override
			public void onMessage(Message message) {
				// TODO Auto-generated method stub
				//8.打印结果
				TextMessage textMessage = (TextMessage)message;
				try {
					String text = textMessage.getText();
					System.out.println(text);
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
		});
		//等待接收消息
		System.in.read();
		//9.关闭资源
		consumer.close();
		session.close();
		connection.close();
	}
	
	@Test
	public void testTopicProducer() throws Exception{
		//1、创建一个连接工厂对象，需要制定服务的ip及端口
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.25.128:61616");
		//2、使用工厂对象创建connection对象
		Connection connection = connectionFactory.createConnection();
		//3、开启连接，调用start方法
		connection.start();
		//4、创建一个session对象
		//第一个参数：是否开启事务，如果true开启事务；第二个参数无意义。一般不开启事务false
		//第二个参数：应答模式。自动应答或者手动应答，一般自动应答
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		//5、使用session对象创建一个Destination对象。两种形式queue、topic，当前应该使用topic
		Topic topic = session.createTopic("test-topic");
		//6、使用session对象创建一个producer对象
		MessageProducer producer = session.createProducer(topic);
		//7、创建一个message对象，可以使用TextMessage
		TextMessage textMessage = new ActiveMQTextMessage();
		textMessage.setText("hello activemq topic!");
		/*session.createTextMessage("hello activemq!");*/
		//8、发送消息
		producer.send(textMessage);
		//9、关闭资源
		producer.close();
		session.close();
		connection.close();
		
	}
	
	@Test
	public void testTopicConsumer() throws Exception {
		//1.创建一个connectionFactory对象连接mq服务器
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.25.128:61616");
		//2.创建一个连接对象
		Connection connection = connectionFactory.createConnection();
		//3.开启连接
		connection.start();
		//4.使用Connection对象创建一个session对象
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		//5.创建一个Destination对象topic
		Topic topic = session.createTopic("test-topic");
		//6.使用session对象创建消费者对象
		MessageConsumer consumer = session.createConsumer(topic);
		//7.接收消息
		consumer.setMessageListener(new MessageListener() {
			
			@Override
			public void onMessage(Message message) {
				// TODO Auto-generated method stub
				//8.打印结果
				TextMessage textMessage = (TextMessage)message;
				try {
					String text = textMessage.getText();
					System.out.println(text);
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
		});
		System.out.println("comsumer b....");
		//等待接收消息
		System.in.read();
		//9.关闭资源
		consumer.close();
		session.close();
		connection.close();
	}

}
