package com.lab.helloworld.listners;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.stereotype.Component;

@Component
public class HelloWorldStepExecutionListner implements StepExecutionListener {

	@Override
	public void beforeStep(StepExecution stepExecution) {
		System.out.println("before step execution "+ stepExecution.getExecutionContext().toString());
		stepExecution.getExecutionContext().put("mystep", "1");
	}

	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		System.out.println("after step execution "+ stepExecution.getExecutionContext().toString());
		return null;
	}

}
