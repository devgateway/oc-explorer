package org.devgateway.toolkit.forms.wicket.page.user;

import de.agilecoders.wicket.core.markup.html.bootstrap.form.BootstrapForm;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxButton;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.string.StringValue;
import org.devgateway.toolkit.forms.wicket.page.BasePage;
import org.devgateway.toolkit.persistence.dao.Person;
import org.devgateway.toolkit.persistence.repository.PersonRepository;
import org.wicketstuff.annotation.mount.MountPath;

@MountPath(value = "/changedPasswordValidate")
public class ChangedPasswordValidatePage extends BasePage {
    private static final long serialVersionUID = 109572901645014684L;

    public static final String PARAM_CHECK = "check";

    @SpringBean
    private PersonRepository personRepository;

    ChangedPasswordValidatePage(final PageParameters parameters) {
        super(parameters);

        ValidatePassForm validatePassForm = new ValidatePassForm("validatePassForm");
        add(validatePassForm);
    }

    class ValidatePassForm extends BootstrapForm<Void> {
        private static final long serialVersionUID = 6478383928100394429L;

        ValidatePassForm(final String componentId) {
            super(componentId);
        }

        @Override
        protected void onInitialize() {
            super.onInitialize();

            if (!getPageParameters().get(ChangedPasswordValidatePage.PARAM_CHECK).isEmpty()) {
                StringValue secret = getPageParameters().get(ChangedPasswordValidatePage.PARAM_CHECK);

                Person person = personRepository.findBySecret(secret.toString());
                if (person != null && person.getSecret() != null
                        && person.getSecret().compareTo(secret.toString()) == 0) {
                    person.setEnabled(true);
                    person.setChangePassword(false);
                    personRepository.saveAndFlush(person);

                    add(new Label("message", new ResourceModel("confirmMessage")));
                } else {
                    add(new Label("message", new ResourceModel("errorMessage")));
                }
            } else {
                add(new Label("message", new ResourceModel("errorMessage")));
            }

            IndicatingAjaxButton submit = new IndicatingAjaxButton(
                    "submit", new StringResourceModel("ok", ChangedPasswordValidatePage.this, null)) {
                private static final long serialVersionUID = 1L;

                @Override
                protected void onSubmit(final AjaxRequestTarget target, final Form<?> form) {
                    setResponsePage(LoginPage.class);
                }
            };
            add(submit);
        }
    }
}
