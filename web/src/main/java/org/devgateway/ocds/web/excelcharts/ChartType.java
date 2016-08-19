package org.devgateway.ocds.web.excelcharts;

/**
 * @author idobre
 * @since 8/16/16
 */

public enum ChartType {
    bar("bar"),

    barcol("barcol"),

    area("area"),

    line("line"),

    pie("pie"),

    scatter("scatter"),

    stacked("stacked"),

    stackedpercentage("stackedpercentage"),

    bubble("bubble");

    private final String value;

    ChartType(final String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }
}
