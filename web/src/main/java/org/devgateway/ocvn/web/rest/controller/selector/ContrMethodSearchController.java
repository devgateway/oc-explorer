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
package org.devgateway.ocvn.web.rest.controller.selector;

import java.util.List;

import org.devgateway.ocvn.web.rest.controller.GenericOcvnController;
import org.devgateway.toolkit.persistence.mongo.dao.ContrMethod;
import org.devgateway.toolkit.persistence.mongo.repository.ContrMethodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.aggregation.Fields;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 
 * @author mpostelnicu
 * 
 */
@RestController
public class ContrMethodSearchController extends GenericOcvnController {

	@Autowired
	private ContrMethodRepository contrMethodRepository;

	@RequestMapping("/api/ocds/contrMethod/all")
	public List<ContrMethod> contrMethods() {

		return contrMethodRepository.findAll(new Sort(Direction.ASC, Fields.UNDERSCORE_ID));

	}

}