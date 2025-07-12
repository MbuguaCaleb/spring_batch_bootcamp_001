/*
 * Copyright 2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.springbatch.spring_batch_bootcamp_001.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.batch.item.file.transform.LineAggregator;

//takes an object from the reader and converts it into a line that
//is going to be written in a file
public class CustomerLineAggregator implements LineAggregator<Customer> {

	private ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public String aggregate(Customer item) {
		try {

			//returns a string that is appended at end of myFile
			return objectMapper.writeValueAsString(item);
		}
		catch (JsonProcessingException e) {
			throw new RuntimeException("Unable to serialize Customer", e);
		}
	}
}
