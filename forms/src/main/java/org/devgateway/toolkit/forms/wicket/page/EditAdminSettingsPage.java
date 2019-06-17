package org.devgateway.toolkit.forms.wicket.page;


import org.springframework.cache.CacheManager;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxFallbackLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.validator.RangeValidator;
import org.devgateway.toolkit.forms.wicket.components.form.CheckBoxToggleBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.form.TextFieldBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.page.edit.AbstractEditPage;
import org.devgateway.toolkit.persistence.dao.AdminSettings;
import org.devgateway.toolkit.persistence.service.AdminSettingsService;
import org.devgateway.toolkit.persistence.repository.AdminSettingsRepository;
import org.devgateway.toolkit.web.security.SecurityConstants;
import org.wicketstuff.annotation.mount.MountPath;

import java.util.List;
import java.util.Optional;

/**
 * @author idobre
 * @since 6/22/16
 */
@AuthorizeInstantiation(SecurityConstants.Roles.ROLE_ADMIN)
@MountPath(value = "/adminsettings")
public class EditAdminSettingsPage extends AbstractEditPage<AdminSettings> {

    private static final long serialVersionUID = 5742724046825803877L;

    private TextFieldBootstrapFormComponent<Integer> excelBatchSize;

    private CheckBoxToggleBootstrapFormComponent rebootServer;

    private CheckBoxToggleBootstrapFormComponent disableApiSecurity;

    private TextFieldBootstrapFormComponent<String> adminEmail;

    private CheckBoxToggleBootstrapFormComponent enableDailyAutomatedImport;

    private TextFieldBootstrapFormComponent<String> importFilesPath;

    @SpringBean
    private CacheManager cacheManager;

    @SpringBean
    private AdminSettingsService adminSettingsService;

    public EditAdminSettingsPage(final PageParameters parameters) {
        super(parameters);

        this.jpaService = adminSettingsService;
        this.listPageClass = Homepage.class;

        if (entityId == null) {
            final List<AdminSettings> listSettings = adminSettingsService.findAll();
            // just keep 1 entry for settings
            if (listSettings.size() == 1) {
                entityId = listSettings.get(0).getId();
            }
        }
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        editForm.add(new Label("excelTitle", new StringResourceModel("excelTitle", this, null)));

        excelBatchSize = new TextFieldBootstrapFormComponent<>("excelBatchSize");
        excelBatchSize.integer();
        excelBatchSize.getField().add(new RangeValidator(1, 10000));
        excelBatchSize.required();
        editForm.add(excelBatchSize);

        editForm.add(new Label("systemTitle", new StringResourceModel("systemTitle", this, null)));

//        rebootServer = new CheckBoxToggleBootstrapFormComponent("rebootServer");
//        editForm.add(rebootServer);

        disableApiSecurity = new CheckBoxToggleBootstrapFormComponent("disableApiSecurity");
        editForm.add(disableApiSecurity);

        enableDailyAutomatedImport = new CheckBoxToggleBootstrapFormComponent("enableDailyAutomatedImport");
        editForm.add(enableDailyAutomatedImport);


        adminEmail = new TextFieldBootstrapFormComponent<>("adminEmail");
        editForm.add(adminEmail);

        importFilesPath = new TextFieldBootstrapFormComponent<>("importFilesPath");
        editForm.add(importFilesPath);

        addCacheClearLink();

    }

    private void addCacheClearLink() {
        IndicatingAjaxFallbackLink link = new IndicatingAjaxFallbackLink<Void>("clearCache") {

            @Override
            public void onClick(Optional optional) {
                cacheManager.getCacheNames().forEach(c -> cacheManager.getCache(c).clear());
            }

        };
        editForm.add(link);
    }
}
