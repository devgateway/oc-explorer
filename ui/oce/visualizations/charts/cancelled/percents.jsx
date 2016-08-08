import FrontendYearFilterableChart from "../frontend-filterable";
import {pluckImm} from "../../../tools";

class CancelledPercents extends FrontendYearFilterableChart{
  getData(){
    let data = super.getData();
    if(!data) return [];
    return [{
      x: data.map(pluckImm('year')).toArray(),
      y: data.map(pluckImm('percentCancelled')).toArray(),
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
        title: this.__("Percent")
      }
    }
  }
}

CancelledPercents.endpoint = 'percentTendersCancelled';
CancelledPercents.getMaxField = imm => imm.get('percentCancelled');

export default CancelledPercents;


