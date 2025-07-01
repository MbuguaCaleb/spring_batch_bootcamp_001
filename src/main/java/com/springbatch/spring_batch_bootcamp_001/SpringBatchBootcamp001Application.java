package com.springbatch.spring_batch_bootcamp_001;


import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableBatchProcessing //a very important annotation to enable spring batch in an application
public class SpringBatchBootcamp001Application {

    public static void main(String[] args) {
        SpringApplication.run(SpringBatchBootcamp001Application.class, args);
    }

}
