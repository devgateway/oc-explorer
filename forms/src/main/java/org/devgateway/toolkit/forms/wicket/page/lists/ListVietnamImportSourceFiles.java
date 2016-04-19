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
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.toolkit.forms.security.SecurityConstants;
import org.devgateway.toolkit.forms.wicket.page.EditVietnamImportSourceFiles;
import org.devgateway.toolkit.persistence.dao.VietnamImportSourceFiles;
import org.devgateway.toolkit.persistence.repository.VietnamImportSourceFilesRepository;
import org.wicketstuff.annotation.mount.MountPath;

@AuthorizeInstantiation(SecurityConstants.Roles.ROLE_ADMIN)
@MountPath(value = "/listImportSourceFiles")
public class ListVietnamImportSourceFiles extends AbstractListPage<VietnamImportSourceFiles> {

	private static final long serialVersionUID = -324298525712620234L;
	@SpringBean
	protected VietnamImportSourceFilesRepository vietnamImportSourceFilesRepository;

	public ListVietnamImportSourceFiles(final PageParameters pageParameters) {
		super(pageParameters);
		this.jpaRepository = vietnamImportSourceFilesRepository;
		this.editPageClass = EditVietnamImportSourceFiles.class;

		columns.add(
				new PropertyColumn<VietnamImportSourceFiles, String>(new Model<String>("Batch Name"), "name", "name"));
		columns.add(new PropertyColumn<VietnamImportSourceFiles, String>(new Model<String>("Created"), "created",
				"created"));
		columns.add(new PropertyColumn<VietnamImportSourceFiles, String>(new Model<String>("Updated"), "lastUpdated",
				"lastUpdated"));
	}

}
