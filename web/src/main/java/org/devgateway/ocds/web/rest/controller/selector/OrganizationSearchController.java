package org.devgateway.ocds.web.rest.controller.selector;

import com.mongodb.DBObject;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import javax.validation.Valid;
import org.devgateway.ocds.persistence.mongo.Organization;
import org.devgateway.ocds.web.rest.controller.request.OrganizationIdWrapper;
import org.devgateway.ocds.web.rest.controller.request.OrganizationSearchRequest;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.Fields;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.project;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.unwind;
import static org.springframework.data.mongodb.core.query.Criteria.where;

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
        return organizationRepository.findOne(id);
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

    @ApiOperation(value = "Counts the releases where the given supplier id is among the winners.")
    @RequestMapping(value = "/api/winnerCount/supplierId/{organizationId}",
            method = {RequestMethod.POST, RequestMethod.GET},
            produces = "application/json")
    public List<DBObject> winnerCount(@PathVariable final String organizationId) {

        Aggregation agg = newAggregation(
                match(where("awards.status").is("active").and("awards.suppliers._id").is(organizationId)),
                unwind("awards"),
                match(where("awards.status").is("active").and("awards.suppliers._id").is(organizationId)),
                group().count().as("cnt"),
                project("cnt").andExclude(Fields.UNDERSCORE_ID)
        );

        AggregationResults<DBObject> results = mongoTemplate.aggregate(agg, "release", DBObject.class);
        List<DBObject> tagCount = results.getMappedResults();
        return tagCount;
    }

}