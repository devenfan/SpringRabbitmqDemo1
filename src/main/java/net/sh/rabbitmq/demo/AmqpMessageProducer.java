package net.sh.rabbitmq.demo;

import org.springframework.validation.ObjectError;

/**
 * Created by codyy on 2017/1/3.
 */
public interface AmqpMessageProducer {

	void sendMessage(Object msg);
}
