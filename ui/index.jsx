import App from "./components/app";
import React from "react";
import flux from "./flux";

flux.onUpdate(state =>
  React.render(
    <App state={state} actions={flux.actions}/>,
    document.getElementById('dg-container')
  )
, true);
