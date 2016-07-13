import React from "react";
import cn from "classnames";

export default class OCApp extends React.Component{
  constructor(props, config){
    super(props);
    this.config = config;
    this.state = {
      currentTab: 0
    }
  }

  __(text){
    return text;
  }

  filters(){
    return <h1>filters</h1>
  }

  comparison(){
    return <h1>comparison</h1>
  }

  navigationLink({name, icon}, index){
    return <a href="javascript:void(0);" key={index}
              className={cn("col-sm-12", {active: index == this.state.currentTab})}
              onClick={_ => this.setState({currentTab: index})}>
          <span className="circle">
            <i className={`glyphicon glyphicon-${icon}`}/>
          </span>
      &nbsp;
      {name()}
    </a>
  }

  navigation(){
    return this.config.tabs.map((tab, index) => this.navigationLink(tab, index));
  }

  content(){
    let tab = this.config.tabs[this.state.currentTab];
    if(tab.Component) return <tab.Component/>;
    return <h1>content</h1>
  }

  yearsBar(){
    return <h1>years bar</h1>
  }
}