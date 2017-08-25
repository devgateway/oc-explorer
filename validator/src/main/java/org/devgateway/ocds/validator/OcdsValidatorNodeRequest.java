package org.devgateway.ocds.validator;

import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.annotations.ApiModelProperty;
import java.util.TreeSet;

/**
 * Created by mpostelnicu on 7/6/17.
 */
public class OcdsValidatorNodeRequest extends OcdsValidatorRequest {

    @ApiModelProperty(value = "The json to validate against OCDS schema")
    private JsonNode node;

    public OcdsValidatorNodeRequest(String version, TreeSet<String> extensions, String schemaType) {
        super(version, extensions, schemaType);
    }

    public OcdsValidatorNodeRequest() {

    }

    public OcdsValidatorNodeRequest(OcdsValidatorRequest request, JsonNode node) {
        super(request);
        this.node = node;
    }

    public JsonNode getNode() {
        return node;
    }

    public void setNode(JsonNode node) {
        this.node = node;
    }
}
