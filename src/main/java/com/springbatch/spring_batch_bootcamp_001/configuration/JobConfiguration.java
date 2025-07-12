package com.springbatch.spring_batch_bootcamp_001.configuration;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;

@Configuration
public class JobConfiguration {


    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;


    //List Item reader, takes in a List and returns all the items in the List
    @Bean
    public ListItemReader<String> itemReader(){

        ArrayList<String> items = new ArrayList<>(100);

        for (int i = 1; i <=100 ; i++) {
            items.add(String.valueOf(i));
        }

        return new ListItemReader<>(items);

    }

    @Bean
    public SysOutItemWriter itemWriter(){
        return new SysOutItemWriter();
    }


    //A step contains of a reader and writer
    @Bean
    public Step step(){
        return stepBuilderFactory.get("step")
                .<String,String> chunk(10)
                .reader(itemReader())
                .writer(itemWriter())
                .build();
    }

    @Bean
    public Job job(){
        return jobBuilderFactory.get("job")
                .start(step())
                .build();
    }


}
