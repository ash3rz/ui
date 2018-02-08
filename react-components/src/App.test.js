import React from 'react';
import ReactDOM from 'react-dom';
import App from './apps/details/CopyTextArea';

it('renders without crashing', () => {
  const div = document.createElement('div');
  ReactDOM.render(<App />, div);
});
