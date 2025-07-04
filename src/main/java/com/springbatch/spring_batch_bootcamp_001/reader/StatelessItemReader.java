package com.springbatch.spring_batch_bootcamp_001.reader;

import org.springframework.batch.item.ItemReader;

import java.util.Iterator;
import java.util.List;

//The item reader is called over and over again within the context of a chunk until an input is exhausted
//It is a generic type that takes any type of input and reads it over and over again until the input is exhausted
public class StatelessItemReader implements ItemReader<String> {

    private final Iterator<String> data;

    public StatelessItemReader(List<String> data) {
        this.data = data.iterator();
    }

    @Override
    public String read() throws Exception{
       if(this.data.hasNext()){
           return this.data.next();
       }else{
          return null;
       }
    }
}
