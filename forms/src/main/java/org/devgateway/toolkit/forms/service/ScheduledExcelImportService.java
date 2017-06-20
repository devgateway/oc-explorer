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
package org.devgateway.toolkit.forms.service;

import org.apache.commons.lang.BooleanUtils;
import org.apache.log4j.Logger;
import org.devgateway.ocds.persistence.mongo.spring.ExcelImportService;
import org.devgateway.ocds.web.util.SettingsUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class ScheduledExcelImportService {

    private static final Logger LOGGER = Logger.getLogger(ScheduledExcelImportService.class);

    @Autowired
    private ExcelImportService excelImportService;

    @Autowired
    private SettingsUtils settingsUtils;

    @Scheduled(cron = "0 0 3 * * ?")
    public void excelImportService() {

        if (BooleanUtils.isFalse(settingsUtils.getSettings().getEnableDailyAutomatedImport())) {
            return;
        }

        //       excelImportService=excelImportService.importAllSheets()


    }


}