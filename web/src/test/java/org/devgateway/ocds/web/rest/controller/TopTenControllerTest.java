package org.devgateway.ocds.web.rest.controller;

import com.mongodb.DBObject;
import org.devgateway.ocds.web.rest.controller.request.YearFilterPagingRequest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author idobre
 * @since 9/13/16
 *
 * @see {@link AbstractEndPointControllerTest}
 */
public class TopTenControllerTest extends AbstractEndPointControllerTest {
    @Autowired
    private TopTenController topTenController;

    @Test
    public void topTenLargestAwards() throws Exception {
        final List<DBObject> topTenLargestAwards = topTenController
                .topTenLargestAwards(new YearFilterPagingRequest());

        logger.error(topTenLargestAwards);
    }

    @Test
    public void topTenLargestTenders() throws Exception {
        final List<DBObject> topTenLargestTenders = topTenController
                .topTenLargestTenders(new YearFilterPagingRequest());

        logger.error(topTenLargestTenders);
    }
}
