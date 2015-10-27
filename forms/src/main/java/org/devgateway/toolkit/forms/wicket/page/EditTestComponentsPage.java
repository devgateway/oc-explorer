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
import org.devgateway.toolkit.forms.wicket.components.form.CheckBoxBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.form.DateFieldBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.form.FileInputBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.form.Select2ChoiceBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.form.Select2MultiChoiceBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.form.TextAreaFieldBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.form.TextFieldBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.page.edit.AbstractEditPage;
import org.devgateway.toolkit.forms.wicket.page.lists.ListTestComponentsPage;
import org.devgateway.toolkit.forms.wicket.providers.GenericPersistableJpaRepositoryTextChoiceProvider;
import org.devgateway.toolkit.persistence.dao.TestComponents;
import org.devgateway.toolkit.persistence.dao.categories.Group;
import org.devgateway.toolkit.persistence.dao.categories.Role;
import org.devgateway.toolkit.persistence.repository.GroupRepository;
import org.devgateway.toolkit.persistence.repository.RoleRepository;
import org.devgateway.toolkit.persistence.repository.TestComponentsRepository;
import org.wicketstuff.annotation.mount.MountPath;

/**
 * @author mpostelnicu
 *
 */

@AuthorizeInstantiation(SecurityConstants.Roles.ROLE_EDITOR)
@MountPath("/testComponents")
public class EditTestComponentsPage extends AbstractEditPage<TestComponents> {

	private static final long serialVersionUID = 1L;

	@SpringBean
	private TestComponentsRepository testComponentsRepository;

	@SpringBean
	private RoleRepository roleRepository;

	@SpringBean
	private GroupRepository groupRepository;

	/**
	 * @param parameters
	 */
	public EditTestComponentsPage(PageParameters parameters) {
		super(parameters);

		this.jpaRepository = testComponentsRepository;
		this.listPageClass = ListTestComponentsPage.class;
	}

	@Override
	protected TestComponents newInstance() {
		return new TestComponents();
	}

	@Override
	protected void onInitialize() {
		super.onInitialize();

		TextFieldBootstrapFormComponent<String> textField = new TextFieldBootstrapFormComponent<>("textField");
		textField.required();
		editForm.add(textField);

		TextAreaFieldBootstrapFormComponent<String> textArea = new TextAreaFieldBootstrapFormComponent<>("textArea");
		textArea.required();
		editForm.add(textArea);

		Select2ChoiceBootstrapFormComponent<Group> entitySelect = new Select2ChoiceBootstrapFormComponent<Group>(
				"entitySelect", new GenericPersistableJpaRepositoryTextChoiceProvider<Group>(groupRepository));
		editForm.add(entitySelect);

		Select2MultiChoiceBootstrapFormComponent<Role> entityMultiSelect = new Select2MultiChoiceBootstrapFormComponent<Role>(
				"entityMultiSelect", new GenericPersistableJpaRepositoryTextChoiceProvider<Role>(roleRepository));
		editForm.add(entityMultiSelect);

		CheckBoxBootstrapFormComponent checkbox = new CheckBoxBootstrapFormComponent("checkbox");
		editForm.add(checkbox);
		
		DateFieldBootstrapFormComponent date=new DateFieldBootstrapFormComponent("date");
		editForm.add(date);
		
		FileInputBootstrapFormComponent fileInput=new FileInputBootstrapFormComponent("fileInput");
		editForm.add(fileInput);
	}

}
