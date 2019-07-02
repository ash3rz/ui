package org.iplantc.de.diskResource.client.presenters.navigation;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.anyBoolean;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import org.iplantc.de.client.events.EventBus;
import org.iplantc.de.client.events.diskResources.FolderRefreshedEvent;
import org.iplantc.de.client.models.HasPath;
import org.iplantc.de.client.models.diskResources.Folder;
import org.iplantc.de.client.util.DiskResourceUtil;
import org.iplantc.de.diskResource.client.DiskResourceView;
import org.iplantc.de.diskResource.client.NavigationView;
import org.iplantc.de.diskResource.client.events.FolderSelectionEvent;
import org.iplantc.de.diskResource.client.events.RequestSimpleUploadEvent;
import org.iplantc.de.diskResource.client.events.selection.ImportFromUrlSelected;
import org.iplantc.de.diskResource.client.events.selection.SimpleUploadSelected;
import org.iplantc.de.diskResource.client.gin.factory.NavigationViewFactory;
import org.iplantc.de.diskResource.client.presenters.grid.proxy.FolderContentsLoadConfig;
import org.iplantc.de.diskResource.client.views.navigation.NavigationViewDnDHandler;
import org.iplantc.de.diskResource.client.views.toolbar.dialogs.FileUploadByUrlDialog;
import org.iplantc.de.shared.AsyncProviderWrapper;

import com.google.common.collect.Lists;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwtmockito.GxtMockitoTestRunner;

import com.sencha.gxt.data.shared.TreeStore;
import com.sencha.gxt.data.shared.loader.BeforeLoadEvent;
import com.sencha.gxt.data.shared.loader.TreeLoader;
import com.sencha.gxt.widget.core.client.tree.Tree;
import com.sencha.gxt.widget.core.client.tree.TreeSelectionModel;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Matchers;
import org.mockito.Mock;

import java.util.List;

/**
 * @author jstroot
 */
@RunWith(GxtMockitoTestRunner.class)
public class NavigationViewPresenterImplTest {
    @Mock NavigationView.Appearance appearanceMock;
    @Mock DiskResourceUtil diskResourceUtilMock;
    @Mock DiskResourceView.FolderRpcProxy folderRpcProxyMock;
    @Mock TreeStore<Folder> treeStoreMock;
    @Mock NavigationViewFactory viewFactoryMock;
    @Mock NavigationView viewMock;
    @Mock EventBus eventBusMock;
    @Mock NavigationView.Presenter.Appearance presenterAppearanceMock;

    @Mock BeforeLoadEvent<FolderContentsLoadConfig> beforeLoadEventMock;
    @Mock Tree<Folder,Folder> treeMock;
    @Mock TreeSelectionModel<Folder> selectionModelMock;
    @Mock Tree.TreeNode<Folder> treeNodeMock;
    @Mock TreeLoader<Folder> treeLoaderMock;
    @Mock AsyncProviderWrapper<FileUploadByUrlDialog> importByUrlDlgProvider;
    @Mock FileUploadByUrlDialog importByUrlDlgMock;

    @Captor ArgumentCaptor<AsyncCallback<FileUploadByUrlDialog>> importByUrlDlgCaptor;

    private NavigationPresenterImpl uut;
    public NavigationViewPresenterImplTest() {
    }

    @Before public void setUp() {
        when(viewFactoryMock.create(Matchers.<TreeStore<Folder>>any(),
                                    Matchers.<TreeLoader<Folder>>any(),
                                    any(NavigationViewDnDHandler.class))).thenReturn(viewMock);
        when(viewMock.getTree()).thenReturn(treeMock);
        when(treeMock.getSelectionModel()).thenReturn(selectionModelMock);
        uut = new NavigationPresenterImpl(viewFactoryMock, treeStoreMock, folderRpcProxyMock, diskResourceUtilMock, eventBusMock, appearanceMock){
            @Override
            void showErrorMsg() {
            }
        };
        uut.appearance = presenterAppearanceMock;
        uut.urlImportDlgProvider = importByUrlDlgProvider;

        verifyConstructor(uut);
    }

    private void verifyConstructor(NavigationPresenterImpl uut) {
        verify(viewMock).addFolderSelectedEventHandler(eq(uut));
        verify(viewMock).getTree();
        verify(treeStoreMock).addStoreDataChangeHandler(any(NavigationPresenterImpl.FolderStoreDataChangeHandler.class));
        verify(eventBusMock, times(6)).addHandler(Matchers.<GwtEvent.Type<NavigationPresenterImpl>>any(), eq(uut));
    }

    @Test public void onBeforeLoad_folderNotNull_contentsNotInCurrentFolder_loadCancelled() {
        FolderContentsLoadConfig loadConfigMock = mock(FolderContentsLoadConfig.class);
        Folder loadConfigFolderMock = mock(Folder.class);
        Folder currSelectedFolderMock = mock(Folder.class);
        String loadConfigIdMock = "mock config id";
        String currFolderIdMock = "mock curr id";
        when(loadConfigFolderMock.getId()).thenReturn(loadConfigIdMock);
        when(currSelectedFolderMock.getId()).thenReturn(currFolderIdMock);

        when(loadConfigMock.getFolder()).thenReturn(loadConfigFolderMock);
        when(beforeLoadEventMock.getLoadConfig()).thenReturn(loadConfigMock);
        when(selectionModelMock.getSelectedItem()).thenReturn(currSelectedFolderMock);

        /** CALL METHOD UNDER TEST **/
        uut.onBeforeLoad(beforeLoadEventMock);

        verify(viewMock, times(3)).getTree();

        verify(beforeLoadEventMock).getLoadConfig();
        verify(loadConfigMock).getFolder();

        verify(beforeLoadEventMock).setCancelled(eq(true));

        verifyNoMoreInteractions(beforeLoadEventMock,
                                 viewMock,
                                 eventBusMock);
    }

    @Test public void onBeforeLoad_folderNotNull_contentsInCurrentFolder_loadNotCancelled() {
        FolderContentsLoadConfig loadConfigMock = mock(FolderContentsLoadConfig.class);
        Folder loadConfigFolderMock = mock(Folder.class);
        Folder currSelectedFolderMock = mock(Folder.class);
        String currFolderIdMock = "mock curr id";
        when(loadConfigFolderMock.getId()).thenReturn(currFolderIdMock);
        when(currSelectedFolderMock.getId()).thenReturn(currFolderIdMock);

        when(loadConfigMock.getFolder()).thenReturn(loadConfigFolderMock);
        when(beforeLoadEventMock.getLoadConfig()).thenReturn(loadConfigMock);
        when(selectionModelMock.getSelectedItem()).thenReturn(currSelectedFolderMock);

        /** CALL METHOD UNDER TEST **/
        uut.onBeforeLoad(beforeLoadEventMock);

        verify(viewMock, times(3)).getTree();

        verify(beforeLoadEventMock).getLoadConfig();
        verify(loadConfigMock).getFolder();

        verify(beforeLoadEventMock, never()).setCancelled(anyBoolean());

        verifyNoMoreInteractions(beforeLoadEventMock,
                                 viewMock,
                                 eventBusMock);
    }

    @Test public void onImportFromUrlSelected_selectedFolderNull_defaultUploadFolderUsed() {
        final Folder uploadFolderMock = mock(Folder.class);
        final NavigationPresenterImpl spy = spy(new NavigationPresenterImpl(viewFactoryMock, treeStoreMock, folderRpcProxyMock, diskResourceUtilMock, eventBusMock, appearanceMock) {
            @Override
            public Folder getSelectedUploadFolder() {
                return uploadFolderMock;
            }

            @Override
            void showErrorMsg() {
            }
        });
        spy.urlImportDlgProvider = importByUrlDlgProvider;
        verify(viewMock, times(2)).addFolderSelectedEventHandler(Matchers.<FolderSelectionEvent.FolderSelectionEventHandler>any());
        verify(viewMock, times(2)).getTree();
        verify(eventBusMock, times(12)).addHandler(Matchers.<GwtEvent.Type<NavigationPresenterImpl>>any(), Matchers.<NavigationPresenterImpl>any());
        ImportFromUrlSelected eventMock = mock(ImportFromUrlSelected.class);
        when(presenterAppearanceMock.permissionErrorMessage()).thenReturn("errorMessage");
        when(presenterAppearanceMock.permissionErrorTitle()).thenReturn("errorTitle");
        when(diskResourceUtilMock.canUploadTo(uploadFolderMock)).thenReturn(true);

        /** CALL METHOD UNDER TEST **/
        spy.onImportFromUrlSelected(eventMock);

        verify(eventMock).getSelectedFolder();
        verify(spy).getSelectedUploadFolder();

        verify(importByUrlDlgProvider).get(importByUrlDlgCaptor.capture());
        importByUrlDlgCaptor.getValue().onSuccess(importByUrlDlgMock);
        verify(importByUrlDlgMock).show(eq(uploadFolderMock));
    }

    @Test public void onImportFromUrlSelected_selectedFolderExists_selectedFolderUsed() {
        final Folder uploadFolderMock = mock(Folder.class);
        final NavigationPresenterImpl spy = spy(new NavigationPresenterImpl(viewFactoryMock, treeStoreMock, folderRpcProxyMock, diskResourceUtilMock, eventBusMock, appearanceMock) {
            @Override
            public Folder getSelectedUploadFolder() {
                return uploadFolderMock;
            }

            @Override
            void showErrorMsg() {
            }
        });
        spy.urlImportDlgProvider = importByUrlDlgProvider;
        verify(viewMock, times(2)).addFolderSelectedEventHandler(Matchers.<FolderSelectionEvent.FolderSelectionEventHandler>any());
        verify(viewMock, times(2)).getTree();
        verify(eventBusMock, times(12)).addHandler(Matchers.<GwtEvent.Type<NavigationPresenterImpl>>any(), Matchers.<NavigationPresenterImpl>any());
        ImportFromUrlSelected eventMock = mock(ImportFromUrlSelected.class);
        when(eventMock.getSelectedFolder()).thenReturn(uploadFolderMock);
        when(diskResourceUtilMock.canUploadTo(uploadFolderMock)).thenReturn(true);

        /** CALL METHOD UNDER TEST **/
        spy.onImportFromUrlSelected(eventMock);

        verify(eventMock).getSelectedFolder();
        verify(spy, never()).getSelectedUploadFolder();

        verify(importByUrlDlgProvider).get(importByUrlDlgCaptor.capture());
        importByUrlDlgCaptor.getValue().onSuccess(importByUrlDlgMock);
        verify(importByUrlDlgMock).show(eq(uploadFolderMock));

        verifyNoMoreInteractions(viewMock,
                                 eventMock,
                                 eventBusMock);
    }

    @Test public void onSimpleUploadSelected_selectedFolderNull_defaultUploadFolderUsed() {
        final Folder uploadFolderMock = mock(Folder.class);
        when(uploadFolderMock.getPath()).thenReturn("mock/path");
        final NavigationPresenterImpl spy = spy(new NavigationPresenterImpl(viewFactoryMock, treeStoreMock, folderRpcProxyMock, diskResourceUtilMock, eventBusMock, appearanceMock) {
            @Override
            public Folder getSelectedUploadFolder() {
                return uploadFolderMock;
            }
        });
        verify(viewMock, times(2)).addFolderSelectedEventHandler(Matchers.<FolderSelectionEvent.FolderSelectionEventHandler>any());
        verify(viewMock, times(2)).getTree();
        verify(eventBusMock, times(12)).addHandler(Matchers.<GwtEvent.Type<NavigationPresenterImpl>>any(), Matchers.<NavigationPresenterImpl>any());
        SimpleUploadSelected eventMock = mock(SimpleUploadSelected.class);

        /** CALL METHOD UNDER TEST **/
        spy.onSimpleUploadSelected(eventMock);

        verify(eventMock).getSelectedFolder();
        verify(spy).getSelectedUploadFolder();

        ArgumentCaptor<RequestSimpleUploadEvent> captor = ArgumentCaptor.forClass(RequestSimpleUploadEvent.class);
        verify(eventBusMock).fireEvent(captor.capture());

        assertEquals(uploadFolderMock, captor.getValue().getDestinationFolder());

        verifyNoMoreInteractions(viewMock,
                                 eventMock,
                                 eventBusMock);
    }

    @Test public void onSimpleUploadSelected_selectedFolderExists_selectedFolderUsed() {
        final Folder uploadFolderMock = mock(Folder.class);
        when(uploadFolderMock.getPath()).thenReturn("mock/path");
        final NavigationPresenterImpl spy = spy(new NavigationPresenterImpl(viewFactoryMock, treeStoreMock, folderRpcProxyMock, diskResourceUtilMock, eventBusMock, appearanceMock) {
            @Override
            public Folder getSelectedUploadFolder() {
                return mock(Folder.class);
            }
        });
        verify(viewMock, times(2)).addFolderSelectedEventHandler(Matchers.<FolderSelectionEvent.FolderSelectionEventHandler>any());
        verify(viewMock, times(2)).getTree();
        verify(eventBusMock, times(12)).addHandler(Matchers.<GwtEvent.Type<NavigationPresenterImpl>>any(), Matchers.<NavigationPresenterImpl>any());
        SimpleUploadSelected eventMock = mock(SimpleUploadSelected.class);
        when(eventMock.getSelectedFolder()).thenReturn(uploadFolderMock);

        /** CALL METHOD UNDER TEST **/
        spy.onSimpleUploadSelected(eventMock);

        verify(eventMock).getSelectedFolder();
        verify(spy, never()).getSelectedUploadFolder();

        ArgumentCaptor<RequestSimpleUploadEvent> captor = ArgumentCaptor.forClass(RequestSimpleUploadEvent.class);
        verify(eventBusMock).fireEvent(captor.capture());

        assertEquals(uploadFolderMock, captor.getValue().getDestinationFolder());

        verifyNoMoreInteractions(viewMock,
                                 eventMock,
                                 eventBusMock);
    }

    @Test public void onFolderRefreshed_methodCalled() {
        final NavigationPresenterImpl spy = spy(new NavigationPresenterImpl(viewFactoryMock, treeStoreMock, folderRpcProxyMock, diskResourceUtilMock, eventBusMock, appearanceMock) {
            @Override
            public void reloadTreeStoreFolderChildren(Folder folder) {
            }
        });
        verify(viewMock, times(2)).addFolderSelectedEventHandler(Matchers.<FolderSelectionEvent.FolderSelectionEventHandler>any());
        verify(viewMock, times(2)).getTree();
        verify(eventBusMock, times(12)).addHandler(Matchers.<GwtEvent.Type<NavigationPresenterImpl>>any(), Matchers.<NavigationPresenterImpl>any());
        FolderRefreshedEvent eventMock = mock(FolderRefreshedEvent.class);
        Folder folderMock = mock(Folder.class);
        when(eventMock.getFolder()).thenReturn(folderMock);

        /** CALL METHOD UNDER TEST **/
        spy.onFolderRefreshed(eventMock);

        verify(eventMock).getFolder();
        verify(spy).reloadTreeStoreFolderChildren(eq(folderMock));

        verifyNoMoreInteractions(viewMock,
                                 eventMock,
                                 eventBusMock);
    }

    @Test public void isPathUnderKnownRoot() {
        List<Folder> roots = Lists.newArrayList(initMockFolder("/test/path1"),
                                                initMockFolder("/test/path2"),
                                                initMockFolder("/test/path3"));

        when(treeStoreMock.getRootItems()).thenReturn(roots);

        assertEquals(true, uut.isPathUnderKnownRoot("/test/path1"));
        assertEquals(true, uut.isPathUnderKnownRoot("/test/path2"));
        assertEquals(true, uut.isPathUnderKnownRoot("/test/path3"));
        assertEquals(true, uut.isPathUnderKnownRoot("/test/path1/test1"));
        assertEquals(true, uut.isPathUnderKnownRoot("/test/path2/test2"));
        assertEquals(true, uut.isPathUnderKnownRoot("/test/path3/test3"));

        assertEquals(false, uut.isPathUnderKnownRoot("/"));
        assertEquals(false, uut.isPathUnderKnownRoot("/test"));
        assertEquals(false, uut.isPathUnderKnownRoot("/foo/bar/baz"));
        assertEquals(false, uut.isPathUnderKnownRoot("/test/path4/test4"));
        assertEquals(false, uut.isPathUnderKnownRoot("/test/path1-test/5"));
    }

    @Test public void reloadTreeStoreFolderChildren_shortCircuit(){
        final NavigationPresenterImpl spy = spy(new NavigationPresenterImpl(viewFactoryMock,
                                                                            treeStoreMock,
                                                                            folderRpcProxyMock,
                                                                            diskResourceUtilMock,
                                                                            eventBusMock,
                                                                            appearanceMock));

        /** CALL METHOD UNDER TEST -- folder null **/
        spy.reloadTreeStoreFolderChildren(null);

        verify(spy, never()).removeChildren(Matchers.<Folder>any());
        verify(spy, never()).setSelectedFolder(Matchers.<Folder>any());

        Folder folderMock = mock(Folder.class);
        when(treeStoreMock.findModel(eq(folderMock))).thenReturn(null);

        /** CALL METHOD UNDER TEST -- folder not found in tree store **/
        spy.reloadTreeStoreFolderChildren(folderMock);

        verify(spy, never()).removeChildren(Matchers.<Folder>any());
        verify(spy, never()).setSelectedFolder(Matchers.<Folder>any());
    }

    @Test public void reloadTreeStoreFolderChildren_selectedFolderCurrent_notDescendant() {
        final NavigationPresenterImpl spy = spy(new NavigationPresenterImpl(viewFactoryMock,
                                                                            treeStoreMock,
                                                                            folderRpcProxyMock,
                                                                            diskResourceUtilMock,
                                                                            eventBusMock,
                                                                            appearanceMock){

            TreeLoader<Folder> initTreeLoader(final DiskResourceView.FolderRpcProxy rpcProxy){
                return treeLoaderMock;
            }
        });

        Folder selectedFolderMock = mock(Folder.class);
        final String mockId = "mockId";
        when(selectedFolderMock.getId()).thenReturn(mockId);
        when(treeStoreMock.findModel(eq(selectedFolderMock))).thenReturn(selectedFolderMock);
        when(selectionModelMock.getSelectedItem()).thenReturn(selectedFolderMock);
        when(diskResourceUtilMock.isDescendantOfFolder(eq(selectedFolderMock),
                                                       eq(selectedFolderMock))).thenReturn(false);
        when(treeMock.findNode(Matchers.<Folder>any())).thenReturn(treeNodeMock);


        /** CALL METHOD UNDER TEST **/
        spy.reloadTreeStoreFolderChildren(selectedFolderMock);
        verify(selectedFolderMock, times(2)).getId();
        verify(spy).setSelectedFolder((HasPath) eq(selectedFolderMock));
        verify(treeLoaderMock).load(Matchers.<Folder>any());
    }

    @Test public void reloadTreeStoreFolderChildren_notCurrent_notDescendant() {
        final NavigationPresenterImpl spy = spy(new NavigationPresenterImpl(viewFactoryMock,
                                                                            treeStoreMock,
                                                                            folderRpcProxyMock,
                                                                            diskResourceUtilMock,
                                                                            eventBusMock,
                                                                            appearanceMock){
            TreeLoader<Folder> initTreeLoader(final DiskResourceView.FolderRpcProxy rpcProxy){
                return treeLoaderMock;
            }
        });

        Folder folderMock = mock(Folder.class);
        Folder selectedFolderMock = mock(Folder.class);
        final String mockId = "mockId";
        final String mockId2 = "mockId2";
        when(folderMock.getId()).thenReturn(mockId);
        when(selectedFolderMock.getId()).thenReturn(mockId2);
        when(treeStoreMock.findModel(eq(folderMock))).thenReturn(folderMock);
        when(selectionModelMock.getSelectedItem()).thenReturn(selectedFolderMock);
        when(diskResourceUtilMock.isDescendantOfFolder(eq(folderMock),
                                                       eq(selectedFolderMock))).thenReturn(false);
        when(treeMock.findNode(Matchers.<Folder>any())).thenReturn(treeNodeMock);


        /** CALL METHOD UNDER TEST **/
        spy.reloadTreeStoreFolderChildren(folderMock);
        verify(folderMock).getId();
        verify(selectedFolderMock).getId();
        verify(spy, never()).setSelectedFolder(any(HasPath.class));
        verify(treeLoaderMock).load(Matchers.<Folder>any());
    }

    @Test public void reloadTreeStoreFolderChildren_notCurrent_descendant() {
        final NavigationPresenterImpl spy = spy(new NavigationPresenterImpl(viewFactoryMock,
                                                                            treeStoreMock,
                                                                            folderRpcProxyMock,
                                                                            diskResourceUtilMock,
                                                                            eventBusMock,
                                                                            appearanceMock){
            TreeLoader<Folder> initTreeLoader(final DiskResourceView.FolderRpcProxy rpcProxy){
                return treeLoaderMock;
            }
        });

        Folder folderMock = mock(Folder.class);
        Folder selectedFolderMock = mock(Folder.class);
        final String mockId = "mockId";
        final String mockId2 = "mockId2";
        when(folderMock.getId()).thenReturn(mockId);
        when(selectedFolderMock.getId()).thenReturn(mockId2);
        when(treeStoreMock.findModel(eq(folderMock))).thenReturn(folderMock);
        when(selectionModelMock.getSelectedItem()).thenReturn(selectedFolderMock);
        when(diskResourceUtilMock.isDescendantOfFolder(eq(folderMock),
                                                       eq(selectedFolderMock))).thenReturn(true);
        when(treeMock.findNode(Matchers.<Folder>any())).thenReturn(treeNodeMock);


        /** CALL METHOD UNDER TEST **/
        spy.reloadTreeStoreFolderChildren(folderMock);
        verify(folderMock).getId();
        verify(selectedFolderMock).getId();
        verify(spy).setSelectedFolder((HasPath) eq(selectedFolderMock));
        verify(treeLoaderMock, never()).load(Matchers.<Folder>any());
    }

    private Folder initMockFolder(String path) {
        Folder folder = mock(Folder.class);
        when(folder.getPath()).thenReturn(path);

        return folder;
    }

}
