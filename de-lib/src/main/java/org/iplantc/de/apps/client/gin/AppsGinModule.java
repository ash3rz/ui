package org.iplantc.de.apps.client.gin;

import org.iplantc.de.apps.client.AppCategoriesView;
import org.iplantc.de.apps.client.AppDetailsView;
import org.iplantc.de.apps.client.AppsListView;
import org.iplantc.de.apps.client.AppsToolbarView;
import org.iplantc.de.apps.client.AppsView;
import org.iplantc.de.apps.client.CommunitiesView;
import org.iplantc.de.apps.client.OntologyHierarchiesView;
import org.iplantc.de.apps.client.SubmitAppForPublicUseView;
import org.iplantc.de.apps.client.WorkspaceView;
import org.iplantc.de.apps.client.gin.factory.AppCategoriesViewFactory;
import org.iplantc.de.apps.client.gin.factory.AppSharingPresenterFactory;
import org.iplantc.de.apps.client.gin.factory.AppsToolbarViewFactory;
import org.iplantc.de.apps.client.gin.factory.AppsViewFactory;
import org.iplantc.de.apps.client.gin.factory.CommunitiesViewFactory;
import org.iplantc.de.apps.client.gin.factory.OntologyHierarchiesViewFactory;
import org.iplantc.de.apps.client.presenter.AppsViewPresenterImpl;
import org.iplantc.de.apps.client.presenter.WorkspacePresenterImpl;
import org.iplantc.de.apps.client.presenter.categories.AppCategoriesPresenterImpl;
import org.iplantc.de.apps.client.presenter.communities.CommunitiesPresenterImpl;
import org.iplantc.de.apps.client.presenter.details.AppDetailsViewPresenterImpl;
import org.iplantc.de.apps.client.presenter.hierarchies.OntologyHierarchiesPresenterImpl;
import org.iplantc.de.apps.client.presenter.list.AppsListPresenterImpl;
import org.iplantc.de.apps.client.presenter.sharing.AppSharingPresenter;
import org.iplantc.de.apps.client.presenter.submit.SubmitAppForPublicPresenter;
import org.iplantc.de.apps.client.presenter.toolBar.AppsToolbarPresenterImpl;
import org.iplantc.de.apps.client.views.AppsViewImpl;
import org.iplantc.de.apps.client.views.WorkspaceViewImpl;
import org.iplantc.de.apps.client.views.categories.AppCategoriesViewImpl;
import org.iplantc.de.apps.client.views.communities.CommunitiesViewImpl;
import org.iplantc.de.apps.client.views.details.AppDetailsViewImpl;
import org.iplantc.de.apps.client.views.hierarchies.OntologyHierarchiesViewImpl;
import org.iplantc.de.apps.client.views.list.AppListViewImpl;
import org.iplantc.de.apps.client.views.sharing.AppSharingView;
import org.iplantc.de.apps.client.views.sharing.AppSharingViewImpl;
import org.iplantc.de.apps.client.views.submit.SubmitAppForPublicUseViewImpl;
import org.iplantc.de.apps.client.views.toolBar.AppsViewToolbarImpl;
import org.iplantc.de.client.models.apps.AppCategory;
import org.iplantc.de.client.models.ontologies.OntologyHierarchy;
import org.iplantc.de.client.services.AppMetadataServiceFacade;
import org.iplantc.de.client.services.OntologyLookupServiceFacade;
import org.iplantc.de.client.services.OntologyServiceFacade;
import org.iplantc.de.client.services.impl.AppMetadataServiceFacadeImpl;
import org.iplantc.de.client.services.impl.OntologyLookupServiceFacadeImpl;
import org.iplantc.de.client.services.impl.OntologyServiceFacadeImpl;
import org.iplantc.de.commons.client.presenter.SharingPresenter;

import com.google.gwt.inject.client.AbstractGinModule;
import com.google.gwt.inject.client.assistedinject.GinFactoryModuleBuilder;
import com.google.inject.TypeLiteral;

import com.sencha.gxt.data.shared.TreeStore;

/**
 * @author jstroot
 */
public class AppsGinModule extends AbstractGinModule {

    @Override
    protected void configure() {
        bind(new TypeLiteral<TreeStore<AppCategory>>() {
        }).toProvider(AppCategoryTreeStoreProvider.class);
        bind(new TypeLiteral<TreeStore<OntologyHierarchy>>() {
        }).toProvider(OntologyHierarchyTreeStoreProvider.class);

        bind(SubmitAppForPublicUseView.class).to(SubmitAppForPublicUseViewImpl.class);
        bind(SubmitAppForPublicUseView.Presenter.class).to(SubmitAppForPublicPresenter.class);
        bind(AppMetadataServiceFacade.class).to(AppMetadataServiceFacadeImpl.class);
        bind(OntologyServiceFacade.class).to(OntologyServiceFacadeImpl.class);
        bind(OntologyLookupServiceFacade.class).to(OntologyLookupServiceFacadeImpl.class);

        // Main View
        install(new GinFactoryModuleBuilder().implement(AppsView.class, AppsViewImpl.class)
                                             .build(AppsViewFactory.class));
        bind(AppsView.Presenter.class).to(AppsViewPresenterImpl.class);

        // Categories View
        install(new GinFactoryModuleBuilder().implement(AppCategoriesView.class,
                                                        AppCategoriesViewImpl.class)
                                             .build(AppCategoriesViewFactory.class));
        bind(AppCategoriesView.Presenter.class).to(AppCategoriesPresenterImpl.class);

        bind(WorkspaceView.class).to(WorkspaceViewImpl.class);
        bind(WorkspaceView.Presenter.class).to(WorkspacePresenterImpl.class);

        // Hierarchies View
        install(new GinFactoryModuleBuilder().implement(OntologyHierarchiesView.class,
                                                        OntologyHierarchiesViewImpl.class)
                                             .build(OntologyHierarchiesViewFactory.class));
        bind(OntologyHierarchiesView.Presenter.class).to(OntologyHierarchiesPresenterImpl.class);

        // Communities View
        install(new GinFactoryModuleBuilder().implement(CommunitiesView.class, CommunitiesViewImpl.class)
                                             .build(CommunitiesViewFactory.class));
        bind(CommunitiesView.Presenter.class).to(CommunitiesPresenterImpl.class);

        // List View
        bind(AppsListView.Presenter.class).to(AppsListPresenterImpl.class);


        // Toolbar View
        install(new GinFactoryModuleBuilder().implement(AppsToolbarView.class, AppsViewToolbarImpl.class)
                                             .build(AppsToolbarViewFactory.class));
        bind(AppsToolbarView.Presenter.class).to(AppsToolbarPresenterImpl.class);

        // Details
        bind(AppDetailsView.class).to(AppDetailsViewImpl.class);

        bind(AppDetailsView.Presenter.class).to(AppDetailsViewPresenterImpl.class);

        bind(AppSharingView.class).to(AppSharingViewImpl.class);
        install(new GinFactoryModuleBuilder().implement(SharingPresenter.class,
                                                        AppSharingPresenter.class)
                                             .build(AppSharingPresenterFactory.class));

        bind(AppsListView.class).to(AppListViewImpl.class);

    }

}

