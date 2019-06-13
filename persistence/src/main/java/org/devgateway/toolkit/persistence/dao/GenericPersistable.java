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
package org.devgateway.toolkit.persistence.dao;

import org.springframework.data.jpa.domain.AbstractPersistable;

import java.io.Serializable;

/**
 * @author mpostelnicu
 *
 */
public class GenericPersistable extends AbstractPersistable<Long> implements Serializable {

}
