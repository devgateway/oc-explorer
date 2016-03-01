/**
 * 
 */
package org.devgateway.ocvn.web.rest.controller;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.query.Criteria.where;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;

import org.devgateway.ocvn.web.rest.controller.request.DefaultFilterPagingRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.query.Criteria;

/**
 * @author mpostelnicu
 *
 */
public class GenericOcvnController {
	
	protected Map<String,Object> filterProjectMap;

	protected final Logger logger = LoggerFactory.getLogger(GenericOcvnController.class);
	
	@Autowired
	protected MongoTemplate mongoTemplate;
	
	/**
	 * Gets the date of the first day of the year (01.01.year)
	 * @param year
	 * @return
	 */
	protected Date getStartDate(int year) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.DAY_OF_YEAR, 1);
		Date start = cal.getTime();
		return start;
	}

	/**
	 * Gets the date of the last date of the year (31.12.year)
	 * @param year
	 * @return
	 */
	protected Date getEndDate(int year) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, 11);
		cal.set(Calendar.DAY_OF_MONTH, 31);
		Date end = cal.getTime();
		return end;
	}

	/**
	 * Appends the procuring bid type id for this filter, this will fitler based on tender.items.classification._id
	 * @param filter
	 * @return the {@link Criteria} for this filter
	 */
	protected Criteria getBidTypeIdFilterCriteria(DefaultFilterPagingRequest filter) {
		Criteria bidTypeCriteria = null;
		if (filter.getBidTypeId() == null)
			bidTypeCriteria = new Criteria();
		else
			bidTypeCriteria = where("tender.items.classification._id").in(filter.getBidTypeId().toArray());

		return bidTypeCriteria;
	}
	
	/**
	 * Appends the procuring entity id for this filter, this will fitler based on tender.procuringEntity._id
	 * @param filter
	 * @return the {@link Criteria} for this filter
	 */
	protected Criteria getProcuringEntityIdCriteria(DefaultFilterPagingRequest filter) {
		Criteria criteria = null;
		if (filter.getProcuringEntityId() == null)
			criteria = new Criteria();
		else
			criteria = where("tender.procuringEntity._id").in(filter.getProcuringEntityId().toArray());

		return criteria;
	}
	
	@PostConstruct
	protected void init() {
		filterProjectMap = new ConcurrentHashMap<>();
		filterProjectMap.put("tender.procuringEntity", 1);
		filterProjectMap.put("tender.items.classification", 1);
		filterProjectMap.put("tender.procurementMethodDetails", 1);
	}
	
	/**
	 * Appends the bid selection method to the filter, this will filter based on tender.procurementMethodDetails.
	 * It accepts multiple elements
	 * @param filter
	 * @return the {@link Criteria} for this filter
	 */
	protected Criteria getBidSelectionMethod(DefaultFilterPagingRequest filter) {
		Criteria criteria = null;
		if (filter.getBidSelectionMethod() == null)
			criteria = new Criteria();
		else
			criteria = where("tender.procurementMethodDetails").in(filter.getBidSelectionMethod().toArray());

		return criteria;
	}
	
	protected Criteria getDefaultFilterCriteria(DefaultFilterPagingRequest filter) {
		return new Criteria().andOperator(getBidTypeIdFilterCriteria(filter),
				getProcuringEntityIdCriteria(filter), getBidSelectionMethod(filter));
	}
	
	protected MatchOperation getMatchDefaultFilterOperation(DefaultFilterPagingRequest filter) {
		return match(getDefaultFilterCriteria(filter));
	}

}
