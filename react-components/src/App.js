import React, { Component } from 'react';
import {hasClipboardAPI, copySelection} from './clipboardFunctions'
import './App.css';

class App extends Component {
    constructor(props) {
        super(props);

        // This binding is necessary to make `this` work in the callback
        this.onCopyText = this.onCopyText.bind(this);
    }

    renderCopyTextArea(text) {
        return <textArea ref="copyTextArea" value={text} readOnly="readonly" />;
    }

    renderCopyButton(btnText) {
        if (hasClipboardAPI()) {
            return <button ref="copyTextBtn" type="button" onClick={this.onCopyText}>{btnText}</button>;
        }
    }

    onCopyText(e) {
        e.preventDefault();

        this.refs.copyTextArea.select();
        if (copySelection()) {
            this.refs.copyTextBtn.innerText = "Copied!";
        }
    }

    componentDidMount() {
        if (!hasClipboardAPI()) {
            this.refs.copyTextArea.select();
        }
    }

    render() {
        let copyTextArea = this.renderCopyTextArea(this.props.text),
            copyButton = this.renderCopyButton(this.props.btnText);

        return (
            <div className="App">
                {copyTextArea}
                {copyButton}
            </div>
        );
    }
}

export default App;
