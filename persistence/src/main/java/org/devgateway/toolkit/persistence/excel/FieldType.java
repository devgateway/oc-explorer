package org.devgateway.toolkit.persistence.excel;

import com.google.common.collect.ImmutableSet;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Identify the type of a Field that will be used in the writing strategy.
 *
 * @author idobre
 * @since 10/11/2017
 */
public enum FieldType {
    basic("basic"),

    object("object"),

    objectSeparateSheet("objectSeparateSheet");

    private final String value;

    FieldType(final String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }

    /**
     * This is not a complete list of primitive types in Java or their wrappers!
     * Is used to quickly identify if a field is a 'simply' object that can be printed in a Cell
     */
    public static final ImmutableSet<Class<?>> BASICTYPES = new ImmutableSet.Builder<Class<?>>()
            .add(String.class)
            .add(BigDecimal.class)
            .add(Date.class)
            .add(Integer.class)
            .add(Long.class)
            .add(Double.class)
            .add(Float.class)
            .add(Boolean.class)
            .add(int.class)
            .add(long.class)
            .add(double.class)
            .add(float.class)
            .add(boolean.class)
            .build();
}
