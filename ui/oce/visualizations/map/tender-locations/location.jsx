import ReactDOM from 'react-dom';
import { Popup } from 'react-leaflet';
import cn from 'classnames';
import Marker from '../location/marker';
import Component from '../../../pure-render-component';
import translatable from '../../../translatable';
import OverviewChart from '../../../visualizations/charts/overview';
import CostEffectiveness from '../../../visualizations/charts/cost-effectiveness';
import { cacheFn, download } from '../../../tools';
import ProcurementMethodChart from '../../../visualizations/charts/procurement-method';
// eslint-disable-next-line no-unused-vars
import styles from './style.less';

const addTenderDeliveryLocationId = cacheFn(
  (filters, id) => filters.set('tenderLoc', id),
);

class Tab extends translatable(Component) {}

export class ChartTab extends Tab {
  constructor(props) {
    super(props);
    this.state = {
      chartData: null,
    };
  }

  static getMargins() {
    return {
      t: 0,
      l: 50,
      r: 10,
      b: 50,
    };
  }

  static getChartClass() { return ''; }

  render() {
    const { filters, styling, years, translations, data, monthly, months } = this.props;
    const decoratedFilters = addTenderDeliveryLocationId(filters, data._id);
    return (
      <div className={cn('map-chart', this.constructor.getChartClass())}>
        <this.constructor.Chart
          filters={decoratedFilters}
          styling={styling}
          years={years}
          monthly={monthly}
          months={months}
          translations={translations}
          data={this.state.chartData}
          requestNewData={(_, chartData) => this.setState({ chartData })}
          width={380}
          height={250}
          margin={this.constructor.getMargins()}
          legend="h"
        />
      </div>
    );
  }
}

class LocationWrapper extends translatable(Component) {
  constructor(props) {
    super(props);
    this.state = {
      currentTab: 0,
    };
  }

  doExcelExport() {
    const { currentTab } = this.state;
    const { data, filters, years, months } = this.props;
    const CurrentTab = this.constructor.TABS[currentTab];
    download({
      ep: CurrentTab.Chart.excelEP,
      filters: addTenderDeliveryLocationId(filters, data._id),
      years,
      months,
      t: translationKey => this.t(translationKey),
    });
  }

  render() {
    const { currentTab } = this.state;
    const { data, translations, filters, years, styling, monthly, months } = this.props;
    const CurrentTab = this.constructor.TABS[currentTab];
    const t = translationKey => this.t(translationKey);
    return (
      <Marker {...this.props}>
        <Popup className="tender-locations-popup">
          <div>
            <header>
              {CurrentTab.prototype instanceof ChartTab &&
                <span className="chart-tools">
                  <a tabIndex={-1} role="button" onClick={() => this.doExcelExport()}>
                    <img
                      src="assets/icons/export-very-black.svg"
                      alt="Export"
                      width="16"
                      height="16"
                    />
                  </a>
                  <a
                    tabIndex={-1}
                    role="button"
                    onClick={() => ReactDOM.findDOMNode(this.currentChart).querySelector('.modebar-btn:first-child').click()}
                  >
                    <img
                      src="assets/icons/camera.svg"
                      alt="Screenshot"
                    />
                  </a>
                </span>
              }
              {data.name}
            </header>
            <div className="row">
              <div className="tabs-bar col-xs-4">
                {this.constructor.TABS.map((tab, index) => (
                  <div
                    key={tab.getName(t)}
                    className={cn({ active: index === currentTab })}
                    onClick={() => this.setState({ currentTab: index })}
                    role="button"
                    tabIndex={0}
                  >
                    <a href="#">{tab.getName(t)}</a>
                  </div>
                ))}
              </div>
              <div className="col-xs-8">
                <CurrentTab
                  data={data}
                  translations={translations}
                  filters={filters}
                  years={years}
                  monthly={monthly}
                  months={months}
                  styling={styling}
                  ref={(c) => { this.currentChart = c; }}
                />
              </div>
            </div>
          </div>
        </Popup>
      </Marker>
    );
  }
}

export class OverviewTab extends Tab {
  static getName(t) { return t('maps:tenderLocations:tabs:overview:title'); }

  render() {
    const { data } = this.props;
    const { count, amount } = data;
    return (<div>
      <p>
        <strong>{this.t('maps:tenderLocations:tabs:overview:nrOfTenders')}</strong> {count}
      </p>
      <p>
        <strong>{this.t('maps:tenderLocations:tabs:overview:totalFundingByLocation')}</strong> {amount.toLocaleString()}
      </p>
    </div>);
  }
}

export class OverviewChartTab extends ChartTab {
  static getName(t) { return t('charts:overview:title'); }

  static getChartClass() { return 'overview'; }
}

const capitalizeAxisTitles = Class => class extends Class {
  getLayout() {
    const layout = super.getLayout();
    layout.xaxis.title = layout.xaxis.title.toUpperCase();
    layout.yaxis.title = layout.yaxis.title.toUpperCase();
    return layout;
  }
};

OverviewChartTab.Chart = capitalizeAxisTitles(OverviewChart);

export class CostEffectivenessTab extends ChartTab {
  static getName(t) { return t('charts:costEffectiveness:title'); }
}

CostEffectivenessTab.Chart = capitalizeAxisTitles(CostEffectiveness);

export class ProcurementMethodTab extends ChartTab {
  static getName(t) { return t('charts:procurementMethod:title'); }
}

ProcurementMethodTab.Chart = capitalizeAxisTitles(ProcurementMethodChart);

LocationWrapper.TABS = [OverviewTab, OverviewChartTab, CostEffectivenessTab, ProcurementMethodTab];

export default LocationWrapper;
