import React from "react";
import Component from "../pure-render-component";
import CostEffectiveness from "./cost-effectiveness";
import BiddingPeriod from "./bidding-period";
import FundingByBidType from "./funding-by-bid-type";

export default class Tender extends Component{
  render(){
    var width = this.props.state.getIn(['globalState', 'contentWidth']);
    return (
        <div className="col-sm-12">
          <CostEffectiveness width={width}/>
          <BiddingPeriod width={width}/>
          <FundingByBidType width={width}/>
        </div>
    )
  }
}