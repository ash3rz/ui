package org.iplantc.de.apps.client.views;

import org.iplantc.de.apps.client.AppDetailsView;

import com.google.web.bindery.autobean.shared.Splittable;

import jsinterop.annotations.JsType;

/**
 * @author psarando
 */
@JsType(isNative = true, namespace = "CyVerseReactComponents.apps", name = "details")
public final class ReactAppDetailsView {
    public static native void renderCategoryTree(String elementID, Splittable app, AppDetailsView presenter, AppDetailsView.AppDetailsAppearance appearance);
    public static native void renderCopyTextArea(String elementID, String btnText, String textToCopy);
    public static native void renderToolDetails(String elementID, AppDetailsView.AppDetailsAppearance appearance, Splittable app);
}
