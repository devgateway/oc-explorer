package org.devgateway.toolkit.forms.wicket.page;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.request.flow.RedirectToUrlException;
import org.devgateway.toolkit.forms.security.SecurityConstants;

/**
 * @author idobre
 * @since 10/26/16
 *
 * Simple class the redirect the user to swagger UI URL (which is an external Wicket URL)
 */
@AuthorizeInstantiation(SecurityConstants.Roles.ROLE_USER)
public class SwaggerPage extends WebPage {
    public static final String URL = "/swagger-ui.html";

    public SwaggerPage() {
        throw new RedirectToUrlException(URL);
    }
}
