package net.sh.rabbitmq.demo.normal_entry;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

/**
 * Created by codyy on 2017/1/3.
 */
public class ConsumerMain {

    public static void main(String[] args) {
        ApplicationContext context =
                new GenericXmlApplicationContext("applicationContext-rabbitmq-consumer.xml");
    }

}
