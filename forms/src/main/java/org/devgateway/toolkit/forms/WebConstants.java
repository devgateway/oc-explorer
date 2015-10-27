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
package org.devgateway.toolkit.forms;

import org.apache.wicket.validation.validator.StringValidator;
import org.devgateway.toolkit.persistence.dao.DBConstants;

public final class WebConstants {
	public static final int PAGE_SIZE = 10;
	public static final int SELECT_PAGE_SIZE = 25;

	public static final String PARAM_VIEW_MODE = "viewMode";

	public static final String PARAM_ID = "id";
	public static final String PARAM_REVISION_ID = "revisionId";
	public static final String PARAM_ENTITY_CLASS = "class";

	public static final class StringValidators {
		public static final StringValidator maximumLengthValidatorOneLineText = StringValidator
				.maximumLength(DBConstants.MAX_DEFAULT_TEXT_LENGTH_ONE_LINE);
		public static final StringValidator maximumLengthValidatorTextArea = StringValidator
				.maximumLength(DBConstants.MAX_DEFAULT_TEXT_AREA);
	}
}
