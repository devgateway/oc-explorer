package org.devgateway.toolkit.forms.wicket.components.charts;

import java.io.Serializable;

/**
 * @author idobre
 * @since 29/10/2017
 */
public class Legend implements Serializable {
    private final String orientation;

    private final Number x;

    private final Number y;

    private final Number borderwidth;

    private final Font font;

    private final String traceorder;

    public Legend(final LegendBuilder legendBuilder) {
        this.orientation = legendBuilder.orientation;
        this.x = legendBuilder.x;
        this.y = legendBuilder.y;
        this.borderwidth = legendBuilder.borderwidth;
        this.font = legendBuilder.font;
        this.traceorder = legendBuilder.traceorder;
    }

    public static class LegendBuilder {
        private String orientation;

        private Number x;

        private Number y;

        private Number borderwidth;

        private Font font;

        private String traceorder;

        public LegendBuilder setOrientation(final String orientation) {
            this.orientation = orientation;
            return this;
        }

        public LegendBuilder setX(final Number x) {
            this.x = x;
            return this;
        }

        public LegendBuilder setY(final Number y) {
            this.y = y;
            return this;
        }

        public LegendBuilder setBorderwidth(final Number borderwidth) {
            this.borderwidth = borderwidth;
            return this;
        }

        public LegendBuilder setFont(final Font font) {
            this.font = font;
            return this;
        }

        public LegendBuilder setTraceorder(final String traceorder) {
            this.traceorder = traceorder;
            return this;
        }

        public Legend build() {
            return new Legend(this);
        }
    }
}
