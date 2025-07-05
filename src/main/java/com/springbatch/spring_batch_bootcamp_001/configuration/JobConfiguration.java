package com.springbatch.spring_batch_bootcamp_001.configuration;


import com.springbatch.spring_batch_bootcamp_001.domain.Customer;
import com.springbatch.spring_batch_bootcamp_001.domain.CustomerRowMapper;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.support.MySqlPagingQueryProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.HashMap;


@Configuration
public class JobConfiguration {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Autowired
    public DataSource dataSource;

    //JDBC Cursor item reader
    //it opens up a cursor, and we are able to read data and map it into an OBJECT
    //IT IS STATEFUL IN THAT IF SOMETHING HAPPENS MID WAY READING I CAN RESTART FROM WHERE I LEFT OFF

    //Draw back, it is not thread safe
    //OrderBy is very, very important for restartability
    //By ordering, it helps the item reader track on state


    /*
    @Bean
    public JdbcCursorItemReader<Customer>  cursorItemReader(){
        JdbcCursorItemReader<Customer> reader = new JdbcCursorItemReader<>();
        reader.setSql("select id, firstName, lastName, birthdate from customer order by lastName, firstName");
        reader.setDataSource(this.dataSource);
        reader.setRowMapper(new CustomerRowMapper());
        return reader;

    }*/


    //The biggest advantage is that it is threaded safe

    @Bean
    public JdbcPagingItemReader<Customer> pagingItemReader(){

        JdbcPagingItemReader<Customer> reader = new JdbcPagingItemReader<>();
        reader.setDataSource(this.dataSource);

        //it is the most performant to use the same fetchSize as chunkSize
        reader.setFetchSize(10);
        reader.setRowMapper(new CustomerRowMapper());

        //Different SQL will be created for each page
        MySqlPagingQueryProvider queryProvider = new MySqlPagingQueryProvider();
        queryProvider.setSelectClause("id,firstName,lastName,birthdate");
        queryProvider.setFromClause("from customer");

        //unlike the cursor reader that tracks only the number that was read.
        //the JDBC tracks the exact key for each value read via the SortKeys
        //used to order the data and keep track of the last key read
        //the sortkey must be a unique key
        HashMap<String, Order> sortKeys = new HashMap<>(1);
        sortKeys.put("id", Order.ASCENDING);
        queryProvider.setSortKeys(sortKeys);

        reader.setQueryProvider(queryProvider);
        return reader;
    }

    @Bean
    public ItemWriter<Customer> customItemWriter(){
        return items->{
            for (Customer book: items){
                System.out.println(book);
            }
        };
    }

    @Bean
    public Step step1(){
        return stepBuilderFactory.get("step1")
                .<Customer,Customer>chunk(10)
                .reader(pagingItemReader())
                .writer(customItemWriter())
                .build();

    }

    @Bean
    public Job job(){
        return jobBuilderFactory.get("jobThree")
                .start(step1())
                .build();
    }
}
