package org.devgateway.toolkit.forms.wicket.components.charts;

import java.io.Serializable;

/**
 * @author idobre
 * @since 2018-12-04
 */
public class Colorbar implements Serializable {
    private final String tickprefix;

    private final String title;

    private final Integer thickness;

    private final Number x;

    private final Number y;

    public Colorbar(final ColorbarBuilder colorbarBuilder) {
        this.tickprefix = colorbarBuilder.tickprefix;
        this.title = colorbarBuilder.title;
        this.thickness = colorbarBuilder.thickness;
        this.x = colorbarBuilder.x;
        this.y = colorbarBuilder.y;
    }

    public static class ColorbarBuilder {
        private String tickprefix;

        private String title;

        private Integer thickness;

        private Number x;

        private Number y;

        public ColorbarBuilder setTickprefix(final String tickprefix) {
            this.tickprefix = tickprefix;
            return this;
        }

        public ColorbarBuilder setTitle(final String title) {
            this.title = title;
            return this;
        }

        public ColorbarBuilder setThickness(final Integer thickness) {
            this.thickness = thickness;
            return this;
        }

        public ColorbarBuilder setX(final Number x) {
            this.x = x;
            return this;
        }

        public ColorbarBuilder setY(final Number y) {
            this.y = y;
            return this;
        }

        public Colorbar build() {
            return new Colorbar(this);
        }
    }
}
