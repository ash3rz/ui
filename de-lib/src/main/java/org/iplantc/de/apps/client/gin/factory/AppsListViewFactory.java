package org.iplantc.de.apps.client.gin.factory;

import org.iplantc.de.apps.client.AppsListView;
import org.iplantc.de.client.models.apps.App;

import com.google.inject.name.Named;

import com.sencha.gxt.data.shared.ListStore;

/**
 * @author aramsey
 */
public interface AppsListViewFactory {
    @Named(AppsListView.GRID_VIEW) AppsListView createGridView(ListStore<App> listStore);
    @Named(AppsListView.TILE_VIEW) AppsListView createTileView(ListStore<App> listStore);
}
