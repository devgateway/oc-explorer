package org.devgateway.ocds.web.rest.controller.flags;

import io.swagger.annotations.ApiOperation;
import org.bson.Document;
import org.devgateway.ocds.persistence.mongo.flags.FlagsConstants;
import org.devgateway.ocds.web.rest.controller.request.YearFilterPagingRequest;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by mpostelnicu
 */
@RestController
@CacheConfig(keyGenerator = "genericPagingRequestKeyGenerator", cacheNames = "genericPagingRequestJson")
@Cacheable
public class FlagI085ReleaseSearchController extends AbstractFlagReleaseSearchController {
    @Override
    protected String getFlagProperty() {
        return FlagsConstants.I085_VALUE;
    }

    @Override
    @ApiOperation(value = "Search releases by flag i085")
    @RequestMapping(value = "/api/flags/i085/releases",
            method = { RequestMethod.POST, RequestMethod.GET }, produces = "application/json")
    public List<Document> releaseFlagSearch(@ModelAttribute @Valid YearFilterPagingRequest filter) {
        return super.releaseFlagSearch(filter);
    }

    @Override
    @ApiOperation(value = "Counts releases by flag i085")
    @RequestMapping(value = "/api/flags/i085/count",
            method = { RequestMethod.POST, RequestMethod.GET }, produces = "application/json")
    public List<Document> releaseFlagCount(@ModelAttribute @Valid YearFilterPagingRequest filter) {
        return super.releaseFlagCount(filter);
    }
}
