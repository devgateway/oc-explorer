package org.devgateway.ocds.web.rest.controller.selector;

import io.swagger.annotations.ApiOperation;
import org.devgateway.ocds.persistence.mongo.Organization;
import org.devgateway.ocds.web.rest.controller.request.OrganizationIdWrapper;
import org.devgateway.ocds.web.rest.controller.request.OrganizationSearchRequest;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 *
 * @author mpostelnicu
 *
 */
@RestController
public class OrganizationSearchController extends AbstractOrganizationSearchController {

    /**
     * Searches organizations based on ID. Returns only one result, if the id
     * exactly matches
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/api/ocds/organization/id/{id}", method = { RequestMethod.POST,
            RequestMethod.GET }, produces = "application/json")
    @ApiOperation(value = "Finds organization entity by the given id")
    public Organization byId(@PathVariable final String id) {
        return organizationRepository.findById(id).orElse(null);
    }
    
    @RequestMapping(value = "/api/ocds/organization/ids",
            method = { RequestMethod.POST, RequestMethod.GET }, produces = "application/json")
    @ApiOperation(value = "Finds organization entities by the given list of ids, comma separated")
    public List<Organization> byIdCollection(@ModelAttribute @Valid OrganizationIdWrapper orgIdWrapper) {
        return organizationRepository.findByIdCollection(orgIdWrapper.getId());
    }

    @RequestMapping(value = "/api/ocds/organization/all", method = { RequestMethod.POST,
            RequestMethod.GET }, produces = "application/json")
    @ApiOperation(value = "Lists all organizations in the database. "
            + "Allows full text search using the text parameter.")
    public List<Organization> searchText(@Valid final OrganizationSearchRequest request) {
        return organizationSearchTextByType(request, null);
    }

    @RequestMapping(value = "/api/ocds/organization/count",
            method = {RequestMethod.POST, RequestMethod.GET}, produces = "application/json")
    @ApiOperation(value = "Counts all organizations in the database. "
            + "Allows full text search using the text parameter.")
    @Override
    public Long count(OrganizationSearchRequest request) {
        return organizationCountTextByType(request, null);
    }

}