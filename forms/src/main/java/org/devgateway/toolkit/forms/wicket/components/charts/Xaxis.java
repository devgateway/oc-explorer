package org.devgateway.toolkit.forms.wicket.components.charts;

import java.io.Serializable;
import java.util.List;

/**
 * @author idobre
 * @since 4/21/17
 */
public class Xaxis implements Serializable {
    private final String title;

    private final Font titlefont;

    private final Font tickfont;

    private final Integer tickangle;

    private final Boolean zeroline;

    private final Integer gridwidth;

    private final Boolean autorange;

    private final String rangemode;

    private final List<Number> range;

    private final Number dtick;

    private final String type;

    private final String tickprefix;

    public Xaxis(final XaxisBuilder xaxisBuilder) {
        this.title = xaxisBuilder.title;
        this.titlefont = xaxisBuilder.titlefont;
        this.tickfont = xaxisBuilder.tickfont;
        this.tickangle = xaxisBuilder.tickangle;
        this.zeroline = xaxisBuilder.zeroline;
        this.gridwidth = xaxisBuilder.gridwidth;
        this.autorange = xaxisBuilder.autorange;
        this.rangemode = xaxisBuilder.rangemode;
        this.range = xaxisBuilder.range;
        this.dtick = xaxisBuilder.dtick;
        this.type = xaxisBuilder.type;
        this.tickprefix = xaxisBuilder.tickprefix;
    }

    public static class XaxisBuilder {
        private String title;

        private Font titlefont;

        private Font tickfont;

        private Integer tickangle;

        private Boolean zeroline;

        private Integer gridwidth;

        private Boolean autorange;

        private String rangemode;

        private List<Number> range;

        private Number dtick;

        private String type;

        private String tickprefix;

        public XaxisBuilder setTitle(final String title) {
            this.title = title;
            return this;
        }

        public XaxisBuilder setTitlefont(final Font titlefont) {
            this.titlefont = titlefont;
            return this;
        }

        public XaxisBuilder setTickfont(final Font tickfont) {
            this.tickfont = tickfont;
            return this;
        }

        public XaxisBuilder setTickangle(final Integer tickangle) {
            this.tickangle = tickangle;
            return this;
        }

        public XaxisBuilder setZeroline(final Boolean zeroline) {
            this.zeroline = zeroline;
            return this;
        }

        public XaxisBuilder setGridwidth(final Integer gridwidth) {
            this.gridwidth = gridwidth;
            return this;
        }

        public XaxisBuilder setAutorange(final Boolean autorange) {
            this.autorange = autorange;
            return this;
        }

        public XaxisBuilder setRangemode(final String rangemode) {
            this.rangemode = rangemode;
            return this;
        }

        public XaxisBuilder setRange(final List<Number> range) {
            this.range = range;
            return this;
        }

        public XaxisBuilder setDtick(final Number dtick) {
            this.dtick = dtick;
            return this;
        }

        public XaxisBuilder setType(final String type) {
            this.type = type;
            return this;
        }

        public XaxisBuilder setTickprefix(final String tickprefix) {
            this.tickprefix = tickprefix;
            return this;
        }

        public Xaxis build() {
            return new Xaxis(this);
        }
    }
}
