package org.devgateway.toolkit.forms.wicket.components.charts;

import java.io.Serializable;

/**
 * @author idobre
 * @since 29/10/2017
 */
public class Legend implements Serializable {
    private final String orientation;

    public Legend(final LegendBuilder legendBuilder) {
        this.orientation = legendBuilder.orientation;
    }

    public static class LegendBuilder {
        private String orientation;

        public LegendBuilder setOrientation(final String orientation) {
            this.orientation = orientation;
            return this;
        }

        public Legend build() {
            return new Legend(this);
        }
    }
}
