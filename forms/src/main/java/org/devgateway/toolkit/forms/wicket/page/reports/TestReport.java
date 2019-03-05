package org.devgateway.toolkit.forms.wicket.page.reports;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.request.Url;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.devgateway.toolkit.forms.security.SecurityConstants;
import org.devgateway.toolkit.forms.wicket.page.Homepage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wicketstuff.annotation.mount.MountPath;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * @author idobre
 * @since 10/13/16
 */
@MountPath(value = "reports/testReport")
@AuthorizeInstantiation(SecurityConstants.Roles.ROLE_USER)
public class TestReport extends AbstractFilteredReportPage {
    private static final Logger logger = LoggerFactory.getLogger(TestReport.class);

    public static final String HOST = "host";

    public TestReport(final PageParameters pageParameters) {
        super("reports/testReport.prpt", pageParameters);

        this.caching = false;
    }

    @Override
    public void onInitialize() {
        super.onInitialize();

        addOrReplace(new Label("pageTitle", "Test Report"));
    }

    @Override
    protected void onFilterSubmit(final AjaxRequestTarget target, final Form form) {
        final PageParameters params = new PageParameters();

        setResponsePage(this.getClass(), params);
    }

    @Override
    public Map<String, Object> getReportParameters() {
        final Map<String, Object> params = new HashMap<>();

        params.put(HOST, host());

        return params;
    }

    private String host() {
        String host = "";
        try {
            URL url = new URL(getRequestCycle().getUrlRenderer().renderFullUrl(
                    Url.parse(urlFor(Homepage.class, null).toString())));
            host = url.getProtocol() + "://" + url.getAuthority();
        } catch (MalformedURLException e) {
            logger.error("Wrong URL", e);
        }

        return host;
    }
}
