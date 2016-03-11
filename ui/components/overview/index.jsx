import Component from "../pure-render-component";
import OverviewChart from "./overview-chart";
import TendersTable from "./tenders-table";
import AwardsTable from "./awards-table";

export default class Overview extends Component{
  render(){
    var globalState = this.props.state.get('globalState');
    var data = globalState.get('data');
    var width = globalState.get('contentWidth');
    var years = globalState.getIn(['filters', 'years']);
    var overviewData = data.get('overview', null);
    if(overviewData){
      var filteredOverviewData = {};
      Object.keys(overviewData).forEach(key =>
          filteredOverviewData[key] = overviewData[key].filter(({_id}) => years.get(_id, false))
      );
    }
    return (
        <div className="col-sm-12 content">
          <OverviewChart width={width} data={filteredOverviewData}/>
          <div className="col-sm-6">
            <TendersTable data={data.get('topTenders')}/>
          </div>
          <div className="col-sm-6">
            <AwardsTable data={data.get('topAwards')}/>
          </div>
        </div>
    )
  }
}