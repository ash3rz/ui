package org.iplantc.de.client.models.diskResources;



import com.google.web.bindery.autobean.shared.AutoBean.PropertyName;

public interface File extends DiskResource {

    @PropertyName("file-size")
    long getSize();

    @PropertyName("file-size")
    void setSize(long size);

    @PropertyName("content-type")
    String getContentType();

    @PropertyName("info-type")
    String getInfoType();

    @PropertyName("info-type")
    void setInfoType(String infoType);

}