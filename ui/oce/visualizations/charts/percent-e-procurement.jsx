import FrontendYearFilterableChart from "./frontend-filterable";
import {pluckImm} from "../../tools";

class PercentEProcurement extends FrontendYearFilterableChart{
  getData(){
    let data = super.getData();
    if(!data) return [];
    return [{
      x: data.map(pluckImm('year')).toArray(),
      y: data.map(pluckImm('percentEgp')).toArray(),
      type: 'scatter',
      fill: 'tonexty'
    }];
  }

  getLayout(){
    return {
      xaxis: {
        title: "Years",
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


PercentEProcurement.endpoint = 'percentTendersUsingEgp';
PercentEProcurement.getName = __ => __('% of tenders using eProcurement');
PercentEProcurement.getMaxField = imm => imm.get('percentEgp', 0);

export default PercentEProcurement;