package org.devgateway.ocds.web.rest.controller;

import org.bson.Document;
import org.devgateway.ocds.web.rest.controller.request.YearFilterPagingRequest;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author idobre
 * @since 9/13/16
 *
 * @see {@link AbstractEndPointControllerTest}
 */
public class CountPlansTendersAwardsControllerTest extends AbstractEndPointControllerTest {
    @Autowired
    private CountPlansTendersAwardsController countPlansTendersAwardsController;

    @Test
    public void countTendersByYear() throws Exception {
        final List<Document> countTendersByYear = countPlansTendersAwardsController
                .countTendersByYear(new YearFilterPagingRequest());

        final Document first = countTendersByYear.get(0);
        int year = (int) first.get(CountPlansTendersAwardsController.Keys.YEAR);
        int count = (int) first.get(CountPlansTendersAwardsController.Keys.COUNT);
        Assert.assertEquals(2014, year);
        Assert.assertEquals(1, count);

        final Document second = countTendersByYear.get(1);
        year = (int) second.get(CountPlansTendersAwardsController.Keys.YEAR);
        count = (int) second.get(CountPlansTendersAwardsController.Keys.COUNT);
        Assert.assertEquals(2015, year);
        Assert.assertEquals(2, count);
    }

    @Test
    public void countAwardsByYear() throws Exception {
        final List<Document> countAwardsByYear = countPlansTendersAwardsController
                .countAwardsByYear(new YearFilterPagingRequest());

        final Document first = countAwardsByYear.get(0);
        int year = (int) first.get(CountPlansTendersAwardsController.Keys.YEAR);
        int count = (int) first.get(CountPlansTendersAwardsController.Keys.COUNT);
        Assert.assertEquals(2015, year);
        Assert.assertEquals(1, count);

        final Document second = countAwardsByYear.get(1);
        year = (int) second.get(CountPlansTendersAwardsController.Keys.YEAR);
        count = (int) second.get(CountPlansTendersAwardsController.Keys.COUNT);
        Assert.assertEquals(2016, year);
        Assert.assertEquals(2, count);
    }

}
