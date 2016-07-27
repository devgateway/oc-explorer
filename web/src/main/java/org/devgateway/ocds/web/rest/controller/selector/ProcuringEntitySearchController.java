package org.devgateway.ocds.web.rest.controller.selector;

import java.util.List;

import javax.validation.Valid;

import org.devgateway.ocds.web.rest.controller.GenericOCDSController;
import org.devgateway.ocds.web.rest.controller.request.OrganizationSearchRequest;
import org.devgateway.ocvn.persistence.mongo.dao.VNOrganization;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.data.mongodb.core.query.TextQuery;
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
public class ProcuringEntitySearchController extends GenericOCDSController {
	/**
	 * Searches the {@link VNOrganization} based on a given text. The text has
	 * to have minimum 3 characters and max 30
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/api/ocds/organization/procuringEntity/all",
			method = { RequestMethod.POST, RequestMethod.GET }, produces = "application/json")
	@ApiOperation(value = "Lists all procuring entities in the database. "
			+ "Procuring entities are organizations that have the property procuringEntity set to true. "
			+ "Allows full text search using the text parameter.")
	public List<VNOrganization> procuringEntitySearchText(@Valid final OrganizationSearchRequest request) {

		PageRequest pageRequest = new PageRequest(request.getPageNumber(), request.getPageSize());

		Query query = null;

		if (request.getText() == null) {
			query = new Query();
		} else {
			query = TextQuery.queryText(new TextCriteria().matching(request.getText())).sortByScore();
		}
		query.addCriteria(Criteria.where("procuringEntity").is(true)).with(pageRequest);

		List<VNOrganization> orgs = mongoTemplate.find(query, VNOrganization.class);

		return orgs;

	}

}