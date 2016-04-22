package org.iplantc.de.theme.base.client.admin.ontologies;

import com.google.gwt.i18n.client.Messages;

/**
 * @author aramsey
 */
public interface OntologyDisplayStrings extends Messages{

    @Key("addOntology")
    String addOntology();

    String ontologyListDialogName();

    String ontologyList();

    String iriColumnLabel();

    String versionColumnLabel();

    String createdByColumnLabel();

    String createdOnColumnLabel();

    String setActiveVersion();
}
