package org.iplantc.de.diskResource.client.views.search;

import org.iplantc.de.client.models.search.DiskResourceQueryTemplate;
import org.iplantc.de.client.models.search.SearchAutoBeanFactory;
import org.iplantc.de.commons.client.events.SubmitTextSearchEvent;
import org.iplantc.de.commons.client.events.SubmitTextSearchEvent.SubmitTextSearchEventHandler;
import org.iplantc.de.commons.client.widgets.search.SearchFieldDecorator;
import org.iplantc.de.diskResource.client.SearchView;
import org.iplantc.de.diskResource.client.events.FolderSelectionEvent;
import org.iplantc.de.diskResource.client.events.search.SaveDiskResourceQueryClickedEvent;
import org.iplantc.de.diskResource.client.events.search.SavedSearchDeletedEvent;
import org.iplantc.de.diskResource.client.events.search.SubmitDiskResourceQueryEvent;
import org.iplantc.de.diskResource.client.events.search.SubmitDiskResourceQueryEvent.SubmitDiskResourceQueryEventHandler;
import org.iplantc.de.diskResource.client.events.search.UpdateSavedSearchesEvent;
import org.iplantc.de.diskResource.client.views.search.cells.DiskResourceSearchCell;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.inject.Inject;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.google.web.bindery.autobean.shared.AutoBeanUtils;
import com.google.web.bindery.autobean.shared.Splittable;

import com.sencha.gxt.widget.core.client.event.CollapseEvent.CollapseHandler;
import com.sencha.gxt.widget.core.client.event.CollapseEvent.HasCollapseHandlers;
import com.sencha.gxt.widget.core.client.event.ExpandEvent.ExpandHandler;
import com.sencha.gxt.widget.core.client.event.ExpandEvent.HasExpandHandlers;
import com.sencha.gxt.widget.core.client.event.ParseErrorEvent;
import com.sencha.gxt.widget.core.client.form.DateField;
import com.sencha.gxt.widget.core.client.form.PropertyEditor;
import com.sencha.gxt.widget.core.client.form.TriggerField;

import java.text.ParseException;

/**
 * This class is a clone-and-own of {@link DateField}.
 * 
 * @author jstroot
 * 
 */
public class DiskResourceSearchField extends TriggerField<String> implements HasExpandHandlers,
                                                                             HasCollapseHandlers,
                                                                             FolderSelectionEvent.FolderSelectionEventHandler,
                                                                             SubmitDiskResourceQueryEvent.HasSubmitDiskResourceQueryEventHandlers,
                                                                             SubmitDiskResourceQueryEventHandler,
                                                                             SubmitTextSearchEventHandler,
                                                                             SavedSearchDeletedEvent.SavedSearchDeletedEventHandler,
                                                                             UpdateSavedSearchesEvent.HasUpdateSavedSearchesEventHandlers {

    public final class QueryStringPropertyEditor extends PropertyEditor<String> {
        private final SearchAutoBeanFactory factory = GWT.create(SearchAutoBeanFactory.class);
        @Override
        public String parse(CharSequence text) throws ParseException {
            clearInvalid();

            if (text.length() < 3) {
                return text.toString();
            }

            DiskResourceQueryTemplate qt = factory.dataSearchFilter().as();
            qt.setFileQuery(text.toString());
            Splittable encode = AutoBeanCodex.encode(AutoBeanUtils.getAutoBean(qt));
            getCell().getSearchPresenter().onSearchBtnClicked(encode);
            return text.toString();
        }

        @Override
        public String render(String object) {
            return object;
        }
    }

    /**
     * Creates a new iPlant Search field.
     */
    @Inject
    public DiskResourceSearchField(final DiskResourceSearchCell searchCell) {
        super(searchCell);

        setPropertyEditor(new QueryStringPropertyEditor());
        getCell().addSubmitDiskResourceQueryEventHandler(this);

        // Add search field decorator to enable "auto-search"
        new SearchFieldDecorator<TriggerField<String>>(this).addSubmitTextSearchEventHandler(this);
    }

    @Override
    public HandlerRegistration addCollapseHandler(CollapseHandler handler) {
        return getCell().addCollapseHandler(handler);
    }

    @Override
    public HandlerRegistration addExpandHandler(ExpandHandler handler) {
        return getCell().addExpandHandler(handler);
    }

    @Override
    public HandlerRegistration addSubmitDiskResourceQueryEventHandler(SubmitDiskResourceQueryEventHandler handler) {
        return getCell().addSubmitDiskResourceQueryEventHandler(handler);
    }

    @Override
    public void onFolderSelected(FolderSelectionEvent event) {
        if (event.getSelectedFolder() instanceof DiskResourceQueryTemplate) {
            final DiskResourceQueryTemplate selectedQuery = (DiskResourceQueryTemplate)event.getSelectedFolder();
            edit(selectedQuery);
        } else {
            // Clear search form
            clearSearch();
        }
    }

    @Override
    public void onSavedSearchDeleted(SavedSearchDeletedEvent event) {
        clearSearch();
    }

    public void clearSearch() {
        // Forward clear call to searchForm
        getCell().getSearchForm().clearSearch();
        clearInvalid();
        clear();
    }

    public void edit(DiskResourceQueryTemplate queryTemplate) {
        // Forward edit call to searchForm
        getCell().edit(queryTemplate);
        clear();
    }

    @Override
    public DiskResourceSearchCell getCell() {
        return (DiskResourceSearchCell)super.getCell();
    }

    protected void expand() {
        getCell().expand(createContext(), getElement(), getValue(), valueUpdater);
    }

    @Override
    protected void onCellParseError(ParseErrorEvent event) {
        super.onCellParseError(event);
        String msg = "Default message";
        forceInvalid(msg);
    }

    @Override
    public void doSubmitDiskResourceQuery(SubmitDiskResourceQueryEvent event) {
        // SS: CORE- 5437
        clear();
    }

    @Override
    public void onSubmitTextSearch(SubmitTextSearchEvent event) {
        // Finish editing to fire search event.
        finishEditing();
        focus();
    }

    @Override
    public HandlerRegistration addUpdateSavedSearchesEventHandler(UpdateSavedSearchesEvent.UpdateSavedSearchesHandler handler) {
        return getCell().addUpdateSavedSearchesEventHandler(handler);
    }

    public SearchView.Presenter getSearchPresenter() {
        return getCell().getSearchPresenter();
    }
}
