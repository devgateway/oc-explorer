package org.devgateway.toolkit.persistence.excel;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;
import org.devgateway.toolkit.persistence.excel.annotation.ExcelImport;
import org.devgateway.toolkit.persistence.excel.info.ClassFields;
import org.devgateway.toolkit.persistence.excel.info.ClassFieldsDefault;
import org.devgateway.toolkit.persistence.excel.info.ClassFieldsExcelImport;
import org.devgateway.toolkit.persistence.excel.info.ImportBean;
import org.devgateway.toolkit.persistence.repository.norepository.BaseJpaRepository;
import org.devgateway.toolkit.persistence.repository.PersonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author idobre
 * @since 19/03/2018
 */
public final class ExcelFieldImportService {
    private static final Logger logger = LoggerFactory.getLogger(ExcelFieldImportService.class);

    private static Map<Field, FieldTypeImport> fieldsTypeCache;

    private static Map<Class, List<Field>> fieldsCache;

    private static Map<String, BaseJpaRepository> repositoryCache;

    private static Map<String, Object> repositoryValuesCache;

    private static Map<String, Method> repositoryMethodsCache;

    /**
     * Cache everything :)
     */
    static {
        fieldsTypeCache = new HashMap<>();
        fieldsCache = new HashMap<>();
        repositoryCache = new HashMap<>();
        repositoryValuesCache = new HashMap<>();
        repositoryMethodsCache = new HashMap<>();
    }

    /**
     * Utility classes should not have a public or default constructor.
     */
    private ExcelFieldImportService() {

    }

    /**
     * Return the {@link FieldTypeImport} of a Field
     * This is used to determine the reading strategy for this particular field.
     *
     * @param field
     * @return {@link FieldTypeImport}
     */
    public static FieldTypeImport getFieldType(final Field field) {
        FieldTypeImport fieldType;

        if (fieldsTypeCache.get(field) != null) {
            fieldType = fieldsTypeCache.get(field);
        } else {
            fieldType = FieldTypeImport.basic;        // default field type
            final Class fieldClass = ExcelFieldService.getFieldClass(field);

            // first we check if we have a basic type
            if (FieldType.BASICTYPES.contains(fieldClass) || fieldClass.isEnum()) {
                fieldType = FieldTypeImport.basic;
            } else {
                final ExcelImport excelImport = field.getAnnotation(ExcelImport.class);
                if (excelImport != null) {
                    fieldType = FieldTypeImport.object;

                    // check if we need to read and import the object from the database.
                    if (!excelImport.importByProp().isEmpty()) {
                        fieldType = FieldTypeImport.objectImportByProp;
                    }
                }
            }

            fieldsTypeCache.put(field, fieldType);
        }

        return fieldType;
    }

    /**
     * Returns an Iterator with the Fields of a Class.
     * The fields are filtered with the {@link ClassFieldsExcelImport} class.
     *
     * @param clazz
     * @return {@link Iterator}
     */
    public static Iterator<Field> getFields(final Class clazz) {
        final Iterator<Field> fields;

        if (fieldsCache.get(clazz) != null) {
            final List<Field> fieldsList = fieldsCache.get(clazz);
            fields = fieldsList.iterator();
        } else {
            final ClassFields classFields = new ClassFieldsExcelImport(new ClassFieldsDefault(clazz, true));
            fields = classFields.getFields();

            fieldsCache.put(clazz, Lists.newArrayList(classFields.getFields()));
        }

        return fields;
    }

    /**
     * Returns the prop that will be used to fetch the {@link Field} data from the database.
     *
     * @param field
     */
    public static String getFieldProp(final Field field) {
        final ExcelImport excelImport = field.getAnnotation(ExcelImport.class);
        if (excelImport != null) {
            if (!excelImport.importByProp().isEmpty()) {
                return excelImport.importByProp();
            }
        }

        return null;
    }

    /**
     * Returns the repository class that will be used to fetch the {@link Field} data from the database.
     *
     * @param field
     */
    public static Class getFieldBean(final Field field) {
        final ExcelImport excelImport = field.getAnnotation(ExcelImport.class);
        if (excelImport != null) {
            if (!excelImport.importBean().getSimpleName().equals("void")) {
                return excelImport.importBean();
            }
        }

        return null;
    }

    /**
     * Returns the onlyForClass prop of the {@link Field}.
     *
     * @param field
     */
    public static Class[] getFieldClazz(final Field field) {
        final ExcelImport excelImport = field.getAnnotation(ExcelImport.class);
        if (excelImport != null) {
            if (excelImport.onlyForClass().length != 0
                    && !excelImport.onlyForClass()[0].getSimpleName().equals("void")) {
                return excelImport.onlyForClass();
            }
        }

        return null;
    }

    /**
     * If we have a field marked as {@link ExcelImport#importByProp()} then we need to fetch it's value from the
     * database. We try to identify the {@link BaseJpaRepository} associated with this {@link Field} and invoke a fetch
     * method depending on the cell value.
     */
    public static Object getObjectFromRepository(final ApplicationContext applicationContext, final Field parentField,
                                                 final Field field, final Object cellValue, final ImportBean importBean)
            throws Exception {
        final Class fieldClass = ExcelFieldService.getFieldClass(field);

        final String classRepositoryPath;
        final Class<? extends BaseJpaRepository> clazzRepository;
        final Class classBean;

        // try to get the field bean from it's annotation or from the field parent annotation
        if (ExcelFieldImportService.getFieldBean(field) != null) {
            classBean = ExcelFieldImportService.getFieldBean(field);
        } else {
            classBean = parentField == null ? null : ExcelFieldImportService.getFieldBean(parentField);
        }

        if (classBean != null) {
            classRepositoryPath = classBean.getName();
            clazzRepository = classBean;
        } else {
            final String packageName = ClassUtils.getPackageName(fieldClass);
            final String repositoryPackageName = packageName.replace("dao", "repository");
            classRepositoryPath = repositoryPackageName + "." + fieldClass.getSimpleName() + "Repository";
            clazzRepository = (Class<? extends BaseJpaRepository>) Class.forName(classRepositoryPath);
        }

        BaseJpaRepository jpaRepository = repositoryCache.get(classRepositoryPath);

        if (jpaRepository == null) {
            jpaRepository = applicationContext.getBean(clazzRepository);
            repositoryCache.put(classRepositoryPath, jpaRepository);
        }

        final String prop = ExcelFieldImportService.getFieldProp(field);
        final String valueKey = classRepositoryPath + importBean.getCategory().getId() + prop + cellValue;
        Object value = repositoryValuesCache.get(valueKey);
        if (value == null && cellValue != null && !((String) cellValue).isEmpty()) {
            if (prop.equals("id")) {
                value = jpaRepository.findById(Long.valueOf((String) cellValue)).get();
            } else {
                if (prop.equals("username")) {
                    // this is a special case for usernames
                    final PersonRepository personRepository = (PersonRepository) jpaRepository;
                    // value = personRepository.findByCategory((String) cellValue, importBean.getCategory());
                } else {
                    final Class methodParamClass;
                    // determine if the method param is Integer or String
                    final boolean isInteger = ((String) cellValue).matches("\\d+")
                            && !field.getName().equals("period");
                    if (isInteger) {
                        methodParamClass = Integer.class;
                    } else {
                        methodParamClass = String.class;
                    }

                    final Method method = findMethod(clazzRepository, prop, methodParamClass);
                    if (method != null) {
                        if (isInteger) {
                            value = method.invoke(jpaRepository, Integer.valueOf((String) cellValue));
                        } else {
                            value = method.invoke(jpaRepository, (String) cellValue);
                        }
                    } else {
                        logger.error("No method for fond for: "
                                + "findBy" + StringUtils.capitalize(prop) + " - " + methodParamClass);
                    }
                }
            }

            repositoryValuesCache.put(valueKey, value);
        }

        return value;
    }

    /**
     * Try to find in the clazzRepository the method that filters by prop and methodParamClass parameters.
     *
     * For example in {@link ScaleRatingRepository} we can find {@link ScaleRatingRepository#findByValue} that
     * accepts a {@link Integer} as a parameter.
     */
    private static Method findMethod(final Class<? extends BaseJpaRepository> clazzRepository,
                                     final String prop, final Class methodParamClass) {
        final String valueKey = clazzRepository + prop + methodParamClass;
        if (repositoryMethodsCache.get(valueKey) != null) {
            return repositoryMethodsCache.get(valueKey);
        }

        // try to find the method in class or in it's superclass
        Method method = null;
        Method methodSuperClass = null;
        try {
            method = clazzRepository.getMethod("findBy" + StringUtils.capitalize(prop), methodParamClass);
            methodSuperClass = clazzRepository.getInterfaces()[0]
                    .getMethod("findBy" + StringUtils.capitalize(prop), methodParamClass);
        } catch (NoSuchMethodException e) {
            // do nothing, log this later and return null for this field
        }

        if (method == null && methodSuperClass != null) {
            method = methodSuperClass;
        }

        repositoryMethodsCache.put(valueKey, method);   // update the cache with the new Method found

        return method;
    }
}
