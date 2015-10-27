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

import java.math.BigDecimal;

import org.apache.wicket.Application;
import org.apache.wicket.ConverterLocator;
import org.apache.wicket.IConverterLocator;
import org.apache.wicket.Page;
import org.apache.wicket.authroles.authentication.AuthenticatedWebApplication;
import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.devutils.diskstore.DebugDiskDataStore;
import org.apache.wicket.markup.html.IPackageResourceGuard;
import org.apache.wicket.markup.html.SecurePackageResourceGuard;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.request.resource.caching.FilenameWithVersionResourceCachingStrategy;
import org.apache.wicket.request.resource.caching.NoOpResourceCachingStrategy;
import org.apache.wicket.request.resource.caching.version.CachingResourceVersion;
import org.apache.wicket.serialize.java.DeflatedJavaSerializer;
import org.apache.wicket.settings.IRequestCycleSettings;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;
import org.devgateway.toolkit.forms.service.SessionFinderService;
import org.devgateway.toolkit.forms.wicket.converters.NonNumericFilteredBigDecimalConverter;
import org.devgateway.toolkit.forms.wicket.page.BasePage;
import org.devgateway.toolkit.forms.wicket.page.Homepage;
import org.devgateway.toolkit.forms.wicket.page.user.LoginPage;
import org.devgateway.toolkit.forms.wicket.styles.EmptyCss;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.wicketstuff.annotation.scan.AnnotatedMountScanner;

import com.google.javascript.jscomp.CompilationLevel;

import de.agilecoders.wicket.core.Bootstrap;
import de.agilecoders.wicket.core.markup.html.RenderJavaScriptToFooterHeaderResponseDecorator;
import de.agilecoders.wicket.core.request.resource.caching.version.Adler32ResourceVersion;
import de.agilecoders.wicket.core.settings.BootstrapSettings;
import de.agilecoders.wicket.core.settings.IBootstrapSettings;
import de.agilecoders.wicket.extensions.javascript.GoogleClosureJavaScriptCompressor;
import de.agilecoders.wicket.extensions.javascript.YuiCssCompressor;
import de.agilecoders.wicket.less.BootstrapLess;
import de.agilecoders.wicket.webjars.WicketWebjars;
import nl.dries.wicket.hibernate.dozer.SessionFinderHolder;

/**
 * The web application class also serves as spring boot starting point by using
 * spring boot's EnableAutoConfiguration annotation and providing the main
 * method.
 *
 * @author Stefan Kloe, mpostelnicu
 *
 */
@EnableScheduling
@SpringBootApplication
@ComponentScan("org.devgateway.toolkit")
@PropertySource("classpath:/org/devgateway/toolkit/forms/application.properties")
public class FormsWebApplication extends AuthenticatedWebApplication {

	private static final String BASE_PACKAGE_FOR_PAGES = BasePage.class.getPackage().getName();

	@Autowired
	private ApplicationContext applicationContext;

	@Autowired
	private SessionFinderService sessionFinderService;

	public static void main(String[] args) {
		SpringApplication.run(FormsWebApplication.class, args);
	}

	/**
	 * @see org.apache.wicket.Application#newConverterLocator() This adds the
	 *      {@link NonNumericFilteredBigDecimalConverter} as the standard
	 *      {@link BigDecimal} converter for ALL fields using this type accross
	 *      the application
	 **/
	@Override
	protected IConverterLocator newConverterLocator() {
		ConverterLocator locator = (ConverterLocator) super.newConverterLocator();
		locator.set(BigDecimal.class, new NonNumericFilteredBigDecimalConverter());
		return locator;
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
		final IBootstrapSettings settings = new BootstrapSettings();
		// specify an empty bootstrap css resource so that we can have more
		// control when do we load the bootstrap styles.
		// By default all pages will load bootstrap.css file and there are
		// situations (like print page) when we don't need this styles.
		// The boostrap.css file is loaded as dependency in MainCss Instance
		settings.setCssResourceReference(EmptyCss.INSTANCE);

		WicketWebjars.install(this);

		// use the default bootstrap theme
		Bootstrap.install(this, settings);
		BootstrapLess.install(this);
	}

	/**
	 * optimize wicket for a better web performance
	 */
	private void optimizeForWebPerformance() {
		// add javascript files at the bottom of the page
		setHeaderResponseDecorator(new RenderJavaScriptToFooterHeaderResponseDecorator("scripts-bucket"));

		// This is only enabled for deployment configuration
		// -Dwicket.configuration=deployment
		// The default is Development, so this code is not used
		if (usesDeploymentConfig()) {
			getResourceSettings().setCachingStrategy(new FilenameWithVersionResourceCachingStrategy("-v-",
					new CachingResourceVersion(new Adler32ResourceVersion())));

			getResourceSettings().setJavaScriptCompressor(
					new GoogleClosureJavaScriptCompressor(CompilationLevel.SIMPLE_OPTIMIZATIONS));
			getResourceSettings().setCssCompressor(new YuiCssCompressor());

			getFrameworkSettings().setSerializer(new DeflatedJavaSerializer(getApplicationKey()));
		} else {
			getResourceSettings().setCachingStrategy(new NoOpResourceCachingStrategy());
		}

		getRequestCycleSettings().setRenderStrategy(IRequestCycleSettings.RenderStrategy.ONE_PASS_RENDER);
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
		}

		getComponentInstantiationListeners().add(new SpringComponentInjector(this, applicationContext));

		new AnnotatedMountScanner().scanPackage(BASE_PACKAGE_FOR_PAGES).mount(this);

		getApplicationSettings().setUploadProgressUpdatesEnabled(true);

		getApplicationSettings().setAccessDeniedPage(Homepage.class);

		// deactivate ajax debug mode
		// getDebugSettings().setAjaxDebugModeEnabled(false);

		configureBootstrap();
		optimizeForWebPerformance();

		// watch this using the URL
		// http://.../wicket/internal/debug/diskDataStore		
		if(usesDevelopmentConfig())
			DebugDiskDataStore.register(this);

		SessionFinderHolder.setSessionFinder(sessionFinderService);
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
