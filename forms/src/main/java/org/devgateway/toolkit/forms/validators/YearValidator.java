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

import java.util.Calendar;

import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.ValidationError;

/**
 * @author mpostelnicu
 *
 */
public class YearValidator implements IValidator<Integer> {

	private static final long serialVersionUID = 1L;
	private boolean maxCurrentYear = false;

	public YearValidator maxCurrentYear() {
		this.maxCurrentYear = true;
		return this;
	}

	@Override
	public void validate(IValidatable<Integer> validatable) {
		if (validatable.getValue() == null)
			return;
//this is redundant to the >2000 check..
//		if (validatable.getValue().toString().length() > 4) {
//			ValidationError error = new ValidationError();
//			error.addKey(this,"fourdigits");
//			validatable.error(error);
//		}

		if (validatable.getValue() < 2000) {
			ValidationError error = new ValidationError();
			error.addKey(this,"after2000");
			validatable.error(error);
		}

		if (maxCurrentYear && validatable.getValue() > Calendar.getInstance().get(Calendar.YEAR)) {
			ValidationError error = new ValidationError();
			error.addKey(this,"maxCurrentYear");
			validatable.error(error);
		}
	}

}
