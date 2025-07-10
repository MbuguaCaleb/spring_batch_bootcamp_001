package com.springbatch.spring_batch_bootcamp_001.configuration;

import org.springframework.batch.item.*;

import java.util.List;

//iTEM Stream takes in 4 methods (read,open,update,close)
public class StatefulItemReader implements ItemStreamReader<String> {

    private final List<String> items;
    private int curIndex = -1;
    private boolean restart = false;

    public StatefulItemReader(List<String> items) {
        this.items = items;
        this.curIndex = 0;
    }

    //Supposed to return item in the current index
    @Override
    public String read() throws Exception{
        String item = null;

        if(this.curIndex < this.items.size()) {
            item = this.items.get(this.curIndex);
            this.curIndex++;
        }

        if(this.curIndex == 42 && !restart) {
            throw new RuntimeException("The Answer to the Ultimate Question of Life, the Universe, and Everything");
        }

        return item;
    }

    //the method that is run first each time
    //resets current index to the previous value
    @Override
    public void open(ExecutionContext executionContext) throws ItemStreamException {
        if(executionContext.containsKey("curIndex")) {
            this.curIndex = executionContext.getInt("curIndex");
            this.restart = true;
        }
        else {
            this.curIndex = 0;
            executionContext.put("curIndex", this.curIndex);
        }
    }

    //if a unit transaction in a STEP Completes successfully,this is where we save the last successful
    @Override
    public void update(ExecutionContext executionContext) throws ItemStreamException {
        executionContext.put("curIndex", this.curIndex);

    }

    @Override
    public void close() throws ItemStreamException {

    }
}
