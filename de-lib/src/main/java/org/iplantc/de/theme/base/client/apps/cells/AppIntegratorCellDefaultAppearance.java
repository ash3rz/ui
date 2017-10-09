package org.iplantc.de.theme.base.client.apps.cells;

import org.iplantc.de.apps.client.views.list.cells.AppIntegratorCell;
import org.iplantc.de.theme.base.client.apps.AppSearchHighlightAppearance;

import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;

/**
 * @author jstroot
 */
public class AppIntegratorCellDefaultAppearance implements AppIntegratorCell.AppIntegratorCellAppearance {

    private final AppSearchHighlightAppearance highlightAppearance;

    public AppIntegratorCellDefaultAppearance() {
        this(GWT.<AppSearchHighlightAppearance> create(AppSearchHighlightAppearance.class));
    }

    AppIntegratorCellDefaultAppearance(final AppSearchHighlightAppearance highlightAppearance) {
        this.highlightAppearance = highlightAppearance;
    }

    @Override
    public void render(SafeHtmlBuilder sb, String value, String pattern) {
        sb.append(highlightAppearance.highlightText(value, pattern));
    }
}
