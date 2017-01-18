package org.iplantc.de.diskResource.client.presenters.callbacks;

import org.iplantc.de.client.models.IsMaskable;
import org.iplantc.de.client.models.diskResources.DiskResource;
import org.iplantc.de.client.models.diskResources.DiskResourceAutoBeanFactory;
import org.iplantc.de.client.models.diskResources.Folder;
import org.iplantc.de.client.models.diskResources.RestoreResponse;
import org.iplantc.de.client.models.diskResources.RestoreResponse.RestoredResource;
import org.iplantc.de.client.models.errors.diskResources.DiskResourceErrorAutoBeanFactory;
import org.iplantc.de.client.models.errors.diskResources.ErrorDiskResourceMove;
import org.iplantc.de.commons.client.ErrorHandler;
import org.iplantc.de.commons.client.info.IplantAnnouncer;
import org.iplantc.de.commons.client.info.SuccessAnnouncementConfig;
import org.iplantc.de.diskResource.client.events.selection.RefreshFolderSelected;

import com.google.gwt.core.client.GWT;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.google.web.bindery.autobean.shared.Splittable;

import java.util.List;

/**
 * A DiskResourceServiceCallback for data service "restore" endpoint requests.
 * 
 * @author psarando, jstroot
 * 
 */
public class DiskResourceRestoreCallback extends DiskResourceServiceCallback<String> {
    private final RefreshFolderSelected.RefreshFolderSelectedHandler refreshHandler;
    private final DiskResourceAutoBeanFactory drFactory;
    private final List<DiskResource> selectedResources;
    private final DiskResourceCallbackAppearance appearance = GWT.create(DiskResourceCallbackAppearance.class);
    private final Folder trashFolder;

    public DiskResourceRestoreCallback(final RefreshFolderSelected.RefreshFolderSelectedHandler refreshHandler,
                                       final IsMaskable maskable,
                                       final DiskResourceAutoBeanFactory drFactory,
                                       final Folder trashFolder,
                                       final List<DiskResource> selectedResources) {
        super(maskable);

        this.drFactory = drFactory;
        this.trashFolder = trashFolder;
        this.selectedResources = selectedResources;
        this.refreshHandler = refreshHandler;
    }

    @Override
    protected String getErrorMessageDefault() {
        return appearance.restoreDefaultMsg();
    }

    @Override
    public void onSuccess(String result) {
        super.onSuccess(result);

        checkForPartialRestore(result);
        refreshHandler.onRefreshFolderSelected(new RefreshFolderSelected(trashFolder));
    }

    @Override
    public void onFailure(Integer statusCode, Throwable caught) {
        unmaskCaller();
        DiskResourceErrorAutoBeanFactory factory = GWT.create(DiskResourceErrorAutoBeanFactory.class);
        AutoBean<ErrorDiskResourceMove> errorBean = AutoBeanCodex.decode(factory, ErrorDiskResourceMove.class, caught.getMessage());

        ErrorHandler.post(errorBean.as(), caught);
    }
    
    private void checkForPartialRestore(String result) {
        RestoreResponse response = AutoBeanCodex.decode(drFactory, RestoreResponse.class, result).as();
        Splittable restored = response.getRestored();

        for (DiskResource resource : selectedResources) {
            Splittable restoredResourceJson = restored.get(resource.getPath());

            if (restoredResourceJson != null) {
                RestoredResource restoredResource = AutoBeanCodex.decode(drFactory,
                        RestoredResource.class, restoredResourceJson).as();

                if (restoredResource.isPartialRestore()) {
                   IplantAnnouncer.getInstance().schedule(appearance.partialRestore());
                   break;
                } else {
                    IplantAnnouncer.getInstance().schedule(new SuccessAnnouncementConfig(appearance.restoreMsg()));
                    break;
                }
            }
        }
    }
}
