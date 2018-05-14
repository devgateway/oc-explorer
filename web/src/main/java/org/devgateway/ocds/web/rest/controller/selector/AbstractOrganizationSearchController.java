package org.devgateway.ocds.web.rest.controller.selector;

import org.devgateway.ocds.persistence.mongo.Organization;
import org.devgateway.ocds.persistence.mongo.repository.main.OrganizationRepository;
import org.devgateway.ocds.web.rest.controller.GenericOCDSController;
import org.devgateway.ocds.web.rest.controller.request.OrganizationSearchRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.PathVariable;

import javax.validation.Valid;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author mpostelnicu
 */
public abstract class AbstractOrganizationSearchController extends GenericOCDSController {

    @Autowired
    protected OrganizationRepository organizationRepository;

    protected List<Organization> organizationSearchTextByType(final OrganizationSearchRequest request,
                                                              Organization.OrganizationType type) {
        List<Organization> orgs = mongoTemplate.find(queryTextByType(request, type), Organization.class);
        return orgs;
    }

    private Query queryTextByType(final OrganizationSearchRequest request,
                                  Organization.OrganizationType type) {
        Query query = null;

        if (request.getText() == null) {
            query = new Query();
        } else {
            //this is for Full Text Search, in case we need this later, right now it's not very useful
            //query = TextQuery.queryText(new TextCriteria().matching(request.getText())).sortByScore();

            query = new Query().addCriteria(Criteria.where("name")
                    .regex(Pattern.compile(request.getText(), Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE)));
        }
        if (type != null) {
            query.addCriteria(Criteria.where("roles").is(type));
        }

        query.with(new PageRequest(request.getPageNumber(), request.getPageSize()));
        return query;
    }

    protected Long organizationCountTextByType(final OrganizationSearchRequest request,
                                               Organization.OrganizationType type) {
        Long cnt = mongoTemplate.count(queryTextByType(request, type), Organization.class);
        return cnt;
    }


    public abstract Organization byId(@PathVariable String id);

    public abstract List<Organization> searchText(@Valid OrganizationSearchRequest request);

    public abstract Long count(@Valid OrganizationSearchRequest request);
}