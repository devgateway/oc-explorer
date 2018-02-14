/*******************************************************************************
 * Copyright (c) 2015 Development Gateway, Inc and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the MIT License (MIT)
 * which accompanies this distribution, and is available at
 * https://opensource.org/licenses/MIT
 *
 * Contributors:
 * Development Gateway - initial API and implementation
 *******************************************************************************/
package org.devgateway.ocds.persistence.mongo.constants;

public final class MongoConstants {

    private MongoConstants() {
    }


    public static final String OCDS_PREFIX = "ocds-ep75k8-";

    public static final int IMPORT_ROW_BATCH = 1000;

    public static final String MONGO_LANGUAGE = "english";

    public static final class FieldNames {
        public static final String AWARDS_DATE = "awards.date";
        public static final String AWARDS_STATUS = "awards.status";
        public static final String AWARDS_SUPPLIERS_ID = "awards.suppliers._id";
        public static final String AWARDS_SUPPLIERS_NAME = "awards.suppliers.name";
        public static final String AWARDS_VALUE_AMOUNT = "awards.value.amount";
        public static final String TENDER_PERIOD_START_DATE = "tender.tenderPeriod.startDate";
        public static final String TENDER_PROCURING_ENTITY_ID = "tender.procuringEntity._id";
        public static final String TENDER_PROCURING_ENTITY_NAME = "tender.procuringEntity.name";
        public static final String TENDER_PERIOD_END_DATE = "tender.tenderPeriod.endDate";
        public static final String TENDER_VALUE_AMOUNT = "tender.value.amount";
        public static final String TENDER_NO_TENDERERS = "tender.numberOfTenderers";
        public static final String TENDER_PROC_METHOD = "tender.procurementMethod";
        public static final String TENDER_STATUS = "tender.status";
        public static final String TENDER_SUBMISSION_METHOD = "tender.submissionMethod";
        public static final String BIDS_DETAILS_TENDERERS_ID = "bids.details.tenderers._id";
        public static final String BIDS_DETAILS_VALUE_AMOUNT = "bids.details.value.amount";
        public static final String FLAGS_TOTAL_FLAGGED = "flags.totalFlagged";
    }

    public static final class Filters {
        public static final String YEAR = "year";

        public static final String TEXT = "text";

        public static final String AWARD_STATUS = "awardStatus";

        public static final String BID_TYPE_ID = "bidTypeId";

        public static final String NOT_BID_TYPE_ID = "notBidTypeId";

        public static final String PROCURING_ENTITY_ID = "procuringEntityId";

        public static final String NOT_PROCURING_ENTITY_ID = "notProcuringEntityId";

        public static final String SUPPLIER_ID = "supplierId";

        public static final String BIDDER_ID = "bidderId";

        public static final String TENDER_LOC = "tenderLoc";

        public static final String PROCUREMENT_METHOD = "procurementMethod";

        public static final String TENDER_VALUE = "tenderValue";

        public static final String AWARD_VALUE = "awardValue";

        public static final String FLAG_TYPE = "flagType";

        public static final String ELECTRONIC_SUBMISSION = "electronicSubmission";

        public static final String FLAGGED = "flagged";

        public static final String TOTAL_FLAGGED = "totalFlagged";
    }
}
