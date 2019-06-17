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
public class TenderPercentagesControllerTest extends AbstractEndPointControllerTest {
    @Autowired
    private TenderPercentagesController tenderPercentagesController;

    @Test
    public void percentTendersCancelled() throws Exception {
        final List<Document> percentTendersCancelled = tenderPercentagesController
                .percentTendersCancelled(new YearFilterPagingRequest());

        final Document first = percentTendersCancelled.get(0);
        int year = (int) first.get(TenderPercentagesController.Keys.YEAR);
        int totalTenders = (int) first.get(TenderPercentagesController.Keys.TOTAL_TENDERS);
        int totalCancelled = (int) first.get(TenderPercentagesController.Keys.TOTAL_CANCELLED);
        double percentCancelled = (double) first.get(TenderPercentagesController.Keys.PERCENT_CANCELLED);
        Assert.assertEquals(2014, year);
        Assert.assertEquals(1, totalTenders);
        Assert.assertEquals(0, totalCancelled);
        Assert.assertEquals(0.0, percentCancelled, 0);

        final Document second = percentTendersCancelled.get(1);
        year = (int) second.get(TenderPercentagesController.Keys.YEAR);
        totalTenders = (int) second.get(TenderPercentagesController.Keys.TOTAL_TENDERS);
        totalCancelled = (int) second.get(TenderPercentagesController.Keys.TOTAL_CANCELLED);
        percentCancelled = (double) second.get(TenderPercentagesController.Keys.PERCENT_CANCELLED);
        Assert.assertEquals(2015, year);
        Assert.assertEquals(2, totalTenders);
        Assert.assertEquals(0, totalCancelled);
        Assert.assertEquals(0.0, percentCancelled, 0);
    }

    @Test
    public void percentTendersWithTwoOrMoreTenderers() throws Exception {
        final List<Document> percentTendersWithTwoOrMoreTenderers = tenderPercentagesController
                .percentTendersWithTwoOrMoreTenderers(new YearFilterPagingRequest());

        final Document first = percentTendersWithTwoOrMoreTenderers.get(0);
        int year = (int) first.get(TenderPercentagesController.Keys.YEAR);
        int totalTenders = (int) first.get(TenderPercentagesController.Keys.TOTAL_TENDERS);
        int totalTendersWithTwoOrMoreTenderers = (int) first
                .get(TenderPercentagesController.Keys.TOTAL_TENDERS_WITH_TWO_OR_MORE_TENDERERS);
        double percentTenders = (double) first.get(TenderPercentagesController.Keys.PERCENT_TENDERS);
        Assert.assertEquals(2014, year);
        Assert.assertEquals(1, totalTenders);
        Assert.assertEquals(1, totalTendersWithTwoOrMoreTenderers);
        Assert.assertEquals(100.0, percentTenders, 0);

        final Document second = percentTendersWithTwoOrMoreTenderers.get(1);
        year = (int) second.get(TenderPercentagesController.Keys.YEAR);
        totalTenders = (int) second.get(TenderPercentagesController.Keys.TOTAL_TENDERS);
        totalTendersWithTwoOrMoreTenderers = (int) second
                .get(TenderPercentagesController.Keys.TOTAL_TENDERS_WITH_TWO_OR_MORE_TENDERERS);
        percentTenders = (double) second.get(TenderPercentagesController.Keys.PERCENT_TENDERS);
        Assert.assertEquals(2015, year);
        Assert.assertEquals(2, totalTenders);
        Assert.assertEquals(1, totalTendersWithTwoOrMoreTenderers);
        Assert.assertEquals(50.0, percentTenders, 0);
    }

    @Test
    public void percentTendersAwarded() throws Exception {
        final List<Document> percentTendersAwarded = tenderPercentagesController
                .percentTendersAwarded(new YearFilterPagingRequest());

        final Document first = percentTendersAwarded.get(0);
        int year = (int) first.get(TenderPercentagesController.Keys.YEAR);
        int totalTendersWithOneOrMoreTenderers = (int) first
                .get(TenderPercentagesController.Keys.TOTAL_TENDERS_WITH_ONE_OR_MORE_TENDERERS);
        int totalTendersWithTwoOrMoreTenderers = (int) first
                .get(TenderPercentagesController.Keys.TOTAL_TENDERS_WITH_TWO_OR_MORE_TENDERERS);
        double percentTenders = (double) first.get(TenderPercentagesController.Keys.PERCENT_TENDERS);
        Assert.assertEquals(2014, year);
        Assert.assertEquals(1, totalTendersWithOneOrMoreTenderers);
        Assert.assertEquals(1, totalTendersWithTwoOrMoreTenderers);
        Assert.assertEquals(100.0, percentTenders, 0);

        final Document second = percentTendersAwarded.get(1);
        year = (int) second.get(TenderPercentagesController.Keys.YEAR);
        totalTendersWithOneOrMoreTenderers = (int) second
                .get(TenderPercentagesController.Keys.TOTAL_TENDERS_WITH_ONE_OR_MORE_TENDERERS);
        totalTendersWithTwoOrMoreTenderers = (int) second
                .get(TenderPercentagesController.Keys.TOTAL_TENDERS_WITH_TWO_OR_MORE_TENDERERS);
        percentTenders = (double) second.get(TenderPercentagesController.Keys.PERCENT_TENDERS);
        Assert.assertEquals(2015, year);
        Assert.assertEquals(2, totalTendersWithOneOrMoreTenderers);
        Assert.assertEquals(1, totalTendersWithTwoOrMoreTenderers);
        Assert.assertEquals(50.0, percentTenders, 0);
    }

    @Test
    public void percentTendersUsingEBid() throws Exception {
        final List<Document> percentTendersUsingEBid = tenderPercentagesController
                .percentTendersUsingEBid(new YearFilterPagingRequest());

        final Document first = percentTendersUsingEBid.get(0);
        int year = (int) first.get(TenderPercentagesController.Keys.YEAR);
        int totalTenders = (int) first
                .get(TenderPercentagesController.Keys.TOTAL_TENDERS);
        int totalTendersUsingEbid = (int) first.get(TenderPercentagesController.Keys.TOTAL_TENDERS_USING_EBID);
        double percentageTendersUsingEbid = (double) first
                .get(TenderPercentagesController.Keys.PERCENTAGE_TENDERS_USING_EBID);
        Assert.assertEquals(2014, year);
        Assert.assertEquals(1, totalTenders);
        Assert.assertEquals(1, totalTendersUsingEbid);
        Assert.assertEquals(100.0, percentageTendersUsingEbid, 0);

        final Document second = percentTendersUsingEBid.get(1);
        year = (int) second.get(TenderPercentagesController.Keys.YEAR);
        totalTenders = (int) second
                .get(TenderPercentagesController.Keys.TOTAL_TENDERS);
        totalTendersUsingEbid = (int) second.get(TenderPercentagesController.Keys.TOTAL_TENDERS_USING_EBID);
        percentageTendersUsingEbid = (double) second
                .get(TenderPercentagesController.Keys.PERCENTAGE_TENDERS_USING_EBID);
        Assert.assertEquals(2015, year);
        Assert.assertEquals(1, totalTenders);
        Assert.assertEquals(1, totalTendersUsingEbid);
        Assert.assertEquals(100.0, percentageTendersUsingEbid, 0);
    }

    @Test
    public void percentTendersWithLinkedProcurementPlan() throws Exception {
        final List<Document> percentTendersWithLinkedProcurementPlan = tenderPercentagesController
                .percentTendersWithLinkedProcurementPlan(new YearFilterPagingRequest());

        final Document first = percentTendersWithLinkedProcurementPlan.get(0);
        int year = (int) first.get(TenderPercentagesController.Keys.YEAR);
        int totalTendersWithLinkedProcurementPlan = (int) first
                .get(TenderPercentagesController.Keys.TOTAL_TENDERS_WITH_LINKED_PROCUREMENT_PLAN);
        int totalTenders = (int) first
                .get(TenderPercentagesController.Keys.TOTAL_TENDERS);
        double percentTenders = (double) first.get(TenderPercentagesController.Keys.PERCENT_TENDERS);
        Assert.assertEquals(2014, year);
        Assert.assertEquals(1, totalTendersWithLinkedProcurementPlan);
        Assert.assertEquals(1, totalTenders);
        Assert.assertEquals(100.0, percentTenders, 0);

        final Document second = percentTendersWithLinkedProcurementPlan.get(1);
        year = (int) second.get(TenderPercentagesController.Keys.YEAR);
        totalTendersWithLinkedProcurementPlan = (int) second
                .get(TenderPercentagesController.Keys.TOTAL_TENDERS_WITH_LINKED_PROCUREMENT_PLAN);
        totalTenders = (int) second
                .get(TenderPercentagesController.Keys.TOTAL_TENDERS);
        percentTenders = (double) second.get(TenderPercentagesController.Keys.PERCENT_TENDERS);
        Assert.assertEquals(2015, year);
        Assert.assertEquals(2, totalTendersWithLinkedProcurementPlan);
        Assert.assertEquals(2, totalTenders);
        Assert.assertEquals(100.0, percentTenders, 0);
    }
}
