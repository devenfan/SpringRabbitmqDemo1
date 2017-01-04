package net.sh.rabbitmq.demo.normal_entry;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by codyy on 2017/1/3.
 */
public class ProducerMain {

    public static void main(String[] args) throws InterruptedException {

        ApplicationContext context =
                new GenericXmlApplicationContext("applicationContext-rabbitmq-producer.xml");

        AmqpTemplate template = (AmqpTemplate) context.getBean("amqpTemplate");

        int number = 0;
        String msg = null;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        do {
            msg = "hello(" + (number++) + ") " + format.format(new Date());
            sendMessage(template, msg);
        }
        while(number < 100);

    }

    private static void sendMessage(AmqpTemplate template, String msg) {

        {
            //direct模式：接收routing-key=queue_one_key的消息
            //template.convertAndSend("queue_one_key", "hello!");

            //topic模式：以foo.* routing-key为模版接收消息
            template.convertAndSend("foo.bar", msg);

            //fanout模式：在集群范围内的所有consumer都会收到消息
//            template.convertAndSend(msg);

            System.out.println("SEND MSG: " + msg);
        }

    }
}
