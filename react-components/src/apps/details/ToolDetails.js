/**
 * @author psarando
 */
import React, { Component } from 'react';

class ToolDetails extends Component {
    constructor(props) {
        super(props);

        this.state = {
            selectedToolIndex: 0
        };

        // This binding is necessary to make `this` work in the callback
        this.onToolSelectionChange = this.onToolSelectionChange.bind(this);
    }

    onToolSelectionChange(event) {
        let selectedToolIndex = event.target.value;

        this.setState({
            selectedToolIndex: selectedToolIndex
        });
    }

    render() {
        let tools      = this.props.app.tools,
            appearance = this.props.appearance,
            toolInfo   = tools[this.state.selectedToolIndex],
            labelClass = appearance.css().label(),
            valueClass = appearance.css().value();

        return (
            <div>
                <select value={this.state.selectedToolIndex}
                        onChange={this.onToolSelectionChange} >
                    {
                        tools.map( (toolInfo, index) => (
                                <option key={index} value={index}>
                                    {toolInfo.name}
                                </option>
                            )
                        )
                    }
                </select>
                <table>
                    <tbody>
                    <tr>
                        <td>
                            {appearance.detailsLabel()}
                        </td>
                        <td>
                            <i>Rendered by React!</i>
                        </td>
                    </tr>
                    <tr>
                        <td className={labelClass}>
                            {appearance.toolNameLabel()}
                        </td>
                        <td className={valueClass}>
                            {toolInfo.name}
                        </td>
                    </tr>
                    <tr>
                        <td className={labelClass}>
                            {appearance.descriptionLabel()}
                        </td>
                        <td className={valueClass}>
                            {toolInfo.description}
                        </td>
                    </tr>
                    <tr>
                        <td className={labelClass}>
                            {appearance.toolPathLabel()}
                        </td>
                        <td className={valueClass}>
                            {toolInfo.location}
                        </td>
                    </tr>
                    <tr>
                        <td className={labelClass}>
                            {appearance.toolVersionLabel()}
                        </td>
                        <td className={valueClass}>
                            {toolInfo.version}
                        </td>
                    </tr>
                    <tr>
                        <td className={labelClass}>
                            {appearance.toolAttributionLabel()}
                        </td>
                        <td className={valueClass}>
                            {toolInfo.attribution}
                        </td>
                    </tr>
                    </tbody>
                </table>
            </div>
        );
    }
}

export default ToolDetails;
