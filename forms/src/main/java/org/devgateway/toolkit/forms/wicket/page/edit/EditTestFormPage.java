/**
 * Copyright (c) 2015 Development Gateway, Inc and others.
 * <p>
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the MIT License (MIT)
 * which accompanies this distribution, and is available at
 * https://opensource.org/licenses/MIT
 * <p>
 * Contributors:
 * Development Gateway - initial API and implementation
 */
/**
 *
 */
package org.devgateway.toolkit.forms.wicket.page.edit;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.toolkit.web.security.SecurityConstants;
import org.devgateway.toolkit.forms.wicket.components.form.CheckBoxBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.form.CheckBoxPickerBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.form.CheckBoxToggleBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.form.ColorPickerBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.form.DateFieldBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.form.DateTimeFieldBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.form.FileInputBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.form.Select2ChoiceBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.form.Select2MultiChoiceBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.form.SummernoteBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.form.TextAreaFieldBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.form.TextFieldBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.page.edit.panel.TestFormChildPanel;
import org.devgateway.toolkit.forms.wicket.page.lists.ListTestFormPage;
import org.devgateway.toolkit.forms.wicket.providers.GenericPersistableJpaTextChoiceProvider;
import org.devgateway.toolkit.persistence.dao.Role;
import org.devgateway.toolkit.persistence.dao.TestForm;
import org.devgateway.toolkit.persistence.dao.categories.Group;
import org.devgateway.toolkit.persistence.service.RoleService;
import org.devgateway.toolkit.persistence.service.TestFormService;
import org.devgateway.toolkit.persistence.service.category.GroupService;
import org.wicketstuff.annotation.mount.MountPath;

/**
 * @author mpostelnicu
 */

@AuthorizeInstantiation(SecurityConstants.Roles.ROLE_USER)
@MountPath("/editTestForm")
public class EditTestFormPage extends AbstractEditPage<TestForm> {

    private static final long serialVersionUID = 1L;

    @SpringBean
    private TestFormService testFormService;

    @SpringBean
    private RoleService roleService;

    @SpringBean
    private GroupService groupService;

    /**
     * @param parameters
     */
    public EditTestFormPage(final PageParameters parameters) {
        super(parameters);

        this.jpaService = testFormService;
        this.listPageClass = ListTestFormPage.class;
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        TextFieldBootstrapFormComponent<String> textField = new TextFieldBootstrapFormComponent<>("textField");
        editForm.add(textField);
        textField.required();
        textField.enableRevisionsView();


        TextAreaFieldBootstrapFormComponent<String> textArea = new TextAreaFieldBootstrapFormComponent<>("textArea");
        editForm.add(textArea);
        textArea.required();

        SummernoteBootstrapFormComponent summernote = new SummernoteBootstrapFormComponent("summernote");
        editForm.add(summernote);
        summernote.required().enableRevisionsView();

        editForm.add(new TestFormChildPanel("testFormChildren"));

        Select2ChoiceBootstrapFormComponent<Group> entitySelect = new Select2ChoiceBootstrapFormComponent<Group>(
                "entitySelect", new GenericPersistableJpaTextChoiceProvider<>(groupService));
        entitySelect.required();
        editForm.add(entitySelect);

        Select2MultiChoiceBootstrapFormComponent<Role> entityMultiSelect =
                new Select2MultiChoiceBootstrapFormComponent<Role>(
                        "entityMultiSelect",
                        new GenericPersistableJpaTextChoiceProvider<Role>(roleService)
                );
        editForm.add(entityMultiSelect);

        CheckBoxBootstrapFormComponent checkbox = new CheckBoxBootstrapFormComponent("checkbox");
        checkbox.required();
        editForm.add(checkbox);
        //checkbox.enableRevisionsView();

        CheckBoxPickerBootstrapFormComponent checkboxPicker =
                new CheckBoxPickerBootstrapFormComponent("checkboxPicker");
        checkboxPicker.required();
        editForm.add(checkboxPicker);
        //checkboxPicker.enableRevisionsView();

        CheckBoxToggleBootstrapFormComponent checkboxToggle =
                new CheckBoxToggleBootstrapFormComponent("checkboxToggle");
        checkboxToggle.required();
        editForm.add(checkboxToggle);

        DateFieldBootstrapFormComponent date = new DateFieldBootstrapFormComponent("date");
        date.required();
        editForm.add(date);

        DateTimeFieldBootstrapFormComponent dateTime = new DateTimeFieldBootstrapFormComponent("dateTime");
        dateTime.required();
        editForm.add(dateTime);

        FileInputBootstrapFormComponent fileInput = new FileInputBootstrapFormComponent("fileInput");
        fileInput.required();
        editForm.add(fileInput);

        ColorPickerBootstrapFormComponent colorPicker = new ColorPickerBootstrapFormComponent("colorPicker");
        colorPicker.required();
        editForm.add(colorPicker);
    }

}
