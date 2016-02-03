import Component from "../pure-render-component";
import {BarStackChart} from "react-d3-basic";
import Plotly from "plotly.js";

export default class CostEffectiveness extends Component{
  componentDidMount(){
    var years = this.props.data.map(datum => datum.year + "");

    var bidPrice = {
      x: years,
      y: this.props.data.map(datum => datum.tender),
      name: 'Bid price',
      type: 'bar'
    };

    var diff = {
      x: years,
      y: this.props.data.map(datum => datum.diff),
      name: 'Difference',
      type: 'bar'
    };

    var data = [bidPrice, diff];

    var layout = {barmode: 'stack'};

    this.chart = Plotly.newPlot(this.refs.chartContainer, data, layout);
  }

  render(){
    return (
        <section>
          <h4 className="page-header">Cost effectiveness</h4>
          <div ref="chartContainer"></div>
        </section>
    )
  }
}