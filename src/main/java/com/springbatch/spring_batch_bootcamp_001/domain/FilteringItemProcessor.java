
package com.springbatch.spring_batch_bootcamp_001.domain;

import org.springframework.batch.item.ItemProcessor;

/**
 * @author Michael Minella
 */
public class FilteringItemProcessor implements ItemProcessor<Customer, Customer> {
	@Override
	public Customer process(Customer item) throws Exception {

		if(item.getId() % 2 == 0) {
			return null;
		}
		else {
			return item;
		}
	}
}
