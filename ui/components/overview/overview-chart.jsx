import Plot from "../plot";
import {pluck} from "../../tools";

export default class OverviewPlot extends Plot{
  getData(){
    var {data} = this.props;
    return data ? ['award', 'bidplan', 'tender'].map(key => {
      return {
        x: data.map(pluck('year')),
        y: data.map(pluck(key)),
        type: 'scatter',
        name: key
      }
    }) : [];
  }

  getLayout(){
    return {
      xaxis: {
        title: "Years",
        type: "category",
        titlefont: {
          color: "#cc3c3b"
        }
      },
      yaxis: {
        title: "Count",
        titlefont: {
          color: "#cc3c3b"
        }
      }
    }
  }
}