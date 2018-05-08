package com.roger.quartz.job;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
public class SimpleJob {

	private static Logger logger = Logger.getLogger(SimpleJob.class);
	private int times = 0;

	public void run() {
		logger.info("---开始执行SimpleJob");
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		String time = sdf.format(new Date());
		System.out.println("【" + time + "】简单任务第【" + ++times + "】次执行……");
	}
}
