import Plot from "../plot";
import {pluckImm} from "../../tools";
import translatable from "../translatable";

export default class OverviewPlot extends translatable(Plot){
  getData(){
    var {data} = this.props;
    if(!data) return [];
    let LINES = {
      award: this.__("Award"),
      bidplan: this.__("Bid plan"),
      tender: this.__("Tender")
    };
    let years = data.map(pluckImm('year')).toJS();
    return Object.keys(LINES).map(key => ({
        x: years,
        y: data.map(pluckImm(key)).toJS(),
        type: 'scatter',
        name: LINES[key]
      })
    );
  }

  getLayout(){
    return {
      xaxis: {
        title: this.__("Years"),
        type: "category",
        titlefont: {
          color: "#cc3c3b"
        }
      },
      yaxis: {
        title: this.__("Count"),
        titlefont: {
          color: "#cc3c3b"
        }
      }
    }
  }
}