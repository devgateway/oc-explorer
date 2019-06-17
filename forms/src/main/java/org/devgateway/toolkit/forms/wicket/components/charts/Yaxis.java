package org.devgateway.toolkit.forms.wicket.components.charts;

import java.io.Serializable;
import java.util.List;

/**
 * @author idobre
 * @since 4/21/17
 */
public class Yaxis implements Serializable {
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

    private final String ticksuffix;

    public Yaxis(final YaxisBuilder yaxisBuilder) {
        this.title = yaxisBuilder.title;
        this.titlefont = yaxisBuilder.titlefont;
        this.tickfont = yaxisBuilder.tickfont;
        this.tickangle = yaxisBuilder.tickangle;
        this.zeroline = yaxisBuilder.zeroline;
        this.gridwidth = yaxisBuilder.gridwidth;
        this.autorange = yaxisBuilder.autorange;
        this.rangemode = yaxisBuilder.rangemode;
        this.range = yaxisBuilder.range;
        this.dtick = yaxisBuilder.dtick;
        this.type = yaxisBuilder.type;
        this.ticksuffix = yaxisBuilder.ticksuffix;
    }

    public static class YaxisBuilder {
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

        private String ticksuffix;

        public YaxisBuilder setTitle(final String title) {
            this.title = title;
            return this;
        }

        public YaxisBuilder setTitlefont(final Font titlefont) {
            this.titlefont = titlefont;
            return this;
        }

        public YaxisBuilder setTickfont(final Font tickfont) {
            this.tickfont = tickfont;
            return this;
        }

        public YaxisBuilder setTickangle(final Integer tickangle) {
            this.tickangle = tickangle;
            return this;
        }

        public YaxisBuilder setZeroline(final Boolean zeroline) {
            this.zeroline = zeroline;
            return this;
        }

        public YaxisBuilder setGridwidth(final Integer gridwidth) {
            this.gridwidth = gridwidth;
            return this;
        }

        public YaxisBuilder setAutorange(final Boolean autorange) {
            this.autorange = autorange;
            return this;
        }

        public YaxisBuilder setRangemode(final String rangemode) {
            this.rangemode = rangemode;
            return this;
        }

        public YaxisBuilder setRange(final List<Number> range) {
            this.range = range;
            return this;
        }

        public YaxisBuilder setDtick(final Number dtick) {
            this.dtick = dtick;
            return this;
        }

        public YaxisBuilder setType(final String type) {
            this.type = type;
            return this;
        }

        public YaxisBuilder setTicksuffix(final String ticksuffix) {
            this.ticksuffix = ticksuffix;
            return this;
        }

        public Yaxis build() {
            return new Yaxis(this);
        }
    }
}
