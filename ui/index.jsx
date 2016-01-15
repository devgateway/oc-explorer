import App from "./components/app";
import React from "react";
import flux from "./flux";
import {debounce} from "./tools";

flux.onUpdate(state =>
  React.render(
    <App state={state} actions={flux.actions}/>,
    document.getElementById('dg-container')
  )
, true);

window.addEventListener("resize", debounce(function(){
  flux.actions.changeContentWidth(document.querySelector('.years-bar').offsetWidth)
}));