package net.sh.rabbitmq.demo.confirm_entry;

import com.sun.org.apache.xpath.internal.SourceTree;
import net.sh.rabbitmq.demo.AmqpMessageProducer;
import net.sh.rabbitmq.demo.GuidWorker;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.retry.RepublishMessageRecoverer;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * Created by codyy on 2017/1/3.
 */
//@Component
public class ProduceConfirmProcessor implements AmqpMessageProducer {


//	private AmqpTemplate amqpTemplate;
	private RabbitTemplate amqpTemplate;

	private RabbitTemplate errorTemplate;

	private GuidWorker guidWorker;

	public void setAmqpTemplate(RabbitTemplate amqpTemplate) {

		this.amqpTemplate = amqpTemplate;

		//确认消息是否到达broker服务器，也就是只确认是否正确到达exchange中即可，只要正确的到达exchange中，broker即可确认该消息返回给客户端ack。
		this.amqpTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback(){
			@Override
			public void confirm(CorrelationData correlationData, boolean ack, String cause) {
				if (ack) {
					System.out.println("消息(" + correlationData + ")确认成功");
				} else {
					//处理丢失的消息（nack）
					System.out.println("消息(" + correlationData + ")确认失败: " + cause);
				}
//				System.out.println("correlationData: " + correlationData);
//				System.out.println("cause: " + cause);
			}
		});

		//使用return-callback时必须设置mandatory为true，或者在配置中设置mandatory-expression的值为true，
		//可针对每次请求的消息去确定’mandatory’的boolean值，只能在提供’return -callback’时使用，与mandatory互斥。
		this.amqpTemplate.setMandatory(true);

		//确认消息是否到达broker服务器，也就是只确认是否正确到达exchange中即可，只要正确的到达exchange中，broker即可确认该消息返回给客户端ack。
		this.amqpTemplate.setReturnCallback(new RabbitTemplate.ReturnCallback() {

			public void returnedMessage(Message message, int replyCode, String replyText,
					String exchange, String routingKey) {

				//重新发布
//				RepublishMessageRecoverer recoverer = new RepublishMessageRecoverer(errorTemplate, "errorExchange", "errorRoutingKey");
//				Throwable cause = new Exception(new Exception("route_fail_and_republish"));
//				recoverer.recover(message, cause);
				System.out.println("Returned Message： " + replyText);
			}
		});
	}


	public void setErrorTemplate(RabbitTemplate errorTemplate) {
		this.errorTemplate = errorTemplate;
	}

	public void setGuidWorker(GuidWorker guidWorker) {
		this.guidWorker = guidWorker;
	}

	//	@Transactional
	@Override
	public void sendMessage(Object msg) {
//		System.out.println("Sleep before send...");
//
//		try {
//			Thread.sleep(5000);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//
//		System.out.println("Wake up before send...");

		long msgId = guidWorker.nextId();

		{
			//direct模式：接收routing-key=queue_one_key的消息
			amqpTemplate.convertAndSend(null, msg, new CorrelationData(String.valueOf(msgId)));

			//topic模式：以foo.* routing-key为模版接收消息
			//template.convertAndSend("foo.bar", msg);

			//fanout模式：在集群范围内的所有consumer都会收到消息
//			amqpTemplate.convertAndSend(msg);

			System.out.println("SEND MSG(" + msgId + "): " + msg);
		}

//		System.out.println("Sleep after send...");
//
//		try {
//			Thread.sleep(5000);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//
//		System.out.println("Wake up after send...");

		//		throw new RuntimeException("Let's see what happened!");
	}
}
