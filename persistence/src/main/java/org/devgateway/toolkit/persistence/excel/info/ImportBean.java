package org.devgateway.toolkit.persistence.excel.info;

import org.devgateway.toolkit.persistence.dao.categories.Category;

/**
 * @author idobre
 * @since 18/04/2018
 *
 * Additional data that can be used in the import process.
 */
public class ImportBean {
    private final Category category;

    public ImportBean(final Category category) {
        this.category = category;
    }

    public Category getCategory() {
        return category;
    }
}
