import React from "react";
import Component from "../pure-render-component";
import CostEffectiveness from "./cost-effectiveness";
import BidSelection from "./bid-selection";
import AvgNrBids from "./avg-nr-bids";
import {toImmutable} from "nuclear-js";
import Comparison from "../comparison";
import translatable from "../translatable";

export default class Tender extends translatable(Component){
  render(){
    let {state, width, translations} = this.props;
    let {compare, costEffectiveness, bidType, avgNrBids} = state;
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