/*******************************************************************************
 * Copyright (c) 2016 Development Gateway, Inc and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the MIT License (MIT)
 * which accompanies this distribution, and is available at
 * https://opensource.org/licenses/MIT
 *
 * Contributors:
 * Development Gateway - initial API and implementation
 *******************************************************************************/
package org.devgateway.ocds.forms.wicket.page.edit;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.ocds.forms.wicket.page.list.ListUserDashboardPage;
import org.devgateway.ocds.persistence.dao.UserDashboard;
import org.devgateway.toolkit.forms.security.SecurityConstants;
import org.devgateway.toolkit.forms.wicket.components.form.TextFieldBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.page.edit.AbstractEditPage;
import org.devgateway.toolkit.persistence.repository.UserDashboardRepository;
import org.wicketstuff.annotation.mount.MountPath;

@AuthorizeInstantiation(SecurityConstants.Roles.ROLE_ADMIN)
@MountPath("/editUserDashboard")
public class EditUserDashboardPage extends AbstractEditPage<UserDashboard> {

    private static final long serialVersionUID = -6069250112046118104L;

    @Override
    protected UserDashboard newInstance() {
        return new UserDashboard();
    }

    @SpringBean
    private UserDashboardRepository userDashboardRepository;

    public EditUserDashboardPage(final PageParameters parameters) {
        super(parameters);
        this.jpaRepository = userDashboardRepository;
        this.listPageClass = ListUserDashboardPage.class;

    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        TextFieldBootstrapFormComponent<String> name = new TextFieldBootstrapFormComponent<>("name");
        name.required();
        editForm.add(name);

    }
}
