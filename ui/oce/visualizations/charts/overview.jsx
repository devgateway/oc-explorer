import FrontendYearFilterableChart from "./frontend-filterable";
import {response2obj, pluckImm} from "../../tools";

class OverviewChart extends FrontendYearFilterableChart{
  transform([bidplansResponse, tendersResponse, awardsResponse]){
    let bidplans = response2obj('count', bidplansResponse);
    let tenders = response2obj('count', tendersResponse);
    let awards = response2obj('count', awardsResponse);
    return Object.keys(tenders).map(year => ({
      year: year,
      bidplan: bidplans[year],
      tender: tenders[year],
      award: awards[year]
    }));
  }

  getData(){
    var data = super.getData();
    if(!data) return [];
    let LINES = {
      award: this.__("Award"),
      bidplan: this.__("Bid plan"),
      tender: this.__("Tender")
    };
    let years = data.map(pluckImm('year')).toArray();
    return Object.keys(LINES).map(key => ({
          x: years,
          y: data.map(pluckImm(key)).toArray(),
          type: 'scatter',
          name: LINES[key]
        })
    );
  }

  getLayout(){
    return {
      xaxis: {
        title: this.__("Years"),
        type: "category",
        titlefont: {
          color: "#cc3c3b"
        }
      },
      yaxis: {
        title: this.__("Count"),
        titlefont: {
          color: "#cc3c3b"
        }
      }
    }
  }
}

OverviewChart.endpoints = ['countBidPlansByYear', 'countTendersByYear', 'countAwardsByYear'];

OverviewChart.getName = __ => __("Overview");

export default OverviewChart;