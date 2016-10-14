/*******************************************************************************
 * Copyright (c) 2016 Development Gateway, Inc and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the MIT License (MIT)
 * which accompanies this distribution, and is available at
 * https://opensource.org/licenses/MIT
 *
 * Contributors:
 * Development Gateway - initial API and implementation
 *******************************************************************************/
package org.devgateway.ocds.forms.wicket.page.list;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.ocds.forms.wicket.page.edit.EditUserDashboardPage;
import org.devgateway.ocds.forms.wicket.providers.PersonDashboardJpaRepositoryProvider;
import org.devgateway.ocds.persistence.dao.UserDashboard;
import org.devgateway.ocds.persistence.repository.UserDashboardRepository;
import org.devgateway.toolkit.forms.security.SecurityConstants;
import org.devgateway.toolkit.forms.wicket.page.lists.AbstractListPage;
import org.devgateway.toolkit.forms.wicket.providers.SortableJpaRepositoryDataProvider;
import org.wicketstuff.annotation.mount.MountPath;

@AuthorizeInstantiation(SecurityConstants.Roles.ROLE_PROCURING_ENTITY)
@MountPath(value = "/listMyDashboards")
public class ListMyDashboardsPage extends AbstractListPage<UserDashboard> {

    /**
     * 
     */
    private static final long serialVersionUID = 8105049572554654046L;

    @SpringBean
    private UserDashboardRepository userDashboardRepository;

    @Override
    public SortableJpaRepositoryDataProvider<UserDashboard> getProvider() {
        return new PersonDashboardJpaRepositoryProvider(userDashboardRepository);
    }

    public ListMyDashboardsPage(final PageParameters pageParameters) {
        super(pageParameters);
        this.jpaRepository = userDashboardRepository;
        this.editPageClass = EditUserDashboardPage.class;
        columns.add(new PropertyColumn<UserDashboard, String>(
                new Model<String>((new StringResourceModel("name", ListMyDashboardsPage.this, null)).getString()),
                "name", "name"));
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        editPageLink.setVisibilityAllowed(false);
    }

}
