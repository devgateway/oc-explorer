import FrontendYearFilterableChart from "./frontend-filterable";
import {pluckImm} from "../../tools";

class PercentEbid extends FrontendYearFilterableChart{
  getData(){
    let data = super.getData();
    if(!data) return [];
    return [{
      x: data.map(pluckImm('year')).toArray(),
      y: data.map(pluckImm('percentageTendersUsingEbid')).toArray(),
      type: 'scatter',
      fill: 'tonexty',
      marker: {
        color: this.props.styling.charts.traceColors[0]
      }
    }];
  }

  getLayout(){
    return {
      xaxis: {
        title: this.__("Year"),
        type: 'category'
      },
      yaxis: {
        title: this.__("Percent")
      }
    }
  }
}

PercentEbid.endpoint = 'percentTendersUsingEBid';
PercentEbid.getName = __ => __('Percent of Tenders Using e-Bid');
PercentEbid.getMaxField = pluckImm('percentageTendersUsingEbid');

export default PercentEbid;