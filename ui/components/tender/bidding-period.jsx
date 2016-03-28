import React from "react";
import Plot from "../plot";
import {pluck} from "../../tools";

export default class BiddingPeriod extends Plot{
  getData(){
    var {data} = this.props;
    var years = data.map(pluck('year'));
    return [{
      x: data.map(pluck('tender')),
      y: years,
      name: "Tender",
      type: "bar",
      orientation: 'h'
    }, {
      x: data.map(pluck('award')),
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
        title: "Days",
        titlefont: {
          color: "#cc3c3b"
        }
      },
      yaxis: {
        title: "Years",
        type: "category",
        titlefont: {
          color: "#cc3c3b"
        }
      }
    }
  }
}
