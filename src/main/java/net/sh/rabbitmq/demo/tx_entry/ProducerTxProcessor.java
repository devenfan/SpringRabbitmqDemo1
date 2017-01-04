package net.sh.rabbitmq.demo.tx_entry;

import net.sh.rabbitmq.demo.AmqpMessageProducer;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * Created by codyy on 2017/1/3.
 */
//@Component
public class ProducerTxProcessor implements AmqpMessageProducer {


//	@Autowired
//	@Qualifier("amqpTemplate2")
	private AmqpTemplate amqpTemplate;

	public void setAmqpTemplate(AmqpTemplate amqpTemplate) {
		this.amqpTemplate = amqpTemplate;
	}

//	@Transactional
	public void sendMessage(Object msg) {

		System.out.println("Sleep before send...");

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		System.out.println("Wake up before send...");

		{
			//direct模式：接收routing-key=queue_one_key的消息
			//template.convertAndSend("queue_one_key", "hello!");

			//topic模式：以foo.* routing-key为模版接收消息
			//template.convertAndSend("foo.bar", msg);

			//fanout模式：在集群范围内的所有consumer都会收到消息
			amqpTemplate.convertAndSend(msg);
			System.out.println("SEND MSG: " + msg);
		}

		System.out.println("Sleep after send...");

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		System.out.println("Wake up after send...");

//		throw new RuntimeException("Let's see what happened!");

	}
}
