import React from "react";
import Component from "../pure-render-component";
import PercentEbid from "./percent-ebid";
import {toImmutable} from "nuclear-js";
import Comparison from "../comparison";
import translatable from "../translatable";

export default class Tender extends translatable(Component){
  render(){
    let {state, width} = this.props;
    let {compare, percentEbid} = state;
    return (
        <div className="col-sm-12 content">
          {compare ?
              <Comparison
                  width={width}
                  state={percentEbid}
                  Component={PercentEbid}
                  title={this.__("% of tenders using eBid")}
              />
              :
              <PercentEbid
                  title={this.__("% of tenders using eBid")}
                  data={percentEbid}
                  width={width}
              />
          }
        </div>
    );
  }
}