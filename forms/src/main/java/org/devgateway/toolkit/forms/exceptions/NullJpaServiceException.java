/**
 * Copyright (c) 2015 Development Gateway, Inc and others.
 * <p>
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the MIT License (MIT)
 * which accompanies this distribution, and is available at
 * https://opensource.org/licenses/MIT
 * <p>
 * Contributors:
 * Development Gateway - initial API and implementation
 */
/**
 *
 */
package org.devgateway.toolkit.forms.exceptions;

/**
 * @author mpostelnicu
 */
public class NullJpaServiceException extends RuntimeException {
    private static final long serialVersionUID = -2779145093766288562L;

    public NullJpaServiceException() {
        super("jpaService is null! Please set the jpaService in your constructor");
    }

}
