package org.iplantc.de.apps.client;

import org.iplantc.de.apps.client.events.AppUpdatedEvent;
import org.iplantc.de.apps.client.events.selection.AppDetailsDocSelected;
import org.iplantc.de.apps.client.events.selection.AppFavoriteSelectedEvent;
import org.iplantc.de.apps.client.events.selection.AppRatingDeselected;
import org.iplantc.de.apps.client.events.selection.AppRatingSelected;
import org.iplantc.de.apps.client.events.selection.DetailsCategoryClicked;
import org.iplantc.de.apps.client.events.selection.DetailsHierarchyClicked;
import org.iplantc.de.apps.client.events.selection.SaveMarkdownSelected;
import org.iplantc.de.client.models.apps.App;
import org.iplantc.de.client.models.apps.AppCategory;
import org.iplantc.de.client.models.apps.AppDoc;
import org.iplantc.de.client.models.ontologies.OntologyHierarchy;

import com.google.gwt.editor.client.Editor;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.ui.HasOneWidget;
import com.google.gwt.user.client.ui.IsWidget;

import com.sencha.gxt.data.shared.TreeStore;
import com.sencha.gxt.widget.core.client.tree.TreeStyle;

import java.util.List;

import jsinterop.annotations.JsIgnore;
import jsinterop.annotations.JsType;

/**
 * Created by jstroot on 3/2/15.
 * @author jstroot
 */
@JsType
public interface AppDetailsView extends IsWidget,
                                        Editor<App>,
                                        AppUpdatedEvent.AppUpdatedEventHandler,
                                        AppFavoriteSelectedEvent.HasAppFavoriteSelectedEventHandlers,
                                        AppDetailsDocSelected.HasAppDetailsDocSelectedHandlers,
                                        SaveMarkdownSelected.HasSaveMarkdownSelectedHandlers,
                                        AppRatingDeselected.HasAppRatingDeselectedHandlers,
                                        AppRatingSelected.HasAppRatingSelectedEventHandlers,
                                        DetailsHierarchyClicked.HasDetailsHierarchyClickedHandlers,
                                        DetailsCategoryClicked.HasDetailsCategoryClickedHandlers {

    @JsType
    interface AppDetailsAppearance {
        @JsType
        interface AppDetailsStyle extends CssResource {

            String label();

            String value();

            String hyperlink();

            String detailsTable();

            String detailsRow();

            String tabPanel();
        }

        String descriptionLabel();

        AppDetailsStyle css();

        String detailsLabel();

        @JsIgnore
        SafeHtml getAppDocError(Throwable caught);

        @JsIgnore
        SafeHtml getCategoriesHtml(List<List<String>> appGroupHierarchies);

        @JsIgnore
        SafeHtml highlightText(String value, String searchRegexPattern);

        String publishedOnLabel();

        String integratorNameLabel();

        String integratorEmailLabel();

        String helpLabel();

        String ratingLabel();

        String categoriesLabel();

        String informationTabLabel();

        @JsIgnore
        SafeHtml saveAppDocError(Throwable caught);

        String toolInformationTabLabel();

        String toolNameLabel();

        String toolPathLabel();

        String toolVersionLabel();

        String toolAttributionLabel();

        String userManual();

        String url();

        String appUrl();

        String copyAppUrl();

        @JsIgnore
        void setTreeIcons(TreeStyle style);

        String completedRun();

        String completedDate();
    }

    interface Presenter extends AppFavoriteSelectedEvent.HasAppFavoriteSelectedEventHandlers,
                                AppRatingDeselected.HasAppRatingDeselectedHandlers,
                                AppRatingSelected.HasAppRatingSelectedEventHandlers,
                                DetailsHierarchyClicked.HasDetailsHierarchyClickedHandlers,
                                DetailsCategoryClicked.HasDetailsCategoryClickedHandlers{

        void go(HasOneWidget widget,
                App app,
                String searchRegexPattern,
                TreeStore<OntologyHierarchy> hierarchyTreeStore,
                TreeStore<AppCategory> categoryTreeStore);
    }

    /**
     * Displays the documentation window
     */
    @JsIgnore
    void showDoc(AppDoc appDoc);

    void onDetailsCategoryClicked(String modelKey);
}
