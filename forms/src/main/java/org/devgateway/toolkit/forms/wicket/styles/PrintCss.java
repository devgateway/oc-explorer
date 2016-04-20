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

/**
 * @author idobre
 * @since 1/13/15
 */
public class PrintCss extends CssResourceReference {
	private static final long serialVersionUID = 1L;

	public static final PrintCss INSTANCE = new PrintCss();

	/**
	 * Construct.
	 */
	public PrintCss() {
		super(PrintCss.class, "print.css");
	}
}
