import ReactDOM from "react-dom";
import OCApp from "./oce";
import OverviewTab from './oce/tabs/overview';
import LocationTab from './oce/tabs/location';
import CompetitivenessTab from './oce/tabs/competitiveness';
import EfficiencyTab from './oce/tabs/efficiency';
import EProcurementTab from './oce/tabs/e-procurement';
import {fetchJson} from "./oce/tools";
import {Map} from "immutable";
import styles from "./style.less";
import ViewSwitcher from "./oce/switcher.jsx";
import CorruptionRickDashboard from "./oce/corruption-risk";

class OCEChild extends OCApp{
  constructor(props) {
    super(props);
    this.registerTab(OverviewTab);
    this.registerTab(LocationTab);
    this.registerTab(CompetitivenessTab);
    this.registerTab(EfficiencyTab);
    this.registerTab(EProcurementTab);
  }

  fetchBidTypes(){
    fetchJson('/api/ocds/bidType/all').then(data =>
        this.setState({
          bidTypes: data.reduce((map, datum) =>
              map.set(datum.id, datum.description), Map())
        })
    );
  }

  render(){
    return (
      <div className="container-fluid dashboard-default" onClick={_ => this.setState({menuBox: ""})}>
        <header className="branding row">
          <div className="col-sm-offset-1 col-sm-4">
            {this.dashboardSwitcher()}
          </div>
          <div className="col-sm-6 menu">
            {this.filters()}
            {this.comparison()}
            {this.exportBtn()}
          </div>
          <div className="col-sm-2 header-icons user-tools">
            {this.loginBox()}
          </div>
          <div className="col-sm-1 header-icons language-switcher">
            {this.languageSwitcher()}
          </div>
        </header>
        <aside className="col-xs-4 col-md-3 col-lg-2">
          <div className="row">
            <div role="navigation">
              {this.navigation()}
            </div>
            <section className="col-sm-12 description">
              <h3><strong>{this.t('general:description:title')}</strong></h3>
              <p>
                <small>
                  {this.t('general:description:content')}
                </small>
              </p>
            </section>
          </div>
        </aside>
        <div className="col-xs-offset-4 col-md-offset-3 col-lg-offset-2 col-xs-8 col-md-9 col-lg-10">
          <div className="row">
            {this.content()}
          </div>
        </div>
        {this.showMonths() && <div className="col-xs-offset-4 col-md-offset-3 col-lg-offset-2 col-xs-8 col-md-9 col-lg-10 months-bar" role="navigation">
        {this.monthsBar()}
        </div>}
        <div className="col-xs-offset-4 col-md-offset-3 col-lg-offset-2 col-xs-8 col-md-9 col-lg-10 years-bar" role="navigation">
          {this.yearsBar()}
        </div>
        <footer className="col-sm-12 main-footer">&nbsp;</footer>
      </div>
    );
  }
}

OCEChild.TRANSLATIONS = {
  en_US: require('../web/public/languages/en_US.json')
};

class OceSwitcher extends ViewSwitcher{}

OceSwitcher.views.default = OCEChild;
OceSwitcher.views.corruptionRiskDashboard = CorruptionRickDashboard;

ReactDOM.render(<OceSwitcher/>, document.getElementById('dg-container'));

if("ocvn.developmentgateway.org" == location.hostname){
  (function(b,o,i,l,e,r){b.GoogleAnalyticsObject=l;b[l]||(b[l]=
      function(){(b[l].q=b[l].q||[]).push(arguments)});b[l].l=+new Date;
    e=o.createElement(i);r=o.getElementsByTagName(i)[0];
    e.src='//www.google-analytics.com/analytics.js';
    r.parentNode.insertBefore(e,r)}(window,document,'script','ga'));
  ga('create','UA-78202947-1');ga('send','pageview');
}
