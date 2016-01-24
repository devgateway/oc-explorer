import React from "react";
import Component from "../pure-render-component";

export default class Popup extends Component{
  render(){
    var {name, amount} = this.props.data;
    return (
        <div>
          <h3>{name}</h3>
          <p>
            <strong>Total Planned Amount (VND):</strong> {amount.toLocaleString()}
          </p>
        </div>
    )
  }
}