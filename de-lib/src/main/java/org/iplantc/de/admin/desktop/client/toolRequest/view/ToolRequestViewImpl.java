package org.iplantc.de.admin.desktop.client.toolRequest.view;

import org.iplantc.de.admin.desktop.client.toolRequest.ToolRequestView;
import org.iplantc.de.admin.desktop.client.toolRequest.view.cells.RequestNameCell;
import org.iplantc.de.admin.desktop.shared.Belphegor;
import org.iplantc.de.client.models.toolRequests.ToolRequest;
import org.iplantc.de.client.models.toolRequests.ToolRequestAutoBeanFactory;
import org.iplantc.de.client.models.toolRequests.ToolRequestDetails;
import org.iplantc.de.client.models.toolRequests.ToolRequestUpdate;
import org.iplantc.de.client.util.StaticIdHelper;

import com.google.common.collect.Lists;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

import com.sencha.gxt.core.client.IdentityValueProvider;
import com.sencha.gxt.core.client.Style.SelectionMode;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.SortDir;
import com.sencha.gxt.data.shared.Store;
import com.sencha.gxt.widget.core.client.Composite;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent.SelectionChangedHandler;

import java.util.Date;
import java.util.List;

/**
 * @author jstroot
 */
public class ToolRequestViewImpl extends Composite implements ToolRequestView, SelectionChangedHandler<ToolRequest> {

    private static ToolRequestViewImplUiBinder uiBinder = GWT.create(ToolRequestViewImplUiBinder.class);

    interface ToolRequestViewImplUiBinder extends UiBinder<Widget, ToolRequestViewImpl> {
    }

    @UiField TextButton updateBtn;
    @UiField Grid<ToolRequest> grid;
    @UiField ListStore<ToolRequest> store;
    @UiField ToolRequestDetailsPanel detailsPanel;
    @UiField(provided = true) ToolRequestViewAppearance appearance;

    private final ToolRequestProperties trProps;
    private ToolRequestView.Presenter presenter;

    private final ToolRequestAutoBeanFactory factory;

    @Inject
    public ToolRequestViewImpl(final ToolRequestProperties trProps,
                               final ToolRequestAutoBeanFactory factory,
                               final ToolRequestViewAppearance appearance) {
        this.trProps = trProps;
        this.factory = factory;
        this.appearance = appearance;
        initWidget(uiBinder.createAndBindUi(this));
        grid.getSelectionModel().addSelectionChangedHandler(this);
        grid.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        //show latest tool request first
        store.addSortInfo(new Store.StoreSortInfo<ToolRequest>(trProps.dateSubmitted(), SortDir.DESC));
    }

    @Override
    public void setPresenter(Presenter presenter) {
        this.presenter = presenter;
    }

    @UiFactory
    ListStore<ToolRequest> createListStore() {
        return new ListStore<>(trProps.id());
    }

    @UiFactory
    ColumnModel<ToolRequest> createColumnModel() {
        List<ColumnConfig<ToolRequest, ?>> list = Lists.newArrayList();
        ColumnConfig<ToolRequest, ToolRequest> nameCol = new ColumnConfig<>(new IdentityValueProvider<>(),
                                                                       appearance.nameColumnWidth(),
                                                                       appearance.nameColumnLabel());
        ColumnConfig<ToolRequest, String> statusCol = new ColumnConfig<>(trProps.status(),
                                                                         appearance.statusColumnWidth(),
                                                                         appearance.statusColumnLabel());
        ColumnConfig<ToolRequest, Date> dateSubmittedCol = new ColumnConfig<>(trProps.dateSubmitted(),
                                                                              appearance.dateSubmittedColumnWidth(),
                                                                              appearance.dateSubmittedColumnLabel());
        ColumnConfig<ToolRequest, Date> dateUpdatedCol = new ColumnConfig<>(trProps.dateUpdated(),
                                                                            appearance.dateUpdatedColumnWidth(),
                                                                            appearance.dateUpdatedColumnLabel());
        ColumnConfig<ToolRequest, String> updatedByCol = new ColumnConfig<>(trProps.updatedBy(),
                                                                            appearance.updatedByColumnWidth(),
                                                                            appearance.updatedByColumnLabel());
        ColumnConfig<ToolRequest, String> versionCol = new ColumnConfig<>(trProps.version(),
                                                                          appearance.versionColumnWidth(),
                                                                          appearance.versionColumnLabel());

        list.add(nameCol);
        nameCol.setCell(new RequestNameCell());
        list.add(statusCol);
        list.add(dateSubmittedCol);
        list.add(dateUpdatedCol);
        list.add(updatedByCol);
        list.add(versionCol);
        return new ColumnModel<>(list);
    }

    @UiHandler("updateBtn")
    void onUpdateBtnClicked(SelectEvent event) {
        final UpdateToolRequestDialog updateToolRequestDialog = new UpdateToolRequestDialog(grid.getSelectionModel().getSelectedItem(), factory);
        updateToolRequestDialog.addOkButtonSelectHandler(event1 -> {
            ToolRequestUpdate tru = updateToolRequestDialog.getToolRequestUpdate();
            presenter.updateToolRequest(grid.getSelectionModel().getSelectedItem().getId(), tru);
        });
        updateToolRequestDialog.setSize(appearance.updateToolRequestDlgWidth(), appearance.updateToolRequestDlgHeight());
        updateToolRequestDialog.show();
    }

    @Override
    public void onSelectionChanged(SelectionChangedEvent<ToolRequest> event) {
        boolean isSingleItemSelected = event.getSelection().size() == 1;
        updateBtn.setEnabled(isSingleItemSelected);
        if (event.getSelection().size() == 0) {
            // JDS Send a null to the details panel to clear it.
            detailsPanel.edit(null);
        }
        if (isSingleItemSelected) {
            presenter.fetchToolRequestDetails(event.getSelection().get(0));
        }

    }

    @Override
    public void setToolRequests(List<ToolRequest> toolRequests) {
        store.addAll(toolRequests);
    }

    @Override
    public void maskDetailsPanel(String loadingMask) {
        detailsPanel.mask(loadingMask);
    }

    @Override
    public void unmaskDetailsPanel() {
        detailsPanel.unmask();
    }

    @Override
    public void setDetailsPanel(ToolRequestDetails toolRequestDetails) {
        detailsPanel.edit(toolRequestDetails);
    }

    @Override
    public void update(ToolRequestUpdate toolRequestUpdate, ToolRequestDetails toolRequestDetails) {

        final ToolRequest findModelWithKey = store.findModelWithKey(toolRequestDetails.getId());
        if (findModelWithKey != null) {
            findModelWithKey.setStatus(toolRequestUpdate.getStatus());
            findModelWithKey.setDateUpdated(new Date());
            store.update(findModelWithKey);
        }
        detailsPanel.edit(toolRequestDetails);
    }

    @Override
    protected void onEnsureDebugId(final String baseID) {
        super.onEnsureDebugId(baseID);

        updateBtn.ensureDebugId(baseID + Belphegor.ToolRequestIds.UPDATE);
        grid.ensureDebugId(baseID + Belphegor.ToolRequestIds.GRID);
        grid.addViewReadyHandler(event -> StaticIdHelper.getInstance()
                                                .gridColumnHeaders(baseID + Belphegor.ToolRequestIds.GRID
                                         + Belphegor.ToolRequestIds.COL_HEADER, grid));

        detailsPanel.ensureDebugId(baseID + Belphegor.ToolRequestIds.DETAILS_PANEL);

    }

    @Override
    public ToolRequestDetailsPanel getDetailsPanel() {
        return detailsPanel;
    }
}
