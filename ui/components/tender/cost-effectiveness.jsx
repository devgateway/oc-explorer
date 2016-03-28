import Plot from "../plot";
import {pluck} from "../../tools";

export default class CostEffectiveness extends Plot{
  getData(){
    var {data} = this.props;
    var years = data.map(pluck('year'));

    var bidPrice = {
      x: years,
      y: data.map(pluck('tender')),
      name: 'Bid price',
      type: 'bar'
    };

    var diff = {
      x: years,
      y: data.map(pluck('diff')),
      name: 'Difference',
      type: 'bar'
    };

    return [bidPrice, diff];
  }

  getLayout(){
    return {
      barmode: "stack",
      xaxis: {
        title: "Years",
        type: "category",
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