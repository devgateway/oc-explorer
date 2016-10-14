package org.devgateway.toolkit.forms.wicket.styles;

import org.apache.wicket.request.resource.CssResourceReference;

/**
 * @author idobre
 * @since 10/14/16
 */
public class ReportsStyles extends CssResourceReference {
    private static final long serialVersionUID = 1L;

    public static final ReportsStyles INSTANCE = new ReportsStyles();

    /**
     * Construct.
     */
    public ReportsStyles() {
        super(ReportsStyles.class, "ReportsStyles.css");
    }
}
