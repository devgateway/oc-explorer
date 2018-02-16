import { Map } from 'immutable';
import cn from 'classnames';
import ReactDOM from 'react-dom';
// eslint-disable-next-line no-unused-vars
import styles from './style.less';
import OCApp from '../oce';
import OverviewTab from '../oce/tabs/overview';
import LocationTab from '../oce/tabs/location';
import CompetitivenessTab from '../oce/tabs/competitiveness';
import EfficiencyTab from '../oce/tabs/efficiency';
import EProcurementTab from '../oce/tabs/e-procurement';
import { fetchJson } from '../oce/tools';
import ViewSwitcher from '../oce/switcher.jsx';
import CorruptionRickDashboard from '../oce/corruption-risk';

class OCEDemoLocation extends LocationTab {
  getHeight() {
    const TOP_OFFSET = 128;
    const BOTTOM_OFFSET = 66;
    return window.innerHeight - TOP_OFFSET - BOTTOM_OFFSET;
  }
}
OCEDemoLocation.CENTER = [37, -100];

class OCEChild extends OCApp {
  constructor(props) {
    super(props);
    this.registerTab(OverviewTab);
    this.registerTab(OCEDemoLocation);
    this.registerTab(CompetitivenessTab);
    this.registerTab(EfficiencyTab);
    this.registerTab(EProcurementTab);
  }

  fetchBidTypes() {
    fetchJson('/api/ocds/bidType/all').then(data =>
      this.setState({
        bidTypes: data.reduce((map, datum) =>
          map.set(datum.id, datum.description), Map())
      })
    );
  }

  loginBox() {
    let linkUrl;
    let text;
    if (this.state.user.loggedIn) {
      linkUrl = '/preLogout?referrer=/ui/index.html';
      text = this.t('general:logout');
    } else {
      linkUrl = '/login?referrer=/ui/index.html';
      text = this.t('general:login');
    }
    return (
      <a href={linkUrl} className="login-logout">
        <button className="btn btn-default">
          {text}
        </button>
      </a>
    );
  }

  dashboardSwitcher() {
    const { dashboardSwitcherOpen } = this.state;
    const { onSwitch } = this.props;
    return (
      <div className={cn('dash-switcher-wrapper', { open: dashboardSwitcherOpen })}>
        <h1 onClick={this.toggleDashboardSwitcher.bind(this)}>
          <strong>Monitoring & Evaluation</strong> Toolkit
          <i className="glyphicon glyphicon-menu-down" />
        </h1>
        {dashboardSwitcherOpen &&
          <div className="dashboard-switcher">
            <a href="javascript:void(0);" onClick={() => onSwitch('crd')}>
              Corruption Risk Dashboard
            </a>
          </div>
        }
      </div>
    );
  }

  exportBtn() {
    if(this.state.exporting) {
      return (
        <div className="export-progress">
          <div className="progress">
            <div className="progress-bar progress-bar-danger" role="progressbar" style={{ width: "100%" }}>
              {this.t('export:exporting')}
            </div>
          </div>
        </div>
      );
    }
    return (
      <div className="export-btn">
        <button className="btn btn-default" disabled>
          <i className="glyphicon glyphicon-download-alt" />
        </button>
      </div>
    );
  }

  languageSwitcher() {
    const { TRANSLATIONS } = this.constructor;
    const { locale: selectedLocale } = this.state;
    if (Object.keys(TRANSLATIONS).length <= 1) return null;
    return Object.keys(TRANSLATIONS).map(locale => (
      <a
        href="javascript:void(0);"
        onClick={() => this.setLocale(locale)}
        className={cn({ active: locale === selectedLocale })}
      >
        {locale.split('_')[0]}
      </a>
    ));
  }

  navigationLink(Tab, index){
    if (OverviewTab !== Tab) return super.navigationLink(Tab, index);
    const { getName, icon } = Tab;
    return (
      <div
        className={cn('navigation-item-overview', { active: index === this.state.currentTab })}
        onClick={_ => this.setState({currentTab: index})}
      >
        <a href="javascript:void(0);" key={index} className="col-sm-12">
          <span className="circle">
            <img className="nav-icon" src={`assets/icons/${icon}.svg`} />
            <i className={`glyphicon glyphicon-${icon}`} />
          </span>
          &nbsp;
          {getName(this.t.bind(this))}
          <i className="glyphicon glyphicon-info-sign" />
        </a>
        <div className="description col-sm-12">
          The Procurement M&E Prototype is an interactive platform for analyzing, monitoring,
          and evaluating information on public procurement. It is specifically designed to help
          users understand procurement efficiency, and the competitiveness and cost-effectiveness
          of public markets.
        </div>
      </div>
    );
  }

  render(){
    return (
      <div className="container-fluid dashboard-default" onClick={() => this.setState({ menuBox: '' })}>
        <header className="branding row">
          <div className="col-sm-9 logo-wrapper">
            <img src="assets/dg-logo.svg" />
            {this.dashboardSwitcher()}
          </div>
          <div className="col-sm-1 header-icons language-switcher">
            {this.languageSwitcher()}
          </div>
          <div className="col-sm-2">
            {this.loginBox()}
          </div>
        </header>
        <div className="header-tools row">
          <div className="col-xs-offset-4 col-md-offset-3 col-sm-5 menu">
            <div className="filters-hint">
              {this.t('filters:hint')}
            </div>
            {this.filters()}
            {this.comparison()}
          </div>
          <div className="col-xs-3 col-md-4">
            {this.exportBtn()}
          </div>
        </div>
        <aside className="col-xs-4 col-md-3">
          <div className="row">
            <div role="navigation">
              {this.navigation()}
            </div>
            {/*
                <section className="col-sm-12 description">
                <h3><strong>{this.t('general:description:title')}</strong></h3>
                <p>
                <small>
                {this.t('general:description:content')}
                </small>
                </p>
                </section>
              */}
          </div>
        </aside>
        <div className="col-xs-offset-4 col-md-offset-3 col-xs-8 col-md-9">
          <div className="row">
            {this.content()}
          </div>
        </div>
        {this.showMonths() && <div
          className="col-xs-offset-4 col-md-offset-3 col-xs-8 col-md-9 months-bar"
          role="navigation"
        >
          {this.monthsBar()}
        </div>}
        <div
          className="col-xs-offset-4 col-md-offset-3 col-xs-8 col-md-9 years-bar"
          role="navigation"
        >
          {this.yearsBar()}
        </div>
        <footer className="col-sm-12 main-footer">&nbsp;</footer>
      </div>
    );
  }
}

const translations = {
  en_US: require('../../web/public/languages/en_US.json'),
  es_ES: require('../../web/public/languages/es_ES.json'),
  fr_FR: require('../../web/public/languages/fr_FR.json'),
};

const BILLION = 1000000000;
const MILLION = 1000000;
const THOUSAND = 1000;
const formatNumber = number => number.toLocaleString(undefined, {maximumFractionDigits: 2});

const styling = {
  charts: {
    axisLabelColor: '#cc3c3b',
    traceColors: ['#324d6e', '#ecac00', '#4b6f33'],
    hoverFormat: ',.2f',
    hoverFormatter: (number) => {
      if (typeof number === 'undefined') return number;
      let abs = Math.abs(number);
      if (abs >= BILLION) return formatNumber(number / BILLION) + 'B';
      if (abs >= MILLION) return formatNumber(number / MILLION) + 'M';
      if (abs >= THOUSAND) return formatNumber(number / THOUSAND) + 'K';
      return formatNumber(number);
    },
  },
  tables: {
    currencyFormatter: formatNumber,
  },
};

OCEChild.STYLING = styling;
OCEChild.TRANSLATIONS = translations;

CorruptionRickDashboard.STYLING = JSON.parse(JSON.stringify(styling));

CorruptionRickDashboard.STYLING.charts.traceColors = ['#234e6d', '#3f7499', '#80b1d3', '#afd5ee', '#d9effd'];

class OceSwitcher extends ViewSwitcher {}

OceSwitcher.views['m-and-e'] = OCEChild;
OceSwitcher.views.crd = CorruptionRickDashboard;

ReactDOM.render(<OceSwitcher
  translations={translations.en_US}
  styling={styling}
/>, document.getElementById('dg-container'));
