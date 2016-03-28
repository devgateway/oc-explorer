import Plot from "../plot";
import {pluck} from "../../tools";

export default class FundingByBidType extends Plot {

  getData(){
    return [{
      x: this.props.data.map(pluck('_id')),
      y: this.props.data.map(pluck('totalTenderAmount')),
      type: 'bar'
    }];
  }

  getLayout(){
    return {
      xaxis: {
        title: "Category",
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