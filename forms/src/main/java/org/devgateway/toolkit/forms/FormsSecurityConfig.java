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
package org.devgateway.toolkit.forms;

import org.devgateway.toolkit.forms.service.CustomJPAUserDetailsService;
import org.devgateway.toolkit.web.spring.WebSecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.RememberMeAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;

@Configuration
@EnableWebSecurity
@Order(1)
public class FormsSecurityConfig extends WebSecurityConfig {
	
	/**
	 * Remember me key for {@link TokenBasedRememberMeServices}
	 */
	private static final String UNIQUE_SECRET_REMEMBER_ME_KEY="secret";

	@Autowired
	protected CustomJPAUserDetailsService customJPAUserDetailsService;

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		StandardPasswordEncoder spe=new StandardPasswordEncoder();
		auth.userDetailsService(customJPAUserDetailsService).passwordEncoder(spe);
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		super.configure(web);
		web.ignoring().antMatchers("/img/**", "/css*/**", "/js*/**",
				"/assets*/**", "/wicket/resource/**/*.js",
				"/wicket/resource/**/*.css", "/wicket/resource/**/*.png",
				"/wicket/resource/**/*.jpg","/wicket/resource/**/*.gif","/login/**","/resources/**", "/resources/public/**");
	}
	
	/**
	 * This bean defines the same key in the {@link RememberMeAuthenticationProvider}
	 * @return
	 */
	@Bean()
	public AuthenticationProvider rememberMeAuthenticationProvider() {
		return new RememberMeAuthenticationProvider(UNIQUE_SECRET_REMEMBER_ME_KEY);
	}

	/**
	 * This bean configures the {@link TokenBasedRememberMeServices} with {@link CustomJPAUserDetailsService}
	 * @return
	 */
	@Bean()
	public TokenBasedRememberMeServices rememberMeServices() {
		TokenBasedRememberMeServices rememberMeServices = new TokenBasedRememberMeServices(
				UNIQUE_SECRET_REMEMBER_ME_KEY, customJPAUserDetailsService);
		rememberMeServices.setAlwaysRemember(true);
		return rememberMeServices;
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		super.configure(http);
		http.anonymous().disable().sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.NEVER).and().csrf().disable();
		
		//we enable http rememberMe cookie for autologin
		http.rememberMe().key(UNIQUE_SECRET_REMEMBER_ME_KEY);

		// resolved the error Refused to display * in a frame because it set 'X-Frame-Options' to 'DENY'.
		http.headers()
				.contentTypeOptions()
				.xssProtection()
				.cacheControl()
				.httpStrictTransportSecurity()
				.addHeaderWriter(new XFrameOptionsHeaderWriter(XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN));
	}
}