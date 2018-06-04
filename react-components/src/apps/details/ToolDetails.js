/**
 * @author sriram
 *
 */
import React, {Component} from "react";
import ExpansionPanel from "@material-ui/core/ExpansionPanel";
import ExpansionPanelSummary from "@material-ui/core/ExpansionPanelSummary";
import ExpansionPanelDetails from "@material-ui/core/ExpansionPanelDetails";
import ExpandMoreIcon from "@material-ui/icons/ExpandMore";
import styles from "../style";
import {css} from "aphrodite";
import intlData from "../messages";
import withI18N, {getMessage} from "../../util/I18NWrapper";


class ToolDetailsV1 extends Component {
    constructor(props) {
        super(props);

        this.state = {
            selectedToolIndex: 0
        };

        // This binding is necessary to make `this` work in the callback
        this.onToolSelectionChange = this.onToolSelectionChange.bind(this);
    }

    onToolSelectionChange(event, selectedToolIndex) {
        this.setState({
            selectedToolIndex: selectedToolIndex
        });
    }

    render() {
        let tools = this.props.app.tools,
            labelClass = css(styles.label),
            valueClass = css(styles.value);
        return (
            <div>
                {
                    tools.map((toolInfo, index) => (
                        <ExpansionPanel key={index}>
                            <ExpansionPanelSummary expandIcon={<ExpandMoreIcon/>}>
                                <p className={labelClass}>{toolInfo.name}:</p>
                                <p className={valueClass}>{toolInfo.description}</p>
                            </ExpansionPanelSummary>
                            <ExpansionPanelDetails>
                                <table>
                                    <tbody>
                                    <tr>
                                        <td>
                                            {getMessage("detailsLabel")}
                                        </td>
                                    </tr>
                                    <tr>
                                        <td className={labelClass}>
                                            {getMessage("toolNameLabel")}
                                        </td>
                                        <td className={valueClass}>
                                            {toolInfo.name}
                                        </td>
                                    </tr>
                                    <tr>
                                        <td className={labelClass}>
                                            {getMessage("descriptionLabel")}
                                        </td>
                                        <td className={valueClass}>
                                            {toolInfo.description}
                                        </td>
                                    </tr>
                                    <tr>
                                        <td className={labelClass}>
                                            {getMessage("imageLabel")}
                                        </td>
                                        <td className={valueClass}>
                                            {toolInfo.image}
                                        </td>
                                    </tr>
                                    <tr>
                                        <td className={labelClass}>
                                            {getMessage("toolVersionLabel")}
                                        </td>
                                        <td className={valueClass}>
                                            {toolInfo.version}
                                        </td>
                                    </tr>
                                    <tr>
                                        <td className={labelClass}>
                                            {getMessage("toolAttributionLabel")}
                                        </td>
                                        <td className={valueClass}>
                                            {toolInfo.attribution}
                                        </td>
                                    </tr>
                                    </tbody>
                                </table>
                            </ExpansionPanelDetails>
                        </ExpansionPanel>
                    ))
                }
            </div>
        );
    }
}

export default withI18N(ToolDetailsV1,intlData);