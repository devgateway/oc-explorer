package org.devgateway.toolkit.forms.wicket.components.charts;

import java.io.Serializable;

/**
 * @author idobre
 * @since 04/10/2018
 */
public class Polar implements Serializable {
    private final Radialaxis radialaxis;

    public Polar(final PolarBuilder polarBuilder) {
        this.radialaxis = polarBuilder.radialaxis;
    }

    public static class PolarBuilder {
        private Radialaxis radialaxis;

        public PolarBuilder setRadialaxis(final Radialaxis radialaxis) {
            this.radialaxis = radialaxis;
            return this;
        }

        public Polar build() {
            return new Polar(this);
        }
    }
}
