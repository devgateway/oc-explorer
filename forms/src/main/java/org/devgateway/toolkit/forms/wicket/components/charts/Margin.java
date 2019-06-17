package org.devgateway.toolkit.forms.wicket.components.charts;

import java.io.Serializable;

/**
 * @author idobre
 * @since 03/11/2017
 */
public class Margin implements Serializable {
    private final Number l;

    private final Number r;

    private final Number b;

    private final Number t;

    public Margin(final MarginBuilder marginBuilder) {
        this.l = marginBuilder.l;
        this.r = marginBuilder.r;
        this.b = marginBuilder.b;
        this.t = marginBuilder.t;
    }


    public static class MarginBuilder {
        private Number l;

        private Number r;

        private Number b;

        private Number t;

        public MarginBuilder setL(final Number l) {
            this.l = l;
            return this;
        }

        public MarginBuilder setR(final Number r) {
            this.r = r;
            return this;
        }

        public MarginBuilder setB(final Number b) {
            this.b = b;
            return this;
        }

        public MarginBuilder setT(final Number t) {
            this.t = t;
            return this;
        }

        public Margin build() {
            return new Margin(this);
        }
    }
}
