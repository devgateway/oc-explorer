import App from "./components/app";
import React from "react";
import flux from "./flux";
import {debounce} from "./tools";
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

window.addEventListener("resize", debounce(function(){
  flux.actions.changeContentWidth(document.querySelector('.years-bar').offsetWidth)
}));

flux.actions.bootstrap();