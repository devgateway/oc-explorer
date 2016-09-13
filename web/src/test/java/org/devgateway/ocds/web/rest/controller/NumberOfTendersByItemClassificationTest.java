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
public class NumberOfTendersByItemClassificationTest extends AbstractEndPointControllerTest {
    @Autowired
    private NumberOfTendersByItemClassification numberOfTendersByItemClassification;

    @Test
    public void numberOfTendersByItemClassification() throws Exception {
        final List<DBObject> numberOfTendersByItem = numberOfTendersByItemClassification
                .numberOfTendersByItemClassification(new YearFilterPagingRequest());

        logger.error(numberOfTendersByItem);
    }
}
