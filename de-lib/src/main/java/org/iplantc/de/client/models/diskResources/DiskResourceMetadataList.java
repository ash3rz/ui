package org.iplantc.de.client.models.diskResources;

import com.google.web.bindery.autobean.shared.AutoBean.PropertyName;

import java.util.List;

/**
 * Convenience autobean for de-serializing lists of metadata.
 * 
 * @author jstroot
 * 
 */
public interface DiskResourceMetadataList {

    @PropertyName("irods-avus")
    List<DiskResourceMetadata> getOtherMetadata();

    @PropertyName("avus")
    List<DiskResourceMetadata> getUserMetadata();
}
