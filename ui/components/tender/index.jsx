import React from "react";
import Component from "../pure-render-component";
import CostEffectiveness from "./cost-effectiveness";
import FundingByBidType from "./funding-by-bid-type";
import BiddingPeriod from "./bidding-period";
import Cancelled from "./cancelled";
import {toImmutable} from "nuclear-js";
import Comparison from "../comparison";
import {pluck} from "../../tools";

export default class Tender extends Component{
  render(){
    let {state, width} = this.props;
    let {compare, costEffectiveness, bidPeriod, bidType, cancelled} = state;
    return (
        <div className="col-sm-12 content">
          {compare ?
              <Comparison
                  width={width}
                  state={costEffectiveness}
                  Component={CostEffectiveness}
                  title="Cost effectiveness"
              />
          :
              <CostEffectiveness
                title="Cost effectiveness"
                data={costEffectiveness}
                width={width}
              />
          }

          {compare ?
              <Comparison
                  width={width}
                  state={bidPeriod}
                  Component={BiddingPeriod}
                  title="Bid period"
              />
              :
              <BiddingPeriod
                  title="Bid period"
                  data={bidPeriod}
                  width={width}
              />
          }

          {compare ?
              <Comparison
                  width={width}
                  state={bidType}
                  Component={FundingByBidType}
                  title="Funding by bid type"
              />
              :
              <FundingByBidType
                  title="Funding by bid type"
                  data={bidType}
                  width={width}
              />
          }

          {compare ?
              <Comparison
                  width={width}
                  state={cancelled}
                  Component={Cancelled}
                  title="Cancelled funding"
              />
              :
              <Cancelled
                  title="Cancelled funding"
                  data={cancelled}
                  width={width}
              />
          }
        </div>
    );
  }
}