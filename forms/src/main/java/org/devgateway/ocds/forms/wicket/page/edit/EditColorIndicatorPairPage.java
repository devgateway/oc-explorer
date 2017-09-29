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
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.validation.AbstractFormValidator;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.ocds.forms.wicket.page.list.ListAllColorIndicatorPage;
import org.devgateway.ocds.persistence.dao.ColorIndicatorPair;
import org.devgateway.ocds.persistence.mongo.flags.FlagsConstants;
import org.devgateway.ocds.persistence.repository.ColorIndicatorPairRepository;
import org.devgateway.toolkit.forms.wicket.components.form.ColorPickerBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.form.Select2ChoiceBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.page.edit.AbstractEditPage;
import org.devgateway.toolkit.forms.wicket.providers.GenericChoiceProvider;
import org.devgateway.toolkit.persistence.repository.PersonRepository;
import org.devgateway.toolkit.web.security.SecurityConstants;
import org.wicketstuff.annotation.mount.MountPath;

@AuthorizeInstantiation(SecurityConstants.Roles.ROLE_ADMIN)
@MountPath("/editColorIndicatorPairPage")
public class EditColorIndicatorPairPage extends AbstractEditPage<ColorIndicatorPair> {

    private static final long serialVersionUID = -6069250112046118104L;

    @Override
    protected ColorIndicatorPair newInstance() {
        return new ColorIndicatorPair();
    }

    @SpringBean
    private ColorIndicatorPairRepository colorIndicatorPairRepository;

    @SpringBean
    private PersonRepository personRepository;

    private Select2ChoiceBootstrapFormComponent<String> firstIndicator;

    private Select2ChoiceBootstrapFormComponent<String> secondIndicator;

    public EditColorIndicatorPairPage(final PageParameters parameters) {
        super(parameters);
        this.jpaRepository = colorIndicatorPairRepository;
        this.listPageClass = ListAllColorIndicatorPage.class;

    }


    @Override
    protected void onInitialize() {
        super.onInitialize();

        firstIndicator =
                new Select2ChoiceBootstrapFormComponent<String>("firstIndicator",
                        new GenericChoiceProvider<String>(FlagsConstants.FLAGS_LIST));
        firstIndicator.required();
        editForm.add(firstIndicator);

        secondIndicator =
                new Select2ChoiceBootstrapFormComponent<String>("secondIndicator",
                        new GenericChoiceProvider<String>(FlagsConstants.FLAGS_LIST));
        secondIndicator.required();
        editForm.add(secondIndicator);

        ColorPickerBootstrapFormComponent color = new ColorPickerBootstrapFormComponent("color");
        color.required();
        editForm.add(color);
        editForm.add(new ColorIndicatorDistinctFormValidator());
        editForm.add(new ColorIndicatorUniquePairFormValidator(compoundModel));

    }


    private class ColorIndicatorDistinctFormValidator extends AbstractFormValidator {

        @Override
        public FormComponent<?>[] getDependentFormComponents() {
            return new FormComponent[]{firstIndicator.getField(), secondIndicator.getField()};
        }

        @Override
        public void validate(Form<?> form) {
            if (firstIndicator.getField().getValue() != null && secondIndicator.getField().getValue() != null
                    && firstIndicator.getField().getValue().equals(secondIndicator.getField().getValue())) {
                error(firstIndicator.getField());
                error(secondIndicator.getField());
            }
        }
    }


    private class ColorIndicatorUniquePairFormValidator extends AbstractFormValidator {

        private final IModel<ColorIndicatorPair> masterModel;

        ColorIndicatorUniquePairFormValidator(IModel<ColorIndicatorPair> masterModel) {
            this.masterModel = masterModel;
        }

        @Override
        public FormComponent<?>[] getDependentFormComponents() {
            return new FormComponent[]{firstIndicator.getField(), secondIndicator.getField()};
        }

        @Override
        public void validate(Form<?> form) {
            if (firstIndicator.getField().getValue() != null && secondIndicator.getField().getValue() != null) {
                ColorIndicatorPair indicator = colorIndicatorPairRepository.
                        findByFirstIndicatorAndSecondIndicator(firstIndicator.getField().getValue(),
                                secondIndicator.getField().getValue());

                if ((masterModel.getObject().isNew() && indicator != null)
                        || (!masterModel.getObject().isNew() && indicator != null
                        && !indicator.getId().equals(masterModel.getObject().getId()))) {
                    error(firstIndicator.getField());
                    error(secondIndicator.getField());
                }
            }
        }
    }
}
