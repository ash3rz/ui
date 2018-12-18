package org.iplantc.de.admin.desktop.client.toolAdmin.presenter;

import org.iplantc.de.admin.desktop.client.toolAdmin.ToolAdminView;
import org.iplantc.de.admin.desktop.client.toolAdmin.events.AddToolSelectedEvent;
import org.iplantc.de.admin.desktop.client.toolAdmin.events.DeleteToolSelectedEvent;
import org.iplantc.de.admin.desktop.client.toolAdmin.events.SaveToolSelectedEvent;
import org.iplantc.de.admin.desktop.client.toolAdmin.events.ToolSelectedEvent;
import org.iplantc.de.admin.desktop.client.toolAdmin.gin.factory.ToolAdminViewFactory;
import org.iplantc.de.admin.desktop.client.toolAdmin.model.ToolProperties;
import org.iplantc.de.admin.desktop.client.toolAdmin.service.ToolAdminServiceFacade;
import org.iplantc.de.admin.desktop.client.toolAdmin.view.dialogs.DeleteToolDialog;
import org.iplantc.de.admin.desktop.client.toolAdmin.view.dialogs.OverwriteToolDialog;
import org.iplantc.de.admin.desktop.client.toolAdmin.view.dialogs.ToolAdminDetailsDialog;
import org.iplantc.de.admin.desktop.shared.Belphegor;
import org.iplantc.de.client.models.errorHandling.ServiceErrorCode;
import org.iplantc.de.client.models.errorHandling.SimpleServiceError;
import org.iplantc.de.client.models.tool.InteractiveApp;
import org.iplantc.de.client.models.tool.Tool;
import org.iplantc.de.client.models.tool.ToolAutoBeanFactory;
import org.iplantc.de.client.models.tool.ToolList;
import org.iplantc.de.client.models.tool.ToolType;
import org.iplantc.de.commons.client.ErrorHandler;
import org.iplantc.de.commons.client.info.IplantAnnouncer;
import org.iplantc.de.commons.client.info.SuccessAnnouncementConfig;
import org.iplantc.de.shared.AsyncProviderWrapper;
import org.iplantc.de.shared.DEProperties;

import com.google.common.collect.Lists;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasOneWidget;
import com.google.inject.Inject;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;

import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.event.SelectEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * @author aramsey
 */
public class ToolAdminPresenterImpl implements ToolAdminView.Presenter,
                                               AddToolSelectedEvent.AddToolSelectedEventHandler,
                                               ToolSelectedEvent.ToolSelectedEventHandler,
                                               SaveToolSelectedEvent.SaveToolSelectedEventHandler,
                                               DeleteToolSelectedEvent.DeleteToolSelectedEventHandler {

    private final ToolAdminView view;
    private final ToolAdminServiceFacade toolAdminServiceFacade;
    private final ToolAutoBeanFactory factory;
    private final ToolAdminView.ToolAdminViewAppearance appearance;
    private final ListStore<Tool> listStore;
    @Inject IplantAnnouncer announcer;
    @Inject DEProperties deProperties;
    @Inject AsyncProviderWrapper<OverwriteToolDialog> overwriteAppDialog;
    @Inject AsyncProviderWrapper<DeleteToolDialog> deleteAppDialog;

    @Inject
    public ToolAdminPresenterImpl(final ToolAdminViewFactory viewFactory,
                                  ToolAdminServiceFacade toolAdminServiceFacade,
                                  ToolAutoBeanFactory factory,
                                  ToolProperties toolProperties,
                                  ToolAdminView.ToolAdminViewAppearance appearance) {
        this.listStore = createListStore(toolProperties);
        this.view = viewFactory.create(listStore);
        view.addAddToolSelectedEventHandler(this);
        view.addSaveToolSelectedEventHandler(this);
        view.addToolSelectedEventHandler(this);
        view.addDeleteToolSelectedEventHandler(this);
        this.factory = factory;
        this.appearance = appearance;
        this.toolAdminServiceFacade = toolAdminServiceFacade;
    }

    @Override
    public void go(HasOneWidget container) {
        toolAdminServiceFacade.getToolTypes(new AsyncCallback<List<ToolType>>() {
            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.post(caught);
            }

            @Override
            public void onSuccess(List<ToolType> result) {
                List<String> toolTypes = Lists.newArrayList();
                result.forEach(toolType -> toolTypes.add(toolType.getName()));
                view.setToolTypes(toolTypes);
            }
        });
        container.setWidget(view);
        updateView();

    }

    @Override
    public void setViewDebugId(String baseId) {
        view.asWidget().ensureDebugId(baseId + Belphegor.ToolAdminIds.VIEW);
    }

    ListStore<Tool> createListStore(final ToolProperties toolProps) {
        final ListStore<Tool> listStore = new ListStore<>(toolProps.id());
        listStore.setEnableFilters(true);
        return listStore;
    }

    @Override
    public void onAddToolSelected(AddToolSelectedEvent event) {
        //The UI handles creating a single tool request, but the admin/tools POST endpoint requires
        // an array of requests.  Wrapping the request inside an array.
        Tool tool = event.getTool();
        checkForViceTool(tool);
        ToolList toolList = factory.getToolList().as();
        List<Tool> listTool = new ArrayList<>();
        listTool.add(tool);
        toolList.setToolList(listTool);

        toolAdminServiceFacade.addTool(toolList, new AsyncCallback<Void>() {

            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.post(caught);
            }

            @Override
            public void onSuccess(Void result) {
                announcer.schedule(new SuccessAnnouncementConfig(appearance.addToolSuccessText()));
                updateView();
            }

        });
    }

    @Override
    public void onDeleteToolSelected(final DeleteToolSelectedEvent event) {
        toolAdminServiceFacade.deleteTool(event.getTool().getId(), new AsyncCallback<Void>() {
            @Override
            public void onFailure(final Throwable caught) {
                String serviceError = getServiceError(caught);
                if (serviceError.equals(ServiceErrorCode.ERR_NOT_WRITEABLE.toString())) {
                    deleteAppDialog.get(new AsyncCallback<DeleteToolDialog>() {
                        @Override
                        public void onFailure(Throwable caught) {
                            ErrorHandler.post(caught);
                        }

                        @Override
                        public void onSuccess(DeleteToolDialog result) {
                            result.setText(caught);
                            result.show();
                        }
                    });
                } else {
                    ErrorHandler.post(caught);
                }
            }

            @Override
            public void onSuccess(Void result) {
                announcer.schedule(new SuccessAnnouncementConfig(appearance.deleteToolSuccessText()));
                listStore.remove(listStore.findModelWithKey(event.getTool().getId()));
            }
        });
    }

    @Override
    public void onToolSelected(ToolSelectedEvent event) {
        toolAdminServiceFacade.getToolDetails(event.getTool().getId(), new AsyncCallback<Tool>() {
            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.post(caught);
            }

            @Override
            public void onSuccess(Tool result) {
                view.editToolDetails(result, ToolAdminDetailsDialog.Mode.EDIT);
            }
        });
    }

    @Override
    public void onSaveToolSelected(SaveToolSelectedEvent event) {
        updateTool(event.getTool(), false);
    }

    void updateTool(final Tool tool, final boolean overwrite) {
        checkForViceTool(tool);
        toolAdminServiceFacade.updateTool(tool, overwrite, new AsyncCallback<Void>() {
            @Override
            public void onFailure(final Throwable caught) {
                String serviceError = getServiceError(caught);
                if (ServiceErrorCode.ERR_NOT_WRITEABLE.toString().equals(serviceError)) {
                    overwriteAppDialog.get(new AsyncCallback<OverwriteToolDialog>() {
                        @Override
                        public void onFailure(Throwable caught) {
                            ErrorHandler.post(caught);
                        }

                        @Override
                        public void onSuccess(OverwriteToolDialog result) {
                            result.setText(caught);
                            result.show();
                            result.addOkButtonSelectHandler(new SelectEvent.SelectHandler() {
                                @Override
                                public void onSelect(SelectEvent event) {
                                    updateTool(tool, true);
                                }
                            });
                        }
                    });

                } else {
                    ErrorHandler.post(caught);
                }
            }

            @Override
            public void onSuccess(Void result) {
                announcer.schedule(new SuccessAnnouncementConfig(appearance.updateToolSuccessText()));
                updateView();
            }
        });
    }

    void checkForViceTool(Tool tool) {
        if ("interactive".equals(tool.getType()) && tool.getContainer().getInteractiveApps() == null) {
            appendDefaultInteractiveAppValues(tool);
        }
    }

    void appendDefaultInteractiveAppValues(Tool tool) {
        InteractiveApp interactiveApp = factory.getInteractiveApp().as();
        interactiveApp.setImage(deProperties.getDefaultViceImage());
        interactiveApp.setName(deProperties.getDefaultViceName());
        interactiveApp.setCasUrl(deProperties.getDefaultViceCasUrl());
        interactiveApp.setCasValidate(deProperties.getDefaultViceCasValidate());

        tool.getContainer().setInteractiveApps(interactiveApp);
    }

    String getServiceError(Throwable caught) {
        try {
            SimpleServiceError simpleServiceError =
                    AutoBeanCodex.decode(factory, SimpleServiceError.class, caught.getMessage()).as();
            return simpleServiceError.getErrorCode();
        }
        catch (Exception e) {
            return null;
        }
    }

    void updateView() {
        String searchTerm = "*";
        updateView(searchTerm);
    }

    void updateView(String searchTerm) {
        toolAdminServiceFacade.getTools(searchTerm, new AsyncCallback<List<Tool>>() {
            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.post(caught);
            }

            @Override
            public void onSuccess(List<Tool> result) {
                listStore.replaceAll(result);
            }
        });
    }
}
