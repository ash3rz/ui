package org.iplantc.de.commons.client.views.dialogs;

import com.google.gwt.safehtml.shared.SafeHtml;

import com.sencha.gxt.widget.core.client.box.MessageBox;

/**
 * @author jstroot
 */
public class IplantInfoBox extends MessageBox {

    public IplantInfoBox(final SafeHtml headingHtml,
                         final SafeHtml messageHtml,
                         final WindowAppearance appearance,
                         final MessageBoxAppearance contentAppearance) {
        super(headingHtml, messageHtml, appearance, contentAppearance);

        setIcon(MessageBox.ICONS.info());
    }

    public IplantInfoBox(final String headingHtml,
                         final String messageHtml) {
        super(headingHtml, messageHtml);
        setIcon(MessageBox.ICONS.info());
    }

}
