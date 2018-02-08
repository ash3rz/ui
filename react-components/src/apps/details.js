import React from 'react';
import ReactDOM from 'react-dom';

import CategoryTree from './details/CategoryTree';
import CopyTextArea from './details/CopyTextArea';
import ToolDetails from './details/ToolDetails';

const renderCategoryTree = (elementID, app, presenter, appearance) => {
    ReactDOM.render(
        <CategoryTree app={app} presenter={presenter} appearance={appearance} />,
        document.getElementById(elementID)
    );
};

const renderCopyTextArea = (elementID, btnText, textToCopy) => {
    ReactDOM.render(
        <CopyTextArea btnText={btnText} text={ textToCopy }/>,
        document.getElementById(elementID)
    );
};

const renderToolDetails = (elementID, toolDetailsAppearance, app) => {
    ReactDOM.render(
        <ToolDetails appearance={toolDetailsAppearance} app={app} />,
        document.getElementById(elementID)
    );
};

export {
    CategoryTree,
    CopyTextArea,
    ToolDetails,
    renderCategoryTree,
    renderCopyTextArea,
    renderToolDetails
};
