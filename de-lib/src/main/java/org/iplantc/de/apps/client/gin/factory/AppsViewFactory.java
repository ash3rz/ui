package org.iplantc.de.apps.client.gin.factory;

import org.iplantc.de.apps.client.AppCategoriesView;
import org.iplantc.de.apps.client.AppsListView;
import org.iplantc.de.apps.client.AppsToolbarView;
import org.iplantc.de.apps.client.AppsView;
import org.iplantc.de.apps.client.CommunitiesView;
import org.iplantc.de.apps.client.OntologyHierarchiesView;

/**
 * Created by jstroot on 2/24/15.
 * @author jstroot
 */
public interface AppsViewFactory {
    AppsView create(AppCategoriesView.Presenter categoriesPresenter,
                    CommunitiesView.Presenter communitiesPresenter,
                    OntologyHierarchiesView.Presenter hierarchiesPresenter,
                    AppsListView.Presenter appsGridPresenter,
                    AppsToolbarView.Presenter toolbarPresenter);
}
