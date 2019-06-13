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
import org.devgateway.ocds.forms.wicket.page.list.ListAllDashboardsPage;
import org.devgateway.ocds.forms.wicket.providers.LabelPersistableJpaRepositoryTextChoiceProvider;
import org.devgateway.ocds.persistence.dao.UserDashboard;
import org.devgateway.toolkit.forms.wicket.components.form.Select2MultiChoiceBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.form.TextAreaFieldBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.form.TextFieldBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.page.edit.AbstractEditPage;
import org.devgateway.toolkit.persistence.dao.Person;
import org.devgateway.toolkit.persistence.service.PersonService;
import org.devgateway.toolkit.persistence.service.UserDashboardService;
import org.devgateway.toolkit.web.security.SecurityConstants;
import org.wicketstuff.annotation.mount.MountPath;

@AuthorizeInstantiation(SecurityConstants.Roles.ROLE_ADMIN)
@MountPath("/editUserDashboard")
public class EditUserDashboardPage extends AbstractEditPage<UserDashboard> {

    private static final long serialVersionUID = -6069250112046118104L;

    @SpringBean
    private UserDashboardService userDashboardService;

    @SpringBean
    private PersonService personService;

    public EditUserDashboardPage(final PageParameters parameters) {
        super(parameters);
        this.jpaService = userDashboardService;
        this.listPageClass = ListAllDashboardsPage.class;

    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        TextFieldBootstrapFormComponent<String> name = new TextFieldBootstrapFormComponent<>("name");
        name.required();
        editForm.add(name);

        TextAreaFieldBootstrapFormComponent<String> formUrlEncodedBody =
                new TextAreaFieldBootstrapFormComponent<>("formUrlEncodedBody");
        formUrlEncodedBody.required();
        formUrlEncodedBody.getField().setEnabled(false);
        editForm.add(formUrlEncodedBody);

        Select2MultiChoiceBootstrapFormComponent<Person> defaultDashboardUsers =
                new Select2MultiChoiceBootstrapFormComponent<>("defaultDashboardUsers",
                        new LabelPersistableJpaRepositoryTextChoiceProvider<Person>(personService));
        defaultDashboardUsers.setEnabled(false);
        editForm.add(defaultDashboardUsers);

        Select2MultiChoiceBootstrapFormComponent<Person> users =
                new Select2MultiChoiceBootstrapFormComponent<>("users",
                        new LabelPersistableJpaRepositoryTextChoiceProvider<>(personService));
        editForm.add(users);

        
    }
}
