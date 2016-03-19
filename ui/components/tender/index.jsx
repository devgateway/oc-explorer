import React from "react";
import Component from "../pure-render-component";
import CostEffectiveness from "./cost-effectiveness";
import FundingByBidType from "./funding-by-bid-type";
import BiddingPeriod from "./bidding-period";
import Cancelled from "./cancelled";
import {toImmutable} from "nuclear-js";
import Comparison from "../comparison";

var sortByYear = (a, b) => {
  if(a.year < b.year) return -1;
  if(a.year == b.year) return 0;
  if(a.year < b.year) return 1;
};

export default class Tender extends Component{
  getCostEffectiveness(){
    var globalState = this.props.state.get('globalState');
    var selectedYears = globalState.getIn(['filters', 'years']);
    var width = globalState.get('contentWidth');
    var data = globalState.get('data');
    if(globalState.get('compareBy')){
      var rawData = globalState.getIn(['comparisonData', 'costEffectiveness']);
      if(!rawData) return;
      var processedData = rawData.map(rawDatum => {
        var hasYear = year => rawDatum.some(datum => datum.year == year);
        var missingYears = selectedYears.filter((selected, year) => selected && !hasYear(year));
        return rawDatum.concat(missingYears.map((_, year) => {
          return {
            year: year,
            tender: 0,
            diff: 0
          }
        }).toArray()).sort(sortByYear);
      });
      return (
          <Comparison
            years={selectedYears}
            width={width}
            data={processedData}
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