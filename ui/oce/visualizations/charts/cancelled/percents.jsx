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
        title: this.__("Percent"),
        titlefont: {
          color: "#cc3c3b"
        },
        range: this.props.yAxisRange
      }
    }
  }
}

CancelledPercents.endpoint = 'percentTendersCancelled';
CancelledPercents.getMaxField = imm => imm.get('percentCancelled');

export default CancelledPercents;


