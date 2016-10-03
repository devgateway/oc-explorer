import React from 'react';
import ReactDOM from 'react-dom';
import App from './components/app';
import flux from './flux/index.es6';
import enUs from './languages/en_US.json';
import roRo from './languages/ro_RO.json';

const TRANSLATIONS = {
  en: enUs,
  ro: roRo,
};

flux.onUpdate((state) => {
  ReactDOM.render(
    <App state={state} actions={flux.actions} translations={TRANSLATIONS[state.getIn(['globalState', 'locale'])]} />,
    document.getElementById('dg-container')
  );
}, true);

