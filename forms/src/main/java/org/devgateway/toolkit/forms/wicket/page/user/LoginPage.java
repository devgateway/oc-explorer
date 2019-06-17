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
package org.devgateway.toolkit.forms.wicket.page.user;

import de.agilecoders.wicket.core.markup.html.bootstrap.common.NotificationPanel;
import de.agilecoders.wicket.core.markup.html.bootstrap.form.BootstrapForm;
import org.apache.commons.lang.BooleanUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.authroles.authentication.AbstractAuthenticatedWebSession;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxButton;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.flow.RedirectToUrlException;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.string.StringValue;
import org.apache.wicket.util.time.Duration;
import org.devgateway.toolkit.forms.WebConstants;
import org.devgateway.toolkit.forms.security.SecurityUtil;
import org.devgateway.toolkit.forms.wicket.SSAuthenticatedWebSession;
import org.devgateway.toolkit.forms.wicket.components.form.PasswordFieldBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.form.TextFieldBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.util.ComponentUtil;
import org.devgateway.toolkit.forms.wicket.page.BasePage;
import org.devgateway.toolkit.forms.wicket.page.Homepage;
import org.devgateway.toolkit.persistence.dao.Person;
import org.devgateway.toolkit.persistence.service.PersonService;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.wicketstuff.annotation.mount.MountPath;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;

/**
 * @author mpostelnicu
 *
 */
@MountPath("/login")
public class LoginPage extends BasePage {
    private static final int HIDE_NOTIFICATION_SECONDS = 15;

    @SpringBean
    private PersonService personService;

    private final LoginBean loginBean;

    public LoginPage(final PageParameters parameters) {
        super(parameters);

        // redirect to homepage if user reaches the /login page while authenticated
        if (AbstractAuthenticatedWebSession.get().isSignedIn()) {
            setResponsePage(Homepage.class);
        }

        loginBean = new LoginBean();
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        pageTitle.setVisible(false);

        final LoginForm loginForm = new LoginForm("loginform", new CompoundPropertyModel<>(loginBean));
        add(loginForm);
    }

    class LoginBean implements Serializable {
        private String username;

        private String password;

        public String getUsername() {
            return username;
        }

        public void setUsername(final String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(final String password) {
            this.password = password;
        }
    }


    class LoginForm extends BootstrapForm<LoginBean> {
        private static final long serialVersionUID = 2066636625524650473L;

        private TextFieldBootstrapFormComponent<String> username;

        private PasswordFieldBootstrapFormComponent password;

        private String referrer;

        LoginForm(final String componentId, final IModel<LoginBean> model) {
            super(componentId, model);
        }

        @Override
        protected void onInitialize() {
            super.onInitialize();

            pageTitle.setVisible(false);

            retrieveReferrerFromSavedRequestIfPresent();

            final NotificationPanel notificationPanel = new NotificationPanel("loginFeedback");
            notificationPanel.hideAfter(Duration.seconds(HIDE_NOTIFICATION_SECONDS));
            notificationPanel.setOutputMarkupId(true);
            add(notificationPanel);

            username = ComponentUtil.addTextLoginField(this, "username");
            username.required();

            password = ComponentUtil.addTextPasswordField(this, "password");
            password.required();
            password.getField().setResetPassword(false);

            final IndicatingAjaxButton submit = new IndicatingAjaxButton("submit",
                    new StringResourceModel("submit.label", LoginPage.this, null)) {
                private static final long serialVersionUID = 1L;

                @Override
                protected void onSubmit(final AjaxRequestTarget target) {
                    super.onSubmit(target);

                    final SSAuthenticatedWebSession session = SSAuthenticatedWebSession.getSSAuthenticatedWebSession();
                    final String name = LoginForm.this.getModelObject().getUsername();
                    final String pass = LoginForm.this.getModelObject().getPassword();

                    if (session.signIn(name, pass)) {
                        Person user = SecurityUtil.getCurrentAuthenticatedPerson();
                        if (BooleanUtils.isTrue(user.getChangePasswordNextSignIn())) {
                            final PageParameters pageParam = new PageParameters();
                            pageParam.add(WebConstants.PARAM_ID, user.getId());
                            setResponsePage(ChangePasswordPage.class, pageParam);
                        } else {
                            if (referrer != null) {
                                throw new RedirectToUrlException(referrer);
                            }
                            setResponsePage(getApplication().getHomePage());
                        }
                    } else if (session.getAe().getMessage().equalsIgnoreCase("User is disabled")) {
                        notificationPanel.error(session.getAe().getMessage());
                        target.add(notificationPanel);
                    } else {
                        notificationPanel.error(getString("bad_credentials"));
                        target.add(notificationPanel);
                    }
                }

                @Override
                protected void onError(final AjaxRequestTarget target) {
                    super.onError(target);

                    target.add(notificationPanel);
                    target.add(username);
                    target.add(password);
                }
            };
            add(submit);

            final IndicatingAjaxButton forgotPassword = new IndicatingAjaxButton("forgotPassword",
                    new StringResourceModel("forgotPassword", LoginPage.this, null)) {
                private static final long serialVersionUID = 1L;

                @Override
                protected void onConfigure() {
                    super.onConfigure();
                    setDefaultFormProcessing(false);
                }

                @Override
                protected void onSubmit(final AjaxRequestTarget target) {
                    super.onSubmit(target);

                    setResponsePage(ForgotYourPasswordPage.class);
                }
            };
            add(forgotPassword);
        }

        private void retrieveReferrerFromSavedRequestIfPresent() {
            final StringValue referrerParam = RequestCycle.get().getRequest().getRequestParameters()
                    .getParameterValue("referrer");
            if (!referrerParam.isEmpty()) {
                referrer = referrerParam.toString();
            } else {

                HttpServletRequest request = ((HttpServletRequest) getRequest().getContainerRequest());
                SavedRequest savedRequest = (SavedRequest) request.getSession()
                        .getAttribute("SPRING_SECURITY_SAVED_REQUEST");
                if (savedRequest != null) {
                    referrer = savedRequest.getRedirectUrl();
                }
            }
        }
    }
}
