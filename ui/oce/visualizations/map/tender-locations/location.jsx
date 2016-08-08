import Marker from "../location/marker";
import Component from "../../../pure-render-component";
import {Popup} from "react-leaflet";
import translatable from "../../../translatable";
import cn from "classnames";
import OverviewChart from "../../../../oce/visualizations/charts/overview";

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

class OverviewChartTab extends Tab{
  static getName(__){
    return __('Overview chart');
  }

  render(){
    let {filters, styling, years} = this.props;
    return <div>
      <OverviewChart
          filters={filters}
          styling={styling}
          years={years}
      />
    </div>
  }
}

class CostEffectiveness extends Tab{
  static getName(__){
    return __('Cost effectiveness');
  }
}

class ProcurementMethod extends Tab{
  static getName(__){
    return __('Procurement method');
  }
}

LocationWrapper.TABS = [Overview, OverviewChartTab, CostEffectiveness, ProcurementMethod];

export default LocationWrapper;