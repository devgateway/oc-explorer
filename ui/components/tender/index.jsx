import React from "react";
import Component from "../pure-render-component";
import CostEffectiveness from "./cost-effectiveness";
import BiddingPeriod from "./bidding-period";
import FundingByBidType from "./funding-by-bid-type";

export default class Tender extends Component{
  render(){
    var {state} = this.props;
    var width = state.getIn(['globalState', 'contentWidth']);
    var data = state.getIn(['globalState', 'data']);
    var year = state.getIn(['globalState', 'year']);
    return (
        <div className="col-sm-12 content">
          <CostEffectiveness width={width} data={data.get('costEffectiveness')}/>
          <BiddingPeriod width={width}/>
          <FundingByBidType width={width} data={data.getIn(['bidType', year], [])}/>
        </div>
    )
  }
}