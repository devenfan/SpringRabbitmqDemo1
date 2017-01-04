package net.sh.rabbitmq.demo.confirm_entry;

import net.sh.rabbitmq.demo.service.DemoService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by codyy on 2017/1/3.
 */
public class ProducerConfirmMain {

    public static void main(String[] args) {

        ApplicationContext context = new GenericXmlApplicationContext(
                "applicationContext-spring-common.xml",
                "applicationContext-rabbitmq-producer-confirm.xml");

        try {
            test1(context);
//            test2(context);
        } catch (Exception ex){
            ex.printStackTrace();
            System.exit(0);
        }

    }


    private static void test1(ApplicationContext context) {
        ProduceConfirmProcessor producer = (ProduceConfirmProcessor) context.getBean("produceConfirmProcessor");

        int number = 1;
        String msg = null;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        do {
            msg = "hello(" + (number++) + ") " + format.format(new Date());
            producer.sendMessage(msg);
        }
        while(number < 10);
    }


    private static void test2(ApplicationContext context) {
        DemoService demoService = (DemoService) context.getBean("demoService");

        int number = 1;
        do {
            demoService.doProduceOperation(number++);
        }
        while(number < 10);
    }


}
