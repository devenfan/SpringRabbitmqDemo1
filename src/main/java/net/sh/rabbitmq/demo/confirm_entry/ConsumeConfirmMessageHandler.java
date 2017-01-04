package net.sh.rabbitmq.demo.confirm_entry;

import net.sh.rabbitmq.demo.AmqpMessageHandler;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;

import java.io.UnsupportedEncodingException;

/**
 * Created by codyy on 2017/1/3.
 */
public class ConsumeConfirmMessageHandler implements AmqpMessageHandler {

	public void onMessage(Message message) {

//		System.out.println("Sleep before receive...");
//
//		try {
//			Thread.sleep(5000);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//
//		System.out.println("Wake up before receive...");

		try {
			//使用fastJson将数据对象转换为json数据
			String receiveMsg = new String(message.getBody(),"utf-8");
			System.out.println("RECV msg:" + receiveMsg);


		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

//		System.out.println("Sleep after receive...");
//
//		try {
//			Thread.sleep(5000);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//
//		System.out.println("Wake up after receive...");
	}
}
