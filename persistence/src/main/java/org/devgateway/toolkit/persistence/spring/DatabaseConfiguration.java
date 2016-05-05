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
import java.util.Properties;

import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.derby.drda.NetworkServerControl;
import org.apache.derby.jdbc.ClientDriver40;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.mock.jndi.SimpleNamingContextBuilder;

import com.zaxxer.hikari.HikariDataSource;

/**
 * @author mpostelnicu
 *
 */
@Configuration
@EnableJpaAuditing
@PropertySource("classpath:/org/devgateway/toolkit/persistence/application.properties")
@Profile("!integration")
public class DatabaseConfiguration {

	private static final int DERBY_PORT = 1527;
	protected static Logger logger = Logger.getLogger(DatabaseConfiguration.class);

	/**
	 * This bean creates the JNDI tree and registers the
	 * {@link javax.sql.DataSource} to this tree. This allows Pentaho Classic
	 * Engine to use a {@link javax.sql.DataSource} ,in our case backed by a
	 * connection pool instead of always opening up JDBC connections. Should
	 * significantly improve performance of all classic reports. In PRD use
	 * connection type=JNDI and name toolkitDS. To use it in PRD you need to add
	 * the configuration to the local PRD. Edit
	 * ~/.pentaho/simple-jndi/default.properties and add the following:
	 * toolkitDS/type=javax.sql.DataSource
	 * toolkitDS/driver=org.apache.derby.jdbc.ClientDriver toolkitDS/user=app
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
	 * 
	 * @return
	 */
	@Bean
	@DependsOn(value = { "derbyServer", "mbeanServer" })
	public DataSource dataSource() {
		HikariDataSource ds = new HikariDataSource();
		ds.setTransactionIsolation("TRANSACTION_READ_COMMITTED");

		ds.setJdbcUrl("jdbc:derby://localhost//derby/toolkit;create=true");
		ds.setUsername("app");
		ds.setPassword("app");
		ds.setDriverClassName(ClientDriver40.class.getName());

		return ds;
	}

	/**
	 * Graciously starts a Derby Database Server when the application starts up
	 * 
	 * @return
	 * @throws Exception
	 */
	@Bean(destroyMethod = "shutdown")
	public NetworkServerControl derbyServer() throws Exception {
		Properties p = System.getProperties();
		p.put("derby.storage.pageCacheSize", "30000");
		p.put("derby.language.maxMemoryPerTable", "20000");
		NetworkServerControl nsc = new NetworkServerControl(InetAddress.getByName("localhost"), DERBY_PORT);
		nsc.start(new PrintWriter(java.lang.System.out, true));
		return nsc;
	}

}
