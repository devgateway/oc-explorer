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

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.devgateway.ocvn.persistence.mongo.ocds.Release;
import org.devgateway.toolkit.persistence.mongo.repository.ReleaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mongodb.DBObject;

/**
 * 
 * @author mpostelnicu
 *
 */
@RestController
public class OcdsController {

	@Autowired
	private ReleaseRepository releaseRepository;

	public Date getStartDate(int year) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.DAY_OF_YEAR, 1);
		Date start = cal.getTime();
		return start;
	}

	public Date getEndDate(int year) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, 11);
		cal.set(Calendar.DAY_OF_MONTH, 31);
		Date end = cal.getTime();
		return end;
	}

	@RequestMapping("/api/ocds/release/budgetProjectId/{projectId}")
	public Release ocdsByProjectId(@PathVariable String projectId) {

		Release release = releaseRepository.findByBudgetProjectId(projectId);
		return release;
	}

	@RequestMapping("/api/ocds/release/planningBidNo/{bidNo}")
	public Release ocdsByPlanningBidNo(@PathVariable String bidNo) {

		Release release = releaseRepository.findByPlanningBidNo(bidNo);
		return release;
	}

}