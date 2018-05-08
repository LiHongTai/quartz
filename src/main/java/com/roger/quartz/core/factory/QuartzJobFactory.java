package com.roger.quartz.core.factory;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.roger.quartz.core.model.ScheduledJob;

public class QuartzJobFactory implements Job {

	private Logger logger = Logger.getLogger(QuartzJobFactory.class);
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		logger.info("任务成功运行");
		ScheduledJob scheduleJob = (ScheduledJob) context.getMergedJobDataMap().get("scheduleJob");
		System.out.println("任务名称 = [" + scheduleJob.getJobName() + "]");
	}

}
