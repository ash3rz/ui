package org.iplantc.de.diskResource.client.views.toolbar.dialogs;

import org.iplantc.de.client.models.diskResources.PathListRequest;
import org.iplantc.de.client.models.viewer.InfoType;
import org.iplantc.de.commons.client.views.dialogs.IPlantDialog;
import org.iplantc.de.diskResource.client.PathListAutomationView;

import com.google.inject.Inject;

import java.util.List;

/**
 * Created by sriram on 7/26/17.
 */
public class PathListAutomationDialog extends IPlantDialog {

    private PathListAutomationView.PathListAutomationAppearance htAppearance;
    private PathListAutomationView view;
    private InfoType pathListInfoType;

    @Inject
    public PathListAutomationDialog(PathListAutomationView.PathListAutomationAppearance htAppearance,
                                    PathListAutomationView view) {
        this.htAppearance = htAppearance;
        this.view = view;
        setWidget(view);
        setHideOnButtonClick(false);
        setSize(htAppearance.dialogWidth(), htAppearance.dialogHeight());
        setModal(false);
    }

    public void show(List<InfoType> infoTypes, InfoType pathListInfoType) {
        view.addInfoTypes(infoTypes);
        this.pathListInfoType = pathListInfoType;
        if (pathListInfoType.equals(InfoType.HT_ANALYSIS_PATH_LIST)) {
            setHeading(htAppearance.dialogHTHeading());
        } else if (pathListInfoType.equals(InfoType.MULTI_INPUT_PATH_LIST)) {
            setHeading(htAppearance.dialogMultiInputHeading());
        }
        super.show();
    }

    public PathListRequest getRequest() {
        PathListRequest request = view.getRequest();
        request.setPathListInfoType(pathListInfoType);
        return request;
    }

    public boolean isValid() {
        return view.isValid();
    }

    @Override
    public void show() throws UnsupportedOperationException{
        throw new UnsupportedOperationException("This method is not supported for this class. Use show(List<InfoType>, InfoType) instead.");
    }
}
