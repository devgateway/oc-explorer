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
package org.devgateway.ocvn.forms.wicket.page;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.link.DownloadLink;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.ocvn.forms.xlsx.RootXlsx;
import org.devgateway.ocvn.persistence.dao.VietnamImportSourceFiles;
import org.devgateway.ocvn.persistence.repository.VietnamImportSourceFilesRepository;
import org.devgateway.toolkit.forms.security.SecurityConstants;
import org.devgateway.toolkit.forms.wicket.components.form.FileInputBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.form.TextAreaFieldBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.form.TextFieldBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.page.edit.AbstractEditPage;
import org.devgateway.toolkit.forms.wicket.page.lists.ListVietnamImportSourceFiles;
import org.wicketstuff.annotation.mount.MountPath;

import java.io.File;
import java.net.URISyntaxException;

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
    public EditVietnamImportSourceFiles(final PageParameters parameters) {
        super(parameters);

        this.jpaRepository = vietnamImportSourceFilesRepository;
        this.listPageClass = ListVietnamImportSourceFiles.class;

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

        TextAreaFieldBootstrapFormComponent<String> description = new TextAreaFieldBootstrapFormComponent<>(
                "description");
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
            DownloadLink locationsTemplate = new DownloadLink("locationsTemplate",
                    new File(RootXlsx.class.getResource("Location_Table_SO.xlsx").toURI()));

            editForm.add(locationsTemplate);

            DownloadLink suppliersTemplate = new DownloadLink("suppliersTemplate",
                    new File(RootXlsx.class.getResource("UM_PUBINSTITU_SUPPLIERS_DQA.xlsx").toURI()));
            editForm.add(suppliersTemplate);

            DownloadLink prototypeDatabase = new DownloadLink("prototypeDatabase",
                    new File(RootXlsx.class.getResource("Prototype_Database_OCDSCore.xlsx").toURI()));

            editForm.add(prototypeDatabase);

        } catch (URISyntaxException e) {
            logger.error(e);
            e.printStackTrace();
        }

        FileInputBootstrapFormComponent locationsFile = new FileInputBootstrapFormComponent("locationsFile");
        locationsFile.maxFiles(1);
        locationsFile.required();
        editForm.add(locationsFile);


    }

}
