import React from 'react';
import ReactDOM from 'react-dom';
import CopyTextArea from '../apps/details/CopyTextArea';

let textToCopy =
`Can we copy?
Yes we can!!!`;

const renderElement = document.getElementById('copy-text-area');
if (renderElement) {
    ReactDOM.render(
        <CopyTextArea btnText='Copy' text={ textToCopy } />,
        renderElement
    );
}
