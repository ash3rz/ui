package org.iplantc.de.resources.client.constants;

import com.google.gwt.i18n.client.Constants;

/**
 * These constants used for validation throughout the app.
 * 
 * @author jstroot
 * 
 */
public interface IplantValidationConstants extends Constants {

    String restrictedCmdLineArgCharsExclNewline();

    String restrictedCmdLineChars();
    
    String restrictedAppNameChars();
    
    String restrictedDiskResourceNameChars();

    String warnedDiskResourceNameChars();

    int maxToolNameLength();

    String restrictedGroupNameChars();

    String newlineToPrint();

    String tabToPrint();

}
