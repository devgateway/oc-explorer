package org.devgateway.toolkit.persistence.excel.test;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.StandardToStringStyle;
import org.devgateway.toolkit.persistence.excel.annotation.ExcelImport;

public class TestOrganization {
    @ExcelImport(importByProp = "id")
    private TestAddress address;

    @ExcelImport
    private String name;

    public void setAddress(TestAddress address) {
        this.address = address;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TestAddress getAddress() {
        return address;
    }

    public String getName() {
        return name;
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
