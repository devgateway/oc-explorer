import React from "react";
import Component from "../pure-render-component";
import {BarChart} from "react-d3-basic";
import Plotly from "plotly.js";

export default class FundingByBidType extends Component{
  componentDidMount(){

    var data = [
      {
        x: this.props.data.map(datum => datum._id),
        y: this.props.data.map(datum => datum.totalTenderAmount),
        type: 'bar'
      }
    ];

    this.chart = Plotly.newPlot(this.refs.chartContainer, data);
  }

  render(){
    return (
        <section>
          <h4 className="page-header">Funding by bid type</h4>
          <div ref="chartContainer"></div>
        </section>
    )
  }
}