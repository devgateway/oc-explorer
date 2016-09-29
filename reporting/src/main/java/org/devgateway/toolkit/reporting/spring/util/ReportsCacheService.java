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
package org.devgateway.toolkit.reporting.spring.util;

import org.pentaho.reporting.engine.classic.core.cache.DataCacheFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * Component for accessing and clearing the pentaho caches
 * 
 * @author mpostelnicu
 *
 */
@Component
@Profile("reports")
public class ReportsCacheService {

    // @Autowired
    // private DataSource dataSource;

    /**
     * Flush mondrian and reports classic cache
     */
    public void flushCache() {
        flushReportsClassicCache();

        // use this if you have Mondrian enabled
        // flushMondrianCache();
    }

    public void flushReportsClassicCache() {
        DataCacheFactory.getCache().getCacheManager().clearAll();
    }

    public void flushMondrianCache() {
        // Util.PropertyList propertyList = new Util.PropertyList();
        // propertyList.put("Provider", "mondrian");

        // ClassLoader classloader = this.getClass().getClassLoader();
        // URL mondrianCubeFileURL =
        // classloader.getResource(MondrianConstants.CATALOG_FILE);

        // propertyList.put("Catalog", "res:" + MondrianConstants.CATALOG_FILE);
        //
        // Connection connection =
        // mondrian.olap.DriverManager.getConnection(propertyList, null,
        // dataSource);
        //
        // CacheControl cacheControl = connection.getCacheControl(null);
        // for (Cube cube : connection.getSchema().getCubes()) {
        // CellRegion createMeasuresRegion =
        // cacheControl.createMeasuresRegion(cube);
        // cacheControl.flush(createMeasuresRegion);
        // }
        // cacheControl.flushSchemaCache();

        // connection.close();
    }

}