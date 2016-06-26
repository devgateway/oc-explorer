import App from "./components/app";
import React from "react";
import ReactDOM from "react-dom";
import flux from "./flux";
import {debounce} from "./tools";
var TRANSLATIONS = {
  en: require('./languages/en_US.json'),
  vn: require('./languages/vn_VN.json')
};

flux.onUpdate(state =>
  ReactDOM.render(
    <App state={state} actions={flux.actions} translations={TRANSLATIONS[state.getIn(['globalState', 'locale'])]}/>,
    document.getElementById('dg-container')
  )
, true);

window.addEventListener("resize", debounce(function(){
  flux.actions.changeContentWidth(document.querySelector('.years-bar').offsetWidth)
}));

flux.actions.bootstrap();