package org.devgateway.toolkit.persistence.excel.info;

import org.devgateway.toolkit.persistence.excel.ExcelFieldImportService;
import org.devgateway.toolkit.persistence.excel.annotation.ExcelImport;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.stream.StreamSupport;

/**
 * Decorator class used to obtain only Excel Importable fields (the ones annotated with {@link ExcelImport}.
 *
 * @author idobre
 * @since 19/03/2018
 */
public final class ClassFieldsExcelImport implements ClassFields {
    private final ClassFields original;

    public ClassFieldsExcelImport(final ClassFields classFields) {
        this.original = classFields;
    }

    @Override
    public Iterator<Field> getFields() {
        final Iterable<Field> originalFields = () -> this.original.getFields();

        // return only classes that are annotated with @ExcelImport
        final Iterator<Field> fields = StreamSupport.stream(originalFields.spliterator(), false)
                .filter(field -> field.getAnnotation(ExcelImport.class) != null)
                .filter(this::filterByClass)
                .iterator();

        return fields;
    }

    /**
     * Filter by {@link ExcelImport#onlyForClass()} property.
     */
    private boolean filterByClass(final Field field) {
        Class[] allowedClasses = ExcelFieldImportService.getFieldClazz(field);

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
