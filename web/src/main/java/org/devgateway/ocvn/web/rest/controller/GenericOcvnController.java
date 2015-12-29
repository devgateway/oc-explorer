/**
 * 
 */
package org.devgateway.ocvn.web.rest.controller;

import java.util.Calendar;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

/**
 * @author mpostelnicu
 *
 */
public class GenericOcvnController {

	protected final Logger logger = LoggerFactory.getLogger(GenericOcvnController.class);
	
	@Autowired
	protected MongoTemplate mongoTemplate;

	protected Date getStartDate(int year) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.DAY_OF_YEAR, 1);
		Date start = cal.getTime();
		return start;
	}

	protected Date getEndDate(int year) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, 11);
		cal.set(Calendar.DAY_OF_MONTH, 31);
		Date end = cal.getTime();
		return end;
	}
}
