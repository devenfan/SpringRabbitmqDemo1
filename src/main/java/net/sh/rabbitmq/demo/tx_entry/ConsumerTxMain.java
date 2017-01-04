package net.sh.rabbitmq.demo.tx_entry;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

/**
 * Created by codyy on 2017/1/3.
 */
public class ConsumerTxMain {

    public static void main(String[] args) {
        ApplicationContext context = new GenericXmlApplicationContext("applicationContext-spring-common.xml", "applicationContext-rabbitmq-consumer-tx.xml");
    }

}
