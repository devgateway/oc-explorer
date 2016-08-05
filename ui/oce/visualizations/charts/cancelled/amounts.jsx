import FrontendYearFilterableChart from "../frontend-filterable";
import {pluckImm} from "../../../tools";

class CancelledFunding extends FrontendYearFilterableChart{
  transform(data){
    return data.map(({_id, totalCancelledTendersAmount}) => ({
      year: _id,
      count: totalCancelledTendersAmount
    }));
  }

  getData(){
    let data = super.getData();
    if(!data) return [];
    return [{
      x: data.map(pluckImm('year')).toArray(),
      y: data.map(pluckImm('count')).toArray(),
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
        title: this.__("Years"),
        type: 'category'
      },
      yaxis: {
        title: this.__("Amount")
      }
    }
  }
}

CancelledFunding.endpoint = 'totalCancelledTendersByYear';

export default CancelledFunding;
