import Marker from "../location/marker";
import Component from "../../../pure-render-component";
import {Popup} from "react-leaflet";
import translatable from "../../../translatable";
import cn from "classnames";

class LocationWrapper extends translatable(Component){
  constructor(props){
    super(props);
    this.state = {
      currentTab: 0
    }
  }

  render(){
    let {currentTab} = this.state;
    let {data, translations} = this.props;
    let CurrentTab = this.constructor.TABS[currentTab];
    return (
        <Marker {...this.props}>
          <Popup>
            <div>
              <ul className="nav nav-tabs">
                {this.constructor.TABS.map((Tab, index) =>
                  <li className={cn({active: index == currentTab})} onClick={e => this.setState({currentTab: index})}>
                    <a href="javascript:void(0);">{Tab.getName(this.__.bind(this))}</a>
                  </li>
                )}
              </ul>
              <CurrentTab
                  data={data}
                  translations={translations}
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

class OverviewChart extends Tab{
  static getName(__){
    return __('Overview chart');
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

LocationWrapper.TABS = [Overview, OverviewChart, CostEffectiveness, ProcurementMethod];

export default LocationWrapper;