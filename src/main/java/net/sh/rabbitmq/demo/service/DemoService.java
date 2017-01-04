package net.sh.rabbitmq.demo.service;

import net.sh.rabbitmq.demo.AmqpMessageProducer;
import net.sh.rabbitmq.demo.dao.DemoMapper;
import net.sh.rabbitmq.demo.entity.DemoEntity;
import net.sh.rabbitmq.demo.entity.DemoExample;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * Created by codyy on 2017/1/3.
 */
@Transactional
@Service
public class DemoService {

	private static Logger logger  = Logger.getLogger(DemoService.class);

	@Autowired
	private DemoMapper demoMapper;

	@Autowired
	AmqpMessageProducer messageProducer;

	SimpleDateFormat format1 = new SimpleDateFormat("yyyyMM-ddHHmmssSSS");
	SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

	Random random = new Random();

	@Transactional
	public void doProduceOperation(int id) {

		Date now = new Date();

		DemoEntity entity = new DemoEntity();
		entity.setName("demo-" + id);
		entity.setT1(now);
		entity.setT2(now);

		int ret = demoMapper.insertSelective(entity);
		if(ret > 0) {
			logger.warn("DB INSERT OK");
		} else {
			logger.warn("DB INSERT FAIL");
			throw new RuntimeException("DB INSERT FAIL");
		}

		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		{
			//String msg = "persist(" + id + ") " + format2.format(now);

			//数据库操作中如果失败了，发送方的outgoing这条消息不会被发送，接收方incoming消息也会返回到broker服务器中，因为这是一条事务链。
			messageProducer.sendMessage(entity);
			logger.warn("MSG(" + id + ") " + "SEND OK");
		}

		if(random.nextInt(10) % 2 == 1) {
			throw new RuntimeException("INSERT TX EXCEPTION");
		} else {
			logger.warn("INSERT TX COMMIT");
		}

//		logger.warn("TX COMMITED");
	}

	@Transactional
	public void doConsumeOperation(String name) {

		Date now = new Date();

		DemoExample example = new DemoExample();
		DemoExample.Criteria criteria = example.createCriteria();
		criteria.andNameEqualTo(name);

		int ret = demoMapper.deleteByExample(example);
		if(ret > 0) {
			logger.warn("DB DELETE OK");
		} else {
			logger.warn("DB DELETE FAIL");
			throw new RuntimeException("DB DELETE FAIL");
		}

		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		if(random.nextInt(10) % 2 == 1) {
			throw new RuntimeException("DELETE TX EXCEPTION");
		} else {
			logger.warn("DELETE TX COMMIT");
		}

	}
}
