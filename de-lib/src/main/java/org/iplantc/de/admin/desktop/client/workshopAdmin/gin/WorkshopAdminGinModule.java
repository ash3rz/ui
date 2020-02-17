package org.iplantc.de.admin.desktop.client.workshopAdmin.gin;

import com.google.gwt.inject.client.AbstractGinModule;
import com.google.gwt.inject.client.assistedinject.GinFactoryModuleBuilder;
import org.iplantc.de.admin.desktop.client.workshopAdmin.WorkshopAdminView;
import org.iplantc.de.admin.desktop.client.workshopAdmin.gin.factory.WorkshopAdminViewFactory;
import org.iplantc.de.admin.desktop.client.workshopAdmin.presenter.WorkshopAdminPresenterImpl;
import org.iplantc.de.admin.desktop.client.workshopAdmin.service.WorkshopAdminServiceFacade;
import org.iplantc.de.admin.desktop.client.workshopAdmin.service.impl.WorkshopAdminServiceFacadeImpl;
import org.iplantc.de.admin.desktop.client.workshopAdmin.view.WorkshopAdminViewImpl;

/**
 * @author sarahr
 */
public class WorkshopAdminGinModule extends AbstractGinModule {
    @Override
    protected void configure() {
        install(new GinFactoryModuleBuilder().implement(WorkshopAdminView.class, WorkshopAdminViewImpl.class)
                                             .build(WorkshopAdminViewFactory.class));

        bind(WorkshopAdminView.Presenter.class).to(WorkshopAdminPresenterImpl.class);
        bind(WorkshopAdminServiceFacade.class).to(WorkshopAdminServiceFacadeImpl.class);
    }
}
