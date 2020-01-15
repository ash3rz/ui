package org.iplantc.de.teams.client.presenter;

import org.iplantc.de.client.models.groups.Group;
import org.iplantc.de.client.models.groups.GroupAutoBeanFactory;
import org.iplantc.de.client.models.groups.GroupList;
import org.iplantc.de.client.services.GroupServiceFacade;
import org.iplantc.de.collaborators.client.util.CollaboratorsUtil;
import org.iplantc.de.commons.client.ErrorHandler;
import org.iplantc.de.commons.client.info.IplantAnnouncer;
import org.iplantc.de.shared.AsyncProviderWrapper;
import org.iplantc.de.teams.client.EditTeamView;
import org.iplantc.de.teams.client.ReactTeamViews;
import org.iplantc.de.teams.client.TeamsView;
import org.iplantc.de.teams.client.events.TeamSaved;
import org.iplantc.de.teams.client.gin.TeamsViewFactory;
import org.iplantc.de.teams.client.models.TeamsFilter;
import org.iplantc.de.teams.shared.Teams;

import com.google.common.collect.Lists;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.google.web.bindery.autobean.shared.Splittable;
import com.google.web.bindery.autobean.shared.impl.StringQuoter;

import java.util.List;

/**
 * The presenter to handle all the logic for the Teams view
 *
 * @author aramsey
 */
public class TeamsPresenterImpl implements TeamsView.Presenter {

    private GroupServiceFacade serviceFacade;
    private TeamsView view;
    private GroupAutoBeanFactory factory;
    private CollaboratorsUtil collaboratorsUtil;
    private List<Group> selectedTeams;

    @Inject EditTeamView.Presenter editTeamPresenter;
    @Inject IplantAnnouncer announcer;
    TeamsFilter currentFilter;

    @Inject
    public TeamsPresenterImpl(GroupServiceFacade serviceFacade,
                              TeamsViewFactory viewFactory,
                              GroupAutoBeanFactory factory,
                              CollaboratorsUtil collaboratorsUtil) {
        this.serviceFacade = serviceFacade;
        this.factory = factory;
        this.collaboratorsUtil = collaboratorsUtil;
        this.view = viewFactory.create(getBaseTeamProps());
    }

    @Override
    public TeamsView getView() {
        return view;
    }

    @Override
    public void showCheckBoxes() {
        view.showCheckBoxes();
    }

    @Override
    public List<Group> getSelectedTeams() {
        return selectedTeams;
    }

    @Override
    public void setViewDebugId(String baseID) {
        view.setBaseId(baseID + Teams.Ids.VIEW);
    }

    @Override
    @SuppressWarnings("unusable-by-js")
    public void onTeamNameSelected(Splittable teamSpl) {
        Group group = convertSplittableToGroup(teamSpl);
        editTeamPresenter.go(group);
        editTeamPresenter.addTeamSavedHandler(event1 -> {
            refreshTeamListing();
        });
        editTeamPresenter.addLeaveTeamCompletedHandler(event -> {
            refreshTeamListing();
        });
        editTeamPresenter.addDeleteTeamCompletedHandler(event -> {
            refreshTeamListing();
        });
        editTeamPresenter.addJoinTeamCompletedHandler(event -> {
            refreshTeamListing();
        });
    }

    void refreshTeamListing() {
        if (TeamsFilter.MY_TEAMS.equals(currentFilter)) {
            getTeams(TeamsFilter.MY_TEAMS.toString(), null);
        } else {
            getTeams(TeamsFilter.ALL.toString(), null);
        }
    }

    @Override
    public void getTeams(String filter, String searchTerm) {
        boolean myTeams;
        if (TeamsFilter.MY_TEAMS.toString().equals(filter)) {
            currentFilter = TeamsFilter.MY_TEAMS;
            myTeams = true;
        } else {
            currentFilter = TeamsFilter.ALL;
            myTeams = false;
        }

        view.mask();

        serviceFacade.getTeams(myTeams, searchTerm, new AsyncCallback<Splittable>(){
            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.postReact(caught);
                view.unmask();
            }

            @Override
            public void onSuccess(Splittable result) {
                updateViewTeamList(result);
            }
        });
    }

    void updateViewTeamList(Splittable result) {
        Splittable groups = StringQuoter.createSplittable();
        if (result != null) {
            groups = result.get("groups");
        }
        view.setTeamList(groups);
    }

    @Override
    public void onCreateTeamSelected() {
        editTeamPresenter.go(null);
        editTeamPresenter.addTeamSavedHandler(event -> refreshTeamListing());
    }

    @Override
    @SuppressWarnings("unusable-by-js")
    public void onTeamSelectionChanged(Splittable teamList) {
        view.setSelectedTeams(teamList);
        this.selectedTeams = convertSplittableToGroupList(teamList);
    }

    ReactTeamViews.TeamProps getBaseTeamProps(){
        ReactTeamViews.TeamProps props = new ReactTeamViews.TeamProps();
        props.presenter = this;
        props.loading = true;
        props.teamListing = StringQuoter.createIndexed();
        props.collaboratorsUtil = collaboratorsUtil;
        props.isSelectable = false;
        props.selectedTeams = StringQuoter.createIndexed();

        return props;
    }

    Group convertSplittableToGroup(Splittable teamSpl) {
        return teamSpl != null ?
               AutoBeanCodex.decode(factory, Group.class, teamSpl.getPayload()).as() :
               null;
    }

    List<Group> convertSplittableToGroupList(Splittable groupSpl) {
        return groupSpl != null ?
               AutoBeanCodex.decode(factory,
                                    GroupList.class,
                                    "{\"groups\": " + groupSpl.getPayload() + "}").as().getGroups() :
               Lists.newArrayList();
    }
}
