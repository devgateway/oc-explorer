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
package org.devgateway.ocvn.web.rest.controller;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

import java.util.List;

import javax.validation.Valid;

import org.devgateway.ocvn.persistence.mongo.ocds.Release;
import org.devgateway.ocvn.web.rest.controller.request.OCDSReleaseRequest;
import org.devgateway.toolkit.persistence.mongo.dao.VNPlanning;
import org.devgateway.toolkit.persistence.mongo.repository.ReleaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 
 * @author mpostelnicu
 *
 */
@RestController
public class OcdsController extends GenericOcvnController {

	@Autowired
	private ReleaseRepository releaseRepository;
	
	
	
	
	@RequestMapping("/api/ocds/release/budgetProjectId/{projectId:^[a-zA-Z0-9]*$}")
	public Release ocdsByProjectId(@PathVariable String projectId) {

		Release release = releaseRepository.findByBudgetProjectId(projectId);
		return release;
	}

	/**
	 * Returns one {@link Release} entity found based on {@link VNPlanning#getBidNo()}
	 * @param bidNo the bidNo
	 * @return the release
	 */
	@RequestMapping("/api/ocds/release/planningBidNo/{bidNo:^[a-zA-Z0-9]*$}")
	public Release ocdsByPlanningBidNo(@PathVariable String bidNo) {

		Release release = releaseRepository.findByPlanningBidNo(bidNo);
		return release;
	}

	/**
	 * Returns a list of OCDS Releases, order by Id, using pagination
	 * @return the release data
	 */
	@RequestMapping("/api/ocds/release/all")
	public List<Release> ocdsReleases(@Valid OCDSReleaseRequest releaseRequest) {

		Criteria[] yearCriteria = null;
		Criteria criteria = new Criteria();

		if (releaseRequest.getBidPlanProjectDateApproveYear() == null) {
			yearCriteria = new Criteria[1];
			yearCriteria[0] = new Criteria();
		} else {
			yearCriteria = new Criteria[releaseRequest.getBidPlanProjectDateApproveYear().size()];
			for (int i = 0; i < releaseRequest.getBidPlanProjectDateApproveYear().size(); i++)
				yearCriteria[i] = where("planning.bidPlanProjectDateApprove").gte(getStartDate(releaseRequest.getBidPlanProjectDateApproveYear().get(i)))
						.lte(getEndDate(releaseRequest.getBidPlanProjectDateApproveYear().get(i)));
		}

		PageRequest pageRequest = new PageRequest(releaseRequest.getPageNumber(), releaseRequest.getPageSize(), Direction.ASC, "id");

		List<Release> find = mongoTemplate.find(query(criteria.orOperator(yearCriteria)).with(pageRequest),
				Release.class);

		return find;

	}

}