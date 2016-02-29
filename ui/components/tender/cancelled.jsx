import React from "react";
import Plot from "../plot";
import {pluck} from "../../tools";

export default class BiddingPeriod extends Plot{
  getTitle() {
    return "Cancelled funding"
  }

  getData(){
    if(!this.props.data) return [];
    return [{
      x: this.props.data.map(pluck('_id')),
      y: this.props.data.map(pluck('totalCancelledTendersAmount')),
      type: 'scatter',
      fill: 'tonexty'
    }];
  }

  getLayout(){
    return {
      xaxis: {
        title: "Years",
        titlefont: {
          color: "#cc3c3b"
        }
      },
      yaxis: {
        title: "Amount",
        titlefont: {
          color: "#cc3c3b"
        }
      }
    }
  }
}
