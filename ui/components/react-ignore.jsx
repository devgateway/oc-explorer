import React, {Component} from "react/addons";

export default class ReactIgnore extends Component{
  shouldComponentUpdate(){
    return false;
  }

  render(){
    return React.Children.only(this.props.children);
  }
}