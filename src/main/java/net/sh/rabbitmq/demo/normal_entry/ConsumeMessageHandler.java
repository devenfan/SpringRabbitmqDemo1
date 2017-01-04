package net.sh.rabbitmq.demo.normal_entry;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;

import java.io.UnsupportedEncodingException;

/**
 * Created by codyy on 2017/1/3.
 */
public class ConsumeMessageHandler implements MessageListener {

	public void onMessage(Message message) {

		try {
			//使用fastJson将数据对象转换为json数据
			String receiveMsg = new String(message.getBody(),"utf-8");
			System.out.println("RECV MSG: " + receiveMsg);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
}
