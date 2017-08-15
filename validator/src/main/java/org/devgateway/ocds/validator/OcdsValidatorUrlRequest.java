package org.devgateway.ocds.validator;

import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.URL;

public class OcdsValidatorUrlRequest extends OcdsValidatorRequest {

    @NotNull(message = "Please provide an URL!")
    @URL
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
