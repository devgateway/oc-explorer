package org.devgateway.toolkit.forms.wicket.components.table;

import java.util.Date;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.FilterForm;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.TextFilteredPropertyColumn;
import org.apache.wicket.model.IModel;
import org.devgateway.toolkit.forms.wicket.components.form.DateFieldBootstrapFormComponent;

/**
 * A TextFilteredPropertyColumn that uses DateFieldBootstrapFormComponent as a
 * filter.
 *
 * Created by mpostelnicu
 */
public class DateFilteredBootstrapPropertyColumn<T, S> extends TextFilteredPropertyColumn<T, Date, S> {

    public DateFilteredBootstrapPropertyColumn(final IModel<String> displayModel, final S sortProperty,
                                               final String propertyExpression) {
        super(displayModel, sortProperty, propertyExpression);
    }

    public DateFilteredBootstrapPropertyColumn(final IModel<String> displayModel, final String propertyExpression) {
        super(displayModel, propertyExpression);
    }

    @Override
    public Component getFilter(final String componentId, final FilterForm<?> form) {
        final DateFieldBootstrapFormComponent dateField =
                new DateFieldBootstrapFormComponent(componentId, getFilterModel(form));
        dateField.hideLabel();
        dateField.getField().add(AttributeModifier.replace("onchange", "this.form.submit();"));
        return dateField;
    }
}
