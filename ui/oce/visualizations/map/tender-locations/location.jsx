import Marker from "../location/marker";
import Component from "../../../pure-render-component";
import {Popup} from "react-leaflet";
import translatable from "../../../translatable";
import cn from "classnames";
import OverviewChart from "../../../../oce/visualizations/charts/overview";
import CostEffectiveness from "../../../../oce/visualizations/charts/cost-effectiveness";
import {cacheFn} from "../../../tools"

class LocationWrapper extends translatable(Component){
  constructor(props){
    super(props);
    this.state = {
      currentTab: 0
    }
  }

  render(){
    let {currentTab} = this.state;
    let {data, translations, filters, years, styling} = this.props;
    let CurrentTab = this.constructor.TABS[currentTab];
    return (
        <Marker {...this.props}>
          <Popup>
            <div>
              <ul className="nav nav-tabs">
                {this.constructor.TABS.map((Tab, index) =>
                  <li key={index}
                      className={cn({active: index == currentTab})}
                      onClick={e => this.setState({currentTab: index})}
                  >
                    <a href="javascript:void(0);">{Tab.getName(this.__.bind(this))}</a>
                  </li>
                )}
              </ul>
              <CurrentTab
                  data={data}
                  translations={translations}
                  filters={filters}
                  years={years}
                  styling={styling}
              />
            </div>
          </Popup>
        </Marker>
    )
  }
}

class Tab extends translatable(Component){}

class Overview extends Tab{
  static getName(__){
    return __('Overview');
  }

  render(){
    let {data} = this.props;
    let {name, count, amount} = data;
    return <div>
      <h3>{name}</h3>
      <p>
        <strong>{this.__('Number of Tenders:')}</strong> {count}
      </p>
      <p>
        <strong>{this.__('Total Funding for the location:')}</strong> {amount.toLocaleString()}
      </p>
    </div>
  }
}

let addTenderDeliveryLocationId = cacheFn(
    (filters, id) => filters.set('tenderLoc', id)
);

export class ChartTab extends Tab{
  constructor(props){
    super(props);
    this.state = {
      chartData: null
    }
  }

  render(){
    let {filters, styling, years, translations, data} = this.props;
    return <div>
      <this.constructor.Chart
          filters={addTenderDeliveryLocationId(filters, data._id)}
          styling={styling}
          years={years}
          translations={translations}
          data={this.state.chartData}
          requestNewData={(_, chartData) => this.setState({chartData})}
          width={450}
          height={350}
          margin={{
            t: 0,
            l: 50,
            r: 50,
            b: 50
          }}
      />
    </div>
  }
}

class OverviewChartTab extends ChartTab{
  static getName(__){
    return __('Overview chart');
  }
}

OverviewChartTab.Chart = OverviewChart;

class CostEffectivenessTab extends ChartTab{
  static getName(__){
    return __('Cost effectiveness');
  }
}

CostEffectivenessTab.Chart = CostEffectiveness;

LocationWrapper.TABS = [Overview, OverviewChartTab, CostEffectivenessTab];

export default LocationWrapper;