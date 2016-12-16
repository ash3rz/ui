import React from 'react';
import ReactDOM from 'react-dom';
import CopyTextArea from './CopyTextArea';
import ToolDetails from './ToolDetails';
import './index.css';

let ToolDetailsAppearance = {
    css: {
        label: "",
        value: ""
    },
    detailsLabel: "Details: ",
    toolNameLabel: "Name: ",
    descriptionLabel: "Description: ",
    toolPathLabel: "Path: ",
    toolVersionLabel: "Version: ",
    toolAttributionLabel: "Attribution: "
};

let ToolInfo = {
    name: "wc_wrapper.sh",
    description: "Word Count",
    location: "/usr/local3/bin/wc_tool-1.00",
    version: "1.00",
    attribution: "Wrapper for GNU wc. iPlant DE version developed by Matt Vaughn (vaughn at iplantcollaborative dot org)."
};

ReactDOM.render(
    <ToolDetails appearance={ToolDetailsAppearance} toolInfo={ToolInfo} />,
    document.getElementById('tool-info')
);

ReactDOM.render(
    <CopyTextArea btnText='Copy' text={
`Can we copy?
Yes we can!!!`
    } />,
    document.getElementById('copy-text-area')
);
