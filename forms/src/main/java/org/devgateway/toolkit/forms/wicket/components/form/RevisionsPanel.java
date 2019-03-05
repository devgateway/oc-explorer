package org.devgateway.toolkit.forms.wicket.components.form;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.TransparentWebMarkupContainer;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import java.util.List;

/**
 * Panel that shows envers revisions and can be attached to {@link GenericBootstrapFormComponent}
 *
 * @param <TYPE> the component type
 */
public class RevisionsPanel<TYPE> extends GenericPanel<List<TYPE>> {
    private final String auditProperty;

    private TransparentWebMarkupContainer revisionsCollapse;

    private TransparentWebMarkupContainer revisionsMasterGroup;

    private TransparentWebMarkupContainer revisionsChildGroup;

    private WebMarkupContainer revisionsPanelLink;

    private Label revisionsPanelLabel;

    /**
     * @param id            the revision panel id
     * @param model         list of revisions coming from the component, this is usually fed with
     *                      {@link GenericBootstrapFormComponent#getRevisionsModel()}
     * @param auditProperty the audit property. This is required because sometimes the component is bound with a
     *                      model that has a different source than the component name as property, so we cannot
     *                      assume the id is the property.
     */
    public RevisionsPanel(final String id, final IModel<List<TYPE>> model, final String auditProperty) {
        super(id, model);
        this.auditProperty = auditProperty;
    }

    private IModel<String> getLabelKeyFromGenericComponent() {
        return ((GenericBootstrapFormComponent) this.getParent()).getLabelModel();
    }

    private boolean printUnescaped() {
        return ((GenericBootstrapFormComponent) this.getParent()).printUnescaped();
    }


    @Override
    protected void onInitialize() {
        super.onInitialize();
        revisionsCollapse = new TransparentWebMarkupContainer("revisionsCollapse");
        revisionsCollapse.setOutputMarkupId(true);
        add(revisionsCollapse);
        setOutputMarkupId(true);

        revisionsPanelLink = new WebMarkupContainer("revisionsPanelLink");
        revisionsPanelLabel = new Label("revisionsPanelLabel", getLabelKeyFromGenericComponent());
        revisionsPanelLink.add(revisionsPanelLabel);
        revisionsPanelLink.add(AttributeModifier.append("href", "#" + revisionsCollapse.getMarkupId()));

        add(revisionsPanelLink);
        add(new ListView<TYPE>("rows", getModel()) {
            public void populateItem(final ListItem<TYPE> item) {
                final Object[] obj = (Object[]) item.getModelObject();
                final Label data = new Label("data", new PropertyModel<>(
                        obj[0],
                        auditProperty
                ));
                data.setEscapeModelStrings(!printUnescaped());
                item.add(data);

                final Label lastUpdated = new Label("lastModifiedDate", new PropertyModel<>(
                        obj[0],
                        "lastModifiedDate"
                ));
                item.add(lastUpdated);


                final Label lastModifiedBy = new Label("lastModifiedBy", new PropertyModel<>(
                        obj[0],
                        "lastModifiedBy"
                ));
                item.add(lastModifiedBy);

                final Label revisionType = new Label("revisionType", new PropertyModel<>(
                        obj[2],
                        "name"
                ));
                item.add(revisionType);

                final Label id = new Label("id", item.getIndex());
                item.add(id);
            }
        });

    }
}
