package com.springbatch.spring_batch_bootcamp_001.configuration;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StepTransitionConfigurationLesson02 {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Bean
    public Step step1() {
        return stepBuilderFactory.get("step1")
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
                        System.out.println(">>This is Step One");
                        return RepeatStatus.FINISHED;
                    }
                }).build();
    }

    @Bean
    public Step step2() {
        return stepBuilderFactory.get("step2")
                .tasklet((stepContribution, chunkContext) -> {
                    System.out.println(">>This is Step Two");
                    return RepeatStatus.FINISHED;
                }).build();
    }

    @Bean
    public Step step3() {
        return stepBuilderFactory.get("step3")
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
                        System.out.println(">>This is Step Threeeee");
                        return RepeatStatus.FINISHED;
                    }
                }).build();
    }


    //This is How transition between Jobs is done
    //Linear Step execution configuration
    /*
    @Bean
    public Job transitionJobSimpleNext(){
        return jobBuilderFactory.get("transitionJobNext")
                .start(step1())
                .next(step2())
                .next(step3())
                .build();
    }

     */

    //we go to the next transition based on the exit codes
    //we can use our own custom exit codes, but if we do not provide them, Spring Batch uses defaults
    /*
    @Bean
    public Job transitionJobSimpleNext(){
        return jobBuilderFactory.get("transitionJobNext")
                .start(step1())
                .on("COMPLETED").to(step2())
                .from(step2()).on("COMPLETED")
                .to(step3()).end()
                .build();

    }*/


    //there are three terminal states that spring boot provide: end(),stop() and failed()

    //(a)end() ->indicates Job has completed successfully, It cannot be restarted
    //(b)fail() & stop() ->job is not completed and can be restarted at any given point
    // stop allows us to restart where we left off

    //in the below example, step 3 will never execute, and our job will be marked as FAILED
    /*
    @Bean
    public Job transitionJobSimpleNext(){
        return jobBuilderFactory.get("transitionJobNext")
                .start(step1())
                .on("COMPLETED").to(step2())
                .from(step2()).on("COMPLETED").fail()
                .from(step3()).end()
                .build();

    }

     */

    //Stop allows us to be able to restart at a given point
    //there is a very large concept on restarteability, i will look at in future lessons
    @Bean
    public Job transitionJobSimpleNext(){
        return jobBuilderFactory.get("transitionJobNext")
                .start(step1())
                .on("COMPLETED").to(step2())
                .from(step2()).on("COMPLETED").stopAndRestart(step3())
                .from(step3()).end()
                .build();

    }


}
