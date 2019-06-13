package org.devgateway.ocds.web.rest.controller;

import org.bson.Document;
import org.devgateway.ocds.web.rest.controller.request.YearFilterPagingRequest;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author mpostelnicu
 * @see {@link AbstractEndPointControllerTest}
 * @since 2/9/17.
 */
public class LocationInfowindowControllerTest extends AbstractEndPointControllerTest {
    @Autowired
    private LocationInfowindowController locationInfowindowController;

    @Test
    public void tendersByLocation() throws Exception {
        final List<Document> tendersByLocation = locationInfowindowController.tendersByLocation(
                new YearFilterPagingRequest());
        Assert.assertEquals(1, tendersByLocation.size());
        final Document first = tendersByLocation.get(0);
    }

    @Test
    public void awardsByLocation() throws Exception {
        final List<Document> awardsByLocation = locationInfowindowController.awardsByLocation(
                new YearFilterPagingRequest());
        Assert.assertEquals(1, awardsByLocation.size());
        final Document first = awardsByLocation.get(0);
    }

    @Test
    public void planningByLocation() throws Exception {
        final List<Document> planningByLocation = locationInfowindowController.planningByLocation(
                new YearFilterPagingRequest());
        Assert.assertEquals(1, planningByLocation.size());
        final Document first = planningByLocation.get(0);
    }

}
