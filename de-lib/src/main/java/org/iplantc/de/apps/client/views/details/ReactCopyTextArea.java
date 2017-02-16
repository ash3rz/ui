package org.iplantc.de.apps.client.views.details;

import gwt.react.client.components.ReactClass;
import gwt.react.client.proptypes.BaseProps;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsProperty;
import jsinterop.annotations.JsType;

/**
 * @author aramsey
 */
@JsType(isNative = true, namespace = "CyVerseReactComponents.apps.details", name = "CopyTextArea")
public class ReactCopyTextArea {

    @JsProperty(namespace = "CyVerseReactComponents.apps.details", name = "CopyTextArea")
    public static ReactClass<CopyProps> CopyTextArea;

    @JsType(isNative = true, namespace = JsPackage.GLOBAL, name = "Object")
    static class CopyProps extends BaseProps {
        public String text;
        public String btnText;
    }
}
