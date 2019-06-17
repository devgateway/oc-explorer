package org.devgateway.toolkit.persistence.excel.test;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.StandardToStringStyle;
import org.devgateway.toolkit.persistence.excel.annotation.ExcelImport;

import java.util.List;

/** ******************************************************************************
 *  Test Entities
 ******************************************************************************* */
public class TestBuyer {
    @ExcelImport
    private String name;

    @ExcelImport
    private TestOrganization org;

    @ExcelImport
    private TestAddress address;

    @ExcelImport
    private TestContract contract;

    private String ignoreField;

    @ExcelImport
    private List<String> ignoreList;

    public TestBuyer() {
        // default constructor - we might need this for reflection purposes.
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setOrg(TestOrganization org) {
        this.org = org;
    }

    public void setAddress(TestAddress address) {
        this.address = address;
    }

    public void setContract(TestContract contract) {
        this.contract = contract;
    }

    public String getName() {
        return name;
    }

    public TestOrganization getOrg() {
        return org;
    }

    @Override
    public String toString() {
        final StandardToStringStyle style = new StandardToStringStyle();
        style.setFieldSeparator(", ");
        style.setUseClassName(false);
        style.setUseIdentityHashCode(false);

        return new ReflectionToStringBuilder(this, style).toString();
    }
}
