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
package org.devgateway.toolkit.forms.wicket.providers;

import java.util.List;

import org.json.JSONException;
import org.json.JSONWriter;

/**
 * @author mpostelnicu
 *
 */
public class GenericBooleanChoiceProvider extends GenericChoiceProvider<Boolean> {

	private static final long serialVersionUID = 1L;

    @Override
    public final void toJson(final Boolean choice, final JSONWriter writer) throws JSONException {
        writer.key("id").value(choice.toString()).key("text").value(choice?"Yes":"No");
    }
	
	public GenericBooleanChoiceProvider(List<Boolean> listOfElements) {
		super(listOfElements);
	}

}
