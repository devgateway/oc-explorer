import React from "react";
import Component from "../pure-render-component";
import CostEffectiveness from "./cost-effectiveness";
import BiddingPeriod from "./bidding-period";
import FundingByBidType from "./funding-by-bid-type";

export default class Tender extends Component{
  render(){
    var {state} = this.props;
    var width = state.getIn(['globalState', 'contentWidth']);
    return (
        <div className="col-sm-12">
          <CostEffectiveness width={width} data={state.getIn(['globalState', 'data', 'costEffectiveness'])}/>
          <BiddingPeriod width={width}/>
          <FundingByBidType width={width}/>
        </div>
    )
  }
}