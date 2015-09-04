import React from "react";
require('./style.less');

export default class Counter extends React.Component{
  constructor(props){
    super(props);
    this.state = {
      counter: 0
    }
  }
  render(){
    var {counter} = this.state;
    return (
      <p>
        <i className="glyphicon glyphicon-minus"></i>&nbsp;
        {counter}&nbsp;
        <i className="glyphicon glyphicon-plus"></i>
      </p>
    )
  }
}