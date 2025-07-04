package com.springbatch.spring_batch_bootcamp_001.configuration;

import com.springbatch.spring_batch_bootcamp_001.listener.ChunkListener;
import com.springbatch.spring_batch_bootcamp_001.listener.CustomJobListener;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.Arrays;
import java.util.List;

@Configuration
public class ListenerJobConfiguration {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Bean
    public ItemReader<String> reader(){
        return new ListItemReader<>(Arrays.asList("one","two","three"));
    }

    @Bean
    public ItemWriter<String> writer(){
        return new ItemWriter<String>() {
            @Override
            public void write(List<? extends String> items) throws Exception {
                for (String item : items) {
                    System.out.println("Writing item" + item);
                }
            }
        };
    }


    //Chunk Step
    @Bean
    public Step step1(){
        return stepBuilderFactory.get("step1")
                .<String,String> chunk(2)
                .faultTolerant()
                .listener(new ChunkListener())
                .reader(reader())
                .writer(writer())
                .build();
    }

    @Bean
    public Job job(JavaMailSender javaMailSender){
        return jobBuilderFactory.get("job")
                .start(step1())
                .listener(new CustomJobListener(javaMailSender))
                .build();
    }

}
