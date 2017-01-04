package net.sh.rabbitmq.demo;

/**
 * Created by codyy on 2017/1/3.
 */
public class DummyMessageProducer implements AmqpMessageProducer {

	public void sendMessage(Object msg) {
		System.out.println("DUMMY SEND MSG: " + msg);
	}
}
