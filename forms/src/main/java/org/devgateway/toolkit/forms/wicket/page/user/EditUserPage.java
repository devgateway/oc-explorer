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

import java.util.Collection;
import java.util.List;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.authroles.authorization.strategies.role.metadata.MetaDataRoleAuthorizationStrategy;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.validation.EqualPasswordInputValidator;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.string.StringValue;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.ValidationError;
import org.apache.wicket.validation.validator.EmailAddressValidator;
import org.apache.wicket.validation.validator.PatternValidator;
import org.devgateway.toolkit.forms.WebConstants;
import org.devgateway.toolkit.forms.security.SecurityConstants;
import org.devgateway.toolkit.forms.security.SecurityUtil;
import org.devgateway.toolkit.forms.service.SendEmailService;
import org.devgateway.toolkit.forms.wicket.components.form.CheckBoxBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.form.PasswordFieldBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.form.Select2ChoiceBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.form.Select2MultiChoiceBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.form.TextFieldBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.page.Homepage;
import org.devgateway.toolkit.forms.wicket.page.edit.AbstractEditPage;
import org.devgateway.toolkit.forms.wicket.page.lists.ListUserPage;
import org.devgateway.toolkit.forms.wicket.providers.GenericPersistableJpaRepositoryTextChoiceProvider;
import org.devgateway.toolkit.persistence.dao.Person;
import org.devgateway.toolkit.persistence.dao.categories.Group;
import org.devgateway.toolkit.persistence.dao.categories.Role;
import org.devgateway.toolkit.persistence.repository.GroupRepository;
import org.devgateway.toolkit.persistence.repository.PersonRepository;
import org.devgateway.toolkit.persistence.repository.RoleRepository;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.wicketstuff.annotation.mount.MountPath;

@AuthorizeInstantiation(SecurityConstants.Roles.ROLE_EDITOR)
@MountPath(value = "/account")
public class EditUserPage extends AbstractEditPage<Person> {
	/**
	 *
	 */
	private static final long serialVersionUID = 5208480049061989277L;

	@SpringBean
	private PersonRepository userRepository;
	@SpringBean
	private GroupRepository groupRepository;
	@SpringBean
	private RoleRepository roleRepository;
	@SpringBean
	private SendEmailService sendEmailService;
	
	private static final int SECRET_LENGTH = 16;

	protected boolean changePassPage = false;

	protected TextFieldBootstrapFormComponent<String> userName = new TextFieldBootstrapFormComponent<>("username");

	protected TextFieldBootstrapFormComponent<String> firstName = new TextFieldBootstrapFormComponent<String>(
			"firstName");

	protected TextFieldBootstrapFormComponent<String> lastName = new TextFieldBootstrapFormComponent<String>(
			"lastName");

	protected TextFieldBootstrapFormComponent<String> email = new TextFieldBootstrapFormComponent<String>("email");

	protected TextFieldBootstrapFormComponent<String> title = new TextFieldBootstrapFormComponent<String>("title");

	protected Select2ChoiceBootstrapFormComponent<Group> group = new Select2ChoiceBootstrapFormComponent<Group>("group",
			new GenericPersistableJpaRepositoryTextChoiceProvider<Group>(groupRepository));

	protected Select2MultiChoiceBootstrapFormComponent<Role> roles = new Select2MultiChoiceBootstrapFormComponent<Role>(
			"roles", new Model<>("Roles"), new GenericPersistableJpaRepositoryTextChoiceProvider<Role>(roleRepository));

	protected CheckBoxBootstrapFormComponent enabled = new CheckBoxBootstrapFormComponent("enabled");

	protected CheckBoxBootstrapFormComponent changePassword = new CheckBoxBootstrapFormComponent("changePassword");

	protected final PasswordFieldBootstrapFormComponent password = new PasswordFieldBootstrapFormComponent(
			"plainPassword");

	protected final PasswordFieldBootstrapFormComponent cpassword = new PasswordFieldBootstrapFormComponent(
			"plainPasswordCheck", new Model<String>());

	protected CheckBoxBootstrapFormComponent cpass = new CheckBoxBootstrapFormComponent("changePass") {
		private static final long serialVersionUID = -1591795804543610117L;

		@Override
		protected void onUpdate(final AjaxRequestTarget target) {
			password.getField().setEnabled(this.getModelObject());
			target.add(password);
			cpassword.getField().setEnabled(this.getModelObject());
			target.add(cpassword);
		}
	};

	protected PageParameters pp;

	public EditUserPage(final PageParameters parameters) {
		super(parameters);
		this.pp = parameters;
		this.jpaRepository = userRepository;
		this.listPageClass = ListUserPage.class;
	}

	protected class RolesValidator implements IValidator<Collection<Role>> {

		private static final long serialVersionUID = -2412508063601996929L;

		@Override
		public void validate(final IValidatable<Collection<Role>> validatable) {
			Collection<Role> value = validatable.getValue();
			Role roleAdmin = roleRepository.findByAuthority(SecurityConstants.Roles.ROLE_ADMIN);
			Role roleUser = roleRepository.findByAuthority(SecurityConstants.Roles.ROLE_EDITOR);
			Role roleValidator = roleRepository.findByAuthority(SecurityConstants.Roles.ROLE_VALIDATOR);

			if (!value.contains(roleAdmin) && value.contains(roleValidator) && !value.contains(roleUser)) {
				validatable.error(new ValidationError(getString("validatorRoleValidator")));
			}

			if (value.contains(roleAdmin) && (!value.contains(roleValidator) || !value.contains(roleUser))) {
				validatable.error(new ValidationError(getString("adminRoleValidator")));
			}
		}
	}

	protected class UniqueUsernameValidator implements IValidator<String> {

		private static final long serialVersionUID = -2412508063601996929L;
		private Long userId;

		public UniqueUsernameValidator() {
			this.userId = new Long(-1);
		}

		public UniqueUsernameValidator(final Long userId) {
			this.userId = userId;
		}

		@Override
		public void validate(final IValidatable<String> validatable) {
			String username = validatable.getValue();
			List<Person> persons = userRepository.findByName(username);
			for (int i = 0; i < persons.size(); i++) {
				if (persons.get(i) != null && !persons.get(i).getId().equals(userId)) {
					ValidationError error = new ValidationError(getString("uniqueUser"));
					validatable.error(error);
					break;
				}
			}
		}
	}

	protected class UniqueEmailAddressValidator implements IValidator<String> {

		private static final long serialVersionUID = 972971245491631372L;

		private Long userId;

		public UniqueEmailAddressValidator() {
			this.userId = new Long(-1);
		}

		public UniqueEmailAddressValidator(final Long userId) {
			this.userId = userId;
		}

		@Override
		public void validate(final IValidatable<String> validatable) {
			String emailAddress = validatable.getValue();
			Person person = userRepository.findByEmail(emailAddress);
			if (person != null && !person.getId().equals(userId)) {
				ValidationError error = new ValidationError(getString("uniqueEmailAddress"));
				validatable.error(error);
			}
		}
	}

	public class PasswordPatternValidator extends PatternValidator {
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

	public class UsernamePatternValidator extends PatternValidator {
		private static final long serialVersionUID = -5456988677371244333L;

		private static final String USERNAME_PATTERN = "[a-zA-Z0-9]*";

		public UsernamePatternValidator() {
			super(USERNAME_PATTERN);
		}
	}

	@Override
	protected Person newInstance() {
		return new Person();
	}

	@Override
	protected void onInitialize() {
		roles.getField().add(new RolesValidator());

		Person person = SecurityUtil.getCurrentAuthenticatedPerson();

		if (!SecurityUtil.isCurrentUserAdmin()) {
			if (person.getId() != getPageParameters().get(WebConstants.PARAM_ID).toLong()) {
				setResponsePage(getApplication().getHomePage());
			}
		}

		super.onInitialize();

		userName.required();
		userName.getField().add(new UsernamePatternValidator());
		StringValue idPerson = getPageParameters().get(WebConstants.PARAM_ID);
		if (!idPerson.isNull()) {
			userName.getField().add(new UniqueUsernameValidator(idPerson.toLong()));
		} else {
			userName.getField().add(new UniqueUsernameValidator());
		}
		userName.setIsFloatedInput(true);
		editForm.add(userName);

		MetaDataRoleAuthorizationStrategy.authorize(userName, Component.ENABLE, SecurityConstants.Roles.ROLE_ADMIN);

		firstName.required();
		firstName.setIsFloatedInput(true);
		editForm.add(firstName);

		lastName.required();
		lastName.setIsFloatedInput(true);
		editForm.add(lastName);

		email.required();
		email.getField().add(EmailAddressValidator.getInstance());
		if (!idPerson.isNull()) {
			email.getField().add(new UniqueEmailAddressValidator(idPerson.toLong()));
		} else {
			email.getField().add(new UniqueEmailAddressValidator());
		}
		email.setIsFloatedInput(true);
		editForm.add(email);

		title.setIsFloatedInput(true);
		editForm.add(title);

		group.required();
		group.setIsFloatedInput(true);
		editForm.add(group);

		MetaDataRoleAuthorizationStrategy.authorize(group, Component.RENDER, SecurityConstants.Roles.ROLE_ADMIN);

		roles.getField().setOutputMarkupId(true);
		roles.setIsFloatedInput(true);
		editForm.add(roles);
		MetaDataRoleAuthorizationStrategy.authorize(roles, Component.RENDER, SecurityConstants.Roles.ROLE_ADMIN);

		if (SecurityUtil.isCurrentUserAdmin() && !SecurityUtil.isUserAdmin(compoundModel.getObject())) {
			password.getField().setResetPassword(false).setEnabled(false);
			cpassword.getField().setResetPassword(false).setEnabled(false);
		} else {
			compoundModel.getObject().setChangePass(compoundModel.getObject().getChangePassword());
			password.getField().setResetPassword(false).setEnabled(compoundModel.getObject().getChangePassword());
			cpassword.getField().setResetPassword(false).setEnabled(compoundModel.getObject().getChangePassword());
		}

		cpass.setIsFloatedInput(true);
		editForm.add(cpass);

		password.getField().add(new PasswordPatternValidator());
		password.setOutputMarkupId(true);
		password.setIsFloatedInput(true);
		editForm.add(password);

		cpassword.setOutputMarkupId(true);
		cpassword.setIsFloatedInput(true);
		editForm.add(cpassword);

		enabled.setIsFloatedInput(true);
		editForm.add(enabled);
		MetaDataRoleAuthorizationStrategy.authorize(enabled, Component.RENDER, SecurityConstants.Roles.ROLE_ADMIN);

		changePassword.setIsFloatedInput(true);
		editForm.add(changePassword);
		MetaDataRoleAuthorizationStrategy.authorize(changePassword, Component.RENDER,
				SecurityConstants.Roles.ROLE_ADMIN);

		MetaDataRoleAuthorizationStrategy.authorize(deleteButton, Component.RENDER, SecurityConstants.Roles.ROLE_ADMIN);

		editForm.add(new EqualPasswordInputValidator(password.getField(), cpassword.getField()));

	}

	@Override
	public SaveEditPageButton getSaveEditPageButton() {
		return new SaveEditPageButton("save", new StringResourceModel("save", EditUserPage.this, null)) {

			private static final long serialVersionUID = 5214537995514151323L;

			@Override
			protected void onSubmit(final AjaxRequestTarget target, final Form<?> form) {
				StandardPasswordEncoder encoder = new StandardPasswordEncoder("");

				if (cpass.getField().getModelObject()) {
					compoundModel.getObject().setPassword(encoder.encode(password.getField().getModelObject()));
				}
				if (changePassPage) {
					compoundModel.getObject().setChangePassword(false);
				}
				if (compoundModel.getObject().isChangePass()) {

					String secret = RandomStringUtils.random(SECRET_LENGTH, true, true);
					compoundModel.getObject().setSecret(encoder.encode(secret));

					if (changePassPage) {
						compoundModel.getObject().setChangePassword(true);
						compoundModel.getObject().setEnabled(false);
					}
					Person saveable = editForm.getModelObject();
					jpaRepository.save(saveable);
					if (changePassPage) {
						// local test
						sendEmailService.sendEmailToEnable(compoundModel.getObject(),
								"http://localhost:8080/changedPasswordValidate?check=");
						getSession().invalidate();
						setResponsePage(GoToEmailPage.class);
					} else {
						setResponsePage(listPageClass);
					}

				} else {
					Person saveable = editForm.getModelObject();
					if (saveable.getPassword() == null || saveable.getPassword().compareTo("") == 0) {
						feedbackPanel.error(new StringResourceModel("nullPassword", this, null).getString());
						target.add(feedbackPanel);
						return;
					}
					jpaRepository.save(saveable);
					if (!SecurityUtil.isCurrentUserAdmin()) {
						setResponsePage(Homepage.class);
					} else {
						setResponsePage(listPageClass);
					}
				}

			}

		};
	}
}
