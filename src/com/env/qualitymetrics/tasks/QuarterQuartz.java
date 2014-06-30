package com.env.qualitymetrics.tasks;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

public class QuarterQuartz extends QuartzJobBean{
	private QuarterTask quarterTask;
	@Override
	protected void executeInternal(JobExecutionContext arg0)
			throws JobExecutionException {
		quarterTask.CollectRankings();
	}
	public QuarterTask getQuarterTask() {
		return quarterTask;
	}
	public void setQuarterTask(QuarterTask quarterTask) {
		this.quarterTask = quarterTask;
	}

}
