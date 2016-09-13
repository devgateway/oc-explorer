package org.devgateway.ocds.web.rest.controller;

import com.mongodb.DBObject;
import org.devgateway.ocds.web.rest.controller.request.DefaultFilterPagingRequest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author idobre
 * @since 9/13/16
 *
 * @see {@link AbstractEndPointControllerTest}
 */
public class TenderPercentagesControllerTest extends AbstractEndPointControllerTest {
    @Autowired
    private TenderPercentagesController tenderPercentagesController;

    @Test
    public void percentTendersCancelled() throws Exception {
        final List<DBObject> percentTendersCancelled = tenderPercentagesController
                .percentTendersCancelled(new DefaultFilterPagingRequest());

        logger.error(percentTendersCancelled);
    }

    @Test
    public void percentTendersWithTwoOrMoreTenderers() throws Exception {
        final List<DBObject> percentTendersWithTwoOrMoreTenderers = tenderPercentagesController
                .percentTendersWithTwoOrMoreTenderers(new DefaultFilterPagingRequest());

        logger.error(percentTendersWithTwoOrMoreTenderers);
    }

    @Test
    public void percentTendersAwarded() throws Exception {
        final List<DBObject> percentTendersAwarded = tenderPercentagesController
                .percentTendersAwarded(new DefaultFilterPagingRequest());

        logger.error(percentTendersAwarded);

    }

    @Test
    public void percentTendersUsingEBid() throws Exception {
        final List<DBObject> percentTendersUsingEBid = tenderPercentagesController
                .percentTendersUsingEBid(new DefaultFilterPagingRequest());

        logger.error(percentTendersUsingEBid);
    }

}