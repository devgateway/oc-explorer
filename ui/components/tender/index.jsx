import React from "react";
import Component from "../pure-render-component";
import CostEffectiveness from "./cost-effectiveness";
import BidSelection from "./bid-selection";
import BiddingPeriod from "./bidding-period";
import Cancelled from "./cancelled";
import CancelledPercents from "./cancelled-percents";
import AvgNrBids from "./avg-nr-bids";
import {toImmutable} from "nuclear-js";
import Comparison from "../comparison";
import translatable from "../translatable";

export default class Tender extends translatable(Component){
  render(){
    let {state, width, actions, translations} = this.props;
    let {compare, costEffectiveness, bidPeriod, bidType, cancelled, avgNrBids,
        showPercentsCancelled} = state;
    return (
        <div className="col-sm-12 content">
          {compare ?
              <Comparison
                  translations={translations}
                  width={width}
                  state={costEffectiveness}
                  Component={CostEffectiveness}
                  title={this.__("Cost effectiveness")}
              />
          :
              <CostEffectiveness
                  translations={translations}
                title={this.__("Cost effectiveness")}
                data={costEffectiveness}
                width={width}
              />
          }

          {compare ?
              <Comparison
                  translations={translations}
                  width={width}
                  state={bidPeriod}
                  Component={BiddingPeriod}
                  title={this.__("Bid period")}
              />
              :
              <BiddingPeriod
                  translations={translations}
                  title={this.__("Bid period")}
                  data={bidPeriod}
                  width={width}
              />
          }

          {compare ?
              <Comparison
                  translations={translations}
                  width={width}
                  state={bidType}
                  Component={BidSelection}
                  title={this.__("Bid Selection Method")}
              />
              :
              <BidSelection
                  translations={translations}
                  title={this.__("Bid Selection Method")}
                  data={bidType}
                  width={width}
              />
          }

          {showPercentsCancelled ?
            (compare ?
              <Comparison
                  translations={translations}
                  width={width}
                  state={cancelled}
                  Component={CancelledPercents}
                  title={this.__("Cancelled funding percentage")}
                  actions={actions}
              />
              :
              <CancelledPercents
                  translations={translations}
                  title={this.__("Cancelled funding percentage")}
                  actions={actions}
                  data={cancelled}
                  width={width}
              />
            ) : (compare ?
              <Comparison
                  translations={translations}
                  width={width}
                  state={cancelled}
                  Component={Cancelled}
                  title={this.__("Cancelled funding")}
              />
              :
              <Cancelled
                  translations={translations}
                  title={this.__("Cancelled funding")}
                  data={cancelled}
                  width={width}
                  actions={actions}
              />
            )
          }

          {compare ?
              <Comparison
                  width={width}
                  state={avgNrBids}
                  Component={AvgNrBids}
                  title={this.__("Average number of bids")}
              />
              :
              <AvgNrBids
                  title={this.__("Average number of bids")}
                  data={avgNrBids}
                  width={width}
              />
          }
        </div>
    );
  }
}