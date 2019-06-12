package org.devgateway.toolkit.forms.util;

/**
 * @author idobre
 * @since 2019-03-29
 */
public final class JQueryUtil {
    private JQueryUtil() {

    }

    /**
     * Returns a JS script that scrolls the page until element defined by selector is visible.
     * @param selector jquery selector
     * @param duration animation duration
     *
     * @return the script
     */
    public static String animateScrollTop(final String selector, final int duration) {
        return "$('html, body').animate({ scrollTop: $(\"" + selector + "\").offset().top - 50 }, " + duration + ");";
    }
}
