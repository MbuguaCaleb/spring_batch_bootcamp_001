package com.springbatch.spring_batch_bootcamp_001.configuration;

import com.springbatch.spring_batch_bootcamp_001.domain.Customer;
import com.springbatch.spring_batch_bootcamp_001.domain.CustomerFieldSetMapper;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import javax.sql.DataSource;


@Configuration
public class JobConfiguration {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Autowired
    public DataSource dataSource;



    //Spring Batch provides a wide range of readers for reading and processing
    //almost everything
    @Bean
    public FlatFileItemReader<Customer> customerItemReader(){

        FlatFileItemReader<Customer> reader = new FlatFileItemReader<>();

        //skipping the firstLine,
        //more things we can do with the reader:identifycomments,
        //There are very many options from the flat file item reader in terms
        //of reading input files
        reader.setLinesToSkip(1);


        //setting resource to be read
        reader.setResource(new ClassPathResource("customer.csv"));

        //parse the file
        DefaultLineMapper<Customer> customerLineMapper = new DefaultLineMapper<>();

        //creates a distinct column for my file
        //the below is a delimited line tokenizer
        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();

        //names of the column i will divide my input into
        tokenizer.setNames("id","firstName","lastName","birthdate");

        customerLineMapper.setLineTokenizer(tokenizer);

        //beanwrappersetMapper (research)
        customerLineMapper.setFieldSetMapper(new CustomerFieldSetMapper());
        customerLineMapper.afterPropertiesSet();

        reader.setLineMapper(customerLineMapper);

        return reader;

    }

    @Bean
    public ItemWriter<Customer> customItemWriter(){
        return items->{
            for (Customer item: items){
                System.out.println(items);
            }
        };
    }

    @Bean
    public Step step1(){
        return stepBuilderFactory.get("step1")
                .<Customer,Customer>chunk(10)
                .reader(customerItemReader())
                .writer(customItemWriter())
                .build();

    }

    @Bean
    public Job job(){
        return jobBuilderFactory.get("jobReadCsvFileThree")
                .start(step1())
                .build();
    }
}
