package org.devgateway.ocds.validator;

import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
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

    public static final String EXTENSIONS_PREFIX = "/extensions/";

    public static final Set<String> EXTENSIONS = Collections.unmodifiableSet(new TreeSet<>(Arrays.asList(
            new String[]{"ocds_bid_extension", "ocds_enquiry_extension"})));

    public static final String EXTENSION_META = "extension.json";
    public static final String EXTENSION_RELEASE_JSON = "release-schema.json";
}
