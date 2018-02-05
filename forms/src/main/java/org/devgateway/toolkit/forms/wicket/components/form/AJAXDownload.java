package org.devgateway.toolkit.forms.wicket.components.form;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.behavior.AbstractAjaxBehavior;
import org.apache.wicket.request.IRequestHandler;

/**
 * AJAX update and file download.
 *
 * https://cwiki.apache.org/confluence/display/WICKET/AJAX+update+and+file+download+in+one+blow
 */
public abstract class AJAXDownload extends AbstractAjaxBehavior {
    private boolean addAntiCache;

    public AJAXDownload() {
        this(true);
    }

    public AJAXDownload(final boolean addAntiCache) {
        super();
        this.addAntiCache = addAntiCache;
    }

    /**
     * Call this method to initiate the download.
     */
    public void initiate(final AjaxRequestTarget target) {
        String url = getCallbackUrl().toString();

        if (addAntiCache) {
            url = url + (url.contains("?") ? "&" : "?");
            url = url + "antiCache=" + System.currentTimeMillis();
        }

        // the timeout is needed to let Wicket release the channel
        target.appendJavaScript("setTimeout(\"window.location.href='" + url + "'\", 100);");
    }

    @Override
    public void onRequest() {
        getComponent().getRequestCycle().scheduleRequestHandlerAfterCurrent(getHandler());
    }

    protected abstract IRequestHandler getHandler();
}
