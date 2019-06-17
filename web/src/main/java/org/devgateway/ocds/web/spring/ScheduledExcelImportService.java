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
package org.devgateway.ocds.web.spring;

import org.apache.commons.lang.BooleanUtils;
import org.devgateway.ocds.persistence.mongo.spring.ExcelImportService;
import org.devgateway.ocds.persistence.mongo.spring.ImportResult;
import org.devgateway.ocds.web.util.SettingsUtils;
import org.devgateway.toolkit.persistence.dao.AdminSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

//@Service
public class ScheduledExcelImportService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduledExcelImportService.class);

    @Autowired
    private ExcelImportService excelImportService;

    @Autowired
    private SendEmailService sendEmailService;

    @Autowired
    private SettingsUtils settingsUtils;

    //@Scheduled(cron = "0 0 3 * * ?")
    public void excelImportService() {

        AdminSettings settings = settingsUtils.getSettings();

        if (BooleanUtils.isFalse(settings.getEnableDailyAutomatedImport())) {
            return;
        }

        ImportResult result = null;
        //result = excelImportService.importAllSheets();

        if (!result.getSuccess()) {
            sendEmailService.sendEmail("Excel import failed!", result.getMsgBuffer().toString(),
                    settings.getAdminEmail());
        }
    }
}