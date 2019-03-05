package org.devgateway.toolkit.forms.wicket.components.charts;

import java.io.Serializable;
import java.util.List;

/**
 * @author idobre
 * @since 04/10/2018
 */
public class Radialaxis implements Serializable {
    private final Boolean visible;

    private final Boolean autorange;

    private final List<Number> range;

    public Radialaxis(final RadialaxisBuilder radialaxisBuilder) {
        this.visible = radialaxisBuilder.visible;
        this.autorange = radialaxisBuilder.autorange;
        this.range = radialaxisBuilder.range;
    }

    public static class RadialaxisBuilder {
        private Boolean visible;

        private Boolean autorange;

        private List<Number> range;

        public RadialaxisBuilder setVisible(final Boolean visible) {
            this.visible = visible;
            return this;
        }

        public RadialaxisBuilder setAutorange(final Boolean autorange) {
            this.autorange = autorange;
            return this;
        }

        public RadialaxisBuilder setRange(final List<Number> range) {
            this.range = range;
            return this;
        }

        public Radialaxis build() {
            return new Radialaxis(this);
        }
    }
}
