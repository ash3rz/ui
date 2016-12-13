import React from 'react';
import ReactDOM from 'react-dom';
import App from './App';
import './index.css';

ReactDOM.render(
    <App btnText='Copy' text={
`Can we copy?
Yes we can!!!`
    }/>,
    document.getElementById('root')
);
