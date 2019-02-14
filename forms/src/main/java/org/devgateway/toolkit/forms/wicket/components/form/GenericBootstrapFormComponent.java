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
package org.devgateway.toolkit.forms.wicket.components.form;

import de.agilecoders.wicket.core.markup.html.bootstrap.components.TooltipConfig;
import de.agilecoders.wicket.core.markup.html.bootstrap.form.FormGroup;
import de.agilecoders.wicket.core.markup.html.bootstrap.form.InputBehavior;
import de.agilecoders.wicket.core.markup.html.bootstrap.form.InputBehavior.Size;
import de.agilecoders.wicket.core.util.Attributes;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.ajax.attributes.ThrottlingSettings;
import org.apache.wicket.ajax.form.AjaxFormChoiceComponentUpdatingBehavior;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.event.IEvent;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.TransparentWebMarkupContainer;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckGroup;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.RadioGroup;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.util.time.Duration;
import org.devgateway.toolkit.forms.models.SubComponentWrapModel;
import org.devgateway.toolkit.forms.models.ViewModeConverterModel;
import org.devgateway.toolkit.forms.wicket.components.ComponentUtil;
import org.devgateway.toolkit.forms.wicket.components.FieldPanel;
import org.devgateway.toolkit.forms.wicket.components.TooltipLabel;
import org.devgateway.toolkit.forms.wicket.page.edit.AbstractEditPage;
import org.devgateway.toolkit.persistence.dao.GenericPersistable;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.query.AuditEntity;
import org.hibernate.envers.query.AuditQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

/**
 * @author mpostelnicu
 */
public abstract class GenericBootstrapFormComponent<TYPE, FIELD extends FormComponent<TYPE>> extends FieldPanel<TYPE> {
    private static final long serialVersionUID = -7051128382707812456L;

    private static final Logger logger = LoggerFactory.getLogger(GenericBootstrapFormComponent.class);

    protected FormGroup border;

    protected FIELD field;

    protected Label viewModeField;

    protected InputBehavior sizeBehavior;

    private TooltipConfig.OpenTrigger configWithTrigger = TooltipConfig.OpenTrigger.hover;

    protected TooltipLabel tooltipLabel;

    protected IModel<String> labelModel;

    protected Class<?> auditorClass;

    protected WebMarkupContainer revisions;

    protected TransparentWebMarkupContainer masterGroup;

    protected TransparentWebMarkupContainer childGroup;


    protected IModel<EntityManager> entityManagerModel;
    protected String auditProperty;

    protected IModel<? extends GenericPersistable> revisionOwningEntityModel;

    //prevents repainting of select boxes and other problems with triggering the update even while the component js
    //is not done updating.
    private static final int THROTTLE_UPDATE_DELAY_MS = 200;

    @Override
    public void onEvent(final IEvent<?> event) {
        ComponentUtil.enableDisableEvent(this, event);
    }

    @SuppressWarnings("unchecked")
    protected IModel<TYPE> initFieldModel() {
        if (getDefaultModel() == null) {
            return new SubComponentWrapModel<>(this);
        }
        return (IModel<TYPE>) getDefaultModel();
    }

    protected void enableRevisionsView(final AbstractEditPage<?> page) {
        enableRevisionsView(page.getNewInstanceClass(), page.getEntityManager(), page.getEditForm().getModel());
    }

    public void enableRevisionsView() {
        AbstractEditPage<?> parentPage = (AbstractEditPage<?>) getPage();
        enableRevisionsView(
                parentPage.getNewInstanceClass(), parentPage.getEntityManager(), parentPage.getEditForm().getModel());
    }

    public void enableRevisionsView(final Class<?> auditorClass,
                                    final EntityManager entityManager,
                                    final IModel<? extends GenericPersistable> owningEntityModel) {
        this.auditorClass = auditorClass;
        this.entityManagerModel = new LoadableDetachableModel<EntityManager>() {
            @Override
            protected EntityManager load() {
                return entityManager;
            }
        };
        this.revisionOwningEntityModel = owningEntityModel;
        addOrReplace(getRevisionsPanel());
    }

    /**
     * True if the control can print contents unescaped when in readonly mode
     * @return
     */
    protected boolean printUnescaped() {
        return false;
    }

    protected RevisionsPanel<TYPE> getRevisionsPanel() {
        return new RevisionsPanel<>("revisions", getRevisionsModel(), auditProperty);
    }

    /**
     * Encloses the component and revisions section with a boostrap panel
     *
     * @return
     */
    public GenericBootstrapFormComponent<TYPE, FIELD> encloseWithBorder() {
        masterGroup.add(AttributeModifier.append("class", "panel panel-default"));
        childGroup.add(AttributeModifier.append("class", "panel-body"));
        return this;
    }

    protected IModel<List<TYPE>> getRevisionsModel() {
        return (IModel<List<TYPE>>) () -> {
            if (revisionOwningEntityModel.getObject().isNew()) {
                return new ArrayList<>();
            }
            AuditReader reader = AuditReaderFactory.get(entityManagerModel.getObject());
            AuditQuery query = reader.createQuery().forRevisionsOfEntity(auditorClass, false, false);
            query.add(AuditEntity.property("id").eq(revisionOwningEntityModel.getObject().getId()));
            query.add(AuditEntity.property(auditProperty).hasChanged());
            return query.getResultList();
        };
    }

    /**
     * use this behavior for choices/groups that are not one component in the
     * html but many.
     */
    protected void getAjaxFormChoiceComponentUpdatingBehavior() {
        updatingBehaviorComponent().add(new AjaxFormChoiceComponentUpdatingBehavior() {
            private static final long serialVersionUID = 1L;

            @Override
            protected void updateAjaxAttributes(final AjaxRequestAttributes attributes) {
                attributes.setThrottlingSettings(new ThrottlingSettings(
                        Duration.milliseconds(THROTTLE_UPDATE_DELAY_MS)));
                super.updateAjaxAttributes(attributes);
            }

            @Override
            protected void onUpdate(final AjaxRequestTarget target) {
                GenericBootstrapFormComponent.this.onUpdate(target);
            }
        });
    }

    /**
     * This is the component that has to be updated with the
     * {@link #getAjaxFormChoiceComponentUpdatingBehavior()} or with
     * {@link #getAjaxFormComponentUpdatingBehavior()}. It usuall is the field,
     * but the field may be a wrapper, in which case you should override this
     * and provide the wrapped field.
     *
     * @return
     */
    protected FormComponent<TYPE> updatingBehaviorComponent() {
        return field;
    }

    protected void getAjaxFormComponentUpdatingBehavior() {
        if (getUpdateEvent() == null) {
            return;
        }
        updatingBehaviorComponent().add(new AjaxFormComponentUpdatingBehavior(getUpdateEvent()) {

            private static final long serialVersionUID = -2696538086634114609L;

            @Override
            protected void updateAjaxAttributes(final AjaxRequestAttributes attributes) {
                attributes.setThrottlingSettings(new ThrottlingSettings(
                        Duration.milliseconds(THROTTLE_UPDATE_DELAY_MS)));
                super.updateAjaxAttributes(attributes);
            }

            @Override
            protected void onUpdate(final AjaxRequestTarget target) {
                target.add(border);
                GenericBootstrapFormComponent.this.onUpdate(target);
            }

            @Override
            protected void onError(final AjaxRequestTarget target, final RuntimeException e) {
                target.add(border);
            }
        });
    }

    public String getUpdateEvent() {
        return "blur";
    }

    public GenericBootstrapFormComponent<TYPE, FIELD> type(final Class<?> clazz) {
        field.setType(clazz);
        return this;
    }

    public GenericBootstrapFormComponent<TYPE, FIELD> size(final Size size) {
        sizeBehavior.size(size);
        return this;
    }

    public GenericBootstrapFormComponent(final String id) {
        this(id, null);
    }

    public IModel<String> getLabelModel() {
        return labelModel;
    }

    public GenericBootstrapFormComponent(final String id, final IModel<TYPE> model) {
        this(id, new ResourceModel(id + ".label"), model);
    }

    public GenericBootstrapFormComponent(final String id, final IModel<String> labelModel, final IModel<TYPE> model) {
        super(id, model);
        this.labelModel = labelModel;
        setOutputMarkupId(true);
        setOutputMarkupPlaceholderTag(true);

        border = new FormGroup("enclosing-field-group");
        border.setOutputMarkupId(true);
        add(border);

        initializeField();

        tooltipLabel = new TooltipLabel("tooltipLabel", id);
        border.add(tooltipLabel);
        auditProperty = this.getId();
    }

    protected InputBehavior getInputBehavior() {
        return new InputBehavior(InputBehavior.Size.Medium);
    }

    protected void initializeField() {
        field = inputField("field", getModel());
        field.setVisibilityAllowed(!isViewMode());
        field.setOutputMarkupId(true);
        sizeBehavior = getInputBehavior();
        if (sizeBehavior != null) {
            field.add(sizeBehavior);
        }

        border.addOrReplace(field);
        field.setLabel(labelModel);
    }

    @Override
    protected void onComponentTag(final ComponentTag tag) {
        super.onComponentTag(tag);

        // add a new class for required fields
        if (field.isRequired()) {
            Attributes.addClass(tag, "required");
        }
    }

    public GenericBootstrapFormComponent<TYPE, FIELD> hideLabel() {
        field.setLabel(null);
        return this;
    }

    protected abstract FIELD inputField(String id, IModel<TYPE> model);

    public GenericBootstrapFormComponent<TYPE, FIELD> required() {
        field.setRequired(true);
        return this;
    }

    protected void onUpdate(final AjaxRequestTarget target) {
    }

    public FIELD getField() {
        return field;
    }

    public boolean isViewMode() {
        return ComponentUtil.isViewMode();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.apache.wicket.Component#onConfigure()
     */
    @Override
    protected void onInitialize() {
        super.onInitialize();

        if ((field instanceof RadioGroup) || (field instanceof CheckGroup)) {
            getAjaxFormChoiceComponentUpdatingBehavior();
        } else {
            getAjaxFormComponentUpdatingBehavior();
        }

        viewModeField = new Label("viewModeField", new ViewModeConverterModel<TYPE>(getModel()));
        viewModeField.setEscapeModelStrings(false);
        viewModeField.setVisibilityAllowed(isViewMode());
        border.add(viewModeField);

        tooltipLabel.setConfigWithTrigger(configWithTrigger);

        masterGroup = new TransparentWebMarkupContainer("masterGroup");
        add(masterGroup);
        childGroup = new TransparentWebMarkupContainer("childGroup");
        add(childGroup);

        revisions = new WebMarkupContainer("revisions"); // this is just a placeholder
        add(revisions);

    }

    /**
     * @return the border
     */
    public FormGroup getBorder() {
        return border;
    }

    public TooltipConfig.OpenTrigger getConfigWithTrigger() {
        return configWithTrigger;
    }

    public void setConfigWithTrigger(final TooltipConfig.OpenTrigger configWithTrigger) {
        this.configWithTrigger = configWithTrigger;
    }

    public String getAuditProperty() {
        return auditProperty;
    }

    public void setAuditProperty(final String auditProperty) {
        this.auditProperty = auditProperty;
    }
}
