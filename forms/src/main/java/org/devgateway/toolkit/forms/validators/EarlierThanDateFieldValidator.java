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
package org.devgateway.toolkit.forms.validators;

import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.ValidationError;
import org.devgateway.toolkit.forms.wicket.components.form.DateFieldBootstrapFormComponent;

import java.util.Date;

/**
 * @author mpostelnicu {@link DateFieldBootstrapFormComponent} validator for
 *         dates that have a chronology
 */
public class EarlierThanDateFieldValidator implements IValidator<Date> {

    private static final long serialVersionUID = 1L;

    private final DateFieldBootstrapFormComponent highDate;

    /**
     * Provide a {@link DateFieldBootstrapFormComponent} that has to be
     * chronologically after the current's
     * {@link DateFieldBootstrapFormComponent} validator
     */
    public EarlierThanDateFieldValidator(final DateFieldBootstrapFormComponent highDate) {
        this.highDate = highDate;
    }

    @Override
    public void validate(final IValidatable<Date> validatable) {
        highDate.getField().validate();
        if (!highDate.getField().isValid()) {
            return;
        }

        final Date endDate = highDate.getField().getConvertedInput();

        if (endDate != null && validatable.getValue() != null && endDate.before(validatable.getValue())) {
            final ValidationError error = new ValidationError(this);
            error.setVariable("highDateName", highDate.getLabelModel().getObject());
            validatable.error(error);
        }
    }

}
