package org.devgateway.ocds.web.util;

import org.devgateway.toolkit.persistence.dao.AdminSettings;
import org.devgateway.toolkit.persistence.repository.AdminSettingsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author idobre
 * @since 6/22/16
 */
@Service
public class SettingsUtils {
    protected static Logger logger = LoggerFactory.getLogger(SettingsUtils.class);

    private static final Integer EXCELBATCHSIZEDEFAULT = 10000;

    @Autowired
    private AdminSettingsRepository adminSettingsRepository;

    private AdminSettings getSettings() {
        List<AdminSettings> list = adminSettingsRepository.findAll();
        if (list.size() == 0) {
            return new AdminSettings();
        } else {
            return list.get(0);
        }
    }

    public Integer getExcelBatchSize() {
        AdminSettings adminSettings = getSettings();
        if (adminSettings.getExcelBatchSize() == null) {
            return EXCELBATCHSIZEDEFAULT;
        }
        return adminSettings.getExcelBatchSize();
    }
}
