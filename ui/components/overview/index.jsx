import Component from "../pure-render-component";
import OverviewChart from "./overview-chart";

export default class Overview extends Component{
  render(){
    var globalState = this.props.state.get('globalState');
    var data = globalState.get('data');
    var width = globalState.get('contentWidth');
    return (
        <div className="col-sm-12 content">
          <OverviewChart width={width} data={data.get('overview', null)}/>
        </div>
    )
  }
}