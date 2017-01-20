import React, { Component } from 'react';
import ToolDetails from '../../../apps/details/ToolDetails';

class ToolDetailsTest extends Component {
    render() {
        const toolDetailsAppearance = {
            css: () => (
                {
                    label: () => "",
                    value: () => ""
                }
            ),
            detailsLabel: () => "Details: ",
            toolNameLabel: () => "Name: ",
            descriptionLabel: () => "Description: ",
            toolPathLabel: () => "Path: ",
            toolVersionLabel: () => "Version: ",
            toolAttributionLabel: () => "Attribution: "
        };

        const app = {
            tools: [
                {
                    id: "66f99e46-854a-11e4-b626-0fcca6cef881",
                    name: "QATestTool.sh",
                    description: "Test script to emulate a tool installed",
                    location: "/usr/local2/bin",
                    version: "0.0.1",
                    attribution: "CyVerse QA Test Engineers"
                },
                {
                    id: "0db32bf0-42ea-11e5-86e2-cfc4a0abcaf5",
                    name: "discoenv/ncbi-sra-submit:test",
                    description: "Test submissions to the NCBI Sequence Read Archive (SRA).",
                    location: "https://registry.hub.docker.com/u/discoenv/ncbi-sra-submit",
                    version: "1.1",
                    attribution: "Paul Sarando, CyVerse"
                }
            ]
        };

        return ( <ToolDetails appearance={toolDetailsAppearance} app={app}/> );
    }
}

const renderElement = {
    element: ToolDetailsTest,
    path: "/apps/details/ToolDetails"
};

export default renderElement;
