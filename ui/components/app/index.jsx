import React from "react";
import Component from "../pure-render-component";
import {tabs} from "../../flux/stores/global-state";
import Overview from "../overview";
import Planning from "../planning";
import Competitiveness from "../competitiveness";
import Efficiency from "../efficiency";
import EProcurement from "../e-procurement";
import NavigationLink from "./navigation-link";
import cn from "classnames";
import {toImmutable} from "nuclear-js";
import Filters from "./filters";
import ComparisonCriteria from "./filters/compare";
import translatable from "../translatable";
require('./style.less');

export default class App extends translatable(Component){
  componentDidMount(){
    // this.props.actions.changeContentWidth(document.querySelector('.years-bar').offsetWidth);
  }

  render(){
    let {state, actions, translations} = this.props;
    let width = state.getIn(['globalState', 'contentWidth']);
    let navigationLink = (text, marker, tab) =>
        <NavigationLink text={text} actions={actions} tab={tab} marker={marker} active={state.getIn(['globalState', 'tab']) == tab}/>
    let globalState = state.get('globalState');
    return

    return (
      <div className="container-fluid">



      </div>
    )
  }
}