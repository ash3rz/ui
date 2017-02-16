import * as apps from './apps';

import React from "react";
import ReactDOM from "react-dom";

// gwt-react needs React and ReactDOM on the global object
window.React = React;
window.ReactDOM = ReactDOM;

export { apps };
