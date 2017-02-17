package org.devgateway.ocds.web.rest.controller.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * Created by mpostelnicu on 2/17/17.
 * A paging request that also holds the language param
 */
public class LangYearFilterPagingRequest extends YearFilterPagingRequest {

    @NotNull(message = "language must not be null")
    @Pattern(regexp = "^[a-z]{2}_[A-Z]{2}$", message = "Invalid email address!")
    protected String language;

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}
