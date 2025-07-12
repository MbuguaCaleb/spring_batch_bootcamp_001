package com.springbatch.spring_batch_bootcamp_001.configuration;

import org.springframework.batch.item.ItemWriter;

import java.util.List;

//My custom item writer that prints items to the console
//AnyItem writer must implement the itemwriter iterface
//ItemWriter takes in manyItems at once, and we can do batch writing,
//You can do the writing step by step if you want but
public class SysOutItemWriter implements ItemWriter<String> {

    @Override
    public void write(List<? extends String> items) throws Exception {

        System.out.println("The size of this chunk was: " + items.size());

        for (String item :items){
            System.out.println(">> " + item);
        }
    }
}
