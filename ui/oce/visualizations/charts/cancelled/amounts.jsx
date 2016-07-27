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
        title: this.__("Amount"),
        titlefont: {
          color: "#cc3c3b"
        },
        range: this.props.yAxisRange
      }
    }
  }
}

CancelledFunding.endpoint = 'totalCancelledTendersByYear';

export default CancelledFunding;
