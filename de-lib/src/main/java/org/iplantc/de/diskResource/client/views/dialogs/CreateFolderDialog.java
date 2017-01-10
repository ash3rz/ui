package org.iplantc.de.diskResource.client.views.dialogs;

import org.iplantc.de.client.models.diskResources.Folder;
import org.iplantc.de.client.util.DiskResourceUtil;
import org.iplantc.de.commons.client.validators.DiskResourceNameValidator;
import org.iplantc.de.commons.client.views.dialogs.IPlantPromptDialog;
import org.iplantc.de.diskResource.client.DiskResourceView;

import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.ui.HTML;

import com.sencha.gxt.widget.core.client.event.SelectEvent;

/**
 * @author jstroot
 */
public class CreateFolderDialog extends IPlantPromptDialog {

    public interface Appearance {

        String dialogWidth();

        String folderName();

        String newFolder();

        SafeHtml renderDestinationPathLabel(String destPath, String createIn);
    }

    private final Appearance appearance;

    private final DiskResourceUtil diskResourceUtil;

    public CreateFolderDialog(final Folder parentFolder, final DiskResourceView.Presenter parentPresenter) {
        this(parentFolder,
             GWT.<Appearance> create(Appearance.class), parentPresenter);
    }

    public CreateFolderDialog(final Folder parentFolder,
                              final Appearance appearance, final DiskResourceView.Presenter parentPresenter) {
        super(appearance.folderName(), -1, "", new DiskResourceNameValidator());
        this.appearance = appearance;
        diskResourceUtil = DiskResourceUtil.getInstance();
        setWidth(appearance.dialogWidth());
        setHeading(appearance.newFolder());
        initDestPathLabel(parentFolder.getPath());
        addOkButtonSelectHandler(new SelectEvent.SelectHandler() {
            @Override
            public void onSelect(SelectEvent event) {
                parentPresenter.doCreateNewFolder(parentFolder, getFieldText());
            }
        });
    }

    private void initDestPathLabel(String destPath) {

        HTML htmlDestText = new HTML(appearance.renderDestinationPathLabel(destPath, diskResourceUtil.parseNameFromPath(destPath)));
        insert(htmlDestText, 0);
    }
}
