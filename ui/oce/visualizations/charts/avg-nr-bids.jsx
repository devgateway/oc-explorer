import FrontendYearFilterableChart from "./frontend-filterable";
import {pluckImm} from "../../tools";
import {Map} from "immutable";
import Plotly from "plotly.js/lib/core";

class AvgNrBids extends FrontendYearFilterableChart{
  getData(){
    let data = super.getData();
    if(!data) return [];
    return [{
      x: data.map(pluckImm('year')).toArray(),
      y: data.map(pluckImm('averageNoTenderers')).toArray(),
      type: 'bar'
    }];
  }

  getLayout(){
    return {
      xaxis: {
        title: this.__("Number"),
        type: "category",
        titlefont: {
          color: "#cc3c3b"
        }
      },
      yaxis: {
        title: this.__("Amount"),
        titlefont: {
          color: "#cc3c3b"
        }
      }
    }
  }
}

AvgNrBids.endpoint = 'averageNumberOfTenderers';
AvgNrBids.getName = __ => __('Average number of bids');
AvgNrBids.getFillerDatum = year => Map({
  year,
  averageNoTenderers: 0
});
export default AvgNrBids;