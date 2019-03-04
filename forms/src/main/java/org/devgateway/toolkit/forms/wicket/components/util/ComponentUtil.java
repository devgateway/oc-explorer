package org.devgateway.toolkit.forms.wicket.components.util;

import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.event.IEvent;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.validator.EmailAddressValidator;
import org.devgateway.toolkit.forms.WebConstants;
import org.devgateway.toolkit.forms.wicket.components.form.CheckBoxBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.form.CheckBoxToggleBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.form.CheckBoxYesNoToggleBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.form.DateFieldBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.form.DateTimeFieldBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.form.GenericBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.form.Select2ChoiceBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.form.Select2MultiChoiceBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.form.TextAreaFieldBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.form.TextFieldBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.events.EditingDisabledEvent;
import org.devgateway.toolkit.forms.wicket.events.EditingEnabledEvent;
import org.devgateway.toolkit.forms.wicket.providers.GenericPersistableJpaTextChoiceProvider;
import org.devgateway.toolkit.persistence.dao.GenericPersistable;
import org.devgateway.toolkit.persistence.dao.Labelable;
import org.devgateway.toolkit.persistence.service.TextSearchableService;

import java.io.Serializable;

/**
 * @author idobre
 * @since 2019-03-04
 */
public final class ComponentUtil {
    private ComponentUtil() {

    }

    /**
     * Returns true if the {@link WebConstants#PARAM_VIEW_MODE} is used as a
     * parameter
     *
     * @return
     */
    public static boolean isViewMode() {
        return RequestCycle.get().getRequest().getRequestParameters().getParameterValue(WebConstants.PARAM_VIEW_MODE)
                .toBoolean(false);
    }

    public static void enableDisableEvent(final Component c, final IEvent<?> event) {
        if (event.getPayload() instanceof EditingDisabledEvent) {
            c.setEnabled(false);
        }

        if (event.getPayload() instanceof EditingEnabledEvent) {
            c.setEnabled(true);
        }

    }

    public static IValidator<? super String> isEmail() {
        return EmailAddressValidator.getInstance();
    }

    public static CheckBoxBootstrapFormComponent addCheckBox(
            final WebMarkupContainer parent,
            final String id,
            final boolean isFloatedInput) {
        final CheckBoxBootstrapFormComponent checkBox = new CheckBoxBootstrapFormComponent(id);
        checkBox.setIsFloatedInput(isFloatedInput);
        parent.add(checkBox);

        return checkBox;
    }

    public static CheckBoxToggleBootstrapFormComponent addCheckToggle(
            final WebMarkupContainer parent,
            final String id,
            final boolean isFloatedInput) {
        final CheckBoxToggleBootstrapFormComponent checkToggle = new CheckBoxToggleBootstrapFormComponent(id);
        checkToggle.setIsFloatedInput(isFloatedInput);
        parent.add(checkToggle);

        return checkToggle;
    }

    public static CheckBoxYesNoToggleBootstrapFormComponent addYesNoToggle(
            final WebMarkupContainer parent,
            final String id,
            final boolean isFloatedInput) {
        final CheckBoxYesNoToggleBootstrapFormComponent checkToggle = new CheckBoxYesNoToggleBootstrapFormComponent(id);
        checkToggle.setIsFloatedInput(isFloatedInput);
        parent.add(checkToggle);

        return checkToggle;
    }

    public static TextAreaFieldBootstrapFormComponent<String> addTextAreaField(
            final WebMarkupContainer parent,
            final String id) {
        final TextAreaFieldBootstrapFormComponent<String> textAreaField = new TextAreaFieldBootstrapFormComponent<>(id);
        parent.add(textAreaField);

        return textAreaField;
    }

    public static TextFieldBootstrapFormComponent<String> addTextField(
            final WebMarkupContainer parent,
            final String id,
            final boolean isFloatedInput) {
        final TextFieldBootstrapFormComponent<String> textField = new TextFieldBootstrapFormComponent<>(id);
        textField.setIsFloatedInput(isFloatedInput);
        parent.add(textField);

        return textField;
    }

    public static TextFieldBootstrapFormComponent<String> addTextLoginField(
            final WebMarkupContainer parent,
            final String id,
            final boolean isFloatedInput) {
        final TextFieldBootstrapFormComponent<String> textField = new TextFieldBootstrapFormComponent<String>(id) {
            @Override
            public String getUpdateEvent() {
                return null;
            }
        };
        textField.setIsFloatedInput(isFloatedInput);
        parent.add(textField);

        return textField;
    }

    public static TextFieldBootstrapFormComponent<Integer> addIntegerTextField(
            final WebMarkupContainer parent,
            final String id,
            final boolean isFloatedInput) {
        final TextFieldBootstrapFormComponent<Integer> textField = new TextFieldBootstrapFormComponent<>(id);
        textField.setIsFloatedInput(isFloatedInput);
        textField.integer();
        parent.add(textField);

        return textField;
    }

    public static TextFieldBootstrapFormComponent<String> addDoubleField(
            final WebMarkupContainer parent,
            final String id,
            final boolean isFloatedInput) {
        final TextFieldBootstrapFormComponent<String> textField = new TextFieldBootstrapFormComponent<>(id);
        textField.setIsFloatedInput(isFloatedInput);
        textField.asDouble();
        parent.add(textField);

        return textField;
    }

    public static DateTimeFieldBootstrapFormComponent addDateTimeField(
            final WebMarkupContainer parent,
            final String id,
            final boolean isFloatedInput) {
        final DateTimeFieldBootstrapFormComponent field = new DateTimeFieldBootstrapFormComponent(id);
        field.setIsFloatedInput(isFloatedInput);
        parent.add(field);

        return field;
    }

    public static DateFieldBootstrapFormComponent addDateField(
            final WebMarkupContainer parent,
            final String id,
            final boolean isFloatedInput) {
        final DateFieldBootstrapFormComponent field = new DateFieldBootstrapFormComponent(id);
        field.setIsFloatedInput(isFloatedInput);
        parent.add(field);

        return field;
    }

    public static <E extends GenericPersistable & Labelable & Serializable> Select2ChoiceBootstrapFormComponent<E>
    addSelect2ChoiceField(
            final WebMarkupContainer parent,
            final String id,
            final TextSearchableService<E> searchService,
            final boolean isFloatedInput) {
        final GenericPersistableJpaTextChoiceProvider<E> choiceProvider
                = new GenericPersistableJpaTextChoiceProvider<>(searchService);
        final Select2ChoiceBootstrapFormComponent<E> component = new Select2ChoiceBootstrapFormComponent<>(id,
                choiceProvider);
        component.setIsFloatedInput(isFloatedInput);
        parent.add(component);

        return component;
    }

    public static <E extends GenericPersistable & Labelable & Serializable> Select2MultiChoiceBootstrapFormComponent<E>
    addSelect2MultiChoiceField(
            final WebMarkupContainer parent,
            final String id,
            final TextSearchableService<E> searchService,
            final boolean isFloatedInput) {
        final GenericPersistableJpaTextChoiceProvider<E> choiceProvider =
                new GenericPersistableJpaTextChoiceProvider<>(searchService);
        final Select2MultiChoiceBootstrapFormComponent<E> component =
                new Select2MultiChoiceBootstrapFormComponent<>(id, choiceProvider);
        component.setIsFloatedInput(isFloatedInput);
        parent.add(component);

        return component;
    }

    /**
     * Trivial method to set the child {@link GenericBootstrapFormComponent}
     * required when added to the parent {@link WebMarkupContainer}
     *
     * @param requiredFlag
     *            the {@link FormComponent#setRequired(boolean)}
     * @param parent
     * @param child
     *
     * @return the parent
     */
    public static MarkupContainer addRequiredFlagBootstrapFormComponent(
            final boolean requiredFlag,
            final WebMarkupContainer parent,
            final GenericBootstrapFormComponent<?, ?> child) {
        return parent.add(requiredFlag ? child.required() : child);
    }
}
