package com.roger.quartz.job;

import org.junit.Before;
import org.junit.Test;

import com.roger.quartz.BaseTestSuitCase;
import com.roger.quartz.core.QuartzManager;
import com.roger.quartz.core.model.ScheduledJob;

public class DynamicJobTest extends BaseTestSuitCase {

	
	@Before
	public void setUp() {
		ScheduledJob scheduledJob = new ScheduledJob();
		scheduledJob.setJobName("jobName");
		scheduledJob.setJobGroup("jobGroupName");
		scheduledJob.setTriggerName("triggerName");
		scheduledJob.setTriggerGroup("triggerGroupName");
		scheduledJob.setClazz("com.roger.quartz.core.factory.QuartzJobFactory");
		scheduledJob.setCronExps("0/20 * * * * ?");
		QuartzManager.addJob(scheduledJob);
	}
	
	@Test
	public void testDynamicJob() {
		while(true) {
			
		}
	}
}
