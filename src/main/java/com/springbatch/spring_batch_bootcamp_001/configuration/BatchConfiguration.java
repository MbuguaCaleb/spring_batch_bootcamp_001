package com.springbatch.spring_batch_bootcamp_001.configuration;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;


@Configuration
public class BatchConfiguration {
    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Bean
    public Tasklet tasklet() {
        return new CountingTasklet();
    }


    //Remember a flow is just like a Job because it takes in a Step
    //step has been constructed inline
    @Bean
    public Flow flow1() {
        return new FlowBuilder<Flow>("flow1")
                .start(stepBuilderFactory.get("step1")
                        .tasklet(tasklet()).build())
                .build();
    }


    //Flow two is executing two flows in sequence
    @Bean
    public Flow flow2() {
        return new FlowBuilder<Flow>("flow2")
                .start(stepBuilderFactory.get("step2")
                        .tasklet(tasklet()).build())
                .next(stepBuilderFactory.get("step3")
                        .tasklet(tasklet()).build())
                .build();
    }


    //Parraleziation within the Job by Using Split
    //i am executing flow one in parallel via asynctask executor and on it adding flow two
    //the executor can take any number of flows and execute them in parallel
    @Bean
    public Job job() {
        return jobBuilderFactory.get("job")
                .start(flow1())
                .split(new SimpleAsyncTaskExecutor()).add(flow2())
                .end()
                .build();
    }

    //job takes in a Step
    //A Step of tasklet type
    public static class CountingTasklet implements Tasklet {
        @Override
        public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
            System.out.println(String.format("%s has been executed on thread %s", chunkContext.getStepContext().getStepName(), Thread.currentThread().getName()));
            return RepeatStatus.FINISHED;
        }
    }
}
