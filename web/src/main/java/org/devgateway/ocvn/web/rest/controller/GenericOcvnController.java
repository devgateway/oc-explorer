/**
 * 
 */
package org.devgateway.ocvn.web.rest.controller;

import static org.springframework.data.mongodb.core.query.Criteria.where;

import java.util.Calendar;
import java.util.Date;

import org.devgateway.ocvn.web.rest.controller.request.UniversalFilterPagingRequest;
import org.devgateway.toolkit.persistence.mongo.aggregate.CustomOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Fields;
import org.springframework.data.mongodb.core.query.Criteria;

import com.mongodb.BasicDBObject;

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
	
	protected Criteria getBidTypeIdFilterCriteria(UniversalFilterPagingRequest filter) {
		Criteria bidTypeCriteria = null;
		if (filter.getBidTypeId() == null)
			bidTypeCriteria = new Criteria();
		else
			bidTypeCriteria = where("tender.items.classification._id").in(filter.getBidTypeId().toArray());

		return bidTypeCriteria;
	}
	
	protected Criteria getProcuringEntityCriteria(UniversalFilterPagingRequest filter) {
		Criteria criteria = null;
		if (filter.getProcuringEntityId() == null)
			criteria = new Criteria();
		else
			criteria = where("tender.procuringEntity._id").in(filter.getProcuringEntityId().toArray());

		return criteria;
	}

}
