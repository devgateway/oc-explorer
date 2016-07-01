import Plot from "../plot";
import {pluck} from "../../tools";

export default class FundingByBidType extends Plot {

  getData(){
    return [{
      x: this.props.data.map(pluck('year')),
      y: this.props.data.map(pluck('averageNoTenderers')),
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