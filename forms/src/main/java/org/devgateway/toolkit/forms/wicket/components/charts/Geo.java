package org.devgateway.toolkit.forms.wicket.components.charts;

import java.io.Serializable;

/**
 * @author idobre
 * @since 2018-12-04
 */
public class Geo implements Serializable {
    private final String scope;

    private final Boolean showframe;

    private final Boolean showcoastlines;

    private final Boolean showland;

    private final Projection projection;

    public Geo(final GeoBuilder geoBuilder) {
        this.scope = geoBuilder.scope;
        this.showframe = geoBuilder.showframe;
        this.showcoastlines = geoBuilder.showcoastlines;
        this.showland = geoBuilder.showland;
        this.projection = geoBuilder.projection;
    }

    public static class GeoBuilder {
        private String scope;

        private Boolean showframe;

        private Boolean showcoastlines;

        private Boolean showland;

        private Projection projection;

        public GeoBuilder setScope(final String scope) {
            this.scope = scope;
            return this;
        }

        public GeoBuilder setShowframe(final Boolean showframe) {
            this.showframe = showframe;
            return this;
        }

        public GeoBuilder setShowcoastlines(final Boolean showcoastlines) {
            this.showcoastlines = showcoastlines;
            return this;
        }

        public GeoBuilder setShowland(final Boolean showland) {
            this.showland = showland;
            return this;
        }

        public GeoBuilder setProjection(final Projection projection) {
            this.projection = projection;
            return this;
        }

        public Geo build() {
            return new Geo(this);
        }
    }
}
