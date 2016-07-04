import React from "react";
import Plot from "../plot";
import {pluck} from "../../tools";

export default class PercentEProcurement extends Plot{
  getTitle() {
    return "% of tenders using eProcurement"
  }

  getData(){
    let {data} = this.props;
    return [{
      x: data.map(pluck('year')),
      y: data.map(pluck('percentEgp')),
      type: 'scatter',
      fill: 'tonexty'
    }];
  }

  getLayout(){
    return {
      xaxis: {
        title: "Years",
        type: 'category',
        titlefont: {
          color: "#cc3c3b"
        }
      },
      yaxis: {
        title: "%",
        titlefont: {
          color: "#cc3c3b"
        },
        range: this.props.yAxisRange
      }
    }
  }
}
