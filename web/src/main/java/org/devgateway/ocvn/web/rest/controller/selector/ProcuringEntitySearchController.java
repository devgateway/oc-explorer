/*******************************************************************************
 * Copyright (c) 2015 Development Gateway, Inc and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the MIT License (MIT)
 * which accompanies this distribution, and is available at
 * https://opensource.org/licenses/MIT
 *
 * Contributors:
 * Development Gateway - initial API and implementation
 *******************************************************************************/
package org.devgateway.ocvn.web.rest.controller.selector;

import java.util.List;

import javax.validation.Valid;

import org.devgateway.ocvn.web.rest.controller.GenericOcvnController;
import org.devgateway.ocvn.web.rest.controller.request.ProcuringEntitySearchRequest;
import org.devgateway.toolkit.persistence.mongo.dao.VNOrganization;
import org.devgateway.toolkit.persistence.mongo.repository.VNOrganizationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.data.mongodb.core.query.TextQuery;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 
 * @author mpostelnicu
 * 
 */
@RestController
public class ProcuringEntitySearchController extends GenericOcvnController {

	@Autowired
	VNOrganizationRepository organizationRepository;

	/**
	 * Searches organizations based on ID. Returns only one result, if the id
	 * exactly matches
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("/api/ocds/organization/id/{id:^[a-zA-Z0-9]*$}")
	public VNOrganization organizationId(@PathVariable final String id) {

		VNOrganization org = organizationRepository.findById(id);
		return org;
	}

	/**
	 * Searches the {@link VNOrganization} based on a given text. The text has
	 * to have minimum 3 characters and max 30
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/api/ocds/organization/procuringEntity/all")
	public List<VNOrganization> procuringEntitySearchText(@Valid final ProcuringEntitySearchRequest request) {

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