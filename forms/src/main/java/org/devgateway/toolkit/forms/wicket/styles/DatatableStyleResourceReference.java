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
package org.devgateway.toolkit.forms.wicket.styles;


import org.apache.wicket.request.resource.CssResourceReference;

public class DatatableStyleResourceReference extends CssResourceReference {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final DatatableStyleResourceReference INSTANCE = new DatatableStyleResourceReference();

    /**
     * Construct.
     */
    public DatatableStyleResourceReference() {
        super(DatatableStyleResourceReference.class, "datatable.css");
    }
}
