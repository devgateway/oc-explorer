package org.devgateway.ocds.web.rest.controller.selector;

import java.util.List;

import javax.validation.Valid;

import org.devgateway.ocds.persistence.mongo.Organization;
import org.devgateway.ocds.persistence.mongo.repository.OrganizationRepository;
import org.devgateway.ocds.web.rest.controller.GenericOCDSController;
import org.devgateway.ocds.web.rest.controller.request.OrganizationSearchRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.data.mongodb.core.query.TextQuery;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;

/**
 * 
 * @author mpostelnicu
 * 
 */
@RestController
public class OrganizationSearchController extends GenericOCDSController {

	@Autowired
	private OrganizationRepository organizationRepository;

	/**
	 * Searches organizations based on ID. Returns only one result, if the id
	 * exactly matches
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/api/ocds/organization/id/{id:^[a-zA-Z0-9]*$}",
			method = { RequestMethod.POST, RequestMethod.GET }, produces = "application/json")	
	@ApiOperation(value = "Finds organization entity by the given id")
	public Organization organizationId(@PathVariable final String id) {

		Organization org = organizationRepository.findOne(id);
		return org;
	}

	/**
	 * Searches the {@link Organization} based on a given text. The text has
	 * to have minimum 3 characters and max 30
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/api/ocds/organization/all",
			method = { RequestMethod.POST, RequestMethod.GET }, produces = "application/json")
	@ApiOperation(value = "Lists all organizations in the database. "
			+ "Allows full text search using the text parameter.")
	@Cacheable(cacheNames = "genericPagingRequestJson", keyGenerator = "genericPagingRequestKeyGenerator")
	public List<Organization> organizationSearchText(@Valid final OrganizationSearchRequest request) {

		PageRequest pageRequest = new PageRequest(request.getPageNumber(), request.getPageSize());

		Query query = null;

		if (request.getText() == null) {
			query = new Query();
		} else {
			query = TextQuery.queryText(new TextCriteria().matching(request.getText())).sortByScore();
		}
		query.addCriteria(Criteria.where("procuringEntity").is(true)).with(pageRequest);

		List<Organization> orgs = mongoTemplate.find(query, Organization.class);

		return orgs;

	}

}