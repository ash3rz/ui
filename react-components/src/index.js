import React, { Component } from 'react';
import ReactDOM from 'react-dom';
import { Router, Route, Link, IndexRoute, hashHistory } from 'react-router';

import apps from './dev/apps';

const links = apps;

function wrapDevComponent(DevComponent) {
    return class extends Component {
        render() {
            return (
                <div>
                    <DevComponent />
                    <hr />
                    <Link to="/">Return to index</Link>
                </div>
            );
        }
    }
}

class IndexLinkList extends Component {
    render() {
        return (
            <ul>
                {
                    links.map( (link, index) => (
                            <li key={ index }>
                                <Link to={ link.path }>{ link.path }</Link>
                            </li>
                        )
                    )
                }
            </ul>
        );
    }
}

class DevList extends Component {
    render() {
        return (<div>{this.props.children}</div>);
    }
}

ReactDOM.render((
        <Router history={ hashHistory } >
            <Route path="/" component={ DevList } >
                <IndexRoute component={ IndexLinkList } />
                {
                    links.map( (link, index) => {
                            return (
                                <Route key={ index }
                                       path={ link.path }
                                       component={ wrapDevComponent(link.element) } />
                            );
                        }
                    )
                }
            </Route>
        </Router>
    ),
    document.getElementById("index-link-list")
);
