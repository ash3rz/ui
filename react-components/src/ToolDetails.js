/**
 * @author psarando
 */
import React, { Component } from 'react';
import './App.css'

class ToolDetails extends Component {

    render() {
        let toolInfo = this.props.toolInfo,
            appearance = this.props.appearance;

        let labelClass = appearance.css.label;
        let valueClass = appearance.css.value;

        return (
            <table>
                <tbody>
                <tr>
                    <td>
                        {appearance.detailsLabel}
                    </td>
                    <td>
                        <i>Rendered by React!</i>
                    </td>
                </tr>
                <tr>
                    <td className={labelClass}>
                        {appearance.toolNameLabel}
                    </td>
                    <td className={valueClass}>
                        {toolInfo.name}
                    </td>
                </tr>
                <tr>
                    <td className={labelClass}>
                        {appearance.descriptionLabel}
                    </td>
                    <td className={valueClass}>
                        {toolInfo.description}
                    </td>
                </tr>
                <tr>
                    <td className={labelClass}>
                        {appearance.toolPathLabel}
                    </td>
                    <td className={valueClass}>
                        {toolInfo.location}
                    </td>
                </tr>
                <tr>
                    <td className={labelClass}>
                        {appearance.toolVersionLabel}
                    </td>
                    <td className={valueClass}>
                        {toolInfo.version}
                    </td>
                </tr>
                <tr>
                    <td className={labelClass}>
                        {appearance.toolAttributionLabel}
                    </td>
                    <td className={valueClass}>
                        {toolInfo.attribution}
                    </td>
                </tr>
                </tbody>
            </table>
        );
    }
}

export default ToolDetails;
