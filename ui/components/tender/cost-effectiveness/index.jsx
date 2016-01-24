import React from "react";
import Component from "../../pure-render-component";
import {BarStackChart} from "react-d3-basic";
require("./style.less");

export default class CostEffectiveness extends Component{
  render(){
    return (
        <section>
          <h4 className="page-header">Cost effectivness</h4>
          <BarStackChart
              data={this.props.data}
              chartSeries={[{
                    field: "tender",
                    name: "Bid price",
                    color: "#639bb4"
                  },
                  {
                    field: "diff",
                    name: "Difference",
                    color: "#a5c651"
                  }]}
              x={d => d.year}
              xScale="ordinal"
              yScale="linear"
              width={this.props.width}
              height={350}
              margins={{top: 80, right: 100, bottom: 80, left: 200}}
              legendClassName="cost-effectiveness-legend"
          />
        </section>
    )

  }
}