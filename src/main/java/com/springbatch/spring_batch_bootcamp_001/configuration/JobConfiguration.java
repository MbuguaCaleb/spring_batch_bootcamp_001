package com.springbatch.spring_batch_bootcamp_001.configuration;

import com.springbatch.spring_batch_bootcamp_001.domain.Customer;
import com.springbatch.spring_batch_bootcamp_001.domain.CustomerFieldSetMapper;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;


@Configuration
public class JobConfiguration {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;


    @Value("classpath*:customer*.csv")
    private Resource[] inputFiles;


    //It will get all my input files and pass them progressively to my reader
    //Being able to read from multiple files can be scaled up to allow multiple file processing thus greater throughPut
    @Bean
    public MultiResourceItemReader<Customer> multiResourceItemReader(){

        //Keep track on what files have been read, and if a file has been read, moves to the next one
        MultiResourceItemReader<Customer> reader = new MultiResourceItemReader<>();

        //delegate the work of reading
        reader.setDelegate(customerItemReader());
        reader.setResources(inputFiles);

        return reader;
    }

    @Bean
    public FlatFileItemReader<Customer> customerItemReader() {
        FlatFileItemReader<Customer> reader = new FlatFileItemReader<>();

        DefaultLineMapper<Customer> customerLineMapper = new DefaultLineMapper<>();

        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
        tokenizer.setNames(new String[] {"id", "firstName", "lastName", "birthdate"});

        customerLineMapper.setLineTokenizer(tokenizer);
        customerLineMapper.setFieldSetMapper(new CustomerFieldSetMapper());
        customerLineMapper.afterPropertiesSet();

        reader.setLineMapper(customerLineMapper);

        return reader;
    }

    @Bean
    public ItemWriter<Customer> customItemWriter(){
        return items->{
            for (Customer item: items){
                System.out.println(item.toString());
            }
        };
    }

    @Bean
    public Step step1(){
        return stepBuilderFactory.get("step1")
                .<Customer,Customer>chunk(10)
                .reader(multiResourceItemReader())
                .writer(customItemWriter())
                .build();

    }

    @Bean
    public Job job(){
        return jobBuilderFactory.get("jobReadMultipleFilesTwo")
                .start(step1())
                .build();
    }
}
