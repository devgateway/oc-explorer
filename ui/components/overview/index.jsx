import Component from "../pure-render-component";
import OverviewChart from "./overview-chart";
import TendersTable from "./tenders-table";
import AwardsTable from "./awards-table";
import Comparison from "../comparison";
import {pluck} from "../../tools";

var filterByYears = years => overviewData => {
  var filteredOverviewData = {};
  Object.keys(overviewData).forEach(key =>
      filteredOverviewData[key] = overviewData[key].filter(({_id}) => years.get(_id, false))
  );
  return filteredOverviewData;
}

export default class Overview extends Component{
  getOverviewChart(){
    var globalState = this.props.state.get('globalState');
    var data = globalState.get('data');
    var width = globalState.get('contentWidth');
    var years = globalState.getIn(['filters', 'years']);
    var filter = filterByYears(years);
    if(globalState.get('compareBy')){
      var overviewData = globalState.getIn(['comparisonData', 'overview'])
      if(!overviewData) return null;
      var filteredOverviewData = overviewData.map(filter);
      var minValue = Math.min.apply(Math, filteredOverviewData.map(datum =>
        Math.min.apply(Math, Object.keys(datum).map(key =>
          Math.min.apply(Math, datum[key].map(pluck('count')))
        ))
      ));
      var maxValue = Math.max.apply(Math, filteredOverviewData.map(datum =>
          Math.max.apply(Math, Object.keys(datum).map(key =>
              Math.max.apply(Math, datum[key].map(pluck('count'))))
          )
      ));
      return (
          <Comparison
              width={width}
              data={filteredOverviewData}
              Component={OverviewChart}
              title="Overview chart"
              yAxisRange={[minValue, maxValue]}
          />
      );
    } else {
      var overviewData = data.get('overview', null);
      if(!overviewData) return null;
      var filteredOverviewData = filter(overviewData);
      return <OverviewChart title="Overview chart" width={width} data={filteredOverviewData}/>;
    }
  }

  render(){
    var globalState = this.props.state.get('globalState');
    var data = globalState.get('data');
    return (
        <div className="col-sm-12 content">
          {this.getOverviewChart()}
          <TendersTable data={data.get('topTenders')}/>
          <AwardsTable data={data.get('topAwards')}/>
        </div>
    )
  }
}