package com.springbatch.spring_batch_bootcamp_001;


import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


//We are able to read from many input files at once
@SpringBootApplication
@EnableBatchProcessing
public class FlatFilesApplication {

    public static void main(String[] args) {
        SpringApplication.run(FlatFilesApplication.class, args);
    }

}
