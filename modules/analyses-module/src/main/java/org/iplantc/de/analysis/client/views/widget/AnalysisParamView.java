package org.iplantc.de.analysis.client.views.widget;

import org.iplantc.de.analysis.client.events.SaveAnalysisParametersEvent;
import org.iplantc.de.client.models.IsHideable;
import org.iplantc.de.client.models.IsMaskable;
import org.iplantc.de.client.models.analysis.AnalysisParameter;
import org.iplantc.de.commons.client.views.dialogs.IPlantDialog;
import org.iplantc.de.diskResource.client.gin.factory.DiskResourceSelectorDialogFactory;
import org.iplantc.de.diskResource.client.views.dialogs.SaveAsDialog;
import org.iplantc.de.resources.client.messages.IplantDisplayStrings;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer.BorderLayoutData;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.toolbar.ToolBar;

import java.util.List;

/**
 * FIXME JDS Fix debug ids.
 */
public class AnalysisParamView implements IsWidget, SaveAnalysisParametersEvent.HasSaveAnalysisParametersEventHandlers {

    private static AnalysisParamViewUiBinder uiBinder = GWT.create(AnalysisParamViewUiBinder.class);

    interface AnalysisParamViewUiBinder extends UiBinder<Widget, AnalysisParamView> {
    }

    private final IplantDisplayStrings displayStrings;

    @UiField(provided = true) final ListStore<AnalysisParameter> listStore;
    @UiField(provided = true) final ColumnModel<AnalysisParameter> cm;
    @UiField Grid<AnalysisParameter> grid;
    @UiField BorderLayoutContainer con;
    @UiField ToolBar menuToolBar;
    @UiField BorderLayoutData northData;
    @UiField IPlantDialog dialog;
    @UiField TextButton btnSave;

    private final Widget widget;

    @Inject DiskResourceSelectorDialogFactory dialogFactory;

    @Inject
    AnalysisParamView(final IplantDisplayStrings displayStrings,
                      @Assisted final AnalysisParamViewColumnModel cm,
                      @Assisted final ListStore<AnalysisParameter> listStore) {
        this.cm = cm;
        this.listStore = listStore;
        this.displayStrings = displayStrings;
        this.widget = uiBinder.createAndBindUi(this);
        grid.getView().setEmptyText(displayStrings.noParameters());
    }

    @Override
    public HandlerRegistration addSaveAnalysisParametersEventHandler(SaveAnalysisParametersEvent.SaveAnalysisParametersEventHandler handler) {
        return widget.addHandler(handler, SaveAnalysisParametersEvent.TYPE);
    }

    @Override
    public Widget asWidget() {
        return widget;
    }

    public void loadParameters(List<AnalysisParameter> items) {
        listStore.addAll(items);
    }

    public void show() {
        dialog.show();
    }

    public void setHeading(String heading) {
        dialog.setHeadingText(heading);
    }

    @UiHandler("btnSave")
    void onSaveClick(SelectEvent event) {
        final SaveAsDialog saveAsDialog = dialogFactory.createSaveAsDialog(null);
        saveAsDialog.addOkButtonSelectHandler(new SelectHandler() {

            @Override
            public void onSelect(SelectEvent event) {
                if (saveAsDialog.isValid()) {
                    String fileContents = writeTabFile();
                    saveFile(saveAsDialog.getSelectedFolder().getPath() + "/" + saveAsDialog.getFileName(),
                             fileContents, saveAsDialog, saveAsDialog);
                }
            }
        });

        saveAsDialog.addCancelButtonSelectHandler(new SelectHandler() {

            @Override
            public void onSelect(SelectEvent event) {
                saveAsDialog.hide();
            }
        });
        saveAsDialog.show();
        saveAsDialog.toFront();
    }

    public void mask() {
        con.mask(displayStrings.loadingMask());
    }

    public void unmask() {
        con.unmask();
    }

    private void saveFile(final String path, String fileContents, IsHideable hideable, IsMaskable maskable) {
        widget.fireEvent(new SaveAnalysisParametersEvent(path, fileContents, hideable, maskable));
    }

    private String writeTabFile() {
        StringBuilder sw = new StringBuilder();
        sw.append(displayStrings.paramName()).append("\t").append(displayStrings.paramType()).append("\t").append(displayStrings.paramValue()).append("\n");
        List<AnalysisParameter> params = grid.getStore().getAll();
        for (AnalysisParameter ap : params) {
            sw.append(ap.getName()).append("\t").append(ap.getType()).append("\t").append(ap.getDisplayValue()).append("\n");
        }

        return sw.toString();
    }

}
