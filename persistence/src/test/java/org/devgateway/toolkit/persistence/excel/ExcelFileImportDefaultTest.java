package org.devgateway.toolkit.persistence.excel;

import org.devgateway.toolkit.persistence.dao.categories.Category;
import org.devgateway.toolkit.persistence.excel.info.ImportBean;
import org.devgateway.toolkit.persistence.excel.test.TestAddressRepository;
import org.devgateway.toolkit.persistence.excel.test.TestBuyer;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.boot.logging.LogLevel;
import org.springframework.boot.logging.LoggingSystem;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.NoSuchMessageException;
import org.springframework.core.ResolvableType;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.Locale;
import java.util.Map;

import static org.slf4j.Logger.ROOT_LOGGER_NAME;

/**
 * @author idobre
 * @since 21/03/2018
 */
public class ExcelFileImportDefaultTest {
    @BeforeClass
    public static void setErrorLogging() {
        LoggingSystem.get(ClassLoader.getSystemClassLoader()).setLogLevel(ROOT_LOGGER_NAME, LogLevel.WARN);
    }

    @Test
    public void readWorkbook() throws Exception {
        final ClassLoader classLoader = getClass().getClassLoader();
        final ExcelFileImport excelFileImport = new ExcelFileImportDefault(new MyApplicationContext(),
                new FileInputStream(classLoader.getResource("file-import.xlsx").getFile()),
                TestBuyer.class, new ImportBean(new Category()));
        final ImportResponse importResponse = excelFileImport.readWorkbook();

        TestBuyer testBuyer = (TestBuyer) importResponse.getObjects().get(0);
        Assert.assertEquals("check number of objects", 2, importResponse.getObjects().size());
        Assert.assertEquals("check buyer name", "buyer 1", testBuyer.getName());
        Assert.assertEquals("check organization name", "organization 1", testBuyer.getOrg().getName());
        Assert.assertEquals("check address", "Street 1", testBuyer.getOrg().getAddress().getStreet());
        Assert.assertEquals("check address", "Romania", testBuyer.getOrg().getAddress().getCountry());
    }

    /**
     * Mock for {@link ApplicationContext} class.
     */
    class MyApplicationContext implements ApplicationContext {
        @Override
        public <T> T getBean(Class<T> aClass) throws BeansException {
            return (T) new TestAddressRepository();
        }

        @Override
        public String getId() {
            return null;
        }

        @Override
        public String getApplicationName() {
            return null;
        }

        @Override
        public String getDisplayName() {
            return null;
        }

        @Override
        public long getStartupDate() {
            return 0;
        }

        @Override
        public ApplicationContext getParent() {
            return null;
        }

        @Override
        public AutowireCapableBeanFactory getAutowireCapableBeanFactory() throws IllegalStateException {
            return null;
        }

        @Override
        public BeanFactory getParentBeanFactory() {
            return null;
        }

        @Override
        public boolean containsLocalBean(String s) {
            return false;
        }

        @Override
        public boolean containsBeanDefinition(String s) {
            return false;
        }

        @Override
        public int getBeanDefinitionCount() {
            return 0;
        }

        @Override
        public String[] getBeanDefinitionNames() {
            return new String[0];
        }

        @Override
        public String[] getBeanNamesForType(ResolvableType resolvableType) {
            return new String[0];
        }

        @Override
        public String[] getBeanNamesForType(Class<?> aClass) {
            return new String[0];
        }

        @Override
        public String[] getBeanNamesForType(Class<?> aClass, boolean b, boolean b1) {
            return new String[0];
        }

        @Override
        public <T> Map<String, T> getBeansOfType(Class<T> aClass) throws BeansException {
            return null;
        }

        @Override
        public <T> Map<String, T> getBeansOfType(Class<T> aClass, boolean b, boolean b1) throws BeansException {
            return null;
        }

        @Override
        public String[] getBeanNamesForAnnotation(Class<? extends Annotation> aClass) {
            return new String[0];
        }

        @Override
        public Map<String, Object> getBeansWithAnnotation(Class<? extends Annotation> aClass) throws BeansException {
            return null;
        }

        @Override
        public <A extends Annotation> A findAnnotationOnBean(String s, Class<A> aClass) throws NoSuchBeanDefinitionException {
            return null;
        }

        @Override
        public Object getBean(String s) throws BeansException {
            return null;
        }

        @Override
        public <T> T getBean(String s, Class<T> aClass) throws BeansException {
            return null;
        }

        @Override
        public Object getBean(String s, Object... objects) throws BeansException {
            return null;
        }

        @Override
        public <T> T getBean(Class<T> aClass, Object... objects) throws BeansException {
            return null;
        }

        @Override
        public <T> ObjectProvider<T> getBeanProvider(Class<T> aClass) {
            return null;
        }

        @Override
        public <T> ObjectProvider<T> getBeanProvider(ResolvableType resolvableType) {
            return null;
        }

        @Override
        public boolean containsBean(String s) {
            return false;
        }

        @Override
        public boolean isSingleton(String s) throws NoSuchBeanDefinitionException {
            return false;
        }

        @Override
        public boolean isPrototype(String s) throws NoSuchBeanDefinitionException {
            return false;
        }

        @Override
        public boolean isTypeMatch(String s, ResolvableType resolvableType) throws NoSuchBeanDefinitionException {
            return false;
        }

        @Override
        public boolean isTypeMatch(String s, Class<?> aClass) throws NoSuchBeanDefinitionException {
            return false;
        }

        @Override
        public Class<?> getType(String s) throws NoSuchBeanDefinitionException {
            return null;
        }

        @Override
        public String[] getAliases(String s) {
            return new String[0];
        }

        @Override
        public void publishEvent(ApplicationEvent applicationEvent) {

        }

        @Override
        public void publishEvent(Object o) {

        }

        @Override
        public String getMessage(String s, Object[] objects, String s1, Locale locale) {
            return null;
        }

        @Override
        public String getMessage(String s, Object[] objects, Locale locale) throws NoSuchMessageException {
            return null;
        }

        @Override
        public String getMessage(MessageSourceResolvable messageSourceResolvable, Locale locale) throws NoSuchMessageException {
            return null;
        }

        @Override
        public Environment getEnvironment() {
            return null;
        }

        @Override
        public Resource[] getResources(String s) throws IOException {
            return new Resource[0];
        }

        @Override
        public Resource getResource(String s) {
            return null;
        }

        @Override
        public ClassLoader getClassLoader() {
            return null;
        }
    }
}

