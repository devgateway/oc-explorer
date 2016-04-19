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
package org.devgateway.toolkit.forms.wicket.page.user;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxButton;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.devgateway.toolkit.forms.wicket.page.BasePage;

import de.agilecoders.wicket.core.markup.html.bootstrap.form.BootstrapForm;


public class GoToEmailPage extends BasePage {

	private static final long serialVersionUID = 4922361569150868592L;

	public GoToEmailPage(final PageParameters parameters) {
		super(parameters);
		GoToEmailPageForm form = new GoToEmailPageForm("form");
		add(form);
	}

	public class GoToEmailPageForm extends BootstrapForm<Void> {

		private static final long serialVersionUID = 5706757478220246192L;

		public GoToEmailPageForm(final String componentId) {
			super(componentId);
			IndicatingAjaxButton submit = new IndicatingAjaxButton("submit",
					new StringResourceModel("ok", GoToEmailPage.this, null)) {
				private static final long serialVersionUID = 1L;

				@Override
				protected void onSubmit(final AjaxRequestTarget target, final Form<?> form) {
					setResponsePage(LoginPage.class);
				}

			};
			add(submit);
		}
		
	}
}
