import App from "./components/app";
import React from "react";
import flux from "./flux";
var TRANSLATIONS = {
  en: require('./languages/en_US.json'),
  ro: require('./languages/ro_RO.json')
};

flux.onUpdate(state =>
  React.render(
    <App state={state} actions={flux.actions} translations={TRANSLATIONS[state.getIn(['globalState', 'locale'])]}/>,
    document.getElementById('dg-container')
  )
, true);
