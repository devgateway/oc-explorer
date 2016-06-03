import React from "react";
import Counter from "./counter";
import Component from "../pure-render-component";
import translatable from "../translatable";
require('./style.less');

export default class App extends translatable(React.Component){
  render(){
    var {state, actions} = this.props;
    return (
      <div className="container">
        <div className="col-md-12">
          <div className="jumbotron">
            <h1>{this.__("I'm just an example")}</h1>
            <p>{this.__("Edit or delete me in ./components/app")}</p>
            <p>{this.__("Documentation is in readme.md")}</p>
            <Counter value={state.getIn(['globalState', 'counter'])} actions={actions}/>
          </div>
        </div>
        <div className="col-md-12">
          {this.__("Language:")}&nbsp;
          <a href="javascript:void(0);" onClick={e => actions.setLocale("en")}>English</a>
          &nbsp;|&nbsp;
          <a href="javascript:void(0);" onClick={e => actions.setLocale("ro")}>Română</a>
        </div>
      </div>
    )
  }
}