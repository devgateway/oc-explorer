import React from "react";
import Component from "../pure-render-component";
import {tabs} from "../../flux/stores/global-state";
import Planning from "../planning";
import Tender from "../tender";
import Header from "../header";
require('./style.less');

export default class App extends React.Component{
  render(){
    var {state} = this.props;
    var globalState = state.get('globalState');
    return (
      <div className="container-fluid">
        <Header {...this.props}/>
        <div className="row content">
          <div className="col-sm-offset-4 col-md-offset-2 col-sm-8 col-md-10">
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
      </div>
    )
  }
}