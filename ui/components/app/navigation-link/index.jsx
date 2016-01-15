import React from "react";
import Component from "../../pure-render-component";
import cn from "classnames";
require("./style.less");

export default class NavigationLink extends Component{
  render(){
    var {active, text, tab, actions, marker} = this.props;
    return (
        <a href="javascript:void(0);" className={cn("col-sm-12", {active: active})} onClick={_ => actions.changeTab(tab)}>
          <span className="circle">
            <i className={`glyphicon glyphicon-${marker}`}/>
          </span>
          &nbsp;
          {text}
        </a>
    )
  }
}