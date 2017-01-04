package net.sh.rabbitmq.demo.tx_entry;

import net.sh.rabbitmq.demo.service.DemoService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by codyy on 2017/1/3.
 */
public class ProducerTxMain {

    public static void main(String[] args) {

        ApplicationContext context = new GenericXmlApplicationContext("applicationContext-spring-common.xml", "applicationContext-rabbitmq-producer-tx.xml");

        try {
            test2(context);
        } catch (Exception ex){
            ex.printStackTrace();
            System.exit(0);
        }

    }


    private static void test2(ApplicationContext context) {
        DemoService demoService = (DemoService) context.getBean("demoService");

        int number = 1;
        do {
            try {
                demoService.doProduceOperation(number++);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        while(number < 10);
    }


}
