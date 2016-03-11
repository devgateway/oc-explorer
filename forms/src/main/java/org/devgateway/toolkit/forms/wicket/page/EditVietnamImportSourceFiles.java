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
package org.devgateway.toolkit.forms.wicket.page;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.toolkit.forms.security.SecurityConstants;
import org.devgateway.toolkit.forms.wicket.components.form.FileInputBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.page.edit.AbstractEditPage;
import org.devgateway.toolkit.persistence.dao.VietnamImportSourceFiles;
import org.devgateway.toolkit.persistence.repository.VietnamImportSourceFilesRepository;
import org.wicketstuff.annotation.mount.MountPath;

/**
 * @author mpostelnicu
 *
 */

@AuthorizeInstantiation(SecurityConstants.Roles.ROLE_EDITOR)
@MountPath("/importSourceFiles")
public class EditVietnamImportSourceFiles extends AbstractEditPage<VietnamImportSourceFiles> {

	private static final long serialVersionUID = 1L;

	@SpringBean
	private VietnamImportSourceFilesRepository vietnamImportSourceFilesRepository;

	/**
	 * @param parameters
	 */
	public EditVietnamImportSourceFiles(PageParameters parameters) {
		super(parameters);

		this.jpaRepository = vietnamImportSourceFilesRepository;
		this.listPageClass=Homepage.class;
	}

	@Override
	protected VietnamImportSourceFiles newInstance() {
		return new VietnamImportSourceFiles();
	}

	@Override
	protected void onInitialize() {
		super.onInitialize();

		FileInputBootstrapFormComponent prototypeDatabaseFile = new FileInputBootstrapFormComponent(
				"prototypeDatabaseFile");
		prototypeDatabaseFile.maxFiles(1);
		editForm.add(prototypeDatabaseFile);

		FileInputBootstrapFormComponent publicInstitutionsSuppliersFile = new FileInputBootstrapFormComponent(
				"publicInstitutionsSuppliersFile");
		publicInstitutionsSuppliersFile.maxFiles(1);
		editForm.add(publicInstitutionsSuppliersFile);

		FileInputBootstrapFormComponent locationsFile = new FileInputBootstrapFormComponent("locationsFile");
		locationsFile.maxFiles(1);
		editForm.add(locationsFile);

	}

}
