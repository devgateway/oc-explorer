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
package org.devgateway.toolkit.persistence.dao;

import org.devgateway.toolkit.persistence.dao.categories.Group;
import org.devgateway.toolkit.persistence.excel.annotation.ExcelExport;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @author mpostelnicu
 *
 */
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Entity
@Audited
public class TestForm extends AbstractAuditableEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @ExcelExport
    private String textField;

    @ExcelExport
    @Column(length = DBConstants.MAX_DEFAULT_TEXT_LENGTH)
    private String textArea;

    @Column(length = DBConstants.MAX_DEFAULT_TEXT_LENGTH)
    private String summernote;

    @ExcelExport
    private Boolean checkbox;

    private Boolean checkboxPicker;

    private Boolean checkboxToggle;

    private String colorPicker;

    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @ManyToOne
    private Group entitySelect;

    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @ManyToMany
    private List<Role> entityMultiSelect;

    private Date date;

    private Date dateTime;

    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @ManyToOne
    private Group preloadedEntitySelect;

    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<FileMetadata> fileInput;

    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @OneToMany(mappedBy = "testForm", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @OrderColumn(name = "index")
    private List<TestFormChild> testFormChildren = new ArrayList<>();

    public TestForm() {
    }

    @Override
    public AbstractAuditableEntity getParent() {
        return null;
    }

    public String getTextField() {
        return textField;
    }

    public void setTextField(final String textField) {
        this.textField = textField;
    }

    public String getTextArea() {
        return textArea;
    }

    public void setTextArea(final String textArea) {
        this.textArea = textArea;
    }

    public Boolean getCheckbox() {
        return checkbox;
    }

    public void setCheckbox(final Boolean checkbox) {
        this.checkbox = checkbox;
    }

    public Group getEntitySelect() {
        return entitySelect;
    }

    public void setEntitySelect(final Group entitySelect) {
        this.entitySelect = entitySelect;
    }

    public List<Role> getEntityMultiSelect() {
        return entityMultiSelect;
    }

    public void setEntityMultiSelect(final List<Role> multiSelect) {
        this.entityMultiSelect = multiSelect;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(final Date date) {
        this.date = date;
    }

    public Set<FileMetadata> getFileInput() {
        return fileInput;
    }

    public void setFileInput(final Set<FileMetadata> fileInput) {
        this.fileInput = fileInput;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(final Date dateTime) {
        this.dateTime = dateTime;
    }

    public String getSummernote() {
        return summernote;
    }

    public void setSummernote(final String summernote) {
        this.summernote = summernote;
    }

    public Boolean getCheckboxPicker() {
        return checkboxPicker;
    }

    public void setCheckboxPicker(final Boolean checkboxPicker) {
        this.checkboxPicker = checkboxPicker;
    }

    public Boolean getCheckboxToggle() {
        return checkboxToggle;
    }

    public void setCheckboxToggle(final Boolean checkboxToggle) {
        this.checkboxToggle = checkboxToggle;
    }

    public Group getPreloadedEntitySelect() {
        return preloadedEntitySelect;
    }

    public void setPreloadedEntitySelect(final Group preloadedEntitySelect) {
        this.preloadedEntitySelect = preloadedEntitySelect;
    }

    public String getColorPicker() {
        return colorPicker;
    }

    public void setColorPicker(final String colorPicker) {
        this.colorPicker = colorPicker;
    }

    public List<TestFormChild> getTestFormChildren() {
        return testFormChildren;
    }

    public void setTestFormChildren(final List<TestFormChild> testFormChildren) {
        this.testFormChildren = testFormChildren;
    }
}
