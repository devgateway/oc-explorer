package org.devgateway.toolkit.forms.wicket.components.charts;

import java.io.Serializable;

/**
 * @author idobre
 * @since 2018-12-04
 */
public class Projection implements Serializable {
    private final String type;

    public Projection(final ProjectionBuilder projectionBuilder) {
        this.type = projectionBuilder.type;
    }

    public static class ProjectionBuilder {
        private String type;

        public ProjectionBuilder setType(final String type) {
            this.type = type;
            return this;
        }

        public Projection build() {
            return new Projection(this);
        }
    }
}
