package org.devgateway.toolkit.forms.wicket.components.charts;

import java.io.Serializable;
import java.util.List;

/**
 * @author idobre
 * @since 4/19/17
 */
public final class Data implements Serializable {
    private final List<? extends Number> values;

    private final List<String> labels;

    private final List<?> x;

    private final List<?> y;

    private final List<?> theta;

    private final List<?> r;

    private final String type;

    private final String name;

    private final String hoverinfo;

    private final String textinfo;

    private final List<String> hovertext;

    private final Double hole;

    private final Boolean sort;

    private final String direction;

    private final Double pull;

    private final List<String> text;

    private final String textposition;

    private final Font textfont;

    private final Marker marker;

    private final String mode;

    private final String fill;

    private final String orientation;

    private final Boolean cliponaxis;

    private final List<String> locations;

    private final String locationmode;

    private final List<?> z;

    private final List<List<Object>> colorscale;

    private final Boolean autocolorscale;

    private final Boolean reversescale;

    private final Boolean zauto;

    private final Colorbar colorbar;

    public Data(final DataBuilder dataBuilder) {
        this.values = dataBuilder.values;
        this.labels = dataBuilder.labels;
        this.x = dataBuilder.x;
        this.y = dataBuilder.y;
        this.theta = dataBuilder.theta;
        this.r = dataBuilder.r;
        this.type = dataBuilder.type;
        this.name = dataBuilder.name;
        this.hoverinfo = dataBuilder.hoverinfo;
        this.textinfo = dataBuilder.textinfo;
        this.hovertext = dataBuilder.hovertext;
        this.hole = dataBuilder.hole;
        this.sort = dataBuilder.sort;
        this.direction = dataBuilder.direction;
        this.pull = dataBuilder.pull;
        this.text = dataBuilder.text;
        this.textposition = dataBuilder.textposition;
        this.textfont = dataBuilder.textfont;
        this.marker = dataBuilder.marker;
        this.mode = dataBuilder.mode;
        this.fill = dataBuilder.fill;
        this.orientation = dataBuilder.orientation;
        this.cliponaxis = dataBuilder.cliponaxis;
        this.locations = dataBuilder.locations;
        this.locationmode = dataBuilder.locationmode;
        this.z = dataBuilder.z;
        this.colorscale = dataBuilder.colorscale;
        this.autocolorscale = dataBuilder.autocolorscale;
        this.reversescale = dataBuilder.reversescale;
        this.zauto = dataBuilder.zauto;
        this.colorbar = dataBuilder.colorbar;
    }

    public static class DataBuilder {
        private List<? extends Number> values;

        private List<String> labels;

        private List<?> x;

        private List<?> y;

        private List<?> theta;

        private List<?> r;

        private String type;

        private String name;

        private String hoverinfo;

        private String textinfo;

        private List<String> hovertext;

        private Double hole;

        private Boolean sort;

        private String direction;

        private Double pull;

        private List<String> text;

        private String textposition;

        private Font textfont;

        private Marker marker;

        private String mode;

        private String fill;

        private String orientation;

        private Boolean cliponaxis;

        private List<String> locations;

        private String locationmode;

        private List<?> z;

        private List<List<Object>> colorscale;

        private Boolean autocolorscale;

        private Boolean reversescale;

        private Boolean zauto;

        private Colorbar colorbar;

        public DataBuilder setValues(final List<? extends Number> values) {
            this.values = values;
            return this;
        }

        public DataBuilder setLabels(final List<String> labels) {
            this.labels = labels;
            return this;
        }

        public DataBuilder setX(final List<?> x) {
            this.x = x;
            return this;
        }

        public DataBuilder setY(final List<?> y) {
            this.y = y;
            return this;
        }

        public DataBuilder setTheta(final List<?> theta) {
            this.theta = theta;
            return this;
        }

        public DataBuilder setR(final List<?> r) {
            this.r = r;
            return this;
        }

        public DataBuilder setType(final String type) {
            this.type = type;
            return this;
        }

        public DataBuilder setName(final String name) {
            this.name = name;
            return this;
        }

        public DataBuilder setHoverinfo(final String hoverinfo) {
            this.hoverinfo = hoverinfo;
            return this;
        }

        public DataBuilder setTextinfo(final String textinfo) {
            this.textinfo = textinfo;
            return this;
        }

        public DataBuilder setHovertext(final List<String> hovertext) {
            this.hovertext = hovertext;
            return this;
        }

        public DataBuilder setHole(final Double hole) {
            this.hole = hole;
            return this;
        }

        public DataBuilder setSort(final Boolean sort) {
            this.sort = sort;
            return this;
        }

        public DataBuilder setDirection(final String direction) {
            this.direction = direction;
            return this;
        }

        public DataBuilder setPull(final Double pull) {
            this.pull = pull;
            return this;
        }

        public DataBuilder setText(final List<String> text) {
            this.text = text;
            return this;
        }

        public DataBuilder setTextposition(final String textposition) {
            this.textposition = textposition;
            return this;
        }

        public DataBuilder setTextfont(final Font textfont) {
            this.textfont = textfont;
            return this;
        }

        public DataBuilder setMarker(final Marker marker) {
            this.marker = marker;
            return this;
        }

        public DataBuilder setMode(final String mode) {
            this.mode = mode;
            return this;
        }

        public DataBuilder setFill(final String fill) {
            this.fill = fill;
            return this;
        }

        public DataBuilder setOrientation(final String orientation) {
            this.orientation = orientation;
            return this;
        }

        public DataBuilder setCliponaxis(final Boolean cliponaxis) {
            this.cliponaxis = cliponaxis;
            return this;
        }

        public DataBuilder setLocations(final List<String> locations) {
            this.locations = locations;
            return this;
        }

        public DataBuilder setLocationmode(final String locationmode) {
            this.locationmode = locationmode;
            return this;
        }

        public DataBuilder setZ(final List<?> z) {
            this.z = z;
            return this;
        }

        public DataBuilder setColorscale(final List<List<Object>> colorscale) {
            this.colorscale = colorscale;
            return this;
        }

        public DataBuilder setAutocolorscale(final Boolean autocolorscale) {
            this.autocolorscale = autocolorscale;
            return this;
        }

        public DataBuilder setReversescale(final Boolean reversescale) {
            this.reversescale = reversescale;
            return this;
        }

        public DataBuilder setZauto(final Boolean zauto) {
            this.zauto = zauto;
            return this;
        }

        public DataBuilder setColorbar(final Colorbar colorbar) {
            this.colorbar = colorbar;
            return this;
        }

        public Data build() {
            return new Data(this);
        }
    }
}
