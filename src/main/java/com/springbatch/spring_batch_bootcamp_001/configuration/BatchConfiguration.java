package com.springbatch.spring_batch_bootcamp_001.configuration;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class BatchConfiguration {
    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Bean
    public Step startStep() {
        return stepBuilderFactory.get("startStep")
                        .tasklet(((stepContribution, chunkContext) -> {
                            System.out.println("This is the start tasklet");
                            return RepeatStatus.FINISHED;
                        })).build();
    }

    @Bean
    public Step evenStep() {
        return stepBuilderFactory.get("evenStep")
                .tasklet(((stepContribution, chunkContext) -> {
                    System.out.println("This is the even tasklet");
                    return RepeatStatus.FINISHED;
                })).build();
    }

    @Bean
    public Step oddStep() {
        return stepBuilderFactory.get("oddStep")
                .tasklet(((stepContribution, chunkContext) -> {
                    System.out.println("This is the odd tasklet");
                    return RepeatStatus.FINISHED;
                })).build();
    }

    @Bean
    public JobExecutionDecider decider(){
        return new OddDecider();
    }

    //When running the decider, we should specify all the other return values
    //that it will return and step associated based on the return value

    //the below decider will run the startStep, then execute the odd step, then
    //finally the even step

    @Bean
    public Job job(){
        return jobBuilderFactory.get("job")
                .start(startStep())
                .next(decider())
                .from(decider()).on("ODD").to(oddStep())
                .from(decider()).on("EVEN").to(evenStep())
                .from(oddStep()).on("*").to(decider())
//                .from(decider()).on("ODD").to(oddStep())
//                .from(decider()).on("EVEN").to(evenStep())
                .end().build();
    }

    //a decider is something I can run to establish a decision
    //Decider takes in two params:jobExecution, stepExecution (previous step executed before this decider)
    public static class OddDecider implements JobExecutionDecider {
        private int count = 0;

        @Override
        public FlowExecutionStatus decide(JobExecution jobExecution, StepExecution stepExecution) {
            count++;

            if (count % 2 == 0) {
                return new FlowExecutionStatus("EVEN");
            } else {
                return new FlowExecutionStatus("ODD");

            }
        }
    }
}
