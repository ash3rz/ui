package org.iplantc.de.apps.client.views.list.cells;

import static com.google.gwt.dom.client.BrowserEvents.CLICK;

import org.iplantc.de.apps.client.events.selection.AppNameSelectedEvent;
import org.iplantc.de.apps.shared.AppsModule;
import org.iplantc.de.client.models.apps.App;

import com.google.common.base.Strings;
import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.HasCell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.shared.HasHandlers;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.Event;

/**
 * @author aramsey
 */
public class AppCardCell extends AbstractCell<App> implements HasCell<App, App> {

    public interface AppCardCellAppearance extends AppNameCell.AppNameCellAppearance {
        void render(Context context, App value, SafeHtmlBuilder sb, String cardUrl, String cardUrlOptions, String textToolTip, String debugId);
    }

    private AppCardCellAppearance appearance;
    private HasHandlers hasHandlers;
    private String baseDebugId;
    private String cardUrl, cardUrlOptions;

    public AppCardCell() {
        this(GWT.<AppCardCellAppearance>create(AppCardCellAppearance.class));
    }

    public AppCardCell(final AppCardCellAppearance appearance) {
        super(CLICK);
        this.appearance = appearance;
    }
    @Override
    public void render(Context context, App value, SafeHtmlBuilder sb) {
        if (!Strings.isNullOrEmpty(cardUrl)) {
            String debugId = baseDebugId + "." + value.getId() + AppsModule.Ids.APP_CARD_CELL;

            String textToolTip;
            if (value.isDisabled()) {
                textToolTip = appearance.appUnavailable();
            } else if (value.isBeta() != null && value.isBeta()) {
                textToolTip = appearance.appBeta();
            } else if (!value.isPublic()) {
                textToolTip = appearance.appPrivate();
            } else {
                textToolTip = appearance.run();
            }

            appearance.render(context, value, sb, cardUrl, cardUrlOptions, textToolTip, debugId);
        }
    }

    @Override
    public Cell<App> getCell() {
        return this;
    }

    @Override
    public FieldUpdater<App, App> getFieldUpdater() {
        return null;
    }

    @Override
    public App getValue(App object) {
        return object;
    }

    @Override
    public void onBrowserEvent(Cell.Context context, Element parent, App value, NativeEvent event,
                               ValueUpdater<App> valueUpdater) {
        Element eventTarget = Element.as(event.getEventTarget());
        if ((value == null) || !parent.isOrHasChild(eventTarget)) {
            return;
        }

        if (parent.isOrHasChild(eventTarget)) {
            switch (Event.as(event).getTypeInt()) {
                case Event.ONCLICK:
                    if(hasHandlers != null){
                        hasHandlers.fireEvent(new AppNameSelectedEvent(value));
                    }
                    break;
                default:
                    break;
            }
        }
    }

    public void setHasHandlers(HasHandlers hasHandlers) {
        this.hasHandlers = hasHandlers;
    }

    public void setBaseDebugId(String baseDebugId) {
        this.baseDebugId = baseDebugId;
    }

    public void setCardUrl(String appsCardUrl, String appsCardUrlOptions) {
        this.cardUrl = appsCardUrl;
        this.cardUrlOptions = appsCardUrlOptions;
    }
}
