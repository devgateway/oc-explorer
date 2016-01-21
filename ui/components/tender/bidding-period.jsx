import React from "react";
import Component from "../pure-render-component";
import {Chart, Bar} from "react-d3-shape";
import XAxis from "react-d3-core/lib/axis/xaxis";
import XGrid from "react-d3-core/lib/grid/xgrid";
import BoxPlot from "react-boxplot";

var ensurePos = maybePos => maybePos >= 0 ? maybePos : 0;

class BoxAndWhiskers extends Component{
  render(){
    var boxPlotHeight = 25;
    var {data, width, height, margins} = this.props;
    var {max, median, min, q1, q3} = data;
    var translateY = (height - margins.top - margins.bottom - boxPlotHeight ) / 2;
    return (
        <g transform={`translate(0, ${translateY})`}>
          <BoxPlot
              width={ensurePos(width - margins.left - margins.right)}
              height={boxPlotHeight}
              orientation="horizontal"
              min={0}
              max={max + 50}
              stats={{
                whiskerLow: min,
                quartile1: q1,
                quartile2: median,
                quartile3: q3,
                whiskerHigh: max
            }}
          />
        </g>
    )
  }
}

export default class BiddingPeriod extends Component{
  render(){
    var {data} = this.props;
    return (
        <section>
          <h4 className="page-header">Bidding period</h4>
          <Chart
              width= {this.props.width}
              height= {350}
              data={data}
              xDomain={[0, data.max + 50]}
              xScale="linear"
              yDomain={[0]}
          >
            <BoxAndWhiskers
                key={0}
                data={data}
            />
            <XGrid/>
            <XAxis/>
          </Chart>
        </section>
    )
  }
}