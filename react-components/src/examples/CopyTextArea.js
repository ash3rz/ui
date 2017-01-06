import React from 'react';
import ReactDOM from 'react-dom';
import CopyTextArea from '../CopyTextArea';

let textToCopy =
`Can we copy?
Yes we can!!!`;

ReactDOM.render(
    <CopyTextArea btnText='Copy' text={ textToCopy }/>,
    document.getElementById('copy-text-area')
);
