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
package org.devgateway.toolkit.forms.wicket.components.form;

import de.agilecoders.wicket.extensions.markup.html.bootstrap.editor.SummernoteConfig;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.editor.SummernoteEditor;
import org.apache.wicket.event.IEvent;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptContentHeaderItem;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.model.IModel;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.ValidationError;
import org.apache.wicket.validation.validator.StringValidator;
import org.devgateway.toolkit.forms.WebConstants;
import org.devgateway.toolkit.forms.wicket.FormsWebApplication;
import org.devgateway.toolkit.forms.wicket.components.ComponentUtil;

/**
 * @author mpostelnicu
 */
public class SummernoteBootstrapFormComponent extends GenericBootstrapFormComponent<String, FormComponent<String>> {
    private static final int SUMMERNOTE_HEIGHT = 50;
    public static final String SUMMERNOTE_EMPTY_HTML = "<p><br></p>";

    private SummernoteEditor summernoteEditor;

    private StringValidator validator = WebConstants.StringValidators.MAXIMUM_LENGTH_VALIDATOR_ONE_LINE_TEXTAREA;

    private SummernoteConfig config;

    public class SummernoteEmptyValidator implements IValidator<String> {

        @Override
        public void validate(final IValidatable<String> validatable) {
            if (validatable.getValue().trim().equals(SUMMERNOTE_EMPTY_HTML)) {
                ValidationError error = new ValidationError(this);
                error.addKey("Required");
                summernoteEditor.setModelObject(SUMMERNOTE_EMPTY_HTML);
                validatable.error(error);
            }
        }
    }

    /**
     *
     */
    private static final long serialVersionUID = -7822733988194369835L;

    public SummernoteBootstrapFormComponent(final String id, final IModel<String> labelModel,
                                            final IModel<String> model) {
        super(id, labelModel, model);
    }

    public SummernoteBootstrapFormComponent(final String id, final IModel<String> labelModel) {
        super(id, labelModel, null);
    }

    /**
     * @param id
     */
    public SummernoteBootstrapFormComponent(final String id) {
        super(id);
    }

//    public String getUpdateEvent() {
//        return "summernote.blur";
//    }

    @Override
    protected FormComponent<String> inputField(final String id, final IModel<String> model) {

        config = new SummernoteConfig();

        // this enabled for demo purposes, but it stores the files in volatile
        // disk dir
        config.useStorageId(FormsWebApplication.STORAGE_ID);

        config.withHeight(SUMMERNOTE_HEIGHT);
        config.withAirMode(false);

        if (isEnabledInHierarchy()) {
            summernoteEditor = new SummernoteEditor(id, initFieldModel(), config);
            return summernoteEditor;
        } else {
            return  new TextArea<String>(id, initFieldModel());
        }

    }

    @Override
    protected void onInitialize() {
        if (!isEnabledInHierarchy()) {
            initializeField();
        }
        super.onInitialize();
        getField().add(validator);
    }

    @Override
    public GenericBootstrapFormComponent<String, FormComponent<String>> required() {
        super.required();
        getField().add(new SummernoteEmptyValidator());
        return this;
    }

    public SummernoteConfig getConfig() {
        return config;
    }

    @Override
    protected FormComponent<String> updatingBehaviorComponent() {
        return summernoteEditor;
    }

    @Override
    protected void onBeforeRender() {
        super.onBeforeRender();
    }


    @Override
    public void onEvent(final IEvent<?> event) {
        ComponentUtil.enableDisableEvent(this, event);
        if (!isEnabledInHierarchy()) {
            initializeField();
        }
    }

    @Override
    public void renderHead(final IHeaderResponse response) {
        super.renderHead(response);

        //see https://github.com/summernote/summernote/issues/2017
        String summernoteChildNodesFix = "if(!!document.createRange) document.getSelection().removeAllRanges();";
        response.render(JavaScriptContentHeaderItem.forScript(summernoteChildNodesFix, "summernote-childNodes-fix"));
        response.render(OnDomReadyHeaderItem.forScript(summernoteChildNodesFix));
    }
}
