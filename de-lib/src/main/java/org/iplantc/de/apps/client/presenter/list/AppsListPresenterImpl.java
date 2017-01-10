package org.iplantc.de.apps.client.presenter.list;

import org.iplantc.de.apps.client.AppsListView;
import org.iplantc.de.apps.client.events.AppFavoritedEvent;
import org.iplantc.de.apps.client.events.AppSearchResultLoadEvent;
import org.iplantc.de.apps.client.events.AppUpdatedEvent;
import org.iplantc.de.apps.client.events.BeforeAppSearchEvent;
import org.iplantc.de.apps.client.events.RunAppEvent;
import org.iplantc.de.apps.client.events.SwapViewButtonClickedEvent;
import org.iplantc.de.apps.client.events.selection.AppCategorySelectionChangedEvent;
import org.iplantc.de.apps.client.events.selection.AppCommentSelectedEvent;
import org.iplantc.de.apps.client.events.selection.AppFavoriteSelectedEvent;
import org.iplantc.de.apps.client.events.selection.AppInfoSelectedEvent;
import org.iplantc.de.apps.client.events.selection.AppNameSelectedEvent;
import org.iplantc.de.apps.client.events.selection.AppRatingDeselected;
import org.iplantc.de.apps.client.events.selection.AppRatingSelected;
import org.iplantc.de.apps.client.events.selection.AppSelectionChangedEvent;
import org.iplantc.de.apps.client.events.selection.DeleteAppsSelected;
import org.iplantc.de.apps.client.events.selection.OntologyHierarchySelectionChangedEvent;
import org.iplantc.de.apps.client.events.selection.RunAppSelected;
import org.iplantc.de.apps.client.gin.factory.AppsListViewFactory;
import org.iplantc.de.apps.client.presenter.callbacks.DeleteRatingCallback;
import org.iplantc.de.apps.client.presenter.callbacks.RateAppCallback;
import org.iplantc.de.client.events.EventBus;
import org.iplantc.de.client.models.UserInfo;
import org.iplantc.de.client.models.apps.App;
import org.iplantc.de.client.models.apps.AppCategory;
import org.iplantc.de.client.models.avu.Avu;
import org.iplantc.de.client.models.ontologies.OntologyHierarchy;
import org.iplantc.de.client.services.AppMetadataServiceFacade;
import org.iplantc.de.client.services.AppServiceFacade;
import org.iplantc.de.client.services.AppUserServiceFacade;
import org.iplantc.de.client.services.OntologyServiceFacade;
import org.iplantc.de.client.util.OntologyUtil;
import org.iplantc.de.commons.client.ErrorHandler;
import org.iplantc.de.commons.client.comments.view.dialogs.CommentsDialog;
import org.iplantc.de.commons.client.info.ErrorAnnouncementConfig;
import org.iplantc.de.commons.client.info.IplantAnnouncer;
import org.iplantc.de.commons.client.views.dialogs.AgaveAuthPrompt;
import org.iplantc.de.shared.AppsCallback;
import org.iplantc.de.shared.AsyncProviderWrapper;
import org.iplantc.de.shared.exceptions.HttpRedirectException;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;

import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.event.StoreAddEvent;
import com.sencha.gxt.data.shared.event.StoreClearEvent;
import com.sencha.gxt.data.shared.event.StoreRemoveEvent;
import com.sencha.gxt.data.shared.event.StoreUpdateEvent;
import com.sencha.gxt.dnd.core.client.DragSource;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.container.CardLayoutContainer;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;

import java.util.List;

/**
 * @author jstroot
 */
public class AppsListPresenterImpl implements AppsListView.Presenter,
                                              AppNameSelectedEvent.AppNameSelectedEventHandler,
                                              AppRatingSelected.AppRatingSelectedHandler,
                                              AppRatingDeselected.AppRatingDeselectedHandler,
                                              AppCommentSelectedEvent.AppCommentSelectedEventHandler,
                                              AppFavoriteSelectedEvent.AppFavoriteSelectedEventHandler,
                                              AppUpdatedEvent.AppUpdatedEventHandler,
                                              AppSelectionChangedEvent.AppSelectionChangedEventHandler,
                                              AppInfoSelectedEvent.AppInfoSelectedEventHandler {

    private class AppListCallback extends AppsCallback<List<App>> {
        @Override
        public void onFailure(Integer statusCode, Throwable caught) {
            if (caught instanceof HttpRedirectException) {
                final String uri = ((HttpRedirectException)caught).getLocation();
                AgaveAuthPrompt prompt = new AgaveAuthPrompt(uri);
                prompt.show();
                prompt.addDialogHideHandler(new DialogHideEvent.DialogHideHandler() {
                    @Override
                    public void onDialogHide(DialogHideEvent event) {
                        if (event.getHideButton() == Dialog.PredefinedButton.NO) {
                            listStore.clear();
                            gridView.setHeading(appearance.agaveAuthRequiredTitle());
                            tileView.setHeading(appearance.agaveAuthRequiredTitle());
                        }
                    }
                });
            } else {
                postToErrorHandler(caught);
                listStore.clear();
                gridView.setHeading(appearance.appLoadError());
                tileView.setHeading(appearance.appLoadError());
            }
            activeView.unmask();
        }

        @Override
        public void onSuccess(final List<App> apps) {
            listStore.clear();
            listStore.addAll(apps);

            if (getDesiredSelectedApp() != null) {

                activeView.select(getDesiredSelectedApp(), false);

            } else if (listStore.size() > 0) {
                // Select first app
                activeView.select(listStore.get(0), false);
            }
            setDesiredSelectedApp(null);
            activeView.unmask();
        }
    }

    final ListStore<App> listStore;
    @Inject IplantAnnouncer announcer;
    @Inject AppServiceFacade appService;
    @Inject AppUserServiceFacade appUserService;
    @Inject AppsListView.AppsListAppearance appearance;
    @Inject AsyncProviderWrapper<CommentsDialog> commentsDialogProvider;
    @Inject AppMetadataServiceFacade metadataFacade;
    @Inject UserInfo userInfo;
    OntologyServiceFacade ontologyService;
    OntologyUtil ontologyUtil;
    HandlerManager handlerManager;
    private final EventBus eventBus;
    AppsListView gridView, tileView, activeView;
    private App desiredSelectedApp;
    CardLayoutContainer cards;

    @Inject
    AppsListPresenterImpl(final AppsListViewFactory viewFactory,
                          final ListStore<App> listStore,
                          final EventBus eventBus,
                          OntologyServiceFacade ontologyService) {
        this.listStore = listStore;
        this.eventBus = eventBus;
        this.ontologyService = ontologyService;
        this.ontologyUtil = OntologyUtil.getInstance();
        this.gridView = viewFactory.createGridView(listStore);
        this.tileView = viewFactory.createTileView(listStore);

        this.gridView.addAppNameSelectedEventHandler(this);
        this.gridView.addAppRatingDeselectedHandler(this);
        this.gridView.addAppRatingSelectedHandler(this);
        this.gridView.addAppCommentSelectedEventHandlers(this);
        this.gridView.addAppFavoriteSelectedEventHandlers(this);
        this.gridView.addAppSelectionChangedEventHandler(this);
        this.gridView.addAppInfoSelectedEventHandler(this);

        this.tileView.addAppNameSelectedEventHandler(this);
        this.tileView.addAppRatingDeselectedHandler(this);
        this.tileView.addAppRatingSelectedHandler(this);
        this.tileView.addAppCommentSelectedEventHandlers(this);
        this.tileView.addAppFavoriteSelectedEventHandlers(this);
        this.tileView.addAppSelectionChangedEventHandler(this);
        this.tileView.addAppInfoSelectedEventHandler(this);

        activeView = tileView;

        eventBus.addHandler(AppUpdatedEvent.TYPE, this);
    }

    @Override
    public void go(CardLayoutContainer container) {
        this.cards = container;

        cards.add(tileView);
        cards.add(gridView);
    }

    @Override
    public void setViewDebugId(String baseID) {
        tileView.asWidget().ensureDebugId(baseID);
        gridView.asWidget().ensureDebugId(baseID);
    }

    @Override
    public HandlerRegistration addStoreAddHandler(StoreAddEvent.StoreAddHandler<App> handler) {
        return listStore.addStoreAddHandler(handler);
    }

    @Override
    public HandlerRegistration addStoreClearHandler(StoreClearEvent.StoreClearHandler<App> handler) {
        return listStore.addStoreClearHandler(handler);
    }

    @Override
    public HandlerRegistration addStoreRemoveHandler(StoreRemoveEvent.StoreRemoveHandler<App> handler) {
        return listStore.addStoreRemoveHandler(handler);
    }

    @Override
    public HandlerRegistration addStoreUpdateHandler(StoreUpdateEvent.StoreUpdateHandler<App> handler) {
        return listStore.addStoreUpdateHandler(handler);
    }

    @Override
    public HandlerRegistration addAppInfoSelectedEventHandler(AppInfoSelectedEvent.AppInfoSelectedEventHandler handler) {
        return ensureHandlers().addHandler(AppInfoSelectedEvent.TYPE, handler);
    }

    @Override
    public HandlerRegistration addAppSelectionChangedEventHandler(AppSelectionChangedEvent.AppSelectionChangedEventHandler handler) {
        return ensureHandlers().addHandler(AppSelectionChangedEvent.TYPE, handler);
    }

    @Override
    public void onAppInfoSelected(AppInfoSelectedEvent event) {
        fireEvent(event);
    }

    @Override
    public void onAppSelectionChanged(AppSelectionChangedEvent event) {
        fireEvent(event);
    }

    @Override
    public void onBeforeAppSearch(BeforeAppSearchEvent event) {
        activeView.mask(appearance.beforeAppSearchLoadingMask());
    }

    public App getDesiredSelectedApp() {
        return desiredSelectedApp;
    }

    @Override
    public App getSelectedApp() {
        return activeView.getSelectedItem();
    }

    @Override
    public List<DragSource> getAppsDragSources() {
        List<DragSource> sources = Lists.newArrayList();
        sources.addAll(gridView.getAppsDragSources());
        sources.addAll(tileView.getAppsDragSources());
        return sources;
    }

    @Override
    public void onAppCategorySelectionChanged(AppCategorySelectionChangedEvent event) {
        tileView.onAppCategorySelectionChanged(event);
        gridView.onAppCategorySelectionChanged(event);

        if (event.getAppCategorySelection().isEmpty()) {
            return;
        }
        Preconditions.checkArgument(event.getAppCategorySelection().size() == 1);
        activeView.mask(appearance.getAppsLoadingMask());

        final AppCategory appCategory = event.getAppCategorySelection().iterator().next();
        appService.getApps(appCategory, new AppListCallback());
    }

    @Override
    public void onOntologyHierarchySelectionChanged(OntologyHierarchySelectionChangedEvent event) {
        tileView.onOntologyHierarchySelectionChanged(event);
        gridView.onOntologyHierarchySelectionChanged(event);

        OntologyHierarchy selectedHierarchy = event.getSelectedHierarchy();
        if (selectedHierarchy != null) {
            activeView.mask(appearance.getAppsLoadingMask());
            Avu avu = ontologyUtil.convertHierarchyToAvu(selectedHierarchy);
            String iri = selectedHierarchy.getIri();
            if (ontologyUtil.isUnclassified(selectedHierarchy)) {
                ontologyService.getUnclassifiedAppsInCategory(ontologyUtil.getUnclassifiedParentIri(selectedHierarchy), avu, new AppListCallback());
            } else {
                ontologyService.getAppsInCategory(iri, avu, new AppListCallback());
            }
        }
    }

    @Override
    public void onAppCommentSelectedEvent(AppCommentSelectedEvent event) {
        final App app = event.getApp();
        commentsDialogProvider.get(new AsyncCallback<CommentsDialog>() {
            @Override
            public void onFailure(Throwable caught) {
                announcer.schedule(new ErrorAnnouncementConfig("Something happened while trying to manage comments. Please try again or contact support for help."));
            }

            @Override
            public void onSuccess(CommentsDialog result) {
                result.show(app,
                            app.getIntegratorEmail().equals(userInfo.getEmail()),
                            metadataFacade);
            }
        });
    }

    @Override
    public void onAppFavoriteSelected(AppFavoriteSelectedEvent event) {
        final App app = event.getApp();
        appUserService.favoriteApp(app, !app.isFavorite(), new AppsCallback<Void>() {
            @Override
            public void onFailure(Integer statusCode, Throwable caught) {
                announcer.schedule(new ErrorAnnouncementConfig(appearance.favServiceFailure()));
            }

            @Override
            public void onSuccess(Void result) {
                app.setFavorite(!app.isFavorite());
                eventBus.fireEvent(new AppFavoritedEvent(app));
                eventBus.fireEvent(new AppUpdatedEvent(app));
            }
        });
    }

    @Override
    public void onAppNameSelected(AppNameSelectedEvent event) {
        doRunApp(event.getSelectedApp());
    }

    @Override
    public void onAppRatingDeselected(final AppRatingDeselected event) {
        final App appToUnRate = event.getApp();
        appUserService.deleteRating(appToUnRate, new DeleteRatingCallback(appToUnRate,
                                                                          eventBus));
    }

    @Override
    public void onAppRatingSelected(final AppRatingSelected event) {
        final App appToRate = event.getApp();
        appUserService.rateApp(appToRate,
                               event.getScore(),
                               new RateAppCallback(appToRate,
                                                   eventBus));
    }

    @Override
    public void onAppSearchResultLoad(AppSearchResultLoadEvent event) {
        tileView.onAppSearchResultLoad(event);
        gridView.onAppSearchResultLoad(event);

        activeView.setSearchPattern(event.getSearchPattern());
        listStore.clear();
        listStore.addAll(event.getResults());
    }

    @Override
    public void onAppUpdated(final AppUpdatedEvent event) {
        App app = event.getApp();
        if (listStore.findModel(app) != null) {
            listStore.update(app);
        }
    }

    @Override
    public void onDeleteAppsSelected(final DeleteAppsSelected event) {
        appUserService.deleteAppsFromWorkspace(event.getAppsToBeDeleted(),
                                               new AppsCallback<Void>() {
                                                   @Override
                                                   public void onFailure(Integer statusCode, Throwable caught) {
                                                       ErrorHandler.post(appearance.appRemoveFailure(), caught);
                                                   }

                                                   @Override
                                                   public void onSuccess(Void result) {
                                                       for (App app : event.getAppsToBeDeleted()) {
                                                           listStore.remove(app);
                                                       }
                                                   }
                                               });
    }

    @Override
    public void onRunAppSelected(RunAppSelected event) {
        doRunApp(event.getApp());
    }

    public void setDesiredSelectedApp(final App desiredSelectedApp) {
        this.desiredSelectedApp = desiredSelectedApp;
    }

    void doRunApp(final App app) {
        if (app.isRunnable()) {
            if (!app.isDisabled()) {
                eventBus.fireEvent(new RunAppEvent(app));
            }
        } else {
            announcer.schedule(new ErrorAnnouncementConfig(appearance.appLaunchWithoutToolError()));
        }
    }

    @Override
    public void onSwapViewButtonClicked(SwapViewButtonClickedEvent event) {
        activeView.deselectAll();
        if (activeView == gridView) {
            activeView = tileView;
        } else {
            activeView = gridView;
        }
        cards.setActiveWidget(activeView);
    }

    HandlerManager createHandlerManager() {
        return new HandlerManager(this);
    }

    HandlerManager ensureHandlers() {
        return handlerManager == null ? handlerManager = createHandlerManager() : handlerManager;
    }

    public void fireEvent(GwtEvent<?> event) {
        if (handlerManager != null) {
            handlerManager.fireEvent(event);
        }
    }

    void postToErrorHandler(Throwable caught) {
        ErrorHandler.post(caught);
    }
}
