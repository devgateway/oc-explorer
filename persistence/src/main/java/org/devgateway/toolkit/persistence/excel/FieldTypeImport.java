package org.devgateway.toolkit.persistence.excel;

/**
 * Identify the type of a Field that will be used in the reading strategy.
 *
 * @author idobre
 * @since 19/03/2018
 */
public enum FieldTypeImport {
    basic("basic"),

    object("object"),

    objectImportByProp("objectImportByProp");

    private final String value;

    FieldTypeImport(final String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }
}