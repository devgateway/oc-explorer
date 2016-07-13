import React from "react";
import cn from "classnames";
import {fetchJson, identity} from "../tools";
import {fromJS} from "immutable";
import Compare from "../components/app/filters/compare";

const MENU_BOX_COMPARE = "compare";

export default class OCApp extends React.Component{
  constructor(props, config){
    super(props);
    this.config = config;
    this.state = {
      currentTab: 0,
      menuBox: "",
      compareBy: "",
      data: fromJS({})
    };

    let tab = this.config.tabs[this.state.currentTab];
    tab.sections.map(
        ({endpoints, transform = identity}, index) =>
            Promise.all(endpoints.map(ep => fetchJson(`/api/${ep}`)))
                .then(transform)
                .then(fromJS)
                .then(data => this.setState({data: this.state.data.setIn([this.state.currentTab, index], data)}))
    );
  }

  __(text){
    return text;
  }

  filters(){
    return <h1>filters</h1>
  }

  updateComparisonCriteria(compareBy){
    this.setState({compareBy});
  }

  comparison(){
    let {menuBox, compareBy} = this.state;
    return <Compare
        open={menuBox == MENU_BOX_COMPARE}
        compareBy={compareBy}
        onClick={_ => this.setState({menuBox: MENU_BOX_COMPARE == menuBox ? "" : MENU_BOX_COMPARE})}
        onChange={compareBy => this.updateComparisonCriteria(compareBy)}
    />
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

  section({Component}, index){
    let {data, currentTab} = this.state;
    return <Component
        key={index}
        data={data.getIn([currentTab, index])}
    />
  }

  content(){
    let tab = this.config.tabs[this.state.currentTab];
    if(tab.Component) return <tab.Component/>;
    if(tab.sections){
      return tab.sections.map((config, index) => this.section(config, index));
    }
    return <h1>content</h1>
  }

  yearsBar(){
    return <h1>years bar</h1>
  }
}