package com.roger.quartz.job;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

public class CronJob extends QuartzJobBean{

	private static Logger logger = Logger.getLogger(SimpleJob.class);
	private static int times = 0;
	
	@Override
	public void executeInternal(JobExecutionContext arg0) throws JobExecutionException {
		logger.info("---开始执行CronJob");
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String time = sdf.format(new Date());
        System.out.println("【" + time + "】定时任务第【" + ++times + "】次执行……");
	}

}
