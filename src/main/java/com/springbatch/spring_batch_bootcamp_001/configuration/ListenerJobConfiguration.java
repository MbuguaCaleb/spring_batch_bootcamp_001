package com.springbatch.spring_batch_bootcamp_001.configuration;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class ListenerJobConfiguration {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;


//    Unlike other beans that are instantiated during startUp, (SingleTon)
//    The stepScope is Lazily loaded, and instantiates at the time
//    the bean calling it is being executed
//    HelloWorld One will not be instantiated at startup, but will rather be instantiated at the time
//    the step is being executed
//    In the meantime a Proxy is used in its place to satisfy the dependency
//
    @Bean
    @StepScope
    public Tasklet helloWorldTasklet(@Value("#{jobParameters['message']}") String message){
        return ((stepContribution, chunkContext) -> {
            System.out.println(message);
            return RepeatStatus.FINISHED;
        });
    }

    @Bean
    public Step step1(){
        return stepBuilderFactory.get("step1")
                .tasklet(helloWorldTasklet(null))
                  .build();
    }

    @Bean
    public Job jobParametersJob(){
        return jobBuilderFactory.get("jobParametersJob")
                .start(step1())
                .build();
    }

}
