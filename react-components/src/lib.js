import React from 'react';
import ReactDOM from 'react-dom';
import ToolDetails from './ToolDetails';
import CopyTextArea from './CopyTextArea';
import './index.css';


const renderToolDetails = (elementID, ToolDetailsAppearance, ToolInfo) => {
    ReactDOM.render(
        <ToolDetails appearance={ToolDetailsAppearance} toolInfo={ToolInfo} />,
        document.getElementById(elementID)
    );
};

const renderCopyTextArea = (elementID, btnText, textToCopy) => {
    ReactDOM.render(
        <CopyTextArea btnText={btnText} text={ textToCopy }/>,
        document.getElementById(elementID)
    );
};

export { renderCopyTextArea, renderToolDetails };
