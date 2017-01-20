import React, { Component } from 'react';
import CopyTextArea from '../../../apps/details/CopyTextArea';

class CopyTextAreaTest extends Component {
    render () {
        let textToCopy =
`Can we copy?
Yes we can!!!`;

        return ( <CopyTextArea btnText='Copy' text={ textToCopy } /> );
    }
}
const renderElement = {
    element: CopyTextAreaTest,
    path: "/apps/details/CopyTextArea"
};

export default renderElement;
