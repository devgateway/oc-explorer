import FrontendYearFilterableChart from "./frontend-filterable";
import {pluckImm, response2obj} from "../../tools";
import {Map} from "immutable";

let ensureNonNegative = a => a < 0 ? 0 : a;

class BidPeriod extends FrontendYearFilterableChart {
  transform([tenders, awards]) {
    let awardsHash = response2obj('averageAwardDays', awards);
    return tenders.map(tender => ({
      year: tender._id,
      tender: +tender.averageTenderDays.toFixed(2),
      award: +(awardsHash[tender._id] || 0).toFixed(2)
    }))
  };

  getData() {
    let data = super.getData();
    if (!data) return [];
    let years = data.map(pluckImm('year')).toArray();
    return [{
      x: data.map(pluckImm('tender')).map(ensureNonNegative).toArray(),
      y: years,
      name: this.__("Tender"),
      type: "bar",
      orientation: 'h',
      marker: {
        color: this.props.styling.charts.traceColors[0]
      }
    }, {
      x: data.map(pluckImm('award')).map(ensureNonNegative).toArray(),
      y: years,
      name: this.__("Award"),
      type: "bar",
      orientation: 'h',
      marker: {
        color: this.props.styling.charts.traceColors[1]
      }
    }];
  }

  getLayout() {
    let annotations = [];
    let data = super.getData();
    if(data){
      annotations = data.map((imm, index) => {
        let sum = (ensureNonNegative(imm.get('tender')) + ensureNonNegative(imm.get('award'))).toFixed(2);
        return {
          y: index,
          x: sum,
          xanchor: 'left',
          yanchor: 'middle',
          text: this.__('Total:') + ' ' + sum,
          showarrow: false
        }
      }).toArray();
    }

    return {
      annotations,
      barmode: "stack",
      xaxis: {
        title: this.__("Days")
      },
      yaxis: {
        title: this.__("Years"),
        type: "category"
      }
    }
  }
}

BidPeriod.endpoints = ['averageTenderPeriod', 'averageAwardPeriod'];
BidPeriod.getName = __ => __('Bid period');
BidPeriod.horizontal = true;
BidPeriod.getFillerDatum = year => Map({
  year,
  tender: 0,
  award: 0
});
BidPeriod.getMaxField = imm => imm.get('tender', 0) + imm.get('award', 0);

export default BidPeriod;