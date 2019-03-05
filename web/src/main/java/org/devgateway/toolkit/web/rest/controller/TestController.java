package org.devgateway.toolkit.web.rest.controller;

import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author idobre
 * @since 10/16/16
 */

@RestController
@CacheConfig(cacheNames = "reportsApiCache")
@Cacheable
public class TestController {
    private static final Logger logger = LoggerFactory.getLogger(TestController.class);

    @ApiOperation(value = "Test API")
    @RequestMapping(value = "/api/testAPI", method = RequestMethod.GET, produces = "application/json")
    public String testAPI() {
        String responseJson = "{\n"
                + "    \"columnNames\": [\"Col 1\", \"Col 2\"],\n"
                + "    \"data\": [[\"aaa\", \"bbb\"], [\"ccc\", \"ddd\"], [\"eee\", \"fff\"]]\n"
                + "}";

        logger.error(">>> responseJson: " + responseJson);

        return responseJson;
    }
}
