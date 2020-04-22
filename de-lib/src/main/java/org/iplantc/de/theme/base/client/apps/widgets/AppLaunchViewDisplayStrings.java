package org.iplantc.de.theme.base.client.apps.widgets;

import com.google.gwt.i18n.client.Messages;

/**
 * @author aramsey
 */
public interface AppLaunchViewDisplayStrings extends Messages {
    String deprecatedAppMask();

    String hpcAppWaitTimes();

    String waitTimes();

    String dontShow();

    String launchPreviewHeader(String templateName);

    String createQuickLaunchSuccess(String name);

    String quickLaunchDeleted();

    String viceLimitTitle();

    String viceLimitBody(String errorMsg);
}
