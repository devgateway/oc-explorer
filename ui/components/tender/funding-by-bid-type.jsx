import Plot from "./plot";

export default class FundingByBidType extends Plot {
  getTitle() {
    return "Funding by bid type"
  }

  getData(){
    return [{
      x: this.props.data.map(datum => datum._id),
      y: this.props.data.map(datum => datum.totalTenderAmount),
      type: 'bar'
    }];
  }

  getLayout(){}
}