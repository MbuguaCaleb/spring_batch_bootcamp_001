package com.springbatch.spring_batch_bootcamp_001.configuration;

import com.springbatch.spring_batch_bootcamp_001.domain.Customer;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.oxm.xstream.XStreamMarshaller;

import java.util.HashMap;
import java.util.Map;


@Configuration
public class JobConfiguration {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;



    //Every Item in the XML File is treated as a chunk
    //the xml reader is stateful in that if an execution fails, it will restart from where it left off
    @Bean
    public StaxEventItemReader<Customer> customerItemReader(){
        XStreamMarshaller unmarshaller = new XStreamMarshaller();

        Map<String, Class> aliases = new HashMap<>();
        aliases.put("customer", Customer.class);
        unmarshaller.setAliases(aliases);

        StaxEventItemReader<Customer> reader = new StaxEventItemReader<>();
        reader.setResource(new ClassPathResource("customers.xml"));

        //Defines each xml chunk
        reader.setFragmentRootElementName("customer");
        reader.setUnmarshaller(unmarshaller);

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
                .reader(customerItemReader())
                .writer(customItemWriter())
                .build();

    }

    @Bean
    public Job job(){
        return jobBuilderFactory.get("jobReadXmlFile")
                .start(step1())
                .build();
    }
}
