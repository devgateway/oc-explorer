package org.devgateway.ocds.validator;

import java.util.SortedSet;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * Created by mpostelnicu on 7/5/17.
 */
public abstract class OcdsValidatorRequest {

    public OcdsValidatorRequest(OcdsValidatorRequest request) {
        this.version = request.getVersion();
        this.extensions = request.getExtensions();
        this.schemaType = request.getSchemaType();
    }

    public OcdsValidatorRequest(String version, SortedSet<String> extensions, String schemaType) {
        this.version = version;
        this.extensions = extensions;
        this.schemaType = schemaType;
    }

    /**
     * This returns a unique key of the validator request based on the set contents , version and schemaType
     * @return
     */
    public String getKey() {
        return schemaType + "-" + version + "-" + extensions;
    }

    private String version;

    private SortedSet<String> extensions;

    private String operation;

    @NotEmpty(message = "Please provide schemaType!")
    private String schemaType;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public SortedSet<String> getExtensions() {
        return extensions;
    }

    public void setExtensions(SortedSet<String> extensions) {
        this.extensions = extensions;
    }


    public String getSchemaType() {
        return schemaType;
    }

    public void setSchemaType(String schemaType) {
        this.schemaType = schemaType;
    }
}
