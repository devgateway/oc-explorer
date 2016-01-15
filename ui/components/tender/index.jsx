import React from "react";
import Component from "../pure-render-component";
import CostEffectiveness from "./cost-effectiveness";
import BiddingPeriod from "./bidding-period";
import FundingByBidType from "./funding-by-bid-type";

export default class Tender extends Component{
  render(){
    return (
        <div className="col-sm-12">
          <CostEffectiveness/>
          <BiddingPeriod/>
          <FundingByBidType/>
        </div>
    )
  }
}