import Plot from "../plot";
import {pluck} from "../../tools";

export default class CostEffectiveness extends Plot{
  getData(){
    var {data} = this.props;
    var years = data.map(pluck('year'))

    var bidPrice = {
      x: years,
      y: data.map(pluck('tender')),
      name: 'Bid price',
      type: 'bar'
    };

    var diff = {
      x: years,
      y: data.map(pluck('tender')),
      name: 'Difference',
      type: 'bar'
    };

    return [bidPrice, diff];
  }

  getLayout(){
    return {
      barmode: "stack",
      xaxis: {
        type: "category"
      },
      yaxis: {
        range: this.props.yAxisRange
      }
    }
  }
}