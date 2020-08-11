package org.iplantc.de.diskResource.client.presenters.grid.proxy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.anyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import org.iplantc.de.client.models.diskResources.DiskResource;
import org.iplantc.de.client.models.diskResources.Folder;
import org.iplantc.de.client.models.diskResources.TYPE;
import org.iplantc.de.client.models.search.DiskResourceQueryTemplate;
import org.iplantc.de.client.models.viewer.InfoType;
import org.iplantc.de.client.services.DiskResourceServiceFacade;
import org.iplantc.de.client.services.FileSystemMetadataServiceFacade;
import org.iplantc.de.client.services.SearchServiceFacade;
import org.iplantc.de.commons.client.info.IplantAnnouncer;
import org.iplantc.de.diskResource.client.GridView;
import org.iplantc.de.diskResource.client.presenters.grid.proxy.FolderContentsRpcProxyImpl.FolderContentsCallback;

import com.google.common.collect.Lists;
import com.google.gwt.safehtml.client.HasSafeHtml;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwtmockito.GxtMockitoTestRunner;

import com.sencha.gxt.data.shared.SortDir;
import com.sencha.gxt.data.shared.SortInfoBean;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;
import com.sencha.gxt.data.shared.loader.PagingLoadResultBean;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;

import java.util.Collections;
import java.util.List;

/**
 * Performs tests on the {@link org.iplantc.de.diskResource.client.presenters.grid.proxy.FolderContentsRpcProxyImpl} and its underlying classes.
 *
 * @author jstroot
 */
@RunWith(GxtMockitoTestRunner.class)
public class FolderContentsRpcProxyTest {

    @Mock DiskResourceServiceFacade diskResourceService;
    @Mock SearchServiceFacade searchServiceMock;
    @Mock IplantAnnouncer announcer;

    @Mock AsyncCallback<PagingLoadResult<DiskResource>> pagingAsyncMock;
    @Mock FileSystemMetadataServiceFacade mockFileSystemMetadataService;
    @Mock HasSafeHtml mockHasSafeHtml;
    @Mock GridView.Presenter.Appearance appearanceMock;

    @Captor ArgumentCaptor<AsyncCallback<PagingLoadResult<DiskResource>>> pagingAsyncCaptor;
    @Captor ArgumentCaptor<PagingLoadResult<DiskResource>> pagingLoadResultArgumentCaptor;

    private FolderContentsRpcProxyImpl folderContentsRpcProxy;

    @Before
    public void setUp() {
        folderContentsRpcProxy = new FolderContentsRpcProxyImpl(diskResourceService,
                                                                searchServiceMock,
                                                                mockFileSystemMetadataService,
                                                                announcer,
                                                                appearanceMock,
                                                                Collections.<InfoType>emptyList(),
                                                                null);
        folderContentsRpcProxy.setHasSafeHtml(mockHasSafeHtml);
        folderContentsRpcProxy.hasSafeHtml = mock(HasSafeHtml.class);
    }

    /**
     * Verifies functionality of load method when the given load config contains a {@link Folder}
     * whose {@link Folder#isFilter()} method returns false.
     */
    @Test public void testLoad_Case1() {
        FolderContentsLoadConfig loadConfigMock = mock(FolderContentsLoadConfig.class);
        Folder mockFolder = mock(Folder.class);
        when(mockFolder.isFilter()).thenReturn(false);

        when(loadConfigMock.getFolder()).thenReturn(mockFolder);

        SortInfoBean sortInfoBeanMock = mock(SortInfoBean.class);
        when(sortInfoBeanMock.getSortField()).thenReturn("");
        when(sortInfoBeanMock.getSortDir()).thenReturn(SortDir.ASC);
        List<SortInfoBean> sortInfos = Lists.newArrayList();
        when(loadConfigMock.getSortInfo()).thenReturn(sortInfos);

        folderContentsRpcProxy.load(loadConfigMock, pagingAsyncMock);

        ArgumentCaptor<FolderContentsRpcProxyImpl.FolderContentsCallback> callBackCaptor
            = ArgumentCaptor.forClass(FolderContentsRpcProxyImpl.FolderContentsCallback.class);
        verify(diskResourceService).getFolderContents(eq(mockFolder), anyList(), any(TYPE.class), eq(loadConfigMock), callBackCaptor.capture());

        assertEquals(loadConfigMock, callBackCaptor.getValue().getLoadConfig());
        assertEquals(pagingAsyncMock, callBackCaptor.getValue().getCallback());
        verifyZeroInteractions(searchServiceMock);
        verifyNoMoreInteractions(diskResourceService, pagingAsyncMock);
    }

    /**
     * Verifies functionality of load method when the given load config contains a {@link Folder}
     * whose {@link Folder#isFilter()} method returns true.
     */
    @Test public void testLoad_Case2() {
        FolderContentsLoadConfig loadConfigMock = mock(FolderContentsLoadConfig.class);
        Folder mockFolder = mock(Folder.class);
        when(mockFolder.isFilter()).thenReturn(true);

        when(loadConfigMock.getFolder()).thenReturn(mockFolder);

        folderContentsRpcProxy.load(loadConfigMock, pagingAsyncMock);
        verify(pagingAsyncMock).onSuccess(pagingLoadResultArgumentCaptor.capture());

        assertEquals(0, pagingLoadResultArgumentCaptor.getValue().getTotalLength());
        assertEquals(0, pagingLoadResultArgumentCaptor.getValue().getOffset());
        assertTrue(pagingLoadResultArgumentCaptor.getValue().getData().isEmpty());

        verifyNoMoreInteractions(pagingAsyncMock);
        verifyZeroInteractions(diskResourceService, searchServiceMock);
    }

    /**
     * Verifies functionality of load method when the given load config contains a
     * {@link DiskResourceQueryTemplate}. The isFilter() method returns false.
     */
    @Test public void testLoad_Case3() {
        FolderContentsLoadConfig loadConfigMock = mock(FolderContentsLoadConfig.class);
        DiskResourceQueryTemplate mockQueryTemplate = mock(DiskResourceQueryTemplate.class);

        when(loadConfigMock.getFolder()).thenReturn(mockQueryTemplate);
        folderContentsRpcProxy.load(loadConfigMock, pagingAsyncMock);

        verify(searchServiceMock).submitSearchQuery(eq(mockQueryTemplate), eq(loadConfigMock), isA(FolderContentsRpcProxyImpl.SearchResultsCallback.class));

        verifyNoMoreInteractions(searchServiceMock, pagingAsyncMock);
        verifyZeroInteractions(diskResourceService);
    }

    /**
     * Verifies functionality of the inner callback class onSuccess method when
     * the result and given callback (the one which is accessed vi
     * {@link FolderContentsCallback#getCallback()}) are not null. Additionally,
     */
    @SuppressWarnings("unchecked")
    @Test public void testFolderContentsCallback_onSuccess_Case1() {

        FolderContentsLoadConfig loadConfigMock = mock(FolderContentsLoadConfig.class);
        Folder mockFolder = mock(Folder.class);
        when(mockFolder.isFilter()).thenReturn(false);

        when(loadConfigMock.getFolder()).thenReturn(mockFolder);

        folderContentsRpcProxy.load(loadConfigMock, pagingAsyncMock);

        ArgumentCaptor<FolderContentsRpcProxyImpl.FolderContentsCallback> callBackCaptor
            = ArgumentCaptor.forClass(FolderContentsRpcProxyImpl.FolderContentsCallback.class);
        verify(diskResourceService).getFolderContents(any(Folder.class), anyList(), any(TYPE.class), eq(loadConfigMock), callBackCaptor.capture());

        // Call method under test
        Folder f = mock(Folder.class);
        f.setName("test");
        callBackCaptor.getValue().onSuccess(f);
        verify(pagingAsyncMock).onSuccess(any(PagingLoadResultBean.class));

        verifyNoMoreInteractions(mockHasSafeHtml, diskResourceService, pagingAsyncMock);
        verifyZeroInteractions(searchServiceMock);
    }

    /**
     * Verifies functionality of the inner callback class onSuccess method when
     * the result is null.
     */
    @Test public void testFolderContentsCallback_onSuccess_Case2() {
        FolderContentsLoadConfig loadConfigMock = mock(FolderContentsLoadConfig.class);
        Folder mockFolder = mock(Folder.class);
        when(mockFolder.isFilter()).thenReturn(false);
        when(appearanceMock.listingFailure()).thenReturn("failure");

        when(loadConfigMock.getFolder()).thenReturn(mockFolder);

        folderContentsRpcProxy.load(loadConfigMock, pagingAsyncMock);

        ArgumentCaptor<FolderContentsRpcProxyImpl.FolderContentsCallback> callBackCaptor
            = ArgumentCaptor.forClass(FolderContentsRpcProxyImpl.FolderContentsCallback.class);
        verify(diskResourceService).getFolderContents(any(Folder.class), anyList(), any(TYPE.class), eq(loadConfigMock), callBackCaptor.capture());

        callBackCaptor.getValue().onSuccess(null);
        verify(pagingAsyncMock).onFailure(any(Throwable.class));
        verifyZeroInteractions(searchServiceMock);
    }

    /**
     * Verifies functionality of the inner callback class onFailure method.
     */
    @Test public void testFolderContentsCallback_onFailure() {
        FolderContentsLoadConfig loadConfigMock = mock(FolderContentsLoadConfig.class);
        Folder mockFolder = mock(Folder.class);
        when(mockFolder.isFilter()).thenReturn(false);
        when(appearanceMock.listingFailure()).thenReturn("failure");

        when(loadConfigMock.getFolder()).thenReturn(mockFolder);

        folderContentsRpcProxy.load(loadConfigMock, pagingAsyncMock);

        ArgumentCaptor<FolderContentsRpcProxyImpl.FolderContentsCallback> callBackCaptor
            = ArgumentCaptor.forClass(FolderContentsRpcProxyImpl.FolderContentsCallback.class);
        verify(diskResourceService).getFolderContents(any(Folder.class), anyList(), any(TYPE.class), eq(loadConfigMock), callBackCaptor.capture());

        callBackCaptor.getValue().onFailure(500, mock(Throwable.class));
        verify(pagingAsyncMock).onFailure(any(Throwable.class));
    }
}
