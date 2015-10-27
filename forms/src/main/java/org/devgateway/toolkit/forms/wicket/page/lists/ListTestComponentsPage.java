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
package org.devgateway.toolkit.forms.wicket.page.lists;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.toolkit.forms.security.SecurityConstants;
import org.devgateway.toolkit.forms.wicket.page.EditTestComponentsPage;
import org.devgateway.toolkit.persistence.dao.TestComponents;
import org.devgateway.toolkit.persistence.repository.TestComponentsRepository;
import org.wicketstuff.annotation.mount.MountPath;

@AuthorizeInstantiation(SecurityConstants.Roles.ROLE_ADMIN)
@MountPath(value = "/listTestComponents")
public class ListTestComponentsPage extends AbstractListPage<TestComponents> {

	private static final long serialVersionUID = -324298525712620234L;
	@SpringBean
	protected TestComponentsRepository testComponentsRepository;

	public ListTestComponentsPage(PageParameters pageParameters) {
		super(pageParameters);
		this.jpaRepository = testComponentsRepository;
		this.editPageClass = EditTestComponentsPage.class;
	}

}
