package com.env.qualitymetrics.tasks;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

public class DailyQuartz extends QuartzJobBean{

	private DailyTask dailyTask;

	@Override
	public void executeInternal(JobExecutionContext arg0)
			throws JobExecutionException {
		dailyTask.intervalCheck();
	}

	public DailyTask getDailyTask() {
		return dailyTask;
	}

	public void setDailyTask(DailyTask dailyTask) {
		this.dailyTask = dailyTask;
	}
}
