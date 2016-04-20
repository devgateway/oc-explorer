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
/**
 *
 */
package org.devgateway.toolkit.forms.wicket.page.user;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.authroles.authentication.AbstractAuthenticatedWebSession;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxButton;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.time.Duration;
import org.devgateway.toolkit.forms.security.SecurityUtil;
import org.devgateway.toolkit.forms.wicket.SSAuthenticatedWebSession;
import org.devgateway.toolkit.forms.wicket.components.form.PasswordFieldBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.form.TextFieldBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.page.BasePage;
import org.devgateway.toolkit.forms.wicket.page.Homepage;
import org.devgateway.toolkit.persistence.dao.Person;
import org.devgateway.toolkit.persistence.repository.PersonRepository;
import org.wicketstuff.annotation.mount.MountPath;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons;
import de.agilecoders.wicket.core.markup.html.bootstrap.common.NotificationPanel;
import de.agilecoders.wicket.core.markup.html.bootstrap.form.BootstrapForm;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.ladda.LaddaAjaxButton;

/**
 * @author mpostelnicu
 *
 */
@MountPath("/login")
public class LoginPage extends BasePage {

	private static final long serialVersionUID = 1L;

	@SpringBean
	private PersonRepository personRepository;

	private IndicatingAjaxButton enableAccount;
	
	private static final int HIDE_NOTIFICATION_SECONDS = 15;

	public class LoginForm extends BootstrapForm<Void> {

		private static final long serialVersionUID = 2066636625524650473L;
		private String username;
		private String password;

		public LoginForm(final String id) {
			super(id);

			pageTitle.setVisible(false);
			final NotificationPanel notificationPanel = new NotificationPanel("loginFeedback");
			notificationPanel.hideAfter(Duration.seconds(HIDE_NOTIFICATION_SECONDS));
			notificationPanel.setOutputMarkupId(true);
			add(notificationPanel);

			TextFieldBootstrapFormComponent<String> username = new TextFieldBootstrapFormComponent<>("username",
					new StringResourceModel("user", LoginPage.this), new PropertyModel<String>(this, "username"));
			username.required();
			add(username);

			PasswordFieldBootstrapFormComponent password = new PasswordFieldBootstrapFormComponent("password",
					new PropertyModel<String>(this, "password"));
			password.getField().setResetPassword(false);
			add(password);

			LaddaAjaxButton submit = new LaddaAjaxButton("submit", Buttons.Type.Primary) {
				private static final long serialVersionUID = 1L;

				@Override
				protected void onSubmit(final AjaxRequestTarget target, final Form<?> form) {
					SSAuthenticatedWebSession session = SSAuthenticatedWebSession.getSSAuthenticatedWebSession();
					if (session.signIn(LoginForm.this.username, LoginForm.this.password)) {
						Person user = SecurityUtil.getCurrentAuthenticatedPerson();
						setResponsePage(getApplication().getHomePage());
					} else {
						notificationPanel.error(getString("bad_credentials"));
						target.add(notificationPanel);
						target.add(LoginForm.this);
					}
				}

				@Override
				protected void onError(final AjaxRequestTarget target, final Form<?> form) {
					target.add(notificationPanel);
					target.add(LoginForm.this);
				}
			};

			submit.setLabel(Model.of("Submit"));
			add(submit);
			// IndicatingAjaxButton forgotPassword = new
			// IndicatingAjaxButton("forgotPassword",new
			// StringResourceModel("forgotPassword", LoginPage.this, null)) {
			// private static final long serialVersionUID = 1L;
			//
			// @Override
			// protected void onConfigure() {
			// super.onConfigure();
			// setDefaultFormProcessing(false);
			// }
			//
			// @Override
			// protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
			// setResponsePage(ForgotYourPasswordPage.class);
			// }
			//
			// @Override
			// protected void onError(AjaxRequestTarget target, Form<?> form) {
			// target.add(notificationPanel);
			// target.add(LoginForm.this);
			// }
			//
			// };
			// add(forgotPassword);

		}
	}

	/**
	 * @param parameters
	 */
	public LoginPage(final PageParameters parameters) {
		super(parameters);

		// redirect to homepage if user reaches the /login page while
		// authenticated
		if (AbstractAuthenticatedWebSession.get().isSignedIn()) {
			setResponsePage(Homepage.class);
		}

		LoginForm loginForm = new LoginForm("loginform");
		add(loginForm);

	}

	@Override
	protected void onInitialize() {
		super.onInitialize();

		// hide footer since it's not stick to the bottom of the page
		footer.setVisibilityAllowed(false);
	}
}
