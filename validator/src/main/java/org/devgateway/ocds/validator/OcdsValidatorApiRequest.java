package org.devgateway.ocds.validator;

import java.util.SortedSet;

/**
 * Created by mpostelnicu on 7/6/17.
 */
public class OcdsValidatorApiRequest extends OcdsValidatorRequest {

    private String json;

    private String url;

    public OcdsValidatorApiRequest(String version, SortedSet<String> extensions, String schemaType) {
        super(version, extensions, schemaType);
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
