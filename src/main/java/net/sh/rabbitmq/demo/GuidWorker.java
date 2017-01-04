package net.sh.rabbitmq.demo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * 代码参考：http://www.cnblogs.com/relucent/p/4955340.html  <br>
 *
 * snowflake是Twitter开源的分布式ID生成算法，结果是一个long型的ID。  <br>
 *
 * 其核心思想是：  <br>
 * 使用41bit作为毫秒数，10bit作为机器的ID（5个bit是数据中心，5个bit的机器ID），12bit作为毫秒内的流水号
 * （意味着每个节点在每毫秒可以产生 4096 个 ID），最后还有一个符号位，永远是0。  <br>
 *
 * 具体实现的代码可以参考：https://github.com/twitter/snowflake
 *
 */
public class GuidWorker {

	private final long twepoch = 1288834974657L; //1288834974657L; //can change to: 1483199999999
	private final long workerIdBits = 5L;
	private final long datacenterIdBits = 5L;
	private final long maxWorkerId = -1L ^ (-1L << workerIdBits);
	private final long maxDatacenterId = -1L ^ (-1L << datacenterIdBits);
	private final long sequenceBits = 12L;
	private final long workerIdShift = sequenceBits;
	private final long datacenterIdShift = sequenceBits + workerIdBits;
	private final long timestampLeftShift = sequenceBits + workerIdBits + datacenterIdBits;
	private final long sequenceMask = -1L ^ (-1L << sequenceBits);

	private long workerId;
	private long datacenterId;
	private long sequence = 0L;
	private long lastTimestamp = -1L;

	/**
	 * @param workerId
	 * 				The worker id, 0-31
	 * @param datacenterId
	 * 				The datacenter id, 0-31
	 * @param lastTimeStamp
	 * 				The latest TimeStamp that permitted to start to work with.
	 * 				For example: 2016-12-22 23:59:59.999
	 * */
	public GuidWorker(long workerId, long datacenterId, String lastTimeStamp) {
		if (workerId > maxWorkerId || workerId < 0) {
			throw new IllegalArgumentException(String.format("worker Id can't be greater than %d or less than 0", maxWorkerId));
		}
		if (datacenterId > maxDatacenterId || datacenterId < 0) {
			throw new IllegalArgumentException(String.format("datacenter Id can't be greater than %d or less than 0", maxDatacenterId));
		}

		try {
			this.lastTimestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").parse(lastTimeStamp).getTime();
		} catch (ParseException e) {
			throw new IllegalArgumentException(String.format("lastTimeStamp(%d) should be like yyyy-MM-dd HH:mm:ss.SSS", lastTimeStamp));
		}

		this.workerId = workerId;
		this.datacenterId = datacenterId;
	}

	public synchronized long nextId() {
		long timestamp = timeGen();
		if (timestamp < lastTimestamp) {
			throw new RuntimeException(String.format("Clock moved backwards.  Refusing to generate id for %d milliseconds", lastTimestamp - timestamp));
		}
		if (lastTimestamp == timestamp) {
			sequence = (sequence + 1) & sequenceMask;
			if (sequence == 0) {
				timestamp = tilNextMillis(lastTimestamp);
			}
		} else {
			sequence = 0L;
		}

		lastTimestamp = timestamp;

		return ((timestamp - twepoch) << timestampLeftShift) | (datacenterId << datacenterIdShift) | (workerId << workerIdShift) | sequence;
	}

	protected long tilNextMillis(long lastTimestamp) {
		long timestamp = timeGen();
		while (timestamp <= lastTimestamp) {
			timestamp = timeGen();
		}
		return timestamp;
	}

	protected long timeGen() {
		return System.currentTimeMillis();
	}

	public static void main(String[] args) {

		GuidWorker idWorker = new GuidWorker(0, 0, "2016-12-22 23:59:59.999");
		System.out.println(idWorker.maxWorkerId);
		System.out.println(idWorker.maxDatacenterId);

		Date twepochDate = new Date(idWorker.twepoch);
		System.out.println(twepochDate);

		try {
			System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").parse("2016-12-31 23:59:59.999").getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}

		for (int i = 0; i < 1000; i++) {
			long id = idWorker.nextId();
			System.out.println(id);
		}
	}
}
