import React from "react";
import { storiesOf } from "@storybook/react";
import { action } from "@storybook/addon-actions";

import AnalysesViewTest from "./analysis/view/AnalysesView.stories";
import AnalysisInfoDialogTest from "./analysis/view/dialogs/AnalysisInfoDialog.stories";
import AnalysisParametersDialogTest from "./analysis/view/dialogs/AnalysisParametersDialog.stories";
import ShareWithSupportDialogTest from "./analysis/view/dialogs/ShareWithSupportDialog.stories";

import ToolDetailsTest from "./apps/details/ToolDetails.stories";
import CategoryTreeTest from "./apps/details/CategoryTree.stories";
import AppStatsTest from "./apps/admin/AppStats.stories";

import SubjectSearchFieldTest from "./collaborators/SubjectSearchField.stories";

import ManageCommunitiesViewTest from "./communities/ManageCommunitiesView.stories";

import BasicDetailsTest from "./data/details/BasicDetails.stories";
import InfoTypeSelectionListTest from "./data/details/InfoTypeSelectionList.stories";
import TagTest from "./data/details/Tag.stories";
import TagPanelTest from "./data/details/TagPanel.stories";

import QueryBuilderTest from './data/search/QueryBuilder.stories';
import EditTagDialogTest from "./data/search/EditTagDialog.stories";
import SaveSearchButtonTest from "./data/search/SaveSearchButton.stories";
import SearchFormTagPanel from './data/search/SearchFormTagPanel.stories';

import DesktopViewTest from "./desktop/view/DesktopView.stories";
import TaskButtonTest from "./desktop/view/TaskButton.stories";
import TaskbarTest from "./desktop/view/Taskbar.stories";

import VideoViewerTest from "./fileViewers/VideoViewer.stories";

import { EditDataCiteMetadataTest, EditMetadataTest, EmptyMetadataTest, ViewMetadataTest } from "./metadata/EditMetadata.stories";
import {
    DataCiteMetadataTemplateViewNoValuesTest,
    DataCiteMetadataTemplateViewTest,
    EditDataCiteMetadataTemplateTest,
    EditNestedAttrMetadataTemplateTest,
    MetadataTemplateReadOnlyViewTest,
    MetadataTemplateViewTest,
} from "./metadata/MetadataTemplate.stories";

import NotificationViewTest from "./notifications/view/NotificationView.stories";
import JoinTeamRequestDialogTest from "./notifications/view/dialogs/JoinTeamRequestDialog.stories";
import DenyJoinRequestDetailsDialogTest
    from "./notifications/view/dialogs/DenyJoinRequestDetailsDialog.stories";
import RequestHistoryDialogTest from "./notifications/view/dialogs/RequestHistoryDialog.stories";

import SearchFieldTest from "./util/SearchField.stories";
import AutocompleteTest from "./util/Autocomplete.stories";
import CopyTextAreaTest from "./util/CopyTextArea.stories";
import DEHyperLinkTest from "./util/hyperlink/DEHyperLink.stories";
import ErrorHandlerTest from "./util/ErrorHandler.stories";
import ErrorExpansionPanelTest from "./util/ErrorExpansionPanel.stories";
import TriggerFieldTest from "./util/TriggerField.stories";
import DEPromptDialogTest from "./util/dialog/DEPromptDialog.stories";


storiesOf('analysis/view', module).add('with test analyses', () => <AnalysesViewTest/>);
storiesOf('analysis/view/dialogs', module).add('with test analysis info', () =>
    <AnalysisInfoDialogTest/>);
storiesOf('analysis/view/dialogs', module).add('with test analysis parameters', () =>
    <AnalysisParametersDialogTest/>);
storiesOf('analysis/view/dialogs', module)
    .add('with test analysis support', () => <ShareWithSupportDialogTest/>);

storiesOf('apps/admin/AppStats', module).add('with test stats', () => <AppStatsTest/>);
storiesOf('apps/details', module).add('CategoryTree', () => <CategoryTreeTest logger={action('hierarchy')} />);
storiesOf('apps/details', module).add('ToolDetails', () => <ToolDetailsTest/>);

storiesOf('collaborators', module).add('Subject Search Field', () => <SubjectSearchFieldTest logger={action('Selected Subject')}/>);

storiesOf('communities', module).add("Manage Communities View", () => <ManageCommunitiesViewTest communityAppsClickedLogger={action("Add apps to community clicked")} confirmedDialogAction={action("Confirmed dialog action")}/>);

storiesOf('data/BasicDetails', module).add('with test diskresource details', () => <BasicDetailsTest logger={action('details')}/>);
storiesOf('data/InfoTypeSelectionList',module).add('with test diskresource details', () =>  <InfoTypeSelectionListTest logger={action('infoTypes')}/>);
storiesOf('data/search', module).add('EditTagDialog', () => <EditTagDialogTest saveTagLogger={action('Save Updated Tag')} closeDlgLogger={action('Close Dialog')}/>);
storiesOf('data/search', module).add('QueryBuilder', () => <QueryBuilderTest searchLogger={action('Search Submitted')} editTagLogger={action('Edit Tag')} saveSearchLogger={action('Save Search')} addTagLogger={action('Add Tag')}/>);
storiesOf('data/search', module).add('SaveSearchButton', () => <SaveSearchButtonTest logger={action('Save Search')}/>);
storiesOf('data/search', module).add('SearchFormTagPanel', () => <SearchFormTagPanel removeTagLogger={action('Remove Tag')} editTagLogger={action('Edit Tag')} appendTagLogger={action('Append Tag')} addTagLogger={action('Add Tag')}/>);
storiesOf('data/Tag', module).add('with test diskresource details', () => <TagTest logger={action('tag')}/>);
storiesOf('data/TagPanel', module).add('with test diskresource details', () => <TagPanelTest logger={action('tagpanel')}/>);

storiesOf('desktop/view', module).add("with test desktop view", () => <DesktopViewTest logger={action('desktop')}/>);
storiesOf('desktop/view', module).add("with test desktop taskbutton", () => <TaskButtonTest/>);
storiesOf('desktop/view', module).add("with test desktop taskbar", () => <TaskbarTest/>);

storiesOf('fileViewers', module).add("VideoViewer", () => <VideoViewerTest/>);

storiesOf('metadata/admin/EditMetadataTemplate', module)
    .add('with nested attributes', () => <EditNestedAttrMetadataTemplateTest logger={action('template')} />)
    .add('with DataCite attributes', () => <EditDataCiteMetadataTemplateTest logger={action('template')} />);
storiesOf('metadata/EditMetadata', module)
    .add('with nested AVUs', () => <EditMetadataTest logger={action('metadata')}/>)
    .add('with read-only metadata', () => <ViewMetadataTest logger={action('metadata')}/>)
    .add('with empty metadata', () => <EmptyMetadataTest logger={action('metadata')}/>)
    .add('with DateCite nested AVUs', () => <EditDataCiteMetadataTest logger={action('metadata')}/>);

storiesOf('metadata/MetadataTemplateView', module)
    .add('with nested attributes', () => <MetadataTemplateViewTest logger={action('templateView')}/>)
    .add('with read-only nested attributes', () => <MetadataTemplateReadOnlyViewTest logger={action('templateView')}/>)
    .add('with DataCite metadata', () => <DataCiteMetadataTemplateViewTest logger={action('templateView')}/>)
    .add('with DataCite Template, no metadata', () => <DataCiteMetadataTemplateViewNoValuesTest logger={action('templateView')}/>);

storiesOf('util', module).add('Autocomplete', () => <AutocompleteTest selectOptionLogger={action('Selected Option')}/>);
storiesOf('notifications/view', module).add('with test notifications', () => <NotificationViewTest
    logger={action('notification Window')}/>);
storiesOf('notifications/view/dialogs', module).add('with test JoinTeamRequestDialog', () =>
    <JoinTeamRequestDialogTest logger={action('notification window')}/>);
storiesOf('notifications/view/dialogs', module).add('with test DenyJoinRequestDetailsDialog', () =>
    <DenyJoinRequestDetailsDialogTest logger={action('notification window')}/>);
storiesOf('notifications/view/dialogs', module).add('with test RequestHistoryDialogTest', () =>
    <RequestHistoryDialogTest logger={action('notification window')}/>);

storiesOf('util', module).add('CopyTextArea', () => <CopyTextAreaTest/>);
storiesOf('util', module).add('DEHyperLink', () => <DEHyperLinkTest/>);
storiesOf('util', module).add('ErrorExpansionPanel', () => <ErrorExpansionPanelTest/>);
storiesOf('util', module).add('ErrorHandler', () => <ErrorHandlerTest/>);
storiesOf('util', module).add('SearchField', () => <SearchFieldTest logger={action('Search')}/>);
storiesOf('util', module).add('TriggerSearchField', () => <TriggerFieldTest logger={action('Search')}/>);
storiesOf('util', module).add('DEPromptDialog', () => <DEPromptDialogTest/>)



