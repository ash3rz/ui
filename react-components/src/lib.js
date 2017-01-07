import React from 'react';
import ReactDOM from 'react-dom';
import ToolDetails from './ToolDetails';
import CopyTextArea from './CopyTextArea';
import CategoryTree from './CategoryTree';

const renderToolDetails = (elementID, toolDetailsAppearance, app) => {
    ReactDOM.render(
        <ToolDetails appearance={toolDetailsAppearance} app={app} />,
        document.getElementById(elementID)
    );
};

const renderCopyTextArea = (elementID, btnText, textToCopy) => {
    ReactDOM.render(
        <CopyTextArea btnText={btnText} text={ textToCopy }/>,
        document.getElementById(elementID)
    );
};

const renderCategoryTree = (elementID, appearance, app) => {
    ReactDOM.render(
        <CategoryTree appearance={appearance} app={app} />,
        document.getElementById(elementID)
    );
};

export { renderCopyTextArea, renderToolDetails, renderCategoryTree };
