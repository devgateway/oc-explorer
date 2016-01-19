import React from "react";
import Component from "../pure-render-component";
import {tabs} from "../../flux/stores/global-state";
import Planning from "../planning";
import Tender from "../tender";
import NavigationLink from "./navigation-link";
import {years} from "../../tools";
import cn from "classnames";
require('./style.less');

export default class App extends React.Component{
  componentDidMount(){
    this.props.actions.changeContentWidth(document.querySelector('.years-bar').offsetWidth);
  }

  render(){
    var {state, actions} = this.props;
    var navigationLink = (text, marker, tab) =>
        <NavigationLink text={text} actions={actions} tab={tab} marker={marker} active={state.getIn(['globalState', 'tab']) == tab}/>
    var globalState = state.get('globalState');
    return (
      <div className="container-fluid">
        <aside className="col-xs-4 col-md-3 col-lg-2">
          <div className="row">
            <section className="col-sm-12">
              <h1>
                E-procurement
                <small>Toolkit</small>
              </h1>
            </section>
            <div role="navigation">
              {navigationLink("Planning", 'map-marker', tabs.PLANNING)}
              {navigationLink("Tender", 'time', tabs.TENDER_AWARD)}
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
        <div className="col-xs-offset-4 col-md-offset-3 col-lg-offset-2 col-xs-8 col-md-9 col-lg-10 years-bar" role="navigation">
          {years().map(year => (
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
        <div className="col-xs-offset-4 col-md-offset-3 col-lg-offset-2 col-xs-8 col-md-9 col-lg-10">
          <div className="row">
            {globalState.get('tab') == tabs.PLANNING ?
                <Planning
                    width={globalState.get('contentWidth')}
                    locations={globalState.getIn(['data', 'locations', globalState.get('year')], [])}
                /> :
                <Tender {...this.props}/>
            }
          </div>
        </div>
      </div>
    )
  }
}