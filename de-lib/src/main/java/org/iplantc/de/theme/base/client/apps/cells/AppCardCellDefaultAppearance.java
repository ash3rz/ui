package org.iplantc.de.theme.base.client.apps.cells;

import org.iplantc.de.apps.client.views.list.cells.AppCardCell;
import org.iplantc.de.client.models.apps.App;
import org.iplantc.de.commons.client.util.MD5Util;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;

/**
 * @author aramsey
 */
public class AppCardCellDefaultAppearance extends AppNameCellDefaultAppearance implements AppCardCell.AppCardCellAppearance {

    interface Templates extends SafeHtmlTemplates {
        @SafeHtmlTemplates.Template("<img class='{0}' src='{1}' qtip='{2}' id='{3}'/>")
        SafeHtml img(String className, String gravatar, String textToolTip, String debugId);
    }

    public interface AppCardCellStyles extends CssResource {
        @CssResource.ClassName("icon")
        String icon();
    }

    public interface AppCardCellResources extends ClientBundle {
        @Source("AppCardCell.css")
        AppCardCellStyles css();
    }

    private AppCardCellResources resources;
    private Templates templates;
    private AppCardCellStyles styles;

    public AppCardCellDefaultAppearance() {
        this ((AppCardCellResources)GWT.create(AppCardCellResources.class),((Templates)GWT.create(Templates.class)));
    }

    public AppCardCellDefaultAppearance(AppCardCellResources appCardCellResources, Templates templates) {
        this.resources = appCardCellResources;
        this.templates = templates;
        this.styles = resources.css();
        styles.ensureInjected();
    }

    @Override
    public void render(Cell.Context context, App value, SafeHtmlBuilder sb, String cardUrl, String cardUrlOptions, String textToolTip, String debugID) {
        String hash = MD5Util.md5Hex(value.getId());
        String gravatar = cardUrl + hash + cardUrlOptions;
        sb.append(templates.img(styles.icon(), gravatar, textToolTip, debugID));
    }
}
