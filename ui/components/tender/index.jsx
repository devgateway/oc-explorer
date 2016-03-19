import React from "react";
import Component from "../pure-render-component";
import CostEffectiveness from "./cost-effectiveness";
import FundingByBidType from "./funding-by-bid-type";
import BiddingPeriod from "./bidding-period";
import Cancelled from "./cancelled";
import {toImmutable} from "nuclear-js";
import Comparison from "../comparison";

export default class Tender extends Component{
  getCostEffectiveness(){
    var globalState = this.props.state.get('globalState');
    var selectedYears = globalState.getIn(['filters', 'years']);
    var width = globalState.get('contentWidth');
    var data = globalState.get('data');
    if(globalState.get('compareBy')){
      return (
          <Comparison
            years={selectedYears}
            width={width}
            data={globalState.getIn(['comparisonData', 'costEffectiveness'])}
            Component={CostEffectiveness}
          />
      )
    } else {
      return (
          <CostEffectiveness
              years={selectedYears}
              width={width}
              data={data.get('costEffectiveness')}/>
      )
    }
  }

  render(){
    var {state} = this.props;
    var globalState = state.get('globalState');
    var selectedYears = globalState.getIn(['filters', 'years']);
    var width = globalState.get('contentWidth');
    var data = globalState.get('data');
    return (
        <div className="col-sm-12 content">
          {this.getCostEffectiveness()}

          <BiddingPeriod
              years={selectedYears}
              width={width}
              data={data.get('bidPeriod')}
          />

          {data.has('bidType') ?
            <FundingByBidType
                width={width}
                data={data.get('bidType')
                    .filter(bidType => globalState.getIn(['filters', 'years', bidType.get('year')], false))
                    .groupBy(bidType => bidType.get('procurementMethodDetails'))
                    .map(bidTypes => bidTypes.reduce((reducedBidType, bidType) => {
                      return {
                        _id: bidType.get('procurementMethodDetails') || "unspecified",
                        totalTenderAmount: reducedBidType.totalTenderAmount + bidType.get('totalTenderAmount')
                      }
                    }, {
                      totalTenderAmount: 0
                    }))
                    .toArray()
                }
            />
          : null}

          <Cancelled
              years={selectedYears}
              width={width}
              data={data.get('cancelled')}
          />
        </div>
    )
  }
}