package org.iplantc.de.diskResource.client.views.search.cells;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.iplantc.de.client.models.search.DateInterval;
import org.iplantc.de.client.models.search.DiskResourceQueryTemplate;
import org.iplantc.de.client.models.search.FileSizeRange.FileSizeUnit;
import org.iplantc.de.client.models.search.SearchAutoBeanFactory;
import org.iplantc.de.client.models.tags.Tag;
import org.iplantc.de.client.util.SearchModelUtils;
import org.iplantc.de.commons.client.widgets.IPlantAnchor;
import org.iplantc.de.diskResource.client.events.search.SubmitDiskResourceQueryEvent;
import org.iplantc.de.tags.client.TagsView;
import org.iplantc.de.tags.client.gin.factory.TagItemFactory;
import org.iplantc.de.tags.client.views.TagSearchFieldImpl;

import com.google.common.collect.Lists;
import com.google.gwt.dom.client.Element;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwtmockito.GwtMockito;
import com.google.gwtmockito.GxtMockitoTestRunner;
import com.google.gwtmockito.fakes.FakeSimpleBeanEditorDriverProvider;

import com.sencha.gxt.core.client.Style.AnchorAlignment;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.HtmlLayoutContainer;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.form.CheckBox;
import com.sencha.gxt.widget.core.client.form.SimpleComboBox;
import com.sencha.gxt.widget.core.client.form.TextField;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import java.util.Date;
import java.util.List;

/**
 * This test verifies the functionality of the {@link org.iplantc.de.diskResource.client.views.search.cells.DiskResourceQueryForm} class when there are no
 * editor errors.
 * 
 * @author jstroot
 * 
 */
@RunWith(GxtMockitoTestRunner.class)
public class DiskResourceQueryFormTest_NoEditorErrors {

    @Mock DiskResourceQueryFormNamePrompt namePrompt;
    @Mock DiskResourceQueryTemplate mockedTemplate;
    @Mock HtmlLayoutContainer con;
    @Mock TagItemFactory tagItemFactoryMock;
    @Mock TagSearchFieldImpl searchFieldMock;
    @Mock TagsView.Presenter tagsViewPresenterMock;
    @Mock TagsView tagsViewMock;
    @Mock SearchAutoBeanFactory factoryMock;
    @Mock DiskResourceQueryForm.DiskResourceQueryFormAppearance appearanceMock;
    @Mock SearchModelUtils searchModelUtilsMock;

    private DiskResourceQueryForm form;

    @Before public void setUp() {
        when(tagsViewPresenterMock.getView()).thenReturn(tagsViewMock);
        GwtMockito.useProviderForType(SimpleBeanEditorDriver.class, new FakeSimpleBeanEditorDriverProvider(false));
        form = new DiskResourceQueryForm(tagsViewPresenterMock,
                                         factoryMock,
                                         searchModelUtilsMock,
                                         namePrompt,
                                         appearanceMock) {

            @Override
            DateInterval createDateInterval(Date from, Date to, String label) {
                DateInterval ret = mock(DateInterval.class);
                ret.setFrom(from);
                ret.setTo(to);
                ret.setLabel(label);
                return ret;
            }

            @Override
            List<FileSizeUnit> createFileSizeUnits() {
                return Lists.newArrayList();
            }

            @Override
            void addTrashAndFilter() {
                VerticalPanel vp = mock(VerticalPanel.class);
                vp.add(includeTrashItems);
                vp.add(createFilterLink);
                vp.setSpacing(5);
            }

            @Override
            void initSearchButton() {
                searchButton = mock(TextButton.class);
                searchButton.addSelectHandler(new SelectHandler() {

                    @Override
                    public void onSelect(SelectEvent event) {
                        onSearchButtonSelect();

                    }
                });
                Label betaLbl = mock(Label.class);
                HorizontalPanel hp = new HorizontalPanel();
                hp.add(searchButton);
                hp.add(betaLbl);
                hp.setSpacing(2);
            }

            @Override
            void initSizeFilterFields() {
                HorizontalPanel hp1 = mock(HorizontalPanel.class);
                hp1.add(fileSizeGreaterThan);
                hp1.add(greaterThanComboBox);
                hp1.setSpacing(3);

                HorizontalPanel hp2 = new HorizontalPanel();
                hp2.add(fileSizeLessThan);
                hp2.add(lessThanComboBox);
                hp2.setSpacing(3);

            }

            @Override
            void initCreateFilter() {
                createFilterLink = mock(IPlantAnchor.class);
                createFilterLink.addClickHandler(new ClickHandler() {

                    @Override
                    public void onClick(ClickEvent event) {
                        // Flush to perform local validations
                        DiskResourceQueryTemplate flushedFilter = editorDriver.flush();
                        if (editorDriver.hasErrors()) {
                            return;
                        }
                        showNamePrompt(flushedFilter);

                    }
                });
            }

            @Override
            void initExcludeTrashField() {
                includeTrashItems = mock(CheckBox.class);
                includeTrashItems.setBoxLabel("Include items in Trash");
            }

            @Override
            void initFileQuery() {
                fileQuery = mock(TextField.class);
                fileQuery.setWidth(64);
                fileQuery.setEmptyText("Enter values...");
            }

            @Override
            void initNegatedFileQuery() {
                negatedFileQuery = mock(TextField.class);
                negatedFileQuery.setEmptyText("Enter values...");
                negatedFileQuery.setWidth(64);
            }

            @Override
            void initMetadataSearchFields() {
                metadataAttributeQuery = mock(TextField.class);
                metadataAttributeQuery.setEmptyText("Enter values...");
                metadataAttributeQuery.setWidth(64);

                metadataValueQuery = mock(TextField.class);
                metadataValueQuery.setEmptyText("Enter values...");
                metadataValueQuery.setWidth(64);

            }

            @Override
            void initOwnerSharedSearchField() {
                ownedBy = mock(TextField.class);
                ownedBy.setEmptyText("Enter iPlant user name");
                ownedBy.setWidth(64);

                sharedWith = mock(TextField.class);
                sharedWith.setEmptyText("Enter iPlant user name");
                sharedWith.setWidth(64);
            }

            @Override
            void initDateRangeCombos() {
                List<DateInterval> timeIntervals = Lists.newArrayList();

                DateInterval interval = createDateInterval(null, null, "---");
                timeIntervals.add(interval);

                // Data range combos
                LabelProvider<DateInterval> dateIntervalLabelProvider = new LabelProvider<DateInterval>() {

                    @Override
                    public String getLabel(DateInterval item) {
                        return item.getLabel();
                    }
                };
                createdWithinCombo = new SimpleComboBox<>(dateIntervalLabelProvider);
                modifiedWithinCombo = new SimpleComboBox<>(dateIntervalLabelProvider);
                createdWithinCombo.add(timeIntervals);
                modifiedWithinCombo.add(timeIntervals);

                createdWithinCombo.setEmptyText("---");
                modifiedWithinCombo.setEmptyText("---");

                createdWithinCombo.setWidth(64);
                modifiedWithinCombo.setWidth(64);

            }

            @Override
            void initTagField() {
                final TagSearchFieldImpl tagSearchField = mock(TagSearchFieldImpl.class);

                tagSearchField.addValueChangeHandler(new ValueChangeHandler<Tag>() {

                    @Override
                    public void onValueChange(ValueChangeEvent<Tag> event) {
                        tagSearchField.clear();
                        tagSearchField.asWidget().getElement().focus();
                    }
                });
            }

        };
        form.namePrompt = namePrompt;
    }
    
    @Test public void testDiskResourceQueryFormInit() {
        DiskResourceQueryForm spy = spy(form);

        spy.init();
        /* Verify that the class under test adds itself as a save DRQE handler */
        verify(namePrompt).addSaveDiskResourceQueryClickedEventHandler(eq(spy));
    }

    /**
     * Verify the following when {@link DiskResourceQueryForm#createFilterLink} is selected;<br/>
     */
    @Test public void testOnCreateQueryTemplateClicked_noErrors() {
        form.onCreateQueryTemplateClicked(mock(ClickEvent.class));

        // Verify that the name prompt is shown
        verify(namePrompt).show(any(DiskResourceQueryTemplate.class), any(Element.class), any(AnchorAlignment.class));
    }

    /**
     * Verify the following when {@link DiskResourceQueryForm#searchButton} is clicked <br/>
     */
    @Test public void testOnSearchBtnSelected_noErrors() {
        DiskResourceQueryForm spy = spy(form);
        spy.onSearchButtonSelect();

        // Verify that the appropriate event is fired
        verify(spy).fireEvent(any(SubmitDiskResourceQueryEvent.class));

        // Verify that the form is hidden
        verify(spy).hide();
    }

}
