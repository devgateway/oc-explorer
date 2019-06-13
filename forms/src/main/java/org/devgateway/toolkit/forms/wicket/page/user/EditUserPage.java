/*******************************************************************************
 * Copyright (c) 2015 Development Gateway, Inc and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the MIT License (MIT)
 * which accompanies this distribution, and is available at
 * https://opensource.org/licenses/MIT
 *
 * Contributors:
 * Development Gateway - initial API and implementation
 *******************************************************************************/
package org.devgateway.toolkit.forms.wicket.page.user;

import org.apache.commons.lang.BooleanUtils;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.authroles.authorization.strategies.role.metadata.MetaDataRoleAuthorizationStrategy;
import org.apache.wicket.extensions.validation.validator.RfcCompliantEmailAddressValidator;
import org.apache.wicket.markup.html.form.validation.EqualPasswordInputValidator;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.string.StringValue;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.ValidationError;
import org.apache.wicket.validation.validator.PatternValidator;
import org.devgateway.ocds.forms.wicket.providers.LabelPersistableJpaRepositoryTextChoiceProvider;
import org.devgateway.ocds.persistence.dao.UserDashboard;
import org.devgateway.ocds.web.spring.SendEmailService;
import org.devgateway.toolkit.forms.WebConstants;
import org.devgateway.toolkit.forms.wicket.components.form.CheckBoxToggleBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.form.PasswordFieldBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.form.Select2ChoiceBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.form.Select2MultiChoiceBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.form.TextFieldBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.util.ComponentUtil;
import org.devgateway.toolkit.forms.wicket.page.Homepage;
import org.devgateway.toolkit.forms.wicket.page.edit.AbstractEditPage;
import org.devgateway.toolkit.forms.wicket.page.lists.ListUserPage;
import org.devgateway.toolkit.forms.wicket.providers.GenericPersistableJpaTextChoiceProvider;
import org.devgateway.toolkit.persistence.dao.Person;
import org.devgateway.toolkit.persistence.dao.Role;
import org.devgateway.toolkit.persistence.dao.categories.Group;
import org.devgateway.toolkit.persistence.service.PersonService;
import org.devgateway.toolkit.persistence.service.RoleService;
import org.devgateway.toolkit.persistence.service.UserDashboardService;
import org.devgateway.toolkit.persistence.service.category.GroupService;
import org.devgateway.toolkit.web.security.SecurityConstants;
import org.devgateway.toolkit.web.security.SecurityUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.wicketstuff.annotation.mount.MountPath;

@AuthorizeInstantiation(SecurityConstants.Roles.ROLE_USER)
@MountPath(value = "/account")
public class EditUserPage extends AbstractEditPage<Person> {
    private static final long serialVersionUID = 5208480049061989277L;

    @SpringBean
    private PersonService personService;

    @SpringBean
    private GroupService groupService;

    @SpringBean
    private RoleService roleService;

    @SpringBean
    private UserDashboardService userDashboardService;


    @SpringBean
    private SendEmailService sendEmailService;

    @SpringBean
    private PasswordEncoder passwordEncoder;

    protected TextFieldBootstrapFormComponent<String> username;

    protected TextFieldBootstrapFormComponent<String> firstName;

    protected TextFieldBootstrapFormComponent<String> lastName;

    protected TextFieldBootstrapFormComponent<String> email;

    protected Select2ChoiceBootstrapFormComponent<Group> group = new Select2ChoiceBootstrapFormComponent<Group>("group",
            new LabelPersistableJpaRepositoryTextChoiceProvider<>(groupService));

    protected TextFieldBootstrapFormComponent<String> title;

    protected Select2ChoiceBootstrapFormComponent<UserDashboard> defaultDashboard =
            new Select2ChoiceBootstrapFormComponent<>("defaultDashboard",
                    new GenericPersistableJpaTextChoiceProvider<UserDashboard>(userDashboardService));

    protected Select2MultiChoiceBootstrapFormComponent<Role> roles;

    protected CheckBoxToggleBootstrapFormComponent enabled;

    protected CheckBoxToggleBootstrapFormComponent changePasswordNextSignIn;

    protected CheckBoxToggleBootstrapFormComponent changeProfilePassword;

    protected PasswordFieldBootstrapFormComponent plainPassword;

    protected PasswordFieldBootstrapFormComponent plainPasswordCheck;


    public EditUserPage(final PageParameters parameters) {
        super(parameters);

        this.jpaService = personService;
        this.listPageClass = ListUserPage.class;
    }

    @Override
    protected void onInitialize() {
        Person person = SecurityUtil.getCurrentAuthenticatedPerson();

        if (!SecurityUtil.isCurrentUserAdmin()) {
            if (person.getId() != getPageParameters().get(WebConstants.PARAM_ID).toLong()) {
                setResponsePage(getApplication().getHomePage());
            }
        }

        super.onInitialize();

        username = ComponentUtil.addTextField(editForm, "username");
        username.required();
        username.getField().add(new UsernamePatternValidator());
        StringValue idPerson = getPageParameters().get(WebConstants.PARAM_ID);
        if (!idPerson.isNull()) {
            username.getField().add(new UniqueUsernameValidator(idPerson.toLong()));
        } else {
            username.getField().add(new UniqueUsernameValidator());
        }
        editForm.add(username);
        MetaDataRoleAuthorizationStrategy.authorize(username, Component.ENABLE, SecurityConstants.Roles.ROLE_ADMIN);

        firstName = ComponentUtil.addTextField(editForm, "firstName");
        firstName.required();

        lastName = ComponentUtil.addTextField(editForm, "lastName");
        lastName.required();

        email = ComponentUtil.addTextField(editForm, "email");
        email.required()
                .getField().add(RfcCompliantEmailAddressValidator.getInstance());
        if (!idPerson.isNull()) {
            email.getField().add(new UniqueEmailAddressValidator(idPerson.toLong()));
        } else {
            email.getField().add(new UniqueEmailAddressValidator());
        }

        title = ComponentUtil.addTextField(editForm, "title");

        group = ComponentUtil.addSelect2ChoiceField(editForm, "group", groupService);
        group.required();
        MetaDataRoleAuthorizationStrategy.authorize(group, Component.RENDER, SecurityConstants.Roles.ROLE_ADMIN);

        editForm.add(defaultDashboard);
        MetaDataRoleAuthorizationStrategy.authorize(defaultDashboard, Component.ENABLE,
                SecurityConstants.Roles.ROLE_ADMIN);

        roles = ComponentUtil.addSelect2MultiChoiceField(editForm, "roles", roleService);
        roles.required();
        MetaDataRoleAuthorizationStrategy.authorize(roles, Component.RENDER, SecurityConstants.Roles.ROLE_ADMIN);

        enabled = ComponentUtil.addCheckToggle(editForm, "enabled");
        MetaDataRoleAuthorizationStrategy.authorize(enabled, Component.RENDER, SecurityConstants.Roles.ROLE_ADMIN);

        changePasswordNextSignIn = ComponentUtil.addCheckToggle(editForm, "changePasswordNextSignIn");
        MetaDataRoleAuthorizationStrategy.authorize(changePasswordNextSignIn, Component.RENDER,
                SecurityConstants.Roles.ROLE_ADMIN);

        changeProfilePassword = new CheckBoxToggleBootstrapFormComponent("changeProfilePassword") {
            @Override
            protected void onUpdate(final AjaxRequestTarget target) {
                plainPassword.setVisibilityAllowed(this.getModelObject());
                plainPasswordCheck.setVisibilityAllowed(this.getModelObject());

                target.add(plainPassword);
                target.add(plainPasswordCheck);
            }
        };
        editForm.add(changeProfilePassword);

        plainPassword = ComponentUtil.addTextPasswordField(editForm, "plainPassword");
        plainPassword.required();
        // stop resetting the password fields each time they are rendered
        plainPassword.getField().setResetPassword(false);
        plainPassword.getField().add(new PasswordPatternValidator());

        plainPasswordCheck = ComponentUtil.addTextPasswordField(editForm, "plainPasswordCheck");
        plainPasswordCheck.required();
        plainPasswordCheck.getField().setResetPassword(false);

        if (SecurityUtil.isCurrentUserAdmin() && idPerson.isNull()) {
            // hide the change password checkbox and set it's model to true
            editForm.getModelObject().setChangeProfilePassword(true);
            changeProfilePassword.setVisibilityAllowed(false);
            plainPassword.setVisibilityAllowed(true);
            plainPasswordCheck.setVisibilityAllowed(true);
        } else {
            plainPassword.setVisibilityAllowed(
                    BooleanUtils.isTrue(editForm.getModelObject().getChangePasswordNextSignIn()));
            plainPasswordCheck.setVisibilityAllowed(
                    BooleanUtils.isTrue(editForm.getModelObject().getChangePasswordNextSignIn()));
        }

        editForm.add(new EqualPasswordInputValidator(plainPassword.getField(), plainPasswordCheck.getField()));

        MetaDataRoleAuthorizationStrategy.authorize(deleteButton, Component.RENDER, SecurityConstants.Roles.ROLE_ADMIN);
    }

    protected boolean isChangePassPage() {
        return false;
    }

    private void ensureDefaultDashboardIsAlsoAssignedDashboard(Person person) {
        if (person.getDefaultDashboard() != null && !person.getDashboards().contains(person.getDefaultDashboard())) {
            person.getDefaultDashboard().getUsers().add(person);
            person.getDashboards().add(person.getDefaultDashboard());
        }
    }

    @Override
    public SaveEditPageButton getSaveEditPageButton() {
        return new SaveEditPageButton("save", new StringResourceModel("save", EditUserPage.this, null)) {
            private static final long serialVersionUID = 5214537995514151323L;

            @Override
            protected void onSubmit(final AjaxRequestTarget target) {
                super.onSubmit(target);

                final Person person = editForm.getModelObject();
                // encode the password
                if (person.getChangeProfilePassword()) {
                    person.setPassword(passwordEncoder.encode(plainPassword.getField().getModelObject()));
                }

                // user just changed his password so don't force him to change it again next time
                if (isChangePassPage()) {
                    person.setChangePasswordNextSignIn(false);
                }


                ensureDefaultDashboardIsAlsoAssignedDashboard(person);

                jpaService.save(person);
                if (!SecurityUtil.isCurrentUserAdmin()) {
                    setResponsePage(Homepage.class);
                } else {
                    setResponsePage(listPageClass);
                }
            }
        };
    }

    public static class PasswordPatternValidator extends PatternValidator {
        private static final long serialVersionUID = 7886016396095273777L;

        // 1 digit, 1 lower, 1 upper, 1 symbol "@#$%", from 6 to 20
        // private static final String PASSWORD_PATTERN =
        // "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{6,20})";
        // 1 digit, 1 caps letter, from 10 to 20
        private static final String PASSWORD_PATTERN = "((?=.*\\d)(?=.*[a-z]).{10,20})";

        public PasswordPatternValidator() {
            super(PASSWORD_PATTERN);
        }

    }

    public static class UsernamePatternValidator extends PatternValidator {
        private static final long serialVersionUID = -5456988677371244333L;

        private static final String USERNAME_PATTERN = "[a-zA-Z0-9]*";

        public UsernamePatternValidator() {
            super(USERNAME_PATTERN);
        }
    }

    protected class UniqueUsernameValidator implements IValidator<String> {
        private static final long serialVersionUID = -2412508063601996929L;

        private Long userId;

        public UniqueUsernameValidator() {
            this.userId = Long.valueOf(-1);
        }

        public UniqueUsernameValidator(final Long userId) {
            this.userId = userId;
        }

        @Override
        public void validate(final IValidatable<String> validatable) {
            final String username = validatable.getValue();
            final Person person = personService.findByUsername(username);
            if (person != null && !person.getId().equals(userId)) {
                final ValidationError error = new ValidationError(getString("uniqueUser"));
                validatable.error(error);
            }
        }
    }

    protected class UniqueEmailAddressValidator implements IValidator<String> {
        private static final long serialVersionUID = 972971245491631372L;

        private Long userId;

        public UniqueEmailAddressValidator() {
            this.userId = Long.valueOf(-1);
        }

        public UniqueEmailAddressValidator(final Long userId) {
            this.userId = userId;
        }

        @Override
        public void validate(final IValidatable<String> validatable) {
            final String emailAddress = validatable.getValue();
            final Person person = personService.findByEmail(emailAddress);
            if (person != null && !person.getId().equals(userId)) {
                final ValidationError error = new ValidationError(getString("uniqueEmailAddress"));
                validatable.error(error);
            }
        }
    }

}
