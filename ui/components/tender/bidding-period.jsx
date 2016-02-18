import React from "react";
import Plot from "../plot";
import {pluck} from "../../tools";

export default class BiddingPeriod extends Plot{
  getTitle() {
    return "Overview chart"
  }

  getData(){
    if(!this.props.data) return [];
    var years = this.props.data.map(pluck('year'));
    return [{
      x: this.props.data.map(pluck('tender')),
      y: years,
      name: "Tender",
      type: "bar",
      orientation: 'h'
    }, {
      x: this.props.data.map(pluck('award')),
      y: years,
      name: "Award",
      type: "bar",
      orientation: 'h'
    }];
  }

  getLayout(){
    return {
      barmode: "stack",
      xaxis: {
        title: "Years",
        titlefont: {
          color: "#cc3c3b"
        }
      },
      yaxis: {
        title: "Days",
        titlefont: {
          color: "#cc3c3b"
        }
      }
    }
  }
}
