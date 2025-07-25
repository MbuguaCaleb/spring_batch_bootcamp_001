package com.springbatch.spring_batch_bootcamp_001;


import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
@EnableBatchProcessing
public class CompositeItemProcessorApplication {

    public static void main(String[] args) {
        SpringApplication.run(CompositeItemProcessorApplication.class, args);
    }


}
