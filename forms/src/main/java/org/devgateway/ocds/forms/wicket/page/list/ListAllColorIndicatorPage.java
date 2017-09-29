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
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.ocds.forms.wicket.page.edit.EditColorIndicatorPairPage;
import org.devgateway.ocds.persistence.dao.ColorIndicatorPair;
import org.devgateway.ocds.persistence.repository.ColorIndicatorPairRepository;
import org.devgateway.toolkit.forms.wicket.page.lists.AbstractListPage;
import org.devgateway.toolkit.web.security.SecurityConstants;
import org.wicketstuff.annotation.mount.MountPath;

@AuthorizeInstantiation(SecurityConstants.Roles.ROLE_ADMIN)
@MountPath(value = "/listColorIndicators")
public class ListAllColorIndicatorPage extends AbstractListPage<ColorIndicatorPair> {

    @SpringBean
    protected ColorIndicatorPairRepository colorIndicatorPairRepository;



    public ListAllColorIndicatorPage(final PageParameters pageParameters) {
        super(pageParameters);
        this.jpaRepository = colorIndicatorPairRepository;
        this.editPageClass = EditColorIndicatorPairPage.class;
        columns.add(new PropertyColumn<ColorIndicatorPair, String>(new Model<String>("First Indicator"),
                "firstIndicator", "firstIndicator"));

        columns.add(new PropertyColumn<ColorIndicatorPair, String>(new Model<String>("Second Indicator"),
                "secondIndicator", "secondIndicator"));

        columns.add(new PropertyColumn<ColorIndicatorPair, String>(new Model<String>("Color"),
                "color", "color"));

    }

}
