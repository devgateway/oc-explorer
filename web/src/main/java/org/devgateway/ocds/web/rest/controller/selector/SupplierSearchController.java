package org.devgateway.ocds.web.rest.controller.selector;

import io.swagger.annotations.ApiOperation;
import org.devgateway.ocds.persistence.mongo.Organization;
import org.devgateway.ocds.persistence.mongo.Organization.OrganizationType;
import org.devgateway.ocds.web.rest.controller.request.OrganizationSearchRequest;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * @author mpostelnicu
 */
@RestController
public class SupplierSearchController extends AbstractOrganizationSearchController {

    @RequestMapping(value = "/api/ocds/organization/supplier/id/{id:^[a-zA-Z0-9]*$}",
            method = {RequestMethod.POST, RequestMethod.GET}, produces = "application/json")
    @ApiOperation(value = "Finds supplier by the given id")
    public Organization byId(@PathVariable final String id) {
        return organizationRepository.findByAllIdsAndType(id, Organization.OrganizationType.supplier);
    }

    /**
     * Searches {@link Organization} entities of {@link Organization.OrganizationType}
     * {@link Organization.OrganizationType#supplier} by the given text
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/api/ocds/organization/supplier/all",
            method = {RequestMethod.POST, RequestMethod.GET}, produces = "application/json")
    @ApiOperation(value = "Lists all suppliers in the database. "
            + "Suppliers are organizations that have the label 'supplier' assigned to organization.types array"
            + "Allows full text search using the text parameter.")
    public List<Organization> searchText(@Valid final OrganizationSearchRequest request) {
        return organizationSearchTextByType(request, OrganizationType.supplier);
    }

    @RequestMapping(value = "/api/ocds/organization/supplier/count",
            method = {RequestMethod.POST, RequestMethod.GET}, produces = "application/json")
    @ApiOperation(value = "Counts all suppliers in the database. "
            + "Suppliers are organizations that have the label 'supplier' assigned to organization.types array"
            + "Allows full text search using the text parameter.")
    @Override
    public Long count(OrganizationSearchRequest request) {
        return organizationCountTextByType(request, OrganizationType.supplier);
    }

}