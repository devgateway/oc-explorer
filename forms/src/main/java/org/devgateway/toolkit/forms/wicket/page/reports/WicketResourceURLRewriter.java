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
package org.devgateway.toolkit.forms.wicket.page.reports;

import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.SharedResourceReference;
import org.devgateway.toolkit.forms.util.FolderContentResource;
import org.pentaho.reporting.engine.classic.core.modules.output.table.html.URLRewriteException;
import org.pentaho.reporting.engine.classic.core.modules.output.table.html.URLRewriter;
import org.pentaho.reporting.libraries.repository.ContentEntity;

/**
 * @author mpostelnicu This {@link URLRewriter} will translate local folder
 *         resources into wicket encoded resources using the
 *         {@link SharedResourceReference} to {@link FolderContentResource}
 */
public class WicketResourceURLRewriter implements URLRewriter {

    private SharedResourceReference folderResourceReference;

    /*
     * (non-Javadoc)
     * 
     * @see org.pentaho.reporting.engine.classic.core.modules.output.table.html.
     * URLRewriter
     * #rewrite(org.pentaho.reporting.libraries.repository.ContentEntity,
     * org.pentaho.reporting.libraries.repository.ContentEntity)
     */
    public WicketResourceURLRewriter(final SharedResourceReference folderResourceReference) {

        this.folderResourceReference = folderResourceReference;
    }

    @Override
    public String rewrite(final ContentEntity sourceDocument, final ContentEntity dataEntity)
            throws URLRewriteException {
        PageParameters parameters = new PageParameters();
        parameters.add(FolderContentResource.PARAM_FILE_NAME, dataEntity.getName());
        return RequestCycle.get().urlFor(folderResourceReference, parameters).toString();
    }
}
