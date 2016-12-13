import React from 'react';
import ReactDOM from 'react-dom';
import App from './App';
import './index.css';


const renderCopyTextArea = (elementID, btnText, textToCopy) => {
    ReactDOM.render(
        <App btnText={btnText} text={ textToCopy }/>,
        document.getElementById(elementID)
    );
};

export { renderCopyTextArea };
