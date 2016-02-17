import React from "react";
import Component from "../pure-render-component";
import CostEffectiveness from "./cost-effectiveness";
import FundingByBidType from "./funding-by-bid-type";
import BiddingPeriod from "./bidding-period";
import {toImmutable} from "nuclear-js";

export default class Tender extends Component{
  render(){
    var {state} = this.props;
    var globalState = state.get('globalState');
    var selectedYears = globalState.get('selectedYears');
    var width = globalState.get('contentWidth');
    var data = globalState.get('data');
    var year = globalState.get('year');
    return (
        <div className="col-sm-12 content">
          <CostEffectiveness
              width={width}
              data={selectedYears.reduce((ceData, selected, year) => selected && data.hasIn(['costEffectiveness', year]) ?
                  ceData.concat([data.getIn(['costEffectiveness', year])]) :
                  ceData
              , [])}/>

          {/*data.has('bidPeriod') ?
              <BiddingPeriod
                  width={width}
                  data={data.get('bidPeriod')}
              />
          : null*/}

          {data.has('bidType') ?
            <FundingByBidType
                width={width}
                data={data.get('bidType')
                    .reduce((data, yearData, year) => selectedYears.get(year) ?
                        data.concat(yearData) :
                        data
                    , toImmutable([]))
                    .groupBy(bidType => bidType.get('_id'))
                    .map(bidTypes => bidTypes.reduce((reducedBidType, bidType) => {
                      return {
                        _id: bidType.get('_id') || "unspecified",
                        totalTenderAmount: reducedBidType.totalTenderAmount + bidType.get('totalTenderAmount')
                      }
                    }, {
                      totalTenderAmount: 0
                    }))
                    .toArray()
                }
            />
          : null}
        </div>
    )
  }
}