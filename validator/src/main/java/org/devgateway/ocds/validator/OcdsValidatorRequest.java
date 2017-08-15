package org.devgateway.ocds.validator;

import java.util.SortedSet;
import java.util.TreeSet;
import javax.validation.constraints.Pattern;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * Created by mpostelnicu on 7/5/17.
 */
public abstract class OcdsValidatorRequest {

    public OcdsValidatorRequest() {

    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

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
     *
     * @return
     */
    public String getKey() {
        return schemaType + "-" + version + "-" + extensions;
    }

    private String version;

    private SortedSet<String> extensions = new TreeSet<>();

    @Pattern(regexp = OcdsValidatorConstants.Operations.VALIDATE + "|"
            + OcdsValidatorConstants.Operations.SHOW_BUILTIN_EXTENSIONS + "|"
            + OcdsValidatorConstants.Operations.SHOW_SUPPORTED_OCDS)
    private String operation = OcdsValidatorConstants.Operations.VALIDATE;

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
