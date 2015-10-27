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
/**
 * 
 */
package org.devgateway.toolkit.persistence.spring;

import java.io.PrintWriter;
import java.net.InetAddress;
import java.sql.Connection;
import java.util.Properties;

import javax.naming.NamingException;

import org.apache.derby.drda.NetworkServerControl;
import org.apache.derby.jdbc.ClientDriver;
import org.apache.log4j.Logger;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.tomcat.jdbc.pool.PoolProperties;
import org.devgateway.toolkit.persistence.dao.AuditorAwareImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.mock.jndi.SimpleNamingContextBuilder;

/**
 * @author mpostelnicu
 *
 */
@Configuration
@EnableJpaAuditing
@PropertySource("classpath:/org/devgateway/toolkit/persistence/application.properties")
public class DatabaseConfiguration {
	
	protected static Logger logger = Logger.getLogger(DatabaseConfiguration.class);
	
	/**
	 * This bean creates the JNDI tree and registers the
	 * {@link javax.sql.DataSource} to this tree. This allows Pentaho Classic
	 * Engine to use a {@link javax.sql.DataSource} ,in our case backed by a
	 * connection pool instead of always opening up JDBC connections. Should
	 * significantly improve performance of all classic reports. In PRD use
	 * connection type=JNDI and name toolkitDS. To use it in PRD you need to add the
	 * configuration to the local PRD. Edit ~/.pentaho/simple-jndi/default.properties
	 * and add the following:
	 * toolkitDS/type=javax.sql.DataSource
	 * toolkitDS/driver=org.apache.derby.jdbc.ClientDriver
	 * toolkitDS/user=app
	 * toolkitDS/password=app
	 * toolkitDS/url=jdbc:derby://localhost//derby/toolkit
	 * 
	 * @return
	 */
	@Bean
	public SimpleNamingContextBuilder jndiBuilder() {
		SimpleNamingContextBuilder builder = new SimpleNamingContextBuilder();
		builder.bind("toolkitDS", dataSource());
		try {
			builder.activate();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return builder;
	}
	
	/**
	 * Creates a {@link javax.sql.DataSource} based on Tomcat {@link DataSource}
	 * @return
	 */
	@Bean
	@DependsOn(value = {"derbyServer","mbeanServer"})
	public DataSource dataSource() {
		PoolProperties pp=new PoolProperties();		
		pp.setJmxEnabled(false);
		pp.setDefaultTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
		pp.setInitialSize(100);
		pp.setMaxWait(60000);
		
		DataSource dataSource = new DataSource(pp);
		
		dataSource.setDriverClassName(ClientDriver.class.getName());
		dataSource.setUrl("jdbc:derby://localhost//derby/toolkit;create=true");
		dataSource.setUsername("app");
		dataSource.setPassword("app");
		return dataSource;
	}

	
	/**
	 * Graciously starts a Derby Database Server when the application starts up
	 * @return
	 * @throws Exception
	 */
	@Bean(destroyMethod = "shutdown")
	public NetworkServerControl derbyServer() throws Exception {
		Properties p = System.getProperties();
		p.put("derby.storage.pageCacheSize", "10000");
		NetworkServerControl nsc = new NetworkServerControl(InetAddress.getByName("localhost"), 1527);
		nsc.start(new PrintWriter(java.lang.System.out, true));
		return nsc;
	}
	

}
