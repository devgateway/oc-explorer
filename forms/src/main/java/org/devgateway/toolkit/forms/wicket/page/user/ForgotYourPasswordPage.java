package org.devgateway.toolkit.forms.wicket.page.user;

import de.agilecoders.wicket.core.markup.html.bootstrap.form.BootstrapForm;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxButton;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.toolkit.forms.service.SendEmailService;
import org.devgateway.toolkit.forms.wicket.components.form.TextFieldBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.util.ComponentUtil;
import org.devgateway.toolkit.forms.wicket.page.BasePage;
import org.devgateway.toolkit.persistence.dao.Person;
import org.devgateway.toolkit.persistence.service.PersonServiceImpl;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.wicketstuff.annotation.mount.MountPath;

import java.io.Serializable;

@MountPath(value = "/forgotPassword")
public class ForgotYourPasswordPage extends BasePage {
    private static final long serialVersionUID = -6767090562116351915L;

    public static final int RANDOM_PASSWORD_LENGTH = 16;

    @SpringBean
    private PersonServiceImpl personService;

    @SpringBean
    private SendEmailService sendEmailService;

    @SpringBean
    private PasswordEncoder passwordEncoder;

    private final ForgotPasswordBean forgotPasswordBean;

    public ForgotYourPasswordPage(final PageParameters parameters) {
        super(parameters);

        forgotPasswordBean = new ForgotPasswordBean();
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        final ForgotPasswordForm form = new ForgotPasswordForm("form", new CompoundPropertyModel<>(forgotPasswordBean));
        add(form);
    }

    class ForgotPasswordBean implements Serializable {
        private String emailAddress;

        public String getEmailAddress() {
            return emailAddress;
        }

        public void setEmailAddress(final String emailAddress) {
            this.emailAddress = emailAddress;
        }
    }

    class ForgotPasswordForm extends BootstrapForm<ForgotPasswordBean> {
        private static final long serialVersionUID = 7708855731894924277L;

        private TextFieldBootstrapFormComponent<String> emailAddress;

        private IndicatingAjaxButton goBack;

        ForgotPasswordForm(final String componentId, final IModel<ForgotPasswordBean> model) {
            super(componentId, model);
        }

        @Override
        protected void onInitialize() {
            super.onInitialize();

            emailAddress = ComponentUtil.addTextField(this, "emailAddress", false);
            emailAddress.getField().add(ComponentUtil.isEmail());
            emailAddress.required();

            final Label message = new Label("message",
                    new StringResourceModel("checkMessage", ForgotYourPasswordPage.this, null));
            message.setVisibilityAllowed(false);
            message.setOutputMarkupId(true);
            add(message);

            final IndicatingAjaxButton submit = new IndicatingAjaxButton("submit",
                    new StringResourceModel("submit.label", ForgotYourPasswordPage.this, null)) {
                private static final long serialVersionUID = 1L;

                @Override
                protected void onSubmit(final AjaxRequestTarget target) {
                    super.onSubmit(target);

                    final String email = ForgotPasswordForm.this.getModelObject().getEmailAddress();
                    final Person person = personService.findByEmail(email);

                    if (person == null) {
                        feedbackPanel.error("Email address not found");
                    } else {
                        final String newPassword = RandomStringUtils.random(RANDOM_PASSWORD_LENGTH, true, true);
                        person.setPassword(passwordEncoder.encode(newPassword));
                        person.setChangePasswordNextSignIn(true);

                        personService.saveAndFlush(person);
                        sendEmailService.sendEmailResetPassword(person, newPassword);

                        emailAddress.setVisibilityAllowed(false);
                        this.setVisibilityAllowed(false);

                        message.setVisibilityAllowed(true);
                        goBack.setVisibilityAllowed(true);

                        target.add(ForgotPasswordForm.this);
                    }

                    target.add(feedbackPanel);
                }

                @Override
                protected void onError(final AjaxRequestTarget target) {
                    super.onError(target);

                    target.add(feedbackPanel);
                }
            };
            add(submit);

            goBack = new IndicatingAjaxButton("goBack",
                    new StringResourceModel("back", ForgotYourPasswordPage.this, null)) {
                private static final long serialVersionUID = 1L;

                @Override
                protected void onSubmit(final AjaxRequestTarget target) {
                    super.onSubmit(target);

                    setResponsePage(LoginPage.class);
                }
            };
            goBack.setVisibilityAllowed(false);
            goBack.setOutputMarkupId(true);
            add(goBack);
        }
    }
}
