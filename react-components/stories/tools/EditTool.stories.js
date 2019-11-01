import React, { Component } from "react";

import EditToolDialog from "../../src/tools/edit/EditTool";
import { boolean, object } from "@storybook/addon-knobs";

class EditToolTest extends Component {
    render() {
        const presenter = {
            addTool:
                this.props.logger ||
                ((tool) => {
                    console.log(tool);
                }),
            updateTool:
                this.props.logger ||
                ((tool) => {
                    console.log(tool);
                }),
            onPublish:
                this.props.logger ||
                ((tool) => {
                    console.log(tool);
                }),
            closeEditToolDlg: () => console.log("close"),
        };

        const tool = {
            description:
                "Cyverse Jupyter Lab beta with updated iJab plugin and jupyterlab_irods",
            permission: "read",
            interactive: true,
            name: "jupyter-lab",
            type: "interactive",
            restricted: false,
            is_public: true,
            id: "5db4e2c7-7a0a-492a-bf79-09cba3801e0d",
            container: {
                interactive_apps: {
                    id: "89e442ee-f1cf-11e8-99d0-008cfa5ae621",
                    image: "discoenv/cas-proxy",
                    name: "cas-proxy",
                    cas_url: "https://olson.cyverse.org/cas",
                    cas_validate: "validate",
                },
                max_cpu_cores: 2,
                container_ports: [
                    {
                        id: "89e4b440-f1cf-11e8-99d0-008cfa5ae621",
                        container_port: 8888,
                        bind_to_host: false,
                    },
                ],
                min_cpu_cores: 0.1,
                uid: 1000,
                working_directory: "/home/jovyan/vice",
                skip_tmp_mount: true,
                id: "89e3f186-f1cf-11e8-99d0-008cfa5ae621",
                memory_limit: 4000000000,
                network_mode: "bridge",
                image: {
                    name: "gims.cyverse.org:5000/jupyter-lab",
                    id: "89e3c5f8-f1cf-11e8-99d0-008cfa5ae621",
                    tag: "1.0",
                    deprecated: false,
                },
            },
            version: "0.0.3",
            implementation: {
                implementor: "Upendra Kumar Devisetty",
                implementor_email: "upendra@cyverse.org",
                test: {
                    input_files: [],
                    output_files: [],
                },
            },
            time_limit_seconds: 0,
        };

        const parentId = "editToolDlg";

        const toolTypes = ["executable", "interactive", "osg"];

        const maxCPUCore = 8;

        const maxMemory = 17179869184;

        const maxDiskSpace = 549755813888;

        return (
            <EditToolDialog
                open={true}
                isAdmin={boolean("isAdmin", true)}
                isAdminPublishing={boolean("isAdminPublishing", false)}
                loading={false}
                parentId={parentId}
                tool={object("tool", tool)}
                toolTypes={toolTypes}
                maxCPUCore={maxCPUCore}
                maxMemory={maxMemory}
                maxDiskSpace={maxDiskSpace}
                presenter={presenter}
            />
        );
    }
}

export default EditToolTest;
