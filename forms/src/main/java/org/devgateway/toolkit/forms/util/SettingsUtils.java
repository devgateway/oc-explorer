package org.devgateway.toolkit.forms.util;

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
}
