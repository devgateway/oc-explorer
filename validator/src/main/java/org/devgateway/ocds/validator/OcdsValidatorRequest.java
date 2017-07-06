package org.devgateway.ocds.validator;

import java.util.TreeSet;
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

    public OcdsValidatorRequest(String version, TreeSet<String> extensions, String schemaType) {
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

    private TreeSet<String> extensions;

    @NotEmpty(message = "Please provide schemaType!")
    private String schemaType;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public TreeSet<String> getExtensions() {
        return extensions;
    }

    public void setExtensions(TreeSet<String> extensions) {
        this.extensions = extensions;
    }


    public String getSchemaType() {
        return schemaType;
    }

    public void setSchemaType(String schemaType) {
        this.schemaType = schemaType;
    }
}
