package org.devgateway.ocds.validator;

import java.util.List;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * Created by mpostelnicu on 7/5/17.
 */
public class OcdsValidatorRequest {

    private String ocdsVersion;

    private List<String> extensions;

    @NotNull(message = "Json content cannot be null!")
    @NotEmpty(message = "Json content cannot be empty!")
    private String json;

    public String getOcdsVersion() {
        return ocdsVersion;
    }

    public void setOcdsVersion(String ocdsVersion) {
        this.ocdsVersion = ocdsVersion;
    }

    public List<String> getExtensions() {
        return extensions;
    }

    public void setExtensions(List<String> extensions) {
        this.extensions = extensions;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }
}
