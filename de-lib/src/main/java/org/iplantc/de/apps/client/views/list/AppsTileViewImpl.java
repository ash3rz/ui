package org.iplantc.de.apps.client.views.list;

import org.iplantc.de.apps.client.AppsListView;
import org.iplantc.de.apps.client.events.AppFavoritedEvent;
import org.iplantc.de.apps.client.events.AppSearchResultLoadEvent;
import org.iplantc.de.apps.client.events.BeforeAppSearchEvent;
import org.iplantc.de.apps.client.events.selection.AppCategorySelectionChangedEvent;
import org.iplantc.de.apps.client.events.selection.AppCommentSelectedEvent;
import org.iplantc.de.apps.client.events.selection.AppFavoriteSelectedEvent;
import org.iplantc.de.apps.client.events.selection.AppInfoSelectedEvent;
import org.iplantc.de.apps.client.events.selection.AppNameSelectedEvent;
import org.iplantc.de.apps.client.events.selection.AppRatingDeselected;
import org.iplantc.de.apps.client.events.selection.AppRatingSelected;
import org.iplantc.de.apps.client.events.selection.AppSelectionChangedEvent;
import org.iplantc.de.apps.client.events.selection.OntologyHierarchySelectionChangedEvent;
import org.iplantc.de.apps.client.models.AppProperties;
import org.iplantc.de.apps.client.views.list.cells.AppTileCell;
import org.iplantc.de.apps.shared.AppsModule;
import org.iplantc.de.client.models.apps.App;
import org.iplantc.de.shared.DEProperties;
import org.iplantc.de.theme.base.client.apps.list.TileListDefaultAppearance;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

import com.sencha.gxt.core.client.IdentityValueProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.SortDir;
import com.sencha.gxt.data.shared.Store;
import com.sencha.gxt.data.shared.StringLabelProvider;
import com.sencha.gxt.dnd.core.client.DragSource;
import com.sencha.gxt.dnd.core.client.ListViewDragSource;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.ListView;
import com.sencha.gxt.widget.core.client.form.SimpleComboBox;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * @author aramsey
 */
public class AppsTileViewImpl extends ContentPanel
        implements AppsListView, SelectionChangedEvent.SelectionChangedHandler<App>, HasHandlers {

    interface AppsGridViewImplUiBinder extends UiBinder<Widget, AppsTileViewImpl> {
    }

    private static final AppsGridViewImplUiBinder ourUiBinder =
            GWT.create(AppsGridViewImplUiBinder.class);

    class AppRatingComparator implements Comparator<App> {

        @Override
        public int compare(App o1, App o2) {
            if (o1.getRating().getAverageRating() < o2.getRating().getAverageRating()) return -1;
            if (o1.getRating().getAverageRating() > o2.getRating().getAverageRating()) return 1;
            return 0;
        }
    }

    enum SortChoice {
        Name,
        Rating,
        Integrator
    }

    ListStore<App> listStore;
    @UiField ListView<App, App> listView;
    @UiField SimpleComboBox<SortChoice> sortBox;
    @UiField(provided = true) AppsListView.AppsListAppearance appearance;
    private TileListDefaultAppearance<App> listAppearance;
    private AppTileCell appTileCell;
    private AppProperties properties;
    @Inject DEProperties deProperties;

    @Inject
    AppsTileViewImpl(final AppsListView.AppsListAppearance appearance,
                     @Assisted final ListStore<App> listStore,
                     TileListDefaultAppearance<App> listAppearance,
                     AppTileCell appTileCell,
                     AppProperties properties) {
        this.appearance = appearance;
        this.listStore = listStore;
        this.listAppearance = listAppearance;
        this.appTileCell = appTileCell;
        this.properties = properties;
        this.deProperties = DEProperties.getInstance();

        appTileCell.setHasHandlers(this);
        appTileCell.setCardUrl(deProperties.getAppsCardUrl(), deProperties.getAppsCardUrlOptions());

        setWidget(ourUiBinder.createAndBindUi(this));

        listView.getSelectionModel().addSelectionChangedHandler(this);
        listView.setCell(this.appTileCell);
    }

    @UiFactory
    SimpleComboBox<SortChoice> createSortBox() {
        SimpleComboBox<SortChoice> comboBox = new SimpleComboBox<>(new StringLabelProvider<>());
        comboBox.add(Arrays.asList(SortChoice.Name,
                                   SortChoice.Integrator,
                                   SortChoice.Rating));
        comboBox.setValue(SortChoice.Name);
        return comboBox;
    }

    @UiFactory
    ListView<App, App> createListView() {
        return new ListView<>(listStore, new IdentityValueProvider<App>(), listAppearance);
    }

    @UiHandler("sortBox")
    void onSortBoxSelection(SelectionEvent<SortChoice> event) {
        mask();
        listStore.clearSortInfo();
        listStore.addSortInfo(getSortInfo());
        unmask();
    }

    public Store.StoreSortInfo<App> getSortInfo() {
        SortChoice sortField = sortBox.getCurrentValue();

        switch(sortField) {
            case Name:
                return new Store.StoreSortInfo<>(properties.name(), SortDir.ASC);
            case Integrator:
                return new Store.StoreSortInfo<>(properties.integratorName(), SortDir.ASC);
            case Rating:
                return new Store.StoreSortInfo<>(new AppRatingComparator(), SortDir.DESC);
            default:
                return null;
        }
    }

    @Override
    public HandlerRegistration addAppNameSelectedEventHandler(AppNameSelectedEvent.AppNameSelectedEventHandler handler) {
        return addHandler(handler, AppNameSelectedEvent.TYPE);
    }

    @Override
    public HandlerRegistration addAppSelectionChangedEventHandler(AppSelectionChangedEvent.AppSelectionChangedEventHandler handler) {
        return addHandler(handler, AppSelectionChangedEvent.TYPE);
    }

    @Override
    public HandlerRegistration addAppInfoSelectedEventHandler(AppInfoSelectedEvent.AppInfoSelectedEventHandler handler) {
        return addHandler(handler, AppInfoSelectedEvent.TYPE);
    }

    @Override
    public HandlerRegistration addAppCommentSelectedEventHandlers(AppCommentSelectedEvent.AppCommentSelectedEventHandler handler) {
        return addHandler(handler, AppCommentSelectedEvent.TYPE);
    }

    @Override
    public HandlerRegistration addAppFavoriteSelectedEventHandlers(AppFavoriteSelectedEvent.AppFavoriteSelectedEventHandler handler) {
        return addHandler(handler, AppFavoriteSelectedEvent.TYPE);
    }

    @Override
    public HandlerRegistration addAppFavoritedEventHandler(AppFavoritedEvent.AppFavoritedEventHandler eventHandler) {
        return addHandler(eventHandler, AppFavoritedEvent.TYPE);
    }

    @Override
    public HandlerRegistration addAppRatingDeselectedHandler(AppRatingDeselected.AppRatingDeselectedHandler handler) {
        return addHandler(handler, AppRatingDeselected.TYPE);
    }

    @Override
    public HandlerRegistration addAppRatingSelectedHandler(AppRatingSelected.AppRatingSelectedHandler handler) {
        return addHandler(handler, AppRatingSelected.TYPE);
    }

    @Override
    public void onAppCategorySelectionChanged(AppCategorySelectionChangedEvent event) {
        // FIXME Move to appearance
        setHeading(Joiner.on(" >> ").join(event.getGroupHierarchy()));

        if (!event.getAppCategorySelection().isEmpty()) {
            // Reset Search
            setSearchPattern("");
        }
    }

    @Override
    public void onOntologyHierarchySelectionChanged(OntologyHierarchySelectionChangedEvent event) {
        setHeading(Joiner.on(" >> ").join(event.getPath()));

        if (event.getSelectedHierarchy() != null) {
            // Reset Search
            setSearchPattern("");
        }
    }

    @Override
    public void onAppSearchResultLoad(AppSearchResultLoadEvent event) {
        int total = event.getResults() == null ? 0 : event.getResults().size();
        setHeading(appearance.searchAppResultsHeader(event.getSearchText(), total));
        unmask();
    }

    @Override
    public void onBeforeAppSearch(BeforeAppSearchEvent event) {
        mask(appearance.beforeAppSearchLoadingMask());
    }

    @Override
    public void onSelectionChanged(SelectionChangedEvent<App> event) {
        fireEvent(new AppSelectionChangedEvent(event.getSelection()));
    }

    @Override
    protected void onEnsureDebugId(String baseID) {
        super.onEnsureDebugId(baseID);
        listView.ensureDebugId(baseID + AppsModule.Ids.APP_TILES);
        appTileCell.setDebugBaseId(baseID + AppsModule.Ids.APP_TILES);
    }

    @Override
    public List<DragSource> getAppsDragSources() {
        List<DragSource> sources = Lists.newArrayList();
        sources.add(new ListViewDragSource<>(listView));
        return sources;
    }

    @Override
    public App getSelectedItem() {
        return listView.getSelectionModel().getSelectedItem();
    }

    @Override
    public void select(App app, boolean keepExisting) {
        listView.getSelectionModel().select(app, keepExisting);
    }

    @Override
    public void deselectAll() {
        listView.getSelectionModel().deselectAll();
    }

    @Override
    public void setSearchPattern(String searchPattern) {
        appTileCell.setSearchRegexPattern(searchPattern);
    }
}
