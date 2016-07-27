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
      fill: 'tonexty'
    }];
  }

  getLayout(){
    return {
      xaxis: {
        title: this.__("Years"),
        type: 'category',
        titlefont: {
          color: "#cc3c3b"
        }
      },
      yaxis: {
        title: "%",
        titlefont: {
          color: "#cc3c3b"
        },
        range: this.props.yAxisRange
      }
    }
  }
}

PercentEbid.endpoint = 'percentTendersUsingEBid';
PercentEbid.getName = __ => __('Percent of tenders using eBid');
PercentEbid.getMaxField = pluckImm('percentageTendersUsingEbid');

export default PercentEbid;