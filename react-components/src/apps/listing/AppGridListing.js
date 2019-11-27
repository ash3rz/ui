import React, { Component } from "react";
import PropTypes from "prop-types";
import exStyles from "../style";
import getAppsSearchRegex from "../appSearchRegex";
import DeleteBtn from "../../data/search/queryBuilder/DeleteBtn";
import {
    AppName,
    AppStatusIcon,
    AppMenu,
    build,
    DECheckbox,
    DETableRow,
    EmptyTable,
    EnhancedTableHead,
    getMessage,
    Highlighter,
    Rate,
    withI18N,
} from "@cyverse-de/ui-lib";

import ids from "./ids";
import messages from "../messages";

import { Table, TableBody, TableCell, withStyles } from "@material-ui/core";

import AppFields from "../AppFields";
import appType from "../../appType";

/**
 * @author aramsey
 *
 * A component that will show a listing of type App
 */
class AppGridListing extends Component {
    constructor(props) {
        super(props);
        ["handleSelectAllClick", "onRequestSort"].forEach(
            (fn) => (this[fn] = this[fn].bind(this))
        );
    }

    onRequestSort(event, property) {
        const orderBy = property;
        let order = "desc";
        if (
            this.props.sortField === property &&
            this.props.sortDir === "desc"
        ) {
            order = "asc";
        }
        this.props.onSortChange(orderBy, order);
    }

    handleSelectAllClick(event, checked) {
        this.props.handleSelectAll(checked);
    }

    render() {
        const {
            parentId,
            apps,
            selectable,
            deletable,
            onRemoveApp,
            selectedApps,
            handleAppSelection,
            isSelected,
            onAppNameClick,
            onAppInfoClick,
            onCommentsClick,
            onFavoriteClick,
            enableMenu,
            onRatingDeleteClick,
            onRatingClick,
            searchText,
            sortField,
            sortDir,
            classes,
            onQuickLaunchClick,
            onSortChange,
        } = this.props;

        let columnData = getTableColumns(deletable, enableMenu);
        const searchRegex = getAppsSearchRegex(searchText);
        return (
            <Table stickyHeader={true} size="small">
                <TableBody>
                    {(!apps || apps.length === 0) && (
                        <EmptyTable
                            message={getMessage("noApps")}
                            numColumns={columnData.length}
                        />
                    )}
                    {apps &&
                        apps.length > 0 &&
                        apps.map((app) => {
                            const {
                                average: averageRating,
                                user: userRating,
                                total: totalRating,
                            } = app.rating;
                            const selected = isSelected && isSelected(app.id);
                            const external = app.app_type !== appType.de;
                            const rowId = build(parentId, app.id);
                            return (
                                <DETableRow
                                    role="checkbox"
                                    tabIndex={-1}
                                    hover
                                    selected={selected}
                                    aria-checked={selected}
                                    onClick={
                                        handleAppSelection
                                            ? () => handleAppSelection(app)
                                            : undefined
                                    }
                                    key={app.id}
                                    id={rowId}
                                >
                                    {selectable && (
                                        <TableCell padding="checkbox">
                                            <DECheckbox checked={selected} />
                                        </TableCell>
                                    )}
                                    <TableCell padding="none">
                                        <AppStatusIcon
                                            isPublic={app.is_public}
                                            isBeta={app.beta}
                                            isDisabled={app.disabled}
                                        />
                                    </TableCell>
                                    <TableCell>
                                        <AppName
                                            baseDebugId={build(
                                                rowId,
                                                ids.LISTING.APP_NAME
                                            )}
                                            isDisabled={app.disabled}
                                            name={app.name}
                                            onAppNameClicked={
                                                onAppNameClick
                                                    ? () => onAppNameClick(app)
                                                    : undefined
                                            }
                                            searchText={searchRegex}
                                        />
                                    </TableCell>
                                    <TableCell>
                                        <span className={classes.listingFont}>
                                            <Highlighter search={searchRegex}>
                                                {app.integrator_name}
                                            </Highlighter>
                                        </span>
                                    </TableCell>
                                    <TableCell>
                                        <Rate
                                            name={app.id}
                                            value={userRating || averageRating}
                                            readOnly={
                                                external ||
                                                !app.is_public ||
                                                !onRatingClick
                                            }
                                            total={totalRating}
                                            onChange={(event, score) => {
                                                onRatingClick(
                                                    event,
                                                    app,
                                                    score
                                                );
                                            }}
                                            onDelete={
                                                userRating
                                                    ? () =>
                                                          onRatingDeleteClick(
                                                              app
                                                          )
                                                    : undefined
                                            }
                                        />
                                    </TableCell>
                                    <TableCell
                                        align="right"
                                        className={classes.listingFont}
                                    >
                                        {app.system_id}
                                    </TableCell>
                                    {deletable && (
                                        <TableCell align="right">
                                            <DeleteBtn
                                                onClick={() => onRemoveApp(app)}
                                            />
                                        </TableCell>
                                    )}
                                    {enableMenu && (
                                        <TableCell padding="none" align="right">
                                            <AppMenu
                                                baseDebugId={rowId}
                                                onAppInfoClick={() =>
                                                    onAppInfoClick(app)
                                                }
                                                onCommentsClick={() =>
                                                    onCommentsClick(app)
                                                }
                                                onFavoriteClick={() =>
                                                    onFavoriteClick(app)
                                                }
                                                isExternal={external}
                                                isFavorite={app.is_favorite}
                                                onQuickLaunchClick={() =>
                                                    onQuickLaunchClick(app)
                                                }
                                            />
                                        </TableCell>
                                    )}
                                </DETableRow>
                            );
                        })}
                </TableBody>
                <EnhancedTableHead
                    selectable={selectable}
                    numSelected={selectedApps ? selectedApps.length : 0}
                    rowsInPage={apps ? apps.length : 0}
                    order={sortDir}
                    orderBy={sortField}
                    baseId={parentId}
                    ids={ids.FIELD}
                    columnData={columnData}
                    onRequestSort={
                        onSortChange ? this.onRequestSort : undefined
                    }
                    onSelectAllClick={this.handleSelectAllClick}
                />
            </Table>
        );
    }
}

function getTableColumns(deletable, enableMenu) {
    let tableColumns = [
        { name: "", numeric: false, enableSorting: false, key: "status" },
        {
            name: AppFields.NAME.fieldName,
            enableSorting: true,
            key: AppFields.NAME.key,
        },
        {
            name: AppFields.INTEGRATOR.fieldName,
            enableSorting: true,
            key: AppFields.INTEGRATOR.key,
        },
        {
            name: AppFields.RATING.fieldName,
            enableSorting: true,
            key: AppFields.RATING.key,
        },
        {
            name: AppFields.SYSTEM.fieldName,
            enableSorting: false,
            key: AppFields.SYSTEM.key,
            align: "right",
        },
    ];

    if (deletable) {
        tableColumns.push({
            name: "",
            enableSorting: false,
            key: "remove",
        });
    }

    if (enableMenu) {
        tableColumns.push({
            name: "",
            enableSorting: false,
            key: "menu",
            align: "right",
        });
    }

    return tableColumns;
}

AppGridListing.defaultProps = {
    deletable: false,
    selectable: true,
    enableMenu: false,
};

AppGridListing.propTypes = {
    parentId: PropTypes.string.isRequired,
    apps: PropTypes.array,
    selectable: PropTypes.bool,
    deletable: PropTypes.bool,
    onRemoveApp: PropTypes.func,
    selectedApps: PropTypes.array.isRequired,
    handleAppSelection: PropTypes.func,
    enableMenu: PropTypes.bool,
    onAppNameClick: PropTypes.func,
    onAppInfoClick: PropTypes.func,
    onCommentsClick: PropTypes.func,
    onFavoriteClick: PropTypes.func,
    onRatingDeleteClick: PropTypes.func,
    onRatingClick: PropTypes.func,
    searchText: PropTypes.oneOfType([
        PropTypes.string,
        PropTypes.instanceOf(RegExp),
    ]),
    sortField: PropTypes.string.isRequired,
    sortDir: PropTypes.string.isRequired,
    onSortChange: PropTypes.func,
    onQuickLaunchClick: PropTypes.func,
    isSelected: PropTypes.func,
};

export default withStyles(exStyles)(withI18N(AppGridListing, messages));
