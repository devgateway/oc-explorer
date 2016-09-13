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
public class TenderPriceByTypeYearControllerTest extends AbstractEndPointControllerTest {
    @Autowired
    private TenderPriceByTypeYearController tenderPriceByTypeYearController;

    @Test
    public void tenderPriceByProcurementMethod() throws Exception {
        final List<DBObject> tenderPriceByProcurementMethod = tenderPriceByTypeYearController
                .tenderPriceByProcurementMethod(new YearFilterPagingRequest());

        logger.error(tenderPriceByProcurementMethod);
    }
}
