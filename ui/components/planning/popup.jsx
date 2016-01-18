import React from "react";
import Component from "../pure-render-component";

export default class Popup extends Component{
  render(){
    var {name, amount} = this.props.data;
    return (
        <table className="table table-bordered">
          <tbody>
            <tr>
              <th>Name</th>
              <td>{name}</td>
            </tr>
            <tr>
              <th>Total planned amount</th>
              <td>{amount}</td>
            </tr>
          </tbody>
        </table>
    )
  }
}