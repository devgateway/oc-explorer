import Marker from "../location/marker";
import Component from "../../../pure-render-component";
import {Popup} from "react-leaflet";
import translatable from "../../../translatable";
import cn from "classnames";
import OverviewChart from "../../../visualizations/charts/overview";
import CostEffectiveness from "../../../visualizations/charts/cost-effectiveness";
import {cacheFn, download} from "../../../tools"
import ProcurementMethodChart from '../../../visualizations/charts/procurement-method';
import ReactDOM from "react-dom";
import style from "./style.less";

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

export class OverviewTab extends Tab{
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
    let decoratedFilters = addTenderDeliveryLocationId(filters, data._id);
    let doExcelExport = e => download({
      ep: this.constructor.Chart.excelEP,
      filters: decoratedFilters,
      years,
      __: this.__.bind(this)
    });
    return <div className="map-chart">
      <this.constructor.Chart
          filters={decoratedFilters}
          styling={styling}
          years={years}
          translations={translations}
          data={this.state.chartData}
          requestNewData={(_, chartData) => this.setState({chartData})}
          width={500}
          height={350}
          margin={{
            t: 0,
            l: 50,
            r: 50,
            b: 50
          }}
      />
      <div className="chart-toolbar">
        <div className="btn btn-default" onClick={doExcelExport}>
          <img
              src="assets/icons/export-black.svg"
              width="16"
              height="16"
          />
        </div>

        <div className="btn btn-default" onClick={e => ReactDOM.findDOMNode(this).querySelector(".modebar-btn:first-child").click()}>
          <img src="assets/icons/camera.svg"/>
        </div>
      </div>
    </div>
  }
}

export class OverviewChartTab extends ChartTab{
  static getName(__){
    return __('Overview chart');
  }
}

OverviewChartTab.Chart = OverviewChart;

export class CostEffectivenessTab extends ChartTab{
  static getName(__){
    return __('Cost effectiveness');
  }
}

CostEffectivenessTab.Chart = CostEffectiveness;

class ProcurementMethodTab extends ChartTab{
  static getName(__){
    return __('Procurement method');
  }
}

ProcurementMethodTab.Chart = ProcurementMethodChart;

LocationWrapper.TABS = [OverviewTab, OverviewChartTab, CostEffectivenessTab, ProcurementMethodTab];

export default LocationWrapper;