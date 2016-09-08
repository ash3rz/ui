package org.iplantc.de.apps.client.presenter;

import org.iplantc.de.apps.client.AppCategoriesView;
import org.iplantc.de.apps.client.AppsListView;
import org.iplantc.de.apps.client.AppsToolbarView;
import org.iplantc.de.apps.client.AppsView;
import org.iplantc.de.apps.client.OntologyHierarchiesView;
import org.iplantc.de.apps.client.gin.factory.AppsViewFactory;
import org.iplantc.de.client.models.HasId;
import org.iplantc.de.client.models.apps.App;
import org.iplantc.de.client.models.apps.AppCategory;
import org.iplantc.de.commons.client.widgets.DETabPanel;

import com.google.gwt.user.client.ui.HasOneWidget;
import com.google.inject.Inject;

import com.sencha.gxt.widget.core.client.TabPanel;
import com.sencha.gxt.widget.core.client.grid.Grid;

/**
 * The presenter for the AppsView.
 *
 * @author jstroot
 */
public class AppsViewPresenterImpl implements AppsView.Presenter {

    protected final AppsView view;
    private final AppCategoriesView.Presenter categoriesPresenter;
    private final AppsListView.Presenter appsGridPresenter;
    private final OntologyHierarchiesView.Presenter hierarchiesPresenter;

    @Inject
    protected AppsViewPresenterImpl(final AppsViewFactory viewFactory,
                                    final AppCategoriesView.Presenter categoriesPresenter,
                                    final AppsListView.Presenter appsGridPresenter,
                                    final AppsToolbarView.Presenter toolbarPresenter,
                                    final OntologyHierarchiesView.Presenter hierarchiesPresenter) {
        this.categoriesPresenter = categoriesPresenter;
        this.appsGridPresenter = appsGridPresenter;
        this.hierarchiesPresenter = hierarchiesPresenter;
        this.view = viewFactory.create(categoriesPresenter,
                                       hierarchiesPresenter,
                                       appsGridPresenter,
                                       toolbarPresenter);

        categoriesPresenter.addAppCategorySelectedEventHandler(appsGridPresenter);
        categoriesPresenter.addAppCategorySelectedEventHandler(appsGridPresenter.getView());
        categoriesPresenter.addAppCategorySelectedEventHandler(toolbarPresenter.getView());

        hierarchiesPresenter.addOntologyHierarchySelectionChangedEventHandler(appsGridPresenter);
        hierarchiesPresenter.addOntologyHierarchySelectionChangedEventHandler(appsGridPresenter.getView());
        hierarchiesPresenter.addOntologyHierarchySelectionChangedEventHandler(toolbarPresenter.getView());

        appsGridPresenter.getView().addAppSelectionChangedEventHandler(toolbarPresenter.getView());
        appsGridPresenter.getView().addAppInfoSelectedEventHandler(hierarchiesPresenter);

        toolbarPresenter.getView().addDeleteAppsSelectedHandler(appsGridPresenter);
        toolbarPresenter.getView().addCopyAppSelectedHandler(categoriesPresenter);
        toolbarPresenter.getView().addCopyWorkflowSelectedHandler(categoriesPresenter);
        toolbarPresenter.getView().addRunAppSelectedHandler(appsGridPresenter);
        toolbarPresenter.getView().addBeforeAppSearchEventHandler(appsGridPresenter.getView());
        toolbarPresenter.getView().addAppSearchResultLoadEventHandler(categoriesPresenter);
        toolbarPresenter.getView().addAppSearchResultLoadEventHandler(hierarchiesPresenter);
        toolbarPresenter.getView().addAppSearchResultLoadEventHandler(appsGridPresenter);
        toolbarPresenter.getView().addAppSearchResultLoadEventHandler(appsGridPresenter.getView());
        toolbarPresenter.getView().addSwapViewButtonClickedEventHandler(appsGridPresenter);

    }

    @Override
    public Grid<App> getAppsGrid() {
        // FIXME Too many levels of misdirection
        return null;
    }

    @Override
    public App getSelectedApp() {
        return appsGridPresenter.getSelectedApp();
    }

    @Override
    public AppCategory getSelectedAppCategory() {
        return categoriesPresenter.getSelectedAppCategory();
    }

    @Override
    public void go(final HasOneWidget container,
                   final HasId selectedAppCategory,
                   final HasId selectedApp) {
        DETabPanel tabPanel = view.getCategoryTabPanel();
        if (isEmpty(tabPanel)) {
            categoriesPresenter.go(selectedAppCategory, tabPanel);
            hierarchiesPresenter.go(tabPanel);
        }
        container.setWidget(view);
    }

    boolean isEmpty(TabPanel tabPanel) {
        return tabPanel.getWidgetCount() == 0;
    }

    @Override
    public AppsView.Presenter hideAppMenu() {
        view.hideAppMenu();
        return this;
    }

    @Override
    public AppsView.Presenter hideWorkflowMenu() {
        view.hideWorkflowMenu();
        return this;
    }

    @Override
    public void setViewDebugId(String baseId) {
        view.asWidget().ensureDebugId(baseId);
    }

}
