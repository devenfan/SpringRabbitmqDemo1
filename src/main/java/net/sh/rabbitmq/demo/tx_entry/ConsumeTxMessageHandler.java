package net.sh.rabbitmq.demo.tx_entry;

import com.alibaba.fastjson.JSON;
import net.sh.rabbitmq.demo.AmqpMessageHandler;
import net.sh.rabbitmq.demo.entity.DemoEntity;
import net.sh.rabbitmq.demo.service.DemoService;
import org.springframework.amqp.core.Message;

import java.io.UnsupportedEncodingException;

/**
 * Created by codyy on 2017/1/3.
 */
public class ConsumeTxMessageHandler implements AmqpMessageHandler {

	private DemoService demoService;

	public void setDemoService(DemoService demoService) {
		this.demoService = demoService;
	}

	public void onMessage(Message message) {

		System.out.println("Sleep before receive...");

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		System.out.println("Wake up before receive...");

		String receiveMsg = null;
		DemoEntity demoEntity = null;

		try {
			receiveMsg = new String(message.getBody(),"utf-8");
			System.out.println("RECV msg:" + receiveMsg);

			demoEntity = JSON.parseObject(receiveMsg, DemoEntity.class);

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		{
			demoService.doConsumeOperation(demoEntity.getName());
		}

		System.out.println("Sleep after receive...");

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		System.out.println("Wake up after receive...");
	}
}
