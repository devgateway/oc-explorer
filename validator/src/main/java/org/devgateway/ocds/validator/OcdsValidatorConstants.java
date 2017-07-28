package org.devgateway.ocds.validator;

import java.util.Arrays;
import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Created by mpostelnicu on 7/5/17.
 */
public final class OcdsValidatorConstants {

    private OcdsValidatorConstants() {

    }

    public static final class Versions {
        public static final String OCDS_1_0_0 = "1.0.0";
        public static final String OCDS_1_0_1 = "1.0.1";
        public static final String OCDS_1_0_2 = "1.0.2";
        public static final String OCDS_1_1_0 = "1.1.0";
    }

    public static final class Schemas {
        public static final String RELEASE = "release";
        public static final String RELEASE_PACKAGE = "release-package";
        public static final String RECORD_PACKAGE = "record-package";
        public static final String[] ALL = {RELEASE, RELEASE_PACKAGE, RECORD_PACKAGE};
    }

    public static final class SchemaPrefixes {
        public static final String RELEASE = "/schema/release/release-schema-";
        public static final String RELEASE_PACKAGE = "/schema/release-package/release-package-schema-";
        public static final String RECORD_PACKAGE = "/schema/record-package/record-package-schema-";
    }

    public static final String SCHEMA_POSTFIX = ".json";

    public static final class Extensions {
        public static final String OCDS_BID_EXTENSION = "ocds_bid_extension";
        public static final String OCDS_ENQUIRY_EXTENSION = "ocds_enquiry_extension";
        public static final String OCDS_LOCATION_EXTENSION = "ocds_location_extension";
        public static final String OCDS_LOTS_EXTENSION = "ocds_lots_extension";
        public static final String OCDS_MILESTONE_DOCUMENTS_EXTENSION = "ocds_milestone_documents_extension";
        public static final String OCDS_PARTICIPATION_FEE_EXTENSION = "ocds_participationFee_extension";
        public static final String OCDS_PROCESS_TITLE_EXTENSION = "ocds_process_title_extension";

        public static final String[] ALL = {OCDS_BID_EXTENSION, OCDS_ENQUIRY_EXTENSION, OCDS_LOCATION_EXTENSION,
                OCDS_LOTS_EXTENSION, OCDS_MILESTONE_DOCUMENTS_EXTENSION, OCDS_PARTICIPATION_FEE_EXTENSION,
                OCDS_PROCESS_TITLE_EXTENSION};
    }

    public static final String EXTENSIONS_PREFIX = "/schema/extensions/";

    public static final SortedSet<String> EXTENSIONS = Collections.unmodifiableSortedSet(new TreeSet<>(Arrays.asList(
            Extensions.ALL)));


    public static final String EXTENSION_META = "extension.json";
    public static final String EXTENSION_RELEASE_JSON = "release-schema.json";
}
