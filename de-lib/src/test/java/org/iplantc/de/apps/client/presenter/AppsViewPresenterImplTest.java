package org.iplantc.de.apps.client.presenter;

import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import org.iplantc.de.apps.client.AppCategoriesView;
import org.iplantc.de.apps.client.AppsListView;
import org.iplantc.de.apps.client.AppsToolbarView;
import org.iplantc.de.apps.client.OntologyHierarchiesView;
import org.iplantc.de.apps.client.gin.factory.AppsViewFactory;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.gwtmockito.GwtMockitoTestRunner;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import java.util.List;

@RunWith(GwtMockitoTestRunner.class)
public class AppsViewPresenterImplTest {

    private AppsViewPresenterImpl uut;
    @Mock AppCategoriesView categoriesViewMock;
    @Mock AppCategoriesView.Presenter categoriesPresenterMock;
    @Mock OntologyHierarchiesView hierarchiesView;
    @Mock OntologyHierarchiesView.Presenter hierarchiesPresenter;

    @Mock AppsListView.Presenter listPresenterMock;

    @Mock AppsToolbarView toolbarViewMock;
    @Mock AppsToolbarView.Presenter toolbarPresenterMock;

    @Mock AppsViewFactory viewFactoryMock;

    @Before public void setUp() {
        when(categoriesPresenterMock.getWorkspaceView()).thenReturn(categoriesViewMock);
        when(toolbarPresenterMock.getView()).thenReturn(toolbarViewMock);
        uut = new AppsViewPresenterImpl(viewFactoryMock,
                                        categoriesPresenterMock, listPresenterMock,
                                        toolbarPresenterMock,
                                        hierarchiesPresenter);
    }

    @Test public void testConstructorEventHandlerWiring() {
        verify(viewFactoryMock).create(eq(categoriesPresenterMock),
                                       eq(hierarchiesPresenter),
                                       eq(listPresenterMock),
                                       eq(toolbarPresenterMock));

        // Verify categories wiring
        verify(categoriesPresenterMock).addAppCategorySelectedEventHandler(eq(listPresenterMock));
        verify(categoriesPresenterMock).addAppCategorySelectedEventHandler(eq(toolbarViewMock));

        hierarchiesPresenter.addOntologyHierarchySelectionChangedEventHandler(listPresenterMock);
        hierarchiesPresenter.addOntologyHierarchySelectionChangedEventHandler(toolbarViewMock);

        // Verify grid wiring
        verify(listPresenterMock).addAppSelectionChangedEventHandler(toolbarViewMock);
        verify(listPresenterMock).addAppInfoSelectedEventHandler(hierarchiesPresenter);

        // Verify toolbar wiring
        verify(toolbarViewMock).addDeleteAppsSelectedHandler(listPresenterMock);
        verify(toolbarViewMock).addCopyAppSelectedHandler(categoriesPresenterMock);
        verify(toolbarViewMock).addCopyWorkflowSelectedHandler(categoriesPresenterMock);
        verify(toolbarViewMock).addRunAppSelectedHandler(listPresenterMock);
        verify(toolbarViewMock).addAppSearchResultLoadEventHandler(categoriesPresenterMock);
        verify(toolbarViewMock).addAppSearchResultLoadEventHandler(listPresenterMock);
        verify(toolbarViewMock).addAppSearchResultLoadEventHandler(hierarchiesPresenter);
        verify(toolbarViewMock).addBeforeAppSearchEventHandler(listPresenterMock);
        verify(toolbarViewMock).addSwapViewButtonClickedEventHandler(listPresenterMock);

        verify(toolbarPresenterMock, times(12)).getView();


        verifyNoMoreInteractions(viewFactoryMock,
                                 categoriesPresenterMock, listPresenterMock,
                                 toolbarPresenterMock,
                                 categoriesViewMock,
                                 listPresenterMock,
                                 toolbarViewMock);

        final List<String> dirStack = Lists.newArrayList();
        final List<String> output = Lists.newArrayList();
        for(String s : Splitter.on("/").split("/foo/bar/baz")){
            dirStack.add(s);
            output.add(Joiner.on("/").join(dirStack));
        }
        System.out.println(output);
    }



}
