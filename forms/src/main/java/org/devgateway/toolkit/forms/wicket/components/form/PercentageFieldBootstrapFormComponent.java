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
package org.devgateway.toolkit.forms.wicket.components.form;

import java.math.BigDecimal;

import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.convert.IConverter;
import org.apache.wicket.util.convert.converter.BigDecimalConverter;
import org.apache.wicket.validation.validator.RangeValidator;

/**
 * @author mpostelnicu
 * Field for showing percentages
 */
public class PercentageFieldBootstrapFormComponent extends TextFieldBootstrapFormComponent<BigDecimal> {
	private static final long serialVersionUID = 1L;
	
	private static final BigDecimalConverter percentageConverter = new BigDecimalConverter(); //{

	private Label label;
	

	public PercentageFieldBootstrapFormComponent(String id, IModel<String> labelModel, IModel<BigDecimal> model) {
		super(id, labelModel, model);
	}
	
	public PercentageFieldBootstrapFormComponent(String id, IModel<BigDecimal> model) {
		super(id, model);
	}

	/**
	 * @param id
	 */
	public PercentageFieldBootstrapFormComponent(String id) {
		super(id);
	}
	
	@Override
	protected TextField<BigDecimal> inputField(String id, IModel<BigDecimal> model) {
		return new TextField<BigDecimal>(id,initFieldModel()) {
			private static final long serialVersionUID = 1L;

			/* (non-Javadoc)
			 * @see org.apache.wicket.Component#getConverter(java.lang.Class)
			 */
			@SuppressWarnings("unchecked")
			@Override
			public <C> IConverter<C> getConverter(Class<C> type) {
				return (IConverter<C>) percentageConverter;
			}
		};
	}
	
	
	@Override
	protected void onInitialize() {
		decimal();
		getField().add(new RangeValidator<>(BigDecimal.ZERO, BigDecimal.valueOf(100)));
		super.onInitialize();	
		
	    label = new Label("label", "%");
	    label.add(new AttributeAppender("for", new Model<>(getField().getMarkupId())));
	    border.add(label);
	}

}
