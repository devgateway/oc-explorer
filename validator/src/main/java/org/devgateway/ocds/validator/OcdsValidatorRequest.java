package org.devgateway.ocds.validator;

import io.swagger.annotations.ApiModelProperty;
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
        this.operation = request.getOperation();
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

    @ApiModelProperty("This is the version of OCDS schema to validate against. Leaving this empty will enable schema"
            + " autodetection. This is helpful to test against another OCDS schema besides the one specified in "
            + "the incoming JSON.")
    private String version;

    @ApiModelProperty("You can provide a set of OCDS extensions here to validate against. All OCDS core extensions are "
            + " supported, in offline mode, as well as any other OCDS extension given by URL")
    private SortedSet<String> extensions = new TreeSet<>();

    @Pattern(regexp = OcdsValidatorConstants.Operations.VALIDATE + "|"
            + OcdsValidatorConstants.Operations.SHOW_BUILTIN_EXTENSIONS + "|"
            + OcdsValidatorConstants.Operations.SHOW_SUPPORTED_OCDS)
    @ApiModelProperty("Provides the operation that needs to be performed. The default is 'validate'."
            + "'show-supported-ocds' will list the supported ocds versions. show-builtin-extensions will list the "
            + "core OCDS extensions that are supported internally and in offline mode.")
    private String operation = OcdsValidatorConstants.Operations.VALIDATE;

    @NotEmpty(message = "Please provide schemaType!")
    @ApiModelProperty(value = "This is the schema type of the input JSON. Currently supported values are 'release' "
            + "and release-package")
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
