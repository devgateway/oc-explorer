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
package org.devgateway.toolkit.forms.wicket.page;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.toolkit.forms.security.SecurityConstants;
import org.devgateway.toolkit.forms.wicket.components.form.TextFieldBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.page.edit.AbstractEditPage;
import org.devgateway.toolkit.forms.wicket.page.lists.ListGroupPage;
import org.devgateway.toolkit.persistence.dao.categories.Group;
import org.devgateway.toolkit.persistence.service.category.GroupService;
import org.wicketstuff.annotation.mount.MountPath;

@AuthorizeInstantiation(SecurityConstants.Roles.ROLE_ADMIN)
@MountPath("/editgroup")
public class EditGroupPage extends AbstractEditPage<Group> {

    private static final long serialVersionUID = -6069250112046118104L;

    @SpringBean
    protected GroupService groupService;

    public EditGroupPage(final PageParameters parameters) {
        super(parameters);
        this.jpaService = groupService;
        this.listPageClass = ListGroupPage.class;

    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        final TextFieldBootstrapFormComponent<String> name = new TextFieldBootstrapFormComponent<>("label");
        name.required();
        editForm.add(name);
    }
}
