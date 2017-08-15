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

        public static final String[] ALL = {OCDS_1_0_0, OCDS_1_0_1, OCDS_1_0_2, OCDS_1_1_0};

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
        //OFFICIAL
        public static final String OCDS_BID_EXTENSION_V1_1 = "ocds_bid_extension/v1.1";
        public static final String OCDS_ENQUIRY_EXTENSION_V1_1 = "ocds_enquiry_extension/v1.1";
        public static final String OCDS_LOCATION_EXTENSION_V1_1 = "ocds_location_extension/v1.1";
        public static final String OCDS_LOTS_EXTENSION_V1_1 = "ocds_lots_extension/v1.1";
        public static final String OCDS_MILESTONE_DOCUMENTS_EXTENSION_V1_1 = "ocds_milestone_documents_extension/v1.1";
        public static final String OCDS_PARTICIPATION_FEE_EXTENSION_V1_1 = "ocds_participationFee_extension/v1.1";
        public static final String OCDS_PROCESS_TITLE_EXTENSION_V1_1 = "ocds_process_title_extension/v1.1";

        //COMMUNITY
        public static final String OCDS_ADDITIONAl_CONTACT_POINTS_EXTENSION = "ocds_additionalContactPoints_extension";
        public static final String OCDS_BUDGET_BREAKDOWN_EXTENSION = "ocds_budget_breakdown_extension";
        public static final String OCDS_BUDGET_PROJECTS_EXTENSION = "ocds_budget_projects_extension";
        public static final String OCDS_CHARGES_EXTENSION = "ocds_charges_extension";
        public static final String OCDS_CONTRACT_SUPPLIERS_EXTENSION = "ocds_contract_suppliers_extension";
        public static final String OCDS_DOCUMENTATION_EXTENSION = "ocds_documentation_extension";
        public static final String OCDS_EXTENDS_CONTRACTID_EXTENSION = "ocds_extendsContractID_extension";

        public static final String[] ALL = {OCDS_BID_EXTENSION_V1_1, OCDS_ENQUIRY_EXTENSION_V1_1,
                OCDS_LOCATION_EXTENSION_V1_1, OCDS_LOTS_EXTENSION_V1_1, OCDS_MILESTONE_DOCUMENTS_EXTENSION_V1_1,
                OCDS_PARTICIPATION_FEE_EXTENSION_V1_1, OCDS_PROCESS_TITLE_EXTENSION_V1_1,

                OCDS_ADDITIONAl_CONTACT_POINTS_EXTENSION, OCDS_BUDGET_BREAKDOWN_EXTENSION,
                OCDS_BUDGET_PROJECTS_EXTENSION, OCDS_CHARGES_EXTENSION, OCDS_CONTRACT_SUPPLIERS_EXTENSION,
                OCDS_DOCUMENTATION_EXTENSION, OCDS_EXTENDS_CONTRACTID_EXTENSION};
    }

    public static final String EXTENSIONS_PREFIX = "/schema/extensions/";
    public static final String REMOTE_EXTENSION_META_POSTFIX = "extension.json";

    public static final String EXTENSIONS_PROPERTY = "extensions";
    public static final String RELEASES_PROPERTY = "releases";
    public static final String OCID_PROPERTY = "ocid";
    public static final String VERSION_PROPERTY = "version";

    public static final SortedSet<String> EXTENSIONS = Collections.unmodifiableSortedSet(new TreeSet<>(Arrays.asList(
            Extensions.ALL)));


    public static final String EXTENSION_META = "extension.json";
    public static final String EXTENSION_META_COMPAT_PROPERTY = "compatibility";
    public static final String EXTENSION_RELEASE_JSON = "release-schema.json";
}
