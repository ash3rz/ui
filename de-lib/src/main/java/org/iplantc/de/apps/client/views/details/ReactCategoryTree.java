package org.iplantc.de.apps.client.views.details;

import org.iplantc.de.apps.client.AppDetailsView;

import com.google.web.bindery.autobean.shared.Splittable;

import gwt.react.client.components.ReactClass;
import gwt.react.client.proptypes.BaseProps;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsProperty;
import jsinterop.annotations.JsType;

/**
 * @author aramsey
 */
@JsType(isNative = true, namespace = "CyVerseReactComponents.apps.details", name = "CategoryTree")
public class ReactCategoryTree {

    @JsProperty(namespace = "CyVerseReactComponents.apps.details", name = "CategoryTree")
    public static ReactClass<CategoryTreeProps> CategoryTree;

    @JsType(isNative = true, namespace = JsPackage.GLOBAL, name = "Object")
    static class CategoryTreeProps extends BaseProps {
        public Splittable app;
        public AppDetailsView presenter;
        public AppDetailsView.AppDetailsAppearance appearance;
    }
}
