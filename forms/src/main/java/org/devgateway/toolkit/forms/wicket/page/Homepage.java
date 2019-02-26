/**
 * Copyright (c) 2015 Development Gateway, Inc and others.
 * <p>
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the MIT License (MIT)
 * which accompanies this distribution, and is available at
 * https://opensource.org/licenses/MIT
 * <p>
 * Contributors:
 * Development Gateway - initial API and implementation
 */
/**
 *
 */
package org.devgateway.toolkit.forms.wicket.page;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.devgateway.toolkit.forms.security.SecurityConstants;

/**
 * @author mpostelnicu
 *
 */
@AuthorizeInstantiation(SecurityConstants.Roles.ROLE_USER)
public class Homepage extends BasePage {
    /**
     * @param parameters
     */
    public Homepage(final PageParameters parameters) {
        super(parameters);
    }
}
