package org.iplantc.de.theme.base.client.apps.cells;

import org.iplantc.de.apps.client.views.list.cells.AppCommentCell;
import org.iplantc.de.apps.client.views.list.cells.AppFavoriteCell;
import org.iplantc.de.apps.client.views.list.cells.AppInfoCell;
import org.iplantc.de.apps.client.views.list.cells.AppIntegratorCell;
import org.iplantc.de.apps.client.views.list.cells.AppNameCell;
import org.iplantc.de.apps.client.views.list.cells.AppRatingCell;
import org.iplantc.de.apps.client.views.list.cells.AppTileCell;
import org.iplantc.de.client.models.apps.App;
import org.iplantc.de.theme.base.client.apps.list.TileListResources;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.HasCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;

/**
 * @author jstroot
 */
public class AppTileCellDefaultAppearance implements AppTileCell.AppTileCellAppearance {

    /**
     * The HTML templates used to render the cell.
     */
    interface Templates extends SafeHtmlTemplates {
        @Template("<div class='{0}'>")
        SafeHtml mod(String className);

        @SafeHtmlTemplates.Template("<img src='{0}'/>")
        SafeHtml img(String gravatar);
    }

    private final TileListResources resources;
    private TileListResources.AppsTileStyle style;
    private Templates templates;

    public AppTileCellDefaultAppearance() {
        this((TileListResources)GWT.create(TileListResources.class));
    }

    public AppTileCellDefaultAppearance(TileListResources resources) {
        this.resources = resources;
        this.style = resources.style();
        style.ensureInjected();
        this.templates = GWT.create(Templates.class);
    }

    @Override
    public <X> void render(Cell.Context context,
                           App value,
                           SafeHtmlBuilder sb,
                           HasCell<App, X> hasCell) {

        if (hasCell instanceof AppInfoCell) {
            sb.append(templates.mod(style.infoMod()));
        } else if (hasCell instanceof AppCommentCell) {
            sb.append(templates.mod(style.commentMod()));
        } else if (hasCell instanceof AppFavoriteCell) {
            sb.append(templates.mod(style.favoriteMod()));
        } else if (hasCell instanceof AppNameCell) {
            sb.append(templates.mod(style.nameMod()));
        } else if (hasCell instanceof AppIntegratorCell) {
            sb.append(templates.mod(style.integratorMod()));
        } else if (hasCell instanceof AppRatingCell) {
            sb.append(templates.mod(style.ratingMod()));
        }
        hasCell.getCell().render(context, hasCell.getValue(value), sb);
        sb.appendHtmlConstant("</div>");

    }


}
