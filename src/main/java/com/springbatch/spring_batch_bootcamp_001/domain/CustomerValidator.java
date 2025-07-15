
package com.springbatch.spring_batch_bootcamp_001.domain;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.validator.ValidationException;
import org.springframework.batch.item.validator.Validator;

/**
 * @author Michael Minella
 */
@Slf4j
public class CustomerValidator implements Validator<Customer> {

	@Override
	public void validate(Customer value) throws ValidationException {
		if(value.getFirstName().startsWith("A")) {

			log.info("invalid" + value);
			throw new ValidationException("First names that begin with A are invalid: " + value);
		}
	}

	/**
	 * @author Mbugua Caleb
	 */

	//The process method in the Item Processor is allowed to chanhe the type if need be
	//It can for instance take in a customer Object and return an Accounts Object if the customer is related to the account
	public static class UpperCaseItemProcessor implements ItemProcessor<Customer, Customer> {

		@Override
		public Customer process(Customer item) throws Exception {
			return new Customer(item.getId(),
					item.getFirstName().toUpperCase(),
					item.getLastName().toUpperCase(),
					item.getBirthdate());
		}
	}
}
