package org.iplantc.de.client.models.notifications.payload;

import org.iplantc.de.client.models.diskResources.File;

import com.google.web.bindery.autobean.shared.AutoBean.PropertyName;

import java.util.List;

/**
 * Payload AutoBean for Data Notifications.
 * 
 * @author psarando
 * 
 */
public interface PayloadData {
    String ACTION_UPLOAD_COMPLETE = "UPLOAD_COMPLETE";
    /**
     * XXX JDS This could be turned into an enum
     * 
     * @return
     */
    @PropertyName("action")
    String getAction();

    void setAction(String action);

    /**
     * Present only for "file_uploaded" actions.
     * 
     * @return File object
     */
    File getData();

    void setData(File file);

    /**
     * Present only for "share" actions.
     * 
     * @return A list of paths that have been shared.
     */
    List<String> getPaths();
}
