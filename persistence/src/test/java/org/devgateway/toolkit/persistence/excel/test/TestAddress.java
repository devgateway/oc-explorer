package org.devgateway.toolkit.persistence.excel.test;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.StandardToStringStyle;
import org.devgateway.toolkit.persistence.excel.annotation.ExcelImport;
import org.junit.Test;

public class TestAddress {
    private Long id;

    @ExcelImport
    private String street;

    @ExcelImport
    private String country;

    public TestAddress() {
        // default constructor - we might need this for reflection purposes.
    }

    public TestAddress(final String street, final String country) {
        this.street = street;
        this.country = country;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getStreet() {
        return street;
    }

    public String getCountry() {
        return country;
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
