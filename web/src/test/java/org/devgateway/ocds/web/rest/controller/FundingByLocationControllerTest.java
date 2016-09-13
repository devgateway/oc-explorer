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
public class FundingByLocationControllerTest extends AbstractEndPointControllerTest {
    @Autowired
    private FundingByLocationController fundingByLocationController;

    @Test
    public void fundingByTenderDeliveryLocation() throws Exception {
        final List<DBObject> fundingByTenderDeliveryLocation = fundingByLocationController
                .fundingByTenderDeliveryLocation(new DefaultFilterPagingRequest());

        logger.error(fundingByTenderDeliveryLocation);
    }

    @Test
    public void qualityFundingByTenderDeliveryLocation() throws Exception {
        final List<DBObject> qualityFundingByTenderDeliveryLocation = fundingByLocationController
                .qualityFundingByTenderDeliveryLocation(new DefaultFilterPagingRequest());

        logger.error(qualityFundingByTenderDeliveryLocation);
    }

}