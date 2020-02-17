package org.iplantc.de.admin.desktop.client.workshopAdmin.presenter;

import org.iplantc.de.admin.desktop.client.workshopAdmin.WorkshopAdminView;
import org.iplantc.de.admin.desktop.client.workshopAdmin.events.DeleteMembersClickedEvent;
import org.iplantc.de.admin.desktop.client.workshopAdmin.events.RefreshMembersClickedEvent;
import org.iplantc.de.admin.desktop.client.workshopAdmin.events.SaveMembersClickedEvent;
import org.iplantc.de.admin.desktop.client.workshopAdmin.gin.factory.WorkshopAdminViewFactory;
import org.iplantc.de.admin.desktop.client.workshopAdmin.model.MemberProperties;
import org.iplantc.de.admin.desktop.client.workshopAdmin.service.WorkshopAdminServiceFacade;
import org.iplantc.de.admin.desktop.shared.Belphegor;
import org.iplantc.de.client.models.collaborators.Subject;
import org.iplantc.de.client.models.groups.GroupAutoBeanFactory;
import org.iplantc.de.client.models.groups.Member;
import org.iplantc.de.client.models.groups.MemberSaveResult;
import org.iplantc.de.collaborators.client.events.UserSearchResultSelected;
import org.iplantc.de.commons.client.ErrorHandler;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasOneWidget;
import com.google.inject.Inject;

import com.sencha.gxt.data.shared.ListStore;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sarahr
 */
public class WorkshopAdminPresenterImpl implements WorkshopAdminView.Presenter {

    private final WorkshopAdminView view;
    private final WorkshopAdminServiceFacade serviceFacade;
    private final GroupAutoBeanFactory groupAutoBeanFactory;
    private final WorkshopAdminView.WorkshopAdminViewAppearance appearance;
    ListStore<Member> listStore;

    final class UserSearchResultSelectedEventHandler
            implements UserSearchResultSelected.UserSearchResultSelectedEventHandler {

        private Member memberFromCollaborator(Subject subject) {
            Member member = groupAutoBeanFactory.getMember().as();
            member.setId(subject.getId());
            member.setAttributes(new ArrayList<String>());
            member.setEmail(subject.getEmail());
            member.setFirstName(subject.getFirstName());
            member.setInstitution(subject.getInstitution());
            member.setLastName(subject.getLastName());
            member.setName(subject.getName());
            return member;
        }

        private boolean listContainsMember(List<Member> members, Member newMember) {
            String newMemberId = newMember.getId();
            if (newMemberId != null) {
                for (Member member : members) {
                    if (member.getId() != null && member.getId().equals(newMemberId)) {
                        return true;
                    }
                }
            }
            return false;
        }

        @Override
        public void onUserSearchResultSelected(UserSearchResultSelected event) {

            // Add the user to the list if not there already.
            Member member = memberFromCollaborator(event.getSubject());
            if (!listContainsMember(listStore.getAll(), member)) {
                listStore.add(member);
            }
        }
    }

    final class DeleteMembersClickedEventHandler
            implements DeleteMembersClickedEvent.DeleteMembersClickedEventHandler {

        @Override
        public void onDeleteMembersClicked(DeleteMembersClickedEvent event) {
            for (Member member : event.getMembers()) {
                listStore.remove(member);
            }
        }
    }

    final class SaveMembersClickedEventHandler
            implements SaveMembersClickedEvent.SaveMembersClickedEventHandler {

        @Override
        public void onSaveMembersClicked(SaveMembersClickedEvent event) {
            saveMembers(event.getMembers());
        }
    }

    final class RefreshMembersClickedEventHandler
            implements RefreshMembersClickedEvent.RefreshMembersClickedEventHandler {

        @Override
        public void onRefreshMembersClicked(RefreshMembersClickedEvent event) {
            updateView();
        }
    }

    @Inject
    public WorkshopAdminPresenterImpl(final WorkshopAdminViewFactory viewFactory,
                                      final WorkshopAdminServiceFacade serviceFacade,
                                      final GroupAutoBeanFactory groupAutoBeanFactory,
                                      final MemberProperties memberProperties,
                                      final WorkshopAdminView.WorkshopAdminViewAppearance appearance) {
        listStore = getMemberListStore(memberProperties);
        view = viewFactory.create(listStore);
        this.serviceFacade = serviceFacade;
        this.groupAutoBeanFactory = groupAutoBeanFactory;
        this.appearance = appearance;

        view.addUserSearchResultSelectedEventHandler(new UserSearchResultSelectedEventHandler());
        view.addLocalEventHandler(DeleteMembersClickedEvent.TYPE, new DeleteMembersClickedEventHandler());
        view.addLocalEventHandler(SaveMembersClickedEvent.TYPE, new SaveMembersClickedEventHandler());
        view.addLocalEventHandler(RefreshMembersClickedEvent.TYPE, new RefreshMembersClickedEventHandler());
    }

    @Override
    public void go(HasOneWidget container) {
        container.setWidget(view);
        updateView();
    }

    @Override
    public void setViewDebugId(String baseId) {
        view.asWidget().ensureDebugId(baseId + Belphegor.WorkshopAdminIds.VIEW);
    }

    void updateView() {
        view.mask(appearance.loadingMask());
        serviceFacade.getMembers(new AsyncCallback<List<Member>>() {
            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.post(caught);
                view.unmask();
            }

            @Override
            public void onSuccess(List<Member> members) {
                listStore.replaceAll(members);
                view.unmask();
            }
        });
    }

    void saveMembers(List<Member> members) {
        view.mask(appearance.loadingMask());
        serviceFacade.saveMembers(members, new AsyncCallback<MemberSaveResult>() {
            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.post(caught);
                view.unmask();
            }

            @Override
            public void onSuccess(MemberSaveResult result) {
                if (!result.getFailures().isEmpty()) {
                    ErrorHandler.post(appearance.partialGroupSaveMsg());
                }
                listStore.replaceAll(result.getMembers());
                view.unmask();
            }
        });
    }

    ListStore<Member> getMemberListStore(MemberProperties memberProperties) {
        return new ListStore<>(memberProperties.id());
    }
}
