package org.devgateway.toolkit.persistence.excel.info;

import org.devgateway.toolkit.persistence.excel.ExcelFieldService;
import org.devgateway.toolkit.persistence.excel.annotation.ExcelExport;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.stream.StreamSupport;

/**
 * Decorator class used to obtain only Excel Exportable fields (the ones annotated with {@link ExcelExport}.
 *
 * @author idobre
 * @since 10/11/2017
 */
public final class ClassFieldsExcelExport implements ClassFields {
    private final ClassFields original;

    public ClassFieldsExcelExport(final ClassFields classFields) {
        this.original = classFields;
    }

    @Override
    public Iterator<Field> getFields() {
        final Iterable<Field> originalFields = () -> this.original.getFields();

        // return only classes that are annotated with @ExcelExport
        final Iterator<Field> fields = StreamSupport.stream(originalFields.spliterator(), false)
                .filter(field -> field.getAnnotation(ExcelExport.class) != null)
                .filter(this::filterByClass)
                .iterator();

        return fields;
    }

    /**
     * Filter by {@link ExcelExport#onlyForClass()} property.
     */
    private boolean filterByClass(final Field field) {
        Class[] allowedClasses = ExcelFieldService.getFieldClazz(field);

        if (allowedClasses == null) {
            return true;
        }

        for (Class clazz : allowedClasses) {
            if (clazz.equals(this.getClazz())) {
                return true;
            }
        }

        return false;
    }

    @Override
    public Class getClazz() {
        return this.original.getClazz();
    }
}
