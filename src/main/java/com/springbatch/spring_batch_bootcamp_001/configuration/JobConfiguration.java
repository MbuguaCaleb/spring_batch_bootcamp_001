package com.springbatch.spring_batch_bootcamp_001.configuration;

import com.springbatch.spring_batch_bootcamp_001.reader.StatelessItemReader;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;


@Configuration
public class JobConfiguration {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Bean
    public StatelessItemReader statelessItemReader() {
        List<String> data = new ArrayList<>(3);
        data.add("Foo");
        data.add("Bar");
        data.add("Baz");
        data.add("Baz");
        data.add("Baz");
        data.add("Baz");
        data.add("Baz");

        return new StatelessItemReader(data);
    }

    //.<String,String> chunk(2)
    //The generic reader and writer helps us ensure type safety within
    //the chunk (they represent the reader and writer

    @Bean
    public Step step1() {
        return stepBuilderFactory.get("step1")
                .<String, String>chunk(2)
                .reader(statelessItemReader())
                .writer(list -> {
                    for (String curItem : list) {
                        System.out.println("curItem = " + curItem);
                    }
                })
                .build();
    }

    @Bean
    public Job intefacesJob() {
        return jobBuilderFactory.get("interfacesJob")
                .start(step1())
                .build();
    }

}
