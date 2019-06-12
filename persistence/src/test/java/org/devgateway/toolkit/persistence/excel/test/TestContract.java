package org.devgateway.toolkit.persistence.excel.test;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.StandardToStringStyle;
import org.devgateway.toolkit.persistence.excel.annotation.ExcelImport;

public class TestContract {
    @ExcelImport
    private String identifier;

    @ExcelImport
    private int amount;

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public void setAmount(int amount) {
        this.amount = amount;
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
