package com.roger.quartz.core;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.util.StringUtils;

import com.roger.quartz.core.model.ScheduledJob;

public class QuartzManager {

	private static SchedulerFactory schedulerFactory = new StdSchedulerFactory();

	@SuppressWarnings("unchecked")
	public static void addJob(ScheduledJob scheduledJob) {
		try {
			// step1:获取任务调度器
			Scheduler scheduler = schedulerFactory.getScheduler();
			// step2:创建作业明细
			String className = "com.roger.quartz.core.factory.QuartzJobFactory";
			if (!StringUtils.isEmpty(scheduledJob.getClazz()))
				className = scheduledJob.getClazz();
			Class<? extends Job> jobClass = (Class<? extends Job>) Class.forName(className);

			JobDetail jobDetail = JobBuilder.newJob(jobClass)//
					.withIdentity(scheduledJob.getJobName(), scheduledJob.getJobGroup())//
					.build();
			// 把当前job的基本信息保存进作业明细中
			jobDetail.getJobDataMap().put("scheduledJob", scheduledJob);

			// step3:创建触发器
			CronTrigger cronTrigger = TriggerBuilder.newTrigger()//
					.withIdentity(scheduledJob.getTriggerName(), scheduledJob.getTriggerGroup())//
					.withSchedule(CronScheduleBuilder.cronSchedule(scheduledJob.getCronExps()))//
					.build();

			// step4:使用调度器管理触发器和作业明细
			scheduler.scheduleJob(jobDetail, cronTrigger);

			// step5：如果调度器关闭，则返回
			if (scheduler.isShutdown())
				return;

			// step6:启动
			scheduler.start();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static void modifyJobTime(ScheduledJob scheduledJob) {
		try {
			// step1:获取任务调度器
			Scheduler scheduler = schedulerFactory.getScheduler();

			// step2:获取TriggerKey
			TriggerKey triggerKey = TriggerKey.triggerKey(scheduledJob.getTriggerName(),
					scheduledJob.getTriggerGroup());

			// step3：获取触发器CronTrigger,判断触发器是否存在，并且校验cronExps表达式是否一样
			CronTrigger cronTrigger = (CronTrigger) scheduler.getTrigger(triggerKey);
			if (cronTrigger == null)
				return;

			String oldCronExps = cronTrigger.getCronExpression();
			if (oldCronExps.equals(scheduledJob.getCronExps()))
				return;

			// step4:获取JobKey
			JobKey jobKey = JobKey.jobKey(scheduledJob.getJobName(), scheduledJob.getJobGroup());
			// step5：获取作业明细JobDetail
			JobDetail jobDetail = scheduler.getJobDetail(jobKey);
			Class<? extends Job> jobClass = jobDetail.getJobClass();
			scheduledJob.setClazz(jobClass.getName());

			removeJob(scheduledJob.getJobName(), scheduledJob.getJobGroup(), scheduledJob.getTriggerName(),
					scheduledJob.getTriggerGroup());
			addJob(scheduledJob);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static void modifyJobTime(String triggerName, String triggerGroupName, String cronExpression) {
		try {
			// 任务调度器
			Scheduler scheduler = schedulerFactory.getScheduler();
			// 获取TriggerKey
			TriggerKey triggerKey = TriggerKey.triggerKey(triggerName, triggerGroupName);
			// 通过TriggerKey获取CronTrigger
			CronTrigger cronTrigger = (CronTrigger) scheduler.getTrigger(triggerKey);

			if (cronTrigger == null)
				return;
			String oldCronExpression = cronTrigger.getCronExpression();

			if (oldCronExpression.equalsIgnoreCase(cronExpression))
				return;
			// 表达式调度构造器
			CronScheduleBuilder schedBuilder = CronScheduleBuilder.cronSchedule(oldCronExpression);
			cronTrigger = cronTrigger.getTriggerBuilder()//
					.withIdentity(triggerKey)//
					.withSchedule(schedBuilder)//
					.withSchedule(CronScheduleBuilder.cronSchedule(cronExpression))//
					.build();
			scheduler.rescheduleJob(triggerKey, cronTrigger);
		} catch (SchedulerException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static void removeJob(String jobName, String jobGroup, String triggerName, String triggerGroup) {
		try {
			// step1:获取任务调度器
			Scheduler scheduler = schedulerFactory.getScheduler();

			// step2:获取TriggerKey
			TriggerKey triggerKey = TriggerKey.triggerKey(triggerName, triggerGroup);

			// step3：获取JobKey
			JobKey jobKey = JobKey.jobKey(jobName, jobGroup);
			
			// step4:根据TriggerKey停止触发器和移除触发器
			scheduler.pauseTrigger(triggerKey);
			scheduler.unscheduleJob(triggerKey);
			// step5：根据JobKey删除任务
			scheduler.deleteJob(jobKey);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	
	public static void startJobs() {
		try {
			// step1:获取任务调度器
			Scheduler scheduler = schedulerFactory.getScheduler();
			// step2:启动任务
			scheduler.start();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public static void shutdownJobs() {
		try {
			// step1:获取任务调度器
			Scheduler scheduler = schedulerFactory.getScheduler();
			// step2:关闭任务
			if(scheduler.isShutdown())
				return;
			
			scheduler.shutdown();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
