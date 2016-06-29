import React from "react";
import Plot from "../plot";
import {pluck} from "../../tools";

export default class BiddingPeriod extends Plot{
  getTitle() {
    return "Cancelled funding"
  }

  getData(){
    let {data} = this.props;
    return [{
      x: data.map(pluck('year')),
      y: data.map(pluck('percentCancelled')),
      type: 'scatter',
      fill: 'tonexty'
    }];
  }

  getLayout(){
    return {
      xaxis: {
        title: this.__("Years"),
        type: 'category',
        titlefont: {
          color: "#cc3c3b"
        }
      },
      yaxis: {
        title: this.__("Percent"),
        titlefont: {
          color: "#cc3c3b"
        },
        range: this.props.yAxisRange
      }
    }
  }

  render(){
    let {pageHeaderTitle, title, actions} = this.props;
    return (
        <section>
          {pageHeaderTitle &&
          <h4 className="page-header">
            {title}
            &nbsp;
            <button className="btn btn-default btn-sm" onClick={e => actions.toggleCancelledPercents(false)}>&#8363;</button>
          </h4>
          }
          <div ref="chartContainer"></div>
        </section>
    )
  }
}
