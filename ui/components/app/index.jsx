import React from "react";
import Component from "../pure-render-component";
import NavigationLink from "./navigation-link";
import {tabs} from "../../flux/stores/global-state";
import cn from "classnames";
import Planning from "../planning";
import TenderAward from "../tender-award";
require('./style.less');

export default class App extends React.Component{
  render(){
    var {state, actions} = this.props;
    var navigationLink = (text, tab) =>
        <NavigationLink text={text} actions={actions} tab={tab} active={state.getIn(['globalState', 'tab']) == tab}/>
    return (
      <div className="container-fluid">
        <div className="row main-title">
          <h1>Planned Procurement Visualization</h1>
        </div>
        <div className="row">
          <aside className="col-sm-4 col-md-2">
            <div className="row">
              <section className="col-sm-12">
                <h2>
                  E-procurement
                  <small>Toolkit</small>
                </h2>
              </section>
              <div role="navigation">
                {navigationLink("Planning", tabs.PLANNING)}
                {navigationLink("Tender", tabs.TENDER_AWARD)}
              </div>
              <section className="col-sm-12">
                <p><strong>Toolkit description</strong></p>
                <p>
                  <small>
                    Lorem ipsum dolor sit amet, consectetur adipiscing elit. Etiam dolor ex, dapibus id consectetur ut, aliquet sed ipsum. Phasellus leo massa, ullamcorper vel auctor eu, fermentum in ante. In consequat ut metus ac pretium. Fusce et lorem aliquet, pretium sapien vel, eleifend nunc.
                  </small>
                </p>
              </section>
              <section className="col-sm-12 faq">
                <strong>
                  Frequently Asked Questions
                </strong>
              </section>
            </div>
          </aside>
          <div className="col-sm-8 col-md-10">
            <div className="row">
              <div className="col-sm-12 years-bar" role="navigation">
                {[2015, 2014, 2013, 2012, 2011, 2010].map(year => (
                  <a
                    key={year}
                    href="javascript:void(0);"
                    className={cn({active: year == state.getIn(['globalState','year'])})}
                    onClick={e => actions.changeYear(year)}
                  >
                    <i className="glyphicon glyphicon-ok-circle"></i> {year}
                  </a>
                ))}
              </div>
              {state.getIn(['globalState', 'tab']) == tabs.PLANNING ?
                  <Planning/> :
                  <TenderAward/>
              }
            </div>
          </div>
        </div>
      </div>
    )
  }
}