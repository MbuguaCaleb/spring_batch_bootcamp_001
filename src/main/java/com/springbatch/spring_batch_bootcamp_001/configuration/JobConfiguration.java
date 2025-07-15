
package com.springbatch.spring_batch_bootcamp_001.configuration;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * @author Michael Minella
 */
@Configuration
public class JobConfiguration {

	@Autowired
	public JobBuilderFactory jobBuilderFactory;

	@Autowired
	public StepBuilderFactory stepBuilderFactory;

	//Restart
	//Job repository is used to maintain the state of the Job as it is being executed
	//When something goes wrong,this allows the job to start from where it left off

	//By default, if an uncaught exception is found,Spring Batch will end processing of the JOB
	//If the Job is restarted with the same params, spring batch will restart from where it left off

	//step scope means each step is going to run its seperate instance
	@Bean
	@StepScope
	public Tasklet restartTasklet(){
		return new Tasklet() {
			@Override
			public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
				Map<String, Object> stepExecutionContext = chunkContext.getStepContext().getStepExecutionContext();

				if(stepExecutionContext.containsKey("ran")) {
					System.out.println("This time we'll let it go.");
					return RepeatStatus.FINISHED;
				}
				else {
					System.out.println("I don't think so...");
					chunkContext.getStepContext().getStepExecution().getExecutionContext().put("ran", true);
					throw new RuntimeException("Not this time...");
				}
			}
		};
	}

	@Bean
	public Step step1(){
		return stepBuilderFactory.get("step1")
				.tasklet(restartTasklet())
				.build();
	}

	@Bean
	public Step step2(){
		return stepBuilderFactory.get("step2")
				.tasklet(restartTasklet())
				.build();
	}

	@Bean
	public Job job() {
		return jobBuilderFactory.get("jobRestart")
				.start(step1())
				.next(step2())
				.build();
	}

}
