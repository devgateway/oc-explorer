import React from "react";
import Component from "../pure-render-component";
import CostEffectiveness from "./cost-effectiveness";
import FundingByBidType from "./funding-by-bid-type";
import BiddingPeriod from "./bidding-period";
import Cancelled from "./cancelled";
import {toImmutable} from "nuclear-js";
import Comparison from "../comparison";
import {pluck} from "../../tools";

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
      var costEffectivenessData = globalState.getIn(['comparisonData', 'costEffectiveness']);
      if(!costEffectivenessData) return null;
      var minValue = Math.min.apply(Math, costEffectivenessData.map(datum =>
          Math.min.apply(Math, datum.map(pluck('tender')))
      ));
      var maxValue = Math.max.apply(Math, costEffectivenessData.map(datum =>
          Math.max.apply(Math, datum.map(pluck('tender')))
      ));
      return (
          <Comparison
            years={selectedYears}
            width={width}
            data={costEffectivenessData}
            Component={CostEffectiveness}
            yAxisRange={[minValue, maxValue]}
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

  getBiddingPeriod(){
    var globalState = this.props.state.get('globalState');
    var selectedYears = globalState.getIn(['filters', 'years']);
    var width = globalState.get('contentWidth');
    var data = globalState.get('data');
    if(globalState.get('compareBy')){
      var bidPeriodData = globalState.getIn(['comparisonData', 'bidPeriod']);
      if(!bidPeriodData) return null;
      var minValue = Math.min.apply(Math, bidPeriodData.map(datum =>
          Math.min.apply(Math, ["award", "tender"].map(key =>
              Math.min.apply(Math, datum.map(pluck(key)))
          ))
      ));
      var maxValue = Math.max.apply(Math, bidPeriodData.map(datum =>
          Math.max.apply(Math, ["award", "tender"].map(key =>
              Math.max.apply(Math, datum.map(pluck(key)))
          ))
      ));
      return (
          <Comparison
              years={selectedYears}
              width={width}
              data={bidPeriodData}
              Component={BiddingPeriod}
              xAxisRange={[minValue, maxValue]}
          />
      )
    } else {
      return (
          <BiddingPeriod
              years={selectedYears}
              width={width}
              data={data.get('bidPeriod')}
          />
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
          {this.getBiddingPeriod()}

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