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
public class TenderPriceByTypeYearControllerTest extends AbstractEndPointControllerTest {
    @Autowired
    private TenderPriceByTypeYearController tenderPriceByTypeYearController;

    @Test
    public void tenderPriceByProcurementMethod() throws Exception {
        final List<Document> tenderPriceByProcurementMethod = tenderPriceByTypeYearController
                .tenderPriceByProcurementMethod(new YearFilterPagingRequest());

        final Document first = tenderPriceByProcurementMethod.get(0);
        String procurementMethod = (String) first.get(TenderPriceByTypeYearController.Keys.PROCUREMENT_METHOD);
        Number totalTenderAmount = (Number) first.get(TenderPriceByTypeYearController.Keys.TOTAL_TENDER_AMOUNT);
        Assert.assertEquals("selective", procurementMethod);
        Assert.assertEquals(600000.0, totalTenderAmount.doubleValue(), 0);

        final Document second = tenderPriceByProcurementMethod.get(1);
        procurementMethod = (String) second.get(TenderPriceByTypeYearController.Keys.PROCUREMENT_METHOD);
        totalTenderAmount = (Number) second.get(TenderPriceByTypeYearController.Keys.TOTAL_TENDER_AMOUNT);
        Assert.assertEquals("open", procurementMethod);
        Assert.assertEquals(9000.0, totalTenderAmount.doubleValue(), 0);
    }
}

