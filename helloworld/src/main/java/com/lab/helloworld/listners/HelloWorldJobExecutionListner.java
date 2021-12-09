package com.lab.helloworld.listners;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;

@Component
public class HelloWorldJobExecutionListner implements JobExecutionListener {

	@Override
	public void beforeJob(JobExecution jobExecution) {
		System.out.println("Before job execution"+jobExecution.getJobInstance().getJobName());
		System.out.println("Before job execution"+jobExecution.getExecutionContext().toString());
		jobExecution.getExecutionContext().put("my name ", "Rajveer");
	}

	@Override
	public void afterJob(JobExecution jobExecution) {
		System.out.println("after job execution"+jobExecution.getExecutionContext().toString());
	}
	
}
