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
import org.apache.wicket.util.string.StringValue;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.ValidationError;
import org.devgateway.ocds.forms.wicket.page.list.ListAllDashboardsPage;
import org.devgateway.ocds.forms.wicket.providers.LabelPersistableJpaRepositoryTextChoiceProvider;
import org.devgateway.ocds.persistence.dao.UserDashboard;
import org.devgateway.ocds.persistence.repository.UserDashboardRepository;
import org.devgateway.toolkit.forms.WebConstants;
import org.devgateway.toolkit.forms.security.SecurityConstants;
import org.devgateway.toolkit.forms.wicket.components.form.Select2MultiChoiceBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.form.TextAreaFieldBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.form.TextFieldBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.page.edit.AbstractEditPage;
import org.devgateway.toolkit.persistence.dao.Person;
import org.devgateway.toolkit.persistence.repository.PersonRepository;
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

    @SpringBean
    private PersonRepository personRepository;

    public EditUserDashboardPage(final PageParameters parameters) {
        super(parameters);
        this.jpaRepository = userDashboardRepository;
        this.listPageClass = ListAllDashboardsPage.class;
    }

    protected class DashboardNameValidator implements IValidator<String> {
        private Long id;

        public DashboardNameValidator() {
            this.id = -1L;
        }

        public DashboardNameValidator(final Long id) {
            this.id = id;
        }

        @Override
        public void validate(final IValidatable<String> validatable) {
            String name = validatable.getValue();

            if (name != null && !name.matches(UserDashboard.NAME_REGEXP)) {
                ValidationError error = new ValidationError(UserDashboard.NAME_REGEXP_MESSAGE);
                validatable.error(error);
            }

            UserDashboard dashboard = userDashboardRepository.findByName(name);
            if (dashboard != null && !dashboard.getId().equals(id)) {
                ValidationError error = new ValidationError(getString("uniqueName"));
                validatable.error(error);
            }
        }
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        TextFieldBootstrapFormComponent<String> name = new TextFieldBootstrapFormComponent<>("name");
        name.required();

        StringValue id = getPageParameters().get(WebConstants.PARAM_ID);

        if (!id.isNull()) {
            name.getField().add(new DashboardNameValidator(id.toLong()));
        } else {
            name.getField().add(new DashboardNameValidator());
        }

        editForm.add(name);

        TextAreaFieldBootstrapFormComponent<String> formUrlEncodedBody =
                new TextAreaFieldBootstrapFormComponent<>("formUrlEncodedBody");
        formUrlEncodedBody.required();
        formUrlEncodedBody.getField().setEnabled(false);
        editForm.add(formUrlEncodedBody);

        Select2MultiChoiceBootstrapFormComponent<Person> defaultDashboardUsers =
                new Select2MultiChoiceBootstrapFormComponent<>("defaultDashboardUsers",
                        new LabelPersistableJpaRepositoryTextChoiceProvider<>(personRepository));
        defaultDashboardUsers.setEnabled(false);
        editForm.add(defaultDashboardUsers);

        Select2MultiChoiceBootstrapFormComponent<Person> users =
                new Select2MultiChoiceBootstrapFormComponent<>("users",
                        new LabelPersistableJpaRepositoryTextChoiceProvider<>(personRepository));
        editForm.add(users);

        
    }
}
