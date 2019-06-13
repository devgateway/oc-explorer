/*******************************************************************************
 * Copyright (c) 2015 Development Gateway, Inc and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the MIT License (MIT)
 * which accompanies this distribution, and is available at
 * https://opensource.org/licenses/MIT
 *
 * Contributors:
 * Development Gateway - initial API and implementation
 *******************************************************************************/
package org.devgateway.toolkit.forms.wicket;

import com.google.javascript.jscomp.CompilationLevel;
import de.agilecoders.wicket.core.Bootstrap;
import de.agilecoders.wicket.core.markup.html.RenderJavaScriptToFooterHeaderResponseDecorator;
import de.agilecoders.wicket.core.request.resource.caching.version.Adler32ResourceVersion;
import de.agilecoders.wicket.core.settings.BootstrapSettings;
import de.agilecoders.wicket.core.settings.IBootstrapSettings;
import de.agilecoders.wicket.extensions.javascript.GoogleClosureJavaScriptCompressor;
import de.agilecoders.wicket.extensions.javascript.YuiCssCompressor;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.editor.SummernoteConfig;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.editor.SummernoteStoredImageResourceReference;
import de.agilecoders.wicket.less.BootstrapLess;
import de.agilecoders.wicket.webjars.WicketWebjars;
import nl.dries.wicket.hibernate.dozer.DozerRequestCycleListener;
import nl.dries.wicket.hibernate.dozer.SessionFinderHolder;
import org.apache.wicket.Application;
import org.apache.wicket.ConverterLocator;
import org.apache.wicket.IConverterLocator;
import org.apache.wicket.Page;
import org.apache.wicket.ajax.AjaxNewWindowNotifyingBehavior;
import org.apache.wicket.authroles.authentication.AuthenticatedWebApplication;
import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.devutils.diskstore.DebugDiskDataStore;
import org.apache.wicket.markup.html.IPackageResourceGuard;
import org.apache.wicket.markup.html.SecurePackageResourceGuard;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.apache.wicket.request.resource.ResourceReference;
import org.apache.wicket.request.resource.caching.FilenameWithVersionResourceCachingStrategy;
import org.apache.wicket.request.resource.caching.NoOpResourceCachingStrategy;
import org.apache.wicket.request.resource.caching.version.CachingResourceVersion;
import org.apache.wicket.settings.RequestCycleSettings.RenderStrategy;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;
import org.apache.wicket.util.file.Folder;
import org.devgateway.toolkit.forms.service.SessionFinderService;
import org.devgateway.toolkit.forms.wicket.components.form.SummernoteJpaStorageService;
import org.devgateway.toolkit.forms.wicket.converters.NonNumericFilteredBigDecimalConverter;
import org.devgateway.toolkit.forms.wicket.page.BasePage;
import org.devgateway.toolkit.forms.wicket.page.Homepage;
import org.devgateway.toolkit.forms.wicket.page.user.LoginPage;
import org.devgateway.toolkit.forms.wicket.styles.BaseStyles;
import org.nustaq.serialization.FSTConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.wicketstuff.annotation.scan.AnnotatedMountScanner;
import org.wicketstuff.pageserializer.fast2.Fast2WicketSerializer;
import org.wicketstuff.select2.ApplicationSettings;

import java.math.BigDecimal;

/**
 * The web application class also serves as spring boot starting point by using
 * spring boot's EnableAutoConfiguration annotation and providing the main
 * method.
 *
 * @author Stefan Kloe, mpostelnicu
 */
@EnableScheduling
@SpringBootApplication(exclude = {org.springframework.boot.autoconfigure.gson.GsonAutoConfiguration.class})
@ComponentScan("org.devgateway")
@PropertySource("classpath:/org/devgateway/toolkit/forms/application.properties")
@EnableCaching
public class FormsWebApplication extends AuthenticatedWebApplication {


    public static final String STORAGE_ID = "fileStorage";

    private static final String BASE_PACKAGE_FOR_PAGES = BasePage.class.getPackage().getName();

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private SessionFinderService sessionFinderService;


    @Autowired
    private SummernoteJpaStorageService summernoteJpaStorageService;

    public static void main(final String[] args) {
        SpringApplication.run(FormsWebApplication.class, args);
    }

    /**
     * @see org.apache.wicket.Application#newConverterLocator() This adds the
     * {@link NonNumericFilteredBigDecimalConverter} as the standard
     * {@link BigDecimal} converter for ALL fields using this type accross
     * the application
     **/
    @Override
    protected IConverterLocator newConverterLocator() {
        ConverterLocator locator = (ConverterLocator) super.newConverterLocator();
        locator.set(BigDecimal.class, new NonNumericFilteredBigDecimalConverter());
        return locator;
    }

    private void configureSummernote() {
        // the folder where to store the images
        Folder folder = new Folder(System.getProperty("java.io.tmpdir"), "bootstrap-summernote");
        folder.mkdirs();
        folder.deleteOnExit();

        SummernoteConfig.addStorage(summernoteJpaStorageService);

        // mount the resource reference responsible for image uploads
        mountResource(
                SummernoteStoredImageResourceReference.SUMMERNOTE_MOUNT_PATH,
                new SummernoteStoredImageResourceReference(SummernoteJpaStorageService.STORAGE_ID)
        );
    }

    /**
     * provides page for default request
     */
    @Override
    public Class<? extends Page> getHomePage() {
        return Homepage.class;
    }

    public static FormsWebApplication get() {
        return (FormsWebApplication) Application.get();
    }

    /**
     * configures wicket-bootstrap and installs the settings.
     */
    private void configureBootstrap() {
        WicketWebjars.install(this);

        final IBootstrapSettings settings = new BootstrapSettings();
        settings.useCdnResources(false);

        // use the default bootstrap theme
        Bootstrap.install(this, settings);
        BootstrapLess.install(this);
    }

    /**
     * optimize wicket for a better web performance This will be invoked if the
     * application is started with -Dwicket.configuration=deployment
     */
    private void optimizeForWebPerformance() {
        // add javascript files at the bottom of the page
        setHeaderResponseDecorator(new RenderJavaScriptToFooterHeaderResponseDecorator("scripts-bucket"));

        // This is only enabled for deployment configuration
        // -Dwicket.configuration=deployment
        // The default is Development, so this code is not used
        if (usesDeploymentConfig()) {
            getResourceSettings().setCachingStrategy(new FilenameWithVersionResourceCachingStrategy("-v-",
                    new CachingResourceVersion(new Adler32ResourceVersion())
            ));
            getResourceSettings().setJavaScriptCompressor(
                    new GoogleClosureJavaScriptCompressor(CompilationLevel.SIMPLE_OPTIMIZATIONS));
            getResourceSettings().setCssCompressor(new YuiCssCompressor());
            getResourceSettings().setUseMinifiedResources(true);

            // getFrameworkSettings().setSerializer(new DeflatedJavaSerializer(getApplicationKey()));
            final FSTConfiguration fstConfiguration = Fast2WicketSerializer.getDefaultFSTConfiguration();
            getFrameworkSettings().setSerializer(new Fast2WicketSerializer(fstConfiguration));

            getMarkupSettings().setStripComments(true);
            getMarkupSettings().setCompressWhitespace(true);
            getMarkupSettings().setStripWicketTags(true);
        } else {
            getResourceSettings().setCachingStrategy(new NoOpResourceCachingStrategy());

            final FSTConfiguration fstConfiguration = Fast2WicketSerializer.getDefaultFSTConfiguration();
            getFrameworkSettings().setSerializer(new Fast2WicketSerializer(fstConfiguration));
        }

        getRequestCycleSettings().setRenderStrategy(RenderStrategy.ONE_PASS_RENDER);
        // be sure that we have added Dozer Listener
        getRequestCycleListeners().add(new DozerRequestCycleListener());

        // additional safety guard for opening in the session the same pages
        Application.get().getComponentInitializationListeners().add(component -> {
            if (component instanceof WebPage) {
                component.add(new AjaxNewWindowNotifyingBehavior());
            }
        });
    }

    @Override
    protected Class<? extends AuthenticatedWebSession> getWebSessionClass() {
        return SSAuthenticatedWebSession.class;
    }

    /**
     * <ul>
     * <li>making the wicket components injectable by activating the
     * SpringComponentInjector</li>
     * <li>mounting the test page</li>
     * <li>logging spring service method output to showcase working integration
     * </li>
     * </ul>
     */
    @Override
    protected void init() {
        super.init();

        // add allowed woff2 extension
        IPackageResourceGuard packageResourceGuard = getResourceSettings().getPackageResourceGuard();
        if (packageResourceGuard instanceof SecurePackageResourceGuard) {
            SecurePackageResourceGuard guard = (SecurePackageResourceGuard) packageResourceGuard;
            guard.addPattern("+*.woff2");
            guard.addPattern("+*.xlsx");
        }

        //this ensures that spring DI works for wicket components and pages
        //see @SpringBean annotation
        getComponentInstantiationListeners().add(new SpringComponentInjector(this, applicationContext));

        //this will scan packages for pages with @MountPath annotations and automatically create URLs for them
        new AnnotatedMountScanner().scanPackage(BASE_PACKAGE_FOR_PAGES).mount(this);

        getApplicationSettings().setUploadProgressUpdatesEnabled(true);
        getApplicationSettings().setAccessDeniedPage(Homepage.class);

        configureBootstrap();
        configureSummernote();
        optimizeForWebPerformance();

        // watch this using the URL
        // http://.../wicket/internal/debug/diskDataStore
        if (usesDevelopmentConfig()) {
            DebugDiskDataStore.register(this);
        }

        SessionFinderHolder.setSessionFinder(sessionFinderService);

        useCustomizedSelect2Version();
    }

    /**
     * see https://github.com/devgateway/dg-toolkit/issues/228
     */
    private void useCustomizedSelect2Version() {
        ResourceReference javaScriptReference = new JavaScriptResourceReference(
                BaseStyles.class, "/assets/js/select2/select2.js");
        ResourceReference javaScriptReferenceFull = new JavaScriptResourceReference(
                BaseStyles.class, "/assets/js/select2/select2.full.js");
        ApplicationSettings select2Settings = ApplicationSettings.get();
        select2Settings.setJavaScriptReference(javaScriptReference);
        select2Settings.setJavascriptReferenceFull(javaScriptReferenceFull);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.apache.wicket.authroles.authentication.AuthenticatedWebApplication#
     * getSignInPageClass()
     */
    @Override
    protected Class<? extends WebPage> getSignInPageClass() {
        return LoginPage.class;
    }

}
