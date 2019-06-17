package org.devgateway.ocds.web.rest.controller;

import org.bson.Document;
import org.devgateway.ocds.web.rest.controller.request.YearFilterPagingRequest;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author mpostelnicu
 * @since 01/13/2017
 *
 * @see {@link AbstractEndPointControllerTest}
 */
public class FrequentTenderersControllerTest extends AbstractEndPointControllerTest {
    @Autowired
    private FrequentTenderersController frequentTenderersController;

    @Test
    public void frequentTenderersTest() throws Exception {
        final List<Document> frequentTenderers = frequentTenderersController
                .frequentTenderers(new YearFilterPagingRequest());

        Assert.assertEquals(0, frequentTenderers.size());
    }

    @Test
    public void activeAwardsCountTest() throws Exception {
        final List<Document> frequentTenderers = frequentTenderersController
                .activeAwardsCount(new YearFilterPagingRequest());

        Assert.assertEquals(2, frequentTenderers.size());
        Assert.assertEquals(1, frequentTenderers.get(0).get("cnt"));
        Assert.assertEquals(1, frequentTenderers.get(1).get("cnt"));
    }

}
