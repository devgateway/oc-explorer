import React from "react";
import Component from "../pure-render-component";
import CostEffectiveness from "./cost-effectiveness";
import FundingByBidType from "./funding-by-bid-type";
import BiddingPeriod from "./bidding-period";
import Cancelled from "./cancelled";
import {toImmutable} from "nuclear-js";
import Comparison from "../comparison";
import {pluck} from "../../tools";

var filterBidTypeData = years => data => data
    .filter(bidType => years.get(bidType.get('year'), false))
    .groupBy(bidType => bidType.get('procurementMethodDetails'))
    .map(bidTypes => bidTypes.reduce((reducedBidType, bidType) => {
      return {
        _id: bidType.get('procurementMethodDetails') || "unspecified",
        totalTenderAmount: reducedBidType.totalTenderAmount + bidType.get('totalTenderAmount')
      }
    }, {
      totalTenderAmount: 0
    }))
    .toArray();

export default class Tender extends Component{
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

  getBidType(){
    var globalState = this.props.state.get('globalState');
    var selectedYears = globalState.getIn(['filters', 'years']);
    var width = globalState.get('contentWidth');
    var data = globalState.get('data');
    if(globalState.get('compareBy')){
      var bidTypeData = globalState.getIn(['comparisonData', 'bidType']);
      if(!bidTypeData) return null;
      var filteredBidTypeData = bidTypeData.map(filterBidTypeData(selectedYears));
      var minValue = Math.min.apply(Math, filteredBidTypeData.map(datum =>
          Math.min.apply(Math, datum.map(pluck("totalTenderAmount")))
      ));
      var maxValue = Math.max.apply(Math, filteredBidTypeData.map(datum =>
          Math.max.apply(Math, datum.map(pluck("totalTenderAmount")))
      ));
      return (
          <Comparison
            years={selectedYears}
            width={width}
            data={filteredBidTypeData}
            Component={FundingByBidType}
            yAxisRange={[minValue, maxValue]}
          />
      )
    } else {
      var bidTypeData = data.get('bidType');
      if(!bidTypeData) return null;
      return (
          <FundingByBidType
              width={width}
              data={filterBidTypeData(selectedYears)(bidTypeData)}
          />
      )
    }
  }

  getCancelled(){
    var globalState = this.props.state.get('globalState');
    var selectedYears = globalState.getIn(['filters', 'years']);
    var width = globalState.get('contentWidth');
    var data = globalState.get('data');
    if(globalState.get('compareBy')) {
      var cancelledData = globalState.getIn(['comparisonData', 'cancelled']);
      if(!cancelledData) return null;
      var minValue = Math.min.apply(Math, cancelledData.map(datum =>
          Math.min.apply(Math, datum.map(pluck("totalCancelledTendersAmount")))
      ));
      var maxValue = Math.max.apply(Math, cancelledData.map(datum =>
          Math.max.apply(Math, datum.map(pluck("totalCancelledTendersAmount")))
      ));
      return (
          <Comparison
              years={selectedYears}
              width={width}
              data={cancelledData}
              Component={Cancelled}
              yAxisRange={[minValue, maxValue]}
          />
      )
    } else {
      return (
          <Cancelled
              years={selectedYears}
              width={width}
              data={data.get('cancelled')}
          />
      )
    }
  }

  render(){
    var {state, width} = this.props;
    var {compare, costEffectiveness, bidPeriod, cancelled} = state;
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

    return (
        <div className="col-sm-12 content">
          {this.getBidType()}
          {this.getCancelled()}
        </div>
    )
  }
}