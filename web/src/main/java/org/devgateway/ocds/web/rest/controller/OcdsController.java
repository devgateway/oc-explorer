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
package org.devgateway.ocds.web.rest.controller;

import org.devgateway.ocds.persistence.mongo.Publisher;
import org.devgateway.ocds.persistence.mongo.Release;
import org.devgateway.ocds.persistence.mongo.ReleasePackage;
import org.devgateway.ocds.persistence.mongo.repository.ReleaseRepository;
import org.devgateway.ocds.web.rest.controller.request.YearFilterPagingRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.data.mongodb.core.query.Query.query;

/**
 *
 * @author mpostelnicu
 *
 */
@RestController
public class OcdsController extends GenericOCDSController {

    private static final String SERVER_DOMAIN = "http://ocexplorer.dgstg.org";

    @Autowired
    private ReleaseRepository releaseRepository;

    @RequestMapping(value = "/api/ocds/release/budgetProjectId/{projectId:^[a-zA-Z0-9]*$}", method = RequestMethod.GET,
            produces = "application/json")
    public Release ocdsByProjectId(@PathVariable final String projectId) {

        Release release = releaseRepository.findByBudgetProjectId(projectId);
        return release;
    }

    /**
     * Returns one {@link Release} entity found based on bidNo
     *
     * @param bidNo
     *            the bidNo
     * @return the release
     */
    @RequestMapping(value = "/api/ocds/release/planningBidNo/{bidNo:^[a-zA-Z0-9]*$}", method = RequestMethod.GET,
            produces = "application/json")
    public Release ocdsByPlanningBidNo(@PathVariable final String bidNo) {

        Release release = releaseRepository.findByPlanningBidNo(bidNo);
        return release;
    }

    @RequestMapping(value = "/api/ocds/release/ocid/{ocid}", method = RequestMethod.GET,
            produces = "application/json")
    public Release ocdsByOcid(@PathVariable final String ocid) {

        Release release = releaseRepository.findByOcid(ocid);
        return release;
    }

    @RequestMapping(value = "/api/ocds/package/ocid/{ocid}", method = RequestMethod.GET,
            produces = "application/json")
    public ReleasePackage ocdsPackageByOcid(@PathVariable final String ocid) {

        Release release = releaseRepository.findByOcid(ocid);
        return createReleasePackage(release);
    }

    public ReleasePackage createReleasePackage(final Release release) {
        ReleasePackage releasePackage = new ReleasePackage();
        releasePackage.setLicense("https://creativecommons.org/licenses/by/2.0/");
        releasePackage.setPublicationPolicy("https://github.com/open-contracting/sample-data/");
        releasePackage.setPublishedDate(release.getDate());
        releasePackage.getReleases().add(release);
        releasePackage.setUri(SERVER_DOMAIN + "/api/ocds/package/ocid/" + release.getOcid());
        Publisher publisher = new Publisher();

        publisher.setName("Government of Vietnam: Public Procurement Agency");
        publisher.setScheme("VN-PPA");
        publisher.setUid(release.getOcid());
        publisher.setUri(SERVER_DOMAIN);
        releasePackage.setPublisher(publisher);
        return releasePackage;
    }

    @RequestMapping(value = "/api/ocds/package/planningBidNo/{bidNo:^[a-zA-Z0-9]*$}", method = RequestMethod.GET,
            produces = "application/json")
    public ReleasePackage packagedReleaseByPlanningBidNo(@PathVariable final String bidNo) {
        Release release = ocdsByPlanningBidNo(bidNo);

        return createReleasePackage(release);
    }

    @RequestMapping(value = "/api/ocds/package/budgetProjectId/{projectId:^[a-zA-Z0-9]*$}", method = RequestMethod.GET,
            produces = "application/json")
    public ReleasePackage packagedReleaseByProjectId(@PathVariable final String projectId) {
        Release release = ocdsByProjectId(projectId);

        return createReleasePackage(release);
    }

    /**
     * Returns a list of OCDS Releases, order by Id, using pagination
     *
     * @return the release data
     */
    @RequestMapping(value = "/api/ocds/release/all", method = RequestMethod.GET,
            produces = "application/json")
    public List<Release> ocdsReleases(@ModelAttribute @Valid final YearFilterPagingRequest releaseRequest) {

        PageRequest pageRequest = new PageRequest(releaseRequest.getPageNumber(), releaseRequest.getPageSize(),
                Direction.ASC, "id");

        List<Release> find = mongoTemplate
                .find(query(getYearFilterCriteria("planning.bidPlanProjectDateApprove", releaseRequest)
                        .andOperator(getDefaultFilterCriteria(releaseRequest))).with(pageRequest), Release.class);

        return find;

    }

    @RequestMapping(value = "/api/ocds/package/all", method = RequestMethod.GET,
            produces = "application/json")
    public List<ReleasePackage> ocdsPackages(@ModelAttribute @Valid final YearFilterPagingRequest releaseRequest) {
        List<Release> ocdsReleases = ocdsReleases(releaseRequest);
        List<ReleasePackage> releasePackages = new ArrayList<>(ocdsReleases.size());
        for (Release release : ocdsReleases) {
            releasePackages.add(createReleasePackage(release));
        }

        return releasePackages;
    }

}