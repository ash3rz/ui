package org.iplantc.de.theme.base.client.diskResource.metadata;

import com.google.gwt.i18n.client.Messages;

/**
 * Created by jstroot on 2/10/15.
 * @author jstroot
 */
public interface MetadataDisplayStrings extends Messages{
    String attribute();

    String metadataTemplateConfirmRemove();

    String metadataTemplateRemove();

    String metadataTemplateSelect();

    String newAttribute();

    String newValue();

	String newUnit();

	String selectTemplate();

	String importMd();

    String userMetadata();

    String metadataTermGuide();

	String templateListingError();

	String loadMetadataError();

	String saveMetadataError();

	String templateinfoError();

	String additionalMetadata();

	String templates();

	String error();

	String incomplete();

	String importMdMsg();

	String importMdTooltip();

	String metadataLink();

	String readMore();

	String urlGhostText();

	String requiredGhostText();
}
