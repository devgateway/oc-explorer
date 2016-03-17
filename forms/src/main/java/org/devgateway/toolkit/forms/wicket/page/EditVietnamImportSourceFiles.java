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

import java.io.File;
import java.net.URISyntaxException;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.link.DownloadLink;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.toolkit.forms.security.SecurityConstants;
import org.devgateway.toolkit.forms.wicket.components.form.FileInputBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.form.TextAreaFieldBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.form.TextFieldBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.page.edit.AbstractEditPage;
import org.devgateway.toolkit.forms.wicket.page.lists.ListVietnamImportSourceFiles;
import org.devgateway.toolkit.persistence.dao.VietnamImportSourceFiles;
import org.devgateway.toolkit.persistence.repository.VietnamImportSourceFilesRepository;
import org.wicketstuff.annotation.mount.MountPath;

/**
 * @author mpostelnicu
 *
 */

@AuthorizeInstantiation(SecurityConstants.Roles.ROLE_ADMIN)
@MountPath("/editImportSourceFiles")
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
		this.listPageClass=ListVietnamImportSourceFiles.class;

	}

	@Override
	protected VietnamImportSourceFiles newInstance() {
		return new VietnamImportSourceFiles();
	}

	@Override
	protected void onInitialize() {
		super.onInitialize();
		

		TextFieldBootstrapFormComponent<String> name = new TextFieldBootstrapFormComponent<>("name");
		name.required();
		editForm.add(name);

		TextAreaFieldBootstrapFormComponent<String> description = new TextAreaFieldBootstrapFormComponent<>("description");		
		editForm.add(description);		

		
		
		FileInputBootstrapFormComponent prototypeDatabaseFile = new FileInputBootstrapFormComponent(
				"prototypeDatabaseFile");
		prototypeDatabaseFile.maxFiles(1);
		prototypeDatabaseFile.required();
		editForm.add(prototypeDatabaseFile);

		FileInputBootstrapFormComponent publicInstitutionsSuppliersFile = new FileInputBootstrapFormComponent(
				"publicInstitutionsSuppliersFile");
		publicInstitutionsSuppliersFile.maxFiles(1);
		publicInstitutionsSuppliersFile.required();
		editForm.add(publicInstitutionsSuppliersFile);
		
		try {
		
		DownloadLink locationsTemplate=new DownloadLink("locationsTemplate", new File(getClass().getResource("/templates/Location_Table_SO.xlsx").toURI()));		
		editForm.add(locationsTemplate);
		
		DownloadLink suppliersTemplate=new DownloadLink("suppliersTemplate", new File(getClass().getResource("/templates/UM_PUBINSTITU_SUPPLIERS_DQA.xlsx").toURI()));		
		editForm.add(suppliersTemplate);
		
		DownloadLink prototypeDatabase=new DownloadLink("prototypeDatabase", new File(getClass().getResource("/templates/Prototype_Database_OCDSCore.xlsx").toURI()));		
		editForm.add(prototypeDatabase);
		
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		
		FileInputBootstrapFormComponent locationsFile = new FileInputBootstrapFormComponent("locationsFile");
		locationsFile.maxFiles(1);
		locationsFile.required();
		editForm.add(locationsFile);
		

	}

}
