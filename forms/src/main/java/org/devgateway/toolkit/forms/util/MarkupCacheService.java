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
package org.devgateway.toolkit.forms.util;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import org.apache.wicket.markup.Markup;
import org.apache.wicket.markup.MarkupCache;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * @author idobre
 * @since 3/3/16
 * <p>
 * Class the removes the cache created in
 * org.devgateway.ccrs.web.wicket.page.reports.AbstractReportPage#ResourceStreamPanel#getCacheKey
 * function
 */
@Component
public class MarkupCacheService {
    protected static final Logger logger = LoggerFactory.getLogger(MarkupCacheService.class);

    /**
     * start-key used to identify the reports markup
     */
    private static final String START_NAME_REPORT_KEY = "REPORTMARKUP";

    /**
     * Flush markup cache for reports page
     */
    public final void flushMarkupCache() {
        final MarkupCache markupCacheClass = (MarkupCache) MarkupCache.get();
        final MarkupCache.ICache<String, Markup> markupCache = markupCacheClass.getMarkupCache();
        final Collection<String> keys = markupCache.getKeys();
        for (String key : keys) {
            // The key for reports markup cache contains the class name so it
            // should end in "ReportPage"
            if (key.startsWith(START_NAME_REPORT_KEY)) {
                markupCacheClass.removeMarkup(key);
            }
        }
    }

    /**
     * Add the content of a report (PDF, Excel, RTF) to cache
     *
     * @param outputType
     * @param reportName
     * @param parameters
     * @param buffer
     */
    public void addPentahoReportToCache(final String outputType, final String reportName, final String parameters,
                                        final byte[] buffer) {
        final CacheManager cm = CacheManager.getInstance();

        // get the reports cache "reportsCache", declared in ehcache.xml
        final Cache cache = cm.getCache("reportsCache");

        cache.put(new Element(createCacheKey(outputType, reportName, parameters), buffer));
    }

    /**
     * Fetch the content of a report from cache
     *
     * @param outputType
     * @param reportName
     * @param parameters
     * @return
     */
    public byte[] getPentahoReportFromCache(final String outputType, final String reportName, final String parameters) {
        final CacheManager cm = CacheManager.getInstance();

        // get the reports cache "reportsCache", declared in ehcache.xml
        final Cache cache = cm.getCache("reportsCache");

        final String key = createCacheKey(outputType, reportName, parameters);

        if (cache.isKeyInCache(key)) {
            return (byte[]) cache.get(key).getObjectValue();
        }

        return null;
    }

    /**
     * Remove from cache all reports content
     */
    public void clearPentahoReportsCache() {
        final CacheManager cm = CacheManager.getInstance();

        // get the reports cache "reportsCache", declared in ehcache.xml
        final Cache cache = cm.getCache("reportsCache");

        if (cache != null) {
            cache.removeAll();
        }
    }

    /**
     * Remove from cache all APIs/Services content.
     */
    public void clearAllCaches() {
        final CacheManager cm = CacheManager.getInstance();

        // get the reports cache "reportsApiCache", declared in ehcache.xml
        final Cache cache = cm.getCache("reportsApiCache");
        if (cache != null) {
            cache.removeAll();
        }

        // get the reports cache "excelExportCache", declared in ehcache.xml
        final Cache excelExportCache = cm.getCache("excelExportCache");
        if (excelExportCache != null) {
            excelExportCache.removeAll();
        }

        // get the reports cache "servicesCache", declared in ehcache.xml
        final Cache servicesCache = cm.getCache("servicesCache");
        if (servicesCache != null) {
            servicesCache.removeAll();
        }
    }

    private String createCacheKey(final String outputType, final String reportName, final String parameters) {
        return reportName + "-" + parameters + "-" + outputType;
    }
}
