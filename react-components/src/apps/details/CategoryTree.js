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
        this.onDetailsCategoryClicked = this.onDetailsCategoryClicked.bind(this);
    }

    onDetailsCategoryClicked(selectedKeys, event) {
        let modelKey = selectedKeys[0];
        this.props.presenter.onDetailsCategoryClicked(modelKey);
    }

    renderHierarchyNode(hierarchyClass, parentKey) {
        let modelKey = (parentKey ? parentKey + "/" : "") + hierarchyClass.label;
        return (
            <TreeNode key={modelKey}
                      title={hierarchyClass.label} >
                {
                    hierarchyClass.subclasses ?
                        hierarchyClass.subclasses.map( (subclass) => this.renderHierarchyNode(subclass, modelKey) )
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
                onSelect={this.onDetailsCategoryClicked} >
                {hierarchies.map( (hierarchyClass) => this.renderHierarchyNode(hierarchyClass) )}
            </Tree>
        );
    }
}

export default CategoryTree;
