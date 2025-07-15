
package com.springbatch.spring_batch_bootcamp_001.domain;

import org.springframework.batch.item.ItemProcessor;

/**
 * @author Michael Minella
 */
public class UpperCaseItemProcessor implements ItemProcessor<Customer, Customer> {

	@Override
	public Customer process(Customer item) throws Exception {
		return new Customer(item.getId(),
				item.getFirstName().toUpperCase(),
				item.getLastName().toUpperCase(),
				item.getBirthdate());
	}
}
