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
package org.devgateway.toolkit.forms.wicket.styles;

import java.util.List;

import org.apache.wicket.markup.head.HeaderItem;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.apache.wicket.resource.JQueryResourceReference;

import com.google.common.collect.Lists;

/**
 * @author idobre
 * @since 1/29/15
 */
public class BlockUiJavaScript extends JavaScriptResourceReference {
    private static final long serialVersionUID = 1L;

    public static final BlockUiJavaScript INSTANCE = new BlockUiJavaScript();

    /**
     * Construct.
     */
    public BlockUiJavaScript() {
        super(BlockUiJavaScript.class, "/assets/js/block-ui.js");
    }

    @Override
    public List<HeaderItem> getDependencies() {
        final List<HeaderItem> dependencies = Lists.newArrayList(super.getDependencies());

        dependencies.add(JavaScriptHeaderItem.forReference(JQueryResourceReference.getV2()));
        dependencies.add(JavaScriptHeaderItem
                .forReference(new JavaScriptResourceReference(EmptyCss.class, "/assets/js/jquery.blockUI.js")));

        return dependencies;
    }
}
