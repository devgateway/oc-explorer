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
package org.devgateway.toolkit.reporting;

import java.io.File;
import java.io.IOException;
import java.util.Random;

import org.pentaho.reporting.engine.classic.core.ClassicEngineBoot;
import org.pentaho.reporting.libraries.base.config.Configuration;

/**
 * @author mpostelnicu
 *
 */
public final class ReportUtil {

    private static final int TIMES_PRD_TRIES_AN_UNUSED_KEY = 200;

    private ReportUtil() {

    }

    /**
     * @param directoryName
     * @return
     * @throws IOException
     * see https://github.com/pentaho/pentaho-reporting/blob/master/designer/
     *      report-designer/src/org/pentaho/reporting/designer/core/actions/
     *      report/preview/PreviewHtmlAction.java
     */
    public static File createTemporaryDirectory(final String directoryName) throws IOException {
        final Configuration configuration = ClassicEngineBoot.getInstance().getGlobalConfig();
        final String s = configuration.getConfigProperty("java.io.tmpdir"); // NON-NLS
        final File tempDir = new File(s);
        if (!tempDir.exists()) {
            tempDir.mkdirs();
        }
        if (!tempDir.exists() || !tempDir.isDirectory()) {
            throw new IOException("Unable to access or create the temp-directory");
        }
        if (!tempDir.canWrite()) {
            throw new IOException("Unable to write to temp-directory.");
        }

        final Random randomGenerator = new Random(System.currentTimeMillis());
        for (int i = 1; i < TIMES_PRD_TRIES_AN_UNUSED_KEY; i++) {
            final int random = (randomGenerator.nextInt());
            final File reportDirectory = new File(s, directoryName + random);

            if (reportDirectory.exists() && !reportDirectory.isDirectory()) {
                continue;
            }
            if (!reportDirectory.exists() && !reportDirectory.mkdirs()) {
                continue;
            }

            reportDirectory.deleteOnExit();
            return reportDirectory;
        }

        throw new IOException("Unable to generate the target directory.");
    }

}
