package org.devgateway.toolkit.forms.wicket.components.form;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.MarkupStream;
import org.apache.wicket.markup.html.form.AbstractTextComponent;
import org.apache.wicket.markup.parser.XmlTag;
import org.apache.wicket.model.IModel;

/**
 * An {@link AbstractTextComponent} that is non editable and also shows html.
 * It can be useful to showing disabled text fields
 * @author mihai
 *
 * We are not longer using this, {@link ToolkitSummernoteEditor} is now smart enough to deal with read only state
 *
 * @param <T>
 */
@Deprecated
public class NonEditableTextField<T> extends AbstractTextComponent<T> {

    public NonEditableTextField(final String id) {
        super(id);
    }

    public NonEditableTextField(final String id, final IModel<T> model) {
        super(id, model);
    }

    @Override
    protected void onComponentTag(final ComponentTag tag) {
        super.onComponentTag(tag);

        //always turn our tag into a div
        tag.setName("div");

        if (tag.isOpenClose()) {
            // always transform the tag to <span></span> so even labels defined as <span/> render
            tag.setType(XmlTag.TagType.OPEN);
        }
    }

    @Override
    public void onComponentTagBody(final MarkupStream markupStream, final ComponentTag openTag) {
        replaceComponentTagBody(markupStream, openTag, getDefaultModelObjectAsString());
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        //allow html unescaped
        setEscapeModelStrings(false);
    }
}
