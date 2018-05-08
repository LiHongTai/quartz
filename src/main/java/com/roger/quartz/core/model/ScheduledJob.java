package com.roger.quartz.core.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScheduledJob implements Serializable{

	private static final long serialVersionUID = -4853674957760684247L;
	
	private String jobId;
	private String jobName;
	private String jobGroup;
	private String triggerName;
	private String triggerGroup;
	private String cronExps;
	/**
	 * job类 包含包名，即类的全名
	 */
	private String clazz;
	/**
	 * 利用SpringContextUtil 获取实现类，执行定时任务
	 */
	private String beanName;
	/**
	 * 任务状态 0禁用 1启用 2删除
	 */
	private String jobStatus;
	private String jobDesc;
}
