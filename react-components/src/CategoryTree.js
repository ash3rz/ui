/**
 * @author psarando
 */
import React, { Component } from 'react';
import Tree, { TreeNode } from 'rc-tree';
import 'rc-tree/assets/index.css';

class CategoryTree extends Component {
    constructor(props) {
        super(props);

        // This binding is necessary to make `this` work in the callback
        this.onHierarchySelectionChange = this.onHierarchySelectionChange.bind(this);
    }

    onHierarchySelectionChange(selectedKeys, event) {
        let selectedHierarchy = selectedKeys;
        console.log(selectedHierarchy);
    }

    renderHierarchyNode(hierarchyClass) {
        return (
            <TreeNode key={hierarchyClass.iri}
                      title={hierarchyClass.label} >
                {
                    hierarchyClass.subclasses ?
                        hierarchyClass.subclasses.map( (subclass) => this.renderHierarchyNode(subclass) )
                        : null
                }
            </TreeNode>
        );
    }

    render() {
        let hierarchies = this.props.app.hierarchies;

        return (
            <Tree
                defaultExpandAll={false}
                onSelect={this.onHierarchySelectionChange} >
                {hierarchies.map( (hierarchyClass) => this.renderHierarchyNode(hierarchyClass) )}
            </Tree>
        );
    }
}

export default CategoryTree;
